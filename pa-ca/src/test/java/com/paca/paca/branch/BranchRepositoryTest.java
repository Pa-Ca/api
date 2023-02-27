package com.paca.paca.branch;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;

import com.paca.paca.PacaTest;
import com.paca.paca.utils.TestUtils;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.branch.model.Amenity;
import com.paca.paca.business.model.Business;
import com.paca.paca.branch.model.BranchAmenity;
import com.paca.paca.user.repository.RoleRepository;
import com.paca.paca.user.repository.UserRepository;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.branch.repository.AmenityRepository;
import com.paca.paca.business.repository.BusinessRepository;
import com.paca.paca.branch.repository.BranchAmenityRepository;

import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Testcontainers
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

public class BranchRepositoryTest extends PacaTest {
    
    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private AmenityRepository amenityRepository;

    @Autowired
    private BranchAmenityRepository branchAmenityRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private TestUtils utils;

    @BeforeAll
    void initUtils() {
        utils = TestUtils.builder()
                .roleRepository(roleRepository)
                .userRepository(userRepository)
                .businessRepository(businessRepository)
                .branchRepository(branchRepository)
                .amenityRepository(amenityRepository)
                .branchAmenityRepository(branchAmenityRepository)
                .build();
    }

    @BeforeEach
    void restoreBranchDB() {
        branchAmenityRepository.deleteAll();
        branchRepository.deleteAll();
        amenityRepository.deleteAll();
    }

    @AfterEach
    void restoreTest() {
        branchAmenityRepository.deleteAll();
        branchRepository.deleteAll();
        amenityRepository.deleteAll();
        businessRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    void shouldCreateBranch() {
        Business business = utils.createBusiness(null);
        Branch branch = Branch.builder()
                .id(1L)
                .business(business)
                .address("address test")
                .coordinates("coordinates test")
                .name("name test")
                .overview("overview test")
                .score(4.0F)
                .capacity(42)
                .reservationPrice(37.0F)
                .reserveOff(false)
                .build();

        Branch savedBranch = branchRepository.save(branch);

        assertThat(savedBranch).isNotNull();
        assertThat(savedBranch.getBusiness().getId()).isEqualTo(branch.getBusiness().getId());
        assertThat(savedBranch.getAddress()).isEqualTo(branch.getAddress());
        assertThat(savedBranch.getCoordinates()).isEqualTo(branch.getCoordinates());
        assertThat(savedBranch.getName()).isEqualTo(branch.getName());
        assertThat(savedBranch.getOverview()).isEqualTo(branch.getOverview());
        assertThat(savedBranch.getScore()).isEqualTo(branch.getScore());
        assertThat(savedBranch.getCapacity()).isEqualTo(branch.getCapacity());
        assertThat(savedBranch.getReservationPrice()).isEqualTo(branch.getReservationPrice());
        assertThat(savedBranch.getReserveOff()).isEqualTo(branch.getReserveOff());
    }

    @Test
    void shouldGetAllBranches() {
        int nUsers = 10;

        for (int i = 0; i < nUsers; i++) {
            utils.createBranch(null);
        }

        List<Branch> branches = branchRepository.findAll();

        assertThat(branches.size()).isEqualTo(nUsers);
    }

    @Test
    void shouldCheckThatBranchExistsById() {
        Branch branch = utils.createBranch(null);

        boolean expected = branchRepository.existsById(branch.getId());
        Optional<Branch> expectedBranch = branchRepository.findById(branch.getId());

        assertThat(expected).isTrue();
        assertThat(expectedBranch.isPresent()).isTrue();
        assertThat(expectedBranch.get().getBusiness().getId()).isEqualTo(branch.getBusiness().getId());
        assertThat(expectedBranch.get().getAddress()).isEqualTo(branch.getAddress());
        assertThat(expectedBranch.get().getCoordinates()).isEqualTo(branch.getCoordinates());
        assertThat(expectedBranch.get().getName()).isEqualTo(branch.getName());
        assertThat(expectedBranch.get().getOverview()).isEqualTo(branch.getOverview());
        assertThat(expectedBranch.get().getScore()).isEqualTo(branch.getScore());
        assertThat(expectedBranch.get().getCapacity()).isEqualTo(branch.getCapacity());
        assertThat(expectedBranch.get().getReservationPrice()).isEqualTo(branch.getReservationPrice());
        assertThat(expectedBranch.get().getReserveOff()).isEqualTo(branch.getReserveOff());
    }

    @Test
    void shouldCheckThatBranchDoesNotExistsById() {
        boolean expected = branchRepository.existsById(1L);
        Optional<Branch> expectedBranch = branchRepository.findById(1L);

        assertThat(expected).isFalse();
        assertThat(expectedBranch.isEmpty()).isTrue();
    }

    @Test
    void shouldDeleteBranch() {
        Branch branch = utils.createBranch(null);

        branchRepository.delete(branch);

        List<Branch> branches = branchRepository.findAll();
        assertThat(branches.size()).isEqualTo(0);
    }

    @Test
    void shouldCreateAmenity() {
        Amenity amenity = Amenity.builder()
                .id(1L)
                .name("name test")
                .build();

        Amenity savedAmenity = amenityRepository.save(amenity);

        assertThat(savedAmenity).isNotNull();
        assertThat(savedAmenity.getName()).isEqualTo(amenity.getName());
    }

    @Test
    void shouldGetAllAmenities() {
        int nUsers = 10;

        for (int i = 0; i < nUsers; i++) {
            utils.createAmenity();
        }

        List<Amenity> amenities = amenityRepository.findAll();

        assertThat(amenities.size()).isEqualTo(nUsers);
    }

    @Test
    void shouldGetAllAmenitiesMatching() {
        Amenity matchingAmenity = Amenity.builder()
                .id(1L)
                .name("name matching")
                .build();
        amenityRepository.save(matchingAmenity);
        matchingAmenity = Amenity.builder()
                .id(2L)
                .name("name mtachin")
                .build();
        amenityRepository.save(matchingAmenity);
        matchingAmenity = Amenity.builder()
                .id(3L)
                .name("Ame maching")
                .build();
        amenityRepository.save(matchingAmenity);
        matchingAmenity = Amenity.builder()
                .id(4L)
                .name("matching")
                .build();
        amenityRepository.save(matchingAmenity);

        Amenity notMatchingAmenity = Amenity.builder()
                .id(5L)
                .name("random")
                .build();
        amenityRepository.save(notMatchingAmenity);
        notMatchingAmenity = Amenity.builder()
                .id(6L)
                .name("another")
                .build();
        amenityRepository.save(notMatchingAmenity);
        notMatchingAmenity = Amenity.builder()
                .id(7L)
                .name("last")
                .build();
        amenityRepository.save(notMatchingAmenity);

        List<Amenity> amenities = amenityRepository.findAllMatching("name matching");

        assertThat(amenities.size()).isEqualTo(4);
    }

    @Test
    void shouldCheckThatAmenityExistsById() {
        Amenity amenity = utils.createAmenity();

        boolean expected = amenityRepository.existsById(amenity.getId());
        Optional<Amenity> expectedAmenity = amenityRepository.findById(amenity.getId());

        assertThat(expected).isTrue();
        assertThat(expectedAmenity.isPresent()).isTrue();
        assertThat(expectedAmenity.get().getName()).isEqualTo(amenity.getName());
    }

    @Test
    void shouldCheckThatAmenityDoesNotExistsById() {
        boolean expected = amenityRepository.existsById(1L);
        Optional<Amenity> expectedAmenity = amenityRepository.findById(1L);

        assertThat(expected).isFalse();
        assertThat(expectedAmenity.isEmpty()).isTrue();
    }

    @Test
    void shouldDeleteAmenity() {
        Amenity amenity = utils.createAmenity();

        amenityRepository.delete(amenity);

        List<Amenity> amenities = amenityRepository.findAll();
        assertThat(amenities.size()).isEqualTo(0);
    }

    @Test
    void shouldCreateBranchAmenity() {
        Branch branch = utils.createBranch(null);
        Amenity amenity = utils.createAmenity();
        BranchAmenity branchAmenity = BranchAmenity.builder()
                .id(1L)
                .branch(branch)
                .amenity(amenity)
                .build();

        BranchAmenity savedBranchAmenity = branchAmenityRepository.save(branchAmenity);

        assertThat(savedBranchAmenity).isNotNull();
        assertThat(savedBranchAmenity.getBranch().getId()).isEqualTo(branch.getId());
        assertThat(savedBranchAmenity.getAmenity().getId()).isEqualTo(amenity.getId());
    }

    @Test
    void shouldGetAllBranchAmenities() {
        int nBranchAmenities = 10;

        for (int i = 0; i < nBranchAmenities; i++) {
            utils.createBranchAmenity(null, null);
        }

        List<BranchAmenity> branchAmenities = branchAmenityRepository.findAll();

        assertThat(branchAmenities.size()).isEqualTo(nBranchAmenities);
    }

    @Test 
    void shoudlGetAllBranchAmenitiesByBranchId() {
        int nBranchAmenities = 10;
        Branch branch = utils.createBranch(null);

        for (int i = 0; i < nBranchAmenities; i++) {
            utils.createBranchAmenity(null, null);
            utils.createBranchAmenity(branch, null);
        }

        List<BranchAmenity> branchAmenities = branchAmenityRepository.findAllByBranchId(branch.getId());

        assertThat(branchAmenities.size()).isEqualTo(nBranchAmenities);
    }

    @Test 
    void shoudlGetAllBranchAmenitiesByAmenityId() {
        int nBranchAmenities = 10;
        Amenity amenity = utils.createAmenity();

        for (int i = 0; i < nBranchAmenities; i++) {
            utils.createBranchAmenity(null, null);
            utils.createBranchAmenity(null, amenity);
        }

        List<BranchAmenity> branchAmenities = branchAmenityRepository.findAllByAmenityId(amenity.getId());

        assertThat(branchAmenities.size()).isEqualTo(nBranchAmenities);
    }

    @Test
    void shouldCheckThatBranchAmenityExistsById() {
        BranchAmenity branchAmenity = utils.createBranchAmenity(null, null);

        boolean expected = branchAmenityRepository.existsById(branchAmenity.getId());
        Optional<BranchAmenity> expectedBranchAmenity = branchAmenityRepository.findById(branchAmenity.getId());

        assertThat(expected).isTrue();
        assertThat(expectedBranchAmenity.isPresent()).isTrue();
        assertThat(expectedBranchAmenity.get().getBranch().getId()).isEqualTo(branchAmenity.getBranch().getId());
        assertThat(expectedBranchAmenity.get().getAmenity().getId()).isEqualTo(branchAmenity.getAmenity().getId());
    }

    @Test
    void shouldCheckThatBranchAmenityDoesNotExistsById() {
        boolean expected = branchAmenityRepository.existsById(1L);
        Optional<BranchAmenity> expectedBranchAmenity = branchAmenityRepository.findById(1L);

        assertThat(expected).isFalse();
        assertThat(expectedBranchAmenity.isEmpty()).isTrue();
    }

    @Test
    void shouldCheckThatBranchAmenityExistsByBranchIdAndAmenityId() {
        BranchAmenity branchAmenity = utils.createBranchAmenity(null, null);

        boolean expected = branchAmenityRepository.existsByBranchIdAndAmenityId(
                branchAmenity.getBranch().getId(),
                branchAmenity.getAmenity().getId());
        Optional<BranchAmenity> expectedBranchAmenity = branchAmenityRepository.findByBranchIdAndAmenityId(
            branchAmenity.getBranch().getId(),
            branchAmenity.getAmenity().getId());

        assertThat(expected).isTrue();
        assertThat(expectedBranchAmenity.isPresent()).isTrue();
        assertThat(expectedBranchAmenity.get().getBranch().getId()).isEqualTo(branchAmenity.getBranch().getId());
        assertThat(expectedBranchAmenity.get().getAmenity().getId()).isEqualTo(branchAmenity.getAmenity().getId());
    }

    @Test
    void shouldCheckThatBranchAmenityDoesNotExistsByBranchIdAndAmenityId() {
        boolean expected = branchAmenityRepository.existsByBranchIdAndAmenityId(1L, 1L);
        Optional<BranchAmenity> expectedBranchAmenity = branchAmenityRepository.findByBranchIdAndAmenityId(1L, 1L);

        assertThat(expected).isFalse();
        assertThat(expectedBranchAmenity.isEmpty()).isTrue();
    }

    @Test
    void shouldDeleteBranchAmenity() {
        BranchAmenity branchAmenity = utils.createBranchAmenity(null, null);

        branchAmenityRepository.delete(branchAmenity);

        List<BranchAmenity> branchAmenities = branchAmenityRepository.findAll();
        assertThat(branchAmenities.size()).isEqualTo(0);
    }

}

