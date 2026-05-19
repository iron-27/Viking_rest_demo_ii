package ru.mephi.vikingdemo.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.mephi.vikingdemo.model.EquipmentItem;
import ru.mephi.vikingdemo.model.EquipmentItemEntity;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.model.VikingEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class VikingStorage {

    private final VikingRepository vikingRepository;
    private final EquipmentItemRepository equipmentItemRepository;
    private final VikingMapper vikingMapper;

    public VikingStorage(
            VikingRepository vikingRepository,
            EquipmentItemRepository equipmentItemRepository,
            VikingMapper vikingMapper
    ) {
        this.vikingRepository = vikingRepository;
        this.equipmentItemRepository = equipmentItemRepository;
        this.vikingMapper = vikingMapper;
    }

    public List<Viking> findAll() {
        Map<Integer, List<EquipmentItemEntity>> equipmentByViking = equipmentItemRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(EquipmentItemEntity::vikingId));

        return vikingRepository.findAll()
                .stream()
                .map(entity -> vikingMapper.toViking(entity, equipmentByViking.getOrDefault(entity.id(), List.of())))
                .toList();
    }

    public Optional<Viking> findById(int id) {
        return vikingRepository.findById(id)
                .map(entity -> vikingMapper.toViking(entity, equipmentItemRepository.findByVikingId(id)));
    }

    @Transactional
    public Viking save(Viking viking) {
        Integer id = vikingRepository.save(vikingMapper.toVikingEntity(viking));
        saveEquipment(id, viking.equipment());
        return findById(id).orElseThrow();
    }

    @Transactional
    public Optional<Viking> updateById(int id, Viking newState) {
        VikingEntity entity = vikingMapper.toVikingEntity(id, newState);
        int changedRows = vikingRepository.updateById(entity);
        if (changedRows == 0) {
            return Optional.empty();
        }
        equipmentItemRepository.deleteByVikingId(id);
        saveEquipment(id, newState.equipment());
        return findById(id);
    }

    @Transactional
    public boolean deleteById(int id) {
        return vikingRepository.deleteById(id) > 0;
    }

    private void saveEquipment(Integer vikingId, List<EquipmentItem> equipment) {
        if (equipment == null || equipment.isEmpty()) {
            return;
        }
        equipment.stream()
                .map(item -> vikingMapper.toEquipmentItemEntity(vikingId, item))
                .forEach(equipmentItemRepository::save);
    }
}