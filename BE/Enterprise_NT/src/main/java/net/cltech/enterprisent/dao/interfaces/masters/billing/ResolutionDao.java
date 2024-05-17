/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.interfaces.masters.billing;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import net.cltech.enterprisent.domain.masters.billing.FilterResolution;
import net.cltech.enterprisent.domain.masters.billing.Resolution;
import net.cltech.enterprisent.domain.masters.billing.SingularResolution;
import net.cltech.enterprisent.domain.masters.billing.SingularResolutionTest;
import static net.cltech.enterprisent.tools.Constants.ISOLATION_READ_UNCOMMITTED;
import net.cltech.enterprisent.tools.Tools;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import net.cltech.enterprisent.domain.masters.demographic.Demographic;
import net.cltech.enterprisent.domain.operation.demographic.SuperDocumentType;
import net.cltech.enterprisent.domain.operation.orders.DemographicValue;
import net.cltech.enterprisent.tools.DateTools;
import org.springframework.jdbc.core.RowMapper;

/**
 * Representa los métodos de acceso a base de datos para la información de los
 * Receptores.
 *
 * @version 1.0.0
 * @author cmartin
 * @since 02/05/2018
 * @see Creación
 */
public interface ResolutionDao
{

    /**
     * Lista las resoluciones desde la base de datos.
     *
     * @return Lista de resoluciones.
     * @throws Exception Error en la base de datos.
     */
    default List<Resolution> list() throws Exception
    {
        try
        {
            return getConnection().query(""
                    + "SELECT lab907c1, lab907c2, lab907c3, lab907c4, lab907c5, lab907c6, lab907c7, lab04.lab04c1, lab04c2, lab04c3, lab04c4, lab907.lab07c1, "
                    + "lab906.lab906c1, lab906c2, lab906c3 "
                    + "FROM lab907 "
                    + "INNER JOIN lab906 ON lab906.lab906c1 = lab907.lab906c1 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab907.lab04c1", (ResultSet rs, int i) ->
            {
                Resolution resolution = new Resolution();
                resolution.setId(rs.getInt("lab907c1"));
                resolution.setResolutionDIAN(rs.getString("lab907c2"));
                resolution.setFromNumber(rs.getInt("lab907c3"));
                resolution.setToNumber(rs.getInt("lab907c4"));
                resolution.setPrefix(rs.getString("lab907c5"));
                resolution.setInitialNumber(rs.getInt("lab907c6"));
                //Entidad
                resolution.getProvider().setId(rs.getInt("lab906c1"));
                resolution.getProvider().setNit(rs.getString("lab906c2"));
                resolution.getProvider().setName(rs.getString("lab906c3"));
                /*Usuario*/
                resolution.getUser().setId(rs.getInt("lab04c1"));
                resolution.getUser().setName(rs.getString("lab04c2"));
                resolution.getUser().setLastName(rs.getString("lab04c3"));
                resolution.getUser().setUserName(rs.getString("lab04c4"));

                resolution.setLastTransaction(rs.getTimestamp("lab907c7"));
                resolution.setState(rs.getInt("lab07c1") == 1);

                return resolution;
            });
        }
        catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Registra una nueva resolución en la base de datos.
     *
     * @param resolution Instancia con los datos de la resolución.
     *
     * @return Instancia con los datos de la resolución.
     * @throws Exception Error en la base de datos.
     */
    default Resolution create(Resolution resolution) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getConnection())
                .withTableName("lab907")
                .usingGeneratedKeyColumns("lab907c1");

        HashMap parameters = new HashMap();
        parameters.put("lab907c2", resolution.getResolutionDIAN().trim());
        parameters.put("lab907c3", resolution.getFromNumber());
        parameters.put("lab907c4", resolution.getToNumber());
        parameters.put("lab907c5", resolution.getPrefix().trim());
        parameters.put("lab907c6", resolution.getInitialNumber());
        parameters.put("lab906c1", resolution.getProvider().getId());
        parameters.put("lab907c7", timestamp);
        parameters.put("lab04c1", resolution.getUser().getId());
        parameters.put("lab07c1", 1);

        Number key = insert.executeAndReturnKey(parameters);
        resolution.setId(key.intValue());
        resolution.setLastTransaction(timestamp);

        return resolution;
    }

    /**
     * Obtener información de una resolucion por id.
     *
     * @param id ID del receptor a ser consultada.
     *
     * @return Instancia con los datos de la resolución.
     * @throws Exception Error en la base de datos.
     */
    default Resolution get(Integer id) throws Exception
    {
        try
        {
            return getConnection().queryForObject(""
                    + "SELECT lab907c1, lab907c2, lab907c3, lab907c4, lab907c5, lab907c6, lab907c7, lab04.lab04c1, lab04c2, lab04c3, lab04c4, lab907.lab07c1, "
                    + "lab906.lab906c1, lab906c2, lab906c3 "
                    + "FROM lab907 "
                    + "INNER JOIN lab906 ON lab906.lab906c1 = lab907.lab906c1 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab907.lab04c1 "
                    + "WHERE lab907c1 = ?",
                    new Object[]
                    {
                        id
                    }, (ResultSet rs, int i) ->
            {
                Resolution resolution = new Resolution();
                resolution.setId(rs.getInt("lab907c1"));
                resolution.setResolutionDIAN(rs.getString("lab907c2"));
                resolution.setFromNumber(rs.getInt("lab907c3"));
                resolution.setToNumber(rs.getInt("lab907c4"));
                resolution.setPrefix(rs.getString("lab907c5"));
                resolution.setInitialNumber(rs.getInt("lab907c6"));
                //Entidad
                resolution.getProvider().setId(rs.getInt("lab906c1"));
                resolution.getProvider().setNit(rs.getString("lab906c2"));
                resolution.getProvider().setName(rs.getString("lab906c3"));
                /*Usuario*/
                resolution.getUser().setId(rs.getInt("lab04c1"));
                resolution.getUser().setName(rs.getString("lab04c2"));
                resolution.getUser().setLastName(rs.getString("lab04c3"));
                resolution.getUser().setUserName(rs.getString("lab04c4"));

                resolution.setLastTransaction(rs.getTimestamp("lab907c7"));
                resolution.setState(rs.getInt("lab07c1") == 1);

                return resolution;
            });
        }
        catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Actualiza la información de una resolución en la base de datos.
     *
     * @param resolution Instancia con los datos de la resolución.
     *
     * @return Objeto de la resolución modificada.
     * @throws Exception Error en la base de datos.
     */
    default Resolution update(Resolution resolution) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        getConnection().update("UPDATE lab907 SET lab907c2 = ?, lab907c3 = ?, lab907c4 = ?, lab907c5 = ?, lab907c6 = ?, lab906c1 = ?, lab907c7 = ?, lab04c1 = ?, lab07c1 = ? "
                + "WHERE lab907c1 = ?",
                resolution.getResolutionDIAN(), resolution.getFromNumber(), resolution.getToNumber(), resolution.getPrefix(), resolution.getInitialNumber(), resolution.getProvider().getId(), timestamp, resolution.getUser().getId(), resolution.isState() ? 1 : 0, resolution.getId());

        resolution.setLastTransaction(timestamp);

        return resolution;
    }

    /**
     * Obtenemos el id de una resolucion por el id de la entidad que esta tiene
     * asociado y se verifica el estado de la resolucion sea activo.
     *
     *
     * @param idProvider
     * @return Id de la resolución
     * @throws java.lang.Exception Error de base de datos
     */
    default int getResolutionIdByProvider(int idProvider) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append(ISOLATION_READ_UNCOMMITTED);
            query.append("SELECT lab907c1 ")
                    .append("FROM lab907 ")
                    .append("WHERE lab906c1 = ").append(idProvider)
                    .append(" AND lab07c1 = 1");

            return getConnection().queryForObject(query.toString(), (ResultSet rs, int i) ->
            {
                return rs.getInt("lab907c1");
            });
        }
        catch (Exception e)
        {
            return -1;
        }
    }

    /**
     *
     * Elimina una resolución de la base de datos.
     *
     * @param id ID de la resolución.
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
     * Obtener resolución 4505 dependiendo de los filtros enviados
     *
     * @param filter
     * @param demographics Lista de demograficos
     * @param account 
     * @param physician 
     * @param rate 
     * @param checkCentralSystem 
     * @param idCentralSystem Sistema central de los listados
     * @param documenttype
     *
     * @return Lista de resoluciones
     * @throws Exception Error en la base de datos.
     */
    default List<SingularResolution> getResolution4505(FilterResolution filter, final List<Demographic> demographics, boolean account, boolean physician, boolean rate, boolean checkCentralSystem, int idCentralSystem, boolean documenttype) throws Exception
    {
        try
        {
            List<Integer> years = Tools.listOfConsecutiveYears(String.valueOf(filter.getStartDate()), String.valueOf(filter.getEndDate()));
            String lab22;
            String lab57;
            String lab95;
            String lab900;
            int currentYear = DateTools.dateToNumberYear(new Date());

            HashMap<Long, SingularResolution> listOrders = new HashMap<>();

            for (Integer year : years)
            {
                lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
                lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;
                lab95 = year.equals(currentYear) ? "lab95" : "lab95_" + year;
                lab900 = year.equals(currentYear) ? "lab900" : "lab900_" + year;
                
                StringBuilder query = new StringBuilder();
            
                query.append(" SELECT lab57.lab22c1") 
                    .append(", lab57.lab39c1")
                    .append(", lab57c14")
                    .append(", lab57c16")
                    .append(", lab57c25")          
                    .append(", lab21c2")
                    .append(", lab21c3")
                    .append(", lab21c4")
                    .append(", lab21c5")
                    .append(", lab21c6")
                    .append(", s.lab80c1")
                    .append(", s.lab80c3")
                    .append(", s.lab80c4")
                    .append(", s.lab80c5")
                    .append(", lab21c7")
                    .append(", lab21c17")
                    .append(", lab21c8")
                    .append(", lab21c16")
                    .append(", lab22c3")
                    .append(", lab22.lab07c1")
                    .append(", lab39.lab43c1")
                    .append(", lab43c2")  
                    .append(", lab43c3")
                    .append(", lab43c4")
                    .append(", lab39.lab07c1 as testStatus")
                    .append(", lab39c2")
                    .append(", lab39c4")
                    .append(", lab39c37")
                    .append(", lab57c4")
                    .append(", lab39c4")
                    .append(", lab57c8")
                    .append(", lab57c1")
                    .append(", lab57c18")
                    .append(", lab57c2")
                    .append(", lab57c34")
                    .append(", uv.lab04c2")
                    .append(", uv.lab04c3")
                    .append(", uv.lab04c4")
                    .append(", lab05c4")
                    .append(", lab95c1 ")
                    .append(", lab900c2")
                    .append(", lab900c3")
                    .append(", lab900c4")
                    .append(", lab900c5 ");
                            
                StringBuilder from = new StringBuilder();
                from.append(" FROM ").append(lab57).append(" AS lab57 ");
                from.append("INNER JOIN ").append(lab22).append(" AS lab22 ON lab22.lab22c1 = lab57.lab22c1 ");
                from.append("INNER JOIN lab05 ON lab05.lab05c1 = lab22.lab05c1  ");
                from.append("INNER JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1  ");
                from.append("INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1  ");
                from.append("INNER JOIN lab80 s ON s.lab80c1 = lab21.lab80c1 ");
                from.append("INNER JOIN lab43 ON lab43.lab43c1 = lab39.lab43c1 ");
                from.append("LEFT JOIN lab04 uv ON lab57.lab57c19 = uv.lab04c1 ");
                from.append("LEFT JOIN ").append(lab900).append(" AS lab900 ON lab900.lab22c1 = lab57.lab22c1 AND lab900.lab39c1 = lab57.lab39c1");       
                from.append(" LEFT JOIN ").append(lab95).append(" AS lab95 ON lab95.lab22c1 = lab57.lab22c1 AND lab95.lab39c1 = lab57.lab39c1 ");
                
                if (account)
                {
                    query.append(" ,lab22.lab14c1,  lab14c2, lab14c3 ");
                    from.append("LEFT JOIN lab14 ON lab14.lab14c1 = lab22.lab14c1  ");
                }
                if (physician)
                {
                    query.append(" ,lab22.lab19c1,  lab19c2, lab19c3 ");
                    from.append("LEFT JOIN lab19 ON lab19.lab19c1 = lab22.lab19c1  ");
                }
                if (rate)
                {
                    query.append(" , lab22.lab904c1, lab904c2, lab904c3 ");
                    from.append("LEFT JOIN lab904 ON lab904.lab904c1 = lab22.lab904c1  ");
                }
                
                if (documenttype)
                {
                    query.append(" ,lab21lab54.lab54c1 AS lab21lab54lab54c1,  lab21lab54.lab54c2 AS lab21lab54lab54c2, lab21lab54.lab54c3 AS lab21lab54lab54c3 ");
                    from.append("LEFT JOIN lab54 lab21lab54 ON lab21lab54.lab54c1 = lab21.lab54c1  ");
                }
                
                if (checkCentralSystem)
                {
                    query.append(" , lab61c1 ");
                    from.append("LEFT JOIN lab61 ON lab61.lab39c1 = lab57.lab39c1 and lab61.lab118c1 = ").append(idCentralSystem).append(" ");
                }
                
                for (Demographic demographic : demographics)
                {
                    query.append(Tools.createDemographicsQuery(demographic).get(0));
                    from.append(Tools.createDemographicsQuery(demographic).get(1));
                }
                
                from.append("WHERE lab22.lab22c2 BETWEEN '").append(filter.getStartDate()).append("' AND ")
                    .append("'").append(filter.getEndDate()).append("'");
                
                if (filter.getIdTests() != null && !filter.getIdTests().isEmpty())
                {
                    from.append(" AND lab57.lab39c1 IN (").append(filter.getIdTests().stream().map(idTest -> String.valueOf(idTest)).collect(Collectors.joining(","))).append(")");
                }
                
                from.append(" AND (lab22c19 = 0 or lab22c19 is null)");
                RowMapper mapper = (RowMapper<SingularResolution>) (ResultSet rs, int i) ->
                {                    
                    SingularResolution order = new SingularResolution();
                    if (!listOrders.containsKey(rs.getLong("lab22c1")))
                    {
                        order.setOrderNumber(rs.getLong("lab22c1"));
                        
                        order.setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                        order.setName1(Tools.decrypt(rs.getString("lab21c3")));
                        order.setName2(Tools.decrypt(rs.getString("lab21c4")));
                        order.setLastName(Tools.decrypt(rs.getString("lab21c5")));
                        order.setSurName(Tools.decrypt(rs.getString("lab21c6")));
                        order.setBirthday(rs.getTimestamp("lab21c7"));
                        order.setAddress(rs.getString("lab21c17"));
                        order.setEmail(rs.getString("lab21c8"));
                        order.setPhone(rs.getString("lab21c16"));
                        order.setOrderStatus(rs.getInt("lab07c1"));
                        order.setOrderDate(rs.getTimestamp("lab22c3"));
                        order.setNameBranch(rs.getString("lab05c4"));

                        //PACIENTE - SEXO
                        order.getSex().setId(rs.getInt("lab80c1"));
                        order.getSex().setCode(rs.getString("lab80c3"));
                        order.getSex().setEsCo(rs.getString("lab80c4"));
                        order.getSex().setEnUsa(rs.getString("lab80c5"));

                        if (account)
                        {
                            //EMPRESA
                            order.setCodeClient(rs.getString("lab14c2") == null ? "" : rs.getString("lab14c2"));
                            order.setNameClient(rs.getString("lab14c3") == null ? "" : rs.getString("lab14c3"));
                        }

                        if (physician)
                        {
                            //MEDICO
                            order.setPhysician(rs.getString("lab19c2") == null ? "" : rs.getString("lab19c2") + " " + rs.getString("lab19c3") == null ? "" : rs.getString("lab19c3"));
                        }

                        if (rate)
                        {
                            //TARIFA
                            order.setCodeRate(rs.getString("lab904c2") == null ? "" : rs.getString("lab904c2"));
                            order.setNameRate(rs.getString("lab904c3") == null ? "" : rs.getString("lab904c3"));
                        }
                        
                        if (documenttype)
                        {
                            //PACIENTE - TIPO DE DOCUMENTO
                            SuperDocumentType documentType = new SuperDocumentType();
                            documentType.setId(rs.getInt("lab21lab54lab54c1"));
                            documentType.setAbbr(rs.getString("lab21lab54lab54c2"));
                            documentType.setName(rs.getString("lab21lab54lab54c3"));
                            order.setDocumentType(documentType);
                        }
                        
                        List<DemographicValue> demographicsOrder = new LinkedList<>();
                        
                        for (Demographic demographic : demographics)
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
                        listOrders.put(order.getOrderNumber(), order);
                    
                    }  else
                    {
                        order = listOrders.get(rs.getLong("lab22c1"));
                    }
                    
                    SingularResolutionTest test = new SingularResolutionTest();
                    
                    test.setIdTest(rs.getInt("lab39c1"));
                    test.setIdArea(rs.getInt("lab43c1"));
                    test.setCodeArea(rs.getString("lab43c2"));
                    test.setAbbrArea(rs.getString("lab43c3"));
                    test.setNameArea(rs.getString("lab43c4"));
                    test.setCodeTest(rs.getString("lab39c2"));
                    test.setNameTest(rs.getString("lab39c4"));
                    test.setEntryDate(rs.getTimestamp("lab57c4"));
                    test.setResultState(rs.getInt("lab57c8"));
                    test.setResult(Tools.decrypt(rs.getString("lab57c1")));
                    test.setTestComment(rs.getString("lab95c1"));
                    test.setTestStatus(1);
                    
                    if (checkCentralSystem)
                    {
                        test.setCups(rs.getString("lab61c1"));
                    }
                    
                    test.setValidationDate(rs.getTimestamp("lab57c18"));
                    test.setResultDate(rs.getTimestamp("lab57c2"));
                    test.setVerificationDate(rs.getInt("lab57c34"));
                    test.setUserValidation(rs.getString("lab04c4") == null ? "" : rs.getString("lab04c4"));
                    
                    test.setTestType(rs.getShort("lab39c37"));
                    test.setProfile(rs.getInt("lab57c14"));
                    
                    test.setSampleState(rs.getInt("lab57c16"));
                    test.setPrint(rs.getInt("lab57c25") == 1);
                    
                    test.setServicePrice(rs.getBigDecimal("lab900c2"));
                    test.setPatientPrice(rs.getBigDecimal("lab900c3"));
                    test.setInsurancePrice(rs.getBigDecimal("lab900c4"));
                            
                    if (!listOrders.get(order.getOrderNumber()).getTests().contains(test))
                    {
                        listOrders.get(order.getOrderNumber()).getTests().add(test);
                    }

                    return order;
                };
                getConnection().query(query.toString() + " " + from.toString(), mapper); 
            }
            return new ArrayList<>(listOrders.values());
        }
        catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }


    /**
     * Obtener resolución 4505 dependiendo de los filtros enviados
     *
     * @param filter
     * @param demographics Lista de demograficos
     * @param account 
     * @param physician 
     * @param rate 
     * @param checkCentralSystem 
     * @param idCentralSystem Sistema central de los listados
     * @param documenttype
     *
     * @return Lista de resoluciones
     * @throws Exception Error en la base de datos.
     */
    default List<SingularResolution> getResolution4505RemoveTests(FilterResolution filter, final List<Demographic> demographics, boolean account, boolean physician, boolean rate, boolean checkCentralSystem, int idCentralSystem, boolean documenttype) throws Exception
    {
        try
        {
            List<Integer> years = Tools.listOfConsecutiveYears(String.valueOf(filter.getStartDate()), String.valueOf(filter.getEndDate()));
            String lab22;
            String lab57;
            String lab03;
            
            int currentYear = DateTools.dateToNumberYear(new Date());

            HashMap<Long, SingularResolution> listOrders = new HashMap<>();

            for (Integer year : years)
            {
                
                lab22 = year.equals(currentYear) ? "lab22" : "lab22_" + year;
                lab03 = year.equals(currentYear) ? "lab03" : "lab03_" + year;
                lab57 = year.equals(currentYear) ? "lab57" : "lab57_" + year;
                
                StringBuilder query = new StringBuilder();
            
                query.append(" SELECT DISTINCT lab03.lab22c1") 
                    .append(", lab39.lab39c1")      
                    .append(", lab21c2")
                    .append(", lab21c3")
                    .append(", lab21c4")
                    .append(", lab21c5")
                    .append(", lab21c6")
                    .append(", s.lab80c1")
                    .append(", s.lab80c3")
                    .append(", s.lab80c4")
                    .append(", s.lab80c5")
                    .append(", lab21c7")
                    .append(", lab21c17")
                    .append(", lab21c8")
                    .append(", lab21c16")
                    .append(", lab22c3")
                    .append(", lab22.lab07c1")
                    .append(", lab39.lab43c1")
                    .append(", lab43c2")  
                    .append(", lab43c3")
                    .append(", lab43c4")
                    .append(", lab39c2")
                    .append(", lab39c4")
                    .append(", lab39c37")
                    .append(", lab39c4")
                    .append(", lab05c4");
                            
                StringBuilder from = new StringBuilder();
                from.append(" FROM ").append(lab22).append(" AS lab22 ");
                from.append("INNER JOIN ").append(lab03).append(" AS lab03 ON lab03.lab22c1 = lab22.lab22c1 AND lab03c2 = 'D' AND lab03c3 = 'T' ");
                from.append("INNER JOIN lab39 ON lab39.lab39c1 = lab03.lab03c1  ");
                from.append("INNER JOIN lab05 ON lab05.lab05c1 = lab22.lab05c1  ");
                from.append("INNER JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1  ");
                from.append("INNER JOIN lab80 s ON s.lab80c1 = lab21.lab80c1 ");
                from.append("INNER JOIN lab43 ON lab43.lab43c1 = lab39.lab43c1 ");
                
                if (account)
                {
                    query.append(" ,lab22.lab14c1,  lab14c2, lab14c3 ");
                    from.append("LEFT JOIN lab14 ON lab14.lab14c1 = lab22.lab14c1  ");
                }
                if (physician)
                {
                    query.append(" ,lab22.lab19c1,  lab19c2, lab19c3 ");
                    from.append("LEFT JOIN lab19 ON lab19.lab19c1 = lab22.lab19c1  ");
                }
                if (rate)
                {
                    query.append(" , lab22.lab904c1, lab904c2, lab904c3 ");
                    from.append("LEFT JOIN lab904 ON lab904.lab904c1 = lab22.lab904c1  ");
                }
                
                if (documenttype)
                {
                    query.append(" ,lab21lab54.lab54c1 AS lab21lab54lab54c1,  lab21lab54.lab54c2 AS lab21lab54lab54c2, lab21lab54.lab54c3 AS lab21lab54lab54c3 ");
                    from.append("LEFT JOIN lab54 lab21lab54 ON lab21lab54.lab54c1 = lab21.lab54c1  ");
                }
                
                if (checkCentralSystem)
                {
                    query.append(" , lab61c1 ");
                    from.append("LEFT JOIN lab61 ON lab61.lab39c1 = lab39.lab39c1 and lab61.lab118c1 = ").append(idCentralSystem).append(" ");
                }
                
                for (Demographic demographic : demographics)
                {
                    query.append(Tools.createDemographicsQuery(demographic).get(0));
                    from.append(Tools.createDemographicsQuery(demographic).get(1));
                }
                
                from.append("WHERE lab22.lab22c2 BETWEEN '").append(filter.getStartDate()).append("' AND ")
                    .append("'").append(filter.getEndDate()).append("'");
                
                if (filter.getIdTests() != null && !filter.getIdTests().isEmpty())
                {
                    from.append(" AND lab03.lab03c1 IN (").append(filter.getIdTests().stream().map(idTest -> String.valueOf(idTest)).collect(Collectors.joining(","))).append(")");
                }
                
                from.append("AND NOT EXISTS (SELECT 1 FROM ").append(lab57).append(" as lab57 WHERE lab39c1 = lab03.lab03c1 AND lab57.lab22c1 = lab03.lab22c1 )  AND (lab22c19 = 0 or lab22c19 is null)");
                
                RowMapper mapper = (RowMapper<SingularResolution>) (ResultSet rs, int i) ->
                {                    
                    SingularResolution order = new SingularResolution();
                    if (!listOrders.containsKey(rs.getLong("lab22c1")))
                    {
                        order.setOrderNumber(rs.getLong("lab22c1"));
                        
                        order.setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                        order.setName1(Tools.decrypt(rs.getString("lab21c3")));
                        order.setName2(Tools.decrypt(rs.getString("lab21c4")));
                        order.setLastName(Tools.decrypt(rs.getString("lab21c5")));
                        order.setSurName(Tools.decrypt(rs.getString("lab21c6")));
                        order.setBirthday(rs.getTimestamp("lab21c7"));
                        order.setAddress(rs.getString("lab21c17"));
                        order.setEmail(rs.getString("lab21c8"));
                        order.setPhone(rs.getString("lab21c16"));
                        order.setOrderStatus(rs.getInt("lab07c1"));
                        order.setOrderDate(rs.getTimestamp("lab22c3"));
                        order.setNameBranch(rs.getString("lab05c4"));

                        //PACIENTE - SEXO
                        order.getSex().setId(rs.getInt("lab80c1"));
                        order.getSex().setCode(rs.getString("lab80c3"));
                        order.getSex().setEsCo(rs.getString("lab80c4"));
                        order.getSex().setEnUsa(rs.getString("lab80c5"));

                        if (account)
                        {
                            //EMPRESA
                            order.setCodeClient(rs.getString("lab14c2") == null ? "" : rs.getString("lab14c2"));
                            order.setNameClient(rs.getString("lab14c3") == null ? "" : rs.getString("lab14c3"));
                        }

                        if (physician)
                        {
                            //MEDICO
                            order.setPhysician(rs.getString("lab19c2") == null ? "" : rs.getString("lab19c2") + " " + rs.getString("lab19c3") == null ? "" : rs.getString("lab19c3"));
                        }

                        if (rate)
                        {
                            //TARIFA
                            order.setCodeRate(rs.getString("lab904c2") == null ? "" : rs.getString("lab904c2"));
                            order.setNameRate(rs.getString("lab904c3") == null ? "" : rs.getString("lab904c3"));
                        }
                        
                        if (documenttype)
                        {
                            //PACIENTE - TIPO DE DOCUMENTO
                            SuperDocumentType documentType = new SuperDocumentType();
                            documentType.setId(rs.getInt("lab21lab54lab54c1"));
                            documentType.setAbbr(rs.getString("lab21lab54lab54c2"));
                            documentType.setName(rs.getString("lab21lab54lab54c3"));
                            order.setDocumentType(documentType);
                        }
                        
                        List<DemographicValue> demographicsOrder = new LinkedList<>();
                        
                        for (Demographic demographic : demographics)
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
                        listOrders.put(order.getOrderNumber(), order);
                    
                    }  else
                    {
                        order = listOrders.get(rs.getLong("lab22c1"));
                    }
                    
                    SingularResolutionTest test = new SingularResolutionTest();
                    
                    test.setIdTest(rs.getInt("lab39c1"));
                    test.setIdArea(rs.getInt("lab43c1"));
                    test.setCodeArea(rs.getString("lab43c2"));
                    test.setAbbrArea(rs.getString("lab43c3"));
                    test.setNameArea(rs.getString("lab43c4"));
                    test.setCodeTest(rs.getString("lab39c2"));
                    test.setNameTest(rs.getString("lab39c4"));
                    test.setTestStatus(2);
                    
                    if (checkCentralSystem)
                    {
                        test.setCups(rs.getString("lab61c1"));
                    }
                    
                    test.setTestType(rs.getShort("lab39c37"));
                         
                    if (!listOrders.get(order.getOrderNumber()).getTests().contains(test))
                    {
                        listOrders.get(order.getOrderNumber()).getTests().add(test);
                    }

                    return order;
                };
                getConnection().query(query.toString() + " " + from.toString(), mapper); 
            }
            return new ArrayList<>(listOrders.values());
        }
        catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }
}
