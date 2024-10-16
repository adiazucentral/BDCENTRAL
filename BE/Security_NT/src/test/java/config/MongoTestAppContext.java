/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import com.mongodb.Mongo;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Clase que configura mongo para un contexto de pruebas
 *
 * @version 1.0.0
 * @author dcortes
 * @since 19/04/2017
 * @see Creaciòn
 */
@Configuration
@EnableMongoRepositories(basePackages = "net.cltech.securitynt.dao.impl.mongodb")
public class MongoTestAppContext extends AbstractMongoConfiguration
{

    @Override
    protected String getDatabaseName()
    {
        return "enterprise_nt";
    }

    @Override
    public Mongo mongo() throws Exception
    {
        return new Mongo("localhost", 27017);
    }
}
