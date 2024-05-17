package net.cltech.enterprisent.dao.interfaces.operation.billing;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import net.cltech.enterprisent.dao.interfaces.common.ToolsDao;
import net.cltech.enterprisent.dao.interfaces.masters.configuration.ConfigurationDao;
import net.cltech.enterprisent.domain.DTO.migracionIngreso.PatientNT;
import net.cltech.enterprisent.domain.masters.billing.RatesByContract;
import net.cltech.enterprisent.domain.masters.demographic.Demographic;
import net.cltech.enterprisent.domain.operation.billing.CashReportDetail;
import net.cltech.enterprisent.domain.operation.billing.CashReportFilter;
import net.cltech.enterprisent.domain.operation.billing.ComboInvoice;
import net.cltech.enterprisent.domain.operation.billing.CreditNote;
import net.cltech.enterprisent.domain.operation.billing.CreditNoteCombo;
import net.cltech.enterprisent.domain.operation.billing.CreditNoteDetail;
import net.cltech.enterprisent.domain.operation.billing.DetailPaymentType;
import net.cltech.enterprisent.domain.operation.billing.DetailToRecalculate;
import net.cltech.enterprisent.domain.operation.billing.DetailedCashReport;
import net.cltech.enterprisent.domain.operation.billing.FilterSimpleInvoice;
import net.cltech.enterprisent.domain.operation.billing.Invoice;
import net.cltech.enterprisent.domain.operation.billing.InvoicePayment;
import net.cltech.enterprisent.domain.operation.billing.PreInvoiceHeader;
import net.cltech.enterprisent.domain.operation.billing.PreInvoiceOrder;
import net.cltech.enterprisent.domain.operation.billing.PreInvoiceTest;
import net.cltech.enterprisent.domain.operation.billing.RecalculateRate;
import net.cltech.enterprisent.domain.operation.billing.SimpleInvoice;
import net.cltech.enterprisent.domain.operation.billing.Tax;
import net.cltech.enterprisent.domain.operation.billing.TaxInvoice;
import net.cltech.enterprisent.domain.operation.billing.integration.OrderBilling;
import net.cltech.enterprisent.domain.operation.billing.integration.TestBilling;
import net.cltech.enterprisent.domain.operation.list.FilterDemographic;
import net.cltech.enterprisent.domain.operation.orders.CommentOrder;
import net.cltech.enterprisent.domain.operation.orders.DemographicValue;
import net.cltech.enterprisent.domain.operation.orders.Patient;
import net.cltech.enterprisent.domain.operation.orders.billing.BillingFilter;
import net.cltech.enterprisent.domain.operation.orders.billing.PriceChange;
import net.cltech.enterprisent.tools.Constants;
import static net.cltech.enterprisent.tools.Constants.ISOLATION_READ_UNCOMMITTED;
import net.cltech.enterprisent.tools.DateTools;
import net.cltech.enterprisent.tools.SQLTools;
import net.cltech.enterprisent.tools.Tools;
import net.cltech.enterprisent.tools.log.orders.OrderCreationLog;
import net.cltech.enterprisent.tools.log.results.ResultsLog;
import net.cltech.enterprisent.tools.mappers.MigrationMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los metodos de acceso para los datos de la factura
 *
 * @version 1.0.0
 * @author Julian
 * @since 13/04/2021
 * @see CreaciÃ³n
 */
public interface InvoiceDao
{

    /**
     * Metodo de conexiÃ³n hacia cualquier motor de base de datos
     *
     * @return
     */
    public JdbcTemplate getJdbcTemplate();

    /**
     * Metodo de conexiÃ³n hacia el dao de configuracion
     *
     * @return
     */
    public ConfigurationDao getDaoConfig();

    /**
     * Obtiene el dao de tools
     *
     * @return Instancia de toolsDao
     */
    public ToolsDao getToolsDao();

    /**
     * Obtiene las pre facturas de un cliente
     *
     * @param customers Id de los clientes
     * @param contracts Id de los contratos
     * @param filter Filtros de la consulta
     *
     * @return lista de cabeceras de la factura
     * @throws Exception Error en base de datos
     */
    default List<PreInvoiceHeader> getPreInvoice(List<Integer> customers, List<Integer> contracts, BillingFilter filter) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            // Campos del cliente
            query.append("SELECT DISTINCT lab14.lab14c1, lab14.lab14c2 ")
                    .append(", lab14.lab14c3 ")
                    .append(", lab14.lab14c4 ")
                    .append(", lab14.lab14c14 ")
                    .append(", lab14.lab14c23 ")
                    // Campos que se traeran del contrato enviado o de la tarifa de ese contrato para ese cliente
                    .append(", lab990.lab990c22  ")
                    .append(", lab63.lab63c3  ")
                    .append(", lab990.lab990c4 ")
                    .append(", lab990.lab990c2 ")
                    .append(", lab990.lab990c5 ")
                    .append(", lab990.lab990c6 ")
                    .append(", lab990.lab990c7 ")
                    .append(", lab990.lab990c8  ")
                    .append(", lab990.lab990c10 ")
                    .append(", lab990.lab990c11 ")
                    .append(", lab990.lab990c12  ")
                    .append(", lab990.lab990c15 ")
                    .append(", lab990.lab990c17 ")
                    .append(", lab990.lab990c16 ")
                    .append(", lab990.lab990c21 ")
                    .append(", lab990.lab990c1 ")
                    .append(", lab990.lab990c1 ")
                    .append(", lab990.lab990c13 ")
                    .append(", lab990.lab990c14 ")
                    // Uniones entre tablas
                    .append(" FROM lab14 ")
                    .append("LEFT JOIN lab990 ON lab990.lab14c1 = lab14.lab14c1 ")
                    .append("LEFT JOIN lab63 ON lab63.lab63c1 = lab990c22 ")
                    .append("JOIN lab993 ON lab993.lab990c1 = lab990.lab990c1 ");

            StringBuilder where = new StringBuilder();
            where.append(" WHERE lab990.lab07c1 = 1");

            where.append(" AND lab14.lab14c1 IN(").append(customers.stream().map(t -> t.toString()).collect(Collectors.joining(","))).append(") ");

            where.append(" AND lab990.lab990c1 IN(").append(contracts.stream().map(t -> t.toString()).collect(Collectors.joining(","))).append(") ");

            query.append(where);

            List<PreInvoiceHeader> list = new ArrayList<>();
            RowMapper mapper = (RowMapper<PreInvoiceHeader>) (ResultSet rs, int i) ->
            {
                PreInvoiceHeader preInvoiceHeader = new PreInvoiceHeader();

                preInvoiceHeader.setCustomerId(rs.getInt("lab14c1"));
                preInvoiceHeader.setAccountNit(rs.getString("lab14c2"));
                preInvoiceHeader.setAccountName(rs.getString("lab14c3"));
                preInvoiceHeader.setAccountPhone(rs.getString("lab14c4"));
                preInvoiceHeader.setAccountAddress(rs.getString("lab14c14"));
                preInvoiceHeader.setParticular(rs.getInt("lab14c23"));
                preInvoiceHeader.setAccountCity(rs.getString("lab990c22"));
                preInvoiceHeader.setAccountCityName(rs.getString("lab63c3"));
                preInvoiceHeader.setVendorName(rs.getString("lab990c16"));
                preInvoiceHeader.setRegimen(rs.getString("lab990c15"));
                preInvoiceHeader.setMaximumAmount(rs.getDouble("lab990c4"));
                preInvoiceHeader.setCurrentAmount(rs.getDouble("lab990c5"));
                preInvoiceHeader.setAlarmAmount(rs.getDouble("lab990c6"));
                preInvoiceHeader.setCapitated(rs.getInt("lab990c7"));
                preInvoiceHeader.setVendorIdentifier(rs.getString("lab990c17"));
                preInvoiceHeader.setContractCode(rs.getString("lab990c21"));
                preInvoiceHeader.setContractName(rs.getString("lab990c2"));
                preInvoiceHeader.setContractId(rs.getInt("lab990c1"));
                preInvoiceHeader.setCopayment(rs.getInt("lab990c14"));
                preInvoiceHeader.setModeratingFee(rs.getInt("lab990c13"));

                Calendar cal = Calendar.getInstance();
                // Adicionamos la fecha de la factura
                cal.setTime(filter.getDateOfInvoice());
                // A la fecha de la factura le adicionamos los dias de plazo de pago autorizado para el cliente
                cal.add(Calendar.DATE, rs.getInt("lab990c12"));
                preInvoiceHeader.setDateOfInvoice(new Timestamp(cal.getTime().getTime()));
                // Forma de pago
                if (rs.getInt("lab990c12") <= rs.getInt("lab990c10"))
                {
                    preInvoiceHeader.setFormOfPayment(Constants.FORM_OF_PAYMENT_CASH);
                } else if (rs.getInt("lab990c12") <= rs.getInt("lab990c11") && rs.getInt("lab990c12") > rs.getInt("lab990c10"))
                {
                    preInvoiceHeader.setFormOfPayment(Constants.FORM_OF_PAYMENT_CREDIT);
                } else
                {
                    preInvoiceHeader.setFormOfPayment(Constants.FORM_OF_PAYMENT_NOT_APPLICABLE);
                }
                preInvoiceHeader.setDiscountPercentage(rs.getDouble("lab990c8"));
                list.add(preInvoiceHeader);
                return preInvoiceHeader;
            };

            getJdbcTemplate().query(query.toString(), mapper);

            return list;
        } catch (DataAccessException e)
        {
            OrderCreationLog.error(e);
            return new ArrayList<>();
        }
    }

    /**
     * Obtiene un listado de los ids de las ordenes creadas durante un rango de
     * fechas: Una inicial y una final
     *
     * @param init Fecha inicial
     * @param end Fecha final
     * @param demographics lista de demograficos
     * @param idBranch Id de la sede
     * @param filterTest Tipo de filtro para los examenes
     * @param tests Lista de examenes a filtrar
     * @param demos Lista de demogragicos
     * @param customers Lista de clientes
     * @param rates Lista de tarifas
     * @param priceType Indica el precio con el que trabaja la factura
     *
     * @return Ordenes dentro de un rango de fechas
     * @throws Exception Error en base de datos
     */
    default List<PreInvoiceOrder> getOrders(Integer init, Integer end, Integer idBranch, final List<FilterDemographic> demographics, Integer filterTest, List<Integer> tests, final List<Demographic> demos, List<Integer> customers, List<Integer> rates, int priceType) throws Exception
    {
        try
        {
            List<PreInvoiceOrder> orders = new LinkedList<>();
            List<Integer> years = Tools.listOfConsecutiveYears(Integer.toString(init), Integer.toString(end));
            int currentYear = DateTools.dateToNumberYear(new Date());
            String lab22;
            String lab57;
            String lab900;

            boolean documenttype = getDaoConfig().get("ManejoTipoDocumento").getValue().equalsIgnoreCase("true");
            boolean physician = getDaoConfig().get("ManejoMedico").getValue().equalsIgnoreCase("true");

            for (Integer year : years)
            {
                lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
                lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;
                lab900 = year.equals(currentYear) ? "lab900" : "lab900_" + year;

                StringBuilder query = new StringBuilder();
                query.append("SELECT lab57.lab22c1 ")
                        .append(", lab22.lab22c3 ")
                        .append(", lab21.lab21c1 ")
                        .append(", lab21.lab21c2 ")
                        .append(", lab21.lab21c3 ")
                        .append(", lab21.lab21c4 ")
                        .append(", lab05.lab05c4 ")
                        .append(", lab21.lab21c5 ")
                        .append(", lab21.lab21c6 ")
                        .append(", lab902.lab902c6 ")
                        .append(", lab902.lab902c7 ")
                        .append(", lab902.lab902c8 ")
                        .append(", lab22.lab14c1 ")
                        .append(", lab14c23 ")
                        .append(", lab22.lab904c1 ")
                        .append(", lab990.lab990c1 ")
                        .append(", lab14.lab118c1 ")
                        .append(", lab39.lab39c1 ")
                        .append(", lab39.lab39c2 ")
                        .append(", lab39.lab39c3 ")
                        .append(", lab39.lab39c4 ")
                        .append(", lab900.lab900c2 ")
                        .append(", lab900.lab900c3 ")
                        .append(", lab900.lab900c4")
                        .append(", lab61c1");

                StringBuilder from = new StringBuilder();
                from.append(" FROM  ").append(lab57).append(" as lab57 ")
                        .append(" INNER JOIN ").append(lab22).append(" as lab22 ON lab22.lab22c1 = lab57.lab22c1 ")
                        .append(" INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1 AND lab39c20 = 1")
                        .append(" INNER JOIN ").append(lab900).append(" as lab900 ON lab900.lab39c1 = lab57.lab39c1 AND lab900.lab22c1 = lab57.lab22c1")
                        .append(" INNER JOIN lab05 ON lab05.lab05c1 = lab22.lab05c1 ")
                        .append(" INNER JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1 ")
                        .append(" INNER JOIN lab14 ON lab22.lab14c1 = lab14.lab14c1 ")
                        .append(" LEFT JOIN lab902 ON lab902.lab22c1 = lab22.lab22c1 ")
                        .append(" INNER JOIN lab993 ON lab993.lab904c1 = lab22.lab904c1 ")
                        .append(" INNER JOIN lab990 ON lab990.lab990c1 = lab993.lab990c1 AND lab990.lab14c1 = lab22.lab14c1 AND lab990.lab07c1 = 1 ")
                        .append(" LEFT JOIN lab61 ON Lab61.lab39c1 = lab57.lab39c1 AND lab61.lab118c1 = lab14.lab118c1 ");

                if (documenttype)
                {
                    query.append(" ,lab54.lab54c2, lab54.lab54c3 ");
                    from.append("JOIN lab54 ON lab54.lab54c1 = lab21.lab54c1 ");
                }

                if (physician)
                {
                    query.append(" ,lab22.lab19c1, lab19c2, lab19c3 ");
                    from.append("LEFT JOIN lab19 ON lab19.lab19c1 = lab22.lab19c1  ");
                }

                for (Demographic demographic : demos)
                {
                    query.append(Tools.createDemographicsQuery(demographic).get(0));
                    from.append(Tools.createDemographicsQuery(demographic).get(1));
                }

                StringBuilder where = new StringBuilder();
                List<Object> params = new ArrayList<>();

                where.append(" WHERE lab22.lab14c1 IN(").append(customers.stream().map(t -> t.toString()).collect(Collectors.joining(","))).append(") ");

                if (rates.size() > 0)
                {
                    where.append("  AND lab22.lab904c1 IN(").append(rates.stream().map(t -> t.toString()).collect(Collectors.joining(","))).append(") ");
                }

                where.append(" AND lab22.lab22c2 BETWEEN ? AND ?");

                params.add(init);
                params.add(end);

                if (idBranch != -1)
                {

                    where.append(" AND lab22.lab05c1 = ?");
                    params.add(idBranch);
                }

                where.append(" AND lab22.lab07c1 = 1  AND (lab22c19 = 0 or lab22c19 is null) AND lab22.lab904c1 IS NOT NULL");

                where.append(" AND lab57c14 IS NULL ");

                where.append(" AND lab901c1i IS NULL ");

                where.append(" AND lab900.lab920c1 IS NULL");

                if (filterTest != null)
                {
                    //Filtro por examenes confidenciales y areas
                    switch (filterTest)
                    {
                        case 1:
                            where.append(" AND lab39.lab43c1 IN (").append(tests.stream().map(area -> area.toString()).collect(Collectors.joining(","))).append(") ");
                            break;
                        case 2:
                            where.append(" AND lab39.lab39c1 IN (").append(tests.stream().map(test -> test.toString()).collect(Collectors.joining(","))).append(") ");
                            break;
                        case 3:
                            where.append(" AND lab39.lab39c1 IN (").append(tests.stream().map(test -> test.toString()).collect(Collectors.joining(","))).append(") ");
                            break;
                        default:
                            break;
                    }
                }

                SQLTools.buildSQLDemographicFilterStatisticsLab(demographics, params, query, from, where);

                RowMapper mapper = (RowMapper<PreInvoiceOrder>) (ResultSet rs, int i) ->
                {
                    PreInvoiceOrder order = new PreInvoiceOrder();
                    order.setOrderId(rs.getLong("lab22c1"));

                    if (orders.contains(order))
                    {
                        PreInvoiceTest test = new PreInvoiceTest();
                        test.setTestId(rs.getInt("lab39c1"));
                        test.setTestCode(rs.getString("lab39c2"));
                        test.setTestCodeCups(rs.getString("lab61c1"));
                        test.setTestAbbr(rs.getString("lab39c3"));
                        test.setTestName(rs.getString("lab39c4"));

                        switch (priceType)
                        {
                            case 1:
                                test.setInsurancePrice(rs.getDouble("lab900c4"));
                                break;
                            case 2:
                                test.setInsurancePrice(rs.getDouble("lab900c3"));
                                break;
                            default:
                                test.setInsurancePrice(rs.getDouble("lab900c4") + rs.getDouble("lab900c3"));
                                break;
                        }

                        orders.get(orders.indexOf(order)).getTests().add(test);

                    } else
                    {

                        order.setOrderCreationDate(rs.getTimestamp("lab22c3"));
                        order.setPatientId(rs.getInt("lab21c1"));
                        order.setHistoryPatient(Tools.decrypt(rs.getString("lab21c2")));
                        order.setFirstName(Tools.decrypt(rs.getString("lab21c3")));
                        order.setSecondName(Tools.decrypt(rs.getString("lab21c4")));
                        order.setLastName(Tools.decrypt(rs.getString("lab21c5")));
                        order.setSecondLastName(Tools.decrypt(rs.getString("lab21c6")));
                        order.setCopayment(rs.getDouble("lab902c6"));
                        order.setModeratorFee(rs.getDouble("lab902c7"));

                        if (rs.getInt("lab14c23") == 1)
                        {
                            order.setTotalPayments(rs.getDouble("lab902c8"));
                        } else
                        {
                            order.setTotalPayments(0.0);
                        }

                        order.setBranchName(rs.getString("lab05c4"));
                        order.setCustomerId(rs.getInt("lab14c1"));
                        order.setContractId(rs.getInt("lab990c1"));
                        order.setRate(rs.getInt("lab904c1"));

                        if (documenttype)
                        {
                            //PACIENTE - TIPO DE DOCUMENTO
                            order.setDocumentTypeAbbr(rs.getString("lab54c2"));
                            order.setDocumentTypeName(rs.getString("lab54c3"));
                        }

                        if (physician)
                        {
                            order.setNameDoctor(rs.getString("lab19c2"));
                            order.setLastNameDoctor(rs.getString("lab19c3"));
                        }

                        List<DemographicValue> demographicsOrder = new LinkedList<>();

                        for (Demographic demographic : demos)
                        {

                            String[] data;
                            if (demographic.isEncoded())
                            {
                                data = new String[]
                                {
                                    rs.getString("demo" + demographic.getId() + "_id"),
                                    rs.getString("demo" + demographic.getId() + "_code"),
                                    rs.getString("demo" + demographic.getId() + "_name")
                                };
                            } else
                            {
                                data = new String[]
                                {
                                    rs.getString("lab_demo_" + demographic.getId())
                                };
                            }
                            demographicsOrder.add(Tools.getDemographicsValue(demographic, data));

                        }
                        order.setAllDemographics(demographicsOrder);

                        if (rs.getString("lab39c1") != null)
                        {
                            PreInvoiceTest test = new PreInvoiceTest();
                            test.setTestId(rs.getInt("lab39c1"));
                            test.setTestCode(rs.getString("lab39c2"));
                            test.setTestCodeCups(rs.getString("lab61c1"));
                            test.setTestAbbr(rs.getString("lab39c3"));
                            test.setTestName(rs.getString("lab39c4"));

                            switch (priceType)
                            {
                                case 1:
                                    test.setInsurancePrice(rs.getDouble("lab900c4"));
                                    break;
                                case 2:
                                    test.setInsurancePrice(rs.getDouble("lab900c3"));
                                    break;
                                default:
                                    test.setInsurancePrice(rs.getDouble("lab900c4") + rs.getDouble("lab900c3"));
                                    break;
                            }
                            order.getTests().add(test);
                            orders.add(order);
                        }
                    }
                    return order;
                };

                getJdbcTemplate().query(query.toString() + from + where, mapper, params.toArray());
            }
            return orders;
        } catch (DataAccessException ex)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Obtiene un listado de los examenes para la factura
     *
     * @param orderId Numero de orden
     * @param type Tipo del filtro
     * @param tests Examenes a filtrar
     * @param invoiceId Id de la factura
     * @param headerId Id de la cabecera
     * @param particular Indica si la busqueda es por factura particular o por
     * cliente
     * @param centralSystem Indica el sistema central
     * @param priceType Indica el precio con el que trabaja la factura
     * @return Ordenes dentro de un rango de fechas
     * @throws Exception Error en base de datos
     */
    default List<PreInvoiceTest> getPreInvoiceTests(long orderId, Integer type, List<Integer> tests, Long invoiceId, Long headerId, Integer particular, int centralSystem, int priceType) throws Exception
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(orderId));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab900 = year.equals(currentYear) ? "lab900" : "lab900_" + year;
            String lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;

            StringBuilder query = new StringBuilder();
            query.append("SELECT DISTINCT lab39.lab39c1 ")
                    .append(", lab39.lab39c2 ")
                    .append(", lab39.lab39c3 ")
                    .append(", lab39.lab39c4 ")
                    .append(", lab900.lab900c2 ")
                    .append(", lab900.lab900c3 ")
                    .append(", lab900.lab900c4 ");

            if (centralSystem != 0)
            {
                query.append(", lab61c1 ");
            }

            query.append(" FROM lab39 ")
                    .append("JOIN ").append(lab900).append(" as lab900 ON lab900.lab39c1 = lab39.lab39c1 ")
                    .append(" JOIN ").append(lab57).append(" as lab57 ON lab57.lab22c1 = lab900.lab22c1 AND lab57.lab39c1 = lab900.lab39c1 ");

            if (centralSystem != 0)
            {
                query.append("LEFT JOIN Lab61 on Lab61.lab39c1 = lab39.lab39c1 and lab118c1 = ").append(centralSystem);
            }

            query.append(" WHERE lab900.lab22c1 = ").append(orderId)
                    .append(" AND lab57c14 IS NULL ");

            if (invoiceId == null && headerId == null)
            {
                query.append(" AND lab901c1i IS NULL ");

                if (particular == 1)
                {
                    query.append(" AND lab900.lab920c1p IS NULL ");
                } else
                {
                    query.append(" AND lab900.lab920c1 IS NULL");
                }

            } else
            {
                query.append(" AND ( (lab901c1P = ").append(invoiceId).append(" OR lab901c1i = ").append(invoiceId).append(" ) ");
                if (particular == 1)
                {
                    query.append(" AND lab900.lab920c1p = ").append(headerId).append(")");
                } else
                {
                    query.append(" AND lab900.lab920c1 = ").append(headerId).append(")");
                }
            }

            if (type != null)
            {
                //Filtro por examenes confidenciales y areas
                switch (type)
                {
                    case 1:
                        query.append(" AND lab39.lab43c1 IN (").append(tests.stream().map(area -> area.toString()).collect(Collectors.joining(","))).append(") ");
                        break;
                    case 2:
                        query.append(" AND lab39.lab39c1 IN (").append(tests.stream().map(test -> test.toString()).collect(Collectors.joining(","))).append(") ");
                        break;
                    case 3:
                        query.append(" AND lab39.lab39c1 IN (").append(tests.stream().map(test -> test.toString()).collect(Collectors.joining(","))).append(") ");
                        break;
                    default:
                        break;
                }
            }

            return getJdbcTemplate().query(query.toString(), (ResultSet rs, int i)
                    ->
            {
                PreInvoiceTest test = new PreInvoiceTest();
                test.setTestId(rs.getInt("lab39c1"));
                test.setTestCode(rs.getString("lab39c2"));
                if (centralSystem != 0)
                {
                    test.setTestCodeCups(rs.getString("lab61c1"));
                }
                test.setTestAbbr(rs.getString("lab39c3"));
                test.setTestName(rs.getString("lab39c4"));

                switch (priceType)
                {
                    case 1:
                        test.setInsurancePrice(rs.getDouble("lab900c4"));
                        break;
                    case 2:
                        test.setInsurancePrice(rs.getDouble("lab900c3"));
                        break;
                    default:
                        test.setInsurancePrice(rs.getDouble("lab900c4") + rs.getDouble("lab900c3"));
                        break;
                }
                return test;
            });
        } catch (DataAccessException e)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Consulta el nÃºmero de la factura
     *
     * @param invoiceNumber
     * @return NÃºmero de factura
     * @throws Exception Error en base de datos
     */
    default String getInvoiceById(String invoiceNumber) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED);
            query.append("SELECT lab901c2 ")
                    .append(" FROM lab901 ")
                    .append("WHERE lab901c2 = ").append(invoiceNumber);

            return getJdbcTemplate().queryForObject(query.toString(), (ResultSet rs, int i)
                    ->
            {
                return rs.getString("lab901c2");
            });
        } catch (DataAccessException e)
        {
            return "";
        }
    }

    /**
     * Obtiene el valor total de impuesto para un cliente
     *
     * @param contractId
     * @return Valor total de impuesto
     * @throws Exception Error en base de datos.
     */
    default Double getTotalTaxValue(int contractId) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT SUM(lab910.lab910c4) AS totalTaxValue")
                    .append(" FROM lab992 ")
                    .append("JOIN lab910 ON lab910.lab910c1 = lab992.lab910c1 ")
                    .append("WHERE lab992.lab990c1 = ").append(contractId);

            return getJdbcTemplate().queryForObject(query.toString(), (ResultSet rs, int i) ->
            {
                return rs.getDouble("totalTaxValue");
            });
        } catch (DataAccessException e)
        {
            return -1.0;
        }
    }

    /**
     * Obtiene una lista de los impuestos de un cliente
     *
     * @param contract
     * @return lista de los Impuestos
     * @throws Exception Error en base de datos.
     */
    default List<Tax> listTax(int contract) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("Select lab910.lab910c1 as taxId, lab910c4 as porcentaje, lab910c3 as name ")
                    .append(" FROM lab910 ")
                    .append(" JOIN lab992 ON lab910.lab910c1 = lab992.lab910c1 ")
                    .append(" WHERE lab992.lab990c1 = ").append(contract);

            return getJdbcTemplate().query(query.toString(), (ResultSet rs, int i)
                    ->
            {
                Tax tax = new Tax();
                tax.setName(rs.getString("name"));
                tax.setTaxId(rs.getInt("taxId"));
                tax.setValue(rs.getDouble("porcentaje"));
                return tax;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Actualizamos el detalle precio del examen adicionando a cada una de las
     * ordenes facturadas el nÃºmero de la factura generado
     *
     * @param headers cabeceras de la factura
     * @param invoiceId Id de la factura
     * @throws Exception Error en base de datos.
     */
    default void updateInvoicedOrders(List<PreInvoiceHeader> headers, long invoiceId) throws Exception
    {
        headers.forEach(header ->
        {
            header.getOrders().forEach(order ->
            {
                Integer year = Tools.YearOfOrder(String.valueOf(order.getOrderId()));
                Integer currentYear = DateTools.dateToNumberYear(new Date());
                String lab900 = year.equals(currentYear) ? "lab900" : "lab900_" + year;
                order.getTests().forEach(test ->
                {
                    getJdbcTemplate().update("UPDATE  " + lab900
                            + " SET lab901c1i = ? , lab920c1 = ?"
                            + " WHERE lab22c1 = ?"
                            + " AND lab39c1 = ?",
                            invoiceId,
                            header.getId(),
                            order.getOrderId(),
                            test.getTestId());
                });
            });
        });
    }

    /**
     * Crea el registro de la factura
     *
     * @param invoice
     * @return
     * @throws Exception Error en base de datos.
     */
    default Invoice insertInvoice(Invoice invoice) throws Exception
    {

        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab901")
                .usingGeneratedKeyColumns("lab901c1");

        HashMap parameters = new HashMap();
        parameters.put("lab901c2", invoice.getInvoiceNumber());
        parameters.put("lab901c3", invoice.getDiscount());
        parameters.put("lab901c4", invoice.getTotal());
        parameters.put("lab901c5", invoice.getDateOfInvoice());
        parameters.put("lab907c1", invoice.getResolutionId());
        parameters.put("lab04c1", invoice.getUserId());
        parameters.put("lab901c9", invoice.getParticular());
        parameters.put("lab901c10", invoice.isState() ? 1 : 0);
        parameters.put("lab906c1", invoice.getPayerId());
        parameters.put("lab901c11", invoice.getTypeOfInvoice() == null ? 0 : invoice.getTypeOfInvoice());
        parameters.put("lab901c12", invoice.getTax());
        parameters.put("lab909c1", invoice.getIdSubsequentCredits() == null ? 0 : invoice.getIdSubsequentCredits());
        parameters.put("lab901c13", invoice.getComment());
        parameters.put("lab901c14", invoice.getCfdi());
        parameters.put("lab901c17", invoice.getTotalPaid() == null ? 0.0 : invoice.getTotalPaid());
        parameters.put("lab901c19", invoice.getSvTypeOfInvoice());
        parameters.put("lab901c20", invoice.getSvRetention());
        parameters.put("lab901c21", invoice.getSvMaximumRetention());
        parameters.put("lab901c22", invoice.getSvThirdPartyInvoice());
        parameters.put("lab901c23", invoice.isSent() ? 1 : 0);
        parameters.put("lab901c24", invoice.getDueDate());
        parameters.put("lab901c25", invoice.getFormOfPayment());
        parameters.put("lab21c1", invoice.getPatientId());
        parameters.put("lab901c27", invoice.getBillingPeriod());
        parameters.put("lab901c31", 0);
        parameters.put("lab14c1", invoice.getCustomerId());

        Number key = insert.executeAndReturnKey(parameters);
        invoice.setInvoiceId(key.longValue());

        insertHeadersInvoice(invoice.getInvoiceHeader(), key.longValue());

        return invoice;
    }

    /**
     * Actualiza el monto actual de un cliente
     *
     * @param currentAmount
     * @param contractId
     * @return Filas afectadas
     * @throws Exception Error en base de datos.
     */
    default int updateCurrentCustomerAmount(Double currentAmount, int contractId) throws Exception
    {
        try
        {
            return getJdbcTemplate().update("UPDATE lab990 "
                    + "SET lab990c5 = ? "
                    + "WHERE lab990c1 = ?",
                    currentAmount,
                    contractId);
        } catch (DataAccessException e)
        {
            return -1;
        }
    }

    /**
     * Consulta la cantidad de ordenes facturadas por capitado
     *
     * @param contractId
     * @param customerId
     * @param dateFormat Formato de fecha de busqueda yyyyMM
     *
     * @return
     * @throws Exception Error en base de datos.
     */
    default Integer getTotalOrdersPerCapita(int contractId, int customerId, String dateFormat) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT SUM(lab920c10) as total")
                    .append(" FROM lab920 ")
                    .append(" INNER JOIN lab901 ON lab901.lab901c1 = lab920.lab901c1 AND lab14c1 = ").append(customerId)
                    .append(" WHERE lab990c1 = ").append(contractId)
                    .append(" AND lab920c11 = '").append(dateFormat).append("'");

            return getJdbcTemplate().queryForObject(query.toString(), (ResultSet rs, int i)
                    ->
            {
                return rs.getInt("total");
            });
        } catch (DataAccessException e)
        {
            return null;
        }
    }

    /**
     * Consulta el id de una factura con el nÃºmero de esta
     *
     * @param invoiceNumber NÃºmero de la factura
     * @return Id de la factura
     * @throws Exception Error en base de datos.
     */
    default long getInvoiceId(String invoiceNumber) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab901c1")
                    .append(" FROM lab901 ")
                    .append("WHERE lab901c2 = '").append(invoiceNumber).append("'");

            return getJdbcTemplate().queryForObject(query.toString(), (ResultSet rs, int i)
                    ->
            {
                return rs.getInt("lab901c1");
            });
        } catch (DataAccessException e)
        {
            return 0;
        }
    }

    /**
     * Guarda la nota de credito
     *
     * @param creditNote
     * @return Entero mayor a 0 si se inserto y menor o igual a cero si no
     * @throws Exception
     */
    default long createCreditNote(CreditNote creditNote) throws Exception
    {
        try
        {
            SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                    .withTableName("lab913")
                    .usingGeneratedKeyColumns("lab913c1");
            HashMap parameters = new HashMap();
            parameters.put("lab901c1", creditNote.getInvoiceId());
            parameters.put("lab913c2", creditNote.getReserveNumber());
            parameters.put("lab913c3", creditNote.getDateOfNote());
            parameters.put("lab04c1", creditNote.getUserId());
            parameters.put("lab913c4", creditNote.getCancellationReason());
            parameters.put("lab913c5", creditNote.getValue());

            Number key = insert.executeAndReturnKey(parameters);

            if (key.longValue() > 0)
            {
                return key.longValue();
            } else
            {
                return 0;
            }
        } catch (Exception e)
        {
            return -1;
        }
    }

    /**
     * Crea el registro del detalle de la nota de credito
     *
     * @param detail Detalle de la nota de credito
     * @throws java.lang.Exception
     */
    default void createCreditNoteDetails(CreditNoteDetail detail) throws Exception
    {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab915");
        HashMap parameters = new HashMap();
        parameters.put("lab913c1", detail.getCreditNoteId());
        parameters.put("lab22c1", detail.getOrderId());
        parameters.put("lab39c1", detail.getTestId());
        parameters.put("lab21c1", detail.getPatientId());
        parameters.put("lab915c2", detail.getServicePrice());
        parameters.put("lab915c3", detail.getPatientPrice());
        parameters.put("lab915c4", detail.getInsurancePrice());
        parameters.put("lab904c1", detail.getRateId());
        parameters.put("lab14c1", detail.getCustomerId());
        parameters.put("lab05c1", detail.getBranchId());

        insert.execute(parameters);
    }

    /**
     * Libera las ordenes asociadas a una factura
     *
     * @param invoiceId
     * @param type
     *
     * @return numero de filas afectadas
     * @throws Exception Error en base de datos.
     */
    default int releaseOrdersFromAnInvoice(long invoiceId, int type) throws Exception
    {
        if (type == 0)
        {
            return getJdbcTemplate().update("UPDATE lab900 SET lab901c1p = NULL, lab920c1p = NULL"
                    + " WHERE lab901c1p = ?",
                    invoiceId);
        } else
        {
            return getJdbcTemplate().update("UPDATE lab900 SET lab901c1i = NULL, lab920c1 = NULL"
                    + " WHERE lab901c1i = ?",
                    invoiceId);
        }
    }

    /**
     * Obtiene el valor total de las notas de credito asociadas a una factura
     *
     * @param invoiceId Id de la factura
     * @return Suma total del valor de todas las notas credito asociadas a una
     * factura
     * @throws Exception Error en base de datos.
     */
    default Double totalValueCreditNotes(long invoiceId) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT SUM(lab913c5) AS totalValue")
                    .append(" FROM lab913 ")
                    .append("WHERE lab901c1 = ").append(invoiceId);

            return getJdbcTemplate().queryForObject(query.toString(), (ResultSet rs, int i)
                    ->
            {
                return rs.getDouble("totalValue");
            });
        } catch (DataAccessException e)
        {
            return null;
        }
    }

    /**
     * Obtiene el id de una factura por el numero de la orden para un particular
     *
     * @param order
     * @return
     * @throws Exception Error en base de datos.
     */
    default Invoice getInvoicedParticularOrder(Long order) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT DISTINCT(lab901c2) AS invoice, lab901c5 as invoiceDate")
                    .append(" FROM lab900 ")
                    .append("INNER JOIN lab901 on lab901c1 = lab901c1P ")
                    .append("WHERE lab900.lab22c1 = ").append(order);

            return getJdbcTemplate().queryForObject(query.toString(), (ResultSet rs, int i)
                    ->
            {
                Invoice invoice = new Invoice();
                invoice.setInvoiceNumber(rs.getString("invoice"));
                invoice.setDateOfInvoice(rs.getTimestamp("invoiceDate"));
                return invoice;
            });
        } catch (DataAccessException e)
        {
            return new Invoice();
        }
    }

    /**
     * Obtiene el id de una orden y de un examen por medio de una serie de
     * filtros
     *
     * @param recalculate
     *
     * @return Lista de detalle para recalcular
     * @throws Exception Error en base de datos.
     */
    default List<DetailToRecalculate> getOrdersWithoutInvoice(RecalculateRate recalculate) throws Exception
    {
        try
        {
            StringBuilder select = new StringBuilder();
            select.append(ISOLATION_READ_UNCOMMITTED);
            select.append("SELECT lab22.lab22c1 AS orderId")
                    .append(", lab39.lab39c1 AS testId")
                    .append(", lab39.lab39c4 AS testName")
                    .append(", lab900.lab900c2 AS servicePrice")
                    .append(", lab900.lab900c3 AS patientPrice")
                    .append(", lab900.lab900c4 AS insurancePrice")
                    .append(" FROM lab22 ")
                    .append("JOIN lab900 ON lab900.lab22c1 = lab22.lab22c1 ")
                    .append("JOIN lab39 ON lab39.lab39c1 = lab900.lab39c1 ")
                    .append("WHERE lab22.lab22c2 between ").append(recalculate.getStartDate())
                    .append(" AND ").append(recalculate.getEndDate())
                    .append(" AND lab900.lab901c1p IS NULL")
                    .append(" AND (lab22c19 = 0 or lab22c19 is null) ")
                    .append(" AND lab900.lab901c1i IS NULL")
                    .append(" AND lab900.lab904c1 = ").append(recalculate.getRateId())
                    .append(" AND lab900.lab14c1 = ").append(recalculate.getCustomerId());

            return getJdbcTemplate().query(select.toString(), (ResultSet rs, int i) ->
            {
                DetailToRecalculate detail = new DetailToRecalculate();
                detail.setOrderId(rs.getLong("orderId"));
                detail.setTestId(rs.getInt("testId"));
                detail.setTestName(rs.getString("testName"));
                detail.setServicePrice(rs.getBigDecimal("servicePrice"));
                detail.setPatientPrice(rs.getBigDecimal("patientPrice"));
                detail.setInsurancePrice(rs.getBigDecimal("insurancePrice"));
                return detail;
            });
        } catch (DataAccessException ex)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Modifica el precio sel servicio, el precio que debe pagar el paciente y
     * el precio que debe pagar la aseguradora
     *
     * @param detailRecalculate Detalle para recalcular
     * @param recalculateRate Recalcular tarifa
     * @throws Exception Error en base de datos.
     */
    default void recalculateRates(DetailToRecalculate detailRecalculate, RecalculateRate recalculateRate) throws Exception
    {
        getJdbcTemplate().update("UPDATE lab900 SET lab900c2 = ?"
                + ", lab900c3 = ?"
                + ", lab900c4 = ?"
                + " WHERE lab22c1 = ?"
                + " AND lab39c1 = ?"
                + " AND lab904c1 = ?"
                + " AND lab14c1 = ?",
                detailRecalculate.getServicePrice(),
                detailRecalculate.getPatientPrice(),
                detailRecalculate.getInsurancePrice(),
                detailRecalculate.getOrderId(),
                detailRecalculate.getTestId(),
                recalculateRate.getRateId(),
                recalculateRate.getCustomerId());
    }

    /**
     * Obtiene el id de una orden y de un examen por medio de una serie de
     * filtros
     *
     * @param recalculate
     *
     * @return Lista de detalle para recalcular
     * @throws Exception Error en base de datos.
     */
    default List<DetailToRecalculate> getTheRecalculatedOrders(RecalculateRate recalculate) throws Exception
    {
        try
        {
            StringBuilder select = new StringBuilder();
            select.append(ISOLATION_READ_UNCOMMITTED);
            select.append("SELECT lab22c1 ")
                    .append(", lab39c1")
                    .append(" FROM lab22 ")
                    .append("JOIN lab900 ON lab900.lab22c1 = lab22.lab22c1 ")
                    .append("WHERE lab22.lab22c2 between ").append(recalculate.getStartDate())
                    .append(" AND ").append(recalculate.getEndDate())
                    .append(" AND lab900.lab901c1p IS NULL")
                    .append(" AND (lab22c19 = 0 or lab22c19 is null) ")
                    .append(" AND lab900.lab901c1i IS NULL")
                    .append(" AND lab900.lab904c1 = ").append(recalculate.getRateId())
                    .append(" AND lab900.lab14c1 = ").append(recalculate.getCustomerId());

            return getJdbcTemplate().query(select.toString(), (ResultSet rs, int i) ->
            {
                DetailToRecalculate detail = new DetailToRecalculate();
                detail.setOrderId(rs.getLong("lab22c1"));
                detail.setTestId(rs.getInt("lab39c1"));
                return detail;
            });
        } catch (DataAccessException ex)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Actualizamos el detalle precio del examen adicionando a cada una de las
     * ordenes facturadas el nÃºmero de la factura generado
     *
     * @param headers Cabeceras de la factura
     * @param invoiceId Id de la factura generada
     * @throws Exception Error en base de datos.
     */
    default void updateInvoicedOrdersToParticular(List<PreInvoiceHeader> headers, long invoiceId) throws Exception
    {
        headers.forEach(header ->
        {
            header.getOrders().forEach(order ->
            {
                Integer year = Tools.YearOfOrder(String.valueOf(order.getOrderId()));
                Integer currentYear = DateTools.dateToNumberYear(new Date());
                String lab900 = year.equals(currentYear) ? "lab900" : "lab900_" + year;
                order.getTests().forEach(test ->
                {
                    getJdbcTemplate().update("UPDATE  " + lab900
                            + " SET lab901c1p = ? , lab920c1p = ?"
                            + " WHERE lab22c1 = ?"
                            + " AND lab39c1 = ?",
                            invoiceId,
                            header.getId(),
                            order.getOrderId(),
                            test.getTestId());
                });
            });
        });
    }

    /**
     * Obtiene el reporte general de caja por filtros especificos
     *
     * @param filter
     * @param paymentTypeIds
     *
     * @return Reporte de caja
     * @throws Exception Error en base de datos.
     */
    default List<CashReportDetail> getCashReport(CashReportFilter filter, List<Integer> paymentTypeIds) throws Exception
    {
        try
        {
            StringBuilder select = new StringBuilder();
            StringBuilder from = new StringBuilder();

            select.append(ISOLATION_READ_UNCOMMITTED);
            select.append("SELECT lab04.lab04c1 AS userId")
                    .append(", lab04.lab04c2 AS userNames")
                    .append(", lab04.lab04c3 AS userLastNames")
                    .append(", lab04.lab04c4 AS userName")
                    .append(", lab01.lab01c2 AS paymentTypeName")
                    .append(", lab05.lab05c1 AS idBranch")
                    .append(", lab05.lab05c4 AS branch")
                    .append(" ,SUM(lab903.lab903c3) AS paymentTypeAmount, CAST(lab903c4 AS DATE) as date");

            from.append(" FROM lab903 ")
                    .append("JOIN lab04 ON lab04.lab04c1 = lab903.lab04c1_i ")
                    .append("JOIN lab01 ON lab01.lab01c1 = lab903.lab01c1 ")
                    .append("JOIN lab22 ON lab22.lab22c1 = lab903.lab22c1 ")
                    .append("JOIN lab05 ON lab22.lab05c1 = lab05.lab05c1 ")
                    .append("JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1 ")
                    .append("JOIN lab902 ON lab902.lab22c1 = lab903.lab22c1 ")
                    .append(" WHERE lab903.lab903c4 BETWEEN '").append(filter.getStartDate()).append("' AND '").append(filter.getEndDate()).append("'")
                    .append(" AND lab903.lab01c1 IN (").append(paymentTypeIds.stream().map(paymentType -> String.valueOf(paymentType)).collect(Collectors.joining(", "))).append(")")
                    .append(" AND lab22.lab07c1 = 1  AND (lab22c19 = 0 or lab22c19 is null) ");

            if (filter.getBranchId() > 0)
            {
                from.append(" AND lab22.lab05c1 = ").append(filter.getBranchId());
            }

            if (filter.getRuc() != null && !filter.getRuc().isEmpty())
            {
                from.append(" AND lab902c17  IN('").append(filter.getRuc().stream().map(ruc -> ruc.toString()).collect(Collectors.joining("','"))).append("') ");
            }

            if (filter.getPhoneBox() != null && !filter.getPhoneBox().isEmpty())
            {

                from.append(" AND lab902c21  IN('").append(filter.getPhoneBox().stream().map(phonebox -> phonebox.toString()).collect(Collectors.joining("','"))).append("') ");
            }

            if (filter.getHistory() != null && !filter.getHistory().isEmpty())
            {
                from.append(" AND lab21.lab21c2  IN('").append(filter.getHistory().stream().map(history -> Tools.encrypt(history.toString())).collect(Collectors.joining("','"))).append("') ");
            }

            if (filter.getEmailPatient() != null && !filter.getEmailPatient().isEmpty())
            {

                from.append(" AND lab21.lab21c8  IN('").append(filter.getEmailPatient().stream().map(emailpatient -> emailpatient.toString()).collect(Collectors.joining("','"))).append("') ");
            }

            if (filter.getPhone() != null && !filter.getPhone().isEmpty())
            {
                from.append(" AND lab21.lab21c16  IN('").append(filter.getPhone().stream().map(phone -> phone.toString()).collect(Collectors.joining("','"))).append("') ");
            }

            from.append(" GROUP BY lab01.lab01c2, lab04.lab04c1, lab04.lab04c2 , lab04.lab04c3 , lab04.lab04c4, idBranch, CAST(lab903c4 AS DATE)")
                    .append(" ORDER BY date asc");

            return getJdbcTemplate().query(select.toString() + from, (ResultSet rs, int i) ->
            {
                CashReportDetail detail = new CashReportDetail();
                detail.setBranchId(rs.getInt("idBranch"));
                detail.setBranchName(rs.getString("branch"));
                detail.setUserId(rs.getInt("userId"));
                detail.setNames(rs.getString("userNames") + " " + rs.getString("userLastNames"));
                detail.setUserName(rs.getString("userName"));
                detail.setPaymentTypeName(rs.getString("paymentTypeName"));
                detail.setPaymentTypeAmount(rs.getDouble("paymentTypeAmount"));
                detail.setDatePayment(rs.getString("date"));
                detail.setTypeOfDetail("1");
                return detail;
            });
        } catch (DataAccessException ex)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Obtiene el reporte general de caja para las devoluciones
     *
     * @param filter
     *
     * @return Reporte de caja
     * @throws Exception Error en base de datos.
     */
    default List<CashReportDetail> getsCashReturns(CashReportFilter filter) throws Exception
    {
        try
        {
            StringBuilder select = new StringBuilder();

            select.append(ISOLATION_READ_UNCOMMITTED);
            select.append("SELECT lab04.lab04c1, lab04c2, lab04c3, lab04c4, lab05.lab05c1 AS idBranch, lab05.lab05c4 AS branch, sum(lab913.lab913c5) as total, CAST(lab901c5 AS DATE) as date ")
                    .append(" FROM lab901 ")
                    .append(" JOIN lab920 ON lab920.lab901c1 = lab901.lab901c1  ")
                    .append(" JOIN lab913 ON lab913.lab901c1 = lab901.lab901c1 ")
                    .append(" JOIN lab04 ON lab04.lab04c1 = lab913.lab04c1  ");

            if (filter.getBranchId() > 0)
            {
                select.append(" JOIN lab22 ON lab920.lab920c3 = lab22.lab22c1 AND lab22.lab05c1 =  ").append(filter.getBranchId());
            } else
            {
                select.append(" JOIN lab22 ON lab920.lab920c3 = lab22.lab22c1 ");
            }
            select.append(" JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1 ");
            select.append(" JOIN lab902 ON lab902.lab22c1 = lab903.lab22c1 ");
            select.append(" JOIN lab05 ON lab22.lab05c1 = lab05.lab05c1 ");
            if (filter.getRuc() != null && !filter.getRuc().isEmpty())
            {
                select.append(" AND lab902c17  IN('").append(filter.getRuc().stream().map(ruc -> ruc.toString()).collect(Collectors.joining("','"))).append("') ");
            }

            if (filter.getPhoneBox() != null && !filter.getPhoneBox().isEmpty())
            {

                select.append(" AND lab902c21  IN('").append(filter.getPhoneBox().stream().map(phonebox -> phonebox.toString()).collect(Collectors.joining("','"))).append("') ");
            }

            if (filter.getHistory() != null && !filter.getHistory().isEmpty())
            {
                select.append(" AND lab21.lab21c2  IN('").append(filter.getHistory().stream().map(history -> Tools.encrypt(history.toString())).collect(Collectors.joining("','"))).append("') ");
            }

            if (filter.getEmailPatient() != null && !filter.getEmailPatient().isEmpty())
            {

                select.append(" AND lab21.lab21c8  IN('").append(filter.getEmailPatient().stream().map(emailpatient -> emailpatient.toString()).collect(Collectors.joining("','"))).append("') ");
            }

            if (filter.getPhone() != null && !filter.getPhone().isEmpty())
            {
                select.append(" AND lab21.lab21c16  IN('").append(filter.getPhone().stream().map(phone -> phone.toString()).collect(Collectors.joining("','"))).append("') ");
            }

            if (filter.getBranchId() > 0)
            {
                select.append(" AND lab22.lab05c1 = ").append(filter.getBranchId());
            }

            select.append(" WHERE lab22.lab07c1 = 1  AND (lab22c19 = 0 or lab22c19 is null) AND lab901c9 = 1 AND lab901.lab901c5 BETWEEN '").append(filter.getStartDate()).append("' AND '").append(filter.getEndDate()).append("'")
                    .append(" group by idBranch, lab04.lab04c1, lab04.lab04c2,lab04.lab04c3, lab04.lab04c4, CAST(lab901c5 AS DATE)")
                    .append(" ORDER BY date asc");

            return getJdbcTemplate().query(select.toString(), (ResultSet rs, int i) ->
            {
                CashReportDetail detail = new CashReportDetail();
                detail.setBranchId(rs.getInt("idBranch"));
                detail.setBranchName(rs.getString("branch"));
                detail.setUserId(rs.getInt("lab04c1"));
                detail.setNames(rs.getString("lab04c2") + " " + rs.getString("lab04c3"));
                detail.setUserName(rs.getString("lab04c4"));
                detail.setPaymentTypeName("Efectivo");
                detail.setPaymentTypeAmount(rs.getDouble("total"));
                detail.setDatePayment(rs.getString("date"));
                detail.setTypeOfDetail("2");
                return detail;
            });
        } catch (DataAccessException ex)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Obtiene el detalle de reporte de caja por filtros especificos
     *
     * @param filter
     * @param account Indica si el sistema trabaja con cuenta
     * @param yearsQuery Años de consulta (historicos)
     *
     * @return Reporte de caja
     * @throws Exception Error en base de datos.
     */
    default List<DetailedCashReport> getDetailedCashReport(CashReportFilter filter, boolean account, int yearsQuery) throws Exception
    {
        try
        {
            List<DetailedCashReport> list = new LinkedList<>();
            int currentYear = DateTools.dateToNumberYear(new Date());

            List<Integer> years = Tools.listOfConsecutiveYears(Integer.toString(currentYear - yearsQuery), Integer.toString(currentYear));
            String lab22;

            for (Integer year : years)
            {
                lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
                boolean tableExists = getToolsDao().tableExists(getJdbcTemplate(), lab22);
                if (tableExists)
                {
                    StringBuilder select = new StringBuilder();
                    select.append(ISOLATION_READ_UNCOMMITTED);

                    select.append("SELECT lab903c1,"
                            + "lab902.lab22c1, lab01c2, lab903c3, lab902c2, lab902c3, lab902c4, lab902c5, lab902c6, lab902c7, lab902c8, lab902c9,"
                            + "lab902c16, lab902c17, lab902c21,"
                            + "lab22.lab05c1 as idBranch, lab05.lab05c4 as branch,"
                            + "lab903.lab04c1_i as idUserPayment, a.lab04c4 as usernamePayment,"
                            + "lab902.lab04c1_i as idUserCashbox, b.lab04c4 as usernameCashbox,"
                            + "lab21.lab21c2,"
                            + "lab21.lab21c3,"
                            + "lab21.lab21c4,"
                            + "lab21.lab21c5,"
                            + "lab21.lab21c6,"
                            + "lab902.lab902c10,"
                            + "lab22.lab904c1,"
                            + "lab904c2,"
                            + "lab904c3");

                    StringBuilder from = new StringBuilder();

                    from.append(" FROM lab902 ")
                            .append(" LEFT JOIN lab903 ON lab903.lab22c1 = lab902.lab22c1 ")
                            .append(" JOIN ").append(lab22).append(" as lab22 ON lab22.lab22c1 = lab902.lab22c1 ")
                            .append(" JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1 ")
                            .append(" JOIN lab904 ON lab904.lab904c1 = lab22.lab904c1 ")
                            .append(" JOIN lab05 ON lab05.lab05c1 = lab22.lab05c1 ")
                            .append(" LEFT JOIN lab01 ON lab01.lab01c1 = lab903.lab01c1 ")
                            .append(" LEFT JOIN lab04 a ON a.lab04c1 = lab903.lab04c1_i ")
                            .append(" LEFT JOIN lab04 b ON b.lab04c1 = lab902.lab04c1_i ");

                    if (account)
                    {
                        select.append(" , lab14c2, lab14c3 ");
                        from.append(" JOIN lab14 ON lab22.lab14c1 = lab14.lab14c1 ");
                    }

                    if (filter.getUserId() != null && filter.getUserId() > 0)
                    {
                        from.append(" WHERE ((lab903.lab04c1_i = ").append(filter.getUserId())
                                .append(" AND lab903c4 BETWEEN '").append(filter.getStartDate()).append("' AND '")
                                .append(filter.getEndDate()).append("')")
                                .append(" OR ( lab902.lab04c1_i = ").append(filter.getUserId())
                                .append(" AND lab902c10 BETWEEN '").append(filter.getStartDate()).append("' AND '")
                                .append(filter.getEndDate()).append("'))");

                    } else
                    {
                        from.append(" WHERE ((lab903c4 BETWEEN '").append(filter.getStartDate()).append("' AND '")
                                .append(filter.getEndDate()).append("')")
                                .append(" OR ( lab902c10 BETWEEN '").append(filter.getStartDate()).append("' AND '")
                                .append(filter.getEndDate()).append("'))");
                    }

                    if (filter.getRuc() != null && !filter.getRuc().isEmpty())
                    {
                        from.append(" AND lab902c17  IN('").append(filter.getRuc().stream().map(ruc -> ruc.toString()).collect(Collectors.joining("','"))).append("') ");
                    }

                    if (filter.getPhoneBox() != null && !filter.getPhoneBox().isEmpty())
                    {

                        from.append(" AND lab902c21  IN('").append(filter.getPhoneBox().stream().map(phonebox -> phonebox.toString()).collect(Collectors.joining("','"))).append("') ");
                    }

                    if (filter.getHistory() != null && !filter.getHistory().isEmpty())
                    {
                        from.append(" AND lab21.lab21c2  IN('").append(filter.getHistory().stream().map(history -> Tools.encrypt(history.toString())).collect(Collectors.joining("','"))).append("') ");
                    }

                    if (filter.getEmailPatient() != null && !filter.getEmailPatient().isEmpty())
                    {

                        from.append(" AND lab21.lab21c8  IN('").append(filter.getEmailPatient().stream().map(emailpatient -> emailpatient.toString()).collect(Collectors.joining("','"))).append("') ");
                    }

                    if (filter.getPhone() != null && !filter.getPhone().isEmpty())
                    {
                        from.append(" AND lab21.lab21c16  IN('").append(filter.getPhone().stream().map(phone -> phone.toString()).collect(Collectors.joining("','"))).append("') ");
                    }

                    if (filter.getBranchId() > 0)
                    {
                        from.append(" AND lab22.lab05c1 = ").append(filter.getBranchId());
                    }
                    
                   
                    RowMapper mapper = (RowMapper<DetailedCashReport>) (ResultSet rs, int i) ->
                    {
                        DetailedCashReport detail = new DetailedCashReport();
                        detail.setOrderId(rs.getLong("lab22c1"));

                        if (list.contains(detail))
                        {

                            if (rs.getString("lab903c1") != null)
                            {
                                DetailPaymentType payment = new DetailPaymentType();
                                payment.setName(rs.getString("lab01c2"));
                                payment.setValue(rs.getDouble("lab903c3"));
                                detail.getDetailPaymentType().add(payment);
                                detail.setUserId(rs.getInt("idUserPayment"));
                                detail.setUserNames(rs.getString("usernamePayment"));
                                detail.setBilled(rs.getString("lab902c16"));
                                detail.setRuc(rs.getString("lab902c17"));
                                detail.setPhone(rs.getString("lab902c21"));
                                detail.setBranchId(rs.getInt("idBranch"));
                                detail.setBranchName(rs.getString("branch"));
                                list.get(list.indexOf(detail)).getDetailPaymentType().add(payment);
                            }
                        } else
                        {

                            String name1 = Tools.decrypt(rs.getString("lab21c3") != null ? rs.getString("lab21c3") : "");
                            String name2 = Tools.decrypt(rs.getString("lab21c4") != null ? rs.getString("lab21c4") : "");
                            String lastName1 = Tools.decrypt(rs.getString("lab21c5") != null ? rs.getString("lab21c5") : "");
                            String lastName2 = Tools.decrypt(rs.getString("lab21c6") != null ? rs.getString("lab21c6") : "");

                            String fullName = name1;
                            fullName += name2.isEmpty() ? "" : " " + name2;
                            fullName += " " + lastName1;
                            fullName += lastName2.isEmpty() ? "" : " " + lastName2;

                            detail.setPatientName(fullName);

                            detail.setUserId(rs.getInt("idUserCashbox"));
                            detail.setUserNames(rs.getString("usernameCashbox"));
                            detail.setBranchId(rs.getInt("idBranch"));
                            detail.setBranchName(rs.getString("branch"));
                            detail.setCopago(rs.getDouble("lab902c6"));
                            detail.setModeratorFee(rs.getDouble("lab902c7"));
                            detail.setDiscount(rs.getDouble("lab902c3"));
                            detail.setDiscountPorcent(rs.getBigDecimal("lab902c4"));
                            detail.setSubTotal(rs.getDouble("lab902c2"));
                            detail.setTax(rs.getDouble("lab902c5"));
                            detail.setTotalPaid(rs.getDouble("lab902c8"));
                            detail.setDate(rs.getTimestamp("lab902c10"));
                            // Calculamos el total
                            detail.setTotal((detail.getSubTotal() + detail.getCopago() + rs.getDouble("lab902c7") + detail.getTax()) - detail.getDiscount());
                            detail.setBalance(rs.getDouble("lab902c9"));
                            detail.setBilled(rs.getString("lab902c16"));
                            detail.setRuc(rs.getString("lab902c17"));
                            detail.setPhoneBox(rs.getString("lab902c21"));

                            detail.setRateId(rs.getInt("lab904c1"));
                            detail.setRateCode(rs.getString("lab904c2") != null ? rs.getString("lab904c2") : "");
                            detail.setRateName(rs.getString("lab904c3") != null ? rs.getString("lab904c3") : "");

                            if (account)
                            {
                                detail.setAccountCode(rs.getString("lab14c2") != null ? rs.getString("lab14c2") : "");
                                detail.setAccountName(rs.getString("lab14c3") != null ? rs.getString("lab14c3") : "");
                            }

                            // Pagado
                            detail.setTypeOfDetail("1");

                            if (rs.getString("lab903c1") != null)
                            {
                                DetailPaymentType payment = new DetailPaymentType();
                                payment.setName(rs.getString("lab01c2"));
                                payment.setValue(rs.getDouble("lab903c3"));
                                detail.getDetailPaymentType().add(payment);
                                detail.setUserId(rs.getInt("idUserPayment"));
                                detail.setUserNames(rs.getString("usernamePayment"));
                            }
                            list.add(detail);
                        }
                        return detail;
                    };

                    getJdbcTemplate().query(select.toString() + " " + from.toString(), mapper);
                }
            }
            return list;
        } catch (DataAccessException ex)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Obtiene el reporte general de caja para las devoluciones
     *
     * @param orderNumber numero de la orden
     * @param filter Filtros para la consulta
     *
     * @return Reporte de caja
     * @throws Exception Error en base de datos.
     */
    default List<DetailPaymentType> getPaymentTypeByCashbox(Long orderNumber, CashReportFilter filter) throws Exception
    {
        try
        {
            StringBuilder select = new StringBuilder();
            select.append(ISOLATION_READ_UNCOMMITTED);
            select.append("SELECT lab903c3")
                    .append(", lab01.lab01c2 ")
                    .append(" FROM lab903 ")
                    .append("JOIN lab01 ON lab01.lab01c1 = lab903.lab01c1 ")
                    .append("WHERE lab22c1 = ").append(orderNumber);

            select.append(" AND lab903.lab903c4 BETWEEN '").append(filter.getStartDate()).append("' AND '")
                    .append(filter.getEndDate()).append("'");

            if (filter.getUserId() != null && filter.getUserId() > 0)
            {
                select.append(" AND lab903.lab04c1_i  = ").append(filter.getUserId());
            }

            return getJdbcTemplate().query(select.toString(), (ResultSet rs, int i) ->
            {
                DetailPaymentType detail = new DetailPaymentType();
                detail.setValue(rs.getDouble("lab903c3"));
                detail.setName(rs.getString("lab01c2"));
                return detail;
            });
        } catch (DataAccessException ex)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Obtiene el detalle de reporte de caja para las devoluciones
     *
     * @param filter Filtros generales de la consulta
     * @param account Indica si el sistema trabaja con cliente
     *
     * @return Reporte de caja
     * @throws Exception Error en base de datos.
     */
    default List<DetailedCashReport> getsDetailedCashReturns(CashReportFilter filter, boolean account) throws Exception
    {
        try
        {
            StringBuilder select = new StringBuilder();
            StringBuilder from = new StringBuilder();

            select.append(ISOLATION_READ_UNCOMMITTED);
            select.append("SELECT lab04.lab04c1 AS userId")
                    .append(", lab04.lab04c2 AS userNames")
                    .append(", lab04.lab04c3 AS userLastNames")
                    .append(", lab04.lab04c4 AS userName")
                    .append(", lab913.lab913c5 AS paymentTypeAmount")
                    .append(", lab22.lab22c1 AS orderId")
                    .append(", lab21.lab21c3 AS name1")
                    .append(", lab21.lab21c4 AS name2")
                    .append(", lab21.lab21c5 AS lastName1")
                    .append(", lab21.lab21c6 AS lastName2")
                    .append(", lab901.lab901c2 AS invoiceNumber")
                    .append(", lab920.lab920c8 AS copago")
                    .append(", lab920.lab920c5 AS discount")
                    .append(", lab901.lab901c4 AS total")
                    .append(", lab902.lab902c9 AS balance")
                    .append(", lab902.lab902c2 AS subtotal")
                    .append(", lab902.lab902c16 AS billed")
                    .append(", lab902.lab902c17 AS ruc")
                    .append(", lab902.lab902c21 AS phoneBox")
                    .append(", lab913.lab913c5 AS value")
                    .append(", lab913.lab913c1 AS creditnote")
                    .append(", lab913.lab913c3 , lab22.lab904c1, lab904c2, lab904c3 ");

            from.append(" FROM lab901 ")
                    .append(" JOIN lab920 ON lab920.lab901c1 = lab901.lab901c1 ")
                    .append(" JOIN lab913 ON lab913.lab901c1 = lab901.lab901c1 ")
                    .append(" JOIN lab04 ON lab04.lab04c1 = lab913.lab04c1 ")
                    .append(" JOIN lab22 ON lab22.lab22c1 = lab920.lab920c3 ")
                    .append(" JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1 ")
                    .append(" JOIN lab902 ON lab902.lab22c1 = lab22.lab22c1 ")
                    .append(" JOIN lab904 ON lab22.lab904c1 = lab904.lab904c1 ");

            if (account)
            {
                select.append(" , lab14c2, lab14c3 ");
                from.append(" JOIN lab14 ON lab22.lab14c1 = lab14.lab14c1 ");
            }

            from.append(" WHERE lab22.lab07c1 = 1  AND (lab22c19 = 0 or lab22c19 is null) AND lab901c9 = 1 AND lab901.lab901c5 BETWEEN '")
                    .append(filter.getStartDate()).append("' AND '")
                    .append(filter.getEndDate()).append("'");

            if (filter.getBranchId() > 0)
            {
                from.append(" AND lab22.lab05c1 = ").append(filter.getBranchId());
            }

            if (filter.getUserId() != null && filter.getUserId() > 0)
            {
                from.append(" AND lab901.lab04c1  = ").append(filter.getUserId());
            }

            if (filter.getRuc() != null && !filter.getRuc().isEmpty())
            {
                from.append(" AND lab902c17  IN('").append(filter.getRuc().stream().map(ruc -> ruc.toString()).collect(Collectors.joining("','"))).append("') ");
            }

            if (filter.getPhoneBox() != null && !filter.getPhoneBox().isEmpty())
            {

                from.append(" AND lab902c21  IN('").append(filter.getPhoneBox().stream().map(phonebox -> phonebox.toString()).collect(Collectors.joining("','"))).append("') ");
            }

            if (filter.getHistory() != null && !filter.getHistory().isEmpty())
            {
                from.append(" AND lab21c2  IN('").append(filter.getHistory().stream().map(history -> history.toString()).collect(Collectors.joining("','"))).append("') ");
            }

            if (filter.getEmailPatient() != null && !filter.getEmailPatient().isEmpty())
            {

                from.append(" AND lab21c8  IN('").append(filter.getEmailPatient().stream().map(emailpatient -> emailpatient.toString()).collect(Collectors.joining("','"))).append("') ");
            }

            if (filter.getPhone() != null && !filter.getPhone().isEmpty())
            {
                from.append(" AND lab21c16  IN('").append(filter.getPhone().stream().map(phone -> phone.toString()).collect(Collectors.joining("','"))).append("') ");
            }

            return getJdbcTemplate().query(select.toString() + from, (ResultSet rs, int i) ->
            {
                DetailedCashReport detail = new DetailedCashReport();

                String name1 = Tools.decrypt(rs.getString("name1"));
                String name2 = Tools.decrypt(rs.getString("name2"));
                String lastName1 = Tools.decrypt(rs.getString("lastName1"));
                String lastName2 = Tools.decrypt(rs.getString("lastName2"));

                String fullName = name1;
                fullName += name2.isEmpty() ? "" : " " + name2;
                fullName += " " + lastName1;
                fullName += lastName2.isEmpty() ? "" : " " + lastName1;

                detail.setUserId(rs.getInt("userId"));
                detail.setUserNames(rs.getString("userNames") + " " + rs.getString("userLastNames"));
                detail.setOrderId(rs.getLong("orderId"));
                detail.setPatientName(fullName);
                detail.setInvoiceNumber(rs.getString("invoiceNumber"));
                detail.setCopago(rs.getDouble("copago"));
                detail.setDiscount(rs.getDouble("discount"));
                detail.setTotal(rs.getDouble("total"));
                detail.setDateCreditNote(rs.getTimestamp("lab913c3"));
                detail.setBilled(rs.getString("billed"));
                detail.setRuc(rs.getString("ruc"));
                detail.setPhoneBox(rs.getString("phoneBox"));
                detail.setSubTotal(rs.getDouble("value"));
                detail.setCreditnote(rs.getInt("creditnote"));
                // Balance: total - valor nota credito
                Double balance = detail.getTotal() - detail.getSubTotal();
                detail.setBalance(balance);
                detail.setCashRegisterReceipt("0");
                // DevoluciÃ³n
                detail.setTypeOfDetail("2");

                detail.setRateId(rs.getInt("lab904c1"));
                detail.setRateCode(rs.getString("lab904c2"));
                detail.setRateName(rs.getString("lab904c3"));

                if (account)
                {
                    detail.setAccountCode(rs.getString("lab14c2"));
                    detail.setAccountName(rs.getString("lab14c3"));
                }

                return detail;
            });
        } catch (DataAccessException ex)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Consulta el nÃºmero de la factura por el id de la misma
     *
     * @param invoiceId
     * @return NÃºmero de la factura
     * @throws Exception Error en base de datos
     */
    default String getInvoiceNumberByInvoiceId(Integer invoiceId) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED);
            query.append("SELECT lab901c2 AS invoiceNumber")
                    .append(" FROM lab901 ")
                    .append("WHERE lab901c1 = ").append(invoiceId);

            return getJdbcTemplate().queryForObject(query.toString(), (ResultSet rs, int i)
                    ->
            {
                return rs.getString("invoiceNumber");
            });
        } catch (DataAccessException e)
        {
            return "";
        }
    }

    /**
     * Obtiene las ordenes de la cabecera de una factura y por el id de la
     * factura
     *
     * @param headerId Id de la cabecera
     * @param invoiceId Id de la factura
     * @param particular Indica si la factura es particular o por cliente
     * @param priceType Indica el precio con el que trabaja la factura
     * @param yearsQuery Años de consulta (Historicos)
     *
     * @return Ordenes que pertenecen a la cabecera de una factura
     * @throws Exception Error en base de datos
     */
    default List<PreInvoiceOrder> obtainsInvoiceOrdersByHeader(Long headerId, Long invoiceId, Integer particular, int priceType, int yearsQuery) throws Exception
    {
        try
        {
            List<PreInvoiceOrder> preinvoices = new LinkedList<>();
            int currentYear = DateTools.dateToNumberYear(new Date());
            List<Integer> years = Tools.listOfConsecutiveYears(Integer.toString(currentYear - yearsQuery), Integer.toString(currentYear));
            String lab22;
            String lab900;

            for (Integer year : years)
            {
                lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
                lab900 = year.equals(currentYear) ? "lab900" : "lab900_" + year;

                StringBuilder query = new StringBuilder();
                query.append("SELECT DISTINCT lab22.lab22c1 AS idOrder")
                        .append(", lab22.lab22c3 AS dateOfOrderCreation ")
                        .append(", lab21.lab21c1 AS idPatient ")
                        .append(", lab21.lab21c2 AS history")
                        .append(", lab21.lab21c3 AS name1")
                        .append(", lab21.lab21c4 AS name2")
                        .append(", lab21.lab21c5 AS lastName1")
                        .append(", lab21.lab21c6 AS lastName2")
                        .append(", lab54.lab54c2 AS documentTypeAbbr")
                        .append(", lab902.lab902c6 AS copago")
                        .append(", lab902.lab902c7 AS moderatorFee")
                        .append(", lab05.lab05c4 AS brachName")
                        .append(", lab19.lab19c2 AS namePhysician")
                        .append(", lab19.lab19c3 AS lastNamePhysician")
                        .append(", lab22.lab14c1 ")
                        .append(", lab990.lab990c1 ")
                        .append(", lab14.lab118c1 ")
                        .append(" FROM  ").append(lab22).append(" as lab22 ")
                        .append("JOIN ").append(lab900).append(" as lab900 ON lab900.lab22c1 = lab22.lab22c1 ")
                        .append("JOIN lab21 ON lab21.lab21c1 = lab900.lab21c1 ")
                        .append("JOIN lab54 ON lab54.lab54c1 = lab21.lab54c1 ")
                        .append("LEFT JOIN lab902 ON lab902.lab22c1 = lab22.lab22c1 ")
                        .append("LEFT JOIN lab14 ON lab14.lab14c1 = lab22.lab14c1 ")
                        .append("JOIN lab05 ON lab05.lab05c1 = lab900.lab05c1 ")
                        .append("LEFT JOIN lab19 ON lab19.lab19c1 = lab22.lab19c1 ")
                        .append("INNER JOIN lab993 ON lab993.lab904c1 = lab22.lab904c1 ")
                        .append("INNER JOIN lab990 ON lab990.lab990c1 = lab993.lab990c1 AND lab990.lab14c1 = lab22.lab14c1 AND lab990.lab07c1 = 1 ");

                if (particular == 1)
                {
                    query.append(" WHERE lab900.lab920c1p = ").append(headerId);
                } else
                {
                    query.append(" WHERE lab900.lab920c1 = ").append(headerId);
                }

                query.append(" AND (lab900.lab901c1i = ").append(invoiceId)
                        .append(" OR lab900.lab901c1p = ").append(invoiceId).append(")")
                        .append(" AND (lab22c19 = 0 or lab22c19 is null) ");
                

                getJdbcTemplate().query(query.toString(), (ResultSet rs, int i)
                        ->
                {
                    PreInvoiceOrder preInvoice = new PreInvoiceOrder();
                    preInvoice.setOrderId(rs.getLong("idOrder"));
                    preInvoice.setOrderCreationDate(rs.getTimestamp("dateOfOrderCreation"));
                    preInvoice.setPatientId(rs.getInt("idPatient"));
                    preInvoice.setHistoryPatient(Tools.decrypt(rs.getString("history")));
                    preInvoice.setFirstName(Tools.decrypt(rs.getString("name1")));
                    preInvoice.setSecondName(Tools.decrypt(rs.getString("name2")));
                    preInvoice.setLastName(Tools.decrypt(rs.getString("lastName1")));
                    preInvoice.setSecondLastName(Tools.decrypt(rs.getString("lastName2")));
                    preInvoice.setDocumentTypeAbbr(rs.getString("documentTypeAbbr"));
                    preInvoice.setCopayment(rs.getDouble("copago"));
                    preInvoice.setModeratorFee(rs.getDouble("moderatorFee"));
                    preInvoice.setBranchName(rs.getString("brachName"));
                    preInvoice.setNameDoctor(rs.getString("namePhysician"));
                    preInvoice.setLastNameDoctor(rs.getString("lastNamePhysician"));
                    preInvoice.setCustomerId(rs.getInt("lab14c1"));
                    preInvoice.setContractId(rs.getInt("lab990c1"));

                    try
                    {
                        preInvoice.setTests(getPreInvoiceTests(preInvoice.getOrderId(), null, null, invoiceId, headerId, particular, rs.getInt("lab118c1"), priceType));
                    } catch (Exception ex)
                    {
                        preInvoice.setTests(new ArrayList<>());
                    }

                    preinvoices.add(preInvoice);
                    return preInvoice;
                });
            }

            return preinvoices;
        } catch (DataAccessException e)
        {
            OrderCreationLog.error(e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Obtiene las ordenes de una factura por el Id de esta
     *
     * @param invoiceCode
     *
     * @return Ordenes que pertenecen a una factura
     * @throws Exception Error en base de datos
     */
    default List<CreditNote> getOrdersFromACreditNote(String invoiceCode) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab913.lab913c1")
                    .append(", lab913.lab913c3 ")
                    .append(", lab913.lab04c1 ")
                    .append(", lab913.lab913c4 ")
                    .append(", lab913.lab913c5 ")
                    .append(", lab04.lab04c4 ")
                    .append(" FROM lab901 ")
                    .append("inner join lab913 ON lab913.lab901c1 = lab901.lab901c1 ")
                    .append("inner join lab04 ON lab04.lab04c1 = lab913.lab04c1 ")
                    .append("WHERE lab901.lab901c2 = '").append(invoiceCode).append("'");

            return getJdbcTemplate().query(query.toString(), (ResultSet rs, int i)
                    ->
            {
                CreditNote creditNote = new CreditNote();
                creditNote.setId(rs.getInt("lab913c1"));
                creditNote.setDateOfNote(rs.getTimestamp("lab913c3"));
                creditNote.setUserId(rs.getInt("lab04c1"));
                creditNote.setUsername(rs.getString("lab04c4"));
                creditNote.setCancellationReason(rs.getString("lab913c4"));
                creditNote.setValue(rs.getDouble("lab913c5"));

                return creditNote;
            });
        } catch (DataAccessException e)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Obtiene un listado de los examenes para la nota credito de una factura
     *
     * @param orderId
     *
     * @return examenes de una orden correspondiente a una nota credito
     * @throws Exception Error en base de datos
     */
    default List<PreInvoiceTest> obtainsCreditNoteOrderReviews(long orderId) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab39.lab39c1 AS testId")
                    .append(", lab39.lab39c2 AS testCode")
                    .append(", lab39.lab39c3 AS testAbre")
                    .append(", lab39.lab39c4 AS testName")
                    .append(", lab915.lab915c2 AS servicePrice")
                    .append(", lab915.lab915c3 AS patientPrice")
                    .append(", lab915.lab915c4 AS insurancePrice")
                    .append(" FROM lab39 ")
                    .append("JOIN lab915 ON lab915.lab39c1 = lab39.lab39c1 ")
                    .append("WHERE lab915.lab22c1 = ").append(orderId);

            return getJdbcTemplate().query(query.toString(), (ResultSet rs, int i)
                    ->
            {
                PreInvoiceTest test = new PreInvoiceTest();
                test.setTestId(rs.getInt("testId"));
                test.setTestCode(rs.getString("testCode"));
                test.setTestAbbr(rs.getString("testAbre"));
                test.setTestName(rs.getString("testName"));
                test.setServicePrice(rs.getDouble("servicePrice"));
                test.setPatientPrice(rs.getDouble("patientPrice"));
                test.setInsurancePrice(rs.getDouble("insurancePrice"));
                return test;
            });
        } catch (DataAccessException e)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Obtiene un listado del detalle simple de las facturas que apliquen segun
     * que filtros
     *
     * @param filter
     * @param initDate
     * @param finalDate
     *
     * @return examenes de una orden correspondiente a una nota credito
     * @throws Exception Error en base de datos
     */
    default List<SimpleInvoice> getListInvoices(FilterSimpleInvoice filter, String initDate, String finalDate) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            String where = "";
            query.append("SELECT DISTINCT lab901.lab901c1, lab901c2 ")
                    .append(", lab901c4, lab901.lab04c1, lab04c4, lab901c5, lab901c9 ,lab901c24, lab901c25, lab901c30, lab901c31, lab901.lab14c1, lab14.lab14c2, lab14.lab14c3, lab14.lab14c4, lab14.lab14c14 ")
                    .append(" FROM lab901 ")
                    .append(" INNER JOIN lab04 ON lab04.lab04c1 = lab901.lab04c1")
                    .append(" LEFT JOIN lab913 ON lab913.lab901c1 = lab901.lab901c1 ")
                    .append(" LEFT JOIN lab14 ON lab14.lab14c1 = lab901.lab14c1 ");

            if (filter.getIdInitialInvoice() > 0)
            {
                where += where.contains("WHERE") ? " AND lab901.lab901c1 >= " + filter.getIdInitialInvoice() : " WHERE lab901.lab901c1 >= " + filter.getIdInitialInvoice();
            }
            if (filter.getIdFinalInvoice() > 0)
            {
                where += where.contains("WHERE") ? " AND lab901.lab901c1 <= " + filter.getIdFinalInvoice() : " WHERE lab901.lab901c1 <= " + filter.getIdFinalInvoice();
            }
            if (filter.getInvoiceDate() != null)
            {
                where += where.contains("WHERE") ? " AND lab901.lab901c5 BETWEEN '" + initDate + "'" + " AND '" + finalDate + "'" : " WHERE lab901.lab901c5 BETWEEN '" + initDate + "'" + " AND '" + finalDate + "'";
            }
            if (filter.getCreditNoteId() != null && filter.getCreditNoteId() > 0)
            {
                where += where.contains("WHERE") ? " AND lab913.lab913c1 = " + filter.getCreditNoteId() : " WHERE lab913.lab913c1 = " + filter.getCreditNoteId();
            }
            if (filter.getCustomer() != null && filter.getCustomer() > 0)
            {
                query.append(" INNER JOIN lab920 ON lab920.lab901c1 = lab901.lab901c1 AND lab14c1 =  ").append(filter.getCustomer());
            }

            // AÃ±adimos los parametros de consulta a la consulta
            query.append(where);
            return getJdbcTemplate().query(query.toString(), (ResultSet rs, int i)
                    ->
            {
                SimpleInvoice invoice = new SimpleInvoice();
                invoice.setId(rs.getLong("lab901c1"));
                invoice.setInvoiceNumber(rs.getString("lab901c2"));
                invoice.setTotal(rs.getDouble("lab901c4"));
                invoice.setIdUser(rs.getInt("lab04c1"));
                invoice.setUserName(rs.getString("lab04c4"));
                invoice.setDate(rs.getTimestamp("lab901c5"));
                invoice.setParticular(rs.getInt("lab901c9"));
                invoice.setExpirationDate(rs.getTimestamp("lab901c24"));
                invoice.setPaymentForm(rs.getInt("lab901c25"));
                invoice.setStatus(rs.getInt("lab901c31"));
                invoice.setPaymentDate(rs.getTimestamp("lab901c30"));
                invoice.setCustomerId(rs.getInt("lab14c1"));
                invoice.setAccountNit(rs.getString("lab14c2"));
                invoice.setAccountName(rs.getString("lab14c3"));
                invoice.setAccountPhone(rs.getString("lab14c4"));
                invoice.setAccountAddress(rs.getString("lab14c14"));
                return invoice;
            });
        } catch (DataAccessException e)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Consulta el id de la factura de Siigo por el id de la factura LIS
     *
     * @param invoiceId
     * @return Id de la factura en Siigo
     * @throws Exception Error en base de datos
     */
    default String getSiigoInvoiceId(Long invoiceId) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED);
            query.append("SELECT lab901c26 AS siigoInvoiceId")
                    .append(" FROM lab901 ")
                    .append("WHERE lab901c1 = ").append(invoiceId);

            return getJdbcTemplate().queryForObject(query.toString(), (ResultSet rs, int i)
                    ->
            {
                return rs.getString("siigoInvoiceId");
            });
        } catch (DataAccessException e)
        {
            return "";
        }
    }

    /**
     * Obtiene el detalle de una factura por el Id de esta, este detalle sera el
     * de las notas credito
     *
     * @param invoiceId
     * @param yearsQuery Años de consulta (Historicos)
     *
     * @return Ordenes que pertenecen a una factura
     * @throws Exception Error en base de datos
     */
    default List<PreInvoiceOrder> getInvoiceDetailFromACreditNote(Long invoiceId, int yearsQuery) throws Exception
    {
        try
        {
            List<PreInvoiceOrder> preinvoices = new LinkedList<>();
            int currentYear = DateTools.dateToNumberYear(new Date());
            List<Integer> years = Tools.listOfConsecutiveYears(Integer.toString(currentYear - yearsQuery), Integer.toString(currentYear));
            String lab22;
            for (Integer year : years)
            {
                lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;

                StringBuilder query = new StringBuilder();
                query.append("SELECT DISTINCT lab22.lab22c1 AS idOrder")
                        .append(", lab22.lab22c3 AS dateOfOrderCreation ")
                        .append(", lab21.lab21c1 AS idPatient ")
                        .append(", lab21.lab21c2 AS history")
                        .append(", lab21.lab21c3 AS name1")
                        .append(", lab21.lab21c4 AS name2")
                        .append(", lab21.lab21c5 AS lastName1")
                        .append(", lab21.lab21c6 AS lastName2")
                        .append(", lab54.lab54c2 AS documentTypeAbbr")
                        .append(", lab902.lab902c6 AS copago")
                        .append(", lab902.lab902c7 AS moderatorFee")
                        .append(", lab05.lab05c4 AS brachName")
                        .append(" FROM lab913 ")
                        .append("JOIN lab915 ON lab915.lab913c1 = lab913.lab913c1 ")
                        .append("JOIN ").append(lab22).append(" AS lab22 ON lab22.lab22c1 = lab915.lab22c1 ")
                        .append("JOIN lab21 ON lab21.lab21c1 = lab915.lab21c1 ")
                        .append("JOIN lab54 ON lab54.lab54c1 = lab21.lab54c1 ")
                        .append("LEFT JOIN lab902 ON lab902.lab22c1 = lab22.lab22c1 ")
                        .append("JOIN lab05 ON lab05.lab05c1 = lab22.lab05c1 ")
                        .append("WHERE lab913.lab901c1 = ").append(invoiceId);

                return getJdbcTemplate().query(query.toString(), (ResultSet rs, int i)
                        ->
                {
                    PreInvoiceOrder preInvoice = new PreInvoiceOrder();
                    preInvoice.setOrderId(rs.getLong("idOrder"));
                    preInvoice.setOrderCreationDate(rs.getTimestamp("dateOfOrderCreation"));
                    preInvoice.setPatientId(rs.getInt("idPatient"));
                    preInvoice.setHistoryPatient(Tools.decrypt(rs.getString("history")));
                    preInvoice.setFirstName(Tools.decrypt(rs.getString("name1")));
                    preInvoice.setSecondName(Tools.decrypt(rs.getString("name2")));
                    preInvoice.setLastName(Tools.decrypt(rs.getString("lastName1")));
                    preInvoice.setSecondLastName(Tools.decrypt(rs.getString("lastName2")));
                    preInvoice.setDocumentTypeAbbr(rs.getString("documentTypeAbbr"));
                    preInvoice.setCopayment(rs.getDouble("copago"));
                    preInvoice.setModeratorFee(rs.getDouble("moderatorFee"));
                    preInvoice.setBranchName(rs.getString("brachName"));
                    try
                    {
                        preInvoice.setTests(obtainsCreditNoteOrderReviews(preInvoice.getOrderId()));
                    } catch (Exception ex)
                    {
                        preInvoice.setTests(new ArrayList<>());
                    }
                    preinvoices.add(preInvoice);
                    return preInvoice;
                });
            }
            return preinvoices;

        } catch (DataAccessException e)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Consulta el valor mensual de un contrato
     *
     * @param contractId Id del contrato
     *
     * @return valor mensual
     * @throws Exception Error en base de datos.
     */
    default Double getMonthlyValueContractById(int contractId) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab990c25")
                    .append(" FROM lab990 ")
                    .append("WHERE lab990c1 = ").append(contractId);

            return getJdbcTemplate().queryForObject(query.toString(), (ResultSet rs, int i)
                    ->
            {
                return rs.getDouble("lab990c25");
            });
        } catch (DataAccessException e)
        {
            return null;
        }
    }

    /**
     * Actualiza el valor de pago de una factura
     *
     * @param invoicePayment pago de la factura
     * @return True - Pago realizado, False - Fallo al realizÃ¡r el pago
     * @throws Exception Error en base de datos.
     */
    default boolean updateInvoicePayment(InvoicePayment invoicePayment) throws Exception
    {
        try
        {
            return getJdbcTemplate().update("UPDATE lab901 SET lab901c30 = ?"
                    + ", lab901c31 = ?"
                    + " WHERE lab901c2 = ?",
                    invoicePayment.getPaymentDate(),
                    1, // 1 - Pagado
                    invoicePayment.getInvoiceNumber()) > 0;
        } catch (DataAccessException e)
        {
            return false;
        }
    }

    /**
     * Inserta las cabeceras de una factura
     *
     * @param headers lista de cabeceras
     * @param idInvoice Id de la factura
     * @return
     * @throws Exception Error en base de datos.
     */
    default int insertHeadersInvoice(List<PreInvoiceHeader> headers, Long idInvoice) throws Exception
    {
        try
        {
            headers.forEach(header ->
            {
                try
                {
                    SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                            .withTableName("lab920")
                            .usingGeneratedKeyColumns("lab920c1");

                    HashMap parameters = new HashMap();
                    parameters.put("lab901c1", idInvoice);
                    parameters.put("lab990c1", header.getContractId());
                    parameters.put("lab920c2", header.getTotalPaidInCash());
                    parameters.put("lab920c3", header.getInitialOrder());
                    parameters.put("lab920c4", header.getFinalOrder());
                    parameters.put("lab920c5", header.getDiscount());
                    parameters.put("lab920c6", header.getTax());
                    parameters.put("lab920c7", header.getTotalModeratingFee());
                    parameters.put("lab920c8", header.getTotalCopayment());
                    parameters.put("lab920c9", header.getCapitated());
                    parameters.put("lab920c10", header.getOrders().size());
                    parameters.put("lab920c11", header.getDatePerCapita());
                    parameters.put("lab920c12", header.getTotal());

                    Number key = insert.executeAndReturnKey(parameters);
                    header.setId(key.longValue());
                    insertTaxs(header);
                } catch (Exception ex)
                {
                    Logger.getLogger(InvoiceDao.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

            return 1;
        } catch (Exception e)
        {
            e.getMessage();
            return -1;
        }
    }

    /**
     * Inserta las cabeceras de una factura
     *
     * @param header Cabecera
     * @return
     * @throws Exception Error en base de datos.
     */
    default int insertTaxs(PreInvoiceHeader header) throws Exception
    {
        List<Tax> listTax = listTax(header.getContractId());
        List<TaxInvoice> listTaxInv = new LinkedList<>();
        //creamos dao para insertar en la nueva tabla lab916 con el numero factura el id del impuesto y el valor.
        for (Tax tax : listTax)
        {
            TaxInvoice taxInvoice = new TaxInvoice();
            taxInvoice.setHeaderId(header.getId());
            taxInvoice.setTaxId(tax.getTaxId());
            taxInvoice.setName(tax.getName());
            taxInvoice.setValue(((header.getTotal() * tax.getValue()) / 100));
            listTaxInv.add(taxInvoice);
        }
        insertTaxHeader(listTaxInv);
        return 1;
    }

    /**
     * Crea el registro de del Impuesto por Cabecera
     *
     * @param taxes Lista de impuestos de la cabecera
     * @return
     * @throws Exception Error en base de datos.
     */
    default int insertTaxHeader(List<TaxInvoice> taxes) throws Exception
    {
        List<HashMap> batchArray = new ArrayList<>();

        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab916")
                .usingGeneratedKeyColumns("lab916c1");

        taxes.stream().map((tax)
                ->
        {
            HashMap parameters = new HashMap();
            parameters.put("lab916c2", tax.getHeaderId());
            parameters.put("lab916c3", tax.getTaxId());
            parameters.put("lab916c4", tax.getValue());
            return parameters;
        }).forEachOrdered((parameters)
                ->
        {
            batchArray.add(parameters);
        });

        insert.executeBatch(batchArray.toArray(new HashMap[taxes.size()]));

        return 1;
    }

    /**
     * Consulta la factura
     *
     * @param invoiceNumber NÃºmero de factura
     * @param orders indica si la consulta debe traer las ordenes y los examenes
     * @param priceType Indica el precio con el que trabaja la factura
     * @param yearsQuery Años de consulta (Historicos)
     * @return factura
     * @throws Exception Error en base de datos
     */
    default Invoice getInvoice(String invoiceNumber, Boolean orders, int priceType, int yearsQuery) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED);

            query.append("SELECT lab901c1, lab901c2, lab901c3, lab901c4, lab901c5, lab907c1 "
                    + ", lab901.lab04c1, lab901c9, lab901c10, lab906c1, lab901c11, lab901c12, lab909c1, lab901c13, lab901c14, "
                    + "lab901c17, lab901c19, lab901c20, lab901c21, lab901c22, lab901c23, lab901c24, lab901c25, lab901.lab21c1, "
                    + "lab901c26, lab901c27, lab901c30, lab901c31, lab21c2, lab21c3, lab21c4, lab21c5, lab21c6, lab21c16, lab21c17, "
                    + " lab901.lab14c1, lab14.lab14c2, lab14.lab14c3, lab14.lab14c4, lab14.lab14c14 ");

            StringBuilder from = new StringBuilder();
            from.append(" FROM lab901 ");
            from.append(" LEFT JOIN lab21 ON lab21.lab21c1 = lab901.lab21c1");
            from.append(" LEFT JOIN lab14 ON lab14.lab14c1 = lab901.lab14c1");

            StringBuilder where = new StringBuilder();

            where.append(" WHERE lab901c2 = '").append(invoiceNumber).append("'");

            query.append(from).append(where);

            return getJdbcTemplate().queryForObject(query.toString(), (ResultSet rs, int i)
                    ->
            {
                Invoice invoice = new Invoice();

                invoice.setInvoiceId(rs.getLong("lab901c1"));
                invoice.setInvoiceNumber(rs.getString("lab901c2"));
                invoice.setDiscount(rs.getDouble("lab901c3"));
                invoice.setTotal(rs.getDouble("lab901c4"));
                invoice.setDateOfInvoice(rs.getTimestamp("lab901c5"));
                invoice.setResolutionId(rs.getInt("lab907c1"));
                invoice.setUserId(rs.getInt("lab04c1"));
                invoice.setParticular(rs.getInt("lab901c9"));
                invoice.setState(rs.getInt("lab901c10") == 0);
                invoice.setPayerId(rs.getInt("lab906c1"));
                invoice.setTypeOfInvoice(rs.getInt("lab901c11"));
                invoice.setTax(rs.getDouble("lab901c12"));
                invoice.setIdSubsequentCredits(rs.getInt("lab909c1"));
                invoice.setComment(rs.getString("lab901c13"));
                invoice.setCfdi(rs.getString("lab901c14"));
                invoice.setTotalPaid(rs.getDouble("lab901c17"));
                invoice.setSvTypeOfInvoice(rs.getInt("lab901c19"));
                invoice.setSvRetention(rs.getDouble("lab901c20"));
                invoice.setSvMaximumRetention(rs.getString("lab901c21") == null ? null : new BigInteger(rs.getString("lab901c21")));
                invoice.setSvThirdPartyInvoice(rs.getString("lab901c22"));
                invoice.setSent(rs.getInt("lab901c23") == 0);
                invoice.setDueDate(rs.getTimestamp("lab901c24"));
                invoice.setFormOfPayment(rs.getInt("lab901c25"));
                invoice.setPatientId(rs.getInt("lab21c1"));
                invoice.setBillingPeriod(rs.getString("lab901c27"));
                invoice.setPaymentDate(rs.getTimestamp("lab901c30"));
                invoice.setPaid(rs.getInt("lab901c31") == 0);
                invoice.setHistory(Tools.decrypt(rs.getString("lab21c2")));
                invoice.setName1(Tools.decrypt(rs.getString("lab21c3")));
                invoice.setName2(Tools.decrypt(rs.getString("lab21c4")));
                invoice.setLastName(Tools.decrypt(rs.getString("lab21c5")));
                invoice.setSurName(Tools.decrypt(rs.getString("lab21c6")));
                invoice.setPhone(rs.getString("lab21c16"));
                invoice.setAddress(rs.getString("lab21c17"));
                invoice.setCustomerId(rs.getInt("lab14c1"));
                invoice.setAccountNit(rs.getString("lab14c2"));
                invoice.setAccountName(rs.getString("lab14c3"));
                invoice.setAccountPhone(rs.getString("lab14c4"));
                invoice.setAccountAddress(rs.getString("lab14c14"));

                try
                {
                    invoice.setInvoiceHeader(getHeadersByInvoice(invoice.getInvoiceId(), orders, invoice.getParticular(), priceType, yearsQuery));
                } catch (Exception ex)
                {
                    invoice.setInvoiceHeader(new ArrayList<>());
                }

                return invoice;
            });
        } catch (DataAccessException e)
        {
            return null;
        }
    }

    /**
     * Obtiene la lista de cabeceras de una factura
     *
     * @param invoiceId Id de la factura
     * @param orders Indica si la consulta debe ir con ordenes y examenes
     * @param particular Indica si la busqueda es por factura particular o por
     * cliente
     * @param priceType Indica el precio con el que trabaja la factura
     * @param yearsQuery Años de consulta (Historicos)
     *
     * @return cabeceras de la factura
     * @throws Exception Error en base de datos
     */
    default List<PreInvoiceHeader> getHeadersByInvoice(Long invoiceId, Boolean orders, Integer particular, int priceType, int yearsQuery) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab920c1, lab901c1, lab920.lab990c1, lab990c2, lab920c2, "
                    + " lab990c13, lab990c14, lab990c15, lab990c21, lab990c22,lab63c3, lab920c3, lab920c4, lab920c5, "
                    + "lab920c6, lab920c7, lab920c8, lab920c9, lab920c10, lab920c11, lab920c12 ");

            StringBuilder from = new StringBuilder();
            from.append(" FROM lab920 ")
                    .append(" LEFT JOIN lab990 ON lab990.lab990c1 = lab920.lab990c1 ")
                    .append(" LEFT JOIN lab63 ON lab63.lab63c1 = lab990c22 ")
                    .append(" LEFT JOIN lab993 ON lab993.lab990c1 = lab990.lab990c1 ");

            StringBuilder where = new StringBuilder();
            List<Object> params = new ArrayList<>();

            where.append(" WHERE lab901c1 = ?");

            params.add(invoiceId);

            RowMapper mapper = (RowMapper<PreInvoiceHeader>) (ResultSet rs, int i) ->
            {
                PreInvoiceHeader header = new PreInvoiceHeader();
                header.setId(rs.getLong("lab920c1"));
                header.setContractCode(rs.getString("lab990c21"));
                header.setAccountCity(rs.getString("lab990c22"));
                header.setAccountCityName(rs.getString("lab63c3"));
                header.setContractId(rs.getInt("lab990c1"));
                header.setContractName(rs.getString("lab990c2"));
                header.setTotalPaidInCash(rs.getDouble("lab920c2"));
                header.setInitialOrder(rs.getLong("lab920c3"));
                header.setFinalOrder(rs.getLong("lab920c4"));
                header.setDiscount(rs.getDouble("lab920c5"));
                header.setTax(rs.getDouble("lab920c6"));
                header.setTotalModeratingFee(rs.getDouble("lab920c7"));
                header.setTotalCopayment(rs.getDouble("lab920c8"));
                header.setCapitated(rs.getInt("lab920c9"));
                header.setTotalOrders(rs.getInt("lab920c10"));
                header.setDatePerCapita(rs.getString("lab920c11"));
                header.setTotal(rs.getDouble("lab920c12"));
                header.setCopayment(rs.getInt("lab990c14"));
                header.setModeratingFee(rs.getInt("lab990c13"));
                header.setRegimen(rs.getString("lab990c15"));

                try
                {
                    header.setTaxs(getTaxesByHeader(header.getId()));
                } catch (Exception ex)
                {
                    header.setTaxs(new ArrayList<>());
                }

                try
                {
                    header.setRates(getRatesByContract(header.getContractId()));
                } catch (Exception ex)
                {
                    header.setRates(new ArrayList<>());
                }

                if (orders)
                {
                    try
                    {
                        header.setOrders(obtainsInvoiceOrdersByHeader(header.getId(), invoiceId, particular, priceType, yearsQuery));
                    } catch (Exception e)
                    {
                        header.setOrders(new ArrayList<>());
                    }
                }

                return header;
            };
            return getJdbcTemplate().query(query.toString() + from + where, mapper, params.toArray());

        } catch (DataAccessException e)
        {
            return null;
        }
    }

    /**
     * Obtiene una lista de los impuestos por cabecera
     *
     * @param headerId id de la cabecera
     * @return lista de los Impuestos por cabecera
     * @throws Exception Error en base de datos.
     */
    default List<TaxInvoice> getTaxesByHeader(Long headerId) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab916c1 ,lab916c2 ,lab916c3 ,lab916c4 ")
                    .append(" FROM lab916 ")
                    .append("WHERE lab916c2 = ").append(headerId);

            return getJdbcTemplate().query(query.toString(), (ResultSet rs, int i)
                    ->
            {
                TaxInvoice taxInvoice = new TaxInvoice();

                taxInvoice.setId(rs.getInt("lab916c1"));
                taxInvoice.setHeaderId(rs.getLong("lab916c2"));
                taxInvoice.setTaxId(rs.getInt("lab916c3"));
                taxInvoice.setValue(rs.getDouble("lab916c4"));

                return taxInvoice;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Obtiene la lista de tarifas de un contrato
     *
     * @param contractId Id del contrato
     * @return Tarifas de un contrato
     * @throws Exception Error en base de datos
     */
    default List<RatesByContract> getRatesByContract(Integer contractId) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab993.lab904c1, lab904c2, lab904c3 ");

            StringBuilder from = new StringBuilder();
            from.append(" FROM lab993 ")
                    .append(" INNER JOIN lab904 ON lab993.lab904c1 = lab904.lab904c1 ");

            StringBuilder where = new StringBuilder();
            List<Object> params = new ArrayList<>();

            where.append(" WHERE lab990c1 = ?");

            params.add(contractId);

            RowMapper mapper = (RowMapper<RatesByContract>) (ResultSet rs, int i) ->
            {
                RatesByContract rate = new RatesByContract();
                rate.setRateId(rs.getInt("lab904c1"));
                rate.setRateCode(rs.getString("lab904c2"));
                rate.setNameRate(rs.getString("lab904c3"));

                return rate;
            };
            return getJdbcTemplate().query(query.toString() + from + where, mapper, params.toArray());

        } catch (DataAccessException e)
        {
            return null;
        }
    }

    /**
     * Obtiene el detalle de reporte de caja por filtros especificos
     *
     * @param filter
     *
     * @return Reporte de caja
     * @throws Exception Error en base de datos.
     */
    default List<DetailedCashReport> cashBalances(CashReportFilter filter) throws Exception
    {
        try
        {
            StringBuilder select = new StringBuilder();
            select.append(ISOLATION_READ_UNCOMMITTED);
            select.append("SELECT lab04.lab04c1 AS userId")
                    .append(", lab04.lab04c2 AS userNames")
                    .append(", lab04.lab04c3 AS userLastNames")
                    .append(", lab04.lab04c4 AS userName")
                    .append(", lab22.lab22c1 AS orderId")
                    .append(", lab21.lab21c3 AS name1")
                    .append(", lab21.lab21c4 AS name2")
                    .append(", lab21.lab21c5 AS lastName1")
                    .append(", lab21.lab21c6 AS lastName2")
                    .append(", lab21.lab21c16 AS phone")
                    .append(", lab22.lab05c1 AS idBranch")
                    .append(", lab05.lab05c4 AS branch")
                    .append(", lab902.lab902c9 AS balance")
                    .append(", lab902.lab902c2 AS subtotal")
                    .append(", lab902.lab902c6 AS copago")
                    .append(", lab902.lab902c3 AS discount")
                    .append(", lab902.lab902c4 AS discountPorcent")
                    .append(", lab902.lab902c7 AS moderatorFee")
                    .append(", lab902.lab902c5 AS tax")
                    .append(", lab902.lab902c16 AS billed")
                    .append(", lab902.lab902c17 AS ruc")
                    .append(", lab902.lab902c21 AS phonebox")
                    .append(", lab902.lab902c10, lab22.lab904c1, lab904c2, lab904c3 ")
                    .append(" FROM lab902 ")
                    .append(" JOIN lab04 ON lab04.lab04c1 = lab902.lab04c1_i ")
                    .append(" JOIN lab22 ON lab22.lab22c1 = lab902.lab22c1 ")
                    .append(" JOIN lab05 ON lab05.lab05c1 = lab22.lab05c1 ")
                    .append(" JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1 ")
                    .append(" JOIN lab904 ON lab904.lab904c1 = lab22.lab904c1 ")
                    .append(" WHERE lab902.lab902c10 BETWEEN '").append(filter.getStartDate()).append("' AND '")
                    .append(filter.getEndDate()).append("'");

            if (filter.getBranchId() > 0)
            {
                select.append(" AND lab22.lab05c1 = ").append(filter.getBranchId());
            }

            select.append(" AND lab902c9 > 0 AND lab22.lab07c1 = 1  AND (lab22c19 = 0 or lab22c19 is null) ");

            if (filter.getUserId() != null && filter.getUserId() > 0)
            {
                select.append(" AND lab902.lab04c1_i  = ").append(filter.getUserId());
            }

            //Filtro de saldos
            switch (filter.getTypeBalance())
            {
                case 1:
                    select.append(" AND lab902c18 = 0 AND lab902c20 = 0 ");
                    break;
                case 2:
                    select.append(" AND lab902c18 = 1 AND lab902c20 = 0 ");
                    break;
                case 3:
                    select.append(" AND lab902c18 = 2 AND lab902c20 = 0 ");
                    break;
                case 4:
                    select.append(" AND lab902c18 = 0 AND lab902c20 = 1 AND lab902c19 = ").append(Constants.UNBILLED);
                    break;
                default:
                    break;
            }

            if (filter.getRuc() != null && !filter.getRuc().isEmpty())
            {
                select.append(" AND lab902c17  IN('").append(filter.getRuc().stream().map(ruc -> ruc.toString()).collect(Collectors.joining("','"))).append("') ");
            }

            if (filter.getPhoneBox() != null && !filter.getPhoneBox().isEmpty())
            {

                select.append(" AND lab902c21  IN('").append(filter.getPhoneBox().stream().map(phonebox -> phonebox.toString()).collect(Collectors.joining("','"))).append("') ");
            }

            if (filter.getHistory() != null && !filter.getHistory().isEmpty())
            {
                select.append(" AND lab21.lab21c2  IN('").append(filter.getHistory().stream().map(history -> Tools.encrypt(history.toString())).collect(Collectors.joining("','"))).append("') ");
            }

            if (filter.getEmailPatient() != null && !filter.getEmailPatient().isEmpty())
            {

                select.append(" AND lab21.lab21c8  IN('").append(filter.getEmailPatient().stream().map(emailpatient -> emailpatient.toString()).collect(Collectors.joining("','"))).append("') ");
            }

            if (filter.getPhone() != null && !filter.getPhone().isEmpty())
            {
                select.append(" AND lab21.lab21c16  IN('").append(filter.getPhone().stream().map(phone -> phone.toString()).collect(Collectors.joining("','"))).append("') ");
            }

            return getJdbcTemplate().query(select.toString(), (ResultSet rs, int i) ->
            {
                DetailedCashReport detail = new DetailedCashReport();

                String name1 = Tools.decrypt(rs.getString("name1") != null ? rs.getString("name1") : "");
                String name2 = Tools.decrypt(rs.getString("name2") != null ? rs.getString("name2") : "");
                String lastName1 = Tools.decrypt(rs.getString("lastName1") != null ? rs.getString("lastName1") : "");
                String lastName2 = Tools.decrypt(rs.getString("lastName2") != null ? rs.getString("lastName2") : "");

                String fullName = name1;
                fullName += name2.isEmpty() ? "" : " " + name2;
                fullName += " " + lastName1;
                fullName += lastName2.isEmpty() ? "" : " " + lastName2;

                detail.setBilled(rs.getString("billed"));
                detail.setRuc(rs.getString("ruc"));
                detail.setPhoneBox(rs.getString("phonebox"));
                detail.setBranchId(rs.getInt("idBranch"));
                detail.setBranchName(rs.getString("branch"));
                detail.setUserId(rs.getInt("userId"));
                detail.setUserNames(rs.getString("userNames") != null ? rs.getString("userNames") : "" + " " + rs.getString("userLastNames") != null ? rs.getString("userLastNames") : "");
                detail.setOrderId(rs.getLong("orderId"));
                detail.setPatientName(fullName);
                detail.setCopago(rs.getDouble("copago"));
                detail.setModeratorFee(rs.getDouble("moderatorFee"));
                detail.setDiscount(rs.getDouble("discount"));
                detail.setDiscountPorcent(rs.getBigDecimal("discountPorcent"));
                detail.setSubTotal(rs.getDouble("subtotal"));
                detail.setTax(rs.getDouble("tax"));
                detail.setDate(rs.getTimestamp("lab902c10"));
                detail.setTotal((detail.getSubTotal() + detail.getCopago() + rs.getDouble("moderatorFee") + detail.getTax()) - detail.getDiscount());
                detail.setBalance(rs.getDouble("balance"));
                detail.setRateId(rs.getInt("lab904c1"));
                detail.setRateCode(rs.getString("lab904c2") != null ? rs.getString("lab904c2") : "");
                detail.setRateName(rs.getString("lab904c3") != null ? rs.getString("lab904c3") : "");
                detail.setPhone(rs.getString("phone") != null ? rs.getString("phone") : "");
                return detail;
            });
        } catch (DataAccessException ex)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Obtiene el detalle de reporte de caja por filtros especificos
     *
     * @param filter
     *
     * @return Reporte de caja
     * @throws Exception Error en base de datos.
     */
    default List<DetailedCashReport> unbilled(CashReportFilter filter) throws Exception
    {
        try
        {
            StringBuilder select = new StringBuilder();
            select.append(ISOLATION_READ_UNCOMMITTED);
            select.append("SELECT lab900.lab22c1, SUM(lab900.lab900c3) AS total, lab21.lab21c1, lab22.lab05c1, lab05.lab05c4, lab21c2, lab21c3,lab21c4, lab21c5, lab21c6, lab21c16, lab902c16, lab902c17, lab902c21")
                    .append(" FROM lab900 ")
                    .append(" LEFT JOIN lab22 ON lab22.lab22c1 = lab900.lab22c1 ")
                    .append(" LEFT JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1 ")
                    .append(" LEFT JOIN lab902 ON lab902.lab22c1 = lab22.lab22c1 ")
                    .append(" JOIN lab05 ON lab05.lab05c1 = lab22.lab05c1 ")
                    .append(" WHERE lab902c1 IS NULL AND lab22.lab07c1 = 1  AND (lab22c19 = 0 or lab22c19 is null) ")
                    .append(" AND lab22.lab22c3 BETWEEN '").append(filter.getStartDate()).append("' AND '")
                    .append(filter.getEndDate()).append("'");

            if (filter.getBranchId() > 0)
            {
                select.append(" AND lab22.lab05c1 = ").append(filter.getBranchId());
            }

            if (filter.getRuc() != null && !filter.getRuc().isEmpty())
            {
                select.append(" AND lab902c17  IN('").append(filter.getRuc().stream().map(ruc -> ruc.toString()).collect(Collectors.joining("','"))).append("') ");
            }

            if (filter.getPhoneBox() != null && !filter.getPhoneBox().isEmpty())
            {

                select.append(" AND lab902c21  IN('").append(filter.getPhoneBox().stream().map(phonebox -> phonebox.toString()).collect(Collectors.joining("','"))).append("') ");
            }

            if (filter.getHistory() != null && !filter.getHistory().isEmpty())
            {
                select.append(" AND lab21.lab21c2  IN('").append(filter.getHistory().stream().map(history -> Tools.encrypt(history.toString())).collect(Collectors.joining("','"))).append("') ");
            }

            if (filter.getEmailPatient() != null && !filter.getEmailPatient().isEmpty())
            {

                select.append(" AND lab21.lab21c8  IN('").append(filter.getEmailPatient().stream().map(emailpatient -> emailpatient.toString()).collect(Collectors.joining("','"))).append("') ");
            }

            if (filter.getPhone() != null && !filter.getPhone().isEmpty())
            {
                select.append(" AND lab21.lab21c16  IN('").append(filter.getPhone().stream().map(phone -> phone.toString()).collect(Collectors.joining("','"))).append("') ");
            }
            select.append(" GROUP BY lab900.lab22c1, lab21.lab21c1, lab21c2, lab21c3, lab21c4, lab21c5, lab21c6, lab21c16, lab22.lab05c1, lab05c4, lab902.lab902c16, lab902c17, lab902c21 ")
                    .append(" ORDER BY lab900.lab22c1 desc");

            return getJdbcTemplate().query(select.toString(), (ResultSet rs, int i) ->
            {
                DetailedCashReport detail = new DetailedCashReport();

                String name1 = Tools.decrypt(rs.getString("lab21c3") != null ? rs.getString("lab21c3") : "");
                String name2 = Tools.decrypt(rs.getString("lab21c4") != null ? rs.getString("lab21c4") : "");
                String lastName1 = Tools.decrypt(rs.getString("lab21c5") != null ? rs.getString("lab21c5") : "");
                String lastName2 = Tools.decrypt(rs.getString("lab21c6") != null ? rs.getString("lab21c6") : "");

                String fullName = name1;
                fullName += name2.isEmpty() ? "" : " " + name2;
                fullName += " " + lastName1;
                fullName += lastName2.isEmpty() ? "" : " " + lastName2;

                detail.setBranchId(rs.getInt("lab05c1"));
                detail.setBranchName(rs.getString("lab05c4"));
                detail.setOrderId(rs.getString("lab22c1") != null ? rs.getLong("lab22c1") : 0);
                detail.setPatientName(fullName);
                detail.setTotal(rs.getString("total") != null ? rs.getDouble("total") : 0);
                detail.setPhone(rs.getString("lab21c16") != null ? rs.getString("lab21c16") : "");
                detail.setBilled(rs.getString("lab902c16"));
                detail.setRuc(rs.getString("lab902c17"));
                detail.setPhoneBox(rs.getString("lab902c21"));
                return detail;
            });
        } catch (DataAccessException ex)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Obtiene el id de una orden y de un examen por medio de una serie de
     * filtros
     *
     * @param prices
     *
     * @return Lista de detalle para recalcular
     * @throws Exception Error en base de datos.
     */
    default int priceChange(PriceChange prices) throws Exception
    {
        try
        {
            Integer year = Tools.YearOfOrder(String.valueOf(prices.getOrder()));
            Integer currentYear = DateTools.dateToNumberYear(new Date());
            String lab900 = year.equals(currentYear) ? "lab900" : "lab900_" + year;

            final List<Object[]> parameters = new ArrayList<>(0);
            String query = "UPDATE " + lab900 + " SET lab900c2 = ?, lab900c3 = ?, lab900c4 = ? WHERE lab22c1 = ? AND lab39c1 = ?";
            prices.getTests().forEach((test) ->
            {
                parameters.add(new Object[]
                {
                    test.getPatientPrice().doubleValue() + test.getInsurancePrice().doubleValue(),
                    test.getPatientPrice(),
                    test.getInsurancePrice(),
                    prices.getOrder(),
                    test.getId()
                }
                );
            });
            return getJdbcTemplate().batchUpdate(query, parameters).length;
        } catch (DataAccessException ex)
        {
            return 0;
        }
    }

    /**
     * Obtiene el consolidado de convenios de caja por filtros especificos -
     * Ordenes
     *
     * @param filter
     * @param account Indica si el sistema trabaja con cuenta
     * @param yearsQuery Años de consulta (Historicos)
     *
     * @return Reporte de caja consolidado de convenios
     * @throws Exception Error en base de datos.
     */
    default List<DetailedCashReport> consolidatedAccountOrders(CashReportFilter filter, boolean account, int yearsQuery) throws Exception
    {
        try
        {
            List<DetailedCashReport> list = new LinkedList<>();

            int currentYear = DateTools.dateToNumberYear(new Date());
            List<Integer> years = Tools.listOfConsecutiveYears(Integer.toString(currentYear - yearsQuery), Integer.toString(currentYear));
            String lab22;
            String lab900;
            String lab57;

            for (Integer year : years)
            {
                lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
                lab900 = year.equals(currentYear) ? "lab900" : "lab900_" + year;
                lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;

                boolean tableExists = getToolsDao().tableExists(getJdbcTemplate(), lab22);
                tableExists = tableExists ? getToolsDao().tableExists(getJdbcTemplate(), lab900) : tableExists;
                if (tableExists)
                {
                    StringBuilder select = new StringBuilder();
                    select.append(ISOLATION_READ_UNCOMMITTED);

                    select.append("SELECT lab22.lab22c1")
                            .append(", lab21c3")
                            .append(", lab21c4")
                            .append(", lab21c5")
                            .append(", lab21c6")
                            .append(", lab14c3")
                            .append(", SUM(lab900c2) as subtotal ")
                            .append(", lab22.lab05c1 as idBranch")
                            .append(", lab05.lab05c4 as branch")
                            .append(", lab902c6")
                            .append(", lab902c3")
                            .append(", lab902c8")
                            .append(", lab902c16")
                            .append(", lab902c17")
                            .append(", lab902c21")
                            .append(", lab22.lab04c1_1")
                            .append(", lab04c4");

                    StringBuilder from = new StringBuilder();

                    from.append(" FROM lab902 ")
                            .append(" JOIN ").append(lab22).append(" as lab22 ON lab22.lab22c1 = lab902.lab22c1 ")
                            .append(" JOIN lab05 ON lab05.lab05c1 = lab22.lab05c1 ")
                            .append(" JOIN ").append(lab57).append(" as lab57 ON lab57.lab22c1 = lab22.lab22c1 AND lab57c14 IS NULL ")
                            .append(" JOIN ").append(lab900).append(" as lab900 ON lab900.lab22c1 = lab22.lab22c1 AND lab900.lab39c1 = lab57.lab39c1 ")
                            .append(" JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1 ")
                            .append(" JOIN lab04 ON lab22.lab04c1_1 = lab04.lab04c1 ");

                    if (account)
                    {
                        select.append(" , lab14c2, lab14c3 ");
                        from.append(" JOIN lab14 ON lab22.lab14c1 = lab14.lab14c1 ");
                    }

                    from.append(" WHERE lab22.lab07c1 = 1  AND (lab22c19 = 0 or lab22c19 is null) ");

                    if (filter.getUserId() != null && filter.getUserId() > 0)
                    {

                        from.append(" AND lab22.lab04c1_1 = ").append(filter.getUserId());
                    }

                    from.append(" AND (lab22c3 BETWEEN '").append(filter.getStartDate()).append("' AND '")
                            .append(filter.getEndDate()).append("')");

                    if (filter.getBranchId() > 0)
                    {
                        from.append(" AND lab22.lab05c1 = ").append(filter.getBranchId());
                    }

                    if (filter.getRuc() != null && !filter.getRuc().isEmpty())
                    {
                        from.append(" AND lab902c17  IN('").append(filter.getRuc().stream().map(ruc -> ruc.toString()).collect(Collectors.joining("','"))).append("') ");
                    }

                    if (filter.getPhoneBox() != null && !filter.getPhoneBox().isEmpty())
                    {

                        from.append(" AND lab902c21  IN('").append(filter.getPhoneBox().stream().map(phonebox -> phonebox.toString()).collect(Collectors.joining("','"))).append("') ");
                    }

                    if (filter.getHistory() != null && !filter.getHistory().isEmpty())
                    {
                        from.append(" AND lab21.lab21c2  IN('").append(filter.getHistory().stream().map(history -> Tools.encrypt(history.toString())).collect(Collectors.joining("','"))).append("') ");
                    }

                    if (filter.getEmailPatient() != null && !filter.getEmailPatient().isEmpty())
                    {

                        from.append(" AND lab21.lab21c8  IN('").append(filter.getEmailPatient().stream().map(emailpatient -> emailpatient.toString()).collect(Collectors.joining("','"))).append("') ");
                    }

                    if (filter.getPhone() != null && !filter.getPhone().isEmpty())
                    {
                        from.append(" AND lab21.lab21c16  IN('").append(filter.getPhone().stream().map(phone -> phone.toString()).collect(Collectors.joining("','"))).append("') ");
                    }

                    from.append(" GROUP BY lab22.lab22c1, lab21c3, lab21c4, lab21c5, lab21c6, lab14c3, lab902c6, lab902c8, lab902c3, lab22.lab04c1_1, lab04c4 , lab14c2, lab14c3 ");
                    from.append(" ORDER BY lab22.lab22c1 asc ");

                    RowMapper mapper = (RowMapper<DetailedCashReport>) (ResultSet rs, int i) ->
                    {
                        DetailedCashReport detail = new DetailedCashReport();
                        detail.setOrderId(rs.getLong("lab22c1"));

                        String name1 = Tools.decrypt(rs.getString("lab21c3") != null ? rs.getString("lab21c3") : "");
                        String name2 = Tools.decrypt(rs.getString("lab21c4") != null ? rs.getString("lab21c4") : "");
                        String lastName1 = Tools.decrypt(rs.getString("lab21c5") != null ? rs.getString("lab21c5") : "");
                        String lastName2 = Tools.decrypt(rs.getString("lab21c6") != null ? rs.getString("lab21c6") : "");

                        String fullName = name1;
                        fullName += name2.isEmpty() ? "" : " " + name2;
                        fullName += " " + lastName1;
                        fullName += lastName2.isEmpty() ? "" : " " + lastName2;

                        detail.setPatientName(fullName);

                        detail.setCopago(rs.getDouble("lab902c6"));
                        detail.setDiscount(rs.getDouble("lab902c3"));
                        detail.setBranchId(rs.getInt("idBranch"));
                        detail.setBranchName(rs.getString("branch"));
                        detail.setSubTotal(rs.getDouble("subtotal"));
                        detail.setTotalPaid(rs.getDouble("lab902c8"));
                        detail.setBilled(rs.getString("lab902c16"));
                        detail.setRuc(rs.getString("lab902c17"));
                        detail.setPhoneBox(rs.getString("lab902c21"));
                        // Calculamos el total
                        detail.setTotal(detail.getSubTotal() - detail.getDiscount() - detail.getTotalPaid());

                        if (account)
                        {
                            detail.setAccountCode(rs.getString("lab14c2") != null ? rs.getString("lab14c2") : "");
                            detail.setAccountName(rs.getString("lab14c3") != null ? rs.getString("lab14c3") : "");
                        }

                        detail.setUserId(rs.getInt("lab04c1_1"));
                        detail.setUserNames(rs.getString("lab04c4"));
                        detail.setType(1);

                        list.add(detail);

                        return detail;
                    };
                    getJdbcTemplate().query(select.toString() + " " + from.toString(), mapper);
                }
            }
            return list;
        } catch (DataAccessException ex)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Obtiene el consolidado de convenios de caja por filtros especificos -
     * Pagos
     *
     * @param filter
     * @param account Indica si el sistema trabaja con cuenta
     * @param yearsQuery Años de consulta (Historicos)
     *
     * @return Reporte de caja consolidado de convenios
     * @throws Exception Error en base de datos.
     */
    default List<DetailedCashReport> consolidatedAccountPayment(CashReportFilter filter, boolean account, int yearsQuery) throws Exception
    {
        try
        {
            List<DetailedCashReport> list = new LinkedList<>();
            int currentYear = DateTools.dateToNumberYear(new Date());
            List<Integer> years = Tools.listOfConsecutiveYears(Integer.toString(currentYear - yearsQuery), Integer.toString(currentYear));
            String lab22;
            String lab900;
            String lab57;

            for (Integer year : years)
            {
                lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
                lab900 = year.equals(currentYear) ? "lab900" : "lab900_" + year;
                lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;

                boolean tableExists = getToolsDao().tableExists(getJdbcTemplate(), lab22);
                tableExists = tableExists ? getToolsDao().tableExists(getJdbcTemplate(), lab900) : tableExists;
                if (tableExists)
                {
                    StringBuilder select = new StringBuilder();
                    select.append(ISOLATION_READ_UNCOMMITTED);

                    select.append("SELECT lab22.lab22c1")
                            .append(", lab21c3")
                            .append(", lab21c4")
                            .append(", lab21c5")
                            .append(", lab21c6")
                            .append(", lab14c3")
                            .append(", SUM(lab900c4) as subtotal ")
                            .append(", lab902c6")
                            .append(", lab902c3")
                            .append(", lab903.lab04c1_i as idUserPayment")
                            .append(", a.lab04c4 as usernamePayment")
                            .append(", lab903c3");

                    StringBuilder from = new StringBuilder();

                    from.append(" FROM lab903 ")
                            .append(" JOIN ").append(lab22).append(" as lab22 ON lab22.lab22c1 = lab903.lab22c1 ")
                            .append(" JOIN ").append(lab57).append(" as lab57 ON lab57.lab22c1 = lab22.lab22c1 AND lab57c14 IS NULL ")
                            .append(" JOIN ").append(lab900).append(" as lab900 ON lab900.lab22c1 = lab22.lab22c1 AND lab900.lab39c1 = lab57.lab39c1 ")
                            .append(" JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1 ")
                            .append(" JOIN lab902 ON lab22.lab22c1 = lab902.lab22c1 ")
                            .append(" LEFT JOIN lab04 a ON a.lab04c1 = lab903.lab04c1_i ");

                    if (account)
                    {
                        select.append(" , lab14c2, lab14c3 ");
                        from.append(" JOIN lab14 ON lab22.lab14c1 = lab14.lab14c1 ");
                    }

                    from.append(" WHERE lab902.lab04c1_i <> lab903.lab04c1_i AND (lab903c4 BETWEEN '").append(filter.getStartDate()).append("' AND '")
                            .append(filter.getEndDate()).append("')");

                    if (filter.getUserId() != null && filter.getUserId() > 0)
                    {
                        from.append(" AND lab903.lab04c1_i = ").append(filter.getUserId());
                    }

                    if (filter.getBranchId() > 0)
                    {
                        from.append(" AND lab22.lab05c1 = ").append(filter.getBranchId());
                    }

                    if (filter.getRuc() != null && !filter.getRuc().isEmpty())
                    {
                        from.append(" AND lab902c17  IN('").append(filter.getRuc().stream().map(ruc -> ruc.toString()).collect(Collectors.joining("','"))).append("') ");
                    }

                    if (filter.getPhoneBox() != null && !filter.getPhoneBox().isEmpty())
                    {

                        from.append(" AND lab902c21  IN('").append(filter.getPhoneBox().stream().map(phonebox -> phonebox.toString()).collect(Collectors.joining("','"))).append("') ");
                    }

                    if (filter.getHistory() != null && !filter.getHistory().isEmpty())
                    {
                        from.append(" AND lab21c2  IN('").append(filter.getHistory().stream().map(history -> history.toString()).collect(Collectors.joining("','"))).append("') ");
                    }

                    if (filter.getEmailPatient() != null && !filter.getEmailPatient().isEmpty())
                    {

                        from.append(" AND lab21c8  IN('").append(filter.getEmailPatient().stream().map(emailpatient -> emailpatient.toString()).collect(Collectors.joining("','"))).append("') ");
                    }

                    if (filter.getPhone() != null && !filter.getPhone().isEmpty())
                    {
                        from.append(" AND lab21c16  IN('").append(filter.getPhone().stream().map(phone -> phone.toString()).collect(Collectors.joining("','"))).append("') ");
                    }

                    from.append(" AND lab22.lab07c1 = 1  AND (lab22c19 = 0 or lab22c19 is null) ");
                    from.append(" GROUP BY lab22.lab22c1, lab21c3, lab21c4, lab21c5, lab21c6, lab14c3, lab902c6, lab902c8, lab902c3, lab903.lab04c1_i,  a.lab04c4, lab903c3 , lab14c2, lab14c3 ");
                    from.append(" ORDER BY lab22.lab22c1 asc ");

                    RowMapper mapper = (RowMapper<DetailedCashReport>) (ResultSet rs, int i) ->
                    {
                        DetailedCashReport detail = new DetailedCashReport();
                        detail.setOrderId(rs.getLong("lab22c1"));

                        String name1 = Tools.decrypt(rs.getString("lab21c3") != null ? rs.getString("lab21c3") : "");
                        String name2 = Tools.decrypt(rs.getString("lab21c4") != null ? rs.getString("lab21c4") : "");
                        String lastName1 = Tools.decrypt(rs.getString("lab21c5") != null ? rs.getString("lab21c5") : "");
                        String lastName2 = Tools.decrypt(rs.getString("lab21c6") != null ? rs.getString("lab21c6") : "");

                        String fullName = name1;
                        fullName += name2.isEmpty() ? "" : " " + name2;
                        fullName += " " + lastName1;
                        fullName += lastName2.isEmpty() ? "" : " " + lastName2;

                        detail.setPatientName(fullName);

                        detail.setCopago(rs.getDouble("lab902c6"));
                        detail.setDiscount(rs.getDouble("lab902c3"));

                        detail.setSubTotal(rs.getDouble("subtotal"));
                        detail.setTotalPaid(rs.getDouble("lab903c3"));

                        // Calculamos el total
                        if (detail.getSubTotal() > 0)
                        {
                            detail.setTotal(detail.getSubTotal() - detail.getDiscount() - detail.getTotalPaid());
                        } else
                        {
                            detail.setTotal(0.0);
                        }

                        if (account)
                        {
                            detail.setAccountCode(rs.getString("lab14c2") != null ? rs.getString("lab14c2") : "");
                            detail.setAccountName(rs.getString("lab14c3") != null ? rs.getString("lab14c3") : "");
                        }

                        detail.setUserId(rs.getInt("idUserPayment"));
                        detail.setUserNames(rs.getString("usernamePayment"));
                        detail.setType(2);

                        list.add(detail);

                        return detail;
                    };
                    getJdbcTemplate().query(select.toString() + " " + from.toString(), mapper);
                }
            }
            return list;
        } catch (DataAccessException ex)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Lista las ordenes para las ordenes de facturas combos
     *
     * @param init Fecha inicial
     * @param end Fecha final
     * @param idBranch Id de la sede
     * @param demographics lista de demograficos
     * @param demos Lista de demogragicos
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    default List<OrderBilling> getOrdersComboInvoice(Integer init, Integer end, Integer idBranch, final List<FilterDemographic> demographics, final List<Demographic> demos) throws Exception
    {
        try
        {
            List<Integer> years = Tools.listOfConsecutiveYears(Integer.toString(init), Integer.toString(end));
            // Consulta de ordenes por historico:
            String lab22;
            String lab57;
            String lab900;

            int currentYear = DateTools.dateToNumberYear(new Date());

            HashMap<Long, OrderBilling> listOrders = new HashMap<>();

            for (Integer year : years)
            {
                lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
                lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;
                lab900 = year.equals(currentYear) ? "lab900" : "lab900_" + year;

                boolean tableExists = getToolsDao().tableExists(getJdbcTemplate(), lab22);
                tableExists = tableExists ? getToolsDao().tableExists(getJdbcTemplate(), lab57) : tableExists;
                if (tableExists)
                {
                    // Query
                    StringBuilder query = new StringBuilder();
                    query.append(ISOLATION_READ_UNCOMMITTED);

                    query.append("SELECT lab57.lab22c1, lab21c2, lab21c3, lab21c5, lab21c6, lab21.lab80c1, lab21c7, lab22.lab05c1,");
                    query.append("lab05c4, lab22.lab904c1, lab904c3, lab57.lab39c1, lab39c2, lab39c4, lab39c49, lab22c3,");
                    query.append("lab22.lab14c1, lab14c3, lab39.lab07c1 as statusTest, lab39c62 ");
                    query.append(", lab900c2, lab900c3, lab900c4, lab900c5, lab900c6, ");
                    query.append("lab60.lab60c1 as idCommentOrder, lab60.lab60c3 as commentOrder, lab60.lab60c4 as typeCommentOrder, lab60.lab60c6 as isPrintCommentOrder ");

                    StringBuilder from = new StringBuilder();
                    from.append(" FROM ").append(lab57).append(" AS lab57 ");
                    from.append("INNER JOIN ").append(lab22).append(" AS lab22 ON lab22.lab22c1 = lab57.lab22c1 AND lab22.lab930c1 IS NULL ");
                    from.append("INNER JOIN lab05 ON lab05.lab05c1 = lab22.lab05c1  ");
                    from.append("INNER JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1  ");
                    from.append("INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1  ");
                    from.append("LEFT JOIN lab14 ON lab22.lab14c1 = lab14.lab14c1  ");
                    from.append("LEFT JOIN lab60 ON lab60.lab60c2 = lab57.lab22c1 ");
                    from.append("LEFT JOIN lab904 ON lab22.lab904c1 = lab904.lab904c1  ");
                    from.append("LEFT JOIN ").append(lab900).append(" AS lab900 ON lab900.lab22c1 = lab57.lab22c1 AND lab900.lab39c1 = lab57.lab39c1");

                    StringBuilder where = new StringBuilder();
                    List<Object> params = new ArrayList<>();

                    where.append(" WHERE lab22.lab22c2 BETWEEN ? AND ?");

                    params.add(init);
                    params.add(end);

                    if (idBranch != -1)
                    {
                        where.append(" AND lab22.lab05c1 = ?");
                        params.add(idBranch);
                    }

                    where.append(" AND lab22.lab07c1 = 1 AND lab57c14 IS NULL AND lab21.lab21c1 != 0  AND (lab22c19 = 0 or lab22c19 is null) ");

                    where.append(" AND lab57c68 = ").append(Constants.UNBILLED);

                    SQLTools.buildSQLDemographicFilterStatisticsLab(demographics, params, query, from, where);

                    RowMapper mapper = (RowMapper<OrderBilling>) (ResultSet rs, int i) ->
                    {
                        OrderBilling order = new OrderBilling();
                        if (!listOrders.containsKey(rs.getLong("lab22c1")))
                        {

                            order.setOrder(rs.getLong("lab22c1"));
                            order.setCompany(null);

                            Patient patient = new Patient();
                            patient.setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                            patient.setName1(Tools.decrypt(rs.getString("lab21c3")));
                            patient.setLastName(Tools.decrypt(rs.getString("lab21c5")));
                            patient.setSurName(Tools.decrypt(rs.getString("lab21c6")));
                            patient.getSex().setId(rs.getInt("lab80c1"));
                            patient.setBirthday(rs.getTimestamp("lab21c7"));

                            PatientNT patientNT = MigrationMapper.toDtoPatientNT(patient);
                            patientNT.setDemographics(null);
                            patientNT.setComment(null);
                            order.setGeneralPatient(patientNT);

                            order.setBranchId(rs.getInt("lab05c1"));
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

                            listOrders.put(order.getOrder(), order);

                        } else
                        {
                            order = listOrders.get(rs.getLong("lab22c1"));
                        }
                        if (rs.getString("commentOrder") != null && !rs.getString("commentOrder").isEmpty())
                        {
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

                        if (!listOrders.get(order.getOrder()).getTestBilling().contains(test))
                        {
                            listOrders.get(order.getOrder()).getTestBilling().add(test);
                        }

                        return order;
                    };
                    getJdbcTemplate().query(query.toString() + from + where, mapper, params.toArray());
                }
            }
            return new ArrayList<>(listOrders.values());
        } catch (EmptyResultDataAccessException ex)
        {
            ResultsLog.error("Error: " + ex);
            return new ArrayList<>(0);
        }
    }

    /**
     * Crea el registro de la factura
     *
     * @param invoice
     * @return
     * @throws Exception Error en base de datos.
     */
    default ComboInvoice createInvoiceCombo(ComboInvoice invoice) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab930")
                .usingColumns("lab930c2", "lab04c1", "lab930c3", "lab930c4")
                .usingGeneratedKeyColumns("lab930c1");

        HashMap parameters = new HashMap();

        parameters.put("lab930c2", timestamp);
        parameters.put("lab04c1", invoice.getUserId());
        parameters.put("lab930c3", 1);
        parameters.put("lab930c4", invoice.getComment());

        Number key = insert.executeAndReturnKey(parameters);
        invoice.setInvoiceId(key.intValue());

        return invoice;
    }

    /**
     * Actualiza las ordenes que pertenecen a una factura combo
     *
     * @param idInvoice Id de la factura
     * @param init Fecha inicial
     * @param end Fecha final
     * @param idBranch Id de la sede
     * @param demographics lista de demograficos
     * @param demos Lista de demogragicos
     * @return True
     * @throws Exception Error en base de datos.
     */
    default boolean updateOrdersInvoiceCombo(Integer idInvoice, Integer init, Integer end, Integer idBranch, final List<FilterDemographic> demographics, final List<Demographic> demos) throws Exception
    {
        try
        {
            List<Integer> years = Tools.listOfConsecutiveYears(Integer.toString(init), Integer.toString(end));
            // Consulta de ordenes por historico:
            String lab22;
            String lab57;

            int currentYear = DateTools.dateToNumberYear(new Date());

            for (Integer year : years)
            {
                lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
                lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;

                boolean tableExists = getToolsDao().tableExists(getJdbcTemplate(), lab22);
                tableExists = tableExists ? getToolsDao().tableExists(getJdbcTemplate(), lab57) : tableExists;
                if (tableExists)
                {
                    StringBuilder query = new StringBuilder();
                    List<Object> params = new ArrayList<>();
                    query.append(ISOLATION_READ_UNCOMMITTED);
                    query.append("UPDATE ").append(lab22).append(" as lab22 SET lab930c1 = ? ");

                    params.add(idInvoice);

                    StringBuilder from = new StringBuilder();

                    from.append(" FROM ").append(lab57).append(" as lab57 ");

                    StringBuilder where = new StringBuilder();

                    where.append(" WHERE lab57.lab22c1 = lab22.lab22c1 AND lab57c14 IS NULL AND lab57c68 = ").append(Constants.UNBILLED);
                    where.append(" AND lab22.lab930c1 IS NULL AND lab22.lab22c2 BETWEEN ? AND ? ");

                    params.add(init);
                    params.add(end);

                    if (idBranch != -1)
                    {
                        where.append(" AND lab22.lab05c1 = ?");
                        params.add(idBranch);
                    }

                    where.append(" AND lab22.lab07c1 = 1  AND (lab22c19 = 0 or lab22c19 is null) ");

                    SQLTools.buildSQLDemographicUpdateOrders(demographics, params, where);

                    getJdbcTemplate().update(query.toString() + from + where, params.toArray());
                }
            }
            return true;
        } catch (DataAccessException e)
        {
            return false;
        }
    }

    /**
     * Obtiene la factura combo
     *
     * @param idInvoice
     * @return
     * @throws Exception Error en base de datos.
     */
    default ComboInvoice getInvoiceCombo(Integer idInvoice) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab930c1, lab930c2, lab930.lab04c1, lab04c2, lab04c3, lab04c4, lab930c3, lab930c4")
                    .append(" FROM lab930 ")
                    .append("LEFT JOIN lab04 ON lab930.lab04c1 = lab04.lab04c1 ")
                    .append("WHERE lab930c3 = 1 AND lab930.lab930c1 = ").append(idInvoice);

            return getJdbcTemplate().queryForObject(query.toString(), (ResultSet rs, int i)
                    ->
            {
                ComboInvoice invoice = new ComboInvoice();
                invoice.setInvoiceId(rs.getInt("lab930c1"));
                invoice.setDateOfInvoice(rs.getTimestamp("lab930c2"));
                invoice.setUserId(rs.getInt("lab04c1"));
                invoice.setUserNames(rs.getString("lab04c2"));
                invoice.setUserLastNames(rs.getString("lab04c3"));
                invoice.setUsername(rs.getString("lab04c4"));
                invoice.setState(rs.getInt("lab930c3") == 1);
                invoice.setComment(rs.getString("lab930c4"));

                return invoice;
            });
        } catch (DataAccessException e)
        {
            return new ComboInvoice();
        }
    }

    /**
     * Lista las ordenes de una factura combo
     *
     * @param idInvoice
     * @param yearsQuery Años de consulta
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    default List<OrderBilling> getOrdersByComboInvoice(Integer idInvoice, Integer yearsQuery) throws Exception
    {
        try
        {
            int currentYear = DateTools.dateToNumberYear(new Date());

            List<Integer> years = Tools.listOfConsecutiveYears(Integer.toString(currentYear - yearsQuery), Integer.toString(currentYear));
            String lab22;
            String lab57;
            String lab900;

            HashMap<Long, OrderBilling> listOrders = new HashMap<>();

            for (Integer year : years)
            {
                lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
                lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;
                lab900 = year.equals(currentYear) ? "lab900" : "lab900_" + year;

                boolean tableExists = getToolsDao().tableExists(getJdbcTemplate(), lab22);
                tableExists = tableExists ? getToolsDao().tableExists(getJdbcTemplate(), lab57) : tableExists;
                if (tableExists)
                {
                    // Query
                    StringBuilder query = new StringBuilder();
                    query.append(ISOLATION_READ_UNCOMMITTED);

                    query.append("SELECT lab57.lab22c1, lab21c2, lab21c3, lab21c5, lab21c6, lab21.lab80c1, lab21c7, lab22.lab05c1,");
                    query.append("lab05c4, lab22.lab904c1, lab904c3, lab57.lab39c1, lab39c2, lab39c4, lab39c49, lab22c3,");
                    query.append("lab22.lab14c1, lab14c3, lab39.lab07c1 as statusTest, lab39c62 ");
                    query.append(", lab900c2, lab900c3, lab900c4, lab900c5, lab900c6, ");
                    query.append("lab60.lab60c1 as idCommentOrder, lab60.lab60c3 as commentOrder, lab60.lab60c4 as typeCommentOrder, lab60.lab60c6 as isPrintCommentOrder ");

                    StringBuilder from = new StringBuilder();
                    from.append(" FROM ").append(lab57).append(" AS lab57 ");
                    from.append("INNER JOIN ").append(lab22).append(" AS lab22 ON lab22.lab22c1 = lab57.lab22c1 ");
                    from.append("INNER JOIN lab05 ON lab05.lab05c1 = lab22.lab05c1  ");
                    from.append("INNER JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1  ");
                    from.append("INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1  ");
                    from.append("LEFT JOIN lab14 ON lab22.lab14c1 = lab14.lab14c1  ");
                    from.append("LEFT JOIN lab60 ON lab60.lab60c2 = lab57.lab22c1 ");
                    from.append("LEFT JOIN lab904 ON lab22.lab904c1 = lab904.lab904c1  ");
                    from.append("LEFT JOIN ").append(lab900).append(" AS lab900 ON lab900.lab22c1 = lab57.lab22c1 AND lab900.lab39c1 = lab57.lab39c1");

                    StringBuilder where = new StringBuilder();
                    List<Object> params = new ArrayList<>();

                    where.append(" WHERE lab57c14 IS NULL AND lab22.lab930c1 = ?  AND (lab22c19 = 0 or lab22c19 is null) ");

                    params.add(idInvoice);

                    RowMapper mapper = (RowMapper<OrderBilling>) (ResultSet rs, int i) ->
                    {
                        OrderBilling order = new OrderBilling();
                        if (!listOrders.containsKey(rs.getLong("lab22c1")))
                        {

                            order.setOrder(rs.getLong("lab22c1"));
                            order.setCompany(null);

                            Patient patient = new Patient();
                            patient.setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                            patient.setName1(Tools.decrypt(rs.getString("lab21c3")));
                            patient.setLastName(Tools.decrypt(rs.getString("lab21c5")));
                            patient.setSurName(Tools.decrypt(rs.getString("lab21c6")));
                            patient.getSex().setId(rs.getInt("lab80c1"));
                            patient.setBirthday(rs.getTimestamp("lab21c7"));

                            PatientNT patientNT = MigrationMapper.toDtoPatientNT(patient);
                            patientNT.setDemographics(null);
                            patientNT.setComment(null);
                            order.setGeneralPatient(patientNT);

                            order.setBranchId(rs.getInt("lab05c1"));
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

                            listOrders.put(order.getOrder(), order);

                        } else
                        {
                            order = listOrders.get(rs.getLong("lab22c1"));
                        }
                        if (rs.getString("commentOrder") != null && !rs.getString("commentOrder").isEmpty())
                        {
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

                        if (!listOrders.get(order.getOrder()).getTestBilling().contains(test))
                        {
                            listOrders.get(order.getOrder()).getTestBilling().add(test);
                        }

                        return order;
                    };
                    getJdbcTemplate().query(query.toString() + from + where, mapper, params.toArray());
                }
            }
            return new ArrayList<>(listOrders.values());
        } catch (EmptyResultDataAccessException ex)
        {
            ResultsLog.error("Error: " + ex);
            return new ArrayList<>(0);
        }
    }

    /**
     * Actualiza los examenes de las ordenes que pertenecen a una factura combo
     *
     * @param idInvoice Id de la factura
     * @param init
     * @param end
     * @param status
     * @return True
     * @throws Exception Error en base de datos.
     */
    default boolean updateOrdersTestsInvoiceCombo(Integer idInvoice, Integer init, Integer end, Integer status) throws Exception
    {
        try
        {
            // Consulta de ordenes por historico:
            int currentYear = DateTools.dateToNumberYear(new Date());

            List<Integer> years = Tools.listOfConsecutiveYears(Integer.toString(init), Integer.toString(end));
            String lab22;
            String lab57;

            for (Integer year : years)
            {
                lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
                lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;

                boolean tableExists = getToolsDao().tableExists(getJdbcTemplate(), lab22);
                tableExists = tableExists ? getToolsDao().tableExists(getJdbcTemplate(), lab57) : tableExists;
                if (tableExists)
                {
                    StringBuilder query = new StringBuilder();
                    List<Object> params = new ArrayList<>();
                    query.append(ISOLATION_READ_UNCOMMITTED);
                    query.append("UPDATE ").append(lab57).append(" as lab57 SET lab57c68 = ? ");

                    params.add(status);

                    StringBuilder from = new StringBuilder();

                    from.append(" FROM ").append(lab22).append(" as lab22 ");

                    StringBuilder where = new StringBuilder();

                    where.append(" WHERE lab22.lab22c1 = lab57.lab22c1 AND lab57c68 =  ").append(Constants.UNBILLED);

                    where.append(" AND lab22.lab930c1 = ? ");
                    params.add(idInvoice);

                    where.append(" AND lab22.lab22c2 BETWEEN ? AND ? ");

                    params.add(init);
                    params.add(end);

                    getJdbcTemplate().update(query.toString() + from + where, params.toArray());
                }
            }
            return true;
        } catch (DataAccessException e)
        {
            return false;
        }
    }

    /**
     * Actualiza los examenes de las ordenes que pertenecen a una factura combo
     *
     * @param idInvoice Id de la factura
     * @param init
     * @param end
     * @param status
     * @return True
     * @throws Exception Error en base de datos.
     */
    default boolean updateCashboxInvoiceCombo(Integer idInvoice, Integer init, Integer end, Integer status) throws Exception
    {
        try
        {
            // Consulta de ordenes por historico:
            int currentYear = DateTools.dateToNumberYear(new Date());

            List<Integer> years = Tools.listOfConsecutiveYears(Integer.toString(init), Integer.toString(end));
            String lab22;

            for (Integer year : years)
            {
                lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;

                boolean tableExists = getToolsDao().tableExists(getJdbcTemplate(), lab22);
                if (tableExists)
                {
                    StringBuilder query = new StringBuilder();
                    List<Object> params = new ArrayList<>();
                    query.append(ISOLATION_READ_UNCOMMITTED);
                    query.append("UPDATE lab902 SET lab902c19 = ? ");
                    params.add(status);

                    StringBuilder from = new StringBuilder();

                    from.append(" FROM ").append(lab22).append(" as lab22 ");

                    StringBuilder where = new StringBuilder();

                    where.append(" WHERE lab22.lab22c1 = lab902.lab22c1  AND lab22.lab930c1 = ? ");
                    params.add(idInvoice);

                    where.append(" AND lab22.lab22c2 BETWEEN ? AND ? ");
                    params.add(init);
                    params.add(end);

                    where.append(" AND lab902c19 = ").append(Constants.UNBILLED);

                    getJdbcTemplate().update(query.toString() + from + where, params.toArray());
                }
            }
            return true;
        } catch (DataAccessException e)
        {
            return false;
        }
    }

    /**
     * Guarda la nota de credito de una factura combo
     *
     * @param creditNote
     * @return Entero mayor a 0 si se inserto y menor o igual a cero si no
     * @throws Exception
     */
    default long creditNoteCombo(CreditNoteCombo creditNote) throws Exception
    {
        try
        {
            Timestamp timestamp = new Timestamp(new Date().getTime());
            SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                    .withTableName("lab931")
                    .usingGeneratedKeyColumns("lab931c1");

            HashMap parameters = new HashMap();

            parameters.put("lab930c1", creditNote.getIdInvoice());
            parameters.put("lab931c2", timestamp);
            parameters.put("lab04c1", creditNote.getUserId());
            parameters.put("lab931c3", creditNote.getCancellationReason());
            parameters.put("lab913c4", creditNote.getTotalOrders());

            Number key = insert.executeAndReturnKey(parameters);
            return key.longValue();

        } catch (Exception e)
        {
            return -1;
        }
    }

    /**
     * Actualiza el estado de una factura combo a anulada
     *
     * @param idInvoice
     * @throws Exception Error en base de datos.
     */
    default int updateStatusInvoiceCombo(int idInvoice) throws Exception
    {
        try
        {
            return getJdbcTemplate().update("UPDATE lab930 "
                    + "SET lab930c3 = 0 "
                    + "WHERE lab930c1 = ?",
                    idInvoice);
        } catch (DataAccessException e)
        {
            return -1;
        }
    }

    /**
     * Libera las ordenes de una factura
     *
     * @param idInvoice Id de la factura
     * @param yearsQuery Años de consulta
     * @return True
     * @throws Exception Error en base de datos.
     */
    default boolean updateOrdersCreditNoteCombo(Integer idInvoice, Integer yearsQuery) throws Exception
    {
        try
        {
            // Consulta de ordenes por historico:
            int currentYear = DateTools.dateToNumberYear(new Date());
            List<Integer> years = Tools.listOfConsecutiveYears(Integer.toString(currentYear - yearsQuery), Integer.toString(currentYear));
            String lab22;

            for (Integer year : years)
            {
                lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;

                boolean tableExists = getToolsDao().tableExists(getJdbcTemplate(), lab22);
                if (tableExists)
                {
                    StringBuilder query = new StringBuilder();
                    List<Object> params = new ArrayList<>();
                    query.append(ISOLATION_READ_UNCOMMITTED);
                    query.append("UPDATE ").append(lab22).append(" SET lab930c1 = null ");
                    query.append(" WHERE lab930c1 = ?");
                    params.add(idInvoice);

                    getJdbcTemplate().update(query.toString(), params.toArray());
                }
            }
            return true;
        } catch (DataAccessException e)
        {
            return false;
        }
    }

    /**
     * Actualiza los examenes de las ordenes que pertenecen a una factura combo
     *
     * @param idInvoice Id de la factura
     * @param yearsQuery Años de consulta
     * @param status
     * @return True
     * @throws Exception Error en base de datos.
     */
    default boolean updateOrdersTestsCreditNoteCombo(Integer idInvoice, Integer yearsQuery, Integer status) throws Exception
    {
        try
        {
            // Consulta de ordenes por historico:
            int currentYear = DateTools.dateToNumberYear(new Date());
            List<Integer> years = Tools.listOfConsecutiveYears(Integer.toString(currentYear - yearsQuery), Integer.toString(currentYear));

            String lab22;
            String lab57;

            for (Integer year : years)
            {
                lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
                lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;

                boolean tableExists = getToolsDao().tableExists(getJdbcTemplate(), lab22);
                tableExists = tableExists ? getToolsDao().tableExists(getJdbcTemplate(), lab57) : tableExists;
                if (tableExists)
                {
                    StringBuilder query = new StringBuilder();
                    List<Object> params = new ArrayList<>();
                    query.append(ISOLATION_READ_UNCOMMITTED);
                    query.append("UPDATE ").append(lab57).append(" as lab57 SET lab57c68 = ? ");
                    params.add(status);
                    StringBuilder from = new StringBuilder();
                    from.append(" FROM ").append(lab22).append(" as lab22 ");
                    StringBuilder where = new StringBuilder();
                    where.append(" WHERE lab22.lab22c1 = lab57.lab22c1 AND lab57c68 =  ").append(Constants.BILLED);
                    where.append(" AND lab22.lab930c1 = ? ");
                    params.add(idInvoice);

                    getJdbcTemplate().update(query.toString() + from + where, params.toArray());
                }
            }
            return true;
        } catch (DataAccessException e)
        {
            return false;
        }
    }

    /**
     * Libera las cajas de las ordenes de una factura combo
     *
     * @param idInvoice Id de la factura
     * @param yearsQuery Años de consulta
     * @param status
     * @return True
     * @throws Exception Error en base de datos.
     */
    default boolean updateCashboxCreditNoteCombo(Integer idInvoice, Integer yearsQuery, Integer status) throws Exception
    {
        try
        {
            // Consulta de ordenes por historico:
            int currentYear = DateTools.dateToNumberYear(new Date());
            List<Integer> years = Tools.listOfConsecutiveYears(Integer.toString(currentYear - yearsQuery), Integer.toString(currentYear));

            String lab22;

            for (Integer year : years)
            {
                lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;

                boolean tableExists = getToolsDao().tableExists(getJdbcTemplate(), lab22);
                if (tableExists)
                {
                    StringBuilder query = new StringBuilder();
                    List<Object> params = new ArrayList<>();
                    query.append(ISOLATION_READ_UNCOMMITTED);
                    query.append("UPDATE lab902 SET lab902c19 = ? ");
                    params.add(status);
                    StringBuilder from = new StringBuilder();
                    from.append(" FROM ").append(lab22).append(" as lab22 ");
                    StringBuilder where = new StringBuilder();
                    where.append(" WHERE lab22.lab22c1 = lab902.lab22c1  AND lab22.lab930c1 = ? ");
                    params.add(idInvoice);
                    where.append(" AND lab902c19 = ").append(Constants.BILLED);

                    getJdbcTemplate().update(query.toString() + from + where, params.toArray());
                }
            }
            return true;
        } catch (DataAccessException e)
        {
            return false;
        }
    }

    /**
     * Obtiene la nota credito
     *
     * @param idCreditoNote
     * @return
     * @throws Exception Error en base de datos.
     */
    default CreditNoteCombo getCreditNodeCombo(Long idCreditNote) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab931c1, lab930c1, lab931.lab04c1, lab04c2, lab04c3, lab04c4, lab931c2, lab931c3, lab931c4")
                    .append(" FROM lab931 ")
                    .append("LEFT JOIN lab04 ON lab931.lab04c1 = lab04.lab04c1 ")
                    .append("WHERE lab931c1 = ").append(idCreditNote);

            return getJdbcTemplate().queryForObject(query.toString(), (ResultSet rs, int i)
                    ->
            {
                CreditNoteCombo creditNote = new CreditNoteCombo();
                creditNote.setId(rs.getInt("lab931c1"));
                creditNote.setIdInvoice(rs.getInt("lab930c1"));
                creditNote.setDateOfNote(rs.getTimestamp("lab931c2"));
                creditNote.setUserId(rs.getInt("lab04c1"));
                creditNote.setUserNames(rs.getString("lab04c2"));
                creditNote.setUserLastNames(rs.getString("lab04c3"));
                creditNote.setUsername(rs.getString("lab04c4"));
                creditNote.setCancellationReason(rs.getString("lab931c3"));
                creditNote.setTotalOrders(rs.getInt("lab931c4"));

                return creditNote;
            });
        } catch (DataAccessException e)
        {
            return new CreditNoteCombo();
        }
    }
}
