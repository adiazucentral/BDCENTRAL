package net.cltech.enterprisent.service.interfaces.tools.events;

import net.cltech.enterprisent.domain.operation.results.ResultTest;

/**
 * Interfaz de servicios a la informacion de Eventos Orden
 *
 * @version 1.0.0
 * @author equijano
 * @since 11/01/2019
 * @see Creación
 */
public interface EventsResultsService
{

    /**
     * Registrar el resultado del examen
     *
     * @param test
     */
    public void create(ResultTest test);

    /**
     * Actualiza el resultado del examen
     *
     * @param test
     */
    public void update(ResultTest test);

    /**
     * Repetir el resultado del examen
     *
     * @param test
     */
    public void rerun(ResultTest test);

    /**
     * Realiza la validación de una orden completa
     *
     * @param order orden
     */
    public void validateOrden(long order);

    /**
     * Realiza la validación de un resultado
     *
     * @param orderId
     * @param testId
     */
    public void validatedTest(Long orderId, int testId);

    /**
     * Realiza la impresion de un examen por id
     *
     * @param orderId
     * @param testId
     */
    public void printedTest(Long orderId, int testId);

    /**
     * Des-validar examen
     *
     * @param testId
     */
    public void invalidate(int testId);

}
