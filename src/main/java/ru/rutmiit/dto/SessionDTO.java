package ru.rutmiit.dto;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Range;
import ru.rutmiit.domain.Difficulty;
import ru.rutmiit.domain.Instructor;
import ru.rutmiit.domain.Type;

import java.time.LocalDateTime;

public class SessionDTO {

    private String name;
    private int duration;
    private String description;
    private LocalDateTime dateTime;
    private int maxCapacity;
    private int price;
    private String difficultyName;
    private String typeName;
    private String instructor;

    public SessionDTO(String name, int duration, String description, LocalDateTime dateTime, int maxCapacity, int price, String difficulty, String type, String instructor) {
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

    protected SessionDTO() {
    }
    @NotEmpty(message = "Имя не может быть пустым")
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

    @NotEmpty(message = "Описание не может быть пустым")
    @Size(min = 5, max = 250, message = "Описание должно содержать минимум 5 символов")
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
    @Min(value = 0, message = "Цена не может быть отрицательной")
    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @NotNull(message = "Выберите сложность занятия!")
    public String getDifficulty() {
        return difficultyName;
    }

    public void setDifficulty(String difficultyName) {
        this.difficultyName = difficultyName;
    }

//    @NotNull(message = "Выберите тип занятия!")
    public String getType() {
        return typeName;
    }

    public void setType(String typeName) {
        this.typeName = typeName;
    }

    @NotEmpty(message = "Занятие не может проводиться без инструктора!")
    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }
}
