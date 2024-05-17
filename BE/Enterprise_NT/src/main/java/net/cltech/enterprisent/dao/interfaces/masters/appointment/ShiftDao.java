/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.interfaces.masters.appointment;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.cltech.enterprisent.domain.masters.appointment.Shift;
import net.cltech.enterprisent.tools.enums.LISEnum;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los metodos de acceso a base de datos para la informacion de las
 * jornadas
 *
 * @version 1.0.0
 * @author cmartin
 * @since 01/08/2018
 * @see Creación
 */
public interface ShiftDao
{

    /**
     * Obtiene la conexion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplate();

    /**
     * Lista las jornadas desde la base de datos.
     *
     * @return Lista de jornadas.
     * @throws Exception Error en la base de datos.
     */
    default List<Shift> list() throws Exception
    {
        try
        {
            return getJdbcTemplate().query(""
                    + "SELECT hmb09c1, hmb09c2, hmb09c3,hmb09c4,hmb09c5, hmb09c6, hmb03c1, hmb09.hmb07c1 "
                    + "FROM hmb09 ", (ResultSet rs, int i) ->
            {
                List<Integer> days;
                if (rs.getString("hmb09c3") != null && !rs.getString("hmb09c3").isEmpty())
                {
                    days = Stream.of(rs.getString("hmb09c3").split(","))
                            .map(day -> Integer.valueOf(day))
                            .collect(Collectors.toList());
                } else
                {
                    days = new ArrayList<>();
                }
                Shift bean = new Shift();
                bean.setId(rs.getInt("hmb09c1"));
                bean.setName(rs.getString("hmb09c2"));
                bean.setDays(days);
                bean.setInit(rs.getInt("hmb09c4"));
                bean.setEnd(rs.getInt("hmb09c5"));
                /*Usuario*/
                bean.getUser().setId(rs.getInt("hmb03c1"));

                bean.setLastTransaction(rs.getTimestamp("hmb09c6"));
                bean.setState(rs.getInt("hmb07c1") == 1);

                return bean;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Registra una nueva jornada en la base de datos.
     *
     * @param bean Instancia con los datos de la jornada.
     *
     * @return Instancia con los datos de la jornada.
     * @throws Exception Error en la base de datos.
     */
    default Shift create(Shift bean) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("hmb09")
                .usingGeneratedKeyColumns("hmb09c1");

        HashMap parameters = new HashMap();
        parameters.put("hmb09c2", bean.getName().trim());
        parameters.put("hmb09c3", bean.getDays().stream().map(d -> d.toString()).collect(Collectors.joining(",")));
        parameters.put("hmb09c4", bean.getInit());
        parameters.put("hmb09c5", bean.getEnd());
        parameters.put("hmb09c6", timestamp);
        parameters.put("hmb03c1", bean.getUser().getId());
        parameters.put("hmb07c1", 1);

        Number key = insert.executeAndReturnKey(parameters);
        bean.setId(key.intValue());
        bean.setLastTransaction(timestamp);

        return bean;
    }

    /**
     * Actualiza la información de una jornada en la base de datos.
     *
     * @param bean Instancia con los datos de la jornada.
     *
     * @return Objeto de la jornada modificado.
     * @throws Exception Error en la base de datos.
     */
    default Shift update(Shift bean) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        getJdbcTemplate().update("UPDATE hmb09 SET hmb09c2 = ?, hmb09c3 = ?,hmb09c4 = ?,hmb09c5 = ?, hmb09c6 = ?, hmb03c1 = ?, hmb07c1 = ? "
                + "WHERE hmb09c1 = ?",
                bean.getName().trim(),
                bean.getDays().stream().map(d -> d.toString()).collect(Collectors.joining(",")),
                bean.getInit(),
                bean.getEnd(),
                timestamp, bean.getUser().getId(), bean.isState() ? 1 : 0, bean.getId());

        bean.setLastTransaction(timestamp);

        return bean;
    }
    
     /**
     * Inserta las jornadas asociadas a un usuario
     *
     * @param shifts Jornadas
     * @param idUser Usuario
     * @return Registros afectados
     * @throws java.lang.Exception Error en la base de datos
     */
    default int insertShiftsbyBranch(List<Shift> shifts, Integer idBranch) throws Exception
    {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate()).withTableName("hmb10");
        List<HashMap<String, Object>> batch = new ArrayList<>(0);
        HashMap<String, Object> parameters = null;

        deleteShiftbyBranch(idBranch);

        for (Shift shift : shifts)
        {
            if (shift.isSelected())
            {
                parameters = new HashMap<>(0);
                parameters.put("lab05c1", idBranch); //Orden
                parameters.put("hmb09c1", shift.getId()); //Id Jornada
                parameters.put("hmb10c1", shift.getQuantity()); //Cantidad de Servicios
                batch.add(parameters);
            }
        }
        return insert.executeBatch(batch.toArray(new HashMap[0])).length;
    }
    
     /**
     * Elimina las jornadas asociadas a un usuario en la base de datos.
     *
     * @param idBranch
     *
     * @throws Exception Error en la base de datos.
     */
    default void deleteShiftbyBranch(Integer idBranch) throws Exception
    {
        getJdbcTemplate().update("DELETE FROM hmb10 WHERE lab05c1 = ?", idBranch);
    }
    
    /**
     * Lista las jornadas de un usuario desde la base de datos.
     *
     * @param idBranch
     * @return Lista de jornadas.
     */
    default List<Shift> listShiftbyBranch(Integer idBranch)
    {
        try
        {
            return getJdbcTemplate().query(""
                    + "SELECT hmb09.hmb09c1, hmb09c2, hmb09c3,hmb09c4,hmb09c5, hmb09.hmb07c1, hmb10.hmb10c1, hmb10.lab05c1 "
                    + "FROM hmb09 "
                    + "LEFT JOIN hmb10 ON hmb10.hmb09c1 = hmb09.hmb09c1 AND hmb10.lab05c1 = ? ",
                    new Object[]
                    {
                        idBranch
                    }, (ResultSet rs, int i) ->
            {
                List<Integer> days;
                if (rs.getString("hmb09c3") != null && !rs.getString("hmb09c3").isEmpty())
                {
                    days = Stream.of(rs.getString("hmb09c3").split(","))
                            .map(day -> Integer.valueOf(day))
                            .collect(Collectors.toList());
                } else
                {
                    days = new ArrayList<>();
                }
                Shift bean = new Shift();
                bean.setId(rs.getInt("hmb09c1"));
                bean.setName(rs.getString("hmb09c2"));
                bean.setDays(days);
                bean.setInit(rs.getInt("hmb09c4"));
                bean.setEnd(rs.getInt("hmb09c5"));
                bean.setState(rs.getInt("hmb07c1") == 1);

                bean.setSelected(rs.getString("lab05c1") != null);
                bean.setQuantity(rs.getInt("hmb10c1"));

                return bean;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }
    
    /**
     * Lista las jornadas con el agendamiento de una sede desde la base de
     * datos.
     *
     * @param branch
     * @param day
     * @param date Fecha del agendamiento.
     * @return Lista de jornadas.
     */
    default List<Shift> listShift(Integer branch, int date, int day)
    {
        try
        {
            final StringBuilder select = new StringBuilder();
            
            select.append("SELECT hmb09.hmb09c1 ")
                    .append(",hmb09c2")
                    .append(",hmb09c3")
                    .append(",hmb09c4")
                    .append(",hmb09c5")
                    .append(",hmb09.hmb07c1")
                    .append(",hmb10.hmb10c1")
                    .append(",hmb10.lab05c1")
                    .append(",((SELECT COUNT(*) FROM hmb12 WHERE hmb12.lab05c1 = hmb10.lab05c1 AND hmb12.hmb09c1 = hmb09.hmb09c1 AND hmb12c4 != ").append(LISEnum.AppointmentState.CANCELED.getValue())
                    .append(" AND hmb12.hmb12c2 = ").append(date).append(") + ")
                    .append(" (SELECT COUNT(*) FROM hmb13 WHERE hmb13.lab05c1 = hmb10.lab05c1 AND hmb13.hmb09c1 = hmb09.hmb09c1 AND hmb13.hmb13c1 = ").append(date).append(")) AS amount")
                    .append(" FROM hmb09 ")
                    .append(" INNER JOIN hmb10 ON hmb10.hmb09c1 = hmb09.hmb09c1 AND hmb10.lab05c1 = ").append(branch);
            
            if(day > 0 ) {
                select.append("WHERE hmb09c3 like '%").append(day).append("%'");
            }
    
            return getJdbcTemplate().query(select.toString(), (ResultSet rs, int i)
                    ->
            {
                List<Integer> days;
                if (rs.getString("hmb09c3") != null && !rs.getString("hmb09c3").isEmpty())
                {
                    days = Stream.of(rs.getString("hmb09c3").split(","))
                            .map(d -> Integer.valueOf(d))
                            .collect(Collectors.toList());
                } else
                {
                    days = new ArrayList<>();
                }
                Shift bean = new Shift();
                bean.setId(rs.getInt("hmb09c1"));
                bean.setName(rs.getString("hmb09c2"));
                bean.setDays(days);
                bean.setInit(rs.getInt("hmb09c4"));
                bean.setEnd(rs.getInt("hmb09c5"));
                bean.setState(rs.getInt("hmb07c1") == 1);
                bean.setSelected(rs.getString("lab05c1") != null);
                bean.setQuantity(rs.getInt("hmb10c1"));
                bean.setAmount(rs.getInt("amount"));

                return bean;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

}

