/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.interfaces.operation.pathology;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.cltech.enterprisent.domain.operation.pathology.TissueProcessor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import net.cltech.enterprisent.tools.Constants;

/**
 * Representa los métodos de acceso a base de datos para la información del procesador de los casetes de las muestras de un caso
 *
 * @version 1.0.0
 * @author omendez
 * @since 19/07/2021
 * @see Creación
 */
public interface TissueProcessorDao 
{
    public JdbcTemplate getJdbcTemplatePat();
   
    /**
     * Obtiene el dao de casetes
     *
     * @return Instancia de CommentDao
     */
    public SampleCaseteDao getSampleCaseteDao();
    
    /**
    * Guarda la información del procesador de los casetes de las muestras
    *
    * @param list Lista de {@link net.cltech.enterprisent.domain.operation.pathology.TissueProcessor}
    *
    * @throws Exception Error en base de datos
    */
    default int create(List<TissueProcessor> list) throws Exception
    {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(getJdbcTemplatePat())
                .withTableName("pat33")
                .usingGeneratedKeyColumns("pat33c1");
        
        List<HashMap<String, Object>> batch = new ArrayList<>(0);
        HashMap<String, Object> parameters = null;
        
        for(TissueProcessor tissueProcessor: list) 
        {
            parameters = new HashMap<>(0);
            parameters.put("pat32c1", tissueProcessor.getCasete());
            parameters.put("pat33c2", tissueProcessor.getHours());
            parameters.put("pat31c1", tissueProcessor.getTime().getId());
            batch.add(parameters);
            getSampleCaseteDao().changeCaseteStatus(tissueProcessor.getCasete(), Constants.TISSUEPROCESSOR_PROCESS);
        }
        return insert.executeBatch(batch.toArray(new HashMap[0])).length;
    }
}
