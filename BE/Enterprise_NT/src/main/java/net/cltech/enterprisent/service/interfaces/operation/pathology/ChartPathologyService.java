/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.operation.pathology;

import java.util.List;
import net.cltech.enterprisent.domain.operation.pathology.ChartPathology;

/**
 * Interfaz de servicios a la informacion de las graficas utilizadas en patología
 *
 * @version 1.0.0
 * @author omendez
 * @since 22/07/2021
 * @see Creación
 */
public interface ChartPathologyService 
{
    /**
     * Obtiene las graficas de la seccion del procesador de tejidos
     * @return
     * {@link net.cltech.enterprisent.domain.operation.pathology.ChartPathology} null en caso de no encontrar resultados
     * @throws Exception Error en el servicio
     */
    public List<ChartPathology> getChartTissueProcessor() throws Exception;
}
