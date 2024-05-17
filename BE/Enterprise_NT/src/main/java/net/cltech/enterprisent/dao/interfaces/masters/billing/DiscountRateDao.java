package net.cltech.enterprisent.dao.interfaces.masters.billing;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import net.cltech.enterprisent.domain.masters.billing.DiscountRate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información de los
 * tipos de descuentos
 *
 * @version 1.0.0
 * @author javila
 * @since 23/03/2021
 * @see Creación
 */
public interface DiscountRateDao
{

    public JdbcTemplate getJdbcTemplate();

    default DiscountRate create(DiscountRate discountRate) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        discountRate.setDateCreation(timestamp);
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab910")
                .usingGeneratedKeyColumns("lab910c1");

        HashMap parameters = new HashMap();
        parameters.put("lab910c2", discountRate.getCode().trim());
        parameters.put("lab910c3", discountRate.getName().trim());
        parameters.put("lab910c4", discountRate.getPercentage());
        parameters.put("lab07c1", 1);
        parameters.put("lab04c1", discountRate.getIdUserCreating());
        parameters.put("lab910c5", discountRate.getDateCreation());

        Number key = insert.executeAndReturnKey(parameters);
        discountRate.setId(key.intValue());

        return discountRate;
    }

    default DiscountRate update(DiscountRate discountRate) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        discountRate.setDateOfModification(timestamp);
        getJdbcTemplate().update("UPDATE lab910 SET lab910c2 = ?"
                + ", lab910c3 = ?"
                + ", lab910c4 = ?"
                + ", lab07c1 = ?"
                + ", lab04c1_2 = ?"
                + ", lab910c6 = ?"
                + " WHERE lab910c1 = ?",
                discountRate.getCode(),
                 discountRate.getName(),
                 discountRate.getPercentage(),
                 discountRate.isState() ? 1 : 0,
                 discountRate.getModifyingUserId(),
                 discountRate.getDateOfModification(),
                 discountRate.getId());

        return discountRate;
    }

    default List<DiscountRate> list() throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT  lab910c1")
                    .append(", lab910c2")
                    .append(", lab910c3")
                    .append(", lab910c4")
                    .append(", lab07c1")
                    .append(", lab04c1")
                    .append(", lab910c5")
                    .append(", lab04c1_2")
                    .append(", lab910c6")
                    .append(" FROM lab910");
            
            return getJdbcTemplate().query(query.toString(), (ResultSet rs, int i) -> 
            {
                DiscountRate discountRate = new DiscountRate();
                discountRate.setId(rs.getInt("lab910c1"));
                discountRate.setCode(rs.getString("lab910c2"));
                discountRate.setName(rs.getString("lab910c3"));
                discountRate.setPercentage(rs.getDouble("lab910c4"));
                discountRate.setState(rs.getBoolean("lab07c1"));
                discountRate.setIdUserCreating(rs.getInt("lab04c1"));
                discountRate.setDateCreation(rs.getTimestamp("lab910c5"));
                discountRate.setModifyingUserId(rs.getInt("lab04c1_2"));
                discountRate.setDateOfModification(rs.getTimestamp("lab910c6"));
                
                return discountRate;
            });
        }
        catch (Exception e)
        {
            return new ArrayList<>();
        }
    }

    default DiscountRate get(Integer id, String code, String name) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            StringBuilder where = new StringBuilder();
            query.append("SELECT  lab910c1")
                    .append(", lab910c2")
                    .append(", lab910c3")
                    .append(", lab910c4")
                    .append(", lab07c1")
                    .append(", lab04c1")
                    .append(", lab910c5")
                    .append(", lab04c1_2")
                    .append(", lab910c6")
                    .append(" FROM lab910");
            
            where.append(" WHERE");
            if(id!= null && id > 0)
            {
                where.append(" lab910c1 = ").append(id);
            }
            else if(code != null && !code.isEmpty())
            {
                where.append(" lab910c2 = '").append(code).append("'");
            }
            else if(name != null && !name.isEmpty())
            {
                where.append(" lab910c3 = '").append(name).append("'");
            }
            
            return getJdbcTemplate().queryForObject(query.toString() + where.toString(), (ResultSet rs, int i) -> 
            {
                DiscountRate discountRate = new DiscountRate();
                discountRate.setId(rs.getInt("lab910c1"));
                discountRate.setCode(rs.getString("lab910c2"));
                discountRate.setName(rs.getString("lab910c3"));
                discountRate.setPercentage(rs.getDouble("lab910c4"));
                discountRate.setState(rs.getBoolean("lab07c1"));
                discountRate.setIdUserCreating(rs.getInt("lab04c1"));
                discountRate.setDateCreation(rs.getTimestamp("lab910c5"));
                discountRate.setModifyingUserId(rs.getInt("lab04c1_2"));
                discountRate.setDateOfModification(rs.getTimestamp("lab910c6"));
                
                return discountRate;
            });
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
