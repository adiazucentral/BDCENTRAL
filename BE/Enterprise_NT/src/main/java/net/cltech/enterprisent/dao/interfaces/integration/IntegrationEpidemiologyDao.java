/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.interfaces.integration;

import java.util.List;
import net.cltech.enterprisent.domain.integration.epidemiology.EpidemiologicalEvents;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Representa la interfaz que contiene los metodos para el acceso de datos y sus implementaciones 
 * para acceder a la informacion de la base de datos de PostgreSQL y SQLServer
 *
 * @version 1.0.0
 * @author omendez
 * @since 09/08/2021
 * @see Creación
 */
public interface IntegrationEpidemiologyDao 
{
     /**
     * Obtiene la conexion a la base de datos
     * 
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplate();
    
    /**
     * Obtiene la lista de pruebas asociadas a la primera entrevista creada
     * 
     * @param area Id del area apidemiologica
     * @return 
     * @throws Exception Error al obtener las pruebas
     */
    public List<EpidemiologicalEvents> getTestsByArea(int area) throws Exception;
}
