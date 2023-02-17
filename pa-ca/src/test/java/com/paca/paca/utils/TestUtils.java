package com.paca.paca.utils;

import java.util.List;

import com.paca.paca.user.model.Role;
import com.paca.paca.user.model.User;
import com.paca.paca.statics.UserRole;
import com.paca.paca.client.model.Client;
import com.paca.paca.client.model.Friend;
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

    public static User createUser() {
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

        return user;
    }

    public static User createUser(RoleRepository roleRepository, UserRepository userRepository) {
        User user = createUser();

        roleRepository.save(user.getRole());
        user = userRepository.save(user);

        return user;
    }

    public static Client createClient(User user) throws ParseException {
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

        return client;
    }

    public static Client createClient(User user, ClientRepository clientRepository) throws ParseException {
        Client client = createClient(user);
        client = clientRepository.save(client);
        return client;
    }

    public static ClientDTO createClientDTO(Client client) {
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
            boolean rejected) {
        Friend request = Friend.builder()
                .requester(requester)
                .addresser(addresser)
                .accepted(accepted)
                .rejected(rejected)
                .build();

        return request;
    }

    public static Friend createFriendRequest(
            Client requester,
            Client addresser,
            boolean accepted,
            boolean rejected,
            FriendRepository friendRepository) {
        Friend request = createFriendRequest(requester, addresser, accepted, rejected);
        request = friendRepository.save(request);
        return request;
    }

    public static FriendDTO createFriendRequestDTO(Friend request) {
        FriendDTO dto = FriendDTO.builder()
                .requesterId(request.getRequester().getId())
                .addresserId(request.getAddresser().getId())
                .accepted(request.getAccepted())
                .rejected(request.getRejected())
                .build();

        return dto;
    }
}
