/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.operation.pathology;

import java.util.List;
import net.cltech.enterprisent.domain.masters.pathology.Specimen;

/**
 * Interfaz de servicios a la informacion de las muestras de los casos de patología
 *
 * @version 1.0.0
 * @author omendez
 * @since 12/04/2021
 * @see Creación
 */
public interface SamplePathologyService 
{
    /**
    * Busca las muestras de un caso de patologia con su contenido
    *
    * @param id Id del caso
    * @return Instancia con las muestras del caso
    * @throws Exception Error en el servicio
    */
    public List<Specimen> getByCase(Integer id) throws Exception;
}
