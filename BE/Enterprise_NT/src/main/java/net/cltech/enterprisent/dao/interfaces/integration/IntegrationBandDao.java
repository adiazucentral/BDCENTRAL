package net.cltech.enterprisent.dao.interfaces.integration;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import net.cltech.enterprisent.domain.integration.band.BandSample;
import net.cltech.enterprisent.domain.integration.band.BandVerifiedSample;
import net.cltech.enterprisent.domain.integration.statusBandReason.StatusBandReason;
import net.cltech.enterprisent.domain.integration.statusBandReason.StatusBandReasonUser;
import net.cltech.enterprisent.domain.masters.common.Motive;
import net.cltech.enterprisent.tools.Tools;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa la interfaz que contiene los metodos para el acceso de datos y sus implementaciones 
 * para acceder a la informacion de la base de datos de PostgreSQL y SQLServer
 *
 * @version 1.0.0
 * @author Julian
 * @since 21/05/2020
 * @see Creación
 */
public interface IntegrationBandDao
{
    
    /**
     * Obtiene la conexion a la base de datos
     * 
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplate();
    
    /**
     * Obtiene un listado con las ordenes registradas el dia de hoy
     * 
     * @return 
     * @throws Exception Error al obtener las muestras verificadas
     */
    public List<Long> ordersOfTheDay() throws Exception;
    
    /**
     * Obtiene un objeto de la muestra verificada en el destino, 
     * solo si esa muestra pertenece al mismo
     * 
     * @param idOrder
     * @param idSample
     * @param idBranch
     * @param idDestination
     * @return Muestra verificada para ese destino
     * Si este dao retorna nulo quiere decir que dicha muestra no a sido ingresado en dicho destino
     * @throws Exception Error al obtener las muestras verificadas
     */
    default BandVerifiedSample verifiedSampleInADestination(Long idOrder, int idSample, int idBranch, int idDestination) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab22.lab22c1 AS orderId")
                    .append(", lab103.lab103c3 AS orderTypeName")
                    .append(", lab54.lab54c2 AS documentTypeAbbr")
                    .append(", lab21.lab21c2 AS patientId")
                    .append(" FROM lab25 ")
                    .append("JOIN lab42 ON lab42.lab42c1 = lab25.lab42c1 ")
                    .append("JOIN lab52 ON lab52.lab52c1 = lab42.lab52c1 ")
                    .append("JOIN lab22 ON lab22.lab22c1 = lab25.lab22c1 ")
                    .append("JOIN lab103 ON lab103.lab103c1 = lab22.lab103c1 ")
                    .append("JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1 ")
                    .append("JOIN lab54 ON lab54.lab54c1 = lab21.lab54c1 ")
                    .append("WHERE lab25.lab22c1 = ").append(idOrder)
                    .append(" AND lab52.lab24c1 = ").append(idSample)
                    .append(" AND lab52.lab05c1 = ").append(idBranch)
                    .append(" AND lab42.lab53c1 = ").append(idDestination)
                    .append(" AND (lab22c19 = 0 or lab22c19 is null)  ");
              
                    
            return getJdbcTemplate().queryForObject(query.toString(), 
                    (ResultSet rs, int i) ->
            {
                BandVerifiedSample verifiedSample = new BandVerifiedSample();
                verifiedSample.setOrder(rs.getString("orderId"));
                verifiedSample.setType(rs.getString("orderTypeName"));
                /*Paciente*/
                verifiedSample.getPatient().setIdtype(rs.getString("documentTypeAbbr"));
                verifiedSample.getPatient().setPatientid(Tools.decrypt(rs.getString("patientId")));
                return verifiedSample;
            });
        }
        catch (Exception e)
        {
            return null;
        }
    }
    
    /**
     * Obtiene el id de la asignación de destinos
     * 
     * @param orderTypeId 
     * @param idSample
     * @param idBranch
     * @param idDestination
     * @return 
     * @throws Exception Error al obtener las muestras verificadas
     */
    default Integer getsTheDestinationAssignmentId(int orderTypeId, int idSample, int idBranch, int idDestination) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab52c1")
                    .append(" FROM lab52 ")
                    .append("WHERE lab103c1 = ").append(orderTypeId)
                    .append(" AND lab05c1 = ").append(idBranch)
                    .append(" AND lab24c1 = ").append(idSample)
                    .append(" AND lab07c1 = 1");
                    
            return getJdbcTemplate().queryForObject(query.toString(), 
                    (ResultSet rs, int i) ->
            {
                return rs.getInt("lab52c1");
            });
        }
        catch (DataAccessException e)
        {
            return null;
        }
    }
    
    /**
     * Verificá que la asignación de destinos a la que pertenece una muestra
     * pertenezca a una ruta y por ende esa muestra perteneceria a ese destino
     * 
     * @param idAssignmentOfDestination 
     * @param destinationId 
     * 
     * @return True - La muestra pertenece a ese destino, False - La muestra no pertenece a ese destino
     * @throws Exception Error al obtener las muestras verificadas
     */
    default Boolean theSampleBelongsToThisDestination(int idAssignmentOfDestination, int destinationId) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab42c1")
                    .append(" FROM lab42 ")
                    .append("WHERE lab52c1 = ").append(idAssignmentOfDestination)
                    .append(" AND lab53c1 = ").append(destinationId);
                    
            return getJdbcTemplate().queryForObject(query.toString(), 
                    (ResultSet rs, int i) ->
            {
                return rs.getInt("lab42c1") > 0;
            });
        }
        catch (DataAccessException e)
        {
            return null;
        }
    }
    
    /**
     * Obtiene un objeto de la muestra no verificada en el destino, por el id de la orden
     * 
     * @param idOrder
     * @return Muestra no verificada para ese destino
     * @throws Exception Error al obtener las muestras verificadas
     */
    default BandVerifiedSample getTheOrderWithUnverifiedSample(Long idOrder) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab22.lab22c1 AS orderId")
                    .append(", lab103.lab103c3 AS orderTypeName")
                    .append(", lab54.lab54c2 AS documentTypeAbbr")
                    .append(", lab21.lab21c2 AS patientId")
                    .append(" FROM lab22 ")
                    .append("JOIN lab103 ON lab103.lab103c1 = lab22.lab103c1 ")
                    .append("JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1 ")
                    .append("JOIN lab54 ON lab54.lab54c1 = lab21.lab54c1 ")
                    .append("WHERE lab22.lab22c1 = ").append(idOrder)
                    .append(" AND (lab22c19 = 0 or lab22c19 is null)  ");
                    
            return getJdbcTemplate().queryForObject(query.toString(), 
                    (ResultSet rs, int i) ->
            {
                BandVerifiedSample verifiedSample = new BandVerifiedSample();
                verifiedSample.setOrder(rs.getString("orderId"));
                verifiedSample.setType(rs.getString("orderTypeName"));
                /*Paciente*/
                verifiedSample.getPatient().setIdtype(rs.getString("documentTypeAbbr"));
                verifiedSample.getPatient().setPatientid(Tools.decrypt(rs.getString("patientId")));
                return verifiedSample;
            });
        }
        catch (Exception e)
        {
            return null;
        }
    }
    
    /**
     * Obtiene el id de una ruta de asignacion
     * 
     * @param idSample
     * @param idBranch
     * @param idDestination
     * @return 
     * @throws Exception Error al obtener las muestras verificadas
     */
    default int getIdAssignRout(int idSample, int idBranch, int idDestination) throws Exception
    {
        try
        {
            StringBuilder query = new StringBuilder();
            query.append("SELECT lab42c1 ")
                    .append("FROM lab42 ")
                    .append("JOIN lab52 ON lab52.lab52c1 = lab42.lab52c1 ")
                    .append("JOIN lab24 ON (lab52.lab24c1 = lab24.lab24c1) ")
                    .append("JOIN lab53 ON (lab53.lab53c1 = lab42.lab53c1) ")
                    .append("WHERE lab52.lab24c1 = ").append(idSample)
                    .append(" AND lab52.lab05c1 = ").append(idBranch)
                    .append(" AND lab42.lab53c1 = ").append(idDestination);
            
            return getJdbcTemplate().queryForObject(query.toString(), (ResultSet rs, int i)
                    ->
            {
               return rs.getInt("lab42c1");
            });
        }
        catch (Exception e)
        {
            return 0;
        }
    }
    
    /**
     * Obtiene la lista de muestras que son verificadas
     * @return lista de muestras
     * @throws Exception Error al obtener la lista de muestras
     */
    default List<BandSample> listSamples() throws Exception
    {
        try
        {
            return getJdbcTemplate().query(""
                    + "SELECT lab24c1, lab24c2, lab24c9, lab24.lab56c1, lab56c2 "
                    + "FROM lab24 "
                    + "LEFT JOIN lab56 ON lab24.lab56c1 = lab56.lab56c1 "
                    + "WHERE lab24.lab07c1 = 1 and lab24c5=1", (ResultSet rs, int i) ->
            {
                BandSample bandSample = new BandSample();
                bandSample.setIdSample(rs.getInt("lab24c1"));
                bandSample.setCodeSample(rs.getString("lab24c9"));
                bandSample.setNameSample(rs.getString("lab24c2"));
                bandSample.setIdContainer(rs.getInt("lab56c1"));
                bandSample.setNameContainer(rs.getString("lab56c2"));
                
                return bandSample;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }
    
    default StatusBandReason createStatusBandReason(StatusBandReason statusBandReason)throws Exception
    {
        try
        {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, 365);

            Timestamp timestamp = new Timestamp(new Date().getTime());
            SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                    .withTableName("lab192")
                    .usingColumns("lab192c2", "lab192c3", "lab192c4", "lab192c5", "lab192c6")
                    .usingGeneratedKeyColumns("lab192c1");

            HashMap parameters = new HashMap();

            parameters.put("lab192c2", statusBandReason.getUserid());
            parameters.put("lab192c3", timestamp);
            parameters.put("lab192c4",statusBandReason.getIdstatusBand());
            parameters.put("lab192c5",statusBandReason.getIdreason());
            parameters.put("lab192c6",statusBandReason.getCommentary());

            
            Number key = insert.executeAndReturnKey(parameters);
            statusBandReason.setIdstatusBand(key.intValue());

            return statusBandReason;
        }
        catch (Exception e)
        {
            return null;
        }
        
    }
    
    
    
    
    /////////////////////////////////////////////////////////////////////////////////////////////////7
    
    /**
     * Obtiene la lista de idusuario, nombre usuario y  motivo
     * @return lista de idusuario, nombre usuario y  motivo
     * @throws Exception Error al obtener la lista 
     */
    default List<StatusBandReasonUser> listReason() throws Exception
    {
        try
        {
            return getJdbcTemplate().query(""
                    + "SELECT lab04.lab04c1, lab04.lab04c2, lab30.lab30c1, lab30.lab30c2, lab30.lab30c3, lab192c3 , lab192c6 "
                    + "FROM lab192 "
                    + "LEFT JOIN lab04 ON lab192.lab192c2 = lab04.lab04c1 "
                    + "LEFT JOIN lab30 ON lab192.lab192c5 = lab30.lab30c1 "
                    , (ResultSet rs, int i) ->
            {
                StatusBandReasonUser statusBandReasonUser=new StatusBandReasonUser();
                
                
                statusBandReasonUser.setUserid(rs.getInt("lab04c1"));
                statusBandReasonUser.setUserName(rs.getString("lab04c2"));
                statusBandReasonUser.setRegistrationDate(rs.getTimestamp("lab192c3"));
                statusBandReasonUser.setIdreason(rs.getInt("lab30c1"));
                statusBandReasonUser.setReasonName(rs.getString("lab30c2"));
                statusBandReasonUser.setDescriptionReason(rs.getString("lab30c3"));
                statusBandReasonUser.setCommentary(rs.getString("lab192c6"));
                
                return statusBandReasonUser;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }
    
    /**
     * Obtiene toda la lista de  motivos
     * @return lista de motivos
     * @throws Exception Error al obtener la lista 
     */
    default List<Motive> listReasonBand() throws Exception
    {
        try {
            return getJdbcTemplate().query(""
                    + "SELECT lab30c1, "
                    + "lab30c2, "
                    + "lab30c3, "
                    + "lab30c5, "
                    + "lab30.lab07c1, "
                    + "lab30.lab04c1, "
                    + "lab80c1 "
                    + "FROM lab30 "
                    + "LEFT JOIN lab80 ON lab80.lab80c1 = lab30.lab30c4 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab30.lab04c1 "
                    + "WHERE lab30c4 = 64 "
                    , (ResultSet rs, int i) ->
            {
                Motive motive=new Motive();
                
                motive.setId(rs.getInt("lab30c1"));
                motive.setName(rs.getString("lab30c2"));
                motive.setDescription(rs.getString("lab30c3"));
                //motive.setType(rs.getInt(""));
                motive.setLastTransaction(rs.getTimestamp("lab30c5"));
                /*Tipo*/
                motive.getType().setId(rs.getInt("lab80c1"));
                 /*Usuario*/
                motive.getUser().setId(rs.getInt("lab04c1"));
                
                motive.setState(rs.getInt("lab07c1") == 1);
                

                return motive;
            });
            
        } catch (EmptyResultDataAccessException ex) {
            return new ArrayList<>(0);
        }
    }
    
    
    
    
    /**
     * Obtiene toda la lista de usuarios y motivos
     * @return lista de usuarios y muestras
     * @throws Exception Error al obtener la lista 
     */
//     default List<StatusBandReason> listReason() throws Exception
//    {
//        try
//        {
//            return getJdbcTemplate().query(""
//                    + "SELECT lab192c1 , lab192c2 , lab192c3 , lab192c4 , lab192c5 , lab192c6 "
//                    + "FROM lab192 "
//                    , (ResultSet rs, int i) ->
//            {
//                StatusBandReason statusBandReason=new StatusBandReason();
//                
//                
//                statusBandReason.setId(rs.getInt("lab192c1"));
//                statusBandReason.setUserid(rs.getInt("lab192c2"));
//              //  statusBandReason.setUserName(rs.getString("lab04c2"));
//                statusBandReason.setRegistrationDate(rs.getTimestamp("lab192c3"));
//                statusBandReason.setIdstatusBand(rs.getInt("lab192c4"));
//                statusBandReason.setIdreason(rs.getInt("lab192c5"));
//                statusBandReason.setCommentary(rs.getString("lab192c6"));
//                
//                return statusBandReason;
//            });
//        } catch (EmptyResultDataAccessException ex)
//        {
//            return new ArrayList<>(0);
//        }
//    }
}
