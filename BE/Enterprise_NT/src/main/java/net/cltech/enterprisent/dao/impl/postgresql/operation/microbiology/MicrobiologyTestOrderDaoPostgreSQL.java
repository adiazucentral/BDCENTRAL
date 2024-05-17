/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.impl.postgresql.operation.microbiology;

import javax.sql.DataSource;
import net.cltech.enterprisent.dao.interfaces.operation.microbiology.MicrobiologyTestOrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Realiza la implementación del acceso a datos a relacion orden muestra y
 * examen para relacion del medio de cultivo para PostgreSQL
 *
 * @version 1.0.0
 * @author equijano
 * @since 10/10/2019
 * @see Creación
 */
@Repository
public class MicrobiologyTestOrderDaoPostgreSQL implements MicrobiologyTestOrderDao
{

    private JdbcTemplate jdbc;

    @Autowired
    public void setDataSource(@Qualifier("dataSource") DataSource dataSource)
    {
        jdbc = new JdbcTemplate(dataSource);
    }

    @Override
    public JdbcTemplate getJdbcTemplate()
    {
        return jdbc;
    }

}
