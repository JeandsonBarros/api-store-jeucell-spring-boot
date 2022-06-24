package com.apiloja.model.user;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ChangePassword {

    @Id
    private int code;

    public Long getExpiration() {
        return expiration;
    }

    public void setExpiration(Long expiration) {
        this.expiration = expiration;
    }

    private Long expiration;

    private String email;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
