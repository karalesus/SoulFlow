package ru.rutmiit.service.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.rutmiit.models.Type;
import ru.rutmiit.repositories.implementations.TypeRepositoryImpl;
import ru.rutmiit.service.TypeService;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class TypeServiceImpl implements TypeService {

    private TypeRepositoryImpl typeRepository;

    @Autowired
    public void setTypeRepository(TypeRepositoryImpl typeRepository) {
        this.typeRepository = typeRepository;
    }


    @Override
    public List<String> getAllTypesByName() {
        return typeRepository.findAll().stream().map(Type::getName).collect(Collectors.toList());
    }
}
