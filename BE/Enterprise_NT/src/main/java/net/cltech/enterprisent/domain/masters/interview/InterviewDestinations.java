package net.cltech.enterprisent.domain.masters.interview;

/**
 * Representa la relacion de prueba y medio de cultivo
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 30/04/2020
 * @see Creacion
 */
import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.domain.masters.tracking.Destination;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

@ApiObject(
        name = "Entrevista",
        group = "Entrevista",
        description = "Objeto que representa la relación entrevista-Destinos"
)
public class InterviewDestinations {

    @ApiObjectField(name = "idInterview", description = "Id Examen", required = true, order = 1)
    private Integer idInterview;
    @ApiObjectField(name = "destination", description = "Medio de cultivo", required = true, order = 2)
    private List<Destination> destination;

    public InterviewDestinations() {

        destination = new ArrayList<>(0);
    }

    public Integer getIdInterview() {
        return idInterview;
    }

    public void setIdInterview(Integer idInterview) {
        this.idInterview = idInterview;
    }

    public List<Destination> getDestination() {
        return destination;
    }

    public void setDestination(List<Destination> destination) {
        this.destination = destination;
    }

}
