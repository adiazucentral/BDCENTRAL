/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.tools;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import net.cltech.enterprisent.domain.integration.ingreso.ResponsDemographicIngreso;
import net.cltech.enterprisent.domain.integration.resultados.DemoFilter;
import net.cltech.enterprisent.domain.integration.resultados.DemoHeader;
import net.cltech.enterprisent.domain.integration.resultados.ResultHeader;
import net.cltech.enterprisent.domain.operation.billing.PreInvoiceOrder;
import net.cltech.enterprisent.domain.operation.list.FilterDemographic;
import net.cltech.enterprisent.domain.operation.orders.DemographicValue;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.OrderList;
import net.cltech.enterprisent.domain.operation.statistic.StatisticDemographic;
import net.cltech.enterprisent.domain.operation.statistic.StatisticOrder;
import org.apache.commons.lang3.ArrayUtils;
import org.joda.time.DateTime;

/**
 *
 * @author EAcuna
 * @version 1.0.0
 * @since 14/11/2017
 * @see Creación
 */
public class StreamFilters
{

    /**
     * Valida si la orden contiene los demograficos enviados para el modulo de
     * listados TEMP MIENTRAS SE REFORMULAN TODOS LOS METODOS DE LISTADOS
     *
     * @param filter orden
     * @param demographics lista de filtros de demograficos
     *
     * @return
     */
    public static boolean containsDemographicList(OrderList filter, List<FilterDemographic> demographics)
    {
        demographics = demographics.stream()
                .filter(demo -> demo.getDemographicItems() != null && !demo.getDemographicItems().isEmpty() || !demo.getValue().equals(""))
                .collect(Collectors.toList());
        for (FilterDemographic demographic : demographics)
        {
            List<Integer> items = demographic.getDemographicItems().stream()
                    .filter(item -> item != null || !demographic.getValue().equals(""))
                    .collect(Collectors.toList());
            
            if (!demographic.getValue().equals("") && demographic.getDemographic() > 0)
            {
                if (filter.getAllDemographics().contains(new DemographicValue(demographic.getDemographic())))
                {
                    String valor = filter.getAllDemographics().get(filter.getAllDemographics().indexOf(new DemographicValue(demographic.getDemographic()))).getNotCodifiedValue();
                    if (demographic.isPromiseTime())
                    {
                        if (valor == null || valor.equals(""))
                        {
                            return false;
                        } else
                        {
                            try
                            {
                                SimpleDateFormat sdformat = new SimpleDateFormat(demographic.getFormat());
                                Date date1 = sdformat.parse(demographic.getInitDate());
                                Date date2 = sdformat.parse(demographic.getEndDate());
                                Date date3 = sdformat.parse(valor);

                                if (!(date1.before(date3) && date2.after(date3)) && !(date3.equals(date1)) && !(date3.equals(date2)))
                                {
                                    return false;
                                }

                            } catch (ParseException ex)
                            {
                            }
                        }

                    } else if (!(valor != null && demographic.getValue().equals(valor)))
                    {
                        return false;
                    }
                } else
                {
                    return false;
                }
            } else if (items.size() > 0)
            {

                switch (demographic.getDemographic())
                {
                    case Constants.BRANCH:
                        if (!demographic.getDemographicItems().contains(filter.getBranch().getId()))
                        {
                            return false;
                        }
                        break;
                    case Constants.SERVICE:
                        if (!demographic.getDemographicItems().contains(filter.getService().getId()))
                        {
                            return false;
                        }
                        break;
                    case Constants.PHYSICIAN:
                        if (!demographic.getDemographicItems().contains(filter.getPhysician().getId()))
                        {
                            return false;
                        }
                        break;
                    case Constants.ACCOUNT:
                        if (!demographic.getDemographicItems().contains(filter.getAccount().getId()))
                        {
                            return false;
                        }
                        break;
                    case Constants.RATE:
                        if (!demographic.getDemographicItems().contains(filter.getRate().getId()))
                        {
                            return false;
                        }
                        break;
                    case Constants.RACE:
                        if (!demographic.getDemographicItems().contains(filter.getPatient().getRace().getId()))
                        {
                            return false;
                        }
                        break;
                    case Constants.DOCUMENT_TYPE:
                        if (!demographic.getDemographicItems().contains(filter.getPatient().getDocumentType().getId()))
                        {
                            return false;
                        }
                        break;
                    case Constants.ORDERTYPE:
                        if (!demographic.getDemographicItems().contains(filter.getType().getId()))
                        {
                            return false;
                        }
                        break;

                    case Constants.PATIENT_SEX:
                        if (!demographic.getDemographicItems().contains(filter.getPatient().getSex().getId()))
                        {
                            return false;
                        }
                        break;
                    default:
                        if (filter.getAllDemographics().contains(new DemographicValue(demographic.getDemographic())))
                        {
                            Integer valor = filter.getAllDemographics().get(filter.getAllDemographics().indexOf(new DemographicValue(demographic.getDemographic()))).getCodifiedId();
                            if (!(valor != null && demographic.getDemographicItems().contains(valor)))
                            {
                                return false;
                            }
                        } else
                        {
                            return false;
                        }
                        break;
                }
            } else
            {
                switch (demographic.getDemographic())
                {
                    case Constants.PATIENT_ID:
                        if (!demographic.getValue().equals(filter.getPatient().getPatientId()))
                        {
                            return false;
                        }
                        break;
                    case Constants.PATIENT_LAST_NAME:
                        if (!demographic.getValue().equals(filter.getPatient().getLastName()))
                        {
                            return false;
                        }
                        break;
                    case Constants.PATIENT_SURNAME:
                        if (!demographic.getValue().equals(filter.getPatient().getSurName()))
                        {
                            return false;
                        }
                        break;
                    case Constants.PATIENT_NAME:
                        if (!demographic.getValue().equals(filter.getPatient().getName1()))
                        {
                            return false;
                        }
                        break;
                    case Constants.PATIENT_SECOND_NAME:
                        if (!demographic.getValue().equals(filter.getPatient().getName2()))
                        {
                            return false;
                        }
                        break;
                    case Constants.PATIENT_AGE:
                        DateTime datetime = new DateTime();
                        String year = datetime.toString("YYYY");
                        String month = datetime.toString("MM");
                        String day = datetime.toString("dd");
                        String date = "";

                        if (month.length() == 1)
                        {
                            month = "0" + month;
                        }

                        switch (demographic.getUnidAge())//edad 1 -> años, 2 -> meses, 3 -> dias
                        {
                            case "1":
                                date = demographic.getValue() + month + day;
                                break;
                            case "2":
                                date = year + demographic.getValue() + day;
                                break;
                            default:
                                date = year + month + demographic.getValue();
                                break;
                        }

                        DateFormat f = new SimpleDateFormat("yyyyMMdd");
                        String patientYear = f.format(filter.getPatient().getBirthday());

                        switch (demographic.getOperator())
                        {
                            case "2":
                                if (!(Integer.parseInt(date) >= Integer.parseInt(patientYear)))
                                {
                                    return false;
                                }
                                break;
                            case "3":
                                if (!(Integer.parseInt(date) <= Integer.parseInt(patientYear)))
                                {
                                    return false;
                                }
                                break;
                            default:
                                break;
                        }

                        break;
                    default:
                        return true;
                }
            }
        }
        return true;
    }

    /**
     * Valida si la orden contiene los demograficos enviados
     *
     * @param filter orden
     * @param demographics lista de filtros de demograficos
     *
     * @return
     */
    public static boolean containsDemographic(Order filter, List<FilterDemographic> demographics)
    {
        demographics = demographics.stream()
                .filter(demo -> demo.getDemographicItems() != null && !demo.getDemographicItems().isEmpty() || !demo.getValue().equals(""))
                .collect(Collectors.toList());
        for (FilterDemographic demographic : demographics)
        {
            List<Integer> items = demographic.getDemographicItems().stream()
                    .filter(item -> item != null || !demographic.getValue().equals(""))
                    .collect(Collectors.toList());
            if (!demographic.getValue().equals(""))
            {
                if (filter.getAllDemographics().contains(new DemographicValue(demographic.getDemographic())))
                {
                    String valor = filter.getAllDemographics().get(filter.getAllDemographics().indexOf(new DemographicValue(demographic.getDemographic()))).getNotCodifiedValue();
                    if (demographic.isPromiseTime())
                    {
                        if (valor == null || valor.equals(""))
                        {
                            return false;
                        } else
                        {
                            try
                            {
                                SimpleDateFormat sdformat = new SimpleDateFormat(demographic.getFormat());
                                Date date1 = sdformat.parse(demographic.getInitDate());
                                Date date2 = sdformat.parse(demographic.getEndDate());
                                Date date3 = sdformat.parse(valor);

                                if (!(date1.before(date3) && date2.after(date3)) && !(date3.equals(date1)) && !(date3.equals(date2)))
                                {
                                    return false;
                                }

                            } catch (ParseException ex)
                            {
                            }
                        }

                    } else if (!(valor != null && demographic.getValue().equals(valor)))
                    {
                        return false;
                    }
                } else
                {
                    return false;
                }
            } else if (items.size() > 0)
            {
                switch (demographic.getDemographic())
                {
                    case Constants.BRANCH:
                        if (!demographic.getDemographicItems().contains(filter.getBranch().getId()))
                        {
                            return false;
                        }
                        break;
                    case Constants.SERVICE:
                        if (!demographic.getDemographicItems().contains(filter.getService().getId()))
                        {
                            return false;
                        }
                        break;
                    case Constants.PHYSICIAN:
                        if (!demographic.getDemographicItems().contains(filter.getPhysician().getId()))
                        {
                            return false;
                        }
                        break;
                    case Constants.ACCOUNT:
                        if (!demographic.getDemographicItems().contains(filter.getAccount().getId()))
                        {
                            return false;
                        }
                        break;
                    case Constants.RATE:
                        if (!demographic.getDemographicItems().contains(filter.getRate().getId()))
                        {
                            return false;
                        }
                        break;
                    case Constants.RACE:
                        if (!demographic.getDemographicItems().contains(filter.getPatient().getRace().getId()))
                        {
                            return false;
                        }
                        break;
                    case Constants.DOCUMENT_TYPE:
                        if (!demographic.getDemographicItems().contains(filter.getPatient().getDocumentType().getId()))
                        {
                            return false;
                        }
                        break;
                    case Constants.ORDERTYPE:
                        if (!demographic.getDemographicItems().contains(filter.getType().getId()))
                        {
                            return false;
                        }
                        break;
                    case Constants.PATIENT_SEX:
                        if (!demographic.getDemographicItems().contains(filter.getPatient().getSex().getId()))
                        {
                            return false;
                        }
                        break;
                    default:
                        if (filter.getAllDemographics().contains(new DemographicValue(demographic.getDemographic())))
                        {
                            Integer valor = filter.getAllDemographics().get(filter.getAllDemographics().indexOf(new DemographicValue(demographic.getDemographic()))).getCodifiedId();
                            if (!(valor != null && demographic.getDemographicItems().contains(valor)))
                            {
                                return false;
                            }
                        } else
                        {
                            return false;
                        }
                        break;
                }
            } else
            {
                switch (demographic.getDemographic())
                {
                    case Constants.PATIENT_ID:
                        if (!demographic.getValue().equals(filter.getPatient().getPatientId()))
                        {
                            return false;
                        }
                        break;
                    case Constants.PATIENT_LAST_NAME:
                        if (!demographic.getValue().equals(filter.getPatient().getLastName()))
                        {
                            return false;
                        }
                        break;
                    case Constants.PATIENT_SURNAME:
                        if (!demographic.getValue().equals(filter.getPatient().getSurName()))
                        {
                            return false;
                        }
                        break;
                    case Constants.PATIENT_NAME:
                        if (!demographic.getValue().equals(filter.getPatient().getName1()))
                        {
                            return false;
                        }
                        break;
                    case Constants.PATIENT_SECOND_NAME:
                        if (!demographic.getValue().equals(filter.getPatient().getName2()))
                        {
                            return false;
                        }
                        break;
                    case Constants.PATIENT_AGE:
                        DateTime datetime = new DateTime();
                        String year = datetime.toString("YYYY");
                        String month = datetime.toString("MM");
                        String day = datetime.toString("dd");
                        String date = "";

                        if (month.length() == 1)
                        {
                            month = "0" + month;
                        }

                        switch (demographic.getUnidAge())//edad 1 -> años, 2 -> meses, 3 -> dias
                        {
                            case "1":
                                date = demographic.getValue() + month + day;
                                break;
                            case "2":
                                date = year + demographic.getValue() + day;
                                break;
                            default:
                                date = year + month + demographic.getValue();
                                break;
                        }

                        DateFormat f = new SimpleDateFormat("yyyyMMdd");
                        String patientYear = f.format(filter.getPatient().getBirthday());

                        switch (demographic.getOperator())
                        {
                            case "2":
                                if (!(Integer.parseInt(date) >= Integer.parseInt(patientYear)))
                                {
                                    return false;
                                }
                                break;
                            case "3":
                                if (!(Integer.parseInt(date) <= Integer.parseInt(patientYear)))
                                {
                                    return false;
                                }
                                break;
                            default:
                                break;
                        }
                        break;
                    default:
                        return true;
                }
            }
        }
        return true;
    }

    /**
     * Valida si la orden contiene los demograficos enviados
     *
     * @param order orden
     * @param demographics lista de filtros de demograficos
     *
     * @return
     */
    public static boolean containsDemographicStatistic(StatisticOrder order, List<FilterDemographic> demographics)
    {
        demographics = demographics == null ? new ArrayList<>() : demographics;
        demographics = demographics.stream()
                .filter(demo -> demo.getDemographic() != null)
                .filter(demo -> demo.getDemographicItems() != null && !demo.getDemographicItems().isEmpty() || !demo.getValue().equals(""))
                .collect(Collectors.toList());
        for (FilterDemographic demographic : demographics)
        {
            List<Integer> items = demographic.getDemographicItems().stream()
                    .filter(item -> item != null)
                    .collect(Collectors.toList());
            if (items.size() > 0)
            {
                switch (demographic.getDemographic())
                {
                    case Constants.BRANCH:
                        if (!demographic.getDemographicItems().contains(order.getBranch()))
                        {
                            return false;
                        }
                        break;
                    case Constants.SERVICE:
                        if (!demographic.getDemographicItems().contains(order.getService()))
                        {
                            return false;
                        }
                        break;
                    case Constants.PHYSICIAN:
                        if (!demographic.getDemographicItems().contains(order.getPhysician()))
                        {
                            return false;
                        }
                        break;
                    case Constants.ACCOUNT:
                        if (!demographic.getDemographicItems().contains(order.getAccount()))
                        {
                            return false;
                        }
                        break;
                    case Constants.RATE:
                        if (!demographic.getDemographicItems().contains(order.getRate()))
                        {
                            return false;
                        }
                        break;
                    case Constants.RACE:
                        if (!demographic.getDemographicItems().contains(order.getPatient().getRace()))
                        {
                            return false;
                        }
                        break;
                    case Constants.DOCUMENT_TYPE:
                        if (!demographic.getDemographicItems().contains(order.getPatient().getDocumentType()))
                        {
                            return false;
                        }
                        break;
                    case Constants.ORDERTYPE:
                        if (!demographic.getDemographicItems().contains(order.getOrderType()))
                        {
                            return false;
                        }
                        break;
                    case Constants.PATIENT_SEX:
                        if (!demographic.getDemographicItems().contains(order.getPatient().getGender()))
                        {
                            return false;
                        }
                        break;
                    case Constants.AGE_GROUP:
                        if (!demographic.getDemographicItems().contains(order.getAgeGroup()))
                        {
                            return false;
                        }
                        break;
                    default:
                        if (order.getAllDemographics().contains(new StatisticDemographic(demographic.getDemographic())))
                        {
                            Integer itemId = order.getAllDemographics().get(order.getAllDemographics().indexOf(new StatisticDemographic(demographic.getDemographic()))).getCodifiedId();
                            if (!(itemId != null && demographic.getDemographicItems().contains(itemId)))
                            {
                                return false;
                            }
                        } else
                        {
                            return false;
                        }
                        break;
                }
            } else
            {
                switch (demographic.getDemographic())
                {
                    case Constants.PATIENT_ID:
                        if (!demographic.getValue().equals(order.getPatient().getPatientId()))
                        {
                            return false;
                        }
                        break;
                    case Constants.PATIENT_LAST_NAME:
                        if (!demographic.getValue().equals(order.getPatient().getLastName()))
                        {
                            return false;
                        }
                        break;
                    case Constants.PATIENT_SURNAME:
                        if (!demographic.getValue().equals(order.getPatient().getSurName()))
                        {
                            return false;
                        }
                        break;
                    case Constants.PATIENT_NAME:
                        if (!demographic.getValue().equals(order.getPatient().getName1()))
                        {
                            return false;
                        }
                        break;
                    case Constants.PATIENT_SECOND_NAME:
                        if (!demographic.getValue().equals(order.getPatient().getName2()))
                        {
                            return false;
                        }
                        break;
                    default:
                        return true;
                }
            }
        }
        return true;
    }

    public static boolean containsDemographicNoEncoded(Order filter, List<FilterDemographic> demographics)
    {
        demographics = demographics.stream()
                .filter(demo -> demo.getValue() != null && !demo.getValue().trim().isEmpty())
                .collect(Collectors.toList());
        for (FilterDemographic demographic : demographics)
        {
            switch (demographic.getDemographic())
            {
                case Constants.ORDER_HIS:
                    if (!demographic.getValue().equalsIgnoreCase(filter.getExternalId()))
                    {
                        return false;
                    }
                    break;
                case Constants.PATIENT_EMAIL:
                    if (!demographic.getValue().equalsIgnoreCase(filter.getPatient().getEmail()))
                    {
                        return false;
                    }
                    break;
                default:
                    if (filter.getAllDemographics().contains(new DemographicValue(demographic.getDemographic())))
                    {
                        String demographicOrderValue = filter.getAllDemographics().get(filter.getAllDemographics().indexOf(new DemographicValue(demographic.getDemographic()))).getValue();
                        if (!demographic.getValue().equalsIgnoreCase(demographicOrderValue))
                        {
                            return false;
                        }
                    } else
                    {
                        return false;
                    }
                    break;
            }
        }
        return true;
    }

    public static boolean containsDemographicPatient(Order filter, List<FilterDemographic> demographics)
    {
        demographics = demographics.stream()
                .filter(demo -> demo.getValue() != null && !demo.getValue().trim().isEmpty())
                .collect(Collectors.toList());

        for (FilterDemographic demographic : demographics)
        {
            switch (demographic.getDemographic())
            {

                case Constants.PATIENT_ID:
                    if (!demographic.getValue().equalsIgnoreCase(filter.getPatient().getPatientId()))
                    {
                        return false;
                    }
                    break;

                default:
                    if (filter.getPatient().getDemographics().contains(new DemographicValue(demographic.getDemographic())))
                    {
                        String demographicOrderValue = filter.getPatient().getDemographics().get(filter.getPatient().getDemographics().indexOf(new DemographicValue(demographic.getDemographic()))).getValue();
                        if (!demographic.getValue().equalsIgnoreCase(demographicOrderValue))
                        {
                            return false;
                        }
                    } else
                    {
                        return false;
                    }
            }
        }
        return true;
    }

    /**
     * Valida si la orden contiene los demograficos enviados
     *
     * @param order orden
     * @param demographics lista de filtros de demograficos
     *
     * @return
     */
    public static boolean containsDemographicPreInvoice(PreInvoiceOrder order, List<FilterDemographic> demographics)
    {
        demographics = demographics == null ? new ArrayList<>() : demographics;
        demographics = demographics.stream()
                .filter(demo -> demo.getDemographicItems() != null && !demo.getDemographicItems().isEmpty())
                .collect(Collectors.toList());
        for (FilterDemographic demographic : demographics)
        {
            switch (demographic.getDemographic())
            {
                case Constants.BRANCH:
                    if (!demographic.getDemographicItems().contains(order.getBranch()))
                    {
                        return false;
                    }
                    break;
                case Constants.SERVICE:
                    if (!demographic.getDemographicItems().contains(order.getService()))
                    {
                        return false;
                    }
                    break;
                case Constants.PHYSICIAN:
                    if (!demographic.getDemographicItems().contains(order.getPhysician()))
                    {
                        return false;
                    }
                    break;
                case Constants.ACCOUNT:
                    if (!demographic.getDemographicItems().contains(order.getAccount()))
                    {
                        return false;
                    }
                    break;
                case Constants.RATE:
                    if (!demographic.getDemographicItems().contains(order.getRate()))
                    {
                        return false;
                    }
                    break;
                case Constants.RACE:
                    if (!demographic.getDemographicItems().contains(order.getRace()))
                    {
                        return false;
                    }
                    break;
                case Constants.DOCUMENT_TYPE:
                    if (!demographic.getDemographicItems().contains(order.getDocumentTypeId()))
                    {
                        return false;
                    }
                    break;
                case Constants.ORDERTYPE:
                    if (!demographic.getDemographicItems().contains(order.getType()))
                    {
                        return false;
                    }
                    break;
                default:
                    if (order.getAllDemographics().contains(new DemographicValue(demographic.getDemographic())))
                    {
                        Integer itemId = order.getAllDemographics().get(order.getAllDemographics().indexOf(new DemographicValue(demographic.getDemographic()))).getCodifiedId();
                        if (!(itemId != null && demographic.getDemographicItems().contains(itemId)))
                        {
                            return false;
                        }
                    } else
                    {
                        return false;
                    }
                    break;
            }
        }
        return true;
    }
    
    /**
     * Valida si la orden contiene los demograficos enviados para el modulo de
     * listados TEMP MIENTRAS SE REFORMULAN TODOS LOS METODOS DE LISTADOS
     *
     * @param filter orden
     * @param demographics lista de filtros de demograficos
     *
     * @return
     */
    public static boolean containsDemographicHeader(ResultHeader filter, List<DemoFilter> demographics)
    {
        for (DemoFilter demographic : demographics)
        {
            if( demographic.isEncoded() ) {
                if(demographic.getIdFilterValues().equals("")) {
                    switch ( demographic.getId() )
                    {
                        default:
                            if (filter.getDemographics().contains(new DemoHeader(demographic.getId())))
                            {
                                Integer value = filter.getDemographics().get(filter.getDemographics().indexOf(new DemoHeader(demographic.getId()))).getIdItem();
                                if (value == null || value == 0)
                                {
                                    return false;
                                }
                            } else
                            {
                                return false;
                            }
                            break;
                    }
                } else {
                    String[] items = demographic.getIdFilterValues().split(",");
                    if( items.length > 0 ) {
                        switch ( demographic.getId() )
                        {
                            default:
                                if (filter.getDemographics().contains(new DemoHeader(demographic.getId())))
                                {
                                    Integer value = filter.getDemographics().get(filter.getDemographics().indexOf(new DemoHeader(demographic.getId()))).getIdItem();
                                    if (!(value != null && ArrayUtils.contains( items, value.toString() ) ))
                                    {
                                        return false;
                                    }
                                } else
                                {
                                    return false;
                                }
                                break;
                        }
                    }
                }
            } else {
                String valor = filter.getDemographics().get(filter.getDemographics().indexOf(new DemoHeader(demographic.getId()))).getValue();
                if(demographic.getIdFilterValues().isEmpty() && (valor == null || valor.isEmpty())) {
                   return false;
                } else if (!demographic.getIdFilterValues().isEmpty() && !(valor != null && demographic.getIdFilterValues().equals(valor)))
                {
                    return false;
                }
            }
        }
        return true;
    }
}
