package net.cltech.enterprisent.service.interfaces.masters.test;

import java.util.List;
import net.cltech.enterprisent.domain.masters.test.Sample;
import net.cltech.enterprisent.domain.masters.test.SampleByService;

/**
 * Interfaz de servicios a la informacion del maestro de Muestra
 *
 * @version 1.0.0
 * @author enavas
 * @since 18/04/2017
 * @see Creación
 */
public interface SampleService
{

    /**
     * Lista las muestras desde la base de datos
     *
     * @return Lista de Muestras
     * @throws Exception Error en base de datos
     */
    public List<Sample> list() throws Exception;

    /**
     * Lista las muestras desde la base de datos
     *
     * @param state Estado de las muestras a ser consultadas
     * @return Lista de Muestras
     * @throws Exception Error en base de datos
     */
    public List<Sample> list(boolean state) throws Exception;

    /**
     * Registra una nueva Muestra en la base de datos
     *
     * @param sample Instancia con los datos del recipiente.
     *
     * @return Instancia con los datos de la muestra.
     * @throws Exception Error en base de datos
     */
    public Sample create(Sample sample) throws Exception;

    /**
     * Obtener informacion de la muestra en la base de datos
     *
     * @param id Id de la muestra.
     * @param name Nombre de la muestra.
     * @param code Codigo de la muestra.
     * @param container Codigo de el recipiente.
     * @param state Estado del recipiente.
     *
     * @return Instancia con los datos de la muestra.
     * @throws Exception Error en base de datos
     */
    public List<Sample> get(Integer id, String name, String code, Integer container, Boolean state) throws Exception;

    /**
     * Actualiza la informacion de la muestra en la base de datos
     *
     * @param sample Instancia con los datos de la muestra.
     * @return
     *
     * @throws Exception Error en base de datos
     */
    public Sample update(Sample sample) throws Exception;

    /**
     * Elimina una muestra en la base de datos
     *
     * @param id Id de la muestra consultada.
     *
     * @throws Exception Error en base de datos
     */
    public void delete(Integer id) throws Exception;

    /**
     * Lista las muestras de un servicio desde la base de datos.
     *
     * @param idService Id de la prueba en la que se va a hacer la consulta.
     *
     * @return Lista de pruebas que tienen concurrencias.
     * @throws java.lang.Exception
     */
    public List<SampleByService> listSampleByService(int idService) throws Exception;

    /**
     * Inserta muestras por servicio desde la base de datos.
     *
     * @param sample Objeto a ser ingresado.
     *
     * @return Objeto registrado.
     * @throws java.lang.Exception
     */
    public SampleByService insertSamplesByService(SampleByService sample) throws Exception;

    /**
     * Obtiene las muestra de los tipos enviados
     *
     * @param types tipos de muestras, id de tipos de laboratorios (listados =
     * 1) separados por coma(,)
     *
     * @return Muestras activas con los tipos enviados
     * @throws Exception Error en base de datos
     */
    public List<Sample> typeFilter(String types) throws Exception;

    /**
     * Obtiene las submuestras de una muestra
     *
     * @param sampleId id de la muestra
     *
     * @return lista de submuestras
     * @throws Exception Error de base de datos
     */
    public List<Sample> subSamples(int sampleId) throws Exception;
    
    /**
     * Obtiene las submuestras de una muestra
     *
     * @param sampleId id de la muestra
     *
     * @return lista de submuestras
     * @throws Exception Error de base de datos
     */
    public List<Sample> listSubSampleSelect(int sampleId) throws Exception;
    
    /**
     * Obtiene las muestras que pertenecen a microbiologia
     *
     *
     * @return lista de muestras 
     * @throws Exception Error de base de datos
     */
    public List<Sample> samplesMicrobiology() throws Exception;

    /**
     * Asigna submuestras a una muestra
     *
     * @param sample Entidad con la información de la relación
     *
     * @return numero de registros afectados
     * @throws Exception Error base de datos
     */
    public int assignSubSamples(Sample sample) throws Exception;
    
     /**
     * Asigna submuestras a una muestra
     *
     * @param sample Entidad con la información de la relación
     *
     * @return numero de registros afectados
     * @throws Exception Error base de datos
     */
    public Sample getTypeLaboratoryBySample(int sample) throws Exception;

}
