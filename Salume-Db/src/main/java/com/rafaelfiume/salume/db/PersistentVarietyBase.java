package com.rafaelfiume.salume.db;

import com.rafaelfiume.salume.domain.Variety;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

import static com.rafaelfiume.salume.db.PersistentVarietyBase.Inserts.INSERT_INTO_VARIETY;
import static com.rafaelfiume.salume.db.PersistentVarietyBase.Inserts.namedParametersFor;

@Repository
public class PersistentVarietyBase { // TODO RF 29/12/2015 implements VarietyRepository

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public PersistentVarietyBase(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public void add(Variety variety) {
        jdbcTemplate.update(INSERT_INTO_VARIETY, namedParametersFor(variety));
    }

    static final class Inserts {
        static final String INSERT_INTO_VARIETY =
                "INSERT INTO salumistore.variety (id, name, image_link) VALUES (:id, :name, :imageLink)";

        static Map<String, Object> namedParametersFor(Variety v) {
            return new HashMap<String, Object>() {{
                put("id", v.getId());
                put("name", v.getName());
                put("imageLink", v.getImageLink());
            }};
        }
    }

}
