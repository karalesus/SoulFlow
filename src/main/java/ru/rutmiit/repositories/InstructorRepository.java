package ru.rutmiit.repositories;

import ru.rutmiit.models.Instructor;

import java.util.List;

public interface InstructorRepository {

    List<Instructor> findAllWithPagination(String searchTerm, int offset, int limit);

    long countInstructors(String searchTerm);

    List<Instructor> findInstructorsNotDeleted();
}
