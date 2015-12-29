package com.rafaelfiume.salume.db.advisor;

import com.rafaelfiume.salume.domain.FatConverter;
import com.rafaelfiume.salume.domain.MoneyDealer;
import com.rafaelfiume.salume.domain.Product;
import com.rafaelfiume.salume.domain.Reputation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.rafaelfiume.salume.db.advisor.PersistentProductBase.Inserts.INSERT_INTO_PRODUCTS;
import static com.rafaelfiume.salume.db.advisor.PersistentProductBase.Inserts.namedParametersFor;
import static com.rafaelfiume.salume.db.advisor.PersistentProductBase.Queries.*;
import static com.rafaelfiume.salume.domain.Reputation.NORMAL;
import static java.lang.String.format;

@Repository
public class PersistentProductBase { // TODO RF 12/12/2015 implements ProductBase

    private static final Logger LOG = LoggerFactory.getLogger(PersistentProductBase.class);

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private final MoneyDealer moneyDealer;
    private final FatConverter fatConverter;

    @Autowired
    public PersistentProductBase(DataSource dataSource, MoneyDealer moneyDealer, FatConverter fatConverter) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.moneyDealer = moneyDealer;
        this.fatConverter = fatConverter;
    }

    public List<Product> productsForMagic()   { return query(MAGIC_PRODUCTS); }
    public List<Product> productsForHealthy() { return query(HEALTHY_PRODUCTS); }
    public List<Product> productsForExpert()  { return query(TRADITIONAL_PRODUCTS); }
    public List<Product> productsForGourmet() { return query(GOURMET_PRODUCTS); }

    private List<Product> query(String query) {
        return jdbcTemplate.query(query, new ProductRowMapper(moneyDealer, fatConverter));
    }

    public void add(Product product) {
        final int insertions = jdbcTemplate.update(
                INSERT_INTO_PRODUCTS,
                namedParametersFor(product, moneyDealer, fatConverter)
        );
        logIfInDebugMode(format("Added %s products", insertions));
    }

    private void logIfInDebugMode(String message) {
        LOG.debug(message);
    }

    static final class Inserts {
        static final String INSERT_INTO_PRODUCTS =
                "INSERT INTO salumistore.products (name, price, fat, reputation) VALUES (:name, :price, :fat, :reputationId)";

        static Map<String, Object> namedParametersFor(Product p, MoneyDealer moneyDealer, FatConverter fatConverter) {
            return new HashMap<String, Object>() {{
                put("name", p.getName());
                put("price", moneyDealer.numberFrom(p.getPrice()));
                put("fat", fatConverter.theFatOf(p.getFatPercentage()));
                put("reputationId", (p.getReputation() == NORMAL) ? 2 : 1); // TODO RF 16/12 Blerghhh But focusing on making the test readable first
            }};
        }
    }

    static final class Queries {

        private static final String BASE_PROFILE_QUERY =
                "SELECT p.*, r.name as reputation_name " +
                        " FROM salumistore.products p, salumistore.reputation r " +
                        " WHERE p.reputation = r.id ";

        static final String MAGIC_PRODUCTS = BASE_PROFILE_QUERY + " ORDER BY price ";
        static final String HEALTHY_PRODUCTS = BASE_PROFILE_QUERY + " ORDER BY fat, price DESC ";
        static final String TRADITIONAL_PRODUCTS = BASE_PROFILE_QUERY + " AND r.name = 'Traditional' ORDER BY price ";
        static final String GOURMET_PRODUCTS = BASE_PROFILE_QUERY + " ORDER BY price DESC ";
    }

    static final class ProductRowMapper implements RowMapper<Product> {

        private final MoneyDealer moneyDealer;
        private final FatConverter fatConverter;

        public ProductRowMapper(MoneyDealer moneyDealer, FatConverter fatConverter) {
            this.moneyDealer = moneyDealer;
            this.fatConverter = fatConverter;
        }

        @Override
        public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Product(
                    rs.getLong("id"),
                    rs.getString("name"),
                    moneyDealer.theAmountOf(rs.getBigDecimal("price")),
                    fatConverter.format(rs.getFloat("fat")),
                    Reputation.valueOf(rs.getString("reputation_name").toUpperCase()));
        }
    }

}
