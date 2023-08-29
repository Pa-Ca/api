package com.paca.paca.branch;

import org.junit.jupiter.api.Test;

import com.paca.paca.RepositoryTest;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.business.model.Business;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class BranchRepositoryTest extends RepositoryTest {

    @Test
    void shouldGetAllByBusinessId() {
        int nBranches = 10;
        Business business = utils.createBusiness(null);

        for (int i = 0; i < nBranches; i++) {
            utils.createBranch(business);
            utils.createBranch(null);
        }

        List<Branch> branches = branchRepository.findAllByBusinessId(business.getId());

        assertThat(branches.size()).isEqualTo(nBranches);
    }

    @Test
    void shouldCheckThatBranchExistsByIdAndBusinessId() {
        Business business = utils.createBusiness(null);
        Branch branch = utils.createBranch(business);

        assertThat(branchRepository.existsByIdAndBusinessId(branch.getId(), business.getId())).isTrue();
    }

    @Test
    void shouldCheckThatBranchDoesNotExistsByIdAndBusinessId() {
        Business business = utils.createBusiness(null);
        Branch branch = utils.createBranch(business);

        assertThat(branchRepository.existsByIdAndBusinessId(branch.getId(), business.getId() + 1)).isFalse();
    }
}
