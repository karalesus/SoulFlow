package ru.rutmiit.service;

import org.springframework.beans.factory.annotation.Autowired;
import ru.rutmiit.repositories.implementations.DifficultyRepositoryImpl;

import java.util.List;

public interface DifficultyService {

    List<String> getAllDifficulties();
}
