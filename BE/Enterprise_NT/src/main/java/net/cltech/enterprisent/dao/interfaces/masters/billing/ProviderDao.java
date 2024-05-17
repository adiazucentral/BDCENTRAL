package net.cltech.enterprisent.dao.interfaces.masters.billing;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import net.cltech.enterprisent.domain.masters.billing.Provider;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información de los Emisores.
 *
 * @version 1.0.0
 * @author eacuna
 * @since 02/05/2018
 * @see Creación
 */
public interface ProviderDao
{

    /**
     * Lista los emisors desde la base de datos.
     *
     * @return Lista de emisors.
     * @throws Exception Error en la base de datos.
     */
    default List<Provider> list() throws Exception
    {
        try
        {
            return getJdbcTemplate().query(""
                    + "SELECT lab906c1, "
                    + "lab906c2, "
                    + "lab906c3, "
                    + "lab906c4, "
                    + "lab906c5, "
                    + "lab906c6, "
                    + "lab906c7, "
                    + "lab906c8, "
                    + "lab906c9, "
                    + "lab906.lab07c1, "
                    + "lab906c10 AS taxRegimen, "
                    + "lab906c11 AS currentNumber, "
                    + "lab906c12 AS state, "
                    + "lab906c13 AS municipality, "
                    + "lab906c14 AS nameElectronicInvoicing, "
                    + "lab906c15 AS electronicBillingPhone, "
                    + "lab906c16 AS privateKey, "
                    + "lab906c17 AS certificate, "
                    + "lab906c18 AS password, "
                    + "lab906c19, "
                    + "lab906.lab04c1, lab04c2, lab04c3, lab04c4 "
                    + "FROM lab906 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab906.lab04c1", (ResultSet rs, int i) ->
            {
                Provider provider = new Provider();
                provider.setId(rs.getInt("lab906c1"));
                provider.setNit(rs.getString("lab906c2"));
                provider.setName(rs.getString("lab906c3"));
                provider.setAddress(rs.getString("lab906c4"));
                provider.setPhone(rs.getString("lab906c5"));
                provider.setResponsable(rs.getString("lab906c6"));
                provider.setCode(rs.getString("lab906c7"));
                provider.setPostalCode(rs.getString("lab906c8"));
                provider.setLastTransaction(rs.getTimestamp("lab906c9"));
                provider.setTaxRegimen(rs.getString("taxRegimen"));
                provider.setCurrentNumber(rs.getInt("currentNumber"));
                provider.setState(rs.getInt("state"));
                provider.setMunicipality(rs.getInt("municipality"));
                provider.setNameElectronicInvoicing(rs.getString("nameElectronicInvoicing"));
                provider.setElectronicBillingPhone(rs.getString("electronicBillingPhone"));
                provider.setPrivateKey(rs.getString("privateKey"));
                provider.setCertificate(rs.getString("certificate"));
                provider.setPassword(rs.getString("password"));
                provider.setApplyParticular(rs.getInt("lab906c19") == 1);
                /*Usuario*/
                provider.getUser().setId(rs.getInt("lab04c1"));
                provider.getUser().setName(rs.getString("lab04c2"));
                provider.getUser().setLastName(rs.getString("lab04c3"));
                provider.getUser().setUserName(rs.getString("lab04c4"));

                provider.setActive(rs.getInt("lab07c1") == 1);

                return provider;
            });
        }
        catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }

    /**
     * Registra un nuevo emisor en la base de datos.
     *
     * @param provider Instancia con los datos del emisor.
     *
     * @return Instancia con los datos del emisor.
     * @throws Exception Error en la base de datos.
     */
    default Provider create(Provider provider) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab906")
                .usingGeneratedKeyColumns("lab906c1");

        HashMap parameters = new HashMap();
        parameters.put("lab906c2", provider.getNit().trim());
        parameters.put("lab906c3", provider.getName().trim());
        parameters.put("lab906c4", provider.getAddress());
        parameters.put("lab906c5", provider.getPhone());
        parameters.put("lab906c6", provider.getResponsable());
        parameters.put("lab906c7", provider.getCode());
        parameters.put("lab906c8", provider.getPostalCode());
        parameters.put("lab906c9", timestamp);
        parameters.put("lab04c1", provider.getUser().getId());
        parameters.put("lab07c1", 1);
        parameters.put("lab906c10", provider.getTaxRegimen());
        parameters.put("lab906c11", provider.getCurrentNumber());
        parameters.put("lab906c12", provider.getState());
        parameters.put("lab906c13", provider.getMunicipality());
        parameters.put("lab906c14", provider.getNameElectronicInvoicing());
        parameters.put("lab906c15", provider.getElectronicBillingPhone());
        parameters.put("lab906c16", provider.getPrivateKey());
        parameters.put("lab906c17", provider.getCertificate());
        parameters.put("lab906c18", provider.getPassword());
        parameters.put("lab906c19", provider.getApplyParticular() == true ? 1 : 0);

        Number key = insert.executeAndReturnKey(parameters);
        provider.setId(key.intValue());
        provider.setLastTransaction(timestamp);

        return provider;
    }

    /**
     * Obtener información de un emisor por un campo especifico.
     *
     * @param id ID del emisor a ser consultado.
     * @param name Nombre del emisor a ser consultado.
     * @param nit Nit del emisor a ser consultado.
     * @param codeEps Codigo EPS del emisor a ser consultado.
     *
     * @return Instancia con los datos del emisor.
     * @throws Exception Error en la base de datos.
     */
    default Provider get(Integer id, String name, String nit, String codeEps) throws Exception
    {
        try
        {
            /*Columnas, Tabla, Inner Joins*/
            String query = "SELECT lab906c1, "
                    + "lab906c2, "
                    + "lab906c3, "
                    + "lab906c4, "
                    + "lab906c5, "
                    + "lab906c6, "
                    + "lab906c7, "
                    + "lab906c8, "
                    + "lab906c9, "
                    + "lab906c10 AS taxRegimen, "
                    + "lab906c11 AS currentNumber, "
                    + "lab906c12 AS state, "
                    + "lab906c13 AS municipality, "
                    + "lab906c14 AS nameElectronicInvoicing, "
                    + "lab906c15 AS electronicBillingPhone, "
                    + "lab906c16 AS privateKey, "
                    + "lab906c17 AS certificate, "
                    + "lab906c18 AS password, "
                    + "lab906.lab07c1, "
                    + "lab906.lab04c1, "
                    + "lab04c2, lab04c3, "
                    + "lab04c4, " 
                    + "lab906c19 "
                    + "FROM lab906 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab906.lab04c1 ";
            /*Where*/
            if (id != null)
            {
                query = query + "WHERE lab906c1 = ? ";
            }
            if (nit != null)
            {
                query = query + "WHERE UPPER(lab906c2) = ? ";
            }
            if (name != null)
            {
                query = query + "WHERE UPPER(lab906c3) = ? ";
            }
            if (codeEps != null)
            {
                query = query + "WHERE UPPER(lab906c7) = ? ";
            }


            /*Order By, Group By y demas complementos de la consulta*/
            query = query + "";

            Object object = null;
            if (id != null)
            {
                object = id;
            }
            if (nit != null)
            {
                object = nit;
            }
            if (name != null)
            {
                object = name.toUpperCase();
            }
            if (codeEps != null)
            {
                object = codeEps.toUpperCase();
            }

            return getJdbcTemplate().queryForObject(query,
                    new Object[]
                    {
                        object
                    }, (ResultSet rs, int i) ->
            {
                Provider provider = new Provider();
                provider.setId(rs.getInt("lab906c1"));
                provider.setNit(rs.getString("lab906c2"));
                provider.setName(rs.getString("lab906c3"));
                provider.setPhone(rs.getString("lab906c5"));
                provider.setResponsable(rs.getString("lab906c6"));
                provider.setCode(rs.getString("lab906c7"));
                provider.setAddress(rs.getString("lab906c4"));
                provider.setPostalCode(rs.getString("lab906c8"));
                provider.setLastTransaction(rs.getTimestamp("lab906c9"));
                provider.setTaxRegimen(rs.getString("taxRegimen"));
                provider.setCurrentNumber(rs.getInt("currentNumber"));
                provider.setState(rs.getInt("state"));
                provider.setMunicipality(rs.getInt("municipality"));
                provider.setNameElectronicInvoicing(rs.getString("nameElectronicInvoicing"));
                provider.setElectronicBillingPhone(rs.getString("electronicBillingPhone"));
                provider.setPrivateKey(rs.getString("privateKey"));
                provider.setCertificate(rs.getString("certificate"));
                provider.setPassword(rs.getString("password"));

                /*Usuario*/
                provider.getUser().setId(rs.getInt("lab04c1"));
                provider.getUser().setName(rs.getString("lab04c2"));
                provider.getUser().setLastName(rs.getString("lab04c3"));
                provider.getUser().setUserName(rs.getString("lab04c4"));

                provider.setActive(rs.getInt("lab07c1") == 1);
                provider.setApplyParticular(rs.getInt("lab906c19") == 1);

                return provider;
            });
        }
        catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }

    /**
     * Actualiza la información de un emisor en la base de datos.
     *
     * @param provider Instancia con los datos del emisor.
     *
     * @return Objeto del provider modificada.
     * @throws Exception Error en la base de datos.
     */
    default Provider update(Provider provider) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        getJdbcTemplate().update("UPDATE lab906 SET lab906c2 = ?, "
                + "lab906c3 = ?, "
                + "lab906c4 = ?, "
                + "lab906c5 = ?, "
                + "lab906c6 = ?, "
                + "lab906c7 = ?, "
                + "lab906c8 = ?, "
                + "lab906c9 = ?, "
                + "lab04c1 = ?, "
                + "lab07c1 = ?, "
                + "lab906c10 = ?, "
                + "lab906c11 = ?, "
                + "lab906c12 = ?, "
                + "lab906c13 = ?, "
                + "lab906c14 = ?, "
                + "lab906c15 = ?, "
                + "lab906c16 = ?, "
                + "lab906c17 = ?, "
                + "lab906c18 = ?, "
                + "lab906c19 = ? "
                + "WHERE lab906c1 = ?",
                provider.getNit(), 
                provider.getName(), 
                provider.getAddress(), 
                provider.getPhone(), 
                provider.getResponsable(), 
                provider.getCode(), 
                provider.getPostalCode(), 
                timestamp, 
                provider.getUser().getId(), 
                provider.isActive() ? 1 : 0, 
                provider.getTaxRegimen(),
                provider.getCurrentNumber(),
                provider.getState(),
                provider.getMunicipality(),
                provider.getNameElectronicInvoicing(),
                provider.getElectronicBillingPhone(),
                provider.getPrivateKey(),
                provider.getCertificate(),
                provider.getPassword(),
                provider.getApplyParticular() ? 1 : 0,
                provider.getId());

        provider.setLastTransaction(timestamp);

        return provider;
    }
    
    
     /**
     * Obtener información de un emisor por un campo especifico.
     *
     * @return Instancia con los datos del emisor.
     * @throws Exception Error en la base de datos.
     */
    default Provider getProviderParticular() throws Exception
    {
        try
        {
            return getJdbcTemplate().queryForObject(""
                    + "SELECT lab906c1, "
                    + "lab906c2, "
                    + "lab906c3, "
                    + "lab906c4, "
                    + "lab906c5, "
                    + "lab906c6, "
                    + "lab906c7, "
                    + "lab906c8, "
                    + "lab906c9, "
                    + "lab906.lab07c1, "
                    + "lab906c10 AS taxRegimen, "
                    + "lab906c11 AS currentNumber, "
                    + "lab906c12 AS state, "
                    + "lab906c13 AS municipality, "
                    + "lab906c14 AS nameElectronicInvoicing, "
                    + "lab906c15 AS electronicBillingPhone, "
                    + "lab906c16 AS privateKey, "
                    + "lab906c17 AS certificate, "
                    + "lab906c18 AS password, "
                    + "lab906c19, "
                    + "lab906.lab04c1, lab04c2, lab04c3, lab04c4 "
                    + "FROM lab906 "
                    + "LEFT JOIN lab04 ON lab04.lab04c1 = lab906.lab04c1 "
                    + "WHERE LAB906c19 = 1", (ResultSet rs, int i) ->
            {
                Provider provider = new Provider();
                provider.setId(rs.getInt("lab906c1"));
                provider.setNit(rs.getString("lab906c2"));
                provider.setName(rs.getString("lab906c3"));
                provider.setAddress(rs.getString("lab906c4"));
                provider.setPhone(rs.getString("lab906c5"));
                provider.setResponsable(rs.getString("lab906c6"));
                provider.setCode(rs.getString("lab906c7"));
                provider.setPostalCode(rs.getString("lab906c8"));
                provider.setLastTransaction(rs.getTimestamp("lab906c9"));
                provider.setTaxRegimen(rs.getString("taxRegimen"));
                provider.setCurrentNumber(rs.getInt("currentNumber"));
                provider.setState(rs.getInt("state"));
                provider.setMunicipality(rs.getInt("municipality"));
                provider.setNameElectronicInvoicing(rs.getString("nameElectronicInvoicing"));
                provider.setElectronicBillingPhone(rs.getString("electronicBillingPhone"));
                provider.setPrivateKey(rs.getString("privateKey"));
                provider.setCertificate(rs.getString("certificate"));
                provider.setPassword(rs.getString("password"));
                provider.setApplyParticular(rs.getInt("lab906c19") == 1);
                /*Usuario*/
                provider.getUser().setId(rs.getInt("lab04c1"));
                provider.getUser().setName(rs.getString("lab04c2"));
                provider.getUser().setLastName(rs.getString("lab04c3"));
                provider.getUser().setUserName(rs.getString("lab04c4"));

                provider.setActive(rs.getInt("lab07c1") == 1);

                return provider;
            });
        }
        catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
       
    }

    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplate();

}
