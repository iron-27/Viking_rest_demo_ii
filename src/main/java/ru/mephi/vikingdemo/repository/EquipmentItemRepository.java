package ru.mephi.vikingdemo.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.mephi.vikingdemo.model.EquipmentItemEntity;

import java.util.List;

@Repository
public class EquipmentItemRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<EquipmentItemEntity> equipmentMapper = (rs, rowNum) -> new EquipmentItemEntity(
            rs.getInt("id"),
            rs.getInt("viking_id"),
            rs.getString("name"),
            rs.getString("quality")
    );

    public EquipmentItemRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<EquipmentItemEntity> selectByViking(int vikingId) {
        String sql = """
                select id, viking_id, name, quality
                from equipment_items
                where viking_id = ?
                order by id
                """;
        return jdbcTemplate.query(sql, equipmentMapper, vikingId);
    }

    public List<EquipmentItemEntity> selectAll() {
        String sql = """
                select id, viking_id, name, quality
                from equipment_items
                order by viking_id, id
                """;
        return jdbcTemplate.query(sql, equipmentMapper);
    }

    public void insert(EquipmentItemEntity item) {
        String sql = """
                insert into equipment_items(viking_id, name, quality)
                values (?, ?, ?)
                """;
        jdbcTemplate.update(sql, item.vikingId(), item.name(), item.quality());
    }

    public int deleteForViking(int vikingId) {
        String sql = "delete from equipment_items where viking_id = ?";
        return jdbcTemplate.update(sql, vikingId);
    }
}
