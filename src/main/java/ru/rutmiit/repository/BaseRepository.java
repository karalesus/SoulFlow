package ru.rutmiit.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public abstract class BaseRepository<Entity, UUID> {
    @PersistenceContext
    private EntityManager entityManager;

    private final Class<Entity> entityClass;

    public BaseRepository(Class<Entity> entityClass) {
        this.entityClass = entityClass;
    }

    @Transactional
    public Entity save(Entity entity) {
        entityManager.persist(entity);
        return entity;
    }

    public Optional<Entity> findById(UUID uuid) {
        return Optional.ofNullable(entityManager.find(entityClass, uuid));
    }

    public List<Entity> findAll() {
        return entityManager.createQuery("FROM " + entityClass.getName(), entityClass).getResultList();
    }

    public Optional<Entity> findByName(String name) {
        List<Entity> result = entityManager.createQuery("SELECT e FROM " + entityClass.getName() + " e WHERE e.name = :name", entityClass)
                .setParameter("name", name)
                .getResultList();

        return result.isEmpty() ? Optional.empty() : Optional.of(result.getFirst());
    }

    public boolean existsByName(String name) {
        String query = "SELECT COUNT(e) FROM " + entityClass.getName() + " e WHERE e.name = :name";
        Long count = entityManager.createQuery(query, Long.class)
                .setParameter("name", name)
                .getSingleResult();
        return count > 0;
    }

    @Transactional
    public Entity update(Entity entity) {
        entityManager.merge(entity);
        return entity;
    }
}

