package ru.rutmiit.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import ru.rutmiit.utils.validation.email.UniqueUserEmail;

public class UserDTO {
    private String name;
    @UniqueUserEmail
    private String email;
    private String password;
    private String confirmPassword;

    public UserDTO(String name, String email, String password, String confirmPassword) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
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

    @NotBlank(message = "Необходимо подтвердить пароль")
    @Length(min = 6, message = "Ошибка: пароль должен состоять из минимум 6 символов")
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
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
