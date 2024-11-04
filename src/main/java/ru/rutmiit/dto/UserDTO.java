package ru.rutmiit.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class UserDTO {

    private String name;
    private String email;
    private String password;
    private String role;
    private String confirmPassword;

    public UserDTO(String name, String email, String password, String role, String confirmPassword) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.confirmPassword = confirmPassword;
    }

    @NotNull
    @NotEmpty
    @Length(min = 2, message = "Ошибка: введите минимум 2 символа")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull
    @NotEmpty
    @Length(min = 6, message = "Ошибка: введите минимум 6 символов")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NotNull
    @NotEmpty
    @Length(min = 6, message = "Ошибка: введите минимум 6 символов")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @NotNull
    @NotEmpty
    @Length(min = 6, message = "Ошибка: введите минимум 6 символов")
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
