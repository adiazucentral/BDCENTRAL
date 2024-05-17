package net.cltech.enterprisent.service.interfaces.masters.tracking;

import java.util.List;
import net.cltech.enterprisent.domain.masters.tracking.Rack;
import net.cltech.enterprisent.domain.operation.common.RackFilter;
import net.cltech.enterprisent.domain.operation.tracking.RackDetail;
import net.cltech.enterprisent.domain.operation.tracking.SampleStore;

/**
 * Interfaz de servicios a la informacion del maestro Gradillas
 *
 * @version 1.0.0
 * @author eacuna
 * @since 28/05/2018
 * @see Creación
 */
public interface RackService
{

    /**
     * Lista gradillas desde la base de datos.
     *
     * @return Lista de gradillas.
     * @throws Exception Error en la base de datos.
     */
    public List<Rack> list() throws Exception;

    /**
     * Registra gradillas en la base de datos.
     *
     * @param create Instancia con los datos del gradillas.
     *
     * @return Instancia con los datos del gradillas.
     * @throws Exception Error en la base de datos.
     */
    public Rack create(Rack create) throws Exception;

    /**
     * Obtener información de gradillas por un campo especifico.
     *
     * @param id ID de gradillas a consultar.
     *
     * @return Instancia con los datos del gradillas.
     * @throws Exception Error en la base de datos.
     */
    public Rack filterById(Integer id) throws Exception;

    /**
     * Obtener información de gradillas por un campo especifico.
     *
     * @param name name de gradillas a consultar.
     *
     * @return Instancia con los datos del gradillas.
     * @throws Exception Error en la base de datos.
     */
    public Rack filterByName(String name) throws Exception;

    /**
     * Obtener información de gradillas por una varios filtros.
     *
     * @param filter Propiedades para filtrar
     *
     * @return Instancia con los datos del gradillas.
     * @throws Exception Error en la base de datos.
     */
    public List<Rack> filterBy(RackFilter filter) throws Exception;

    /**
     * Lista gradillas por un campo especifico.
     *
     * @param branchId id de la sede
     *
     * @return Instancia con los datos del gradillas.
     * @throws Exception Error en la base de datos.
     */
    public List<Rack> listByBranch(Integer branchId) throws Exception;

    /**
     * Actualiza la información de un gradillas en la base de datos.
     *
     * @param update Instancia con los datos del gradillas.
     *
     * @return Objeto de gradillas modificada.
     * @throws Exception Error en la base de datos.
     */
    public Rack update(Rack update) throws Exception;

    /**
     * Almacena una muestra validando las gradillas enviadas
     *
     * @param store bean con la informacion para realizar el almacenamiento
     * @return {id}|{pos}|{isFull} <br> Informacion del almacenamiento id de la
     * gradilla, posicion de almacenamiento y si la gradilla esta llena.
     * @throws Exception Error en servicio
     */
    public RackDetail store(SampleStore store) throws Exception;

    /**
     * Almecena una muestra retirada previamente
     *
     * @param store informacion para lamacenar la muestra
     * @return Detalle del almacenamiento
     * @throws Exception Error en el servicio
     */
    public RackDetail storeOld(SampleStore store) throws Exception;

    /**
     * Lista las muestras en una gradilla
     *
     * @param rack id de la gradilla
     * @return Lista de muestras en la gradilla
     * @throws Exception
     */
    public List<RackDetail> listRackDetail(int rack) throws Exception;

    /**
     * Lista la ubicacion de la muestra enviada
     *
     * @param order número de orden
     * @param sampleCode código de la muestra
     * @return Lista la ubicacion de la muestra
     * @throws Exception Error en el servicio
     */
    public List<RackDetail> findSample(long order, String sampleCode) throws Exception;

    /**
     * Obtiene informacion del almacenamiento de la muestra
     *
     * @param rackId id de la gradilla
     * @param position posicion de la muestra
     * @return
     * @throws Exception Error en el servicio
     */
    public RackDetail getSampleDetail(int rackId, String position) throws Exception;

    /**
     * Cierra la gradilla almacenandola en una nevera
     *
     * @param rack informacion para cerrrar la gradilla (id, nevera y piso)
     * @throws Exception Error en la base de datos.
     */
    public void close(Rack rack) throws Exception;

    /**
     * Marca como desechadas las gradillas enviadas
     *
     * @param racks lista de id´s de las gradillas desechadas
     * @throws Exception error en el servicio
     */
    public void dispouse(List<Integer> racks) throws Exception;

    /**
     * Retira la muestra de la gradilla
     *
     * @param store informacion de la grdilla y la posicion a retirar
     * @return registros afectados
     * @throws Exception Error
     */
    public int removeSample(SampleStore store) throws Exception;

    /**
     * Generacion de cadena para imprimir código de barras EPL
     *
     * @param rack informacion gradilla
     * @return Comando EPL
     * @throws Exception
     */
    public String rackBarcode(Rack rack) throws Exception;

    /**
     * Lista gradillas candidatas para ser desechadas.
     *
     * @return Lista de gradillas.
     * @throws Exception Error en la base de datos.
     */
    public List<Rack> listRacksToDiscard() throws Exception;
    
      /**
     * Almacena una muestra validando las gradillas enviadas con el codigo
     *
     * @param store bean con la informacion para realizar el almacenamiento
     * @return {id}|{pos}|{isFull} <br> Informacion del almacenamiento id de la
     * gradilla, posicion de almacenamiento y si la gradilla esta llena.
     * @throws Exception Error en servicio
     */
    public RackDetail storecode(SampleStore store) throws Exception;

}
