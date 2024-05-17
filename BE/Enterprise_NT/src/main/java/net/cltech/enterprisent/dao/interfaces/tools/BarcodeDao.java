package net.cltech.enterprisent.dao.interfaces.tools;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import net.cltech.enterprisent.domain.tools.BarcodeDesigner;
import net.cltech.enterprisent.domain.tools.OrderTestPatientHistory;
import net.cltech.enterprisent.domain.tools.PrintLog;
import static net.cltech.enterprisent.tools.Constants.ISOLATION_READ_UNCOMMITTED;
import net.cltech.enterprisent.tools.Log;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información de las
 * Tarifas.
 *
 * @version 1.0.0
 * @author eacuna
 * @since 10/12/2018
 * @see Creación
 */
public interface BarcodeDao
{

    /**
     * Lista barcode desde la base de datos.
     *
     * @return Lista de sitios anatomicos.
     * @throws Exception Error en la base de datos.
     */
    default List<BarcodeDesigner> list() throws Exception
    {
        try
        {
            return getJdbcTemplate().query(""
                    + "SELECT lab105c1,lab105c2, lab105c3,lab105c4,lab07c1, lab105c5, lab105c6 "
                    + "FROM lab105 ", (ResultSet rs, int i) ->
            {
                BarcodeDesigner barcode = new BarcodeDesigner();
                barcode.setId(rs.getInt("lab105c1"));
                barcode.setType(rs.getInt("lab105c2"));
                barcode.setTemplate(rs.getString("lab105c3"));
                barcode.setCommand(rs.getString("lab105c4"));
                barcode.setVersion(rs.getInt("lab105c5"));
                barcode.setActive(rs.getInt("lab07c1") == 1);
                barcode.setOrderType(rs.getString("lab105c6"));
                return barcode;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Barcode predeterminado por tipo.
     *
     * @param type
     * @return Lista de sitios anatomicos.
     * @throws Exception Error en la base de datos.
     */
    default List<BarcodeDesigner> barcodePredetermined(int type) throws Exception
    {
        try
        {
            return getJdbcTemplate().query(""
                    + "SELECT lab105c1,lab105c2, lab105c3,lab105c4,lab07c1, lab105c5, lab105c6 "
                    + "FROM lab105 WHERE lab105c2 = ? AND lab07c1 = 1 ", (ResultSet rs, int i) ->
            {
                BarcodeDesigner barcode = new BarcodeDesigner();
                barcode.setId(rs.getInt("lab105c1"));
                barcode.setType(rs.getInt("lab105c2"));
                barcode.setTemplate(rs.getString("lab105c3"));
                barcode.setCommand(rs.getString("lab105c4"));
                barcode.setVersion(rs.getInt("lab105c5"));
                barcode.setActive(rs.getInt("lab07c1") == 1);
                barcode.setOrderType(rs.getString("lab105c6"));
                return barcode;
            }, type);
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Registra barcode en la base de datos.
     *
     * @param barcode Instancia con los datos .
     *
     * @return Instancia con los datos .
     * @throws Exception Error en la base de datos.
     */
    default BarcodeDesigner create(BarcodeDesigner barcode) throws Exception
    {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab105")
                .usingGeneratedKeyColumns("lab105c1");

        HashMap parameters = new HashMap();
        parameters.put("lab105c2", barcode.getType());
        parameters.put("lab105c3", barcode.getTemplate());
        parameters.put("lab105c4", barcode.getCommand());
        parameters.put("lab105c5", barcode.getVersion());
        parameters.put("lab07c1", barcode.isActive() ? 1 : 0);
        parameters.put("lab105c6", barcode.getOrderType());

        Number key = insert.executeAndReturnKey(parameters);
        barcode.setId(key.intValue());
        barcode.setActive(true);
        return barcode;
    }

    /**
     * Actualiza la información de barcode en la base de datos.
     *
     * @param barcode Instancia con los datos.
     *
     * @return Objeto modificado.
     * @throws Exception Error en la base de datos.
     */
    default BarcodeDesigner update(BarcodeDesigner barcode) throws Exception
    {
        getJdbcTemplate().update("UPDATE lab105 SET lab105c5 = ?, lab105c2 = ?,lab105c3 = ?,lab105c4 = ?, lab07c1 = ?, lab105c6 = ?  "
                + "WHERE lab105c1 = ?",
                barcode.getVersion(), barcode.getType(), barcode.getTemplate(), barcode.getCommand(), barcode.isActive() ? 1 : 0, barcode.getOrderType(), barcode.getId());

        return barcode;
    }
    ///--------------------------actualiza capo de fecha historicos

    public Long lastOrderValidate(OrderTestPatientHistory order) throws Exception;

    public Date lastDateTestValidate(OrderTestPatientHistory order) throws Exception;

    ///-----metods para actualizar la fechasd e historicos  de la base de datos 
    default List<OrderTestPatientHistory> listOrderTestPatien() throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED);
            query.append(" SELECT  lab22.lab22c1 AS lab22c1 ,lab39c1 ,lab21c1 "
                    + " FROM lab57 "
                    + "inner join lab22 on lab22.lab22c1 =lab57.lab22c1  "
                    + "WHERE  "
                    + "lab57c6 is not null  "
                    + "and lab57c7 is null "
                    + "and lab57c16 = 4 "
                    + "union  "
                    + " SELECT  lab22.lab22c1 AS lab22c1 ,lab39c1 ,lab21c1 "
                    + " FROM lab57_2021 "
                    + "inner join lab22 on lab22.lab22c1 =lab57_2021.lab22c1  "
                    + " WHERE  "
                    + "lab57c6 is not null  "
                    + "and lab57c7 is null  "
                    + "and lab57c16 = 4 "
                    + "order by lab22c1 desc ");

            return getJdbcTemplate().query(query.toString(), (ResultSet rs, int i) ->
            {
                OrderTestPatientHistory orderTestPatient = new OrderTestPatientHistory();
                orderTestPatient.setNumberOrderSinDate(rs.getLong("lab22c1"));
                orderTestPatient.setIdTest(rs.getInt("lab39c1"));
                orderTestPatient.setIdPatient(rs.getInt("lab21c1"));
                return orderTestPatient;

            });
        } catch (EmptyResultDataAccessException e)
        {
            return new ArrayList<>(0);
        }

    }

    default Long lastOrderValidate1(OrderTestPatientHistory order) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED);
            query.append("SELECT TOP 1 lab22.lab22c1  AS lab22c1 "
                    + "FROM lab57 "
                    + "inner join lab22 on lab22.lab22c1 =lab57.lab22c1  "
                    + "WHERE  lab22.lab22c1  <  ")
                    .append(order.getNumberOrderSinDate())
                    .append(" and lab39c1 =  ").append(order.getIdTest())
                    .append(" and lab22.lab21c1=  ").append(order.getIdPatient())
                    .append(" and lab57c18 is not null ")
                    .append(" order by lab22.lab22c1 desc  ");
            return getJdbcTemplate().queryForObject(query.toString(), (ResultSet rs, int i)
                    ->
            {
                return rs.getLong("lab22c1");
            });
        } catch (DataAccessException e)
        {
            return 0L;
        }

    }

    default Long lastOrderValidate2(OrderTestPatientHistory order) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED);
            query.append("SELECT TOP 1 lab22.lab22c1  AS lab22c1 "
                    + "FROM lab57_2021 as lab57  "
                    + "inner join lab22 on lab22.lab22c1 =lab57.lab22c1  "
                    + "WHERE  lab22.lab22c1  <  ")
                    .append(order.getNumberOrderSinDate())
                    .append(" and lab39c1 =  ").append(order.getIdTest())
                    .append(" and lab22.lab21c1=  ").append(order.getIdPatient())
                    .append(" and lab57c18 is not null ")
                    .append(" order by lab22.lab22c1 desc  ");
            return getJdbcTemplate().queryForObject(query.toString(), (ResultSet rs, int i)
                    ->
            {
                return rs.getLong("lab22c1");
            });
        } catch (DataAccessException e)
        {
            return 0L;
        }

    }

    default Long lastOrderValidate3(OrderTestPatientHistory order) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED);
            query.append("SELECT TOP 1 lab22.lab22c1  AS lab22c1 "
                    + "FROM lab57_2021 as lab57  "
                    + "inner join lab22_2021 as lab22 on lab22.lab22c1 =lab57.lab22c1  "
                    + "WHERE  lab22.lab22c1  <  ")
                    .append(order.getNumberOrderSinDate())
                    .append(" and lab39c1 =  ").append(order.getIdTest())
                    .append(" and lab22.lab21c1=  ").append(order.getIdPatient())
                    .append(" and lab57c18 is not null ")
                    .append(" order by lab22.lab22c1 desc  ");
            return getJdbcTemplate().queryForObject(query.toString(), (ResultSet rs, int i)
                    ->
            {
                return rs.getLong("lab22c1");
            });
        } catch (DataAccessException e)
        {
            return 0L;
        }

    }

    //metodo para actualizar la tabla lb17 dode existe ultimo resultado y no esxite ultima fecha 
    default void updateHistory(OrderTestPatientHistory order) throws Exception
    {
        String query = " UPDATE lab57 SET Lab57C7 = (select lab57c18 from lab57 where lab22c1= ? and lab57.Lab39C1 = ? ) "
                + "WHERE lab22c1= ? "
                + "and lab57.Lab39C1 = ? "
                + "and lab57c6 is not null "
                + "and lab57c7 is null ";
        getJdbcTemplate().update(query,
                order.getNumberOrderLastValidate(),
                order.getIdTest(),
                order.getNumberOrderSinDate(),
                order.getIdTest());

    }

    default void updateHistory1(OrderTestPatientHistory order) throws Exception
    {
        String query = " UPDATE lab57_2021 SET Lab57C7 = (select lab57c18 from lab57_2021 where lab22c1= ? and lab57_2021.Lab39C1 = ? ) "
                + "WHERE lab22c1= ? "
                + "and lab57_2021.Lab39C1 = ? "
                + "and lab57c6 is not null "
                + "and lab57c7 is null ";
        getJdbcTemplate().update(query,
                order.getNumberOrderLastValidate(),
                order.getIdTest(),
                order.getNumberOrderSinDate(),
                order.getIdTest());

    }

    default void updateHistory2(OrderTestPatientHistory order) throws Exception
    {
        String query = " UPDATE lab57 SET Lab57C7 = (select lab57c18 from lab57_2021 where lab22c1= ? and lab57_2021.Lab39C1 = ? ) "
                + "WHERE lab22c1= ? "
                + "and lab57.Lab39C1 = ? "
                + "and lab57c6 is not null "
                + "and lab57c7 is null ";
        getJdbcTemplate().update(query,
                order.getNumberOrderLastValidate(),
                order.getIdTest(),
                order.getNumberOrderSinDate(),
                order.getIdTest());

    }

    ///----lista update lab17
    default List<OrderTestPatientHistory> listOrderTestPatienLab17() throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED);
            query.append(" SELECT  lab21c1 , lab39c1  "
                    + " FROM lab17 "
                    + "WHERE  "
                    + "lab17c1 is not null "
                    + "and lab17c2 is null");

            return getJdbcTemplate().query(query.toString(), (ResultSet rs, int i) ->
            {
                OrderTestPatientHistory orderTestPatient = new OrderTestPatientHistory();
                orderTestPatient.setIdTest(rs.getInt("lab39c1"));
                orderTestPatient.setIdPatient(rs.getInt("lab21c1"));
                return orderTestPatient;

            });
        } catch (EmptyResultDataAccessException e)
        {
            return new ArrayList<>(0);
        }

    }

    default void updateHistory17(OrderTestPatientHistory order) throws Exception
    {
        String query = " UPDATE  lab17 SET Lab17C2 = "
                + " (select top 1 lab57.lab57c18 from lab57_2021 as lab57 "
                + "inner join lab22_2021 AS LAB22 on LAB22.lab22c1 = lab57.lab22c1 "
                + "where lab57.lab22c1 < 2022010100001  "
                + "and lab57.Lab39C1 = ? "
                + " and lab22.lab21c1= ? "
                + " order by lab57.lab22c1 desc ) "
                + " WHERE  lab17.lab21c1 = ? "
                + " AND lab17.LAB39C1 = ? "
                + " and lab17c2 is null ";
        getJdbcTemplate().update(query,
                order.getIdTest(),
                order.getIdPatient(),
                order.getIdPatient(),
                order.getIdTest()
        );

    }

    default void updateHistory1722(OrderTestPatientHistory order) throws Exception
    {
        String query = " UPDATE  lab17 SET Lab17C2 = "
                + " (select top 1 lab57.lab57c18 from lab57 as lab57 "
                + "inner join lab22 AS LAB22 on LAB22.lab22c1 = lab57.lab22c1 "
                + "where  lab57.Lab39C1 = ? "
                + " and lab22.lab21c1= ? "
                + " and lab57.lab57c8 = 4 "
                + " order by lab57.lab22c1 desc ) "
                + " WHERE  lab17.lab21c1 = ? "
                + " AND lab17.LAB39C1 = ? "
                + " and lab17c2 is null ";
        getJdbcTemplate().update(query,
                order.getIdTest(),
                order.getIdPatient(),
                order.getIdPatient(),
                order.getIdTest()
        );

    }

//METODOS PARA INSERT Y UPDATE COMENTARIOS LOGS DE IMPRESION 
    /**
     * Registra log de cleuinte de impresion en la base de datos.
     *
     * @param print Instancia con los log cliente impresion .
     *
     * @return Instancia con los datos .
     * @throws Exception Error en la base de datos.
     */
    default PrintLog createLogPrintClient(PrintLog print) throws Exception
    {
        try
        {
            if (checkLogsClientPrint(print.getOrder(), print.getBranch()))
            {
                String update = ""
                        + " UPDATE cliprint "
                        + "SET  cliprintc1 = ?,"//mensage
                        + "      cliprintc2 = ?,"//correo 
                        + "      lab05c4 = ? "; // nombre sede
                String where = ""
                        + " WHERE "
                        + " lab22c1 = ? "//orderid
                        + " AND lab05c1 = ? ";//idbranch
                getJdbcTemplate().update(update + where,
                        new Object[]
                        {
                            print.getMessage(), print.getCorreos(), print.getBranchName(), print.getOrder(), print.getBranch()
                        });

                return print;
            } else
            {
                SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                        .withTableName("cliprint");
                HashMap parameters = new HashMap();
                parameters.put("lab22c1", print.getOrder());
                parameters.put("lab05c1", print.getBranch());//id branch
                parameters.put("lab05c4", print.getBranchName());//name branch
                parameters.put("cliprintc1", print.getMessage());//mensage
                parameters.put("cliprintc2", print.getCorreos());//correo
                int rows = insert.execute(parameters);
                return rows > 0 ? print : null;
            }

        } catch (Exception e)
        {
            Log.info(String.class, "ERROR: " + e);
            return null;
        }
    }

    /**
     * Lista PrintLog desde la base de datos.
     *
     * @return Lista de PrintLog .
     * @throws Exception Error en la base de datos.
     */
    default List<PrintLog> listLogPrintClient(String orderInit, String orderEnd) throws Exception
    {
        StringBuilder query = new StringBuilder();
        query.append("");
        query.append(" SELECT lab22c1 , lab05c1 , lab05c4 , cliprintc1 ,cliprintc2 FROM cliprint ");

        try
        {
            return getJdbcTemplate().query(query.toString(), (ResultSet rs, int i) ->
            {
                PrintLog print = new PrintLog();
                print.setOrder(rs.getLong("lab22c1"));
                print.setBranch(rs.getInt("lab05c1"));
                print.setBranchName(rs.getString("lab05c4"));
                print.setMessage(rs.getString("cliprintc1"));
                print.setCorreos(rs.getString("cliprintc2"));
                return print;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Verifica si log de clientPrint tiene registro.
     *
     * @param orderNumber
     * @param idBranch
     *
     * @return boolean
     * @throws Exception Error en la base de datos.
     */
    default boolean checkLogsClientPrint(long orderNumber, int idBranch) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("" + ISOLATION_READ_UNCOMMITTED)
                    .append(" SELECT * FROM cliprint ")
                    .append(" WHERE lab22c1 = ? ")
                    .append(" AND lab05c1 = ? ");
            return getJdbcTemplate().queryForObject(query.toString(), new Object[]
            {
                orderNumber, idBranch
            }, (ResultSet rs, int i) ->
            {
                return true;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            ex.getMessage();
            return false;
        }
    }

    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplate();
}
