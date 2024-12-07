package ru.rutmiit.repositories.implementations;

import org.springframework.stereotype.Repository;
import ru.rutmiit.models.Type;
import ru.rutmiit.repositories.BaseRepository;
import ru.rutmiit.repositories.TypeRepository;

import java.util.UUID;

@Repository
public class TypeRepositoryImpl extends BaseRepository<Type, UUID> implements TypeRepository {
    public TypeRepositoryImpl() {
        super(Type.class);
    }
}
