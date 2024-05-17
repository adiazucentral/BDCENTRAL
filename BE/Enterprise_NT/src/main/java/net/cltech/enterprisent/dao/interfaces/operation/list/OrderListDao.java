package net.cltech.enterprisent.dao.interfaces.operation.list;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import net.cltech.enterprisent.dao.interfaces.common.ToolsDao;
import net.cltech.enterprisent.dao.interfaces.operation.common.CommentDao;
import net.cltech.enterprisent.domain.DTO.migracionIngreso.PatientNT;
import net.cltech.enterprisent.domain.integration.resultados.DemoHeader;
import net.cltech.enterprisent.domain.masters.appointment.Shift;
import net.cltech.enterprisent.domain.masters.common.Item;
import net.cltech.enterprisent.domain.masters.demographic.Branch;
import net.cltech.enterprisent.domain.masters.demographic.Demographic;
import net.cltech.enterprisent.domain.masters.demographic.DocumentType;
import net.cltech.enterprisent.domain.masters.interview.Interview;
import net.cltech.enterprisent.domain.masters.interview.PanicInterview;
import net.cltech.enterprisent.domain.masters.test.ExcludeTest;
import net.cltech.enterprisent.domain.masters.test.Laboratory;
import net.cltech.enterprisent.domain.masters.test.Sample;
import net.cltech.enterprisent.domain.operation.billing.integration.OrderBilling;
import net.cltech.enterprisent.domain.operation.billing.integration.TestBilling;
import net.cltech.enterprisent.domain.operation.common.Filter;
import net.cltech.enterprisent.domain.operation.demographic.SuperAccount;
import net.cltech.enterprisent.domain.operation.demographic.SuperBranch;
import net.cltech.enterprisent.domain.operation.demographic.SuperDocumentType;
import net.cltech.enterprisent.domain.operation.demographic.SuperPhysician;
import net.cltech.enterprisent.domain.operation.demographic.SuperRace;
import net.cltech.enterprisent.domain.operation.demographic.SuperRate;
import net.cltech.enterprisent.domain.operation.demographic.SuperService;
import net.cltech.enterprisent.domain.operation.list.FilterDemographic;
import net.cltech.enterprisent.domain.operation.list.TestBranchCheck;
import net.cltech.enterprisent.domain.operation.orders.CommentOrder;
import net.cltech.enterprisent.domain.operation.orders.DemographicValue;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.OrderBasic;
import net.cltech.enterprisent.domain.operation.orders.OrderList;
import net.cltech.enterprisent.domain.operation.orders.OrderReportINS;
import net.cltech.enterprisent.domain.operation.orders.Patient;
import net.cltech.enterprisent.domain.operation.orders.SuperPatient;
import net.cltech.enterprisent.domain.operation.orders.SuperTest;
import net.cltech.enterprisent.domain.operation.orders.Test;
import net.cltech.enterprisent.domain.operation.orders.TestList;
import net.cltech.enterprisent.domain.operation.orders.TestPrice;
import net.cltech.enterprisent.domain.operation.orders.Worklist;
import net.cltech.enterprisent.domain.operation.orders.excel.OrderReportAidaAcs;
import net.cltech.enterprisent.domain.operation.orders.excel.TestReportAida;
import net.cltech.enterprisent.domain.operation.remission.RemissionOrderCentral;
import net.cltech.enterprisent.domain.operation.results.ResultTestComment;
import net.cltech.enterprisent.domain.operation.test.SuperContainer;
import net.cltech.enterprisent.tools.Constants;
import static net.cltech.enterprisent.tools.Constants.ISOLATION_READ_UNCOMMITTED;
import net.cltech.enterprisent.tools.DateTools;
import net.cltech.enterprisent.tools.HtmlToTxt;
import net.cltech.enterprisent.tools.SQLTools;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.tools.enums.LISEnum;
import net.cltech.enterprisent.tools.log.orders.OrderCreationLog;
import net.cltech.enterprisent.tools.log.results.ResultsLog;
import net.cltech.enterprisent.tools.mappers.MigrationMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información de los
 * listados.
 *
 * @version 1.0.0
 * @author cmartin
 * @since 05/09/2017
 * @see Creación
 */
public interface OrderListDao {

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
     * Obtiene el dao de Comentarios
     *
     * @return Instancia de CommentDao
     */
    public CommentDao getCommentDao();

    /**
     * Lista las ordenes por rango de fecha o numero de orden desde base de
     * datos para el modulo de listados.
     *
     * @param search
     * @param demographics Lista de demograficos.
     * @param account
     * @param physician
     * @param rate
     * @param service
     * @param documenttype
     * @param race
     * @param price indica si se consultan los precios de los examenes
     *
     *
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    default List<OrderList> listBasic(Filter search, final List<Demographic> demographics, boolean account, boolean physician, boolean rate, boolean service, boolean race, boolean documenttype, boolean consultarSistemaCentral, int sistemaCentralListados, Boolean price,  List<Integer> laboratorys, int idbranch) throws Exception {
        try {
            // Consulta de ordenes por historico:
            List<Integer> years = Tools.listOfConsecutiveYears(String.valueOf(search.getInit()), String.valueOf(search.getEnd()));
            String lab22;
            String lab57;
            String lab95;
            String lab900;
            int currentYear = DateTools.dateToNumberYear(new Date());

            HashMap<Long, OrderList> listOrders = new HashMap<>();

            for (Integer year : years) {
                lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
                lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;
                lab95 = year.equals(currentYear) ? "lab95" : "lab95_" + year;
                lab900 = year.equals(currentYear) ? "lab900" : "lab900_" + year;

                boolean tableExists = getToolsDao().tableExists(getConnection(), lab22);
                tableExists = tableExists ? getToolsDao().tableExists(getConnection(), lab57) : tableExists;
                if (tableExists) {
                    // Query
                    StringBuilder query = new StringBuilder();
                    query.append(ISOLATION_READ_UNCOMMITTED);
                    query.append("SELECT lab22.lab22c1, lab22c2, lab22c3, lab22c7, ");
                    query.append("lab22.lab103c1, lab103c2, lab103c3, ");
                    query.append("lab21.lab21c1, lab21c2, lab21c3, lab21c4, lab21c5, lab21c6, lab21c7, lab21c8, lab21c9, lab21c10, lab21c16, lab21c17, ");
                    query.append("lab21lab80.lab80c1 AS lab21lab80lab80c1, ");
                    query.append("lab21lab80.lab80c3 AS lab21lab80lab80c3, ");
                    query.append("lab21lab80.lab80c4 AS lab21lab80lab80c4, lab21lab80.lab80c5 AS lab21lab80lab80c5, ");
                    query.append("lab05.lab05c1, lab05c10, lab05c4, ");
                    query.append("lab04.lab04c2, lab04.lab04c3, lab04.lab04c4, ");
                    query.append("userCreation.lab04c1 AS userCreationc1, userCreation.lab04c2 AS userCreationc2, userCreation.lab04c3 AS userCreationc3, userCreation.lab04c4 AS userCreationc4, ");
                    query.append("lab57.lab57c1, lab57.lab57c2, lab57.lab57c8, lab57.lab57c9, lab57.lab57c14, lab57.lab57c15, lab57.lab57c16, lab57.lab57c4, lab57.lab57c18, lab57.lab57c39, lab57c37, lab57c22, lab57c25, ");
                    query.append("userResult.lab04c1 AS userResultc1, userResult.lab04c2 AS userResultc2, userResult.lab04c3 AS userResultc3,  ");
                    query.append("userValidation.lab04c1 AS userValidationc1, userValidation.lab04c2 AS userValidationc2, userValidation.lab04c3 AS userValidationc3,  ");
                    query.append("userPrinted.lab04c1 AS userPrintedc1, userPrinted.lab04c2 AS userPrintedc2, userPrinted.lab04c3 AS userPrintedc3,  ");
                    query.append("lab39.lab39c1, lab39.lab39c2, lab39.lab39c3, lab39.lab39c4, lab39.lab39c6,lab39.lab39c37, lab39.lab39c27, lab39.lab39c26, ");
                    query.append("lab43c3, lab43.lab43c4, ");
                    query.append("lab39.lab24c1, lab24c2, lab24c9, ");
                    query.append("lab45.lab45c2, ");
                    query.append("lab56.lab56c2, ");
                    query.append("panel.lab39c1 AS panellab39c1, panel.lab39c2 AS panellab39c2, panel.lab39c3 AS panellab39c3, panel.lab39c4 AS panellab39c4, panel.lab39c37 AS panellab39c37, lab39.lab39c39 AS panellab39c39, ");
                    query.append("pack.lab39c1 AS packlab39c1, pack.lab39c2 AS packlab39c2, pack.lab39c3 AS packlab39c3, pack.lab39c4 AS packlab39c4, pack.lab39c37 AS packlab39c37,  lab39.lab39c39 AS packlab39c39, ");
                    query.append("lab40.lab40c2, lab40.lab40c3, ");
                    query.append("lab16.lab16c3, lab11.lab11c1,");
                    query.append("lab57.lab57c54, lab57.lab57c39, ");
                    query.append("lab60.lab60c1 as idCommentOrder, lab60.lab60c3 as commentOrder, lab60.lab60c4 as typeCommentOrder, lab60.lab60c6 as isPrintCommentOrder, ");
                    query.append("lab95c1, lab95c2, lab95c3, lab95.lab04c1 as userComment ");
                    StringBuilder from = new StringBuilder();
                    from.append(" FROM ").append(lab57).append(" AS lab57 ");
                    from.append("INNER JOIN ").append(lab22).append(" AS lab22 ON lab22.lab22c1 = lab57.lab22c1 ");
                    from.append("INNER JOIN lab05 ON lab05.lab05c1 = lab22.lab05c1  ");
                    from.append("INNER JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1  ");
                    from.append("INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1  ");
                    from.append("INNER JOIN lab80 lab21lab80 ON lab21lab80.lab80c1 = lab21.lab80c1  ");
                    from.append("INNER JOIN lab103 ON lab103.lab103c1 = lab22.lab103c1  ");
                    from.append("LEFT JOIN lab04 userCreation ON userCreation.lab04c1 = lab22.lab04c1_1  ");
                    from.append("LEFT JOIN lab04 ON lab04.lab04c1 = lab22.lab04c1  ");
                    from.append("LEFT JOIN lab39 panel ON panel.lab39c1 = lab57.lab57c14 ");
                    from.append("LEFT JOIN lab39 pack ON pack.lab39c1 = lab57.lab57c15 ");
                    from.append("LEFT JOIN lab43 ON lab43.lab43c1 = lab39.lab43c1 ");
                    from.append("LEFT JOIN lab24 ON lab24.lab24c1 = lab39.lab24c1 ");
                    from.append("LEFT JOIN lab45 ON lab45.lab45c1 = lab39.lab45c1 ");
                    from.append("LEFT JOIN lab40 ON lab40.lab40c1 = lab57.lab40c1 ");
                    from.append("LEFT JOIN lab56 ON lab56.lab56c1 = lab24.lab56c1 ");
                    from.append("LEFT JOIN lab60 ON lab60.lab60c2 = lab57.lab22c1 ");
                    from.append("LEFT JOIN lab11 ON lab11.lab24c1 = lab57.lab24c1 AND lab11.lab22c1 = lab57.lab22c1 ");
                    from.append("LEFT JOIN lab16 ON lab16.lab16c1 = lab11.lab16c1 ");
                    from.append("LEFT JOIN lab04 AS userResult ON userResult.lab04c1 = lab57.lab57c3 ");
                    from.append("LEFT JOIN lab04 AS userValidation ON userValidation.lab04c1 = lab57.lab57c19 ");
                    from.append("LEFT JOIN lab04 AS userPrinted ON userPrinted.lab04c1 = lab57.lab57c23 ");

                    if (price) {
                        query.append(", lab900c2, lab900c3, lab900c4, lab900c5 ");
                        from.append("LEFT JOIN ").append(lab900).append(" AS lab900 ON lab900.lab22c1 = lab57.lab22c1 AND lab900.lab39c1 = lab57.lab39c1");
                    }

                    from.append(" LEFT JOIN ").append(lab95).append(" AS lab95 ON lab95.lab22c1 = lab57.lab22c1 AND lab95.lab39c1 = lab57.lab39c1 ");
                    if (documenttype) {
                        query.append(" ,lab21lab54.lab54c1 AS lab21lab54lab54c1,  lab21lab54.lab54c2 AS lab21lab54lab54c2, lab21lab54.lab54c3 AS lab21lab54lab54c3 ");
                        from.append("LEFT JOIN lab54 lab21lab54 ON lab21lab54.lab54c1 = lab21.lab54c1  ");
                    }
                    if (service) {
                        query.append(" , lab22.lab10c1, lab10c2, lab10c7 ");
                        from.append("LEFT JOIN lab10 ON lab10.lab10c1 = lab22.lab10c1  ");
                    }
                    if (account) {
                        query.append(" ,lab22.lab14c1,  lab14c2, lab14c3, lab14c32 ");
                        from.append("LEFT JOIN lab14 ON lab14.lab14c1 = lab22.lab14c1  ");
                    }
                    if (physician) {
                        query.append(" ,lab22.lab19c1,  lab19c2, lab19c3, lab19c21 ");
                        from.append("LEFT JOIN lab19 ON lab19.lab19c1 = lab22.lab19c1  ");
                    }
                    if (rate) {
                        query.append(" , lab22.lab904c1, lab904c2, lab904c3 ");
                        from.append("LEFT JOIN lab904 ON lab904.lab904c1 = lab22.lab904c1  ");
                    }
                    if (race) {
                        query.append(" , lab21lab08.lab08c1 AS lab21lab08lab08c1, lab21lab08.lab08c2 AS lab21lab08lab08c2, lab21lab08.lab08c5 AS lab21lab08lab08c5 ");
                        from.append("LEFT JOIN lab08 lab21lab08 ON lab21lab08.lab08c1 = lab21.lab08c1  ");
                    }

                    if (consultarSistemaCentral) {
                        query.append(" , lab61c1 ");
                        from.append("LEFT JOIN lab61 ON lab61.lab39c1 = lab57.lab39c1 and lab61.lab118c1 = ").append(sistemaCentralListados).append(" ");
                    }

                    for (Demographic demographic : demographics) {
                        query.append(Tools.createDemographicsQuery(demographic).get(0));
                        from.append(Tools.createDemographicsQuery(demographic).get(1));
                    }

                    Object[] params = null;
                    switch (search.getRangeType()) {
                        case 0:
                            from.append(" WHERE lab39.lab39c37 != 2 AND (lab57.lab57c34 BETWEEN ? AND ? )");
                            break;
                        case 1:
                            from.append(" WHERE lab39.lab39c37 != 2 AND (lab22.lab22c1 BETWEEN ? AND ? )");
                            break;
                        case 3:
                            from.append(" WHERE lab39.lab39c37 != 2 AND (lab22.lab22c2 BETWEEN ? AND ? ) ");
                            break;
                        default:
                            from.append(" WHERE lab39.lab39c37 != 2 AND lab22.lab22c1 IN(").append(search.getOrders().stream().map(order -> order.toString()).collect(Collectors.joining(","))).append(") ");
                            break;
                    }

                    // Filtro por tipo de orden 
                    if (search.getOrderType() != 0) {
                        from.append(" AND lab22.lab103c1 = ").append(search.getOrderType());
                    }

                    //Filtro por examenes confidenciales y areas
                    switch (search.getTestFilterType()) {
                        case 1:
                            from.append(" AND lab39.lab43c1 IN (").append(search.getTests().stream().map(area -> area.toString()).collect(Collectors.joining(","))).append(") ");
                            break;
                        case 2:
                            String listtest = search.getTests().stream().map(test -> test.toString()).collect(Collectors.joining(","));
                            from.append(" AND ((lab57.lab57c14 IN (").append(listtest).append(")) or lab39.lab39c1 IN (").append(listtest).append(")) ");
                            break;
                        case 3:
                            String listtestconfidencial = search.getTests().stream().map(test -> test.toString()).collect(Collectors.joining(","));
                            from.append(" AND ((lab57.lab57c14 IN (").append(listtestconfidencial).append(")) or lab39.lab39c1 IN (").append(listtestconfidencial).append(")) ");
                            break;
                        default:
                            break;
                    }

                    // Filtro por laboratorio 
                    if (search.getLaboratory() != 0) {
                        from.append(" AND lab57.lab40c1 = ").append(search.getLaboratory());
                    }

                    //Filtro por descripcion de paquetes
                    if (search.isPackageDescription()) {
                        from.append(" AND lab57.lab57c15 is not null");
                    }

                    if (!search.getTestState().isEmpty() && !search.getSampleState().isEmpty()) {
                        if (search.isGroupProfiles()) {
                            from.append("  AND (lab39.lab39c37 = 1 or lab57.lab57c8 IN(").append(search.getTestState().stream().map(t -> t.toString()).collect(Collectors.joining(","))).append(") "
                                    + " AND ((lab57.lab57c16 IN(").append(search.getSampleState().stream().map(t -> t.toString()).collect(Collectors.joining(","))).append("))  )  )");
                        } else {
                            from.append("  AND ( lab39.lab39c37 = 0 and lab57.lab57c8 IN(").append(search.getTestState().stream().map(t -> t.toString()).collect(Collectors.joining(","))).append(") "
                                    + " AND ((lab57.lab57c16 IN(").append(search.getSampleState().stream().map(t -> t.toString()).collect(Collectors.joining(","))).append("))  )  )");
                        }
                    } else if (!search.getTestState().isEmpty()) {
                        if (search.isGroupProfiles()) {
                            from.append(" AND ( lab39.lab39c37 = 1 or lab57.lab57c8 IN(").append(search.getTestState().stream().map(t -> t.toString()).collect(Collectors.joining(","))).append(")) ");
                        } else {
                            from.append(" AND ( lab39.lab39c37 = 0 and lab57.lab57c8 IN(").append(search.getTestState().stream().map(t -> t.toString()).collect(Collectors.joining(","))).append(")) ");
                        }
                    } else if (!search.getSampleState().isEmpty()) {
                        if (search.isGroupProfiles()) {
                            from.append(" AND ( lab39.lab39c37 = 1 or lab57.lab57c16 IN(").append(search.getSampleState().stream().map(t -> t.toString()).collect(Collectors.joining(","))).append(") )");
                        } else {
                            from.append(" AND ( lab57.lab57c16 IN(").append(search.getSampleState().stream().map(t -> t.toString()).collect(Collectors.joining(","))).append(") )");
                        }
                    }
                    if (search.getResultState() != null) {
                        switch (search.getResultState().get(0)) {
                            case 1: //modificado
                                from.append("  ");
                                break;
                            case 2://repeticion
                                from.append(" AND (lab57.lab24c1 is null or lab57.lab57c48 is not null) ");
                                break;
                            case 3://patologico
                                if (search.isGroupProfiles()) {
                                    from.append(" AND (lab39.lab39c37 = 1 or lab57.lab57c9 IN(1,2,3))");
                                } else {
                                    from.append(" AND lab57.lab57c9 IN(1,2,3)");
                                }
                                break;
                            case 4://panico
                                if (search.isGroupProfiles()) {
                                    from.append(" AND ( lab39.lab39c37 = 1 or lab57.lab57c9 IN(4,5,6))");
                                } else {
                                    from.append(" AND lab57.lab57c9 IN(4,5,6)");
                                }
                                break;
                            case 5://deltacheck
                                from.append(" ");
                                break;
                            default:
                                break;
                        }
                    }

                    //filtro por estado de la muestra.
                    switch (search.getCheck()) {
                        case 1:
                            from.append(" AND lab57.lab57c16 = ").append(LISEnum.ResultSampleState.CHECKED.getValue());
                            break;
                        case 2:
                            from.append(" AND (lab57.lab24c1 is null or lab57.lab57c16 < ").append(LISEnum.ResultSampleState.CHECKED.getValue()).append(" ) ");
                            break;
                        default:
                            break;
                    }

                    if (search.getUserId() > 0) {
                        from.append(" AND lab22.lab04c1_1 = ").append(search.getUserId());
                    }
                    
                    from.append(SQLTools.buildSQLLaboratoryFilter(laboratorys, idbranch));

                    from.append("  AND lab22.lab07C1 != 0 AND lab21.lab21c1 != 0 AND (lab22c19 = 0 or lab22c19 is null)");

                    params = new Object[]{
                        search.getInit(), search.getEnd()
                    };

                    RowMapper mapper = (RowMapper<OrderList>) (ResultSet rs, int i)
                            -> {
                        OrderList order = new OrderList();
                        if (!listOrders.containsKey(rs.getLong("lab22c1"))) {
                            order.setOrderNumber(rs.getLong("lab22c1"));

                            order.setCreatedDateShort(rs.getInt("lab22c2"));
                            order.setCreatedDate(rs.getTimestamp("lab22c3"));
                            order.setExternalId(rs.getString("lab22c7"));

                            order.getCreateUser().setId(rs.getInt("userCreationc1"));
                            order.getCreateUser().setName(rs.getString("userCreationc2"));
                            order.getCreateUser().setLastName(rs.getString("userCreationc3"));
                            order.getCreateUser().setUserName(rs.getString("userCreationc4"));

                            order.getLastUpdateUser().setName(rs.getString("lab04c2"));
                            order.getLastUpdateUser().setLastName(rs.getString("lab04c3"));
                            order.getLastUpdateUser().setUserName(rs.getString("lab04c4"));

                            //TIPO DE ORDEN
                            order.getType().setId(rs.getInt("lab103c1"));
                            order.getType().setCode(rs.getString("lab103c2"));
                            order.getType().setName(rs.getString("lab103c3"));

                            //PACIENTE
                            SuperPatient patient = new SuperPatient();
                            patient.setId(rs.getInt("lab21c1"));
                            patient.setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                            patient.setName1(Tools.decrypt(rs.getString("lab21c3")));
                            patient.setName2(Tools.decrypt(rs.getString("lab21c4")));
                            patient.setLastName(Tools.decrypt(rs.getString("lab21c5")));
                            patient.setSurName(Tools.decrypt(rs.getString("lab21c6")));
                            patient.setBirthday(rs.getTimestamp("lab21c7"));
                            patient.setEmail(rs.getString("lab21c8"));
                            patient.setSize(rs.getBigDecimal("lab21c9"));
                            patient.setWeight(rs.getBigDecimal("lab21c10"));
                            patient.setPhone(rs.getString("lab21c16"));
                            patient.setAddress(rs.getString("lab21c17"));

                            //PACIENTE - SEXO
                            patient.getSex().setId(rs.getInt("lab21lab80lab80c1"));
                            patient.getSex().setCode(rs.getString("lab21lab80lab80c3"));
                            patient.getSex().setEsCo(rs.getString("lab21lab80lab80c4"));
                            patient.getSex().setEnUsa(rs.getString("lab21lab80lab80c5"));

                            if (documenttype) {
                                //PACIENTE - TIPO DE DOCUMENTO
                                SuperDocumentType documenttypeLaboratory = new SuperDocumentType();
                                documenttypeLaboratory.setId(rs.getInt("lab21lab54lab54c1"));
                                documenttypeLaboratory.setAbbr(rs.getString("lab21lab54lab54c2"));
                                documenttypeLaboratory.setName(rs.getString("lab21lab54lab54c3"));
                                patient.setDocumentType(documenttypeLaboratory);
                            }

                            order.setPatient(patient);

                            //SEDE
                            SuperBranch branch = new SuperBranch();
                            branch.setId(rs.getInt("lab05c1"));
                            branch.setCode(rs.getString("lab05c10"));
                            branch.setName(rs.getString("lab05c4"));
                            order.setBranch(branch);

                            if (service) {
                                //SERVICIO
                                SuperService serviceLaboratory = new SuperService();
                                serviceLaboratory.setId(rs.getInt("lab10c1"));
                                serviceLaboratory.setCode(rs.getString("lab10c7"));
                                serviceLaboratory.setName(rs.getString("lab10c2"));
                                order.setService(serviceLaboratory);
                            }

                            if (account) {
                                //EMPRESA
                                SuperAccount accountLaboratory = new SuperAccount();
                                accountLaboratory.setNit(rs.getString("lab14c2"));
                                accountLaboratory.setId(rs.getInt("lab14c1"));
                                accountLaboratory.setName(rs.getString("lab14c3"));
                                accountLaboratory.setEncryptionReportResult(rs.getBoolean("lab14c32"));
                                order.setAccount(accountLaboratory);
                            }

                            if (physician) {
                                //MEDICO
                                SuperPhysician physicianLaboratory = new SuperPhysician();
                                physicianLaboratory.setId(rs.getInt("lab19c1"));
                                physicianLaboratory.setName(rs.getString("lab19c2"));
                                physicianLaboratory.setName(rs.getString("lab19c2"));
                                physicianLaboratory.setEmail(rs.getString("lab19c21"));
                                order.setPhysician(physicianLaboratory);
                            }
                            if (rate) {
                                //TARIFA
                                SuperRate rateLaboratory = new SuperRate();
                                rateLaboratory.setId(rs.getInt("lab904c1"));
                                rateLaboratory.setCode(rs.getString("lab904c2"));
                                rateLaboratory.setName(rs.getString("lab904c3"));
                                order.setRate(rateLaboratory);
                            }
                            if (race) {
                                //PACIENTE - RAZA
                                SuperRace racePatient = new SuperRace();
                                racePatient.setId(rs.getInt("lab21lab08lab08c1"));
                                racePatient.setCode(rs.getString("lab21lab08lab08c5"));
                                racePatient.setName(rs.getString("lab21lab08lab08c2"));
                                order.getPatient().setRace(racePatient);
                            }

                            List<DemographicValue> demographicsOrder = new LinkedList<>();

                            for (Demographic demographic : demographics) {

                                String[] data;
                                if (demographic.isEncoded()) {
                                    data = new String[]{
                                        rs.getString("demo" + demographic.getId() + "_id"),
                                        rs.getString("demo" + demographic.getId() + "_code"),
                                        rs.getString("demo" + demographic.getId() + "_name")
                                    };
                                } else {
                                    data = new String[]{
                                        rs.getString("lab_demo_" + demographic.getId())
                                    };
                                }
                                demographicsOrder.add(Tools.getDemographicsValue(demographic, data));

                            }
                            order.setAllDemographics(demographicsOrder);
                            listOrders.put(order.getOrderNumber(), order);

                        } else {
                            order = listOrders.get(rs.getLong("lab22c1"));
                        }
                        if (rs.getString("commentOrder") != null && !rs.getString("commentOrder").isEmpty()) {
                            CommentOrder commentOrder = new CommentOrder();
                            commentOrder.setId(rs.getInt("idCommentOrder"));
                            commentOrder.setIdRecord(order.getOrderNumber());
                            commentOrder.setComment(rs.getString("commentOrder"));
                            commentOrder.setType(rs.getShort("typeCommentOrder"));
                            commentOrder.setPrint(rs.getInt("isPrintCommentOrder") == 1);
                            if (!listOrders.get(order.getOrderNumber()).getComments().contains(commentOrder)) {
                                listOrders.get(order.getOrderNumber()).getComments().add(commentOrder);
                            }
                        }

                        TestList test = new TestList();
                        test.setId(rs.getInt("lab39c1"));
                        test.setCode(rs.getString("lab39c2"));
                        test.setAbbr(rs.getString("lab39c3"));
                        test.setName(rs.getString("lab39c4"));
                        test.setViewquery(rs.getInt("lab39c26"));

                        test.getResult().setResult(Tools.decrypt(rs.getString("lab57c1")));
                        test.getResult().setDateResult(rs.getTimestamp("lab57c2"));
                        test.getResult().setDateOrdered(rs.getTimestamp("lab57c4"));
                        test.getResult().setDateTake(rs.getTimestamp("lab57c39"));
                        test.getResult().setDateValidation(rs.getTimestamp("lab57c18"));
                        test.getResult().setDateVerific(rs.getTimestamp("lab57c37"));
                        test.getResult().setState(rs.getInt("lab57c8"));
                        test.getResult().setSampleState(rs.getInt("lab57c16"));
                        test.getResult().setPathology(rs.getInt("lab57c9"));
                        test.getResult().setDatePrint(rs.getTimestamp("lab57c22"));
                        test.getResult().setPrint(rs.getInt("lab57c25") == 1);

                        if (rs.getString("userResultc1") != null) {
                            test.getResult().getUserRes().setId(rs.getInt("userResultc1"));
                            test.getResult().getUserRes().setName(rs.getString("userResultc2"));
                            test.getResult().getUserRes().setLastName(rs.getString("userResultc3"));
                        } else {
                            test.getResult().setUserRes(null);
                        }

                        if (rs.getString("userValidationc1") != null) {
                            test.getResult().getUserVal().setId(rs.getInt("userValidationc1"));
                            test.getResult().getUserVal().setName(rs.getString("userValidationc2"));
                            test.getResult().getUserVal().setLastName(rs.getString("userValidationc3"));
                        } else {
                            test.getResult().setUserVal(null);
                        }

                        if (rs.getString("userPrintedc1") != null) {
                            test.getResult().getUserPri().setId(rs.getInt("userPrintedc1"));
                            test.getResult().getUserPri().setName(rs.getString("userPrintedc2"));
                            test.getResult().getUserPri().setLastName(rs.getString("userPrintedc3"));
                        } else {
                            test.getResult().setUserPri(null);
                        }

                        test.setTestType(rs.getShort("lab39c37"));

                        test.setConfidential(rs.getInt("lab39c27") == 1);
                        test.setGender(new Item(rs.getInt("lab39c6")));

                        test.setRemission(rs.getInt("lab57c54"));
                        test.setProfile(rs.getInt("lab57c14"));

                        test.getLaboratory().setCode(rs.getInt("lab40c2"));
                        test.getLaboratory().setName(rs.getString("lab40c3"));

                        test.setExcluideTestProfile(rs.getInt("panellab39c39"));

                        if (rs.getString("panellab39c1") != null) {
                            SuperTest panel = new SuperTest();
                            panel.setId(rs.getInt("panellab39c1"));
                            panel.setCode(rs.getString("panellab39c2"));
                            panel.setAbbr(rs.getString("panellab39c3"));
                            panel.setName(rs.getString("panellab39c4"));
                            panel.setTestType(rs.getShort("panellab39c37"));
                            test.setPanel(panel);
                        }

                        if (rs.getString("packlab39c1") != null) {
                            SuperTest pack = new SuperTest();
                            pack.setId(rs.getInt("packlab39c1"));
                            pack.setCode(rs.getString("packlab39c2"));
                            pack.setAbbr(rs.getString("packlab39c3"));
                            pack.setName(rs.getString("packlab39c4"));
                            pack.setTestType(rs.getShort("packlab39c37"));
                            test.setPack(pack);
                        }

                        test.getArea().setAbbreviation(rs.getString("lab43c3"));
                        test.getArea().setName(rs.getString("lab43c4"));

                        test.getSample().setId(rs.getInt("lab24c1"));
                        test.getSample().setName(rs.getString("lab24c2"));
                        test.getSample().setCodesample(rs.getString("lab24c9"));

                        SuperContainer container = new SuperContainer();
                        container.setName(rs.getString("lab56c2"));
                        test.getSample().setContainer(container);
                        test.getSample().setTakeDate(rs.getTimestamp("lab57c39"));

                        test.setRackStore(rs.getString("lab16c3"));
                        test.setPositionStore(rs.getString("lab11c1"));
                        test.getUnit().setName(rs.getString("lab45c2"));

                        ResultTestComment comment = new ResultTestComment();
                        comment.setOrder(rs.getLong("lab22c1"));
                        comment.setTestId(rs.getInt("lab39c1"));
                        comment.setComment(rs.getString("lab95c1"));
                        comment.setCommentDate(rs.getDate("lab95c2"));
                        comment.setPathology(rs.getShort("lab95c3"));
                        comment.setUserId(rs.getInt("userComment"));
                        comment.setCommentChanged(false);
                        test.setResultComment(comment);

                        if (consultarSistemaCentral) {
                            test.setHomologationCodes(rs.getString("lab61c1"));
                        }

                        if (price) {
                            TestPrice testPrice = new TestPrice();
                            testPrice.setServicePrice(rs.getBigDecimal("lab900c2"));
                            testPrice.setPatientPrice(rs.getBigDecimal("lab900c3"));
                            testPrice.setInsurancePrice(rs.getBigDecimal("lab900c4"));
                            testPrice.setTax(rs.getDouble("lab900c5"));
                            test.setTestPrice(testPrice);
                        }

                        if (!listOrders.get(order.getOrderNumber()).getTests().contains(test)) {
                            listOrders.get(order.getOrderNumber()).getTests().add(test);
                        }

                        return order;
                    };
                    if (search.getRangeType() == 2) {
                        getConnection().query(query.toString() + " " + from.toString(), mapper);
                    } else {

                        getConnection().query(query.toString() + "  " + from.toString() + "  ", mapper, params);
                    }
                }
            }
            return new ArrayList<>(listOrders.values());
        } catch (EmptyResultDataAccessException ex) {
            ResultsLog.error("Error listBasic: " + ex);
            return new ArrayList<>(0);
        }
    }
    
     /**
     * Lista las ordenes por rango de fecha o numero de orden desde base de
     * datos para el modulo de listados.
     *
     * @param search
     * @param demographics Lista de demograficos.
     * @param account
     * @param physician
     * @param rate
     * @param service
     * @param documenttype
     * @param race
     * @param price indica si se consultan los precios de los examenes
     *
     *
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    default List<OrderList> listBasicAppointment(Filter search, final List<Demographic> demographics, boolean account, boolean physician, boolean rate, boolean service, boolean race, boolean documenttype, Boolean price,  List<Integer> laboratorys, int idbranch) throws Exception {
        try {
            // Consulta de ordenes por historico:
            List<Integer> years = Tools.listOfConsecutiveYears(String.valueOf(search.getInit()), String.valueOf(search.getEnd()));
            String lab22;
            String lab57;
            int currentYear = DateTools.dateToNumberYear(new Date());

            HashMap<Long, OrderList> listOrders = new HashMap<>();

            for (Integer year : years) {
                lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
                lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;
             
                boolean tableExists = getToolsDao().tableExists(getConnection(), lab22);
                tableExists = tableExists ? getToolsDao().tableExists(getConnection(), lab57) : tableExists;
                if (tableExists) {
                    // Query
                    StringBuilder query = new StringBuilder();
                    query.append(ISOLATION_READ_UNCOMMITTED);
                    query.append("SELECT lab22.lab22c1, lab22c2, lab22c3, lab22c7, ");
                    query.append("lab22.lab103c1, lab103c2, lab103c3, ");
                    query.append("lab21.lab21c1, lab21c2, lab21c3, lab21c4, lab21c5, lab21c6, lab21c7, lab21c8, lab21c9, lab21c10, lab21c16, lab21c17, ");
                    query.append("lab21lab80.lab80c1 AS lab21lab80lab80c1, ");
                    query.append("lab21lab80.lab80c3 AS lab21lab80lab80c3, ");
                    query.append("lab21lab80.lab80c4 AS lab21lab80lab80c4, lab21lab80.lab80c5 AS lab21lab80lab80c5, ");
                    query.append("lab05.lab05c1, lab05c10, lab05c4, ");
                    query.append("userCreation.lab04c1 AS userCreationc1, userCreation.lab04c2 AS userCreationc2, userCreation.lab04c3 AS userCreationc3, userCreation.lab04c4 AS userCreationc4, ");
                    query.append("  lab57.lab57c14, lab57.lab57c15,      ");
                    query.append("lab39.lab39c1, lab39.lab39c2, lab39.lab39c3, lab39.lab39c4, lab39.lab39c6,lab39.lab39c37, lab39.lab39c27, lab39.lab39c26, ");
                    query.append("lab43c3, lab43.lab43c4, ");
                    query.append("lab39.lab24c1, lab24c2, lab24c9, ");
                    query.append("lab45.lab45c2, ");
                    query.append("lab56.lab56c2, ");
                    query.append("panel.lab39c1 AS panellab39c1, panel.lab39c2 AS panellab39c2, panel.lab39c3 AS panellab39c3, panel.lab39c4 AS panellab39c4, panel.lab39c37 AS panellab39c37, lab39.lab39c39 AS panellab39c39, ");
                    query.append("pack.lab39c1 AS packlab39c1, pack.lab39c2 AS packlab39c2, pack.lab39c3 AS packlab39c3, pack.lab39c4 AS packlab39c4, pack.lab39c37 AS packlab39c37,  lab39.lab39c39 AS packlab39c39, ");
                    query.append("lab40.lab40c2, lab40.lab40c3, ");
                    query.append("lab57.lab57c54, lab57.lab57c39, ");
                    query.append("hmb12.hmb12c1, hmb12.hmb12c2, ");
                    query.append("hmb12.hmb09c1, hmb09c2, hmb09c4,hmb09c5 ");
              
                    StringBuilder from = new StringBuilder();
                    from.append(" FROM ").append(lab57).append(" AS lab57 ");
                    from.append("INNER JOIN ").append(lab22).append(" AS lab22 ON lab22.lab22c1 = lab57.lab22c1 ");
                    from.append("INNER JOIN lab05 ON lab05.lab05c1 = lab22.lab05c1  ");
                    from.append("INNER JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1  ");
                    from.append("INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1  ");
                    from.append("INNER JOIN lab80 lab21lab80 ON lab21lab80.lab80c1 = lab21.lab80c1  ");
                    from.append("INNER JOIN lab103 ON lab103.lab103c1 = lab22.lab103c1  ");
                    from.append("INNER JOIN hmb12 on lab22.lab22c1 = hmb12.hmb12c5  ");
                    from.append("INNER JOIN hmb09 on hmb12.hmb09c1 = hmb09.hmb09c1 ");
                    from.append("LEFT JOIN lab04 userCreation ON userCreation.lab04c1 = lab22.lab04c1_1  ");
                    from.append("LEFT JOIN lab39 panel ON panel.lab39c1 = lab57.lab57c14 ");
                    from.append("LEFT JOIN lab39 pack ON pack.lab39c1 = lab57.lab57c15 ");
                    from.append("LEFT JOIN lab43 ON lab43.lab43c1 = lab39.lab43c1 ");
                    from.append("LEFT JOIN lab24 ON lab24.lab24c1 = lab39.lab24c1 ");
                    from.append("LEFT JOIN lab45 ON lab45.lab45c1 = lab39.lab45c1 ");
                    from.append("LEFT JOIN lab40 ON lab40.lab40c1 = lab57.lab40c1 ");
                    from.append("LEFT JOIN lab56 ON lab56.lab56c1 = lab24.lab56c1 ");
                    
                    if (documenttype) {
                        query.append(" ,lab21lab54.lab54c1 AS lab21lab54lab54c1,  lab21lab54.lab54c2 AS lab21lab54lab54c2, lab21lab54.lab54c3 AS lab21lab54lab54c3 ");
                        from.append("LEFT JOIN lab54 lab21lab54 ON lab21lab54.lab54c1 = lab21.lab54c1  ");
                    }
                    if (service) {
                        query.append(" , lab22.lab10c1, lab10c2, lab10c7 ");
                        from.append("LEFT JOIN lab10 ON lab10.lab10c1 = lab22.lab10c1  ");
                    }
                    if (account) {
                        query.append(" ,lab22.lab14c1,  lab14c2, lab14c3, lab14c32 ");
                        from.append("LEFT JOIN lab14 ON lab14.lab14c1 = lab22.lab14c1  ");
                    }
                    if (physician) {
                        query.append(" ,lab22.lab19c1,  lab19c2, lab19c3, lab19c21 ");
                        from.append("LEFT JOIN lab19 ON lab19.lab19c1 = lab22.lab19c1  ");
                    }
                    if (rate) {
                        query.append(" , lab22.lab904c1, lab904c2, lab904c3 ");
                        from.append("LEFT JOIN lab904 ON lab904.lab904c1 = lab22.lab904c1  ");
                    }
                    if (race) {
                        query.append(" , lab21lab08.lab08c1 AS lab21lab08lab08c1, lab21lab08.lab08c2 AS lab21lab08lab08c2, lab21lab08.lab08c5 AS lab21lab08lab08c5 ");
                        from.append("LEFT JOIN lab08 lab21lab08 ON lab21lab08.lab08c1 = lab21.lab08c1  ");
                    }
                 
                    for (Demographic demographic : demographics) {
                        query.append(Tools.createDemographicsQuery(demographic).get(0));
                        from.append(Tools.createDemographicsQuery(demographic).get(1));
                    }

                    Object[] params = null;
                    switch (search.getRangeType()) {
                        case 0:
                            from.append(" WHERE lab39.lab39c37 != 2 AND (lab57.lab57c34 BETWEEN ? AND ? )");
                            break;
                        case 1:
                            from.append(" WHERE lab39.lab39c37 != 2 AND (lab22.lab22c1 BETWEEN ? AND ? )");
                            break;
                        case 3:
                            from.append(" WHERE lab39.lab39c37 != 2 AND (lab22.lab22c2 BETWEEN ? AND ? ) ");
                            break;
                        default:
                            from.append(" WHERE lab39.lab39c37 != 2 AND lab22.lab22c1 IN(").append(search.getOrders().stream().map(order -> order.toString()).collect(Collectors.joining(","))).append(") ");
                            break;
                    }
                    
                    from.append(" AND hmb12c4 = ? ");

                    from.append(SQLTools.buildSQLLaboratoryFilter(laboratorys, idbranch));
                    from.append("  AND lab22.lab07C1 != 0 ");

                    params = new Object[]{
                        search.getInit(), search.getEnd(), search.getAppointmenttype()
                    };

                    RowMapper mapper = (RowMapper<OrderList>) (ResultSet rs, int i)
                            -> {
                        OrderList order = new OrderList();
                        if (!listOrders.containsKey(rs.getLong("lab22c1"))) {
                            order.setOrderNumber(rs.getLong("lab22c1"));

                            order.setCreatedDateShort(rs.getInt("lab22c2"));
                            order.setCreatedDate(rs.getTimestamp("lab22c3"));
                            order.setExternalId(rs.getString("lab22c7"));

                            order.getCreateUser().setId(rs.getInt("userCreationc1"));
                            order.getCreateUser().setName(rs.getString("userCreationc2"));
                            order.getCreateUser().setLastName(rs.getString("userCreationc3"));
                            order.getCreateUser().setUserName(rs.getString("userCreationc4"));

                            //TIPO DE ORDEN
                             
                            order.getType().setId(rs.getInt("lab103c1"));
                            order.getType().setCode(rs.getString("lab103c2"));
                            order.getType().setName(rs.getString("lab103c3"));

                            //PACIENTE
                            SuperPatient patient = new SuperPatient();
                            patient.setId(rs.getInt("lab21c1"));
                            patient.setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                            patient.setName1(Tools.decrypt(rs.getString("lab21c3")));
                            patient.setName2(Tools.decrypt(rs.getString("lab21c4")));
                            patient.setLastName(Tools.decrypt(rs.getString("lab21c5")));
                            patient.setSurName(Tools.decrypt(rs.getString("lab21c6")));
                            patient.setBirthday(rs.getTimestamp("lab21c7"));
                            patient.setEmail(rs.getString("lab21c8"));
                            patient.setSize(rs.getBigDecimal("lab21c9"));
                            patient.setWeight(rs.getBigDecimal("lab21c10"));
                            patient.setPhone(rs.getString("lab21c16"));
                            patient.setAddress(rs.getString("lab21c17"));

                            //PACIENTE - SEXO
                            patient.getSex().setId(rs.getInt("lab21lab80lab80c1"));
                            patient.getSex().setCode(rs.getString("lab21lab80lab80c3"));
                            patient.getSex().setEsCo(rs.getString("lab21lab80lab80c4"));
                            patient.getSex().setEnUsa(rs.getString("lab21lab80lab80c5"));

                            if (documenttype) {
                                //PACIENTE - TIPO DE DOCUMENTO
                                SuperDocumentType documenttypeLaboratory = new SuperDocumentType();
                                documenttypeLaboratory.setId(rs.getInt("lab21lab54lab54c1"));
                                documenttypeLaboratory.setAbbr(rs.getString("lab21lab54lab54c2"));
                                documenttypeLaboratory.setName(rs.getString("lab21lab54lab54c3"));
                                patient.setDocumentType(documenttypeLaboratory);
                            }

                            order.setPatient(patient);

                            //SEDE
                            SuperBranch branch = new SuperBranch();
                            branch.setId(rs.getInt("lab05c1"));
                            branch.setCode(rs.getString("lab05c10"));
                            branch.setName(rs.getString("lab05c4"));
                            order.setBranch(branch);
                            
                            order.getAppointment().setId(rs.getInt("hmb12c1"));
                            order.getAppointment().setDate(rs.getInt("hmb12c2"));
                            Shift bean = new Shift();
                            bean.setId(rs.getInt("hmb09c1"));
                            bean.setName(rs.getString("hmb09c2"));
                            bean.setInit(rs.getInt("hmb09c4"));
                            bean.setEnd(rs.getInt("hmb09c5"));

                            order.getAppointment().setShift(bean);

                            if (service) {
                                //SERVICIO
                                SuperService serviceLaboratory = new SuperService();
                                serviceLaboratory.setId(rs.getInt("lab10c1"));
                                serviceLaboratory.setCode(rs.getString("lab10c7"));
                                serviceLaboratory.setName(rs.getString("lab10c2"));
                                order.setService(serviceLaboratory);
                            }

                            if (account) {
                                //EMPRESA
                                SuperAccount accountLaboratory = new SuperAccount();
                                accountLaboratory.setNit(rs.getString("lab14c2"));
                                accountLaboratory.setId(rs.getInt("lab14c1"));
                                accountLaboratory.setName(rs.getString("lab14c3"));
                                accountLaboratory.setEncryptionReportResult(rs.getBoolean("lab14c32"));
                                order.setAccount(accountLaboratory);
                            }

                            if (physician) {
                                //MEDICO
                                SuperPhysician physicianLaboratory = new SuperPhysician();
                                physicianLaboratory.setId(rs.getInt("lab19c1"));
                                physicianLaboratory.setName(rs.getString("lab19c2"));
                                physicianLaboratory.setName(rs.getString("lab19c2"));
                                physicianLaboratory.setEmail(rs.getString("lab19c21"));
                                order.setPhysician(physicianLaboratory);
                            }
                            if (rate) {
                                //TARIFA
                                SuperRate rateLaboratory = new SuperRate();
                                rateLaboratory.setId(rs.getInt("lab904c1"));
                                rateLaboratory.setCode(rs.getString("lab904c2"));
                                rateLaboratory.setName(rs.getString("lab904c3"));
                                order.setRate(rateLaboratory);
                            }
                            if (race) {
                                //PACIENTE - RAZA
                                SuperRace racePatient = new SuperRace();
                                racePatient.setId(rs.getInt("lab21lab08lab08c1"));
                                racePatient.setCode(rs.getString("lab21lab08lab08c5"));
                                racePatient.setName(rs.getString("lab21lab08lab08c2"));
                                order.getPatient().setRace(racePatient);
                            }

                            List<DemographicValue> demographicsOrder = new LinkedList<>();

                            for (Demographic demographic : demographics) {

                                String[] data;
                                if (demographic.isEncoded()) {
                                    data = new String[]{
                                        rs.getString("demo" + demographic.getId() + "_id"),
                                        rs.getString("demo" + demographic.getId() + "_code"),
                                        rs.getString("demo" + demographic.getId() + "_name")
                                    };
                                } else {
                                    data = new String[]{
                                        rs.getString("lab_demo_" + demographic.getId())
                                    };
                                }
                                demographicsOrder.add(Tools.getDemographicsValue(demographic, data));

                            }
                            order.setAllDemographics(demographicsOrder);
                            listOrders.put(order.getOrderNumber(), order);

                        } else {
                            order = listOrders.get(rs.getLong("lab22c1"));
                        }

                        TestList test = new TestList();
                        test.setId(rs.getInt("lab39c1"));
                        test.setCode(rs.getString("lab39c2"));
                        test.setAbbr(rs.getString("lab39c3"));
                        test.setName(rs.getString("lab39c4"));
                        test.setViewquery(rs.getInt("lab39c26"));
                        test.setTestType(rs.getShort("lab39c37"));
                        test.setConfidential(rs.getInt("lab39c27") == 1);
                        test.setGender(new Item(rs.getInt("lab39c6")));
                        test.setRemission(rs.getInt("lab57c54"));
                        test.setProfile(rs.getInt("lab57c14"));
                        test.getLaboratory().setCode(rs.getInt("lab40c2"));
                        test.getLaboratory().setName(rs.getString("lab40c3"));
                        test.setExcluideTestProfile(rs.getInt("panellab39c39"));

                        if (rs.getString("panellab39c1") != null) {
                            SuperTest panel = new SuperTest();
                            panel.setId(rs.getInt("panellab39c1"));
                            panel.setCode(rs.getString("panellab39c2"));
                            panel.setAbbr(rs.getString("panellab39c3"));
                            panel.setName(rs.getString("panellab39c4"));
                            panel.setTestType(rs.getShort("panellab39c37"));
                            test.setPanel(panel);
                        }

                        if (rs.getString("packlab39c1") != null) {
                            SuperTest pack = new SuperTest();
                            pack.setId(rs.getInt("packlab39c1"));
                            pack.setCode(rs.getString("packlab39c2"));
                            pack.setAbbr(rs.getString("packlab39c3"));
                            pack.setName(rs.getString("packlab39c4"));
                            pack.setTestType(rs.getShort("packlab39c37"));
                            test.setPack(pack);
                        }

                        test.getArea().setAbbreviation(rs.getString("lab43c3"));
                        test.getArea().setName(rs.getString("lab43c4"));

                        test.getSample().setId(rs.getInt("lab24c1"));
                        test.getSample().setName(rs.getString("lab24c2"));
                        test.getSample().setCodesample(rs.getString("lab24c9"));

                        SuperContainer container = new SuperContainer();
                        container.setName(rs.getString("lab56c2"));
                        test.getSample().setContainer(container);
                        test.getSample().setTakeDate(rs.getTimestamp("lab57c39"));

                 
                        test.getUnit().setName(rs.getString("lab45c2"));

                        if (!listOrders.get(order.getOrderNumber()).getTests().contains(test)) {
                            listOrders.get(order.getOrderNumber()).getTests().add(test);
                        }

                        return order;
                    };
                    if (search.getRangeType() == 2) {
                        getConnection().query(query.toString() + " " + from.toString(), mapper);
                    } else {

                        getConnection().query(query.toString() + "  " + from.toString() + "  ", mapper, params);
                    }
                }
            }
            return new ArrayList<>(listOrders.values());
        } catch (EmptyResultDataAccessException ex) {
            ResultsLog.error("Error listBasic: " + ex);
            return new ArrayList<>(0);
        }
    }

    /**
     * Lista las ordenes por rango de fecha o numero de orden desde base de
     * datos para el modulo de listados.
     *
     * @param search
     * @param demographics Lista de demograficos.
     * @param account
     * @param physician
     * @param rate
     * @param service
     * @param documenttype
     * @param race
     *
     *
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    default List<Long> getOrdersbyFilter(Filter search, final List<Demographic> demographics, boolean account, boolean physician, boolean rate, boolean service, boolean race, boolean documenttype) throws Exception {
        try {
            // Consulta de ordenes por historico:
            List<Integer> years = Tools.listOfConsecutiveYears(String.valueOf(search.getInit()), String.valueOf(search.getEnd()));
            String lab22;
            String lab57;
            int currentYear = DateTools.dateToNumberYear(new Date());

            long time1 = System.currentTimeMillis();
            List<Long> listOrdersString = new LinkedList<>();

            for (Integer year : years) {
                lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
                lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;

                boolean tableExists = getToolsDao().tableExists(getConnection(), lab57);
                tableExists = tableExists ? getToolsDao().tableExists(getConnection(), lab22) : tableExists;
                if (tableExists) {
                    // Query
                    StringBuilder query = new StringBuilder();
                    query.append(ISOLATION_READ_UNCOMMITTED);
                    query.append("SELECT DISTINCT lab22.lab22c1 ");
                    StringBuilder from = new StringBuilder();
                    from.append(" FROM ").append(lab57).append(" AS lab57 ");
                    from.append("INNER JOIN ").append(lab22).append(" AS lab22 ON lab22.lab22c1 = lab57.lab22c1 ");
                    from.append("INNER JOIN lab05 ON lab05.lab05c1 = lab22.lab05c1  ");
                    from.append("INNER JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1  ");
                    from.append("INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1  ");
                    from.append("INNER JOIN lab80 lab21lab80 ON lab21lab80.lab80c1 = lab21.lab80c1  ");
                    from.append("INNER JOIN lab103 ON lab103.lab103c1 = lab22.lab103c1  ");
                    from.append("LEFT JOIN lab04 ON lab04.lab04c1 = lab22.lab04c1  ");
                    from.append("LEFT JOIN lab39 panel ON panel.lab39c1 = lab57.lab57c14 ");
                    from.append("LEFT JOIN lab39 pack ON pack.lab39c1 = lab57.lab57c15 ");
                    from.append("LEFT JOIN lab43 ON lab43.lab43c1 = lab39.lab43c1 ");
                    from.append("LEFT JOIN lab24 ON lab24.lab24c1 = lab39.lab24c1 ");
                    from.append("LEFT JOIN lab45 ON lab45.lab45c1 = lab39.lab45c1 ");
                    from.append("LEFT JOIN lab40 ON lab40.lab40c1 = lab57.lab40c1 ");
                    from.append("LEFT JOIN lab56 ON lab56.lab56c1 = lab24.lab56c1 ");
                    from.append("LEFT JOIN lab11 ON lab11.lab24c1 = lab57.lab24c1 AND lab11.lab22c1 = lab57.lab22c1 ");
                    from.append("LEFT JOIN lab16 ON lab16.lab16c1 = lab11.lab16c1 ");

                    if (documenttype) {

                        from.append("LEFT JOIN lab54 lab21lab54 ON lab21lab54.lab54c1 = lab21.lab54c1  ");
                    }
                    if (service) {

                        from.append("LEFT JOIN lab10 ON lab10.lab10c1 = lab22.lab10c1  ");
                    }
                    if (account) {

                        from.append("LEFT JOIN lab14 ON lab14.lab14c1 = lab22.lab14c1  ");
                    }
                    if (physician) {

                        from.append("LEFT JOIN lab19 ON lab19.lab19c1 = lab22.lab19c1  ");
                    }
                    if (rate) {

                        from.append("LEFT JOIN lab904 ON lab904.lab904c1 = lab22.lab904c1  ");
                    }
                    if (race) {

                        from.append("LEFT JOIN lab08 lab21lab08 ON lab21lab08.lab08c1 = lab21.lab08c1  ");
                    }

                    for (Demographic demographic : demographics) {
                        query.append(Tools.createDemographicsQuery(demographic).get(0));
                        from.append(Tools.createDemographicsQuery(demographic).get(1));
                    }

                    Object[] params = null;
                    switch (search.getRangeType()) {
                        case 0:
                            from.append(" WHERE lab39.lab39c37 != 2 AND (lab57.lab57c34 BETWEEN ? AND ? )");
                            break;
                        case 1:
                            from.append(" WHERE lab39.lab39c37 != 2 AND (lab22.lab22c1 BETWEEN ? AND ? )");
                            break;
                        case 3:
                            from.append(" WHERE lab39.lab39c37 != 2 AND (lab22.lab22c2 BETWEEN ? AND ? ) ");
                            break;
                        default:
                            from.append(" WHERE lab39.lab39c37 != 2 AND lab22.lab22c1 IN(").append(search.getOrders().stream().map(order -> order.toString()).collect(Collectors.joining(","))).append(") ");
                            break;
                    }

                    // Filtro por tipo de orden 
                    if (search.getOrderType() != 0) {
                        from.append(" AND lab22.lab103c1 == ").append(search.getOrderType());
                    }

                    //Filtro por examenes confidenciales y areas
                    switch (search.getTestFilterType()) {
                        case 1:
                            from.append(" AND lab39.lab43c1 IN (").append(search.getTests().stream().map(area -> area.toString()).collect(Collectors.joining(","))).append(") ");
                            break;
                        case 2:
                            String listtest = search.getTests().stream().map(test -> test.toString()).collect(Collectors.joining(","));
                            from.append(" AND ((lab57.lab57c14 IN (").append(listtest).append(")) or lab39.lab39c1 IN (").append(listtest).append(")) ");
                            break;
                        case 3:
                            String listtestconfidencial = search.getTests().stream().map(test -> test.toString()).collect(Collectors.joining(","));
                            from.append(" AND ((lab57.lab57c14 IN (").append(listtestconfidencial).append(")) or lab39.lab39c1 IN (").append(listtestconfidencial).append(")) ");
                            break;
                        default:
                            break;
                    }

                    // Filtro por laboratorio 
                    if (search.getLaboratory() != 0) {
                        from.append(" AND lab57.lab40c1 = ").append(search.getLaboratory());
                    }

                    //Filtro por descripcion de paquetes
                    if (search.isPackageDescription()) {
                        from.append(" AND lab57.lab57c15 is not null");
                    }

                    if (!search.getTestState().isEmpty() && !search.getSampleState().isEmpty()) {
                        if (search.isGroupProfiles()) {
                            from.append("  AND (lab39.lab39c37 = 1 or lab57.lab57c8 IN(").append(search.getTestState().stream().map(t -> t.toString()).collect(Collectors.joining(","))).append(") "
                                    + " AND ((lab57.lab57c16 IN(").append(search.getSampleState().stream().map(t -> t.toString()).collect(Collectors.joining(","))).append("))  )  )");
                        } else {
                            from.append("  AND ( lab39.lab39c37 = 0 and lab57.lab57c8 IN(").append(search.getTestState().stream().map(t -> t.toString()).collect(Collectors.joining(","))).append(") "
                                    + " AND ((lab57.lab57c16 IN(").append(search.getSampleState().stream().map(t -> t.toString()).collect(Collectors.joining(","))).append("))  )  )");
                        }
                    } else if (!search.getTestState().isEmpty()) {
                        if (search.isGroupProfiles()) {
                            from.append(" AND ( lab39.lab39c37 = 1 or lab57.lab57c8 IN(").append(search.getTestState().stream().map(t -> t.toString()).collect(Collectors.joining(","))).append(")) ");
                        } else {
                            from.append(" AND ( lab39.lab39c37 = 0 and lab57.lab57c8 IN(").append(search.getTestState().stream().map(t -> t.toString()).collect(Collectors.joining(","))).append(")) ");
                        }
                    } else if (!search.getSampleState().isEmpty()) {
                        if (search.isGroupProfiles()) {
                            from.append(" AND ( lab39.lab39c37 = 1 or lab57.lab57c16 IN(").append(search.getSampleState().stream().map(t -> t.toString()).collect(Collectors.joining(","))).append(") )");
                        } else {
                            from.append(" AND ( lab57.lab57c16 IN(").append(search.getSampleState().stream().map(t -> t.toString()).collect(Collectors.joining(","))).append(") )");
                        }
                    }
                    if (search.getResultState() != null) {
                        switch (search.getResultState().get(0)) {
                            case 1: //modificado
                                from.append("  ");
                                break;
                            case 2://repeticion
                                from.append(" AND (lab57.lab24c1 is null or lab57.lab57c48 is not null) ");
                                break;
                            case 3://patologico
                                if (search.isGroupProfiles()) {
                                    from.append(" AND (lab39.lab39c37 = 1 or lab57.lab57c9 IN(1,2,3))");
                                } else {
                                    from.append(" AND lab57.lab57c9 IN(1,2,3)");
                                }
                                break;
                            case 4://panico
                                if (search.isGroupProfiles()) {
                                    from.append(" AND ( lab39.lab39c37 = 1 or lab57.lab57c9 IN(4,5,6))");
                                } else {
                                    from.append(" AND lab57.lab57c9 IN(4,5,6)");
                                }
                                break;
                            case 5://deltacheck
                                from.append(" ");
                                break;
                            default:
                                break;
                        }
                    }

                    //filtro por estado de la muestra.
                    switch (search.getCheck()) {
                        case 1:
                            from.append(" AND lab57.lab57c16 = ").append(LISEnum.ResultSampleState.CHECKED.getValue());
                            break;
                        case 2:
                            from.append(" AND (lab57.lab24c1 is null or lab57.lab57c16 < ").append(LISEnum.ResultSampleState.CHECKED.getValue()).append(" ) ");
                            break;
                        default:
                            break;
                    }

                    from.append("  AND lab22.lab07C1 != 0  AND (lab22c19 = 0 or lab22c19 is null) and lab21.lab21c1 != 0");

                    params = new Object[]{
                        search.getInit(), search.getEnd()
                    };

                    long time = System.currentTimeMillis();
                    RowMapper mapper = (RowMapper<List<Long>>) (ResultSet rs, int i)
                            -> {

                        listOrdersString.add(rs.getLong("lab22c1"));
                        return listOrdersString;
                    };

                    time1 = System.currentTimeMillis();
                    if (search.getRangeType() == 2) {
                        getConnection().query(query.toString() + " " + from.toString(), mapper);
                    } else {

                        getConnection().query(query.toString() + "  " + from.toString() + "  ", mapper, params);
                    }
                }
            }
            OrderCreationLog.info("Se demora serializando: " + (System.currentTimeMillis() - time1));
            return listOrdersString;
        } catch (EmptyResultDataAccessException ex) {
            return new ArrayList<>(0);
        }
    }

    /**
     * Lista las ordenes por rango de fecha o numero de orden desde base de
     * datos.
     *
     * @param vInitial Rango Inicial.
     * @param vFinal Rango Final.
     * @param searchType Indica si la consulta se hace por rango de orden(1),
     * fechas(0) o Listado de ordenes(2)
     * @param demographics Lista de demograficos.
     * @param orders lista de ordenes a consultar searchType == 2
     * @param tests Lista de Examenes
     * @param idUser
     *
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    default List<Order> list(Long vInitial, Long vFinal, int searchType, final List<Demographic> demographics, List<Long> orders, List<Integer> tests, Integer idUser) throws Exception {

        try {
            StringBuilder query = new StringBuilder();
            query.append("SET NOCOUNT ON ");
            query.append("SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED ");
            query.append("SELECT lab22.lab22c1, lab22c2, lab22c3, lab22c4, lab22c5, lab22.lab07c1, lab22c7, lab22c9, lab22c10, ");
            query.append("lab21.lab21c1, lab21c2, lab21c3, lab21c4, lab21c5, lab21c6, lab21lab80.lab80c1 AS lab21lab80lab80c1, lab21lab80.lab80c2 AS lab21lab80lab80c2, lab21lab80.lab80c3 AS lab21lab80lab80c3, lab21lab80.lab80c4 AS lab21lab80lab80c4, lab21lab80.lab80c5 AS lab21lab80lab80c5, lab21c7, lab21c8, lab21c9, lab21c10, lab21c11, lab21c16, lab21c17, lab21lab08.lab08c1 AS lab21lab08lab08c1, lab21lab08.lab08c2 AS lab21lab08lab08c2, lab21lab08.lab08c4 AS lab21lab08lab08c4, lab21lab08.lab08c5 AS lab21lab08lab08c5, lab21lab54.lab54c1 AS lab21lab54lab54c1, lab21lab54.lab54c2 AS lab21lab54lab54c2, lab21lab54.lab54c3 AS lab21lab54lab54c3, ");
            query.append("lab05.lab05c1, lab05c10, lab05c4, ");
            query.append("lab22.lab04c1, lab04c2, lab04c3, lab04c4 ");
            query.append(", demo17.lab63c1 as demo17_id ");
            query.append(", demo17.lab63c2 as demo17_code ");
            query.append(", demo17.lab63c3 as demo17_name ");
            query.append(", demo16.lab63c1 as demo16_id ");
            query.append(", demo16.lab63c2 as demo16_code ");
            query.append(", demo16.lab63c3 as demo16_name ");
            query.append(", demo11.lab63c1 as demo11_id ");
            query.append(", demo11.lab63c2 as demo11_code ");
            query.append(", demo11.lab63c3 as demo11_name ");
            query.append(", demo12.lab63c1 as demo12_id ");
            query.append(", demo12.lab63c2 as demo12_code ");
            query.append(", demo12.lab63c3 as demo12_name ");
            StringBuilder from = new StringBuilder();
            from.append(" FROM lab57 ");
            from.append("INNER JOIN lab22 ON lab22.lab22c1 = lab57.lab22c1 ");
            from.append("INNER JOIN lab05 ON lab05.lab05c1 = lab22.lab05c1  ");
            from.append("INNER JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1  ");
            from.append("INNER JOIN lab80 lab21lab80 ON lab21lab80.lab80c1 = lab21.lab80c1  ");
            from.append("INNER JOIN lab93 ON lab93.lab05c1 = lab22.lab05c1 and lab93.lab04c1 = ").append(idUser).append(" ");
            from.append("LEFT JOIN lab08 lab21lab08 ON lab21lab08.lab08c1 = lab21.lab08c1  ");
            from.append("LEFT JOIN lab54 lab21lab54 ON lab21lab54.lab54c1 = lab21.lab54c1  ");
            from.append("LEFT JOIN lab04 ON lab04.lab04c1 = lab22.lab04c1  ");
            from.append(" LEFT JOIN Lab63 demo17 ON lab22.lab_demo_17 = demo17.lab63c1 ");
            from.append(" LEFT JOIN Lab63 demo16 ON lab22.lab_demo_16 = demo16.lab63c1 ");
            from.append(" LEFT JOIN Lab63 demo11 ON lab22.lab_demo_11 = demo11.lab63c1 ");
            from.append(" LEFT JOIN Lab63 demo12 ON lab22.lab_demo_12 = demo12.lab63c1 ");

            Object[] params = null;
            switch (searchType) {
                case 0:
                    from.append(" WHERE lab22.lab22c2 BETWEEN ? AND ? ");
                    break;
                case 1:
                    from.append(" WHERE lab22.lab22c1 BETWEEN ? AND ? ");
                    break;
                case 3:
                    from.append(" WHERE lab22.lab22c2 BETWEEN ? AND ? ");
                    break;
                default:
                    from.append(" WHERE lab22.lab22c1 IN(").append(orders.stream().map(order -> order.toString()).collect(Collectors.joining(","))).append(") ");
                    break;
            }
            
            from.append(" AND (lab22c19 = 0 or lab22c19 is null)");
            
            params = new Object[]{
                vInitial, vFinal
            };
            List<Order> listOrders = new ArrayList<>();
            RowMapper mapper = (RowMapper<Order>) (ResultSet rs, int i)
                    -> {
                Order order = new Order();
                order.setOrderNumber(rs.getLong("lab22c1"));
                if (listOrders.contains(order)) {
                } else {
                    order.setCreatedDateShort(rs.getInt("lab22c2"));
                    order.setCreatedDate(rs.getTimestamp("lab22c3"));
                    order.setInconsistency(rs.getInt("lab22c10") == 1);
                    order.setHomebound(rs.getInt("lab22c4") == 1);
                    order.setMiles(rs.getInt("lab22c5"));
                    order.setExternalId(rs.getString("lab22c7"));
                    order.setState(rs.getInt("lab07c1"));
                    order.setPreviousState(rs.getString("lab22c9") == null ? null : rs.getInt("lab22c9"));
                    order.getLastUpdateUser().setId(rs.getInt("lab04c1"));
                    order.getLastUpdateUser().setName(rs.getString("lab04c2"));
                    order.getLastUpdateUser().setLastName(rs.getString("lab04c3"));
                    order.getLastUpdateUser().setUserName(rs.getString("lab04c4"));

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
                    order.getPatient().getRace().setCode(rs.getString("lab21lab08lab08c5"));
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

                    DemographicValue demoValue17 = new DemographicValue();
                    demoValue17.setIdDemographic(17);
                    demoValue17.setEncoded(true);
                    demoValue17.setCodifiedId(rs.getInt("demo17_id"));
                    demoValue17.setCodifiedCode(rs.getString("demo17_code"));
                    demoValue17.setCodifiedName(rs.getString("demo17_name"));

                    order.getDemographics().add(demoValue17);

                    DemographicValue demoValue16 = new DemographicValue();
                    demoValue16.setIdDemographic(16);
                    demoValue16.setCodifiedId(rs.getInt("demo16_id"));
                    demoValue16.setCodifiedCode(rs.getString("demo16_code"));
                    demoValue16.setCodifiedName(rs.getString("demo16_name"));
                    demoValue16.setEncoded(true);

                    order.getDemographics().add(demoValue16);

                    DemographicValue demoValue11 = new DemographicValue();
                    demoValue11.setIdDemographic(11);
                    demoValue11.setCodifiedId(rs.getInt("demo11_id"));
                    demoValue11.setCodifiedCode(rs.getString("demo11_code"));
                    demoValue11.setCodifiedName(rs.getString("demo11_name"));
                    demoValue11.setEncoded(true);
                    order.getDemographics().add(demoValue11);

                    DemographicValue demoValue12 = new DemographicValue();
                    demoValue12.setIdDemographic(12);
                    demoValue12.setCodifiedId(rs.getInt("demo12_id"));
                    demoValue12.setCodifiedCode(rs.getString("demo12_code"));
                    demoValue12.setCodifiedName(rs.getString("demo12_name"));
                    demoValue12.setEncoded(true);
                    order.getDemographics().add(demoValue12);

                    listOrders.add(order);
                }
                return order;
            };
            if (searchType == 2) {
                getConnection().query(query.toString() + " " + from.toString(), mapper);
            } else {
                getConnection().query(query.toString() + "  " + from.toString() + "  ", mapper, params);
            }
            return listOrders;
        } catch (EmptyResultDataAccessException ex) {
            return new ArrayList<>(0);
        }
    }

    /**
     * Lista las ordenes por rango de fecha o numero de orden desde base de
     * datos.
     *
     * @param vInitial Rango Inicial.
     * @param vFinal Rango Final.
     * @param searchType Indica si la consulta se hace por rango de orden(1),
     * fechas(0) o Listado de ordenes(2)
     * @param demographics Lista de demograficos.
     * @param orders lista de ordenes a consultar searchType == 2
     * @param tests Lista de Examenes
     *
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    default List<Order> list(Long vInitial, Long vFinal, int searchType, final List<Demographic> demographics, List<Long> orders, List<Integer> tests, int check, int remission,  List<Integer> laboratorys, int branch) throws Exception {
        try {
            List<Order> listOrders = new LinkedList<>();
            // Consulta de ordenes por historico:
            List<Integer> years = Tools.listOfConsecutiveYears(String.valueOf(vInitial), String.valueOf(vFinal));
            String lab22;
            String lab57;
            int currentYear = DateTools.dateToNumberYear(new Date());
            for (Integer year : years) {
                lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
                lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;
                boolean tableExists = getToolsDao().tableExists(getConnection(), lab22);
                tableExists = tableExists ? getToolsDao().tableExists(getConnection(), lab57) : tableExists;
                if (tableExists) {
                    // Query
                    StringBuilder query = new StringBuilder();
                    query.append(ISOLATION_READ_UNCOMMITTED);
                    query.append("SELECT lab22.lab22c1, lab22c2, lab22c3, lab22c4, lab22c5, lab22.lab07c1, lab22c7, lab22c9, lab22c10, ");
                    query.append("lab103.lab103c1, lab103c2, lab103c3, lab103c4, lab103.lab07c1 AS lab103lab07c1,  ");
                    query.append("lab21.lab21c1, lab21c2, lab21c3, lab21c4, lab21c5, lab21c6, lab21lab80.lab80c1 AS lab21lab80lab80c1, lab21lab80.lab80c2 AS lab21lab80lab80c2, lab21lab80.lab80c3 AS lab21lab80lab80c3, lab21lab80.lab80c4 AS lab21lab80lab80c4, lab21lab80.lab80c5 AS lab21lab80lab80c5, lab21c7, lab21c8, lab21c9, lab21c10, lab21c11, lab21c16, lab21c17, lab21lab08.lab08c1 AS lab21lab08lab08c1, lab21lab08.lab08c2 AS lab21lab08lab08c2, lab21lab08.lab08c4 AS lab21lab08lab08c4, lab21lab08.lab08c5 AS lab21lab08lab08c5, lab21lab54.lab54c1 AS lab21lab54lab54c1, lab21lab54.lab54c2 AS lab21lab54lab54c2, lab21lab54.lab54c3 AS lab21lab54lab54c3, ");
                    query.append("lab05.lab05c1, lab05c10, lab05c4, ");
                    query.append("lab10.lab10c1, lab10c2, lab10c7,  ");
                    query.append("lab14.lab14c1, lab14c2, lab14c3, lab14c32, ");
                    query.append("lab19.lab19c1, lab19c2, lab19c3, lab19c21, ");
                    query.append("lab22.lab04c1, lab04c2, lab04c3, lab04c4, ");
                    query.append("lab904.lab904c1, lab904c2, lab904c3, ");
                    query.append("lab57.lab22c1, lab57.lab57c1, lab57.lab57c2, lab57.lab57c8, lab57.lab57c14, lab57.lab57c15, lab57c16, lab57.lab57c56, ");
                    query.append("lab39.lab39c1, lab39.lab39c2, lab39.lab39c3, lab39.lab39c4, lab39.lab39c6,lab39.lab39c7,lab39.lab39c8, lab39.lab39c9, lab39.lab39c37, lab39.lab39c27, lab39.lab39c56, ");
                    query.append("lab43.lab43c1, lab43c3, lab43.lab43c4, ");
                    query.append("lab24.lab24c1, lab24c2, lab24c4, lab24c9, lab24.lab24c10, lab24c6, ");
                    query.append("lab45.lab45c1, lab45.lab45c2, ");
                    query.append("lab24.lab56c1, lab56.lab56c2, ");
                    query.append("panel.lab39c1 AS panellab39c1, panel.lab39c2 AS panellab39c2, panel.lab39c3 AS panellab39c3, panel.lab39c4 AS panellab39c4, panel.lab39c37 AS panellab39c37, panel.lab39c6 AS panellab39c6,panel.lab39c7 AS panellab39c7, panel.lab39c8 AS panellab39c8, panel.lab39c9 AS panellab39c9, lab39.lab39c39 AS panellab39c39, ");
                    query.append("pack.lab39c1 AS packlab39c1, pack.lab39c2 AS packlab39c2, pack.lab39c3 AS packlab39c3, pack.lab39c4 AS packlab39c4, pack.lab39c37 AS packlab39c37,  pack.lab39c6 AS packlab39c6, pack.lab39c7 AS packlab39c7, pack.lab39c8 AS packlab39c8, pack.lab39c9 AS packlab39c9, lab39.lab39c39 AS packlab39c39, ");
                    query.append("lab57.lab40c1, lab40.lab40c2, lab40.lab40c3, ");
                    query.append("lab57.lab40c1a as idorigin, origin.lab40c2 as codeorigin, origin.lab40c3 as nameorigin, ");
                    query.append("lab16.lab16c3, lab11.lab11c1,");
                    query.append("lab32.lab37c1, lab32.lab32c1, ");
                    query.append("lab57.lab57c54, lab57.lab57c39 ");
                    StringBuilder from = new StringBuilder();
                    from.append(" FROM ").append(lab57).append(" AS lab57 ");
                    from.append("INNER JOIN ").append(lab22).append(" AS lab22 ON lab22.lab22c1 = lab57.lab22c1 ");
                    from.append("INNER JOIN lab05 ON lab05.lab05c1 = lab22.lab05c1  ");
                    from.append("INNER JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1  ");
                    from.append("INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1 AND lab39.lab39c26 = 1");
                    from.append("INNER JOIN lab80 lab21lab80 ON lab21lab80.lab80c1 = lab21.lab80c1  ");
                    from.append("INNER JOIN lab103 ON lab103.lab103c1 = lab22.lab103c1  ");
                    from.append("LEFT JOIN lab08 lab21lab08 ON lab21lab08.lab08c1 = lab21.lab08c1  ");
                    from.append("LEFT JOIN lab54 lab21lab54 ON lab21lab54.lab54c1 = lab21.lab54c1  ");
                    from.append("LEFT JOIN lab10 ON lab10.lab10c1 = lab22.lab10c1  ");
                    from.append("LEFT JOIN lab14 ON lab14.lab14c1 = lab22.lab14c1  ");
                    from.append("LEFT JOIN lab19 ON lab19.lab19c1 = lab22.lab19c1  ");
                    from.append("LEFT JOIN lab04 ON lab04.lab04c1 = lab22.lab04c1  ");
                    from.append("LEFT JOIN lab904 ON lab904.lab904c1 = lab22.lab904c1  ");
                    from.append("LEFT JOIN lab39 panel ON panel.lab39c1 = lab57.lab57c14 ");
                    from.append("LEFT JOIN lab39 pack ON pack.lab39c1 = lab57.lab57c15 ");
                    from.append("LEFT JOIN lab43 ON lab43.lab43c1 = lab39.lab43c1 ");
                    from.append("LEFT JOIN lab24 ON lab24.lab24c1 = lab39.lab24c1 ");
                    from.append("LEFT JOIN lab45 ON lab45.lab45c1 = lab39.lab45c1 ");
                    from.append("LEFT JOIN lab40 ON lab40.lab40c1 = lab57.lab40c1 ");
                    from.append("LEFT JOIN lab56 ON lab56.lab56c1 = lab24.lab56c1 ");
                    from.append("LEFT JOIN lab11 ON lab11.lab24c1 = lab57.lab24c1 AND lab11.lab22c1 = lab57.lab22c1 ");
                    from.append("LEFT JOIN lab16 ON lab16.lab16c1 = lab11.lab16c1 ");
                    from.append("LEFT JOIN lab32 ON lab32.lab22c1 = lab57.lab22c1 AND lab32.lab39c1 = lab57.lab39c1 ");
                    from.append("LEFT JOIN lab40 origin ON origin.lab40c1 = lab57.lab40c1a ");

                    for (Demographic demographic : demographics) {
                        if (demographic.getOrigin().equals("O")) {
                            if (demographic.isEncoded()) {
                                query.append(", demo").append(demographic.getId()).append(".lab63c1 as demo").append(demographic.getId()).append("_id ");
                                query.append(", demo").append(demographic.getId()).append(".lab63c2 as demo").append(demographic.getId()).append("_code ");
                                query.append(", demo").append(demographic.getId()).append(".lab63c3 as demo").append(demographic.getId()).append("_name ");

                                from.append(" LEFT JOIN Lab63 demo").append(demographic.getId()).append(" ON lab22.lab_demo_").append(demographic.getId()).append(" = demo").append(demographic.getId()).append(".lab63c1 ");
                            } else {
                                query.append(", lab22.lab_demo_").append(demographic.getId());
                            }
                        } else {
                            if (demographic.getOrigin().equals("H")) {
                                if (demographic.isEncoded()) {
                                    query.append(", demo").append(demographic.getId()).append(".lab63c1 as demo").append(demographic.getId()).append("_id ");
                                    query.append(", demo").append(demographic.getId()).append(".lab63c2 as demo").append(demographic.getId()).append("_code ");
                                    query.append(", demo").append(demographic.getId()).append(".lab63c3 as demo").append(demographic.getId()).append("_name ");

                                    from.append(" LEFT JOIN Lab63 demo").append(demographic.getId()).append(" ON lab21.lab_demo_").append(demographic.getId()).append(" = demo").append(demographic.getId()).append(".lab63c1 ");
                                } else {
                                    query.append(", lab21.lab_demo_").append(demographic.getId());
                                }
                            }
                        }
                    }
                    Object[] params = null;
                    switch (searchType) {
                        case 0:
                            from.append(" WHERE lab39.lab39c37 != 2 AND lab57.lab57c34 BETWEEN ? AND ? ");
                            break;
                        case 1:
                            from.append(" WHERE lab39.lab39c37 != 2 AND lab22.lab22c1 BETWEEN ? AND ? ");
                            break;
                        case 3:
                            from.append(" WHERE lab39.lab39c37 != 2 AND lab22.lab22c2 BETWEEN ? AND ? ");
                            break;
                        default:
                            from.append(" WHERE lab39.lab39c37 != 2 AND lab22.lab22c1 IN(").append(orders.stream().map(order -> order.toString()).collect(Collectors.joining(","))).append(") ");
                            break;
                    }

                    if (tests != null && !tests.isEmpty()) {
                        if (tests.size() == 1) {
                            from.append(" AND lab39.lab39c1 = ").append(tests.get(0));
                        } else {
                            from.append(" AND lab39.lab39c1 IN (").append(tests.stream().map(test -> test.toString()).collect(Collectors.joining(","))).append(") ");
                        }
                    }

                    //filtro por estado de la muestra.
                    switch (check) {
                        case 1:
                            from.append(" AND lab57.lab57c16 = ").append(LISEnum.ResultSampleState.CHECKED.getValue());
                            break;
                        case 2:
                            from.append(" AND lab57.lab57c16 != ").append(LISEnum.ResultSampleState.CHECKED.getValue());
                            break;
                        default:
                            break;
                    }

                    if (remission == 1) {
                        from.append(" AND lab57.lab57C54 is null ");
                    }
                    
                    from.append(SQLTools.buildSQLLaboratoryFilter(laboratorys, branch));
                    from.append(" AND lab22.lab07C1 != 0 AND (lab22c19 = 0 or lab22c19 is null) ");

                    params = new Object[]{
                        vInitial, vFinal
                    };

                    RowMapper mapper = (RowMapper<Order>) (ResultSet rs, int i)
                            -> {
                        Order order = new Order();
                        order.setOrderNumber(rs.getLong("lab22c1"));
                        if (listOrders.contains(order)) {
                            Test test = new Test();
                            test.setId(rs.getInt("lab39c1"));
                            test.setCode(rs.getString("lab39c2"));
                            test.setAbbr(rs.getString("lab39c3"));
                            test.setName(rs.getString("lab39c4"));
                            test.getResult().setResult(Tools.decrypt(rs.getString("lab57c1")));
                            test.getResult().setDateResult(rs.getTimestamp("lab57c2"));
                            test.getResult().setState(rs.getInt("lab57c8"));
                            test.getResult().setSampleState(rs.getInt("lab57c16"));
                            test.setTestType(rs.getShort("lab39c37"));
                            test.setConfidential(rs.getInt("lab39c27") == 1);
                            test.setGender(new Item(rs.getInt("lab39c6")));
                            test.setUnitAge(rs.getShort("lab39c9"));
                            test.setMinAge(rs.getInt("lab39c7"));
                            test.setMaxAge(rs.getInt("lab39c8"));
                            test.setRemission(rs.getInt("lab57c54"));
                            test.setProfile(rs.getInt("lab57c14"));
                            test.getLaboratory().setId(rs.getInt("lab40c1"));
                            test.getLaboratory().setCode(rs.getInt("lab40c2"));
                            test.getLaboratory().setName(rs.getString("lab40c3"));
                            test.getOriginRemission().setId(rs.getInt("idorigin"));
                            test.getOriginRemission().setCode(rs.getInt("codeorigin"));
                            test.getOriginRemission().setName(rs.getString("nameorigin"));
                            test.setExcluideTestProfile(rs.getInt("panellab39c39"));
                            test.setTemperatureTest(rs.getInt("lab39c56"));
                            if (rs.getString("lab37c1") != null) {
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
                            panel.setGender(new Item(rs.getInt("panellab39c6")));
                            panel.setUnitAge(rs.getShort("panellab39c9"));
                            panel.setMinAge(rs.getInt("panellab39c7"));
                            panel.setMaxAge(rs.getInt("panellab39c8"));

                            test.setPanel(panel);

                            Test pack = new Test();
                            pack.setId(rs.getString("packlab39c1") == null ? null : rs.getInt("packlab39c1"));
                            pack.setCode(rs.getString("packlab39c2"));
                            pack.setAbbr(rs.getString("packlab39c3"));
                            pack.setName(rs.getString("packlab39c4"));
                            pack.setTestType(rs.getShort("packlab39c37"));
                            pack.setGender(new Item(rs.getInt("packlab39c6")));
                            pack.setUnitAge(rs.getShort("packlab39c9"));
                            pack.setMinAge(rs.getInt("packlab39c7"));
                            pack.setMaxAge(rs.getInt("packlab39c8"));

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
                            test.getSample().setTakeDate(rs.getTimestamp("lab57c39"));
                            test.getSample().setManagementsample(rs.getString("lab24c6"));

                            test.setRackStore(rs.getString("lab16c3"));
                            test.setPositionStore(rs.getString("lab11c1"));

                            test.getUnit().setId(rs.getString("lab45c1") == null ? null : rs.getInt("lab45c1"));
                            test.getUnit().setName(rs.getString("lab45c2"));

                            listOrders.get(listOrders.indexOf(order)).getTests().add(test);
                        } else {
                            order.setCreatedDateShort(rs.getInt("lab22c2"));
                            order.setCreatedDate(rs.getTimestamp("lab22c3"));
                            order.setInconsistency(rs.getInt("lab22c10") == 1);
                            order.setHomebound(rs.getInt("lab22c4") == 1);
                            order.setMiles(rs.getInt("lab22c5"));
                            order.setExternalId(rs.getString("lab22c7"));
                            order.setState(rs.getInt("lab07c1"));
                            order.setPreviousState(rs.getString("lab22c9") == null ? null : rs.getInt("lab22c9"));
                            order.getLastUpdateUser().setId(rs.getInt("lab04c1"));
                            order.getLastUpdateUser().setName(rs.getString("lab04c2"));
                            order.getLastUpdateUser().setLastName(rs.getString("lab04c3"));
                            order.getLastUpdateUser().setUserName(rs.getString("lab04c4"));

                            //TIPO DE ORDEN
                            order.getType().setId(rs.getString("lab103c1") == null ? null : rs.getInt("lab103c1"));
                            order.getType().setCode(rs.getString("lab103c2"));
                            order.getType().setName(rs.getString("lab103c3"));
                            order.getType().setColor(rs.getString("lab103c4"));
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
                            order.getPatient().getRace().setCode(rs.getString("lab21lab08lab08c5"));
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
                            order.getPhysician().setEmail(rs.getString("lab19c21"));
                            //TARIFA
                            order.getRate().setId(rs.getInt("lab904c1"));
                            order.getRate().setCode(rs.getString("lab904c2"));
                            order.getRate().setName(rs.getString("lab904c3"));
                            //COMENTARIOS
                            //order.setComments(getCommentDao().listCommentOrder(order.getOrderNumber(), null));
                            //order.getPatient().setDiagnostic(getCommentDao().listCommentOrder(null, order.getPatient().getId()));

                            DemographicValue demoValue = null;
                            for (Demographic demographic : demographics) {
                                demoValue = new DemographicValue();
                                demoValue.setIdDemographic(demographic.getId());
                                demoValue.setDemographic(demographic.getName());
                                demoValue.setEncoded(demographic.isEncoded());
                                if (demographic.isEncoded()) {
                                    if (rs.getString("demo" + demographic.getId() + "_id") != null) {
                                        demoValue.setCodifiedId(rs.getInt("demo" + demographic.getId() + "_id"));
                                        demoValue.setCodifiedCode(rs.getString("demo" + demographic.getId() + "_code"));
                                        demoValue.setCodifiedName(rs.getString("demo" + demographic.getId() + "_name"));
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

                            if (rs.getString("lab39c1") != null) {
                                Test test = new Test();
                                test.setId(rs.getInt("lab39c1"));
                                test.setCode(rs.getString("lab39c2"));
                                test.setAbbr(rs.getString("lab39c3"));
                                test.setName(rs.getString("lab39c4"));
                                test.getResult().setResult(Tools.decrypt(rs.getString("lab57c1")));
                                test.getResult().setDateResult(rs.getTimestamp("lab57c2"));
                                test.getResult().setState(rs.getInt("lab57c8"));
                                test.getResult().setSampleState(rs.getInt("lab57c16"));
                                test.setTestType(rs.getShort("lab39c37"));
                                test.setConfidential(rs.getInt("lab39c27") == 1);
                                test.setGender(new Item(rs.getInt("lab39c6")));
                                test.setUnitAge(rs.getShort("lab39c9"));
                                test.setMinAge(rs.getInt("lab39c7"));
                                test.setMaxAge(rs.getInt("lab39c8"));
                                test.setProfile(rs.getInt("lab57c14"));
                                test.setRemission(rs.getInt("lab57c54"));
                                test.getLaboratory().setId(rs.getInt("lab40c1"));
                                test.getLaboratory().setCode(rs.getInt("lab40c2"));
                                test.getLaboratory().setName(rs.getString("lab40c3"));
                                test.getOriginRemission().setId(rs.getInt("idorigin"));
                                test.getOriginRemission().setCode(rs.getInt("codeorigin"));
                                test.getOriginRemission().setName(rs.getString("nameorigin"));
                                test.setExcluideTestProfile(rs.getInt("panellab39c39"));
                                test.setTemperatureTest(rs.getInt("lab39c56"));
                                test.setTentativeDeliveryDate(rs.getTimestamp("lab57c56"));
                                if (rs.getString("lab37c1") != null) {
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
                                panel.setGender(new Item(rs.getInt("panellab39c6")));
                                panel.setUnitAge(rs.getShort("panellab39c9"));
                                panel.setMinAge(rs.getInt("panellab39c7"));
                                panel.setMaxAge(rs.getInt("panellab39c8"));

                                test.setPanel(panel);

                                Test pack = new Test();
                                pack.setId(rs.getString("packlab39c1") == null ? null : rs.getInt("packlab39c1"));
                                pack.setCode(rs.getString("packlab39c2"));
                                pack.setAbbr(rs.getString("packlab39c3"));
                                pack.setName(rs.getString("packlab39c4"));
                                pack.setTestType(rs.getShort("packlab39c37"));
                                pack.setGender(new Item(rs.getInt("packlab39c6")));
                                pack.setUnitAge(rs.getShort("packlab39c9"));
                                pack.setMinAge(rs.getInt("packlab39c7"));
                                pack.setMaxAge(rs.getInt("packlab39c8"));

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
                                test.getSample().setTakeDate(rs.getTimestamp("lab57c39"));
                                test.getSample().setManagementsample(rs.getString("lab24c6"));

                                test.setRackStore(rs.getString("lab16c3"));
                                test.setPositionStore(rs.getString("lab11c1"));

                                test.getUnit().setId(rs.getString("lab45c1") == null ? null : rs.getInt("lab45c1"));
                                test.getUnit().setName(rs.getString("lab45c2"));

                                order.getTests().add(test);
                            }
                            String selectConfidentialOrder = "SELECT COUNT(*) "
                                    + " FROM lab25"
                                    + " WHERE lab22c1 = ?";
                            Integer sql = getConnection().queryForObject(selectConfidentialOrder, Integer.class, rs.getLong("lab22c1"));
                            if (sql >= 2) {
                                order.setIsReceiveReference(true);
                            } else {
                                order.setIsReceiveReference(false);
                            }
                            listOrders.add(order);
                        }
                        return order;
                    };
                    if (searchType == 2) {
                        getConnection().query(query.toString() + " " + from.toString(), mapper);
                    } else {
                        getConnection().query(query.toString() + "  " + from.toString() + "  ", mapper, params);
                    }
                }

            }
            return listOrders;
        } catch (EmptyResultDataAccessException ex) {
            return new ArrayList<>(0);
        }
    }

    /**
     * Lista las ordenes por rango de fecha o numero de orden desde base de
     * datos.
     *
     * @param vInitial Rango Inicial.
     * @param vFinal Rango Final.
     * @param searchType Indica si la consulta se hace por rango de orden(1),
     * fechas(0) o Listado de ordenes(2)
     * @param demographics Lista de demograficos.
     * @param orders lista de ordenes a consultar searchType == 2
     * @param tests Lista de Examenes
     *
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    default List<Order> listdeletedorder(Long vInitial, Long vFinal) throws Exception {
        try {
            List<Order> listOrders = new LinkedList<>();
            // Consulta de ordenes por historico:
            List<Integer> years = Tools.listOfConsecutiveYears(String.valueOf(vInitial), String.valueOf(vFinal));
            String lab22;
            int currentYear = DateTools.dateToNumberYear(new Date());
            for (Integer year : years) {
                lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
                boolean tableExists = getToolsDao().tableExists(getConnection(), lab22);
                if (tableExists) {
                    // Query
                    StringBuilder query = new StringBuilder();
                    query.append(ISOLATION_READ_UNCOMMITTED);
                    query.append("SELECT lab22.lab22c1, lab22c2, lab22c3, ");
                    query.append("lab21.lab21c1, lab21c2, lab21c3, lab21c4, lab21c5, lab21c6, lab21c7, lab21c8 ");
                    StringBuilder from = new StringBuilder();
                    from.append(" FROM ").append(lab22).append(" AS lab22 ");
                    from.append("INNER JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1  ");
                    from.append(" WHERE lab22.lab07C1 != 0  AND (lab22c19 = 0 or lab22c19 is null)  AND lab22.lab22c1 BETWEEN ? AND ? ");

                    Object[] params = null;
                    params = new Object[]{
                        vInitial, vFinal
                    };
                    RowMapper mapper = (RowMapper<Order>) (ResultSet rs, int i)
                            -> {
                        Order order = new Order();
                        order.setOrderNumber(rs.getLong("lab22c1"));
                        order.setCreatedDateShort(rs.getInt("lab22c2"));
                        order.setCreatedDate(rs.getTimestamp("lab22c3"));
                        //PACIENTE
                        order.getPatient().setId(rs.getInt("lab21c1"));
                        order.getPatient().setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                        order.getPatient().setName1(Tools.decrypt(rs.getString("lab21c3")));
                        order.getPatient().setName2(Tools.decrypt(rs.getString("lab21c4")));
                        order.getPatient().setLastName(Tools.decrypt(rs.getString("lab21c5")));
                        order.getPatient().setSurName(Tools.decrypt(rs.getString("lab21c6")));
                        order.getPatient().setBirthday(rs.getTimestamp("lab21c7"));
                        order.getPatient().setEmail(rs.getString("lab21c8"));
                        listOrders.add(order);

                        return order;
                    };
                    getConnection().query(query.toString() + "  " + from.toString() + "  ", mapper, params);
                }

            }
            return listOrders;
        } catch (EmptyResultDataAccessException ex) {
            return new ArrayList<>(0);
        }
    }

    /**
     * Lista las ordenes por rango de fecha o numero de orden desde base de
     * datos.
     *
     * @param vInitial Rango Inicial.
     * @param vFinal Rango Final.
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    default List<Order> listdeleted(Long vInitial, Long vFinal) throws Exception {
        try {
            List<Order> listOrders = new LinkedList<>();
            // Consulta de ordenes por historico:
            List<Integer> years = Tools.listOfConsecutiveYears(String.valueOf(vInitial), String.valueOf(vFinal));
            String lab22;
            String lab57;
            int currentYear = DateTools.dateToNumberYear(new Date());
            for (Integer year : years) {
                lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
                lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;
                boolean tableExists = getToolsDao().tableExists(getConnection(), lab22);
                tableExists = tableExists ? getToolsDao().tableExists(getConnection(), lab57) : tableExists;
                if (tableExists) {
                    // Query
                    StringBuilder query = new StringBuilder();
                    query.append(ISOLATION_READ_UNCOMMITTED);
                    query.append("SELECT lab22.lab22c1, lab22c2, lab22c3, lab22.lab07c1, ");
                    query.append("lab21.lab21c1, lab21c2, lab21c3, lab21c4, lab21c5, lab57.lab57c8, lab21c6, lab21c7, lab21c8, ");
                    query.append("lab39.lab39c1, lab39.lab39c2, lab39.lab39c3, lab39.lab39c37, lab39.lab39c4, ");
                    query.append("panel.lab39c1 AS panellab39c1, panel.lab39c2 AS panellab39c2, panel.lab39c3 AS panellab39c3, panel.lab39c37 AS panellab39c37, panel.lab39c4 AS panellab39c4, ");
                    query.append("pack.lab39c1 AS packlab39c1, pack.lab39c2 AS packlab39c2, pack.lab39c37 AS packlab39c37, pack.lab39c3 AS packlab39c3, pack.lab39c4 AS packlab39c4 ");

                    StringBuilder from = new StringBuilder();
                    from.append(" FROM ").append(lab57).append(" AS lab57 ");
                    from.append("INNER JOIN ").append(lab22).append(" AS lab22 ON lab22.lab22c1 = lab57.lab22c1 ");
                    from.append("INNER JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1  ");
                    from.append("INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1 AND lab39.lab39c26 = 1");
                    from.append("LEFT JOIN lab39 panel ON panel.lab39c1 = lab57.lab57c14 ");
                    from.append("LEFT JOIN lab39 pack ON pack.lab39c1 = lab57.lab57c15 ");

                    Object[] params = null;
                    from.append(" WHERE lab39.lab39c37 != 2 AND lab22.lab22c1 BETWEEN ? AND ? ");
                    from.append(" AND lab22.lab07C1 != 0  AND (lab22c19 = 0 or lab22c19 is null) ");

                    params = new Object[]{
                        vInitial, vFinal
                    };

                    RowMapper mapper = (RowMapper<Order>) (ResultSet rs, int i)
                            -> {
                        Order order = new Order();
                        order.setOrderNumber(rs.getLong("lab22c1"));
                        if (listOrders.contains(order)) {
                            Test test = new Test();
                            test.setId(rs.getInt("lab39c1"));
                            test.setCode(rs.getString("lab39c2"));
                            test.setAbbr(rs.getString("lab39c3"));
                            test.setName(rs.getString("lab39c4"));
                            test.setTestType(rs.getShort("lab39c37"));
                            test.getResult().setState(rs.getInt("lab57c8"));

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
                            listOrders.get(listOrders.indexOf(order)).getTests().add(test);
                        } else {
                            order.setCreatedDateShort(rs.getInt("lab22c2"));
                            order.setCreatedDate(rs.getTimestamp("lab22c3"));
                            order.setState(rs.getInt("lab07c1"));
                            //PACIENTE
                            order.getPatient().setId(rs.getInt("lab21c1"));
                            order.getPatient().setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                            order.getPatient().setName1(Tools.decrypt(rs.getString("lab21c3")));
                            order.getPatient().setName2(Tools.decrypt(rs.getString("lab21c4")));
                            order.getPatient().setLastName(Tools.decrypt(rs.getString("lab21c5")));
                            order.getPatient().setSurName(Tools.decrypt(rs.getString("lab21c6")));
                            order.getPatient().setBirthday(rs.getTimestamp("lab21c7"));
                            order.getPatient().setEmail(rs.getString("lab21c8"));
                            DemographicValue demoValue = null;

                            if (rs.getString("lab39c1") != null) {
                                Test test = new Test();
                                test.setId(rs.getInt("lab39c1"));
                                test.setCode(rs.getString("lab39c2"));
                                test.setAbbr(rs.getString("lab39c3"));
                                test.setName(rs.getString("lab39c4"));
                                test.setTestType(rs.getShort("lab39c37"));
                                test.getResult().setState(rs.getInt("lab57c8"));

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
                                order.getTests().add(test);
                            }
                            listOrders.add(order);
                        }
                        return order;
                    };
                    getConnection().query(query.toString() + "  " + from.toString() + "  ", mapper, params);
                }

            }
            return listOrders;
        } catch (EmptyResultDataAccessException ex) {
            return new ArrayList<>(0);
        }
    }

    /**
     * Lista las ordenes por rango de fecha o numero de orden desde base de
     * datos.
     *
     * @param vInitial Rango Inicial.
     * @param vFinal Rango Final.
     * @param deleteType
     * @param tests
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    default List<OrderBasic> listdeleted(Long vInitial, Long vFinal, int deleteType, List<Integer> tests) throws Exception {
        try {
            List<OrderBasic> listOrders = new LinkedList<>();
            // Consulta de ordenes por historico:
            List<Integer> years = Tools.listOfConsecutiveYears(String.valueOf(vInitial), String.valueOf(vFinal));
            String lab22;
            String lab57;
            int currentYear = DateTools.dateToNumberYear(new Date());
            List<String> listOrdersString = new LinkedList<>();
            for (Integer year : years) {
                lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
                lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;
                boolean tableExists = getToolsDao().tableExists(getConnection(), lab22);
                tableExists = tableExists ? getToolsDao().tableExists(getConnection(), lab57) : tableExists;
                if (tableExists) {
                    // Query
                    StringBuilder query = new StringBuilder();
                    StringBuilder from = new StringBuilder();
                    query.append(ISOLATION_READ_UNCOMMITTED);

                    if (deleteType == 1) {
                        query.append("SELECT lab22.lab22c1 ");
                        from.append(" FROM ").append(lab22).append(" AS lab22 ");
                    } else {
                        query.append("SELECT lab22.lab22c1, lab57.lab39c1");
                        from.append(" FROM ").append(lab57).append(" AS lab57 ");
                        from.append("INNER JOIN ").append(lab22).append(" AS lab22 ON lab22.lab22c1 = lab57.lab22c1 ");
                    }
                    StringBuilder where = new StringBuilder();
                    where.append(" WHERE lab22.lab22c1 BETWEEN ? AND ? ");
                    where.append(" AND lab22.lab07C1 != 0  AND (lab22c19 = 0 or lab22c19 is null) ");

                    switch (deleteType) {
                        case 1:
                            break;
                        case 2:
                            where.append(" AND lab57.lab57C8 >= ").append(LISEnum.ResultTestState.REPORTED.getValue());
                            where.append(" AND lab57.lab39c1 in (").append(tests.stream().map(test -> test.toString()).collect(Collectors.joining(","))).append(")");
                            break;
                        case 3:
                            where.append(" AND lab57.lab39c1 in (").append(tests.stream().map(test -> test.toString()).collect(Collectors.joining(","))).append(")");
                            break;
                        case 4:
                            where.append(" AND lab57.lab57C8 < ").append(LISEnum.ResultTestState.REPORTED.getValue());
                            where.append(" AND lab57.lab39c1 in (").append(tests.stream().map(test -> test.toString()).collect(Collectors.joining(","))).append(")");
                            break;
                        case 5:
                            where.append(" AND lab57.lab57C8 = ").append(LISEnum.ResultTestState.VALIDATED.getValue());
                            where.append(" AND lab57.lab39c1 in (").append(tests.stream().map(test -> test.toString()).collect(Collectors.joining(","))).append(")");
                            break;
                    }

                    Object[] params = null;
                    params = new Object[]{
                        vInitial, vFinal
                    };

                    RowMapper mapper = (RowMapper<OrderBasic>) (ResultSet rs, int i)
                            -> {
                        OrderBasic order = new OrderBasic();
                        order.setOrderNumber(rs.getLong("lab22c1"));
                        if (deleteType != 1) {
                            if (listOrdersString.contains(order.getOrderNumber().toString())) {
                                SuperTest test = new SuperTest();
                                test.setId(rs.getInt("lab39c1"));
                                listOrders.get(listOrdersString.indexOf(order.getOrderNumber().toString())).getTests().add(test);
                            } else {
                                if (rs.getString("lab39c1") != null) {
                                    SuperTest test = new SuperTest();
                                    test.setId(rs.getInt("lab39c1"));
                                    order.getTests().add(test);
                                }
                                listOrdersString.add(order.getOrderNumber().toString());
                                listOrders.add(order);
                            }
                        } else {
                            listOrders.add(order);
                        }
                        return order;
                    };
                    getConnection().query(query.toString() + "  " + from.toString() + "  " + where.toString(), mapper, params);
                }

            }
            return listOrders;
        } catch (EmptyResultDataAccessException ex) {
            return new ArrayList<>(0);
        }
    }

    /**
     * Lista las ordenes por rango de fecha o numero de orden desde base de
     * datos.
     *
     * @param vInitial Rango Inicial.
     * @param vFinal Rango Final.
     * @param searchType Indica si la consulta se hace por rango de orden(1),
     * fechas(0) o Listado de ordenes(2)
     * @param demographics Lista de demograficos.
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    default List<Order> listPendingTest(Long vInitial, Long vFinal, int searchType, final List<Demographic> demographics,  List<Integer> laboratorys, int branch) throws Exception {
        try {
            List<Order> listOrders = new LinkedList<>();
            // Consulta de ordenes por historico:
            List<Integer> years = Tools.listOfConsecutiveYears(String.valueOf(vInitial), String.valueOf(vFinal));
            String lab22;
            String lab57;
            int currentYear = DateTools.dateToNumberYear(new Date());
            for (Integer year : years) {
                lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
                lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;
                boolean tableExists = getToolsDao().tableExists(getConnection(), lab22);
                tableExists = tableExists ? getToolsDao().tableExists(getConnection(), lab57) : tableExists;
                if (tableExists) {
                    // Query
                    StringBuilder query = new StringBuilder();
                    query.append(ISOLATION_READ_UNCOMMITTED);
                    query.append("SELECT lab22.lab22c1, lab22c2, lab22c3, lab22c4, lab22c5, lab22.lab07c1, lab22c7, lab22c9, lab22c10, ");
                    query.append("lab103.lab103c1, lab103c2, lab103c3, lab103c4, lab103.lab07c1 AS lab103lab07c1,  ");
                    query.append("lab21.lab21c1, lab21c2, lab21c3, lab21c4, lab21c5, lab21c6, lab21lab80.lab80c1 AS lab21lab80lab80c1, lab21lab80.lab80c2 AS lab21lab80lab80c2, lab21lab80.lab80c3 AS lab21lab80lab80c3, lab21lab80.lab80c4 AS lab21lab80lab80c4, lab21lab80.lab80c5 AS lab21lab80lab80c5, lab21c7, lab21c8, lab21c9, lab21c10, lab21c11, lab21c16, lab21c17, lab21lab08.lab08c1 AS lab21lab08lab08c1, lab21lab08.lab08c2 AS lab21lab08lab08c2, lab21lab08.lab08c4 AS lab21lab08lab08c4, lab21lab08.lab08c5 AS lab21lab08lab08c5, lab21lab54.lab54c1 AS lab21lab54lab54c1, lab21lab54.lab54c2 AS lab21lab54lab54c2, lab21lab54.lab54c3 AS lab21lab54lab54c3, ");
                    query.append("lab05.lab05c1, lab05c10, lab05c4, ");
                    query.append("lab10.lab10c1, lab10c2, lab10c7,  ");
                    query.append("lab14.lab14c1, lab14c2, lab14c3, lab14c32, ");
                    query.append("lab19.lab19c1, lab19c2, lab19c3, lab19c21, ");
                    query.append("lab04p.lab04c1, lab04p.lab04c2, lab04p.lab04c3, lab04p.lab04c4, ");
                    query.append("lab22.lab04c1, lab04.lab04c2, lab04.lab04c3, lab04.lab04c4, ");
                    query.append("lab30.lab30c1, lab30.lab30c2, lab30.lab30c3, ");
                    query.append("lab904.lab904c1, lab904c2, lab904c3, ");
                    query.append("lab57.lab22c1, lab57.lab57c1, lab57.lab57c2, lab57.lab57c8, lab57.lab57c14, lab57.lab57c15, lab57c16, lab57.lab57c52, ");
                    query.append("lab39.lab39c1, lab39.lab39c2, lab39.lab39c3, lab39.lab39c4, lab39.lab39c6,lab39.lab39c7,lab39.lab39c8, lab39.lab39c9, lab39.lab39c37, lab39.lab39c27, lab39.lab39c56, ");
                    query.append("lab43.lab43c1, lab43c3, lab43.lab43c4, ");
                    query.append("panel.lab39c1 AS panellab39c1, panel.lab39c2 AS panellab39c2, panel.lab39c3 AS panellab39c3, panel.lab39c4 AS panellab39c4, panel.lab39c37 AS panellab39c37, panel.lab39c6 AS panellab39c6,panel.lab39c7 AS panellab39c7, panel.lab39c8 AS panellab39c8, panel.lab39c9 AS panellab39c9, lab39.lab39c39 AS panellab39c39, ");
                    query.append("pack.lab39c1 AS packlab39c1, pack.lab39c2 AS packlab39c2, pack.lab39c3 AS packlab39c3, pack.lab39c4 AS packlab39c4, pack.lab39c37 AS packlab39c37,  pack.lab39c6 AS packlab39c6, pack.lab39c7 AS packlab39c7, pack.lab39c8 AS packlab39c8, pack.lab39c9 AS packlab39c9, lab39.lab39c39 AS packlab39c39 ");

                    StringBuilder from = new StringBuilder();
                    from.append(" FROM ").append(lab57).append(" AS lab57 ");
                    from.append("INNER JOIN ").append(lab22).append(" AS lab22 ON lab22.lab22c1 = lab57.lab22c1 ");
                    from.append("INNER JOIN lab05 ON lab05.lab05c1 = lab22.lab05c1  ");
                    from.append("INNER JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1  ");
                    from.append("INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1 AND lab39.lab39c26 = 1");
                    from.append("INNER JOIN lab80 lab21lab80 ON lab21lab80.lab80c1 = lab21.lab80c1  ");
                    from.append("INNER JOIN lab30 ON lab30.lab30c1 = lab57.lab57c52 ");
                    from.append("INNER JOIN lab04 as lab04p ON lab04p.lab04c1 = lab57.lab57c66 ");
                    from.append("INNER JOIN lab103 ON lab103.lab103c1 = lab22.lab103c1  ");
                    from.append("LEFT JOIN lab08 lab21lab08 ON lab21lab08.lab08c1 = lab21.lab08c1  ");
                    from.append("LEFT JOIN lab54 lab21lab54 ON lab21lab54.lab54c1 = lab21.lab54c1  ");
                    from.append("LEFT JOIN lab10 ON lab10.lab10c1 = lab22.lab10c1  ");
                    from.append("LEFT JOIN lab14 ON lab14.lab14c1 = lab22.lab14c1  ");
                    from.append("LEFT JOIN lab19 ON lab19.lab19c1 = lab22.lab19c1  ");
                    from.append("LEFT JOIN lab04 ON lab04.lab04c1 = lab22.lab04c1  ");
                    from.append("LEFT JOIN lab904 ON lab904.lab904c1 = lab22.lab904c1  ");
                    from.append("LEFT JOIN lab39 panel ON panel.lab39c1 = lab57.lab57c14 ");
                    from.append("LEFT JOIN lab39 pack ON pack.lab39c1 = lab57.lab57c15 ");
                    from.append("LEFT JOIN lab43 ON lab43.lab43c1 = lab39.lab43c1 ");

                    for (Demographic demographic : demographics) {
                        if (demographic.getOrigin().equals("O")) {
                            if (demographic.isEncoded()) {
                                query.append(", demo").append(demographic.getId()).append(".lab63c1 as demo").append(demographic.getId()).append("_id ");
                                query.append(", demo").append(demographic.getId()).append(".lab63c2 as demo").append(demographic.getId()).append("_code ");
                                query.append(", demo").append(demographic.getId()).append(".lab63c3 as demo").append(demographic.getId()).append("_name ");

                                from.append(" LEFT JOIN Lab63 demo").append(demographic.getId()).append(" ON lab22.lab_demo_").append(demographic.getId()).append(" = demo").append(demographic.getId()).append(".lab63c1 ");
                            } else {
                                query.append(", lab22.lab_demo_").append(demographic.getId());
                            }
                        } else {
                            if (demographic.getOrigin().equals("H")) {
                                if (demographic.isEncoded()) {
                                    query.append(", demo").append(demographic.getId()).append(".lab63c1 as demo").append(demographic.getId()).append("_id ");
                                    query.append(", demo").append(demographic.getId()).append(".lab63c2 as demo").append(demographic.getId()).append("_code ");
                                    query.append(", demo").append(demographic.getId()).append(".lab63c3 as demo").append(demographic.getId()).append("_name ");

                                    from.append(" LEFT JOIN Lab63 demo").append(demographic.getId()).append(" ON lab21.lab_demo_").append(demographic.getId()).append(" = demo").append(demographic.getId()).append(".lab63c1 ");
                                } else {
                                    query.append(", lab21.lab_demo_").append(demographic.getId());
                                }
                            }
                        }
                    }
                    Object[] params = null;
                    switch (searchType) {
                        case 0:
                            from.append(" WHERE lab39.lab39c37 != 2 AND lab57.lab57c34 BETWEEN ? AND ? ");
                            break;
                        case 1:
                            from.append(" WHERE lab39.lab39c37 != 2 AND lab22.lab22c1 BETWEEN ? AND ? ");
                            break;
                        case 3:
                            from.append(" WHERE lab39.lab39c37 != 2 AND lab22.lab22c2 BETWEEN ? AND ? ");
                            break;
                    }

                    from.append(SQLTools.buildSQLLaboratoryFilter(laboratorys, branch));

                    from.append(" AND lab57c16 = -1 and lab22.lab07C1 != 0 AND (lab22c19 = 0 or lab22c19 is null) ");

                    params = new Object[]{
                        vInitial, vFinal
                    };

                    RowMapper mapper = (RowMapper<Order>) (ResultSet rs, int i)
                            -> {
                        Order order = new Order();
                        order.setOrderNumber(rs.getLong("lab22c1"));
                        if (listOrders.contains(order)) {
                            Test test = new Test();
                            test.setId(rs.getInt("lab39c1"));
                            test.setCode(rs.getString("lab39c2"));
                            test.setAbbr(rs.getString("lab39c3"));
                            test.setName(rs.getString("lab39c4"));
                            test.getResult().setResult(Tools.decrypt(rs.getString("lab57c1")));
                            test.getResult().setDateResult(rs.getTimestamp("lab57c2"));
                            test.getResult().setState(rs.getInt("lab57c8"));
                            test.getResult().setSampleState(rs.getInt("lab57c16"));
                            test.setTestType(rs.getShort("lab39c37"));
                            test.setConfidential(rs.getInt("lab39c27") == 1);
                            test.setGender(new Item(rs.getInt("lab39c6")));
                            test.setUnitAge(rs.getShort("lab39c9"));
                            test.setMinAge(rs.getInt("lab39c7"));
                            test.setMaxAge(rs.getInt("lab39c8"));

                            test.setProfile(rs.getInt("lab57c14"));

                            test.getMotive().setId(rs.getInt("lab30c1"));
                            test.getMotive().setName(rs.getString("lab30c2"));
                            test.getMotive().setDescription(rs.getString("lab30c3"));

                            test.getUserpending().setId(rs.getInt("lab04c1"));
                            test.getUserpending().setName(rs.getString("lab04c2"));
                            test.getUserpending().setLastName(rs.getString("lab04c3"));
                            test.getUserpending().setUserName(rs.getString("lab04c4"));

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

                            Test pack = new Test();
                            pack.setId(rs.getString("packlab39c1") == null ? null : rs.getInt("packlab39c1"));
                            pack.setCode(rs.getString("packlab39c2"));
                            pack.setAbbr(rs.getString("packlab39c3"));
                            pack.setName(rs.getString("packlab39c4"));
                            pack.setTestType(rs.getShort("packlab39c37"));
                            pack.setGender(new Item(rs.getInt("packlab39c6")));
                            pack.setUnitAge(rs.getShort("packlab39c9"));
                            pack.setMinAge(rs.getInt("packlab39c7"));
                            pack.setMaxAge(rs.getInt("packlab39c8"));

                            test.setPack(pack);

                            test.getArea().setId(rs.getString("lab43c1") == null ? null : rs.getInt("lab43c1"));
                            test.getArea().setAbbreviation(rs.getString("lab43c3"));
                            test.getArea().setName(rs.getString("lab43c4"));

                            listOrders.get(listOrders.indexOf(order)).getTests().add(test);
                        } else {
                            order.setCreatedDateShort(rs.getInt("lab22c2"));
                            order.setCreatedDate(rs.getTimestamp("lab22c3"));
                            order.setInconsistency(rs.getInt("lab22c10") == 1);
                            order.setHomebound(rs.getInt("lab22c4") == 1);
                            order.setMiles(rs.getInt("lab22c5"));
                            order.setExternalId(rs.getString("lab22c7"));
                            order.setState(rs.getInt("lab07c1"));
                            order.setPreviousState(rs.getString("lab22c9") == null ? null : rs.getInt("lab22c9"));
                            order.getLastUpdateUser().setId(rs.getInt("lab04c1"));
                            order.getLastUpdateUser().setName(rs.getString("lab04c2"));
                            order.getLastUpdateUser().setLastName(rs.getString("lab04c3"));
                            order.getLastUpdateUser().setUserName(rs.getString("lab04c4"));

                            //TIPO DE ORDEN
                            order.getType().setId(rs.getString("lab103c1") == null ? null : rs.getInt("lab103c1"));
                            order.getType().setCode(rs.getString("lab103c2"));
                            order.getType().setName(rs.getString("lab103c3"));
                            order.getType().setColor(rs.getString("lab103c4"));
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
                            order.getPatient().getRace().setCode(rs.getString("lab21lab08lab08c5"));
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
                            order.getPhysician().setEmail(rs.getString("lab19c21"));
                            //TARIFA
                            order.getRate().setId(rs.getInt("lab904c1"));
                            order.getRate().setCode(rs.getString("lab904c2"));
                            order.getRate().setName(rs.getString("lab904c3"));
                            //COMENTARIOS
                            //order.setComments(getCommentDao().listCommentOrder(order.getOrderNumber(), null));
                            //order.getPatient().setDiagnostic(getCommentDao().listCommentOrder(null, order.getPatient().getId()));

                            DemographicValue demoValue = null;
                            for (Demographic demographic : demographics) {
                                demoValue = new DemographicValue();
                                demoValue.setIdDemographic(demographic.getId());
                                demoValue.setDemographic(demographic.getName());
                                demoValue.setEncoded(demographic.isEncoded());
                                if (demographic.isEncoded()) {
                                    if (rs.getString("demo" + demographic.getId() + "_id") != null) {
                                        demoValue.setCodifiedId(rs.getInt("demo" + demographic.getId() + "_id"));
                                        demoValue.setCodifiedCode(rs.getString("demo" + demographic.getId() + "_code"));
                                        demoValue.setCodifiedName(rs.getString("demo" + demographic.getId() + "_name"));
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

                            if (rs.getString("lab39c1") != null) {
                                Test test = new Test();
                                test.setId(rs.getInt("lab39c1"));
                                test.setCode(rs.getString("lab39c2"));
                                test.setAbbr(rs.getString("lab39c3"));
                                test.setName(rs.getString("lab39c4"));
                                test.getResult().setResult(Tools.decrypt(rs.getString("lab57c1")));
                                test.getResult().setDateResult(rs.getTimestamp("lab57c2"));
                                test.getResult().setState(rs.getInt("lab57c8"));
                                test.getResult().setSampleState(rs.getInt("lab57c16"));
                                test.setTestType(rs.getShort("lab39c37"));
                                test.setConfidential(rs.getInt("lab39c27") == 1);
                                test.setGender(new Item(rs.getInt("lab39c6")));
                                test.setUnitAge(rs.getShort("lab39c9"));
                                test.setMinAge(rs.getInt("lab39c7"));
                                test.setMaxAge(rs.getInt("lab39c8"));
                                test.setProfile(rs.getInt("lab57c14"));

                                test.getMotive().setId(rs.getInt("lab30c1"));
                                test.getMotive().setName(rs.getString("lab30c2"));
                                test.getMotive().setDescription(rs.getString("lab30c3"));

                                test.getUserpending().setId(rs.getInt("lab04c1"));
                                test.getUserpending().setName(rs.getString("lab04c2"));
                                test.getUserpending().setLastName(rs.getString("lab04c3"));
                                test.getUserpending().setUserName(rs.getString("lab04c4"));

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
                                order.getTests().add(test);
                            }
                            listOrders.add(order);
                        }
                        return order;
                    };
                    if (searchType == 2) {
                        getConnection().query(query.toString() + " " + from.toString(), mapper);
                    } else {
                        getConnection().query(query.toString() + "  " + from.toString() + "  ", mapper, params);
                    }
                }

            }
            return listOrders;
        } catch (EmptyResultDataAccessException ex) {
            return new ArrayList<>(0);
        }
    }

    /**
     * Lista las ordenes por rango de fecha o numero de orden desde base de
     * datos.
     *
     * @param vInitial Rango Inicial.
     * @param vFinal Rango Final.
     * @param searchType Indica si la consulta se hace por rango de orden(1),
     * fechas(0) o Listado de ordenes(2)
     * @param demographics Lista de demograficos.
     * @param orders lista de ordenes a consultar searchType == 2
     * @param tests Lista de Examenes
     * @param check
     * @param remission
     * @param laboratorys
     * @param branch
     *
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    default List<Order> listw(Long vInitial, Long vFinal, int searchType, final List<Demographic> demographics, List<Long> orders, List<Integer> tests, int check, int remission, List<Integer> laboratorys, int branch) throws Exception {
        try {
            List<Order> listOrders = new LinkedList<>();
            // Consulta de ordenes por historico:
            List<Integer> years = Tools.listOfConsecutiveYears(String.valueOf(vInitial), String.valueOf(vFinal));
            String lab22;
            String lab57;
            int currentYear = DateTools.dateToNumberYear(new Date());
            for (Integer year : years) {
                lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
                lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;

                // Query
                StringBuilder query = new StringBuilder();
                query.append(ISOLATION_READ_UNCOMMITTED);
                query.append("SELECT lab22.lab22c1, lab22c2, lab22c3, lab22c4, lab22c5, lab22.lab07c1, lab22c7, lab22c9, lab22c10, ");
                query.append("lab103.lab103c1, lab103c2, lab103c3, lab103c4, lab103.lab07c1 AS lab103lab07c1,  ");
                query.append("lab21.lab21c1, lab21c2, lab21c3, lab21c4, lab21c5, lab21c6, lab21lab80.lab80c1 AS lab21lab80lab80c1, lab21lab80.lab80c2 AS lab21lab80lab80c2, lab21lab80.lab80c3 AS lab21lab80lab80c3, lab21lab80.lab80c4 AS lab21lab80lab80c4, lab21lab80.lab80c5 AS lab21lab80lab80c5, lab21c7, lab21c8, lab21c9, lab21c10, lab21c11, lab21c16, lab21c17, lab21lab08.lab08c1 AS lab21lab08lab08c1, lab21lab08.lab08c2 AS lab21lab08lab08c2, lab21lab08.lab08c4 AS lab21lab08lab08c4, lab21lab08.lab08c5 AS lab21lab08lab08c5, lab21lab54.lab54c1 AS lab21lab54lab54c1, lab21lab54.lab54c2 AS lab21lab54lab54c2, lab21lab54.lab54c3 AS lab21lab54lab54c3, ");
                query.append("lab05.lab05c1, lab05c10, lab05c4, ");
                query.append("lab10.lab10c1, lab10c2, lab10c7,  ");
                query.append("lab14.lab14c1, lab14c2, lab14c3, lab14c32, ");
                query.append("lab19.lab19c1, lab19c2, lab19c3, lab19c21, ");
                query.append("lab22.lab04c1, lab04c2, lab04c3, lab04c4, ");
                query.append("lab904.lab904c1, lab904c2, lab904c3, ");
                query.append("lab57.lab22c1, lab57.lab57c1, lab57.lab57c2, lab57.lab57c8, lab57.lab57c14, lab57.lab57c15, lab57c16, ");
                query.append("lab39.lab39c1, lab39.lab39c2, lab39.lab39c3, lab39.lab39c4, lab39.lab39c6,lab39.lab39c7,lab39.lab39c8, lab39.lab39c9, lab39.lab39c37, lab39.lab39c27, ");
                query.append("lab43.lab43c1, lab43c3, lab43.lab43c4, ");
                query.append("lab24.lab24c1, lab24c2, lab24c4, lab24c9, lab24.lab24c10, lab24c6, ");
                query.append("lab45.lab45c1, lab45.lab45c2, ");
                query.append("lab24.lab56c1, lab56.lab56c2, ");
                query.append("panel.lab39c1 AS panellab39c1, panel.lab39c2 AS panellab39c2, panel.lab39c3 AS panellab39c3, panel.lab39c4 AS panellab39c4, panel.lab39c37 AS panellab39c37, panel.lab39c6 AS panellab39c6,panel.lab39c7 AS panellab39c7, panel.lab39c8 AS panellab39c8, panel.lab39c9 AS panellab39c9, lab39.lab39c39 AS panellab39c39, ");
                query.append("pack.lab39c1 AS packlab39c1, pack.lab39c2 AS packlab39c2, pack.lab39c3 AS packlab39c3, pack.lab39c4 AS packlab39c4, pack.lab39c37 AS packlab39c37,  pack.lab39c6 AS packlab39c6, pack.lab39c7 AS packlab39c7, pack.lab39c8 AS packlab39c8, pack.lab39c9 AS packlab39c9, lab39.lab39c39 AS packlab39c39, ");
                query.append("lab57.lab40c1, lab40.lab40c2, lab40.lab40c3, ");
                query.append("lab57.lab40c1a as idorigin, origin.lab40c2 as codeorigin, origin.lab40c3 as nameorigin, ");
                query.append("lab16.lab16c3, lab11.lab11c1,");
                query.append("lab32.lab37c1, lab32.lab32c1, ");
                query.append("lab57.lab57c54, lab57.lab57c39 ");
                StringBuilder from = new StringBuilder();
                from.append(" FROM ").append(lab57).append(" AS lab57 ");
                from.append("INNER JOIN ").append(lab22).append(" AS lab22 ON lab22.lab22c1 = lab57.lab22c1 ");
                from.append("INNER JOIN lab05 ON lab05.lab05c1 = lab22.lab05c1  ");
                from.append("INNER JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1  ");
                from.append("INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1 AND lab39.lab39c26 = 1");
                from.append("INNER JOIN lab80 lab21lab80 ON lab21lab80.lab80c1 = lab21.lab80c1  ");
                from.append("INNER JOIN lab103 ON lab103.lab103c1 = lab22.lab103c1  ");
                from.append("LEFT JOIN lab08 lab21lab08 ON lab21lab08.lab08c1 = lab21.lab08c1  ");
                from.append("LEFT JOIN lab54 lab21lab54 ON lab21lab54.lab54c1 = lab21.lab54c1  ");
                from.append("LEFT JOIN lab10 ON lab10.lab10c1 = lab22.lab10c1  ");
                from.append("LEFT JOIN lab14 ON lab14.lab14c1 = lab22.lab14c1  ");
                from.append("LEFT JOIN lab19 ON lab19.lab19c1 = lab22.lab19c1  ");
                from.append("LEFT JOIN lab04 ON lab04.lab04c1 = lab22.lab04c1  ");
                from.append("LEFT JOIN lab904 ON lab904.lab904c1 = lab22.lab904c1  ");
                from.append("LEFT JOIN lab39 panel ON panel.lab39c1 = lab57.lab57c14 ");
                from.append("LEFT JOIN lab39 pack ON pack.lab39c1 = lab57.lab57c15 ");
                from.append("LEFT JOIN lab43 ON lab43.lab43c1 = lab39.lab43c1 ");
                from.append("LEFT JOIN lab24 ON lab24.lab24c1 = lab39.lab24c1 ");
                from.append("LEFT JOIN lab45 ON lab45.lab45c1 = lab39.lab45c1 ");
                from.append("LEFT JOIN lab40 ON lab40.lab40c1 = lab57.lab40c1 ");
                from.append("LEFT JOIN lab56 ON lab56.lab56c1 = lab24.lab56c1 ");
                from.append("LEFT JOIN lab11 ON lab11.lab24c1 = lab57.lab24c1 AND lab11.lab22c1 = lab57.lab22c1 ");
                from.append("LEFT JOIN lab16 ON lab16.lab16c1 = lab11.lab16c1 ");
                from.append("LEFT JOIN lab32 ON lab32.lab22c1 = lab57.lab22c1 AND lab32.lab39c1 = lab57.lab39c1 ");
                from.append("LEFT JOIN lab40 origin ON origin.lab40c1 = lab57.lab40c1a ");

                for (Demographic demographic : demographics) {
                    if (demographic.getOrigin().equals("O")) {
                        if (demographic.isEncoded()) {
                            query.append(", demo").append(demographic.getId()).append(".lab63c1 as demo").append(demographic.getId()).append("_id ");
                            query.append(", demo").append(demographic.getId()).append(".lab63c2 as demo").append(demographic.getId()).append("_code ");
                            query.append(", demo").append(demographic.getId()).append(".lab63c3 as demo").append(demographic.getId()).append("_name ");

                            from.append(" LEFT JOIN Lab63 demo").append(demographic.getId()).append(" ON lab22.lab_demo_").append(demographic.getId()).append(" = demo").append(demographic.getId()).append(".lab63c1 ");
                        } else {
                            query.append(", lab22.lab_demo_").append(demographic.getId());
                        }
                    } else {
                        if (demographic.getOrigin().equals("H")) {
                            if (demographic.isEncoded()) {
                                query.append(", demo").append(demographic.getId()).append(".lab63c1 as demo").append(demographic.getId()).append("_id ");
                                query.append(", demo").append(demographic.getId()).append(".lab63c2 as demo").append(demographic.getId()).append("_code ");
                                query.append(", demo").append(demographic.getId()).append(".lab63c3 as demo").append(demographic.getId()).append("_name ");

                                from.append(" LEFT JOIN Lab63 demo").append(demographic.getId()).append(" ON lab21.lab_demo_").append(demographic.getId()).append(" = demo").append(demographic.getId()).append(".lab63c1 ");
                            } else {
                                query.append(", lab21.lab_demo_").append(demographic.getId());
                            }
                        }
                    }
                }
                Object[] params = null;
                switch (searchType) {
                    case 0:
                        from.append(" WHERE lab39.lab39c37 != 2 AND lab57.lab57c34 BETWEEN ? AND ? ");
                        break;
                    case 1:
                        from.append(" WHERE lab39.lab39c37 != 2 AND lab22.lab22c1 BETWEEN ? AND ? ");
                        break;
                    case 3:
                        from.append(" WHERE lab39.lab39c37 != 2 AND lab22.lab22c2 BETWEEN ? AND ? ");
                        break;
                    default:
                        from.append(" WHERE lab39.lab39c37 != 2 AND lab22.lab22c1 IN(").append(orders.stream().map(order -> order.toString()).collect(Collectors.joining(","))).append(") ");
                        break;
                }

                if (tests != null && !tests.isEmpty()) {
                    if (tests.size() == 1) {
                        from.append(" AND lab39.lab39c1 = ").append(tests.get(0));
                    } else {
                        from.append(" AND lab39.lab39c1 IN (").append(tests.stream().map(test -> test.toString()).collect(Collectors.joining(","))).append(") ");
                    }
                }

                //filtro por estado de la muestra.
                switch (check) {
                    case 1:
                        from.append(" AND lab57.lab57c16 = ").append(LISEnum.ResultSampleState.CHECKED.getValue());
                        break;
                    case 2:
                        from.append(" AND lab57.lab57c16 != ").append(LISEnum.ResultSampleState.CHECKED.getValue());
                        break;
                    default:
                        break;
                }

                if (remission == 1) {
                    from.append(" AND lab57.lab57C54 is null ");
                }

                from.append(" AND lab22.lab07C1 != 0  AND (lab22c19 = 0 or lab22c19 is null) ");
                
                from.append(SQLTools.buildSQLLaboratoryFilter(laboratorys, branch));
                
                params = new Object[]{
                    vInitial, vFinal
                };

                RowMapper mapper = (RowMapper<Order>) (ResultSet rs, int i)
                        -> {
                    Order order = new Order();
                    order.setOrderNumber(rs.getLong("lab22c1"));
                    if (listOrders.contains(order)) {
                        Test test = new Test();
                        test.setId(rs.getInt("lab39c1"));
                        test.setCode(rs.getString("lab39c2"));
                        test.setAbbr(rs.getString("lab39c3"));
                        test.setName(rs.getString("lab39c4"));
                        test.getResult().setResult(Tools.decrypt(rs.getString("lab57c1")));
                        test.getResult().setDateResult(rs.getTimestamp("lab57c2"));
                        test.getResult().setState(rs.getInt("lab57c8"));
                        test.getResult().setSampleState(rs.getInt("lab57c16"));
                        test.setTestType(rs.getShort("lab39c37"));
                        test.setConfidential(rs.getInt("lab39c27") == 1);
                        test.setGender(new Item(rs.getInt("lab39c6")));
                        test.setUnitAge(rs.getShort("lab39c9"));
                        test.setMinAge(rs.getInt("lab39c7"));
                        test.setMaxAge(rs.getInt("lab39c8"));
                        test.setRemission(rs.getInt("lab57c54"));
                        test.setProfile(rs.getInt("lab57c14"));
                        test.getLaboratory().setId(rs.getInt("lab40c1"));
                        test.getLaboratory().setCode(rs.getInt("lab40c2"));
                        test.getLaboratory().setName(rs.getString("lab40c3"));
                        test.getOriginRemission().setId(rs.getInt("idorigin"));
                        test.getOriginRemission().setCode(rs.getInt("codeorigin"));
                        test.getOriginRemission().setName(rs.getString("nameorigin"));
                        test.setExcluideTestProfile(rs.getInt("panellab39c39"));
                        if (rs.getString("lab37c1") != null) {
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
                        panel.setGender(new Item(rs.getInt("panellab39c6")));
                        panel.setUnitAge(rs.getShort("panellab39c9"));
                        panel.setMinAge(rs.getInt("panellab39c7"));
                        panel.setMaxAge(rs.getInt("panellab39c8"));

                        test.setPanel(panel);

                        Test pack = new Test();
                        pack.setId(rs.getString("packlab39c1") == null ? null : rs.getInt("packlab39c1"));
                        pack.setCode(rs.getString("packlab39c2"));
                        pack.setAbbr(rs.getString("packlab39c3"));
                        pack.setName(rs.getString("packlab39c4"));
                        pack.setTestType(rs.getShort("packlab39c37"));
                        pack.setGender(new Item(rs.getInt("packlab39c6")));
                        pack.setUnitAge(rs.getShort("packlab39c9"));
                        pack.setMinAge(rs.getInt("packlab39c7"));
                        pack.setMaxAge(rs.getInt("packlab39c8"));

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
                        test.getSample().setTakeDate(rs.getTimestamp("lab57c39"));
                        test.getSample().setManagementsample(rs.getString("lab24c6"));

                        test.setRackStore(rs.getString("lab16c3"));
                        test.setPositionStore(rs.getString("lab11c1"));

                        test.getUnit().setId(rs.getString("lab45c1") == null ? null : rs.getInt("lab45c1"));
                        test.getUnit().setName(rs.getString("lab45c2"));

                        listOrders.get(listOrders.indexOf(order)).getTests().add(test);
                    } else {
                        order.setCreatedDateShort(rs.getInt("lab22c2"));
                        order.setCreatedDate(rs.getTimestamp("lab22c3"));
                        order.setInconsistency(rs.getInt("lab22c10") == 1);
                        order.setHomebound(rs.getInt("lab22c4") == 1);
                        order.setMiles(rs.getInt("lab22c5"));
                        order.setExternalId(rs.getString("lab22c7"));
                        order.setState(rs.getInt("lab07c1"));
                        order.setPreviousState(rs.getString("lab22c9") == null ? null : rs.getInt("lab22c9"));
                        order.getLastUpdateUser().setId(rs.getInt("lab04c1"));
                        order.getLastUpdateUser().setName(rs.getString("lab04c2"));
                        order.getLastUpdateUser().setLastName(rs.getString("lab04c3"));
                        order.getLastUpdateUser().setUserName(rs.getString("lab04c4"));

                        //TIPO DE ORDEN
                        order.getType().setId(rs.getString("lab103c1") == null ? null : rs.getInt("lab103c1"));
                        order.getType().setCode(rs.getString("lab103c2"));
                        order.getType().setName(rs.getString("lab103c3"));
                        order.getType().setColor(rs.getString("lab103c4"));
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
                        order.getPatient().getRace().setCode(rs.getString("lab21lab08lab08c5"));
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
                        order.getPhysician().setEmail(rs.getString("lab19c21"));
                        //TARIFA
                        order.getRate().setId(rs.getInt("lab904c1"));
                        order.getRate().setCode(rs.getString("lab904c2"));
                        order.getRate().setName(rs.getString("lab904c3"));
                        //COMENTARIOS
                        //order.setComments(getCommentDao().listCommentOrder(order.getOrderNumber(), null));
                        //order.getPatient().setDiagnostic(getCommentDao().listCommentOrder(null, order.getPatient().getId()));

                        DemographicValue demoValue = null;
                        for (Demographic demographic : demographics) {
                            demoValue = new DemographicValue();
                            demoValue.setIdDemographic(demographic.getId());
                            demoValue.setDemographic(demographic.getName());
                            demoValue.setEncoded(demographic.isEncoded());
                            if (demographic.isEncoded()) {
                                if (rs.getString("demo" + demographic.getId() + "_id") != null) {
                                    demoValue.setCodifiedId(rs.getInt("demo" + demographic.getId() + "_id"));
                                    demoValue.setCodifiedCode(rs.getString("demo" + demographic.getId() + "_code"));
                                    demoValue.setCodifiedName(rs.getString("demo" + demographic.getId() + "_name"));
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

                        if (rs.getString("lab39c1") != null) {
                            Test test = new Test();
                            test.setId(rs.getInt("lab39c1"));
                            test.setCode(rs.getString("lab39c2"));
                            test.setAbbr(rs.getString("lab39c3"));
                            test.setName(rs.getString("lab39c4"));
                            test.getResult().setResult(Tools.decrypt(rs.getString("lab57c1")));
                            test.getResult().setDateResult(rs.getTimestamp("lab57c2"));
                            test.getResult().setState(rs.getInt("lab57c8"));
                            test.getResult().setSampleState(rs.getInt("lab57c16"));
                            test.setTestType(rs.getShort("lab39c37"));
                            test.setConfidential(rs.getInt("lab39c27") == 1);
                            test.setGender(new Item(rs.getInt("lab39c6")));
                            test.setUnitAge(rs.getShort("lab39c9"));
                            test.setMinAge(rs.getInt("lab39c7"));
                            test.setMaxAge(rs.getInt("lab39c8"));
                            test.setProfile(rs.getInt("lab57c14"));
                            test.setRemission(rs.getInt("lab57c54"));
                            test.getLaboratory().setId(rs.getInt("lab40c1"));
                            test.getLaboratory().setCode(rs.getInt("lab40c2"));
                            test.getLaboratory().setName(rs.getString("lab40c3"));
                            test.getOriginRemission().setId(rs.getInt("idorigin"));
                            test.getOriginRemission().setCode(rs.getInt("codeorigin"));
                            test.getOriginRemission().setName(rs.getString("nameorigin"));
                            test.setExcluideTestProfile(rs.getInt("panellab39c39"));
                            if (rs.getString("lab37c1") != null) {
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
                            panel.setGender(new Item(rs.getInt("panellab39c6")));
                            panel.setUnitAge(rs.getShort("panellab39c9"));
                            panel.setMinAge(rs.getInt("panellab39c7"));
                            panel.setMaxAge(rs.getInt("panellab39c8"));

                            test.setPanel(panel);

                            Test pack = new Test();
                            pack.setId(rs.getString("packlab39c1") == null ? null : rs.getInt("packlab39c1"));
                            pack.setCode(rs.getString("packlab39c2"));
                            pack.setAbbr(rs.getString("packlab39c3"));
                            pack.setName(rs.getString("packlab39c4"));
                            pack.setTestType(rs.getShort("packlab39c37"));
                            pack.setGender(new Item(rs.getInt("packlab39c6")));
                            pack.setUnitAge(rs.getShort("packlab39c9"));
                            pack.setMinAge(rs.getInt("packlab39c7"));
                            pack.setMaxAge(rs.getInt("packlab39c8"));

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
                            test.getSample().setTakeDate(rs.getTimestamp("lab57c39"));
                            test.getSample().setManagementsample(rs.getString("lab24c6"));

                            test.setRackStore(rs.getString("lab16c3"));
                            test.setPositionStore(rs.getString("lab11c1"));

                            test.getUnit().setId(rs.getString("lab45c1") == null ? null : rs.getInt("lab45c1"));
                            test.getUnit().setName(rs.getString("lab45c2"));

                            order.getTests().add(test);
                        }
                        listOrders.add(order);
                    }
                    return order;
                };
                if (searchType == 2) {
                    getConnection().query(query.toString() + " " + from.toString(), mapper);
                } else {
                    getConnection().query(query.toString() + "  " + from.toString() + "  ", mapper, params);
                }
            }
            return listOrders;
        } catch (EmptyResultDataAccessException ex) {
            return new ArrayList<>(0);
        }
    }

    /**
     * Lista las ordenes por rango de fecha o numero de orden desde base de
     * datos.
     *
     * @param vInitial Rango Inicial.
     * @param vFinal Rango Final.
     * @param searchType Indica si la consulta se hace por rango de orden(1),
     * fechas(0) o Listado de ordenes(2)
     * @param demographics Lista de demograficos.
     * @param orders lista de ordenes a consultar searchType == 2
     * @param tests Lista de Examenes
     *
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    default List<Order> listTestOrder(Long vInitial, Long vFinal, int searchType, final List<Demographic> demographics, List<Long> orders, List<Integer> tests) throws Exception {
        try {
            List<Order> listOrders = new ArrayList<>();
            // Consulta de ordenes por historico:
            List<Integer> years = Tools.listOfConsecutiveYears(String.valueOf(vInitial), String.valueOf(vFinal));
            String lab22;
            String lab57;
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            for (Integer year : years) {
                lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
                lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;

                // Query
                StringBuilder query = new StringBuilder();
                query.append(ISOLATION_READ_UNCOMMITTED);
                query.append("SELECT lab22.lab22c1, lab22c2, lab22c3, lab39.lab39c1, ");
                query.append("lab05.lab05c1, lab05c10, lab05c4, ");
                query.append("lab43.lab43c1, lab43c3, lab43.lab43c4 ");
                StringBuilder from = new StringBuilder();
                from.append(" FROM ").append(lab57).append(" AS lab57 ");
                from.append("INNER JOIN ").append(lab22).append(" AS lab22 ON lab22.lab22c1 = lab57.lab22c1 ");
                from.append("INNER JOIN lab05 ON lab05.lab05c1 = lab22.lab05c1  ");
                from.append("INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1  ");
                from.append("LEFT JOIN lab43 ON lab43.lab43c1 = lab39.lab43c1 ");

                Object[] params = null;
                switch (searchType) {
                    case 0:
                        from.append(" WHERE lab39.lab39c37 != 2 AND lab57.lab57c34 BETWEEN ? AND ? ");
                        break;
                    case 1:
                        from.append(" WHERE lab39.lab39c37 != 2 AND lab22.lab22c1 BETWEEN ? AND ? ");
                        break;
                    case 3:
                        from.append(" WHERE lab39.lab39c37 != 2 AND lab22.lab22c2 BETWEEN ? AND ? ");
                        break;
                    default:
                        from.append(" WHERE lab39.lab39c37 != 2 AND lab22.lab22c1 IN(").append(orders.stream().map(order -> order.toString()).collect(Collectors.joining(","))).append(") ");
                        break;
                }

                if (tests != null && !tests.isEmpty()) {
                    if (tests.size() == 1) {
                        from.append(" AND lab39.lab39c1 = ").append(tests.get(0));
                    } else {
                        from.append(" AND lab39.lab39c1 IN (").append(tests.stream().map(test -> test.toString()).collect(Collectors.joining(","))).append(") ");
                    }
                }

                from.append(" AND lab22.lab07C1 != 0  AND (lab22c19 = 0 or lab22c19 is null) ");

                params = new Object[]{
                    vInitial, vFinal
                };

                RowMapper mapper = (RowMapper<Order>) (ResultSet rs, int i)
                        -> {
                    Order order = new Order();
                    order.setOrderNumber(rs.getLong("lab22c1"));
                    if (listOrders.contains(order)) {
                        Test test = new Test();
                        test.setId(rs.getInt("lab39c1"));

                        test.getArea().setId(rs.getString("lab43c1") == null ? null : rs.getInt("lab43c1"));
                        test.getArea().setAbbreviation(rs.getString("lab43c3"));
                        test.getArea().setName(rs.getString("lab43c4"));

                        listOrders.get(listOrders.indexOf(order)).getTests().add(test);
                    } else {
                        order.setCreatedDateShort(rs.getInt("lab22c2"));
                        order.setCreatedDate(rs.getTimestamp("lab22c3"));

                        //SEDE
                        order.getBranch().setId(rs.getInt("lab05c1"));
                        order.getBranch().setCode(rs.getString("lab05c10"));
                        order.getBranch().setName(rs.getString("lab05c4"));

                        if (rs.getString("lab39c1") != null) {
                            Test test = new Test();
                            test.setId(rs.getInt("lab39c1"));

                            test.getArea().setId(rs.getString("lab43c1") == null ? null : rs.getInt("lab43c1"));
                            test.getArea().setAbbreviation(rs.getString("lab43c3"));
                            test.getArea().setName(rs.getString("lab43c4"));

                            order.getTests().add(test);
                        }
                        listOrders.add(order);
                    }
                    return order;
                };
                if (searchType == 2) {
                    getConnection().query(query.toString() + " " + from.toString(), mapper);
                } else {
                    getConnection().query(query.toString() + "  " + from.toString() + "  ", mapper, params);
                }
            }
            return listOrders;
        } catch (EmptyResultDataAccessException ex) {
            return new ArrayList<>(0);
        }
    }

    /*
    **
     * Lista las ordenes por rango de fecha o numero de orden desde base de
     * datos.
     *
     * @param vInitial Rango Inicial.
     * @param vFinal Rango Final.
     * @param searchType Indica si la consulta se hace por rango de orden(1),
     * fechas(0) o Listado de ordenes(2)
     * @param demographics Lista de demograficos.
     * @param orders lista de ordenes a consultar searchType == 2
     * @param tests Lista de Examenes
     *
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    default Order orderBranch(Long orderNumber) throws Exception {
        try {
            Integer year = Tools.YearOfOrder(String.valueOf(orderNumber));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;

            return getConnection().queryForObject("" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT lab22.lab22c1, lab22c2, lab22c3, lab05.lab05c1, lab05c10, lab05c4, lab103.lab103c1, lab103c2, lab103c3, lab21c1, lab14c1, lab904c1 "
                    + "FROM  " + lab22 + " as lab22 "
                    + "INNER JOIN lab05 ON lab05.lab05c1 = lab22.lab05c1 "
                    + "INNER JOIN lab103 ON lab103.lab103c1 = lab22.lab103c1  "
                    + "WHERE lab22c1 = ?  ",
                    new Object[]{
                        orderNumber
                    }, (ResultSet rs, int i)
                    -> {
                Order order = new Order();
                order.setOrderNumber(rs.getLong("lab22c1"));
                order.setCreatedDateShort(rs.getInt("lab22c2"));

                Branch branch = new Branch();
                branch.setId(rs.getInt("lab05c1"));
                branch.setCode(rs.getString("lab05c10"));
                branch.setName(rs.getString("lab05c4"));
                order.setBranch(branch);

                //TIPO DE ORDEN
                order.getType().setId(rs.getString("lab103c1") == null ? null : rs.getInt("lab103c1"));
                order.getType().setCode(rs.getString("lab103c2"));
                order.getType().setName(rs.getString("lab103c3"));

                order.getPatient().setId(rs.getInt("lab21c1"));
                order.getAccount().setId(rs.getInt("lab14c1"));
                order.getRate().setId(rs.getInt("lab904c1"));

                return order;
            });
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }

    }

    default List<Order> orderbyDateDelete(Long vInitial, Long vFinal, int branch) throws Exception {
        try {
            List<Order> listOrders = new LinkedList<>();
            List<Integer> years = Tools.listOfConsecutiveYears(String.valueOf(vInitial), String.valueOf(vFinal));
            String lab22;
            int currentYear = DateTools.dateToNumberYear(new Date());
            for (Integer year : years) {
                lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;

                getConnection().query("" + ISOLATION_READ_UNCOMMITTED
                        + "SELECT lab22.lab22c1 , lab21.lab21c1, lab21c2, lab21c3, lab21c4, lab21c5, lab21c6 "
                        + "FROM  " + lab22 + " as lab22 "
                        + "INNER JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1  "
                        + "WHERE lab22.lab22c2 BETWEEN ? AND ? and lab07c1 = 0  AND (lab22c19 = 0 or lab22c19 is null) AND lab05c1 = ? ",
                        new Object[]{
                            vInitial, vFinal, branch
                        }, (ResultSet rs, int i)
                        -> {
                    Order order = new Order();
                    order.setOrderNumber(rs.getLong("lab22c1"));
                    order.getPatient().setId(rs.getInt("lab21c1"));
                    order.getPatient().setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                    order.getPatient().setName1(Tools.decrypt(rs.getString("lab21c3")));
                    order.getPatient().setName2(Tools.decrypt(rs.getString("lab21c4")));
                    order.getPatient().setLastName(Tools.decrypt(rs.getString("lab21c5")));
                    order.getPatient().setSurName(Tools.decrypt(rs.getString("lab21c6")));
                    listOrders.add(order);
                    return order;
                });
            }
            return listOrders;
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }

    }

    default List<Order> orderState(List<Long> orders) throws Exception {
        try {

            return getConnection().query("" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT lab22.lab22c1 , lab21.lab21c1, lab21c2, lab21c3, lab21c4, lab21c5, lab21c6, lab07c1 "
                    + "FROM lab22 "
                    + "INNER JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1  "
                    + "WHERE lab22.lab22c1  IN (" + orders.stream().map(o -> o.toString()).collect(Collectors.joining(",")) + ") ",
                    (ResultSet rs, int i)
                    -> {
                Order order = new Order();
                order.setOrderNumber(rs.getLong("lab22c1"));
                order.getPatient().setId(rs.getInt("lab21c1"));
                order.getPatient().setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                order.getPatient().setName1(Tools.decrypt(rs.getString("lab21c3")));
                order.getPatient().setName2(Tools.decrypt(rs.getString("lab21c4")));
                order.getPatient().setLastName(Tools.decrypt(rs.getString("lab21c5")));
                order.getPatient().setSurName(Tools.decrypt(rs.getString("lab21c6")));
                order.setState(rs.getInt("lab07c1"));
                return order;
            });
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }

    }

    /**
     * Lista ordenes sin paciente
     *
     * @param vInitial rango inicial
     * @param vFinal rango final
     * @param searchType tipo busqueda ( 0 - Fecha, 1 - Orden, 2 -Sin filtro)
     * @param branch
     *
     * @return
     * @throws Exception
     */
    default List<Order> listNoPatient(Long vInitial, Long vFinal, int searchType, int branch) throws Exception {
        try {
            String query = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT lab22c1, lab22c2, lab22c3, lab22c4, lab22c5, lab22.lab07c1, lab22c7, lab22c9, "
                    + "lab103.lab103c1, lab103c2, lab103c3, lab103c4, lab103.lab07c1 AS lab103lab07c1,  "
                    + "lab21.lab21c1, lab21c2, lab21c3, lab21c4, lab21c5, lab21c6,"
                    + "lab05.lab05c1, lab05c10, lab05c4, "
                    + "lab10.lab10c1, lab10c2, lab10c7,  "
                    + "lab22.lab04c1, lab04c2, lab04c3, lab04c4 ";
            String from = " "
                    + "FROM lab22 "
                    + "LEFT JOIN lab103 ON lab103.lab103c1 = lab22.lab103c1  "
                    + "LEFT JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1  "
                    + "LEFT JOIN lab54 lab21lab54 ON lab21lab54.lab54c1 = lab21.lab54c1  "
                    + "LEFT JOIN lab05 ON lab05.lab05c1 = lab22.lab05c1  "
                    + "LEFT JOIN lab10 ON lab10.lab10c1 = lab22.lab10c1  "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab22.lab04c1  ";

            String where = "WHERE lab22.lab07c1 != 0 AND Lab22.lab21c1 = 0  AND (lab22c19 = 0 or lab22c19 is null) AND lab22.lab05c1 = ? ";

            Object[] params = null;
            switch (searchType) {
                case 1:
                    where = where + " AND lab22c1 BETWEEN ? AND ?";
                    params = new Object[]{
                        branch, vInitial, vFinal
                    };
                    break;
                case 0:
                    where = where + " AND lab22c2 BETWEEN ? AND ?";
                    params = new Object[]{
                        branch, vInitial, vFinal
                    };
                    break;
                default:
                    break;
            }
            params = new Object[]{
                branch, vInitial, vFinal
            };

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
                //TIPO DE ORDEN
                order.getType().setId(rs.getString("lab103c1") == null ? null : rs.getInt("lab103c1"));
                order.getType().setCode(rs.getString("lab103c2"));
                order.getType().setName(rs.getString("lab103c3"));
                order.getType().setState(rs.getInt("lab103lab07c1") == 1);
                //PACIENTE
                order.getPatient().setId(rs.getInt("lab21c1"));
                //SEDE
                order.getBranch().setId(rs.getInt("lab05c1"));
                order.getBranch().setCode(rs.getString("lab05c10"));
                order.getBranch().setName(rs.getString("lab05c4"));
                //SERVICIO
                order.getService().setId(rs.getInt("lab10c1"));
                order.getService().setCode(rs.getString("lab10c7"));
                order.getService().setName(rs.getString("lab10c2"));

                return order;
            };
            if (searchType == 2) {
                return getConnection().query(query + from + where, mapper);
            } else {
                return getConnection().query(query + from + where, mapper, params);
            }
        } catch (EmptyResultDataAccessException ex) {
            return new ArrayList<>(0);
        }
    }

    /**
     * Lista de cabeceras de las ordenes por rango de fecha o numero de orden
     * desde base de datos.
     *
     * @param vInitial Rango Inicial.
     * @param vFinal Rango Final.
     * @param searchType Indica si la consulta se hace por rango de orden(1),
     * fechas(0) o Listado de ordenes(2)
     * @param demographics Lista de demograficos completa.
     * @param orders lista de ordenes a consultar searchType == 2
     * @param tests Lista de Examenes
     * @param orderingPrint Ordenamiento por order o historia
     * @param testFilterType
     * @param demographicsOrder Demograficos para ordenar
     *
     * @return Lista de cabeceras de las ordenes.
     * @throws Exception Error en la base de datos.
     */
    default List<Order> ordersHeader(Long vInitial, Long vFinal, int searchType, List<Long> orders, List<Integer> tests, int orderingPrint, final List<FilterDemographic> demographicsOrder, List<Demographic> demographics, int testFilterType) throws Exception {
        try {
            List<Order> listOrders = new LinkedList<>();
            // Consulta de ordenes por historico:
            List<Integer> years = Tools.listOfConsecutiveYears(String.valueOf(vInitial), String.valueOf(vFinal));
            String lab22;
            String lab57;
            String lab221;

            int currentYear = DateTools.dateToNumberYear(new Date());
            for (Integer year : years) {

                lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
                lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;
                lab221 = year.equals(currentYear) ? "lab221" : "lab221_" + year;

                StringBuilder query = new StringBuilder();
                query.append(ISOLATION_READ_UNCOMMITTED);
                query.append("SELECT ");
                query.append("lab22.lab22c1, lab04c2, lab04c3, lab04c4, lab221.lab22c1_1, lab22.lab22c16, ");
                query.append("lab22.lab21c1, lab21c2, lab21c3, lab21c4, lab21c5, lab21c6, ");
                query.append("lab21lab80.lab80c1 AS lab21lab80lab80c1, lab21lab80.lab80c2 AS lab21lab80lab80c2, ");
                query.append("lab21lab80.lab80c3 AS lab21lab80lab80c3, lab21lab80.lab80c4 AS lab21lab80lab80c4, ");
                query.append("lab21lab80.lab80c5 AS lab21lab80lab80c5, lab21c7, lab21c8, lab21c9, ");
                query.append("lab21c10, lab21c11, lab21c16, lab21c17, ");
                query.append("lab22.lab07c1, lab22c9, lab22.lab04c1, lab22c2, lab22c3, lab57c14 ");
                StringBuilder from = new StringBuilder();
                from.append(" FROM  ").append(lab57).append(" as lab57 ");
                from.append("INNER JOIN ").append(lab22).append(" as lab22 ON lab22.lab22c1 = lab57.lab22c1 ");
                from.append("INNER JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1  ");
                from.append("INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1 ");
                from.append("LEFT JOIN lab43 ON lab43.lab43c1 = lab39.lab43c1 ");
                from.append("INNER JOIN lab80 lab21lab80 ON lab21lab80.lab80c1 = lab21.lab80c1  ");
                from.append("LEFT JOIN lab04 ON lab04.lab04c1 = lab22.lab04c1  ");
                from.append("LEFT JOIN ").append(lab221).append(" as lab221 ON lab221.lab22c1_2 = lab22.lab22c1 ");
                String ordering = "";

                SQLTools.buildSQLDemographicSelect(demographics, query, from);

                List<Object> parametersList = new ArrayList<>();
                StringBuilder where = new StringBuilder("");
                switch (searchType) {
                    case 1:
                        where.append(" WHERE lab39.lab39c37 != 2 AND lab22.lab22c1 BETWEEN ? AND ? ");
                        break;
                    case 0:
                        where.append(" WHERE lab39.lab39c37 != 2 AND lab57.lab57c34 BETWEEN ? AND ? ");
                        break;
                    case 3:
                        where.append(" WHERE lab39.lab39c37 != 2 AND lab22.lab22c2 BETWEEN ? AND ? ");
                        break;
                    default:
                        where.append(" WHERE lab39.lab39c37 != 2 AND lab22.lab22c1 IN(").append(orders.stream().map(order -> order.toString()).collect(Collectors.joining(","))).append(") ");
                        break;
                }

                //Filtro por examenes confidenciales y areas
                switch (testFilterType) {
                    case 1:
                        where.append(" AND lab39.lab43c1 IN (").append(tests.stream().map(area -> area.toString()).collect(Collectors.joining(","))).append(") ");
                        break;
                    case 2:
                        String listtest = tests.stream().map(test -> test.toString()).collect(Collectors.joining(","));
                        where.append(" AND ((lab57.lab57c14 IN (").append(listtest).append(")) or lab39.lab39c1 IN (").append(listtest).append(")) ");
                        break;
                    case 3:
                        String listtestconfidencial = tests.stream().map(test -> test.toString()).collect(Collectors.joining(","));
                        where.append(" AND ((lab57.lab57c14 IN (").append(listtestconfidencial).append(")) or lab39.lab39c1 IN (").append(listtestconfidencial).append(")) ");
                        break;
                    default:
                        break;
                }

                parametersList.add(vInitial);
                parametersList.add(vFinal);

//                if (tests != null && !tests.isEmpty())
//                {
//                    if (tests.size() == 1)
//                    {
//                        where.append(" AND lab39.lab39c1 = ").append(tests.get(0));
//                    } else
//                    {
//                        where.append(" AND lab39.lab39c1 IN (").append(tests.stream().map(test -> test.toString()).collect(Collectors.joining(","))).append(") ");
//                    }
//                }
                if (demographicsOrder != null && !demographicsOrder.isEmpty()) {
                    demographicsOrder.forEach((demographic)
                            -> {
                        where.append(SQLTools.buildSQLDemographicFilter(demographic, parametersList));
                    });
                }

                where.append(" AND lab39.lab07c1 != ? AND lab22.lab21c1 != 0 AND lab22.lab07c1 = 1  AND (lab22c19 = 0 or lab22c19 is null) ");
                parametersList.add(LISEnum.ResultOrderState.CANCELED.getValue());

                Object[] parametersArr = new Object[parametersList.size()];
                parametersArr = parametersList.toArray(parametersArr);

                RowMapper mapper = (RowMapper<Order>) (ResultSet rs, int i)
                        -> {
                    Order order = new Order();
                    order.setOrderNumber(rs.getLong("lab22c1"));
                    order.setObservations(rs.getString("lab22c16"));
                    order.setCreatedDateShort(rs.getInt("lab22c2"));
                    order.setCreatedDate(rs.getTimestamp("lab22c3"));
                    order.setFatherOrder(rs.getLong("lab22c1_1"));
                    order.setState(rs.getInt("lab07c1"));
                    order.setPreviousState(rs.getString("lab22c9") == null ? null : rs.getInt("lab22c9"));
                    order.getLastUpdateUser().setId(rs.getInt("lab04c1"));
                    order.getLastUpdateUser().setName(rs.getString("lab04c2"));
                    order.getLastUpdateUser().setLastName(rs.getString("lab04c3"));
                    order.getLastUpdateUser().setUserName(rs.getString("lab04c4"));
                    //PACIENTE
                    order.getPatient().setId(rs.getInt("lab21c1"));
                    order.getPatient().setPatientId(Tools.decryptPatient(rs.getString("lab21c2")));
                    order.getPatient().setName1(Tools.decryptPatient(rs.getString("lab21c3")));
                    order.getPatient().setName2(Tools.decryptPatient(rs.getString("lab21c4")));
                    order.getPatient().setLastName(Tools.decryptPatient(rs.getString("lab21c5")));
                    order.getPatient().setSurName(Tools.decryptPatient(rs.getString("lab21c6")));
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

                    //MEDICO
                    if (query.toString().contains("lab19c1") == true && rs.getString("lab19c1") != null) {
                        order.getPhysician().setId(rs.getInt("lab19c1"));
                        order.getPhysician().setName(rs.getString("lab19c2"));
                        order.getPhysician().setCode(rs.getString("lab19c22"));
                        order.getPhysician().setLastName(rs.getString("lab19c3"));
                        order.getPhysician().setEmail(rs.getString("lab19c21"));
                        order.getPhysician().setAlternativeMails(rs.getString("lab19c24"));
                    }

                    if (query.toString().contains("lab05c1") == true && rs.getString("lab05c1") != null) {
                        //SEDE
                        order.getBranch().setId(rs.getInt("lab05c1"));
                        order.getBranch().setCode(rs.getString("lab05c10"));
                        order.getBranch().setName(rs.getString("lab05c4"));
                        order.getBranch().setEmail(rs.getString("lab05c12"));
                    }
                    if (query.toString().contains("lab10c1") == true && rs.getString("lab10c1") != null) {
                        //SERVICIO
                        order.getService().setId(rs.getInt("lab10c1"));
                        order.getService().setCode(rs.getString("lab10c7"));
                        order.getService().setName(rs.getString("lab10c2"));
                    }
                    if (query.toString().contains("lab19c1") == true && rs.getString("lab19c1") != null) {
                        //MEDICO
                        order.getPhysician().setId(rs.getInt("lab19c1"));
                        order.getPhysician().setName(rs.getString("lab19c2"));
                        order.getPhysician().setCode(rs.getString("lab19c22"));
                        order.getPhysician().setLastName(rs.getString("lab19c3"));
                        order.getPhysician().setEmail(rs.getString("lab19c21"));
                    }
                    if (query.toString().contains("lab14c1") == true && rs.getString("lab14c1") != null) {
                        //EMPRESA
                        order.getAccount().setId(rs.getInt("lab14c1"));
                        order.getAccount().setNit(rs.getString("lab14c2"));
                        order.getAccount().setName(rs.getString("lab14c3"));
                    }
                    if (query.toString().contains("lab904c1") == true && rs.getString("lab904c1") != null) {
                        //TARIFA
                        order.getRate().setId(rs.getInt("lab904c1"));
                        order.getRate().setCode(rs.getString("lab904c2"));
                        order.getRate().setName(rs.getString("lab904c3"));
                    }
                    if (query.toString().contains("lab21lab08lab08c1") == true && rs.getString("lab21lab08lab08c1") != null) {
                        //PACIENTE - RAZA
                        order.getPatient().getRace().setId(rs.getInt("lab21lab08lab08c1"));
                        order.getPatient().getRace().setCode(rs.getString("lab21lab08lab08c5"));
                        order.getPatient().getRace().setName(rs.getString("lab21lab08lab08c2"));
                        order.getPatient().getRace().setValue(rs.getFloat("lab21lab08lab08c4"));
                    }
                    if (query.toString().contains("lab21lab54lab54c1") == true && rs.getString("lab21lab54lab54c1") != null) {
                        //PACIENTE - TIPO DE DOCUMENTO
                        order.getPatient().getDocumentType().setId(rs.getInt("lab21lab54lab54c1"));
                        order.getPatient().getDocumentType().setAbbr(rs.getString("lab21lab54lab54c2"));
                        order.getPatient().getDocumentType().setName(rs.getString("lab21lab54lab54c3"));
                    }
                    if (query.toString().contains("lab103c1") == true && rs.getString("lab103c1") != null) {
                        //TIPO DE ORDEN
                        order.getType().setId(rs.getString("lab103c1") == null ? null : rs.getInt("lab103c1"));
                        order.getType().setCode(rs.getString("lab103c2"));
                        order.getType().setName(rs.getString("lab103c3"));
                        order.getType().setState(rs.getInt("lab103lab07c1") == 1);
                    }
                    //ORDER_HIS
                    if (query.toString().contains("lab22c7") == true) {
                        order.setExternalId(rs.getString("lab22c7"));
                    }
                    if (demographics != null) {
                        DemographicValue demoValue = null;
                        for (Demographic demographic : demographics) {
                            if (demographic.getOrigin() != null) {
                                demoValue = new DemographicValue();
                                demoValue.setIdDemographic(demographic.getId());
                                demoValue.setDemographic(demographic.getName());
                                demoValue.setEncoded(demographic.isEncoded());
                                if (demographic.isEncoded()) {
                                    if (rs.getString("demo" + demographic.getId() + "_id") != null) {
                                        demoValue.setCodifiedId(rs.getInt("demo" + demographic.getId() + "_id"));
                                        demoValue.setCodifiedCode(rs.getString("demo" + demographic.getId() + "_code"));
                                        demoValue.setCodifiedName(rs.getString("demo" + demographic.getId() + "_name"));
                                        demoValue.setCodifiedNameEnglish(rs.getString("demo" + demographic.getId() + "_nameenglish"));
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
                        }
                    }

                    listOrders.add(order);
                    return order;
                };

                if (searchType == 2) {
                    List<Order> orderOrdering = getConnection().query(query.toString() + from.toString() + ordering, mapper);
                    orderOrdering = orderOrdering.stream().distinct().collect(Collectors.toList());

                    return orderOrdering;
                } else {
                    List<Order> orderOrdering = getConnection().query(query.toString() + from.toString() + where.toString() + ordering, mapper, parametersArr);
                    orderOrdering = orderOrdering.stream()
                            .distinct()
                            .collect(Collectors.toList());
                    if (orderingPrint == 2) {
                        orderOrdering = orderOrdering.stream()
                                .sorted((Order o1, Order o2)
                                        -> {
                                    return o1.getPatient().getPatientId().compareTo(o2.getPatient().getPatientId());
                                }).collect(Collectors.toList());
                    }
                    return orderOrdering;
                }
            }
            return listOrders;
        } catch (EmptyResultDataAccessException ex) {
            return new ArrayList<>(0);
        }
    }

    /**
     * Informacion detallada o cuerpo de la orden desde base de datos.
     *
     * @param order Orden a adicionar informacion detallada
     * @param tests Lista de Examenes
     * @param demographics Lista de demograficos.
     *
     * @return Lista de cabeceras de las ordenes.
     * @throws Exception Error en la base de datos.
     */
    default Order ordersBody(Order order, List<Integer> tests, final List<Demographic> demographics) throws Exception {
        try {
            Integer year = Tools.YearOfOrder(String.valueOf(order.getOrderNumber()));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;
            String lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;

            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED);
            query.append("SELECT lab22c4, lab22c5, lab22.lab07c1, lab22c10, ");
            query.append("lab57.lab57c1, lab57.lab57c2, lab57.lab57c8, lab57.lab57c14, lab57.lab57c15, lab57c16, ");
            query.append("lab39.lab39c1, lab39.lab39c2, lab39.lab39c3, lab39.lab39c4, lab39.lab39c6,lab39.lab39c7,lab39.lab39c8, lab39.lab39c9, lab39.lab39c37, lab39.lab39c27, ");
            query.append("lab43.lab43c1, lab43c3, lab43.lab43c4, ");
            query.append("lab24.lab24c1, lab24c2, lab24c4, lab24c9, lab24.lab24c10, ");
            query.append("lab45.lab45c1, lab45.lab45c2, ");
            query.append("lab24.lab56c1, lab56.lab56c2, ");
            query.append("panel.lab39c1 AS panellab39c1, panel.lab39c2 AS panellab39c2, panel.lab39c3 AS panellab39c3, panel.lab39c4 AS panellab39c4, panel.lab39c37 AS panellab39c37, panel.lab39c6 AS panellab39c6,panel.lab39c7 AS panellab39c7, panel.lab39c8 AS panellab39c8, panel.lab39c9 AS panellab39c9, ");
            query.append("pack.lab39c1 AS packlab39c1, pack.lab39c2 AS packlab39c2, pack.lab39c3 AS packlab39c3, pack.lab39c4 AS packlab39c4, pack.lab39c37 AS packlab39c37,  pack.lab39c6 AS packlab39c6, pack.lab39c7 AS packlab39c7, pack.lab39c8 AS packlab39c8, pack.lab39c9 AS packlab39c9, ");
            query.append("lab57.lab40c1, lab40.lab40c2, lab40.lab40c3, ");
            query.append("lab32.lab37c1, lab32.lab32c1 ");
            StringBuilder from = new StringBuilder();
            from.append(" FROM  ").append(lab57).append(" as lab57 ");
            from.append(" INNER JOIN ").append(lab22).append(" as lab22 ON lab22.lab22c1 = lab57.lab22c1 ");
            from.append(" INNER JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1  ");
            from.append(" INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1 ");
            from.append(" LEFT JOIN lab39 panel ON panel.lab39c1 = lab57.lab57c14 ");
            from.append(" LEFT JOIN lab39 pack ON pack.lab39c1 = lab57.lab57c15 ");
            from.append(" LEFT JOIN lab43 ON lab43.lab43c1 = lab39.lab43c1 ");
            from.append(" LEFT JOIN lab24 ON lab24.lab24c1 = lab39.lab24c1 ");
            from.append(" LEFT JOIN lab45 ON lab45.lab45c1 = lab39.lab45c1 ");
            from.append(" LEFT JOIN lab40 ON lab40.lab40c1 = lab57.lab40c1 ");
            from.append(" LEFT JOIN lab56 ON lab56.lab56c1 = lab24.lab56c1 ");
            from.append(" LEFT JOIN lab32 ON lab32.lab22c1 = lab57.lab22c1 AND lab32.lab39c1 = lab57.lab39c1 ");
            SQLTools.buildSQLDemographicSelect(demographics, query, from);
            from.append(" WHERE lab39.lab39c37 != 2 AND lab22.lab22c1 = ? AND lab22.lab07c1 = 1  AND (lab22c19 = 0 or lab22c19 is null) ");
            Object[] params = null;
            if (tests != null && !tests.isEmpty()) {
                if (tests.size() == 1) {
                    from.append(" AND lab39.lab39c1 = ").append(tests.get(0));
                } else {
                    from.append(" AND lab39.lab39c1 IN (").append(tests.stream().map(test -> test.toString()).collect(Collectors.joining(","))).append(") ");
                }
            }

            params = new Object[]{
                order.getOrderNumber()
            };
            RowMapper mapper = (RowMapper) (ResultSet rs, int i)
                    -> {
                if (order.getBranch().getId() != null) {
                    Test test = new Test();
                    test.setId(rs.getInt("lab39c1"));
                    test.setCode(rs.getString("lab39c2"));
                    test.setAbbr(rs.getString("lab39c3"));
                    test.setName(rs.getString("lab39c4"));
                    test.getResult().setResult(Tools.decrypt(rs.getString("lab57c1")));
                    test.getResult().setDateResult(rs.getTimestamp("lab57c2"));
                    test.getResult().setState(rs.getInt("lab57c8"));
                    test.getResult().setSampleState(rs.getInt("lab57c16"));
                    test.setTestType(rs.getShort("lab39c37"));
                    test.setConfidential(rs.getInt("lab39c27") == 1);
                    test.setGender(new Item(rs.getInt("lab39c6")));
                    test.setUnitAge(rs.getShort("lab39c9"));
                    test.setMinAge(rs.getInt("lab39c7"));
                    test.setMaxAge(rs.getInt("lab39c8"));

                    test.getLaboratory().setId(rs.getInt("lab40c1"));
                    test.getLaboratory().setCode(rs.getInt("lab40c2"));
                    test.getLaboratory().setName(rs.getString("lab40c3"));
                    if (rs.getString("lab37c1") != null) {
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
                    panel.setGender(new Item(rs.getInt("panellab39c6")));
                    panel.setUnitAge(rs.getShort("panellab39c9"));
                    panel.setMinAge(rs.getInt("panellab39c7"));
                    panel.setMaxAge(rs.getInt("panellab39c8"));

                    test.setPanel(panel);

                    Test pack = new Test();
                    pack.setId(rs.getString("packlab39c1") == null ? null : rs.getInt("packlab39c1"));
                    pack.setCode(rs.getString("packlab39c2"));
                    pack.setAbbr(rs.getString("packlab39c3"));
                    pack.setName(rs.getString("packlab39c4"));
                    pack.setTestType(rs.getShort("packlab39c37"));
                    pack.setGender(new Item(rs.getInt("packlab39c6")));
                    pack.setUnitAge(rs.getShort("packlab39c9"));
                    pack.setMinAge(rs.getInt("packlab39c7"));
                    pack.setMaxAge(rs.getInt("packlab39c8"));
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

                    order.getTests().add(test);
                } else {
                    order.setInconsistency(rs.getInt("lab22c10") == 1);
                    order.setHomebound(rs.getInt("lab22c4") == 1);
                    order.setMiles(rs.getInt("lab22c5"));
                    //COMENTARIOS
                    order.setComments(getCommentDao().listCommentOrder(order.getOrderNumber(), null));
                    order.getPatient().setDiagnostic(getCommentDao().listCommentOrder(null, order.getPatient().getId()));
                    if (query.toString().contains("lab05c1") == true && rs.getString("lab05c1") != null) {
                        //SEDE
                        order.getBranch().setId(rs.getInt("lab05c1"));
                        order.getBranch().setCode(rs.getString("lab05c10"));
                        order.getBranch().setName(rs.getString("lab05c4"));
                    }
                    if (query.toString().contains("lab10c1") == true && rs.getString("lab10c1") != null) {
                        //SERVICIO
                        order.getService().setId(rs.getInt("lab10c1"));
                        order.getService().setCode(rs.getString("lab10c7"));
                        order.getService().setName(rs.getString("lab10c2"));
                    }
                    if (query.toString().contains("lab19c1") == true && rs.getString("lab19c1") != null) {
                        //MEDICO
                        order.getPhysician().setId(rs.getInt("lab19c1"));
                        order.getPhysician().setName(rs.getString("lab19c2"));
                        order.getPhysician().setCode(rs.getString("lab19c22"));
                        order.getPhysician().setLastName(rs.getString("lab19c3"));
                        order.getPhysician().setEmail(rs.getString("lab19c21"));
                    }
                    if (query.toString().contains("lab14c1") == true && rs.getString("lab14c1") != null) {
                        //EMPRESA
                        order.getAccount().setId(rs.getInt("lab14c1"));
                        order.getAccount().setNit(rs.getString("lab14c2"));
                        order.getAccount().setName(rs.getString("lab14c3"));
                    }
                    if (query.toString().contains("lab904c1") == true && rs.getString("lab904c1") != null) {
                        //TARIFA
                        order.getRate().setId(rs.getInt("lab904c1"));
                        order.getRate().setCode(rs.getString("lab904c2"));
                        order.getRate().setName(rs.getString("lab904c3"));
                    }
                    if (query.toString().contains("lab21lab08lab08c1") == true && rs.getString("lab21lab08lab08c1") != null) {
                        //PACIENTE - RAZA
                        order.getPatient().getRace().setId(rs.getInt("lab21lab08lab08c1"));
                        order.getPatient().getRace().setCode(rs.getString("lab21lab08lab08c5"));
                        order.getPatient().getRace().setName(rs.getString("lab21lab08lab08c2"));
                        order.getPatient().getRace().setValue(rs.getFloat("lab21lab08lab08c4"));
                    }
                    if (query.toString().contains("lab21lab54lab54c1") == true && rs.getString("lab21lab54lab54c1") != null) {
                        //PACIENTE - TIPO DE DOCUMENTO
                        order.getPatient().getDocumentType().setId(rs.getInt("lab21lab54lab54c1"));
                        order.getPatient().getDocumentType().setAbbr(rs.getString("lab21lab54lab54c2"));
                        order.getPatient().getDocumentType().setName(rs.getString("lab21lab54lab54c3"));
                    }
                    if (query.toString().contains("lab103c1") == true && rs.getString("lab103c1") != null) {
                        //TIPO DE ORDEN
                        order.getType().setId(rs.getString("lab103c1") == null ? null : rs.getInt("lab103c1"));
                        order.getType().setCode(rs.getString("lab103c2"));
                        order.getType().setName(rs.getString("lab103c3"));
                        order.getType().setState(rs.getInt("lab103lab07c1") == 1);
                    }
                    //ORDER_HIS
                    if (query.toString().contains("lab22c7") == true) {
                        order.setExternalId(rs.getString("lab22c7"));
                    }
                    if (demographics != null) {
                        DemographicValue demoValue = null;
                        for (Demographic demographic : demographics) {
                            if (demographic.getOrigin() != null) {
                                demoValue = new DemographicValue();
                                demoValue.setIdDemographic(demographic.getId());
                                demoValue.setDemographic(demographic.getName());
                                demoValue.setEncoded(demographic.isEncoded());
                                if (demographic.isEncoded()) {
                                    if (rs.getString("demo" + demographic.getId() + "_id") != null) {
                                        demoValue.setCodifiedId(rs.getInt("demo" + demographic.getId() + "_id"));
                                        demoValue.setCodifiedCode(rs.getString("demo" + demographic.getId() + "_code"));
                                        demoValue.setCodifiedName(rs.getString("demo" + demographic.getId() + "_name"));
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
                        }
                    }

                    if (rs.getString("lab39c1") != null) {
                        Test test = new Test();
                        test.setId(rs.getInt("lab39c1"));
                        test.setCode(rs.getString("lab39c2"));
                        test.setAbbr(rs.getString("lab39c3"));
                        test.setName(rs.getString("lab39c4"));
                        test.getResult().setResult(Tools.decrypt(rs.getString("lab57c1")));
                        test.getResult().setDateResult(rs.getTimestamp("lab57c2"));
                        test.getResult().setState(rs.getInt("lab57c8"));
                        test.getResult().setSampleState(rs.getInt("lab57c16"));
                        test.setTestType(rs.getShort("lab39c37"));
                        test.setConfidential(rs.getInt("lab39c27") == 1);
                        test.setGender(new Item(rs.getInt("lab39c6")));
                        test.setUnitAge(rs.getShort("lab39c9"));
                        test.setMinAge(rs.getInt("lab39c7"));
                        test.setMaxAge(rs.getInt("lab39c8"));

                        test.getLaboratory().setId(rs.getInt("lab40c1"));
                        test.getLaboratory().setCode(rs.getInt("lab40c2"));
                        test.getLaboratory().setName(rs.getString("lab40c3"));
                        if (rs.getString("lab37c1") != null) {
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
                        panel.setGender(new Item(rs.getInt("panellab39c6")));
                        panel.setUnitAge(rs.getShort("panellab39c9"));
                        panel.setMinAge(rs.getInt("panellab39c7"));
                        panel.setMaxAge(rs.getInt("panellab39c8"));

                        test.setPanel(panel);

                        Test pack = new Test();
                        pack.setId(rs.getString("packlab39c1") == null ? null : rs.getInt("packlab39c1"));
                        pack.setCode(rs.getString("packlab39c2"));
                        pack.setAbbr(rs.getString("packlab39c3"));
                        pack.setName(rs.getString("packlab39c4"));
                        pack.setTestType(rs.getShort("packlab39c37"));
                        pack.setGender(new Item(rs.getInt("packlab39c6")));
                        pack.setUnitAge(rs.getShort("packlab39c9"));
                        pack.setMinAge(rs.getInt("packlab39c7"));
                        pack.setMaxAge(rs.getInt("packlab39c8"));
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

                        order.getTests().add(test);
                    }
                }
                return null;
            };
            getConnection().query(query.toString() + from.toString(), mapper, params);
            return order;
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    /**
     * Lista los examenes pendientes en un rango de ordenes especificado
     *
     *
     * @param idOrder
     * @param idTest
     * @return 1 si el examen de esa orden esta en estado pendiente - 0 si esta
     * en cualquier otro estado
     * @throws Exception Error en la base de datos.
     */
    default int listPendingExams(long idOrder, int idTest) throws Exception {
        try {
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED);
            query.append("SELECT 1 AS Pending ")
                    .append("FROM lab57 ")
                    .append("WHERE lab22c1 = ").append(idOrder)
                    .append(" AND lab39c1 = ").append(idTest)
                    .append(" AND lab57c16 = -1");

            return getConnection().queryForObject(query.toString(),
                    (ResultSet rs, int i)
                    -> {
                return rs.getInt("Pending");
            });
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Obtendra una lista de ordenes dentro del rango de una orden inicial hasta
     * una orden final
     *
     * @param initialOrder
     * @param finalOrder
     * @return
     * @throws Exception Error al retornar el rango de ordenes especificado
     */
    default List<Long> getListOrders(long initialOrder, long finalOrder) throws Exception {
        try {
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED);
            query.append("SELECT DISTINCT lab57.lab22c1 AS id_order_results, lab22.lab22c1 AS id_order_orders ")
                    .append(" FROM lab57 ")
                    .append(" INNER JOIN lab22 ON lab57.lab22c1 = lab22.lab22c1 ")
                    .append(" WHERE lab57.lab22c1 >= ").append(initialOrder)
                    .append(" AND lab57.lab22c1 <= ").append(finalOrder)
                    .append(" AND lab57c8 > 3 ")
                    .append(" AND (lab22c19 = 0 or lab22c19 is null) ");

            return getConnection().query(query.toString(),
                    (ResultSet rs, int i)
                    -> {
                return rs.getLong("id_order_results");
            });

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Obtiene todos los examenes de una orden
     *
     * @param order Numero de la orden
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.masters.test.Test}, vacia en caso
     * de no encontrarse
     * @throws Exception Error en base de datos
     */
    default List<Test> getListTest(long order) throws Exception {
        try {
            Integer year = Tools.YearOfOrder(String.valueOf(order));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;

            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED);
            query.append("SELECT lab57.lab22c1")
                    .append(", lab57.lab39c1") //examen
                    .append(", lab57.lab57c39") //Fecha Toma
                    .append(", lab39.lab39c2") //código
                    .append(", lab39.lab39c4") //nombre
                    .append(", lab39.lab39c26") //ver en consultas
                    .append(", lab40.lab40c1") //id del laboratorio
                    .append(", lab40.lab40c10") //url del laboratorio
                    .append(", lab40.lab40c11") //Si se valida al ingreso
                    .append(", lab40.lab40c12") //Si se valida en verificacion
                    .append(", lt.lab24c1 AS ltlab24c1") //muestra id
                    .append(", lt.lab24c9 AS ltlab24c9") //muestra codigo
                    .append(", lt.lab24c2 AS ltlab24c2") //muestra nombre
                    .append(", lt.lab24c10") //tipo laboratorio
                    .append(", lab16.lab16c3, lab11.lab11c1 ")
                    .append(" FROM ").append(lab57).append(" as lab57 ") //Resultados
                    .append(" INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1") //Examen
                    .append(" LEFT JOIN lab40 ON lab40.lab40c1 = lab57.lab40c1") // Laboratorio
                    .append(" INNER JOIN lab24 AS lt ON lt.lab24c1 = lab39.lab24c1") // Muestra                    
                    .append(" LEFT JOIN lab11 ON lab11.lab24c1 = lab57.lab24c1 AND lab11.lab22c1 = lab57.lab22c1 ")
                    .append(" LEFT JOIN lab16 ON lab16.lab16c1 = lab11.lab16c1 ")
                    .append(" WHERE lab57c14 is null ")
                    .append(" AND lab57.lab22c1 = ? ");

            return getConnection().query(query.toString(),
                    (ResultSet rs, int i)
                    -> {
                Test test = new Test();
                test.setId(rs.getInt("lab39c1"));
                test.setCode(rs.getString("lab39c2"));
                test.setName(rs.getString("lab39c4"));
                test.setRackStore(rs.getString("lab16c3"));
                test.setPositionStore(rs.getString("lab11c1"));
                test.setShowInQuery(rs.getInt("lab39c26") == 1);
                Laboratory laboratory = new Laboratory();
                laboratory.setId(rs.getInt("lab40c1"));
                laboratory.setUrl(rs.getString("lab40c10"));
                laboratory.setEntry(rs.getBoolean("lab40c11"));
                laboratory.setCheck(rs.getBoolean("lab40c12"));
                test.setLaboratory(laboratory);
                Sample sample = new Sample();
                sample.setId(rs.getInt("ltlab24c1"));
                sample.setCodesample(rs.getString("ltlab24c9"));
                sample.setName(rs.getString("ltlab24c2"));
                sample.setLaboratorytype(rs.getString("lab24c10"));
                sample.setTakeDate(rs.getTimestamp("lab57c39"));
                test.setSample(sample);
                return test;
            }, order);
        } catch (EmptyResultDataAccessException ex) {
            return new ArrayList<>(0);
        }
    }

    /**
     * Setea las sedes dentro del objeto test
     *
     * @param orderNumber
     * @param sampleId
     * @param laboratoryId
     * @return
     * @throws Exception Error al retornar el rango de ordenes especificado
     */
    default List<TestBranchCheck> setTestCheckByBranch(long orderNumber, int sampleId, int laboratoryId) throws Exception {
        try {
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED);
            query.append("SELECT lab182.lab05c1 branchId, lab05c4, lab159.lab05c1 AS branchCode ")
                    .append(" FROM lab182 ")
                    .append(" LEFT JOIN lab159 ON lab182.lab05c1 = lab159.lab05c1 and lab22c1 = ").append(orderNumber)
                    .append(" AND lab24c1 = ").append(sampleId)
                    .append(" AND lab159c1 = 4 ")
                    .append(" LEFT JOIN lab05 on lab05.lab05c1 = lab182.lab05c1 ")
                    .append(" WHERE lab40c1 = ").append(laboratoryId);

            return getConnection().query(query.toString(),
                    (ResultSet rs, int i)
                    -> {

                TestBranchCheck testCheckByBranch = new TestBranchCheck();

                testCheckByBranch.setBranchId(rs.getInt("branchId"));
                testCheckByBranch.setBranchName(rs.getString("lab05c4"));
                testCheckByBranch.setSelected(rs.getString("branchCode") == null ? false : true);

                return testCheckByBranch;
            });

        } catch (Exception e) {
            return null;
        }
    }

    //
    /**
     * Identificar si un examen tiene un perfil asociado y este perfil tiene
     * muestra.
     *
     * @param idOrder
     * @param idTest
     * @return Si ya existe creado el campo.
     * @throws Exception Error en la base de datos.
     */
    default int containProfile(long idOrder, int idTest) throws Exception {
        try {
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED);
            query.append("SELECT lab24c1 AS sample ")
                    .append("FROM lab57 WHERE lab39c1 IN (SELECT lab57c14 FROM lab57 WHERE lab22c1 = ").append(idOrder)
                    .append(" AND lab39c1 = ").append(idTest)
                    .append(" ) AND  lab22c1 = ").append(idOrder);

            return getConnection().queryForObject(query.toString(),
                    (ResultSet rs, int i)
                    -> {
                return rs.getInt("sample");
            });
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Lista las entrevistas de panicos por un filtro especifico. desde base de
     * datos.
     *
     * @param orders Lista de ordenes
     *
     * @return Lista de entrevistas
     * @throws Exception Error en la base de datos.
     */
    default List<PanicInterview> getPanicInterview(List<Long> orders) throws Exception {
        try {
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED);

            query.append("SELECT lab179.lab22c1, lab21c2, lab21c3, lab21c4, lab21c5, lab21c6, ");
            query.append("lab179.lab39c1, lab39c2, lab39c4,   ");
            query.append("lab179.lab70c1, lab70c3,   ");
            query.append("lab179.lab90c1, lab90c2,    ");
            query.append("lab179.lab179c1, ");
            query.append("lab57c2,    ");
            query.append("lab57c18,    ");
            query.append("lab179c2, lab179c3, lab179c4, lab179c5,    ");
            query.append("lab179.lab04c1, lab04c4    ");

            StringBuilder from = new StringBuilder();

            from.append(" FROM lab179 ");
            from.append("LEFT JOIN lab22 ON lab22.lab22c1 = lab179.lab22c1 ");
            from.append("LEFT JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1 ");
            from.append("LEFT JOIN lab39 ON lab39.lab39c1 = lab179.lab39c1 ");
            from.append("INNER JOIN lab57 ON lab57.lab39c1 = lab179.lab39c1 AND lab57.lab22c1 = lab179.lab22c1 ");
            from.append("LEFT JOIN lab70 ON lab70.lab70c1 = lab179.lab70c1 ");
            from.append("LEFT JOIN lab90 ON lab90.lab90c1 = lab179.lab90c1 ");
            from.append("LEFT JOIN lab04 ON lab04.lab04c1 = lab179.lab04c1  ");

            from.append(" WHERE lab179.lab22c1 in (").append(orders.stream().map(order -> order.toString()).collect(Collectors.joining(","))).append(")");

            return getConnection().query(query.toString() + from,
                    (ResultSet rs, int i)
                    -> {

                PanicInterview interview = new PanicInterview();
                interview.setOrder(rs.getLong("lab22c1"));
                interview.setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                interview.setName1(Tools.decrypt(rs.getString("lab21c3")));
                interview.setName2(Tools.decrypt(rs.getString("lab21c4")));
                interview.setLastName(Tools.decrypt(rs.getString("lab21c5")));
                interview.setSurName(Tools.decrypt(rs.getString("lab21c6")));
                interview.setTestId(rs.getInt("lab39c1"));
                interview.setTestCode(rs.getString("lab39c2"));
                interview.setTestName(rs.getString("lab39c4"));
                interview.setQuestionId(rs.getInt("lab70c1"));
                interview.setQuestion(rs.getString("lab70c3"));
                interview.setAnswerId(rs.getInt("lab90c1"));
                interview.setAnswerClose(rs.getString("lab90c2"));
                interview.setAnswer(rs.getString("lab179c1"));
                interview.setDate(rs.getTimestamp("lab179c2"));
                interview.setDateValidated(rs.getTimestamp("lab57c18"));
                interview.setPanic(rs.getInt("lab179c3"));
                interview.setDelta(rs.getInt("lab179c4"));
                interview.setCritic(rs.getInt("lab179c5"));
                interview.setUserId(rs.getInt("lab04c1"));
                interview.setUsername(rs.getString("lab04c4"));

                return interview;
            });
        } catch (EmptyResultDataAccessException ex) {
            return new ArrayList<>(0);
        }
    }

    /**
     * Lista las alarmas de panico no atendidas desde base de datos.
     *
     * @param orders Lista de ordenes
     * @param interviewpanic
     *
     *
     * @return Lista de entrevistas
     * @throws Exception Error en la base de datos.
     */
    default List<PanicInterview> getPanicUnattended(List<Long> orders, Interview interviewpanic) throws Exception {
        try {
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED);

            query.append("SELECT lab57.lab22c1, lab21c2, lab21c3, lab21c4, lab21c5, lab21c6, ");
            query.append("lab57.lab39c1, lab39c2, lab39c4   ");

            StringBuilder from = new StringBuilder();

            from.append(" FROM lab57 ");
            from.append("LEFT JOIN lab22 ON lab22.lab22c1 = lab57.lab22c1 ");
            from.append("LEFT JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1 ");
            from.append("LEFT JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1 ");
            from.append("LEFT JOIN lab179 ON lab179.lab39c1 = lab57.lab39c1 AND lab179.lab22c1 = lab57.lab22c1 ");
            from.append(" INNER JOIN lab66 ON lab66.lab66c1=lab39.lab39c1 AND lab66.lab44c1=").append(interviewpanic.getId());
            from.append(" WHERE lab57.lab57c9 >=4 AND lab179.lab22c1 IS NULL AND lab179.lab39c1 IS NULL  ");
            from.append(" AND lab57.lab22c1 in (").append(orders.stream().map(order -> order.toString()).collect(Collectors.joining(","))).append(")");

            return getConnection().query(query.toString() + from,
                    (ResultSet rs, int i)
                    -> {

                PanicInterview interview = new PanicInterview();
                interview.setOrder(rs.getLong("lab22c1"));
                interview.setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                interview.setName1(Tools.decrypt(rs.getString("lab21c3")));
                interview.setName2(Tools.decrypt(rs.getString("lab21c4")));
                interview.setLastName(Tools.decrypt(rs.getString("lab21c5")));
                interview.setTestId(rs.getInt("lab39c1"));
                interview.setTestCode(rs.getString("lab39c2"));
                interview.setTestName(rs.getString("lab39c4"));
                return interview;
            });
        } catch (EmptyResultDataAccessException ex) {
            return new ArrayList<>(0);
        }
    }

    /**
     * Lista las ordenes por rango de fecha o numero de orden desde base de
     * datos.
     *
     * @param vInitial Rango Inicial.
     * @param vFinal Rango Final.
     * @param demographics Lista de demograficos.
     *
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    default List<OrderReportINS> listOrderRange(Long vInitial, Long vFinal, List<Demographic> demographics) throws Exception {
        try {
            List<OrderReportINS> listOrders = new LinkedList<>();
            // Consulta de ordenes por historico:
            List<Integer> years = Tools.listOfConsecutiveYears(String.valueOf(vInitial), String.valueOf(vFinal));
            String lab22;
            String lab57;
            int currentYear = DateTools.dateToNumberYear(new Date());
            for (Integer year : years) {
                lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
                lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;

                // Query
                StringBuilder query = new StringBuilder();
                query.append(ISOLATION_READ_UNCOMMITTED);
                query.append("SELECT lab22.lab22c1, lab22c2, lab22c3, lab22c4, lab22c5, lab22.lab07c1, lab22c7, lab22c9, lab22c10, ");
                query.append("lab103.lab103c1, lab103c2, lab103c3, lab103c4, lab103.lab07c1 AS lab103lab07c1,  ");
                query.append("lab21.lab21c1, lab21c2, lab21c3, lab21c4, lab21c5, lab21c6, lab21lab80.lab80c1 AS lab21lab80lab80c1, lab21lab80.lab80c2 AS lab21lab80lab80c2, lab21lab80.lab80c3 AS lab21lab80lab80c3, lab21lab80.lab80c4 AS lab21lab80lab80c4, lab21lab80.lab80c5 AS lab21lab80lab80c5, lab21c7, lab21c8, lab21c9, lab21c10, lab21c11, lab21c16, lab21c17, lab21lab08.lab08c1 AS lab21lab08lab08c1, lab21lab08.lab08c2 AS lab21lab08lab08c2, lab21lab08.lab08c4 AS lab21lab08lab08c4, lab21lab08.lab08c5 AS lab21lab08lab08c5, lab21lab54.lab54c1 AS lab21lab54lab54c1, lab21lab54.lab54c2 AS lab21lab54lab54c2, lab21lab54.lab54c3 AS lab21lab54lab54c3, ");
                query.append("lab05.lab05c1, lab05c10, lab05c4, ");
                query.append("lab10.lab10c1, lab10c2, lab10c7,  ");
                query.append("lab14.lab14c1, lab14c2, lab14c3, lab14c32, ");
                query.append("lab19.lab19c1, lab19c2, lab19c3, lab19c21, ");
                query.append("lab22.lab04c1, lab04c2, lab04c3, lab04c4, ");
                query.append("lab904.lab904c1, lab904c2, lab904c3, ");
                query.append("lab57.lab22c1, lab57.lab57c1, lab57.lab57c2, lab57.lab57c8, lab57.lab57c14, lab57.lab57c15, lab57c16, ");
                query.append("lab39.lab39c1, lab39.lab39c2, lab39.lab39c3, lab39.lab39c4, lab39.lab39c6,lab39.lab39c7,lab39.lab39c8, lab39.lab39c9, lab39.lab39c37, lab39.lab39c27, ");
                query.append("lab43.lab43c1, lab43c3, lab43.lab43c4, ");
                query.append("lab24.lab24c1, lab24c2, lab24c4, lab24c9, lab24.lab24c10, lab24c6, ");
                query.append("lab45.lab45c1, lab45.lab45c2, ");
                query.append("lab24.lab56c1, lab56.lab56c2, ");
                query.append("panel.lab39c1 AS panellab39c1, panel.lab39c2 AS panellab39c2, panel.lab39c3 AS panellab39c3, panel.lab39c4 AS panellab39c4, panel.lab39c37 AS panellab39c37, panel.lab39c6 AS panellab39c6,panel.lab39c7 AS panellab39c7, panel.lab39c8 AS panellab39c8, panel.lab39c9 AS panellab39c9, lab39.lab39c39 AS panellab39c39, ");
                query.append("pack.lab39c1 AS packlab39c1, pack.lab39c2 AS packlab39c2, pack.lab39c3 AS packlab39c3, pack.lab39c4 AS packlab39c4, pack.lab39c37 AS packlab39c37,  pack.lab39c6 AS packlab39c6, pack.lab39c7 AS packlab39c7, pack.lab39c8 AS packlab39c8, pack.lab39c9 AS packlab39c9, lab39.lab39c39 AS packlab39c39, ");
                query.append("lab57.lab40c1, lab40.lab40c2, lab40.lab40c3, ");
                query.append("lab57.lab40c1a as idorigin, origin.lab40c2 as codeorigin, origin.lab40c3 as nameorigin, ");
                query.append("lab16.lab16c3, lab11.lab11c1,");
                query.append("lab32.lab37c1, lab32.lab32c1, ");
                query.append("lab57.lab57c54, lab57.lab57c39 ");
                StringBuilder from = new StringBuilder();
                from.append(" FROM ").append(lab57).append(" AS lab57 ");
                from.append("INNER JOIN ").append(lab22).append(" AS lab22 ON lab22.lab22c1 = lab57.lab22c1 ");
                from.append("INNER JOIN lab05 ON lab05.lab05c1 = lab22.lab05c1  ");
                from.append("INNER JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1  ");
                from.append("INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1 AND lab39.lab39c26 = 1");
                from.append("INNER JOIN lab80 lab21lab80 ON lab21lab80.lab80c1 = lab21.lab80c1  ");
                from.append("INNER JOIN lab103 ON lab103.lab103c1 = lab22.lab103c1  ");
                from.append("LEFT JOIN lab08 lab21lab08 ON lab21lab08.lab08c1 = lab21.lab08c1  ");
                from.append("LEFT JOIN lab54 lab21lab54 ON lab21lab54.lab54c1 = lab21.lab54c1  ");
                from.append("LEFT JOIN lab10 ON lab10.lab10c1 = lab22.lab10c1  ");
                from.append("LEFT JOIN lab14 ON lab14.lab14c1 = lab22.lab14c1  ");
                from.append("LEFT JOIN lab19 ON lab19.lab19c1 = lab22.lab19c1  ");
                from.append("LEFT JOIN lab04 ON lab04.lab04c1 = lab22.lab04c1  ");
                from.append("LEFT JOIN lab904 ON lab904.lab904c1 = lab22.lab904c1  ");
                from.append("LEFT JOIN lab39 panel ON panel.lab39c1 = lab57.lab57c14 ");
                from.append("LEFT JOIN lab39 pack ON pack.lab39c1 = lab57.lab57c15 ");
                from.append("LEFT JOIN lab43 ON lab43.lab43c1 = lab39.lab43c1 ");
                from.append("LEFT JOIN lab24 ON lab24.lab24c1 = lab39.lab24c1 ");
                from.append("LEFT JOIN lab45 ON lab45.lab45c1 = lab39.lab45c1 ");
                from.append("LEFT JOIN lab40 ON lab40.lab40c1 = lab57.lab40c1 ");
                from.append("LEFT JOIN lab56 ON lab56.lab56c1 = lab24.lab56c1 ");
                from.append("LEFT JOIN lab11 ON lab11.lab24c1 = lab57.lab24c1 AND lab11.lab22c1 = lab57.lab22c1 ");
                from.append("LEFT JOIN lab16 ON lab16.lab16c1 = lab11.lab16c1 ");
                from.append("LEFT JOIN lab32 ON lab32.lab22c1 = lab57.lab22c1 AND lab32.lab39c1 = lab57.lab39c1 ");
                from.append("LEFT JOIN lab40 origin ON origin.lab40c1 = lab57.lab40c1a ");

                for (Demographic demographic : demographics) {
                    if (demographic.getOrigin().equals("O")) {
                        if (demographic.isEncoded()) {
                            query.append(", demo").append(demographic.getId()).append(".lab63c1 as demo").append(demographic.getId()).append("_id ");
                            query.append(", demo").append(demographic.getId()).append(".lab63c2 as demo").append(demographic.getId()).append("_code ");
                            query.append(", demo").append(demographic.getId()).append(".lab63c3 as demo").append(demographic.getId()).append("_name ");

                            from.append(" LEFT JOIN Lab63 demo").append(demographic.getId()).append(" ON lab22.lab_demo_").append(demographic.getId()).append(" = demo").append(demographic.getId()).append(".lab63c1 ");
                        } else {
                            query.append(", lab22.lab_demo_").append(demographic.getId());
                        }
                    } else {
                        if (demographic.getOrigin().equals("H")) {
                            if (demographic.isEncoded()) {
                                query.append(", demo").append(demographic.getId()).append(".lab63c1 as demo").append(demographic.getId()).append("_id ");
                                query.append(", demo").append(demographic.getId()).append(".lab63c2 as demo").append(demographic.getId()).append("_code ");
                                query.append(", demo").append(demographic.getId()).append(".lab63c3 as demo").append(demographic.getId()).append("_name ");

                                from.append(" LEFT JOIN Lab63 demo").append(demographic.getId()).append(" ON lab21.lab_demo_").append(demographic.getId()).append(" = demo").append(demographic.getId()).append(".lab63c1 ");
                            } else {
                                query.append(", lab21.lab_demo_").append(demographic.getId());
                            }
                        }
                    }
                }
                Object[] params = null;
                from.append(" WHERE lab39.lab39c37 != 2 AND lab22.lab22c2 BETWEEN ? AND ? ");

                from.append(" AND lab22.lab07C1 != 0  AND (lab22c19 = 0 or lab22c19 is null) ");

                params = new Object[]{
                    vInitial, vFinal
                };

                RowMapper mapper = (RowMapper<OrderReportINS>) (ResultSet rs, int i)
                        -> {
                    OrderReportINS order = new OrderReportINS();
                    order.setOrderNumber(rs.getLong("lab22c1"));
                    if (listOrders.contains(order)) {
                        Test test = new Test();
                        test.setId(rs.getInt("lab39c1"));
                        test.setCode(rs.getString("lab39c2"));
                        test.setAbbr(rs.getString("lab39c3"));
                        test.setName(rs.getString("lab39c4"));
                        test.getResult().setResult(Tools.decrypt(rs.getString("lab57c1")));
                        test.getResult().setDateResult(rs.getTimestamp("lab57c2"));
                        test.getResult().setState(rs.getInt("lab57c8"));
                        test.getResult().setSampleState(rs.getInt("lab57c16"));
                        test.setTestType(rs.getShort("lab39c37"));
                        test.setConfidential(rs.getInt("lab39c27") == 1);
                        test.setGender(new Item(rs.getInt("lab39c6")));
                        test.setUnitAge(rs.getShort("lab39c9"));
                        test.setMinAge(rs.getInt("lab39c7"));
                        test.setMaxAge(rs.getInt("lab39c8"));
                        test.setRemission(rs.getInt("lab57c54"));
                        test.setProfile(rs.getInt("lab57c14"));
                        test.getLaboratory().setId(rs.getInt("lab40c1"));
                        test.getLaboratory().setCode(rs.getInt("lab40c2"));
                        test.getLaboratory().setName(rs.getString("lab40c3"));
                        test.getOriginRemission().setId(rs.getInt("idorigin"));
                        test.getOriginRemission().setCode(rs.getInt("codeorigin"));
                        test.getOriginRemission().setName(rs.getString("nameorigin"));
                        test.setExcluideTestProfile(rs.getInt("panellab39c39"));
                        if (rs.getString("lab37c1") != null) {
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
                        panel.setGender(new Item(rs.getInt("panellab39c6")));
                        panel.setUnitAge(rs.getShort("panellab39c9"));
                        panel.setMinAge(rs.getInt("panellab39c7"));
                        panel.setMaxAge(rs.getInt("panellab39c8"));

                        test.setPanel(panel);

                        Test pack = new Test();
                        pack.setId(rs.getString("packlab39c1") == null ? null : rs.getInt("packlab39c1"));
                        pack.setCode(rs.getString("packlab39c2"));
                        pack.setAbbr(rs.getString("packlab39c3"));
                        pack.setName(rs.getString("packlab39c4"));
                        pack.setTestType(rs.getShort("packlab39c37"));
                        pack.setGender(new Item(rs.getInt("packlab39c6")));
                        pack.setUnitAge(rs.getShort("packlab39c9"));
                        pack.setMinAge(rs.getInt("packlab39c7"));
                        pack.setMaxAge(rs.getInt("packlab39c8"));

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
                        test.getSample().setTakeDate(rs.getTimestamp("lab57c39"));
                        test.getSample().setManagementsample(rs.getString("lab24c6"));

                        test.setRackStore(rs.getString("lab16c3"));
                        test.setPositionStore(rs.getString("lab11c1"));

                        test.getUnit().setId(rs.getString("lab45c1") == null ? null : rs.getInt("lab45c1"));
                        test.getUnit().setName(rs.getString("lab45c2"));

                        listOrders.get(listOrders.indexOf(order)).getTests().add(test);
                    } else {
                        order.setCreatedDateShort(rs.getInt("lab22c2"));
                        order.setCreatedDate(rs.getTimestamp("lab22c3"));
                        order.getLastUpdateUser().setId(rs.getInt("lab04c1"));
                        order.getLastUpdateUser().setName(rs.getString("lab04c2"));
                        order.getLastUpdateUser().setLastName(rs.getString("lab04c3"));
                        order.getLastUpdateUser().setUserName(rs.getString("lab04c4"));

                        //TIPO DE ORDEN
                        order.getType().setId(rs.getString("lab103c1") == null ? null : rs.getInt("lab103c1"));
                        order.getType().setCode(rs.getString("lab103c2"));
                        order.getType().setName(rs.getString("lab103c3"));
                        order.getType().setColor(rs.getString("lab103c4"));
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
                        order.getPatient().getRace().setCode(rs.getString("lab21lab08lab08c5"));
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
                        order.getPhysician().setEmail(rs.getString("lab19c21"));

                        DemographicValue demoValue = null;
                        for (Demographic demographic : demographics) {
                            demoValue = new DemographicValue();
                            demoValue.setIdDemographic(demographic.getId());
                            demoValue.setDemographic(demographic.getName());
                            demoValue.setEncoded(demographic.isEncoded());
                            if (demographic.isEncoded()) {
                                if (rs.getString("demo" + demographic.getId() + "_id") != null) {
                                    demoValue.setCodifiedId(rs.getInt("demo" + demographic.getId() + "_id"));
                                    demoValue.setCodifiedCode(rs.getString("demo" + demographic.getId() + "_code"));
                                    demoValue.setCodifiedName(rs.getString("demo" + demographic.getId() + "_name"));
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

                        if (rs.getString("lab39c1") != null) {
                            Test test = new Test();
                            test.setId(rs.getInt("lab39c1"));
                            test.setCode(rs.getString("lab39c2"));
                            test.setAbbr(rs.getString("lab39c3"));
                            test.setName(rs.getString("lab39c4"));
                            test.getResult().setResult(Tools.decrypt(rs.getString("lab57c1")));
                            test.getResult().setDateResult(rs.getTimestamp("lab57c2"));
                            test.getResult().setState(rs.getInt("lab57c8"));
                            test.getResult().setSampleState(rs.getInt("lab57c16"));
                            test.setTestType(rs.getShort("lab39c37"));
                            test.setConfidential(rs.getInt("lab39c27") == 1);
                            test.setGender(new Item(rs.getInt("lab39c6")));
                            test.setUnitAge(rs.getShort("lab39c9"));
                            test.setMinAge(rs.getInt("lab39c7"));
                            test.setMaxAge(rs.getInt("lab39c8"));
                            test.setProfile(rs.getInt("lab57c14"));
                            test.setRemission(rs.getInt("lab57c54"));
                            test.getLaboratory().setId(rs.getInt("lab40c1"));
                            test.getLaboratory().setCode(rs.getInt("lab40c2"));
                            test.getLaboratory().setName(rs.getString("lab40c3"));
                            test.getOriginRemission().setId(rs.getInt("idorigin"));
                            test.getOriginRemission().setCode(rs.getInt("codeorigin"));
                            test.getOriginRemission().setName(rs.getString("nameorigin"));
                            test.setExcluideTestProfile(rs.getInt("panellab39c39"));
                            if (rs.getString("lab37c1") != null) {
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
                            panel.setGender(new Item(rs.getInt("panellab39c6")));
                            panel.setUnitAge(rs.getShort("panellab39c9"));
                            panel.setMinAge(rs.getInt("panellab39c7"));
                            panel.setMaxAge(rs.getInt("panellab39c8"));

                            test.setPanel(panel);

                            Test pack = new Test();
                            pack.setId(rs.getString("packlab39c1") == null ? null : rs.getInt("packlab39c1"));
                            pack.setCode(rs.getString("packlab39c2"));
                            pack.setAbbr(rs.getString("packlab39c3"));
                            pack.setName(rs.getString("packlab39c4"));
                            pack.setTestType(rs.getShort("packlab39c37"));
                            pack.setGender(new Item(rs.getInt("packlab39c6")));
                            pack.setUnitAge(rs.getShort("packlab39c9"));
                            pack.setMinAge(rs.getInt("packlab39c7"));
                            pack.setMaxAge(rs.getInt("packlab39c8"));

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
                            test.getSample().setTakeDate(rs.getTimestamp("lab57c39"));
                            test.getSample().setManagementsample(rs.getString("lab24c6"));

                            test.setRackStore(rs.getString("lab16c3"));
                            test.setPositionStore(rs.getString("lab11c1"));

                            test.getUnit().setId(rs.getString("lab45c1") == null ? null : rs.getInt("lab45c1"));
                            test.getUnit().setName(rs.getString("lab45c2"));

                            order.getTests().add(test);
                        }
                        listOrders.add(order);
                    }
                    return order;
                };
                getConnection().query(query.toString() + "  " + from.toString() + "  ", mapper, params);
            }
            return listOrders;
        } catch (EmptyResultDataAccessException ex) {
            return new ArrayList<>(0);
        }
    }

    /**
     * Lista las ordenes por rango de fecha o numero de orden desde base de
     * datos.
     *
     * @param vInitial Rango Inicial.
     * @param vFinal Rango Final.
     * @param isSearchOrder
     * @param digitsOrder
     * @param demographics Lista de demograficos
     * @param rate
     * @param account
     * @param physician
     * @param checkCentralSystem
     * @param idCentralSystem
     * @param groupByProfil
     *
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    default List<OrderReportAidaAcs> listOrderFacturationRange(long vInitial, long vFinal, boolean isSearchOrder, int digitsOrder, List<Demographic> demographics, boolean rate, boolean account, boolean physician, boolean checkCentralSystem, int idCentralSystem, boolean groupByProfil) throws Exception {
        try {
            if (isSearchOrder) {
                vInitial = Tools.formatOrder(vInitial, digitsOrder);
                vFinal = Tools.formatOrder(vFinal, digitsOrder);
            }
            // Consulta de ordenes por historico:
            List<Integer> years = Tools.listOfConsecutiveYears(String.valueOf(vInitial), String.valueOf(vFinal));
            String lab22;
            String lab57;
            String lab900;
            String lab95;

            HashMap<Long, OrderReportAidaAcs> listOrders = new HashMap<>();
            int currentYear = DateTools.dateToNumberYear(new Date());
            for (Integer year : years) {
                lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
                lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;
                lab900 = year.equals(currentYear) ? "lab900" : "lab900_" + year;
                lab95 = year.equals(currentYear) ? "lab95" : "lab95_" + year;

                // Query
                StringBuilder query = new StringBuilder();
                query.append(ISOLATION_READ_UNCOMMITTED);
                query.append("SELECT lab900c2, lab57.lab39c1, lab39.lab39c2, lab39.lab39c4, lab39.lab39c21, lab43c3, lab43c4, l.lab80c4 as level, ");
                query.append("lab40c2, lab40c3, lab57c26, lab57.lab57c9, lab103c2, lab103c3, lab57.lab22c1, lab21c2, lab54c3, lab21c3, lab21c4, lab21c5, lab21c6, ");
                query.append("lab21.lab80c1 as idSex, s.lab80c3 AS codeSex, s.lab80c4 AS sex, lab21c7, lab22c3, lab57c4, lab57c2, lab57c18, lab57c22, lab57.lab57c1, lab57c39, lab21c16, lab21c17, ");
                query.append("lab21c8, lab22.lab07c1 as statusOrder, lab57c8, ");
                query.append("lab95c1, lab57c34, uv.lab04c2, uv.lab04c3, ");
                query.append("lab39P.lab39c1 AS lab39c1p, lab39P.lab39c4 AS lab39c4p, lab39P.lab39c2 AS lab39c2p, lab39P.lab39c3 AS lab39c3p");

                StringBuilder from = new StringBuilder();

                from.append(" FROM ").append(lab57).append(" AS lab57 ");
                from.append("INNER JOIN ").append(lab22).append(" AS lab22 ON lab22.lab22c1 = lab57.lab22c1 ");
                from.append("LEFT JOIN ").append(lab900).append(" as lab900 ON lab900.lab39c1 = lab57.lab39c1 AND lab900.lab22c1 = lab57.lab22c1 ");
                from.append("INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1  ");
                from.append(" LEFT JOIN lab39 lab39P ON lab39P.lab39c1 = lab57.lab57c14 ");
                from.append("INNER JOIN lab43 ON lab39.lab43c1 = lab43.lab43c1 ");
                from.append("LEFT JOIN lab80 l ON l.lab80c1 = lab39.lab39c5 ");
                from.append("INNER JOIN lab40 ON lab57.lab40c1 = lab40.lab40c1   ");
                from.append("INNER JOIN lab103 ON lab103.lab103c1 = lab22.lab103c1 ");
                from.append("INNER JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1  ");
                from.append("LEFT JOIN lab54 ON lab54.lab54c1 = lab21.lab54c1  ");
                from.append("LEFT JOIN lab80 s ON s.lab80c1 = lab21.lab80c1 ");
                from.append("LEFT JOIN ").append(lab95).append(" AS lab95 ON lab95.lab22c1 = lab57.lab22c1 AND lab95.lab39c1 = lab57.lab39c1 ");
                from.append("LEFT JOIN lab04 uv ON uv.lab04c1 = lab57.lab57c19");

                for (Demographic demographic : demographics) {
                    query.append(Tools.createDemographicsQuery(demographic).get(0));
                    from.append(Tools.createDemographicsQuery(demographic).get(1));
                }

                if (rate) {
                    query.append(" , lab22.lab904c1, lab904c2, lab904c3 ");
                    from.append("LEFT JOIN lab904 ON lab904.lab904c1 = lab22.lab904c1  ");
                }

                if (account) {
                    query.append(" ,lab22.lab14c1,  lab14c2, lab14c3, lab14c32 ");
                    from.append("LEFT JOIN lab14 ON lab14.lab14c1 = lab22.lab14c1  ");
                }
                if (physician) {
                    query.append(" ,lab22.lab19c1,  lab19c2, lab19c3, lab19c21 ");
                    from.append("LEFT JOIN lab19 ON lab19.lab19c1 = lab22.lab19c1  ");
                }

                if (checkCentralSystem) {
                    query.append(" , lab61c1 ");
                    from.append("LEFT JOIN lab61 ON lab61.lab39c1 = lab57.lab39c1 and lab61.lab118c1 = ").append(idCentralSystem).append(" ");
                }

                Object[] params;

                from.append(" WHERE lab39.lab39c37 != 2 ");

                if (groupByProfil) {
                    from.append(" AND lab57c14 IS NULL");
                }

                if (!isSearchOrder) {
                    params = new Object[]{
                        vInitial, vFinal
                    };
                    from.append(" AND lab22.lab22c2 BETWEEN ? AND ? ");
                } else {
                    params = new Object[]{
                        vInitial, vFinal
                    };
                    from.append(" AND lab22.lab22c1 BETWEEN ? AND ? ");
                }

                from.append(" AND lab22.lab07C1 != 0  AND (lab22c19 = 0 or lab22c19 is null) ");

                RowMapper mapper = (RowMapper<OrderReportAidaAcs>) (ResultSet rs, int i)
                        -> {
                    OrderReportAidaAcs order = new OrderReportAidaAcs();

                    order.setOrderNumber(rs.getLong("lab22c1"));
                    if (!listOrders.containsKey(rs.getLong("lab22c1"))) {

                        order.setOrderTypeCode(rs.getString("lab103c2"));
                        order.setOrderTypeName(rs.getString("lab103c3"));
                        order.setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                        order.setDocumentTypeName(rs.getString("lab54c3"));
                        order.setName1(Tools.decrypt(rs.getString("lab21c3")));
                        order.setName2(Tools.decrypt(rs.getString("lab21c4")));
                        order.setLastName(Tools.decrypt(rs.getString("lab21c5")));
                        order.setSurName(Tools.decrypt(rs.getString("lab21c6")));
                        order.setSexId(rs.getInt("idSex"));
                        order.setCodeSex(rs.getString("codeSex"));
                        order.setSex(rs.getString("sex"));
                        order.setBirthday(rs.getTimestamp("lab21c7"));
                        order.setCreatedDateShort(rs.getDate("lab22c3"));
                        order.setPhone(rs.getString("lab21c16"));
                        order.setAddress(rs.getString("lab21c17"));
                        order.setEmail(rs.getString("lab21c8"));
                        order.setStatusOrder(rs.getInt("statusOrder"));

                        if (account) {
                            order.setNit(rs.getString("lab14c2"));
                            order.setNameAccount(rs.getString("lab14c3"));
                        }

                        if (physician) {
                            order.setPhysicianName(rs.getString("lab19c2") == null ? "" : rs.getString("lab19c2") + " " + rs.getString("lab19c3") == null ? "" : rs.getString("lab19c3"));
                        }
                        if (rate) {
                            order.setRateName(rs.getString("lab904c3"));
                        }

                        List<DemographicValue> demographicsOrder = new LinkedList<>();

                        for (Demographic demographic : demographics) {

                            String[] data;
                            if (demographic.isEncoded()) {
                                data = new String[]{
                                    rs.getString("demo" + demographic.getId() + "_id"),
                                    rs.getString("demo" + demographic.getId() + "_code"),
                                    rs.getString("demo" + demographic.getId() + "_name")
                                };
                            } else {
                                data = new String[]{
                                    rs.getString("lab_demo_" + demographic.getId())
                                };
                            }
                            demographicsOrder.add(Tools.getDemographicsValue(demographic, data));

                        }
                        order.setAllDemographics(demographicsOrder);

                        listOrders.put(order.getOrderNumber(), order);
                    } else {
                        order = listOrders.get(rs.getLong("lab22c1"));
                    }
                    if (rs.getString("lab39c1") != null) {
                        TestReportAida test = new TestReportAida();

                        test.setPriceAccount(rs.getFloat("lab900c2"));
                        test.setCode(rs.getString("lab39c2"));
                        test.setName(rs.getString("lab39c4"));
                        test.setNameStadistic(rs.getString("lab39c21"));
                        test.setCodeArea(rs.getString("lab43c3"));
                        test.setNameArea(rs.getString("lab43c4"));
                        test.setLevel(rs.getString("level"));
                        test.setCodeLaboratory(rs.getString("lab40c2"));
                        test.setLaboratory(rs.getString("lab40c3"));
                        test.setPathology(rs.getInt("lab57c9"));
                        test.setAntibiogram(rs.getInt("lab57c26"));
                        test.setDateOrdered(rs.getTimestamp("lab57c4"));
                        test.setDateResult(rs.getTimestamp("lab57c2"));
                        test.setDateValidation(rs.getTimestamp("lab57c18"));
                        test.setDatePrint(rs.getTimestamp("lab57c22"));
                        test.setDateTake(rs.getTimestamp("lab57c39"));
                        test.setResult(Tools.decrypt(rs.getString("lab57c1")));
                        test.setStatus(rs.getInt("lab57c8"));
                        //covierte formato HTML a Txt plano
                        test.setComment(rs.getString("lab95c1") == null ? null : HtmlToTxt.htmlToString(rs.getString("lab95c1")));
                        test.setDateVerification(rs.getInt("lab57c34"));
                        String name = rs.getString("lab04c2") == null ? "" : rs.getString("lab04c2") + " ";
                        String lastname = rs.getString("lab04c3") == null ? "" : rs.getString("lab04c3");
                        test.setUserValidation(name + lastname);

                        if (checkCentralSystem) {
                            test.setCodeCups(rs.getString("lab61c1"));
                        }

                        test.setProfileId(rs.getInt("lab39c1p"));
                        test.setProfileName(rs.getString("lab39c4p"));
                        test.setProfileCode(rs.getString("lab39c2p"));
                        test.setProfileAbbr(rs.getString("lab39c3p"));

                        if (!listOrders.get(order.getOrderNumber()).getTests().contains(test)) {
                            listOrders.get(order.getOrderNumber()).getTests().add(test);
                        }
                    }
                    return order;
                };
                getConnection().query(query.toString() + "  " + from.toString() + "  ", mapper, params);
            }
            return new ArrayList<>(listOrders.values());
        } catch (EmptyResultDataAccessException ex) {
            return new ArrayList<>(0);
        }
    }

    /**
     * Lista las ordenes por rango de fecha o numero de orden desde base de
     * datos para la integracion con kbits
     *
     * @param startDate Fecha de inicio
     * @param endDate Fecha final
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    default List<OrderBilling> getOrdersBilling(String startDate, String endDate, final List<Demographic> demographics) throws Exception {
        try {
            // Consulta de ordenes por historico:
            List<Integer> years = Tools.listOfConsecutiveYears(startDate, endDate);
            String lab22;
            String lab57;
            String lab900;

            int currentYear = DateTools.dateToNumberYear(new Date());

            HashMap<Long, OrderBilling> listOrders = new HashMap<>();

            for (Integer year : years) {
                lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
                lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;
                lab900 = year.equals(currentYear) ? "lab900" : "lab900_" + year;

                boolean tableExists = getToolsDao().tableExists(getConnection(), lab22);
                tableExists = tableExists ? getToolsDao().tableExists(getConnection(), lab57) : tableExists;
                if (tableExists) {
                    // Query
                    StringBuilder query = new StringBuilder();
                    query.append(ISOLATION_READ_UNCOMMITTED);

                    query.append("SELECT lab57.lab22c1, lab21c2, lab21c3, lab21c5, lab21c6, lab21.lab80c1, lab21c7, lab21c8, lab21c16, lab22.lab05c1");
                    query.append(",lab21lab54.lab54c1 AS lab21lab54lab54c1,  lab21lab54.lab54c2 AS lab21lab54lab54c2, lab21lab54.lab54c3 AS lab21lab54lab54c3 ");
                    query.append(",lab05c4, lab05c10, lab22.lab904c1, lab904c3, lab57.lab39c1, lab39c2, lab39c4, lab39c49, lab22c3, lab22.lab930c1");
                    query.append(",lab22.lab14c1, lab14c3, lab39.lab07c1 as statusTest, lab39c62 ");
                    query.append(",lab900c2, lab900c3, lab900c4, lab900c5, lab900c6 ");
                    query.append(",lab60.lab60c1 as idCommentOrder, lab60.lab60c3 as commentOrder, lab60.lab60c4 as typeCommentOrder, lab60.lab60c6 as isPrintCommentOrder ");

                    StringBuilder from = new StringBuilder();
                    from.append(" FROM ").append(lab57).append(" AS lab57 ");
                    from.append("INNER JOIN ").append(lab22).append(" AS lab22 ON lab22.lab22c1 = lab57.lab22c1 ");
                    from.append("INNER JOIN lab05 ON lab05.lab05c1 = lab22.lab05c1  ");
                    from.append("INNER JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1  ");
                    from.append("LEFT JOIN lab54 lab21lab54 ON lab21lab54.lab54c1 = lab21.lab54c1  ");
                    from.append("INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1  ");
                    from.append("LEFT JOIN lab14 ON lab22.lab14c1 = lab14.lab14c1  ");
                    from.append("LEFT JOIN lab60 ON lab60.lab60c2 = lab57.lab22c1 ");
                    from.append("LEFT JOIN lab904 ON lab22.lab904c1 = lab904.lab904c1  ");
                    from.append("LEFT JOIN ").append(lab900).append(" AS lab900 ON lab900.lab22c1 = lab57.lab22c1 AND lab900.lab39c1 = lab57.lab39c1");

                    for (Demographic demographic : demographics) {
                        if (demographic.getId() > -1) {
                            query.append(Tools.createDemographicsQuery(demographic).get(0));
                            from.append(Tools.createDemographicsQuery(demographic).get(1));
                        }
                    }

                    from.append(" WHERE lab57.lab22c1 BETWEEN ").append(startDate).append(" AND ")
                            .append(endDate).append(" AND lab57c14 IS NULL ");

                    from.append(" AND lab22.lab07C1 != 0 and lab21.lab21c1 != 0  AND (lab22c19 = 0 or lab22c19 is null) ");

                    from.append(" AND (lab57c68 = ").append(Constants.SEND);

                    from.append(" OR lab57c68 = ").append(Constants.BILLED).append(") ");

                    RowMapper mapper = (RowMapper<OrderBilling>) (ResultSet rs, int i)
                            -> {
                        OrderBilling order = new OrderBilling();
                        if (!listOrders.containsKey(rs.getLong("lab22c1"))) {

                            order.setOrder(rs.getLong("lab22c1"));
                            order.setCompany(null);

                            Patient patient = new Patient();
                            patient.setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                            patient.setName1(Tools.decrypt(rs.getString("lab21c3")));
                            patient.setLastName(Tools.decrypt(rs.getString("lab21c5")));
                            patient.setSurName(Tools.decrypt(rs.getString("lab21c6")));
                            patient.getSex().setId(rs.getInt("lab80c1"));
                            patient.setBirthday(rs.getTimestamp("lab21c7"));
                            patient.setEmail(rs.getString("lab21c8"));
                            patient.setPhone(rs.getString("lab21c16"));

                            //PACIENTE - TIPO DE DOCUMENTO
                            DocumentType document = new DocumentType();
                            document.setId(rs.getInt("lab21lab54lab54c1"));
                            document.setAbbr(rs.getString("lab21lab54lab54c2"));
                            document.setName(rs.getString("lab21lab54lab54c3"));
                            patient.setDocumentType(document);

                            PatientNT patientNT = MigrationMapper.toDtoPatientNT(patient);
                            patientNT.setDemographics(null);
                            patientNT.setComment(null);
                            order.setGeneralPatient(patientNT);

                            order.setBranchId(rs.getInt("lab05c1"));
                            order.setBranchCode(rs.getString("lab05c10"));
                            order.setBranch(rs.getString("lab05c4"));

                            order.setTypeOfAccountingDocument(1);

                            order.setIdRate(rs.getInt("lab904c1"));
                            order.setRate(rs.getString("lab904c3"));

                            //formato fecha de creacion 
                            DateFormat simple = new SimpleDateFormat("dd/MM/yyyy");
                            Date dateFormat = new Date(rs.getTimestamp("lab22c3").getTime());
                            order.setCreatedDate(simple.format(dateFormat));

                            order.setIdAccount(rs.getInt("lab14c1"));
                            order.setAccount(rs.getString("lab14c3"));

                            order.setComboInvoice(rs.getInt("lab930c1"));

                            order.setIdAccountDemo(rs.getString("demo17_id"));
                            order.setCodeAccountDemo(rs.getString("demo17_code"));
                            order.setNameAccountDemo(rs.getString("demo17_name"));

                            listOrders.put(order.getOrder(), order);

                        } else {
                            order = listOrders.get(rs.getLong("lab22c1"));
                        }
                        if (rs.getString("commentOrder") != null && !rs.getString("commentOrder").isEmpty()) {
                            CommentOrder commentOrder = new CommentOrder();
                            commentOrder.setId(rs.getInt("idCommentOrder"));
                            commentOrder.setIdRecord(order.getOrder());
                            commentOrder.setComment(rs.getString("commentOrder"));
                            commentOrder.setType(rs.getShort("typeCommentOrder"));
                            commentOrder.setPrint(rs.getInt("isPrintCommentOrder") == 1);

                            listOrders.get(order.getOrder()).setComment(commentOrder.getComment());
                        }

                        TestBilling test = new TestBilling();

                        test.setTestId(rs.getInt("lab39c1"));
                        test.setTestCode(rs.getString("lab39c2"));
                        test.setName(rs.getString("lab39c4"));
                        test.setStatus(rs.getString("statusTest"));
                        test.setCpt(rs.getString("lab39c62"));
                        test.setPrice(rs.getBigDecimal("lab900c2"));
                        test.setTax(rs.getDouble("lab900c5"));
                        test.setDiscount(rs.getBigDecimal("lab900c6"));
                        test.setCustomePay(rs.getBigDecimal("lab900c4"));

                        if (!listOrders.get(order.getOrder()).getTestBilling().contains(test)) {
                            listOrders.get(order.getOrder()).getTestBilling().add(test);
                        }

                        return order;
                    };
                    getConnection().query(query.toString() + " " + from.toString(), mapper);
                }
            }
            return new ArrayList<>(listOrders.values());
        } catch (EmptyResultDataAccessException ex) {
            ResultsLog.error("Error: " + ex);
            return new ArrayList<>(0);
        }
    }

    /**
     * Lista las ordenes por rango de fecha o numero de orden desde base de
     * datos.
     *
     * @param vInitial Rango Inicial.
     * @param vFinal Rango Final.
     * @param searchType Indica si la consulta se hace por rango de orden(1),
     * fechas(0) o Listado de ordenes(2)
     * @param demographics Lista de demograficos.
     * @param orders lista de ordenes a consultar searchType == 2
     * @param tests Lista de Examenes
     *
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    default List<Order> listRemissionOrders(Long vInitial, Long vFinal, int searchType, final List<Demographic> demographics, List<Long> orders, List<Integer> tests, int check, int remission) throws Exception {
        try {
            List<Order> listOrders = new LinkedList<>();
            // Consulta de ordenes por historico:
            List<Integer> years = Tools.listOfConsecutiveYears(String.valueOf(vInitial), String.valueOf(vFinal));
            String lab22;
            String lab57;
            int currentYear = DateTools.dateToNumberYear(new Date());
            for (Integer year : years) {
                lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
                lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;
                boolean tableExists = getToolsDao().tableExists(getConnection(), lab22);
                tableExists = tableExists ? getToolsDao().tableExists(getConnection(), lab57) : tableExists;
                if (tableExists) {
                    // Query
                    StringBuilder query = new StringBuilder();
                    query.append(ISOLATION_READ_UNCOMMITTED);
                    query.append("SELECT lab22.lab22c1, lab22c2, lab22c3, lab22c4, lab22c5, lab22.lab07c1, lab22c7, lab22c9, lab22c10, ");
                    query.append("lab22.lab103c1,   ");
                    query.append("lab21.lab21c1, lab21c2, lab21c3, lab21c4, lab21c5, lab21c6, lab21.lab80c1 AS lab21lab80lab80c1, lab21.lab08c1 AS lab21lab08lab08c1, lab21.lab54c1 AS lab21lab54lab54c1, ");
                    query.append("lab22.lab05c1, ");
                    query.append("lab22.lab10c1,   ");
                    query.append("lab22.lab14c1,  ");
                    query.append("lab22.lab19c1,  ");
                    query.append("lab22.lab904c1,  ");
                    query.append("lab57.lab22c1, lab57.lab57c1, lab57.lab57c2, lab57.lab57c8, lab57.lab57c14, lab57.lab57c15, lab57c16, lab57.lab57c56, ");
                    query.append("lab39.lab39c1, lab39.lab39c2, lab39.lab39c3, lab39.lab39c4, lab39.lab39c6,lab39.lab39c7,lab39.lab39c8, lab39.lab39c9, lab39.lab39c37, lab39.lab39c27, lab39.lab39c56, ");

                    query.append("panel.lab39c1 AS panellab39c1, panel.lab39c2 AS panellab39c2, panel.lab39c3 AS panellab39c3, panel.lab39c4 AS panellab39c4, panel.lab39c37 AS panellab39c37, panel.lab39c6 AS panellab39c6,panel.lab39c7 AS panellab39c7, panel.lab39c8 AS panellab39c8, panel.lab39c9 AS panellab39c9, lab39.lab39c39 AS panellab39c39, ");
                    query.append("pack.lab39c1 AS packlab39c1, pack.lab39c2 AS packlab39c2, pack.lab39c3 AS packlab39c3, pack.lab39c4 AS packlab39c4, pack.lab39c37 AS packlab39c37,  pack.lab39c6 AS packlab39c6, pack.lab39c7 AS packlab39c7, pack.lab39c8 AS packlab39c8, pack.lab39c9 AS packlab39c9, lab39.lab39c39 AS packlab39c39, ");

                    query.append("lab57.lab57c54, lab57.lab57c39 ");
                    StringBuilder from = new StringBuilder();
                    from.append(" FROM ").append(lab57).append(" AS lab57 ");
                    from.append("INNER JOIN ").append(lab22).append(" AS lab22 ON lab22.lab22c1 = lab57.lab22c1 ");
                    from.append("INNER JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1  ");
                    from.append("INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1 AND lab39.lab39c26 = 1");
                    from.append("LEFT JOIN lab39 panel ON panel.lab39c1 = lab57.lab57c14 ");
                    from.append("LEFT JOIN lab39 pack ON pack.lab39c1 = lab57.lab57c15 ");
                    from.append("LEFT JOIN lab500 ON lab500.lab22c1 = lab57.lab22c1 and lab500.lab39c1 = lab57.lab39c1 ");

                    for (Demographic demographic : demographics) {
                        if (demographic.getOrigin().equals("O")) {
                            if (demographic.isEncoded()) {
                                query.append(", demo").append(demographic.getId()).append(".lab63c1 as demo").append(demographic.getId()).append("_id ");
                                query.append(", demo").append(demographic.getId()).append(".lab63c2 as demo").append(demographic.getId()).append("_code ");
                                query.append(", demo").append(demographic.getId()).append(".lab63c3 as demo").append(demographic.getId()).append("_name ");

                                from.append(" LEFT JOIN Lab63 demo").append(demographic.getId()).append(" ON lab22.lab_demo_").append(demographic.getId()).append(" = demo").append(demographic.getId()).append(".lab63c1 ");
                            } else {
                                query.append(", lab22.lab_demo_").append(demographic.getId());
                            }
                        } else {
                            if (demographic.getOrigin().equals("H")) {
                                if (demographic.isEncoded()) {
                                    query.append(", demo").append(demographic.getId()).append(".lab63c1 as demo").append(demographic.getId()).append("_id ");
                                    query.append(", demo").append(demographic.getId()).append(".lab63c2 as demo").append(demographic.getId()).append("_code ");
                                    query.append(", demo").append(demographic.getId()).append(".lab63c3 as demo").append(demographic.getId()).append("_name ");

                                    from.append(" LEFT JOIN Lab63 demo").append(demographic.getId()).append(" ON lab21.lab_demo_").append(demographic.getId()).append(" = demo").append(demographic.getId()).append(".lab63c1 ");
                                } else {
                                    query.append(", lab21.lab_demo_").append(demographic.getId());
                                }
                            }
                        }
                    }
                    Object[] params = null;
                    switch (searchType) {
                        case 0:
                            from.append(" WHERE lab39.lab39c37 != 2 AND lab22.lab22c2 BETWEEN ? AND ? ");
                            break;
                        case 1:
                            from.append(" WHERE lab39.lab39c37 != 2 AND lab22.lab22c1 BETWEEN ? AND ? ");
                            break;
                        default:
                            from.append(" WHERE lab39.lab39c37 != 2 AND lab22.lab22c1 IN(").append(orders.stream().map(order -> order.toString()).collect(Collectors.joining(","))).append(") ");
                            break;
                    }

                    if (tests != null && !tests.isEmpty()) {
                        if (tests.size() == 1) {
                            from.append(" AND lab39.lab39c1 = ").append(tests.get(0));
                        } else {
                            from.append(" AND lab39.lab39c1 IN (").append(tests.stream().map(test -> test.toString()).collect(Collectors.joining(","))).append(") ");
                        }
                    }

                    //filtro por estado de la muestra.
                    switch (check) {
                        case 1:
                            from.append(" AND lab57.lab57c16 = ").append(LISEnum.ResultSampleState.CHECKED.getValue());
                            break;
                        case 2:
                            from.append(" AND lab57.lab57c16 != ").append(LISEnum.ResultSampleState.CHECKED.getValue());
                            break;
                        default:
                            break;
                    }

                    from.append(" AND lab22.lab07C1 != 0 AND lab500.lab22c1 is null  AND (lab22c19 = 0 or lab22c19 is null) ");

                    params = new Object[]{
                        vInitial, vFinal
                    };

                    RowMapper mapper = (RowMapper<Order>) (ResultSet rs, int i)
                            -> {
                        Order order = new Order();
                        order.setOrderNumber(rs.getLong("lab22c1"));
                        if (listOrders.contains(order)) {
                            Test test = new Test();
                            test.setId(rs.getInt("lab39c1"));
                            test.setCode(rs.getString("lab39c2"));
                            test.setAbbr(rs.getString("lab39c3"));
                            test.setName(rs.getString("lab39c4"));
                            test.setTestType(rs.getShort("lab39c37"));
                            test.setProfile(rs.getInt("lab57c14"));
                            test.setExcluideTestProfile(rs.getInt("panellab39c39"));

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

                            listOrders.get(listOrders.indexOf(order)).getTests().add(test);
                        } else {

                            //TIPO DE ORDEN
                            order.getType().setId(rs.getString("lab103c1") == null ? null : rs.getInt("lab103c1"));

                            //PACIENTE
                            order.getPatient().setId(rs.getInt("lab21c1"));
                            order.getPatient().setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                            order.getPatient().setName1(Tools.decrypt(rs.getString("lab21c3")));
                            order.getPatient().setName2(Tools.decrypt(rs.getString("lab21c4")));
                            order.getPatient().setLastName(Tools.decrypt(rs.getString("lab21c5")));
                            order.getPatient().setSurName(Tools.decrypt(rs.getString("lab21c6")));

                            //PACIENTE - SEXO
                            order.getPatient().getSex().setId(rs.getInt("lab21lab80lab80c1"));

                            //PACIENTE - RAZA
                            order.getPatient().getRace().setId(rs.getInt("lab21lab08lab08c1"));

                            //PACIENTE - TIPO DE DOCUMENTO
                            order.getPatient().getDocumentType().setId(rs.getInt("lab21lab54lab54c1"));

                            //SEDE
                            order.getBranch().setId(rs.getInt("lab05c1"));

                            //SERVICIO
                            order.getService().setId(rs.getInt("lab10c1"));

                            //EMPRESA
                            order.getAccount().setId(rs.getInt("lab14c1"));

                            //MEDICO
                            order.getPhysician().setId(rs.getInt("lab19c1"));

                            order.getRate().setId(rs.getInt("lab904c1"));

                            DemographicValue demoValue = null;
                            for (Demographic demographic : demographics) {
                                demoValue = new DemographicValue();
                                demoValue.setIdDemographic(demographic.getId());
                                demoValue.setDemographic(demographic.getName());
                                demoValue.setEncoded(demographic.isEncoded());
                                if (demographic.isEncoded()) {
                                    if (rs.getString("demo" + demographic.getId() + "_id") != null) {
                                        demoValue.setCodifiedId(rs.getInt("demo" + demographic.getId() + "_id"));
                                        demoValue.setCodifiedCode(rs.getString("demo" + demographic.getId() + "_code"));
                                        demoValue.setCodifiedName(rs.getString("demo" + demographic.getId() + "_name"));
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

                            if (rs.getString("lab39c1") != null) {
                                Test test = new Test();
                                test.setId(rs.getInt("lab39c1"));
                                test.setCode(rs.getString("lab39c2"));
                                test.setAbbr(rs.getString("lab39c3"));
                                test.setName(rs.getString("lab39c4"));
                                test.setTestType(rs.getShort("lab39c37"));
                                test.setProfile(rs.getInt("lab57c14"));
                                test.setExcluideTestProfile(rs.getInt("panellab39c39"));

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

                                order.getTests().add(test);
                            }

                            listOrders.add(order);
                        }
                        return order;
                    };
                    if (searchType == 2) {
                        getConnection().query(query.toString() + " " + from.toString(), mapper);
                    } else {
                        getConnection().query(query.toString() + "  " + from.toString() + "  ", mapper, params);
                    }
                }

            }
            return listOrders;
        } catch (EmptyResultDataAccessException ex) {
            return new ArrayList<>(0);
        }
    }

    /**
     * Lista las alarmas de panico no atendidas desde base de datos.
     *
     * @param order
     *
     *
     * @return Lista de entrevistas
     * @throws Exception Error en la base de datos.
     */
    default List<RemissionOrderCentral> getRemissionCentralOrders(long order) throws Exception {
        try {
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED);
            query.append("SELECT lab57.lab22c1 AS ORDERORIGIN, lab500.lab22c1 AS ORDER, lab57.lab39c1, lab39c2, lab39c4, lab57c8  ");
            StringBuilder from = new StringBuilder();
            from.append(" FROM lab57 ");
            from.append("JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1 ");
            from.append("LEFT JOIN lab500 ON lab500.lab22c1 = lab57.lab22c1 and lab500.lab39c1 = lab57.lab39c1 ");
            from.append(" WHERE lab57.lab22c1 = ").append(order).append("");

            return getConnection().query(query.toString() + from,
                    (ResultSet rs, int i)
                    -> {

                RemissionOrderCentral interview = new RemissionOrderCentral();
                interview.setOrder(rs.getLong("ORDERORIGIN"));
                interview.setTest(rs.getInt("lab39c1"));
                interview.setTestCode(rs.getString("lab39c2"));
                interview.setTestName(rs.getString("lab39c4"));
                interview.setTestState(rs.getInt("lab57c8"));
                interview.setRemission(rs.getString("ORDER") == null ? 0 : 1);
                return interview;
            });
        } catch (EmptyResultDataAccessException ex) {
            return new ArrayList<>(0);
        }
    }

    /**
     * Inserta los examenes a remitir
     *
     * @param testList
     *
     * @return registros insertados
     */
    default int insertRemmisioncTest(List<RemissionOrderCentral> testList) {
        List<HashMap> batchArray = new ArrayList<>();
        // Formatear la fecha en YYYYMMDD
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String formattedDate = dateFormat.format(currentDate);

        SimpleJdbcInsert insert = new SimpleJdbcInsert(getConnection())
                .withTableName("lab500");
        testList.stream().map((test)
                -> {
            HashMap parameters = new HashMap();
            parameters.put("lab22c1", test.getOrder());
            parameters.put("lab39c1", test.getTest());
            parameters.put("lab500c1", 0);
            parameters.put("lab500c2", Integer.parseInt(formattedDate));
            parameters.put("lab500c3", 0);
            return parameters;
        }).forEachOrdered((parameters)
                -> {
            batchArray.add(parameters);
        });
        return insert.executeBatch(batchArray.toArray(new HashMap[testList.size()])).length;
    }

    /**
     * Lista las alarmas de panico no atendidas desde base de datos.
     *
     * @param filter
     * @param order
     *
     *
     * @return Lista de entrevistas
     * @throws Exception Error en la base de datos.
     */
    default List<RemissionOrderCentral> listremissionCentralOrders(Filter filter) throws Exception {
        try {
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED);
            query.append("SELECT lab500.lab22c1 AS ORDER, lab500.lab39c1, lab39c2, lab39c4  ");
            StringBuilder from = new StringBuilder();
            from.append(" FROM lab500 ");
            from.append("JOIN lab39 ON lab39.lab39c1 = lab500.lab39c1 ");
            from.append(" WHERE lab500.lab500c2 BETWEEN ").append(filter.getInitDate()).append(" AND ").append(filter.getEndDate());

            return getConnection().query(query.toString() + from,
                    (ResultSet rs, int i)
                    -> {

                RemissionOrderCentral interview = new RemissionOrderCentral();
                interview.setOrder(rs.getLong("ORDER"));
                interview.setTest(rs.getInt("lab39c1"));
                interview.setTestCode(rs.getString("lab39c2"));
                interview.setTestName(rs.getString("lab39c4"));

                return interview;
            });
        } catch (EmptyResultDataAccessException ex) {
            return new ArrayList<>(0);
        }
    }
}
