package com.paca.paca.branch;

import org.junit.jupiter.api.Test;

import com.paca.paca.RepositoryTest;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.branch.model.Amenity;
import com.paca.paca.branch.model.BranchAmenity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class AmenityRepositoryTest extends RepositoryTest {

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
