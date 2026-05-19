package ru.mephi.vikingdemo.repository;

import org.springframework.stereotype.Component;
import ru.mephi.vikingdemo.model.EquipmentItem;
import ru.mephi.vikingdemo.model.EquipmentItemEntity;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.model.VikingEntity;

import java.util.List;

@Component
public class VikingMapper {

    public VikingEntity toRow(Viking viking) {
        return new VikingEntity(
                viking.id(),
                viking.name(),
                viking.age(),
                viking.heightCm(),
                viking.hairColor(),
                viking.beardStyle(),
                ""
        );
    }

    public VikingEntity toRow(int id, Viking viking) {
        return new VikingEntity(
                id,
                viking.name(),
                viking.age(),
                viking.heightCm(),
                viking.hairColor(),
                viking.beardStyle(),
                ""
        );
    }

    public EquipmentItemEntity toEquipmentRow(Integer vikingId, EquipmentItem item) {
        return new EquipmentItemEntity(null, vikingId, item.name(), item.quality());
    }

    public EquipmentItem toEquipmentItem(EquipmentItemEntity row) {
        return new EquipmentItem(row.name(), row.quality());
    }

    public Viking toViking(VikingEntity row, List<EquipmentItemEntity> equipmentRows) {
        List<EquipmentItem> equipment = equipmentRows.stream()
                .map(this::toEquipmentItem)
                .toList();

        return new Viking(
                row.id(),
                row.name(),
                row.age(),
                row.heightCm(),
                row.hairColor(),
                row.beardStyle(),
                equipment
        );
    }
}
