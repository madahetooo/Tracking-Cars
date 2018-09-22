package com.carsecurity.carsecurity.model;

/**
 * Created by eslam on 01/03/2018.
 */

public class UserModel {
    private String Name;
    private String State;
    private String UserId;
    private String EmailAddress;
    private String PhoneNumber;
    private String Verification;
    private String Password;


    public String getPassword() {
        return Password;
    }

    public void setPassword(String passsword) {
        Password = passsword;
    }


    public UserModel() {


    }

    public UserModel(String Name, String State, String UserId, String EmailAddress, String PhoneNumber, String Verification, String Password) {
        Name = Name;
        State = State;
        UserId = UserId;
        EmailAddress = EmailAddress;
        PhoneNumber = PhoneNumber;
        Verification = Verification;
        Password = Password;

    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getEmailAddress() {
        return EmailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        EmailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getVerification() {
        return Verification;
    }

    public void setVerification(String verification) {
        Verification = verification;
    }
}
