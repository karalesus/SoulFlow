package ru.rutmiit.dto.Session;

import jakarta.validation.constraints.*;
import ru.rutmiit.models.Difficulty;
import ru.rutmiit.models.Instructor;
import ru.rutmiit.models.Type;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class SessionInputDTO {

    private String name;
    private int duration;
    private String description;
    private LocalDateTime dateTime;
    private int maxCapacity;
    private BigDecimal price;
    private String difficultyName;
    private String typeName;
    private String instructor;

    public SessionInputDTO(String name, int duration, String description, LocalDateTime dateTime, int maxCapacity, BigDecimal price, String difficulty, String type, String instructor) {
        this.name = name;
        this.duration = duration;
        this.description = description;
        this.dateTime = dateTime;
        this.maxCapacity = maxCapacity;
        this.price = price;
        this.difficultyName = difficulty;
        this.typeName = type;
        this.instructor = instructor;
    }

    protected SessionInputDTO() {
    }

    @NotBlank(message = "Имя не может быть пустым")
    @Size(min = 5, max = 127, message = "Имя должно содержать минимум 5 символов")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull(message = "Длительность не может быть пустой")
    @Min(value = 20, message = "Длительность занятия должна быть от 20 минут")
    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @NotBlank(message = "Описание не может быть пустым")
    @Size(min = 5, max = 255, message = "Описание должно содержать не менее 5 и не более 255 символов")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    @NotNull(message = "Дата и время не могут быть пустыми")
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
    @Min(value = 1, message = "На занятие может прийти минимум 1 человек")
    @Max(value = 40, message = "На занятие могут прийти не больше 40 людей")
    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    @NotNull(message = "Цена не может быть пустой")
    @DecimalMin(value = "0.00", message = "Цена не может быть отрицательной")
    @Digits(integer=4, fraction=2)
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @NotBlank(message = "Выберите сложность занятия!")
    public String getDifficulty() {
        return difficultyName;
    }

    public void setDifficulty(String difficultyName) {
        this.difficultyName = difficultyName;
    }

    @NotBlank(message = "Выберите тип занятия!")
    public String getType() {
        return typeName;
    }

    public void setType(String typeName) {
        this.typeName = typeName;
    }

    @NotBlank(message = "Занятие не может проводиться без инструктора!")
    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    @Override
    public String toString() {
        return "SessionDTO{" +
                "name='" + name + '\'' +
                ", duration=" + duration +
                ", description='" + description + '\'' +
                ", dateTime=" + dateTime +
                ", maxCapacity=" + maxCapacity +
                ", price=" + price +
                ", difficultyName='" + difficultyName + '\'' +
                ", typeName='" + typeName + '\'' +
                ", instructor='" + instructor + '\'' +
                '}';
    }
}
