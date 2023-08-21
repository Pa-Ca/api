package com.paca.paca.business;

import com.paca.paca.ServiceTest;
import com.paca.paca.user.model.User;
import com.paca.paca.utils.TestUtils;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.business.model.Tier;
import com.paca.paca.statics.BusinessTier;
import com.paca.paca.business.model.Business;
import com.paca.paca.business.dto.BusinessDTO;
import com.paca.paca.branch.dto.BranchInfoListDTO;
import com.paca.paca.business.service.BusinessService;
import com.paca.paca.exception.exceptions.ConflictException;
import com.paca.paca.exception.exceptions.NoContentException;

import junit.framework.TestCase;

import org.junit.Assert;
import org.mockito.Mockito;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class BusinessServiceTest extends ServiceTest {

    @InjectMocks
    private BusinessService businessService;

    @Test
    void shouldGetNoContentDueToMissingBusinessInGetBusinessById() {
        when(businessRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            businessService.getById(1L);
            TestCase.fail();
        } catch (Exception e){
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Business with id 1 does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 28);
        }
    }

    @Test
    void shouldGetBusinessById() {
        Business business = utils.createBusiness(null);
        BusinessDTO dto = utils.createBusinessDTO(business);

        when(businessRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(business));
        when(businessMapper.toDTO(any(Business.class))).thenReturn(dto);

        BusinessDTO dtoResponse = businessService.getById(business.getId());

        assertThat(dtoResponse).isEqualTo(dto);
    }

    @Test
    void shouldGetNoContentDueToMissingUserInSave() {
        BusinessDTO dto = utils.createBusinessDTO(null);

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());

        try {
            businessService.save(dto);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "User with email " + dto.getEmail() + " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 30);
        }
    }

    @Test
    void shouldGetBusinessDueToExistingBusinessInSave() {
        Business business = utils.createBusiness(null);
        BusinessDTO dto = utils.createBusinessDTO(business);

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.ofNullable(business.getUser()));
        when(businessRepository.existsByUserEmail(any(String.class))).thenReturn(true);

        try {
            businessService.save(dto);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof ConflictException);
            Assert.assertEquals(e.getMessage(), "Business with email " + dto.getEmail() + " already exists");
            Assert.assertEquals(((ConflictException) e).getCode(), (Integer) 12);
        }
    }

    @Test
    void shouldSave() {
        Business business = utils.createBusiness(null);
        Tier tier = utils.createTier(BusinessTier.basic);
        BusinessDTO dto = utils.createBusinessDTO(business);

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.ofNullable(business.getUser()));
        when(businessRepository.existsByUserEmail(any(String.class))).thenReturn(false);
        when(businessRepository.save(any(Business.class))).thenReturn(business);
        when(tierRepository.findByName(any(BusinessTier.class))).thenReturn(Optional.ofNullable(tier));

        when(businessMapper.toEntity(any(BusinessDTO.class), any(Tier.class), any(User.class))).thenReturn(business);
        when(businessMapper.toDTO(any(Business.class))).thenReturn(dto);

        BusinessDTO dtoResponse = businessService.save(dto);

        assertThat(dtoResponse).isEqualTo(dto);
    }

    @Test
    void shouldGetNoContentDueToMissingBusinessInUpdate() {
        Business business = utils.createBusiness(null);
        BusinessDTO dto = utils.createBusinessDTO(business);

        when(businessRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            businessService.update(business.getId(), dto);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Business with id " + business.getId() + " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 28);
        }
    }

    @Test
    void shouldUpdate() {
        Business business = utils.createBusiness(null);
        Tier tier = utils.createTier(BusinessTier.basic);
        BusinessDTO dto = utils.createBusinessDTO(business);

        when(businessRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(business));
        when(businessRepository.save(any(Business.class))).thenReturn(business);
        when(businessMapper.updateModel(any(BusinessDTO.class), any(Business.class), any(Tier.class)))
                .thenReturn(business);
        when(businessMapper.toDTO(any(Business.class))).thenReturn(dto);
        when(tierRepository.findByName(any(BusinessTier.class))).thenReturn(Optional.ofNullable(tier));

        BusinessDTO dtoResponse = businessService.update(business.getId(), dto);

        assertThat(dtoResponse).isEqualTo(dto);
    }

    @Test
    void shouldGetNoContentDueToMissingBusinessInDelete() {
        Business business = utils.createBusiness(null);

        when(businessRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            businessService.delete(business.getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Business with id " + business.getId() + " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 28);
        }
    }

    @Test
    void shouldGetNoContentDueToMissingBusinessInGetBusinessByUserId() {
        Business business = utils.createBusiness(null);

        when(businessRepository.findByUserId(any(Long.class))).thenReturn(Optional.empty());

        try {
            businessService.getByUserId(business.getUser().getId());
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "User with id " + business.getUser().getId() + " does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 12);
        }
    }

    @Test
    void shouldGetBusinessByUserId() {
        Business business = utils.createBusiness(null);
        BusinessDTO dto = utils.createBusinessDTO(business);

        when(businessRepository.findByUserId(any(Long.class))).thenReturn(Optional.ofNullable(business));
        when(businessMapper.toDTO(any(Business.class))).thenReturn(dto);

        BusinessDTO dtoResponse = businessService.getByUserId(business.getUser().getId());

        assertThat(dtoResponse).isEqualTo(dto);
    }

    @Test
    void shouldGetAllBranchesById() {
        Business business = utils.createBusiness(null);
        List<Branch> branches = TestUtils.castList(Branch.class, Mockito.mock(List.class));

        when(branchRepository.findAllByBusinessId(anyLong())).thenReturn(branches);
        BranchInfoListDTO responseDTO = businessService.getAllBranchesById(business.getId());

        assertThat(responseDTO).isNotNull();
    }
}