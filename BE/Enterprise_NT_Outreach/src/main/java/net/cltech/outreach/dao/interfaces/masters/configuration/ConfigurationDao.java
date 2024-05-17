package net.cltech.outreach.dao.interfaces.masters.configuration;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import net.cltech.outreach.domain.masters.configuration.Configuration;
import net.cltech.outreach.domain.masters.configuration.DocumentType;
import net.cltech.outreach.tools.Tools;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 * Representa los metodos de acceso a base de datos para la informacion de la
 * configuración general de la consulta web
 *
 * @version 1.0.0
 * @author eacuna
 * @since 23/04/2018
 * @see Creación
 */
public interface ConfigurationDao
{

    /**
     * Obtiene todas las llaves de configuracion
     *
     * @return Lista de
     * {@link net.cltech.outreach.domain.masters.configuration.Configuration}
     * @throws Exception Error en base de datos
     */
    default List<Configuration> get() throws Exception
    {
        List<Configuration> configurationList = new ArrayList<>(0);
        try
        {
            configurationList = getJdbcTemplate().query(""
                    + "SELECT   lab99c1 "
                    + "         , lab99c2 "
                    + "FROM     lab99",
                    new RowMapper<Configuration>()
            {

                @Override
                public Configuration mapRow(ResultSet rs, int i) throws SQLException
                {
                    Configuration configuration = new Configuration();
                    configuration.setKey(rs.getString("lab99c1"));
                    configuration.setValue(rs.getString("lab99c2"));
                    return configuration;
                }
            });

        } catch (EmptyResultDataAccessException ex)
        {

        } finally
        {
            configurationList.addAll(getLISConfiguration());
        }
        return configurationList;

    }
    
     /**
     * Obtiene todas las llaves de configuracion enciptadas
     *
     * @return Lista de
     * {@link net.cltech.outreach.domain.masters.configuration.Configuration}
     * @throws Exception Error en base de datos
     */
    default List<Configuration> getEncrypted() throws Exception
    {
        List<Configuration> configurationList = new ArrayList<>(0);
        List<Configuration> configurationList2 = new ArrayList<>(0);
        try
        {
            configurationList = getJdbcTemplate().query(""
                    + "SELECT   lab99c1 "
                    + "         , lab99c2 "
                    + "FROM     lab99",
                    new RowMapper<Configuration>()
            {

                @Override
                public Configuration mapRow(ResultSet rs, int i) throws SQLException
                {
                    Configuration configuration = new Configuration();
                    configuration.setKey(Base64.getEncoder().encodeToString(rs.getString("lab99c1").getBytes()));
                    configuration.setValue(rs.getString("lab99c2") != null ? Base64.getEncoder().encodeToString(rs.getString("lab99c2").getBytes()): "");
                    configuration.setOrigin(1);
                    return configuration;
                }
            });

        } catch (EmptyResultDataAccessException ex)
        {

        } finally
        {
            configurationList.addAll(getLISConfigurationEncrypted());
            String dataconfig= Tools.jsonObject(configurationList);              
            Configuration configurationpru = new Configuration();
            configurationpru.setKey("dataconfig");            
            configurationpru.setValue(Base64.getEncoder().encodeToString(dataconfig.getBytes()));    
            configurationList2.add(configurationpru);            
        }
        return configurationList2;

    }
    
      /**
     * Obtiene el nombre del laboratorio de la llave de configurtacion
     *
     * @return Objeto Configuration
     * @throws Exception Error en base de datos
     */
    default List<Configuration> getLISConfigurationEncrypted() throws Exception
    {
        try
        {
            return getJdbcTemplate().query(""
                    + "SELECT   lab98c1, "
                    + "         lab98c2 "
                    + "FROM     lab98 "
                    + " WHERE   Lab98c1 = 'Entidad' OR Lab98c1 = 'ManejoTipoDocumento' OR Lab98c1 = 'HistoriaAutomatica' OR Lab98c1 = 'FormatoFecha' OR Lab98c1 = 'DigitosOrden' OR Lab98c1 = 'ComentarioResultadoPendiente' OR Lab98c1 = 'ComentarioRegistroRestringido' OR Lab98c1 = 'SmtpHostName' OR Lab98c1 = 'SmtpPort' OR Lab98c1 = 'SmtpPort' OR Lab98c1 = 'SmtpAuthUser' OR Lab98c1 = 'SmtpPasswordUser' OR Lab98c1 = 'UrlSecurity' OR Lab98c1 = 'ManejoDemograficoConsultaWeb' OR Lab98c1 = 'DemograficoConsultaWeb' OR Lab98c1 = 'SecurityPolitics' OR Lab98c1 = 'SessionExpirationTime' OR Lab98c1 = 'AniosConsultas' ",
                    new RowMapper<Configuration>()
            {

                @Override
                public Configuration mapRow(ResultSet rs, int i) throws SQLException
                {
                    Configuration configuration = new Configuration();
                    configuration.setKey(Base64.getEncoder().encodeToString(rs.getString("lab98c1").getBytes()));
                    configuration.setValue(rs.getString("lab98c2") != null ? Base64.getEncoder().encodeToString(rs.getString("lab98c2").getBytes()) : "");                    
                    configuration.setOrigin(2);
                    return configuration;
                }
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>();
        }
    }


    /**
     * Obtiene el nombre del laboratorio de la llave de configurtacion
     *
     * @return Objeto Configuration
     * @throws Exception Error en base de datos
     */
    default List<Configuration> getLISConfiguration() throws Exception
    {
        try
        {
            return getJdbcTemplate().query(""
                    + "SELECT   lab98c1, "
                    + "         lab98c2 "
                    + "FROM     lab98 "
                    + " WHERE   Lab98c1 = 'Entidad' OR Lab98c1 = 'ManejoTipoDocumento' OR Lab98c1 = 'HistoriaAutomatica' OR Lab98c1 = 'FormatoFecha' OR Lab98c1 = 'DigitosOrden' OR Lab98c1 = 'ComentarioResultadoPendiente' OR Lab98c1 = 'ComentarioRegistroRestringido' OR Lab98c1 = 'SmtpHostName' OR Lab98c1 = 'SmtpPort' OR Lab98c1 = 'SmtpPort' OR Lab98c1 = 'SmtpAuthUser' OR Lab98c1 = 'SmtpPasswordUser' OR Lab98c1 = 'UrlSecurity' OR Lab98c1 = 'ManejoDemograficoConsultaWeb' OR Lab98c1 = 'DemograficoConsultaWeb' OR Lab98c1 = 'SecurityPolitics' OR Lab98c1 = 'SessionExpirationTime' OR Lab98c1 = 'AniosConsultas' OR Lab98c1 = 'MedicosAuxiliares' ",
                    new RowMapper<Configuration>()
            {

                @Override
                public Configuration mapRow(ResultSet rs, int i) throws SQLException
                {
                    Configuration configuration = new Configuration();
                    configuration.setKey(rs.getString("lab98c1"));
                    configuration.setValue(rs.getString("lab98c2"));
                    return configuration;
                }
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>();
        }
    }

    /**
     * Obtiene una llave de configuraciòn
     *
     * @param key Llave de configuraciòn
     * @return
     * {@link net.cltech.outreach.domain.masters.configuration.Configuration},
     * null en caso de que no se encuentre la llave
     * @throws Exception Error en base de datos
     */
    default Configuration get(String key) throws Exception
    {
        try
        {
            return getJdbcTemplate().queryForObject(""
                    + "SELECT   lab99c1 "
                    + "         , lab99c2 "
                    + "FROM     lab99 "
                    + "WHERE    lab99c1 = ?",
                    new Object[]
                    {
                        key
                    },
                    new RowMapper<Configuration>()
            {

                @Override
                public Configuration mapRow(ResultSet rs, int i) throws SQLException
                {
                    Configuration configuration = new Configuration();
                    configuration.setKey(rs.getString("lab99c1"));
                    configuration.setValue(rs.getString("lab99c2"));
                    return configuration;
                }
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Actualiza una llave de configuración si existe
     *
     * @param configuration
     * {@link net.cltech.outreach.domain.masters.configuration.Configuration}
     * @throws Exception Error en base de datos
     */
    default void update(Configuration configuration) throws Exception
    {
        getJdbcTemplate().update("UPDATE lab99 SET lab99c2 = ? WHERE lab99c1 = ?", configuration.getValue(), configuration.getKey());
    }

    /**
     * Lista los tipos de documento desde la base de datos.
     *
     * @return Lista de tipos de documento.
     * @throws Exception Error en la base de datos.
     */
    default List<DocumentType> listDocumentType() throws Exception
    {
        try
        {
            return getJdbcTemplate().query(""
                    + "SELECT lab54c1, lab54c2, lab54c3, lab54c4, lab54.lab04c1, lab04c2, lab04c3, lab04c4, lab54.lab07c1 "
                    + "FROM lab54 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab54.lab04c1",
                    new RowMapper<DocumentType>()
            {
                @Override
                public DocumentType mapRow(ResultSet rs, int i) throws SQLException
                {
                    DocumentType documentType = new DocumentType();
                    documentType.setId(rs.getInt("lab54c1"));
                    documentType.setAbbr(rs.getString("lab54c2"));
                    documentType.setName(rs.getString("lab54c3"));
                    documentType.setState(rs.getInt("lab07c1") == 1);

                    return documentType;
                }
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplate();
}
