package ru.rutmiit.repositories.implementations;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.rutmiit.models.Instructor;
import ru.rutmiit.repositories.BaseRepository;
import ru.rutmiit.repositories.InstructorRepository;

import java.util.List;
import java.util.UUID;

@Repository
public class InstructorRepositoryImpl extends BaseRepository<Instructor, UUID> implements InstructorRepository {

    @PersistenceContext
    EntityManager entityManager;

    public InstructorRepositoryImpl() {
        super(Instructor.class);
    }

    @Override
    public List<Instructor> findAllWithPagination(String searchTerm, int offset, int limit) {
        var query = entityManager.createQuery(
                "SELECT i FROM Instructor i WHERE upper(i.name) LIKE :searchTerm ORDER BY i.name ASC", Instructor.class);
        query.setParameter("searchTerm", "%" + searchTerm + "%");
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    @Override
    public long countInstructors(String searchTerm) {
        var query = entityManager.createQuery(
                "SELECT COUNT(i) FROM Instructor i WHERE upper(i.name) LIKE :searchTerm ", Long.class);
        query.setParameter("searchTerm", "%" + searchTerm + "%");
        return query.getSingleResult();
    }

    @Override
    public List<Instructor> findInstructorsNotDeleted() {
        var query = entityManager.createQuery("SELECT i FROM Instructor i WHERE i.deleted = false", Instructor.class);
        return query.getResultList();
    }
}
