package net.cltech.enterprisent.dao.interfaces.masters.test;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.xml.bind.DatatypeConverter;
import net.cltech.enterprisent.domain.masters.test.Container;
import net.cltech.enterprisent.domain.masters.test.Unit;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información de los
 * Recipientes.
 *
 * @version 1.0.0
 * @author enavas
 * @since 12/04/2017
 * @see Creación
 */
public interface ContainerDao {

    /**
     * Lista los recipientes desde la base de datos
     *
     * @return Lista de recipientes
     * @throws Exception Error en base de datos
     */
    default List<Container> list() throws Exception {
        try {
            return getJdbcTemplate().query(""
                    + "SELECT lab56.lab56c1"
                    + " ,lab56.lab56c2"
                    + " ,lab56.lab56c3"
                    + " ,lab56.lab56c4"
                    + " ,lab56.lab04c1"
                    + " ,lab56.lab56c5"
                    + " ,lab56.lab07c1"
                    + " ,lab04.lab04c2"
                    + " ,lab04.lab04c3"
                    + " ,lab04.lab04c4"
                    + " ,lab45.lab45c1"
                    + " ,lab45.lab45c2"
                    + " FROM lab56"
                    + " LEFT JOIN lab04 ON lab04.lab04c1 = lab56.lab04c1"
                    + " LEFT JOIN lab45 ON lab45.lab45c1 = lab56.lab45c1", (ResultSet rs, int i)
                    -> {
                Container container = new Container();
                container.setId(rs.getInt("lab56c1"));
                container.setName(rs.getString("lab56c2"));
                String Imabas64 = "";
                byte[] ImaBytes = rs.getBytes("lab56c3");
                if (ImaBytes != null) {
                    Imabas64 = Base64.getEncoder().encodeToString(ImaBytes);
                }
                container.setImage(Imabas64);
                container.setPriority(rs.getInt("lab56c5"));
                container.setState(rs.getBoolean("lab07c1"));
                container.setLastTransaction(rs.getTimestamp("lab56c4"));

                if (rs.getString("lab45c1") != null) {
                    container.setUnit(new Unit(rs.getInt("lab45c1")));
                    container.getUnit().setName(rs.getString("lab45c2"));
                }

                /*Usuario*/
                container.getUser().setId(rs.getInt("lab04c1"));
                container.getUser().setName(rs.getString("lab04c2"));
                container.getUser().setLastName(rs.getString("lab04c3"));
                container.getUser().setUserName(rs.getString("lab04c4"));
                return container;
            });
        } catch (EmptyResultDataAccessException ex) {
            return new ArrayList<>(0);
        }
    }

    /**
     * Registra un nuevo recipiente en la base de datos
     *
     * @param container Instancia con los datos del recipiente.
     *
     * @return Instancia con los datos del recipiente.
     * @throws Exception Error en base de datos
     */
    default Container create(Container container) throws Exception {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab56")
                .usingGeneratedKeyColumns("lab56c1");

        HashMap parameters = new HashMap();
        parameters.put("lab56c2", container.getName());
        byte[] imageByte = DatatypeConverter.parseBase64Binary(container.getImage());
        parameters.put("lab56c3", imageByte);
        parameters.put("lab56c4", timestamp);
        parameters.put("lab04c1", container.getUser().getId());
        parameters.put("lab56c5", container.getPriority());
        parameters.put("lab07c1", 1);
        parameters.put("lab45c1", container.getUnit() == null ? null : container.getUnit().getId());

        Number key = insert.executeAndReturnKey(parameters);
        container.setId(key.intValue());
        container.setLastTransaction(timestamp);

        return container;
    }

    /**
     * Obtener informacion de un recipiente en la base de datos
     *
     * @param id Id del resipiente consultado.
     * @param name Nombre del resipiente consultado.
     * @param priority Prioridad consultada.
     *
     * @return Instancia con los datos del recipiente.
     * @throws Exception Error en base de datos
     */
    default Container get(Integer id, String name, Integer priority) throws Exception {
        try {

            String query;

            query = ""
                    + "SELECT lab56.lab56c1"
                    + " ,lab56.lab56c2"
                    + " ,lab56.lab56c3"
                    + " ,lab56.lab56c4"
                    + " ,lab56.lab04c1"
                    + " ,lab56.lab56c5"
                    + " ,lab56.lab07c1"
                    + " ,lab04.lab04c2"
                    + " ,lab04.lab04c3"
                    + " ,lab04.lab04c4"
                    + " ,lab45.lab45c1"
                    + " ,lab45.lab45c2"
                    + " FROM lab56"
                    + " LEFT JOIN lab04 ON lab04.lab04c1 = lab56.lab04c1 "
                    + " LEFT JOIN lab45 ON lab45.lab45c1 = lab56.lab45c1 ";
            //where
            if (id != null) {
                query += " WHERE lab56.lab56c1 = ? ";
            }

            if (name != null) {
                query += " WHERE UPPER(lab56.lab56c2) = ? ";
            }

            if (priority != null) {
                query += " WHERE lab56.lab56c5 = ? ";
            }

            /*Order By, Group By y demas complementos de la consulta*/
            query = query + "";

            Object object = null;
            if (id != null) {
                object = id;
            }
            if (name != null) {
                object = name.toUpperCase();
            }
            if (priority != null) {
                object = priority;
            }

            return getJdbcTemplate().queryForObject(query,
                    new Object[]{
                        object
                    }, (ResultSet rs, int i)
                    -> {
                Container container = new Container();
                container.setId(rs.getInt("lab56c1"));
                container.setName(rs.getString("lab56c2"));
                String Imabas64 = "";
                byte[] ImaBytes = rs.getBytes("lab56c3");
                if (ImaBytes != null) {
                    Imabas64 = Base64.getEncoder().encodeToString(ImaBytes);
                }
                container.setImage(Imabas64);
                container.setPriority(rs.getInt("lab56c5"));
                container.setState(rs.getBoolean("lab07c1"));
                container.setLastTransaction(rs.getTimestamp("lab56c4"));

                /*Usuario*/
                container.getUser().setId(rs.getInt("lab04c1"));
                container.getUser().setName(rs.getString("lab04c2"));
                container.getUser().setLastName(rs.getString("lab04c3"));
                container.getUser().setUserName(rs.getString("lab04c4"));
                if (rs.getString("lab45c1") != null) {
                    container.setUnit(new Unit(rs.getInt("lab45c1")));
                    container.getUnit().setName(rs.getString("lab45c2"));
                }
                return container;
            });

        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    /**
     * Actualiza la informacion de un recipiente en la base de datos
     *
     * @param container Instancia con los datos del recipiente.
     *
     * @return
     *
     * @throws Exception Error en base de datos
     */
    default Container update(Container container) throws Exception {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        String query;
        query = ""
                + "UPDATE lab56 SET"
                + " lab56c2 = ?"
                + " ,lab56c3 = ?"
                + " ,lab56c4 = ?"
                + " ,lab04c1 = ?"
                + " ,lab56c5 = ?"
                + " ,lab07c1 = ?"
                + " ,lab45c1 = ?"
                + " WHERE lab56c1 = ? ";
        byte[] imageByte = DatatypeConverter.parseBase64Binary(container.getImage());
        getJdbcTemplate().update(query, container.getName(), imageByte, timestamp, container.getUser().getId(), container.getPriority(), container.isState() ? 1 : 0, container.getUnit() == null ? null : container.getUnit().getId(), container.getId());
        container.setLastTransaction(timestamp);
        return container;
    }

    /**
     * Actualiza la prioridad de un recipiente en la base de datos
     *
     * @param container Instancia con los datos del recipiente.
     *
     * @return
     *
     * @throws Exception Error en base de datos
     */
    default Container updatePriority(Container container) throws Exception {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        String query;
        query = ""
                + "UPDATE lab56 SET"
                + " lab56c4 = ?"
                + " ,lab04c1 = ?"
                + " ,lab56c5 = ?"
                + " WHERE lab56c1 = ? ";
        getJdbcTemplate().update(query, timestamp, container.getUser().getId(), container.getPriority(), container.getId());
        container.setLastTransaction(timestamp);
        return container;
    }

    

    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplate();
}
