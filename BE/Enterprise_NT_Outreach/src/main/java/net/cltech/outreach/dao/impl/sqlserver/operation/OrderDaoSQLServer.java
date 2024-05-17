package net.cltech.outreach.dao.impl.sqlserver.operation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import net.cltech.outreach.dao.interfaces.common.ToolsDao;
import net.cltech.outreach.dao.interfaces.operation.OrderDao;
import net.cltech.outreach.domain.common.AuthorizedUser;
import net.cltech.outreach.domain.demographic.Demographic;
import net.cltech.outreach.domain.demographic.QueryDemographic;
import net.cltech.outreach.domain.operation.Filter;
import net.cltech.outreach.domain.operation.OrderSearch;
import net.cltech.outreach.tools.Constants;
import net.cltech.outreach.tools.Tools;
import net.cltech.outreach.tools.enums.LISEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import net.cltech.outreach.tools.DateTools;
import org.springframework.dao.DataAccessException;


/**
 * Implementa los metodos de acceso a datos para SQLServer
 *
 * @version 1.0.0
 * @author cmartin
 * @since 08/05/2018
 * @see Creacion
 */
@Repository
public class OrderDaoSQLServer implements OrderDao
{
    private JdbcTemplate jdbc;
    
    @Autowired
    public void setDataSource(@Qualifier("dataSource") DataSource dataSource)
    {
        jdbc = new JdbcTemplate(dataSource);
    }

    @Override
    public JdbcTemplate getJdbcTemplate()
    {
        return jdbc;
    }
    
    /**
     * Busca ordenes en el sistema por los filtros enviados
     *
     * @param filter Filtros.
     * @param showResult Mostrar resultados: 1 -> Ingresados, 2 -> Verificados.
     * @param user Usuario que realizo la consulta.
     * @param yearsQuery Años de consulta
     * @param orderAuxPhysician Ordenes del medico auxiliar
     * @param isAuxPhysicians Indica si se utilizan medicos auxiliares
     * @return Lista de Ordenes.
     * @throws Exception Error en base de datos.
     */
    @Override
    public List<OrderSearch> listOrders(Filter filter, int showResult, AuthorizedUser user, int yearsQuery, List<Long> orderAuxPhysician, boolean isAuxPhysicians, Demographic queryDemo, QueryDemographic idItemDemo) throws Exception
    {
        try
        {
            String lab22;
            String lab57;
            
            int currentYear = DateTools.dateToNumberYear(new Date());
            
            List<Integer> years = new ArrayList<>();
            
            if(filter.getDateNumber() != null || filter.getYear() != null || filter.getOrder() != null) {
                
                if(filter.getDateNumber() != null && (filter.getYear() == null || filter.getYear() == 0) && ( filter.getOrder() == null || filter.getOrder() == 0 ) ) {
                    years = Tools.listOfConsecutiveYears(String.valueOf(filter.getDateNumber()), String.valueOf(filter.getDateNumber()));
                }
                
                if(filter.getDateNumberInit() != null && (filter.getYear() == null || filter.getYear() == 0) && ( filter.getOrder() == null || filter.getOrder() == 0 ) ) {
                    years = Tools.listOfConsecutiveYears(String.valueOf(filter.getDateNumberInit()), String.valueOf(filter.getDateNumberEnd()));
                }

                if(filter.getYear() != null && (filter.getDateNumber() == null || filter.getDateNumber() == 0) && ( filter.getOrder() == null || filter.getOrder() == 0 )) {
                    years = Tools.listOfConsecutiveYears(String.valueOf(filter.getYear()), String.valueOf(filter.getYear()));
                }

                if(filter.getOrder() != null && (filter.getDateNumber() == null || filter.getDateNumber() == 0) && (filter.getYear() == null || filter.getYear() == 0) ) {
                    years = Tools.listOfConsecutiveYears(String.valueOf(filter.getOrder()), String.valueOf(filter.getOrder()));
                }

            } else {
                // Consulta de ordenes por historico:
                years = Tools.listOfConsecutiveYears(String.valueOf(currentYear - yearsQuery), String.valueOf(currentYear));
            }
            
            List<OrderSearch> listOrders = new LinkedList<>();
            
            for (Integer year : years) {
                
                lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
                lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;
                
                boolean t = tableExists(jdbc, lab22);
                t = t ? tableExists(jdbc, lab57) : t;
                if (t)
                {
                    String query = ""
                        + "SELECT   lab22.lab22c1 "
                        + "         , lab21.lab21c1 "
                        + "         , lab54.lab54c1 "
                        + "         , lab54.lab54c3 "
                        + "         , lab21.lab21c2 "
                        + "         , lab21.lab21c3 "
                        + "         , lab21.lab21c4 "
                        + "         , lab21.lab21c5 "
                        + "         , lab21.lab21c6 "
                        + "         , lab80.lab80c3 "
                        + "         , lab21.lab21c7 "
                        + "FROM      " + lab22 + " as lab22 "
                        + "         INNER JOIN lab21 ON lab22.lab21c1 = lab21.lab21c1 "
                        + "         LEFT JOIN lab54 ON lab21.lab54c1 = lab54.lab54c1 "
                        + "         INNER JOIN lab80 on lab21.lab80c1 = lab80.lab80c1 ";

                    String where = "WHERE    lab22.lab07c1 = 1 "
                            + (filter.getOrder() != null ? " AND lab22.lab22c1 = ? " : "")
                            + (filter.getDateNumber() != null ? " AND lab22.lab22c2 = ? " : "")
                            + (filter.getDateNumberInit() != null ? " AND lab22.lab22c2 between ? and ?" : "")
                            + (filter.getDocumentType() != null ? " AND lab21.lab54C1 = ? " : "")
                            + (filter.getPatientId() != null ? " AND lab21.lab21c2 = ? " : "")
                            + (filter.getLastName() != null ? " AND lab21.lab21c5 = ? " : "")
                            + (filter.getSurName() != null ? " AND lab21.lab21c6 = ? " : "")
                            + (filter.getName1() != null ? " AND lab21.lab21c3 = ? " : "")
                            + (filter.getName2() != null ? " AND lab21.lab21c4 = ? " : "")
                            + (filter.getYear() != null ? " AND FORMAT(lab22c3, 'yyyy') = '" + filter.getYear() + "' " : "");

                    List parameters = new ArrayList(0);
                    if (filter.getOrder() != null)
                    {
                        parameters.add(filter.getOrder());
                    }
                    if (filter.getDateNumber() != null)
                    {
                        parameters.add(filter.getDateNumber());
                    }
                    if (filter.getDateNumberInit() != null)
                    {
                        parameters.add(filter.getDateNumberInit());
                        parameters.add(filter.getDateNumberEnd());
                    }
                    if (filter.getDocumentType() != null)
                    {
                        parameters.add(filter.getDocumentType());
                    }
                    if (filter.getPatientId() != null)
                    {
                        parameters.add(filter.getPatientId());
                    }
                    if (filter.getLastName() != null)
                    {
                        parameters.add(filter.getLastName());
                    }
                    if (filter.getSurName() != null)
                    {
                        parameters.add(filter.getSurName());
                    }
                    if (filter.getName1() != null)
                    {
                        parameters.add(filter.getName1());
                    }
                    if (filter.getName2() != null)
                    {
                        parameters.add(filter.getName2());
                    }

                    if (showResult == 1)
                    {
                        where += " AND (SELECT COUNT(*) FROM "+ lab57 +" as lab57 WHERE lab57.lab22c1 = lab22.lab22c1 AND lab57c16 >= ?) > 0 ";
                        parameters.add(LISEnum.ResultSampleState.ORDERED.getValue());
                    } else
                    {
                        if (showResult == 2)
                        {
                            where += " AND (SELECT COUNT(*) FROM "+ lab57 +" as lab57 WHERE lab57.lab22c1 = lab22.lab22c1 AND lab57c16 >= ?) > 0 ";
                            parameters.add(LISEnum.ResultSampleState.CHECKED.getValue());
                        }
                    }
                    if(filter.isOnlyValidated()){
                        where += " AND (SELECT COUNT(*) FROM "+ lab57 +" as lab57 WHERE lab57.lab22c1 = lab22.lab22c1 AND lab57c8 >= ?) > 0 ";
                         parameters.add(LISEnum.ResultTestState.VALIDATED.getValue());
                    }

                    if(user.getType() != null){
                        switch (user.getType())
                        {
                            case Constants.PHYSICIAN:
                                if(isAuxPhysicians && orderAuxPhysician.size() > 0) {
                                    where += " AND (lab22.lab19c1 = ? OR lab22.lab22c1 IN (" +  orderAuxPhysician.stream().map(order -> order.toString()).collect(Collectors.joining(",")) + "))";                               
                                } else {
                                    where += " AND lab22.lab19c1 = ? ";
                                } 
                                parameters.add(user.getId());
                                break;
                            case Constants.PATIENT:
                                where += " AND lab22.lab21c1 = ? ";
                                parameters.add(user.getId());
                                break;
                            case Constants.ACCOUNT:
                                where += " AND lab22.lab14c1 = ? ";
                                parameters.add(user.getId());
                                break;
                            case Constants.RATE:
                                where += " AND lab22.lab904c1 = ? ";
                                parameters.add(user.getId());
                                break;    
                            case Constants.DEMOGRAPHIC:
                                if(queryDemo != null && idItemDemo != null) {
                                    if(queryDemo.getType().equals("O")) {
                                        where += " AND lab22.lab_demo_" + queryDemo.getId() + " = ? ";
                                    } else {
                                        where += " AND lab21.lab_demo_" + queryDemo.getId() + " = ? ";
                                    }
                                    parameters.add(idItemDemo.getIdItem());
                                }
                                break;
                            default:
                                break;
                        }
                    }
                    
                    if ( filter.getArea() > 0 )
                    {
                        where += " AND (SELECT COUNT(*) FROM "+ lab57 +" as lab57 inner join lab39 on lab57.lab39c1 = lab39.lab39c1 WHERE lab57.lab22c1 = lab22.lab22c1 AND lab43c1 = ?) > 0 ";
                        parameters.add(filter.getArea());
                    }

                    jdbc.query(query + where, (ResultSet rs, int rowNum) ->
                    {
                        OrderSearch order = new OrderSearch();
                        order.setOrder(rs.getLong("lab22c1"));
                        order.setPatientIdDB(rs.getInt("lab21c1"));
                        order.setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                        order.setDocumentTypeId(rs.getInt("lab54c1"));
                        order.setDocumentType(rs.getString("lab54c3"));
                        order.setLastName(Tools.decrypt(rs.getString("lab21c5")));
                        order.setSurName(Tools.decrypt(rs.getString("lab21c6")));
                        order.setName1(Tools.decrypt(rs.getString("lab21c3")));
                        order.setName2(Tools.decrypt(rs.getString("lab21c4")));
                        order.setSex(rs.getInt("lab80c3"));
                        order.setBirthday(rs.getTimestamp("lab21c7"));
                        listOrders.add(order);
                        return order;
                    }, parameters.toArray());
                }
            }
            return listOrders;
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList(0);
        }
    }
    
    /**
    * Valida si una tabla existe en la base de datos
    *
     * @param connection Conexion a la base de datos
    * @param name Nombre de la tabla 
     * @return  
    * @throws SQLException Error en base de datos
    */
    public boolean tableExists(JdbcTemplate connection, String name) throws SQLException 
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT count(*) as count FROM information_schema.tables WHERE table_name =  '").append(name).append("'");
           
            return connection.queryForObject(query.toString(), (ResultSet rs, int i) ->
            {
                return rs.getInt("count") != 0;
            });
        }
        catch (DataAccessException e)
        {
            return false;
        }
    }
}
