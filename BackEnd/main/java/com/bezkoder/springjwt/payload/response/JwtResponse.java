package com.bezkoder.springjwt.payload.response;

import lombok.Data;

import java.util.List;

@Data
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;

    private String name;

    private String surname;

    private String phone;
    private boolean isEmailPublic ;
    private boolean isPositionPublic ;
    private boolean isCompanyPublic ;
    private boolean isCVPublic ;
    private boolean isPhonePublic;

    private boolean isNamePublic;

    private boolean isSurnamePublic;
    private List<String> roles;

    public JwtResponse(String accessToken, Long id, String username, String email,String name, String surname, String phone, boolean isEmailPublic, boolean isPositionPublic, boolean isCompanyPublic, boolean isCVPublic, boolean isPhonePublic, boolean isNamePublic, boolean isSurnamePublic, List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.isEmailPublic = isEmailPublic;
        this.isPositionPublic = isPositionPublic;
        this.isCompanyPublic = isCompanyPublic;
        this.isPhonePublic = isPhonePublic;
        this.isCVPublic = isCVPublic;
        this.isNamePublic = isNamePublic;
        this.isSurnamePublic = isSurnamePublic;
        this.roles = roles;
    }
}
