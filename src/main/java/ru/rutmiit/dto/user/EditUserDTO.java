package ru.rutmiit.dto.user;

import ru.rutmiit.utils.validation.user.MinLengthForNewPassword;

public class EditUserDTO {

    private String id;
    private String name;
    private String email;
    private String currentPassword;
    @MinLengthForNewPassword(value = 6, message = "Новый пароль должен содержать не менее 6 символов")
    private String newPassword;
    @MinLengthForNewPassword(value = 6, message = "Новый пароль должен содержать не менее 6 символов")
    private String confirmNewPassword;

    public EditUserDTO(String id, String name, String email, String currentPassword, String newPassword, String confirmNewPassword) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.confirmNewPassword = confirmNewPassword;
    }

    protected EditUserDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
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
