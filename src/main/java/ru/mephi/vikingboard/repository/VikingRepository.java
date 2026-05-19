package ru.mephi.vikingboard.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.mephi.vikingboard.model.BeardStyle;
import ru.mephi.vikingboard.model.HairColor;
import ru.mephi.vikingboard.model.VikingEntity;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class VikingRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<VikingEntity> rowMapper = (rs, rowNum) -> new VikingEntity(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getInt("age"),
            rs.getInt("height_cm"),
            HairColor.valueOf(rs.getString("hair_color")),
            BeardStyle.valueOf(rs.getString("beard_style")),
            rs.getString("description")
    );

    public VikingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<VikingEntity> selectAll() {
        String sql = """
                select id, name, age, height_cm, hair_color, beard_style, description
                from vikings
                order by id
                """;
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<VikingEntity> selectOne(int id) {
        String sql = """
                select id, name, age, height_cm, hair_color, beard_style, description
                from vikings
                where id = ?
                """;
        List<VikingEntity> rows = jdbcTemplate.query(sql, rowMapper, id);
        return rows.stream().findFirst();
    }

    public Integer insert(VikingEntity viking) {
        String sql = """
                insert into vikings(name, age, height_cm, hair_color, beard_style, description)
                values (?, ?, ?, ?, ?, ?)
                """;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, viking.name());
            ps.setInt(2, viking.age());
            ps.setInt(3, viking.heightCm());
            ps.setString(4, viking.hairColor().name());
            ps.setString(5, viking.beardStyle().name());
            ps.setString(6, viking.description());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key == null) {
            throw new IllegalStateException("Не удалось получить id созданного викинга");
        }
        return key.intValue();
    }

    public int rewrite(VikingEntity viking) {
        String sql = """
                update vikings
                set name = ?, age = ?, height_cm = ?, hair_color = ?, beard_style = ?, description = ?
                where id = ?
                """;
        return jdbcTemplate.update(
                sql,
                viking.name(),
                viking.age(),
                viking.heightCm(),
                viking.hairColor().name(),
                viking.beardStyle().name(),
                viking.description(),
                viking.id()
        );
    }

    public int deleteOne(int id) {
        String sql = "delete from vikings where id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public void clear() {
        jdbcTemplate.update("delete from vikings");
    }
}
