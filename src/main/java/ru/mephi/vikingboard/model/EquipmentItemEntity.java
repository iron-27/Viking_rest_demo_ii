package ru.mephi.vikingboard.model;

public record EquipmentItemEntity(
        Integer id,
        Integer vikingId,
        String name,
        String quality
) {
}
