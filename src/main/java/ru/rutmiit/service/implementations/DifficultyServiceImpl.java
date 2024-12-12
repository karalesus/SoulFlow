package ru.rutmiit.service.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.rutmiit.models.Difficulty;
import ru.rutmiit.repositories.implementations.DifficultyRepositoryImpl;
import ru.rutmiit.service.DifficultyService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DifficultyServiceImpl implements DifficultyService {

    private DifficultyRepositoryImpl difficultyRepository;
    @Autowired
    public void setDifficultyRepository(DifficultyRepositoryImpl difficultyRepository) {
        this.difficultyRepository = difficultyRepository;
    }

    @Override
    public List<String> getAllDifficultiesByName() {
        return difficultyRepository.findAll().stream().map(Difficulty::getName).collect(Collectors.toList());
    }
}
