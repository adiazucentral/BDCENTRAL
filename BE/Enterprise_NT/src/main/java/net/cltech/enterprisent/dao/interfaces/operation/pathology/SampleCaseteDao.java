/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.interfaces.operation.pathology;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.operation.pathology.CaseteTracking;
import net.cltech.enterprisent.domain.operation.pathology.FilterPathology;
import net.cltech.enterprisent.domain.operation.pathology.SampleCasete;
import net.cltech.enterprisent.domain.operation.pathology.WidgetPathology;
import net.cltech.enterprisent.tools.Constants;
import static net.cltech.enterprisent.tools.Constants.ISOLATION_READ_UNCOMMITTED;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información de los casetes de las muestras de un caso
 *
 * @version 1.0.0
 * @author omendez
 * @since 05/05/2021
 * @see Creación
 */
public interface SampleCaseteDao 
{
 
    public JdbcTemplate getJdbcTemplatePat();
    
    /**
     * Obtiene el dao del tracking
     *
     * @return Instancia de CaseteTrackingDao
     */
    public CaseteTrackingDao getCaseteTrackingDao();
    
    
    /**
     * Obtiene el usuario del token
     *
     * @return Usuario logueado
     * @throws java.lang.Exception
     */
    public AuthorizedUser getUser() throws Exception;
    
    /**
    * Obtiene los casetes de las muestras de un caso de patologia por id
    *
    * @param idSample Id del contenido de la muestra.
    *
    * @throws Exception Error en base de datos
    */
    default List<SampleCasete> getById(int idSample) throws Exception
    {
        try
        {
            String select = "SELECT pat18c1, pat14c1, pat18c2, pat18c3, pat18.pat03c1, pat03c2, pat03c3, pat03c4, pat03c5 " + 
                    " FROM pat18 " +
                    " LEFT JOIN pat03 ON pat18.pat03c1 = pat03.pat03c1 ";
            
            List<Object> parametersList = new ArrayList<>();
            StringBuilder where = new StringBuilder("");
            where.append(" WHERE pat14c1 = ?");
              
            parametersList.add(idSample);

            Object[] parametersArr = new Object[parametersList.size()];
            parametersArr = parametersList.toArray(parametersArr);
            
            return getJdbcTemplatePat().query(select + where.toString(),
                    parametersArr, (ResultSet rs, int i) ->
            {
                
                SampleCasete sample = new SampleCasete();
                sample.setId(rs.getInt("pat18c1"));
                sample.setSample(rs.getInt("pat14c1"));
                sample.setQuantity(rs.getInt("pat18c2"));
                sample.setConsecutive(rs.getString("pat18c3"));
                sample.getCasete().setId(rs.getInt("pat03c1"));
                sample.getCasete().setCode(rs.getString("pat03c2"));
                sample.getCasete().setName(rs.getString("pat03c3"));
                sample.getCasete().setStatus(rs.getInt("pat03c4"));
                sample.getCasete().setColour(rs.getString("pat03c5"));
                
                return sample;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }
    
    /**
    * Obtiene los casetes de las muestras de un caso de patologia
    *
    * @param idSample Id de la muestra.
    * @param idCase Id del caso.
    *
    * @throws Exception Error en base de datos
    */
    default List<SampleCasete> getBySample(int idSample, int idCase) throws Exception
    {
        try
        {
            String select = "SELECT pat18c1, pat14.pat14c1, pat18c2, pat18c3, pat03c1 " + 
                    "FROM pat18 " +
                    "LEFT JOIN pat14 ON pat14.pat14c1 = pat18.pat14c1 " +
                    "LEFT JOIN pat13 ON pat13.pat13c1 = pat14.pat13c1 ";
            
            List<Object> parametersList = new ArrayList<>();
            StringBuilder where = new StringBuilder("");
            where.append(" WHERE pat13.lab24c1 = ? AND pat01c1 = ?");
              
            parametersList.add(idSample);
            parametersList.add(idCase);

            Object[] parametersArr = new Object[parametersList.size()];
            parametersArr = parametersList.toArray(parametersArr);
            
            return getJdbcTemplatePat().query(select + where.toString(),
                    parametersArr, (ResultSet rs, int i) ->
            {
                
                SampleCasete sample = new SampleCasete();
                sample.setId(rs.getInt("pat18c1"));
                sample.setSample(rs.getInt("pat14c1"));
                sample.setQuantity(rs.getInt("pat18c2"));
                sample.setConsecutive(rs.getString("pat18c3"));
                sample.getCasete().setId(rs.getInt("pat03c1"));
                return sample;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }
    
    /**
    * Guarda la información general de los casetes de las muestras
    *
    * @param samples Lista de {@link net.cltech.enterprisent.domain.operation.pathology.SampleCasete}
    *
    * @throws Exception Error en base de datos
    */
    default void create(List<SampleCasete> samples) throws Exception
    {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplatePat())
                .withTableName("pat18")
                .usingGeneratedKeyColumns("pat18c1");
        
        HashMap<String, Object> parameters = new HashMap<>(0);
        
        for(SampleCasete sample : samples) {
            deleteCasetes(sample);
            deleteListCasetes(sample);
            parameters = new HashMap<>(0);
            parameters.put("pat14c1", sample.getSample());
            parameters.put("pat18c2", sample.getQuantity());
            parameters.put("pat18c3", sample.getConsecutive().trim());
            parameters.put("pat03c1", sample.getCasete().getId());
            Number id = insert.executeAndReturnKey(parameters);
            
            createListCasetes(sample, id.intValue());
        }
    }
    
    /**
    * Elimina los casetes de las muestras
    *
    * @param sample datos de la muestra.
    *
    * @return número de registros afectados
    * @throws Exception Error BD
    */
    default int deleteCasetes(SampleCasete sample) throws Exception
    {
        String query = " DELETE FROM pat18 WHERE pat14c1 = ? AND pat18c1 = ?";
        return getJdbcTemplatePat().update(query, sample.getSample(), sample.getId());
    }
    
    /**
    * Guarda la lista de los casetes de las muestras
    *
    * @param sample datos generales de la configuracion del casete
    * @param id datos generales de la configuracion del casete
    *
    * @throws Exception Error en base de datos
    */
    default void createListCasetes(SampleCasete sample, Integer id) throws Exception
    {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplatePat())
                .withTableName("pat32")
                .usingGeneratedKeyColumns("pat32c1");
        
        HashMap<String, Object> parameters = new HashMap<>(0);
        for(int i=1; i <= sample.getQuantity() ; i++) {
            parameters = new HashMap<>(0);
            parameters.put("pat14c1", sample.getSample());
            parameters.put("pat32c2", i + sample.getConsecutive().trim());
            parameters.put("pat03c1", sample.getCasete().getId());
            parameters.put("pat18c1", id);
            parameters.put("pat32c3", 1);
            Number casete = insert.executeAndReturnKey(parameters);
            
            CaseteTracking tracking = new CaseteTracking();
            tracking.setCasete(casete.intValue());
            tracking.setStatus(Constants.TISSUEPROCESSOR_PENDING);
            tracking.setCauser(getUser());
            getCaseteTrackingDao().create(tracking);
        }
    }
    
    /**
    * Elimina la lista de los casetes de las muestras
    *
    * @param sample datos de la muestra.
    *
    * @return número de registros afectados
    * @throws Exception Error BD
    */
    default int deleteListCasetes(SampleCasete sample) throws Exception
    {
        String query = " DELETE FROM pat32 WHERE pat14c1 = ? AND pat18c1 = ?";
        return getJdbcTemplatePat().update(query, sample.getSample(), sample.getId());
    }
    
    /**
     * Obtiene una lista de casetes de casos filtrados
     *
     * @param filter Filtro de
     * {@link net.cltech.enterprisent.domain.operation.pathology.FilterPathology}
     * casos 
     * @param branch Id sede usuario
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.pathology.SampleCasete}
     * @throws Exception Error en la base de datos.
     */
    default List<SampleCasete> getCasetesByFilterCases(final FilterPathology filter, Integer branch) throws Exception
    {
        try
        {
            String select = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT pat32.pat32c1, pat32.pat14c1, pat32c2, pat32c3, pat32.pat03c1, pat03c2, pat03c3, pat03c5, " 
                    + " lab24c1, pat14.pat13c1, pat13.pat07c1, pat07c2, pat07c3, pat13.pat01c1, pat01c2, lab22c1, pat01.pat11c1, pat11c2, pat11c3, "
                    + " pat33.pat33c1, pat33c2, pat33.pat31c1, pat31c2 ";

            String from = ""
                    + " FROM pat32 "
                    + " LEFT JOIN pat03 ON pat03.pat03c1 = pat32.pat03c1 "
                    + " LEFT JOIN pat14 ON pat14.pat14c1 = pat32.pat14c1 "
                    + " LEFT JOIN pat13 ON pat13.pat13c1 = pat14.pat13c1 "
                    + " LEFT JOIN pat07 ON pat07.pat07c1 = pat13.pat07c1 "
                    + " LEFT JOIN pat01 ON pat01.pat01c1 = pat13.pat01c1 "
                    + " LEFT JOIN pat11 ON pat11.pat11c1 = pat01.pat11c1 "
                    + " LEFT JOIN pat33 ON pat33.pat32c1 = pat32.pat32c1 "
                    + " LEFT JOIN pat31 ON pat33.pat31c1 = pat31.pat31c1 "
                    + "";

            List<Object> parametersList = new ArrayList<>();
            StringBuilder where = new StringBuilder("");

            if (filter != null)
            {
                //--------Filtro por estados
                if (filter.getAreas().size() > 0)
                {
                    where.append(" WHERE pat32.pat32c3 in (").append(filter.getStatus().stream().map(status -> status.toString()).collect(Collectors.joining(","))).append(") ");
                }
                
                //--------Filtro por areas
                if (filter.getAreas().size() > 0)
                {
                    where.append(" AND pat01.pat01c4 in (").append(filter.getAreas().stream().map(area -> area.toString()).collect(Collectors.joining(","))).append(") ");
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
                
                SampleCasete sample = new SampleCasete();
                
                sample.setId(rs.getInt("pat32c1"));
                sample.setSample(rs.getInt("pat14c1"));
                sample.setConsecutive(rs.getString("pat32c2"));
                sample.setStatus(rs.getInt("pat32c3"));
                sample.getCasete().setId(rs.getInt("pat03c1"));
                sample.getCasete().setCode(rs.getString("pat03c2"));
                sample.getCasete().setName(rs.getString("pat03c3"));
                sample.getCasete().setColour(rs.getString("pat03c5"));
                sample.getSpecimen().setId(rs.getInt("lab24c1"));
                sample.getSpecimen().setSample(rs.getInt("pat13c1"));
                sample.getSpecimen().getOrgan().setId(rs.getInt("pat07c1"));
                sample.getSpecimen().getOrgan().setCode(rs.getString("pat07c2"));
                sample.getSpecimen().getOrgan().setName(rs.getString("pat07c3"));
                sample.getSpecimen().getCasePat().setId(rs.getInt("pat01c1"));
                sample.getSpecimen().getCasePat().setNumberCase(rs.getLong("pat01c2"));
                sample.getSpecimen().getCasePat().getOrder().setNumberOrder(rs.getLong("lab22c1"));
                sample.getSpecimen().getCasePat().getStudyType().setId(rs.getInt("pat11c1"));
                sample.getSpecimen().getCasePat().getStudyType().setCode(rs.getString("pat11c2"));
                sample.getSpecimen().getCasePat().getStudyType().setName(rs.getString("pat11c3"));
                
                /*Procesador de tejidos*/
                sample.getTissueProcessor().setId(rs.getInt("pat33c1"));
                sample.getTissueProcessor().setHours(rs.getInt("pat33c2"));
                sample.getTissueProcessor().getTime().setId(rs.getInt("pat31c1"));
                sample.getTissueProcessor().getTime().setTime(rs.getString("pat31c2"));
                
                return sample;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            ex.getMessage();
            return new ArrayList<>(0);
        }
    }
    
    /**
    * Actualiza el estado de un casete en el sistema
    *
    * @param casete Id del casete
    * @param status estado
    *
    * @throws Exception Error en base de datos
    */
    default void changeCaseteStatus(Integer casete, Integer status) throws Exception
    {
        String update = ""
                + "UPDATE pat32 "
                + "SET pat32c3 = ? ";
        
        List object = new ArrayList(0);
        object.add(status);

        update += " WHERE pat32c1 = ? ";
        object.add(casete);
        getJdbcTemplatePat().update(update, object.toArray());
        
        CaseteTracking tracking = new CaseteTracking();
        tracking.setCasete(casete);
        tracking.setStatus(status);
        tracking.setCauser(getUser());
        getCaseteTrackingDao().create(tracking);
    }
    
    /**
     * Obtiene la cantidad de casetes en un estado
     *
     * @param status Estado
     *
     * @return cantidad
     * @throws java.lang.Exception
     */
    default Integer getCountCasetes(int status) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT COUNT(pat32c1) FROM pat32 ")
                    .append("WHERE pat32c3 = ").append(status);

            return getJdbcTemplatePat().queryForObject(query.toString(), (ResultSet rs, int i) ->
            {
                return rs.getInt("count");
            });
        }
        catch (Exception e)
        {
            return 0;
        }
    }
    
    /**
     * Obtiene la cantidad de casetes en un estado por tipos de estudios
     *
     * @param status Estado
     *
     * @return 
     * @throws java.lang.Exception
     */
    default List<WidgetPathology> getCountCasetesByStudyType(int status) throws Exception
    {
        try
        {
            String select = ""
                    + "SELECT pat11c3, COUNT(pat11.pat11c1) ";

            String from = ""
                    + " FROM pat11 "
                    + " JOIN pat01 ON pat11.pat11c1 = pat01.pat11c1 "
                    + " JOIN pat13 ON pat13.pat01c1 = pat01.pat01c1 "
                    + " JOIN pat14 ON pat14.pat13c1 = pat13.pat13c1 "
                    + " JOIN pat32 ON pat14.pat14c1 = pat32.pat14c1 "
                    + "";

            List<Object> parametersList = new ArrayList<>();
            StringBuilder where = new StringBuilder("");

            if (status != 0)
            {
                where.append(" WHERE pat32c3 = ? ");
                parametersList.add(status);

            } else
            {
                return new ArrayList<>(0);
            }
            
            where.append(" GROUP BY pat11c3 ");


            Object[] parametersArr = new Object[parametersList.size()];
            parametersArr = parametersList.toArray(parametersArr);

            return getJdbcTemplatePat().query(select + from + where.toString(),
                    parametersArr, (ResultSet rs, int i) ->
            {
                WidgetPathology widget = new WidgetPathology();
                widget.setLabel(rs.getString("pat11c3"));
                widget.setValue(rs.getInt("count"));
                return widget;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            ex.getMessage();
            return new ArrayList<>(0);
        }
    }
    
    /**
     * Obtiene la cantidad de casetes en un estado por horas de procesamiento
     *
     * @param status Estado
     *
     * @return 
     * @throws java.lang.Exception
     */
    default List<WidgetPathology> getCountCasetesByTime(int status) throws Exception
    {
        try
        {
            String select = ""
                    + "SELECT pat31c2, COUNT(pat32.pat32c1) ";

            String from = ""
                    + " FROM pat31 "
                    + " JOIN pat33 ON pat33.pat31c1 = pat31.pat31c1 "
                    + " JOIN pat32 ON pat32.pat32c1 = pat33.pat32c1 "
                    + "";

            List<Object> parametersList = new ArrayList<>();
            StringBuilder where = new StringBuilder("");

            if (status != 0)
            {
                where.append(" WHERE pat32c3 = ? ");
                parametersList.add(status);

            } else
            {
                return new ArrayList<>(0);
            }
            
            where.append(" GROUP BY pat31c2 ");


            Object[] parametersArr = new Object[parametersList.size()];
            parametersArr = parametersList.toArray(parametersArr);

            return getJdbcTemplatePat().query(select + from + where.toString(),
                    parametersArr, (ResultSet rs, int i) ->
            {
                WidgetPathology widget = new WidgetPathology();
                widget.setLabel(rs.getString("pat31c2"));
                widget.setValue(rs.getInt("count"));
                return widget;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            ex.getMessage();
            return new ArrayList<>(0);
        }
    }
}
