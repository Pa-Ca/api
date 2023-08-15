package com.paca.paca.client.statics;

public interface ClientStatics {

    interface Endpoint {
        String PATH = "/api/v1/client";

        String SAVE = "";
        String UPDATE = "/{id}";
        String DELETE = "/{id}";
        String GET_BY_ID = "/{id}";
        String GET_BY_USER_ID = "/user/{id}";
        String FRIENDS_ACCEPTED = "/friends/accept/{id}";
        String FRIENDS_REJECTED = "/friends/reject/{id}";
        String FRIENDS_PENDING = "/friends/pending/{id}";
        String FRIENDS_REQUEST = "/{id}/friend/pending/{requesterId}";
        String DELETE_FRIEND_REQUEST = "/{id}/friend/pending/{requesterId}";
        String ACCEPT_FRIEND_REQUEST = "/{id}/friend/pending/{requesterId}/accept";
        String REJECT_FRIEND_REQUEST = "/{id}/friend/pending/{requesterId}/reject";
        String RESERVATIONS = "/{id}/reservation";
        String FAVORITE_BRANCHES = "/{id}/favorite-branches";
        String SAVE_FAVORITE_BRANCH = "/{id}/favorite-branches/{branchId}";
        String DELETE_FAVORITE_BRANCH = "/{id}/favorite-branches/{branchId}";
    }
}
