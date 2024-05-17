
package net.cltech.enterprisent.dao.impl.postgresql.integration;

import java.sql.ResultSet;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import net.cltech.enterprisent.dao.interfaces.integration.IntegrationSklDao;
import org.springframework.dao.EmptyResultDataAccessException;

/**
 * Implementa el acceso a datos para examenes y demograficos en PostgreSQL
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 12/05/2020
 * @see Creación
 */
@Repository
public class IntegrationSklDaoPostgresSQL implements IntegrationSklDao {
    
       private JdbcTemplate jdbc;

    @Autowired
    public void setDataSource(@Qualifier("dataSource") DataSource dataSource)
    {
        jdbc = new JdbcTemplate(dataSource);
    }
    
    /**
     * Obtiene los comentarios de una orden o el diagnostico permanente de un
     * paciente.
     *
     * @param idPatient id del paciente   
     * @return Lista de comentarios.
    */
    @Override
    public String listCommentPatient(int idPatient) {
        try {
            return getJdbcTemplate().queryForObject(""
                    + "SELECT lab60c1, lab60c2, lab60c3, lab60c4, lab60c5, lab60c6, lab60.lab04c1, lab04c2, lab04c3, lab04c4 "
                    + "FROM lab60 "
                    + "INNER JOIN lab04 ON lab04.lab04c1 = lab60.lab04c1 "
                    + "WHERE lab60c2 = ? AND lab60c4 = 2 LIMIT 1",
                    (ResultSet rs, int numRow)
                    -> {
                return rs.getString("lab60c3");
            }, idPatient);
        } catch (EmptyResultDataAccessException ex) {
            return "";
        }
    }
    
    @Override
    public String listCommentOrder(Long idOrder) {
        try {
            return getJdbcTemplate().queryForObject(""
                    + "SELECT lab60c1, lab60c2, lab60c3, lab60c4, lab60c5, lab60c6, lab60.lab04c1, lab04c2, lab04c3, lab04c4 "
                    + "FROM lab60 "
                    + "INNER JOIN lab04 ON lab04.lab04c1 = lab60.lab04c1 "
                    + "WHERE lab60c2 = ? AND lab60c4 = 1 LIMIT 1",
                    (ResultSet rs, int numRow)
                    -> {
                return rs.getString("lab60c3");
            }, idOrder);
        } catch (Exception ex) {
            return "";
        }
    
    }

    @Override
    public JdbcTemplate getJdbcTemplate()
    {
        return jdbc;
    }

    
}
