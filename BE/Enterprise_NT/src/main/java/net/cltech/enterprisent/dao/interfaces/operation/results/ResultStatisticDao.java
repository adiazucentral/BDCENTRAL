/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.interfaces.operation.results;

import java.sql.ResultSet;
import net.cltech.enterprisent.domain.operation.results.ResultFilter;
import net.cltech.enterprisent.domain.operation.results.ResultStatistic;
import static net.cltech.enterprisent.tools.Constants.ISOLATION_READ_UNCOMMITTED;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Interfaz de acceso a datos para las órdenes en el módulo de registro de
 * resultados
 *
 * @version 1.0.0
 * @author jblanco
 * @since 02/07/2017
 * @see Creación
 */
public interface ResultStatisticDao
{

    /**
     * Obtiene la información de las estadísticas del procesamiento de los
     * resultados.
     *
     * @param resultFilter Filtro de
     * {@link net.cltech.enterprisent.domain.operation.results.ResultFilter}
     * órdenes aplicado por el usuario
     * @return
     * {@link net.cltech.enterprisent.domain.operation.results.ResultStatistic}
     * @throws Exception Error en la base de datos.
     */
    default ResultStatistic statistic(final ResultFilter resultFilter) throws Exception
    {
        try
        {
            String select = "" + ISOLATION_READ_UNCOMMITTED
                    + "SELECT    count(lab22c1) ";
            String from = ""
                    + " FROM     lab22 "
                    //BANNER: Revisar esta tabla en el modelo.
                    //+ " INNER JOIN lab159 ON lab159.lab22c1 = lab22.lab22c1"
                    + "";
            String where = ""
                    + " WHERE lab22.lab07C1 = 1  AND (lab22c19 = 0 or lab22c19 is null) "
                    //+ " WHERE lab159.lab07C1 = ? ";
                    + "";

            if (resultFilter != null)
            {
                where += " AND lab22c1 > ? ";
                where += " AND lab22c1 < ? ";
            } else
            {
                return null;
            }

            return getJdbcTemplate().queryForObject(select + from + where,
                    new Object[]
                    {
                        //ResultSampleState.CHECKED.getValue(),
                        resultFilter.getFirstOrder(),
                        resultFilter.getLastOrder()
                    }, (ResultSet rs, int i) ->
            {
                ResultStatistic bean = new ResultStatistic();
                bean.setOrderCount(rs.getInt(1));
                return bean;
            });
        } catch (EmptyResultDataAccessException ex)
        {
            ex.getMessage();
            return null;
        }
    }

    /**
     * Obtiene la conección a la base de datos
     *
     * @return jdbc
     */
    public JdbcTemplate getJdbcTemplate();
}
