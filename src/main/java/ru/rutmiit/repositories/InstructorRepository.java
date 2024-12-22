package ru.rutmiit.repositories;

import org.springframework.data.domain.Pageable;
import ru.rutmiit.models.Instructor;

import java.util.List;

public interface InstructorRepository {

    List<Instructor> findAllInstructorsWithPagination(String searchTerm, Pageable pageable);

    long countInstructors(String searchTerm);

    List<Instructor> findInstructorsNotDeleted();
}
