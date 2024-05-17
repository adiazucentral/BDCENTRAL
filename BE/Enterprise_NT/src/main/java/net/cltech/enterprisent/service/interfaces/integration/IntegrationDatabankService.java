package net.cltech.enterprisent.service.interfaces.integration;

import java.util.List;
import net.cltech.enterprisent.domain.operation.results.ResultTest;


/**
 * Interface para integracion de middleware
 *
 * @version 1.0.0
 * @author eacuna
 * @since 18/04/2018
 * @see Creación
 */
public interface IntegrationDatabankService
{
    /**
     * Inserta los resultados enviados por el databank
     *
     * @param resultTest listado de resultados
     * @throws Exception Error en el servicio
     */
    public void results(List<ResultTest> resultTest) throws Exception;
}
