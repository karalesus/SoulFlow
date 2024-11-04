package ru.rutmiit.repository.implementations;

import org.springframework.stereotype.Repository;
import ru.rutmiit.domain.Type;
import ru.rutmiit.repository.BaseRepository;
import ru.rutmiit.repository.TypeRepository;

import java.util.UUID;

@Repository
public class TypeRepositoryImpl extends BaseRepository<Type, UUID> implements TypeRepository {
    public TypeRepositoryImpl() {
        super(Type.class);
    }
}
