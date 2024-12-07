package ru.rutmiit.dto.instructor;

public class InstructorsViewOutputDTO {

    private String id;

    private String name;
    private String certificate;
    //    private BigDecimal rating; // ограничение 1..5. Вероятно убрать
    private String photoUrl;

    public InstructorsViewOutputDTO(String id, String name, String certificate, String photoUrl) {
        this.id = id;
        this.name = name;
        this.certificate = certificate;
        this.photoUrl = photoUrl;
    }

    public InstructorsViewOutputDTO() {
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

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    @Override
    public String toString() {
        return name;
    }
}
