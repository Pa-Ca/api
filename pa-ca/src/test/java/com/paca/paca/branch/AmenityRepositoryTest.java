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
public class AmenityRepositoryTest extends PacaTest {

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
    void shouldCheckThatBranchAmenityExistsByBranchIdAndAmenityId() {
        BranchAmenity branchAmenity = utils.createBranchAmenity(null, null);

        Optional<BranchAmenity> expectedBranchAmenity = branchAmenityRepository.findByBranchIdAndAmenityId(
                branchAmenity.getBranch().getId(),
                branchAmenity.getAmenity().getId());

        assertThat(expectedBranchAmenity.isPresent()).isTrue();
        assertThat(expectedBranchAmenity.get().getBranch().getId()).isEqualTo(branchAmenity.getBranch().getId());
        assertThat(expectedBranchAmenity.get().getAmenity().getId()).isEqualTo(branchAmenity.getAmenity().getId());
    }

    @Test
    void shouldCheckThatBranchAmenityDoesNotExistsByBranchIdAndAmenityId() {
        Optional<BranchAmenity> expectedBranchAmenity = branchAmenityRepository.findByBranchIdAndAmenityId(1L, 1L);

        assertThat(expectedBranchAmenity.isEmpty()).isTrue();
    }

}
