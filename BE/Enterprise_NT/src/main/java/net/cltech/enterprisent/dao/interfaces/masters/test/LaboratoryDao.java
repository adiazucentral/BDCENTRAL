package net.cltech.enterprisent.dao.interfaces.masters.test;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.domain.masters.test.Laboratory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Representa los métodos de acceso a base de datos para la información del
 * maestro.
 *
 * @version 1.0.0
 * @author eacuna
 * @since 18/05/2017
 * @see Creación
 */
public interface LaboratoryDao
{

    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc Template de Sprint para acceso a datos
     */
    public JdbcTemplate getJdbcTemplate();

    /**
     * Lista servicios desde la base de datos.
     *
     * @return Lista de servicios.
     * @throws Exception Error en la base de datos.
     */
    public List<Laboratory> list() throws Exception;

    /**
     * Registra servicio en la base de datos.
     *
     * @param create Instancia con los datos de servicio.
     *
     * @return Instancia con los datos de servicio.
     * @throws Exception Error en la base de datos.
     */
    public Laboratory create(Laboratory create) throws Exception;

    /**
     * Obtener información de servicio por nombre.
     *
     * @param name Nombre de servicio a ser consultada.
     *
     * @return Instancia con los datos de servicio.
     * @throws Exception Error en la base de datos.
     */
    public Laboratory filterByName(String name) throws Exception;

    /**
     * Obtener información de servicio por nombre.
     *
     * @param id id de servicio.
     *
     * @return Instancia con los datos de servicio.
     * @throws Exception Error en la base de datos.
     */
    public Laboratory filterById(Integer id) throws Exception;

    /**
     * Actualiza la información de servicio en la base de datos.
     *
     * @param update Instancia con los datos de servicio.
     *
     * @return Objeto de servicio modificada.
     * @throws Exception Error en la base de datos.
     */
    public Laboratory update(Laboratory update) throws Exception;

    /**
     * Lista de laboratorios de procesamiento
     * 
     * @return Lista de laboratorios de procesamiento
     */
    default List<Laboratory> listLaboratorysProcessing()
    {
        try
        {
            String query = ""
                    + "SELECT "
                    + "         lab40.lab40c1 " //id del laboratorio
                    + "       , lab40.lab40c2 " //Codigo
                    + "       , lab40.lab40c3 " //Nombre
                    + "       , lab40.lab40c10 " //url del laboratorio
                    + "       , lab40.lab07c1 " //Si esta activo
                    + "       , lab40.lab05c1 AS winery " //Bodega (Sede)
                    + " FROM    lab40 " //Resultados
                    + " WHERE lab40.lab07c1 = 1 AND (lab40.lab40c10 != '' OR lab40.lab40c10 IS NOT NULL) ";
            return getJdbcTemplate().query(query, (ResultSet rs, int i) ->
            {
                Laboratory bean = new Laboratory();
                bean.setId(rs.getInt("lab40c1"));
                bean.setCode(rs.getInt("lab40c2"));
                bean.setName(rs.getString("lab40c3"));
                bean.setUrl(rs.getString("lab40c10") == null ? "" : rs.getString("lab40c10"));
                bean.setState(rs.getInt("lab07c1") == 1);
                bean.setWinery(rs.getInt("winery"));
                return bean;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

}
