package com.paca.paca.branch.utils;

import com.paca.paca.branch.model.Review;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.client.model.Client;
import com.paca.paca.branch.dto.ReviewDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "branch.id", target = "branchId")
    @Mapping(target = "likes", ignore = true)
    ReviewDTO toDTO(Review review);

    @Mapping(target = "client", ignore = true)
    @Mapping(target = "branch", ignore = true)
    Review toEntity(ReviewDTO dto);

    default Review toEntity(ReviewDTO dto, Client client, Branch branch) {
        Review review = toEntity(dto);
        review.setClient(client);
        review.setBranch(branch);

        return review;
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "client", ignore = true)
    @Mapping(target = "branch", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Review updateModel(ReviewDTO dto, @MappingTarget Review review);
}