package net.cltech.enterprisent.dao.interfaces.masters.microbiology;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.domain.masters.microbiology.Antibiotic;
import net.cltech.enterprisent.domain.masters.microbiology.AntibioticBySensitivity;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Representa los métodos de acceso a base de datos para la información del
 * maestro.
 *
 * @version 1.0.0
 * @author eacuna
 * @since 07/06/2017
 * @see Creación
 */
public interface AntibioticDao
{

    /**
     * Lista antibioticos desde la base de datos.
     *
     * @return Lista de antibioticos.
     * @throws Exception Error en la base de datos.
     */
    default List<Antibiotic> list() throws Exception
    {
        try
        {
            return getJdbcTemplate().query(""
                    + " SELECT lab79c1, lab79c2, lab79c3, lab79.lab07c1"
                    + " ,lab79.lab04c1, lab04c2, lab04c3, lab04c4 "
                    + "FROM lab79 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab79.lab04c1", (ResultSet rs, int i) ->
            {
                Antibiotic bean = new Antibiotic();
                bean.setId(rs.getInt("lab79c1"));
                bean.setName(rs.getString("lab79c2"));
                bean.setLastTransaction(rs.getTimestamp("lab79c3"));
                bean.setState(rs.getInt("lab07c1") == 1);

                /*Usuario*/
                bean.getUser().setId(rs.getInt("lab04c1"));
                bean.getUser().setName(rs.getString("lab04c2"));
                bean.getUser().setLastName(rs.getString("lab04c3"));
                bean.getUser().setUserName(rs.getString("lab04c4"));

                return bean;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Registra alarma en la base de datos.
     *
     * @param create Instancia con los datos de alarma.
     *
     * @return Instancia con los datos de alarma.
     * @throws Exception Error en la base de datos.
     */
    public Antibiotic create(Antibiotic create) throws Exception;

    /**
     * Actualiza la información de alarma en la base de datos.
     *
     * @param update Instancia con los datos de alarma.
     *
     * @return Objeto de alarma modificada.
     * @throws Exception Error en la base de datos.
     */
    public Antibiotic update(Antibiotic update) throws Exception;

    /**
     * Lista antibioticos asociados a un antibiograma.
     *
     * @param id
     *
     * @return Lista de antibioticos.
     * @throws Exception Error en la base de datos.
     */
    default List<AntibioticBySensitivity> filterBySensitivity(int id) throws Exception
    {
        try
        {
            return getJdbcTemplate().query(""
                    + "SELECT lab79.lab79c1, lab79c2, lab79c3, lab79.lab07c1, lab77c1, "
                    + "lab45c1, lab78c2 "
                    + "FROM lab79 "
                    + "LEFT JOIN lab78 ON lab78.lab79c1 = lab79.lab79c1 AND lab78.lab77c1 = ? "
                    + "WHERE lab79.lab07c1 = 1 ",
                    new Object[]
                    {
                        id
                    }, (ResultSet rs, int i) ->
            {
                AntibioticBySensitivity relation = new AntibioticBySensitivity();
                relation.getAntibiotic().setId(rs.getInt("lab79c1"));
                relation.getAntibiotic().setName(rs.getString("lab79c2"));
                relation.setSelected(rs.getString("lab77c1") != null);
                relation.setLine(rs.getString("lab78c2") == null ? 1 : rs.getInt("lab78c2"));
                relation.setUnit(rs.getString("lab45c1") == null ? null : rs.getInt("lab45c1"));

                return relation;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplate();

}
