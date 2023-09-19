package com.paca.paca.auth.statics;

import com.paca.paca.health.HealthStatics;

import java.util.Map;
import static java.util.Map.entry;

public interface AuthenticationStatics {

  interface Auth {
    String EMAIL_REGEX = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    Integer PASS_MIN_LENGTH = 8;
    Integer PASS_MAX_LENGTH = 64;
    Map<String, String> PASSWORD_ERRORS_PROPS = Map.ofEntries(
        entry("TOO_SHORT", "2"),
        entry("TOO_LONG", "3"));
    String[] ALLOW_MATCHES = {
        "/swagger-ui**",
        "/swagger-ui/**",
        "/v3/api-docs/**",
        "/swagger-ui/index.html/**",
        AuthenticationStatics.Endpoint.AUTH_PATH + "/**",
        HealthStatics.Endpoint.PATH + "/**"
    };
  }

  interface Endpoint {
    String AUTH_PATH = "/api/v1/auth";

    String LOGIN = "/login";
    String SIGNUP = "/signup";
    String LOGOUT = "/logout";
    String REFRESH = "/refresh";
    String RESET_PASSWORD = "/reset-password";
    String GOOGLE_LOGIN = "/google/login";
    String GOOGLE_SIGNUP = "/google/signup";
    String RESET_PASSWORD_REQUEST = "/reset-password-request";
    String VERIFY_EMAIL = "/verify-email";
    String VERIFY_EMAIL_REQUEST = "/verify-email-request";
  }

  interface Password {
    String PASSWORD_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+";
  }
}
