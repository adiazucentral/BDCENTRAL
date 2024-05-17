package net.cltech.enterprisent.dao.impl.postgresql.masters.billing;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.masters.billing.PriceDao;
import net.cltech.enterprisent.domain.masters.billing.PriceAssignmentBatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

/**
 * Realiza la implementación del acceso a datos de información del maestro
 * para PostgreSQL
 *
 * @version 1.0.0
 * @author eacuna
 * @since 17/08/2017
 * @see Creación
 */
@Repository
public class PriceDaoPostgreSQL implements PriceDao
{

    private JdbcTemplate jdbc;

    @Autowired
    public void setDataSource(@Qualifier("dataSource") DataSource dataSource)
    {
        jdbc = new JdbcTemplate(dataSource);
    }

    @Override
    public JdbcTemplate getJdbcTemplate()
    {
        return jdbc;
    }
    
    @Override
    public int priceBatchCreate(List<PriceAssignmentBatch> list) throws Exception
    {
        List<HashMap> batchArray = new ArrayList<>();
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab120");

        list.stream().map((test) -> {
            HashMap parameters = new HashMap();
            parameters.put("lab120c1", test.getPrice());
            parameters.put("lab39c1", test.getIdTest());
            parameters.put("lab904c1", test.getIdRate());
            parameters.put("lab116c1", test.getIdValid());
            parameters.put("lab120c2", test.getPatientPercentage() == null ? 0 : test.getPatientPercentage());
            Timestamp timestamp = new Timestamp(new Date().getTime());
            parameters.put("lab120c3", timestamp);
            parameters.put("lab04c1", test.getUser().getId());
            return parameters;
        }).forEachOrdered((parameters) -> {
            batchArray.add(parameters);
        });

        insert.executeBatch(batchArray.toArray(new HashMap[list.size()]));
        return 1;
    }
}
