package com.paca.paca.sale.service;

import java.util.Optional;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.paca.paca.sale.model.Sale;
import com.paca.paca.product.model.Product;
import com.paca.paca.sale.model.SaleProduct;
import com.paca.paca.sale.dto.SaleProductDTO;
import com.paca.paca.sale.statics.SaleStatics;
import com.paca.paca.sale.utils.SaleProductMapper;
import com.paca.paca.sale.repository.SaleRepository;
import com.paca.paca.product.repository.ProductRepository;
import com.paca.paca.sale.repository.SaleProductRepository;
import com.paca.paca.exception.exceptions.ForbiddenException;
import com.paca.paca.exception.exceptions.BadRequestException;

@Service
@RequiredArgsConstructor
public class SaleProductService {

    private final SaleProductRepository saleProductRepository;
    private final SaleProductMapper saleProductMapper;

    private final SaleRepository saleRepository;

    private final ProductRepository productRepository;

    public SaleProductDTO save(SaleProductDTO saleProductDTO) throws BadRequestException {
        Optional<Sale> sale = saleRepository.findById(saleProductDTO.getSaleId());
        if (!sale.isPresent()) {
            throw new BadRequestException("Sale with id " + saleProductDTO.getSaleId() + " does not exist", 42);
        }

        if (sale.get().getStatus() != SaleStatics.Status.ONGOING) {
            throw new BadRequestException("Sale with id " + saleProductDTO.getSaleId() + " is not ongoing", 47);
        }

        Optional<Product> product = productRepository.findById(saleProductDTO.getProductId());
        if (!product.isPresent()) {
            throw new BadRequestException("Product with id " + saleProductDTO.getProductId() + " does not exist", 25);
        }

        // Map the DTO to a sale product
        SaleProduct saleProduct = saleProductMapper.toEntity(saleProductDTO, sale.get(), product.get());
        saleProduct.setName(product.get().getName());
        saleProduct.setPrice(product.get().getPrice());

        // Save the sale product
        saleProduct = saleProductRepository.save(saleProduct);

        // Create the response DTO
        SaleProductDTO response = saleProductMapper.toDTO(saleProduct);
        return response;
    }

    public void delete(long id) throws BadRequestException, ForbiddenException {
        Optional<SaleProduct> saleProduct = saleProductRepository.findById(id);
        if (!saleProduct.isPresent()) {
            throw new BadRequestException("Sale product with id " + id + " does not exist", 42);
        }

        // Check the status of the sale
        // If the sale is not ongoing, the sale product cannot be deleted
        Sale sale = saleProduct.get().getSale();
        if (sale.getStatus() != SaleStatics.Status.ONGOING) {
            throw new ForbiddenException("Sale with id " + sale.getId() + " is not ongoing", 47);
        }

        saleProductRepository.deleteById(id);
    }

    public SaleProductDTO update(long id, SaleProductDTO saleProductDTO)
            throws BadRequestException, ForbiddenException {
        Optional<SaleProduct> saleProduct = saleProductRepository.findById(id);
        if (!saleProduct.isPresent()) {
            throw new BadRequestException("Sale product with id " + id + " does not exist", 42);
        }

        // Check the status of the sale
        // If the sale is not ongoing, the sale product cannot be updated
        Sale sale = saleProduct.get().getSale();
        if (sale.getStatus() != SaleStatics.Status.ONGOING) {
            throw new ForbiddenException("Sale with id " + sale.getId() + " is not ongoing", 47);
        }

        saleProductMapper.updateModel(saleProductDTO, saleProduct.get());
        saleProductRepository.save(saleProduct.get());

        return saleProductMapper.toDTO(saleProduct.get());
    }

}
