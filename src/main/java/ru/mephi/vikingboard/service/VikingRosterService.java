package ru.mephi.vikingboard.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.mephi.vikingboard.model.Viking;
import ru.mephi.vikingboard.storage.VikingRosterStorage;

import java.util.List;

@Service
public class VikingRosterService {

    private final VikingFactory vikingFactory;
    private final VikingRosterStorage storage;

    public VikingRosterService(VikingFactory vikingFactory, VikingRosterStorage storage) {
        this.vikingFactory = vikingFactory;
        this.storage = storage;
    }

    public List<Viking> loadAll() {
        return storage.readAll();
    }

    public Viking loadOne(int id) {
        return storage.readOne(id)
                .orElseThrow(() -> missingViking(id));
    }

    public Viking createExact(Viking viking) {
        return storage.append(viking);
    }

    public Viking createRandom() {
        return createExact(vikingFactory.createRandomViking());
    }

    public Viking overwrite(int id, Viking newState) {
        return storage.rewrite(id, newState)
                .orElseThrow(() -> missingViking(id));
    }

    public void erase(int id) {
        if (!storage.erase(id)) {
            throw missingViking(id);
        }
    }

    private ResponseStatusException missingViking(int id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Викинг с id " + id + " не найден");
    }
}
