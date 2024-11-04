package ru.rutmiit.repository.implementations;

import org.springframework.stereotype.Repository;
import ru.rutmiit.domain.Instructor;
import ru.rutmiit.domain.Status;
import ru.rutmiit.repository.BaseRepository;
import ru.rutmiit.repository.StatusRepository;

import java.util.UUID;

@Repository
public class StatusRepositoryImpl extends BaseRepository<Status, UUID> implements StatusRepository {

    public StatusRepositoryImpl() {
        super(Status.class);
    }
}
