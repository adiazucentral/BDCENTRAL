package net.cltech.enterprisent.dao.interfaces.operation.results;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import net.cltech.enterprisent.dao.interfaces.common.ToolsDao;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.demographic.Demographic;
import net.cltech.enterprisent.domain.operation.common.Common;
import net.cltech.enterprisent.domain.operation.orders.DemographicValue;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.Test;
import net.cltech.enterprisent.domain.operation.orders.Worklist;
import static net.cltech.enterprisent.tools.Constants.ISOLATION_READ_UNCOMMITTED;
import net.cltech.enterprisent.tools.DateTools;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.tools.log.stadistics.StadisticsLog;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información de las
 * hojas de trabajo.
 *
 * @version 1.0.0
 * @author eacuna
 * @since 09/10/2017
 * @see Creación
 */
public interface WorklistDao
{

    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getConnection();

    /**
     * Obtiene el dao de tools
     *
     * @return Instancia de toolsDao
     */
    public ToolsDao getToolsDao();

    /**
     * Lista las ordenes pertenecientes a un grupo de hoja de trabajo.
     *
     * @param group numero del grupo
     * @param worksheet id de la hoja de trabajo
     * @param demographics Lista de demograficos.
     * @param yearsQuery Años de consulta (historicos)
     *
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    default List<Order> list(int group, int worksheet, final List<Demographic> demographics, int yearsQuery) throws Exception
    {
        try
        {
            List<Order> listOrders = new LinkedList<>();
            int currentYear = DateTools.dateToNumberYear(new Date());
            List<Integer> years = Tools.listOfConsecutiveYears(String.valueOf(currentYear - yearsQuery), String.valueOf(currentYear));
            String lab22;
            String lab57;
            for (Integer year : years)
            {
                lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
                lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;
                boolean tableExists = getToolsDao().tableExists(getConnection(), lab22);
                tableExists = tableExists ? getToolsDao().tableExists(getConnection(), lab57) : tableExists;
                if (tableExists)
                {
                    String query = "" + ISOLATION_READ_UNCOMMITTED
                            + "SELECT DISTINCT lab22.lab22c1, lab22c2, lab22c3, lab22c4, lab22c5, lab22.lab07c1, lab22c7, "
                            + "lab103.lab103c1, lab103c2, lab103c3, lab103c4, lab103.lab07c1 AS lab103lab07c1,  "
                            + "lab21.lab21c1, lab21c2, lab21c3, lab21c4, lab21c5, lab21c6, lab21lab80.lab80c1 AS lab21lab80lab80c1, lab21lab80.lab80c2 AS lab21lab80lab80c2, lab21lab80.lab80c3 AS lab21lab80lab80c3, lab21lab80.lab80c4 AS lab21lab80lab80c4, lab21lab80.lab80c5 AS lab21lab80lab80c5, lab21c7, lab21c8, lab21c9, lab21c10, lab21c11, lab21c16, lab21c17, lab21lab08.lab08c1 AS lab21lab08lab08c1, lab21lab08.lab08c2 AS lab21lab08lab08c2, lab21lab08.lab08c4 AS lab21lab08lab08c4, lab21lab54.lab54c1 AS lab21lab54lab54c1, lab21lab54.lab54c2 AS lab21lab54lab54c2, lab21lab54.lab54c3 AS lab21lab54lab54c3, "
                            + "lab05.lab05c1, lab05c10, lab05c4, "
                            + "lab10.lab10c1, lab10c2, lab10c7,  "
                            + "lab14.lab14c1, lab14c2, lab14c3,  "
                            + "lab19.lab19c1, lab19c2, lab19c3,  "
                            + "lab22.lab04c1, lab04c2, lab04c3, lab04c4, "
                            + "lab904.lab904c1, lab904c2, lab904c3 ";
                    String from = " "
                            + "FROM  " + lab22 + " AS lab22 "
                            + "INNER JOIN " + lab57 + " AS lab57 ON lab57.lab22c1 = lab22.lab22c1 "
                            + "INNER JOIN lab32 ON lab57.lab22c1 = lab32.lab22c1 AND lab57.lab39c1 = lab32.lab39c1 "
                            + "LEFT JOIN lab103 ON lab103.lab103c1 = lab22.lab103c1  "
                            + "LEFT JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1  "
                            + "LEFT JOIN lab80 lab21lab80 ON lab21lab80.lab80c1 = lab21.lab80c1  "
                            + "LEFT JOIN lab08 lab21lab08 ON lab21lab08.lab08c1 = lab21.lab08c1  "
                            + "LEFT JOIN lab54 lab21lab54 ON lab21lab54.lab54c1 = lab21.lab54c1  "
                            + "LEFT JOIN lab05 ON lab05.lab05c1 = lab22.lab05c1  "
                            + "LEFT JOIN lab10 ON lab10.lab10c1 = lab22.lab10c1  "
                            + "LEFT JOIN lab14 ON lab14.lab14c1 = lab22.lab14c1  "
                            + "LEFT JOIN lab19 ON lab19.lab19c1 = lab22.lab19c1  "
                            + "LEFT JOIN lab04 ON lab04.lab04c1 = lab22.lab04c1  "
                            + "LEFT JOIN lab904 ON lab904.lab904c1 = lab22.lab904c1  ";

                    for (Demographic demographic : demographics)
                    {
                        if (demographic.getOrigin().equals("O"))
                        {
                            if (demographic.isEncoded())
                            {
                                query = query + ", demo" + demographic.getId() + ".lab63c1 as demo" + demographic.getId() + "_id";
                                query = query + ", demo" + demographic.getId() + ".lab63c2 as demo" + demographic.getId() + "_code";
                                query = query + ", demo" + demographic.getId() + ".lab63c3 as demo" + demographic.getId() + "_name";

                                from = from + " LEFT JOIN Lab63 demo" + demographic.getId() + " ON Lab22.lab_demo_" + demographic.getId() + " = demo" + demographic.getId() + ".lab63c1";
                            } else
                            {
                                query = query + ", Lab22.lab_demo_" + demographic.getId();
                            }
                        } else
                        {
                            if (demographic.getOrigin().equals("H"))
                            {
                                if (demographic.isEncoded())
                                {
                                    query = query + ", demo" + demographic.getId() + ".lab63c1 as demo" + demographic.getId() + "_id";
                                    query = query + ", demo" + demographic.getId() + ".lab63c2 as demo" + demographic.getId() + "_code";
                                    query = query + ", demo" + demographic.getId() + ".lab63c3 as demo" + demographic.getId() + "_name";

                                    from = from + " LEFT JOIN Lab63 demo" + demographic.getId() + " ON Lab21.lab_demo_" + demographic.getId() + " = demo" + demographic.getId() + ".lab63c1";
                                } else
                                {
                                    query = query + ", Lab21.lab_demo_" + demographic.getId();
                                }
                            }
                        }
                    }
                    from = from + " WHERE lab22.lab07c1 = 1 AND lab32.lab37c1 = ? AND lab32.lab32c1 = ?  AND (lab22c19 = 0 or lab22c19 is null) ";

                    getConnection().query(query + from,
                            new Object[]
                            {
                                worksheet, group
                            }, (ResultSet rs, int i) ->
                    {
                        Order order = new Order();
                        order.setOrderNumber(rs.getLong("lab22c1"));
                        order.setCreatedDateShort(rs.getInt("lab22c2"));
                        order.setCreatedDate(rs.getTimestamp("lab22c3"));
                        order.setHomebound(rs.getInt("lab22c4") == 1);
                        order.setMiles(rs.getInt("lab22c5"));
                        order.setExternalId(rs.getString("lab22c7"));
                        order.getLastUpdateUser().setId(rs.getInt("lab04c1"));
                        order.getLastUpdateUser().setName(rs.getString("lab04c2"));
                        order.getLastUpdateUser().setLastName(rs.getString("lab04c3"));
                        order.getLastUpdateUser().setUserName(rs.getString("lab04c4"));
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
                        //EMPRESA
                        order.getAccount().setId(rs.getInt("lab14c1"));
                        order.getAccount().setNit(rs.getString("lab14c2"));
                        order.getAccount().setName(rs.getString("lab14c3"));
                        //MEDICO
                        order.getPhysician().setId(rs.getInt("lab19c1"));
                        order.getPhysician().setName(rs.getString("lab19c2"));
                        order.getPhysician().setLastName(rs.getString("lab19c3"));
                        //TARIFA
                        order.getRate().setId(rs.getInt("lab904c1"));
                        order.getRate().setCode(rs.getString("lab904c2"));
                        order.getRate().setName(rs.getString("lab904c3"));

                        DemographicValue demoValue = null;
                        for (Demographic demographic : demographics)
                        {
                            demoValue = new DemographicValue();
                            demoValue.setIdDemographic(demographic.getId());
                            demoValue.setDemographic(demographic.getName());
                            demoValue.setEncoded(demographic.isEncoded());
                            if (demographic.isEncoded())
                            {
                                if (rs.getString("demo" + demographic.getId() + "_id") != null)
                                {
                                    demoValue.setCodifiedId(rs.getInt("demo" + demographic.getId() + "_id"));
                                    demoValue.setCodifiedCode(rs.getString("demo" + demographic.getId() + "_code"));
                                    demoValue.setCodifiedName(rs.getString("demo" + demographic.getId() + "_name"));
                                }
                            } else
                            {
                                demoValue.setNotCodifiedValue(rs.getString("lab_demo_" + demographic.getId()));
                            }
                            if (demographic.getOrigin().equals("O"))
                            {
                                order.getDemographics().add(demoValue);
                            } else
                            {
                                order.getPatient().getDemographics().add(demoValue);
                            }
                        }

                        listTest(order, group, worksheet);
                        listOrders.add(order);
                        return order;
                    });
                }

            }
            return listOrders;

        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Lista las pruebas de una orden.
     *
     * @param order Objeto de la orden a la que se le quieren consultar los
     * examenes.
     * @param group
     * @param worksheet
     */
    default void listTest(Order order, int group, int worksheet)
    {
        try
        {
            Integer years = Tools.YearOfOrder(String.valueOf(order.getOrderNumber()));
            int currentYear = DateTools.dateToNumberYear(new Date());
            String lab57 = years.equals(currentYear) ? "lab57" : "lab57_" + years;
            boolean tableExists = getToolsDao().tableExists(getConnection(), lab57);
            if (tableExists)
            {
                String query = ISOLATION_READ_UNCOMMITTED + "SELECT lab57.lab22c1, lab57.lab57c8, lab57.lab57c14, lab57.lab57c15, "
                        + "lab39.lab39c1, lab39.lab39c2, lab39.lab39c3, lab39.lab39c4, lab39.lab39c37, lab39.lab39c27, "
                        + "lab43.lab43c1, lab43c3, lab43.lab43c4, "
                        + "lab24.lab24c1, lab24c2, lab24c4, lab24c9, lab24.lab24c10, "
                        + "lab45.lab45c1, lab45.lab45c2, "
                        + "lab24.lab56c1, lab56.lab56c2, "
                        + "panel.lab39c1 AS panellab39c1, panel.lab39c2 AS panellab39c2, panel.lab39c3 AS panellab39c3, panel.lab39c4 AS panellab39c4, panel.lab39c37 AS panellab39c37, "
                        + "pack.lab39c1 AS packlab39c1, pack.lab39c2 AS packlab39c2, pack.lab39c3 AS packlab39c3, pack.lab39c4 AS packlab39c4, pack.lab39c37 AS packlab39c37, "
                        + "lab57.lab40c1, lab40.lab40c2, lab40.lab40c3, "
                        + "lab32.lab37c1, lab32.lab32c1, "
                        + "lab16.lab16c3, lab11.lab11c1 "
                        + "FROM " + lab57 + " AS lab57 "
                        + "INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1 "
                        + "LEFT JOIN lab39 panel ON panel.lab39c1 = lab57.lab57c14 "
                        + "LEFT JOIN lab39 pack ON pack.lab39c1 = lab57.lab57c15 "
                        + "LEFT JOIN lab43 ON lab43.lab43c1 = lab39.lab43c1 "
                        + "LEFT JOIN lab24 ON lab24.lab24c1 = lab39.lab24c1 "
                        + "LEFT JOIN lab45 ON lab45.lab45c1 = lab39.lab45c1 "
                        + "LEFT JOIN lab40 ON lab40.lab40c1 = lab57.lab40c1 "
                        + "LEFT JOIN lab56 ON lab56.lab56c1 = lab24.lab56c1 "
                        + "INNER JOIN lab32 ON lab32.lab22c1 = lab57.lab22c1 AND lab32.lab39c1 = lab57.lab39c1 "
                        + "LEFT JOIN lab11 ON lab11.lab24c1 = lab57.lab24c1 AND lab11.lab22c1 = lab57.lab22c1 "
                        + "LEFT JOIN lab16 ON lab16.lab16c1 = lab11.lab16c1  "
                        + "WHERE lab57.lab22c1 = ? AND lab32.lab37c1 = ? AND lab32.lab32c1 = ?  ";

                order.setTests(getConnection().query(query,
                        new Object[]
                        {
                            order.getOrderNumber(),
                            worksheet,
                            group
                        }, (ResultSet rs, int i) ->
                {
                    Test test = new Test();
                    test.setId(rs.getInt("lab39c1"));
                    test.setCode(rs.getString("lab39c2"));
                    test.setAbbr(rs.getString("lab39c3"));
                    test.setName(rs.getString("lab39c4"));
                    test.getResult().setState(rs.getInt("lab57c8"));
                    test.setTestType(rs.getShort("lab39c37"));
                    test.setConfidential(rs.getInt("lab39c27") == 1);
                    test.setRackStore(rs.getString("lab16c3"));
                    test.setPositionStore(rs.getString("lab11c1"));

                    test.getLaboratory().setId(rs.getInt("lab40c1"));
                    test.getLaboratory().setCode(rs.getInt("lab40c2"));
                    test.getLaboratory().setName(rs.getString("lab40c3"));
                    if (rs.getString("lab37c1") != null)
                    {
                        test.setWorklist(new Worklist());
                        test.getWorklist().setId(rs.getInt("lab37c1"));
                        test.getWorklist().setConsecutive(rs.getInt("lab32c1"));
                    }

                    Test panel = new Test();
                    panel.setId(rs.getString("panellab39c1") == null ? null : rs.getInt("panellab39c1"));
                    panel.setCode(rs.getString("panellab39c2"));
                    panel.setAbbr(rs.getString("panellab39c3"));
                    panel.setName(rs.getString("panellab39c4"));
                    panel.setTestType(rs.getShort("panellab39c37"));
                    test.setPanel(panel);

                    Test pack = new Test();
                    pack.setId(rs.getString("packlab39c1") == null ? null : rs.getInt("packlab39c1"));
                    pack.setCode(rs.getString("packlab39c2"));
                    pack.setAbbr(rs.getString("packlab39c3"));
                    pack.setName(rs.getString("packlab39c4"));
                    pack.setTestType(rs.getShort("packlab39c37"));
                    test.setPack(pack);

                    test.getArea().setId(rs.getString("lab43c1") == null ? null : rs.getInt("lab43c1"));
                    test.getArea().setAbbreviation(rs.getString("lab43c3"));
                    test.getArea().setName(rs.getString("lab43c4"));

                    test.getSample().setId(rs.getString("lab24c1") == null ? null : rs.getInt("lab24c1"));
                    test.getSample().setName(rs.getString("lab24c2"));
                    test.getSample().setCodesample(rs.getString("lab24c9"));
                    test.getSample().setLaboratorytype(rs.getString("lab24c10"));
                    test.getSample().setCanstiker(rs.getInt("lab24c4"));
                    test.getSample().getContainer().setId(rs.getInt("lab56c1"));
                    test.getSample().getContainer().setName(rs.getString("lab56c2"));

                    test.getUnit().setId(rs.getString("lab45c1") == null ? null : rs.getInt("lab45c1"));
                    test.getUnit().setName(rs.getString("lab45c2"));
                    return test;

                }));
            }
        } catch (SQLException | DataAccessException e)
        {
            StadisticsLog.error(e.getMessage());
        }

    }

    /**
     * Registra examenes de un grupo
     *
     * @param orders
     * @param worksheet
     * @param group
     *
     * @return
     */
    default int insertTestGroup(List<Order> orders, int worksheet, int group, int user)
    {

        Timestamp timestamp = new Timestamp(new Date().getTime());
        List<HashMap> mapArray = new ArrayList<>();
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getConnection())
                .withTableName("lab32");

        for (Order order : orders)
        {
            for (Test test : order.getTests())
            {
                if (test.isSelected())
                {
                    HashMap map = new HashMap();
                    map.put("lab22c1", order.getOrderNumber());
                    map.put("lab39c1", test.getId());
                    map.put("lab37c1", worksheet);
                    map.put("lab32c1", group);
                    map.put("lab32c2", timestamp);
                    map.put("lab04c1", user);
                    mapArray.add(map);
                }
            }
        }
        return insert.executeBatch(mapArray.toArray(new HashMap[mapArray.size()])).length;

    }

    /**
     * Elimina los exámenes de una hoja de trabajo
     *
     * @param worksheet id de la hoja del trabajo
     *
     * @return
     */
    default int deleteWorksheetSecuence(int worksheet)
    {
        return getConnection().update("DELETE FROM lab32 WHERE lab37c1 = ?", worksheet);
    }

    /**
     * Obtiene el consecutivo de la hoja de trabajo
     *
     * @param worksheet
     *
     * @return
     */
    default int getConsecutive(int worksheet)
    {
        try
        {
            return getConnection().queryForObject(ISOLATION_READ_UNCOMMITTED + "SELECT MAX(lab32c1) lab32c1 FROM lab32 WHERE lab37c1 = ?",
                    (ResultSet rs, int i) -> rs.getInt("lab32c1"), worksheet);
        } catch (EmptyResultDataAccessException ex)
        {
            return 0;
        }
    }

    /**
     * Lista las secuencias de una hoja de trabajo
     *
     * @param worksheet
     *
     * @return
     */
    default List<Common> listSecuences(int worksheet)
    {
        String query = ISOLATION_READ_UNCOMMITTED + "SELECT DISTINCT lab32c1,lab32c2, "
                + " lab04.lab04c1, lab04c2, lab04c3, lab04c4 "
                + "FROM lab32 "
                + "INNER JOIN lab04 on lab04.lab04c1 = lab32.lab04c1 "
                + "WHERE lab32.lab37c1 = ? ";
        try
        {
            return getConnection().query(query, (ResultSet rs, int i) ->
            {
                Common test = new Common();
                test.setNumber(rs.getInt("lab32c1"));
                test.setDate(rs.getTimestamp("lab32c2"));
                test.setUser(new AuthorizedUser());

                test.getUser().setId(rs.getInt("lab04c1"));
                test.getUser().setName(rs.getString("lab04c2"));
                test.getUser().setLastName(rs.getString("lab04c3"));
                test.getUser().setUserName(rs.getString("lab04c4"));

                return test;
            }, worksheet);
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>();
        }
    }

}
