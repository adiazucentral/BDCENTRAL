package net.cltech.enterprisent.dao.interfaces.common;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.domain.masters.common.Item;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Representa los métodos de acceso a base de datos para la información de las
 * Listas.
 *
 * @version 1.0.0
 * @author cmartin
 * @since 19/04/2017
 * @see Creación
 */
public interface ListDao
{

    /**
     * Lista items desde la base de datos.
     *
     * @param id Id del padre de los items a ser consultados.
     *
     * @return Lista.
     * @throws Exception Error en la base de datos.
     */
    default List<Item> list(int id) throws Exception
    {
        try
        {
            return getJdbcTemplate().query(""
                    + "SELECT lab80c1, lab80c2, lab80c3, lab80c4, lab80c5, lab80c6 "
                    + "FROM lab80 "
                    + "WHERE lab80c2 = ?",
                    new Object[]
                    {
                        id
                    }, (ResultSet rs, int i) ->
            {
                Item item = new Item();

                item.setId(rs.getInt("lab80c1"));
                item.setIdParent(rs.getInt("lab80c2"));
                item.setCode(rs.getString("lab80c3"));
                item.setEsCo(rs.getString("lab80c4"));
                item.setEnUsa(rs.getString("lab80c5"));
                item.setAdditional(rs.getString("lab80c6"));
                return item;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Actualiza valor adicional de las listas
     *
     * @param item item a actualizar
     *
     * @return item actualizado
     * @throws Exception
     */
    default Item update(Item item) throws Exception
    {
        getJdbcTemplate().update("UPDATE lab80 SET lab80c6 = ? "
                + "WHERE lab80c1 = ?",
                item.getAdditional(), item.getId());

        return item;
    }

    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplate();
}
