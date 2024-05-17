/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.impl.postgresql.integration.sendOrderExternalLIS;

import net.cltech.enterprisent.dao.interfaces.integration.sendOrderExternalLIS.IntegrationTStoWEBDao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 *
 * @author hpoveda
 */
@Repository
public class IntegrationTStoWEBDaoPostgreSQL implements IntegrationTStoWEBDao
{

    private JdbcTemplate jdbc;

    @Override
    public JdbcTemplate getJdbcTemplate()
    {
        return jdbc;
    }

}
