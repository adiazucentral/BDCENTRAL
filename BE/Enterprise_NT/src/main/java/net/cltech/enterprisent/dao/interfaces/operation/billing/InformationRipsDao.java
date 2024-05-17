/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.interfaces.operation.billing;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import net.cltech.enterprisent.domain.masters.common.Item;
import net.cltech.enterprisent.domain.operation.billing.RipsFilter;
import net.cltech.enterprisent.domain.operation.billing.InformationRips;
import net.cltech.enterprisent.domain.operation.billing.TestsRips;
import net.cltech.enterprisent.tools.SQLTools;
import net.cltech.enterprisent.tools.Tools;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import net.cltech.enterprisent.domain.masters.configuration.RIPS;
import net.cltech.enterprisent.domain.operation.billing.DemographicRips;
import net.cltech.enterprisent.service.impl.enterprisent.operation.tracking.SampleTrackingServiceEnterpriseNT;
import net.cltech.enterprisent.tools.Constants;
import net.cltech.enterprisent.tools.DateTools;
import org.springframework.dao.DataAccessException;

/**
 * Representa los métodos de acceso a base de datos para la información rips.
 *
 * @version 1.0.0
 * @author omendez
 * @since 21/01/2021
 * @see Creación
 */
public interface InformationRipsDao 
{
    /**
    * Obtiene la conexion a la base de datos
    *
    * @return jdbc
    */
    public JdbcTemplate getConnection();
    
    /**
     * Obtiene la informacion de un demografico
     *
     * @param demographic Id del demografico
     *
     * @return Informacion de un demografico.
     * @throws Exception Error en la base de datos.
     */
    default DemographicRips demographics(Integer demographic) throws Exception
    {
        try
        {
            String query = ""
                    + "SELECT lab62c1, lab62c3, lab62c2, lab62c4 ";
            String from = " "
                    + "FROM lab62 ";
            String where = " "
                    + "WHERE lab62c1 = ?";

            return getConnection().queryForObject(query + from + where, (ResultSet rs, int i) ->
            {
                DemographicRips demographicRips = new DemographicRips();
                
                demographicRips.setDemographic(rs.getInt("lab62c1"));
                demographicRips.setOrigin(rs.getString("lab62c3"));
                demographicRips.setName(rs.getString("lab62c2"));
                demographicRips.setEncoded(rs.getShort("lab62c4") == 1);

                return demographicRips;
            }, demographic);

        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Obtiene un listado de ordenes que aplican para los rips
     *
     * @param filter Filtros generales de los rips
     * @param rips Lista de configuracion rips
     * @param centralSystem Sistema central
     *
     * @return Ordenes dentro de un rango de fechas
     * @throws Exception Error en base de datos
     */
    default List<InformationRips> list(RipsFilter filter, List<RIPS> rips, RIPS centralSystem)  throws Exception
    {
        try
        {
            List<InformationRips> orders = new LinkedList<>();
            List<Integer> years = Tools.listOfConsecutiveYears(String.valueOf(filter.getInit()), String.valueOf(filter.getEnd()));
            int currentYear = DateTools.dateToNumberYear(new Date());
            String lab22;
            String lab57;
            String lab900;

            for (Integer year : years)
            {
                lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
                lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;
                lab900 = year.equals(currentYear) ? "lab900" : "lab900_" + year;

                StringBuilder query = new StringBuilder();
                query.append("SELECT lab57.lab22c1 ")
                    .append(", lab57.lab39c1 ")
                    .append(", lab39c2 ")
                    .append(", lab57c4 ")
                    .append(", lab900c2 ")
                    .append(", lab901c1p ")
                    .append(", lab901c1i ")
                    .append(", lab900c3 ")
                    .append(", lab900c4 ")
                    .append(", lab901c2 ")
                    .append(", lab21c2 ")
                    .append(", lab21c3 ")
                    .append(", lab21c4 ")
                    .append(", lab21c5 ") 
                    .append(", lab21c6 ")
                    .append(", lab22c3 ")
                    .append(", lab14c13 ")
                    .append(", lab14c3 ")
                    .append(", lab22c8 ")
                    .append(", lab902c6")
                    .append(", lab21.lab80c1")
                    .append(", lab80c3 ")
                    .append(", lab80c4 ")
                    .append(", lab80c5 ")
                    .append(", lab21c7 ")
                    .append(", lab902c8 ");
                
                StringBuilder from = new StringBuilder();

                from.append(" FROM ").append(lab57).append(" as lab57 ");
                from.append(" INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1 AND lab39c20 = 1");
                from.append(" LEFT JOIN ").append(lab900).append(" as lab900 ON lab57.lab39c1 = lab900.lab39c1 AND lab900.lab22c1 = lab57.lab22c1");
                from.append(" LEFT JOIN lab901 ON lab901.lab901c1 = lab900.lab901c1i ");
                from.append(" LEFT JOIN ").append(lab22).append(" as lab22 ON lab22.lab22c1 = lab57.lab22c1 ");
                from.append(" LEFT JOIN lab21 ON lab22.lab21c1 = lab21.lab21c1 ");
                from.append(" LEFT JOIN lab14 ON lab22.lab14c1 = lab14.lab14c1 ");
                from.append(" LEFT JOIN lab902 ON lab22.lab22c1 = lab902.lab22c1 ");
                from.append(" INNER JOIN lab80 ON lab21.lab80C1 = lab80.lab80C1 ");

                rips.stream().forEach (r ->
                {
                    try {
                        if(Integer.parseInt(r.getValue()) > 0) {

                            DemographicRips demographicRips = demographics(Integer.parseInt(r.getValue())); 

                            if(demographicRips.getOrigin() != null) {

                                if (demographicRips.getOrigin().equals("O"))
                                {
                                    if (demographicRips.isEncoded())
                                    {
                                        query.append(", demo").append(demographicRips.getDemographic()).append(".lab63c1 as demo").append(demographicRips.getDemographic()).append("_id");
                                        query.append(", demo").append(demographicRips.getDemographic()).append(".lab63c2 as demo").append(demographicRips.getDemographic()).append("_code");
                                        query.append(", demo").append(demographicRips.getDemographic()).append(".lab63c3 as demo").append(demographicRips.getDemographic()).append("_name");

                                        from.append("  LEFT JOIN Lab63 demo").append(demographicRips.getDemographic()).append(" ON Lab22.lab_demo_").append(demographicRips.getDemographic()).append(" = demo").append(demographicRips.getDemographic()).append(".lab63c1");
                                    } else
                                    {
                                        query.append(", lab22.lab_demo_").append(demographicRips.getDemographic());
                                    }
                                } else {
                                    if (demographicRips.getOrigin().equals("H"))
                                    {
                                        if (demographicRips.isEncoded())
                                        {
                                            query.append(", demo").append(demographicRips.getDemographic()).append(".lab63c1 as demo").append(demographicRips.getDemographic()).append("_id");
                                            query.append(", demo").append(demographicRips.getDemographic()).append(".lab63c2 as demo").append(demographicRips.getDemographic()).append("_code");
                                            query.append(", demo").append(demographicRips.getDemographic()).append(".lab63c3 as demo").append(demographicRips.getDemographic()).append("_name");

                                            from.append(" LEFT JOIN Lab63 demo").append(demographicRips.getDemographic()).append(" ON lab21.lab_demo_").append(demographicRips.getDemographic()).append(" = demo").append(demographicRips.getDemographic()).append(".lab63c1");
                                        } else
                                        {
                                            query.append(", lab21.lab_demo_").append(demographicRips.getDemographic());
                                        }
                                    }
                                } 
                            }
                        } else {
                            switch (Integer.parseInt(r.getValue()))
                            {
                                case Constants.BRANCH:
                                    query.append(", lab05c4 ");
                                    from.append(" LEFT JOIN lab05 ON lab22.lab05c1 = lab05.lab05c1 ");
                                    break;
                                case Constants.SERVICE:
                                    query.append(", lab10c2 ");
                                    from.append(" LEFT JOIN lab10 ON lab22.lab10c1 = lab10.lab10c1 ");
                                    break;
                                case Constants.PHYSICIAN:
                                    query.append(", lab19c2, lab19c3 ");
                                    from.append(" LEFT JOIN lab19 ON lab22.lab19c1 = lab19.lab19c1 ");
                                    break;
                                case Constants.ACCOUNT:
                                    query.append(", lab14c3 ");
                                    from.append(" LEFT JOIN lab14 ON lab22.lab14c1 = lab14.lab14c1 ");
                                    break;
                                case Constants.RATE:
                                    query.append(", lab904c3 ");
                                    from.append(" LEFT JOIN lab904 ON lab22.lab904c1 = lab904.lab904c1 ");
                                    break;
                                case Constants.RACE:
                                    query.append(", lab08c2 ");
                                    from.append(" LEFT JOIN lab08 ON lab08.lab08c1 = lab21.lab08c1 ");
                                    break;
                                case Constants.DOCUMENT_TYPE:
                                    query.append(", lab54c2 ");
                                    from.append(" LEFT JOIN lab54 ON lab54.lab54c1 = lab21.lab54c1 ");
                                    break;
                                case Constants.ORDERTYPE:
                                    query.append(", lab103c3 ");
                                    from.append(" LEFT JOIN lab103 ON lab22.lab103c1 = lab103.lab103c1 ");
                                    break;
                                case Constants.ORDER_HIS:
                                    query.append(", lab22c7 ");
                                    break;
                                case Constants.PATIENT_EMAIL:
                                    query.append(", lab21c8 ");
                                    from.append(" LEFT JOIN lab21 ON lab22.lab21c1 = lab21.lab21c1 ");
                                    break;
                            }
                        }

                    } catch (Exception ex) {
                        Logger.getLogger(SampleTrackingServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });

                StringBuilder where = new StringBuilder();

                switch (filter.getRangeType())
                {
                    case 0:
                        where.append(" WHERE lab22.lab22c2 BETWEEN ? AND ?");
                        break;
                    case 1:

                        where.append(" WHERE lab22.lab22c1 BETWEEN ? AND ?");
                        break;
                    default:
                        return new ArrayList<>();
                }

                where.append(" AND lab22.lab07C1 = 1  AND (lab22c19 = 0 or lab22c19 is null) AND lab57c14 IS NULL AND lab39.lab39c20 = 1");

                if(filter.getBilling() == 1 ) {
                    where.append(" AND lab901c1i IS NOT NULL ");
                }

                if(centralSystem != null && !centralSystem.getValue().isEmpty()) {
                    query.append( " , lab61.lab61c1  " );
                    from.append(" LEFT JOIN lab61 ON lab61.lab39c1 = lab57.lab39c1 AND lab61.lab118c1 = ").append( centralSystem.getValue() );
                }

                //Filtro por examenes confidenciales y areas
                switch (filter.getTestFilterType())
                {
                    case 1:
                        where.append(" AND lab39.lab43c1 IN (").append(filter.getTests().stream().map(area -> area.toString()).collect(Collectors.joining(","))).append(") ");
                        break;
                    case 2:
                        String listtest = filter.getTests().stream().map(test -> test.toString()).collect(Collectors.joining(","));
                        where.append(" AND ((lab57.lab57c14 IN (").append(listtest).append(")) or lab39.lab39c1 IN (").append(listtest).append(")) ");
                        break;
                    case 3:
                        String listtestconfidencial = filter.getTests().stream().map(test -> test.toString()).collect(Collectors.joining(","));
                        where.append(" AND ((lab57.lab57c14 IN (").append(listtestconfidencial).append(")) or lab39.lab39c1 IN (").append(listtestconfidencial).append(")) ");
                        break;
                    default:
                        break;
                }

                List<Object> params = new ArrayList<>();

                params.add(filter.getInit());
                params.add(filter.getEnd());

                SQLTools.buildSQLDemographicFilterStatisticsLab(filter.getDemographics(), params, query, from, where);

                RowMapper mapper = (RowMapper<InformationRips>) (ResultSet rs, int i) ->
                {
                    InformationRips order = new InformationRips();
                    
                    order.setOrderNumber(rs.getLong("lab22c1"));

                    if(orders.contains(order)) {
                        TestsRips test = new TestsRips();
                        test.setCode(rs.getString("lab39c2"));
                        test.setEntryDate(rs.getTimestamp("lab57c4"));
                        test.setServicePrice(rs.getBigDecimal("lab900c2"));
                        test.setPriceAccount(rs.getBigDecimal("lab900c4"));
                        test.setPriceParticular(rs.getBigDecimal("lab900c3"));
                        test.setInvoiceP(rs.getString("lab901c1p"));
                        test.setInvoiceC(rs.getString("lab901c2"));
                        if(centralSystem != null && !centralSystem.getValue().isEmpty()) {
                            test.setCups(rs.getString("lab61c1"));
                        }  
                        orders.get(orders.indexOf(order)).getTests().add(test);
                        
                    } else {
                        
                        order.setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                        order.setName1(Tools.decrypt(rs.getString("lab21c3"))); 
                        order.setName2(Tools.decrypt(rs.getString("lab21c4")));
                        order.setLastName(Tools.decrypt(rs.getString("lab21c5")));
                        order.setSurName(Tools.decrypt(rs.getString("lab21c6")));
                        order.setOrderNumber(rs.getLong("lab22c1"));
                        order.setCreatedDate(rs.getTimestamp("lab22c3"));
                        order.setClientCode(rs.getString("lab14c13"));
                        order.setClientName(rs.getString("lab14c3"));
                        order.setTotalTest(rs.getInt("lab22c8"));
                        order.setCopay(rs.getBigDecimal("lab902c6"));

                        order.setBirthday(rs.getTimestamp("lab21c7"));

                        Item item = new Item();
                        item.setId(rs.getInt("lab80c1"));
                        item.setCode(rs.getString("lab80c3"));
                        item.setEsCo(rs.getString("lab80c4"));
                        item.setEnUsa(rs.getString("lab80c5"));
                        order.setSex(item);

                        if (rs.getString("lab39c1") != null)
                        {   
                            TestsRips test = new TestsRips();
                
                            test.setCode(rs.getString("lab39c2"));
                            test.setEntryDate(rs.getTimestamp("lab57c4"));
                            test.setServicePrice(rs.getBigDecimal("lab900c2"));
                            test.setPriceAccount(rs.getBigDecimal("lab900c4"));
                            test.setPriceParticular(rs.getBigDecimal("lab900c3"));
                            test.setInvoiceP(rs.getString("lab901c1p"));
                            test.setInvoiceC(rs.getString("lab901c2"));
                            if(centralSystem != null && !centralSystem.getValue().isEmpty()) {
                                test.setCups(rs.getString("lab61c1"));
                            }  
                            order.getTests().add(test);
                            orders.add(order);
                        }
                        
                        if(rs.getString("lab39c1") != null) {
                            order.setTotalPaid(rs.getBigDecimal("lab902c8"));
                        } else {
                            order.setTotalPaid(new BigDecimal(0.0));
                        }
                        
                        List<DemographicRips> list = new ArrayList<>();
                        
                        rips.stream().forEach (r ->
                        {
                            try {

                                DemographicRips demographicRips = new DemographicRips();
                                demographicRips.setDemographic(Integer.parseInt(r.getValue()));  
                                demographicRips.setRipsProperty(r.getKey());
                                if(Integer.parseInt(r.getValue()) > 0) {

                                    DemographicRips demographic = demographics(Integer.parseInt(r.getValue()));

                                    if(demographic.getOrigin() != null) {
                                        demographicRips.setOrigin(demographic.getOrigin());
                                        demographicRips.setEncoded(demographic.isEncoded());
                                        if (demographic.isEncoded())
                                        {
                                            if (rs.getString("demo" + demographic.getDemographic() + "_name") != null)
                                            {
                                                demographicRips.setValue(rs.getString("demo" + demographic.getDemographic() + "_name"));
                                                demographicRips.setCodedemographic(rs.getString("demo" + demographic.getDemographic() + "_code"));
                                            }
                                        } else
                                        {
                                            demographicRips.setValue(rs.getString("lab_demo_" + demographic.getDemographic()));
                                        }
                                    }
                                } else {
                                    switch (Integer.parseInt(r.getValue()))
                                    {
                                        case Constants.BRANCH:
                                            demographicRips.setValue(rs.getString("lab05c4"));
                                            break;
                                        case Constants.SERVICE:
                                            demographicRips.setValue(rs.getString("lab10c2"));
                                            break;
                                        case Constants.PHYSICIAN:
                                            demographicRips.setValue(rs.getString("lab19c2") + rs.getString("lab19c3"));
                                            break;
                                        case Constants.ACCOUNT:
                                            demographicRips.setValue(rs.getString("lab14c3"));
                                            break;
                                        case Constants.RATE:
                                            demographicRips.setValue(rs.getString("lab904c3"));
                                            break;
                                        case Constants.RACE:
                                            demographicRips.setValue(rs.getString("lab08c2"));
                                            break;
                                        case Constants.DOCUMENT_TYPE:
                                            demographicRips.setValue(rs.getString("lab54c2"));
                                            break;
                                        case Constants.ORDERTYPE:
                                            demographicRips.setValue(rs.getString("lab103c3"));
                                            break;
                                        case Constants.ORDER_HIS:
                                            demographicRips.setValue(rs.getString("lab22c7"));
                                            break;
                                        case Constants.PATIENT_EMAIL:
                                            demographicRips.setValue(rs.getString("lab21c8"));
                                            break;
                                    }
                                }

                                list.add(demographicRips);
                            }
                            catch (Exception ex) {
                                Logger.getLogger(SampleTrackingServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                        
                        order.setDemographicRips(list);
                        
                    }
                    return order;
                };
                getConnection().query(query.toString() + from + where, mapper, params.toArray());
            }
            return orders;
        } catch (DataAccessException ex)
        {
            return new ArrayList<>();
        }
    }
}
