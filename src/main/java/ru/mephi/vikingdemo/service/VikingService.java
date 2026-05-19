package ru.mephi.vikingdemo.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.repository.VikingStorage;

import java.util.List;

@Service
public class VikingService {

    private final VikingFactory vikingFactory;
    private final VikingStorage vikingStorage;

    public VikingService(VikingFactory vikingFactory, VikingStorage vikingStorage) {
        this.vikingFactory = vikingFactory;
        this.vikingStorage = vikingStorage;
    }

    public List<Viking> findAll() {
        return vikingStorage.findAll();
    }

    public Viking findById(int id) {
        return vikingStorage.findById(id)
                .orElseThrow(() -> missingViking(id));
    }

    public Viking createViking(Viking viking) {
        return vikingStorage.save(viking);
    }

    public Viking createRandomViking() {
        return createViking(vikingFactory.createRandomViking());
    }

    public Viking updateById(int id, Viking newState) {
        return vikingStorage.updateById(id, newState)
                .orElseThrow(() -> missingViking(id));
    }

    public void deleteById(int id) {
        if (!vikingStorage.deleteById(id)) {
            throw missingViking(id);
        }
    }

    private ResponseStatusException missingViking(int id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Викинг с id " + id + " не найден");
    }
}