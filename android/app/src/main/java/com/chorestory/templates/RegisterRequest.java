package com.chorestory.templates;

public class RegisterRequest {
    private String clan_name;
    private String name;
    private String email;
    private String password;

    public RegisterRequest(String clanName, String username, String password) {
        this.clan_name = clanName;
        this.name = username; // TODO: change the form to take name and email separately
        this.email = username;
        this.password = password;
    }
}
