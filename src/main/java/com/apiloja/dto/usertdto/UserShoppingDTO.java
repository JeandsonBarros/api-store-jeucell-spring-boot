package com.apiloja.dto.usertdto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


public class UserShoppingDTO {

    @NotBlank(message = "mandatory")
    private String name;

    @NotBlank(message = "mandatory")
    private String birthDate;

    @NotNull
    private Long cpf;

    @NotBlank(message = "mandatory")
    private String email;

    @NotBlank(message = "mandatory")
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public Long getCpf() {
        return cpf;
    }

    public void setCpf(Long cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
