package com.paca.paca.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import com.paca.paca.user.utils.ValidateUserInterceptor;
import com.paca.paca.auth.utils.ValidateRolesInterceptor;
import com.paca.paca.client.utils.ValidateClientInterceptor;
import com.paca.paca.business.utils.ValidateBusinessInterceptor;
import com.paca.paca.client.utils.ValidateReviewOwnerInterceptor;
import com.paca.paca.branch.utils.ValidateBranchOwnerInterceptor;
import com.paca.paca.branch.utils.ValidateDefaultTaxOwnerInterceptor;
import com.paca.paca.branch.utils.ValidateTableOwnerInterceptor;
import com.paca.paca.branch.utils.ValidatePaymentOptionOwnerInterceptor;
import com.paca.paca.product.utils.ValidateProductOwnerInterceptor;
import com.paca.paca.promotion.utils.ValidatePromotionOwnerInterceptor;
import com.paca.paca.reservation.utils.ValidateReservationOwnerInterceptor;
import com.paca.paca.sale.utils.ValidateSaleOwnerInterceptor;
import com.paca.paca.sale.utils.ValidateSaleProductOwnerInterceptor;
import com.paca.paca.sale.utils.ValidateTaxOwnerInterceptor;
import com.paca.paca.productSubCategory.utils.ValidateProductSubCategoryOwnerInterceptor;

@Configuration
public class SpringMVCConfig implements WebMvcConfigurer {

    @Autowired
    ValidateUserInterceptor validateUserInterceptor;

    @Autowired
    ValidateRolesInterceptor validateRolesInterceptor;

    @Autowired
    ValidateClientInterceptor validateClientInterceptor;

    @Autowired
    ValidateBusinessInterceptor validateBusinessInterceptor;

    @Autowired
    ValidateBranchOwnerInterceptor validateBranchOwnerInterceptor;

    @Autowired
    ValidateReviewOwnerInterceptor validateReviewOwnerInterceptor;

    @Autowired
    ValidateProductOwnerInterceptor validateProductOwnerInterceptor;

    @Autowired
    ValidatePromotionOwnerInterceptor validatePromotionOwnerInterceptor;

    @Autowired
    ValidateReservationOwnerInterceptor validateReservationOwnerInterceptor;

    @Autowired
    ValidateProductSubCategoryOwnerInterceptor validateProductSubCategoryOwnerInterceptor;

    @Autowired
    ValidateSaleOwnerInterceptor validateSaleOwnerInterceptor;

    @Autowired
    ValidateSaleProductOwnerInterceptor validateSaleProductOwnerInterceptor;

    @Autowired
    ValidateTaxOwnerInterceptor validateTaxOwnerInterceptor;

    @Autowired
    ValidateTableOwnerInterceptor validateTableOwnerInterceptor;

    @Autowired
    ValidateDefaultTaxOwnerInterceptor validateDefaultTaxOwnerInterceptor;

    @Autowired
    ValidatePaymentOptionOwnerInterceptor validatePaymentOptionOwnerInterceptor;

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(validateUserInterceptor);
        registry.addInterceptor(validateRolesInterceptor);
        registry.addInterceptor(validateBusinessInterceptor);
        registry.addInterceptor(validateClientInterceptor);
        registry.addInterceptor(validateBranchOwnerInterceptor);
        registry.addInterceptor(validateReviewOwnerInterceptor);
        registry.addInterceptor(validateProductOwnerInterceptor);
        registry.addInterceptor(validatePromotionOwnerInterceptor);
        registry.addInterceptor(validateReservationOwnerInterceptor);
        registry.addInterceptor(validateProductSubCategoryOwnerInterceptor);
        registry.addInterceptor(validateSaleOwnerInterceptor);
        registry.addInterceptor(validateSaleProductOwnerInterceptor);
        registry.addInterceptor(validateTaxOwnerInterceptor);
        registry.addInterceptor(validateTableOwnerInterceptor);
        registry.addInterceptor(validateDefaultTaxOwnerInterceptor);
        registry.addInterceptor(validatePaymentOptionOwnerInterceptor);
    }
}