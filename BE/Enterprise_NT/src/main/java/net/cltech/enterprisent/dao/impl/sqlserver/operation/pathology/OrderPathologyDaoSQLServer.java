/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.impl.sqlserver.operation.pathology;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.util.Base64;
import net.cltech.enterprisent.dao.interfaces.operation.pathology.OrderPathologyDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import net.cltech.enterprisent.domain.masters.pathology.Specimen;
import net.cltech.enterprisent.domain.operation.pathology.OrderPathology;
import net.cltech.enterprisent.domain.operation.results.ResultFilter;
import net.cltech.enterprisent.tools.Tools;
import org.springframework.dao.EmptyResultDataAccessException;

/**
 * Realiza la implementación del acceso a datos de las ordenes de patologia
 * para SQLServer
 *
 * @version 1.0.0
 * @author omendez
 * @since 07/10/2020
 * @see Creación
 */
@Repository
public class OrderPathologyDaoSQLServer implements OrderPathologyDao 
{
    private JdbcTemplate jdbc;
    
    @Autowired
    public void setDataSource(@Qualifier("dataSource") DataSource dataSource)
    {
        jdbc = new JdbcTemplate(dataSource);
    }
    
    @Override
    public JdbcTemplate getJdbcTemplate() {
        return jdbc;
    }
    
    @Override
    public List<OrderPathology> list(final ResultFilter resultFilter, boolean orderByOrderId, Integer branch) throws Exception
    {
        try
        {
            String select = "SELECT DISTINCT lab22.lab22c1, lab22.lab21c1, lab22.lab05c1, lab05c2, lab05c4, lab21c2, lab21.lab54c1, lab54c3, lab21c3, lab21c4, lab21c5, lab21c6, lab21.lab80c1, lab80c4, lab21c7, lab21c8, lab21c16, lab21c17, lab22.lab10c1, "
                   + "lab10c2, lab22.lab19c1, lab19c2, lab19c3, lab22c3 "
                   + "FROM lab22 "
                   + "LEFT JOIN lab57 ON lab22.lab22c1 = lab57.lab22c1 "
                   + "LEFT JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1 "
                   + "LEFT JOIN lab24 ON lab39.lab24c1 = lab24.lab24c1 "
                   + "LEFT JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1 "
                   + "LEFT JOIN lab54 ON lab21.lab54c1 = lab54.lab54c1 "
                   + "LEFT JOIN lab80 ON lab21.lab80c1 = lab80.lab80c1 "
                   + "LEFT JOIN lab10 ON lab22.lab10c1 = lab10.lab10c1 "
                   + "LEFT JOIN lab19 ON lab19.lab19c1 = lab22.lab19c1 "
                   + "LEFT JOIN lab05 ON lab05.lab05c1 = lab22.lab05c1 ";
            
            List<Object> parametersList = new ArrayList<>();
            StringBuilder where = new StringBuilder("");
            where.append(" WHERE (CHARINDEX('4', lab24c10 ) > 0) ");
            where.append( " AND lab22.lab05c1 = ? AND (lab22c19 = 0 or lab22c19 is null) ");
            parametersList.add(branch);
                     
            // TODO: Identificar las muestras verificadas
            if (resultFilter != null)
            {
                //--------Filtro por número de orden o fecha de verificación
                if (resultFilter.getFirstOrder() > 0)
                {
                    where.append(" AND lab22.lab22c1 > ? AND lab22.lab22c1 < ? ");
                    parametersList.add(resultFilter.getFirstOrder());
                    parametersList.add(resultFilter.getLastOrder());
                } else
                {
                    where.append(" AND lab57c34 > ? AND lab57c34 < ? ");
                    parametersList.add(resultFilter.getFirstDate());
                    parametersList.add(resultFilter.getLastDate());
                }
 
                //--------Ordenamiento por número de orden (VALOR POR DEFECTO)
                if (orderByOrderId)
                {
                    where.append(" ORDER BY lab22.lab22c1");
                } else
                {
                    where.append(" ORDER BY lab103c2, lab22.lab22c1");
                }

            }
            
            Object[] parametersArr = new Object[parametersList.size()];
            parametersArr = parametersList.toArray(parametersArr);
             
            return jdbc.query(select + where.toString(),
                    parametersArr, (ResultSet rs, int i) ->
            {
                OrderPathology orderPathology = new OrderPathology();
                orderPathology.setNumberOrder(rs.getLong("lab22c1"));
                orderPathology.setPatientIdDB(rs.getInt("lab21c1"));
                orderPathology.setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                orderPathology.setDocumentTypeId(rs.getInt("lab54c1"));
                orderPathology.setDocumentType(rs.getString("lab54c3"));
                orderPathology.setLastName(Tools.decrypt(rs.getString("lab21c5")));
                orderPathology.setSurName(Tools.decrypt(rs.getString("lab21c6")));
                orderPathology.setName1(Tools.decrypt(rs.getString("lab21c3")));
                orderPathology.setName2(Tools.decrypt(rs.getString("lab21c4")));
                orderPathology.setSexId(rs.getInt("lab80c1"));
                orderPathology.setSex(rs.getString("lab80c4"));
                orderPathology.setBirthday(rs.getTimestamp("lab21c7"));
                orderPathology.setEmail(rs.getString("lab21c8"));
                orderPathology.setPhone(rs.getString("lab21c16"));
                orderPathology.setDirection(rs.getString("lab21c17"));
                orderPathology.setIdService(rs.getInt("lab10c1"));
                orderPathology.setService(rs.getString("lab10c2"));
                orderPathology.setIdDoctor(rs.getInt("lab19c1"));
                orderPathology.setNameDoctor(rs.getString("lab19c2"));
                orderPathology.setLastNameDoctor(rs.getString("lab19c3"));
                orderPathology.setCreatedAt(rs.getTimestamp("lab22c3"));
                orderPathology.getBranch().setId(rs.getInt("lab05c1"));
                orderPathology.getBranch().setCode(rs.getString("lab05c2"));
                orderPathology.getBranch().setName(rs.getString("lab05c4"));
            
                return orderPathology;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }
    
    @Override
    public List<Specimen> samplesByOrder(long idOrder) throws Exception
    {
        try
        {
            String select = "SELECT DISTINCT lab39.lab24c1, lab57.lab22c1, lab24.lab07c1, lab24c2, lab24c9, lab24.lab56c1, lab56c2, lab56c3 " + 
                    "FROM lab57 " +
                    "LEFT JOIN lab22 ON lab22.lab22c1 = lab57.lab22c1 " +
                    "LEFT JOIN lab39 ON lab57.lab39c1 = lab39.lab39c1 " +
                    "LEFT JOIN lab24 ON lab39.lab24c1 = lab24.lab24c1 " +
                    "LEFT JOIN lab56 ON lab24.lab56c1 = lab56.lab56c1 ";
            
            List<Object> parametersList = new ArrayList<>();
            StringBuilder where = new StringBuilder("");
            where.append(" WHERE (CHARINDEX('4', lab24c10 ) > 0) ");
            where.append(" AND lab57.lab22c1 = ? AND lab39.lab39c37 != 2 AND (lab22c19 = 0 or lab22c19 is null) ");
              
            parametersList.add(idOrder);
            
            Object[] parametersArr = new Object[parametersList.size()];
            parametersArr = parametersList.toArray(parametersArr);
            
            return jdbc.query(select + where.toString(),
                    parametersArr, (ResultSet rs, int i) ->
            {
                Specimen sample = new Specimen();
                sample.setId(rs.getInt("lab24c1"));
                sample.setState(rs.getBoolean("lab07c1"));
                sample.setName(rs.getString("lab24c2"));
                sample.setCode(rs.getString("lab24c9"));
                
                /*CONTENEDOR*/
                sample.getContainer().setId(rs.getInt("lab56c1"));
                sample.getContainer().setName(rs.getString("lab56c2"));                        
                String Imabas64 = "";
                byte[] ImaBytes = rs.getBytes("lab56c3");
                if (ImaBytes != null)
                {
                    Imabas64 = Base64.getEncoder().encodeToString(ImaBytes);
                }
                sample.getContainer().setImage(Imabas64);

                return sample;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }
}
