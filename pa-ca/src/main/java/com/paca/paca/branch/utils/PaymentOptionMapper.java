package com.paca.paca.branch.utils;

import com.paca.paca.branch.model.Branch;
import com.paca.paca.branch.model.PaymentOption;
import com.paca.paca.branch.dto.PaymentOptionDTO;

import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PaymentOptionMapper {

    @Mapping(source = "branch.id", target = "branchId")
    PaymentOptionDTO toDTO(PaymentOption paymentOption);

    @Mapping(target = "branch", ignore = true)
    PaymentOption toEntity(PaymentOptionDTO dto);

    default PaymentOption toEntity(PaymentOptionDTO dto, Branch branch) {
        PaymentOption paymentOption = toEntity(dto);
        paymentOption.setBranch(branch);
        return paymentOption;
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "branch", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    PaymentOption updateModel(PaymentOptionDTO dto, @MappingTarget PaymentOption paymentOption);
}