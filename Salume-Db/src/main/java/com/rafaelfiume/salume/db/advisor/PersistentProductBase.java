package com.rafaelfiume.salume.db.advisor;

import com.rafaelfiume.salume.domain.Product;
import com.rafaelfiume.salume.domain.Product.Reputation;
import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.List;

import static com.rafaelfiume.salume.db.advisor.PersistentProductBase.Queries.*;
import static java.util.Locale.ITALY;

@Repository
public class PersistentProductBase { // TODO RF 12/12/2015 implements ProductBase

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersistentProductBase(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Product> productsForMagic() {
        return query(MAGIC_PRODUCTS);
    }

    public List<Product> productsForHealthy() {
        return query(HEALTHY_PRODUCTS);
    }

    public List<Product> productsForExpert() {
        return query(TRADITIONAL_PRODUCTS);
    }

    public List<Product> productsForGourmet() {
        return query(GOURMETL_PRODUCTS);
    }

    private List<Product> query(String query) {
        return jdbcTemplate.query(query, new ProductRowMapper());
    }

    static final class Queries {

        private static final int RESULT_LIMIT = 3;  // TODO RF 22/10/2015 Pass the limit as an app configuration

        private static final String BASE_PROFILE_QUERY =
                "SELECT p.*, r.name as reputation_name " +
                        " FROM salumistore.products p, salumistore.reputation r " +
                        " WHERE p.reputation = r.id ";

        static final String MAGIC_PRODUCTS = BASE_PROFILE_QUERY + " ORDER BY price LIMIT " + RESULT_LIMIT;

        static final String HEALTHY_PRODUCTS = BASE_PROFILE_QUERY + " ORDER BY fat, price DESC LIMIT " + RESULT_LIMIT;

        static final String TRADITIONAL_PRODUCTS = BASE_PROFILE_QUERY + " AND r.name = 'Traditional' ORDER BY price LIMIT " + RESULT_LIMIT;

        static final String GOURMETL_PRODUCTS = BASE_PROFILE_QUERY + " ORDER BY price DESC LIMIT " + RESULT_LIMIT;
    }

    static final class ProductRowMapper implements RowMapper<Product> {

        /*
         * In case you're wondering what #format is doing here, it was decided to represent fat % as a String
         * in the domain layer for simplicity, and as real in the database for performance.
         */
        // TODO RF 22/10/2015 Pass the locale as an app configuration
        private static final NumberFormat FAT_FORMATTER = NumberFormat.getNumberInstance(ITALY);
        {
            FAT_FORMATTER.setMinimumFractionDigits(2);
        }

        @Override
        public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Product(
                    rs.getLong("id"),
                    rs.getString("name"),
                    // TODO RF 22/10/2015 Pass the currency code as an app configuration
                    Money.of(rs.getBigDecimal("price"), "EUR"),
                    FAT_FORMATTER.format(rs.getFloat("fat")),
                    Reputation.valueOf(rs.getString("reputation_name").toUpperCase()));
        }
    }

}
