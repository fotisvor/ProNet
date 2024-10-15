package com.bezkoder.springjwt.payload.request;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserUpdateRequest {

    private String EmailPublic ;
    private String PositionPublic ;
    private String CompanyPublic ;
    private String CVPublic ;

    private String PhonePublic;

    private String NamePublic;

    private String SurnamePublic;
    @NotBlank
    @Size(max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    private String name;
    private String surname;

    private String phone;
    private String position;
    private String password;
    private String company;

    private MultipartFile file; // For profile image
    private MultipartFile csvFile; // For CV



    // Getters and Setters

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getisPositionPublic() {
        return PositionPublic;
    }

    // Setter for isPositionPublic flag
    public void setPositionPublic(String positionPublic) {
        this.PositionPublic = positionPublic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPhone(){
        return phone;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public MultipartFile getCsvFile() {
        return csvFile;
    }

    public void setCsvFile(MultipartFile csvFile) {
        this.csvFile = csvFile;
    }

    // Getter for isEmailPublic flag, returns boolean-like value
    public String getisEmailPublic() {
        return EmailPublic ;
    }

    // Setter for isEmailPublic flag
    public void setEmailPublic(String phonePublic) {
        this.EmailPublic = phonePublic ;
    }

    public String getisPhonePublic() {
        return PhonePublic ;
    }

    // Setter for isEmailPublic flag
    public void setPhonePublic(String phonePublic) {
        this.PhonePublic = phonePublic ;
    }


    // Getter for isCompanyPublic flag, returns boolean-like value
    public String getisCompanyPublic() {
        return CompanyPublic;
    }

    // Setter for isCompanyPublic flag
    public void setCompanyPublic(String companyPublic) {
        this.CompanyPublic = companyPublic;
    }

    // Getter for isCVPublic flag, returns boolean-like value
    public String getisCvPublic() {
        return CVPublic ;
    }

    // Setter for isCVPublic flag
    public void setCVPublic(String cvPublic) {
        this.CVPublic = cvPublic;
    }

    public String getisNamePublic() {
        return NamePublic ;
    }

    // Setter for isCVPublic flag
    public void setNamePublic(String namePublic) {
        this.NamePublic = namePublic;
    }

    public String getisSurnamePublic() {
        return SurnamePublic ;
    }

    // Setter for isCVPublic flag
    public void setSurnamePublic(String surnamePublic) {
        this.SurnamePublic = surnamePublic;
    }
}
