package net.cltech.enterprisent.dao.interfaces.masters.test;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import net.cltech.enterprisent.domain.integration.orderForExternal.TestForExternal;
import net.cltech.enterprisent.domain.masters.billing.TestPrice;
import net.cltech.enterprisent.domain.masters.common.Item;
import net.cltech.enterprisent.domain.masters.demographic.PypDemographic;
import net.cltech.enterprisent.domain.masters.interview.Interview;
import net.cltech.enterprisent.domain.masters.test.Area;
import net.cltech.enterprisent.domain.masters.test.AutomaticTest;
import net.cltech.enterprisent.domain.masters.test.Concurrence;
import net.cltech.enterprisent.domain.masters.test.Container;
import net.cltech.enterprisent.domain.masters.test.Laboratory;
import net.cltech.enterprisent.domain.masters.test.Profile;
import net.cltech.enterprisent.domain.masters.test.ReferenceValue;
import net.cltech.enterprisent.domain.masters.test.Requirement;
import net.cltech.enterprisent.domain.masters.test.Sample;
import net.cltech.enterprisent.domain.masters.test.Technique;
import net.cltech.enterprisent.domain.masters.test.Test;
import net.cltech.enterprisent.domain.masters.test.TestBasic;
import net.cltech.enterprisent.domain.masters.test.TestByLaboratory;
import net.cltech.enterprisent.domain.masters.test.TestByService;
import net.cltech.enterprisent.domain.masters.test.TestInformation;
import net.cltech.enterprisent.domain.masters.test.Unit;
import net.cltech.enterprisent.tools.DateTools;
import net.cltech.enterprisent.tools.Tools;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información de las
 * Pruebas.
 *
 * @version 1.0.0
 * @author cmartin
 * @since 21/06/2017
 * @see Creación
 */
public interface TestDao
{

    /**
     * Lista las pruebas desde la base de datos.
     *
     * @return Lista de pruebas.
     * @throws Exception Error en la base de datos.
     */
    default List<TestBasic> list() throws Exception
    {
        try
        {
            return getConnection().query(""
                    + "SELECT   lab39c1"
                    + " , lab39c2"
                    + " , lab39c3"
                    + " , lab39c4"
                    + " , lab39c7"
                    + " , lab39c8"
                    + " , lab39c9"
                    + " , lab39c11"
                    + " , lab39c36"
                    + " , lab39.lab04c1"
                    + " , lab04c2"
                    + " , lab04c3"
                    + " , lab04c4"
                    + " , lab39.lab07c1"
                    + " , lab39.lab43c1"
                    + " , lab43c3"
                    + " , lab43c4"
                    + " , lab39c37"
                    + " , lab39c42"
                    + " , lab39c43"
                    + " , lab39c31"
                    + " , lab39c23"
                    + " , lab39c44"
                    + " , lab39c45"
                    + " , lab39c46"
                    + " , lab39c12"
                    + " , lab39c17"
                    + " , lab39c27 "
                    + " , lab24.lab24c1"
                    + " , lab24c2"
                    + " , lab24.lab24c10"
                    + " , lab45.lab45c1"
                    + " , lab45c2 "
                    + " , lab39.lab64c1 "
                    + " , lab64.lab64c2 "
                    + " , lab64.lab64c3 "
                    + " , lab39.lab39c24 "
                    + " , lab39.lab39c25 "
                    + " , lab39.lab39c29 "
                    + " , lab39.lab39c49 "
                    + " , lab39.lab39c20 "
                    + " , lab39.lab39c52 "
                    + " , lab80c1 "
                    + " , lab80c2 "
                    + " , lab80c3 "
                    + " , lab80c4 "
                    + " , lab80c5 "
                    + " , lab39.lab39c55 "
                    + "FROM lab39 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab39.lab04c1 "
                    + "LEFT JOIN lab43 ON lab43.lab43c1 = lab39.lab43c1 "
                    + "LEFT JOIN lab24 ON lab24.lab24c1 = lab39.lab24c1 "
                    + "LEFT JOIN lab45 ON lab45.lab45c1 = lab39.lab45c1 "
                    + "LEFT JOIN lab64 ON lab64.lab64c1 = lab39.lab64c1 "
                    + "LEFT JOIN lab80 ON lab80.lab80c1 = lab39.lab39c6 ",
                    (ResultSet rs, int i) ->
            {
                TestBasic testBasic = new TestBasic();
                testBasic.setId(rs.getInt("lab39c1"));
                testBasic.setCode(rs.getString("lab39c2"));
                testBasic.setAbbr(rs.getString("lab39c3"));
                testBasic.setName(rs.getString("lab39c4"));
                testBasic.setResultType(rs.getShort("lab39c11"));
                testBasic.setTestType(rs.getShort("lab39c37"));
                testBasic.setProcessingBy(rs.getShort("lab39c31"));
                testBasic.setDecimal(rs.getShort("lab39c12"));
                testBasic.setProcessingDays(rs.getString("lab39c17"));
                testBasic.setConfidential(rs.getInt("lab39c27") == 1);
                testBasic.setDeleteProfile(rs.getInt("lab39c23"));
                testBasic.setUnitAge(rs.getShort("lab39c9"));
                testBasic.setMinAge(rs.getInt("lab39c7"));
                testBasic.setMaxAge(rs.getInt("lab39c8"));
                /*Area*/
                testBasic.getArea().setId(rs.getInt("lab43c1"));
                testBasic.getArea().setAbbreviation(rs.getString("lab43c3"));
                testBasic.getArea().setName(rs.getString("lab43c4"));
                /*Usuario*/
                testBasic.getUser().setId(rs.getInt("lab04c1"));
                testBasic.getUser().setName(rs.getString("lab04c2"));
                testBasic.getUser().setLastName(rs.getString("lab04c3"));
                testBasic.getUser().setUserName(rs.getString("lab04c4"));
                /*Genero*/
                testBasic.getGender().setId(rs.getInt("lab80c1"));
                testBasic.getGender().setIdParent(rs.getInt("lab80c2"));
                testBasic.getGender().setCode(rs.getString("lab80c3"));
                testBasic.getGender().setEsCo(rs.getString("lab80c4"));
                testBasic.getGender().setEnUsa(rs.getString("lab80c5"));

                testBasic.setLastTransaction(rs.getTimestamp("lab39c36"));
                testBasic.setState(rs.getInt("lab07c1") == 1);
                testBasic.setPrintOrder(rs.getInt("lab39c42"));
                testBasic.setConversionFactor(rs.getBigDecimal("lab39c43"));

                testBasic.setDeltacheckDays(rs.getString("lab39c44") == null ? null : rs.getInt("lab39c44"));
                testBasic.setDeltacheckMin(rs.getString("lab39c45") == null ? null : rs.getBigDecimal("lab39c45"));
                testBasic.setDeltacheckMax(rs.getString("lab39c46") == null ? null : rs.getBigDecimal("lab39c46"));

                testBasic.getSample().setId(rs.getInt("lab24c1"));
                testBasic.getSample().setName(rs.getString("lab24c2"));
                testBasic.getSample().setLaboratorytype(rs.getString("lab24c10"));
                if (rs.getString("lab45c1") != null)
                {
                    testBasic.getUnit().setId(rs.getInt("lab45c1"));
                    testBasic.getUnit().setName(rs.getString("lab45c2"));
                }
                if (rs.getString("lab64c1") != null)
                {
                    Technique technique = new Technique();
                    technique.setId(rs.getInt("lab64c1"));
                    technique.setCode(rs.getString("lab64c2"));
                    technique.setName(rs.getString("lab64c3"));
                    testBasic.setTechnique(technique);
                }
                testBasic.setViewInOrderEntry(rs.getInt("lab39c24"));
                testBasic.setPrint(rs.getInt("lab39c25"));
                testBasic.setPrintHistoricGraphic(rs.getInt("lab39c29"));
                testBasic.setTax(rs.getBigDecimal("lab39c49"));
                testBasic.setBilling(rs.getBoolean("lab39c20"));
                testBasic.setInformedConsent(rs.getBoolean("lab39c52"));
                testBasic.setTestalarm(rs.getInt("lab39c55") == 1);

                return testBasic;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }
    
    /**
     * Lista las pruebas desde la base de datos.
     *
     * @param branch
     * @return Lista de pruebas.
     * @throws Exception Error en la base de datos.
    */
    default List<TestBasic> listTestByBranch(int branch) throws Exception
    {
        try
        {
            return getConnection().query(""
                    + "SELECT   lab39.lab39c1"
                    + " , lab39c2"
                    + " , lab39c3"
                    + " , lab39c4"
                    + " , lab39c7"
                    + " , lab39c8"
                    + " , lab39c9"
                    + " , lab39c11"
                    + " , lab39c36"
                    + " , lab39.lab04c1"
                    + " , lab04c2"
                    + " , lab04c3"
                    + " , lab04c4"
                    + " , lab39.lab07c1"
                    + " , lab39.lab43c1"
                    + " , lab43c3"
                    + " , lab43c4"
                    + " , lab39c37"
                    + " , lab39c42"
                    + " , lab39c43"
                    + " , lab39c31"
                    + " , lab39c23"
                    + " , lab39c44"
                    + " , lab39c45"
                    + " , lab39c46"
                    + " , lab39c12"
                    + " , lab39c17"
                    + " , lab39c27 "
                    + " , lab24.lab24c1"
                    + " , lab24c2"
                    + " , lab24.lab24c10"
                    + " , lab45.lab45c1"
                    + " , lab45c2 "
                    + " , lab39.lab64c1 "
                    + " , lab64.lab64c2 "
                    + " , lab64.lab64c3 "
                    + " , lab39.lab39c24 "
                    + " , lab39.lab39c25 "
                    + " , lab39.lab39c29 "
                    + " , lab39.lab39c49 "
                    + " , lab39.lab39c20 "
                    + " , lab39.lab39c52 "
                    + " , lab80c1 "
                    + " , lab80c2 "
                    + " , lab80c3 "
                    + " , lab80c4 "
                    + " , lab80c5 "
                    + " , lab39.lab39c55 "
                    + "FROM lab39 "
                    + "INNER JOIN lab145 ON lab145.lab39c1 = lab39.lab39c1 and lab05c1 = " + branch + " and lab145c1 = 1 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab39.lab04c1 "
                    + "LEFT JOIN lab43 ON lab43.lab43c1 = lab39.lab43c1 "
                    + "LEFT JOIN lab24 ON lab24.lab24c1 = lab39.lab24c1 "
                    + "LEFT JOIN lab45 ON lab45.lab45c1 = lab39.lab45c1 "
                    + "LEFT JOIN lab64 ON lab64.lab64c1 = lab39.lab64c1 "
                    + "LEFT JOIN lab80 ON lab80.lab80c1 = lab39.lab39c6 " ,
                    (ResultSet rs, int i) ->
            {
                TestBasic testBasic = new TestBasic();
                testBasic.setId(rs.getInt("lab39c1"));
                testBasic.setCode(rs.getString("lab39c2"));
                testBasic.setAbbr(rs.getString("lab39c3"));
                testBasic.setName(rs.getString("lab39c4"));
                testBasic.setResultType(rs.getShort("lab39c11"));
                testBasic.setTestType(rs.getShort("lab39c37"));
                testBasic.setProcessingBy(rs.getShort("lab39c31"));
                testBasic.setDecimal(rs.getShort("lab39c12"));
                testBasic.setProcessingDays(rs.getString("lab39c17"));
                testBasic.setConfidential(rs.getInt("lab39c27") == 1);
                testBasic.setDeleteProfile(rs.getInt("lab39c23"));
                testBasic.setUnitAge(rs.getShort("lab39c9"));
                testBasic.setMinAge(rs.getInt("lab39c7"));
                testBasic.setMaxAge(rs.getInt("lab39c8"));
                /*Area*/
                testBasic.getArea().setId(rs.getInt("lab43c1"));
                testBasic.getArea().setAbbreviation(rs.getString("lab43c3"));
                testBasic.getArea().setName(rs.getString("lab43c4"));
                /*Usuario*/
                testBasic.getUser().setId(rs.getInt("lab04c1"));
                testBasic.getUser().setName(rs.getString("lab04c2"));
                testBasic.getUser().setLastName(rs.getString("lab04c3"));
                testBasic.getUser().setUserName(rs.getString("lab04c4"));
                /*Genero*/
                testBasic.getGender().setId(rs.getInt("lab80c1"));
                testBasic.getGender().setIdParent(rs.getInt("lab80c2"));
                testBasic.getGender().setCode(rs.getString("lab80c3"));
                testBasic.getGender().setEsCo(rs.getString("lab80c4"));
                testBasic.getGender().setEnUsa(rs.getString("lab80c5"));

                testBasic.setLastTransaction(rs.getTimestamp("lab39c36"));
                testBasic.setState(rs.getInt("lab07c1") == 1);
                testBasic.setPrintOrder(rs.getInt("lab39c42"));
                testBasic.setConversionFactor(rs.getBigDecimal("lab39c43"));

                testBasic.setDeltacheckDays(rs.getString("lab39c44") == null ? null : rs.getInt("lab39c44"));
                testBasic.setDeltacheckMin(rs.getString("lab39c45") == null ? null : rs.getBigDecimal("lab39c45"));
                testBasic.setDeltacheckMax(rs.getString("lab39c46") == null ? null : rs.getBigDecimal("lab39c46"));

                testBasic.getSample().setId(rs.getInt("lab24c1"));
                testBasic.getSample().setName(rs.getString("lab24c2"));
                testBasic.getSample().setLaboratorytype(rs.getString("lab24c10"));
                if (rs.getString("lab45c1") != null)
                {
                    testBasic.getUnit().setId(rs.getInt("lab45c1"));
                    testBasic.getUnit().setName(rs.getString("lab45c2"));
                }
                if (rs.getString("lab64c1") != null)
                {
                    Technique technique = new Technique();
                    technique.setId(rs.getInt("lab64c1"));
                    technique.setCode(rs.getString("lab64c2"));
                    technique.setName(rs.getString("lab64c3"));
                    testBasic.setTechnique(technique);
                }
                testBasic.setViewInOrderEntry(rs.getInt("lab39c24"));
                testBasic.setPrint(rs.getInt("lab39c25"));
                testBasic.setPrintHistoricGraphic(rs.getInt("lab39c29"));
                testBasic.setTax(rs.getBigDecimal("lab39c49"));
                testBasic.setBilling(rs.getBoolean("lab39c20"));
                testBasic.setInformedConsent(rs.getBoolean("lab39c52"));
                testBasic.setTestalarm(rs.getInt("lab39c55") == 1);

                return testBasic;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Lista las pruebas desde la base de datos.
     *
     * @return Lista de pruebas.
     * @throws Exception Error en la base de datos.
     */
    default List<TestBasic> listAll() throws Exception
    {
        try
        {
            return getConnection().query(""
                    + "SELECT   lab39c1"
                    + " , lab39c2"
                    + " , lab39c3"
                    + " , lab39c4"
                    + " , lab39c7"
                    + " , lab39c8"
                    + " , lab39c9"
                    + " , lab39c11"
                    + " , lab39c36"
                    + " , lab39.lab04c1"
                    + " , lab04c2"
                    + " , lab04c3"
                    + " , lab04c4"
                    + " , lab39.lab07c1"
                    + " , lab39.lab43c1"
                    + " , lab43c3"
                    + " , lab43c4"
                    + " , lab39c37"
                    + " , lab39c42"
                    + " , lab39c43"
                    + " , lab39c31"
                    + " , lab39c44"
                    + " , lab39c45"
                    + " , lab39c46"
                    + " , lab39c12"
                    + " , lab39c17"
                    + " , lab39c27 "
                    + " , lab24.lab24c1"
                    + " , lab24c2"
                    + " , lab24.lab24c10"
                    + " , lab45.lab45c1"
                    + " , lab45c2 "
                    + " , lab39.lab64c1 "
                    + " , lab64.lab64c2 "
                    + " , lab64.lab64c3 "
                    + " , lab39.lab39c24 "
                    + " , lab39.lab39c25 "
                    + " , lab39.lab39c29 "
                    + " , lab39.lab39c49 "
                    + " , lab39.lab39c20 "
                    + " , lab39.lab39c52 "
                    + " , lab80c1 "
                    + " , lab80c2 "
                    + " , lab80c3 "
                    + " , lab80c4 "
                    + " , lab80c5 "
                    + " , lab39.lab39c55 "
                    + " , lab39.lab39c56 "
                    + "FROM lab39 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab39.lab04c1 "
                    + "LEFT JOIN lab43 ON lab43.lab43c1 = lab39.lab43c1 "
                    + "LEFT JOIN lab24 ON lab24.lab24c1 = lab39.lab24c1 "
                    + "LEFT JOIN lab45 ON lab45.lab45c1 = lab39.lab45c1 "
                    + "LEFT JOIN lab64 ON lab64.lab64c1 = lab39.lab64c1 "
                    + "LEFT JOIN lab80 ON lab80.lab80c1 = lab39.lab39c6 ",
                    (ResultSet rs, int i) ->
            {
                TestBasic testBasic = new TestBasic();
                testBasic.setId(rs.getInt("lab39c1"));
                testBasic.setCode(rs.getString("lab39c2"));
                testBasic.setAbbr(rs.getString("lab39c3"));
                testBasic.setName(rs.getString("lab39c4"));
                testBasic.setResultType(rs.getShort("lab39c11"));
                testBasic.setTestType(rs.getShort("lab39c37"));
                testBasic.setProcessingBy(rs.getShort("lab39c31"));
                testBasic.setDecimal(rs.getShort("lab39c12"));
                testBasic.setProcessingDays(rs.getString("lab39c17"));
                testBasic.setConfidential(rs.getInt("lab39c27") == 1);
                testBasic.setUnitAge(rs.getShort("lab39c9"));
                testBasic.setMinAge(rs.getInt("lab39c7"));
                testBasic.setMaxAge(rs.getInt("lab39c8"));
                /*Area*/
                testBasic.getArea().setId(rs.getInt("lab43c1"));
                testBasic.getArea().setAbbreviation(rs.getString("lab43c3"));
                testBasic.getArea().setName(rs.getString("lab43c4"));
                /*Usuario*/
                testBasic.getUser().setId(rs.getInt("lab04c1"));
                testBasic.getUser().setName(rs.getString("lab04c2"));
                testBasic.getUser().setLastName(rs.getString("lab04c3"));
                testBasic.getUser().setUserName(rs.getString("lab04c4"));
                /*Genero*/
                testBasic.getGender().setId(rs.getInt("lab80c1"));
                testBasic.getGender().setIdParent(rs.getInt("lab80c2"));
                testBasic.getGender().setCode(rs.getString("lab80c3"));
                testBasic.getGender().setEsCo(rs.getString("lab80c4"));
                testBasic.getGender().setEnUsa(rs.getString("lab80c5"));

                testBasic.setLastTransaction(rs.getTimestamp("lab39c36"));
                testBasic.setState(rs.getInt("lab07c1") == 1);
                testBasic.setPrintOrder(rs.getInt("lab39c42"));
                testBasic.setConversionFactor(rs.getBigDecimal("lab39c43"));

                testBasic.setDeltacheckDays(rs.getString("lab39c44") == null ? null : rs.getInt("lab39c44"));
                testBasic.setDeltacheckMin(rs.getString("lab39c45") == null ? null : rs.getBigDecimal("lab39c45"));
                testBasic.setDeltacheckMax(rs.getString("lab39c46") == null ? null : rs.getBigDecimal("lab39c46"));

                testBasic.getSample().setId(rs.getInt("lab24c1"));
                testBasic.getSample().setName(rs.getString("lab24c2"));
                testBasic.getSample().setLaboratorytype(rs.getString("lab24c10"));
                if (rs.getString("lab45c1") != null)
                {
                    testBasic.getUnit().setId(rs.getInt("lab45c1"));
                    testBasic.getUnit().setName(rs.getString("lab45c2"));
                }
                if (rs.getString("lab64c1") != null)
                {
                    Technique technique = new Technique();
                    technique.setId(rs.getInt("lab64c1"));
                    technique.setCode(rs.getString("lab64c2"));
                    technique.setName(rs.getString("lab64c3"));
                    testBasic.setTechnique(technique);
                }
                testBasic.setViewInOrderEntry(rs.getInt("lab39c24"));
                testBasic.setPrint(rs.getInt("lab39c25"));
                testBasic.setPrintHistoricGraphic(rs.getInt("lab39c29"));
                testBasic.setTax(rs.getBigDecimal("lab39c49"));
                testBasic.setBilling(rs.getBoolean("lab39c20"));
                testBasic.setInformedConsent(rs.getBoolean("lab39c52"));
                testBasic.setTestalarm(rs.getInt("lab39c55") == 1);
                testBasic.setExcludeHoliday(rs.getInt("lab39c56") == 1);
                return testBasic;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }
    
    /**
     * Lista las pruebas desde la base de datos.
     *
     * @return Lista de pruebas.
     * @throws Exception Error en la base de datos.
     */
    default List<TestBasic> listAll(int branch) throws Exception
    {
        try
        {
            return getConnection().query(""
                    + "SELECT   lab39.lab39c1"
                    + " , lab39c2"
                    + " , lab39c3"
                    + " , lab39c4"
                    + " , lab39c7"
                    + " , lab39c8"
                    + " , lab39c9"
                    + " , lab39c11"
                    + " , lab39c36"
                    + " , lab39.lab04c1"
                    + " , lab04c2"
                    + " , lab04c3"
                    + " , lab04c4"
                    + " , lab39.lab07c1"
                    + " , lab39.lab43c1"
                    + " , lab43c3"
                    + " , lab43c4"
                    + " , lab39c37"
                    + " , lab39c42"
                    + " , lab39c43"
                    + " , lab39c31"
                    + " , lab39c44"
                    + " , lab39c45"
                    + " , lab39c46"
                    + " , lab39c12"
                    + " , lab39c17"
                    + " , lab39c27 "
                    + " , lab24.lab24c1"
                    + " , lab24c2"
                    + " , lab24.lab24c10"
                    + " , lab45.lab45c1"
                    + " , lab45c2 "
                    + " , lab39.lab64c1 "
                    + " , lab64.lab64c2 "
                    + " , lab64.lab64c3 "
                    + " , lab39.lab39c24 "
                    + " , lab39.lab39c25 "
                    + " , lab39.lab39c29 "
                    + " , lab39.lab39c49 "
                    + " , lab39.lab39c20 "
                    + " , lab39.lab39c52 "
                    + " , lab80c1 "
                    + " , lab80c2 "
                    + " , lab80c3 "
                    + " , lab80c4 "
                    + " , lab80c5 "
                    + " , lab39.lab39c55 "
                    + " , lab39.lab39c56 "
                    + "FROM lab39 "
                    + "INNER JOIN lab145 ON lab145.lab39c1 = lab39.lab39c1 and lab05c1 = " + branch + " and lab145c1 = 1 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab39.lab04c1 "
                    + "LEFT JOIN lab43 ON lab43.lab43c1 = lab39.lab43c1 "
                    + "LEFT JOIN lab24 ON lab24.lab24c1 = lab39.lab24c1 "
                    + "LEFT JOIN lab45 ON lab45.lab45c1 = lab39.lab45c1 "
                    + "LEFT JOIN lab64 ON lab64.lab64c1 = lab39.lab64c1 "
                    + "LEFT JOIN lab80 ON lab80.lab80c1 = lab39.lab39c6 ",
                    (ResultSet rs, int i) ->
            {
                TestBasic testBasic = new TestBasic();
                testBasic.setId(rs.getInt("lab39c1"));
                testBasic.setCode(rs.getString("lab39c2"));
                testBasic.setAbbr(rs.getString("lab39c3"));
                testBasic.setName(rs.getString("lab39c4"));
                testBasic.setResultType(rs.getShort("lab39c11"));
                testBasic.setTestType(rs.getShort("lab39c37"));
                testBasic.setProcessingBy(rs.getShort("lab39c31"));
                testBasic.setDecimal(rs.getShort("lab39c12"));
                testBasic.setProcessingDays(rs.getString("lab39c17"));
                testBasic.setConfidential(rs.getInt("lab39c27") == 1);
                testBasic.setUnitAge(rs.getShort("lab39c9"));
                testBasic.setMinAge(rs.getInt("lab39c7"));
                testBasic.setMaxAge(rs.getInt("lab39c8"));
                /*Area*/
                testBasic.getArea().setId(rs.getInt("lab43c1"));
                testBasic.getArea().setAbbreviation(rs.getString("lab43c3"));
                testBasic.getArea().setName(rs.getString("lab43c4"));
                /*Usuario*/
                testBasic.getUser().setId(rs.getInt("lab04c1"));
                testBasic.getUser().setName(rs.getString("lab04c2"));
                testBasic.getUser().setLastName(rs.getString("lab04c3"));
                testBasic.getUser().setUserName(rs.getString("lab04c4"));
                /*Genero*/
                testBasic.getGender().setId(rs.getInt("lab80c1"));
                testBasic.getGender().setIdParent(rs.getInt("lab80c2"));
                testBasic.getGender().setCode(rs.getString("lab80c3"));
                testBasic.getGender().setEsCo(rs.getString("lab80c4"));
                testBasic.getGender().setEnUsa(rs.getString("lab80c5"));

                testBasic.setLastTransaction(rs.getTimestamp("lab39c36"));
                testBasic.setState(rs.getInt("lab07c1") == 1);
                testBasic.setPrintOrder(rs.getInt("lab39c42"));
                testBasic.setConversionFactor(rs.getBigDecimal("lab39c43"));

                testBasic.setDeltacheckDays(rs.getString("lab39c44") == null ? null : rs.getInt("lab39c44"));
                testBasic.setDeltacheckMin(rs.getString("lab39c45") == null ? null : rs.getBigDecimal("lab39c45"));
                testBasic.setDeltacheckMax(rs.getString("lab39c46") == null ? null : rs.getBigDecimal("lab39c46"));

                testBasic.getSample().setId(rs.getInt("lab24c1"));
                testBasic.getSample().setName(rs.getString("lab24c2"));
                testBasic.getSample().setLaboratorytype(rs.getString("lab24c10"));
                if (rs.getString("lab45c1") != null)
                {
                    testBasic.getUnit().setId(rs.getInt("lab45c1"));
                    testBasic.getUnit().setName(rs.getString("lab45c2"));
                }
                if (rs.getString("lab64c1") != null)
                {
                    Technique technique = new Technique();
                    technique.setId(rs.getInt("lab64c1"));
                    technique.setCode(rs.getString("lab64c2"));
                    technique.setName(rs.getString("lab64c3"));
                    testBasic.setTechnique(technique);
                }
                testBasic.setViewInOrderEntry(rs.getInt("lab39c24"));
                testBasic.setPrint(rs.getInt("lab39c25"));
                testBasic.setPrintHistoricGraphic(rs.getInt("lab39c29"));
                testBasic.setTax(rs.getBigDecimal("lab39c49"));
                testBasic.setBilling(rs.getBoolean("lab39c20"));
                testBasic.setInformedConsent(rs.getBoolean("lab39c52"));
                testBasic.setTestalarm(rs.getInt("lab39c55") == 1);
                testBasic.setExcludeHoliday(rs.getInt("lab39c56") == 1);
                return testBasic;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Lista las pruebas en las que el examen esta presente en la formula desde
     * la base de datos.
     *
     * @param id de la prueba.
     *
     * @return Lista de pruebas.
     * @throws Exception Error en la base de datos.
     */
    default List<TestBasic> list(Integer id) throws Exception
    {
        try
        {
            return getConnection().query(""
                    + "SELECT lab39.lab39c1, lab39c2, lab39c3, lab39c4, lab39c11, lab39c36, lab39.lab04c1, lab04c2, lab04c3, lab04c4, lab39.lab07c1, lab39.lab43c1, lab43c3, lab43c4, lab39c37, lab39c42, lab39c43, lab39c31, lab39c52 "
                    + "FROM lab46 "
                    + "INNER JOIN lab39 ON lab39.lab39c1 = lab46.lab39c1 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab39.lab04c1 "
                    + "LEFT JOIN lab43 ON lab43.lab43c1 = lab39.lab43c1 "
                    + "WHERE lab46c1 = ? AND lab46c2 = 1 AND lab39.lab07c1 = 1",
                    new Object[]
                    {
                        id
                    }, (ResultSet rs, int i) ->
            {
                TestBasic testBasic = new TestBasic();
                testBasic.setId(rs.getInt("lab39c1"));
                testBasic.setCode(rs.getString("lab39c2"));
                testBasic.setAbbr(rs.getString("lab39c3"));
                testBasic.setName(rs.getString("lab39c4"));
                testBasic.setResultType(rs.getShort("lab39c11"));
                testBasic.setTestType(rs.getShort("lab39c37"));
                testBasic.setProcessingBy(rs.getShort("lab39c31"));
                /*Area*/
                testBasic.getArea().setId(rs.getInt("lab43c1"));
                testBasic.getArea().setAbbreviation(rs.getString("lab43c3"));
                testBasic.getArea().setName(rs.getString("lab43c4"));
                /*Usuario*/
                testBasic.getUser().setId(rs.getInt("lab04c1"));
                testBasic.getUser().setName(rs.getString("lab04c2"));
                testBasic.getUser().setLastName(rs.getString("lab04c3"));
                testBasic.getUser().setUserName(rs.getString("lab04c4"));

                testBasic.setLastTransaction(rs.getTimestamp("lab39c36"));
                testBasic.setState(rs.getInt("lab07c1") == 1);
                testBasic.setPrintOrder(rs.getInt("lab39c42"));
                testBasic.setConversionFactor(rs.getBigDecimal("lab39c43"));
                testBasic.setInformedConsent(rs.getBoolean("lab39c52"));

                return testBasic;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Obtiene los examenes buscandolos por id
     *
     * @param ids Ids de examen separados por comma
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.masters.test.TestBasic}
     * @throws Exception Error en base de datos
     */
    default List<TestBasic> list(String ids) throws Exception
    {
        try
        {
            return getConnection().query(""
                    + "SELECT lab39.lab39c1, lab39c2, lab39c3, lab39c4, lab39c11, lab39c36, lab39.lab04c1, lab04c2, lab04c3, lab04c4, lab39.lab07c1, lab39.lab43c1, lab43c3, lab43c4, lab39c37, lab39c42, lab39c43, lab39c31, Lab39C17, lab39c56 "
                    + "FROM lab46 "
                    + "INNER JOIN lab39 ON lab39.lab39c1 = lab46.lab39c1 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab39.lab04c1 "
                    + "LEFT JOIN lab43 ON lab43.lab43c1 = lab39.lab43c1 "
                    + "WHERE lab46.lab39c1 IN (" + ids + ")",
                    (ResultSet rs, int i) ->
            {
                TestBasic testBasic = new TestBasic();
                testBasic.setId(rs.getInt("lab39c1"));
                testBasic.setCode(rs.getString("lab39c2"));
                testBasic.setAbbr(rs.getString("lab39c3"));
                testBasic.setName(rs.getString("lab39c4"));
                testBasic.setResultType(rs.getShort("lab39c11"));
                testBasic.setTestType(rs.getShort("lab39c37"));
                testBasic.setProcessingBy(rs.getShort("lab39c31"));
                testBasic.setProcessingDays(rs.getString("Lab39C17"));
                testBasic.setExcludeHoliday(rs.getInt("lab39c57") == 1);
                /*Area*/
                testBasic.getArea().setId(rs.getInt("lab43c1"));
                testBasic.getArea().setAbbreviation(rs.getString("lab43c3"));
                testBasic.getArea().setName(rs.getString("lab43c4"));
                /*Usuario*/
                testBasic.getUser().setId(rs.getInt("lab04c1"));
                testBasic.getUser().setName(rs.getString("lab04c2"));
                testBasic.getUser().setLastName(rs.getString("lab04c3"));
                testBasic.getUser().setUserName(rs.getString("lab04c4"));

                testBasic.setLastTransaction(rs.getTimestamp("lab39c36"));
                testBasic.setState(rs.getInt("lab07c1") == 1);
                testBasic.setPrintOrder(rs.getInt("lab39c42"));
                testBasic.setConversionFactor(rs.getBigDecimal("lab39c43"));

                return testBasic;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Registra una nueva prueba en la base de datos.
     *
     * @param test Instancia con los datos del prueba.
     *
     * @return Instancia con los datos del prueba.
     * @throws Exception Error en la base de datos.
     */
    default Test create(Test test) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getConnection())
                .withTableName("lab39")
                .usingGeneratedKeyColumns("lab39c1");

        HashMap parameters = new HashMap();
        parameters.put("lab43c1", test.getArea().getId());
        parameters.put("lab39c2", test.getCode().trim());
        parameters.put("lab39c3", test.getAbbr().trim());
        parameters.put("lab39c4", test.getName().trim());
        parameters.put("lab39c5", test.getLevel().getId());
        parameters.put("lab24c1", test.getSample().getId());
        parameters.put("lab44c1", test.getInterview() == null ? null : test.getInterview().getId());
        parameters.put("lab39c6", test.getGender().getId());
        parameters.put("lab39c7", test.getMinAge());
        parameters.put("lab39c8", test.getMaxAge());
        parameters.put("lab39c9", test.getUnitAge());
        parameters.put("lab39c10", test.getFormula());
        parameters.put("lab39c11", test.getResultType());
        parameters.put("lab39c12", test.getDecimal());
        parameters.put("lab64c1", test.getTechnique().getId());
        parameters.put("lab45c1", test.getUnit().getId());
        parameters.put("lab39c13", test.getVolume());
        parameters.put("lab39c14", test.getAutomaticResult());
        parameters.put("lab39c15", test.getMaxDays());
        parameters.put("lab39c16", test.getDeliveryDays());
        parameters.put("lab39c17", test.getProcessingDays());
        parameters.put("lab39c18", test.getSelfValidation());
        parameters.put("lab39c19", test.isStatistics() ? 1 : 0);
        parameters.put("lab39c20", test.isBilling() ? 1 : 0);
        parameters.put("lab39c21", test.getStatisticalTitle());
        parameters.put("lab39c22", test.getMultiplyBy());
        parameters.put("lab39c23", test.isDeleteProfile() ? 1 : 0);
        parameters.put("lab39c24", test.isShowEntry() ? 1 : 0);
        parameters.put("lab39c25", test.getPrintOnReport());
        parameters.put("lab39c26", test.isShowInQuery() ? 1 : 0);
        parameters.put("lab39c27", test.isConfidential() ? 1 : 0);
        parameters.put("lab39c28", test.isResultRequest() ? 1 : 0);
        parameters.put("lab39c29", test.isPrintGraph() ? 1 : 0);
        parameters.put("lab39c30", 0);
        parameters.put("lab39c31", test.getProcessingBy());
        parameters.put("lab39c32", test.getGroupTitle());
        parameters.put("lab39c33", test.getFixedComment());
        parameters.put("lab39c34", test.getPrintComment());
        parameters.put("lab39c35", test.getGeneralInformation());
        parameters.put("lab39c36", timestamp);
        parameters.put("lab39c37", test.getTestType());
        parameters.put("lab39c38", test.isDependentExam() ? 1 : 0);
        parameters.put("lab39c39", test.isExcludeAnalytes() ? 1 : 0);
        parameters.put("lab39c40", test.isStatisticsProcessed() ? 1 : 0);
        parameters.put("lab39c41", test.getMaxPrintDays());
        parameters.put("lab04c1", test.getUser().getId());
        parameters.put("lab07c1", 1);
        parameters.put("lab39c42", test.getPrintOrder());
        parameters.put("lab39c43", test.getConversionFactor());
        parameters.put("lab39c47", test.isBasic() ? 1 : 0);
        parameters.put("lab39c48", test.getValidResult());
        parameters.put("lab39c50", test.isPreliminaryValidation() ? 1 : 0);
        parameters.put("lab39c51", test.getTrendAlert());
        parameters.put("lab39c52", test.isInformedConsent() ? 1 : 0);
        parameters.put("lab39c53", test.isLicuota() ? 1 : 0);
        parameters.put("lab39c56", test.getTemperatureTest());
        parameters.put("lab39c57", test.isExcludeHoliday() ? 1 : 0);
        parameters.put("lab39c58", test.getNameEnglish());
        parameters.put("lab39c59", test.getFixedCommentEn());
        parameters.put("lab39c60", test.getPrintCommentEn());
        parameters.put("lab39c61", test.getGeneralInformationEn());        
        parameters.put("lab39c62", test.getCpt());
        parameters.put("lab39c63", test.getCommentResult());
        parameters.put("lab39c64", test.getDeployPackages());

        Number key = insert.executeAndReturnKey(parameters);
        test.setId(key.intValue());
        test.setLastTransaction(timestamp);

        insertRequirement(test);
        insertConcurrences(test);

        return test;
    }

    /**
     * Obtener información de una prueba por un campo especifico.
     *
     * @param id ID de la prueba a ser consultada.
     * @param code Codigo de la prueba a ser consultada.
     * @param name Nombre de la prueba a ser consultada.
     * @param abbr Abreviatura de la prueba a ser consultada.
     *
     * @return Instancia con los datos del prueba.
     * @throws Exception Error en la base de datos.
     */
    default Test get(Integer id, String code, String name, String abbr) throws Exception
    {
        try
        {

            String query = "SELECT lab39c1, lab39c2, lab39c3, lab39c4, lab39c5, lab39c7,"
                    + " lab39c8, lab39c9, lab39c10, lab39c11, lab39c12, lab39c13, lab39c14,"
                    + " lab39c15, lab39c16, lab39c17, lab39c18, lab39c19, lab39c20, lab39c21, "
                    + " lab39c22, lab39c23, lab39c24, lab39c25, lab39c26, lab39c27, lab39c28, "
                    + " lab39c29, lab39c30, lab39c31, lab39c32, lab39c33, lab39c34, lab39c35, lab39c36, lab39c58, "
                    + " lab39c59, lab39c60, lab39c61, lab39c62, lab39c63, lab39c64, "
                    + " lab39.lab04c1, lab04c2, lab04c3, lab04c4, lab39.lab07c1,  lab39.lab64c1,"
                    + " lab39.lab45c1, lab80.lab80c1, lab80.lab80c2, lab80.lab80c3, lab80.lab80c4,"
                    + " lab80.lab80c5, lab39c37, lab39c38, lab39c39, lab39c40, lab39c41, lab39c42, "
                    + " lab39c43, lab39c44, lab39c45, lab39c46, lab39c47, lab39c48, lab39c50, "
                    + " lab39c51, lab39c52, lab39c53, lab39c56, lab39c57 "
                    + ",lab39.lab24c1, lab24.lab24c2, lab24.lab24c9, lab24.lab24c4, lab24.lab24c10  "
                    + ",lab56.lab56c1, lab56.lab56c2, lab56.lab56c3  "
                    + ",lab43.lab43c1, lab43.lab43c2, lab43.lab43c3, lab43.lab43c4, lab43.lab43c5  "
                    + ",lab44.lab44c1, lab44c2, lab64.lab64c3, lab45.lab45c2 "
                    + ",labLevel.lab80c4 AS lablevelc4, labLevel.lab80c5 AS lablevelc5 "
                    + "FROM lab39 "
                    + "LEFT JOIN lab24 ON lab24.lab24c1 = lab39.lab24c1 "
                    + "LEFT JOIN lab56 ON lab24.lab56c1 = lab56.lab56c1 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab39.lab04c1 "
                    + "LEFT JOIN lab64 ON lab64.lab64c1 = lab39.lab64c1 "
                    + "LEFT JOIN lab45 ON lab45.lab45c1 = lab39.lab45c1 "
                    + "LEFT JOIN lab80 ON lab80.lab80c1 = lab39.lab39c6 "
                    + "LEFT JOIN lab80 AS labLevel ON labLevel.lab80c1 = lab39.lab39c5 "
                    + "LEFT JOIN lab43 ON lab43.lab43c1 = lab39.lab43c1 "
                    + "LEFT JOIN lab44 ON lab44.lab44c1 = lab39.lab44c1 ";
            /*Where*/
            if (id != null)
            {
                query = query + "WHERE lab39c1 = ? ";
            }
            if (code != null)
            {
                query = query + "WHERE UPPER(lab39c2) = ? ";
            }
            if (abbr != null)
            {
                query = query + "WHERE UPPER(lab39c3) = ? ";
            }
            if (name != null)
            {
                query = query + "WHERE UPPER(lab39c4) = ? ";
            }
            /*Order By, Group By y demas complementos de la consulta*/
            query = query + "";

            Object object = null;
            if (id != null)
            {
                object = id;
            }
            if (code != null)
            {
                object = code.toUpperCase();
            }
            if (name != null)
            {
                object = name.toUpperCase();
            }
            if (abbr != null)
            {
                object = abbr.toUpperCase();
            }

            return getConnection().queryForObject(query,
                    new Object[]
                    {
                        object
                    }, (ResultSet rs, int i)
                    ->
            {
                Test test = new Test();
                test.setId(rs.getInt("lab39c1"));
                test.setCode(rs.getString("lab39c2"));
                test.setAbbr(rs.getString("lab39c3"));
                test.setName(rs.getString("lab39c4"));
                test.setNameEnglish(rs.getString("lab39c58"));
                test.setMinAge(rs.getInt("lab39c7"));
                test.setMaxAge(rs.getInt("lab39c8"));
                test.setUnitAge(rs.getShort("lab39c9"));
                test.setFormula(rs.getString("lab39c10"));
                test.setResultType(rs.getShort("lab39c11"));
                test.setDecimal(rs.getShort("lab39c12"));
                test.setVolume(rs.getString("lab39c13"));
                test.setAutomaticResult(rs.getString("lab39c14"));
                test.setMaxDays(rs.getInt("lab39c15"));
                test.setDeliveryDays(rs.getInt("lab39c16"));
                test.setProcessingDays(rs.getString("lab39c17"));
                test.setSelfValidation(rs.getInt("lab39c18"));
                test.setStatistics(rs.getInt("lab39c19") == 1);
                test.setBilling(rs.getInt("lab39c20") == 1);
                test.setStatisticalTitle(rs.getString("lab39c21"));
                test.setMultiplyBy(rs.getInt("lab39c22"));
                test.setDeleteProfile(rs.getInt("lab39c23") == 1);
                test.setShowEntry(rs.getInt("lab39c24") == 1);
                test.setPrintOnReport(rs.getShort("lab39c25"));
                test.setShowInQuery(rs.getInt("lab39c26") == 1);
                test.setConfidential(rs.getInt("lab39c27") == 1);
                test.setResultRequest(rs.getInt("lab39c28") == 1);
                test.setPrintGraph(rs.getInt("lab39c29") == 1);
//                    test.setSpecialStorage(rs.getInt("lab39c30") == 1);
                test.setProcessingBy(rs.getShort("lab39c31"));
                test.setGroupTitle(rs.getString("lab39c32"));
                test.setFixedComment(rs.getString("lab39c33"));
                test.setPrintComment(rs.getString("lab39c34"));
                test.setGeneralInformation(rs.getString("lab39c35"));
                test.setTestType(rs.getShort("lab39c37"));
                test.setDependentExam(rs.getInt("lab39c38") == 1);
                test.setExcludeAnalytes(rs.getInt("lab39c39") == 1);
                test.setStatisticsProcessed(rs.getInt("lab39c40") == 1);
                test.setMaxPrintDays(rs.getInt("lab39c41"));
                test.setTemperatureTest(rs.getInt("lab39c56"));
                test.setExcludeHoliday(rs.getInt("lab39c57") == 1);
                /*Area*/
                test.getArea().setId(rs.getInt("lab43c1"));
                test.getArea().setOrdering(rs.getShort("lab43c2"));
                test.getArea().setAbbreviation(rs.getString("lab43c3"));
                test.getArea().setName(rs.getString("lab43c4"));
                test.getArea().setColor(rs.getString("lab43c5"));
                /*Nivel de complejidad*/
                if (rs.getString("lab39c5") != null)
                {
                    test.getLevel().setId(rs.getInt("lab39c5"));
                    test.getLevel().setEsCo(rs.getString("lablevelc4"));
                    test.getLevel().setEnUsa(rs.getString("lablevelc5"));
                }
                /*Muestra*/
                if (rs.getString("lab24c1") != null)
                {
                    test.getSample().setId(rs.getInt("lab24c1"));
                    test.getSample().setName(rs.getString("lab24c2"));
                    test.getSample().setCodesample(rs.getString("lab24c9"));
                    test.getSample().setCanstiker(rs.getInt("lab24c4"));
                    test.getSample().setLaboratorytype(rs.getString("lab24c10"));
                }
                /*Recipiente*/
                if (rs.getString("Lab56C1") != null)
                {
                    Container container = new Container();
                    container.setId(rs.getInt("Lab56C1"));
                    container.setName(rs.getString("Lab56C2"));
                    if (rs.getBytes("Lab56C3") != null)
                    {
                        container.setImage(Base64.getEncoder().encodeToString(rs.getBytes("Lab56C3")));
                    }
                    test.getSample().setContainer(container);
                }
                /*Entrevista*/
                if (rs.getString("lab44c1") != null && rs.getInt("lab44c1") != 0)
                {
                    test.setInterview(new Interview());
                    test.getInterview().setId(rs.getInt("lab44c1"));
                    test.getInterview().setName(rs.getString("lab44c2"));
                }
                /*Genero*/
                if (rs.getString("lab80c1") != null)
                {
                    test.getGender().setId(rs.getInt("lab80c1"));
                    test.getGender().setIdParent(rs.getInt("lab80c2"));
                    test.getGender().setCode(rs.getString("lab80c3"));
                    test.getGender().setEsCo(rs.getString("lab80c4"));
                    test.getGender().setEnUsa(rs.getString("lab80c5"));
                }
                /*Tecnica*/
                if (rs.getString("lab64c1") != null)
                {
                    test.getTechnique().setId(rs.getInt("lab64c1"));
                    test.getTechnique().setName(rs.getString("lab64c3"));
                }
                /*Unidad*/
                if (rs.getString("lab45c1") != null)
                {
                    test.getUnit().setId(rs.getInt("lab45c1"));
                    test.getUnit().setName(rs.getString("lab45c2"));
                }
                /*Usuario*/
                test.getUser().setId(rs.getInt("lab04c1"));
                test.getUser().setName(rs.getString("lab04c2"));
                test.getUser().setLastName(rs.getString("lab04c3"));
                test.getUser().setUserName(rs.getString("lab04c4"));

                test.setLastTransaction(rs.getTimestamp("lab39c36"));
                test.setState(rs.getInt("lab07c1") == 1);
                test.setPrintOrder(rs.getInt("lab39c42"));
                test.setConversionFactor(rs.getFloat("lab39c43"));

                test.setDeltacheckDays(rs.getString("lab39c44") == null ? null : rs.getInt("lab39c44"));
                test.setDeltacheckMin(rs.getString("lab39c45") == null ? null : rs.getFloat("lab39c45"));
                test.setDeltacheckMax(rs.getString("lab39c46") == null ? null : rs.getFloat("lab39c46"));

                //test.setBasic("1".equals(rs.getString("lab39c47")));
                test.setBasic(rs.getString("lab39c47") != null ? rs.getString("lab39c47").equals("1") : false);
                test.setValidResult(rs.getLong("lab39c48"));
                test.setPreliminaryValidation(rs.getInt("lab39c50") == 1);
                test.setTrendAlert(rs.getInt("lab39c51"));
                test.setInformedConsent(rs.getInt("lab39c52") == 1);
                test.setLicuota(rs.getInt("lab39c53") == 1);

                test.setFixedCommentEn(rs.getString("lab39c59"));
                test.setPrintCommentEn(rs.getString("lab39c60"));
                test.setGeneralInformationEn(rs.getString("lab39c61"));
                test.setCommentResult(rs.getString("lab39c63"));
                test.setCpt(rs.getString("lab39c62"));
                test.setDeployPackages(rs.getInt("lab39c64"));

                readRequirement(test);
                readConcurrences(test);

                return test;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Obtener información basica de una prueba por id para envio externo.
     *
     * @param id ID de la prueba a ser consultada.
     *
     *
     * @return Instancia con los datos del prueba.
     * @throws Exception Error en la base de datos.
     */
    default TestForExternal getTestForExternal(Long id) throws Exception
    {
        try
        {
            String query = "SELECT lab39c1,  lab39c4 , "
                    + " lab39.lab24c1 AS lab24c1 , lab24.lab24c2 AS lab24c2 "
                    + " FROM lab39 "
                    + " LEFT JOIN lab24 ON lab24.lab24c1 = lab39.lab24c1 "
                    + " WHERE lab39c1 = ? ";
            /*Where*/
            return getConnection().queryForObject(query,
                    new Object[]
                    {
                        id
                    }, (ResultSet rs, int i)
                    ->
            {
                TestForExternal test = new TestForExternal();
                test.setTestsIdField(rs.getInt("lab39c1"));
                test.setTestsField(rs.getString("lab39c4"));
                /*Muestra*/
                if (rs.getString("lab24c1") != null)
                {
                    test.setSampleIDField(rs.getInt("lab24c1"));
                    test.setSampleField(rs.getString("lab24c2"));
                }
                return test;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Actualiza la información de una prueba en la base de datos.
     *
     * @param test Instancia con los datos de la prueba.
     *
     * @return Objeto de la prueba modificada.
     * @throws Exception Error en la base de datos.
     */
    default Test update(Test test) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        getConnection().update("UPDATE lab39 SET lab39c2 = ?, "
                + "lab39c3 = ?, "
                + "lab39c4 = ?, "
                + "lab39c5 = ?, "
                + "lab39c6 = ?, "
                + "lab39c7 = ?, "
                + "lab39c8 = ?, "
                + "lab39c9 = ?, "
                + "lab39c10 = ?, "
                + "lab39c11 = ?, "
                + "lab39c12 = ?, "
                + "lab39c13 = ?, "
                + "lab39c14 = ?, "
                + "lab39c15 = ?, "
                + "lab39c16 = ?, "
                + "lab39c18 = ?, "
                + "lab39c19 = ?, "
                + "lab39c20 = ?, "
                + "lab39c21 = ?, "
                + "lab39c22 = ?, "
                + "lab39c23 = ?, "
                + "lab39c24 = ?, "
                + "lab39c25 = ?, "
                + "lab39c26 = ?, "
                + "lab39c27 = ?, "
                + "lab39c28 = ?, "
                + "lab39c29 = ?, "
                + "lab39c31 = ?, "
                + "lab39c32 = ?, "
                + "lab39c33 = ?, "
                + "lab39c34 = ?, "
                + "lab39c35 = ?, "
                + "lab39c36 = ?, "
                + "lab43c1 = ?, "
                + "lab24c1 = ?, "
                + "lab44c1 = ?, "
                + "lab64c1 = ?, "
                + "lab45c1 = ?, "
                + "lab04c1 = ?, "
                + "lab07c1 = ?, "
                + "lab39c17 = ?, "
                + "lab39c37 = ?, "
                + "lab39c38 = ?, "
                + "lab39c39 = ?, "
                + "lab39c40 = ?, "
                + "lab39c41 = ?, "
                + "lab39c43 = ?, "
                + "lab39c47 = ?, "
                + "lab39c48 = ?, "
                + "lab39c50 = ?, "
                + "lab39c51 = ?, "
                + "lab39c52 = ?, "
                + "lab39c53 = ?, "
                + "lab39c56 = ?, "
                + "lab39c57 = ?, "
                + "lab39c58 = ?, "
                + "lab39c59 = ?, "
                + "lab39c60 = ?, "
                + "lab39c61 = ?, "
                + "lab39c62 = ?, "
                + "lab39c63 = ?, "
                + "lab39c64 = ? "
                + "WHERE lab39c1 = ?",
                test.getCode(),
                test.getAbbr(),
                test.getName(),
                test.getLevel().getId(),
                test.getGender().getId(),
                test.getMinAge(),
                test.getMaxAge(),
                test.getUnitAge(),
                test.getFormula(),
                test.getResultType(),
                test.getDecimal(),
                test.getVolume(),
                test.getAutomaticResult(),
                test.getMaxDays(),
                test.getDeliveryDays(),
                test.getSelfValidation(),
                test.isStatistics() ? 1 : 0,
                test.isBilling() ? 1 : 0,
                test.getStatisticalTitle(),
                test.getMultiplyBy(),
                test.isDeleteProfile() ? 1 : 0,
                test.isShowEntry() ? 1 : 0,
                test.getPrintOnReport(),
                test.isShowInQuery() ? 1 : 0,
                test.isConfidential() ? 1 : 0,
                test.isResultRequest() ? 1 : 0,
                test.isPrintGraph() ? 1 : 0,
                test.getProcessingBy(),
                test.getGroupTitle(),
                test.getFixedComment(),
                test.getPrintComment(),
                test.getGeneralInformation(),
                timestamp,
                test.getArea().getId(),
                test.getSample().getId(),
                test.getInterview() == null ? null : test.getInterview().getId(),
                test.getTechnique().getId(),
                test.getUnit().getId(),
                test.getUser().getId(),
                test.isState() ? 1 : 0,
                test.getProcessingDays(),
                test.getTestType(),
                test.isDependentExam() ? 1 : 0,
                test.isExcludeAnalytes() ? 1 : 0,
                test.isStatisticsProcessed() ? 1 : 0,
                test.getMaxPrintDays(),
                test.getConversionFactor(),
                test.isBasic() ? 1 : 0,
                test.getValidResult(),
                test.isPreliminaryValidation() ? 1 : 0,
                test.getTrendAlert(),
                test.isInformedConsent() ? 1 : 0,
                test.isLicuota() ? 1 : 0,
                test.getTemperatureTest(),
                test.isExcludeHoliday() ? 1 : 0,
                test.getNameEnglish(),
                test.getFixedCommentEn(),
                test.getPrintCommentEn(),
                test.getGeneralInformationEn(),
                test.getCpt(),
                test.getCommentResult(),
                test.getDeployPackages(),
                test.getId());

        test.setLastTransaction(timestamp);

        insertRequirement(test);
        insertConcurrences(test);

        return test;
    }

    /**
     *
     * Elimina una prueba de la base de datos.
     *
     * @param id ID de la prueba.
     *
     * @throws Exception Error en base de datos.
     */
    default void delete(Integer id) throws Exception
    {

    }

    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getConnection();

    /**
     * Asociar requerimientos a una prueba.
     *
     * @param test Instancia con los datos de la prueba.
     *
     * @throws java.lang.Exception
     */
    default void insertRequirement(Test test) throws Exception
    {
        deleteRequirements(test.getId());
        test.getRequirements().stream().forEach((Requirement requirement) ->
        {
            SimpleJdbcInsert insert = new SimpleJdbcInsert(getConnection())
                    .withTableName("lab71");

            HashMap parameters = new HashMap();
            parameters.put("lab41c1", requirement.getId());
            parameters.put("lab39c1", test.getId());

            insert.execute(parameters);
        });
    }

    /**
     * Obtener requerimientos asociadas a una prueba.
     *
     * @param test Instancia con los datos de la prueba.
     */
    default void readRequirement(Test test)
    {
        try
        {
            test.setRequirements(getConnection().query("SELECT lab41.lab41c1, "
                    + "lab41c2, "
                    + "lab41c3, "
                    + "lab07c1, "
                    + "lab71.lab39c1 "
                    + "FROM lab41 "
                    + "LEFT JOIN lab71 ON lab71.lab41c1 = lab41.lab41c1 "
                    + "AND lab71.lab39c1 = ?"
                    + "",
                    new Object[]
                    {
                        test.getId()
                    }, (ResultSet rs, int i) ->
            {
                Requirement requirement = new Requirement();
                requirement.setId(rs.getInt("lab41c1"));
                requirement.setCode(rs.getString("lab41c2"));
                requirement.setRequirement(rs.getString("lab41c3"));
                requirement.setState(rs.getInt("lab07c1") == 1);
                requirement.setSelected(rs.getString("lab39c1") != null);

                return requirement;
            }));
        } catch (EmptyResultDataAccessException ex)
        {
            test.setRequirements(new ArrayList<>());
        }
    }

    /**
     * Eliminar los requisitos asociadas a una prueba.
     *
     * @param idTest Id de la prueba.
     *
     * @throws java.lang.Exception
     */
    default void deleteRequirements(Integer idTest) throws Exception
    {
        getConnection().execute(" DELETE FROM lab71 WHERE lab39c1 = " + idTest);
    }

    /**
     * Lista las pruebas con concurrencias desde la base de datos.
     *
     * @return Lista de pruebas que tienen concurrencias.
     * @throws java.lang.Exception
     */
    default List<TestBasic> listConcurrences() throws Exception
    {
        try
        {
            return getConnection().query(""
                    + "SELECT DISTINCT(lab39.lab39c1), lab39c2, lab39c3, lab39c4, lab39c36, lab39.lab04c1, lab04c2, lab04c3, lab04c4, lab39.lab07c1, lab39.lab43c1, lab43c3, lab43c4, lab39c42, lab39c43, lab39c31  "
                    + "FROM lab39 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab39.lab04c1 "
                    + "LEFT JOIN lab43 ON lab43.lab43c1 = lab39.lab43c1 "
                    + "INNER JOIN lab46 ON lab46.lab39c1 = lab39.lab39c1", (ResultSet rs, int i) ->
            {
                TestBasic testBasic = new TestBasic();
                testBasic.setId(rs.getInt("lab39c1"));
                testBasic.setCode(rs.getString("lab39c2"));
                testBasic.setAbbr(rs.getString("lab39c3"));
                testBasic.setName(rs.getString("lab39c4"));
                testBasic.setProcessingBy(rs.getShort("lab39c31"));
                /*Area*/
                testBasic.getArea().setId(rs.getInt("lab43c1"));
                testBasic.getArea().setAbbreviation(rs.getString("lab43c3"));
                testBasic.getArea().setName(rs.getString("lab43c4"));
                /*Usuario*/
                testBasic.getUser().setId(rs.getInt("lab04c1"));
                testBasic.getUser().setName(rs.getString("lab04c2"));
                testBasic.getUser().setLastName(rs.getString("lab04c3"));
                testBasic.getUser().setUserName(rs.getString("lab04c4"));

                testBasic.setLastTransaction(rs.getTimestamp("lab39c36"));
                testBasic.setState(rs.getInt("lab07c1") == 1);
                testBasic.setPrintOrder(rs.getInt("lab39c42"));
                testBasic.setConversionFactor(rs.getBigDecimal("lab39c43"));
                return testBasic;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Lista las pruebas con concurrencias desde la base de datos.
     *
     * @param idBranch Id de la sede en la que se va a hacer la consulta.
     *
     * @return Lista de pruebas que tienen concurrencias.
     * @throws java.lang.Exception
     */
    default List<TestByLaboratory> listTestLaboratory(int idBranch) throws Exception
    {
        try
        {
            return getConnection().query(""
                    + "SELECT lab39.lab39c1, lab39c2, lab39c3, lab39c4, lab39c36, lab145.lab04c1, lab04c2, lab04c3, lab04c4, lab39.lab07c1, lab39.lab43c1, lab43c3, lab43c4, lab145.lab05c1, lab145.lab40c1, lab145.lab145c1, lab40c3, lab05c4, lab145c2 "
                    + "FROM lab39 "
                    + "LEFT JOIN lab43 ON lab43.lab43c1 = lab39.lab43c1 "
                    + "LEFT JOIN lab145 ON lab145.lab39c1 = lab39.lab39c1 AND lab145.lab05c1 = ? "
                    + "LEFT JOIN lab40 ON lab40.lab40c1 = lab145.lab40c1 "
                    + "LEFT JOIN lab05 ON lab05.lab05c1 = lab145.lab05c1 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab145.lab04c1 "
                    + "WHERE lab39.lab39c37 != 2 AND lab39.lab07c1 = 1",
                    new Object[]
                    {
                        idBranch
                    }, (ResultSet rs, int i) ->
            {
                /*prueba*/
                TestBasic testBasic = new TestBasic();
                testBasic.setId(rs.getInt("lab39c1"));
                testBasic.setCode(rs.getString("lab39c2"));
                testBasic.setAbbr(rs.getString("lab39c3"));
                testBasic.setName(rs.getString("lab39c4"));
                /*Area*/
                testBasic.getArea().setId(rs.getInt("lab43c1"));
                testBasic.getArea().setAbbreviation(rs.getString("lab43c3"));
                testBasic.getArea().setName(rs.getString("lab43c4"));
                testBasic.setState(rs.getInt("lab07c1") == 1);

                TestByLaboratory testByLaboratory = new TestByLaboratory();
                testByLaboratory.setTest(testBasic);
                testByLaboratory.setGroupType(rs.getShort("lab145c1"));
                testByLaboratory.setIdLaboratory(rs.getInt("lab40c1"));
                testByLaboratory.setIdBranch(rs.getInt("lab05c1"));
                
                /*Usuario*/
                testByLaboratory.getUser().setId(rs.getInt("lab04c1"));
                testByLaboratory.getUser().setName(rs.getString("lab04c2"));
                testByLaboratory.getUser().setLastName(rs.getString("lab04c3"));
                testByLaboratory.getUser().setUserName(rs.getString("lab04c4"));

                testByLaboratory.setLastTransaction(rs.getTimestamp("lab145c2"));

                return testByLaboratory;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Lista las sedes, laboratorios y pruebas asociadas.
     *
     *
     * @param testId
     * @param branchId
     * @param laboratoryId
     * @return Lista las sedes, laboratorios y pruebas asociadas.
     * @throws java.lang.Exception
     */
    default List<TestByLaboratory> TestByBranchByLaboratory(Integer testId, Integer branchId, Integer laboratoryId) throws Exception
    {
        try
        {
            String where = "";
            List params = new ArrayList<>(0);
            if (testId != null && branchId != null && laboratoryId != null)
            {
                where = "WHERE lab39.lab39c1 = ? AND lab05.lab05c1 = ? AND lab40.lab40c1 = ?";
                params.add(testId);
                params.add(branchId);
                params.add(laboratoryId);
            }
            return getConnection().query(""
                    + "SELECT lab39.lab39c1, lab39c2, lab39c3, lab39c4, lab40.lab40c1, lab40c2, lab40c3, lab05.lab05c1, lab05c4, lab145.lab145c1 "
                    + "FROM lab145 "
                    + "INNER JOIN lab39 ON lab145.lab39c1 = lab39.lab39c1  "
                    + "LEFT JOIN lab40 ON lab40.lab40c1 = lab145.lab40c1 "
                    + "LEFT JOIN lab05 ON lab05.lab05c1 = lab145.lab05c1 "
                    + where,
                    (ResultSet rs, int numRow) ->
            {
                /*prueba*/
                TestBasic testBasic = new TestBasic();
                testBasic.setId(rs.getInt("lab39c1"));
                testBasic.setCode(rs.getString("lab39c2"));
                testBasic.setAbbr(rs.getString("lab39c3"));
                testBasic.setName(rs.getString("lab39c4"));

                /*laboratorio*/
                TestByLaboratory testByLaboratory = new TestByLaboratory();
                testByLaboratory.setTest(testBasic);
                testByLaboratory.setIdLaboratory(rs.getInt("lab40c1"));
                testByLaboratory.setCodeLaboratory(rs.getInt("lab40c2"));
                testByLaboratory.setNameLaboratory(rs.getString("lab40c3"));
                testByLaboratory.setIdBranch(rs.getInt("lab05c1"));
                testByLaboratory.setNameBranch(rs.getString("lab05c4"));
                testByLaboratory.setGroupType(rs.getShort("lab145c1"));

                return testByLaboratory;
            }, params.toArray());
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Obtiene los datos de las sedes, laboratorios y examenes asociados
     *
     * @param tests Lista de pruebas con laboratorio, sede y grupo tipo de orden
     *
     * @return Numero de campos registrados.
     * @throws java.lang.Exception
     */
    default int insertTestLaboratory(List<TestByLaboratory> tests) throws Exception
    {
        deleteTestByLaboratory(tests);
        final List<String> quantity = new ArrayList<>(0);
        Timestamp timestamp = new Timestamp(new Date().getTime());
        tests.stream().forEach((TestByLaboratory test) ->
        {
            if (test.getUrgency() != null && test.getUrgency() == 1)
            {
                SimpleJdbcInsert insert = new SimpleJdbcInsert(getConnection())
                        .withTableName("lab145");

                HashMap parameters = new HashMap();
                parameters.put("lab39c1", test.getTest().getId());
                parameters.put("lab05c1", test.getIdBranch());
                parameters.put("lab145c1", 1);
                parameters.put("lab40c1", test.getIdLaboratory());
                parameters.put("lab145c2", timestamp);
                parameters.put("lab04c1", test.getUser().getId());
                insert.execute(parameters);
                quantity.add(null);
            }

            if (test.getRoutine() != null && test.getRoutine() == 1)
            {
                SimpleJdbcInsert insert = new SimpleJdbcInsert(getConnection())
                        .withTableName("lab145");

                HashMap parameters = new HashMap();
                parameters.put("lab39c1", test.getTest().getId());
                parameters.put("lab05c1", test.getIdBranch());
                parameters.put("lab145c1", 2);
                parameters.put("lab40c1", test.getIdLaboratory());
                parameters.put("lab145c2", timestamp);
                parameters.put("lab04c1", test.getUser().getId());
                insert.execute(parameters);
                quantity.add(null);
            }
        });
        return quantity.size();
    }

    default Laboratory getLaboratory(int branch, int test, int groupType) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT lab40.lab40c1, lab40c2, lab40c3, lab40c4, lab40c5, lab40c6, lab40c7, lab40c8, lab40c9, lab40.lab07c1, lab40.lab04c1, lab04c2, lab04c3, lab04c4, lab40c10, lab40c11, lab40c12 "
                    + "FROM lab145 "
                    + "INNER JOIN lab40 ON lab40.lab40c1 = lab145.lab40c1 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab40.lab04c1 "
                    + "WHERE lab145.lab05c1 = ? AND lab145.lab39c1 = ? AND lab145.lab145c1 = ? ";

            return getConnection().queryForObject(query,
                    new Object[]
                    {
                        branch, test, groupType
                    }, (ResultSet rs, int i) ->
            {
                Laboratory bean = new Laboratory();
                bean.setId(rs.getInt("lab40c1"));
                bean.setCode(rs.getInt("lab40c2"));
                bean.setName(rs.getString("lab40c3"));
                bean.setAddress(rs.getString("lab40c4"));
                bean.setPhone(rs.getString("lab40c5"));
                bean.setContact(rs.getString("lab40c6"));
                bean.setType(rs.getShort("lab40c7"));
                bean.setPath(rs.getString("lab40c8"));
                bean.setUrl(rs.getString("lab40c10"));
                bean.setLastTransaction(rs.getTimestamp("lab40c9"));
                bean.setState(rs.getInt("lab07c1") == 1);
                bean.setEntry(rs.getBoolean("lab40c11"));
                bean.setCheck(rs.getBoolean("lab40c12"));
                /*Usuario*/
                bean.getUser().setId(rs.getInt("lab04c1"));
                bean.getUser().setName(rs.getString("lab04c2"));
                bean.getUser().setLastName(rs.getString("lab04c3"));
                bean.getUser().setUserName(rs.getString("lab04c4"));

                return bean;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Lista las concurrencias de una prueba.
     *
     * @param test Prueba.
     */
    default void readConcurrences(Test test)
    {
        Object[] objects = new Object[0];
        String query = "SELECT lab39.lab39c1, "
                + "lab39c2, "
                + "lab39c3, "
                + "lab39c4, "
                + "lab39c6, "
                + "lab39c7, "
                + "lab39c8, "
                + "lab39c9, "
                + "lab46c1, "
                + "lab46c2, "
                + "(select count(*) from lab46 where lab39c1 = lab39.lab39c1) AS quantity "
                + " ,lab39.lab24c1, lab24.lab24c2, lab24.lab24c9, lab24.lab24c4 "
                + "FROM lab39 "
                + "LEFT JOIN lab46 ON lab46.lab46c1 = lab39.lab39c1 "
                + "INNER JOIN lab24 ON lab39.lab24c1 = lab24.lab24c1 "
                + "AND lab46.lab39c1 = ? "
                + "WHERE lab39.lab39c1 != ? AND lab39.lab07c1 = 1 ";

        if (test.getTestType() == 0)
        {
            query = query + "AND lab39.lab39c37 = 0 ";
            objects = new Object[]
            {
                test.getId(), test.getId()
            };
        } else
        {
            if (test.getTestType() == 1)
            {
                query = query + "AND lab39.lab39c37 = 0 AND lab39.lab43c1 = ? ";
                objects = new Object[]
                {
                    test.getId(), test.getId(), test.getArea().getId()
                };
            } else
            {
                if (test.getTestType() == 2)
                {
                    query = query + "AND lab39.lab39c37 = 0 OR lab39.lab39c37 = 1 ";
                    objects = new Object[]
                    {
                        test.getId(), test.getId()
                    };
                }
            }
        }

        try
        {
            test.setConcurrences(getConnection().query(query, objects, (ResultSet rs, int i) ->
            {
                Concurrence concurrence = new Concurrence();
                concurrence.getConcurrence().setId(rs.getInt("lab39c1"));
                concurrence.getConcurrence().setCode(rs.getString("lab39c2"));
                concurrence.getConcurrence().setAbbr(rs.getString("lab39c3"));
                concurrence.getConcurrence().setName(rs.getString("lab39c4"));
                concurrence.setGender(rs.getInt("lab39c6"));
                concurrence.setMinAge(rs.getInt("lab39c7"));
                concurrence.setMaxAge(rs.getInt("lab39c8"));
                concurrence.setUnitAge(rs.getShort("lab39c9"));
                concurrence.setFormula(rs.getInt("lab46c2") == 1);
                concurrence.setSelected(rs.getString("lab46c1") != null);
                concurrence.setQuantity(rs.getInt("quantity"));

                /*Muestra*/
                if (rs.getString("lab24c1") != null)
                {
                    Sample sample = new Sample();
                    sample.setId(rs.getInt("lab24c1"));
                    sample.setName(rs.getString("lab24c2"));
                    sample.setCodesample(rs.getString("lab24c9"));
                    sample.setCanstiker(rs.getInt("lab24c4"));
                    concurrence.setSample(sample);
                }
                return concurrence;
            }));
        } catch (EmptyResultDataAccessException ex)
        {
            test.setConcurrences(new ArrayList<>());
        }
    }

    /**
     * Lista las concurrencias de una prueba.
     *
     * @param testType Tipo de prueba.
     *
     * @return Lista de pruebas que pueden ser concurrencia.
     */
    default List<Concurrence> getConcurrences(int testType)
    {
        String query = "SELECT lab39.lab39c1, "
                + "lab39c2, "
                + "lab39c3, "
                + "lab39c4, "
                + "lab39c6, "
                + "lab39c7, "
                + "lab39c8, "
                + "lab39c9, "
                + "lab46c1, "
                + "lab46c2, "
                + "(select count(*) from lab46 where lab39c1 = lab39.lab39c1) AS quantity "
                + "FROM lab39 "
                + "LEFT JOIN lab46 ON lab46.lab46c1 = lab39.lab39c1 "
                + "AND lab46.lab39c1 = 0 "
                + "WHERE lab39.lab39c1 != 0 AND lab39.lab07c1 = 1 ";

        if (testType == 0 || testType == 1)
        {
            query = query + "AND lab39.lab39c37 = 0 ";
        } else
        {
            if (testType == 2)
            {
                query = query + "AND lab39.lab39c37 = 0 OR lab39.lab39c37 = 1 ";
            }
        }

        try
        {
            return getConnection().query(query, (ResultSet rs, int i) ->
            {
                Concurrence concurrence = new Concurrence();
                concurrence.getConcurrence().setId(rs.getInt("lab39c1"));
                concurrence.getConcurrence().setCode(rs.getString("lab39c2"));
                concurrence.getConcurrence().setAbbr(rs.getString("lab39c3"));
                concurrence.getConcurrence().setName(rs.getString("lab39c4"));
                concurrence.setGender(rs.getInt("lab39c6"));
                concurrence.setMinAge(rs.getInt("lab39c7"));
                concurrence.setMaxAge(rs.getInt("lab39c8"));
                concurrence.setUnitAge(rs.getShort("lab39c9"));
                concurrence.setFormula(rs.getInt("lab46c2") == 1);
                concurrence.setSelected(rs.getString("lab46c1") != null);
                concurrence.setQuantity(rs.getInt("quantity"));
                return concurrence;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Asociar concurrencias a una prueba.
     *
     * @param test Instancia con los datos de la prueba.
     *
     * @throws Exception Error en base de datos.
     */
    default void insertConcurrences(Test test) throws Exception
    {
        deleteConcurrences(test.getId());
        test.getConcurrences().stream().forEach((concurrence) ->
        {
            SimpleJdbcInsert insert = new SimpleJdbcInsert(getConnection())
                    .withTableName("lab46");

            HashMap parameters = new HashMap();
            parameters.put("lab46c1", concurrence.getConcurrence().getId());
            parameters.put("lab39c1", test.getId());
            parameters.put("lab46c2", concurrence.isFormula() ? 1 : 0);

            insert.execute(parameters);
        });
    }

    /**
     * Eliminar concurrencias asociadas a la prueba.
     *
     * @param idTest Id de la prueba.
     *
     * @throws Exception Error en base de datos.
     */
    default void deleteConcurrences(Integer idTest) throws Exception
    {
        getConnection().execute(" DELETE FROM lab46 WHERE lab39c1 = " + idTest);
    }

    /**
     * Eliminar pruebas por laboratorio asociadas a los examenes.
     *
     * @param tests Pruebas por laboratorio.
     * @throws Exception Error en base de datos.
     */
    default void deleteTestByLaboratory(List<TestByLaboratory> tests) throws Exception
    {
        tests.stream().forEach((TestByLaboratory test) ->
        {
            getConnection().execute(" DELETE FROM lab145 WHERE lab39c1 = " + test.getTest().getId() + " AND lab05c1 = " + test.getIdBranch() + " AND lab40c1 = " + test.getIdLaboratory());
        });
    }

    /**
     * Actualiza el orden de impresion de la prueba
     *
     * @param tests lsita de examnes a actualizar
     *
     * @return Cantidad de registros actualizados
     * @throws java.lang.Exception
     */
    default int updatePrintOrder(List<TestBasic> tests) throws Exception
    {
        List<Object[]> parameters = new ArrayList<>(0);
        Timestamp timestamp = new Timestamp(new Date().getTime());
        String query = "UPDATE lab39 SET lab39c42 = ? , lab39c36 = ?  WHERE lab39c1 = ?";
        tests.forEach((test) ->
        {
            parameters.add(new Object[]
            {
                test.getPrintOrder(),
                timestamp,
                test.getId()
            });
        });
        int[] update = getConnection().batchUpdate(query, parameters);
        return update.length;
    }

    /**
     * Actualiza el orden de impresion de la prueba
     *
     * @param tests lsita de examnes a actualizar
     *
     * @return Cantidad de registros actualizados
     * @throws java.lang.Exception
     */
    default int updateTax(List<TestBasic> tests) throws Exception
    {
        List<Object[]> parameters = new ArrayList<>(0);
        Timestamp timestamp = new Timestamp(new Date().getTime());
        String query = "UPDATE lab39 SET lab39c49 = ? , lab39c36 =?  WHERE lab39c1 = ?";
        tests.stream().forEach((test) ->
        {
            parameters.add(new Object[]
            {
                test.getTax(),
                timestamp,
                test.getId()
            }
            );
        });
        return getConnection().batchUpdate(query, parameters).length;
    }

    /**
     * Lista los valores de referencia de una prueba desde la base de datos.
     *
     * @param test Id de la prueba en la que se va a hacer la consulta.
     *
     * @return Lista de pruebas que tienen concurrencias.
     * @throws java.lang.Exception
     */
    default List<ReferenceValue> listReferenceValues(int test) throws Exception
    {
              
        try
        {
            String query = "SELECT lab39.lab39c1, "
                    + "lab39c2, "
                    + "lab39c3, "
                    + "lab39c4, "
                    + "lab48.lab04c1, "
                    + "lab04c2, "
                    + "lab04c3, "
                    + "lab04c4, "
                    + "lab39.lab43c1, "
                    + "lab48.lab48c1, "
                    + "lab48.lab48c2, "
                    + "lab48.lab48c3, "
                    + "lab48.lab48c4, "
                    + "lab48.lab48c5, "
                    + "lab48.lab48c6, "
                    + "lab48.lab48c9, "
                    + "lab48.lab08c1, "
                    + "lab48.lab48c10, "
                    + "lab48.lab48c11, "
                    + "lab48.lab50c1_1, "
                    + "lab48.lab50c1_3, "
                    + "lab48.lab48c12, "
                    + "lab48.lab48c13, "
                    + "lab48.lab48c14, "
                    + "lab48.lab48c15, "
                    + "lab48.lab48c16, "
                    + "panic.lab50c2 AS panic, "
                    + "normal.lab50c2 AS normal, "
                    + "lab08.lab08c2, "
                    + "lab80c1, "
                    + "lab80c2, "
                    + "lab80c3, "
                    + "lab80c4, "
                    + "lab80c5, "
                    + "lab48c17, "
                    + "lab48c18, "
                    + "lab48c19, "
                    + "lab48c20 "
                    + "FROM lab48 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab48.lab04c1 "
                    + "INNER JOIN lab39 ON lab39.lab39c1 = lab48.lab39c1 "
                    + "LEFT JOIN lab50 panic ON panic.lab50c1 = lab48.lab50c1_1 "
                    + "LEFT JOIN lab50 normal ON normal.lab50c1 = lab48.lab50c1_3 "
                    + "LEFT JOIN lab08 ON lab08.lab08c1 = lab48.lab08c1 "
                    + "LEFT JOIN lab80 ON lab80.lab80c1 = lab48.lab48c10 "
                    + "WHERE lab48.lab39c1 = ? AND lab48.lab48c17 = 1 ";

            return getConnection().query(query, (ResultSet rs, int i) ->
            {
                ReferenceValue referenceValue = new ReferenceValue();
                referenceValue.getTest().setId(rs.getInt("lab39c1"));
                referenceValue.getTest().setCode(rs.getString("lab39c2"));
                referenceValue.getTest().setAbbr(rs.getString("lab39c3"));
                referenceValue.getTest().setName(rs.getString("lab39c4"));
                referenceValue.getTest().getArea().setId(rs.getInt("lab43c1"));

                referenceValue.setId(rs.getInt("lab48c1"));
                referenceValue.setUnitAge(rs.getShort("lab48c2"));
                referenceValue.setAgeMin(rs.getString("lab48c3") == null ? null : rs.getInt("lab48c3"));
                referenceValue.setAgeMax(rs.getString("lab48c4") == null ? null : rs.getInt("lab48c4"));
                referenceValue.setPanicMin(rs.getString("lab48c5") == null ? null : rs.getBigDecimal("lab48c5"));
                referenceValue.setPanicMax(rs.getString("lab48c6") == null ? null : rs.getBigDecimal("lab48c6"));
                referenceValue.setNormalMin(rs.getString("lab48c12") == null ? null : rs.getBigDecimal("lab48c12"));
                referenceValue.setNormalMax(rs.getString("lab48c13") == null ? null : rs.getBigDecimal("lab48c13"));
                referenceValue.setReportableMin(rs.getString("lab48c14") == null ? null : rs.getBigDecimal("lab48c14"));
                referenceValue.setReportableMax(rs.getString("lab48c15") == null ? null : rs.getBigDecimal("lab48c15"));
                referenceValue.setComment(rs.getString("lab48c9"));
                referenceValue.setCommentEnglish(rs.getString("lab48c20"));
                referenceValue.setCriticalCh(rs.getInt("lab48c16") == 1);
                referenceValue.setMandatoryNotation(rs.getInt("lab48c16") == 1);
                /*Raza*/
                referenceValue.getRace().setId(rs.getString("lab08c1") == null ? null : rs.getInt("lab08c1"));
                referenceValue.getRace().setName(rs.getString("lab08c2"));
                /*Genero*/
                referenceValue.getGender().setId(rs.getInt("lab80c1"));
                referenceValue.getGender().setIdParent(rs.getInt("lab80c2"));
                referenceValue.getGender().setCode(rs.getString("lab80c3"));
                referenceValue.getGender().setEsCo(rs.getString("lab80c4"));
                referenceValue.getGender().setEnUsa(rs.getString("lab80c5"));
                /*Resultado Literal Panico*/
                referenceValue.getPanic().setId(rs.getString("lab50c1_1") == null ? null : rs.getInt("lab50c1_1"));
                referenceValue.getPanic().setName(rs.getString("panic"));
                /*Resultado Literal Normal*/
                referenceValue.getNormal().setId(rs.getString("lab50c1_3") == null ? null : rs.getInt("lab50c1_3"));
                referenceValue.getNormal().setName(rs.getString("normal"));
                /*Usuario*/
                referenceValue.getUser().setId(rs.getInt("lab04c1"));
                referenceValue.getUser().setName(rs.getString("lab04c2"));
                referenceValue.getUser().setLastName(rs.getString("lab04c3"));
                referenceValue.getUser().setUserName(rs.getString("lab04c4"));
                referenceValue.setLastTransaction(rs.getTimestamp("lab48c11"));

                referenceValue.setState(rs.getInt("lab48c17") == 1);
                referenceValue.setMandatoryNotation(rs.getInt("lab48c18") == 1);
                referenceValue.setAnalizerUserId(rs.getInt("lab48c19"));

                return referenceValue;
            }, test);
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Inserta valores de referencia a una prueba desde la base de datos.
     *
     * @param referenceValue Valor de referencia.
     *
     * @return Numero de campos registrados.
     * @throws java.lang.Exception
     */
    default ReferenceValue insertReferenceValues(ReferenceValue referenceValue) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getConnection())
                .withTableName("lab48")
                .usingGeneratedKeyColumns("lab48c1");

        HashMap parameters = new HashMap();
        parameters.put("lab39c1", referenceValue.getTest().getId());
        parameters.put("lab48c2", referenceValue.getUnitAge());
        parameters.put("lab48c3", referenceValue.getAgeMin());
        parameters.put("lab48c4", referenceValue.getAgeMax());
        parameters.put("lab48c5", referenceValue.getPanicMin());
        parameters.put("lab48c6", referenceValue.getPanicMax());
        parameters.put("lab48c12", referenceValue.getNormalMin());
        parameters.put("lab48c13", referenceValue.getNormalMax());
        parameters.put("lab48c14", referenceValue.getReportableMin());
        parameters.put("lab48c15", referenceValue.getReportableMax());
        parameters.put("lab48c9", referenceValue.getComment());
        parameters.put("lab48c20", referenceValue.getCommentEnglish());
        parameters.put("lab08c1", referenceValue.getRace() == null ? 0 : referenceValue.getRace().getId());
        parameters.put("lab48c10", referenceValue.getGender() == null ? 0 : referenceValue.getGender().getId());
        parameters.put("lab50c1_1", (referenceValue.getPanic() == null || referenceValue.getPanic().getId() == null) ? 0 : referenceValue.getPanic().getId());
        parameters.put("lab50c1_3", (referenceValue.getNormal() == null || referenceValue.getPanic().getId() == null) ? 0 : referenceValue.getNormal().getId());
        parameters.put("lab48c16", referenceValue.isCriticalCh() ? 1 : 0);
        parameters.put("lab04c1", referenceValue.getUser().getId());
        parameters.put("lab48c11", timestamp);
        parameters.put("lab48c17", 1);
        parameters.put("lab48c18", 1);
        parameters.put("lab48c19", referenceValue.getAnalizerUserId());

        Number key = insert.executeAndReturnKey(parameters);
        referenceValue.setId(key.intValue());
        referenceValue.setLastTransaction(timestamp);

        return referenceValue;
    }

    /**
     * Actualiza valores de referencia a una prueba desde la base de datos.
     *
     * @param referenceValue Lista de valores de referencia.
     *
     * @return Numero de campos registrados.
     * @throws java.lang.Exception
     */
    default ReferenceValue updateReferenceValue(ReferenceValue referenceValue) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        getConnection().update("UPDATE lab48 SET lab48c17 = ? WHERE lab48c1 = ?",
                0, referenceValue.getId());

        referenceValue.setLastTransaction(timestamp);
        return insertReferenceValues(referenceValue);
    }

    /**
     * Desactiva valores de referencia asociadas a los examenes.
     *
     * @param id Id Valor de referencia.
     *
     * @throws Exception Error en base de datos.
     */
    default void inactivateReferenceValues(int id) throws Exception
    {
        getConnection().update("UPDATE lab48 SET lab48c17 = ? WHERE lab48c1 = ?",
                0, id);
    }

    /**
     * Actualiza valores de delta check del examen
     *
     * @param test examen con valores de delta check
     *
     * @return Bean actualizada
     * @throws java.lang.Exception
     */
    default TestBasic updateDeltacheck(TestBasic test) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        String query = "UPDATE lab39 SET lab39c44 = ? , lab39c45 =?, lab39c46 = ?, lab39c36 = ?,lab04c1 = ?  WHERE lab39c1 = ?";
        getConnection().update(query, new Object[]
        {
            test.getDeltacheckDays(),
            test.getDeltacheckMin(),
            test.getDeltacheckMax(),
            timestamp,
            test.getUser().getId(),
            test.getId()
        });
        return test;
    }

    default TestBasic informationTestBasic(Integer id) throws Exception
    {
        try
        {
            return getConnection().queryForObject(""
                    + "SELECT   lab39c1"
                    + " , lab39c2"
                    + " , lab39c3"
                    + " , lab39c4"
                    + " , lab39c7"
                    + " , lab39c8"
                    + " , lab39c9"
                    + " , lab39c11"
                    + " , lab39c36"
                    + " , lab39.lab04c1"
                    + " , lab04c2"
                    + " , lab04c3"
                    + " , lab04c4"
                    + " , lab39.lab07c1"
                    + " , lab39.lab43c1"
                    + " , lab43c3"
                    + " , lab43c4"
                    + " , lab39c37"
                    + " , lab39c42"
                    + " , lab39c43"
                    + " , lab39c31"
                    + " , lab39c44"
                    + " , lab39c45"
                    + " , lab39c46"
                    + " , lab39c12"
                    + " , lab39c17"
                    + " , lab39c27 "
                    + " , lab24.lab24c1"
                    + " , lab24c2"
                    + " , lab24.lab24c10"
                    + " , lab45.lab45c1"
                    + " , lab45c2 "
                    + " , lab39.lab64c1 "
                    + " , lab64.lab64c2 "
                    + " , lab64.lab64c3 "
                    + " , lab39.lab39c24 "
                    + " , lab39.lab39c25 "
                    + " , lab39.lab39c29 "
                    + " , lab39.lab39c49 "
                    + " , lab39.lab39c20 "
                    + " , lab80c1 "
                    + " , lab80c2 "
                    + " , lab80c3 "
                    + " , lab80c4 "
                    + " , lab80c5 "
                    + "FROM lab39 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab39.lab04c1 "
                    + "INNER JOIN lab43 ON lab43.lab43c1 = lab39.lab43c1 "
                    + "LEFT JOIN lab24 ON lab24.lab24c1 = lab39.lab24c1 "
                    + "LEFT JOIN lab45 ON lab45.lab45c1 = lab39.lab45c1 "
                    + "LEFT JOIN lab64 ON lab64.lab64c1 = lab39.lab64c1 "
                    + "LEFT JOIN lab80 ON lab80.lab80c1 = lab39.lab39c6 "
                    + "WHERE lab39.lab39c1 = ? ", new Object[]
                    {
                        id
                    },
                    (ResultSet rs, int i) ->
            {
                TestBasic testBasic = new TestBasic();
                testBasic.setId(rs.getInt("lab39c1"));
                testBasic.setCode(rs.getString("lab39c2"));
                testBasic.setAbbr(rs.getString("lab39c3"));
                testBasic.setName(rs.getString("lab39c4"));
                testBasic.setResultType(rs.getShort("lab39c11"));
                testBasic.setTestType(rs.getShort("lab39c37"));
                testBasic.setProcessingBy(rs.getShort("lab39c31"));
                testBasic.setDecimal(rs.getShort("lab39c12"));
                testBasic.setProcessingDays(rs.getString("lab39c17"));
                testBasic.setConfidential(rs.getInt("lab39c27") == 1);
                testBasic.setUnitAge(rs.getShort("lab39c9"));
                testBasic.setMinAge(rs.getInt("lab39c7"));
                testBasic.setMaxAge(rs.getInt("lab39c8"));
                /*Area*/
                testBasic.getArea().setId(rs.getInt("lab43c1"));
                testBasic.getArea().setAbbreviation(rs.getString("lab43c3"));
                testBasic.getArea().setName(rs.getString("lab43c4"));
                /*Usuario*/
                testBasic.getUser().setId(rs.getInt("lab04c1"));
                testBasic.getUser().setName(rs.getString("lab04c2"));
                testBasic.getUser().setLastName(rs.getString("lab04c3"));
                testBasic.getUser().setUserName(rs.getString("lab04c4"));
                /*Genero*/
                testBasic.getGender().setId(rs.getInt("lab80c1"));
                testBasic.getGender().setIdParent(rs.getInt("lab80c2"));
                testBasic.getGender().setCode(rs.getString("lab80c3"));
                testBasic.getGender().setEsCo(rs.getString("lab80c4"));
                testBasic.getGender().setEnUsa(rs.getString("lab80c5"));

                testBasic.setLastTransaction(rs.getTimestamp("lab39c36"));
                testBasic.setState(rs.getInt("lab07c1") == 1);
                testBasic.setPrintOrder(rs.getInt("lab39c42"));
                testBasic.setConversionFactor(rs.getBigDecimal("lab39c43"));

                testBasic.setDeltacheckDays(rs.getString("lab39c44") == null ? null : rs.getInt("lab39c44"));
                testBasic.setDeltacheckMin(rs.getString("lab39c45") == null ? null : rs.getBigDecimal("lab39c45"));
                testBasic.setDeltacheckMax(rs.getString("lab39c46") == null ? null : rs.getBigDecimal("lab39c46"));

                testBasic.getSample().setId(rs.getInt("lab24c1"));
                testBasic.getSample().setName(rs.getString("lab24c2"));
                testBasic.getSample().setLaboratorytype(rs.getString("lab24c10"));
                if (rs.getString("lab45c1") != null)
                {
                    testBasic.getUnit().setId(rs.getInt("lab45c1"));
                    testBasic.getUnit().setName(rs.getString("lab45c2"));
                }
                if (rs.getString("lab64c1") != null)
                {
                    Technique technique = new Technique();
                    technique.setId(rs.getInt("lab64c1"));
                    technique.setCode(rs.getString("lab64c2"));
                    technique.setName(rs.getString("lab64c3"));
                    testBasic.setTechnique(technique);
                }
                testBasic.setViewInOrderEntry(rs.getInt("lab39c24"));
                testBasic.setPrint(rs.getInt("lab39c25"));
                testBasic.setPrintHistoricGraphic(rs.getInt("lab39c29"));
                testBasic.setTax(rs.getBigDecimal("lab39c49"));
                testBasic.setBilling(rs.getBoolean("lab39c20"));

                return testBasic;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Actualiza los dias de procesamiento de examenes
     *
     * @param tests Lista de examenes
     *
     * @return Bean actualizada
     * @throws java.lang.Exception
     */
    default int updateProcessingDays(List<TestBasic> tests) throws Exception
    {
        final List<Object[]> parameters = new ArrayList<>(0);
        String query = "UPDATE lab39 SET lab39c17 = ? WHERE lab39c1 = ?";
        tests.forEach((test) ->
        {
            parameters.add(new Object[]
            {
                test.getProcessingDays(),
                test.getId()
            }
            );
        });
        return getConnection().batchUpdate(query, parameters).length;
    }

    /**
     * Lista las pruebas automaticas de una prueba desde la base de datos.
     *
     * @param idTest Id de la prueba en la que se va a hacer la consulta.
     *
     * @return Lista de pruebas que tienen concurrencias.
     * @throws java.lang.Exception
     */
    default List<AutomaticTest> listAutomaticTest(int idTest) throws Exception
    {
        try
        {
            return getConnection().query(""
                    + "SELECT lab47.lab47c1, lab47.lab47c2, lab47.lab47c3, lab39.lab39c1, lab39.lab39c2, lab39.lab39c3, lab39.lab39c4, at.lab39c1 as atlab39c1, at.lab39c2 as atlab39c2, at.lab39c3 as atlab39c3, at.lab39c4 as atlab39c4, lab80c1, lab80c2, lab80c3, lab80c4, lab80c5 "
                    + "FROM lab47 "
                    + "LEFT JOIN lab39 ON lab39.lab39c1 = lab47.lab39c1_1 "
                    + "LEFT JOIN lab39 at ON at.lab39c1 = lab47.lab39c1_2 "
                    + "LEFT JOIN lab43 ON lab43.lab43c1 = lab39.lab43c1 "
                    + "LEFT JOIN lab80 ON lab80.lab80c1 = lab47.lab47c1 "
                    + "WHERE lab47.lab39c1_1 = ?",
                    new Object[]
                    {
                        idTest
                    }, (ResultSet rs, int i) ->
            {
                AutomaticTest automaticTest = new AutomaticTest();
                automaticTest.setId(rs.getInt("lab39c1"));
                automaticTest.setCode(rs.getString("lab39c2"));
                automaticTest.setAbbr(rs.getString("lab39c3"));
                automaticTest.setName(rs.getString("lab39c4"));
                automaticTest.setResult1(rs.getString("lab47c2"));
                automaticTest.setResult2(rs.getString("lab47c3"));
                /*Prueba Automatica*/
                automaticTest.getAutomaticTest().setId(rs.getInt("atlab39c1"));
                automaticTest.getAutomaticTest().setCode(rs.getString("atlab39c2"));
                automaticTest.getAutomaticTest().setAbbr(rs.getString("atlab39c3"));
                automaticTest.getAutomaticTest().setName(rs.getString("atlab39c4"));
                /*Tipo*/
                automaticTest.getSign().setId(rs.getInt("lab80c1"));
                automaticTest.getSign().setIdParent(rs.getInt("lab80c2"));
                automaticTest.getSign().setCode(rs.getString("lab80c3"));
                automaticTest.getSign().setEsCo(rs.getString("lab80c4"));
                automaticTest.getSign().setEnUsa(rs.getString("lab80c5"));

                return automaticTest;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Inserta pruebas automaticas desde la base de datos.
     *
     * @param tests Lista de pruebas automaticas
     * @param idTest
     *
     * @return Numero de campos registrados.
     * @throws java.lang.Exception
     */
    default int insertAutomaticTest(List<AutomaticTest> tests, int idTest) throws Exception
    {
        int quantity = 0;
        deleteAutomaticTest(idTest);
        for (AutomaticTest test : tests)
        {
            SimpleJdbcInsert insert = new SimpleJdbcInsert(getConnection())
                    .withTableName("lab47");

            HashMap parameters = new HashMap();
            parameters.put("lab39c1_1", test.getId());
            parameters.put("lab47c1", test.getSign().getId());
            parameters.put("lab47c2", test.getResult1());
            parameters.put("lab47c3", test.getResult2());
            parameters.put("lab39c1_2", test.getAutomaticTest().getId());
            insert.execute(parameters);
            quantity++;
        }
        return quantity;
    }

    /**
     * Eliminar pruebas automaticas asociadas a los examenes.
     *
     * @param idTest Id de la prueba.
     *
     * @throws Exception Error en base de datos.
     */
    default void deleteAutomaticTest(int idTest) throws Exception
    {
        getConnection().execute("DELETE FROM lab47 WHERE lab39c1_1 = " + idTest);
    }

    /**
     * Metodo que actualiza algunos campos del exámen
     *
     * @param test informacion a actualizar
     *
     * @throws Exception Error en la base de datos
     */
    default void updateTestFields(Test test) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        getConnection().update("UPDATE lab39 SET lab39c2 = ?, "
                + "lab39c3 = ?, "
                + "lab39c4 = ?, "
                + "lab24c1 = ?, "
                + "lab43c1 = ?, "
                + "lab45c1 = ?, "
                + "lab04c1 = ?, "
                + "lab39c36 = ?, "
                + "lab39c42 = ? "
                + "WHERE lab39c1 = ?",
                test.getCode(),
                test.getAbbr(),
                test.getName(),
                test.getSample().getId(),
                test.getArea().getId(),
                test.getUnit().getId(),
                test.getUser().getId(),
                timestamp,
                test.getPrintOrder(),
                test.getId());
    }

    /**
     * Lista los examenes asignados a items de PyP.
     *
     * @param idDemographic id del demografico item
     *
     * @return PyPDemographic.
     */
    default PypDemographic getPyPTests(int idDemographic)
    {
        PypDemographic pyp = new PypDemographic();
        List<TestBasic> tests;
        String query = "SELECT lab175c1,lab175c2,lab175c3,lab175c4,lab175c5,lab39.lab39c1, lab39c2, lab39c3, lab39c4 "
                + " FROM lab39 "
                + "LEFT JOIN lab175 ON lab175.lab39c1 = lab39.lab39c1  AND lab175.lab175c1 = ? "
                + "WHERE lab39.lab07c1 = 1 ";

        try
        {
            tests = getConnection().query(query, (ResultSet rs, int i) ->
            {
                if (pyp.getId() == null && rs.getString("lab175c1") != null)
                {
                    pyp.setId(rs.getInt("lab175c1"));
                    pyp.setDemographicItemName(rs.getString("lab175c1"));
                    pyp.setMinAge(rs.getInt("lab175c2"));
                    pyp.setMaxAge(rs.getInt("lab175c3"));
                    pyp.setUnit(rs.getShort("lab175c4"));
                    pyp.setGender(rs.getInt("lab175c5"));
                }
                TestBasic test = new TestBasic(rs.getInt("lab39c1"));
                test.setCode(rs.getString("lab39c2"));
                test.setAbbr(rs.getString("lab39c3"));
                test.setName(rs.getString("lab39c4"));
                test.setSelected(rs.getString("lab175c1") != null);

                return test;
            }, idDemographic);
        } catch (EmptyResultDataAccessException ex)
        {
            return pyp;
        }
        pyp.setTests(tests);
        return pyp;
    }

    /**
     * Inserta relacion examen Demografico PyP
     *
     * @param pyp
     *
     * @return registros afectados
     */
    default int insertPyPTest(PypDemographic pyp)
    {
        List<HashMap> batchArray = new ArrayList<>();
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getConnection())
                .withTableName("lab175");
        pyp.getTests().stream().map((test) ->
        {
            HashMap parameters = new HashMap();
            parameters.put("lab175c1", pyp.getId());
            parameters.put("lab175c2", pyp.getMinAge());
            parameters.put("lab175c3", pyp.getMaxAge());
            parameters.put("lab175c4", pyp.getUnit());
            parameters.put("lab175c5", pyp.getGender());
            parameters.put("lab39c1", test.getId());
            return parameters;
        }).forEachOrdered((parameters)
                ->
        {
            batchArray.add(parameters);
        });
        return insert.executeBatch(batchArray.toArray(new HashMap[pyp.getTests().size()])).length;
    }

    /**
     * Elimina examenes relacionados con demografico item
     *
     * @param id id demográfico
     *
     * @return registros eliminados
     */
    default int deletePyPTest(Integer id)
    {
        return getConnection().update("DELETE FROM lab175 WHERE lab175c1 = ?", id);
    }

    /**
     * Lista las pruebas de un servicio desde la base de datos.
     *
     * @param idService Id de la prueba en la que se va a hacer la consulta.
     *
     * @return Lista de pruebas que tienen concurrencias.
     * @throws java.lang.Exception
     */
    default List<TestByService> listTestByService(int idService) throws Exception
    {
        try
        {
            return getConnection().query(""
                    + " SELECT lab171.lab10c1, lab10.lab10c2, lab171c1, lab171c2, lab171c3, lab171c4, lab39.lab39c1, lab39.lab39c2, lab39.lab39c3, lab39.lab39c4, "
                    + " lab171.lab04c1, lab04c2, lab04c3, lab04c4, lab171c5 "
                    + " FROM lab39 "
                    + " LEFT JOIN lab171 ON lab171.lab171c1 = lab39.lab39c1 AND lab171.lab171c3 = 0 AND lab171.lab10c1 = ? "
                    + " LEFT JOIN lab10 ON lab10.lab10c1 = lab171.lab10c1 "
                    + " LEFT JOIN lab04 ON lab04.lab04c1 = lab171.lab04c1  ",
                    new Object[]
                    {
                        idService
                    }, (ResultSet rs, int i) ->
            {
                TestByService testByService = new TestByService();
                testByService.getTest().setId(rs.getInt("lab39c1"));
                testByService.getTest().setCode(rs.getString("lab39c2"));
                testByService.getTest().setAbbr(rs.getString("lab39c3"));
                testByService.getTest().setName(rs.getString("lab39c4"));
                testByService.getTest().setSelected(rs.getString("lab171c1") != null);
                if (rs.getString("lab10c1") != null)
                {
                    testByService.getService().setId(rs.getInt("lab10c1"));
                    testByService.getService().setName(rs.getString("lab10c2"));
                }
                testByService.setExpectedTime(rs.getInt("lab171c2"));
                testByService.setMaximumTime(rs.getInt("lab171c4"));
                
                testByService.setLastTransaction(rs.getTimestamp("lab171c5"));
                /*Usuario*/
                testByService.getUser().setId(rs.getInt("lab04c1"));
                testByService.getUser().setName(rs.getString("lab04c2"));
                testByService.getUser().setLastName(rs.getString("lab04c3"));
                testByService.getUser().setUserName(rs.getString("lab04c4"));
                
                
                return testByService;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Inserta pruebas por servicio desde la base de datos.
     *
     * @param test Objeto a ser registrado
     *
     * @return Numero de campos registrados.
     * @throws java.lang.Exception
     */
    default TestByService insertTestByService(TestByService test) throws Exception
    {
        deleteTestByService(test.getService().getId(), test.getTest().getId());
        if (test.getTest().isSelected())
        {
            Timestamp timestamp = new Timestamp(new Date().getTime());
            SimpleJdbcInsert insert = new SimpleJdbcInsert(getConnection())
                    .withTableName("lab171");

            HashMap parameters = new HashMap();
            parameters.put("lab10c1", test.getService().getId());
            parameters.put("lab171c1", test.getTest().getId());
            parameters.put("lab171c2", test.getExpectedTime());
            parameters.put("lab171c4", test.getMaximumTime());
            parameters.put("lab171c3", 0);
            parameters.put("lab171c5", timestamp);
            parameters.put("lab04c1", test.getUser().getId());
            
            insert.execute(parameters);
        }
        return test;
    }

    /**
     * Eliminar pruebas por servicio.
     *
     * @param idService Id del Servicio.
     * @param idTest Id de la prueba.
     *
     * @throws Exception Error en base de datos.
     */
    default void deleteTestByService(int idService, int idTest) throws Exception
    {
        getConnection().execute("DELETE FROM lab171 WHERE lab171c1 = " + idTest + " AND lab10c1 = " + idService + " AND lab171c3 = 0");
    }

    /**
     * Obtiene los examenes hijos de un perfil o paquete
     *
     * @param id
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.masters.test.Test}, vacia en caso
     * de no encontrarse
     * @throws Exception Error en base de datos
     */
    default List<Test> getChilds(int id) throws Exception
    {
        return getConnection().query(""
                + "SELECT   lab39.lab39c1"
                + " , lab39.lab39c2 "
                + " , lab39.lab39c3 "
                + " , lab39.lab39c4 "
                + " , lab39.lab39c14 "
                + " , lab45.lab45c1 "
                + " , lab45.lab45c2 "
                + " , lab64.lab64c1 "
                + " , lab64.lab64c2 "
                + " , lab64.lab64c3 "
                + " , lab24.lab24c1 "
                + " , lab24.lab24c2 "
                + " , lab24.lab24c9 "
                + " , lab24.lab24c5 "
                + " , lab24.lab24c4 "
                + " , lab56.lab56c1 "
                + " , lab56.lab56c1 "
                + " , lab56.lab56c2 "
                + " , lab56.lab56c3 "
                + " , lab39.lab39c25 "
                + " , lab39.lab39c29 "
                + " , lab39.lab39c37 "
                + " , lab39.lab39c48 "
                + " , lab39.lab39c33 "
                + " , lab39.lab39c34 "
                + " , lab39.lab39c63 "
                + " , lab39.lab07c1 "
                + "FROM     lab46 "
                + "         INNER JOIN lab39 ON lab46.lab46c1 = lab39.lab39c1  and lab39.lab07c1 = 1"
                + "         LEFT JOIN lab45 ON lab39.lab45c1 = lab45.lab45c1 "
                + "         LEFT JOIN lab64 ON lab64.lab64c1 = lab39.lab64c1 "
                + "         LEFT JOIN lab24 ON lab39.lab24c1 = lab24.lab24c1 "
                + "         LEFT JOIN lab56 ON lab24.lab56c1 = lab56.lab56c1 "
                + "WHERE    lab46.lab39c1 = ? ",
                new Object[]
                {
                    id
                }, (ResultSet rs, int i) ->
        {
            Test test = new Test();
            test.setId(rs.getInt("lab39c1"));
            test.setCode(rs.getString("lab39c2"));
            test.setAbbr(rs.getString("lab39c3"));
            test.setName(rs.getString("lab39c4"));
            test.setAutomaticResult(rs.getString("lab39c14"));
            test.setCommentResult(rs.getString("lab39c63"));
            test.setState(rs.getInt("lab07c1") == 1);

            if (rs.getString("lab45c1") != null)
            {
                Unit unit = new Unit();
                unit.setId(rs.getInt("lab45c1"));
                unit.setName(rs.getString("lab45c2"));
                test.setUnit(unit);
            }

            if (rs.getString("lab64c1") != null)
            {
                Technique technique = new Technique();
                technique.setId(rs.getInt("lab64c1"));
                technique.setCode(rs.getString("lab64c2"));
                technique.setName(rs.getString("lab64c3"));
                test.setTechnique(technique);
            }

            if (rs.getString("lab24c1") != null)
            {
                Sample sample = new Sample();
                sample.setId(rs.getInt("lab24c1"));
                sample.setCodesample(rs.getString("lab24c9"));
                sample.setName(rs.getString("lab24c2"));
                sample.setCheck(rs.getInt("lab24c5") == 1);
                sample.setCanstiker(rs.getInt("lab24c4"));
                if (rs.getString("lab56c1") != null)
                {
                    Container container = new Container();
                    container.setId(rs.getInt("lab56c1"));
                    container.setName(rs.getString("lab56c2"));
                    container.setImage(Base64.getEncoder().encodeToString(rs.getBytes("Lab56C3")));
                    sample.setContainer(container);
                }
                test.setSample(sample);
            }
            test.setTestType(rs.getShort("lab39c37"));
            test.setPrintGraph(rs.getInt("lab39c29") == 1);
            test.setPrintOnReport(rs.getShort("lab39c25"));
            test.setValidResult(rs.getString("lab39c48") == null ? null : rs.getLong("lab39c48"));
            test.setFixedComment(rs.getString("lab39c33"));
            test.setPrintComment(rs.getString("lab39c34"));

            return test;
        });
    }

    /**
     * Lista las pruebas que estan asociadas con medios de cultivo.
     *
     * @return Lista de pruebas que tienen concurrencias.
     * @throws java.lang.Exception
     */
    default List<TestBasic> listTestMediaCulture() throws Exception
    {
        try
        {
            return getConnection().query(""
                    + "SELECT lab39.lab39c1, lab39c2, lab39c3, lab39c4, lab39c36, lab39c37, lab39.lab04c1, lab04c2, lab04c3, lab04c4, lab39.lab07c1, lab39.lab43c1, lab43c3, lab43c4, lab164.lab164c1, "
                    + "lab24.lab24c1, lab24.lab24c2, lab24.lab24c10 "
                    + "FROM lab39 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab39.lab04c1 "
                    + "INNER JOIN lab43 ON lab43.lab43c1 = lab39.lab43c1 "
                    + "INNER JOIN lab24 ON lab24.lab24c1 = lab39.lab24c1 "
                    + "LEFT JOIN lab164 ON lab164.lab39c1 = lab39.lab39c1 ", (ResultSet rs, int i) ->
            {
                TestBasic testBasic = new TestBasic();
                testBasic.setId(rs.getInt("lab39c1"));
                testBasic.setCode(rs.getString("lab39c2"));
                testBasic.setAbbr(rs.getString("lab39c3"));
                testBasic.setName(rs.getString("lab39c4"));
                /*Area*/
                testBasic.getArea().setId(rs.getInt("lab43c1"));
                testBasic.getArea().setAbbreviation(rs.getString("lab43c3"));
                testBasic.getArea().setName(rs.getString("lab43c4"));
                /*Muestra*/
                testBasic.getSample().setId(rs.getInt("lab24c1"));
                testBasic.getSample().setName(rs.getString("lab24c2"));
                testBasic.getSample().setLaboratorytype(rs.getString("lab24c10"));
                /*Usuario*/
                testBasic.getUser().setId(rs.getInt("lab04c1"));
                testBasic.getUser().setName(rs.getString("lab04c2"));
                testBasic.getUser().setLastName(rs.getString("lab04c3"));
                testBasic.getUser().setUserName(rs.getString("lab04c4"));

                testBasic.setLastTransaction(rs.getTimestamp("lab39c36"));
                testBasic.setTestType(rs.getShort("lab39c37"));
                testBasic.setState(rs.getInt("lab07c1") == 1);
                testBasic.setSelected(rs.getString("lab164c1") != null);

                return testBasic;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Obtiene los requierimientos de los examens
     *
     * @param tests Lista de examenes separados por comma
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.masters.test.Requirement}
     * @throws Exception Error en base de datos
     */
    default List<Requirement> getRequirements(String tests) throws Exception
    {
        try
        {
            return getConnection().query(""
                    + "SELECT   lab41.lab41c1 "
                    + " , lab41.lab41c2 "
                    + " , lab41.lab41c3 "
                    + " , lab41.lab07c1 "
                    + "FROM     lab71 "
                    + "         inner join lab41 on lab71.lab41c1 = lab41.lab41c1 "
                    + "WHERE    lab71.lab39c1 in (" + tests + ")",
                    (ResultSet rs, int numRow) ->
            {
                Requirement requirement = new Requirement();
                requirement.setId(rs.getInt("lab41c1"));
                requirement.setCode(rs.getString("lab41c2"));
                requirement.setRequirement(rs.getString("lab41c3"));
                requirement.setState(rs.getInt("lab07c1") == 1);
                return requirement;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Obtiene los perfiles con sus examenes
     *
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.masters.test.Profile}
     * @throws Exception Error en base de datos
     */
    default List<Profile> getProfiles() throws Exception
    {
        try
        {
            return getConnection().query(""
                    + "select 	p.lab39c1 profileId "
                    + " , p.lab39c4 profileName "
                    + " , p.lab07c1 profileState "
                    + " , t.lab39c1 testId "
                    + " , t.lab39c4 testName "
                    + " , t.lab07c1 testState "
                    + " , t.lab39c37 testType "
                    + " , p.lab24c1 sample "
                    + "from 	lab46 "
                    + "         inner join lab39 p on p.lab39c1 = lab46.lab39c1 "
                    + "         inner join lab39 t on t.lab39c1 = lab46.lab46c1 "
                    + "where 	p.lab39c37 in (1,2)",
                    (ResultSet rs, int numRow) ->
            {
                Profile profile = new Profile();
                profile.setProfileId(rs.getInt("profileId"));
                profile.setProfileName(rs.getString("profileName"));
                profile.setProfileState(rs.getInt("profileState"));
                profile.setTestId(rs.getInt("testId"));
                profile.setTestName(rs.getString("testName"));
                profile.setTestState(rs.getInt("testState"));
                profile.setTestType(rs.getInt("testType"));
                profile.setIdSample(rs.getInt("sample"));
                return profile;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Obtiene la informacion del examen.
     *
     * @param test id del examen
     * @param type tipo del examen
     * @return objeto con la informacion del examen.
     * @throws java.lang.Exception
     */
    default TestInformation informationTestByid(long test, int type) throws Exception
    {
        try
        {
            String query = "SELECT   lab39c1"
                    + " , lab39c2"
                    + " , lab39c3"
                    + " , lab39c4"
                    + " , lab39c7"
                    + " , lab39c8"
                    + " , lab39c9"
                    + " , lab39c11"
                    + " , lab39c14"
                    + " , lab39c17"
                    + " , lab39c28"
                    + " , lab39c35"
                    + " , lab39c37"
                    + " , lab39.lab43c1"
                    + " , lab43c3"
                    + " , lab43c4"
                    + " , lab24.lab24c1"
                    + " , lab24c2"
                    + " , lab24.lab24c10"
                    + " , lab45.lab45c1"
                    + " , lab45c2 "
                    + " , lab39.lab64c1 "
                    + " , lab64.lab64c2 "
                    + " , lab64.lab64c3 "
                    + " , lab80c1 "
                    + " , lab80c2 "
                    + " , lab80c3 "
                    + " , lab80c4 "
                    + " , lab80c5 "
                    + " , lab24.lab56c1 "
                    + " , lab56c2 "
                    + " , lab56c5 "
                    + "FROM lab39 "
                    + "LEFT JOIN lab43 ON lab43.lab43c1 = lab39.lab43c1 "
                    + "LEFT JOIN lab24 ON lab24.lab24c1 = lab39.lab24c1 "
                    + "LEFT JOIN lab56 ON lab56.lab56c1 = lab24.lab56c1 "
                    + "LEFT JOIN lab45 ON lab45.lab45c1 = lab39.lab45c1 "
                    + "LEFT JOIN lab64 ON lab64.lab64c1 = lab39.lab64c1 "
                    + "LEFT JOIN lab80 ON lab80.lab80c1 = lab39.lab39c6 "
                    + "WHERE lab39c1 = ? AND lab39c37 = ? ";
            return getConnection().queryForObject(query,
                    new Object[]
                    {
                        test, type
                    }, (ResultSet rs, int i) ->
            {
                TestInformation testInformation = new TestInformation();
                testInformation.setId(rs.getInt("lab39c1"));
                testInformation.setCode(rs.getString("lab39c2"));
                testInformation.setAbbr(rs.getString("lab39c3"));
                testInformation.setName(rs.getString("lab39c4"));
                testInformation.setResultType(rs.getInt("lab39c11"));
                testInformation.setAutomaticResult(rs.getString("lab39c14"));
                testInformation.setProcessingDays(rs.getString("lab39c17"));
                testInformation.setResultRequest(rs.getBoolean("lab39c28"));
                testInformation.setGeneralInformation(rs.getString("lab39c35"));
                testInformation.setTestType(rs.getShort("lab39c37"));
                testInformation.setUnitAge(rs.getShort("lab39c9"));
                testInformation.setMinAge(rs.getInt("lab39c7"));
                testInformation.setMaxAge(rs.getInt("lab39c8"));
                /*Area*/
                if (rs.getString("lab43c1") != null)
                {
                    Area area = new Area();
                    area.setId(rs.getInt("lab43c1"));
                    area.setAbbreviation(rs.getString("lab43c3"));
                    area.setName(rs.getString("lab43c4"));
                    testInformation.setArea(area);
                }
                /*Genero*/
                if (rs.getString("lab80c1") != null)
                {
                    Item gender = new Item();
                    gender.setId(rs.getInt("lab80c1"));
                    gender.setIdParent(rs.getInt("lab80c2"));
                    gender.setCode(rs.getString("lab80c3"));
                    gender.setEsCo(rs.getString("lab80c4"));
                    gender.setEnUsa(rs.getString("lab80c5"));
                    testInformation.setGender(gender);
                }
                /*Muestra*/
                if (rs.getString("lab24c1") != null)
                {
                    List<Sample> listSample = new ArrayList<>();
                    Sample sample = new Sample();
                    sample.setId(rs.getInt("lab24c1"));
                    sample.setName(rs.getString("lab24c2"));
                    sample.setLaboratorytype(rs.getString("lab24c10"));
                    /*Recipiente*/
                    if (rs.getString("lab56c1") != null)
                    {
                        Container container = new Container();
                        container.setId(rs.getInt("lab56c1"));
                        container.setName(rs.getString("lab56c2"));
                        container.setPriority(rs.getInt("lab56c5"));
                        sample.setContainer(container);
                    }
                    listSample.add(sample);
                    testInformation.setListSample(listSample);
                }
                /*Unidad*/
                if (rs.getString("lab45c1") != null)
                {
                    Unit unit = new Unit();
                    unit.setId(rs.getInt("lab45c1"));
                    unit.setName(rs.getString("lab45c2"));
                    testInformation.setUnit(unit);
                }
                /*Tecnica*/
                if (rs.getString("lab64c1") != null)
                {
                    Technique technique = new Technique();
                    technique.setId(rs.getInt("lab64c1"));
                    technique.setCode(rs.getString("lab64c2"));
                    technique.setName(rs.getString("lab64c3"));
                    testInformation.setTechnique(technique);
                }
                return testInformation;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Devolver requerimientos asociadas a una prueba.
     *
     * @param test id del examen
     * @return lista de4 requerimientos de una prueba.
     */
    default List<Requirement> requirementByTest(long test)
    {
        try
        {
            Object objeto = test;
            return getConnection().query("SELECT lab41.lab41c1, lab41c2, lab41c3, lab07c1, lab71.lab39c1 FROM lab41 "
                    + "LEFT JOIN lab71 ON lab71.lab41c1 = lab41.lab41c1 "
                    + "WHERE lab71.lab39c1 = ?"
                    + "",
                    new Object[]
                    {
                        objeto
                    }, (ResultSet rs, int i) ->
            {
                Requirement requirement = new Requirement();
                requirement.setId(rs.getInt("lab41c1"));
                requirement.setCode(rs.getString("lab41c2"));
                requirement.setRequirement(rs.getString("lab41c3"));
                requirement.setState(rs.getInt("lab07c1") == 1);
                requirement.setSelected(rs.getString("lab39c1") != null);

                return requirement;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Obtiene la informacion del perfil.
     *
     * @param test id del perfil
     * @param type tipo del examen
     * @return objeto con la informacion del examen.
     * @throws java.lang.Exception
     */
    default TestInformation informationProfileByid(long test, int type) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT   lab39c1"
                    + " , lab39c2"
                    + " , lab39c3"
                    + " , lab39c4"
                    + " , lab39c17"
                    + " , lab39.lab43c1"
                    + " , lab43c3"
                    + " , lab43c4"
                    + " , lab80c1 "
                    + " , lab80c2 "
                    + " , lab80c3 "
                    + " , lab80c4 "
                    + " , lab80c5 "
                    + "FROM lab39 "
                    + "LEFT JOIN lab43 ON lab43.lab43c1 = lab39.lab43c1 "
                    + "LEFT JOIN lab80 ON lab80.lab80c1 = lab39.lab39c6 "
                    + "WHERE lab39c1 = ? AND lab39c37 = ? ";
            return getConnection().queryForObject(query,
                    new Object[]
                    {
                        test, type
                    }, (ResultSet rs, int i) ->
            {
                TestInformation testInformation = new TestInformation();
                testInformation.setId(rs.getInt("lab39c1"));
                testInformation.setCode(rs.getString("lab39c2"));
                testInformation.setAbbr(rs.getString("lab39c3"));
                testInformation.setName(rs.getString("lab39c4"));
                testInformation.setProcessingDays(rs.getString("lab39c17"));
                /*Area*/
                if (rs.getString("lab43c1") != null)
                {
                    Area area = new Area();
                    area.setId(rs.getInt("lab43c1"));
                    area.setAbbreviation(rs.getString("lab43c3"));
                    area.setName(rs.getString("lab43c4"));
                    testInformation.setArea(area);
                }
                /*Genero*/
                if (rs.getString("lab80c1") != null)
                {
                    Item gender = new Item();
                    gender.setId(rs.getInt("lab80c1"));
                    gender.setIdParent(rs.getInt("lab80c2"));
                    gender.setCode(rs.getString("lab80c3"));
                    gender.setEsCo(rs.getString("lab80c4"));
                    gender.setEnUsa(rs.getString("lab80c5"));
                    testInformation.setGender(gender);
                }
                return testInformation;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Obtiene los examenes hijos de un perfil o paquete retornando objeto
     * TestInformation
     *
     * @param test
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.masters.test.Test}, vacia en caso
     * de no encontrarse
     * @throws Exception Error en base de datos
     */
    default List<TestInformation> getChildsByProfile(long test) throws Exception
    {
        return getConnection().query(""
                + "SELECT   lab39.lab39c1"
                + " , lab39.lab39c2 "
                + " , lab39.lab39c3 "
                + " , lab39.lab39c4 "
                + " , lab39.lab39c7 "
                + " , lab39.lab39c8 "
                + " , lab39.lab39c9 "
                + " , lab39.lab39c11 "
                + " , lab39.lab39c14 "
                + " , lab39.lab39c17 "
                + " , lab39.lab39c28 "
                + " , lab39.lab39c35 "
                + " , lab45.lab45c1 "
                + " , lab45.lab45c2 "
                + " , lab64.lab64c1 "
                + " , lab64.lab64c2 "
                + " , lab64.lab64c3 "
                + " , lab24.lab24c1 "
                + " , lab24.lab24c2 "
                + " , lab24.lab24c10 "
                + " , lab56.lab56c1 "
                + " , lab56.lab56c2 "
                + " , lab56.lab56c5 "
                + " , lab39.lab39c37 "
                + " , lab39.lab43c1 "
                + " , lab43.lab43c3 "
                + " , lab43.lab43c4 "
                + " , lab80.lab80c1 "
                + " , lab80.lab80c2 "
                + " , lab80.lab80c3 "
                + " , lab80.lab80c4 "
                + " , lab80.lab80c5 "
                + "FROM     lab46 "
                + "         INNER JOIN lab39 ON lab46.lab46c1 = lab39.lab39c1 "
                + "         LEFT JOIN lab43 ON lab43.lab43c1 = lab39.lab43c1 "
                + "         LEFT JOIN lab45 ON lab39.lab45c1 = lab45.lab45c1 "
                + "         LEFT JOIN lab64 ON lab64.lab64c1 = lab39.lab64c1 "
                + "         LEFT JOIN lab24 ON lab39.lab24c1 = lab24.lab24c1 "
                + "         LEFT JOIN lab56 ON lab24.lab56c1 = lab56.lab56c1 "
                + "         LEFT JOIN lab80 ON lab80.lab80c1 = lab39.lab39c6 "
                + "WHERE    lab46.lab39c1 = ? ",
                new Object[]
                {
                    test
                }, (ResultSet rs, int i) ->
        {
            TestInformation testInformation = new TestInformation();
            testInformation.setId(rs.getInt("lab39c1"));
            testInformation.setCode(rs.getString("lab39c2"));
            testInformation.setAbbr(rs.getString("lab39c3"));
            testInformation.setName(rs.getString("lab39c4"));
            testInformation.setResultType(rs.getInt("lab39c11"));
            testInformation.setAutomaticResult(rs.getString("lab39c14"));
            testInformation.setProcessingDays(rs.getString("lab39c17"));
            testInformation.setResultRequest(rs.getBoolean("lab39c28"));
            testInformation.setGeneralInformation(rs.getString("lab39c35"));
            testInformation.setTestType(rs.getShort("lab39c37"));
            testInformation.setUnitAge(rs.getShort("lab39c9"));
            testInformation.setMinAge(rs.getInt("lab39c7"));
            testInformation.setMaxAge(rs.getInt("lab39c8"));
            /*Area*/
            if (rs.getString("lab43c1") != null)
            {
                Area area = new Area();
                area.setId(rs.getInt("lab43c1"));
                area.setAbbreviation(rs.getString("lab43c3"));
                area.setName(rs.getString("lab43c4"));
                testInformation.setArea(area);
            }
            /*Genero*/
            if (rs.getString("lab80c1") != null)
            {
                Item gender = new Item();
                gender.setId(rs.getInt("lab80c1"));
                gender.setIdParent(rs.getInt("lab80c2"));
                gender.setCode(rs.getString("lab80c3"));
                gender.setEsCo(rs.getString("lab80c4"));
                gender.setEnUsa(rs.getString("lab80c5"));
                testInformation.setGender(gender);
            }
            /*Muestra*/
            if (rs.getString("lab24c1") != null)
            {
                List<Sample> listSample = new ArrayList<>();
                Sample sample = new Sample();
                sample.setId(rs.getInt("lab24c1"));
                sample.setName(rs.getString("lab24c2"));
                sample.setLaboratorytype(rs.getString("lab24c10"));
                /*Recipiente*/
                if (rs.getString("lab56c1") != null)
                {
                    Container container = new Container();
                    container.setId(rs.getInt("lab56c1"));
                    container.setName(rs.getString("lab56c2"));
                    container.setPriority(rs.getInt("lab56c5"));
                    sample.setContainer(container);
                }
                listSample.add(sample);
                testInformation.setListSample(listSample);
            }
            /*Unidad*/
            if (rs.getString("lab45c1") != null)
            {
                Unit unit = new Unit();
                unit.setId(rs.getInt("lab45c1"));
                unit.setName(rs.getString("lab45c2"));
                testInformation.setUnit(unit);
            }
            /*Tecnica*/
            if (rs.getString("lab64c1") != null)
            {
                Technique technique = new Technique();
                technique.setId(rs.getInt("lab64c1"));
                technique.setCode(rs.getString("lab64c2"));
                technique.setName(rs.getString("lab64c3"));
                testInformation.setTechnique(technique);
            }
            return testInformation;
        });
    }

    /**
     * Obtiene los Ids de los hijos asociados a un perfil o paquete
     *
     * @param id Id perfil o paquete
     * @return Lista de Ids de los examenes hijos
     * @throws Exception Error en el servicio
     */
    default List<Integer> getChildrenIds(int id) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab39.lab39c1")
                    .append(" FROM lab46 ")
                    .append("INNER JOIN lab39 ON lab46.lab46c1 = lab39.lab39c1 ")
                    .append("WHERE lab46.lab39c1 = ").append(id);

            return getConnection().query(query.toString(), (ResultSet rs, int i) ->
            {
                return rs.getInt("lab39c1");
            });
        } catch (DataAccessException e)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Obtiene el tipo de examen de un examen por el id de este
     *
     * @param id Id del examen en custion
     * @return Tipo de examen
     * @throws Exception Error en el servicio
     */
    default int getTestTypeByTestId(int id) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab39c37")
                    .append(" FROM lab39 ")
                    .append("WHERE lab39c1 = ").append(id);

            return getConnection().queryForObject(query.toString(), (ResultSet rs, int i) ->
            {
                return rs.getInt("lab39c37");
            });
        } catch (DataAccessException e)
        {
            return -1;
        }
    }
    
    /**
     * Obtiene el tipo de examen de un examen por el id de este
     *
     * @param id Id del examen en custion
     * @param idOrder
     * @return Tipo de examen
     * @throws Exception Error en el servicio
     */
    default int getSampleState(int id, long idOrder) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab57c16")
                    .append(" FROM lab57 ")
                    .append("WHERE lab39c1 = ").append(id).append(" and lab22c1 = ").append(idOrder);

            return getConnection().queryForObject(query.toString(), (ResultSet rs, int i) ->
            {
                return rs.getInt("lab57c16");
            });
        } catch (DataAccessException e)
        {
            return -1;
        }
    }

    /**
     * Obtiene los Ids de los hijos asociados a un perfil o paquete
     *
     * @return Lista de Ids de los examenes hijos
     * @throws Exception Error en el servicio
     */
    default List<TestPrice> getCodeTest() throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab39c1, lab39.lab39c2")
                    .append(" FROM lab39 ");

            return getConnection().query(query.toString(), (ResultSet rs, int i) ->
            {
                TestPrice test = new TestPrice();
                test.setId(rs.getInt("lab39c1"));
                test.setCode(rs.getString("lab39c2"));
                return test;
            });
        } catch (DataAccessException e)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Lista las pruebas asociadas a una sede
     *
     *
     * @param branchId Id de la sede
     * @param testsIds Lista de ids de los examenes
     * @param laboratoriesIds Lista de ids de los laboratorios
     * @param groupType Tipo de grupo
     * @return Lista las pruebas asociadas a una sede.
     * @throws java.lang.Exception
     */
    default List<net.cltech.enterprisent.domain.operation.orders.Test> testsByBranch(Integer branchId, List<Integer> testsIds, List<Integer> laboratoriesIds, int groupType) throws Exception
    {
        try
        {
            StringBuilder where = new StringBuilder();
            List params = new ArrayList<>(0);

            where.append(" WHERE lab145.lab05c1 = ? ");
            params.add(branchId);

            where.append(" AND lab145.lab39c1 IN(").append(testsIds.stream().map(t -> t.toString()).collect(Collectors.joining(","))).append(") ");
            where.append(" AND lab145.lab40c1 IN(").append(laboratoriesIds.stream().map(t -> t.toString()).collect(Collectors.joining(","))).append(") ");
            where.append(" AND lab145.lab145c1 = ").append(groupType);

            return getConnection().query(""
                    + "SELECT lab39.lab39c1, lab39c2, lab39c3, lab39c4, lab39c16, lab39c17, lab39c37, lab40.lab40c1, lab40c2, lab40c3, lab05.lab05c1, lab05c4, lab145.lab145c1, lab39.lab24c1 "
                    + "FROM lab145 "
                    + "INNER JOIN lab39 ON lab145.lab39c1 = lab39.lab39c1  "
                    + "INNER JOIN lab24 ON lab24.lab24c1 = lab39.lab24c1  "
                    + "LEFT JOIN lab40 ON lab40.lab40c1 = lab145.lab40c1 "
                    + "LEFT JOIN lab05 ON lab05.lab05c1 = lab145.lab05c1 "
                    + where.toString(),
                    (ResultSet rs, int numRow) ->
            {
                /*prueba*/
                net.cltech.enterprisent.domain.operation.orders.Test test = new net.cltech.enterprisent.domain.operation.orders.Test();
                test.setId(rs.getInt("lab39c1"));
                test.setCode(rs.getString("lab39c2"));
                test.setAbbr(rs.getString("lab39c3"));
                test.setName(rs.getString("lab39c4"));

                test.setDeliveryDays(rs.getInt("lab39c16"));
                test.setProccessDays(rs.getString("lab39c17"));
                test.setTestType(rs.getShort("lab39c37"));

                Sample sample = new Sample();
                sample.setId(rs.getInt("lab24c1"));
                test.setSample(sample);

                /*laboratorio*/
                Laboratory laboratory = new Laboratory();
                laboratory.setId(rs.getInt("lab40c1"));
                laboratory.setCode(rs.getInt("lab40c2"));
                laboratory.setName(rs.getString("lab40c3"));
                test.setLaboratory(laboratory);
                return test;
            }, params.toArray());
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }
    
    /**
     * Obtiene los examenes hijos de un paquete o de un perfil
     *
     * @param id
     * @param idOrder Ide de la orden
     * @param isPackage Indica si es paquete o perfil
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.masters.test.Test}, vacia en caso
     * de no encontrarse
     * @throws Exception Error en base de datos
     */
    default List<Test> getChildsPackageOrProfile(int id, long idOrder, boolean isPackage) throws Exception
    {
        // Año de la orden
        Integer year = Tools.YearOfOrder(String.valueOf(idOrder));
        // Año actual
        Integer currentYear = DateTools.dateToNumberYear(new Date());
        // Según el año de la orden, este me indicará en que tabla del historicó buscarla
        String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;
        
        String select = ""
                + "SELECT   lab57.lab39c1"
                + " , lab39.lab39c2 "
                + " , lab39.lab39c3 "
                + " , lab39.lab39c4 "
                + " , lab39.lab39c14 "
                + " , lab45.lab45c1 "
                + " , lab45.lab45c2 "
                + " , lab64.lab64c1 "
                + " , lab64.lab64c2 "
                + " , lab64.lab64c3 "
                + " , lab24.lab24c1 "
                + " , lab24.lab24c2 "
                + " , lab24.lab24c9 "
                + " , lab24.lab24c5 "
                + " , lab24.lab24c4 "
                + " , lab56.lab56c1 "
                + " , lab56.lab56c1 "
                + " , lab56.lab56c2 "
                + " , lab56.lab56c3 "
                + " , lab39.lab39c25 "
                + " , lab39.lab39c29 "
                + " , lab39.lab39c37 "
                + " , lab39.lab39c48 "
                + " , lab39.lab39c33 "
                + " , lab39.lab39c34 "
                + " , lab57.lab57c16 ";
        
        String from = "FROM      " + lab57 + " as lab57 "
                + "         INNER JOIN lab39 ON lab57.lab39c1 = lab39.lab39c1 "
                + "         LEFT JOIN lab45 ON lab39.lab45c1 = lab45.lab45c1 "
                + "         LEFT JOIN lab64 ON lab64.lab64c1 = lab39.lab64c1 "
                + "         LEFT JOIN lab24 ON lab39.lab24c1 = lab24.lab24c1 "
                + "         LEFT JOIN lab56 ON lab24.lab56c1 = lab56.lab56c1 ";
        
        String where = "WHERE    lab57.lab22c1 = ? ";
        
        if(isPackage) {
            where += "AND lab57c15 = ? ";
        } else {
           where += "AND lab57c14 = ? "; 
        }
        
        return getConnection().query(select + from + where, 
                new Object[]
                {
                    idOrder, id
                }, (ResultSet rs, int i) ->
        {
            Test test = new Test();
            test.setId(rs.getInt("lab39c1"));
            test.setCode(rs.getString("lab39c2"));
            test.setAbbr(rs.getString("lab39c3"));
            test.setName(rs.getString("lab39c4"));
            test.setAutomaticResult(rs.getString("lab39c14"));
            test.setSampleState(rs.getInt("lab57c16"));
            if (rs.getString("lab45c1") != null)
            {
                Unit unit = new Unit();
                unit.setId(rs.getInt("lab45c1"));
                unit.setName(rs.getString("lab45c2"));
                test.setUnit(unit);
            }

            if (rs.getString("lab64c1") != null)
            {
                Technique technique = new Technique();
                technique.setId(rs.getInt("lab64c1"));
                technique.setCode(rs.getString("lab64c2"));
                technique.setName(rs.getString("lab64c3"));
                test.setTechnique(technique);
            }

            if (rs.getString("lab24c1") != null)
            {
                Sample sample = new Sample();
                sample.setId(rs.getInt("lab24c1"));
                sample.setCodesample(rs.getString("lab24c9"));
                sample.setName(rs.getString("lab24c2"));
                sample.setCheck(rs.getInt("lab24c5") == 1);
                sample.setCanstiker(rs.getInt("lab24c4"));
                if (rs.getString("lab56c1") != null)
                {
                    Container container = new Container();
                    container.setId(rs.getInt("lab56c1"));
                    container.setName(rs.getString("lab56c2"));
                    container.setImage(Base64.getEncoder().encodeToString(rs.getBytes("Lab56C3")));
                    sample.setContainer(container);
                }
                test.setSample(sample);
            }
            test.setTestType(rs.getShort("lab39c37"));
            test.setPrintGraph(rs.getInt("lab39c29") == 1);
            test.setPrintOnReport(rs.getShort("lab39c25"));
            test.setValidResult(rs.getString("lab39c48") == null ? null : rs.getLong("lab39c48"));
            test.setFixedComment(rs.getString("lab39c33"));
            test.setPrintComment(rs.getString("lab39c34"));

            return test;
        });
    }
    
    /**
     * Lista las pruebas desde la base de datos.
     *
     * @return Lista de pruebas.
     * @throws Exception Error en la base de datos.
     */
    default List<TestBasic> listBaisc() throws Exception
    {
        try
        {
            return getConnection().query(""
                    + "SELECT   lab39c1"
                    + " , lab39c2"
                    + " , lab39c3"
                    + " , lab39c4 "
                    + " FROM lab39 ",
                    (ResultSet rs, int i) ->
            {
                TestBasic testBasic = new TestBasic();
                testBasic.setId(rs.getInt("lab39c1"));
                testBasic.setCode(rs.getString("lab39c2"));
                testBasic.setAbbr(rs.getString("lab39c3"));
                testBasic.setName(rs.getString("lab39c4"));
                return testBasic;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }
    
     /**
     * Obtener requerimientos asociadas a las preuabs de una orden.
     *
     * @param order
     * @return 
     */
    default List<Requirement> readRequirementbyOrder(long order)
    {
        try
        {
            return getConnection().query("SELECT lab41.lab41c1, "
                    + "lab41c2, "
                    + "lab41c3 "
                    + "FROM lab71 "
                    + "JOIN lab41 ON lab71.lab41c1 = lab41.lab41c1 "
                    + "WHERE lab41.lab07c1 = 1 and lab71.lab39c1 in (select lab39c1 from lab57 where lab22c1 = ?)"
                    + "",
                    new Object[]
                    {
                        order
                    }, (ResultSet rs, int i) ->
            {
                Requirement requirement = new Requirement();
                requirement.setId(rs.getInt("lab41c1"));
                requirement.setCode(rs.getString("lab41c2"));
                requirement.setRequirement(rs.getString("lab41c3"));
                return requirement;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>();
        }
    }
}
