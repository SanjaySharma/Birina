package com.birina.bsecure.notification;

public class TokenRegistrationRequest {
    String token;


    public TokenRegistrationRequest(String token) {
        this.token = token;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
