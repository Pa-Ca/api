package com.paca.paca.branch;

import org.junit.Assert;
import org.mockito.Mockito;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import junit.framework.TestCase;

import com.paca.paca.ServiceTest;
import com.paca.paca.utils.TestUtils;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.branch.model.Amenity;
import com.paca.paca.branch.dto.AmenityDTO;
import com.paca.paca.branch.dto.BranchListDTO;
import com.paca.paca.branch.dto.AmenityListDTO;
import com.paca.paca.branch.model.BranchAmenity;
import com.paca.paca.branch.service.BranchService;
import com.paca.paca.branch.service.AmenityService;
import com.paca.paca.exception.exceptions.NoContentException;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AmenityServiceTest extends ServiceTest {

    @InjectMocks
    private BranchService branchService;

    @InjectMocks
    private AmenityService amenityService;

    @Test
    void shouldGetAllAmenities() {
        List<Amenity> amenities = TestUtils.castList(Amenity.class, Mockito.mock(List.class));

        when(amenityRepository.findAll()).thenReturn(amenities);
        AmenityListDTO responseDTO = amenityService.getAll();

        assertThat(responseDTO).isNotNull();
    }

    @Test
    void shouldGetAllAmenitiesBySearchWord() {
        List<Amenity> amenities = TestUtils.castList(Amenity.class, Mockito.mock(List.class));

        when(amenityRepository.findAllMatching(any(String.class))).thenReturn(amenities);
        AmenityListDTO responseDTO = amenityService.getBySearchWord("test");

        assertThat(responseDTO).isNotNull();
    }

    @Test
    void shouldGetNoContentDueToMissingBranchInAllAmenitiesByBranchId() {
        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            amenityService.getAllByBranchId(1L);
            TestCase.fail();
        } catch (Exception e){
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Branch does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 12);
        }
    }

    @Test
    void shouldGetAllAmenitiesByBranchId() {
        Branch branch = utils.createBranch(null);
        List<BranchAmenity> branchAmenities = TestUtils.castList(BranchAmenity.class, Mockito.mock(List.class));

        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(branch));
        when(branchAmenityRepository.findAllByBranchId(any(Long.class))).thenReturn(branchAmenities);

        AmenityListDTO responseDTO = amenityService.getAllByBranchId(branch.getId());

        assertThat(responseDTO).isNotNull();
    }

    @Test
    void shouldGetNoContentDueToMissingBranchInSaveAllAmenitiesByBranchId() {
        AmenityListDTO amenitiesDTO = AmenityListDTO.builder().build();

        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            amenityService.saveAllByBranchId(1L, amenitiesDTO);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Branch does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 12);
        }
    }

    @Test
    void shouldGetNoContentDueToMissingAmenityInSaveAllAmenitiesByBranchId() {
        Branch branch = utils.createBranch(null);
        Amenity amenity = utils.createAmenity();
        List<AmenityDTO> amenities = new ArrayList<AmenityDTO>();
        amenities.add(utils.createAmenityDTO(amenity));
        AmenityListDTO amenitiesDTO = AmenityListDTO.builder().amenities(amenities).build();

        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(branch));
        when(amenityMapper.toEntity(any(AmenityDTO.class))).thenReturn(amenity);
        when(amenityRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            amenityService.saveAllByBranchId(1L, amenitiesDTO);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Amenity does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 20);
        }
    }

    @Test
    void shouldSaveAllAmenitiesByBranchId() {
        Branch branch = utils.createBranch(null);
        Amenity amenity = utils.createAmenity();
        BranchAmenity branchAmenity = utils.createBranchAmenity(branch, amenity);
        List<AmenityDTO> amenities = new ArrayList<AmenityDTO>();
        amenities.add(utils.createAmenityDTO(amenity));
        AmenityListDTO amenitiesDTO = AmenityListDTO.builder().amenities(amenities).build();
        List<BranchAmenity> branchAmenities = TestUtils.castList(BranchAmenity.class, Mockito.mock(List.class));

        when(branchRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(branch));
        when(amenityMapper.toEntity(any(AmenityDTO.class)))
                .thenReturn(amenity);
        when(amenityRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(amenity));
        when(branchAmenityRepository.findByBranchIdAndAmenityId(any(Long.class), any(Long.class)))
                .thenReturn(Optional.empty());
        when(branchAmenityRepository.save(any(BranchAmenity.class)))
                .thenReturn(branchAmenity);
        when(branchAmenityRepository.findAllByBranchId(any(Long.class)))
                .thenReturn(branchAmenities);

        AmenityListDTO responseDTO = amenityService.saveAllByBranchId(branch.getId(), amenitiesDTO);

        assertThat(responseDTO).isNotNull();
    }

    @Test
    void shouldGetNoContentDueToMissingBranchInDeleteAllAmenitiesByBranchId() {
        AmenityListDTO amenitiesDTO = AmenityListDTO.builder().build();

        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            amenityService.deleteAllByBranchId(1L, amenitiesDTO);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Branch does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 12);
        }
    }

    @Test
    void shouldGetNoContentDueToMissingAmenityInDeleteAllAmenitiesByBranchId() {
        Branch branch = utils.createBranch(null);
        Amenity amenity = utils.createAmenity();
        List<AmenityDTO> amenities = new ArrayList<AmenityDTO>();
        amenities.add(utils.createAmenityDTO(amenity));
        AmenityListDTO amenitiesDTO = AmenityListDTO.builder().amenities(amenities).build();

        when(branchRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(branch));
        when(amenityMapper.toEntity(any(AmenityDTO.class))).thenReturn(amenity);
        when(amenityRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        try {
            amenityService.deleteAllByBranchId(1L, amenitiesDTO);
            TestCase.fail();
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Amenity does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 20);
        }
    }

    @Test
    void shouldDeleteAllAmenitiesByBranchId() {
        Branch branch = utils.createBranch(null);
        Amenity amenity = utils.createAmenity();
        BranchAmenity branchAmenity = utils.createBranchAmenity(branch, amenity);
        List<AmenityDTO> amenities = new ArrayList<AmenityDTO>();
        amenities.add(utils.createAmenityDTO(amenity));
        AmenityListDTO amenitiesDTO = AmenityListDTO.builder().amenities(amenities).build();
        List<BranchAmenity> branchAmenities = TestUtils.castList(BranchAmenity.class, Mockito.mock(List.class));

        when(branchRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(branch));
        when(amenityMapper.toEntity(any(AmenityDTO.class)))
                .thenReturn(amenity);
        when(amenityRepository.findById(any(Long.class)))
                .thenReturn(Optional.ofNullable(amenity));
        when(branchAmenityRepository.findByBranchIdAndAmenityId(any(Long.class), any(Long.class)))
                .thenReturn(Optional.ofNullable(branchAmenity));
        when(branchAmenityRepository.findAllByBranchId(any(Long.class)))
                .thenReturn(branchAmenities);

        AmenityListDTO responseDTO = amenityService.deleteAllByBranchId(branch.getId(), amenitiesDTO);

        assertThat(responseDTO).isNotNull();
    }

    @Test
    void shouldGetNoContentDueToMissingAmenityInGetAllBranchesByAmenityId() {
        when(amenityRepository.existsById(any(Long.class))).thenReturn(false);

        try {
            amenityService.getAllBranches(1L);
            TestCase.fail();
        } catch (Exception e){
            Assert.assertTrue(e instanceof NoContentException);
            Assert.assertEquals(e.getMessage(), "Amenity with id 1 does not exists");
            Assert.assertEquals(((NoContentException) e).getCode(), (Integer) 34);
        }
    }

    @Test
    void shouldGetAllBranchesByAmenityId() {
        Amenity amenity = utils.createAmenity();
        List<BranchAmenity> branchAmenities = TestUtils.castList(
                BranchAmenity.class,
                Mockito.mock(List.class));

        when(amenityRepository.existsById(any(Long.class))).thenReturn(true);
        when(branchAmenityRepository.findAllByAmenityId(any(Long.class))).thenReturn(branchAmenities);

        BranchListDTO responseDTO = amenityService.getAllBranches(amenity.getId());

        assertThat(responseDTO).isNotNull();
    }

}
