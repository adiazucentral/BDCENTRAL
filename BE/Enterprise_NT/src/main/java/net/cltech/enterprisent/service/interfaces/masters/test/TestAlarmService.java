/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.masters.test;

import net.cltech.enterprisent.domain.masters.test.TestAlarm;

/**
 * Interfaz de servicios a la informacion del maestro alarma de examen desde ingreso de ordenes
 *
 * @version 1.0.0
 * @author omendez
 * @since 16/12/2021
 * @see Creación
 */
public interface TestAlarmService {
    
    /**
     * Registra los examenes que estan relacionados a la alarma desde ingreso de ordenes.
     *
     * @param testAlarm Instancia con la lista de examenes relacionados a la alarma.
     *
     * @return Instancia con la lista de examenes relacionados a la alarma.
     * @throws Exception Error en la base de datos.
     */
    public TestAlarm create(TestAlarm testAlarm) throws Exception;
}
