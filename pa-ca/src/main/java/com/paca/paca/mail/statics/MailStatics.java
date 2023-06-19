package com.paca.paca.mail.statics;

public interface MailStatics {
  interface Keys {
    String USERNAME = "${username}";
    String LINK_URL = "${url}";
  }

  interface Content {
    String RESET_PASSWORD_URL = "http://localhost:3001/reset-password?token=";

    String VERIFIED_EMAIL_URL = "http://localhost:3001/verify-email?token=";

    String RESET_PASSWORD_SUBJECT = "Reinicio de contraseña";

    String VERIFY_EMAIL_SUBJECT = "Verificación de cuenta";
  }
}
