/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.interfaces.operation.pathology;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import net.cltech.enterprisent.domain.operation.pathology.Audio;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

/**
 * Implementa los servicios de los audios de patologia
 *
 * @version 1.0.0
 * @author omendez
 * @see 16/06/2021
 * @see Creaciòn
 */
public interface AudioDao 
{
    public JdbcTemplate getJdbcTemplatePat();
    
    default Audio create(Audio audio) {

        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplatePat())
                .withTableName("pat28")
                .usingColumns("pat28c2", "pat28c3", "pat28c4", "pat28c5", "lab04c1")
                .usingGeneratedKeyColumns("pat28c1");

        HashMap parameters = new HashMap();
        parameters.put("pat28c2", audio.getName());
        parameters.put("pat28c3", audio.getExtension());
        parameters.put("pat28c4", audio.getUrl());
        parameters.put("pat28c5", timestamp);
        parameters.put("lab04c1", audio.getUserCreated().getId());

        Number key = insert.executeAndReturnKey(parameters);
        audio.setId(key.intValue());
        audio.setCreatedAt(timestamp);
        return audio;
    }
    
    default Audio update(Audio audio) throws Exception {

        Timestamp timestamp = new Timestamp(new Date().getTime());

        getJdbcTemplatePat().update("UPDATE pat28 SET pat28c2 = ?, pat28c3 = ?, pat28c4 = ?, pat28c5 = ?, lab04c1 = ? "
                + " WHERE pat28c1 = ? ",
                audio.getName(), audio.getExtension(), audio.getUrl(), timestamp, audio.getUserUpdated().getId(), audio.getId());

        audio.setUpdatedAt(timestamp);

        return audio;
    }
}
