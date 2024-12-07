package ru.rutmiit.repositories.implementations;

import org.springframework.stereotype.Repository;
import ru.rutmiit.models.Status;
import ru.rutmiit.repositories.BaseRepository;
import ru.rutmiit.repositories.StatusRepository;

import java.util.UUID;

@Repository
public class StatusRepositoryImpl extends BaseRepository<Status, UUID> implements StatusRepository {

    public StatusRepositoryImpl() {
        super(Status.class);
    }
}
