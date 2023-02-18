package com.paca.paca.business.utils;

import com.paca.paca.business.model.Business;
import com.paca.paca.business.dto.BusinessDTO;
import com.paca.paca.business.tier.Tier;
import com.paca.paca.statics.BusinessTier;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BusinessMapper {
    @Mapping(source = "tier.name", target = "tier")
    @Mapping(source = "user.id", target = "userId")
    BusinessDTO toDTO(Business business);

//    @Mapping(source = "tier", target = "tier")
//    @Mapping(target = "verified", defaultExpression = "java(false)")
//    @Mapping(target = "loggedIn", defaultExpression = "java(false)")
//    Business toEntity(BusinessDTO dto, BusinessTier role);
//
//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    @Mapping(source = "role", target = "role")
//    @Mapping(target = "email", ignore = true)
//    @Mapping(target = "verified", ignore = true)
//    @Mapping(target = "loggedIn", ignore = true)
//    Business updateEntity(BusinessDTO dto, @MappingTarget Business user, BusinessTier role);

    default Tier stringToTier(BusinessTier tier) {
        return new Tier(tier);
    }
}