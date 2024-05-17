/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.interfaces.operation.common;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.operation.audit.AuditEvent;
import net.cltech.enterprisent.domain.operation.microbiology.CommentMicrobiology;
import net.cltech.enterprisent.domain.operation.orders.CommentOrder;
import static net.cltech.enterprisent.tools.Constants.ISOLATION_READ_UNCOMMITTED;
import net.cltech.enterprisent.tools.DateTools;
import net.cltech.enterprisent.tools.Tools;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información de
 * microbiologia.
 *
 * @version 1.0.0
 * @author cmartin
 * @since 28/02/2018
 * @see Creación
 */
public interface CommentDao
{

    /**
     * Obtiene la conexion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplate();

    /**
     * Obtiene los comentarios de una orden o el diagnostico permanente de un
     * paciente.
     *
     * @param idOrder Numero de la Orden.
     * @param idPatient Id del paciente.
     * @return Lista de comentarios.
     */
    default List<CommentOrder> listCommentOrder(Long idOrder, Integer idPatient)
    {
        try
        {
            String lab60 = "lab60";     
            if (idOrder != null)
            {
                Integer year = Tools.YearOfOrder(String.valueOf(idOrder));
                Integer currentYear = DateTools.dateToNumberYear(new Date());
                lab60 = year.equals(currentYear) ? "lab60" : "lab60_" + year;      
            }

            String query = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT lab60c1, "
                    + "lab60c2, "
                    + "lab60c3, "
                    + "lab60c4, "
                    + "lab60c5, "
                    + "lab60c6, "
                    + "lab60.lab04c1, "
                    + "lab04c2, "
                    + "lab04c3, "
                    + "lab04c4 "
                    + "FROM  " + lab60 + " as lab60 "
                    + "INNER JOIN lab04 ON lab04.lab04c1 = lab60.lab04c1 ";
            String where = "";

            List<Object> parameters = new ArrayList<>();
            if (idOrder != null)
            {
                where = "WHERE lab60c2 = ? AND lab60c4 = 1 ";
                parameters.add(idOrder);
            }

            if (idPatient != null)
            {
                where = "WHERE lab60c2 = ? AND lab60c4 = 2 ";
                parameters.add(idPatient);
            }

            return getJdbcTemplate().query(query + where, parameters.toArray(), (ResultSet rs, int i) ->
            {
                CommentOrder comment = new CommentOrder();
                comment.setId(rs.getInt("lab60c1"));
                comment.setIdRecord(rs.getLong("lab60c2"));
                comment.setComment(rs.getString("lab60c3"));
                comment.setType(rs.getShort("lab60c4"));
                comment.setPrint(rs.getInt("lab60c6") == 1);
                /*Usuario*/
                comment.getUser().setId(rs.getInt("lab04c1"));
                comment.getUser().setName(rs.getString("lab04c2"));
                comment.getUser().setLastName(rs.getString("lab04c3"));
                comment.getUser().setUserName(rs.getString("lab04c4"));

                comment.setLastTransaction(rs.getTimestamp("lab60c5"));
                return comment;
            });

        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Obtiene los comentarios de microbiologia de una orden y muestra o examen.
     *
     *
     * @param idOrder Numero de la Orden.
     * @param idTest Id del examen.
     * @param idSample Id de la muestra
     * @return Lista de Comentarios.
     */
    default List<CommentMicrobiology> listCommentMicrobiology(long idOrder, Integer idTest, Integer idSample)
    {
        try
        {
            String query = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT lab22c1, lab39c1, lab24c1, lab211c1, lab211c2, lab211c3, lab211.lab04c1, lab04c2, lab04c3, lab04c4, lab04c16 "
                    + "FROM lab211 "
                    + "INNER JOIN lab04 ON lab04.lab04c1 = lab211.lab04c1 ";
            String where = ""
                    + "WHERE lab22c1 = ? ";

            List<Object> parameters = new ArrayList<>();
            parameters.add(idOrder);
            if (idSample != null)
            {
                where += " AND lab24c1 = ? ";
                parameters.add(idSample);
            }

            if (idTest != null)
            {
                where += " AND lab39c1 = ? ";
                parameters.add(idTest);
            }

            return getJdbcTemplate().query(query + where, parameters.toArray(), (ResultSet rs, int i) ->
            {
                CommentMicrobiology comment = new CommentMicrobiology();
                comment.setId(rs.getInt("lab211c1"));
                comment.setOrder(rs.getLong("lab22c1"));
                comment.setIdTest(rs.getInt("lab39c1"));
                comment.setIdSample(rs.getInt("lab24c1"));
                comment.setComment(rs.getString("lab211c2"));
                /*Usuario*/
                comment.getUser().setId(rs.getInt("lab04c1"));
                comment.getUser().setName(rs.getString("lab04c2"));
                comment.getUser().setLastName(rs.getString("lab04c3"));
                comment.getUser().setUserName(rs.getString("lab04c4"));
                comment.getUser().setPhoto(rs.getString("lab04c16"));

                comment.setLastTransaction(rs.getTimestamp("lab211c3"));
                return comment;
            });

        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Inserta los comentarios de la orden o el paciente
     *
     * @param commentsOrder Comentario
     * @return Registros afectados
     * @throws Exception Error en la base de datos.
     */
    default int insertCommentOrder(List<CommentOrder> commentsOrder) throws Exception
    {
       String lab60  = "lab60";
        if ( commentsOrder.get(0).getType() == 1)
        {
            Integer year = Tools.YearOfOrder(String.valueOf(commentsOrder.get(0).getIdRecord()));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            lab60 = year.equals(currentYear) ? "lab60" : "lab60_" + year;      
        }

        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate()).withTableName(lab60).usingGeneratedKeyColumns("lab60c1");

        for (CommentOrder comment : commentsOrder)
        {
            HashMap parameters = new HashMap();
            parameters.put("lab60c2", comment.getIdRecord()); //Orden o paciente
            parameters.put("lab60c3", comment.getComment()); //Comentario
            parameters.put("lab60c4", comment.getType()); //Tipo: 1 -> Orden, 2 -> Paciente
            parameters.put("lab60c5", timestamp);
            parameters.put("lab60c6", comment.isPrint() ? 1 : 0);
            parameters.put("lab04c1", comment.getUser().getId());

            Number key = insert.executeAndReturnKey(parameters);
            comment.setId(key.intValue());
            comment.setLastTransaction(timestamp);
        }

        return commentsOrder.size();
    }

    /**
     * Inserta los comentarios de microbiologia
     *
     * @param commentsMicrobiology Comentario
     * @return Registros afectados
     * @throws Exception Error en la base de datos.
     */
    default int insertCommentMicrobiology(List<CommentMicrobiology> commentsMicrobiology) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate()).withTableName("lab211").usingGeneratedKeyColumns("lab211c1");

        for (CommentMicrobiology comment : commentsMicrobiology)
        {
            HashMap parameters = new HashMap();
            parameters.put("lab22c1", comment.getOrder()); //Orden
            parameters.put("lab39c1", comment.getIdTest()); //Examen
            parameters.put("lab24c1", comment.getIdSample()); //Muestra
            parameters.put("lab211c2", comment.getComment()); //Comentario
            parameters.put("lab211c3", timestamp);
            parameters.put("lab04c1", comment.getUser().getId());

            Number key = insert.executeAndReturnKey(parameters);
            comment.setId(key.intValue());
            comment.setLastTransaction(timestamp);
        }

        return commentsMicrobiology.size();
    }

    /**
     * Actualiza comentarios de la orden.
     *
     * @param commentsOrder lista de comentarios.
     *
     * @return Registros afectados.
     * @throws Exception Error en la base de datos.
     */
    default int updateCommentOrder(List<CommentOrder> commentsOrder) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        List<Object[]> parameters = new ArrayList<>();

        String query = "UPDATE lab60 SET lab60c3 = ?, lab60c5 = ?, lab60c6 = ? WHERE lab60c1 = ?";

        commentsOrder.forEach((comment) ->
        {
            parameters.add(new Object[]
            {
                comment.getComment(), timestamp, comment.isPrint() ? 1 : 0, comment.getId()
            });
        });

        return getJdbcTemplate().batchUpdate(query, parameters).length;
    }

    /**
     * Actualiza comentarios de microbiologia.
     *
     * @param commentsMicrobiology lista de comentarios.
     *
     * @return Registros afectados.
     * @throws Exception Error en la base de datos.
     */
    default int updateCommentMicrobiology(List<CommentMicrobiology> commentsMicrobiology) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        List<Object[]> parameters = new ArrayList<>();

        String query = "UPDATE lab211 SET lab211c2 = ?, lab211c3 = ? WHERE lab211c1 = ?";

        commentsMicrobiology.forEach((comment) ->
        {
            parameters.add(new Object[]
            {
                comment.getComment(), timestamp, comment.getId()
            });
        });

        return getJdbcTemplate().batchUpdate(query, parameters).length;
    }

    /**
     * Elimina comentarios de la orden.
     *
     * @param commentsOrder lista de comentarios.
     *
     * @return Registros afectados.
     * @throws Exception Error en la base de datos.
     */
    default int deleteCommentOrder(List<CommentOrder> commentsOrder) throws Exception
    {
        List<Object[]> parameters = new ArrayList<>();

        String query = "DELETE FROM lab60 WHERE lab60c1 = ?";

        commentsOrder.forEach((comment) ->
        {
            parameters.add(new Object[]
            {
                comment.getId()
            });
        });

        return getJdbcTemplate().batchUpdate(query, parameters).length;
    }

    /**
     * Elimina comentarios de microbiologia.
     *
     * @param commentsMicrobiology lista de comentarios.
     *
     * @return Registros afectados.
     * @throws Exception Error en la base de datos.
     */
    default int deleteCommentMicrobiology(List<CommentMicrobiology> commentsMicrobiology) throws Exception
    {
        List<Object[]> parameters = new ArrayList<>();

        String query = "DELETE FROM lab211 WHERE lab211c1 = ?";

        commentsMicrobiology.forEach((comment) ->
        {
            parameters.add(new Object[]
            {
                comment.getId()
            });
        });

        return getJdbcTemplate().batchUpdate(query, parameters).length;
    }

    /**
     * Consulta la trazabilidad del comentario de microbiologia
     *
     * @param order Numero de orden.
     * @param test Id del examen.
     * @return Trazabilidad del comentario de microbiologia.
     */
    default List<AuditEvent> listTrackingMicrobiologyComment(Long order, Integer test)
    {
        try
        {
            String lab03  = "lab03";
            
            Integer year = Tools.YearOfOrder(String.valueOf(order));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            lab03 = year.equals(currentYear) ? "lab03" : "lab03_" + year;      
            
            
            String query = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT lab03.lab03c2, lab03.lab03c4, lab03c5, "
                    + "lab04.lab04c1, lab04.lab04c4, lab04.lab04c16 "
                    + "FROM " + lab03 + " as lab03 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab03.lab04c1 "
                    + "WHERE lab03.lab22c1 = ? AND lab03c1 = ? AND lab03c3 = 'MC'";

            return getJdbcTemplate().query(query,
                    new Object[]
                    {
                        order, test
                    }, (ResultSet rs, int i) ->
            {
                AuditEvent event = new AuditEvent();
                event.setDate(rs.getTimestamp("lab03c5"));
                event.setUser(new AuthorizedUser(rs.getInt("lab04c1")));
                event.getUser().setUserName(rs.getString("lab04c4"));
                event.getUser().setPhoto(rs.getString("lab04c16"));
                event.setCurrent(rs.getString("lab03c4"));
                event.setAction(rs.getString("lab03c2"));
                return event;
            });

        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Consulta la trazabilidad del comentario de la orden o diagnostico
     * permanente
     *
     * @param idRecord Numero de orden o id del paciente.
     * @param type Indica si la consulta es de la orden o el paciente.
     * @return Trazabilidad del comentario de la orden.
     */
    default List<AuditEvent> listTrackingOrderComment(Long idRecord, Integer type)
    {
        try
        {
            String lab03  = "lab03";
            if ( type == 1)
            {
                Integer year = Tools.YearOfOrder(String.valueOf(idRecord));
                Integer currentYear = DateTools.dateToNumberYear(new Date());
                lab03 = year.equals(currentYear) ? "lab03" : "lab03_" + year;      
            }

                  
            String query = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT lab03.lab03c2, lab03.lab03c4, lab03c5, "
                    + "lab04.lab04c1, lab04.lab04c4, lab04.lab04c16 "
                    + "FROM " + lab03 + " as lab03 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab03.lab04c1 ";
            if (type == 1)
            {
                query += "WHERE lab03.lab22c1 = ? AND lab03c3 = 'OC'";
            } else
            {
                query += "WHERE lab03.lab03c1 = ? AND lab03c3 = 'PD'";
            }

            return getJdbcTemplate().query(query,
                    new Object[]
                    {
                        idRecord
                    }, (ResultSet rs, int i) ->
            {
                AuditEvent event = new AuditEvent();
                event.setDate(rs.getTimestamp("lab03c5"));
                event.setUser(new AuthorizedUser(rs.getInt("lab04c1")));
                event.getUser().setUserName(rs.getString("lab04c4"));
                event.getUser().setPhoto(rs.getString("lab04c16"));
                event.setCurrent(rs.getString("lab03c4"));
                event.setAction(rs.getString("lab03c2"));
                return event;
            });

        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>();
        }
    }
    
    /**
     * Obtiene los comentarios de una orden o el diagnostico permanente de un
     * paciente.
     *
     * @param idOrder Numero de la Orden.
     * @return Lista de comentarios.
     */
    default List<CommentOrder> getlistCommentOrder(Long idOrder)
    {
        try
        {
            String lab60 = "lab60";     
            if (idOrder != null)
            {
                Integer year = Tools.YearOfOrder(String.valueOf(idOrder));
                Integer currentYear = DateTools.dateToNumberYear(new Date());
                lab60 = year.equals(currentYear) ? "lab60" : "lab60_" + year;      
            }

            String query = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT lab60c1, "
                    + "lab60c2, "
                    + "lab60c3, "
                    + "lab60c4, "
                    + "lab60c5, "
                    + "lab60c6, "
                    + "lab60.lab04c1, "
                    + "lab04c2, "
                    + "lab04c3, "
                    + "lab04c4 "
                    + "FROM  " + lab60 + " as lab60 "
                    + "INNER JOIN lab04 ON lab04.lab04c1 = lab60.lab04c1 ";
            String where = "";

            List<Object> parameters = new ArrayList<>();
            if (idOrder != null)
            {
                where = "WHERE lab60c2 = ? AND lab60c4 = 1 ";
                parameters.add(idOrder);
            }

            return getJdbcTemplate().query(query + where, parameters.toArray(), (ResultSet rs, int i) ->
            {
                CommentOrder comment = new CommentOrder();
                comment.setId(rs.getInt("lab60c1"));
                comment.setIdRecord(rs.getLong("lab60c2"));
                comment.setComment(rs.getString("lab60c3"));
                comment.setType(rs.getShort("lab60c4"));
                comment.setPrint(rs.getInt("lab60c6") == 1);
                /*Usuario*/
                comment.getUser().setId(rs.getInt("lab04c1"));
                comment.getUser().setName(rs.getString("lab04c2"));
                comment.getUser().setLastName(rs.getString("lab04c3"));
                comment.getUser().setUserName(rs.getString("lab04c4"));

                comment.setLastTransaction(rs.getTimestamp("lab60c5"));
                return comment;
            });

        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>();
        }
    }

}
