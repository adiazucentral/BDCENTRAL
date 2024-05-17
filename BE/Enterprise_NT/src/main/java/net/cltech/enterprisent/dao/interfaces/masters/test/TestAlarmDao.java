/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.interfaces.masters.test;

import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import net.cltech.enterprisent.domain.masters.test.TestAlarm;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Representa los métodos de acceso a base de datos para la información de las
 * Pruebas que estan relacionadas a la alarma desde ingreso de ordenes.
 *
 * @version 1.0.0
 * @author omendez
 * @since 16/12/2021
 * @see Creación
 */
public interface TestAlarmDao {
    
    /**
     * Obtiene la coneccion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplate();

    default int createTestAlarm(TestAlarm testAlarm) throws Exception
    {
        Timestamp timestamp = new Timestamp(new Date().getTime());

        String sql = "UPDATE lab39 SET  " 
                + " lab39c55 = ?,  "
                + " lab39c36 = ?, "
                + " lab04c1 = ? "
                + " WHERE lab39c1 = ? AND lab43c1 = ? ";

        List<Object[]> parameters = new LinkedList<>();

        testAlarm.getTests().forEach((test) -> {
            parameters.add(new Object[] {
                test.isTestalarm() == true ? 1 : 0,
                timestamp,
                testAlarm.getUser(),
                test.getId(),
                testAlarm.getIdArea()
            });
        });
        
        return getJdbcTemplate().batchUpdate(sql, parameters).length;
    } 
}
