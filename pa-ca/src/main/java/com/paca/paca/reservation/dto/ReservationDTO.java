package com.paca.paca.reservation.dto;

import java.util.Date;
import java.util.List;

import com.paca.paca.client.model.Client;
import com.paca.paca.reservation.model.Guest;
import com.paca.paca.reservation.model.ClientGroup;
import com.paca.paca.client.repository.ClientRepository;
import com.paca.paca.reservation.repository.GuestRepository;
import com.paca.paca.reservation.repository.ClientGroupRepository;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {

    private Long id;
    private Long branchId;
    private Long guestId;
    private Date requestDate;
    private Date reservationDate;
    private Integer clientNumber;
    private String payment;
    private Integer status;
    private Date payDate;
    private BigDecimal price;
    private String occasion;
    private Boolean byClient;
    private Boolean haveGuest;
    private String name;
    private String surname;
    private String email;
    private String phoneNumber;

    public void completeData(
            GuestRepository guestRepository,
            ClientGroupRepository clientGroupRepository,
            ClientRepository clientRepository) {
        if (this.guestId != null) {
            // Complete the reservation data with those that appear in the
            // associated guest
            Guest guest = guestRepository.findById(this.guestId).get();
            this.name = guest.getName();
            this.surname = guest.getSurname();
            this.email = guest.getEmail();
            this.phoneNumber = guest.getPhoneNumber();
            this.haveGuest = Boolean.TRUE;
        } else {
            // We complete the reservation data with the data of the client who
            // made the reservation
            List<ClientGroup> clientGroups = clientGroupRepository.findAllByReservationId(this.id);
            ClientGroup owner = clientGroups.stream()
                    .filter(clientGroup -> clientGroup.getIsOwner())
                    .findFirst()
                    .get();
            Client client = clientRepository.findById(owner.getClient().getId()).get();

            this.name = client.getName();
            this.surname = client.getSurname();
            this.email = client.getUser().getEmail();
            this.phoneNumber = client.getPhoneNumber();
            this.haveGuest = Boolean.FALSE;
        }
    }
}
