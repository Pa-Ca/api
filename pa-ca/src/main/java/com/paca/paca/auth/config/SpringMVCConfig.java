package com.paca.paca.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import com.paca.paca.auth.utils.ValidateRolesInterceptor;
import com.paca.paca.sale.utils.ValidateTaxOwnerInterceptor;
import com.paca.paca.client.utils.ValidateClientInterceptor;
import com.paca.paca.sale.utils.ValidateSaleOwnerInterceptor;
import com.paca.paca.business.utils.ValidateBusinessInterceptor;
import com.paca.paca.branch.utils.ValidateTableOwnerInterceptor;
import com.paca.paca.client.utils.ValidateReviewOwnerInterceptor;
import com.paca.paca.branch.utils.ValidateBranchOwnerInterceptor;
import com.paca.paca.product.utils.ValidateProductOwnerInterceptor;
import com.paca.paca.sale.utils.ValidateSaleProductOwnerInterceptor;
import com.paca.paca.branch.utils.ValidateDefaultTaxOwnerInterceptor;
import com.paca.paca.promotion.utils.ValidatePromotionOwnerInterceptor;
import com.paca.paca.branch.utils.ValidatePaymentOptionOwnerInterceptor;
import com.paca.paca.reservation.utils.ValidateReservationOwnerInterceptor;
import com.paca.paca.productSubCategory.utils.ValidateProductSubCategoryOwnerInterceptor;

@Configuration
public class SpringMVCConfig implements WebMvcConfigurer {

    @Autowired
    ValidateRolesInterceptor validateRolesInterceptor;

    @Autowired
    ValidateClientInterceptor validateClientInterceptor;

    @Autowired
    ValidateBusinessInterceptor validateBusinessInterceptor;

    @Autowired
    ValidateTaxOwnerInterceptor validateTaxOwnerInterceptor;

    @Autowired
    ValidateTableOwnerInterceptor validateTableOwnerInterceptor;

    @Autowired
    ValidateSaleOwnerInterceptor validateSaleOwnerInterceptor;

    @Autowired
    ValidateBranchOwnerInterceptor validateBranchOwnerInterceptor;

    @Autowired
    ValidateReviewOwnerInterceptor validateReviewOwnerInterceptor;

    @Autowired
    ValidateProductOwnerInterceptor validateProductOwnerInterceptor;

    @Autowired
    ValidatePromotionOwnerInterceptor validatePromotionOwnerInterceptor;

    @Autowired
    ValidateDefaultTaxOwnerInterceptor validateDefaultTaxOwnerInterceptor;

    @Autowired
    ValidateReservationOwnerInterceptor validateReservationOwnerInterceptor;

    @Autowired
    ValidateSaleProductOwnerInterceptor validateSaleProductOwnerInterceptor;

    @Autowired
    ValidatePaymentOptionOwnerInterceptor validatePaymentOptionOwnerInterceptor;

    @Autowired
    ValidateProductSubCategoryOwnerInterceptor validateProductSubCategoryOwnerInterceptor;

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(validateRolesInterceptor);
        registry.addInterceptor(validateClientInterceptor);
        registry.addInterceptor(validateTaxOwnerInterceptor);
        registry.addInterceptor(validateBusinessInterceptor);
        registry.addInterceptor(validateSaleOwnerInterceptor);
        registry.addInterceptor(validateTableOwnerInterceptor);
        registry.addInterceptor(validateBranchOwnerInterceptor);
        registry.addInterceptor(validateReviewOwnerInterceptor);
        registry.addInterceptor(validateProductOwnerInterceptor);
        registry.addInterceptor(validatePromotionOwnerInterceptor);
        registry.addInterceptor(validateDefaultTaxOwnerInterceptor);
        registry.addInterceptor(validateReservationOwnerInterceptor);
        registry.addInterceptor(validateSaleProductOwnerInterceptor);
        registry.addInterceptor(validatePaymentOptionOwnerInterceptor);
        registry.addInterceptor(validateProductSubCategoryOwnerInterceptor);
    }
}