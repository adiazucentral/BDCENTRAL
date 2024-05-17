package net.cltech.enterprisent.domain.integration.middleware;

import java.util.ArrayList;
import java.util.List;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa el destino a seguir de una muestra y si esta tapada o no
 *
 * @author JDuarte
 * @version 1.0.0
 * @since 11/08/2020
 * @see Creación
 */
@ApiObject(
        group = "Middleware",
        name = "Muestra con su destino y tapa",
        description = "Representa una muestra"
)
public class DestinationCovered
{
    @ApiObjectField(name = "destinationNext", description = "Destino a verificar de la muestra", required = true, order = 1)
    private String destinationNext;
    @ApiObjectField(name = "cover", description = "Si la muestra esta tapada o no", required = true, order = 2)
    private boolean cover;
    @ApiObjectField(name = "totalVolume", description = "Volumen total de los examenes", required = true, order = 3)
    private Double totalVolume;
    @ApiObjectField(name = "totalVolumeLicuota", description = "Volumen total de los examenes que son licuotas", required = true, order = 4)
    private Double totalVolumeLicuota;
    @ApiObjectField(name = "alicuotaWithVolum", description = "Alicuota con volumen, separados por coma", required = true, order = 5)
    private List<AlicuotaWithVolum> alicuotaWithVolum;
    
    public DestinationCovered()
    {
        totalVolume = 0.0;
        totalVolumeLicuota = 0.0;
        alicuotaWithVolum = new ArrayList<>();
    }

    public String getDestinationNext()
    {
        return destinationNext;
    }

    public void setDestinationNext(String destinationNext)
    {
        this.destinationNext = destinationNext;
    }

    public boolean isCover()
    {
        return cover;
    }

    public void setCover(boolean cover)
    {
        this.cover = cover;
    }

    public Double getTotalVolume()
    {
        return totalVolume;
    }

    public void setTotalVolume(Double totalVolume)
    {
        this.totalVolume = totalVolume;
    }

    public Double getTotalVolumeLicuota()
    {
        return totalVolumeLicuota;
    }

    public void setTotalVolumeLicuota(Double totalVolumeLicuota)
    {
        this.totalVolumeLicuota = totalVolumeLicuota;
    }

    public List<AlicuotaWithVolum> getAlicuotaWithVolum()
    {
        return alicuotaWithVolum;
    }

    public void setAlicuotaWithVolum(List<AlicuotaWithVolum> alicuotaWithVolum)
    {
        this.alicuotaWithVolum = alicuotaWithVolum;
    }
}
