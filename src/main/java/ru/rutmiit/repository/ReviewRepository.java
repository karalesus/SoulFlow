package ru.rutmiit.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.repository.NoRepositoryBean;

import java.math.BigDecimal;
import java.util.UUID;


public interface ReviewRepository {

    BigDecimal getRatingByInstructor(UUID instructorId);
}
