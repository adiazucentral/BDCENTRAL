package net.cltech.enterprisent.domain.operation.results;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Esta clase representa la actualización del resultado, y su respectivo examen
 * a modificar
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 05/02/2020
 * @see Creación
 */
@ApiObject(
        group = "Operación - Resultados",
        name = "Actualizacion del resultado",
        description = "Servicio encargado de la actualización de ordenes, según sea su número de orden y su id de examen"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateResult
{

    @ApiObjectField(name = "numberOrder", description = "Número de la orden", required = true, order = 1)
    private long numberOrder;
    @ApiObjectField(name = "oldExamIdentifier", description = "Es el id del examen que viene a ser modificado", required = true, order = 2)
    private int oldExamIdentifier;
    @ApiObjectField(name = "newExamIdentifier", description = "Es el id del examen que va a remplazar al antiguo id", required = true, order = 3)
    private int newExamIdentifier;

    public UpdateResult()
    {
    }

    public UpdateResult(long numberOrder, int oldExamIdentifier, int newExamIdentifier)
    {
        this.numberOrder = numberOrder;
        this.oldExamIdentifier = oldExamIdentifier;
        this.newExamIdentifier = newExamIdentifier;
    }

    public int getNewExamIdentifier()
    {
        return newExamIdentifier;
    }

    public void setNewExamIdentifier(int newExamIdentifier)
    {
        this.newExamIdentifier = newExamIdentifier;
    }

    public long getNumberOrder()
    {
        return numberOrder;
    }

    public void setNumberOrder(long numberOrder)
    {
        this.numberOrder = numberOrder;
    }

    public int getOldExamIdentifier() {
        return oldExamIdentifier;
    }

    public void setOldExamIdentifier(int oldExamIdentifier) {
        this.oldExamIdentifier = oldExamIdentifier;
    }

    

}
