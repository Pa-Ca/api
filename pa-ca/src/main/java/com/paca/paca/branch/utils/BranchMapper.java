package com.paca.paca.branch.utils;

import com.paca.paca.branch.model.Branch;
import com.paca.paca.branch.dto.BranchDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BranchMapper {
    BranchDTO toDTO(Branch branch);

    Branch toEntity(BranchDTO dto);

    default Branch updateModel(Branch branch, BranchDTO dto) {
        if (dto.getAddress() != null) {
            branch.setAddress(dto.getAddress());
        }
        if (dto.getCoordinates() != null) {
            branch.setCoordinates(dto.getCoordinates());
        }
        if (dto.getName() != null) {
            branch.setName(dto.getName());
        }
        if (dto.getOverview() != null) {
            branch.setOverview(dto.getOverview());
        }
        if (dto.getScore() != null) {
            branch.setScore(dto.getScore());
        }
        if (dto.getCapacity() != null) {
            branch.setCapacity(dto.getCapacity());
        }
        if (dto.getReservationPrice() != null) {
            branch.setReservationPrice(dto.getReservationPrice());
        }
        if (dto.getReserveOff() != null) {
            branch.setReserveOff(dto.getReserveOff());
        }

        return branch;
    }
}