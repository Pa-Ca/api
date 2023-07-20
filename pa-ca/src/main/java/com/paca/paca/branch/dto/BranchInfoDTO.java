package com.paca.paca.branch.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BranchInfoDTO extends BranchDTO {

    private List<DefaultTaxDTO> defaultTaxes;

    public BranchInfoDTO(BranchDTO branchDTO) {
        this.id = branchDTO.getId();
        this.businessId = branchDTO.getBusinessId();
        this.location = branchDTO.getLocation();
        this.mapsLink = branchDTO.getMapsLink();
        this.name = branchDTO.getName();
        this.overview = branchDTO.getOverview();
        this.score = branchDTO.getScore();
        this.capacity = branchDTO.getCapacity();
        this.reservationPrice = branchDTO.getReservationPrice();
        this.reserveOff = branchDTO.getReserveOff();
        this.averageReserveTime = branchDTO.getAverageReserveTime();
        this.visibility = branchDTO.getVisibility();
        this.phoneNumber = branchDTO.getPhoneNumber();
        this.type = branchDTO.getType();
        this.hourIn = branchDTO.getHourIn();
        this.hourOut = branchDTO.getHourOut();
        this.deleted = branchDTO.getDeleted();
        this.dollarToLocalCurrencyExchange = branchDTO.getDollarToLocalCurrencyExchange();
        this.defaultTaxes = List.of();
    }

}
