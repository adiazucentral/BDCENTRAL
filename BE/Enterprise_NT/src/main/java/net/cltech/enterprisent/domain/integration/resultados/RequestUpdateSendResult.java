package net.cltech.enterprisent.domain.integration.resultados;

import lombok.Getter;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Esta clase representa la petición con los datos requeridos
 * para la consulta a lab57 para la interfaz de resultados
 * 
 * @version 1.0.0
 * @author javila
 * @since 3/03/2020
 * @see Creación
 */
@ApiObject(
        group = "Integración",
        name = "Actualización de resultados",
        description = "Actualización del campo de envio al HIS y la fecha instantanea de la actualización del resultado"
)
@Getter
@Setter
public class RequestUpdateSendResult
{
    @ApiObjectField(name = "order", description = "Id de la orden", required = true, order = 1)
    private long order;
    @ApiObjectField(name = "centralCode", description = "Codigo central", required = true, order = 2)
    private String centralCode;
    @ApiObjectField(name = "state", description = "Estado del envio al HIS", required = true, order = 3)
    private int state;
    @ApiObjectField(name = "testId", description = "Id del examen que sera enviado al HIS", required = true, order = 4)
    private Integer testId;

    public RequestUpdateSendResult()
    {
    }

    public RequestUpdateSendResult(long order, String centralCode, int state)
    {
        this.order = order;
        this.centralCode = centralCode;
        this.state = state;
    }
}
