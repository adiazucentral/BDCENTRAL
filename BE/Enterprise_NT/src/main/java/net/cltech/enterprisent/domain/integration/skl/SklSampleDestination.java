package net.cltech.enterprisent.domain.integration.skl;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa los destinos de la muestra que 
 * seran enviados con el formato correcto a SKL
 * 
 * @version 1.0.0
 * @author Julian
 * @since 14/05/2020
 */

@ApiObject(
        group = "SKL",
        name = "Destino de la muestra",
        description = "Objeto de tipo destino de la muestra que se le enviara a SKL según lo requiera para su integración con NT"
)
public class SklSampleDestination
{
    @ApiObjectField(name = "idDestination", description = "Id del destino", required = true, order = 1)
    private int idDestination;
    @ApiObjectField(name = "name", description = "Nombre del destino", required = true, order = 2)
    private String name;
    @ApiObjectField(name = "destinationCode", description = "Codigo del destino", required = true, order = 3)
    private String destinationCode;
    @ApiObjectField(name = "destinationType", description = "Tipo de destino", required = true, order = 4)
    private int destinationType;
    @ApiObjectField(name = "idRoute", description = "id de la ruta", required = true, order = 5)
    private int idRoute;
    @ApiObjectField(name = "sampleId", description = "id de la muestra", required = true, order = 6)
    private int sampleId;

    public SklSampleDestination()
    {
    }

    public int getIdDestination()
    {
        return idDestination;
    }

    public void setIdDestination(int idDestination)
    {
        this.idDestination = idDestination;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDestinationCode()
    {
        return destinationCode;
    }

    public void setDestinationCode(String destinationCode)
    {
        this.destinationCode = destinationCode;
    }

    public int getDestinationType()
    {
        return destinationType;
    }

    public void setDestinationType(int destinationType)
    {
        this.destinationType = destinationType;
    }

    public int getIdRoute()
    {
        return idRoute;
    }

    public void setIdRoute(int idRoute)
    {
        this.idRoute = idRoute;
    }

    public int getSampleId()
    {
        return sampleId;
    }

    public void setSampleId(int sampleId)
    {
        this.sampleId = sampleId;
    }
}
