package net.cltech.enterprisent.dao.interfaces.masters.tracking;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import net.cltech.enterprisent.domain.masters.demographic.Branch;
import net.cltech.enterprisent.domain.masters.tracking.Refrigerator;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información del
 * maestro.
 *
 * @version 1.0.0
 * @author eacuna
 * @since 08/06/2017
 * @see Creación
 */
public interface RefrigeratorDao
{

    /**
     * Lista neveras desde la base de datos.
     *
     * @return Lista de neveras.
     * @throws Exception Error en la base de datos.
     */
    default List<Refrigerator> list() throws Exception
    {
        try
        {
            return getConnection().query(""
                    + " SELECT lab31c1, lab31c2, lab31c3, lab31.lab07c1"
                    + " ,lab31.lab04c1, lab04c2, lab04c3, lab04c4 "
                    + " , lab05.lab05c1, lab05c2 "
                    + "FROM lab31 "
                    + "INNER JOIN lab05 ON lab05.lab05c1 = lab31.lab05c1 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab31.lab04c1", (ResultSet rs, int i) ->
            {
                Refrigerator bean = new Refrigerator();
                bean.setId(rs.getInt("lab31c1"));
                bean.setName(rs.getString("lab31c2"));
                bean.setLastTransaction(rs.getTimestamp("lab31c3"));
                bean.setState(rs.getInt("lab07c1") == 1);

                /*Usuario*/
                bean.getUser().setId(rs.getInt("lab04c1"));
                bean.getUser().setName(rs.getString("lab04c2"));
                bean.getUser().setLastName(rs.getString("lab04c3"));
                bean.getUser().setUserName(rs.getString("lab04c4"));
                Branch branch = new Branch(rs.getInt("lab05c1"));
                branch.setName(rs.getString("lab05c2"));
                bean.setBranch(branch);
                return bean;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Registra nevera en la base de datos.
     *
     * @param create Instancia con los datos de nevera.
     *
     * @return Instancia con los datos de nevera.
     * @throws Exception Error en la base de datos.
     */
    default Refrigerator create(Refrigerator create) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getConnection())
                .withTableName("lab31")
                .usingGeneratedKeyColumns("lab31c1");
        
        HashMap parameters = new HashMap();
        parameters.put("lab31c2", create.getName().trim());
        parameters.put("lab31c3", timestamp);
        parameters.put("lab04c1", create.getUser().getId());
        parameters.put("lab07c1", 1);
        parameters.put("lab05c1", create.getBranch().getId());
        
        Number key = insert.executeAndReturnKey(parameters);
        create.setId(key.intValue());
        create.setLastTransaction(timestamp);
        create.setState(true);
        
        return create;
    }

    /**
     * Actualiza la información de nevera en la base de datos.
     *
     * @param update Instancia con los datos de nevera.
     *
     * @return Objeto de nevera modificada.
     * @throws Exception Error en la base de datos.
     */
    default Refrigerator update(Refrigerator update) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        
        getConnection().update("UPDATE lab31 SET lab31c2 = ?, lab31c3 = ?,  lab04c1 = ?, lab07c1 = ?, lab05c1 = ? "
                + "WHERE lab31c1 = ?",
                update.getName().trim(), timestamp, update.getUser().getId(), update.isState() ? 1 : 0, update.getBranch().getId(), update.getId());
        
        update.setLastTransaction(timestamp);
        
        return update;
    }
    
    public JdbcTemplate getConnection();
    
}
