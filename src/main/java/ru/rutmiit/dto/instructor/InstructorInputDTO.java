package ru.rutmiit.dto.instructor;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class InstructorInputDTO {

    private String name;
    private String certificate;
    //    private BigDecimal rating; // ограничение 1..5. Вероятно убрать
    private String photoUrl;

    public InstructorInputDTO(String name, String certificate, String photoUrl) {
        this.name = name;
        this.certificate = certificate;
        this.photoUrl = photoUrl;
    }

    protected InstructorInputDTO() {
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
}
