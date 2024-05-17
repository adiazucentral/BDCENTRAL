package net.cltech.enterprisent.dao.interfaces.integration;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import net.cltech.enterprisent.domain.integration.dashboard.AreaDashboard;
import net.cltech.enterprisent.domain.integration.dashboard.DashBoardOpportunityTime;
import net.cltech.enterprisent.domain.integration.dashboard.DashBoardProductivity;
import net.cltech.enterprisent.domain.integration.dashboard.DashBoardSampleTaking;
import net.cltech.enterprisent.domain.integration.dashboard.TestDashboard;
import net.cltech.enterprisent.domain.integration.dashboard.TestTrackingDashboard;
import net.cltech.enterprisent.domain.integration.dashboard.UserDashboard;
import net.cltech.enterprisent.tools.DateTools;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.tools.enums.LISEnum;
import net.cltech.enterprisent.tools.log.integration.DashBoardLog;
import net.cltech.enterprisent.tools.log.integration.SigaLog;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Representa los métodos de acceso a base de datos para la información de los tableros.
 *
 * @version 1.0.0
 * @author eacuna
 * @since 12/07/2018
 * @see Creación
 */
public interface DashboardDao
{

    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getConnection();

    /**
     * Obtiene la coneccion a la base de datos de estadisticas
     *
     * @return jdbc
     */
    public JdbcTemplate getConnectionStat();

    /**
     * Conteo de productividad
     *
     * @param vInitial rango inicial
     * @param vFinal rango final
     * @param branch id sede
     * @param section id sección
     * @return Conteo de estados del examen por sede y sección
     * @throws Exception
     */
    default List<DashBoardProductivity> listProductivityBySection(Long vInitial, Long vFinal, int branch, int section) throws Exception
    {
        final List<DashBoardProductivity> productivity = new ArrayList<>();
        try
        {
            String query = ""
                    + "SELECT sta2c5,sta2c6,sta2c7 "
                    + ",sta3c4,sta3c5,sta3c6,sta3c10,sta3c11  ";
            String from = " "
                    + "FROM sta3 "
                    + "INNER JOIN sta2 ON sta2.sta2c1 = sta3.sta2c1 "
                    + "WHERE sta2.sta2c20 between  ? AND ?  "
                    + "AND sta2c5 = ? AND  sta3c4 = ?";

            getConnectionStat().query(query + from, (ResultSet rs) ->
            {
                DashBoardProductivity record = new DashBoardProductivity(rs.getInt("sta2c5"), rs.getInt("sta3c4"));
                if (!productivity.contains(record))
                {
                    record.setBranchName(rs.getString("sta2c7"));
                    record.setSectionName(rs.getString("sta3c6"));
                    productivity.add(record);
                }

                record = productivity.get(productivity.indexOf(record));

                if (rs.getInt("sta3c11") == LISEnum.ResultSampleState.CHECKED.getValue())
                {
                    record.setVerified(record.getVerified() + 1);
                }
                switch (rs.getInt("sta3c10"))
                {
                    case 5://Impresa
                        record.setResults(record.getResults() + 1);
                        record.setValidated(record.getValidated() + 1);
                        record.setPrinted(record.getPrinted() + 1);
                        break;
                    case 4://Validado
                        record.setResults(record.getResults() + 1);
                        record.setValidated(record.getValidated() + 1);
                        break;
                    case 2://Resultado
                        record.setResults(record.getResults() + 1);
                        break;
                }

            }, vInitial, vFinal, branch, section);
        }
        catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
        finally
        {
            return productivity;
        }
    }

    /**
     * Obtiene informacion necesaria del examen para el tablero de tiempos de oportunidad.
     *
     * @param idOrder
     * @param idTest
     * @param demographicSendDashboard
     * @param itemSendDashboardDemographic
     * @return Información para registrar tiempos de oportunidad en tableros.
     * @throws Exception
     */
    default DashBoardOpportunityTime getOpportunityTime(Long idOrder, Integer idTest, String demographicSendDashboard, String  itemSendDashboardDemographic) throws Exception
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(idOrder));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;
            String lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
            
            String condicionalDashoard = "";
            if(!itemSendDashboardDemographic.isEmpty() && !"0".equals(itemSendDashboardDemographic)){
                DashBoardLog.info("------------------------------ADICION DEMOGRAFICO URGENCIAS----------------------------------------------------------");
                condicionalDashoard = " AND lab22.lab_demo_" + demographicSendDashboard + " = " + itemSendDashboardDemographic;
            }
            
            DashBoardLog.info("---------------" + condicionalDashoard);
            return getConnection().queryForObject(""
                    + "SELECT   lab22.lab22c1, "
                    + "         lab10.lab10c1, "
                    + "         lab10.lab10c2, "
                    + "         lab10.lab10c7, "
                    + "         lab39.lab39c1, "
                    + "         lab39.lab39c2, "
                    + "         lab39.lab39c4, "
                    + "         lab39.lab39c3, "
                    + "         lab05.lab05c1, "
                    + "         lab05.lab05c4, "
                    + "         lab103.lab103c2, "
                    + "         lab21.lab21c3, "
                    + "         lab21.lab21c4, "
                    + "         lab21.lab21c5, "
                    + "         lab21.lab21c6, "
                    + "         lab22.lab22c3, "
                    + "         lab57.lab57c8, "
                    + "         lab57.lab57c18, "
                    + "         lab43.lab43c1, "
                    + "         lab57.lab57c37 AS verifyDate, "
                    + "         lab57.lab57c37 AS dateTake, "
                    + "         lab57.lab57c2 AS dateresult, "
                    + "         lab43.lab43c4 "
                    + "FROM      " + lab57 + " as lab57 "
                    + "INNER JOIN " + lab22 + " as lab22 ON lab22.lab22c1 = lab57.lab22c1 "
                    + "INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1 "
                    + "LEFT JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1 "
                    + "LEFT JOIN lab10 ON lab10.lab10c1 = lab22.lab10c1 "
                    + "LEFT JOIN lab05 ON lab05.lab05c1 = lab22.lab05c1 "
                    + "LEFT JOIN lab103 ON lab103.lab103c1 = lab22.lab103c1 "
                    + "LEFT JOIN lab43 ON lab43.lab43c1 = lab39.lab43c1 "
                    + "WHERE    lab57.lab22c1 = ? AND lab57.lab39c1 = ? AND (lab22c19 = 0 or lab22c19 is null) "
                    + condicionalDashoard,
                    (ResultSet rs, int numRow) ->
            {
                DashBoardOpportunityTime opportunityTime = new DashBoardOpportunityTime();
                opportunityTime.setOrder(rs.getLong("lab22c1"));
                opportunityTime.setIdService(rs.getInt("lab10c1"));
                opportunityTime.setServiceCode(rs.getString("lab10c7"));
                opportunityTime.setServiceName(rs.getString("lab10c2"));
                opportunityTime.setIdTest(rs.getInt("lab39c1"));
                opportunityTime.setTestCode(rs.getString("lab39c2"));
                opportunityTime.setTestName(rs.getString("lab39c4"));
                opportunityTime.setTestAbbreviation(rs.getString("lab39c3"));
                opportunityTime.setIdBranch(rs.getInt("lab05c1"));
                opportunityTime.setBranchName(rs.getString("lab05c4"));
                opportunityTime.setOrderType(rs.getString("lab103c2"));
                opportunityTime.setPatientName(Tools.decrypt(rs.getString("lab21c3")).concat(" ").concat(rs.getString("lab21c4") == null ? "" : Tools.decrypt(rs.getString("lab21c4")).concat(" ")).concat(Tools.decrypt(rs.getString("lab21c5"))).concat(rs.getString("lab21c6") == null ? "" : " " + Tools.decrypt(rs.getString("lab21c6"))));
                opportunityTime.setOrderDate(rs.getTimestamp("lab22c3"));
                opportunityTime.setValidated(rs.getInt("lab57c8") >= LISEnum.ResultTestState.VALIDATED.getValue());
                opportunityTime.setValidateDate(rs.getTimestamp("lab57c18"));
               
                opportunityTime.setIdSection(rs.getInt("lab43c1"));
                opportunityTime.setSectionName(rs.getString("lab43c4"));
                opportunityTime.setVerifyDate(rs.getTimestamp("verifyDate"));
                opportunityTime.setDateTake(rs.getTimestamp("dateTake"));
                opportunityTime.setResultDate(rs.getTimestamp("dateresult"));
                opportunityTime.setSection(rs.getString("lab43c4"));
                return opportunityTime;
            }, idOrder, idTest);
        }
        catch (EmptyResultDataAccessException ex)
        {
            DashBoardLog.info("error consulta orden" + ex);
            return null;
        }
    }
    
    /**
     * Obtiene informacion necesaria del examen para el tablero de tiempos de oportunidad.
     *
     * @param idOrder
     * @param idTest
     * @param demographicSendDashboard
     * @param itemSendDashboardDemographic
     * @return Información para registrar tiempos de oportunidad en tableros.
     * @throws Exception
     */
    default List<DashBoardOpportunityTime> getTestSendDashoboard(Long fromOrder, Long untilOrder, String demographicSendDashboard, String  itemSendDashboardDemographic) throws Exception
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(fromOrder));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;
            String lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
            
            String condicionalDashoard = "";
            if(!itemSendDashboardDemographic.isEmpty() && !"0".equals(itemSendDashboardDemographic)){
                DashBoardLog.info("------------------------------ADICION DEMOGRAFICO URGENCIAS----------------------------------------------------------");
                condicionalDashoard = " AND lab22.lab_demo_" + demographicSendDashboard + " = " + itemSendDashboardDemographic;
            }
            
            DashBoardLog.info("---------------" + condicionalDashoard);
            return getConnection().query(""
                    + "SELECT top 500 lab22.lab22c1, "
                    + "         lab10.lab10c1, "
                    + "         lab10.lab10c2, "
                    + "         lab10.lab10c7, "
                    + "         lab39.lab39c1, "
                    + "         lab39.lab39c2, "
                    + "         lab39.lab39c4, "
                    + "         lab39.lab39c3, "
                    + "         p.lab39c2 plab39c2 , "
                    + "         p.lab39c4 plab39c4, "
                    + "         p.lab39c3 plab39c3, "
                    + "         lab05.lab05c1, "
                    + "         lab05.lab05c4, "
                    + "         lab103.lab103c2, "
                    + "         lab21.lab21c2, "
                    + "         lab21.lab21c3, "
                    + "         lab21.lab21c4, "
                    + "         lab21.lab21c5, "
                    + "         lab21.lab21c6, "
                    + "         lab22.lab22c3, "
                    + "         lab57.lab57c8, "
                    + "         lab57.lab57c18, "
                    + "         lab57.lab57c14, "
                    + "         lab43.lab43c1, "
                    + "         lab57.lab57c37 AS verifyDate, "
                    + "         lab57.lab57c39 AS dateTake, "
                    + "         lab57.lab57c2 AS dateresult, "
                    + "         lab43.lab43c4 "
                    + "FROM      " + lab57 + " as lab57 "
                    + "INNER JOIN " + lab22 + " as lab22 ON lab22.lab22c1 = lab57.lab22c1 "
                    + "INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1 "
                    + "LEFT JOIN lab39 p ON p.lab39c1 = lab57.lab57c14 "        
                    + "LEFT JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1 "
                    + "LEFT JOIN lab10 ON lab10.lab10c1 = lab22.lab10c1 "
                    + "LEFT JOIN lab05 ON lab05.lab05c1 = lab22.lab05c1 "
                    + "LEFT JOIN lab103 ON lab103.lab103c1 = lab22.lab103c1 "
                    + "LEFT JOIN lab43 ON lab43.lab43c1 = lab39.lab43c1 "
                    + "WHERE lab22.lab22c1 between ? AND ?  and lab57c8 >= 4  and (lab57c72 = 0 or lab57c72 is null) AND (lab22c19 = 0 or lab22c19 is null) "
                    + condicionalDashoard,
                    (ResultSet rs, int numRow) ->
            {
                DashBoardOpportunityTime opportunityTime = new DashBoardOpportunityTime();
                opportunityTime.setOrder(rs.getLong("lab22c1"));
                opportunityTime.setIdService(rs.getInt("lab10c1"));
                opportunityTime.setServiceCode(rs.getString("lab10c7"));
                opportunityTime.setServiceName(rs.getString("lab10c2"));
                opportunityTime.setIdTest(rs.getInt("lab39c1"));
                opportunityTime.setTestCode(rs.getString("lab39c2"));
                opportunityTime.setTestName(rs.getString("lab39c4"));
                opportunityTime.setTestAbbreviation(rs.getString("lab39c3"));
                opportunityTime.setCodeProfile(rs.getString("plab39c2"));
                opportunityTime.setNameProfile(rs.getString("plab39c4"));
                opportunityTime.setAbbreviationProfile(rs.getString("plab39c3"));
                opportunityTime.setIdProfile(rs.getInt("lab57c14"));
                opportunityTime.setIdBranch(rs.getInt("lab05c1"));
                opportunityTime.setBranchName(rs.getString("lab05c4"));
                opportunityTime.setOrderType(rs.getString("lab103c2"));
                opportunityTime.setPatientHistory(Tools.decrypt(rs.getString("lab21c2")));
                opportunityTime.setPatientName(Tools.decrypt(rs.getString("lab21c3")).concat(" ").concat(rs.getString("lab21c4") == null ? "" : Tools.decrypt(rs.getString("lab21c4")).concat(" ")).concat(Tools.decrypt(rs.getString("lab21c5"))).concat(rs.getString("lab21c6") == null ? "" : " " + Tools.decrypt(rs.getString("lab21c6"))));
                opportunityTime.setOrderDate(rs.getTimestamp("lab22c3"));
                opportunityTime.setValidated(rs.getInt("lab57c8") >= LISEnum.ResultTestState.VALIDATED.getValue());
                opportunityTime.setValidateDate(rs.getTimestamp("lab57c18"));
                opportunityTime.setState(rs.getInt("lab57c8"));
               
                opportunityTime.setIdSection(rs.getInt("lab43c1"));
                opportunityTime.setSectionName(rs.getString("lab43c4"));
                opportunityTime.setVerifyDate(rs.getTimestamp("verifyDate"));
                opportunityTime.setDateTake(rs.getTimestamp("dateTake"));
                opportunityTime.setResultDate(rs.getTimestamp("dateresult"));
                opportunityTime.setSection(rs.getString("lab43c4"));
                return opportunityTime;
            }, fromOrder, untilOrder);
        }
        catch (EmptyResultDataAccessException ex)
        {
            DashBoardLog.info("error consulta orden" + ex);
            return null;
        }
    }
    
    
    
    /**
     * Obtiene informacion necesaria del examen para el tablero de tiempos de oportunidad.
     * @param fromOrder
     * @param untilOrder
     * @param demographicSendDashboard
     * @param itemSendDashboardDemographic
     * @return Información para registrar tiempos de oportunidad en tableros.
     * @throws Exception
     */
    default List<DashBoardOpportunityTime> getTestEntry(Long fromOrder, Long untilOrder, String demographicSendDashboard, String  itemSendDashboardDemographic) throws Exception
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(fromOrder));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;
            String lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
            
            String condicionalDashoard = "";
            if(!itemSendDashboardDemographic.isEmpty() && !"0".equals(itemSendDashboardDemographic)){
                DashBoardLog.info("------------------------------ADICION DEMOGRAFICO URGENCIAS----------------------------------------------------------");
                condicionalDashoard = " AND lab22.lab_demo_" + demographicSendDashboard + " = " + itemSendDashboardDemographic;
            }
            
            DashBoardLog.info("---------------" + condicionalDashoard);
            return getConnection().query(""
                    + "SELECT   lab22.lab22c1, "
                    + "         lab10.lab10c1, "
                    + "         lab10.lab10c2, "
                    + "         lab10.lab10c7, "
                    + "         lab39.lab39c1, "
                    + "         lab39.lab39c2, "
                    + "         lab39.lab39c4, "
                    + "         lab39.lab39c3, "
                    + "         p.lab39c2 plab39c2 , "
                    + "         p.lab39c4 plab39c4, "
                    + "         p.lab39c3 plab39c3, "
                    + "         lab05.lab05c1, "
                    + "         lab05.lab05c4, "
                    + "         lab103.lab103c2, "
                    + "         lab21.lab21c2, "
                    + "         lab21.lab21c3, "
                    + "         lab21.lab21c4, "
                    + "         lab21.lab21c5, "
                    + "         lab21.lab21c6, "
                    + "         lab22.lab22c3, "
                    + "         lab57.lab57c8, "
                    + "         lab57.lab57c18, "
                    + "         lab57.lab57c14, "
                    + "         lab43.lab43c1, "
                    + "         lab57.lab57c37 AS verifyDate, "
                    + "         lab57.lab57c39 AS dateTake, "
                    + "         lab57.lab57c2 AS dateresult, "
                    + "         lab43.lab43c4 "
                    + "FROM      " + lab57 + " as lab57 "
                    + "INNER JOIN " + lab22 + " as lab22 ON lab22.lab22c1 = lab57.lab22c1 "
                    + "INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1 "
                    + "LEFT JOIN lab39 p ON p.lab39c1 = lab57.lab57c14 "        
                    + "LEFT JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1 "
                    + "LEFT JOIN lab10 ON lab10.lab10c1 = lab22.lab10c1 "
                    + "LEFT JOIN lab05 ON lab05.lab05c1 = lab22.lab05c1 "
                    + "LEFT JOIN lab103 ON lab103.lab103c1 = lab22.lab103c1 "
                    + "LEFT JOIN lab43 ON lab43.lab43c1 = lab39.lab43c1 "
                    + "WHERE lab22.lab22c1 between ? AND ?  and lab57c8 = 0  and (lab57c73 = 0 or lab57c73 is null) AND (lab22c19 = 0 or lab22c19 is null) "
                    + condicionalDashoard,
                    (ResultSet rs, int numRow) ->
            {
                DashBoardOpportunityTime opportunityTime = new DashBoardOpportunityTime();
                opportunityTime.setOrder(rs.getLong("lab22c1"));
                opportunityTime.setIdService(rs.getInt("lab10c1"));
                opportunityTime.setServiceCode(rs.getString("lab10c7"));
                opportunityTime.setServiceName(rs.getString("lab10c2"));
                opportunityTime.setIdTest(rs.getInt("lab39c1"));
                opportunityTime.setTestCode(rs.getString("lab39c2"));
                opportunityTime.setTestName(rs.getString("lab39c4"));
                opportunityTime.setTestAbbreviation(rs.getString("lab39c3"));
                opportunityTime.setCodeProfile(rs.getString("plab39c2"));
                opportunityTime.setNameProfile(rs.getString("plab39c4"));
                opportunityTime.setAbbreviationProfile(rs.getString("plab39c3"));
                opportunityTime.setIdProfile(rs.getInt("lab57c14"));
                opportunityTime.setIdBranch(rs.getInt("lab05c1"));
                opportunityTime.setBranchName(rs.getString("lab05c4"));
                opportunityTime.setOrderType(rs.getString("lab103c2"));
                opportunityTime.setPatientHistory(Tools.decrypt(rs.getString("lab21c2")));
                opportunityTime.setPatientName(Tools.decrypt(rs.getString("lab21c3")).concat(" ").concat(rs.getString("lab21c4") == null ? "" : Tools.decrypt(rs.getString("lab21c4")).concat(" ")).concat(Tools.decrypt(rs.getString("lab21c5"))).concat(rs.getString("lab21c6") == null ? "" : " " + Tools.decrypt(rs.getString("lab21c6"))));
                opportunityTime.setOrderDate(rs.getTimestamp("lab22c3"));
                opportunityTime.setValidated(rs.getInt("lab57c8") >= LISEnum.ResultTestState.VALIDATED.getValue());
                opportunityTime.setValidateDate(rs.getTimestamp("lab57c18"));
                opportunityTime.setState(rs.getInt("lab57c8"));
               
                opportunityTime.setIdSection(rs.getInt("lab43c1"));
                opportunityTime.setSectionName(rs.getString("lab43c4"));
                opportunityTime.setVerifyDate(rs.getTimestamp("verifyDate"));
                opportunityTime.setDateTake(rs.getTimestamp("dateTake"));
                opportunityTime.setResultDate(rs.getTimestamp("dateresult"));
                opportunityTime.setSection(rs.getString("lab43c4"));
                return opportunityTime;
            }, fromOrder, untilOrder);
        }
        catch (EmptyResultDataAccessException ex)
        {
            DashBoardLog.info("error consulta orden" + ex);
            return null;
        }
    }
    
    /**
     * Actualizamos el registro de resultados en el campo lab57c50 el cual
     * indica que ese examen de esa orden ya fue enviado a un sistema central
     *
     * @param idOrder
     * @param idTest
     * @param idCentralSystem
     * @return Lista de ordenes que aun no se han enviado al sistema central
     * @throws Exception Error base de datos
     */
    default int updateSentDashboard(long idOrder, int idTest) throws Exception
    {
        try
        {
            return getConnection().update("UPDATE lab57 SET lab57c72 = 1 "
                    + "WHERE lab22c1 = ? "
                    + "AND lab39c1 = ?",
                    idOrder,
                    Integer.valueOf(idTest));
        } catch (Exception e)
        {
            return 0;
        }
    }
    
    /**
     * Actualizamos el registro de resultados en el campo lab57c50 el cual
     * indica que ese examen de esa orden ya fue enviado a un sistema central
     *
     * @param idOrder
     * @param idTest
     * @param idCentralSystem
     * @return Lista de ordenes que aun no se han enviado al sistema central
     * @throws Exception Error base de datos
     */
    default int updateSentDashboardEntry(long idOrder, int idTest) throws Exception
    {
        try
        {
            return getConnection().update("UPDATE lab57 SET lab57c73 = 1 "
                    + "WHERE lab22c1 = ? "
                    + "AND lab39c1 = ?",
                    idOrder,
                    Integer.valueOf(idTest));
        } catch (Exception e)
        {
            return 0;
        }
    }

    /**
     *
     * Retorna listado de usaurio para el dashboard.
     *
     * @param user
     * @param password
     * @return usuario del dashboard
     * @throws Exception Error en la base de datos.
     */
    default UserDashboard getUserDashboard(String user, String password) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT DISTINCT")
                    .append(" lab04.lab04c1 AS iduser,")
                    .append(" lab04.lab04c2 AS nameuser,")
                    .append(" lab04.lab04c3 AS lastname,")
                    .append(" lab04.lab04c5 AS password,")
                    .append(" lab04.lab04c8 AS passwordexpiration,")
                    .append(" lab04.lab04c4 AS userd,")
                    .append(" lab04.lab04c31 AS dashboard")
                    .append(" FROM lab04")
                    .append(" WHERE UPPER(lab04.lab04c4) = '").append(user.toUpperCase()).append("'")
                    .append(" AND lab04.lab04c5 = '").append(Tools.encrypt(password)).append("'");

            return getConnection().queryForObject(query.toString(),
                    (ResultSet rs, int i) ->
            {
                UserDashboard bean = new UserDashboard();
                bean.setLastName(rs.getString("lastname"));
                bean.setPassword(null);
                bean.setPasswordExpiration(rs.getTimestamp("passwordexpiration"));
                bean.setSuccess(rs.getInt("dashboard") == 1);
                bean.setValorIv(null);
                bean.setValorSalr(null);
                bean.setName(rs.getString("nameuser"));
                bean.setId(rs.getInt("iduser"));
                bean.setKey(true);
                bean.setUser(rs.getString("userd"));
                bean.setAccessDirect(false);
                bean.setAdministrator(true);

                return bean;
            });
        }
        catch (DataAccessException e)
        {
            return null;
        }
    }

    /**
     *
     * Retorna listado de examenes para el dashboard.
     *
     * @return listado de TestDashboard
     * @throws Exception Error en la base de datos.
     */
    default List<TestDashboard> listTestsDashboard() throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab39.lab39c1 AS idtest, lab39.lab39c2 AS codtest, lab39.lab39c3 AS abbrtest, lab39.lab39c4 AS nametest, ")
                    .append("lab39.lab39c7 AS agemintest, lab39.lab39c8 AS agemaxtest, lab39.lab39c9 AS ageuntittest, lab39.lab39c37 AS typetest, ")
                    .append("lab43.lab43c1 AS idarea, lab43.lab43c3 AS abbrarea,  lab43.lab43c4 AS namearea, lab39.lab07c1 AS statetest, lab39.lab39c24 AS viewInOrder ")
                    .append("FROM lab39 ")
                    .append("LEFT JOIN lab04 ON lab04.lab04c1 = lab39.lab04c1 ")
                    .append("LEFT JOIN lab43 ON lab43.lab43c1 = lab39.lab43c1 ")
                    .append("LEFT JOIN lab24 ON lab24.lab24c1 = lab39.lab24c1 ")
                    .append("LEFT JOIN lab45 ON lab45.lab45c1 = lab39.lab45c1 ")
                    .append("LEFT JOIN lab64 ON lab64.lab64c1 = lab39.lab64c1 ")
                    .append("LEFT JOIN lab80 ON lab80.lab80c1 = lab39.lab39c6 ");

            return getConnection().query(query.toString(),
                    (ResultSet rs, int i) ->
            {
                TestDashboard testDashboard = new TestDashboard();
                testDashboard.setId(rs.getInt("idtest"));
                testDashboard.setCode(rs.getString("codtest"));
                testDashboard.setAbbr(rs.getString("abbrtest"));
                testDashboard.setName(rs.getString("nametest"));
                testDashboard.setMinAge(rs.getInt("agemintest"));
                testDashboard.setMaxAge(rs.getInt("agemaxtest"));
                testDashboard.setAgeUnit(rs.getInt("ageuntittest"));
                testDashboard.setTestType(rs.getShort("typetest"));
                /*Area*/
                AreaDashboard area = new AreaDashboard();
                area.setId(rs.getInt("idarea"));
                area.setCode(rs.getString("abbrarea"));
                area.setAbbr(rs.getString("abbrarea"));
                area.setName(rs.getString("namearea"));
                testDashboard.setArea(area);

                testDashboard.setActive(rs.getInt("statetest") == 1);
                testDashboard.setViewInOrder(rs.getInt("viewInOrder") == 1);

                return testDashboard;
            });
        }
        catch (Exception e)
        {
            return new ArrayList<>(0);
        }

    }

    /**
     *
     * Retorna listado de objeto de productividad para los tableros.
     *
     * @param currentTime
     * @param hourback
     * @param branch
     * @param section
     * @return listado de DashBoardProductivity
     * @throws Exception Error en la base de datos.
     */
    default List<DashBoardProductivity> listProductivityByDashboard(Timestamp currentTime, Timestamp hourback, int branch, int section) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT sta2.sta2c5 AS idBranch, sta3.sta3c4 AS idSection, sta2.sta2c7 AS branchName, sta3.sta3c6 AS sectionName, ")
                    .append("sta3.sta3c11 AS sampleState, sta3.sta3c10 AS testState ")
                    .append("FROM sta3 ")
                    .append("INNER JOIN sta2 ON sta3.sta2c1  = sta2.sta2c1 ")
                    .append("WHERE sta2.sta2c21 BETWEEN '").append(hourback)
                    .append("' AND '").append(currentTime).append("'")
                    .append(" AND sta3.sta3c4 = ").append(section);
            
            if(branch != -2){
                query.append(" AND sta2.sta2c5 = ").append(branch);
            }
            SigaLog.info("QUERY CONSULTA TABLERO " + query.toString());
            return getConnectionStat().query(query.toString(),
                    (ResultSet rs, int i) ->
            {

                DashBoardProductivity bean = new DashBoardProductivity();
                
                bean.setIdBranch(rs.getInt("idBranch"));
                bean.setIdSection(rs.getInt("idSection"));
                bean.setBranchName(rs.getString("branchName"));
                bean.setSectionName(rs.getString("sectionName"));

              
                
                if (rs.getInt("sampleState") == LISEnum.ResultSampleState.CHECKED.getValue())
                {
                    bean.setVerified(bean.getVerified() + 1);
                }
                SigaLog.info("estate ERROR QUERY CONSULTA TABLERO " + rs.getInt("testState"));
                switch (rs.getInt("testState"))
                {
                    case 5://Impresa
                        bean.setResults(bean.getResults() + 1);
                        bean.setValidated(bean.getValidated() + 1);
                        bean.setPrinted(bean.getPrinted() + 1);
                        break;
                    case 4://Validado
                        bean.setResults(bean.getResults() + 1);
                        bean.setValidated(bean.getValidated() + 1);
                        break;
                    case 2://Resultado
                        bean.setResults(bean.getResults() + 1);
                        break;
                }

                return bean;
            });
        }
        catch (Exception e)
        {
            SigaLog.info("ERROR QUERY CONSULTA TABLERO " + e);
            return null;
        }

    }

    /**
     * Obtiene informacion necesaria del examen para el tablero de toma de muestras hospitalarias
     *
     * @param idOrder
     * @param idSample
     * @param idTest
     * @param demoHospitalLocation
     *
     * @return Información para registrar tiempos de oportunidad en tableros.
     * @throws Exception
     */
    default DashBoardSampleTaking getHospitalSampling(Long idOrder, Integer idSample, Integer idTest, String demoHospitalLocation) throws Exception
    {
        try
        {
            String query = ""
                    + "SELECT   lab22.lab22c1, "
                    + "         lab10.lab10c1, "
                    + "         lab10.lab10c2, "
                    + "         lab10.lab10c7, "
                    + "         lab05.lab05c1, "
                    + "         lab05.lab05c4, "
                    + "         lab103.lab103c2, "
                    + "         lab21.lab21c3, "
                    + "         lab21.lab21c4, "
                    + "         lab21.lab21c5, "
                    + "         lab21.lab21c6, "
                    + "         lab22.lab22c3 AS orderDate, "
                    + "         lab57.lab57c8, "
                    + "         lab57.lab57c18, "
                    + "         lab43.lab43c1, "
                    + "         lab57.lab57c37 AS verifyDate, "
                    + "         lab57.lab57c39 AS dateTake, "
                    + "         lab24.lab24c1 AS idSample, "
                    + "         lab24.lab24c2 AS sampleName, "
                    + "         lab24.lab24c9 AS sampleCode, ";

            if (!demoHospitalLocation.isEmpty())
            {
                query += "         lab22.lab_demo_" + demoHospitalLocation + " AS demoHospitalLocation, ";
            }

            query += "         lab43.lab43c4 "
                    + "FROM     lab57 "
                    + "INNER JOIN lab22 ON lab22.lab22c1 = lab57.lab22c1 "
                    + "INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1 "
                    + "INNER JOIN lab24 ON lab24.lab24c1 = lab57.lab24c1 "
                    + "LEFT JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1 "
                    + "LEFT JOIN lab10 ON lab10.lab10c1 = lab22.lab10c1 "
                    + "LEFT JOIN lab05 ON lab05.lab05c1 = lab22.lab05c1 "
                    + "LEFT JOIN lab103 ON lab103.lab103c1 = lab22.lab103c1 "
                    + "LEFT JOIN lab43 ON lab43.lab43c1 = lab39.lab43c1 "
                    + "WHERE    lab57.lab22c1 = ? AND lab57.lab24c1 = ? AND lab57.lab39c1 = ? AND (lab22c19 = 0 or lab22c19 is null) ";

            return getConnection().queryForObject(query,
                    (ResultSet rs, int numRow) ->
            {
                DashBoardSampleTaking hospitalSampling = new DashBoardSampleTaking();
                hospitalSampling.setOrder(rs.getLong("lab22c1"));
                hospitalSampling.setOrderDate(rs.getTimestamp("orderDate"));
                hospitalSampling.setIdService(rs.getInt("lab10c1"));
                hospitalSampling.setServiceCode(rs.getString("lab10c7"));
                hospitalSampling.setServiceName(rs.getString("lab10c2"));
                hospitalSampling.setIdBranch(rs.getInt("lab05c1"));
                hospitalSampling.setBranchName(rs.getString("lab05c4"));
                hospitalSampling.setOrderType(rs.getString("lab103c2"));
                hospitalSampling.setPatientName(Tools.decrypt(rs.getString("lab21c3")).concat(" ").concat(rs.getString("lab21c4") == null ? "" : Tools.decrypt(rs.getString("lab21c4")).concat(" ")).concat(Tools.decrypt(rs.getString("lab21c5"))).concat(rs.getString("lab21c6") == null ? "" : " " + Tools.decrypt(rs.getString("lab21c6"))));
                hospitalSampling.setIdSample(rs.getInt("idSample"));
                hospitalSampling.setSampleCode(rs.getString("sampleCode"));
                hospitalSampling.setSampleName(rs.getString("sampleName"));
                hospitalSampling.setSampleTakeDate(rs.getTimestamp("dateTake"));
                hospitalSampling.setTaked(rs.getTimestamp("dateTake") != null ? 1 : 0);
                if (!demoHospitalLocation.isEmpty())
                {
                    hospitalSampling.setHospitalUbication(rs.getString("demoHospitalLocation"));
                }

                return hospitalSampling;
            }, idOrder, idSample, idTest);
        }
        catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Obtenemos el id del perfil al que pertenece un estudio de la orden (Si es que este pertenece a un perfil)
     *
     * @param idOrder
     * @param idTest
     *
     * @return Id del perfil
     * @throws java.lang.Exception
     */
    default Integer getIdsProfilesOfTheOrder(long idOrder, int idTest) throws Exception
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(idOrder));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;
            
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab57c14 FROM  ").append(lab57).append(" as lab57 ")
                    .append("WHERE lab22c1 = ").append(idOrder)
                    .append(" AND lab39c1 = ").append(idTest);

            return getConnection().queryForObject(query.toString(), (ResultSet rs, int i) ->
            {
                return rs.getInt("lab57c14");
            });
        }
        catch (Exception e)
        {
            return 0;
        }
    }

    /**
     * Cuenta los analitos de un perfil
     *
     * @param idProfile
     *
     * @return Conteo de analitos
     * @throws java.lang.Exception
     */
    default Integer profileAnalyteCount(int idProfile) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT COUNT(lab46c1) AS analyteCounter FROM lab46 ")
                    .append("WHERE lab39c1 = ").append(idProfile);

            return getConnection().queryForObject(query.toString(), (ResultSet rs, int i) ->
            {
                return rs.getInt("analyteCounter");
            });
        }
        catch (Exception e)
        {
            return 0;
        }
    }

    /**
     * Cuenta los analitos validados de un perfil correspondiente a una orden
     *
     * @param idOrder
     * @param idTest
     *
     * @return Conteo de analitos validados del perfil
     * @throws java.lang.Exception
     */
    default Integer validatedAnalyteCounter(long idOrder, int idTest) throws Exception
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(idOrder));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;
            
            StringBuilder query = new StringBuilder();
            query.append("SELECT COUNT(lab39c1) AS analyteCounter FROM  ").append(lab57).append(" as lab57 ")
                    .append("WHERE lab22c1 = ").append(idOrder)
                    .append(" AND lab57c14 = ").append(idTest)
                    .append(" AND lab57c8 >= ").append(LISEnum.ResultTestState.VALIDATED.getValue());

            return getConnection().queryForObject(query.toString(), (ResultSet rs, int i) ->
            {
                return rs.getInt("analyteCounter");
            });
        }
        catch (Exception e)
        {
            return 0;
        }
    }

    /**
     * Obtiene informacion necesaria del examen para el tablero de seguimiento de pruebas
     *
     * @param idOrder
     * @param idTest
     * @param demoHospitalLocation
     *
     * @return Información para registrar tiempos de oportunidad en tableros.
     * @throws Exception
     */
    default TestTrackingDashboard getTestTracking(Long idOrder, Integer idTest, String demoHospitalLocation) throws Exception
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(idOrder));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;
            String lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
            
            String query = "SELECT   lab22.lab22c1, ";
            
            // Demografico Ubicación hospitalaria
            if (!demoHospitalLocation.isEmpty())
            {
                query += "      lab22.lab_demo_" + demoHospitalLocation + " AS demoHospitalLocation, ";
            }

            query += "          lab10.lab10c1, "
                    + "         lab10.lab10c2, "
                    + "         lab10.lab10c7, "
                    + "         lab39.lab39c1, "
                    + "         lab39.lab39c2, "
                    + "         lab39.lab39c3, "
                    + "         lab39.lab39c4, "
                    + "         lab05.lab05c1, "
                    + "         lab05.lab05c4, "
                    + "         lab103.lab103c2, "
                    + "         lab21.lab21c2, "
                    + "         lab21.lab21c3, "
                    + "         lab21.lab21c4, "
                    + "         lab21.lab21c5, "
                    + "         lab21.lab21c6, "
                    + "         lab22.lab22c3, "
                    + "         lab57.lab57c18, "
                    + "         lab43.lab43c1, "
                    + "         lab57.lab57c37 AS verifyDate, "
                    + "         lab57.lab57c37 AS dateTake, "
                    + "         lab43.lab43c4 "
                    + "FROM      " + lab57 + " as lab57 "
                    + "INNER JOIN " + lab22 + " as lab22 ON lab22.lab22c1 = lab57.lab22c1 "
                    + "INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1 "
                    + "LEFT JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1 "
                    + "LEFT JOIN lab10 ON lab10.lab10c1 = lab22.lab10c1 "
                    + "LEFT JOIN lab05 ON lab05.lab05c1 = lab22.lab05c1 "
                    + "LEFT JOIN lab103 ON lab103.lab103c1 = lab22.lab103c1 "
                    + "LEFT JOIN lab43 ON lab43.lab43c1 = lab39.lab43c1 "
                    + "WHERE    lab57.lab22c1 = ? AND lab57.lab39c1 = ? AND (lab22c19 = 0 or lab22c19 is null) ";

            return getConnection().queryForObject(query,
                    (ResultSet rs, int numRow) ->
            {
                TestTrackingDashboard testTracking = new TestTrackingDashboard();
                testTracking.setOrder(rs.getLong("lab22c1"));
                testTracking.setIdService(rs.getInt("lab10c1"));
                testTracking.setServiceCode(rs.getString("lab10c7"));
                testTracking.setServiceName(rs.getString("lab10c2"));
                testTracking.setIdTest(rs.getInt("lab39c1"));
                testTracking.setTestCode(rs.getString("lab39c2"));
                testTracking.setTestAbbreviation(rs.getString("lab39c3"));
                testTracking.setTestName(rs.getString("lab39c4"));
                testTracking.setIdBranch(rs.getInt("lab05c1"));
                testTracking.setBranchName(rs.getString("lab05c4"));
                testTracking.setOrderType(rs.getString("lab103c2"));
                testTracking.setPatientHistory(Tools.decrypt(rs.getString("lab21c2")));
                testTracking.setPatientName(Tools.decrypt(rs.getString("lab21c3")).concat(" ").concat(rs.getString("lab21c4") == null ? "" : Tools.decrypt(rs.getString("lab21c4")).concat(" ")).concat(Tools.decrypt(rs.getString("lab21c5"))).concat(rs.getString("lab21c6") == null ? "" : " " + Tools.decrypt(rs.getString("lab21c6"))));
                testTracking.setOrderDate(rs.getTimestamp("lab22c3"));
                testTracking.setValidateDate(rs.getTimestamp("lab57c18"));
                testTracking.setVerifyDate(rs.getTimestamp("verifyDate"));
                testTracking.setDateTake(rs.getTimestamp("dateTake"));
                if (!demoHospitalLocation.isEmpty())
                {
                    testTracking.setHospitalUbication(rs.getString("demoHospitalLocation"));
                }
                
                return testTracking;
            }, idOrder, idTest);
        }
        catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }
}
