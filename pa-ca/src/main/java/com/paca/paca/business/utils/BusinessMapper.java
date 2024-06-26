package com.paca.paca.business.utils;

import org.mapstruct.*;

import com.paca.paca.user.model.User;
import com.paca.paca.business.model.Tier;
import com.paca.paca.business.model.Business;
import com.paca.paca.business.dto.BusinessDTO;

@Mapper(componentModel = "spring")
public interface BusinessMapper {
    @Mapping(source = "tier.name", target = "tier")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.email", target = "email")
    BusinessDTO toDTO(Business business);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "tier", ignore = true)
    Business toEntity(BusinessDTO dto);

    default Business toEntity(BusinessDTO dto, Tier tier, User user) {
        Business business = toEntity(dto);
        business.setUser(user);
        business.setTier(tier);
        return business;
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(source = "tier", target = "tier")
    @Mapping(source = "dto.name", target = "name")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Business updateModel(BusinessDTO dto, @MappingTarget Business business, Tier tier);
}