
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.impl.mongodb;

import net.cltech.enterprisent.domain.common.Tracking;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Interfaz de acceso a datos sobre la auditoria
 *
 * @version 1.0.0
 * @author dcortes
 * @since 14/04/2017
 * @see Creación
 */
public interface TrackingDao extends MongoRepository<Tracking, String>
{
}