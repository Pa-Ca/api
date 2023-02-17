package com.paca.paca.utils;

import java.util.List;

import com.paca.paca.user.model.Role;
import com.paca.paca.user.model.User;
import com.paca.paca.statics.UserRole;
import com.paca.paca.client.model.Client;
import com.paca.paca.client.model.Friend;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.business.model.Business;
import com.paca.paca.client.dto.ClientDTO;
import com.paca.paca.client.dto.FriendDTO;
import com.paca.paca.user.repository.RoleRepository;
import com.paca.paca.user.repository.UserRepository;
import com.paca.paca.client.repository.ClientRepository;
import com.paca.paca.client.repository.FriendRepository;

import java.util.ArrayList;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TestUtils {
    public static <T> List<T> castList(Class<? extends T> clazz, List<?> rawCollection) {
        List<T> result = new ArrayList<>(rawCollection.size());

        for (int i = 0; i < rawCollection.size(); i++) {
            result.add(clazz.cast(rawCollection.get(i)));
        }

        return result;
    }

    public static User createUser(RoleRepository roleRepository, UserRepository userRepository) {
        Role role = Role.builder()
                .id((long) UserRole.client.ordinal())
                .name(UserRole.client)
                .build();
        User user = User.builder()
                .id(1L)
                .email("test@test.com")
                .password("123456789aA#")
                .verified(false)
                .loggedIn(false)
                .role(role)
                .build();

        if (roleRepository != null) {
            roleRepository.save(user.getRole());
        }
        if (userRepository != null) {
            user = userRepository.save(user);
        }

        return user;
    }

    public static Client createClient(User user, ClientRepository clientRepository) throws ParseException {
        if (user == null) {
            user = createUser(null, null);
        }
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
        if (clientRepository != null) {
            client = clientRepository.save(client);
        }
        return client;
    }

    public static ClientDTO createClientDTO(Client client) throws ParseException {
        if (client == null) {
            client = createClient(null, null);
        }
        ClientDTO dto = ClientDTO.builder()
                .id(client.getId())
                .userId(client.getUser().getId())
                .email(client.getUser().getEmail())
                .name(client.getName())
                .surname(client.getSurname())
                .stripeCustomerId(client.getStripeCustomerId())
                .phoneNumber(client.getPhoneNumber())
                .address(client.getAddress())
                .dateOfBirth(client.getDateOfBirth())
                .build();

        return dto;
    }

    public static Friend createFriendRequest(
            Client requester,
            Client addresser,
            boolean accepted,
            boolean rejected,
            FriendRepository friendRepository) throws ParseException {
        if (requester == null) {
            requester = createClient(null, null);
        }
        if (addresser == null) {
            addresser = createClient(null, null);
            addresser.setId(requester.getId() + 1);
        }

        Friend request = Friend.builder()
                .requester(requester)
                .addresser(addresser)
                .accepted(accepted)
                .rejected(rejected)
                .build();

        if (friendRepository != null) {
            request = friendRepository.save(request);
        }

        return request;
    }


    public static FriendDTO createFriendRequestDTO(Friend request) throws ParseException {
        if (request == null) {
            request = createFriendRequest(null, null, false, false, null);
        }
        FriendDTO dto = FriendDTO.builder()
                .requesterId(request.getRequester().getId())
                .addresserId(request.getAddresser().getId())
                .accepted(request.getAccepted())
                .rejected(request.getRejected())
                .build();

        return dto;
    }

    public static Business createBussiness() {
        Business business = Business.builder()
                .id(1L)
                .build();

        return business;
    }

}
