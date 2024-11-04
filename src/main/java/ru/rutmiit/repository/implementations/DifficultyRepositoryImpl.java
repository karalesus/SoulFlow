package ru.rutmiit.repository.implementations;

import org.springframework.stereotype.Repository;
import ru.rutmiit.domain.Difficulty;
import ru.rutmiit.repository.BaseRepository;

import java.util.UUID;

@Repository
public class DifficultyRepositoryImpl extends BaseRepository<Difficulty, UUID> {
    public DifficultyRepositoryImpl() {
        super(Difficulty.class);
    }
}
