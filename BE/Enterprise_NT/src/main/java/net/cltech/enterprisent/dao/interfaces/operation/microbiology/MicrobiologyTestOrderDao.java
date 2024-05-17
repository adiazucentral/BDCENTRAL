/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.interfaces.operation.microbiology;

import java.util.HashMap;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Representa los métodos de acceso a base de datos para la relacion orden
 * muestra y examen para relacion del medio de cultivo.
 *
 * @version 1.0.0
 * @author equijano
 * @since 10/10/2019
 * @see Creación
 */
public interface MicrobiologyTestOrderDao
{

    /**
     * Obtiene la conexion a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplate();

    /**
     * Obtiene el examen asociado a medios de cultivo de la muestra.
     *
     * @param idSample Id de la muestra.
     * @param order Numero de la orden
     * @param idTest Id del examen
     *
     * @return Examen.
     */
    default int create(Integer idSample, Long order, Integer idTest)
    {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplate())
                .withTableName("lab165");

        HashMap parameters = new HashMap();
        parameters.put("lab22c1", order);
        parameters.put("lab39c1", idTest);
        parameters.put("lab24c1", idSample);

        return insert.execute(parameters);
    }

}
