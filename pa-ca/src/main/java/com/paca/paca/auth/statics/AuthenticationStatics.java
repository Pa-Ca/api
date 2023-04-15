package com.paca.paca.auth.statics;

import java.util.Map;
import static java.util.Map.entry;

public interface AuthenticationStatics {

  interface Jwt {
    Integer TOKEN_EXPIRATION = 300000;
    Integer REFRESH_EXPIRATION = 86400000;
    Integer RESET_PASSWORD_EXPIRATION = 300000;
    String SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
  }

  interface Auth {
    String EMAIL_REGEX = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    Integer PASS_MIN_LENGTH = 8;
    Integer PASS_MAX_LENGTH = 64;
    Map<String, String> PASSWORD_ERRORS_PROPS = Map.ofEntries(
        entry("TOO_SHORT", "2"),
        entry("TOO_LONG", "3"));
  }

  interface Endpoint {
    String LOGIN = "/login";
    String SIGNUP = "/signup";
    String LOGOUT = "/logout";
    String REFRESH = "/refresh";
    String AUTH_PATH = "/api/v1/auth";
    String RESET_PASSWORD = "/reset-password";
    String RESET_PASSWORD_REQUEST = "/reset-password-request";
  }
}
