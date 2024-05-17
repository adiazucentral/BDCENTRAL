/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.impl.mongodb;

import net.cltech.enterprisent.domain.exception.WebException;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Representa la persistencia del manejo de errores en la aplicacion
 *
 * @version 1.0.0
 * @author dcortes
 * @since 03/04/2017
 * @see Creacion
 */
public interface ErrorHandlerDao extends MongoRepository<WebException, String>
{

    public WebException findByCode(String code);
}
