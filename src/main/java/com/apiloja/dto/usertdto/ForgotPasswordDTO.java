package com.apiloja.dto.usertdto;

import com.sun.istack.NotNull;

import javax.validation.constraints.NotBlank;

public class ForgotPasswordDTO {

    @NotBlank
    private String email;
    @NotBlank
    private String newPassword;
    @NotBlank
    private String confirmNewPassword;
    @NotNull
    private int code;

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

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    public void setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
    }
}
