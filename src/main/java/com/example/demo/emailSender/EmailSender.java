package com.example.demo.emailSender;

public interface EmailSender {

    void send(String to, String from, String subject);
    String buildEmailRegistration(String name, String link);
    String buildEmailPasswordReset(String name, String token);
    String buildEmailAccountDelete(String name, String token);
    String buildEmailInvitationToApp(String name, String token);

}
