package net.cltech.enterprisent.dao.interfaces.operation.reports;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import net.cltech.enterprisent.dao.interfaces.common.ToolsDao;
import net.cltech.enterprisent.dao.interfaces.operation.common.CommentDao;
import net.cltech.enterprisent.domain.masters.demographic.Demographic;
import net.cltech.enterprisent.domain.masters.demographic.DocumentType;
import net.cltech.enterprisent.domain.operation.common.Filter;
import net.cltech.enterprisent.domain.operation.orders.DemographicValue;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.TestList;
import net.cltech.enterprisent.domain.operation.reports.DeliveryDetail;
import net.cltech.enterprisent.domain.operation.reports.DeliveryOrder;
import net.cltech.enterprisent.domain.operation.reports.DeliveryResult;
import static net.cltech.enterprisent.tools.Constants.ISOLATION_READ_UNCOMMITTED;
import net.cltech.enterprisent.tools.DateTools;
import net.cltech.enterprisent.tools.SQLTools;
import net.cltech.enterprisent.tools.Tools;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información de la
 * entrega de resultados.
 *
 * @version 1.0.0
 * @author cmartin
 * @since 24/11/2017
 * @see Creación
 */
public interface DeliveryResultDao {

    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getConnection();

    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbcRep
     */
    public JdbcTemplate getConnectionRep();

    /**
     * Obtiene el dao de Comentarios
     *
     * @return Instancia de CommentDao
     */
    public CommentDao getCommentDao();

    /**
     * Obtiene el dao de tools
     *
     * @return Instancia de toolsDao
     */
    public ToolsDao getToolsDao();

    /**
     * Lista las ordenes por rango de fecha o numero de orden desde base de
     * datos.
     *
     * @param vInitial Rango Inicial.
     * @param vFinal Rango Final.
     * @param searchType Tipo de Busqueda ( 0 -> Fecha, 1 -> Orden)
     * @param demographics Lista de demograficos.
     *
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    default List<Order> list(Long vInitial, Long vFinal, int searchType, final List<Demographic> demographics) throws Exception {
        try {
            List<Order> list = new LinkedList<>();
            int currentYear = DateTools.dateToNumberYear(new Date());
            List<Integer> years = Tools.listOfConsecutiveYears(String.valueOf(vInitial), String.valueOf(vFinal));
            String lab22;

            for (Integer year : years) {
                lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;

                String query = "" + ISOLATION_READ_UNCOMMITTED
                        + "SELECT lab22.lab22c1, lab22c2, lab22c3, lab22c4, lab22c5, lab22.lab07c1, lab22c7, lab22c9, "
                        + "lab103.lab103c1, lab103c2, lab103c3, lab103c4, lab103.lab07c1 AS lab103lab07c1,  "
                        + "lab21.lab21c1, lab21c2, lab21c3, lab21c4, lab21c5, lab21c6, lab21lab80.lab80c1 AS lab21lab80lab80c1, lab21lab80.lab80c2 AS lab21lab80lab80c2, lab21lab80.lab80c3 AS lab21lab80lab80c3, lab21lab80.lab80c4 AS lab21lab80lab80c4, lab21lab80.lab80c5 AS lab21lab80lab80c5, lab21c7, lab21c8, lab21c16, lab21c17, lab21c9, lab21c10, lab21c11, lab21lab08.lab08c1 AS lab21lab08lab08c1, lab21lab08.lab08c2 AS lab21lab08lab08c2, lab21lab08.lab08c4 AS lab21lab08lab08c4, lab21lab54.lab54c1 AS lab21lab54lab54c1, lab21lab54.lab54c2 AS lab21lab54lab54c2, lab21lab54.lab54c3 AS lab21lab54lab54c3, "
                        + "lab05.lab05c1, lab05c10, lab05c4, "
                        + "lab10.lab10c1, lab10c2, lab10c7,  "
                        + "lab14.lab14c1, lab14c2, lab14c3, lab14c32, "
                        + "lab19.lab19c1, lab19c2, lab19c3,  "
                        + "lab22.lab04c1, lab04c2, lab04c3, lab04c4, "
                        + "lab904.lab904c1, lab904c2, lab904c3 ";

                String from = " "
                        + "FROM  " + lab22 + " as lab22 "
                        + "INNER JOIN lab167 ON lab167.lab22c1 = lab22.lab22c1  "
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

                for (Demographic demographic : demographics) {
                    if (demographic.getOrigin().equals("O")) {
                        if (demographic.isEncoded()) {
                            query = query + ", demo" + demographic.getId() + ".lab63c1 as demo" + demographic.getId() + "_id";
                            query = query + ", demo" + demographic.getId() + ".lab63c2 as demo" + demographic.getId() + "_code";
                            query = query + ", demo" + demographic.getId() + ".lab63c3 as demo" + demographic.getId() + "_name";

                            from = from + " LEFT JOIN Lab63 demo" + demographic.getId() + " ON Lab22.lab_demo_" + demographic.getId() + " = demo" + demographic.getId() + ".lab63c1";
                        } else {
                            query = query + ", Lab22.lab_demo_" + demographic.getId();
                        }
                    } else {
                        if (demographic.getOrigin().equals("H")) {
                            if (demographic.isEncoded()) {
                                query = query + ", demo" + demographic.getId() + ".lab63c1 as demo" + demographic.getId() + "_id";
                                query = query + ", demo" + demographic.getId() + ".lab63c2 as demo" + demographic.getId() + "_code";
                                query = query + ", demo" + demographic.getId() + ".lab63c3 as demo" + demographic.getId() + "_name";

                                from = from + " LEFT JOIN Lab63 demo" + demographic.getId() + " ON Lab21.lab_demo_" + demographic.getId() + " = demo" + demographic.getId() + ".lab63c1";
                            } else {
                                query = query + ", Lab21.lab_demo_" + demographic.getId();
                            }
                        }
                    }
                }
                Object[] params = null;
                switch (searchType) {
                    case 1:
                        from = from + " WHERE lab22.lab22c1 BETWEEN ? AND ?";
                        params = new Object[]{
                            vInitial, vFinal
                        };
                        break;
                    case 0:
                        from = from + " WHERE lab22c2 BETWEEN ? AND ?";
                        params = new Object[]{
                            vInitial, vFinal
                        };
                        break;
                    default:
                        break;
                }

                from = from + " AND (lab22c19 = 0 or lab22c19 is null) ";

                RowMapper mapper = (RowMapper<Order>) (ResultSet rs, int i)
                        -> {
                    Order order = new Order();
                    order.setOrderNumber(rs.getLong("lab22c1"));
                    order.setCreatedDateShort(rs.getInt("lab22c2"));
                    order.setCreatedDate(rs.getTimestamp("lab22c3"));
                    order.setHomebound(rs.getInt("lab22c4") == 1);
                    order.setMiles(rs.getInt("lab22c5"));
                    order.setExternalId(rs.getString("lab22c7"));
                    order.setState(rs.getInt("lab07c1"));
                    order.setPreviousState(rs.getString("lab22c9") == null ? null : rs.getInt("lab22c9"));
                    order.getLastUpdateUser().setId(rs.getInt("lab04c1"));
                    order.getLastUpdateUser().setName(rs.getString("lab04c2"));
                    order.getLastUpdateUser().setLastName(rs.getString("lab04c3"));
                    order.getLastUpdateUser().setUserName(rs.getString("lab04c4"));
                    order.setComments(getCommentDao().listCommentOrder(order.getOrderNumber(), null));
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
                    order.getPatient().setOrderNumber(order.getOrderNumber());
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
                    order.getAccount().setEncryptionReportResult(rs.getBoolean("lab14c32"));
                    //MEDICO
                    order.getPhysician().setId(rs.getInt("lab19c1"));
                    order.getPhysician().setName(rs.getString("lab19c2"));
                    order.getPhysician().setLastName(rs.getString("lab19c3"));
                    //TARIFA
                    order.getRate().setId(rs.getInt("lab904c1"));
                    order.getRate().setCode(rs.getString("lab904c2"));
                    order.getRate().setName(rs.getString("lab904c3"));

                    DemographicValue demoValue = null;
                    for (Demographic demographic : demographics) {
                        demoValue = new DemographicValue();
                        demoValue.setIdDemographic(demographic.getId());
                        demoValue.setDemographic(demographic.getName());
                        demoValue.setEncoded(demographic.isEncoded());
                        if (demographic.isEncoded()) {
                            if (rs.getString("demo" + demographic.getId() + "_id") != null) {
                                demoValue.setCodifiedId(rs.getInt("demo" + demographic.getId() + "_id"));
                                demoValue.setCodifiedCode(rs.getString("demo" + demographic.getId() + "_id"));
                                demoValue.setCodifiedName(rs.getString("demo" + demographic.getId() + "_id"));
                            }
                        } else {
                            demoValue.setNotCodifiedValue(rs.getString("lab_demo_" + demographic.getId()));
                        }
                        if (demographic.getOrigin().equals("O")) {
                            order.getDemographics().add(demoValue);
                        } else {
                            order.getPatient().getDemographics().add(demoValue);
                        }
                    }

                    list.add(order);
                    return order;
                };
                getConnection().query(query + from, mapper, params);
            }

            return list;

        } catch (EmptyResultDataAccessException ex) {
            return new ArrayList<>(0);
        }
    }

    /**
     * Lista los destinos desdel base de datos.
     *
     * @param orderId
     * @param testId
     * @return Lista de destinos.
     * @throws Exception Error en la base de datos.
     */
    default List<DeliveryOrder> listDeliveryTestOrder(long orderId, int testId) throws Exception {
        try {
            List<DeliveryOrder> listOrders = new LinkedList<>();
            List<String> listOrdersString = new LinkedList<>();
            List<Integer> years = Tools.listOfConsecutiveYears(String.valueOf(orderId), String.valueOf(orderId));

            String lab03;
            int currentYear = DateTools.dateToNumberYear(new Date());

            for (Integer year : years) {
                lab03 = year.equals(currentYear) ? "lab03" : "lab03_" + year;

                StringBuilder query = new StringBuilder();
                query.append("SELECT  lab03.lab22c1, lab03c5, lab03c6, lab03.lab75c1, lab03c10, ");
                query.append("lab39.lab39c1, lab39.lab39c2, lab39.lab39c4, ");
                query.append(" lab03.lab04c1, lab04c2, lab04c3, lab04c4 ");
                query.append("FROM  ").append(lab03).append(" as lab03 ");
                query.append("LEFT JOIN lab04 ON lab04.lab04c1 = lab03.lab04c1 ");
                query.append("INNER JOIN lab39 ON lab39.lab39c1 = lab03.lab03c1 ");

                StringBuilder from = new StringBuilder();
                from.append(" WHERE lab03c3 = 'T' and lab75c1 is not null AND lab03.lab22c1 =  ? AND lab03.lab03c1 = ? ");

                Object[] params = null;
                params = new Object[]{
                    orderId, testId
                };

                getConnection().query(query + " " + from, (ResultSet rs, int i)
                        -> {

                    DeliveryOrder deliveryOrder = new DeliveryOrder();
                    deliveryOrder.setOrderNumber(rs.getLong("lab22c1"));

                    DeliveryDetail delivery = new DeliveryDetail();
                    delivery.setDate(rs.getTimestamp("lab03c5"));
                    delivery.setSendingType(rs.getString("lab03c6"));
                    delivery.setReceivesPerson(rs.getString("lab03c10"));
                    delivery.setIdMediumDelivery(rs.getInt("lab75c1"));
                    delivery.setIdTest(rs.getInt("lab39c1"));
                    delivery.setCodeTest(rs.getString("lab39c2"));
                    delivery.setNameTest(rs.getString("lab39c4"));

                    /*Usuario*/
                    delivery.getUser().setId(rs.getInt("lab04c1"));
                    delivery.getUser().setName(rs.getString("lab04c2"));
                    delivery.getUser().setLastName(rs.getString("lab04c3"));
                    delivery.getUser().setUserName(rs.getString("lab04c4"));

                    if (!listOrdersString.contains(deliveryOrder.getOrderNumber().toString())) {
                        listOrdersString.add(deliveryOrder.getOrderNumber().toString());
                        listOrders.add(deliveryOrder);
                    }

                    listOrders.get(listOrdersString.indexOf(deliveryOrder.getOrderNumber().toString())).getTests().add(delivery);
                    //listTest(delivery);

                    return deliveryOrder;
                }, params);
            }
            return listOrders;
        } catch (EmptyResultDataAccessException ex) {
            return new ArrayList<>(0);
        }
    }

    /**
     * Lista los destinos desdel base de datos.
     *
     * @param search
     * @param laboratorys
     * @param branch
     * @return Lista de destinos.
     * @throws Exception Error en la base de datos.
     */
    default List<DeliveryOrder> listDeliveryResult(Filter search,  List<Integer> laboratorys, int branch) throws Exception {
        try {
            List<DeliveryOrder> listOrders = new LinkedList<>();
            List<String> listOrdersString = new LinkedList<>();
            List<Integer> years = Tools.listOfConsecutiveYears(String.valueOf(search.getInit()), String.valueOf(search.getEnd()));

            int currentYear = DateTools.dateToNumberYear(new Date());
            String lab22;
            String lab57;
            for (Integer year : years) {
                lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
                lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;
                boolean tableExists = getToolsDao().tableExists(getConnection(), lab22);
                tableExists = tableExists ? getToolsDao().tableExists(getConnection(), lab57) : tableExists;
                if (tableExists) {
                    StringBuilder query = new StringBuilder();
                    query.append("SELECT  lab167.lab22c1, lab167.lab167c2, lab167.lab167c3, lab167.lab80c1, ");
                    query.append("lab39.lab39c1, lab39.lab39c2, lab39.lab39c4, ");
                    query.append("panel.lab39c1 AS panellab39c1, panel.lab39c2 AS panellab39c2, panel.lab39c4 AS panellab39c4, ");
                    query.append(" lab167.lab04c1, lab04c2, lab04c3, lab04c4, ");
                    query.append(" lab21c2 , lab21c3 , lab21c4  , lab21c5 , lab21c6, ");
                    query.append(" lab54.lab54c1 ,lab54.lab54c2 , lab54.lab54c3 ");
                    query.append("FROM lab167 ");
                    query.append("LEFT JOIN lab04 ON lab04.lab04c1 = lab167.lab04c1 ");
                    query.append("INNER JOIN lab39 ON lab39.lab39c1 = lab167.lab39c1 ");
                    query.append("INNER JOIN ").append(lab22).append(" AS lab22 ON lab22.lab22c1 = lab167.lab22c1 ");
                    query.append("INNER JOIN lab21 ON lab22.lab21c1 = lab21.lab21c1 ");
                    query.append("LEFT JOIN lab54 ON lab21.lab54c1 = lab54.lab54c1 ");
                    query.append("LEFT JOIN lab39 panel ON panel.lab39c1 = lab167.lab46c1 ");
                    query.append("INNER JOIN  ").append(lab57).append(" AS lab57 ON lab167.lab39c1 = lab57.lab39c1 AND lab167.lab22c1 = lab57.lab22c1 ");

                    StringBuilder from = new StringBuilder();
                    switch (search.getRangeType()) {
                        case 0:
                            from.append(" WHERE lab57.lab57c34 BETWEEN ? AND ? ");
                            break;
                        case 1:
                            from.append(" WHERE lab167.lab22c1 BETWEEN ? AND ? ");
                            break;
                        default:
                            from.append(" WHERE lab167.lab22c1 IN(").append(search.getOrders().stream().map(order -> order.toString()).collect(Collectors.joining(","))).append(") ");
                            break;
                    }

                    if (search.getTypeDelivery() != 0) {
                        from.append(" AND lab167.lab80c1 = ").append(search.getTypeDelivery());
                    }
                    from.append(" AND (lab22c19 = 0 or lab22c19 is null) ");
                    
                    from.append(SQLTools.buildSQLLaboratoryFilter(laboratorys, branch));

                    Object[] params = null;
                    params = new Object[]{
                        search.getInit(), search.getEnd()
                    };

                    getConnection().query(query + " " + from, (ResultSet rs, int i)
                            -> {

                        DeliveryOrder deliveryOrder = new DeliveryOrder();
                        deliveryOrder.setOrderNumber(rs.getLong("lab22c1"));

                        DeliveryDetail delivery = new DeliveryDetail();
                        delivery.setDate(rs.getTimestamp("lab167c3"));
                        delivery.setReceivesPerson(rs.getString("lab167c2"));
                        delivery.setIdMediumDelivery(rs.getInt("lab80c1"));
                        delivery.setIdTest(rs.getInt("lab39c1"));
                        delivery.setCodeTest(rs.getString("lab39c2"));
                        delivery.setNameTest(rs.getString("lab39c4"));

                        delivery.setIdProfile(rs.getInt("panellab39c1"));
                        delivery.setCodeProfile(rs.getString("panellab39c2"));
                        delivery.setNameProfile(rs.getString("panellab39c4"));

                        /*Usuario*/
                        delivery.getUser().setId(rs.getInt("lab04c1"));
                        delivery.getUser().setName(rs.getString("lab04c2"));
                        delivery.getUser().setLastName(rs.getString("lab04c3"));
                        delivery.getUser().setUserName(rs.getString("lab04c4"));

                        if (!listOrdersString.contains(deliveryOrder.getOrderNumber().toString())) {

                            deliveryOrder.setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                            deliveryOrder.setName1(Tools.decrypt(rs.getString("lab21c3")));
                            deliveryOrder.setName2(Tools.decrypt(rs.getString("lab21c4")));
                            deliveryOrder.setLastName(Tools.decrypt(rs.getString("lab21c5")));
                            deliveryOrder.setSurName(Tools.decrypt(rs.getString("lab21c6")));

                            if (rs.getString("lab54c1") != null) {
                                DocumentType documentType = new DocumentType();
                                documentType.setId(rs.getInt("lab54c1"));
                                documentType.setAbbr(rs.getString("lab54c2"));
                                documentType.setName(rs.getString("lab54c3"));
                                deliveryOrder.setDocumentType(documentType);
                            }

                            listOrdersString.add(deliveryOrder.getOrderNumber().toString());
                            listOrders.add(deliveryOrder);
                        }

                        listOrders.get(listOrdersString.indexOf(deliveryOrder.getOrderNumber().toString())).getTests().add(delivery);

                        return deliveryOrder;
                    }, params);
                }
            }
            return listOrders;
        } catch (EmptyResultDataAccessException ex) {
            return new ArrayList<>(0);
        }
    }

    /**
     * Registra un nuevo destino en la base de datos.
     *
     * @param delivery Instancia con los datos de la entrega de resultados.
     *
     * @return Instancia con los datos del destino.
     * @throws Exception Error en la base de datos.
     */
    default DeliveryResult create(DeliveryResult delivery) throws Exception {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getConnection())
                .withTableName("lab167")
                .usingGeneratedKeyColumns("lab167c1");

        HashMap parameters = new HashMap();
        parameters.put("lab22c1", delivery.getOrderNumber());
        parameters.put("lab167c2", delivery.getReceivesPerson());
        parameters.put("lab04c1", delivery.getUser().getId());
        parameters.put("lab167c3", timestamp);
        parameters.put("lab39c1", delivery.getIdTest());
        parameters.put("lab46c1", delivery.getIdProfile());
        parameters.put("lab80c1", delivery.getTypeDelivery());

        Number key = insert.executeAndReturnKey(parameters);
        delivery.setId(key.intValue());

        //createTest(delivery);
        return delivery;
    }

    /**
     * Registra una nueva ruta en la base de datos.
     *
     * @param delivery Instancia con los datos de la entrega de resultados.
     *
     * @throws Exception Error en la base de datos.
     */
    default void createTest(DeliveryResult delivery) throws Exception {
        for (TestList resultTest : delivery.getTests()) {
            SimpleJdbcInsert insert = new SimpleJdbcInsert(getConnection())
                    .withTableName("lab168");

            HashMap parameters = new HashMap();
            parameters.put("lab167c1", delivery.getId());
            parameters.put("lab39c1", resultTest.getId());

            insert.execute(parameters);
        }
    }

    /**
     * Actualiza la fecha de atencion de una orden
     *
     * @param orderId
     * @param testId
     * @return True - Si la fecha de atendicon de la orden se actualizo, False-
     * Si no fue así
     * @throws Exception Error en la base de datos.
     */
    default boolean getDeliverybyOrderTest(long orderId, int testId) throws Exception {
        try {
            StringBuilder query = new StringBuilder();
            query.append("SELECT 1 AS EXIST ")
                    .append("FROM lab167 ")
                    .append("WHERE lab22c1 =").append(orderId).append(" AND lab39c1 =").append(testId);

            return getConnection().queryForObject(query.toString(),
                    (ResultSet rs, int i)
                    -> {
                return rs.getInt("EXIST") == 1;
            });
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Inserta en la tabla de reporte final en base 64 el reporte final asignado
     * a cierta orden
     *
     * @param idOrder
     * @param reportBase64
     * @return Retorna OK (Si la insercion fue correcta) - Error (Si ocurrio un
     * error durante la transacción)
     * @throws Exception Error al insertar el reporte final
     */
    default String insertFinalReport(long idOrder, String reportBase64) throws Exception {
        try {
            Timestamp timestamp = new Timestamp(new Date().getTime());
            SimpleJdbcInsert insert = new SimpleJdbcInsert(getConnectionRep())
                    .withTableName("rep01");

            HashMap parameters = new HashMap();
            parameters.put("lab22c1", idOrder);
            parameters.put("rep01c1", reportBase64);
            parameters.put("rep01c2", timestamp);
            insert.execute(parameters);

            return "OK";
        } catch (Exception e) {
            return "Error";
        }
    }
}
