package com.paca.paca.sale.service;


import java.util.List;
import java.util.Optional;
import java.util.ArrayList;


import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;


import com.paca.paca.sale.model.Sale;
import com.paca.paca.sale.model.SaleProduct;
import com.paca.paca.sale.repository.SaleProductRepository;
import com.paca.paca.sale.repository.SaleRepository;

import com.paca.paca.sale.dto.SaleProductDTO;
import com.paca.paca.sale.dto.SaleProductListDTO;
import com.paca.paca.sale.statics.SaleStatics;


import com.paca.paca.sale.utils.SaleProductMapper;

import com.paca.paca.exception.exceptions.BadRequestException;
import com.paca.paca.exception.exceptions.ForbiddenException;


import com.paca.paca.product.model.Product;
import com.paca.paca.product.repository.ProductRepository;



@Service
@RequiredArgsConstructor
public class SaleProductService {

    private final SaleProductRepository saleProductRepository;
    private final SaleProductMapper saleProductMapper;
    
    private final SaleRepository saleRepository;

    private final ProductRepository productRepository;


    public SaleProductListDTO getSaleProducts(long saleId)
            throws BadRequestException{

        // Check if the sale exists
        Optional<Sale> sale = saleRepository.findById(saleId);
        if (!sale.isPresent()) {
            throw new BadRequestException("Sale with id " + saleId + " does not exist", 42);
        }

        // Get the products
        List<SaleProduct> saleProducts = saleProductRepository.findAllBySaleId(saleId);

        // Map the products to DTOs
        List<SaleProductDTO> response = new ArrayList<>();
        for (SaleProduct saleProduct : saleProducts) {
            
            SaleProductDTO dto = saleProductMapper.toDTO(saleProduct);
            response.add(dto);
        }

        // Return the DTOs
        return new SaleProductListDTO(response);
    }

    // Save a sale product
    public SaleProductDTO save(SaleProductDTO saleProductDTO) throws BadRequestException {

        // Check if the sale exists
        Optional<Sale> sale = saleRepository.findById(saleProductDTO.getSaleId());
        if (!sale.isPresent()) {
            throw new BadRequestException("Sale with id " + saleProductDTO.getSaleId() + " does not exist", 42);
        }
        // Check if the sale is ongoing
        if (sale.get().getStatus() != SaleStatics.Status.ongoing) {
            throw new BadRequestException("Sale with id " + saleProductDTO.getSaleId() + " is not ongoing", 47);
        }

        // Check if the product exists
        Optional<Product> product = productRepository.findById(saleProductDTO.getProductId());

        if (!product.isPresent()) {
            throw new BadRequestException("Product with id " + saleProductDTO.getProductId() + " does not exist", 25);
        }

        // Map the DTO to a sale product
        SaleProduct saleProduct = saleProductMapper.toEntity(saleProductDTO);

        // Save the sale product
        saleProduct = saleProductRepository.save(saleProduct);

        // Create the response DTO
        SaleProductDTO response = saleProductMapper.toDTO(saleProduct);
        return response;
    }

    // Delete a sale product

    public void delete(long saleProductId) throws BadRequestException, ForbiddenException{

        // Check if the sale product exists
        Optional<SaleProduct> saleProduct = saleProductRepository.findById(saleProductId);
        if (!saleProduct.isPresent()) {
            throw new BadRequestException("Sale product with id " + saleProductId + " does not exist", 42);
        }

        // Check the status of the sale 
        // If the sale is not ongoing, the sale product cannot be deleted
        Sale sale = saleProduct.get().getSale();
        if (sale.getStatus() != SaleStatics.Status.ongoing) {
            throw new ForbiddenException("Sale with id " + sale.getId() + " is not ongoing", 47);
        }
        

        // Delete the sale product
        saleProductRepository.deleteById(saleProductId);
    }

    // Update a sale product
    public SaleProductDTO update(SaleProductDTO saleProductDTO) throws BadRequestException, ForbiddenException {

        // Check if the sale product exists
        Optional<SaleProduct> saleProduct = saleProductRepository.findById(saleProductDTO.getId());
        if (!saleProduct.isPresent()) {
            throw new BadRequestException("Sale product with id " + saleProductDTO.getId() + " does not exist", 42);
        }

        // Check the status of the sale 
        // If the sale is not ongoing, the sale product cannot be updated
        Sale sale = saleProduct.get().getSale();
        if (sale.getStatus() != SaleStatics.Status.ongoing) {
            throw new ForbiddenException("Sale with id " + sale.getId() + " is not ongoing", 47);
        }

        // Map the DTO to a sale product
        saleProductMapper.updateModel(saleProductDTO, saleProduct.get());

        // Save the sale product
        saleProductRepository.save(saleProduct.get());

        // Create the response DTO
        SaleProductDTO response = saleProductMapper.toDTO(saleProduct.get());
        return response;
    }

    
}
