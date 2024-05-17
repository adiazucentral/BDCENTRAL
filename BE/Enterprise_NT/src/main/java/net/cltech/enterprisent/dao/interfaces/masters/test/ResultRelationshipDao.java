package net.cltech.enterprisent.dao.interfaces.masters.test;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import net.cltech.enterprisent.domain.masters.interview.Question;
import net.cltech.enterprisent.domain.masters.test.Alarm;
import net.cltech.enterprisent.domain.masters.test.ResultRelationship;
import net.cltech.enterprisent.domain.masters.test.TestBasic;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información de
 * relaciones de resultados.
 *
 * @version 1.0.0
 * @author eacuna
 * @since 28/07/2017
 * @see Creación
 */
public interface ResultRelationshipDao
{

    /**
     * Lista relaciones de examenes desde la base de datos.
     *
     * @param alarm id de la alrma
     *
     * @return Lista de relacion de examenes.
     * @throws Exception Error en la base de datos.
     */
    default List<ResultRelationship> list(int alarm) throws Exception
    {
        try
        {
            return getJdbcTemplate().query(""
                    + "SELECT lab74c1, lab74c2, lab74c3, lab74c4, lab74c5"
                    + ",lab04.lab04c1, lab04c2, lab04c3, lab04c4 "
                    + ",lab39c1,lab39c2,lab39c3 "
                    + ",lab70c1,lab70c2,lab70c3 "
                    + ",lab73.lab73c1,lab73c2  "
                    + "FROM lab74 "
                    + "INNER JOIN lab73 ON lab73.lab73c1 = lab74.lab73c1 "
                    + "LEFT JOIN lab39 ON lab39c1 = lab74c6 AND lab74c1 = 1 "
                    + "LEFT JOIN lab70 ON lab70c1 = lab74c6 AND lab74c1 = 2 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab74.lab04c1 "
                    + "WHERE lab74.lab73c1 = ? ", (ResultSet rs, int i) ->
            {
                ResultRelationship relation = new ResultRelationship();
                relation.setType(rs.getInt("lab74c1"));
                relation.setOperator(rs.getString("lab74c2"));
                relation.setResult(rs.getString("lab74c3"));
                relation.setResult2(rs.getString("lab74c4"));
                //Examen 1
                if (rs.getString("lab39c1") != null)
                {
                    relation.setTest(new TestBasic(rs.getInt("lab39c1")));
                    relation.getTest().setCode(rs.getString("lab39c2"));
                    relation.getTest().setAbbr(rs.getString("lab39c3"));
                }
                if (rs.getString("lab70c1") != null)
                {
                    relation.setQuestion(new Question());
                    relation.getQuestion().setId(rs.getInt("lab70c1"));
                    relation.getQuestion().setName(rs.getString("lab70c2"));
                    relation.getQuestion().setQuestion(rs.getString("lab70c3"));
                }
                /*Usuario*/
                relation.getUser().setId(rs.getInt("lab04c1"));
                relation.getUser().setName(rs.getString("lab04c2"));
                relation.getUser().setLastName(rs.getString("lab04c3"));
                relation.getUser().setUserName(rs.getString("lab04c4"));

                relation.setLastTransaction(rs.getTimestamp("lab74c5"));
                return relation;
            }, alarm);
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Registra las reglas de una alarma.
     *
     * @param alarm id de la alarma
     *
     * @return Instancia con los datos del Relación.
     * @throws Exception Error en la base de datos.
     */
    default int create(Alarm alarm) throws Exception
    {
        List<HashMap> inserts = new ArrayList<>();
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab74");

        for (ResultRelationship rule : alarm.getRules())
        {
            HashMap parameters = new HashMap();
            parameters.put("lab73c1", alarm.getId());
            parameters.put("lab74c1", rule.getType());
            parameters.put("lab74c2", rule.getOperator().trim());
            parameters.put("lab74c3", rule.getResult());
            parameters.put("lab74c4", rule.getResult2());
            parameters.put("lab74c5", timestamp);
            parameters.put("lab74c6", rule.getType() == 1 ? rule.getTest().getId() : rule.getQuestion().getId());
            parameters.put("lab04c1", alarm.getUser().getId());
            inserts.add(parameters);
        }
        return insert.executeBatch(inserts.toArray(new HashMap[inserts.size()])).length;

    }

    /**
     * Elimina las reglas de una alarma.
     *
     * @param alarm Id de la alarma.
     *
     * @return Registros eliminados.
     * @throws Exception Error en la base de datos.
     */
    default int delete(int alarm) throws Exception
    {
        return getJdbcTemplate().update("DELETE FROM lab74 WHERE lab73c1 = ?", alarm);
    }

    /**
     * Lista relaciones de examenes por identificador de la prueba/entrevista.
     *
     * @param type tipo de alarma (1-prueba / 2-pregunta)
     * @param id identificador de la prueba o la pregunta
     *
     * @return Lista de relacion de examenes.
     * @throws Exception Error en la base de datos.
     */
    default List<ResultRelationship> listByTest(int type, int id) throws Exception
    {
        try
        {
            return getJdbcTemplate().query(""
                    + "SELECT lab74c1, lab74c2, lab74c3, lab74c4, lab74c5"
                    + ",lab04.lab04c1, lab04c2, lab04c3, lab04c4 "
                    + ",lab39c1,lab39c2,lab39c3 "
                    + ",lab70c1,lab70c2,lab70c3 "
                    + ",lab73.lab73c1,lab73c2  "
                    + "FROM lab74 "
                    + "INNER JOIN lab73 ON lab73.lab73c1 = lab74.lab73c1 "
                    + "LEFT JOIN lab39 ON lab39c1 = lab74c6 AND lab74c1 = 1 "
                    + "LEFT JOIN lab70 ON lab70c1 = lab74c6 AND lab74c1 = 2 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab74.lab04c1 "
                    + "WHERE lab74.lab74c1 = ? AND lab74.lab74c6 = ? ",
                    new Object[]
                    {
                        type, id
                    }, (ResultSet rs, int i) ->
            {
                ResultRelationship relation = new ResultRelationship();
                relation.setType(rs.getInt("lab74c1"));
                relation.setOperator(rs.getString("lab74c2"));
                relation.setResult(rs.getString("lab74c3"));
                relation.setResult2(rs.getString("lab74c4"));
                //Examen 1
                if (rs.getString("lab39c1") != null)
                {
                    relation.setTest(new TestBasic(rs.getInt("lab39c1")));
                    relation.getTest().setCode(rs.getString("lab39c2"));
                    relation.getTest().setAbbr(rs.getString("lab39c3"));
                }
                if (rs.getString("lab70c1") != null)
                {
                    relation.setQuestion(new Question());
                    relation.getQuestion().setId(rs.getInt("lab70c1"));
                    relation.getQuestion().setName(rs.getString("lab70c2"));
                    relation.getQuestion().setQuestion(rs.getString("lab70c3"));
                }
                /*Usuario*/
                relation.getUser().setId(rs.getInt("lab04c1"));
                relation.getUser().setName(rs.getString("lab04c2"));
                relation.getUser().setLastName(rs.getString("lab04c3"));
                relation.getUser().setUserName(rs.getString("lab04c4"));

                relation.setLastTransaction(rs.getTimestamp("lab74c5"));
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
