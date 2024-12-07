package ru.rutmiit.repositories.implementations;

import org.springframework.stereotype.Repository;
import ru.rutmiit.models.Difficulty;
import ru.rutmiit.repositories.BaseRepository;

import java.util.UUID;

@Repository
public class DifficultyRepositoryImpl extends BaseRepository<Difficulty, UUID> {
    public DifficultyRepositoryImpl() {
        super(Difficulty.class);
    }
}
