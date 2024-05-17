package net.cltech.enterprisent.dao.interfaces.operation.tracking;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.common.Item;
import net.cltech.enterprisent.domain.masters.common.Motive;
import net.cltech.enterprisent.domain.masters.demographic.ServiceLaboratory;
import net.cltech.enterprisent.domain.masters.interview.Answer;
import net.cltech.enterprisent.domain.masters.interview.Question;
import net.cltech.enterprisent.domain.masters.test.Laboratory;
import net.cltech.enterprisent.domain.masters.test.Sample;
import net.cltech.enterprisent.domain.masters.test.TestBasic;
import net.cltech.enterprisent.domain.masters.test.Unit;
import net.cltech.enterprisent.domain.masters.tracking.AssignmentDestination;
import net.cltech.enterprisent.domain.masters.tracking.Destination;
import net.cltech.enterprisent.domain.masters.tracking.DestinationRoute;
import net.cltech.enterprisent.domain.operation.common.Reason;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.Patient;
import net.cltech.enterprisent.domain.operation.orders.Test;
import net.cltech.enterprisent.domain.operation.tracking.SampleDelayed;
import net.cltech.enterprisent.domain.operation.tracking.SampleState;
import net.cltech.enterprisent.domain.operation.tracking.SampleTracking;
import net.cltech.enterprisent.domain.operation.tracking.TestSampleTake;
import net.cltech.enterprisent.domain.operation.tracking.TestSampleTakeTracking;
import net.cltech.enterprisent.domain.operation.tracking.TestToRecall;
import net.cltech.enterprisent.domain.operation.tracking.VerifyDestination;
import static net.cltech.enterprisent.tools.Constants.ISOLATION_READ_UNCOMMITTED;
import net.cltech.enterprisent.tools.DateTools;
import net.cltech.enterprisent.tools.SQLTools;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.tools.enums.LISEnum;
import net.cltech.enterprisent.tools.log.orders.OrderCreationLog;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información de la
 * verificación de la muestra.
 *
 * @version 1.0.0
 * @author cmartin
 * @since 19/09/2017
 * @see Creación
 */
public interface SampleTrackingDao
{

    /**
     * Obtiene la conexion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getConnection();

    /**
     * Lista las ordenes por un filtro especifico dado por la clase de listados.
     *
     * @param idOrder Id de la orden.
     * @param laboratorys
     * @param branch
     *
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    default Order getOrder(long idOrder,  List<Integer> laboratorys, int branch) throws Exception
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(idOrder));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;

            String query = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT lab22c1, lab22c2, lab22c3, lab22c4, lab22c5, lab22.lab07c1, lab22c7, lab22c13, lab22c19 , "
                    + "lab103.lab103c1, lab103c2, lab103c3, lab103c4, lab103.lab07c1 AS lab103lab07c1,  "
                    + "lab21.lab21c1, lab21c2, lab21c3, lab21c4, lab21c5, lab21c6, lab21lab80.lab80c1 AS lab21lab80lab80c1, lab21lab80.lab80c2 AS lab21lab80lab80c2, lab21lab80.lab80c3 AS lab21lab80lab80c3, lab21lab80.lab80c4 AS lab21lab80lab80c4, lab21lab80.lab80c5 AS lab21lab80lab80c5, lab21c7, lab21c8, lab21c9, lab21c10, lab21c11, lab21c16, lab21c17, lab21lab08.lab08c1 AS lab21lab08lab08c1, lab21lab08.lab08c2 AS lab21lab08lab08c2, lab21lab08.lab08c4 AS lab21lab08lab08c4, lab21lab54.lab54c1 AS lab21lab54lab54c1, lab21lab54.lab54c2 AS lab21lab54lab54c2, lab21lab54.lab54c3 AS lab21lab54lab54c3, "
                    + "lab05.lab05c1, lab05c10, lab05c4, "
                    + "lab10.lab10c1, lab10c2, lab10c7, lab10c5, lab10c8, "
                    + "lab14.lab14c1, lab14c2, lab14c3, lab14c32, "
                    + "lab19.lab19c1, lab19c2, lab19c3,  "
                    + "lab904.lab904c1, lab904c2, lab904c3 "
                    + "FROM  " + lab22 + " as lab22 "
                    + "LEFT JOIN lab103 ON lab103.lab103c1 = lab22.lab103c1  "
                    + "LEFT JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1  "
                    + "LEFT JOIN lab80 lab21lab80 ON lab21lab80.lab80c1 = lab21.lab80c1  "
                    + "LEFT JOIN lab08 lab21lab08 ON lab21lab08.lab08c1 = lab21.lab08c1  "
                    + "LEFT JOIN lab54 lab21lab54 ON lab21lab54.lab54c1 = lab21.lab54c1  "
                    + "LEFT JOIN lab05 ON lab05.lab05c1 = lab22.lab05c1  "
                    + "LEFT JOIN lab10 ON lab10.lab10c1 = lab22.lab10c1  "
                    + "LEFT JOIN lab14 ON lab14.lab14c1 = lab22.lab14c1  "
                    + "LEFT JOIN lab19 ON lab19.lab19c1 = lab22.lab19c1  "
                    + "LEFT JOIN lab904 ON lab904.lab904c1 = lab22.lab904c1 "
                    + "WHERE lab22c1 = ? AND lab22.lab07c1 = 1 ";

            return getConnection().queryForObject(query,
                    new Object[]
                    {
                        idOrder
                    }, (ResultSet rs, int i) ->
            {
                Order order = new Order();
                order.setOrderNumber(rs.getLong("lab22c1"));
                order.setCreatedDateShort(rs.getInt("lab22c2"));
                order.setCreatedDate(rs.getTimestamp("lab22c3"));
                order.setHomebound(rs.getInt("lab22c4") == 1);
                order.setMiles(rs.getInt("lab22c5"));
                order.setExternalId(rs.getString("lab22c7") == null ? "" : rs.getString("lab22c7"));
                order.setTurn(rs.getString("lab22c13"));
                order.setHasAppointment(rs.getInt("lab22c19"));
                //TIPO DE ORDEN
                order.getType().setId(rs.getString("lab103c1") == null ? null : rs.getInt("lab103c1"));
                order.getType().setCode(rs.getString("lab103c2"));
                order.getType().setName(rs.getString("lab103c3"));
                order.getType().setState(rs.getInt("lab103lab07c1") == 1);
                //PACIENTE
                order.getPatient().setId(rs.getInt("lab21c1"));
                order.getPatient().setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                order.getPatient().setName1(Tools.decrypt(rs.getString("lab21c3")));
                order.getPatient().setName2(Tools.decrypt(rs.getString("lab21c4")));
                order.getPatient().setLastName(Tools.decrypt(rs.getString("lab21c5")));
                order.getPatient().setSurName(Tools.decrypt(rs.getString("lab21c6")));
                order.getPatient().setBirthday(rs.getTimestamp("lab21c7"));
                order.getPatient().setEmail(rs.getString("lab21c8"));
                order.getPatient().setSize(rs.getBigDecimal("lab21c9"));
                order.getPatient().setWeight(rs.getBigDecimal("lab21c10"));
                order.getPatient().setDateOfDeath(rs.getTimestamp("lab21c11"));
                order.getPatient().setPhone(rs.getString("lab21c16"));
                order.getPatient().setAddress(rs.getString("lab21c17"));
                //PACIENTE - SEXO
                order.getPatient().getSex().setId(rs.getInt("lab21lab80lab80c1"));
                order.getPatient().getSex().setIdParent(rs.getInt("lab21lab80lab80c2"));
                order.getPatient().getSex().setCode(rs.getString("lab21lab80lab80c3"));
                order.getPatient().getSex().setEsCo(rs.getString("lab21lab80lab80c4"));
                order.getPatient().getSex().setEnUsa(rs.getString("lab21lab80lab80c5"));
                //PACIENTE - RAZA
                order.getPatient().getRace().setId(rs.getInt("lab21lab08lab08c1"));
                order.getPatient().getRace().setName(rs.getString("lab21lab08lab08c2"));
                order.getPatient().getRace().setValue(rs.getFloat("lab21lab08lab08c4"));
                //PACIENTE - TIPO DE DOCUMENTO
                order.getPatient().getDocumentType().setId(rs.getInt("lab21lab54lab54c1"));
                order.getPatient().getDocumentType().setAbbr(rs.getString("lab21lab54lab54c2"));
                order.getPatient().getDocumentType().setName(rs.getString("lab21lab54lab54c3"));
                //SEDE
                order.getBranch().setId(rs.getInt("lab05c1"));
                order.getBranch().setCode(rs.getString("lab05c10"));
                order.getBranch().setName(rs.getString("lab05c4"));
                //SERVICIO
                order.getService().setId(rs.getInt("lab10c1"));
                order.getService().setCode(rs.getString("lab10c7"));
                order.getService().setName(rs.getString("lab10c2"));
                order.getService().setExternal(rs.getBoolean("lab10c5"));
                order.getService().setHospitalSampling(rs.getBoolean("lab10c8"));
                //EMPRESA
                order.getAccount().setId(rs.getInt("lab14c1"));
                order.getAccount().setNit(rs.getString("lab14c2"));
                order.getAccount().setName(rs.getString("lab14c3"));
                order.getAccount().setEncryptionReportResult(rs.getBoolean("lab14c32"));

                //MEDICO
                order.getPhysician().setId(rs.getInt("lab19c1"));
                order.getPhysician().setName(rs.getString("lab19c2"));
                order.getPhysician().setLastName(rs.getString("lab19c3"));
                //TARIFA
                order.getRate().setId(rs.getInt("lab904c1"));
                order.getRate().setCode(rs.getString("lab904c2"));
                order.getRate().setName(rs.getString("lab904c3"));

                try {
                    listTest(order,laboratorys, branch);
                } catch (Exception ex) {
                    Logger.getLogger(SampleTrackingDao.class.getName()).log(Level.SEVERE, null, ex);
                }
                return order;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Lista las ordenes por un filtro especifico dado por la clase de listados.
     *
     * @param idOrder Id de la orden.
     * @param user
     * @param laboratorys
     * @param branch
     *
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    default Order getOrderTracking(long idOrder, int user, List<Integer> laboratorys, int idbranch ) throws Exception
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(idOrder));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;

            String query = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT lab22c1, lab22c2, lab22c3, lab22c4, lab22c5, lab22.lab07c1, lab22c7, lab22c13, "
                    + "lab103.lab103c1, lab103c2, lab103c3, lab103c4, lab103.lab07c1 AS lab103lab07c1,  "
                    + "lab21.lab21c1, lab21c2, lab21c3, lab21c4, lab21c5, lab21c6, lab21lab80.lab80c1 AS lab21lab80lab80c1, lab21lab80.lab80c2 AS lab21lab80lab80c2, lab21lab80.lab80c3 AS lab21lab80lab80c3, lab21lab80.lab80c4 AS lab21lab80lab80c4, lab21lab80.lab80c5 AS lab21lab80lab80c5, lab21c7, lab21c8, lab21c9, lab21c10, lab21c11, lab21c16, lab21c17, lab21lab08.lab08c1 AS lab21lab08lab08c1, lab21lab08.lab08c2 AS lab21lab08lab08c2, lab21lab08.lab08c4 AS lab21lab08lab08c4, lab21lab54.lab54c1 AS lab21lab54lab54c1, lab21lab54.lab54c2 AS lab21lab54lab54c2, lab21lab54.lab54c3 AS lab21lab54lab54c3, "
                    + "lab05.lab05c1, lab05c10, lab05c4, "
                    + "lab10.lab10c1, lab10c2, lab10c7, lab10c5, lab10c8, "
                    + "lab14.lab14c1, lab14c2, lab14c3, lab14c32, "
                    + "lab19.lab19c1, lab19c2, lab19c3,  "
                    + "lab904.lab904c1, lab904c2, lab904c3 "
                    + "FROM " + lab22 + " as lab22 "
                    + "LEFT JOIN lab103 ON lab103.lab103c1 = lab22.lab103c1  "
                    + "LEFT JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1  "
                    + "LEFT JOIN lab80 lab21lab80 ON lab21lab80.lab80c1 = lab21.lab80c1  "
                    + "LEFT JOIN lab08 lab21lab08 ON lab21lab08.lab08c1 = lab21.lab08c1  "
                    + "LEFT JOIN lab54 lab21lab54 ON lab21lab54.lab54c1 = lab21.lab54c1  "
                    + "LEFT JOIN lab05 ON lab05.lab05c1 = lab22.lab05c1  "
                    + "LEFT JOIN lab10 ON lab10.lab10c1 = lab22.lab10c1  "
                    + "LEFT JOIN lab14 ON lab14.lab14c1 = lab22.lab14c1  "
                    + "LEFT JOIN lab19 ON lab19.lab19c1 = lab22.lab19c1  "
                    + "LEFT JOIN lab904 ON lab904.lab904c1 = lab22.lab904c1 "
                    + "WHERE lab22c1 = ? AND lab22.lab07c1 = 1 AND (lab22c19 = 0 or lab22c19 is null)";

            return getConnection().queryForObject(query,
                    new Object[]
                    {
                        idOrder
                    }, (ResultSet rs, int i) ->
            {
                Order order = new Order();
                order.setOrderNumber(rs.getLong("lab22c1"));
                order.setCreatedDateShort(rs.getInt("lab22c2"));
                order.setCreatedDate(rs.getTimestamp("lab22c3"));
                order.setHomebound(rs.getInt("lab22c4") == 1);
                order.setMiles(rs.getInt("lab22c5"));
                order.setExternalId(rs.getString("lab22c7"));
                order.setTurn(rs.getString("lab22c13"));
                //TIPO DE ORDEN
                order.getType().setId(rs.getString("lab103c1") == null ? null : rs.getInt("lab103c1"));
                order.getType().setCode(rs.getString("lab103c2"));
                order.getType().setName(rs.getString("lab103c3"));
                order.getType().setState(rs.getInt("lab103lab07c1") == 1);
                //PACIENTE
                order.getPatient().setId(rs.getInt("lab21c1"));
                order.getPatient().setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                order.getPatient().setName1(Tools.decrypt(rs.getString("lab21c3")));
                order.getPatient().setName2(Tools.decrypt(rs.getString("lab21c4")));
                order.getPatient().setLastName(Tools.decrypt(rs.getString("lab21c5")));
                order.getPatient().setSurName(Tools.decrypt(rs.getString("lab21c6")));
                order.getPatient().setBirthday(rs.getTimestamp("lab21c7"));
                order.getPatient().setEmail(rs.getString("lab21c8"));
                order.getPatient().setSize(rs.getBigDecimal("lab21c9"));
                order.getPatient().setWeight(rs.getBigDecimal("lab21c10"));
                order.getPatient().setDateOfDeath(rs.getTimestamp("lab21c11"));
                order.getPatient().setPhone(rs.getString("lab21c16"));
                order.getPatient().setAddress(rs.getString("lab21c17"));
                //PACIENTE - SEXO
                order.getPatient().getSex().setId(rs.getInt("lab21lab80lab80c1"));
                order.getPatient().getSex().setIdParent(rs.getInt("lab21lab80lab80c2"));
                order.getPatient().getSex().setCode(rs.getString("lab21lab80lab80c3"));
                order.getPatient().getSex().setEsCo(rs.getString("lab21lab80lab80c4"));
                order.getPatient().getSex().setEnUsa(rs.getString("lab21lab80lab80c5"));
                //PACIENTE - RAZA
                order.getPatient().getRace().setId(rs.getInt("lab21lab08lab08c1"));
                order.getPatient().getRace().setName(rs.getString("lab21lab08lab08c2"));
                order.getPatient().getRace().setValue(rs.getFloat("lab21lab08lab08c4"));
                //PACIENTE - TIPO DE DOCUMENTO
                order.getPatient().getDocumentType().setId(rs.getInt("lab21lab54lab54c1"));
                order.getPatient().getDocumentType().setAbbr(rs.getString("lab21lab54lab54c2"));
                order.getPatient().getDocumentType().setName(rs.getString("lab21lab54lab54c3"));
                //SEDE
                order.getBranch().setId(rs.getInt("lab05c1"));
                order.getBranch().setCode(rs.getString("lab05c10"));
                order.getBranch().setName(rs.getString("lab05c4"));
                //SERVICIO
                order.getService().setId(rs.getInt("lab10c1"));
                order.getService().setCode(rs.getString("lab10c7"));
                order.getService().setName(rs.getString("lab10c2"));
                order.getService().setExternal(rs.getBoolean("lab10c5"));
                order.getService().setHospitalSampling(rs.getBoolean("lab10c8"));
                //EMPRESA
                order.getAccount().setId(rs.getInt("lab14c1"));
                order.getAccount().setNit(rs.getString("lab14c2"));
                order.getAccount().setName(rs.getString("lab14c3"));
                order.getAccount().setEncryptionReportResult(rs.getBoolean("lab14c32"));

                //MEDICO
                order.getPhysician().setId(rs.getInt("lab19c1"));
                order.getPhysician().setName(rs.getString("lab19c2"));
                order.getPhysician().setLastName(rs.getString("lab19c3"));
                //TARIFA
                order.getRate().setId(rs.getInt("lab904c1"));
                order.getRate().setCode(rs.getString("lab904c2"));
                order.getRate().setName(rs.getString("lab904c3"));

                try {
                    listTestTracking(order, user, laboratorys, idbranch);
                } catch (Exception ex) {
                    Logger.getLogger(SampleTrackingDao.class.getName()).log(Level.SEVERE, null, ex);
                }
                return order;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Lista las pruebas de una orden.
     *
     * @param order Objeto de la orden a la que se le quieren consultar los
     * examenes.
     */
    default void listTest(Order order,  List<Integer> laboratorys, int branch) throws Exception
    {
        Integer year = Tools.YearOfOrder(String.valueOf(order.getOrderNumber()));
        Integer currentYear = DateTools.dateToNumberYear(new Date());
        String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;

        String query = ISOLATION_READ_UNCOMMITTED + "SELECT lab57.lab22c1, lab57.lab57c8, lab57.lab57c16, "
                + "lab57.lab57c1, lab57.lab57c2, lab57.lab57c4, lab57.lab57c18, lab57.lab57c20, lab57.lab57c14, "
                + "lab43.lab43c1, lab43c3, lab43.lab43c4, "
                + "lab39.lab39c1, lab39.lab39c2, lab39.lab39c3, lab39.lab39c4, lab39.lab39c37, lab39.lab39c27, lab39.lab39c13, lab39.lab39c16, lab39.lab39c17, lab39.lab39c57, "
                + "lab24.lab24c1, lab24c2, lab24c4, lab24c5, lab24c6, lab24c9, lab24.lab24c10, lab24c12, lab24c13, "
                + "lab45.lab45c1, lab45.lab45c2, "
                + "lab45r.lab45c1 lab45c1r, lab45r.lab45c2 lab45c2r, "
                + "lab24.lab56c1, lab56.lab56c2, lab56c3,"
                + "lab16.lab16c3, lab11.lab11c1,"
                + "userO.lab04c1 AS userOc1, userO.lab04c2 AS userOc2, userO.lab04c3 AS userOc3, "
                + "userR.lab04c1 AS userRc1, userR.lab04c2 AS userRc2, userR.lab04c3 AS userRc3, "
                + "userV.lab04c1 AS userVc1, userV.lab04c2 AS userVc2, userV.lab04c3 AS userVc3, "
                + "userSV.lab04c1 AS userSVc1, userSV.lab04c2 AS userSVc2, userSV.lab04c3 AS userSVc3, "
                + "panel.lab39c1 AS panellab39c1, panel.lab39c2 AS panellab39c2, panel.lab39c3 AS panellab39c3, panel.lab39c4 AS panellab39c4, panel.lab39c37 AS panellab39c37, panel.lab39c6 AS panellab39c6,panel.lab39c7 AS panellab39c7, panel.lab39c8 AS panellab39c8, panel.lab39c9 AS panellab39c9, "
                + "panel.lab39c38 as panellab39c38, "
                + " lab57.lab40c1, lab40c2, lab40c3, lab40c10 "
                + "FROM " + lab57 + " as lab57 "
                + "INNER JOIN lab22 ON lab22.lab22c1 = lab57.lab22c1 "
                + "INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1 "
                + "LEFT JOIN lab43 ON lab43.lab43c1 = lab39.lab43c1 "
                + "LEFT JOIN lab24 ON lab24.lab24c1 = lab39.lab24c1 "
                + "LEFT JOIN lab56 ON lab56.lab56c1 = lab24.lab56c1 "
                + "LEFT JOIN lab45 lab45r ON lab45r.lab45c1 = lab56.lab45c1 "
                + "LEFT JOIN lab45 ON lab45.lab45c1 = lab39.lab45c1 "
                + "LEFT JOIN lab04 userO ON userO.lab04c1 = lab57.lab57c5 "
                + "LEFT JOIN lab04 userR ON userR.lab04c1 = lab57.lab57c3 "
                + "LEFT JOIN lab04 userV ON userV.lab04c1 = lab57.lab57c19 "
                + "LEFT JOIN lab04 userSV ON userSV.lab04c1 = lab57.lab57c21 "
                + "LEFT JOIN lab39 panel ON panel.lab39c1 = lab57.lab57c14 "
                + "LEFT JOIN lab11 ON lab11.lab24c1 = lab57.lab24c1 AND lab11.lab22c1 = lab57.lab22c1 "
                + "LEFT JOIN lab16 ON lab16.lab16c1 = lab11.lab16c1 "
                + "LEFT JOIN lab40 ON lab40.lab40c1 = lab57.lab40c1 "
                + " WHERE lab57.lab22c1 = ? AND lab39.lab39c37 != 2 ";
        
        
        query += (SQLTools.buildSQLLaboratoryFilter(laboratorys, branch));

        order.setTests(getConnection().query(query,
                new Object[]
                {
                    order.getOrderNumber()
                }, (ResultSet rs, int i) ->
        {
            Test test = new Test();
            test.setId(rs.getInt("lab39c1"));
            test.setCode(rs.getString("lab39c2"));
            test.setAbbr(rs.getString("lab39c3"));
            test.setName(rs.getString("lab39c4"));
            test.setVolume(rs.getString("lab39c13"));
            test.setRackStore(rs.getString("lab16c3"));
            test.setPositionStore(rs.getString("lab11c1"));
            test.setDependentTest(rs.getInt("panellab39c38"));

            /*RESULTADO*/
            test.setProfile(rs.getInt("lab57c14"));
            test.getResult().setState(rs.getInt("lab57c8"));
            test.getResult().setSampleState(rs.getInt("lab57c16"));
            test.getResult().setResult(Tools.decrypt(rs.getString("lab57c1")));
            test.getResult().setDateOrdered(rs.getTimestamp("lab57c4"));
            test.getResult().setDateResult(rs.getTimestamp("lab57c2"));
            test.getResult().setDateValidation(rs.getTimestamp("lab57c18"));
            test.getResult().setDatePreValidation(rs.getTimestamp("lab57c20"));
            test.getResult().getUserOrdered().setId(rs.getString("userOc1") == null ? null : rs.getInt("userOc1"));
            test.getResult().getUserOrdered().setName(rs.getString("userOc2"));
            test.getResult().getUserOrdered().setLastName(rs.getString("userOc3"));
            test.getResult().getUserResult().setId(rs.getString("userRc1") == null ? null : rs.getInt("userRc1"));
            test.getResult().getUserResult().setName(rs.getString("userRc2"));
            test.getResult().getUserResult().setLastName(rs.getString("userRc3"));
            test.getResult().getUserValidation().setId(rs.getString("userVc1") == null ? null : rs.getInt("userVc1"));
            test.getResult().getUserValidation().setName(rs.getString("userVc2"));
            test.getResult().getUserValidation().setLastName(rs.getString("userVc3"));
            test.getResult().getUserPreValidation().setId(rs.getString("userSVc1") == null ? null : rs.getInt("userSVc1"));
            test.getResult().getUserPreValidation().setName(rs.getString("userSVc2"));
            test.getResult().getUserPreValidation().setLastName(rs.getString("userSVc3"));

            test.setTestType(rs.getShort("lab39c37"));
            test.setConfidential(rs.getInt("lab39c27") == 1);
            test.setDeliveryDays(rs.getInt("lab39c16"));
            test.setProccessDays(rs.getString("lab39c17"));
            test.setExcludeHoliday(rs.getBoolean("lab39c57"));

            test.getArea().setId(rs.getString("lab43c1") == null ? null : rs.getInt("lab43c1"));
            test.getArea().setAbbreviation(rs.getString("lab43c3"));
            test.getArea().setName(rs.getString("lab43c4"));

            test.getSample().setId(rs.getString("lab24c1") == null ? null : rs.getInt("lab24c1"));
            test.getSample().setName(rs.getString("lab24c2"));
            test.getSample().setCodesample(rs.getString("lab24c9"));
            test.getSample().setCanstiker(rs.getInt("lab24c4"));
            test.getSample().setCheck(rs.getInt("lab24c5") == 1);
            test.getSample().setQualityTime(rs.getString("lab24c12") == null ? null : rs.getLong("lab24c12"));
            test.getSample().setQualityPercentage(rs.getString("lab24c13") == null ? null : rs.getInt("lab24c13"));
            test.getSample().setManagementsample(rs.getString("lab24c6"));
            test.getSample().setLaboratorytype(rs.getString("lab24c10"));
            test.getSample().getContainer().setId(rs.getInt("lab56c1"));
            test.getSample().getContainer().setName(rs.getString("lab56c2"));
            if (rs.getString("lab45c1R") != null)
            {
                test.getSample().getContainer().setUnit(new Unit(rs.getInt("lab45c1R")));
                test.getSample().getContainer().getUnit().setName(rs.getString("lab45c2R"));
            }
            String Imabas64 = "";
            byte[] ImaBytes = rs.getBytes("lab56c3");
            if (ImaBytes != null)
            {
                Imabas64 = Base64.getEncoder().encodeToString(ImaBytes);
            }
            test.getSample().getContainer().setImage(Imabas64);

            test.getUnit().setId(rs.getString("lab45c1") == null ? null : rs.getInt("lab45c1"));
            test.getUnit().setName(rs.getString("lab45c2"));

            Test panel = new Test();
            panel.setId(rs.getString("panellab39c1") == null ? null : rs.getInt("panellab39c1"));
            panel.setCode(rs.getString("panellab39c2"));
            panel.setAbbr(rs.getString("panellab39c3"));
            panel.setName(rs.getString("panellab39c4"));
            panel.setTestType(rs.getShort("panellab39c37"));
            panel.setGender(new Item(rs.getInt("panellab39c6")));
            panel.setUnitAge(rs.getShort("panellab39c9"));
            panel.setMinAge(rs.getInt("panellab39c7"));
            panel.setMaxAge(rs.getInt("panellab39c8"));
            test.setPanel(panel);

            Laboratory laboratory = new Laboratory();
            laboratory.setId(rs.getInt("lab40c1"));
            laboratory.setCode(rs.getInt("lab40c2"));
            laboratory.setName(rs.getString("lab40c3"));
            laboratory.setUrl(rs.getString("lab40c10"));
            test.setLaboratory(laboratory);

            return test;
        }));
    }

    /**
     * Lista las pruebas de una orden.
     *
     * @param order Objeto de la orden a la que se le quieren consultar los
     * examenes.
     */
    default void listTestTracking(Order order, int user, List<Integer> laboratorys, int idbranch) throws Exception
    {
        Integer year = Tools.YearOfOrder(String.valueOf(order.getOrderNumber()));
        Integer currentYear = DateTools.dateToNumberYear(new Date());
        String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;

        String query = ISOLATION_READ_UNCOMMITTED + "SELECT lab57.lab22c1, lab57.lab57c8, lab57.lab57c16, "
                + "lab57.lab57c1, lab57.lab57c2, lab57.lab57c4, lab57.lab57c18, lab57.lab57c20, lab57.lab57c14, "
                + "lab43.lab43c1, lab43c3, lab43.lab43c4, "
                + "lab39.lab39c1, lab39.lab39c2, lab39.lab39c3, lab39.lab39c4, lab39.lab39c37, lab39.lab39c27, lab39.lab39c13, "
                + "lab24.lab24c1, lab24c2, lab24c4, lab24c5, lab24c6, lab24c9, lab24.lab24c10, lab24c12, lab24c13, "
                + "lab45.lab45c1, lab45.lab45c2, "
                + "lab45r.lab45c1 lab45c1r, lab45r.lab45c2 lab45c2r, "
                + "lab24.lab56c1, lab56.lab56c2, lab56c3,"
                + "lab16.lab16c3, lab11.lab11c1,"
                + "userO.lab04c1 AS userOc1, userO.lab04c2 AS userOc2, userO.lab04c3 AS userOc3, "
                + "userR.lab04c1 AS userRc1, userR.lab04c2 AS userRc2, userR.lab04c3 AS userRc3, "
                + "userV.lab04c1 AS userVc1, userV.lab04c2 AS userVc2, userV.lab04c3 AS userVc3, "
                + "userSV.lab04c1 AS userSVc1, userSV.lab04c2 AS userSVc2, userSV.lab04c3 AS userSVc3, "
                + "panel.lab39c1 AS panellab39c1, panel.lab39c2 AS panellab39c2, panel.lab39c3 AS panellab39c3, panel.lab39c4 AS panellab39c4, panel.lab39c37 AS panellab39c37, panel.lab39c6 AS panellab39c6,panel.lab39c7 AS panellab39c7, panel.lab39c8 AS panellab39c8, panel.lab39c9 AS panellab39c9, "
                + "panel.lab39c38 as panellab39c38 "
                + "FROM " + lab57 + " as lab57 "
                + "INNER JOIN lab22 ON lab22.lab22c1 = lab57.lab22c1 "
                + "INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1 "
                + "LEFT JOIN lab43 ON lab43.lab43c1 = lab39.lab43c1 "
                + "LEFT JOIN lab24 ON lab24.lab24c1 = lab39.lab24c1 "
                + "LEFT JOIN lab56 ON lab56.lab56c1 = lab24.lab56c1 "
                + "LEFT JOIN lab45 lab45r ON lab45r.lab45c1 = lab56.lab45c1 "
                + "LEFT JOIN lab45 ON lab45.lab45c1 = lab39.lab45c1 "
                + "LEFT JOIN lab04 userO ON userO.lab04c1 = lab57.lab57c5 "
                + "LEFT JOIN lab04 userR ON userR.lab04c1 = lab57.lab57c3 "
                + "LEFT JOIN lab04 userV ON userV.lab04c1 = lab57.lab57c19 "
                + "LEFT JOIN lab04 userSV ON userSV.lab04c1 = lab57.lab57c21 "
                + "LEFT JOIN lab39 panel ON panel.lab39c1 = lab57.lab57c14 "
                + "LEFT JOIN lab11 ON lab11.lab24c1 = lab57.lab24c1 AND lab11.lab22c1 = lab57.lab22c1 "
                + "LEFT JOIN lab16 ON lab16.lab16c1 = lab11.lab16c1 "
                + "INNER JOIN lab69 ON lab69.lab43c1 = lab43.lab43c1 and lab69.lab04c1 = " + user + " "
                + "WHERE lab57.lab22c1 = ? AND lab39.lab39c37 != 2";
        
        query  += (SQLTools.buildSQLLaboratoryFilter(laboratorys, idbranch));


        order.setTests(getConnection().query(query,
                new Object[]
                {
                    order.getOrderNumber()
                }, (ResultSet rs, int i) ->
        {
            Test test = new Test();
            test.setId(rs.getInt("lab39c1"));
            test.setCode(rs.getString("lab39c2"));
            test.setAbbr(rs.getString("lab39c3"));
            test.setName(rs.getString("lab39c4"));
            test.setVolume(rs.getString("lab39c13"));
            test.setRackStore(rs.getString("lab16c3"));
            test.setPositionStore(rs.getString("lab11c1"));
            test.setDependentTest(rs.getInt("panellab39c38"));

            /*RESULTADO*/
            test.setProfile(rs.getInt("lab57c14"));
            test.getResult().setState(rs.getInt("lab57c8"));
            test.getResult().setSampleState(rs.getInt("lab57c16"));
            test.getResult().setResult(Tools.decrypt(rs.getString("lab57c1")));
            test.getResult().setDateOrdered(rs.getTimestamp("lab57c4"));
            test.getResult().setDateResult(rs.getTimestamp("lab57c2"));
            test.getResult().setDateValidation(rs.getTimestamp("lab57c18"));
            test.getResult().setDatePreValidation(rs.getTimestamp("lab57c20"));
            test.getResult().getUserOrdered().setId(rs.getString("userOc1") == null ? null : rs.getInt("userOc1"));
            test.getResult().getUserOrdered().setName(rs.getString("userOc2"));
            test.getResult().getUserOrdered().setLastName(rs.getString("userOc3"));
            test.getResult().getUserResult().setId(rs.getString("userRc1") == null ? null : rs.getInt("userRc1"));
            test.getResult().getUserResult().setName(rs.getString("userRc2"));
            test.getResult().getUserResult().setLastName(rs.getString("userRc3"));
            test.getResult().getUserValidation().setId(rs.getString("userVc1") == null ? null : rs.getInt("userVc1"));
            test.getResult().getUserValidation().setName(rs.getString("userVc2"));
            test.getResult().getUserValidation().setLastName(rs.getString("userVc3"));
            test.getResult().getUserPreValidation().setId(rs.getString("userSVc1") == null ? null : rs.getInt("userSVc1"));
            test.getResult().getUserPreValidation().setName(rs.getString("userSVc2"));
            test.getResult().getUserPreValidation().setLastName(rs.getString("userSVc3"));

            test.setTestType(rs.getShort("lab39c37"));
            test.setConfidential(rs.getInt("lab39c27") == 1);

            test.getArea().setId(rs.getString("lab43c1") == null ? null : rs.getInt("lab43c1"));
            test.getArea().setAbbreviation(rs.getString("lab43c3"));
            test.getArea().setName(rs.getString("lab43c4"));

            test.getSample().setId(rs.getString("lab24c1") == null ? null : rs.getInt("lab24c1"));
            test.getSample().setName(rs.getString("lab24c2"));
            test.getSample().setCodesample(rs.getString("lab24c9"));
            test.getSample().setCanstiker(rs.getInt("lab24c4"));
            test.getSample().setCheck(rs.getInt("lab24c5") == 1);
            test.getSample().setQualityTime(rs.getString("lab24c12") == null ? null : rs.getLong("lab24c12"));
            test.getSample().setQualityPercentage(rs.getString("lab24c13") == null ? null : rs.getInt("lab24c13"));
            test.getSample().setManagementsample(rs.getString("lab24c6"));
            test.getSample().setLaboratorytype(rs.getString("lab24c10"));
            test.getSample().getContainer().setId(rs.getInt("lab56c1"));
            test.getSample().getContainer().setName(rs.getString("lab56c2"));
            if (rs.getString("lab45c1R") != null)
            {
                test.getSample().getContainer().setUnit(new Unit(rs.getInt("lab45c1R")));
                test.getSample().getContainer().getUnit().setName(rs.getString("lab45c2R"));
            }
            String Imabas64 = "";
            byte[] ImaBytes = rs.getBytes("lab56c3");
            if (ImaBytes != null)
            {
                Imabas64 = Base64.getEncoder().encodeToString(ImaBytes);
            }
            test.getSample().getContainer().setImage(Imabas64);

            test.getUnit().setId(rs.getString("lab45c1") == null ? null : rs.getInt("lab45c1"));
            test.getUnit().setName(rs.getString("lab45c2"));

            Test panel = new Test();
            panel.setId(rs.getString("panellab39c1") == null ? null : rs.getInt("panellab39c1"));
            panel.setCode(rs.getString("panellab39c2"));
            panel.setAbbr(rs.getString("panellab39c3"));
            panel.setName(rs.getString("panellab39c4"));
            panel.setTestType(rs.getShort("panellab39c37"));
            panel.setGender(new Item(rs.getInt("panellab39c6")));
            panel.setUnitAge(rs.getShort("panellab39c9"));
            panel.setMinAge(rs.getInt("panellab39c7"));
            panel.setMaxAge(rs.getInt("panellab39c8"));

            test.setPanel(panel);
            return test;
        }));
    }

    /**
     * Actualiza el estado de la muestra en la base de datos.
     *
     * @param idOrder Id de la orden a verificar.
     * @param tests Lista de examenes que seran verificados.
     * @param user Usuario Logeado.
     * @param idSample Id de la Sede.
     * @param type Tipo.
     * @param reason Razón.
     * @param idRoute Id ruta asignada
     * @param listholiday
     *
     * @return Retorna objeto para la trazabilidad de la muestra.
     *
     * @throws Exception Error en la base de datos.
     */
    default SampleTracking sampleTracking(long idOrder, List<Test> tests, AuthorizedUser user, int idSample, int type, Reason reason, Integer idRoute, List<String> listholiday) throws Exception
    {
        Integer year = Tools.YearOfOrder(String.valueOf(idOrder));
        Integer currentYear = DateTools.dateToNumberYear(new Date());
        String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;

        List<Object[]> parameters = new ArrayList<>();
        Date currentDate = new Date();
        SampleTracking sampleState = getSampleTracking(idSample, idOrder).stream().filter(state -> state.getState() == type).findAny().orElse(null);
        String update = "UPDATE " + lab57 + " SET lab57c16 = ? ";
        String where = " WHERE lab22c1 = ? AND lab39c1 = ? ";
        if (type == LISEnum.ResultSampleState.CHECKED.getValue())
        {
            update += ",lab57c34 = ?,lab57c37 = ?,lab57c38 = ? ";
            where += " AND lab57c16 != -1";
        } else if (type == LISEnum.ResultSampleState.COLLECTED.getValue())
        {
            update += ",lab57c39 = ?,lab57c40 = ?,lab57c56 = ? ";
            where += " AND lab57c16 != -1 OR (lab22c1 = ? AND LAB39C1 = (SELECT LAB57C14 FROM LAB57 WHERE lab22c1 = ? and LAB39C1 = ?) AND LAB24C1 IS NULL AND LAB57C56 IS NULL ) ";
        }

        tests.forEach((test) ->
        {
            if (type == LISEnum.ResultSampleState.CHECKED.getValue())
            {
                if (sampleState != null)
                {
                    if (test.getResult().getSampleState() <= LISEnum.ResultSampleState.CHECKED.getValue())
                    {
                        parameters.add(new Object[]
                        {
                            type,
                            DateTools.dateToNumber(sampleState.getDate()),
                            new Timestamp(currentDate.getTime()),
                            user.getId(),
                            idOrder,
                            test.getId()
                        });
                    }

                } else
                {
                    parameters.add(new Object[]
                    {
                        type,
                        DateTools.dateToNumber(currentDate),
                        new Timestamp(currentDate.getTime()),
                        user.getId(),
                        idOrder,
                        test.getId()
                    });
                }
            } else if (type == LISEnum.ResultSampleState.COLLECTED.getValue())
            {

                String processday = test.getProccessDays();
                int deliveryDays = test.getDeliveryDays();

                Calendar calendarEnd = Calendar.getInstance();
                calendarEnd.setTime(sampleState == null ? currentDate : sampleState.getDate());
                calendarEnd.add(Calendar.DAY_OF_YEAR, deliveryDays);

                Calendar calendarInit = Calendar.getInstance();
                calendarInit.setTime(sampleState == null ? currentDate : sampleState.getDate());

                boolean validdayprocess = false;
                while (calendarInit.before(calendarEnd) || calendarInit.equals(calendarEnd))
                {
                    int day = calendarInit.get(Calendar.DAY_OF_WEEK);
                    day = day == 1 ? 7 : day - 1;
                    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                    String formatted = format1.format(calendarInit.getTime()) + " 00:00:00.0";

                    if (!test.isExcludeHoliday() && listholiday.contains(formatted))
                    {
                        calendarEnd.add(Calendar.DAY_OF_YEAR, 1);
                    } else
                    {
                        validdayprocess = validdayprocess == true ? validdayprocess : processday.contains(String.valueOf(day));
                    }
                    calendarInit.add(Calendar.DAY_OF_YEAR, 1);
                }

                if (!validdayprocess)
                {
                    calendarEnd = null;
                }

                if (sampleState != null)
                {
                    parameters.add(new Object[]
                    {
                        type,
                        new Timestamp(sampleState.getDate().getTime()),
                        user.getId(),
                        !validdayprocess ? null : new Timestamp(calendarEnd.getTime().getTime()),
                        idOrder,
                        test.getId(),
                        idOrder,
                        idOrder,
                        test.getId()
                    });
                } else
                {
                    parameters.add(new Object[]
                    {
                        type,
                        new Timestamp(currentDate.getTime()),
                        user.getId(),
                        !validdayprocess ? null : new Timestamp(calendarEnd.getTime().getTime()),
                        idOrder,
                        test.getId(),
                        idOrder,
                        idOrder,
                        test.getId()
                    });
                }
            } else
            {
                parameters.add(new Object[]
                {
                    type,
                    idOrder,
                    test.getId()
                });

            }
        });
        getConnection().batchUpdate(update + where, parameters);
        insertSampleTracking(idOrder, user.getId(), user.getBranch(), reason, idSample, type, tests, currentDate);
        if (type != LISEnum.ResultSampleState.NEW_SAMPLE.getValue())
        {
            updateSampleState(idOrder, user.getId(), user.getBranch(), null, idSample, type, idRoute);
        }

        SampleTracking tracking = new SampleTracking();
        tracking.setOrder(idOrder);
        tracking.setId(idSample);
        tracking.setTests(type == LISEnum.ResultSampleState.NEW_SAMPLE.getValue() ? tests : null);
        tracking.setMotive(reason == null || reason.getMotive() == null || reason.getMotive().getId() == null ? null : reason.getMotive());
        tracking.setComment(reason == null ? null : reason.getComment());
        tracking.setState(type);

        return tracking;
    }

    /**
     * Actualiza el estado de la muestracuando es cread la orden y no se cuenta
     * con el modulo de verificacion en la base de datos
     *
     * @param idOrder Id de la orden a verificar.
     * @param tests Lista de examenes que seran verificados.
     * @param user Usuario Logeado.
     * @param type Tipo.
     *
     * @return Retorna objeto para la trazabilidad de la muestra.
     *
     * @throws Exception Error en la base de datos.
     */
    default SampleTracking sampleTrackingDefault(long idOrder, List<Test> tests, AuthorizedUser user, int type) throws Exception
    {
        // Año de la orden
        Integer year = Tools.YearOfOrder(String.valueOf(idOrder));
        Integer currentYear = DateTools.dateToNumberYear(new Date());
        String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;

        List<Object[]> parameters = new ArrayList<>();
        Date currentDate = new Date();
        //SampleTracking sampleState = getSampleTracking(idSample, idOrder).stream().filter(state -> state.getState() == type).findAny().orElse(null);
        String update = "UPDATE " + lab57 + " SET lab57c16 = ? ";
        String where = " WHERE lab22c1 = ?  ";
        
        if(tests != null) {
            where += " AND lab39c1 in (" + tests.stream().map(test -> String.valueOf(test.getId())).collect(Collectors.joining(",")) + ")"; 
        }
        
        if (type == LISEnum.ResultSampleState.CHECKED.getValue())
        {
            update += ",lab57c34 = ?,lab57c37 = ?,lab57c38 = ? ";
            where += " AND lab57c16 != -1";

            parameters.add(new Object[]
            {
                type,
                DateTools.dateToNumber(currentDate),
                new Timestamp(currentDate.getTime()),
                user.getId(),
                idOrder
            });
        } else if (type == LISEnum.ResultSampleState.COLLECTED.getValue())
        {
            update += ",lab57c39 = ?,lab57c40 = ? ";
            where += " AND lab57c16 != -1";

            parameters.add(new Object[]
            {
                type,
                new Timestamp(currentDate.getTime()),
                user.getId(),
                idOrder,
            });
        }

        getConnection().batchUpdate(update + where, parameters);
        //insertSampleTracking(idOrder, user.getId(), user.getBranch(), type, tests, currentDate);
        if (type != LISEnum.ResultSampleState.NEW_SAMPLE.getValue())
        {
            updateSampleState(idOrder, user.getId(), user.getBranch(), type);
        }

        SampleTracking tracking = new SampleTracking();
        tracking.setOrder(idOrder);
        tracking.setTests(type == LISEnum.ResultSampleState.NEW_SAMPLE.getValue() ? tests : null);
        tracking.setState(type);
        return tracking;
    }

    default void updateDateRetakes(long idOrder, List<Test> tests, AuthorizedUser user, int idSample)
    {
        List<Object[]> parameters = new ArrayList<>();
        Date currentDate = new Date();
        String update = "UPDATE lab57 SET lab57c44 = ?, lab57c45 = ? ";
        String where = " WHERE lab22c1 = ? AND lab39c1 = ? ";
        tests.forEach((test) ->
        {
            parameters.add(new Object[]
            {
                currentDate,
                user.getId(),
                idOrder,
                test.getId()
            });
        });
        getConnection().batchUpdate(update + where, parameters);
    }

    default void updateCheckRetake(long idOrder, int idSample, AuthorizedUser user, int checked)
    {
        List<Object[]> parameters = new ArrayList<>();
        String update = "UPDATE lab57 SET lab57c44 = null, lab57c45 = null, lab57c16 = ?";
        String where = " WHERE lab22c1 = ? AND lab24c1 = ? AND lab57c16 = 1 ";
        parameters.add(new Object[]
        {
            checked,
            idOrder,
            idSample
        });
        getConnection().batchUpdate(update + where, parameters);
    }

    /**
     * Consulta los examenes de la trazabilidad de la muestra.
     *
     * @param tracking Trazabilidad de la muestra.
     */
    default void getTestsSampleTracking(SampleTracking tracking)
    {
        try
        {
            String query = ISOLATION_READ_UNCOMMITTED + "SELECT lab39.lab39c1, lab39.lab39c2, lab39.lab39c3, lab39.lab39c4 "
                    + "FROM lab160 "
                    + "LEFT JOIN lab39 ON lab39.lab39c1 = lab160.lab39c1 "
                    + "WHERE lab160.lab144c1 = ?";

            tracking.setTests(getConnection().query(query,
                    new Object[]
                    {
                        tracking.getId()
                    }, (ResultSet rs, int i) ->
            {
                Test test = new Test();
                test.setId(rs.getInt("lab39c1"));
                test.setCode(rs.getString("lab39c2"));
                test.setAbbr(rs.getString("lab39c3"));
                test.setName(rs.getString("lab39c4"));

                return test;
            }));
        } catch (EmptyResultDataAccessException ex)
        {
            tracking.setTests(null);
        }
    }

    /**
     * Consulta la trazabilidad de la muestra.
     *
     * @param idOrder Id de la Orden.
     * @param idSample Id de la sede.
     * @param idBranch
     *
     * @return Retorna la trazabilidad de la muestra.
     */
    default SampleState getSampleState(int idSample, long idOrder, int idBranch)
    {
        try
        {

            Integer year = Tools.YearOfOrder(String.valueOf(idOrder));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab159 = year.equals(currentYear) ? "lab159" : "lab159_" + year;

            String query = ISOLATION_READ_UNCOMMITTED + "SELECT lab159.lab22c1, lab159.lab159c1, lab159.lab159c2, "
                    + "lab24.lab24c1, lab24c2, lab24c9, lab24c5, "
                    + "lab05.lab05c1, lab05.lab05c10, lab05.lab05c4, "
                    + "lab04.lab04c1, lab04.lab04c2, lab04.lab04c3, "
                    + "lab53.lab53c1, lab53.lab53c2, lab53.lab53c3 "
                    + "FROM  " + lab159 + " as lab159 "
                    + "LEFT JOIN lab53 ON lab53.lab53c1 = lab159.lab53c1 "
                    + "INNER JOIN lab24 ON lab24.lab24c1 = lab159.lab24c1 "
                    + "INNER JOIN lab04 ON lab04.lab04c1 = lab159.lab04c1 "
                    + "INNER JOIN lab05 ON lab05.lab05c1 = lab159.lab05c1  "
                    + "WHERE lab159.lab22c1 = ? AND lab159.lab24c1 = ? AND lab159.lab05c1 = ? ";

            return getConnection().queryForObject(query,
                    new Object[]
                    {
                        idOrder, idSample, idBranch
                    }, (ResultSet rs, int i) ->
            {
                SampleState state = new SampleState();
                state.setOrder(rs.getLong("lab22c1"));

                state.getBranch().setId(rs.getInt("lab05c1"));
                state.getBranch().setCode(rs.getString("lab05c10"));
                state.getBranch().setName(rs.getString("lab05c4"));

                state.getUser().setId(rs.getInt("lab04c1"));
                state.getUser().setName(rs.getString("lab04c2"));
                state.getUser().setLastName(rs.getString("lab04c3"));

                Sample sample = new Sample();
                sample.setId(rs.getInt("lab24c1"));
                sample.setName(rs.getString("lab24c2"));
                sample.setCodesample(rs.getString("lab24c9"));
                sample.setCheck(rs.getInt("lab24c5") == 1);

                state.setSample(sample);

                state.getDestination().setId(rs.getInt("lab53c1"));
                state.getDestination().setCode(rs.getString("lab53c2"));
                state.getDestination().setName(rs.getString("lab53c3"));

                state.setState(rs.getInt("lab159c1"));
                state.setDate(rs.getTimestamp("lab159c2"));

                return state;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Insertar el motivo del aplazamiento o retoma de la muestra.
     *
     * @param idOrder Id de la orden a rechazar.
     * @param user Usuario Logeado.
     * @param branch Sede.
     * @param reason Razon por la que se rechaza la muestra.
     * @param idSample Muestra.
     * @param type Indica si es aplazamiento o retoma.
     * @param tests lista de examenes
     * @param currentDate fecha de registro
     *
     * @throws Exception Error en la base de datos.
     */
    default void insertSampleTracking(long idOrder, Integer user, Integer branch, Reason reason, int idSample, int type, List<Test> tests, Date currentDate) throws Exception
    {
        List<SampleTracking> sampletraking = getSampleTracking(idSample, idOrder);
        // Año de la orden
        Integer year = Tools.YearOfOrder(String.valueOf(idOrder));
        Integer currentYear = DateTools.dateToNumberYear(new Date());
        String lab144 = year.equals(currentYear) ? "lab144" : "lab144_" + year;

        Timestamp timestamp = new Timestamp(currentDate.getTime());

        if (!sampletraking.stream().anyMatch(state -> state.getState() == type))
        {

            SimpleJdbcInsert insert = new SimpleJdbcInsert(getConnection())
                    .withTableName(lab144)
                    .usingGeneratedKeyColumns("lab144c1");

            HashMap parameters = new HashMap();
            parameters.put("lab22c1", idOrder);
            parameters.put("lab24c1", idSample);
            parameters.put("lab04c1", user);
            parameters.put("lab05c1", branch);
            parameters.put("lab30c1", reason == null ? null : reason.getMotive().getId());
            parameters.put("lab144c2", timestamp);
            parameters.put("lab144c3", type);
            parameters.put("lab144c4", reason == null ? "" : reason.getComment());
            parameters.put("lab07c1", 1);

            int idTracking = insert.executeAndReturnKey(parameters).intValue();

            if (type == LISEnum.ResultSampleState.NEW_SAMPLE.getValue())
            {
                tests.forEach(test ->
                {
                    SimpleJdbcInsert insertTest = new SimpleJdbcInsert(getConnection())
                            .withTableName("lab160");

                    HashMap parametersTests = new HashMap();
                    parametersTests.put("lab144c1", idTracking);
                    parametersTests.put("lab39c1", test.getId());

                    insertTest.execute(parametersTests);
                });
            }
        } else
        {
            String update = "UPDATE " + lab144 + " SET lab144c2 = ?, "
                    + "lab04c1 = ? "
                    + "where lab22c1 = ? and "
                    + "lab24c1 = ? and "
                    + "lab05c1 = ? and "
                    + "lab144c3 = ?";
            List<Object> params = new ArrayList<>();
            params.add(timestamp);
            params.add(user);
            params.add(idOrder);
            params.add(idSample);
            params.add(branch);
            params.add(type);

            getConnection().update(update, params.toArray());
        }
    }

    /**
     * Actualizar el estado actual de la muestra.
     *
     * @param idOrder Id de la orden a rechazar.
     * @param idUser Usuario Logeado.
     * @param idBranch Sede.
     * @param idDestination Destino de la muestra.
     * @param idSample Muestra.
     * @param type Indica si es aplazamiento o retoma.
     * @param idRoute id de la ruta asignada, null si no esta asignada
     *
     * @throws Exception Error en la base de datos.
     */
    default void updateSampleState(long idOrder, Integer idUser, Integer idBranch, Integer idDestination, int idSample, int type, Integer idRoute) throws Exception
    {
        Integer year = Tools.YearOfOrder(String.valueOf(idOrder));
        Integer currentYear = DateTools.dateToNumberYear(new Date());
        String lab159 = year.equals(currentYear) ? "lab159" : "lab159_" + year;

        Timestamp timestamp = new Timestamp(new Date().getTime());
        String update = "UPDATE " + lab159 + " SET lab159c1 = ?, lab04c1 = ?, lab159c2 = ?, lab53c1 = ?, lab05c1 = ? ";
        List<Object> params = new ArrayList<>();
        params.add(type);
        params.add(idUser);
        params.add(timestamp);
        params.add(idDestination);
        params.add(idBranch);

        if (idRoute != null)
        {
            update = update + ", lab52c1 = ?";
            params.add(idRoute);
        }
        update = update + " WHERE lab22c1 = ? AND lab24c1 = ? AND LAB05C1 = ?";
        params.add(idOrder);
        params.add(idSample);
        params.add(idBranch);

        getConnection().update(update, params.toArray());
    }

    /**
     * Actualizar el estado actual de la muestra.
     *
     * @param idOrder Id de la orden a rechazar.
     * @param idUser Usuario Logeado.
     * @param idBranch Sede.
     * @param type Indica si es aplazamiento o retoma.
     *
     * @throws Exception Error en la base de datos.
     */
    default void updateSampleState(long idOrder, Integer idUser, Integer idBranch, int type) throws Exception
    {
        // Año de la orden
        Integer year = Tools.YearOfOrder(String.valueOf(idOrder));
        Integer currentYear = DateTools.dateToNumberYear(new Date());
        String lab159 = year.equals(currentYear) ? "lab159" : "lab159_" + year;

        Timestamp timestamp = new Timestamp(new Date().getTime());
        String update = "UPDATE " + lab159 + " SET lab159c1 = ?, lab04c1 = ?, lab159c2 = ?, lab05c1 = ? ";
        List<Object> params = new ArrayList<>();
        params.add(type);
        params.add(idUser);
        params.add(timestamp);
        params.add(idBranch);

        update = update + " WHERE lab22c1 = ? AND LAB05C1 = ?";
        params.add(idOrder);

        params.add(idBranch);

        getConnection().update(update, params.toArray());
    }

    /**
     * Elimina el estado actual y la trazabilidad de la muestra.
     *
     * @param idOrder Id de la orden.
     * @param idSample Id de la muestra.
     *
     * @throws Exception Error en la base de datos.
     */
    default void deleteSampleStateTracking(long idOrder, int idSample) throws Exception
    {
        // Año de la orden
        Integer year = Tools.YearOfOrder(String.valueOf(idOrder));
        Integer currentYear = DateTools.dateToNumberYear(new Date());
        String lab144 = year.equals(currentYear) ? "lab144" : "lab144_" + year;
        String lab159 = year.equals(currentYear) ? "lab159" : "lab159_" + year;

        String deleteTracking = "DELETE FROM " + lab144 + " WHERE lab22c1 = ? AND lab24c1 = ?";
        getConnection().update(deleteTracking, idOrder, idSample);

        String deleteState = "DELETE FROM " + lab159 + " WHERE lab22c1 = ? AND lab24c1 = ?";
        getConnection().update(deleteState, idOrder, idSample);
    }

    /**
     * Actualizar el estado actual de la muestra.
     *
     * @param idOrder Id de la orden a rechazar.
     * @param idUser Usuario Logeado.
     * @param idBranch Sede.
     * @param idSample Muestra.
     * @param type Indica si es aplazamiento o retoma.
     * @param currentDate
     *
     * @throws Exception Error en la base de datos.
     */
    default void insertSampleOrdered(long idOrder, Integer idUser, Integer idBranch, int idSample, int type, Date currentDate) throws Exception
    {
        
        Integer year = Tools.YearOfOrder(String.valueOf(idOrder));
        Integer currentYear = DateTools.dateToNumberYear(new Date());
        String lab159 = year.equals(currentYear) ? "lab159" : "lab159_" + year;

        Timestamp timestamp = new Timestamp(currentDate.getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getConnection())
                .withTableName(lab159);

        HashMap parameters = new HashMap();
        parameters.put("lab22c1", idOrder);
        parameters.put("lab24c1", idSample);
        parameters.put("lab159c1", type);
        parameters.put("lab159c2", timestamp);
        parameters.put("lab04c1", idUser);
        parameters.put("lab05c1", idBranch);
        parameters.put("lab53c1", null);
        parameters.put("lab52c1", null);

        insert.execute(parameters);
    }

    /**
     * Consulta la ruta de destinos asociados a la orden insertada.
     *
     * @param idOrder Id de la orden.
     * @param idSample Id de la muestra.
     * @param idOrderType Id del tipo de orden.
     * @param idBranch Id de la sede.
     *
     * @return Retorna si el destino ya se verifico.
     *
     * @throws Exception Error en la base de datos.
     */
    default AssignmentDestination getDestinationRoute(long idOrder, int idOrderType, int idBranch, int idSample) throws Exception
    {
        try
        {
            String query = ISOLATION_READ_UNCOMMITTED + "SELECT lab52c1, lab52c2, lab52.lab04c1, lab04c2, lab04c3, lab04c4, lab52.lab07c1 "
                    + " FROM lab52 "
                    + " LEFT JOIN lab04 ON lab04.lab04c1 = lab52.lab04c1 "
                    + " WHERE lab52.lab05c1 = ? AND lab52.lab24c1 = ? AND lab52.lab103c1 = ? AND lab52.lab07c1 = 1";

            return getConnection().queryForObject(query,
                    new Object[]
                    {
                        idBranch, idSample, idOrderType
                    }, (ResultSet rs, int i) ->
            {
                AssignmentDestination assignmentDestination = new AssignmentDestination();
                assignmentDestination.setId(rs.getInt("lab52c1"));
                /*Usuario*/
                assignmentDestination.getUser().setId(rs.getInt("lab04c1"));
                assignmentDestination.getUser().setName(rs.getString("lab04c2"));
                assignmentDestination.getUser().setLastName(rs.getString("lab04c3"));
                assignmentDestination.getUser().setUserName(rs.getString("lab04c4"));

                assignmentDestination.setLastTransaction(rs.getTimestamp("lab52c2"));
                assignmentDestination.setState(rs.getInt("lab07c1") == 1);

                getRoute(assignmentDestination, idSample, idOrder);

                return assignmentDestination;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Consulta la ruta de destinos asociados a la orden insertada.
     *
     * @param idOrder Id de la orden.
     * @param idSample Id de la muestra.
     * @param idAssigment Id de la asignacion de destinos.
     *
     * @return Retorna si el destino ya se verifico.
     *
     * @throws Exception Error en la base de datos.
     */
    default AssignmentDestination getDestinationRouteAssigment(long idOrder, int idSample, int idAssigment) throws Exception
    {
        try
        {
            String query = ISOLATION_READ_UNCOMMITTED + "SELECT lab52c1, lab52c2, lab52.lab04c1, lab04c2, lab04c3, lab04c4, lab52.lab07c1 "
                    + "FROM lab52 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab52.lab04c1 "
                    + "WHERE lab52.lab52c1 = ?";

            return getConnection().queryForObject(query,
                    new Object[]
                    {
                        idAssigment
                    }, (ResultSet rs, int i) ->
            {
                AssignmentDestination assignmentDestination = new AssignmentDestination();
                assignmentDestination.setId(rs.getInt("lab52c1"));
                /*Usuario*/
                assignmentDestination.getUser().setId(rs.getInt("lab04c1"));
                assignmentDestination.getUser().setName(rs.getString("lab04c2"));
                assignmentDestination.getUser().setLastName(rs.getString("lab04c3"));
                assignmentDestination.getUser().setUserName(rs.getString("lab04c4"));

                assignmentDestination.setLastTransaction(rs.getTimestamp("lab52c2"));
                assignmentDestination.setState(rs.getInt("lab07c1") == 1);

                getRoute(assignmentDestination, idSample, idOrder);

                return assignmentDestination;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Lista la ruta asociada a una sede, una muestra y un tipo de orden desde
     * la base de datos.
     *
     * @param assignmentDestination Objeto de la asignacion de destinos.
     * @param idSample Id de la muestra en la que se debe hacer la consulta.
     * @param idOrder Orden.
     */
    default void getRoute(AssignmentDestination assignmentDestination, int idSample, long idOrder)
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(idOrder));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab25 = year.equals(currentYear) ? "lab25" : "lab25_" + year;

            assignmentDestination.setDestinationRoutes(getConnection().query("" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT lab42.lab42c1, lab42.lab42c2, lab53.lab53c1, lab53c2, lab53c3, lab53c4, lab53c5, lab53c6, lab53c7, lab53.lab07c1, lab80c1, lab80c2, lab80c3, lab80c4, lab80c5, lab25.lab22c1, lab25.lab25c1, "
                    + " lab25.lab04c1, lab04c2, lab04c3, lab04c4 "
                    + " FROM lab42 "
                    + " INNER JOIN lab53 ON lab42.lab53c1 = lab53.lab53c1 "
                    + " LEFT JOIN lab80 ON lab80.lab80c1 = lab53.lab53c5 "
                    + " LEFT JOIN " + lab25 + " as lab25 ON lab25.lab42c1 = lab42.lab42c1 AND lab25.lab22c1 = ?"
                    + " LEFT JOIN lab04 ON lab04.lab04c1 = lab25.lab04c1 "
                    + " WHERE lab42.lab52c1 = ?",
                    new Object[]
                    {
                        idOrder, assignmentDestination.getId()
                    }, (ResultSet rs, int i) ->
            {
                DestinationRoute route = new DestinationRoute();
                route.setId(rs.getInt("lab42c1"));
                route.setOrder(rs.getInt("lab42c2"));
                route.getDestination().setId(rs.getInt("lab53c1"));
                route.getDestination().setCode(rs.getString("lab53c2"));
                route.getDestination().setName(rs.getString("lab53c3"));
                route.getDestination().setDescription(rs.getString("lab53c4"));
                route.getDestination().setColor(rs.getString("lab53c6"));
                /*Tipo*/
                route.getDestination().getType().setId(rs.getInt("lab80c1"));
                route.getDestination().getType().setIdParent(rs.getInt("lab80c2"));
                route.getDestination().getType().setCode(rs.getString("lab80c3"));
                route.getDestination().getType().setEsCo(rs.getString("lab80c4"));
                route.getDestination().getType().setEnUsa(rs.getString("lab80c5"));

                route.getUserVerify().setId(rs.getString("lab04c1") == null ? null : rs.getInt("lab04c1"));
                route.getUserVerify().setName(rs.getString("lab04c2"));
                route.getUserVerify().setLastName(rs.getString("lab04c3"));
                route.getUserVerify().setUserName(rs.getString("lab04c4"));

                route.getDestination().setLastTransaction(rs.getTimestamp("lab53c7"));
                route.getDestination().setState(rs.getInt("lab07c1") == 1);
                route.setVerify(rs.getString("lab22c1") != null);
                route.setDateVerify(rs.getTimestamp("lab25c1"));

                readTests(route, idSample);

                return route;
            }));
        } catch (EmptyResultDataAccessException ex)
        {
            assignmentDestination.setDestinationRoutes(new ArrayList<>(0));
        }
    }

    /**
     * Obtener examenes asociados a un destino.
     *
     * @param destination Instancia con los datos del destino.
     * @param idSample Id de la muestra
     */
    default void readTests(DestinationRoute destination, int idSample)
    {
        try
        {
            destination.setTests(getConnection().query(ISOLATION_READ_UNCOMMITTED + " SELECT lab39.lab39c1, lab39c2, lab39c3, lab39c4, lab39c11, lab39c37, lab39.lab07c1, lab39.lab43c1, lab43c3, lab43c4, lab87.lab42c1 "
                    + " FROM lab39 "
                    + " LEFT JOIN lab43 ON lab43.lab43c1 = lab39.lab43c1 "
                    + " INNER JOIN lab87 ON lab87.lab39c1 = lab39.lab39c1 AND lab87.lab42c1 = ? "
                    + " WHERE lab39.lab39c37 = 0 AND lab39.lab24c1 = ? ",
                    new Object[]
                    {
                        destination.getId(), idSample
                    }, (ResultSet rs, int i) ->
            {
                TestBasic test = new TestBasic();
                test.setId(rs.getInt("lab39c1"));
                test.setCode(rs.getString("lab39c2"));
                test.setAbbr(rs.getString("lab39c3"));
                test.setName(rs.getString("lab39c4"));
                test.setResultType(rs.getShort("lab39c11"));
                test.setTestType(rs.getShort("lab39c37"));
                /*Area*/
                test.getArea().setId(rs.getInt("lab43c1"));
                test.getArea().setAbbreviation(rs.getString("lab43c3"));
                test.getArea().setName(rs.getString("lab43c4"));
                test.setSelected(rs.getString("lab42c1") != null);

                return test;
            }));
        } catch (EmptyResultDataAccessException ex)
        {
            destination.setTests(new ArrayList<>());
        }
    }

    /**
     * Consulta los destinos verificados por orden y muestra.
     *
     * @param idOrder Id de la orden.
     * @param idSample Id de la muestra.
     * @param idBranch Id de la sede.
     *
     * @return Retorna una lista con el id del destino ruta y el id de la
     * asignación del destino.
     * @throws Exception Error en la base de datos.
     */
    default List<VerifyDestination> getRouteByOrder(long idOrder, int idSample, int idBranch) throws Exception
    {
        Integer year = Tools.YearOfOrder(String.valueOf(idOrder));
        Integer currentYear = DateTools.dateToNumberYear(new Date());
        String lab25 = year.equals(currentYear) ? "lab25" : "lab25_" + year;

        String query = "" + ISOLATION_READ_UNCOMMITTED
                + " SELECT lab52.lab05c1, lab52.lab24c1, lab42.lab42c1, lab52.lab52c1, lab25.lab22c1 "
                + " FROM " + lab25 + " as lab25"
                + " INNER JOIN lab42 ON lab42.lab42c1 = lab25.lab42c1 "
                + " INNER JOIN lab52 ON lab52.lab52c1 = lab42.lab52c1 "
                + " WHERE lab22c1 = ? AND lab52.lab24c1 = ? AND lab52.lab05c1 = ?";

        return getConnection().query(query,
                new Object[]
                {
                    idOrder, idSample, idBranch
                }, (ResultSet rs, int i) ->
        {
            VerifyDestination verifyDestination = new VerifyDestination();
            verifyDestination.setOrder(rs.getLong("lab22c1"));
            verifyDestination.setBranch(rs.getInt("lab05c1"));
            verifyDestination.setDestination(rs.getInt("lab42c1"));
            verifyDestination.setAssigmentDestination(rs.getInt("lab52c1"));

            return verifyDestination;
        });

    }

    /**
     * Obtener validación si ya se encuentra verificada la muestra en la base de
     * datos.
     *
     * @param idOrder Id de la orden a rechazar.
     * @param idDestination Destino.
     * @param idUser Usuario.
     * @param idBranch Id de la Sede
     * @param idSample Id de la Muestra
     * @param idRoute
     * @param destination
     *
     * @return Retorna la trazabilidad de la muestra.
     *
     * @throws Exception Error en la base de datos.
     */
    default SampleTracking verifyDestination(long idOrder, int idDestination, int idUser, int idBranch, int idSample, Integer idRoute, Destination destination) throws Exception
    {

        // Año de la orden
        Integer year = Tools.YearOfOrder(String.valueOf(idOrder));
        Integer currentYear = DateTools.dateToNumberYear(new Date());
        String lab25 = year.equals(currentYear) ? "lab25" : "lab25_" + year;

        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getConnection())
                .withTableName(lab25);

        HashMap parameters = new HashMap();
        parameters.put("lab22c1", idOrder);
        parameters.put("lab04c1", idUser);
        parameters.put("lab42c1", idDestination);
        parameters.put("lab25c1", timestamp);
        parameters.put("lab25c2", 1);

        insert.execute(parameters);

        updateSampleState(idOrder, idUser, idBranch, idDestination, idSample, LISEnum.ResultSampleState.CHECKED.getValue(), idRoute);

        SampleTracking tracking = new SampleTracking();
        tracking.setOrder(idOrder);
        tracking.setId(idSample);
        tracking.setState(LISEnum.ResultSampleState.CHECKED.getValue());
        tracking.getDestination().setId(idDestination);
        tracking.getDestination().setDestination(destination);
        tracking.setTests(null);
        tracking.setMotive(null);

        return tracking;
    }

    /**
     * Verifica si el destino ya esta verificado.
     *
     * @param idOrder Id de la orden.
     * @param idDestination Destino.
     *
     * @return Retorna si el destino ya se verifico.
     *
     * @throws Exception Error en la base de datos.
     */
    default boolean verifyDestination(long idOrder, int idDestination) throws Exception
    {
        // Año de la orden
        Integer year = Tools.YearOfOrder(String.valueOf(idOrder));
        Integer currentYear = DateTools.dateToNumberYear(new Date());
        String lab25 = year.equals(currentYear) ? "lab25" : "lab25_" + year;

        String query = ISOLATION_READ_UNCOMMITTED + "SELECT COUNT(*) AS verify FROM " + lab25 + " as lab25 WHERE lab22c1 = ? AND lab42c1 = ?";

        return getConnection().queryForObject(query,
                new Object[]
                {
                    idOrder, idDestination
                }, (ResultSet rs, int i) -> rs.getInt("verify") >= 1);
    }

    /**
     * Obtiene las preguntas que se le deben aplicar a una orden.
     *
     * @param order Orden a la cual se le realizara la entrevista.
     *
     * @return Retorna la lista de preguntas que se le deben hacer a la orden.
     *
     * @throws Exception Error en la base de datos.
     */
    default List<Question> getInterview(Order order) throws Exception
    {
        String tests = order.getSamples().stream()
                .map(sample -> sample.getTests().stream().map(test -> test.getId().toString()).collect(Collectors.joining(", ")))
                .collect(Collectors.joining(", "));

        String query = "SELECT DISTINCT lab70.lab70c1, "
                + "lab70.lab70c2, "
                + "lab70.lab70c3, "
                + "lab70.lab70c4, "
                + "lab70.lab70c5, "
                + "lab70.lab70c6, "
                + "lab92.lab92c2, "
                + "lab70.lab07c1, "
                + "lab23.lab23c1 AS lab23c1, "
                + "lab23.lab70c5 AS type, "
                + "lab92.lab92c1 "
                + " FROM lab23 "
                + " FULL JOIN lab70 ON lab23.lab22c1 = ? AND lab23.lab70c1 = lab70.lab70c1 "
                + " INNER JOIN lab92 ON lab70.lab70c1 = lab92.lab70c1 AND (lab70.lab07c1 = 1 OR (lab23.lab90c1 IS NOT NULL OR lab23.lab23c1 IS NOT NULL)) "
                + " LEFT JOIN lab44 ON lab44.lab44c1 = lab92.lab44c1 AND (lab44.lab07c1 = 1 OR (lab23.lab90c1 IS NOT NULL OR lab23.lab23c1 IS NOT NULL)) "
                + " LEFT JOIN lab66 ON lab66.lab44c1 = lab44.lab44c1 "
                + " WHERE (lab66.lab66c1 = ? AND lab44.lab44c3 = 1) OR "
                + " (lab66.lab66c1 IN(" + tests + ") AND lab44.lab44c3 = 3) "
                + " ORDER BY lab92.lab92c1 ASC, lab70.lab70c1 ASC";
        List<Question> list = getConnection().query(query,
                new Object[]
                {
                    order.getOrderNumber(),
                    order.getType().getId()
                }, (ResultSet rs, int i) ->
        {
            Question question = new Question();
            question.setId(rs.getInt("lab70c1"));
            question.setName(rs.getString("lab70c2"));
            question.setQuestion(rs.getString("lab70c3"));
            question.setOpen(rs.getInt("lab70c4") == 1);
            question.setControl((rs.getString("type") == null || rs.getString("type").equals("-1")) ? rs.getShort("lab70c5") : rs.getShort("type"));
            question.setLastTransaction(rs.getTimestamp("lab70c6"));
            question.setRequired(rs.getInt("lab92c2") == 1);
            question.setState(rs.getInt("lab07c1") == 1);
            question.setInterviewAnswer(rs.getString("lab23c1"));
            question.setOrder(rs.getInt("lab92c1"));

            readAnswer(question, order.getOrderNumber());
            return question;
        });
        if (!list.isEmpty())
        {
            list = list.stream().distinct().collect(Collectors.toList());
        }
        return list;
    }

    /**
     * Obtiene las preguntas que se le deben aplicar a una orden.
     *
     * @param order Orden a la cual se le realizara la entrevista.
     *
     * @return Retorna la lista de preguntas que se le deben hacer a la orden.
     *
     * @throws Exception Error en la base de datos.
     */
    default List<Question> getInterviewskl(Order order, String intervieworder) throws Exception
    {
        String tests = order.getSamples().stream()
                .map(sample -> sample.getTests().stream().map(test -> test.getId().toString()).collect(Collectors.joining(", ")))
                .collect(Collectors.joining(", "));
        if (tests != null && !tests.isEmpty())
        {
            if (tests.contains(","))
            {
                String[] lines = tests.split(",");
                String testAux = "";
                for (String line : lines)
                {
                    if (line != null && !line.trim().isEmpty())
                    {
                        testAux += line.trim() + ", ";
                    }
                }
                testAux = !testAux.isEmpty() ? testAux.substring(0, testAux.lastIndexOf(", ")).trim() : testAux.trim();
                tests = testAux;
                tests = tests.startsWith(", ") ? tests.substring(tests.indexOf(", ") + 1).trim() : (tests.startsWith(",") ? tests.substring(tests.indexOf(",") + 1).trim() : tests);
                tests = tests.endsWith(", ") ? tests.substring(0, tests.lastIndexOf(", ")).trim() : (tests.endsWith(",") ? tests.substring(0, tests.lastIndexOf(",")).trim() : tests);
            }

//            
        } else
        {
            tests = "0";
        }

        String query = "SELECT DISTINCT lab70.lab70c1, "
                + "lab70.lab70c2, "
                + "lab70.lab70c3, "
                + "lab70.lab70c4, "
                + "lab70.lab70c5, "
                + "lab70.lab70c6, "
                + "lab92.lab92c2, "
                + "lab70.lab07c1, "
                + "lab23.lab23c1 AS lab23c1, "
                + "lab23.lab70c5 AS type, "
                + "lab92.lab92c1, "
                + "lab44.lab44c1, "
                + "lab44.lab44c2, "
                + "lab44.lab44c7 "
                + " FROM lab23 "
                + " FULL JOIN lab70 ON lab23.lab22c1 = ? AND lab23.lab70c1 = lab70.lab70c1 "
                + " INNER JOIN lab92 ON lab70.lab70c1 = lab92.lab70c1 AND (lab70.lab07c1 = 1 OR (lab23.lab90c1 IS NOT NULL OR lab23.lab23c1 IS NOT NULL)) "
                + " LEFT JOIN lab44 ON lab44.lab44c1 = lab92.lab44c1 AND (lab44.lab07c1 = 1 OR (lab23.lab90c1 IS NOT NULL OR lab23.lab23c1 IS NOT NULL)) and lab44c4 = 0"
                + " LEFT JOIN lab66 ON lab66.lab44c1 = lab44.lab44c1 "
                + " WHERE (lab66c1 = ? AND lab44c3 = 1) OR "
                + " (lab66c1 IN(" + tests + ") AND lab44c3 = 3) ";

        String interview = intervieworder;
        if (interview.equals("True"))
        {
            query = query + " ORDER BY lab44.lab44c7 ASC, lab44.lab44c1 ASC, lab92.lab92c1 ASC";
        } else
        {
            query = query + " ORDER BY lab92.lab92c1 ASC, lab70.lab70c1 ASC";
        }

        List<Question> list = getConnection().query(query,
                new Object[]
                {
                    order.getOrderNumber(),
                    order.getType().getId()
                }, (ResultSet rs, int i) ->
        {
            Question question = new Question();
            question.setId(rs.getInt("lab70c1"));
            question.setName(rs.getString("lab70c2"));
            question.setQuestion(rs.getString("lab70c3"));
            question.setOpen(rs.getInt("lab70c4") == 1);
            question.setControl((rs.getString("type") == null || rs.getString("type").equals("-1")) ? rs.getShort("lab70c5") : rs.getShort("type"));
            question.setLastTransaction(rs.getTimestamp("lab70c6"));
            question.setRequired(rs.getInt("lab92c2") == 1);
            question.setState(rs.getInt("lab07c1") == 1);
            question.setInterviewAnswer(rs.getString("lab23c1"));
            question.setOrder(rs.getInt("lab92c1"));
            question.getTypeInterview().setId(rs.getInt("lab44c1"));
            question.getTypeInterview().setName(rs.getString("lab44c2"));
            question.getTypeInterview().setOrderInterview(rs.getShort("lab44c7"));
            readAnswer(question, order.getOrderNumber());
            return question;
        });
        if (!list.isEmpty())
        {
            list = list.stream().distinct().collect(Collectors.toList());
        }
        return list;
    }

    /**
     * Obtener respuestas asociadas a una pregunta.
     *
     * @param question Instancia con los datos de la pregunta.
     * @param idOrder Id de la orden.
     */
    default void readAnswer(Question question, long idOrder)
    {
        try
        {
            String query = ISOLATION_READ_UNCOMMITTED + "SELECT DISTINCT lab90.lab90c1, "
                    + "lab90c2, "
                    + "lab91.lab70c1, "
                    + "lab90.lab07c1, "
                    + "lab23.lab22c1 "
                    + "FROM lab90 "
                    + "INNER JOIN lab70 ON lab70.lab70c1 = ? "
                    + "INNER JOIN lab91 ON lab91.lab90c1 = lab90.lab90c1 AND lab91.lab70c1 = lab70.lab70c1 "
                    + "LEFT JOIN lab23 ON lab23.lab90c1 = lab90.lab90c1 AND lab23.lab22c1 = ? AND lab23.lab70c1 = ?";
            question.setAnswers(getConnection().query(query,
                    new Object[]
                    {
                        question.getId(),
                        idOrder,
                        question.getId()
                    }, (ResultSet rs, int i) ->
            {
                Answer answer = new Answer();
                answer.setId(rs.getInt("lab90c1"));
                answer.setName(rs.getString("lab90c2"));
                answer.setState(rs.getInt("lab07c1") == 1);
                answer.setSelected(rs.getString("lab22c1") != null);
                return answer;
            }));
        } catch (EmptyResultDataAccessException ex)
        {
            question.setAnswers(new ArrayList<>());
        }
    }

    default void readAnswerDestination(Question question, long idOrder, int idDestination)
    {
        try
        {
            String query = ISOLATION_READ_UNCOMMITTED + "SELECT DISTINCT lab90.lab90c1, lab90c2, lab91.lab70c1, lab90.lab07c1, lab23.lab22c1 FROM lab90 "
                    + "INNER JOIN lab70 ON lab70.lab70c1 = ? "
                    + "INNER JOIN lab91 ON lab91.lab90c1 = lab90.lab90c1 AND lab91.lab70c1 = lab70.lab70c1 "
                    + "LEFT JOIN lab23 ON lab23.lab90c1 = lab90.lab90c1 AND lab23.lab22c1 = ? AND lab23.lab70c1 = ? AND lab23.lab53C1 = ?";
            question.setAnswers(getConnection().query(query,
                    new Object[]
                    {
                        question.getId(),
                        idOrder,
                        question.getId(),
                        idDestination
                    }, (ResultSet rs, int i) ->
            {
                Answer answer = new Answer();
                answer.setId(rs.getInt("lab90c1"));
                answer.setName(rs.getString("lab90c2"));
                answer.setState(rs.getInt("lab07c1") == 1);
                answer.setSelected(rs.getString("lab22c1") != null);
                return answer;
            }));
        } catch (EmptyResultDataAccessException ex)
        {
            question.setAnswers(new ArrayList<>());
        }
    }

    /**
     * Inserta los resultados de la entrevista de la orden.
     *
     * @param questions Preguntas que se deben insertar.
     * @param idOrder Id de la orden a la que se le realizo la entrevista.
     * @param user Usuario que ingreso la entrevista.
     *
     * @return Retorna la cantidad de registros insertados.
     *
     * @throws Exception Error en la base de datos.
     */
    default int insertResultInterview(List<Question> questions, long idOrder, AuthorizedUser user) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        List<HashMap> batchArray = new ArrayList<>();
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getConnection())
                .withTableName("lab23");

        deleteInterview(idOrder);
        for (Question question : questions)
        {
            if (!question.isOpen())
            {
                if (question.getAnswers().isEmpty())
                {
                    HashMap parameters = new HashMap();
                    parameters.put("lab22c1", idOrder);
                    parameters.put("lab70c1", question.getId());
                    parameters.put("lab23c2", timestamp);
                    parameters.put("lab04c1", user.getId());
                    parameters.put("lab70c5", question.getControl());
                    batchArray.add(parameters);
                } else
                {
                    for (Answer answer : question.getAnswers())
                    {
                        if (answer.isSelected())
                        {
                            HashMap parameters = new HashMap();
                            parameters.put("lab22c1", idOrder);
                            parameters.put("lab70c1", question.getId());
                            parameters.put("lab90c1", answer.getId());
                            parameters.put("lab23c2", timestamp);
                            parameters.put("lab04c1", user.getId());
                            parameters.put("lab70c5", question.getControl());
                            batchArray.add(parameters);
                        }
                    }
                }
            } else
            {
                HashMap parameters = new HashMap();
                parameters.put("lab22c1", idOrder);
                parameters.put("lab70c1", question.getId());
                parameters.put("lab23c1", question.getInterviewAnswer());
                parameters.put("lab23c2", timestamp);
                parameters.put("lab04c1", user.getId());
                parameters.put("lab70c5", question.getControl());
                batchArray.add(parameters);
            }
        }

        int[] inserted = insert.executeBatch(batchArray.toArray(new HashMap[batchArray.size()]));

        return inserted.length;
    }

    /**
     * Consulta las muestras retrasadas.
     *
     * @param idDestination Id del destino.
     * @param idSample Id de la sede.
     *
     * @return Retorna la lista de muestras retrasadas.
     *
     * @throws Exception Error en la base de datos.
     */
    default List<SampleDelayed> sampleDelayed(int idDestination, int idSample) throws Exception
    {
        String query = ISOLATION_READ_UNCOMMITTED + "SELECT filter.lab22c1, lab24.lab24c1, lab24c2, lab24c9, lab53.lab53c1, lab53.lab53c2, lab53.lab53c3, lab53c5, lab83.lab83c1, lab83.lab83c2, lab25.lab25c1 "
                + "FROM lab42 "
                + "INNER JOIN "
                + "	(SELECT lab22c1,lab24c1,max(lab42.lab42c1) lastId "
                + "	FROM lab25 "
                + "	INNER JOIN lab42 ON lab42.lab42c1 = lab25.lab42c1 "
                + "	INNER JOIN lab52 ON lab52.lab52c1 = lab42.lab52c1 "
                + "	INNER JOIN lab53 ON lab53.lab53c1 = lab42.lab53c1 "
                + "	GROUP BY lab22c1,lab24c1) filter ON filter.lastId = lab42.lab42c1 "
                + "INNER JOIN lab22 ON lab22.lab22c1 = filter.lab22c1 "
                + "INNER JOIN lab83 ON lab83.lab42c1 = lab42.lab42c1 AND lab83.lab10c1 = lab22.lab10c1 "
                + "INNER JOIN lab53 ON lab53.lab53c1 = lab42.lab53c1 "
                + "INNER JOIN lab52 ON lab52.lab52c1 = lab42.lab52c1 "
                + "INNER JOIN lab24 ON lab24.lab24c1 = lab52.lab24c1 "
                + "INNER JOIN lab25 ON lab25.lab42c1 = lab42.lab42c1 AND lab25.lab22c1 = filter.lab22c1 "
                + "WHERE lab42.lab53c1 = ? AND lab22.lab07c1 = 1 AND lab52.lab05c1 = ? AND (lab22c19 = 0 or lab22c19 is null)";

        return getConnection().query(query,
                new Object[]
                {
                    idDestination, idSample
                }, (ResultSet rs, int i) ->
        {
            SampleDelayed sampleDelayed = new SampleDelayed();
            sampleDelayed.setOrder(rs.getLong("lab22c1"));
            sampleDelayed.getDestination().setId(rs.getInt("lab53c1"));
            sampleDelayed.getDestination().setCode(rs.getString("lab53c2"));
            sampleDelayed.getDestination().setName(rs.getString("lab53c3"));
            sampleDelayed.getDestination().getType().setId(rs.getInt("lab53c5"));
            sampleDelayed.getSample().setId(rs.getInt("lab24c1"));
            sampleDelayed.getSample().setName(rs.getString("lab24c2"));
            sampleDelayed.getSample().setCodesample(rs.getString("lab24c9"));
            sampleDelayed.setExpectedTime(rs.getInt("lab83c1"));
            sampleDelayed.setMaxTime(rs.getInt("lab83c2"));
            sampleDelayed.setDate(rs.getTimestamp("lab25c1"));

            return sampleDelayed;
        });
    }

    /**
     * Elimina las preguntas asociadas a una orden.
     *
     * @param idOrder Id de la orden a la que se le borrara la entrevista.
     *
     * @return Retorna la cantidad de registros insertados.
     *
     * @throws Exception Error en la base de datos.
     */
    default int deleteInterview(long idOrder) throws Exception
    {
        String deleteSql = "DELETE FROM lab23 WHERE lab22c1 = ? AND Lab53c1 IS NULL";
        return getConnection().update(deleteSql, idOrder);
    }

    /**
     * Consulta la trazabilidad de la muestra.
     *
     * @param idOrder Id de la Orden.
     * @param idSample Id de la sede.
     *
     * @return Retorna la trazabilidad de la muestra.
     */
    default List<SampleTracking> getSampleTracking(int idSample, long idOrder)
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(idOrder));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab144 = year.equals(currentYear) ? "lab144" : "lab144_" + year;

            String query = ISOLATION_READ_UNCOMMITTED + "SELECT lab144.lab22c1, lab144.lab144c1, lab144.lab144c2, lab144.lab144c3, lab144.lab144c4, "
                    + "lab30.lab30c1, lab30.lab30c2, lab30.lab30c3, "
                    + "lab05.lab05c1, lab05.lab05c10, lab05.lab05c4, "
                    + "lab04.lab04c1, lab04.lab04c2, lab04.lab04c3 "
                    + "FROM  " + lab144 + " as lab144 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab144.lab04c1 "
                    + "LEFT JOIN lab05 ON lab05.lab05c1 = lab144.lab05c1 "
                    + "LEFT JOIN lab30 ON lab30.lab30c1 = lab144.lab30c1 "
                    + "WHERE lab144.lab22c1 = ? AND lab144.lab24c1 = ?";

            return getConnection().query(query,
                    new Object[]
                    {
                        idOrder, idSample
                    }, (ResultSet rs, int i) ->
            {
                SampleTracking sampleTracking = new SampleTracking();
                sampleTracking.setOrder(rs.getLong("lab22c1"));
                sampleTracking.setId(rs.getInt("lab144c1"));

                sampleTracking.getBranch().setId(rs.getInt("lab05c1"));
                sampleTracking.getBranch().setCode(rs.getString("lab05c10"));
                sampleTracking.getBranch().setName(rs.getString("lab05c4"));

                sampleTracking.getUser().setId(rs.getInt("lab04c1"));
                sampleTracking.getUser().setName(rs.getString("lab04c2"));
                sampleTracking.getUser().setLastName(rs.getString("lab04c3"));

                sampleTracking.getMotive().setId(rs.getInt("lab30c1"));
                sampleTracking.getMotive().setName(rs.getString("lab30c2"));
                sampleTracking.getMotive().setDescription(rs.getString("lab30c3"));

                sampleTracking.setDate(rs.getTimestamp("lab144c2"));
                sampleTracking.setState(rs.getInt("lab144c3"));
                sampleTracking.setComment(rs.getString("lab144c4"));

                if (sampleTracking.getState() == LISEnum.ResultSampleState.NEW_SAMPLE.getValue())
                {
                    getTestsSampleTracking(sampleTracking);
                }

                return sampleTracking;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            OrderCreationLog.error(ex);
            return new ArrayList<>(0);
        }
    }

    /**
     * Obtiene los ids de las muestras que no se encuentran en la tabla de
     * estado actual
     *
     * @param order Numero de Orden
     *
     * @return Lista de ids
     * @throws Exception Error en base de datos
     */
    default List<Integer> getNotExistsSampleTracking(long order) throws Exception
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(order));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab159 = year.equals(currentYear) ? "lab159" : "lab159_" + year;

            return getConnection().query("" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT   lab24c1 "
                    + "FROM      " + lab159 + " as lab159 "
                    + "WHERE    lab22c1 = ? ",
                    (ResultSet rs, int i) ->
            {
                return rs.getInt("lab24c1");
            }, order);
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Realiza una insercion en batch de los estados actuales de la muestra
     *
     * @param sampleTracking Lista de
     * {@link net.cltech.enterprisent.domain.operation.tracking.SampleState}
     *
     * @throws Exception Error en base de datos
     */
    default void insertSampleTracking(List<SampleState> sampleTracking) throws Exception
    {
        Integer year = Tools.YearOfOrder(String.valueOf(sampleTracking.get(0).getOrder()));
        Integer currentYear = DateTools.dateToNumberYear(new Date());
        String lab159 = year.equals(currentYear) ? "lab159" : "lab159_" + year;

        getConnection().batchUpdate(""
                + "INSERT INTO " + lab159 + " (lab22c1, lab24c1, lab159c1, lab04c1, lab159c2, lab05c1) "
                + "VALUES(?,?,?,?,?,?)",
                new BatchPreparedStatementSetter()
        {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException
            {
                SampleState tracking = sampleTracking.get(i);
                ps.setLong(1, tracking.getOrder());
                ps.setInt(2, tracking.getSample().getId());
                ps.setInt(3, tracking.getState());
                ps.setInt(4, tracking.getUser().getId());
                ps.setTimestamp(5, new Timestamp(tracking.getDate().getTime()));
                ps.setInt(6, tracking.getBranch().getId());
            }

            @Override
            public int getBatchSize()
            {
                return sampleTracking.size();
            }
        });
    }

    /**
     * Obtiene los examenes a retomar de una orden
     *
     * @param order Numero de Orden
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.tracking.TestToRecall}
     * @throws Exception Error en base de datos
     */
    default List<TestToRecall> getTestToRecall(long order) throws Exception
    {
        return getConnection().query("" + ISOLATION_READ_UNCOMMITTED
                + "SELECT   lab144.lab144c1 "
                + " , lab39.lab39c1 "
                + " , lab39.lab39c2 "
                + " , lab39.lab39c3 "
                + " , lab39.lab39c4 "
                + " , lab39.lab39c37 "
                + "FROM     lab144 "
                + "         INNER JOIN lab160 ON lab160.lab144c1 = lab144.lab144c1 "
                + "         INNER JOIN lab39 on lab160.lab39c1 = lab39.lab39c1 "
                + "WHERE    lab144.lab22c1 = ? "
                + "         AND lab144.lab144c3 = 1   ",
                (ResultSet rs, int numRow) ->
        {
            TestToRecall record = new TestToRecall();
            record.setId(rs.getInt("lab144c1"));
            Test test = new Test();
            test.setId(rs.getInt("lab39c1"));
            test.setCode(rs.getString("lab39c2"));
            test.setAbbr(rs.getString("lab39c3"));
            test.setName(rs.getString("lab39c4"));
            test.setTestType(rs.getShort("lab39c37"));
            record.getTests().add(test);
            return record;
        }, order);
    }

    /**
     * Actualiza el estado de las ordenes a rellamado
     *
     * @param order Numero de Orden
     * @param tests Lista de
     * {@link net.cltech.enterprisent.domain.operation.tracking.TestToRecall}
     * @throws Exception Error en base de datos
     */
    default void updateStateToRecall(long order, List<TestToRecall> tests) throws Exception
    {
        getConnection().batchUpdate(""
                + "UPDATE   lab144 "
                + "SET      lab144c3 = 5 "
                + "WHERE    lab144c1 = ? ", new BatchPreparedStatementSetter()
        {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException
            {
                ps.setInt(1, tests.get(i).getId());
            }

            @Override
            public int getBatchSize()
            {
                return tests.size();
            }
        });
    }

    /**
     * Obtiene la cantidad de examenes asociados a una muestra de una orden
     *
     * @param idOrder Id de la Orden.
     * @param idSample Id de la muestra.
     *
     * @return Retorna la cantidad de registros.
     */
    default int getTestsForSample(int idSample, long idOrder)
    {
        Integer year = Tools.YearOfOrder(String.valueOf(idOrder));
        Integer currentYear = DateTools.dateToNumberYear(new Date());
        String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;

        String query = ISOLATION_READ_UNCOMMITTED + "SELECT COUNT(*) "
                + "FROM  " + lab57 + " as lab57 "
                + "WHERE lab57.lab22c1 = ? AND lab57.lab24c1 = ?";

        return getConnection().queryForObject(query,
                new Object[]
                {
                    idOrder, idSample
                }, (ResultSet rs, int i) -> rs.getInt(1));
    }

    /**
     * Actializa toma la muestra ingresada desde el SKL
     *
     * @param idOrder Id de la Orden.
     * @param idSample Id de la sede.
     * @param idUser
     * @param testSampleTake
     * @param stateChange
     * @param testChange
     * @param listholiday
     * @return Retorna la cantidad de registros.
     * @throws java.lang.Exception
     */
    default TestSampleTake updateSampleTrackingTestTake(long idOrder, long idSample, int idUser, TestSampleTake testSampleTake, int stateChange, int testChange, List<String> listholiday, int branch, int samplestate) throws Exception
    {

        StringBuilder update = new StringBuilder();
        Date dateUpdate = new Date();
        Timestamp currentDate = new Timestamp(dateUpdate.getTime());

        update.append("UPDATE lab57 SET lab57c16 = ").append(stateChange);
        StringBuilder where = new StringBuilder();
        if (stateChange == LISEnum.ResultSampleState.CHECKED.getValue())
        {
            update.append(" , lab57c37 = '").append(currentDate)
                    .append("', lab57c38 = ").append(idUser)
                    .append(" , lab57c34 = ").append(DateTools.dateToNumber(currentDate));
            where.append(" WHERE lab22c1 = ").append(idOrder).append(" AND (lab39c1 = ").append(testChange).append(" OR lab57c14 = ").append(testChange).append(")");
            getConnection().update(update.toString() + where);
            
            if(samplestate < LISEnum.ResultSampleState.CHECKED.getValue())
            {
                updateSampleState(idOrder, idUser, branch, null, (int) idSample, LISEnum.ResultSampleState.CHECKED.getValue(), null);
            }
        }
        //PENDIENTE DESARROLLO
        else if (stateChange == LISEnum.ResultSampleState.PENDING.getValue() || stateChange == LISEnum.ResultSampleState.NEW_SAMPLE.getValue())
        {
            update.append(" , lab57c37 = NULL")
                    .append(", lab57c38 = NULL")
                    .append(" , lab57c34 = NULL");
            where.append(" WHERE lab22c1 = ").append(idOrder).append(" AND (lab39c1 = ").append(testChange).append(" OR lab57c14 = ").append(testChange).append(")");
            getConnection().update(update.toString() + where);

            //updateSampleState(idOrder, idUser, branch, null, (int)idSample, LISEnum.ResultSampleState.CHECKED.getValue(), null);
        } else
        {
            Test test = getTestProccessDay(testChange);
            String processday = test.getProccessDays();
            int deliveryDays = test.getDeliveryDays();

            Calendar calendarEnd = Calendar.getInstance();
            calendarEnd.setTime(currentDate);
            calendarEnd.add(Calendar.DAY_OF_YEAR, deliveryDays);

            Calendar calendarInit = Calendar.getInstance();
            calendarInit.setTime(currentDate);

            boolean validdayprocess = false;
            while (calendarInit.before(calendarEnd) || calendarInit.equals(calendarEnd))
            {
                int day = calendarInit.get(Calendar.DAY_OF_WEEK);
                day = day == 1 ? 7 : day - 1;
                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                String formatted = format1.format(calendarInit.getTime()) + " 00:00:00.0";

                if (!test.isExcludeHoliday() && listholiday.contains(formatted))
                {
                    calendarEnd.add(Calendar.DAY_OF_YEAR, 1);
                } else
                {
                    validdayprocess = validdayprocess == true ? validdayprocess : processday.contains(String.valueOf(day));
                }
                calendarInit.add(Calendar.DAY_OF_YEAR, 1);
            }

            if (!validdayprocess)
            {
                calendarEnd = null;
            }
            
            
            where = new StringBuilder();
            update.append(" , lab57c39 = '").append(currentDate)
                    .append("' , lab57c40 = ").append(idUser)
                    .append(" , lab57c56 = '").append(!validdayprocess ? null : new Timestamp(calendarEnd.getTime().getTime())).append("'");

            where.append(" WHERE lab22c1 = ").append(idOrder).append(" AND (lab39c1 = ").append(testChange).append(" OR lab57c14 = ").append(testChange).append(")");
            where.append("OR (lab22c1 = ").append(idOrder).append(" AND LAB39C1 = (SELECT LAB57C14 FROM LAB57 WHERE lab22c1 = ").append(idOrder).append(" and LAB39C1 = ").append(testChange).append(") AND LAB24C1 IS NULL AND LAB57C56 IS NULL) ");
            getConnection().update(update.toString() + where);
            
            if(samplestate == LISEnum.ResultSampleState.ORDERED.getValue())
            {
                updateSampleState(idOrder, idUser, branch, null, (int) idSample, LISEnum.ResultSampleState.COLLECTED.getValue(), null);
            }
        }
        return testSampleTake;
    }

    default Test getTestProccessDay(int idTest)
    {
        String query = ISOLATION_READ_UNCOMMITTED + "SELECT lab39.lab39c16, lab39.lab39c17, lab39.lab39c57 "
                + "FROM  lab39 "
                + "WHERE lab39.lab39c1 = ?";

        return getConnection().queryForObject(query, new Object[]
        {
            idTest
        }, (ResultSet rs, int i) ->
        {
            Test test = new Test();
            test.setDeliveryDays(rs.getInt("lab39c16"));
            test.setProccessDays(rs.getString("lab39c17"));
            test.setExcludeHoliday(rs.getBoolean("lab39c57"));

            return test;
        });
    }

    /**
     * Actializa toma la muestra ingresada desde el SKL con estado pendiente
     *
     * @param idOrder Id de la Orden.
     * @param idSample Id de la sede.
     * @param testSampleTake
     * @return Retorna la cantidad de registros.
     */
    default TestSampleTake updateSampleTrackingTestPending(long idOrder, long idSample, int idUser, TestSampleTake testSampleTake, int stateChange, int testChange, int motive) throws Exception
    {

        StringBuilder update = new StringBuilder();

        update.append("UPDATE lab57 SET lab57c16 = ").append(stateChange)
                .append(" , lab57c52 = ").append(motive)
                .append(" , lab57c66 = ").append(idUser)
                .append(" WHERE lab22c1 = ").append(idOrder).append(" AND lab39c1 = ").append(testChange);

        getConnection().update(update.toString());
        return testSampleTake;

    }

    /**
     * Lista Examnes desde la base de datos. con filtro de un estado
     *
     * @param idOrder
     * @param idSample
     *
     * @return Lista.
     * @throws Exception Error en la base de datos.
     */
    default List<TestSampleTakeTracking> listSampleTakeByOrder(long idOrder, int idSample) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED);
            query.append("SELECT lab22c1, lab57.lab24c1, lab57.lab39c1, lab57c16, lab57c52, lab39.lab39c4 ")
                    .append("FROM lab57 INNER JOIN lab39 ON lab57.lab39c1 = lab39.lab39c1 ")
                    .append(" WHERE lab22c1 = ").append(idOrder).append(" AND lab57.lab24c1 = ").append(idSample)
                    .append(" AND lab57c16 in (-1,2,1)");
            return getConnection().query(query.toString(),
                    (ResultSet rs, int i)
                    ->
            {
                TestSampleTakeTracking testSampleTakeTracking = new TestSampleTakeTracking();
                testSampleTakeTracking.setIdTest(rs.getInt("lab39c1"));
                testSampleTakeTracking.setState(rs.getInt("lab57c16"));
                testSampleTakeTracking.setMotive(rs.getInt("lab57c52"));
                testSampleTakeTracking.setName(rs.getString("lab39c4"));

                return testSampleTakeTracking;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Verifica que una muestra ya se encuentre verificada
     *
     * @param idOrder
     * @param idRoute
     * @return 1 Si esta verificada - 0 Si no esta verificada - -1 si surguio un
     * error
     * @throws Exception Error en la base de datos.
     */
    default int isSampleCheck(long idOrder, int idRoute) throws Exception
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(idOrder));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab25 = year.equals(currentYear) ? "lab25" : "lab25_" + year;

            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED);
            query.append("SELECT 1 AS Exist ").append("FROM ")
                    .append(lab25).append("  as lab25 ")
                    .append("WHERE lab22c1 = ").append(idOrder)
                    .append(" AND lab42c1 = ").append(idRoute);

            return getConnection().queryForObject(query.toString(), (ResultSet rs, int i)
                    ->
            {
                return rs.getInt("Exist");
            });
        } catch (Exception e)
        {
            return -1;
        }
    }

    /**
     * Obtiene un registro si esa muestra ya se encuentra verificada para
     * verificaion sencilla, Al igual que tambien consulta que una orden tenga
     * asociada una muestra en particular
     *
     * @param idOrder
     * @param sampleId
     * @return True si existe - False si no existe
     * @throws Exception Error en la base de datos.
     */
    default int isSampleCheckSimple(long idOrder, int sampleId) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED);
            query.append("SELECT 1 AS Exist ")
                    .append("FROM lab159 ")
                    .append("WHERE lab22c1 = ").append(idOrder)
                    .append(" AND lab24c1 = ").append(sampleId);
            return getConnection().queryForObject(query.toString(),
                    (ResultSet rs, int i) ->
            {
                return rs.getInt("Exist");
            });
        } catch (Exception e)
        {
            return -1;
        }
    }

    /**
     * Obtiene las preguntas que se le deben aplicar a un Destino.
     *
     * @param destination Destino a la cual se le debe realizar la pregunta
     *
     * @return Retorna la lista de preguntas que se le deben hacer a la orden.
     *
     * @throws Exception Error en la base de datos.
     */
    default List<Question> getInterviewDestination(Destination destination) throws Exception
    {

        String query = "SELECT DISTINCT lab70.lab70c1, lab70.lab70c2, lab70.lab70c3, lab70.lab70c4, lab70.lab70c5, lab70.lab70c6, lab92.lab92c2, lab70.lab07c1, lab92.lab92c1  "
                + " FROM lab70 "
                + " INNER JOIN lab92 ON lab70.lab70c1 = lab92.lab70c1 AND (lab70.lab07c1 = 1) "
                + " LEFT JOIN lab44 ON lab44.lab44c1 = lab92.lab44c1 AND (lab44.lab07c1 = 1) "
                + " LEFT JOIN lab66 ON lab66.lab44c1 = lab44.lab44c1 "
                + "  WHERE lab66c1 = ? AND lab44c3 = 4 "
                + " ORDER BY lab92.lab92c1 ASC, lab70.lab70c1 ASC ";
        List<Question> list = getConnection().query(query, (ResultSet rs, int i) ->
        {
            Question question = new Question();
            question.setId(rs.getInt("lab70c1"));
            question.setName(rs.getString("lab70c2"));
            question.setQuestion(rs.getString("lab70c3"));
            question.setOpen(rs.getInt("lab70c4") == 1);
            question.setLastTransaction(rs.getTimestamp("lab70c6"));
            question.setRequired(rs.getInt("lab92c2") == 1);
            question.setState(rs.getInt("lab07c1") == 1);
            question.setOrder(rs.getInt("lab92c1"));
            question.setControl(rs.getShort("lab70c5"));
            readAnswer(question, destination.getId());
            return question;
        }, destination.getId());
        if (!list.isEmpty())
        {
            list = list.stream().distinct().collect(Collectors.toList());
        }
        return list;
    }

    /**
     * Inserta los resultados de la entrevista de la orden con destino y sede.
     *
     * @param questions Preguntas que se deben insertar.
     * @param idOrder Id de la orden a la que se le realizo la entrevista.
     * @param idDestination
     * @param idBranch
     * @param user Usuario que ingreso la entrevista.
     *
     * @return Retorna la cantidad de registros insertados.
     *
     * @throws Exception Error en la base de datos.
     */
    default int insertResultInterviewDestination(List<Question> questions, long idOrder, int idSample, int idDestination, int idBranch, AuthorizedUser user) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        List<HashMap> batchArray = new ArrayList<>();
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getConnection())
                .withTableName("lab23");

        deleteInterview(idOrder);
        for (Question question : questions)
        {
            if (!question.isOpen())
            {
                if (question.getAnswers().isEmpty())
                {
                    HashMap parameters = new HashMap();
                    parameters.put("lab22c1", idOrder);
                    parameters.put("lab70c1", question.getId());
                    parameters.put("lab23c2", timestamp);
                    parameters.put("lab04c1", user.getId());
                    parameters.put("lab70c5", question.getControl());
                    parameters.put("lab53c1", idDestination);
                    parameters.put("lab05c1", idBranch);
                    parameters.put("lab24c1", idSample);
                    batchArray.add(parameters);
                } else
                {
                    for (Answer answer : question.getAnswers())
                    {
                        if (answer.isSelected())
                        {
                            HashMap parameters = new HashMap();
                            parameters.put("lab22c1", idOrder);
                            parameters.put("lab70c1", question.getId());
                            parameters.put("lab90c1", answer.getId());
                            parameters.put("lab23c2", timestamp);
                            parameters.put("lab04c1", user.getId());
                            parameters.put("lab70c5", question.getControl());
                            parameters.put("lab53c1", idDestination);
                            parameters.put("lab05c1", idBranch);
                            parameters.put("lab24c1", idSample);
                            batchArray.add(parameters);
                        }
                    }
                }
            } else
            {
                HashMap parameters = new HashMap();
                parameters.put("lab22c1", idOrder);
                parameters.put("lab70c1", question.getId());
                parameters.put("lab23c1", question.getInterviewAnswer());
                parameters.put("lab23c2", timestamp);
                parameters.put("lab04c1", user.getId());
                parameters.put("lab70c5", question.getControl());
                parameters.put("lab53c1", idDestination);
                parameters.put("lab05c1", idBranch);
                parameters.put("lab24c1", idSample);
                batchArray.add(parameters);
            }
        }

        int[] inserted = insert.executeBatch(batchArray.toArray(new HashMap[batchArray.size()]));

        return inserted.length;
    }

    /**
     * Obtiene las preguntas que se le deben aplicar a una orden con sede y
     * destino.
     *
     * @param order Orden a la cual se le realizara la entrevista.
     * @param idSample
     * @param idDestination
     * @param idBranch
     *
     * @return Retorna la lista de preguntas que se le deben hacer a la orden.
     *
     * @throws Exception Error en la base de datos.
     */
    default List<Question> getInterviewDestination(Order order, int idSample, int idDestination, int idBranch) throws Exception
    {
        String query = ISOLATION_READ_UNCOMMITTED + "SELECT DISTINCT lab70.lab70c1, lab70.lab70c2, lab70.lab70c3, lab70.lab70c4, lab70.lab70c5, lab70.lab70c6, lab92.lab92c2, lab70.lab07c1, lab23.lab23c1, lab23.lab70c5 AS type, lab23.lab24c1, lab92.lab92c1 "
                + " FROM lab23 "
                + " FULL JOIN lab70 ON  lab23.lab53c1 = ? AND lab23.lab05c1 = ? AND lab23.lab70c1 = lab70.lab70c1 "
                + " INNER JOIN lab92 ON lab70.lab70c1 = lab92.lab70c1 AND (lab70.lab07c1 = 1 OR (lab23.lab90c1 IS NOT NULL OR lab23.lab23c1 IS NOT NULL)) "
                + " LEFT JOIN lab44 ON lab44.lab44c1 = lab92.lab44c1 AND (lab44.lab07c1 = 1 OR (lab23.lab90c1 IS NOT NULL OR lab23.lab23c1 IS NOT NULL)) "
                + " LEFT JOIN lab66 ON lab66.lab44c1 = lab44.lab44c1 "
                + " WHERE lab23.lab44c3 = 4 and  lab23.lab22c1 = ? and lab23.lab24c1 = ? "
                + " ORDER BY lab92.lab92c1 ASC, lab70.lab70c1 ASC";
        List<Question> list = getConnection().query(query,
                new Object[]
                {
                    idDestination,
                    idBranch,
                    order.getOrderNumber(),
                    idSample
                //order.getType().getId()
                }, (ResultSet rs, int i) ->
        {
            Question question = new Question();
            question.setId(rs.getInt("lab70c1"));
            question.setName(rs.getString("lab70c2"));
            question.setQuestion(rs.getString("lab70c3"));
            question.setOpen(rs.getInt("lab70c4") == 1);
            question.setControl((rs.getString("type") == null || rs.getString("type").equals("-1")) ? rs.getShort("lab70c5") : rs.getShort("type"));
            question.setLastTransaction(rs.getTimestamp("lab70c6"));
            question.setRequired(rs.getInt("lab92c2") == 1);
            question.setState(rs.getInt("lab07c1") == 1);
            question.setInterviewAnswer(rs.getString("lab23c1"));
            question.setOrder(rs.getInt("lab92c1"));

            readAnswerDestination(question, order.getOrderNumber(), idDestination);
            return question;
        });
        if (!list.isEmpty())
        {
            list = list.stream().distinct().collect(Collectors.toList());
        }
        return list;
    }

    /**
     * Actualizar el estado actual de la muestra con la temperatura.
     *
     * @param idOrder Id de la orden a rechazar.
     * @param idUser Usuario Logeado.
     * @param idBranch Sede.
     * @param idSample Muestra.
     * @param type Indica si es aplazamiento o retoma.
     * @param currentDate
     * @param temperature
     *
     * @throws Exception Error en la base de datos.
     */
    default void insertSampleOrderedTemperature(long idOrder, Integer idUser, Integer idBranch, int idSample, int type, Date currentDate, String temperature) throws Exception
    {
        Timestamp timestamp = new Timestamp(currentDate.getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getConnection())
                .withTableName("lab159");

        HashMap parameters = new HashMap();
        parameters.put("lab22c1", idOrder);
        parameters.put("lab24c1", idSample);
        parameters.put("lab159c1", type);
        parameters.put("lab159c2", timestamp);
        parameters.put("lab04c1", idUser);
        parameters.put("lab05c1", idBranch);
        parameters.put("lab53c1", null);
        parameters.put("lab52c1", null);
        parameters.put("lab159c3", Double.parseDouble(temperature));

        insert.execute(parameters);
    }

    /**
     * Obtiene la muestra con las temperaturas minimas y maximas.
     *
     * @param idSample
     * @return
     * @throws java.lang.Exception
     */
    default Sample getTemperatures(int idSample) throws Exception
    {
        try
        {
            String query = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT lab24c15, lab24c16 "
                    + "FROM lab24 "
                    + "WHERE lab24c1 = ? ";

            return getConnection().queryForObject(query,
                    new Object[]
                    {
                        idSample
                    }, (ResultSet rs, int i) ->
            {
                Sample sample = new Sample();
                sample.setMinimumTemperature(rs.getFloat("lab24c15"));
                sample.setMaximumTemperature(rs.getFloat("lab24c16"));
                return sample;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Validar si un examen es un perfil
     *
     * @param testChange
     * @return Si es perfil retorna true.
     * @throws Exception Error en la base de datos.
     */
    default boolean validateProfile(int testChange) throws Exception
    {
        try
        {

            return getConnection().queryForObject("" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT lab39c37 "
                    + "FROM lab39"
                    + " WHERE lab39c1 = ? AND lab39c37 = 1",
                    new Object[]
                    {
                        testChange
                    }, (ResultSet rs, int i) -> true);
        } catch (EmptyResultDataAccessException ex)
        {
            return false;
        }
    }

    /**
     * Actializa toma la muestra ingresada desde el SKL con estado
     *
     * @param idOrder Id de la Orden.
     * @param idSample Id de la sede.
     * @param testSampleTake
     * @return Retorna la cantidad de registros.
     */
    default TestSampleTake updateSampleTrackingIsProfile(long idOrder, long idSample, int idUser, TestSampleTake testSampleTake, int stateChange, int testChange, int motive) throws Exception
    {

        StringBuilder update = new StringBuilder();

        update.append("UPDATE lab57 SET lab57c16 = ").append(stateChange)
                .append(" , lab57c52 = ").append(motive)
                .append(" , lab57c66 = ").append(idUser)
                .append(" WHERE lab22c1 = ").append(idOrder).append(" AND lab57c14 = ").append(testChange);

        getConnection().update(update.toString());
        return testSampleTake;
    }

    /**
     * Obtiene una lista de ordenes por un rango de fechas (inicial y final) y
     * por un id de una muestra en particular, si el id de esta es 0 debemos
     * traer todas las muestras de esta, lo mismo pasa con el idService, si este
     * es cero no se filtra por servicio, de lo contrario se debera filtrar por
     * ese servicio en especifico
     *
     * @param initialDate
     * @param endDate
     * @param idSample
     * @param idService
     *
     * @return Orden.
     * @throws Exception Error en la base de datos.
     */
    default List<Order> getSamplesByTemperatureAndDate(long initialDate, long endDate, int idSample, int idService,  List<Integer> laboratorys, int branch) throws Exception
    {
        try
        {
            StringBuilder sql = new StringBuilder();
            sql.append(ISOLATION_READ_UNCOMMITTED);
            sql.append("SELECT DISTINCT lab22.lab22c1 AS lab22c1, ")
                    .append("lab10.lab10c1 AS lab10c1, ")
                    .append("lab10.lab10c2 AS lab10c2, ")
                    .append("lab10.lab10c7 AS lab10c7, ")
                    .append("lab21.lab21c1 AS lab21c1, ")
                    .append("lab21.lab21c2 AS lab21c2, ")
                    .append("lab21.lab21c3 AS lab21c3, ")
                    .append("lab21.lab21c4 AS lab21c4, ")
                    .append("lab21.lab21c5 AS lab21c5, ")
                    .append("lab21.lab21c6 AS lab21c6 ")
                    .append("FROM lab57 ")
                    .append("JOIN lab22 on lab22.lab22c1 = lab57.lab22c1 ")
                    .append("JOIN lab10 on lab10.lab10c1 = lab22.lab10c1 ")
                    .append("JOIN lab21 on lab21.lab21c1 = lab22.lab21c1 ")
                    .append("WHERE lab57.lab57c34 BETWEEN ").append(initialDate).append(" AND ").append(endDate);

            if (idService > 0)
            {
                sql.append(" AND lab22.lab10c1  = ").append(idService);
            }
            
            sql.append(SQLTools.buildSQLLaboratoryFilter(laboratorys, branch));
            sql.append(" AND (lab22c19 = 0 or lab22c19 is null) ");
            
            return getConnection().query(sql.toString(), (ResultSet rs, int i)
                    ->
            {
                try
                {
                    Order order = new Order();
                    ServiceLaboratory service = new ServiceLaboratory();
                    Patient patient = new Patient();

                    order.setOrderNumber(rs.getLong("lab22c1"));
                    // Servicio de la orden
                    service.setId(rs.getInt("lab10c1"));
                    service.setName(rs.getString("lab10c2"));
                    service.setCode(rs.getString("lab10c7"));
                    order.setService(service);
                    // Paciente
                    patient.setId(rs.getInt("lab21c1"));
                    patient.setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                    patient.setName1(Tools.decrypt(rs.getString("lab21c3")));
                    patient.setName2(Tools.decrypt(rs.getString("lab21c4")));
                    patient.setLastName(Tools.decrypt(rs.getString("lab21c5")));
                    patient.setSurName(Tools.decrypt(rs.getString("lab21c6")));
                    order.setPatient(patient);
                    // Obtiene la lista de muestras relacionadas a una orden
                    order.setSamples(getSamplesByIdOrder(order.getOrderNumber(), idSample));

                    if (order.getSamples().isEmpty() || order.getSamples() == null)
                    {
                        return null;
                    } else
                    {
                        return order;
                    }
                } catch (Exception ex)
                {
                    return null;
                }
            });
        } catch (Exception e)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Obtiene las muestras asignadas a una orden, o la muestra
     *
     * @param idOrder
     * @param idSample
     *
     * @return Lista de estudios correspondientes a esa muestra para esa orden
     * @throws java.lang.Exception
     */
    default List<Sample> getSamplesByIdOrder(long idOrder, int idSample) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED);
            query.append("SELECT DISTINCT lab24.lab24c1 AS lab24c1, ")
                    .append("lab24.lab24c2 AS lab24c2, ")
                    .append("lab24.lab24c9 AS lab24c9, ")
                    .append("lab159.lab159c3 AS lab159c3, ")
                    .append("lab57.lab22c1 AS lab22c1 ")
                    .append("FROM lab57 ")
                    .append("JOIN lab24 on lab24.lab24c1 = lab57.lab24c1 ")
                    .append("JOIN lab159 on lab159.lab22c1 = lab57.lab22c1 ")
                    .append("WHERE lab57.lab22c1 = ").append(idOrder)
                    .append(" AND lab57.lab57c16 = 4 ");

            if (idSample > 0)
            {
                query.append(" AND lab24.lab24c1 = ").append(idSample);
            }

            return getConnection().query(query.toString(), (ResultSet rs, int i)
                    ->
            {
                try
                {
                    Sample sample = new Sample();
                    sample.setId(rs.getInt("lab24c1"));
                    sample.setName(rs.getString("lab24c2"));
                    sample.setCodesample(rs.getString("lab24c9"));
                    sample.setTemperature(rs.getDouble("lab159c3"));
                    sample.setTests(getTestsWithSameSample(idOrder, sample.getId()));

                    return sample;
                } catch (Exception ex)
                {
                    return null;
                }
            });
        } catch (Exception e)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Obtiene los estudios relacionados a una orden, con una muestra especifica
     * y validando que estos esten verificados
     *
     * @param idOrder
     * @param idSample
     * @return Lista de estudios correspondientes a esa muestra para esa orden
     * @throws java.lang.Exception
     */
    default List<Test> getTestsWithSameSample(long idOrder, int idSample) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED);
            query.append("SELECT lab39.lab39c1 AS lab39c1, ")
                    .append("lab39.lab39c2 AS lab39c2, ")
                    .append("lab39.lab39c3 AS lab39c3, ")
                    .append("lab39.lab39c4 AS lab39c4 ")
                    .append("FROM lab39 ")
                    .append("JOIN lab57 on lab57.lab39c1 = lab39.lab39c1 ")
                    .append("WHERE lab39.lab39c7 = 0 ")
                    .append(" AND lab57.lab22c1 = ").append(idOrder)
                    .append(" AND lab57.lab24c1 = ").append(idSample);

            return getConnection().query(query.toString(), (ResultSet rs, int i)
                    ->
            {
                Test test = new Test();
                test.setId(rs.getInt("lab39c1"));
                test.setCode(rs.getString("lab39c2"));
                test.setAbbr(rs.getString("lab39c3"));
                test.setName(rs.getString("lab39c4"));
                return test;
            });
        } catch (Exception e)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Obtiene la temperatura para una muestra especifica de una orden
     *
     * @param idOrder
     * @param idSample
     * @return La temperatura de la muestra
     * @throws java.lang.Exception
     */
    default Double getTemperature(long idOrder, int idSample, int idBranch) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED);
            query.append("SELECT lab159c3 ")
                    .append("FROM lab159 ")
                    .append("WHERE lab22c1 = ").append(idOrder)
                    .append(" AND lab24c1 = ").append(idSample)
                    .append(" AND lab05c1 = ").append(idBranch);

            return getConnection().queryForObject(query.toString(), (ResultSet rs, int i)
                    ->
            {
                return rs.getDouble("lab159c3");
            });
        } catch (Exception e)
        {
            return 0.0;
        }
    }

    /**
     * Actualiza el estado de la muestra en la base de datos - CON TEMPERATURA.
     *
     * @param idOrder Id de la orden a verificar.
     * @param tests Lista de examenes que seran verificados.
     * @param user Usuario Logeado.
     * @param idSample Id de la Sede.
     * @param type Tipo.
     * @param reason Razón.
     * @param idRoute Id ruta asignada
     * @param temperature
     *
     * @return Retorna objeto para la trazabilidad de la muestra.
     *
     * @throws Exception Error en la base de datos.
     */
    default SampleTracking sampleTracking(long idOrder, List<Test> tests, AuthorizedUser user, int idSample, int type, Reason reason, Integer idRoute, Double temperature) throws Exception
    {
        List<Object[]> parameters = new ArrayList<>();
        Date currentDate = new Date();
        SampleTracking sampleState = getSampleTracking(idSample, idOrder).stream().filter(state -> state.getState() == type).findAny().orElse(null);
        String update = "UPDATE lab57 SET lab57c16 = ? ";
        String where = " WHERE lab22c1 = ? AND lab39c1 = ? ";
        if (type == LISEnum.ResultSampleState.CHECKED.getValue())
        {
            update += ",lab57c34 = ?,lab57c37 = ?,lab57c38 = ? ";
            where += " AND lab57c16 != -1";
        } else if (type == LISEnum.ResultSampleState.COLLECTED.getValue())
        {
            update += ",lab57c39 = ?,lab57c40 = ? ";
            where += " AND lab57c16 != -1";
        }

        tests.forEach((test) ->
        {
            if (type == LISEnum.ResultSampleState.CHECKED.getValue())
            {
                if (sampleState != null)
                {
                    parameters.add(new Object[]
                    {
                        type,
                        DateTools.dateToNumber(sampleState.getDate()),
                        new Timestamp(sampleState.getDate().getTime()),
                        user.getId(),
                        idOrder,
                        test.getId()
                    });
                } else
                {
                    parameters.add(new Object[]
                    {
                        type,
                        DateTools.dateToNumber(currentDate),
                        new Timestamp(currentDate.getTime()),
                        user.getId(),
                        idOrder,
                        test.getId()
                    });
                }
            } else if (type == LISEnum.ResultSampleState.COLLECTED.getValue())
            {
                if (sampleState != null)
                {
                    parameters.add(new Object[]
                    {
                        type,
                        new Timestamp(sampleState.getDate().getTime()),
                        user.getId(),
                        idOrder,
                        test.getId()
                    });
                } else
                {
                    parameters.add(new Object[]
                    {
                        type,
                        new Timestamp(currentDate.getTime()),
                        user.getId(),
                        idOrder,
                        test.getId()
                    });
                }
            } else
            {
                parameters.add(new Object[]
                {
                    type,
                    idOrder,
                    test.getId()
                });

            }
        });
        getConnection().batchUpdate(update + where, parameters);
        insertSampleTracking(idOrder, user.getId(), user.getBranch(), reason, idSample, type, tests, currentDate);
        if (type != LISEnum.ResultSampleState.NEW_SAMPLE.getValue())
        {
            updateSampleState(idOrder, user.getId(), user.getBranch(), null, idSample, type, idRoute, temperature);
        }

        SampleTracking tracking = new SampleTracking();
        tracking.setOrder(idOrder);
        tracking.setId(idSample);
        tracking.setTests(type == LISEnum.ResultSampleState.NEW_SAMPLE.getValue() ? tests : null);
        tracking.setMotive(reason == null || reason.getMotive() == null || reason.getMotive().getId() == null ? null : reason.getMotive());
        tracking.setComment(reason == null ? null : reason.getComment());
        tracking.setState(type);

        return tracking;
    }

    /**
     * Actualiza el estado de la muestra en la base de datos.
     *
     * @param idOrder Id de la orden a verificar.
     * @param tests Lista de examenes que seran verificados.
     * @param user Usuario Logeado.
     * @param idSample Id de la Sede.
     * @param type Tipo.
     * @param reason Razón.
     * @param idRoute Id ruta asignada
     *
     * @return Retorna objeto para la trazabilidad de la muestra.
     *
     * @throws Exception Error en la base de datos.
     */
    default SampleTracking sampleTracking(long idOrder, List<Test> tests, AuthorizedUser user, int idSample, int type, Reason reason, Integer idRoute) throws Exception
    {
        List<Object[]> parameters = new ArrayList<>();
        Date currentDate = new Date();
        SampleTracking sampleState = getSampleTracking(idSample, idOrder).stream().filter(state -> state.getState() == type).findAny().orElse(null);
        String update = "UPDATE lab57 SET lab57c16 = ? ";
        String where = " WHERE lab22c1 = ? AND lab39c1 = ? ";
        if (type == LISEnum.ResultSampleState.CHECKED.getValue())
        {
            update += ",lab57c34 = ?,lab57c37 = ?,lab57c38 = ? ";
            where += " AND lab57c16 != -1";
        } else if (type == LISEnum.ResultSampleState.COLLECTED.getValue())
        {
            update += ",lab57c39 = ?,lab57c40 = ? ";
        }

        tests.forEach((test) ->
        {
            if (type == LISEnum.ResultSampleState.CHECKED.getValue())
            {
                if (sampleState != null)
                {
                    parameters.add(new Object[]
                    {
                        type,
                        DateTools.dateToNumber(sampleState.getDate()),
                        new Timestamp(sampleState.getDate().getTime()),
                        user.getId(),
                        idOrder,
                        test.getId()
                    });
                } else
                {
                    parameters.add(new Object[]
                    {
                        type,
                        DateTools.dateToNumber(currentDate),
                        new Timestamp(currentDate.getTime()),
                        user.getId(),
                        idOrder,
                        test.getId()
                    });
                }
            } else if (type == LISEnum.ResultSampleState.COLLECTED.getValue())
            {
                if (sampleState != null)
                {
                    parameters.add(new Object[]
                    {
                        type,
                        new Timestamp(sampleState.getDate().getTime()),
                        user.getId(),
                        idOrder,
                        test.getId()
                    });
                } else
                {
                    parameters.add(new Object[]
                    {
                        type,
                        new Timestamp(currentDate.getTime()),
                        user.getId(),
                        idOrder,
                        test.getId()
                    });
                }
            } else
            {
                parameters.add(new Object[]
                {
                    type,
                    idOrder,
                    test.getId()
                });

            }
        });
        getConnection().batchUpdate(update + where, parameters);
        insertSampleTracking(idOrder, user.getId(), user.getBranch(), reason, idSample, type, tests, currentDate);
        if (type != LISEnum.ResultSampleState.NEW_SAMPLE.getValue())
        {
            updateSampleState(idOrder, user.getId(), user.getBranch(), null, idSample, type, idRoute);
        }

        SampleTracking tracking = new SampleTracking();
        tracking.setOrder(idOrder);
        tracking.setId(idSample);
        tracking.setTests(type == LISEnum.ResultSampleState.NEW_SAMPLE.getValue() ? tests : null);
        tracking.setMotive(reason == null || reason.getMotive() == null || reason.getMotive().getId() == null ? null : reason.getMotive());
        tracking.setComment(reason == null ? null : reason.getComment());
        tracking.setState(type);

        return tracking;
    }

    /**
     * Actualizar el estado actual de la muestra - CON TEMPERATURA
     *
     * @param idOrder Id de la orden a rechazar.
     * @param idUser Usuario Logeado.
     * @param idBranch Sede.
     * @param idDestination Destino de la muestra.
     * @param idSample Muestra.
     * @param type Indica si es aplazamiento o retoma.
     * @param idRoute id de la ruta asignada, null si no esta asignada
     * @param temperature
     *
     * @throws Exception Error en la base de datos.
     */
    default void updateSampleState(long idOrder, Integer idUser, Integer idBranch, Integer idDestination, int idSample, int type, Integer idRoute, Double temperature) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        String update = "UPDATE lab159 SET lab159c1 = ?, lab04c1 = ?, lab159c2 = ?, lab53c1 = ?, lab05c1 = ?, lab159c3 = ? ";
        List<Object> params = new ArrayList<>();
        params.add(type);
        params.add(idUser);
        params.add(timestamp);
        params.add(idDestination);
        params.add(idBranch);
        params.add(temperature);

        if (idRoute != null)
        {
            update = update + ", lab52c1 = ?";
            params.add(idRoute);
        }
        update = update + " WHERE lab22c1 = ? AND lab24c1 = ? AND LAB05C1 = ?";
        params.add(idOrder);
        params.add(idSample);
        params.add(idBranch);

        getConnection().update(update, params.toArray());
    }

    /**
     * Obtiene la muestra con las temperaturas minimas y maximas.
     *
     * @param idOrder
     * @param idSample
     * @return
     * @throws java.lang.Exception
     */
    default SampleTracking getSampleTrackingRetake(long idOrder, int idSample) throws Exception
    {
        try
        {
            String query = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT lab30.lab30c1, lab30.lab30c2, lab144.lab144c4 "
                    + "FROM lab144 "
                    + "INNER JOIN lab30 ON "
                    + "lab30.lab30c1 = lab144.lab30c1 "
                    + "WHERE lab144.lab22c1 = ? "
                    + "AND lab144.lab24c1 = ? "
                    + "AND lab144.lab144c3 = 1 "
                    + "AND (lab144.lab144c4 = '') is false";

            return getConnection().queryForObject(query,
                    new Object[]
                    {
                        idOrder,
                        idSample
                    }, (ResultSet rs, int i) ->
            {
                SampleTracking sampleT = new SampleTracking();
                Motive motive = new Motive();
                motive.setId(rs.getInt("lab30c1"));
                motive.setName(rs.getString("lab30c2"));
                sampleT.setMotive(motive);
                sampleT.setComment(rs.getString("lab144c4"));
                return sampleT;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }
}
