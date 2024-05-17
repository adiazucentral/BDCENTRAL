/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.interfaces.operation.pathology;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import net.cltech.enterprisent.domain.operation.pathology.CaseSearch;
import net.cltech.enterprisent.domain.operation.pathology.Case;
import net.cltech.enterprisent.domain.operation.pathology.FilterPathology;
import net.cltech.enterprisent.tools.Constants;
import static net.cltech.enterprisent.tools.Constants.ISOLATION_READ_UNCOMMITTED;
import net.cltech.enterprisent.tools.Tools;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información de los casos de patologia.
 *
 * @version 1.0.0
 * @author omendez
 * @since 23/02/2021
 * @see Creación
 */
public interface CaseDao {
    
    public JdbcTemplate getJdbcTemplatePat();
    public JdbcTemplate getJdbcTemplate();
    
    default Case get(Integer id, Integer studyType, Long number, Long order) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT pat01c1, pat01c2, pat01c3, pat01c4, lab22c1, "
                    + " lab05c1, pat01.pat11c1, pat11c2, pat11c3, pat01.lab04c1a, pat01c5, "
                    + " pat01.lab04c1b, pat01c6, lab04c1c  "
                    + " FROM pat01 "
                    + " JOIN pat11 ON pat11.pat11c1 = pat01.pat11c1 ";
            
            /*Where*/
            if (id != null)
            {
                query = query + "WHERE pat01c1 = ? ";
            }
            if (studyType!= null && number != null)
            {
                query = query + "WHERE pat01.pat11c1 = ? AND pat01c2 = ? ";
            }
            if (studyType!= null && order != null)
            {
                query = query + "WHERE pat01.pat11c1 = ? AND lab22c1 = ? ";
            }
            
            /*Order By, Group By y demas complementos de la consulta*/
            query = query + "";

            List object = new ArrayList(0);
            if (id != null)
            {
                object.add(id);
            }
            if (studyType != null && number != null)
            {
                object.add(studyType);
                object.add(number);
            }
            
            if (studyType != null && order != null)
            {
                object.add(studyType);
                object.add(order);
            }
            
            return getJdbcTemplatePat().queryForObject(query, object.toArray(), (ResultSet rs, int i) ->
            {
                Case casePat = new Case();
                
                casePat.setId(rs.getInt("pat01c1"));
                casePat.setNumberCase(rs.getLong("pat01c2"));
                casePat.getStudyType().setId(rs.getInt("pat11c1"));
                casePat.getStudyType().setCode(rs.getString("pat11c2"));
                casePat.getStudyType().setName(rs.getString("pat11c3"));
                casePat.setCreatedDateShort(rs.getInt("pat01c3"));
                casePat.getOrder().setNumberOrder(rs.getLong("lab22c1"));
                casePat.setStatus(rs.getInt("pat01c4"));
                casePat.getBranch().setId(rs.getInt("lab05c1"));
                casePat.getUserCreated().setId(rs.getInt("lab04c1a"));
                casePat.getUserUpdated().setId(rs.getInt("lab04c1b"));
                casePat.getPathologist().setId(rs.getInt("lab04c1c"));
                casePat.setCreatedAt(rs.getTimestamp("pat01c5"));
                if (rs.getTimestamp("pat01c6") != null) {
                    casePat.setUpdatedAt(rs.getTimestamp("pat01c6"));
                }
                return casePat;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }
    
    /**
    * Crea un caso de patologia en el sistema
    *
    * @param casePat {@link net.cltech.enterprisent.domain.operation.pathology.Case}
    *
    * @return {@link net.cltech.enterprisent.domain.operation.pathology.Case} con el numero de caso asignado
    * @throws Exception Error en base de datos
    */
    default Case create(Case casePat) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplatePat())
                .withTableName("pat01")
                .usingGeneratedKeyColumns("pat01c1");
        HashMap<String, Object> parameters = new HashMap<>(0);
        parameters.put("pat01c2", casePat.getNumberCase());
        parameters.put("pat11c1", casePat.getStudyType().getId());
        parameters.put("pat01c3", casePat.getCreatedDateShort());
        parameters.put("lab22c1", casePat.getOrder().getNumberOrder());
        parameters.put("pat01c4", Constants.RECEPTION);
        parameters.put("lab05c1", casePat.getBranch() != null ? casePat.getBranch().getId() : null);
        parameters.put("lab04c1a", casePat.getUserCreated().getId());
        parameters.put("pat01c5", timestamp);
        parameters.put("lab04c1c", casePat.getPathologist() != null ? casePat.getPathologist().getId() : null);

        Number id = insert.executeAndReturnKey(parameters);
        
        if(id.intValue() > 0) {
            casePat.setId(id.intValue());
            return casePat;
        } else {
            return null;
        }       
    }
    
    /**
    * Actualiza un caso en el sistema
    *
    * @param casePat {@link net.cltech.enterprisent.domain.operation.pathology.Case}
    *
    * @return {@link net.cltech.enterprisent.domain.operation.pathology.Case} con el numero de caso
    * @throws Exception Error en base de datos
    */
    default Case update(Case casePat) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        String update = ""
                + "UPDATE pat01 "
                + "SET lab04c1b = ? "
                + ", pat01c6 = ?, lab04c1c = ? ";
        
        List object = new ArrayList(0);
        object.add(casePat.getUserUpdated().getId());
        object.add(timestamp);
        object.add(casePat.getPathologist() != null ? casePat.getPathologist().getId() : null);

        update += " WHERE pat01c1 = ? ";
        object.add(casePat.getId());
        int affectedRows = getJdbcTemplatePat().update(update, object.toArray());
        return affectedRows > 0 ? casePat : null;
    }
    
    /**
    * Obtiene casos buscandolos por fecha de ingreso
    *
    * @param date Fecha en formato Y    YYYMMDD
    * @param branch Id Sede, -1 en caso de no realizar filtro por sede
    * @return Lista de {@link net.cltech.enterprisent.domain.operation.pathology.CaseSearch}, vacio si no existen casos
    * @throws Exception Error en base de datos
    */
    default List<CaseSearch> getByEntryDate(int date, int branch) throws Exception
    {
        try
        {
            String query = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT pat01c1, pat01c2, pat01.pat11c1, pat11c2, pat11c3, lab22c1, lab05c1 "
                    + "FROM pat01 "
                    + "JOIN pat11 ON pat11.pat11c1 = pat01.pat11c1 "
                    + "WHERE pat01c3 = ?"
                    + (branch != -1 ? " AND pat01.lab05c1 = " + branch : "");
            return getJdbcTemplatePat().query(query, (ResultSet rs, int row)
                    ->
            {
                CaseSearch casePat = new CaseSearch();
                
                casePat.setId(rs.getInt("pat01c1"));
                casePat.setNumberCase(rs.getLong("pat01c2"));
                casePat.getStudyType().setId(rs.getInt("pat11c1"));
                casePat.getStudyType().setCode(rs.getString("pat11c2"));
                casePat.getStudyType().setName(rs.getString("pat11c3"));
                casePat.setOrderNumber(rs.getLong("lab22c1"));
                casePat.getBranch().setId(rs.getInt("lab05c1"));
                return casePat;
            }, date);
        }
        catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList(0);
        }
    }
    
    /**
    * Obtiene los datos de un paciente de una orden asociada a un caso de patologia
    *
    * @param casePat Caso de patologia
    * @param order Numero de orden
    * @throws Exception Error en base de datos
    */
    default void getDataPatient(Case casePat, Long order) throws Exception
    {
        String query = "" + ISOLATION_READ_UNCOMMITTED
                + "SELECT   lab22.lab22c1 "
                + " , lab21.lab21c1 "
                + " , lab54.lab54c1 "
                + " , lab54.lab54c2 "
                + " , lab54.lab54c3 "
                + " , lab21.lab21c2 "
                + " , lab21.lab21c3 "
                + " , lab21.lab21c4 "
                + " , lab21.lab21c5 "
                + " , lab21.lab21c6 "
                + " , lab80.lab80c4 "
                + " , lab80.lab80c3 "
                + " , lab21.lab21c7 "
                + "FROM lab22 "
                + " INNER JOIN lab21 ON lab22.lab21c1 = lab21.lab21c1 "
                + " LEFT JOIN lab54 ON lab21.lab54c1 = lab54.lab54c1 "
                + " INNER JOIN lab80 on lab21.lab80c1 = lab80.lab80c1 "
                + "WHERE lab22.lab22c1 = ? ";
        try
        {
            getJdbcTemplate().query(query, new Object[]
            {
                order
            }, (ResultSet rs, int i) ->
            {
                casePat.getOrder().setPatientIdDB(rs.getInt("lab21c1"));
                casePat.getOrder().setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                casePat.getOrder().setDocumentTypeId(rs.getInt("lab54c1"));
                casePat.getOrder().setDocumentTypeCode(rs.getString("lab54c2"));
                casePat.getOrder().setDocumentType(rs.getString("lab54c3"));
                casePat.getOrder().setLastName(Tools.decrypt(rs.getString("lab21c5")));
                casePat.getOrder().setSurName(Tools.decrypt(rs.getString("lab21c6")));
                casePat.getOrder().setName1(Tools.decrypt(rs.getString("lab21c3")));
                casePat.getOrder().setName2(Tools.decrypt(rs.getString("lab21c4")));
                casePat.getOrder().setSexCode(rs.getString("lab80c3"));
                casePat.getOrder().setSex(rs.getString("lab80c4"));
                casePat.getOrder().setBirthday(rs.getTimestamp("lab21c7"));
                return casePat;
            });
        }
        catch (EmptyResultDataAccessException ex)
        {}
    }
    
    /**
    * Obtiene los datos de una orden asociada a un caso de patologia
    *
    * @param casePat Caso de patologia
    * @param order Numero de orden
    * @throws Exception Error en base de datos
    */
    default void getDataOrder(CaseSearch casePat, Long order) throws Exception
    {
        String query = "" + ISOLATION_READ_UNCOMMITTED
                + "SELECT   lab22.lab22c1 "
                + " , lab21.lab21c1 "
                + " , lab54.lab54c1 "
                + " , lab54.lab54c3 "
                + " , lab21.lab21c2 "
                + " , lab21.lab21c3 "
                + " , lab21.lab21c4 "
                + " , lab21.lab21c5 "
                + " , lab21.lab21c6 "
                + " , lab80.lab80c3 "
                + " , lab21.lab21c7 "
                + "FROM lab22 "
                + " INNER JOIN lab21 ON lab22.lab21c1 = lab21.lab21c1 "
                + " LEFT JOIN lab54 ON lab21.lab54c1 = lab54.lab54c1 "
                + " INNER JOIN lab80 on lab21.lab80c1 = lab80.lab80c1 "
                + "WHERE lab22.lab22c1 = ? ";
        try
        {
            getJdbcTemplate().query(query, new Object[]
            {
                order
            }, (ResultSet rs, int i) ->
            {
                casePat.setPatientIdDB(rs.getInt("lab21c1"));
                casePat.setPatientId(Tools.decrypt(rs.getString("lab21c2")));
                casePat.setDocumentTypeId(rs.getInt("lab54c1"));
                casePat.setDocumentType(rs.getString("lab54c3"));
                casePat.setLastName(Tools.decrypt(rs.getString("lab21c5")));
                casePat.setSurName(Tools.decrypt(rs.getString("lab21c6")));
                casePat.setName1(Tools.decrypt(rs.getString("lab21c3")));
                casePat.setName2(Tools.decrypt(rs.getString("lab21c4")));
                casePat.setSex(rs.getInt("lab80c3"));
                casePat.setBirthday(rs.getTimestamp("lab21c7"));
                return casePat;
            });
        }
        catch (EmptyResultDataAccessException ex)
        {}
    }
    
    /**
     * Obtiene una lista de casos filtrados
     *
     * @param filter Filtro de
     * {@link net.cltech.enterprisent.domain.operation.pathology.FilterPathology}
     * casos 
     * @param branch Id sede usuario
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.pathology.CaseSearch}
     * @throws Exception Error en la base de datos.
     */
    default List<CaseSearch> getFilterCases(final FilterPathology filter, Integer branch) throws Exception
    {
        try
        {
            String select = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT pat01c1, pat01c2, pat01.pat11c1, pat01c3, lab22c1, pat01c4, lab05c1, pat01.lab04c1a, pat01c5, pat01.lab04c1b, " 
                    + " pat01c6, pat01.lab04c1c, pat11c2, pat11c3, pat11c4 ";

            String from = ""
                    + " FROM pat01 "
                    + " INNER JOIN pat11 ON pat11.pat11c1 = pat01.pat11c1 "
                    + "";

            List<Object> parametersList = new ArrayList<>();
            StringBuilder where = new StringBuilder("");

            if (filter != null)
            {
                //--------Filtro por areas
                if (filter.getAreas().size() > 0)
                {
                    where.append(" WHERE pat01.pat01c4 in (").append(filter.getAreas().stream().map(area -> area.toString()).collect(Collectors.joining(","))).append(") ");
                }
                
                //--------Filtro por número de orden o fecha de ingreso
                if (filter.getFirstCase() > 0)
                {
                    where.append(" AND pat01.pat01c2 > ? AND pat01.pat01c2 < ? ");
                    parametersList.add(filter.getFirstCase());
                    parametersList.add(filter.getLastCase());
                } else
                {
                    where.append(" AND pat01.pat01c3 > ? AND pat01.pat01c3 < ? ");

                    parametersList.add(filter.getInit());
                    parametersList.add(filter.getEnd());
                }

                //--------Filtro por tipo de estudio
                if (filter.getStudyTypeList().size() > 0)
                {
                    where.append(" AND pat01.pat11c1 in (").append(filter.getStudyTypeList().stream().map(area -> area.toString()).collect(Collectors.joining(","))).append(") ");
                }

                //--------Filtro por la sede del usuario
                where.append(" AND pat01.lab05c1 = ?");
                parametersList.add(branch);
                
                where.append(" ORDER BY pat01.pat01c2 DESC");

            } else
            {
                return new ArrayList<>(0);
            }

            Object[] parametersArr = new Object[parametersList.size()];
            parametersArr = parametersList.toArray(parametersArr);

            return getJdbcTemplatePat().query(select + from + where.toString(),
                    parametersArr, (ResultSet rs, int i) ->
            {
                CaseSearch casePat = new CaseSearch();
                
                casePat.setId(rs.getInt("pat01c1"));
                casePat.setNumberCase(rs.getLong("pat01c2"));
                casePat.setArea(rs.getInt("pat01c4"));
                casePat.getStudyType().setId(rs.getInt("pat11c1"));
                casePat.getStudyType().setCode(rs.getString("pat11c2"));
                casePat.getStudyType().setName(rs.getString("pat11c3"));
                casePat.setOrderNumber(rs.getLong("lab22c1"));
                casePat.getBranch().setId(rs.getInt("lab05c1"));
                return casePat;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            ex.getMessage();
            return new ArrayList<>(0);
        }
    }
    
    
    default void changeStatus(Case casePat) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        String update = ""
                + "UPDATE pat01 "
                + "SET lab04c1b = ?, "
                + " pat01c6 = ?, "
                + " pat01c4 = ? ";
        
        List object = new ArrayList(0);
        object.add(casePat.getUserUpdated().getId());
        object.add(timestamp);
        object.add(casePat.getStatus());

        update += " WHERE pat01c1 = ? ";
        object.add(casePat.getId());
        getJdbcTemplatePat().update(update, object.toArray());
    }
}
