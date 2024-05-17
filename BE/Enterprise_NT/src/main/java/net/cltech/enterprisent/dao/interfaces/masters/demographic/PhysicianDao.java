package net.cltech.enterprisent.dao.interfaces.masters.demographic;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import net.cltech.enterprisent.domain.masters.demographic.Physician;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información del
 * maestro.
 *
 * @version 1.0.0
 * @author eacuna
 * @since 24/05/2017
 * @see Creación
 */
public interface PhysicianDao
{

    /**
     * Obtiene la conexión a la base de datos
     *
     * @return jdbc Template de Sprint para acceso a datos
     */
    public JdbcTemplate getJdbcTemplate();

    /**
     * Lista médicos desde la base de datos.
     *
     * @return Lista de médicos.
     * @throws Exception Error en la base de datos.
     */
    default List<Physician> list() throws Exception
    {
        try
        {
            return getJdbcTemplate().query(""
                    + "SELECT lab19c1, lab19c2, lab19c3, lab19c4, lab19c5"
                    + ", lab19c6, lab19c7, lab19c8, lab19c9, lab19c10, lab19c11"
                    + ", lab19c12, lab19c13, lab19c14, lab19c15, lab19c16"
                    + ", lab19c17, lab19c18, lab19c19, lab19c20, lab19c21, lab19c22, lab19c23"
                    + ", lab19.lab09c1, lab09c2, lab19.lab07c1"
                    + ", lab19.lab04c1, lab04c2, lab04c3, lab04c4 "
                    + "FROM lab19 "
                    + "LEFT JOIN lab09 ON lab09.lab09c1 = lab19.lab09c1 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab19.lab04c1", (ResultSet rs, int i) ->
            {
                Physician physician = new Physician();
                physician.setId(rs.getInt("lab19c1"));
                physician.setName(rs.getString("lab19c2").trim());
                physician.setLastName(rs.getString("lab19c3"));
                physician.setPhone(rs.getString("lab19c4"));
                physician.setFax(rs.getString("lab19c5"));
                physician.setAddress1(rs.getString("lab19c6"));
                physician.setAddress2(rs.getString("lab19c7"));
                physician.setCity(rs.getString("lab19c8"));
                physician.setZipCode(rs.getString("lab19c9"));
                physician.setObs(rs.getString("lab19c10"));
                physician.setMmis(rs.getString("lab19c11"));
                physician.setLicense(rs.getString("lab19c12"));
                physician.setNpi(rs.getString("lab19c13"));
                physician.setProviderId(rs.getString("lab19c14"));
                physician.setInstitutional(rs.getInt("lab19c15") == 2);
                physician.setAdditionalReport(rs.getInt("lab19c16") == 1);
                physician.setUserName(rs.getString("lab19c17"));
                physician.setIdentification(rs.getString("lab19c19").trim());
                physician.setLastTransaction(rs.getTimestamp("lab19c20"));
                physician.setEmail(rs.getString("lab19c21"));
                physician.setCode(rs.getString("lab19c22"));
                physician.setActive(rs.getInt("lab07c1") == 1);
                physician.setState(rs.getString("lab19c23"));
                /*Especialidad*/
                physician.getSpecialty().setId(rs.getInt("lab09c1") == 0 ? null : rs.getInt("lab09c1"));
                physician.getSpecialty().setName(rs.getString("lab09c2"));
                /*Usuario*/
                physician.getUser().setId(rs.getInt("lab04c1"));
                physician.getUser().setName(rs.getString("lab04c2"));
                physician.getUser().setLastName(rs.getString("lab04c3"));
                physician.getUser().setUserName(rs.getString("lab04c4"));

                return physician;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Registra médicos en la base de datos.
     *
     *
     * @param newBean
     * @return Instancia con los datos de médicos.
     * @throws Exception Error en la base de datos.
     */
    default Physician create(Physician newBean) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab19")
                .usingGeneratedKeyColumns("lab19c1");

        HashMap parameters = new HashMap();
        parameters.put("lab19c2", newBean.getName().trim());
        parameters.put("lab19c3", newBean.getLastName());
        parameters.put("lab19c4", newBean.getPhone());
        parameters.put("lab19c5", newBean.getFax());
        parameters.put("lab19c6", newBean.getAddress1());
        parameters.put("lab19c7", newBean.getAddress2());
        parameters.put("lab19c8", newBean.getCity());
        parameters.put("lab19c9", newBean.getZipCode());
        parameters.put("lab19c10", newBean.getObs());
        parameters.put("lab19c11", newBean.getMmis());
        parameters.put("lab19c12", newBean.getLicense());
        parameters.put("lab19c13", newBean.getNpi());
        parameters.put("lab19c14", newBean.getProviderId());
        parameters.put("lab19c15", newBean.isInstitutional() ? 2 : 1);
        parameters.put("lab19c16", newBean.isAdditionalReport() ? 1 : 0);
        parameters.put("lab19c17", newBean.getEmail()); // username
        parameters.put("lab19c19", newBean.getIdentification().trim());
        parameters.put("lab19c20", timestamp);
        parameters.put("lab19c21", newBean.getEmail());
        parameters.put("lab19c22", newBean.getCode());
        parameters.put("lab19c23", newBean.getState());
        parameters.put("lab09c1", newBean.getSpecialty().getId());
        parameters.put("lab04c1", newBean.getUser().getId());
        parameters.put("lab07c1", 1);
        parameters.put("lab19c24", newBean.getAlternativeMails());

        Number key = insert.executeAndReturnKey(parameters);
        newBean.setId(key.intValue());
        newBean.setLastTransaction(timestamp);
        newBean.setActive(true);

        return newBean;
    }

    /**
     * Obtener información de médicos por identificación.
     *
     *
     * @param id id medico
     * @return Instancia con los datos de médicos.
     * @throws Exception Error en la base de datos.
     */
    default Physician filterById(Integer id) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = ""
                    + "SELECT lab19c1, lab19c2, lab19c3, lab19c4, lab19c5"
                    + ", lab19c6, lab19c7, lab19c8, lab19c9, lab19c10, lab19c11"
                    + ", lab19c12, lab19c13, lab19c14, lab19c15, lab19c16"
                    + ", lab19c17, lab19c18, lab19c19, lab19c20, lab19c21, lab19.lab09c1, lab09.lab09c2, lab19c22, lab19c23 "
                    + ", lab19.lab07c1"
                    + ", lab19.lab19c24 AS lab19c24"
                    + ", lab19.lab04c1, lab04c2, lab04c3, lab04c4 "
                    + "FROM lab19 "
                    + "INNER JOIN lab09 ON lab19.lab09c1 = lab09.lab09c1 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab19.lab04c1 "
                    + "WHERE lab19c1 = ? ";

            return getJdbcTemplate().queryForObject(query,
                    new Object[]
                    {
                        id
                    }, (ResultSet rs, int i) ->
            {
                Physician physician = new Physician();

                physician.setId(rs.getInt("lab19c1"));
                physician.setName(rs.getString("lab19c2"));
                physician.setLastName(rs.getString("lab19c3"));
                physician.setPhone(rs.getString("lab19c4"));
                physician.setFax(rs.getString("lab19c5"));
                physician.setAddress1(rs.getString("lab19c6"));
                physician.setAddress2(rs.getString("lab19c7"));
                physician.setCity(rs.getString("lab19c8"));
                physician.setZipCode(rs.getString("lab19c9"));
                physician.setObs(rs.getString("lab19c10"));
                physician.setMmis(rs.getString("lab19c11"));
                physician.setLicense(rs.getString("lab19c12"));
                physician.setNpi(rs.getString("lab19c13"));
                physician.setProviderId(rs.getString("lab19c14"));
                physician.setInstitutional(rs.getInt("lab19c15") == 2);
                physician.setAdditionalReport(rs.getInt("lab19c16") == 1);
                physician.setUserName(rs.getString("lab19c17"));
                physician.setIdentification(rs.getString("lab19c19").trim());
                physician.setLastTransaction(rs.getTimestamp("lab19c20"));
                physician.setEmail(rs.getString("lab19c21"));
                physician.setCode(rs.getString("lab19c22"));
                physician.setActive(rs.getInt("lab07c1") == 1);
                physician.setState(rs.getString("lab19c23"));
                physician.setAlternativeMails(rs.getString("lab19c24"));
                /*Especialidad*/
                physician.getSpecialty().setId(rs.getInt("lab09c1") == 0 ? null : rs.getInt("lab09c1"));
                physician.getSpecialty().setName(rs.getString("lab09c2"));
                /*Usuario*/
                physician.getUser().setId(rs.getInt("lab04c1"));
                physician.getUser().setName(rs.getString("lab04c2"));
                physician.getUser().setLastName(rs.getString("lab04c3"));
                physician.getUser().setUserName(rs.getString("lab04c4"));

                return physician;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Obtener información de médicos por id.
     *
     * @param identification No identificacion
     * @return Instancia con los datos de médicos.
     * @throws Exception Error en la base de datos.
     */
    default Physician filterByIdentification(String identification) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = ""
                    + "SELECT lab19c1, lab19c2, lab19c3, lab19c4, lab19c5"
                    + ", lab19c6, lab19c7, lab19c8, lab19c9, lab19c10, lab19c11"
                    + ", lab19c12, lab19c13, lab19c14, lab19c15, lab19c16, lab19c22,lab19c23 "
                    + ", lab19c17, lab19c18, lab19c19, lab19c20, lab19.lab07c1"
                    + ", lab19.lab04c1, lab04c2, lab04c3, lab04c4 "
                    + ", lab19.lab19c24 AS lab19c24"
                    + " FROM lab19 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab19.lab04c1 "
                    + "WHERE lower(lab19c19) = ? ";

            return getJdbcTemplate().queryForObject(query,
                    new Object[]
                    {
                        identification.toLowerCase().trim()
                    }, (ResultSet rs, int i) ->
            {
                Physician physician = new Physician();
                physician.setId(rs.getInt("lab19c1"));
                physician.setName(rs.getString("lab19c2"));
                physician.setLastName(rs.getString("lab19c3"));
                physician.setPhone(rs.getString("lab19c4"));
                physician.setFax(rs.getString("lab19c5"));
                physician.setAddress1(rs.getString("lab19c6"));
                physician.setAddress2(rs.getString("lab19c7"));
                physician.setCity(rs.getString("lab19c8"));
                physician.setZipCode(rs.getString("lab19c9"));
                physician.setObs(rs.getString("lab19c10"));
                physician.setMmis(rs.getString("lab19c11"));
                physician.setLicense(rs.getString("lab19c12"));
                physician.setNpi(rs.getString("lab19c13"));
                physician.setProviderId(rs.getString("lab19c14"));
                physician.setInstitutional(rs.getInt("lab19c15") == 2);
                physician.setAdditionalReport(rs.getInt("lab19c16") == 1);
                physician.setUserName(rs.getString("lab19c17"));
                physician.setIdentification(rs.getString("lab19c19").trim());
                physician.setLastTransaction(rs.getTimestamp("lab19c20"));
                physician.setEmail(rs.getString("lab19c21"));
                physician.setActive(rs.getInt("lab07c1") == 1);
                physician.setCode(rs.getString("lab19c22"));
                physician.setState(rs.getString("lab19c23"));
                physician.setAlternativeMails(rs.getString("lab19c24"));

                /*Especialidad*/
                physician.getSpecialty().setId(rs.getInt("lab09c1") == 0 ? null : rs.getInt("lab09c1"));

                /*Usuario*/
                physician.getUser().setId(rs.getInt("lab04c1"));
                physician.getUser().setName(rs.getString("lab04c2"));
                physician.getUser().setLastName(rs.getString("lab04c3"));
                physician.getUser().setUserName(rs.getString("lab04c4"));
                return physician;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Actualiza la información de médicos en la base de datos.
     *
     * @param update Instancia con los datos de médicos.
     *
     * @return Objeto de médicos modificada.
     * @throws Exception Error en la base de datos.
     */
    default Physician update(Physician update) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        getJdbcTemplate().update("UPDATE lab19 SET lab19c2 = ?, "
                + "lab19c3 = ?, "
                + "lab19c4 = ?, "
                + "lab19c5 = ?, "
                + "lab19c6 = ?, "
                + "lab19c7 = ?, "
                + "lab19c8 = ?, "
                + "lab19c9 = ?, "
                + "lab19c10 = ?, "
                + "lab19c11 = ?, "
                + "lab19c12 = ?, "
                + "lab19c13 = ?, "
                + "lab19c14 = ?, "
                + "lab19c15 = ?, "
                + "lab19c16 = ?, "
                + "lab19c17 = ?, "
                + "lab19c19 = ?, "
                + "lab19c20 = ?, "
                + "lab19c21 = ?, "
                + "lab09c1 = ?, "
                + "lab04c1 = ?, "
                + "lab07c1 = ?, "
                + "lab19c22 = ?, "
                + "lab19c23 = ?, "
                + "lab19c24 = ? "
                + "WHERE lab19c1 = ?",
                update.getName().trim(), 
                update.getLastName(), 
                update.getPhone(), 
                update.getFax(),
                update.getAddress1(), 
                update.getAddress2(), 
                update.getCity(), 
                update.getZipCode(), 
                update.getObs(),
                update.getMmis(), 
                update.getLicense(), 
                update.getNpi(), 
                update.getProviderId(), 
                update.isInstitutional() ? 2 : 1,
                update.isAdditionalReport() ? 1 : 0, 
                update.getEmail(), 
                update.getIdentification().trim(), 
                timestamp,
                update.getEmail(), 
                update.getSpecialty().getId(),
                update.getUser().getId(), 
                update.isActive() ? 1 : 0, 
                update.getCode().trim(), 
                update.getState(),
                update.getAlternativeMails(),
                update.getId());

        update.setLastTransaction(timestamp);

        return update;
    }

    /**
     * Actualiza el password del médico en la base de datos.
     *
     * @param update Instancia con los datos de médicos.
     *
     * @throws Exception Error en la base de datos.
     */
    default void updatePassword(Physician update) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        getJdbcTemplate().update("UPDATE lab19 SET lab19c18 = ?, lab19c20 = ?, lab04c1 = ?, lab07c1 = ? "
                + "WHERE lab19c1 = ?",
                update.getPassword(), timestamp, update.getUser().getId(), update.isActive() ? 1 : 0, update.getId());
    }
}
