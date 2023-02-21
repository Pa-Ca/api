package com.paca.paca.utils;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.paca.paca.user.model.Role;
import com.paca.paca.user.model.User;
import com.paca.paca.statics.BusinessTier;
import com.paca.paca.statics.UserRole;
import com.paca.paca.business.tier.Tier;
import com.paca.paca.client.model.Client;
import com.paca.paca.client.model.Friend;
import com.paca.paca.branch.model.Branch;
import com.paca.paca.branch.dto.BranchDTO;
import com.paca.paca.branch.model.Amenity;
import com.paca.paca.client.dto.ClientDTO;
import com.paca.paca.client.dto.FriendDTO;
import com.paca.paca.branch.dto.AmenityDTO;
import com.paca.paca.business.model.Business;
import com.paca.paca.business.repository.BusinessRepository;
import com.paca.paca.user.repository.RoleRepository;
import com.paca.paca.user.repository.UserRepository;
import com.paca.paca.client.repository.ClientRepository;
import com.paca.paca.client.repository.FriendRepository;
import com.paca.paca.branch.repository.BranchRepository;
import com.paca.paca.branch.repository.AmenityRepository;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Setter
@Getter
@Builder
public class TestUtils {

    RoleRepository roleRepository;

    UserRepository userRepository;

    ClientRepository clientRepository;

    FriendRepository friendRepository;

    BranchRepository branchRepository;

    AmenityRepository amenityRepository;

    BusinessRepository businessRepository;

    public static <T> List<T> castList(Class<? extends T> clazz, List<?> rawCollection) {
        List<T> result = new ArrayList<>(rawCollection.size());

        for (int i = 0; i < rawCollection.size(); i++) {
            result.add(clazz.cast(rawCollection.get(i)));
        }

        return result;
    }

    public User createUser() {
        Role role = Role.builder()
                .id((long) UserRole.client.ordinal())
                .name(UserRole.client)
                .build();
        User user = User.builder()
                .id(ThreadLocalRandom.current().nextLong(999999999))
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

    public Client createClient(User user) throws ParseException {
        if (user == null) {
            user = createUser();
        }
        Client client = Client.builder()
                .id(ThreadLocalRandom.current().nextLong(999999999))
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

    public ClientDTO createClientDTO(Client client) throws ParseException {
        if (client == null) {
            client = createClient(null);
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

    public Friend createFriendRequest(
            Client requester,
            Client addresser,
            boolean accepted,
            boolean rejected) throws ParseException {
        if (requester == null) {
            requester = createClient(null);
        }
        if (addresser == null) {
            addresser = createClient(null);
        }

        Friend request = Friend.builder()
                .id(ThreadLocalRandom.current().nextLong(999999999))
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

    public FriendDTO createFriendRequestDTO(Friend request) throws ParseException {
        if (request == null) {
            request = createFriendRequest(null, null, false, false);
        }
        FriendDTO dto = FriendDTO.builder()
                .requesterId(request.getRequester().getId())
                .addresserId(request.getAddresser().getId())
                .accepted(request.getAccepted())
                .rejected(request.getRejected())
                .build();

        return dto;
    }

    public Business createBusiness(User user) {
        if (user == null) {
            user = createUser();
        }
        Tier tier = Tier.builder()
                .id((long) BusinessTier.basic.ordinal())
                .name(BusinessTier.basic)
                .build();
        Business business = Business.builder()
                .id(ThreadLocalRandom.current().nextLong(999999999))
                .user(user)
                .name("Test")
                .verified(false)
                .tier(tier)
                .build();

        if (businessRepository != null) {
            business = businessRepository.save(business);
        }
        return business;
    }

    public Branch createBranch(Business business) {
        if (business == null) {
            business = createBusiness(null);
        }

        Branch branch = Branch.builder()
                .id(ThreadLocalRandom.current().nextLong(999999999))
                .business(business)
                .address("address test")
                .coordinates("coordinates test")
                .name("name test")
                .overview("overview test")
                .score(4.0F)
                .capacity(42)
                .reservationPrice(37.0F)
                .reserveOff(false)
                .build();

        if (branchRepository != null) {
            branch = branchRepository.save(branch);
        }

        return branch;
    }

    public BranchDTO createBranchDTO(Branch branch) throws ParseException {
        if (branch == null) {
            branch = createBranch(null);
        }
        BranchDTO dto = BranchDTO.builder()
                .id(branch.getId())
                .businessId(branch.getBusiness().getId())
                .address(branch.getAddress())
                .coordinates(branch.getCoordinates())
                .name(branch.getName())
                .overview(branch.getOverview())
                .score(branch.getScore())
                .capacity(branch.getCapacity())
                .reservationPrice(branch.getReservationPrice())
                .reserveOff(branch.getReserveOff())
                .build();

        return dto;
    }

    public Amenity createAmenity() {
        Amenity amenity = Amenity.builder()
                .id(ThreadLocalRandom.current().nextLong(999999999))
                .name("test")
                .build();

        if (amenityRepository != null) {
            amenity = amenityRepository.save(amenity);
        }

        return amenity;
    }

    public AmenityDTO createAmenityDTO(Amenity amenity) {
        if (amenity == null) {
            amenity = createAmenity();
        }
        AmenityDTO dto = AmenityDTO.builder()
                .id(amenity.getId())
                .name(amenity.getName())
                .build();

        return dto;
    }
}
