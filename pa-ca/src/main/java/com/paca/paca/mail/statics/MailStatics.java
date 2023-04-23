package com.paca.paca.mail.statics;

public interface MailStatics {
  interface Keys {
    String USERNAME = "${username}";
    String LINK_URL = "${url}";
  }

  interface Content {
    String RESET_PASSWORD_URL = "https://paca.app/reset-password&token=";

    String VERIFIED_EMAIL_URL = "https://paca.app/verify-email&token=";

    String RESET_PASSWORD_SUBJECT = "Reinicio de contraseña";

    String VERIFY_EMAIL_SUBJECT = "Verificación de cuenta";
  }
}
