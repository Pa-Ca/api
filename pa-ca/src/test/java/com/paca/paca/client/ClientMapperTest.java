package com.paca.paca.client;

import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;
import com.paca.paca.user.model.User;
import com.paca.paca.client.model.Client;
import com.paca.paca.client.model.Friend;
import com.paca.paca.client.dto.ClientDTO;
import com.paca.paca.client.dto.FriendDTO;

import org.junit.jupiter.api.extension.ExtendWith;
import com.paca.paca.client.utils.ClientMapperImpl;
import com.paca.paca.client.utils.FriendMapperImpl;

import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
public class ClientMapperTest {

    @InjectMocks
    private ClientMapperImpl clientMapper;

    @InjectMocks
    private FriendMapperImpl friendMapper;

    @Test
    void shouldMapClientEntityToClientDTO() throws ParseException {
        User user = User.builder()
                .id(1L)
                .build();
        Client client = Client.builder()
                .id(1L)
                .user(user)
                .name("test")
                .surname("Test")
                .stripeCustomerId("stripe_id_test")
                .phoneNumber("+580000000")
                .address("Test address")
                .dateOfBirth(new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-01"))
                .build();

        ClientDTO response = clientMapper.toDTO(client);
        response.setUserId(user.getId());

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(client.getId());
        assertThat(response.getUserId()).isEqualTo(user.getId());
        assertThat(response.getName()).isEqualTo(client.getName());
        assertThat(response.getSurname()).isEqualTo(client.getSurname());
        assertThat(response.getStripeCustomerId()).isEqualTo(client.getStripeCustomerId());
        assertThat(response.getPhoneNumber()).isEqualTo(client.getPhoneNumber());
        assertThat(response.getAddress()).isEqualTo(client.getAddress());
        assertThat(response.getDateOfBirth()).isEqualTo(client.getDateOfBirth());
    }

    @Test 
    void shouldMapClientDTOtoClientEntity() throws ParseException {
        User user = User.builder()
                .id(1L)
                .build();
        ClientDTO dto = ClientDTO.builder()
                .id(1L)
                .userId(user.getId())
                .email(user.getEmail())
                .name("test")
                .surname("Test")
                .stripeCustomerId("stripe_id_test")
                .phoneNumber("+580000000")
                .address("Test address")
                .dateOfBirth(new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-01"))
                .build();

        Client client = clientMapper.toEntity(dto);
        client.setUser(user);

        assertThat(client).isNotNull();
        assertThat(client.getId()).isEqualTo(dto.getId());
        assertThat(client.getUser().getId()).isEqualTo(user.getId());
        assertThat(client.getName()).isEqualTo(dto.getName());
        assertThat(client.getSurname()).isEqualTo(dto.getSurname());
        assertThat(client.getStripeCustomerId()).isEqualTo(dto.getStripeCustomerId());
        assertThat(client.getPhoneNumber()).isEqualTo(dto.getPhoneNumber());
        assertThat(client.getAddress()).isEqualTo(dto.getAddress());
        assertThat(client.getDateOfBirth()).isEqualTo(dto.getDateOfBirth());
    }
    
    @Test
    void shouldPartiallyMapUserDTOtoUserEntity() throws ParseException {
        User user = User.builder()
                .id(1L)
                .build();
        Client client = Client.builder()
                .id(1L)
                .user(user)
                .name("test")
                .surname("Test")
                .stripeCustomerId("stripe_id_test")
                .phoneNumber("+580000000")
                .address("Test address")
                .dateOfBirth(new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-01"))
                .build();

        // Not changing ID
        ClientDTO dto = ClientDTO.builder()
                .id(1L)
                .build();
        Client updatedClient = clientMapper.updateModel(client, dto);
        assertThat(updatedClient).isNotNull();
        assertThat(updatedClient.getId()).isEqualTo(client.getId());

        // Not changing User ID
        dto = ClientDTO.builder()
                .userId(user.getId() + 1)
                .build();
        updatedClient = clientMapper.updateModel(client, dto);
        assertThat(updatedClient).isNotNull();
        assertThat(updatedClient.getUser().getId()).isEqualTo(client.getUser().getId());

        // Changing name
        dto = ClientDTO.builder()
                .name("m_test")
                .build();
        updatedClient = clientMapper.updateModel(client, dto);
        assertThat(updatedClient).isNotNull();
        assertThat(updatedClient.getName()).isEqualTo(dto.getName());

        // Changing surname
        dto = ClientDTO.builder()
                .surname("m_Test")
                .build();
        updatedClient = clientMapper.updateModel(client, dto);
        assertThat(updatedClient).isNotNull();
        assertThat(updatedClient.getSurname()).isEqualTo(dto.getSurname());

        // Change stripe customer id
        dto = ClientDTO.builder()
                .stripeCustomerId("m_stripe_id_test")
                .build();
        updatedClient = clientMapper.updateModel(client, dto);
        assertThat(updatedClient).isNotNull();
        assertThat(updatedClient.getStripeCustomerId()).isEqualTo(dto.getStripeCustomerId());

        // Change phone number
        dto = ClientDTO.builder()
                .phoneNumber("+580000001")
                .build();
        updatedClient = clientMapper.updateModel(client, dto);
        assertThat(updatedClient).isNotNull();
        assertThat(updatedClient.getPhoneNumber()).isEqualTo(dto.getPhoneNumber());

        // Change address
        dto = ClientDTO.builder()
                .address("m_Test address")
                .build();
        updatedClient = clientMapper.updateModel(client, dto);
        assertThat(updatedClient).isNotNull();
        assertThat(updatedClient.getAddress()).isEqualTo(dto.getAddress());

        // Change date of birth
        dto = ClientDTO.builder()
                .dateOfBirth(new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-02"))
                .build();
        updatedClient = clientMapper.updateModel(client, dto);
        assertThat(updatedClient).isNotNull();
        assertThat(updatedClient.getDateOfBirth()).isEqualTo(dto.getDateOfBirth());
    }

    @Test 
    void shouldMapFriendEntityToFriendDTO() {
        Client requester = Client.builder()
                .id(1L)
                .build();
        Client addresser = Client.builder()
                .id(2L)
                .build();
        Friend friend = Friend.builder()
                .id(1L)
                .requester(requester)
                .addresser(addresser)
                .accepted(false)
                .rejected(false)
                .build();

        FriendDTO response = friendMapper.toDTO(friend);
        response.setRequesterId(requester.getId());
        response.setAddresserId(addresser.getId());

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(friend.getId());
        assertThat(response.getRequesterId()).isEqualTo(requester.getId());
        assertThat(response.getAddresserId()).isEqualTo(addresser.getId());
        assertThat(response.getAccepted()).isEqualTo(false);
        assertThat(response.getRejected()).isEqualTo(false);
    }
    
    @Test 
    void shouldMapFriendDTOtoFriendEntity() {
        Client requester = Client.builder()
                .id(1L)
                .build();
        Client addresser = Client.builder()
                .id(2L)
                .build();
        FriendDTO dto = FriendDTO.builder()
                .id(1L)
                .requesterId(requester.getId())
                .addresserId(addresser.getId())
                .accepted(false)
                .rejected(false)
                .build();

        Friend friend = friendMapper.toEntity(dto);
        friend.setRequester(requester);
        friend.setAddresser(addresser);

        assertThat(friend).isNotNull();
        assertThat(friend.getId()).isEqualTo(dto.getId());
        assertThat(friend.getRequester().getId()).isEqualTo(requester.getId());
        assertThat(friend.getAddresser().getId()).isEqualTo(addresser.getId());
        assertThat(friend.getAccepted()).isEqualTo(dto.getAccepted());
        assertThat(friend.getRejected()).isEqualTo(dto.getRejected());
    }

    @Test 
    void shouldPartiallyMapFriendDTOtoFriendEntity() {
        Client requester = Client.builder()
                .id(1L)
                .build();
        Client addresser = Client.builder()
                .id(2L)
                .build();
        Friend friend = Friend.builder()
                .id(1L)
                .requester(requester)
                .addresser(addresser)
                .accepted(false)
                .rejected(false)
                .build();

        // Not changing ID
        FriendDTO dto = FriendDTO.builder()
                .id(1L)
                .build();
        Friend updateFriend = friendMapper.updateModel(friend, dto);
        assertThat(updateFriend).isNotNull();
        assertThat(updateFriend.getId()).isEqualTo(friend.getId());

        // Not changing Requester ID
        dto = FriendDTO.builder()
                .requesterId(requester.getId() + 1)
                .build();
        updateFriend = friendMapper.updateModel(friend, dto);
        assertThat(updateFriend).isNotNull();
        assertThat(updateFriend.getRequester().getId()).isEqualTo(requester.getId());

        // Not changing Addreser ID
        dto = FriendDTO.builder()
                .addresserId(addresser.getId() + 1)
                .build();
        updateFriend = friendMapper.updateModel(friend, dto);
        assertThat(updateFriend).isNotNull();
        assertThat(updateFriend.getAddresser().getId()).isEqualTo(addresser.getId());

        // Changing accepted
        dto = FriendDTO.builder()
                .accepted(true)
                .build();
        updateFriend = friendMapper.updateModel(friend, dto);
        assertThat(updateFriend).isNotNull();
        assertThat(updateFriend.getAccepted()).isEqualTo(dto.getAccepted());

        // Changing rejected
        dto = FriendDTO.builder()
                .rejected(true)
                .build();
        updateFriend = friendMapper.updateModel(friend, dto);
        assertThat(updateFriend).isNotNull();
        assertThat(updateFriend.getRejected()).isEqualTo(dto.getRejected());
    }
}