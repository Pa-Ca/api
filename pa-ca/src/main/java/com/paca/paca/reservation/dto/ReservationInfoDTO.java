package com.paca.paca.reservation.dto;

import java.util.List;
import java.util.Optional;

import com.paca.paca.client.model.Client;
import com.paca.paca.client.dto.ClientDTO;
import com.paca.paca.reservation.model.Guest;
import com.paca.paca.reservation.model.Invoice;
import com.paca.paca.client.utils.ClientMapper;
import com.paca.paca.reservation.model.Reservation;
import com.paca.paca.reservation.model.ClientGroup;
import com.paca.paca.reservation.utils.GuestMapper;
import com.paca.paca.reservation.utils.InvoiceMapper;
import com.paca.paca.client.repository.ClientRepository;
import com.paca.paca.reservation.utils.ReservationMapper;
import com.paca.paca.reservation.repository.GuestRepository;
import com.paca.paca.reservation.repository.InvoiceRepository;
import com.paca.paca.reservation.repository.ClientGroupRepository;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationInfoDTO {

    private ReservationDTO reservation;
    private InvoiceDTO invoice;
    private GuestDTO guest;
    private ClientDTO owner;

    public void completeOwner(
            GuestRepository guestRepository,
            ClientGroupRepository clientGroupRepository,
            ClientRepository clientRepository,
            GuestMapper guestMapper,
            ClientMapper clientMapper) {
        if (guestRepository == null || clientGroupRepository == null || clientRepository == null) {
            return;
        }

        if (this.reservation.getByClient()) {
            // We complete the reservation data with the data of the client who
            // made the reservation
            List<ClientGroup> clientGroups = clientGroupRepository.findAllByReservationId(this.reservation.getId());
            ClientGroup owner = clientGroups.stream()
                    .filter(clientGroup -> clientGroup.getIsOwner())
                    .findFirst()
                    .get();
            Client client = clientRepository.findById(owner.getClient().getId()).get();

            this.guest = null;
            this.owner = clientMapper.toDTO(client);
        } else {
            // Complete the reservation data with those that appear in the
            // associated guest
            Guest guest = guestRepository.findById(this.reservation.getGuestId()).get();
            this.owner = null;
            this.guest = guestMapper.toDTO(guest);
        }
    }

    public ReservationInfoDTO(
            Reservation reservation,
            GuestRepository guestRepository,
            ClientRepository clientRepository,
            InvoiceRepository invoiceRepository,
            ClientGroupRepository clientGroupRepository,
            GuestMapper guestMapper,
            ClientMapper clientMapper,
            InvoiceMapper invoiceMapper,
            ReservationMapper reservationMapper) {
        this.reservation = reservationMapper.toDTO(reservation);

        if (this.reservation.getInvoiceId() != null) {
            Optional<Invoice> invoice = invoiceRepository.findById(this.reservation.getInvoiceId());
            this.invoice = invoiceMapper.toDTO(invoice.get());
        } else {
            this.invoice = null;
        }

        this.completeOwner(
                guestRepository,
                clientGroupRepository,
                clientRepository,
                guestMapper,
                clientMapper);
    }
}
