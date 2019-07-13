package com.chorestory.templates;

public class RegisterRequest {
    private String email;
    private String clan_name;
    private String name;
    private String password;
    private Integer picture;

    public RegisterRequest(String email, String clanName, String name, String password, Integer picture) {
        this.email = email;
        this.clan_name = clanName;
        this.name = name;
        this.password = password;
        this.picture = picture;
    }
}
