package ru.rutmiit.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import ru.rutmiit.domain.Session;

import java.math.BigDecimal;
import java.util.List;

public class InstructorDTO {

    private String name;
    private String certificate;
//    private BigDecimal rating; // ограничение 1..5. Вероятно убрать
    private String photoUrl;

    public InstructorDTO(String name, String certificate, String photoUrl) {
        this.name = name;
        this.certificate = certificate;
        this.photoUrl = photoUrl;
    }

    protected InstructorDTO() {
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
