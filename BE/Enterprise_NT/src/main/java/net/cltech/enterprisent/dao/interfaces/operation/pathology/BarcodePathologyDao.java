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
import net.cltech.enterprisent.domain.operation.pathology.BarcodePathologyDesigner;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la información del codigo de barras.
 *
 * @version 1.0.0
 * @author omendez
 * @since 10/05/2021
 * @see Creación
 */
public interface BarcodePathologyDao 
{
    public JdbcTemplate getJdbcTemplatePat();
    
    /**
     * Lista barcode desde la base de datos.
     *
     * @return Lista de sitios anatomicos.
     * @throws Exception Error en la base de datos.
     */
    default List<BarcodePathologyDesigner> list() throws Exception
    {
        try
        {
            return getJdbcTemplatePat().query(""
                    + "SELECT pat100c1, pat100c2, pat100c3, pat100c4, pat100c5 "
                    + "FROM pat100 ", (ResultSet rs, int i) ->
            {
                BarcodePathologyDesigner barcode = new BarcodePathologyDesigner();
                barcode.setId( rs.getInt("pat100c1") );
                barcode.setTemplate( rs.getString("pat100c2") );
                barcode.setVersion( rs.getInt("pat100c3") );
                barcode.setActive(rs.getInt("pat100c4") == 1 );
                barcode.setCommand(rs.getString("pat100c5") );
                return barcode;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return new ArrayList<>(0);
        }
    }
    
    /**
     * Barcode predeterminado para patologia
     *
     * @return Lista de datos del codigo de barras.
     * @throws Exception Error en la base de datos.
     */
    default List<BarcodePathologyDesigner> barcodePredetermined() throws Exception
    {
        try
        {
            return getJdbcTemplatePat().query(""
                    + "SELECT pat100c1, pat100c2, pat100c3, pat100c4, pat100c5  "
                    + "FROM pat100 WHERE pat100c4 = 1 ", (ResultSet rs, int i) ->
            {
                BarcodePathologyDesigner barcode = new BarcodePathologyDesigner();
                barcode.setId( rs.getInt("pat100c1") );
                barcode.setTemplate( rs.getString("pat100c2") );
                barcode.setVersion( rs.getInt("pat100c3") );
                barcode.setActive(rs.getInt("pat100c4") == 1 );
                barcode.setCommand(rs.getString("pat100c5") );
                return barcode;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            return null;
        }
    }
    
    /**
     * Registra barcode en la base de datos.
     *
     * @param barcode Instancia con los datos .
     *
     * @return Instancia con los datos .
     * @throws Exception Error en la base de datos.
     */
    default BarcodePathologyDesigner create(BarcodePathologyDesigner barcode) throws Exception
    {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplatePat())
                .withTableName("pat100")
                .usingGeneratedKeyColumns("pat100c1");

        HashMap parameters = new HashMap();
        parameters.put("pat100c2", barcode.getTemplate());
        parameters.put("pat100c3", barcode.getVersion());
        parameters.put("pat100c4", barcode.isActive() ? 1 : 0);
        parameters.put("pat100c5", barcode.getCommand());

        Number key = insert.executeAndReturnKey(parameters);
        barcode.setId(key.intValue());
        barcode.setActive(true);
        return barcode;
    }

    /**
     * Actualiza la información de barcode en la base de datos.
     *
     * @param barcode Instancia con los datos.
     *
     * @return Objeto modificado.
     * @throws Exception Error en la base de datos.
     */
    default BarcodePathologyDesigner update(BarcodePathologyDesigner barcode) throws Exception
    {
        getJdbcTemplatePat().update("UPDATE pat100 SET pat100c2 = ?, pat100c3 = ?, pat100c4 = ?, pat100c5 = ?  "
                + "WHERE pat100c1 = ?",
                barcode.getTemplate(), barcode.getVersion(), barcode.isActive() ? 1 : 0, barcode.getCommand(), barcode.getId());
        return barcode;
    }
    
}
