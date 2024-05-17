package net.cltech.enterprisent.dao.interfaces.migration;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.migration.Inconsistency;
import net.cltech.enterprisent.domain.operation.orders.DemographicValue;
import net.cltech.enterprisent.domain.operation.orders.Patient;
import net.cltech.enterprisent.tools.Tools;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información de las
 * inconsistencias.
 *
 * @version 1.0.0
 * @author cmartin
 * @since 07/11/2017
 * @see Creación
 */
public interface InconsistencyDao
{

    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getConnection();

    /**
     * *
     * Inserta las inconsistencias en la base de datos.
     *
     * @param inconsistency Inconsistencias encontradas al insertar una orden.
     */
    default void create(Inconsistency inconsistency) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getConnection())
                .withTableName("lab33");

        HashMap parameters = new HashMap();
        parameters.put("lab22c1", inconsistency.getOrderNumber());
        parameters.put("lab33c1", Tools.jsonObject(inconsistency.getPatientHIS()));
        parameters.put("lab33c2", Tools.jsonObject(inconsistency.getPatientLIS()));
        parameters.put("lab04c1", inconsistency.getUser().getId());
        parameters.put("lab33c3", timestamp);
        parameters.put("lab33c4", inconsistency.getInconsistencies());

        insert.execute(parameters);

        getConnection().update("UPDATE lab22 SET lab22c10 = 1 WHERE lab22c1 = ?", inconsistency.getOrderNumber());

        getConnection().update("UPDATE lab21 SET lab21c15 = 1 WHERE lab21c1 = ?", inconsistency.getPatientLIS().getId());
    }

    /**
     * Consulta las inconsistencias registradas.
     *
     * @param idOrder
     * @return Retorna una lista de inconsistencias.
     */
    default Inconsistency getInconsistency(long idOrder)
    {
        try
        {
            String query = "SELECT lab22c1, lab33c1, lab33c2, lab33c3, lab33c4, lab04.lab04c1, lab04.lab04c2, lab04.lab04c3 FROM lab33 "
                    + "INNER JOIN lab04 ON lab04.lab04c1 = lab33.lab04c1 "
                    + "WHERE lab22c1 = ?";

            return getConnection().queryForObject(query,
                    new Object[]
                    {
                        idOrder
                    }, (ResultSet rs, int i) ->
            {
                Inconsistency inconsistency = new Inconsistency();
                inconsistency.setOrderNumber(rs.getLong("lab22c1"));
                inconsistency.setDate(rs.getTimestamp("lab33c3"));
                inconsistency.setInconsistencies(rs.getString("lab33c4"));
                //Paciente HIS
                inconsistency.setPatientHIS(Tools.jsonObject(rs.getString("lab33c1"), Patient.class));
                inconsistency.getPatientHIS().setPatientId(Tools.decrypt(inconsistency.getPatientHIS().getPatientId()));
                inconsistency.getPatientHIS().setName1(Tools.decrypt(inconsistency.getPatientHIS().getName1()));
                inconsistency.getPatientHIS().setName2(Tools.decrypt(inconsistency.getPatientHIS().getName2()));
                inconsistency.getPatientHIS().setLastName(Tools.decrypt(inconsistency.getPatientHIS().getLastName()));
                inconsistency.getPatientHIS().setSurName(Tools.decrypt(inconsistency.getPatientHIS().getSurName()));
                //Paciente LIS
                inconsistency.setPatientLIS(Tools.jsonObject(rs.getString("lab33c2"), Patient.class));
                inconsistency.getPatientLIS().setPatientId(Tools.decrypt(inconsistency.getPatientLIS().getPatientId()));
                inconsistency.getPatientLIS().setName1(Tools.decrypt(inconsistency.getPatientLIS().getName1()));
                inconsistency.getPatientLIS().setName2(Tools.decrypt(inconsistency.getPatientLIS().getName2()));
                inconsistency.getPatientLIS().setLastName(Tools.decrypt(inconsistency.getPatientLIS().getLastName()));
                inconsistency.getPatientLIS().setSurName(Tools.decrypt(inconsistency.getPatientLIS().getSurName()));
                //Usuario
                inconsistency.getUser().setId(rs.getInt("lab04c1"));
                inconsistency.getUser().setName(rs.getString("lab04c2"));
                inconsistency.getUser().setLastName(rs.getString("lab04c3"));
                return inconsistency;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Consulta la trazabilidad de la muestra.
     *
     * @param init Fecha Inicial
     * @param end Fecha Final
     * @return Retorna la trazabilidad de la muestra.
     */
    default List<Inconsistency> list(Date init, Date end)
    {
        try
        {
            String query = "SELECT lab22c1, lab33c1, lab33c2, lab33c3, lab33c4, lab04.lab04c1, lab04.lab04c2, lab04.lab04c3 FROM lab33 "
                    + "INNER JOIN lab04 ON lab04.lab04c1 = lab33.lab04c1 "
                    + "WHERE lab33c3 BETWEEN ? AND ?";

            return getConnection().query(query,
                    new Object[]
                    {
                        init, end
                    }, (ResultSet rs, int i) ->
            {
                Inconsistency inconsistency = new Inconsistency();
                inconsistency.setOrderNumber(rs.getLong("lab22c1"));
                inconsistency.setDate(rs.getTimestamp("lab33c3"));
                inconsistency.setInconsistencies(rs.getString("lab33c4"));
                //Paciente HIS
                inconsistency.setPatientHIS(Tools.jsonObject(rs.getString("lab33c1"), Patient.class));
                inconsistency.getPatientHIS().setPatientId(Tools.decrypt(inconsistency.getPatientHIS().getPatientId()));
                inconsistency.getPatientHIS().setName1(Tools.decrypt(inconsistency.getPatientHIS().getName1()));
                inconsistency.getPatientHIS().setName2(Tools.decrypt(inconsistency.getPatientHIS().getName2()));
                inconsistency.getPatientHIS().setLastName(Tools.decrypt(inconsistency.getPatientHIS().getLastName()));
                inconsistency.getPatientHIS().setSurName(Tools.decrypt(inconsistency.getPatientHIS().getSurName()));
                //Paciente LIS
                inconsistency.setPatientLIS(Tools.jsonObject(rs.getString("lab33c2"), Patient.class));
                inconsistency.getPatientLIS().setPatientId(Tools.decrypt(inconsistency.getPatientLIS().getPatientId()));
                inconsistency.getPatientLIS().setName1(Tools.decrypt(inconsistency.getPatientLIS().getName1()));
                inconsistency.getPatientLIS().setName2(Tools.decrypt(inconsistency.getPatientLIS().getName2()));
                inconsistency.getPatientLIS().setLastName(Tools.decrypt(inconsistency.getPatientLIS().getLastName()));
                inconsistency.getPatientLIS().setSurName(Tools.decrypt(inconsistency.getPatientLIS().getSurName()));
                //Usuario
                inconsistency.getUser().setId(rs.getInt("lab04c1"));
                inconsistency.getUser().setName(rs.getString("lab04c2"));
                inconsistency.getUser().setLastName(rs.getString("lab04c3"));
                return inconsistency;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Actualiza la información de un paciente a favor del HIS en la base de
     * datos.
     *
     * @param patient Instancia con los datos del paciente.
     * @param user Usuario
     * @param demographicValue Demografico de inconsistencias.
     *
     * @throws Exception Error en la base de datos.
     */
    default void updatePatient(Patient patient, AuthorizedUser user, DemographicValue demographicValue) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        if (demographicValue != null && demographicValue.getIdDemographic() != 0)
        {
            if (demographicValue.isEncoded())
            {
                getConnection().update("UPDATE lab21 SET lab21c2 = ?, lab21c3 = ?, lab21c4 = ?, lab21c5 = ?, lab21c6 = ?, lab80c1 = ?, lab04c1 = ?, lab21c12 = ?, lab_demo_" + demographicValue.getIdDemographic() + " = ?"
                        + "WHERE lab21c1 = ?",
                        patient.getPatientId(), patient.getName1(), patient.getName2(), patient.getLastName(), patient.getSurName(), patient.getSex().getId(), user.getId(), timestamp, demographicValue.getCodifiedId(), patient.getId());
            } else
            {
                getConnection().update("UPDATE lab21 SET lab21c2 = ?, lab21c3 = ?, lab21c4 = ?, lab21c5 = ?, lab21c6 = ?, lab80c1 = ?, lab04c1 = ?, lab21c12 = ?, lab_demo_" + demographicValue.getIdDemographic() + " = ?"
                        + "WHERE lab21c1 = ?",
                        patient.getPatientId(), patient.getName1(), patient.getName2(), patient.getLastName(), patient.getSurName(), patient.getSex().getId(), user.getId(), timestamp, demographicValue.getNotCodifiedValue(), patient.getId());
            }
        } else
        {
            getConnection().update("UPDATE lab21 SET lab21c2 = ?, lab21c3 = ?, lab21c4 = ?, lab21c5 = ?, lab21c6 = ?, lab80c1 = ?, lab04c1 = ?, lab21c12 = ? "
                    + "WHERE lab21c1 = ?",
                    patient.getPatientId(), patient.getName1(), patient.getName2(), patient.getLastName(), patient.getSurName(), patient.getSex().getId(), user.getId(), timestamp, patient.getId());
        }

    }

    /**
     * Resuelve las incosistencias en la base de datos.
     *
     * @param idOrder Numero de Orden
     * @param idPatient Id del Paciente
     * @throws Exception Error en la base de datos.
     */
    default void resolveInconsistency(long idOrder, int idPatient) throws Exception
    {
        //Eliminar Inconsistencia por Orden
        getConnection().update("DELETE FROM lab33 WHERE lab22c1 = ?", idOrder);
        //Desbloquear Orden
        getConnection().update("UPDATE lab22 SET lab22c10 = 0 WHERE lab22c1 = ?", idOrder);
        //Desbloquear Paciente
        getConnection().update("UPDATE lab21 SET lab21c15 = 0 WHERE lab21c1 = ?", idPatient);
    }
}
