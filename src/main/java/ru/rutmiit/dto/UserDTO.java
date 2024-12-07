package ru.rutmiit.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class UserDTO extends BaseDTO {
    private String name;
    private String email;
    private String password;

    public UserDTO(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    protected UserDTO() {
    }

    @NotNull
    @NotEmpty
    @Length(min = 2, message = "Ошибка: имя должно состоять из минимум 2 символов")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotBlank
    @Length(min = 6, message = "Ошибка: email должен состоять из минимум 6 символов")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NotBlank
    @Length(min = 6, message = "Ошибка: пароль должен состоять из минимум 6 символов")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
