package ru.mephi.vikingboard.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.mephi.vikingboard.model.EquipmentItem;
import ru.mephi.vikingboard.model.EquipmentItemEntity;
import ru.mephi.vikingboard.model.Viking;
import ru.mephi.vikingboard.model.VikingEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class VikingStorage {

    private final VikingRepository vikingRows;
    private final EquipmentItemRepository equipmentRows;
    private final VikingMapper mapper;

    public VikingStorage(
            VikingRepository vikingRows,
            EquipmentItemRepository equipmentRows,
            VikingMapper mapper
    ) {
        this.vikingRows = vikingRows;
        this.equipmentRows = equipmentRows;
        this.mapper = mapper;
    }

    public List<Viking> readAll() {
        Map<Integer, List<EquipmentItemEntity>> equipmentByViking = equipmentRows.selectAll()
                .stream()
                .collect(Collectors.groupingBy(EquipmentItemEntity::vikingId));

        return vikingRows.selectAll()
                .stream()
                .map(row -> mapper.toViking(row, equipmentByViking.getOrDefault(row.id(), List.of())))
                .toList();
    }

    public Optional<Viking> readOne(int id) {
        return vikingRows.selectOne(id)
                .map(row -> mapper.toViking(row, equipmentRows.selectByViking(id)));
    }

    @Transactional
    public Viking append(Viking viking) {
        Integer id = vikingRows.insert(mapper.toRow(viking));
        saveEquipment(id, viking.equipment());
        return readOne(id).orElseThrow();
    }

    @Transactional
    public Optional<Viking> rewrite(int id, Viking newState) {
        VikingEntity row = mapper.toRow(id, newState);
        int changedRows = vikingRows.rewrite(row);
        if (changedRows == 0) {
            return Optional.empty();
        }

        equipmentRows.deleteForViking(id);
        saveEquipment(id, newState.equipment());
        return readOne(id);
    }

    @Transactional
    public boolean erase(int id) {
        return vikingRows.deleteOne(id) > 0;
    }

    private void saveEquipment(Integer vikingId, List<EquipmentItem> equipment) {
        if (equipment == null || equipment.isEmpty()) {
            return;
        }
        equipment.stream()
                .map(item -> mapper.toEquipmentRow(vikingId, item))
                .forEach(equipmentRows::insert);
    }
}
