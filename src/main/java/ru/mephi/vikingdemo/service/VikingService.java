package ru.mephi.vikingdemo.service;


import org.springframework.stereotype.Service;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.repository.VikingStorage;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class VikingService {

    private final VikingFactory vikingFactory;
    private final VikingStorage vikingStorage;

    @Autowired
    public VikingService(VikingFactory vikingFactory, VikingStorage vikingStorage) {
        this.vikingFactory = vikingFactory;
        this.vikingStorage = vikingStorage;
    }

    public List<Viking> findAll() {
        return vikingStorage.findAll();
    }

    public Viking createRandomViking() {
        Viking viking = vikingFactory.createRandomViking();
        return vikingStorage.save(viking);
    }
    public List<Viking> generateManyRandomVikings(int count) {
        List<Viking> newVikings = vikingFactory.createRandomVikings(count);
        newVikings.forEach(vikingStorage::save);
        return newVikings;
    }
    public void addViking(Viking viking) {
        vikingStorage.save(viking);
    }

    public void deleteViking(int id) {
        vikingStorage.deleteById(id);
    }

    public void updateViking(int id, Viking viking) {
        vikingStorage.update(id, viking);
    }
}
