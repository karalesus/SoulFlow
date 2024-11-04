package ru.rutmiit.repository.implementations;

import org.springframework.stereotype.Repository;
import ru.rutmiit.domain.Instructor;
import ru.rutmiit.repository.BaseRepository;
import ru.rutmiit.repository.InstructorRepository;

import java.util.UUID;

@Repository
public class InstructorRepositoryImpl extends BaseRepository<Instructor, UUID> implements InstructorRepository {
    public InstructorRepositoryImpl() {
        super(Instructor.class);
    }
}
