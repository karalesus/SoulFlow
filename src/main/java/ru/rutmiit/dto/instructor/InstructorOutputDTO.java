package ru.rutmiit.dto.instructor;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class InstructorOutputDTO {

    private String id;
    private String name;
    private String certificate;
//    private BigDecimal rating; // ограничение 1..5. Вероятно убрать
    private String photoUrl;
    private boolean isDeleted;

    public InstructorOutputDTO(String id, String name, String certificate, String photoUrl, boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.certificate = certificate;
        this.photoUrl = photoUrl;
        this.isDeleted = isDeleted;
    }

    protected InstructorOutputDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @NotNull(message = "Имя не может быть пустым")
    @Length(min = 2, message = "Ошибка: введите минимум 2 символа")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull(message = "Сертификат не может быть пустым")
    @Length(min = 2, message = "Ошибка: введите минимум 2 символа")
    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    @NotEmpty(message = "Укажите ссылку на фотографию!")
    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return name;
    }
}
