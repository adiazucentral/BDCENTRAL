package net.cltech.enterprisent.dao.interfaces.operation.microbiology;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import net.cltech.enterprisent.dao.interfaces.operation.common.CommentDao;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.microbiology.MediaCulture;
import net.cltech.enterprisent.domain.masters.microbiology.MicrobiologyDestination;
import net.cltech.enterprisent.domain.masters.microbiology.Microorganism;
import net.cltech.enterprisent.domain.masters.microbiology.Procedure;
import net.cltech.enterprisent.domain.masters.microbiology.Task;
import net.cltech.enterprisent.domain.masters.test.OptionTemplate;
import net.cltech.enterprisent.domain.masters.test.ResultTemplate;
import net.cltech.enterprisent.domain.masters.test.Sample;
import net.cltech.enterprisent.domain.masters.test.TestBasic;
import net.cltech.enterprisent.domain.operation.audit.AuditEvent;
import net.cltech.enterprisent.domain.operation.filters.MicrobiologyFilter;
import net.cltech.enterprisent.domain.operation.microbiology.MicrobialDetection;
import net.cltech.enterprisent.domain.operation.microbiology.MicrobiologyGrowth;
import net.cltech.enterprisent.domain.operation.microbiology.MicrobiologyTask;
import net.cltech.enterprisent.domain.operation.microbiology.ResultMicrobiology;
import net.cltech.enterprisent.domain.operation.results.ResultTest;
import net.cltech.enterprisent.domain.operation.results.ResultTestComment;
import static net.cltech.enterprisent.tools.Constants.ISOLATION_READ_UNCOMMITTED;
import net.cltech.enterprisent.tools.DateTools;
import net.cltech.enterprisent.tools.Tools;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información de
 * microbiologia.
 *
 * @version 1.0.0
 * @author cmartin
 * @since 19/01/2018
 * @see Creación
 */
public interface MicrobiologyDao
{

    /**
     * Obtiene la conexion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplate();

    /**
     * Obtiene el dao de Comentarios
     *
     * @return Instancia de CommentDao
     */
    public CommentDao getCommentDao();

    /**
     * Obtiene el examen asociado a medios de cultivo de la muestra.
     *
     * @param idSample Id de la muestra.
     * @param order
     *
     * @return Examen.
     */
    default TestBasic getTestMicrobiologySample(Integer idSample, Long order)
    {
        try
        {
            String query = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT DISTINCT lab39.lab39c1, "
                    + "         lab39.lab39c2, "
                    + "         lab39.lab39c3, "
                    + "         lab39.lab39c4 "
                    + "FROM lab39 "
                    + "INNER JOIN lab24 ON lab24.lab24c1 = lab39.lab24c1 "
                    + "INNER JOIN lab164 ON lab164.lab39c1 = lab39.lab39c1 "
                    + "INNER JOIN lab165 ON lab165.lab39c1 = lab39.lab39c1 "
                    + "WHERE lab24.lab24c1 = ? AND lab22c1 = ?";

            return getJdbcTemplate().queryForObject(query,
                    new Object[]
                    {
                        idSample,
                        order
                    }, (ResultSet rs, int i) ->
            {
                TestBasic test = new TestBasic();

                test.setId(rs.getInt("lab39c1"));
                test.setCode(rs.getString("lab39c2"));
                test.setAbbr(rs.getString("lab39c3"));
                test.setName(rs.getString("lab39c4"));

                return test;
            });

        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Obtiene la lista de examenes asociados a medios de cultivo de la muestra.
     *
     * @param idSample Id de la muestra.
     * @param order
     *
     * @return Examen.
     */
    default List<TestBasic> getTestsMicrobiologySample(Integer idSample, Long order)
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(order));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;

            String query = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT DISTINCT lab39.lab39c1, "
                    + "         lab39.lab39c2, "
                    + "         lab39.lab39c3, "
                    + "         lab39.lab39c4 "
                    + "FROM lab39 "
                    + "INNER JOIN " + lab57 + " lab57 ON lab57.lab39c1 = lab39.lab39c1 "
                    + "INNER JOIN lab22 ON lab22.lab22c1 = lab57.lab22c1 AND lab22.lab22c1 = ? "
                    + "INNER JOIN lab24 ON lab24.lab24c1 = lab39.lab24c1 "
                    + "INNER JOIN lab164 ON lab164.lab39c1 = lab39.lab39c1 "
                    + "WHERE lab24.lab24c1 = ?";

            return getJdbcTemplate().query(query,
                    new Object[]
                    {
                        order,
                        idSample
                    }, (ResultSet rs, int i) ->
            {
                TestBasic test = new TestBasic();

                test.setId(rs.getInt("lab39c1"));
                test.setCode(rs.getString("lab39c2"));
                test.setAbbr(rs.getString("lab39c3"));
                test.setName(rs.getString("lab39c4"));

                return test;
            });

        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Obtiene la informacion de la siembra de una orden y una muestra.
     *
     * @param microbiologyGrowth Objeto de Microbiologia - Siembra.
     *
     * @return Objeto de Microbiologia - Siembra.
     */
    default MicrobiologyGrowth getAntiobiogram(MicrobiologyGrowth microbiologyGrowth)
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(microbiologyGrowth.getOrder().getOrderNumber()));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;

            String query = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT "
                    + "     lab57.lab22c1, "
                    + "     lab57.lab39c1, "
                    + "     lab57.lab24c1, "
                    + "     lab57.lab24c1_1, "
                    + "     lab57.lab158c1, "
                    + "     lab57.lab57c64, "
                    + "     lab158.lab158c2, "
                    + "     lab158.lab158c3, "
                    + "     lab57.lab201c1, "
                    + "     lab201.lab201c2 "
                    + "FROM " + lab57 + " as lab57 "
                    + "LEFT JOIN lab158 ON lab158.lab158c1 = lab57.lab158c1 "
                    + "LEFT JOIN lab201 ON lab201.lab201c1 = lab57.lab201c1 "
                    + "WHERE lab57.lab22c1 = ? AND lab57.lab39c1 = ?";

            return getJdbcTemplate().queryForObject(query,
                    new Object[]
                    {
                        microbiologyGrowth.getOrder().getOrderNumber(), microbiologyGrowth.getTest().getId()
                    }, (ResultSet rs, int i) ->
            {
                for (Sample subSample : microbiologyGrowth.getSample().getSubSamples())
                {
                    subSample.setSelected(subSample.getId().equals(rs.getInt("lab24c1_1")));
                }

                if (rs.getString("lab158c1") != null)
                {
                    microbiologyGrowth.getAnatomicalSite().setId(rs.getInt("lab158c1"));
                    microbiologyGrowth.getAnatomicalSite().setName(rs.getString("lab158c2"));
                    microbiologyGrowth.getAnatomicalSite().setAbbr(rs.getString("lab158c3"));
                } else
                {
                    microbiologyGrowth.setAnatomicalSite(null);
                }

                if (rs.getString("lab201c1") != null)
                {
                    microbiologyGrowth.getCollectionMethod().setId(rs.getInt("lab201c1"));
                    microbiologyGrowth.getCollectionMethod().setName(rs.getString("lab201c2"));
                } else
                {
                    microbiologyGrowth.setCollectionMethod(null);
                }
                microbiologyGrowth.setDestination(rs.getInt("lab57c64"));
                return microbiologyGrowth;
            });

        } catch (EmptyResultDataAccessException ex)
        {
            return microbiologyGrowth;
        }
    }

    /**
     * Obtiene la informacion de la siembra de una orden y una muestra.
     *
     * @param microbiologyGrowth Objeto de Microbiologia - Siembra.
     *
     * @return Objeto de Microbiologia - Siembra.
     */
    default MicrobiologyGrowth getMicrobiologyGrowth(MicrobiologyGrowth microbiologyGrowth)
    {
        try
        {

            String query = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT "
                    + "     lab200.lab22c1, "
                    + "     lab200.lab39c1, "
                    + "     lab200.lab200c2, "
                    + "     lab200.lab04c1, "
                    + "     lab04.lab04c2, "
                    + "     lab04.lab04c3, "
                    + "     lab04.lab04c4, "
                    + "     lab200.lab200c3, "
                    + "     lab200.lab04c1_1, "
                    + "     userGrowth.lab04c2 AS lab04c2_1, "
                    + "     userGrowth.lab04c3 AS lab04c3_1, "
                    + "     userGrowth.lab04c4 AS lab04c4_1 "
                    + "FROM lab200 "
                    + "LEFT JOIN lab57 ON lab57.lab22c1 = lab200.lab22c1 AND lab57.lab39c1 = lab200.lab39c1 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab200.lab04c1 "
                    + "LEFT JOIN lab04 AS userGrowth ON userGrowth.lab04c1 = lab200.lab04c1_1 "
                    + "WHERE lab200.lab22c1 = ? AND lab200.lab39c1 = ?";

            return getJdbcTemplate().queryForObject(query,
                    new Object[]
                    {
                        microbiologyGrowth.getOrder().getOrderNumber(), microbiologyGrowth.getTest().getId()
                    }, (ResultSet rs, int i) ->
            {
                getAntiobiogram(microbiologyGrowth);

                /*Usuario*/
                microbiologyGrowth.getUser().setId(rs.getInt("lab04c1"));
                microbiologyGrowth.getUser().setName(rs.getString("lab04c2"));
                microbiologyGrowth.getUser().setLastName(rs.getString("lab04c3"));
                microbiologyGrowth.getUser().setUserName(rs.getString("lab04c4"));

                /*Usuario Siembra*/
                if (rs.getString("lab04c1_1") != null)
                {
                    microbiologyGrowth.getUserGrowth().setId(rs.getInt("lab04c1_1"));
                    microbiologyGrowth.getUserGrowth().setName(rs.getString("lab04c2_1"));
                    microbiologyGrowth.getUserGrowth().setLastName(rs.getString("lab04c3_1"));
                    microbiologyGrowth.getUserGrowth().setUserName(rs.getString("lab04c4_1"));
                } else
                {
                    microbiologyGrowth.setUserGrowth(null);
                }

                microbiologyGrowth.setCommentsMicrobiology(getCommentDao().listCommentMicrobiology(microbiologyGrowth.getOrder().getOrderNumber(), microbiologyGrowth.getTest().getId(), null));
                microbiologyGrowth.setLastTransaction(rs.getTimestamp("lab200c2"));
                microbiologyGrowth.setDateGrowth(rs.getTimestamp("lab200c3"));

                getMediaCultureGrowth(microbiologyGrowth);
                getProcedureGrowth(microbiologyGrowth);

                return microbiologyGrowth;
            });

        } catch (EmptyResultDataAccessException ex)
        {
            return microbiologyGrowth;
        }
    }

    /**
     * Obtiene los medios de cultivos asociados a una orden y a un examen.
     *
     * @param microbiologyGrowth Objeto de Microbiologia - Siembra.
     */
    default void getMediaCultureGrowth(MicrobiologyGrowth microbiologyGrowth)
    {
        try
        {
            String query = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT lab155.lab155c1, lab155c2, lab155c3, lab22c1, lab164c1 "
                    + "FROM lab164 "
                    + "INNER JOIN lab155 ON lab155.lab155c1 = lab164.lab155c1 "
                    + "LEFT JOIN lab203 ON lab203.lab155c1 = lab155.lab155c1 AND lab203.lab22c1 = ? "
                    + "WHERE lab164.lab39c1 = ?";

            microbiologyGrowth.setMediaCultures(getJdbcTemplate().query(query,
                    new Object[]
                    {
                        microbiologyGrowth.getOrder().getOrderNumber(), microbiologyGrowth.getTest().getId()
                    }, (ResultSet rs, int i) ->
            {
                MediaCulture mediaCulture = new MediaCulture();
                mediaCulture.setId(rs.getInt("lab155c1"));
                mediaCulture.setCode(rs.getString("lab155c2"));
                mediaCulture.setName(rs.getString("lab155c3"));
                mediaCulture.setSelect(rs.getString("lab22c1") != null || rs.getInt("lab164c1") == 1);

                return mediaCulture;
            }));

        } catch (EmptyResultDataAccessException ex)
        {
            microbiologyGrowth.setMediaCultures(new ArrayList<>());
        }
    }

    /**
     * Obtiene los procedimientos asociados a una orden y a un examen.
     *
     * @param microbiologyGrowth Objeto de Microbiologia - Siembra.
     */
    default void getProcedureGrowth(MicrobiologyGrowth microbiologyGrowth)
    {
        try
        {
            String query = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT lab156.lab156c1, lab156c2, lab156c3, lab157c1, lab157c2, lab22c1 "
                    + "FROM lab157 "
                    + "INNER JOIN lab156 ON lab156.lab156c1 = lab157.lab156c1 "
                    + "LEFT JOIN lab202 ON lab202.lab156c1 = lab156.lab156c1 AND lab202.lab22c1 = ? "
                    + "WHERE lab157.lab39c1 = ?";

            microbiologyGrowth.setProcedures(getJdbcTemplate().query(query,
                    new Object[]
                    {
                        microbiologyGrowth.getOrder().getOrderNumber(), microbiologyGrowth.getTest().getId()
                    }, (ResultSet rs, int i) ->
            {
                Procedure procedure = new Procedure();
                procedure.setId(rs.getInt("lab156c1"));
                procedure.setCode(rs.getString("lab156c2"));
                procedure.setName(rs.getString("lab156c3"));
                procedure.setSelected(rs.getString("lab22c1") != null || rs.getInt("lab157c1") == 1);
                procedure.setConfirmatorytest(rs.getInt("lab157c2"));

                return procedure;
            }));

        } catch (EmptyResultDataAccessException ex)
        {
            microbiologyGrowth.setProcedures(new ArrayList<>());
        }
    }

    /**
     * Actualiza campos relacionados con el antibiograma en resultados en la
     * base de datos.
     *
     * @param microbiologyGrowth Instancia con los datos de la microbiologia.
     * @param timestamp Fecha en que se ejecuta la acción.
     *
     * @return Instancia con las datos del medio de cultivo.
     * @throws Exception Error en la base de datos.
     */
    default boolean verifyMicrobiologyUpdateTest(MicrobiologyGrowth microbiologyGrowth, Timestamp timestamp) throws Exception
    {
        Integer year = Tools.YearOfOrder(String.valueOf(microbiologyGrowth.getOrder().getOrderNumber()));
        Integer currentYear = DateTools.dateToNumberYear(new Date());
        String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;

        getJdbcTemplate().update("UPDATE " + lab57 + "  SET lab24c1_1 = ?, lab158c1 = ?, lab201c1 = ?, lab57c64 = ? "
                + "WHERE lab22c1 = ? AND lab39c1 = ?",
                microbiologyGrowth.getSubSample() == null ? null : microbiologyGrowth.getSubSample().getId(),
                microbiologyGrowth.getAnatomicalSite() == null ? null : microbiologyGrowth.getAnatomicalSite().getId(),
                microbiologyGrowth.getCollectionMethod() == null ? null : microbiologyGrowth.getCollectionMethod().getId(),
                microbiologyGrowth.getDestination(),
                //                timestamp,
                //                microbiologyGrowth.getUser().getId(),
                microbiologyGrowth.getOrder().getOrderNumber(),
                microbiologyGrowth.getTest().getId());

        return true;
    }

    /**
     * Registra la verificación de microbiologia en la base de datos.
     *
     * @param microbiologyGrowth Instancia con los datos de la microbiologia.
     * @param timestamp Fecha.
     *
     * @return Instancia con las datos del medio de cultivo.
     * @throws Exception Error en la base de datos.
     */
    default boolean verifyMicrobiology(MicrobiologyGrowth microbiologyGrowth, Timestamp timestamp) throws Exception
    {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab200");

        HashMap parameters = new HashMap();
        parameters.put("lab22c1", microbiologyGrowth.getOrder().getOrderNumber());
        parameters.put("lab39c1", microbiologyGrowth.getTest().getId());
        parameters.put("lab200c2", timestamp);
        parameters.put("lab04c1", microbiologyGrowth.getUser().getId());

        insert.execute(parameters);
        microbiologyGrowth.setLastTransaction(timestamp);
        return true;
    }

    /**
     * Actualiza la informacion de la verificación de microbiología (Submuestra,
     * Sitio anatomico, Metodo de recolección, Comentario)
     *
     * @param verification información de la verificacion
     * @return Cantidad de registros afectados.
     */
    default int updateVerification(MicrobiologyGrowth verification)
    {
        Integer year = Tools.YearOfOrder(String.valueOf(verification.getOrder().getOrderNumber()));
        Integer currentYear = DateTools.dateToNumberYear(new Date());
        String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;

        return getJdbcTemplate().update("UPDATE  " + lab57 + "  SET lab24c1_1 = ?, lab158c1 = ?, lab201c1 = ?, lab57c64 = ? "
                + "WHERE lab22c1 = ? AND lab39c1 = ?",
                verification.getSubSample() == null ? null : verification.getSubSample().getId(),
                verification.getAnatomicalSite() == null ? null : verification.getAnatomicalSite().getId(),
                verification.getCollectionMethod() == null ? null : verification.getCollectionMethod().getId(),
                verification.getDestination(),
                verification.getOrder().getOrderNumber(),
                verification.getTest().getId());
    }

    /**
     * Actualiza la informacion de la siembra para el exámen
     *
     * @param growth información de la siembra
     * @return Cantidad de registros afectados.
     */
    default int updateGrowth(MicrobiologyGrowth growth)
    {

        Timestamp timestamp = new Timestamp(new Date().getTime());
        return getJdbcTemplate().update("UPDATE lab200 SET lab200c3 = ?, lab04c1_1 = ? "
                + "WHERE lab22c1 = ? AND lab39c1 = ?",
                timestamp,
                growth.getUserGrowth().getId(),
                growth.getOrder().getOrderNumber(),
                growth.getTest().getId());
    }

    /**
     * Actualiza el tipo de entrada del examen a cultivo.
     *
     * @param order Numero de la Orden.
     * @param idTest Id del examen.
     * @param entryType Tipo de entrada: 1 -> Cultivo, 2 -> Directo
     */
    default void updateEntryTestType(Long order, Integer idTest, Short entryType)
    {
        Integer year = Tools.YearOfOrder(String.valueOf(order));
        Integer currentYear = DateTools.dateToNumberYear(new Date());
        String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;

        if (entryType == 1)
        {
            getJdbcTemplate().update("UPDATE " + lab57 + " SET lab57c36 = ? WHERE lab22c1 = ? AND lab39c1 = ?", entryType, order, idTest);
        } else if (entryType == 2)
        {
            getJdbcTemplate().update("UPDATE " + lab57 + " SET lab57c34 = ?, lab57c36 = ? WHERE lab22c1 = ? AND lab39c1 = ?", DateTools.dateToNumber(new Date()), entryType, order, idTest);
        }
    }

    /**
     * Inserta los medios de cultivo
     *
     * @param order Numero de orden
     * @param test Id examen
     * @param mediaCultures lista con los medios de cultivo
     * @return Registros afectados
     */
    default int insertCultureMedia(Long order, Integer test, List<MediaCulture> mediaCultures)
    {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate()).withTableName("lab203");
        List<HashMap<String, Object>> batch = new ArrayList<>(0);
        HashMap<String, Object> parameters = null;
        for (MediaCulture cultureMedia : mediaCultures)
        {
            parameters = new HashMap<>(0);
            parameters.put("lab22c1", order); //Orden
            parameters.put("lab39c1", test); //Id Examen
            parameters.put("lab155c1", cultureMedia.getId());
            batch.add(parameters);
        }
        return insert.executeBatch(batch.toArray(new HashMap[0])).length;
    }

    /**
     * Elimina medios de cultivo asignados en la siembra
     *
     * @param order numero de orden
     * @param test id examen
     * @return Registros afectados
     */
    default int deleteCultureMedia(Long order, Integer test)
    {
        return getJdbcTemplate().update("DELETE FROM lab203 WHERE lab22c1 = ? AND lab39c1 = ?",
                order,
                test);
    }

    /**
     * Inserta procedimientos en la siembra
     *
     * @param order numero de orden
     * @param test id examen
     * @param procedures lista de procedimietos
     * @return Registros afectados.
     */
    default int insertProcedures(Long order, Integer test, List<Procedure> procedures)
    {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate()).withTableName("lab202");
        List<HashMap<String, Object>> batch = new ArrayList<>(0);
        HashMap<String, Object> parameters = null;
        for (Procedure procedure : procedures)
        {
            parameters = new HashMap<>(0);
            parameters.put("lab22c1", order); //Orden
            parameters.put("lab39c1", test); //Id Examen
            parameters.put("lab156c1", procedure.getId());
            batch.add(parameters);
        }
        return insert.executeBatch(batch.toArray(new HashMap[0])).length;
    }

    /**
     * Elimina procedimiento asignados en la siembra
     *
     * @param order numero de orden
     * @param test id examen
     * @return Registros afectados
     */
    default int deleteProcedure(Long order, Integer test)
    {
        return getJdbcTemplate().update("DELETE FROM lab202 WHERE lab22c1 = ? AND lab39c1 = ?",
                order,
                test);
    }

    /**
     * Consulta la trazabilidad de microbiologia para una orden y muestra
     *
     * @param order numero de orden
     * @param sample id muestra
     * @return Trazabilidad de la orden.
     */
    default List<AuditEvent> listTrackingMicrobiology(Long order, Integer sample)
    {
        try
        {
            Object[] params = null;
            String query = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT lab03.lab03c2, lab03.lab03c4, lab03c5, "
                    + " lab04.lab04c1, lab04.lab04c4 "
                    + " FROM lab03 "
                    + "INNER JOIN lab04 ON lab04.lab04c1 = lab03.lab04c1 ";

            String where = "";
            if (sample != null)
            {
                where = " WHERE lab03.lab22c1 = ? AND lab03c1 = ? AND lab03c3 = 'M'";
                params = new Object[]
                {
                    order, sample
                };
            } else
            {
                where = " WHERE lab03.lab22c1 = ? AND lab03c3 = 'M'";
                params = new Object[]
                {
                    order
                };
            }

            return getJdbcTemplate().query(query + where, params, (ResultSet rs, int i) ->
            {
                AuditEvent event = new AuditEvent();
                event.setDate(rs.getTimestamp("lab03c5"));
                event.setUser(new AuthorizedUser(rs.getInt("lab04c1")));
                event.getUser().setUserName(rs.getString("lab04c4"));
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
     * Inserta la Detección Microbial.
     *
     * @param microbialDetection Clase que representa la deteccion microbiana.
     * @return Registros afectados
     */
    default int insertMicrobialDetection(MicrobialDetection microbialDetection)
    {

        Integer year = Tools.YearOfOrder(String.valueOf(microbialDetection.getOrder()));
        Integer currentYear = DateTools.dateToNumberYear(new Date());
        String lab204 = year.equals(currentYear) ? "lab204" : "lab204_" + year;

        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate()).withTableName(lab204).usingGeneratedKeyColumns("lab204c1");

        List<HashMap<String, Object>> batch = new ArrayList<>(0);
        HashMap<String, Object> parameters = null;

        for (Microorganism microorganism : microbialDetection.getMicroorganisms())
        {
            if (getMicrobialDetectionMicroorganism(microbialDetection.getOrder(), microbialDetection.getTest(), microorganism.getId()) == null)
            {
                parameters = new HashMap<>(0);
                parameters.put("lab22c1", microbialDetection.getOrder()); //Orden
                parameters.put("lab39c1", microbialDetection.getTest()); //Id Examen
                parameters.put("lab77c1", microorganism.getSensitivity().getId()); //Id Examen
                parameters.put("lab76c1", microorganism.getId()); //Microorganismo
                parameters.put("lab204c2", microorganism.getComment());
                parameters.put("lab204c4", microorganism.getRecount());
                parameters.put("lab204c6", microorganism.getComplementations());
                parameters.put("lab204c3", timestamp);
                parameters.put("lab04c1", microbialDetection.getUser().getId());
                batch.add(parameters);
            }
        }
        return insert.executeBatch(batch.toArray(new HashMap[0])).length;
    }

    /**
     * Actualiza el campo tiene antibiograma del examen.
     *
     * @param idOrder Numero de la orden.
     * @param idTest Id del examen.
     * @param hasMicroorganism Indica si el resultado tiene microorganismos
     * asociados
     * @param idUser Id usuario.
     */
    default void updateTestMicrobialDetection(long idOrder, int idTest, boolean hasMicroorganism, int idUser)
    {
        Integer year = Tools.YearOfOrder(String.valueOf(idOrder));
        Integer currentYear = DateTools.dateToNumberYear(new Date());
        String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;

        getJdbcTemplate().update("UPDATE " + lab57 + " SET lab57c26 = ? "
                + " WHERE lab22c1 = ? AND lab39c1 = ?",
                hasMicroorganism ? 1 : 0, idOrder, idTest);
    }

    /**
     * Actualiza el campo tiene plantilla del examen.
     *
     * @param idOrder Numero de la orden.
     * @param idTest Id del examen.
     * @param hasTemplate Indica si el resultado tiene una plantilla asociada
     * @param idUser Id usuario.
     */
    default void updateTestTemplate(long idOrder, int idTest, boolean hasTemplate, int idUser)
    {
        Integer year = Tools.YearOfOrder(String.valueOf(idOrder));
        Integer currentYear = DateTools.dateToNumberYear(new Date());
        String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;

        getJdbcTemplate().update("UPDATE " + lab57 + " SET lab57c42 = ? "
                + " WHERE lab22c1 = ? AND lab39c1 = ?",
                hasTemplate ? 1 : 0, idOrder, idTest);
    }

    /**
     * Actualiza el comentario de la detección microbiana.
     *
     * @param microorganism Clase que representa la deteccion microbiana.
     * @param order
     * @return Registros afectados
     */
    default Microorganism updateCommentMicrobialDetection(Microorganism microorganism, Long order)
    {

        Integer year = Tools.YearOfOrder(String.valueOf(order));
        Integer currentYear = DateTools.dateToNumberYear(new Date());
        String lab204 = year.equals(currentYear) ? "lab204" : "lab204_" + year;

        Timestamp timestamp = new Timestamp(new Date().getTime());
        getJdbcTemplate().update("UPDATE " + lab204 + " SET lab204c2 = ?, lab204c4 = ?, lab204c6 = ?, lab204c3 = ?, lab04c1 = ?"
                + " WHERE lab204c1 = ?",
                microorganism.getComment(), microorganism.getRecount(), microorganism.getComplementations(), timestamp, microorganism.getUser().getId(), microorganism.getIdMicrobialDetection());

        microorganism.setLastTransaction(timestamp);
        return microorganism;
    }

    /**
     * Actualiza el comentario de la detección microbiana.
     *
     * @param idOrder
     * @param idTest
     * @param idMicrobialDetection
     * @return comentario de la deteccion
     */
    default String getCommentMicrobialDetection(long idOrder, int idTest, int idMicrobialDetection)
    {
        Integer year = Tools.YearOfOrder(String.valueOf(idOrder));
        Integer currentYear = DateTools.dateToNumberYear(new Date());
        String lab204 = year.equals(currentYear) ? "lab204" : "lab204_" + year;

        Object[] params = null;
        String query = "" + ISOLATION_READ_UNCOMMITTED
                + "SELECT lab204.lab204c2, lab204.lab204c4, lab204.lab204c6 "
                + "FROM " + lab204 + " as lab204 "
                + "WHERE lab22c1 = ? and lab39c1 = ? and lab204c1 = ?";

        return getJdbcTemplate().queryForObject(query,
                new Object[]
                {
                    idOrder, idTest, idMicrobialDetection
                }, (ResultSet rs, int i) ->
        {
            return rs.getString("lab204c2");
        });
    }

    /**
     * Borra microorganismos de la Detección Microbial.
     *
     * @param microbialDetection Clase que representa la deteccion microbiana.
     */
    default void deleteMicrobialDetection(MicrobialDetection microbialDetection)
    {

        Integer year = Tools.YearOfOrder(String.valueOf(microbialDetection.getOrder()));
        Integer currentYear = DateTools.dateToNumberYear(new Date());
        String lab204 = year.equals(currentYear) ? "lab204" : "lab204_" + year;
        String lab205 = year.equals(currentYear) ? "lab205" : "lab205_" + year;

        for (Microorganism microorganism : microbialDetection.getMicroorganisms())
        {
            getJdbcTemplate().update("DELETE FROM " + lab205 + " WHERE lab204c1 = ?", microorganism.getIdMicrobialDetection());
            getJdbcTemplate().update("DELETE FROM " + lab204 + " WHERE lab204c1 = ?", microorganism.getIdMicrobialDetection());
        }
    }

    /**
     * Consulta la Detección microbial
     *
     * @param idMicrobialDetection Id de la detección microbiana
     * @param order
     * @return Detección microbial (Microorganismos)
     */
    default MicrobialDetection getMicrobialDetection(Integer idMicrobialDetection, Long order)
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(order));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab204 = year.equals(currentYear) ? "lab204" : "lab204_" + year;

            Object[] params = null;
            String query = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT lab204.lab22c1, lab204.lab39c1 "
                    + "FROM " + lab204 + " as lab204 "
                    + "WHERE lab204.lab204c1 = ?";

            return getJdbcTemplate().queryForObject(query,
                    new Object[]
                    {
                        idMicrobialDetection
                    }, (ResultSet rs, int i) ->
            {
                MicrobialDetection microbialDetection = new MicrobialDetection();
                microbialDetection.setOrder(rs.getLong("lab22c1"));
                microbialDetection.setTest(rs.getInt("lab39c1"));

                microbialDetection.setMicroorganisms(getMicrobialDetection(microbialDetection.getOrder(), microbialDetection.getTest()));

                return microbialDetection;
            });

        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Consulta la orden y el examen de una Detección microbial
     *
     * @param idMicrobialDetection Id de la detección microbiana
     * @return Detección microbial (Microorganismos)
     */
    default MicrobialDetection getMicrobialDetectionGeneral(Integer idMicrobialDetection, Long order)
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(order));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab204 = year.equals(currentYear) ? "lab204" : "lab204_" + year;

            Object[] params = null;
            String query = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT lab204.lab22c1, lab204.lab39c1 "
                    + "FROM " + lab204 + " as lab204 "
                    + "WHERE lab204.lab204c1 = ?";

            return getJdbcTemplate().queryForObject(query,
                    new Object[]
                    {
                        idMicrobialDetection
                    }, (ResultSet rs, int i) ->
            {
                MicrobialDetection microbialDetection = new MicrobialDetection();
                microbialDetection.setOrder(rs.getLong("lab22c1"));
                microbialDetection.setTest(rs.getInt("lab39c1"));

                return microbialDetection;
            });

        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Consulta la Detección microbial de una orden y examen
     *
     * @param idOrder Numero de orden
     * @param idTest Id del examen
     * @param idMicroorganism Id del microorganismo
     * @return Detección microbial (Microorganismos)
     */
    default Microorganism getMicrobialDetectionMicroorganism(Long idOrder, Integer idTest, Integer idMicroorganism)
    {
        try
        {

            Integer year = Tools.YearOfOrder(String.valueOf(idOrder));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab204 = year.equals(currentYear) ? "lab204" : "lab204_" + year;

            String query = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT lab204c1 "
                    + "FROM " + lab204 + " as lab204 "
                    + "WHERE lab22c1 = ? AND lab39c1 = ? and lab76c1 = ?";

            return getJdbcTemplate().queryForObject(query,
                    new Object[]
                    {
                        idOrder, idTest, idMicroorganism
                    }, (ResultSet rs, int i) ->
            {
                Microorganism microorganism = new Microorganism();
                microorganism.setIdMicrobialDetection(rs.getString("lab204c1") == null ? null : rs.getInt("lab204c1"));
                return microorganism;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Consulta la Detección microbial de una orden y examen
     *
     * @param idOrder Numero de orden
     * @param idTest Id del examen
     * @return Detección microbial (Microorganismos)
     */
    default List<Microorganism> getMicrobialDetection(Long idOrder, Integer idTest)
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(idOrder));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab204 = year.equals(currentYear) ? "lab204" : "lab204_" + year;

            Object[] params = null;
            String query = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT lab76.lab76c1, lab76c2, lab76c3, lab76.lab07c1, lab204c1, lab204c2, lab204c4, lab204c6 "
                    + "FROM lab206 "
                    + "LEFT JOIN lab76 ON lab206.lab76c1 = lab76.lab76c1 "
                    + "LEFT JOIN " + lab204 + " as lab204 ON lab204.lab22c1 = ? AND lab204.lab39c1 = ? AND lab206.lab76c1 = lab204.lab76c1 "
                    + "WHERE lab206.lab39c1 IS NULL "
                    + "ORDER BY lab76c1";

            return getJdbcTemplate().query(query,
                    new Object[]
                    {
                        idOrder, idTest
                    }, (ResultSet rs, int i) ->
            {
                Microorganism microorganism = new Microorganism();
                microorganism.setId(rs.getInt("lab76c1"));
                microorganism.setName(rs.getString("lab76c2"));
                microorganism.setIdMicrobialDetection(rs.getString("lab204c1") == null ? null : rs.getInt("lab204c1"));
                microorganism.setSelected(rs.getString("lab204c1") != null);
                microorganism.setComment(rs.getString("lab204c2"));
                microorganism.setRecount(rs.getString("lab204c4"));
                microorganism.setComplementations(rs.getString("lab204c6"));

                if (microorganism.getIdMicrobialDetection() != null)
                {
                    microorganism.setResultsMicrobiology(listResultMicrobiologySensitivity(microorganism.getIdMicrobialDetection(), idOrder));
                    microorganism.setCountResultMicrobiology( microorganism.getResultsMicrobiology().size());
                }
                return microorganism;
            });

        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Consulta la Detección microbial de una orden y examen
     *
     * @param idMicrobialDetection Id de la detección microbiana
     * @param order
     * @return Lista de Resultados de Microbiologia
     */
    default List<ResultMicrobiology> listResultMicrobiologySensitivity(Integer idMicrobialDetection, Long order)
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(order));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab204 = year.equals(currentYear) ? "lab204" : "lab204_" + year;
            String lab205 = year.equals(currentYear) ? "lab205" : "lab205_" + year;

            Object[] params = null;
            String query = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT lab77.lab77c1, lab78.lab78c2, lab78.lab45c1, "
                    + "lab79.lab79c1, lab79.lab79c2, "
                    + "lab205.lab205c1, lab205.lab205c2, lab205.lab205c3, lab205.lab205c4, lab205.lab205c5, lab205.lab205c6, lab205.lab205c7, lab205.lab205c8, lab205.lab205c9, lab205.lab205c10, lab205.lab205c11,"
                    + "lab04_1.lab04c1 AS lab04c1_1, lab04_1.lab04c2 AS lab04c2_1, lab04_1.lab04c3 AS lab04c3_1, lab04_1.lab04c4 AS lab04c4_1, "
                    + "lab04_2.lab04c1 AS lab04c1_2, lab04_2.lab04c2 AS lab04c2_2, lab04_2.lab04c3 AS lab04c3_2, lab04_2.lab04c4 AS lab04c4_2, "
                    + "lab04_3.lab04c1 AS lab04c1_3, lab04_3.lab04c2 AS lab04c2_3, lab04_3.lab04c3 AS lab04c3_3, lab04_3.lab04c4 AS lab04c4_3 "
                    + "FROM " + lab204 + " as lab204 "
                    + "INNER JOIN lab77 ON lab77.lab77c1 = lab204.lab77c1 "
                    + "INNER JOIN lab78 ON lab78.lab77c1 = lab77.lab77c1 "
                    + "LEFT JOIN lab79 ON lab79.lab79c1 = lab78.lab79c1 "
                    + "LEFT JOIN " + lab205 + " as lab205 ON lab205.lab204c1 = lab204.lab204c1 AND lab205.lab79c1 = lab79.lab79c1 "
                    + "LEFT JOIN lab04 AS lab04_1 ON lab04_1.lab04c1 = lab205.lab04c1_1 "
                    + "LEFT JOIN lab04 AS lab04_2 ON lab04_2.lab04c1 = lab205.lab04c1_2 "
                    + "LEFT JOIN lab04 AS lab04_3 ON lab04_3.lab04c1 = lab205.lab04c1_3 "
                    + "WHERE lab204.lab204c1 = ?";

            return getJdbcTemplate().query(query,
                    new Object[]
                    {
                        idMicrobialDetection
                    }, (ResultSet rs, int i) ->
            {
                ResultMicrobiology result = new ResultMicrobiology();
                result.setIdSensitivity(rs.getInt("lab77c1"));
                result.setIdAntibiotic(rs.getInt("lab79c1"));
                result.setNameAntibiotic(rs.getString("lab79c2"));
                result.setLine(rs.getInt("lab78c2"));
                result.setUnit(rs.getString("lab45c1") == null ? null : rs.getInt("lab45c1"));
                result.setCmi(rs.getString("lab205c1"));
                result.setInterpretationCMI(rs.getString("lab205c2"));
                result.setCmiM(rs.getString("lab205c3"));
                result.setInterpretationCMIM(rs.getString("lab205c4"));
                result.setCmiMPrint(rs.getInt("lab205c5") == 1);
                result.setDisk(rs.getString("lab205c6"));
                result.setInterpretationDisk(rs.getString("lab205c7"));
                result.setDiskPrint(rs.getInt("lab205c8") == 1);
                boolean select = (rs.getTimestamp("lab205c9") != null) || (rs.getTimestamp("lab205c10") != null) || (rs.getTimestamp("lab205c11") != null);
                result.setSelected(select);
                /*CMI*/
                result.setDateCMI(rs.getTimestamp("lab205c9"));
                if (rs.getString("lab04c1_1") != null)
                {
                    result.getUserCMI().setId(rs.getInt("lab04c1_1"));
                    result.getUserCMI().setName(rs.getString("lab04c2_1"));
                    result.getUserCMI().setLastName(rs.getString("lab04c3_1"));
                    result.getUserCMI().setUserName(rs.getString("lab04c4_1"));
                } else
                {
                    result.setUserCMI(null);
                }

                /*CMI Manual*/
                result.setDateCMIM(rs.getTimestamp("lab205c10"));
                if (rs.getString("lab04c1_2") != null)
                {
                    result.getUserCMIM().setId(rs.getInt("lab04c1_2"));
                    result.getUserCMIM().setName(rs.getString("lab04c2_2"));
                    result.getUserCMIM().setLastName(rs.getString("lab04c3_2"));
                    result.getUserCMIM().setUserName(rs.getString("lab04c4_2"));
                } else
                {
                    result.setUserCMIM(null);
                }

                /*Disco*/
                result.setDateDisk(rs.getTimestamp("lab205c11"));
                if (rs.getString("lab04c1_3") != null)
                {
                    result.getUserDisk().setId(rs.getInt("lab04c1_3"));
                    result.getUserDisk().setName(rs.getString("lab04c2_3"));
                    result.getUserDisk().setLastName(rs.getString("lab04c3_3"));
                    result.getUserDisk().setUserName(rs.getString("lab04c4_3"));
                } else
                {
                    result.setUserDisk(null);
                }
                return result;
            });

        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Consulta la Detección microbial de una orden y examen
     *
     * @param idMicrobialDetection Id de la detección microbiana
     * @param idAntibiotic Id Anbiotico
     * @param order
     * @return Lista de Resultados de Microbiologia
     */
    default ResultMicrobiology getResultMicrobiologySensitivity(Integer idMicrobialDetection, Integer idAntibiotic, Long order)
    {
        try
        {

            Integer year = Tools.YearOfOrder(String.valueOf(order));
            Integer currentYear = DateTools.dateToNumberYear(new Date());

            String lab205 = year.equals(currentYear) ? "lab205" : "lab205_" + year;

            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED);
            query.append("SELECT lab205.lab205c1 AS lab205c1, ")
                    .append("lab205.lab205c2 AS lab205c2, ")
                    .append("lab205.lab205c3 AS lab205c3, ")
                    .append("lab205.lab205c4 AS lab205c4, ")
                    .append("lab205.lab205c5 AS lab205c5, ")
                    .append("lab205.lab205c6 AS lab205c6, ")
                    .append("lab205.lab205c7 AS lab205c7, ")
                    .append("lab205.lab205c8 AS lab205c8, ")
                    .append("lab205.lab205c9 AS lab205c9, ")
                    .append("lab205.lab205c10 AS lab205c10, ")
                    .append("lab205.lab205c11 AS lab205c11, ")
                    .append("lab04_1.lab04c1 AS lab04c1_1, ")
                    .append("lab04_1.lab04c2 AS lab04c2_1, ")
                    .append("lab04_1.lab04c3 AS lab04c3_1, ")
                    .append("lab04_1.lab04c4 AS lab04c4_1, ")
                    .append("lab04_2.lab04c1 AS lab04c1_2, ")
                    .append("lab04_2.lab04c2 AS lab04c2_2, ")
                    .append("lab04_2.lab04c3 AS lab04c3_2, ")
                    .append("lab04_2.lab04c4 AS lab04c4_2, ")
                    .append("lab04_3.lab04c1 AS lab04c1_3, ")
                    .append("lab04_3.lab04c2 AS lab04c2_3, ")
                    .append("lab04_3.lab04c3 AS lab04c3_3, ")
                    .append("lab04_3.lab04c4 AS lab04c4_3 ")
                    .append("FROM  " + lab205 + " as lab205 ")
                    .append("LEFT JOIN lab04 AS lab04_1 ON lab04_1.lab04c1 = lab205.lab04c1_1 ")
                    .append("LEFT JOIN lab04 AS lab04_2 ON lab04_2.lab04c1 = lab205.lab04c1_2 ")
                    .append("LEFT JOIN lab04 AS lab04_3 ON lab04_3.lab04c1 = lab205.lab04c1_3 ")
                    .append("WHERE lab205.lab204c1 = ").append(idMicrobialDetection).append(" AND lab205.lab79c1 = ").append(idAntibiotic);

            return getJdbcTemplate().queryForObject(query.toString(),
                    (ResultSet rs, int i) ->
            {
                ResultMicrobiology result = new ResultMicrobiology();
                result.setCmi(rs.getString("lab205c1"));
                result.setInterpretationCMI(rs.getString("lab205c2"));
                result.setCmiM(rs.getString("lab205c3"));
                result.setInterpretationCMIM(rs.getString("lab205c4"));
                result.setCmiMPrint(rs.getInt("lab205c5") == 1);
                result.setDisk(rs.getString("lab205c6"));
                result.setInterpretationDisk(rs.getString("lab205c7"));
                result.setDiskPrint(rs.getInt("lab205c8") == 1);
                /*CMI*/
                result.setDateCMI(rs.getTimestamp("lab205c9"));
                if (rs.getString("lab04c1_1") != null)
                {
                    result.getUserCMI().setId(rs.getInt("lab04c1_1"));
                    result.getUserCMI().setName(rs.getString("lab04c2_1"));
                    result.getUserCMI().setLastName(rs.getString("lab04c3_1"));
                    result.getUserCMI().setUserName(rs.getString("lab04c4_1"));
                } else
                {
                    result.setUserCMI(null);
                }

                /*CMI Manual*/
                result.setDateCMIM(rs.getTimestamp("lab205c10"));
                if (rs.getString("lab04c1_2") != null)
                {
                    result.getUserCMIM().setId(rs.getInt("lab04c1_2"));
                    result.getUserCMIM().setName(rs.getString("lab04c2_2"));
                    result.getUserCMIM().setLastName(rs.getString("lab04c3_2"));
                    result.getUserCMIM().setUserName(rs.getString("lab04c4_2"));
                } else
                {
                    result.setUserCMIM(null);
                }

                /*Disco*/
                result.setDateDisk(rs.getTimestamp("lab205c11"));
                if (rs.getString("lab04c1_3") != null)
                {
                    result.getUserDisk().setId(rs.getInt("lab04c1_3"));
                    result.getUserDisk().setName(rs.getString("lab04c2_3"));
                    result.getUserDisk().setLastName(rs.getString("lab04c3_3"));
                    result.getUserDisk().setUserName(rs.getString("lab04c4_3"));
                } else
                {
                    result.setUserDisk(null);
                }
                return result;
            });
        } catch (DataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Consulta la Detección microbial - antibiotico, por su respectivo id de
     * deteccion microbiana
     *
     * @param idMicrobialDetection Id de la detección microbiana
     * @return Lista de Resultados de Microbiologia
     */
    default List<ResultMicrobiology> resultMicrobiologySensitivityByidMicrobialDetection(int idMicrobialDetection, Long order)
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(order));
            Integer currentYear = DateTools.dateToNumberYear(new Date());

            String lab205 = year.equals(currentYear) ? "lab205" : "lab205_" + year;

            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED);
            query.append("SELECT lab205.lab205c1 AS lab205c1, ")
                    .append("lab205.lab205c2 AS lab205c2, ")
                    .append("lab205.lab205c3 AS lab205c3, ")
                    .append("lab205.lab205c4 AS lab205c4, ")
                    .append("lab205.lab205c5 AS lab205c5, ")
                    .append("lab205.lab205c6 AS lab205c6, ")
                    .append("lab205.lab205c7 AS lab205c7, ")
                    .append("lab205.lab205c8 AS lab205c8, ")
                    .append("lab205.lab205c9 AS lab205c9, ")
                    .append("lab205.lab205c10 AS lab205c10, ")
                    .append("lab205.lab205c11 AS lab205c11, ")
                    .append("lab04_1.lab04c1 AS lab04c1_1, ")
                    .append("lab04_1.lab04c2 AS lab04c2_1, ")
                    .append("lab04_1.lab04c3 AS lab04c3_1, ")
                    .append("lab04_1.lab04c4 AS lab04c4_1, ")
                    .append("lab04_2.lab04c1 AS lab04c1_2, ")
                    .append("lab04_2.lab04c2 AS lab04c2_2, ")
                    .append("lab04_2.lab04c3 AS lab04c3_2, ")
                    .append("lab04_2.lab04c4 AS lab04c4_2, ")
                    .append("lab04_3.lab04c1 AS lab04c1_3, ")
                    .append("lab04_3.lab04c2 AS lab04c2_3, ")
                    .append("lab04_3.lab04c3 AS lab04c3_3, ")
                    .append("lab04_3.lab04c4 AS lab04c4_3 ")
                    .append("FROM " + lab205 + " as lab205 ")
                    .append("LEFT JOIN lab04 AS lab04_1 ON lab04_1.lab04c1 = lab205.lab04c1_1 ")
                    .append("LEFT JOIN lab04 AS lab04_2 ON lab04_2.lab04c1 = lab205.lab04c1_2 ")
                    .append("LEFT JOIN lab04 AS lab04_3 ON lab04_3.lab04c1 = lab205.lab04c1_3 ")
                    .append("WHERE lab205.lab204c1 = ").append(idMicrobialDetection);

            return getJdbcTemplate().query(query.toString(),
                    (ResultSet rs, int i) ->
            {
                ResultMicrobiology result = new ResultMicrobiology();
                result.setCmi(rs.getString("lab205c1"));
                result.setInterpretationCMI(rs.getString("lab205c2"));
                result.setCmiM(rs.getString("lab205c3"));
                result.setInterpretationCMIM(rs.getString("lab205c4"));
                result.setCmiMPrint(rs.getInt("lab205c5") == 1);
                result.setDisk(rs.getString("lab205c6"));
                result.setInterpretationDisk(rs.getString("lab205c7"));
                result.setDiskPrint(rs.getInt("lab205c8") == 1);
                /*CMI*/
                result.setDateCMI(rs.getTimestamp("lab205c9"));
                if (rs.getString("lab04c1_1") != null)
                {
                    result.getUserCMI().setId(rs.getInt("lab04c1_1"));
                    result.getUserCMI().setName(rs.getString("lab04c2_1"));
                    result.getUserCMI().setLastName(rs.getString("lab04c3_1"));
                    result.getUserCMI().setUserName(rs.getString("lab04c4_1"));
                } else
                {
                    result.setUserCMI(null);
                }

                /*CMI Manual*/
                result.setDateCMIM(rs.getTimestamp("lab205c10"));
                if (rs.getString("lab04c1_2") != null)
                {
                    result.getUserCMIM().setId(rs.getInt("lab04c1_2"));
                    result.getUserCMIM().setName(rs.getString("lab04c2_2"));
                    result.getUserCMIM().setLastName(rs.getString("lab04c3_2"));
                    result.getUserCMIM().setUserName(rs.getString("lab04c4_2"));
                } else
                {
                    result.setUserCMIM(null);
                }

                /*Disco*/
                result.setDateDisk(rs.getTimestamp("lab205c11"));
                if (rs.getString("lab04c1_3") != null)
                {
                    result.getUserDisk().setId(rs.getInt("lab04c1_3"));
                    result.getUserDisk().setName(rs.getString("lab04c2_3"));
                    result.getUserDisk().setLastName(rs.getString("lab04c3_3"));
                    result.getUserDisk().setUserName(rs.getString("lab04c4_3"));
                } else
                {
                    result.setUserDisk(null);
                }
                return result;
            });
        } catch (Exception ex)
        {
            return null;
        }
    }

    /**
     * Inserta los antibioticos asociados a la Detección Microbiana.
     *
     * @param idMicrobialDetection Id Detección Microbiana
     * @param resultMicrobiologys Lista de resultados de microbiologia
     * @param order
     * @return Registros afectados
     */
    default int insertAntibioticsMicrobialDetection(Integer idMicrobialDetection, List<ResultMicrobiology> resultMicrobiologys, Long order)
    {
        Integer year = Tools.YearOfOrder(String.valueOf(order));
        Integer currentYear = DateTools.dateToNumberYear(new Date());

        String lab205 = year.equals(currentYear) ? "lab205" : "lab205_" + year;

        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate()).withTableName(lab205);

        List<HashMap<String, Object>> batch = new ArrayList<>(0);
        HashMap<String, Object> parameters;

        for (ResultMicrobiology result : resultMicrobiologys)
        {
            if (result.isSelected())
            {
                parameters = new HashMap<>(0);
                parameters.put("lab204c1", idMicrobialDetection);
                parameters.put("lab79c1", result.getIdAntibiotic());
                parameters.put("lab205c1", result.getCmi());
                parameters.put("lab205c2", result.getInterpretationCMI());
                parameters.put("lab205c3", result.getCmiM());
                parameters.put("lab205c4", result.getInterpretationCMIM());
                parameters.put("lab205c5", result.isCmiMPrint() ? 1 : 0);
                parameters.put("lab205c6", result.getDisk());
                parameters.put("lab205c7", result.getInterpretationDisk());
                parameters.put("lab205c8", result.isDiskPrint() ? 1 : 0);
                parameters.put("lab205c9", result.getDateCMI());
                parameters.put("lab04c1_1", result.getUserCMI() == null || result.getUserCMI().getId() == null ? null : result.getUserCMI().getId());
                parameters.put("lab205c10", result.getDateCMIM());
                parameters.put("lab04c1_2", result.getUserCMIM() == null || result.getUserCMIM().getId() == null ? null : result.getUserCMIM().getId());
                parameters.put("lab205c11", result.getDateDisk());
                parameters.put("lab04c1_3", result.getUserDisk() == null || result.getUserDisk().getId() == null ? null : result.getUserDisk().getId());
                batch.add(parameters);
            }
        }
        return insert.executeBatch(batch.toArray(new HashMap[0])).length;
    }

    /**
     * Inserta los antibioticos asociados a la Detección Microbiana.
     *
     * @param idMicrobialDetection Id Detección Microbiana
     * @param resultMicrobiologys Lista de resultados de microbiologia
     * @param order
     *
     * @return Registros afectados
     */
    default int updateAntibioticsMicrobialDetection(Integer idMicrobialDetection, List<ResultMicrobiology> resultMicrobiologys, Long order)
    {
        Integer year = Tools.YearOfOrder(String.valueOf(order));
        Integer currentYear = DateTools.dateToNumberYear(new Date());

        String lab205 = year.equals(currentYear) ? "lab205" : "lab205_" + year;

        List<Object[]> parameters = new ArrayList<>();

        String query = "UPDATE " + lab205 + " SET lab205c1 = ?, lab205c2 = ?, lab205c3 = ?, lab205c4 = ?, lab205c5 = ?, lab205c6 = ?, lab205c7 = ?, lab205c8 = ?, lab205c9 = ?, lab04c1_1 = ?, lab205c10 = ?, lab04c1_2 = ?, lab205c11 = ?, lab04c1_3 = ? WHERE lab204c1 = ? AND lab79c1 = ? ";

        resultMicrobiologys.forEach((result) ->
        {
            parameters.add(new Object[]
            {
                result.getCmi(),
                result.getInterpretationCMI(),
                result.getCmiM(),
                result.getInterpretationCMIM(),
                result.isCmiMPrint() ? 1 : 0,
                result.getDisk(),
                result.getInterpretationDisk(),
                result.isDiskPrint() ? 1 : 0,
                result.getDateCMI(),
                result.getUserCMI() == null || result.getUserCMI().getId() == null ? null : result.getUserCMI().getId(),
                result.getDateCMIM(),
                result.getUserCMIM() == null || result.getUserCMIM().getId() == null ? null : result.getUserCMIM().getId(),
                result.getDateDisk(),
                result.getUserDisk() == null || result.getUserDisk().getId() == null ? null : result.getUserDisk().getId(),
                idMicrobialDetection,
                result.getIdAntibiotic()
            });
        });
        return getJdbcTemplate().batchUpdate(query, parameters).length;
    }

    /**
     * Elimina antibioticos asociados a la Detección Microbiana.
     *
     * @param idMicrobialDetection Clase que representa el microorganismo con
     * antibioticos.
     * @param resultMicrobiologys Lista de resultados de microbiologia.
     * @param order
     */
    default void deleteAntibioticsMicrobialDetection(Integer idMicrobialDetection, List<ResultMicrobiology> resultMicrobiologys, Long order)
    {
        Integer year = Tools.YearOfOrder(String.valueOf(order));
        Integer currentYear = DateTools.dateToNumberYear(new Date());

        String lab205 = year.equals(currentYear) ? "lab205" : "lab205_" + year;

        resultMicrobiologys.forEach((result) ->
        {
            getJdbcTemplate().update("DELETE FROM  " + lab205 + " WHERE lab204c1 = ? AND lab79c1 = ?", idMicrobialDetection, result.getIdAntibiotic());
        });
    }

    /**
     * Inserta las tareas de microbiologia asociadas a una orden y un examen.
     *
     * @param microbiologyTask Tareas de Microbiologiaç
     *
     * @return Registros afectados
     * @throws java.lang.Exception Error en la base de datos
     */
    default MicrobiologyTask insertMicrobiologyTask(MicrobiologyTask microbiologyTask) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab208")
                .usingGeneratedKeyColumns("lab208c1");

        HashMap parameters = new HashMap();
        parameters.put("lab208c2", microbiologyTask.getIdRecord());
        parameters.put("lab208c3", microbiologyTask.getComment());
        parameters.put("lab208c4", microbiologyTask.getType());
        parameters.put("lab22c1", microbiologyTask.getOrder());
        parameters.put("lab39c1", microbiologyTask.getIdTest());
        parameters.put("lab207c1", microbiologyTask.getDestination().getId());
        parameters.put("lab208c5", timestamp);
        parameters.put("lab04c1", microbiologyTask.getUser().getId());
        parameters.put("lab208c6", 0);
        parameters.put("lab208c7", timestamp);
        parameters.put("lab04c1_1", microbiologyTask.getUser().getId());

        Number key = insert.executeAndReturnKey(parameters);
        microbiologyTask.setId(key.intValue());
        microbiologyTask.setLastTransaction(timestamp);

        insertTask(microbiologyTask);

        return microbiologyTask;
    }

    /**
     * Inserta las tareas de microbiologia asociadas a una orden y un examen.
     *
     * @param microbiologyTask Tareas de Microbiologia
     *
     * @throws java.lang.Exception Error en la base de datos
     */
    default void insertTask(MicrobiologyTask microbiologyTask) throws Exception
    {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate()).withTableName("lab209");

        List<HashMap<String, Object>> batch = new ArrayList<>(0);
        HashMap<String, Object> parameters;

        for (Task task : microbiologyTask.getTasks())
        {
            parameters = new HashMap<>(0);
            parameters.put("lab208c1", microbiologyTask.getId());
            parameters.put("lab169c1", task.getId());
            batch.add(parameters);
        }
        insert.executeBatch(batch.toArray(new HashMap[0]));
    }

    /**
     * Consulta las tareas asociadas a una orden, un examen y un medio de
     * cultivo o un procedimiento
     *
     * @param idOrder Numero de Orden.
     * @param idTest Id del examen.
     * @param idRecord Id del medio de cultivo o prcedimiento.
     * @param type Tipo 1 -> Medio de Cultivo, 2 -> Proceso.
     * @return Lista de Resultados de Microbiologia
     */
    default List<MicrobiologyTask> getMicrobiologyTask(Long idOrder, Integer idTest, Integer idRecord, Short type)
    {
        try
        {
            Object[] params = null;
            String query = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT lab208.lab22c1, lab208.lab39c1, lab208c1, lab208c2, lab208c3, lab208c5, lab208c6, lab208c7, "
                    + "lab208.lab207c1, lab207c2, lab207c3, "
                    + "lab208.lab04c1, lab04.lab04c2, lab04.lab04c3, lab04.lab04c4, "
                    + "lab208.lab04c1_1, lab04_1.lab04c2 AS lab04c2_1, lab04_1.lab04c3 AS lab04c3_1, lab04_1.lab04c4 AS lab04c4_1 "
                    + "FROM lab208 "
                    + "INNER JOIN lab207 ON lab207.lab207c1 = lab208.lab207c1 "
                    + "INNER JOIN lab04 ON lab04.lab04c1 = lab208.lab04c1 "
                    + "INNER JOIN lab04 AS lab04_1 ON lab04_1.lab04c1 = lab208.lab04c1 "
                    + "WHERE lab22c1 = ? AND lab39c1 = ? AND lab208c2 = ? AND lab208c4 = ? ";

            return getJdbcTemplate().query(query,
                    new Object[]
                    {
                        idOrder, idTest, idRecord, type
                    }, (ResultSet rs, int i) ->
            {
                MicrobiologyTask task = new MicrobiologyTask();
                task.setOrder(rs.getLong("lab22c1"));
                task.setIdTest(rs.getInt("lab39c1"));
                task.setId(rs.getInt("lab208c1"));
                task.setIdRecord(rs.getInt("lab208c2"));
                task.setComment(rs.getString("lab208c3"));
                task.setReported(rs.getInt("lab208c6") == 1);
                //Destino
                MicrobiologyDestination destination = new MicrobiologyDestination();
                destination.setId(rs.getInt("lab207c1"));
                destination.setCode(rs.getString("lab207c2"));
                destination.setName(rs.getString("lab207c3"));
                task.setDestination(destination);
                /*Usuario que Actualizo*/
                task.setLastTransaction(rs.getTimestamp("lab208c5"));
                task.getUser().setId(rs.getInt("lab04c1"));
                task.getUser().setName(rs.getString("lab04c2"));
                task.getUser().setLastName(rs.getString("lab04c3"));
                task.getUser().setUserName(rs.getString("lab04c4"));
                /*Usuario que Actualizo*/
                task.setDateRegister(rs.getTimestamp("lab208c7"));
                task.getUserRegister().setId(rs.getInt("lab04c1_1"));
                task.getUserRegister().setName(rs.getString("lab04c2_1"));
                task.getUserRegister().setLastName(rs.getString("lab04c3_1"));
                task.getUserRegister().setUserName(rs.getString("lab04c4_1"));

                task.setTasks(getMicrobiologyDestinationTask(task.getId()));
                return task;
            });

        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Consulta las tareas asociadas a una orden, un examen y un medio de
     * cultivo o un procedimiento
     *
     * @param idMicrobiologyDestination Id de la relacion entre orden, examen,
     * medio de cultivo o procedimiento y destino de microbiologia.
     * @return Lista de Tareas.
     */
    default List<Task> getMicrobiologyDestinationTask(Integer idMicrobiologyDestination)
    {
        try
        {
            return getJdbcTemplate().query("" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT DISTINCT lab169.lab169c1, lab169c2, lab169.lab07c1 "
                    + "FROM lab209 "
                    + "INNER JOIN lab169 ON lab169.lab169c1 = lab209.lab169c1 "
                    + "WHERE lab209.lab208c1 = ? ",
                    new Object[]
                    {
                        idMicrobiologyDestination
                    }, (ResultSet rs, int i) ->
            {
                Task task = new Task();
                task.setId(rs.getInt("lab169c1"));
                task.setDescription(rs.getString("lab169c2"));
                task.setState(rs.getInt("lab07c1") == 1);
                task.setUser(null);

                return task;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Elimina antibioticos asociados a la Detección Microbiana.
     *
     * @param microbiologyTask Clase que representa las tareas de microbiologia
     * asociadas a una orden, examen, medio de cultivo o procedimiento y
     * destino.
     *
     * @return Objeto actualizado.
     */
    default MicrobiologyTask updateMicrobiologyTask(MicrobiologyTask microbiologyTask) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        getJdbcTemplate().update("UPDATE lab208 SET lab208c3 = ?, lab208c5 = ?, lab04c1 = ? WHERE lab208c1 = ?", microbiologyTask.getComment(), timestamp, microbiologyTask.getUser().getId(), microbiologyTask.getId());

        microbiologyTask.setLastTransaction(timestamp);
        return microbiologyTask;
    }

    /**
     * Consulta la trazabilidad de microbiologia para una orden y examen
     *
     * @param order Numero de orden.
     * @param test Id del examen.
     * @return Trazabilidad de la orden.
     */
    default List<AuditEvent> listTrackingMicrobiologyTask(Long order, Integer test)
    {
        try
        {
            Object[] params = null;
            String query = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT lab03.lab03c2, lab03.lab03c4, lab03c5, "
                    + " lab04.lab04c1, lab04.lab04c4 "
                    + " FROM lab03 "
                    + "INNER JOIN lab04 ON lab04.lab04c1 = lab03.lab04c1 ";

            String where = "";
            if (test != null)
            {
                where = " WHERE lab03.lab22c1 = ? AND lab03c1 = ? AND lab03c3 = 'MT' AND lab03c2 IN('I', 'U')";
                params = new Object[]
                {
                    order, test
                };
            } else
            {
                where = " WHERE lab03.lab22c1 = ? AND lab03c3 = 'MT' AND lab03c2 IN('I', 'U')";
                params = new Object[]
                {
                    order
                };
            }

            return getJdbcTemplate().query(query + where, params, (ResultSet rs, int i) ->
            {
                AuditEvent event = new AuditEvent();
                event.setDate(rs.getTimestamp("lab03c5"));
                event.setUser(new AuthorizedUser(rs.getInt("lab04c1")));
                event.getUser().setUserName(rs.getString("lab04c4"));
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
     * Consulta las tareas asociadas a una orden, un examen que esten o no
     * reportadas
     *
     * @param idOrder Numero de Orden.
     * @param idTest Id del examen.
     * @param isReported Indica si la consulta trae tareas reportadas.
     * @return Lista de Resultados de Microbiologia
     */
    default List<MicrobiologyTask> listMicrobiologyTaskReported(Long idOrder, Integer idTest, boolean isReported)
    {
        try
        {
            Object[] params = null;
            String query = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT lab208.lab22c1, lab208c1, lab208c2, lab208c3, lab208c4, lab208c5, lab208c6, lab208c7, "
                    + "lab208.lab39c1, lab39.lab39c3, lab39.lab39c4, "
                    + "lab208.lab207c1, lab207c2, lab207c3, "
                    + "lab208.lab04c1, lab04.lab04c2, lab04.lab04c3, lab04.lab04c4, "
                    + "lab208.lab04c1_1, lab04_1.lab04c2 AS lab04c2_1, lab04_1.lab04c3 AS lab04c3_1, lab04_1.lab04c4 AS lab04c4_1, "
                    + "lab22.lab21c1, lab21.lab21c2, lab21.lab21c3, lab21.lab21c4, lab21.lab21c5, lab21.lab21c6 "
                    + "FROM lab208 "
                    + "INNER JOIN lab207 ON lab207.lab207c1 = lab208.lab207c1 "
                    + "INNER JOIN lab22 ON lab22.lab22c1 = lab208.lab22c1 "
                    + "INNER JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1 "
                    + "INNER JOIN lab39 ON lab39.lab39c1 = lab208.lab39c1 "
                    + "INNER JOIN lab04 ON lab04.lab04c1 = lab208.lab04c1 "
                    + "INNER JOIN lab04 AS lab04_1 ON lab04_1.lab04c1 = lab208.lab04c1 ";

            if (isReported)
            {
                query += "WHERE lab22.lab22c1 = ? AND lab39.lab39c1 = ? AND lab208c6 = 1";
            } else
            {
                query += "WHERE lab22.lab22c1 = ? AND lab39.lab39c1 = ? AND lab208c6 = 0";
            }

            return getJdbcTemplate().query(query,
                    new Object[]
                    {
                        idOrder, idTest
                    }, (ResultSet rs, int i) ->
            {
                MicrobiologyTask task = new MicrobiologyTask();
                task.setOrder(rs.getLong("lab22c1"));
                task.setIdTest(rs.getInt("lab39c1"));
                task.setAbbrTest(rs.getString("lab39c3"));
                task.setNameTest(rs.getString("lab39c4"));
                task.setId(rs.getInt("lab208c1"));
                task.setIdRecord(rs.getInt("lab208c2"));
                task.setComment(rs.getString("lab208c3"));
                task.setType(rs.getShort("lab208c4"));
                task.setReported(rs.getInt("lab208c6") == 1);
                //Paciente
                task.getPatient().setId(rs.getInt("lab21c1"));
                task.getPatient().setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                task.getPatient().setName1(Tools.decrypt(rs.getString("lab21c3")));
                task.getPatient().setName2(Tools.decrypt(rs.getString("lab21c4")));
                task.getPatient().setLastName(Tools.decrypt(rs.getString("lab21c5")));
                task.getPatient().setSurName(Tools.decrypt(rs.getString("lab21c6")));
                //Destino
                MicrobiologyDestination destination = new MicrobiologyDestination();
                destination.setId(rs.getInt("lab207c1"));
                destination.setCode(rs.getString("lab207c2"));
                destination.setName(rs.getString("lab207c3"));
                task.setDestination(destination);
                /*Usuario que Actualizo*/
                task.setLastTransaction(rs.getTimestamp("lab208c5"));
                task.getUser().setId(rs.getInt("lab04c1"));
                task.getUser().setName(rs.getString("lab04c2"));
                task.getUser().setLastName(rs.getString("lab04c3"));
                task.getUser().setUserName(rs.getString("lab04c4"));
                /*Usuario que Actualizo*/
                task.setDateRegister(rs.getTimestamp("lab208c7"));
                task.getUserRegister().setId(rs.getInt("lab04c1_1"));
                task.getUserRegister().setName(rs.getString("lab04c2_1"));
                task.getUserRegister().setLastName(rs.getString("lab04c3_1"));
                task.getUserRegister().setUserName(rs.getString("lab04c4_1"));

                task.setTasks(getMicrobiologyDestinationTask(task.getId()));
                return task;
            });

        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Reporta tareas de microbiologia del examen de la orden.
     *
     * @param idOrder Numero de la Orden.
     * @param idTest Numero de la Orden.
     * @param idUser Numero de la Orden.
     * @param isReport Indica si se van a reportar las tareas o se van a
     * reiniciar las tareas.
     *
     * @return Cantidad de datos afectados.
     */
    default int updateStateMicrobiologyTask(Long idOrder, Integer idTest, Integer idUser, boolean isReport)
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        int quantity;
        if (isReport)
        {
            quantity = getJdbcTemplate().update("UPDATE lab208 SET lab208c6 = 1, lab208c5 = ?, lab04c1 = ? WHERE lab22c1 = ? AND lab39c1 = ? AND lab208c6 = 0", timestamp, idUser, idOrder, idTest);
        } else
        {
            quantity = getJdbcTemplate().update("UPDATE lab208 SET lab208c6 = 0, lab208c5 = ?, lab04c1 = ? WHERE lab22c1 = ? AND lab39c1 = ? AND lab208c6 = 1", timestamp, idUser, idOrder, idTest);
        }

        return quantity;
    }

    /**
     * Lista examenes pendientes de verificación de microbiología
     *
     * @param resultFilter filtros para la consulta
     * @return Lista
     * @throws Exception Error en base de datos
     */
    default List<ResultTest> listMicrobiologyVerificationPending(final MicrobiologyFilter resultFilter) throws Exception
    {
        try
        {
            String select = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT DISTINCT  lab57.lab22c1 "
                    + "       , lab43.lab43c1 " //area id
                    + "       , lab43.lab43c2 " //area codigo
                    + "       , lab43.lab43c3 " //area abreviatura
                    + "       , lab43.lab43c4 " //area nombre
                    + "       , lab45.lab45c1 " //unidad id
                    + "       , lab45.lab45c2 " //unidad nombre
                    + "       , lab45.lab45c3 " //unidad internacional
                    + "       , lab45.lab45c4 " //unidad factor de conversion
                    + "       , lab57.lab39c1 " //examen
                    + "       , lab39c2 " //código
                    + "       , lab39c3 " //abreviatura
                    + "       , lab39c4 " //nombre
                    + "       , lab57c1 " //resultado
                    + "       , lab57c2 " //fecha resultado
                    + "       , lab57c3 " //usuario del resultado
                    + "       , lab57c5 " //usuario de ingreso
                    + "       , lab57c8 " //estado
                    + "       , lab57c16 " //estado muestra
                    + "       , lab39c11 " //tipo resultado
                    + "       , lab57c9 " //patología
                    + "       , lab57.lab48c12 " //referecnia mínima
                    + "       , lab57.lab48c13 " //referec2nia máxima
                    + "       , lab57.lab48c5 " //panico minimo
                    + "       , lab57.lab48c6 " //panico máximo
                    + "       , lab57.lab48c14 " //reportable minimo
                    + "       , lab57.lab48c15 " //reportable máximo
                    + "       , lab57.lab57c22 " //
                    + "       , lab57.lab57c23 " //
                    + "       , lab48c16 " //pánico crítico
                    + "       , lab50n.lab50c2 as lab50c2n " //normal literal
                    + "       , lab50p.lab50c2 as lab50c2p " //pánico literal
                    + "       , lab39c3 " //abreviatura
                    + "       , lab39c12 " //decimales
                    + "       , lab39c27 " //confidencial
                    + "       , lab57c32 " //tiene comentario
                    + "       , lab57c33 " //numero de repeticiones
                    + "       , lab57c24 " //numero modificacion
                    + "       , lab95c1 " //comentario
                    + "       , lab95c2 " //fecha comentario
                    + "       , lab95c3 " //patología comentario
                    + "       , lab95.lab04c1 " //usuario comentario
                    + "       , lab57.lab57c27 " //delta mínimo
                    + "       , lab57.lab57c28 " //delta máximmo
                    + "       , lab57.lab57c6" //último resultado
                    + "       , lab57.lab57c7" //fecha último resultado
                    + "       , lab57.lab57c30" //penúltimo resultado
                    + "       , lab57.lab57c31" //fecha penúltimo resultado
                    + "       , lab57.lab57c18" //fecha validacion
                    + "       , lab57.lab57c19" //fecha validacion
                    + "       , lab57.lab57c4" //fecha ingreso
                    + "       , lt.lab24c10" //tipo laboratorio
                    + "       , lab57c35" //tipo ingreso resultado
                    + "       , lab57c36" //tipo ingreso microbiología
                    + "       , lab57c37" //Fecha verificación
                    + "       , lab57c38" //Id usuario verifica
                    + "       , lab57c39" //Fecha Toma
                    + "       , lab57c40" //Usuario toma
                    + "";

            String from = ""
                    + " FROM    lab57 "
                    + " INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1 "
                    + " INNER JOIN lab43 ON lab43.lab43c1 = lab39.lab43c1 "
                    + " INNER JOIN lab24 AS lt ON lt.lab24c1 = lab39.lab24c1 "
                    + " INNER JOIN lab164 ON lab164.lab39c1 = lab39.lab39c1 "
                    + " LEFT JOIN lab45 ON lab45.lab45c1 = lab39.lab45c1 "
                    + " LEFT JOIN lab48 ON lab48.lab48c1 = lab57.lab48c1 "
                    + " LEFT JOIN lab50 lab50n ON lab50n.lab50c1 = lab48.lab50c1_3 "
                    + " LEFT JOIN lab50 lab50p ON lab50p.lab50c1 = lab48.lab50c1_1 "
                    + " LEFT JOIN lab95 ON lab95.lab22c1 = lab57.lab22c1 AND lab95.lab39c1 = lab57.lab39c1 "
                    + " LEFT JOIN lab200 ON lab200.lab22c1 = lab57.lab22c1 AND lab200.lab39c1 = lab57.lab39c1 ";
            String where = ""
                    + " WHERE lab39c37 = 0 AND lab200.lab22c1 IS NULL";//Mirar con Edwin

            List<Object> params = new ArrayList<>();

            if (resultFilter != null)
            {
                switch (resultFilter.getRangeType())
                {
                    case 1:
                        where = where + " AND lab57.lab22c1 BETWEEN ? AND ?";
                        params.add(resultFilter.getInit());
                        params.add(resultFilter.getEnd());
                        break;
                    case 0:
                        where = where + " AND lab57.lab57c34 BETWEEN ? AND ?";
                        params.add(resultFilter.getInit());
                        params.add(resultFilter.getEnd());
                        break;
                    default:
                        break;
                }

            }

            RowMapper mapper = (RowMapper<ResultTest>) (ResultSet rs, int i) ->
            {
                ResultTest bean = new ResultTest();
                bean.setOrder(rs.getLong("lab22c1"));
                bean.setTestId(rs.getInt("lab39c1"));
                bean.setTestCode(rs.getString("lab39c2"));
                bean.setTestName(rs.getString("lab39c4"));
                bean.setEntryDate(rs.getTimestamp("lab57c4"));
                bean.setEntryUserId(rs.getInt("lab57c5") == 0 ? null : rs.getInt("lab57c5"));

                bean.setVerificationDate(rs.getTimestamp("lab57c37"));
                bean.setVerificationUserId(rs.getInt("lab57c38") == 0 ? null : rs.getInt("lab57c38"));

                bean.setResult(Tools.decrypt(rs.getString("lab57c1")));
                bean.setResultDate(rs.getTimestamp("lab57c2"));
                bean.setResultUserId(rs.getInt("lab57c3"));

                bean.setValidationDate(rs.getTimestamp("lab57c18"));
                bean.setValidationUserId(rs.getInt("lab57c19") == 0 ? null : rs.getInt("lab57c19"));

                bean.setPrintDate(rs.getTimestamp("lab57c22"));
                bean.setPrintUserId(rs.getInt("lab57c23") == 0 ? null : rs.getInt("lab57c23"));

                bean.setTakenDate(rs.getTimestamp("lab57c39"));
                bean.setTakenUserId(rs.getInt("lab57c40") == 0 ? null : rs.getInt("lab57c40"));

                bean.setEntryType(rs.getShort("lab57c35"));
                bean.setState(rs.getInt("lab57c8"));
                bean.setSampleState(rs.getInt("lab57c16"));
                bean.setAreaId(rs.getInt("lab43c1"));
                bean.setAreaCode(rs.getString("lab43c2"));
                bean.setAreaAbbr(rs.getString("lab43c3"));
                bean.setAreaName(rs.getString("lab43c4"));
                bean.setResultType(rs.getShort("lab39c11"));
                bean.setPathology(rs.getInt("lab57c9"));
                bean.setRefLiteral(rs.getString("lab50c2n"));
                bean.setPanicLiteral(rs.getString("lab50c2p"));
                bean.setConfidential(rs.getInt("lab39c27") == 1);
                bean.setRepeatAmmount(rs.getInt("lab57c33"));
                bean.setModificationAmmount(rs.getInt("lab57c24"));

                BigDecimal refMin = rs.getBigDecimal("lab48c12");
                BigDecimal refMax = rs.getBigDecimal("lab48c13");
                if (!rs.wasNull())
                {
                    bean.setRefMin(refMin);
                    bean.setRefMax(refMax);
                    bean.setRefInterval(refMin.toString() + " - " + refMax.toString());
                } else
                {
                    bean.setRefMin(BigDecimal.ZERO);
                    bean.setRefMax(BigDecimal.ZERO);
                    bean.setRefInterval(bean.getRefLiteral());
                }

                BigDecimal panicMin = rs.getBigDecimal("lab48c5");
                BigDecimal panicMax = rs.getBigDecimal("lab48c6");
                if (!rs.wasNull())
                {
                    bean.setPanicMin(panicMin);
                    bean.setPanicMax(panicMax);

                    String bigDecimalPanicMin = String.valueOf(panicMin.doubleValue());
                    String bigDecimalPanicMax = String.valueOf(panicMax.doubleValue());
                    bean.setPanicInterval(bigDecimalPanicMin + " - " + bigDecimalPanicMax);
                } else
                {
                    bean.setPanicMin(BigDecimal.ZERO);
                    bean.setPanicMax(BigDecimal.ZERO);
                    bean.setPanicInterval(bean.getPanicLiteral());
                }

                BigDecimal reportedMin = rs.getBigDecimal("lab48c14");
                BigDecimal reportedMax = rs.getBigDecimal("lab48c15");
                if (!rs.wasNull())
                {
                    bean.setReportedMin(reportedMin);
                    bean.setReportedMax(reportedMax);
                    String bigDecimalReportedMin = String.valueOf(reportedMin.doubleValue());
                    String bigDecimalReportedMax = String.valueOf(reportedMax.doubleValue());
                    bean.setReportedInterval(bigDecimalReportedMin + " - " + bigDecimalReportedMax);
                } else
                {
                    bean.setReportedMin(BigDecimal.ZERO);
                    bean.setReportedMax(BigDecimal.ZERO);
                }

                BigDecimal deltaMin = rs.getBigDecimal("lab57c27");
                BigDecimal deltaMax = rs.getBigDecimal("lab57c28");
                if (!rs.wasNull())
                {
                    bean.setDeltaMin(deltaMin);
                    bean.setDeltaMax(deltaMax);
                    bean.setDeltaInterval(deltaMin.toString() + " - " + deltaMax.toString());
                } else
                {
                    bean.setDeltaMin(BigDecimal.ZERO);
                    bean.setDeltaMax(BigDecimal.ZERO);
                    bean.setDeltaInterval("");
                }

                bean.setLastResult(Tools.decrypt(rs.getString("lab57c6")));
                bean.setLastResultDate(rs.getDate("lab57c7"));
                bean.setSecondLastResult(Tools.decrypt(rs.getString("lab57c30")));
                bean.setSecondLastResultDate(rs.getDate("lab57c31"));

                bean.setCritic(rs.getShort("lab48c16"));
                bean.setUnitId(rs.getInt("lab45c1"));
                bean.setUnit(rs.getString("lab45c2"));
                bean.setUnitInternational(rs.getString("lab45c3"));
                bean.setUnitConversionFactor(rs.getBigDecimal("lab45c4"));
                bean.setAbbreviation(rs.getString("lab39c3"));
                bean.setDigits(rs.getShort("lab39c12"));
                bean.setHasComment(rs.getShort("lab57c32") == 1);

                ResultTestComment comment = new ResultTestComment();
                comment.setOrder(rs.getLong("lab22c1"));
                comment.setTestId(rs.getInt("lab39c1"));
                comment.setComment(rs.getString("lab95c1"));
                comment.setCommentDate(rs.getDate("lab95c2"));
                comment.setPathology(rs.getShort("lab95c3"));
                comment.setUserId(rs.getInt("lab04c1"));
                comment.setCommentChanged(false);
                bean.setResultComment(comment);
                bean.setLaboratoryType(rs.getString("lab24c10"));
                bean.setEntryTestType(rs.getShort("lab57c36") == 0 ? null : rs.getShort("lab57c36"));

                return bean;
            };

            return getJdbcTemplate().query(select + from + where, mapper, params.toArray());

        } catch (EmptyResultDataAccessException ex)
        {
            ex.getMessage();
            return new ArrayList<>(0);
        }
    }

    /**
     * Consulta la plantilla de resultados asociada a una orden y examen.
     *
     * @param idOrder Numero de Orden.
     * @param idTest Id del examen.
     * @return Plantilla de Resultados.
     */
    default List<OptionTemplate> listResultTemplate(Long idOrder, Integer idTest)
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(idOrder));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab58 = year.equals(currentYear) ? "lab58" : "lab58_" + year;

            String query = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT lab51.lab51c1, lab51.lab51c2, lab51.lab51c3, lab51.lab51c4, lab51.lab39c1, "
                    + "lab58.lab58c1, lab58.lab58c2, lab58.lab58c3, lab58.lab58c4, lab58.lab22c1, lab58.lab04c1, lab04c2, lab04c3, lab04c4 "
                    + "FROM lab51 "
                    + "FULL JOIN " + lab58 + " as lab58 ON lab58.lab22c1 = ? AND lab58.lab39c1 = lab51.lab39c1 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab58.lab04c1 "
                    + "WHERE lab51.lab39c1 = ?";

            return getJdbcTemplate().query(query,
                    new Object[]
                    {
                        idOrder, idTest
                    }, (ResultSet rs, int i) ->
            {
                OptionTemplate optionTemplate = new OptionTemplate();
                if (rs.getString("lab58c4") == null || rs.getString("lab58c4").isEmpty())
                {
                    optionTemplate.setId(rs.getInt("lab51c1"));
                    optionTemplate.setOption(rs.getString("lab51c2"));
                    optionTemplate.setComment(rs.getString("lab51c3"));
                    optionTemplate.setSort(rs.getInt("lab51c4"));
                    optionTemplate.setIdTest(rs.getInt("lab39c1"));
                    optionTemplate.setResults(listResultTemplate(optionTemplate.getId()));
                } else
                {
                    OptionTemplate beforeTemplate = Tools.jsonObject(rs.getString("lab58c4"), OptionTemplate.class);
                    optionTemplate.setId(beforeTemplate.getId());
                    optionTemplate.setOption(beforeTemplate.getOption());
                    optionTemplate.setComment(beforeTemplate.getComment());
                    optionTemplate.setSort(beforeTemplate.getSort());
                    optionTemplate.setIdTest(beforeTemplate.getIdTest());
                    optionTemplate.setResults(beforeTemplate.getResults());
                }

                optionTemplate.setOrder(rs.getLong("lab22c1"));
                optionTemplate.setResult(rs.getString("lab58c1"));
                optionTemplate.setNormalResult(rs.getString("lab58c3"));

                optionTemplate.setLastTransaction(rs.getTimestamp("lab58c2"));
                //Usuario
                optionTemplate.getUser().setId(rs.getInt("lab04c1"));
                optionTemplate.getUser().setName(rs.getString("lab04c2"));
                optionTemplate.getUser().setLastName(rs.getString("lab04c3"));
                optionTemplate.getUser().setUserName(rs.getString("lab04c4"));

                return optionTemplate;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Consulta la plantilla de resultados asociada a una orden y examen.
     *
     * @param idTemplate Id de la Plantilla de Resultados.
     * @return Plantilla de Resultados.
     */
    default List<ResultTemplate> listResultTemplate(int idTemplate)
    {
        try
        {
            String query = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT lab146.lab146c1"
                    + ", lab146.lab146c2"
                    + ", lab146.lab146c3"
                    + ", lab146.lab146c4"
                    + " FROM lab146 "
                    + "WHERE lab146.lab51c1 = ?";

            return getJdbcTemplate().query(query,
                    new Object[]
                    {
                        idTemplate
                    }, (ResultSet rs, int i) ->
            {
                ResultTemplate result = new ResultTemplate();
                result.setResult(rs.getString("lab146c1"));
                result.setSort(rs.getInt("lab146c2"));
                result.setReference(rs.getInt("lab146c3") == 1);
                result.setComment(rs.getString("lab146c4"));
                return result;
            });

        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Consulta si una orden y examen tienen asociada plantilla de resultados.
     *
     * @param idOrder Numero de la orden.
     * @param idTest Id del examen.
     * @return Indica si el resultado tiene plantilla de resultados.
     */
    default boolean getHasTemplate(long idOrder, int idTest)
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(idOrder));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab58 = year.equals(currentYear) ? "lab58" : "lab58_" + year;

            String query = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT COUNT(*) AS quantity "
                    + "FROM  " + lab58 + " as lab58 "
                    + "WHERE lab22c1 = ? AND lab39c1 = ? ";

            return getJdbcTemplate().queryForObject(query,
                    new Object[]
                    {
                        idOrder, idTest
                    }, (ResultSet rs, int i) -> rs.getInt("quantity") > 0);

        } catch (EmptyResultDataAccessException ex)
        {
            return false;
        }
    }

    /**
     * Inserta la plantilla de resultados asociada a una orden y examen.
     *
     * @param templates Plantillas de resultados a ser insertadas.
     * @param idOrder Numero de la Orden.
     * @param idTest Id del examen.
     * @param user Usuario que ingreso.
     * @param userdatabank
     *
     * @return Retorna la cantidad de registros afectados.
     * @throws Exception Error en la base de datos.
     */
    default int insertResultTemplate(List<OptionTemplate> templates, Long idOrder, Integer idTest, AuthorizedUser user, Integer userdatabank) throws Exception
    {

        Integer year = Tools.YearOfOrder(String.valueOf(idOrder));
        Integer currentYear = DateTools.dateToNumberYear(new Date());
        String lab58 = year.equals(currentYear) ? "lab58" : "lab58_" + year;

        Timestamp timestamp = new Timestamp(new Date().getTime());
        List<HashMap> batchArray = new ArrayList<>();
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName(lab58);

        deleteResultTemplate(idOrder, idTest);
        for (OptionTemplate template : templates)
        {
            OptionTemplate optionTemplate = new OptionTemplate();
            optionTemplate.setId(template.getId());
            optionTemplate.setOption(template.getOption());
            optionTemplate.setComment(template.getComment());
            optionTemplate.setSort(template.getSort());
            optionTemplate.setIdTest(idTest);
            optionTemplate.setResults(template.getResults());

            HashMap parameters = new HashMap();
            parameters.put("lab22c1", idOrder);
            parameters.put("lab39c1", idTest);
            parameters.put("lab58c1", template.getResult());
            parameters.put("lab58c2", timestamp);
            parameters.put("lab58c3", template.getNormalResult());
            parameters.put("lab58c4", Tools.jsonObject(optionTemplate));
            parameters.put("lab04c1", userdatabank == null ? user.getId() : userdatabank);
            batchArray.add(parameters);
        }

        int[] inserted = insert.executeBatch(batchArray.toArray(new HashMap[batchArray.size()]));

        return inserted.length;
    }

    /**
     * Elimina los resultados de plantillas asociados a una orden y examen.
     *
     * @param idOrder Id de la orden a la que se le borrara la plantilla.
     * @param idTest Id del examen al que se le borrara la plantilla.
     *
     * @return Retorna la cantidad de registros afectados.
     * @throws Exception Error en la base de datos.
     */
    default int deleteResultTemplate(long idOrder, int idTest) throws Exception
    {
        Integer year = Tools.YearOfOrder(String.valueOf(idOrder));
        Integer currentYear = DateTools.dateToNumberYear(new Date());
        String lab58 = year.equals(currentYear) ? "lab58" : "lab58_" + year;

        String deleteSql = "DELETE FROM " + lab58 + " WHERE lab22c1 = ? AND lab39c1 = ?";
        return getJdbcTemplate().update(deleteSql, idOrder, idTest);
    }

    /**
     * Consulta los microorganismos asociados a un examen
     *
     * @param idOrder Numero de orden
     * @param idTest Id del examen
     * @return Detección microbial (Microorganismos)
     */
    default List<Microorganism> getByTest(Long idOrder, Integer idTest)
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(idOrder));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab204 = year.equals(currentYear) ? "lab204" : "lab204_" + year;

            Object[] params = null;
            String query = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT lab76.lab76c1, lab76c2, lab76c3, lab76.lab07c1, lab204c1, lab204c2, lab204c4, lab204c6 "
                    + "FROM lab206 "
                    + "LEFT JOIN lab76 ON lab206.lab76c1 = lab76.lab76c1 "
                    + "LEFT JOIN " + lab204 + " as lab204 ON lab204.lab22c1 = ? AND lab204.lab39c1 = ? AND lab206.lab76c1 = lab204.lab76c1 "
                    + "WHERE lab206.lab39c1 = ? OR lab206.lab39c1 IS NULL";

            return getJdbcTemplate().query(query,
                    new Object[]
                    {
                        idOrder, idTest, idTest
                    }, (ResultSet rs, int i) ->
            {
                Microorganism microorganism = new Microorganism();
                microorganism.setId(rs.getInt("lab76c1"));
                microorganism.setName(rs.getString("lab76c2"));
                microorganism.setIdMicrobialDetection(rs.getString("lab204c1") == null ? null : rs.getInt("lab204c1"));
                microorganism.setSelected(rs.getString("lab204c1") != null);
                microorganism.setComment(rs.getString("lab204c2"));
                microorganism.setRecount(rs.getString("lab204c4"));
                microorganism.setComplementations(rs.getString("lab204c6"));

                if (microorganism.getIdMicrobialDetection() != null)
                {
                    microorganism.setResultsMicrobiology(listResultMicrobiologySensitivity(microorganism.getIdMicrobialDetection(), idOrder));
                    microorganism.setCountResultMicrobiology( microorganism.getResultsMicrobiology().size());
                }
                return microorganism;
            });

        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>();
        }
    }

}
