package net.cltech.enterprisent.domain.integration.resultados;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Esta clase representa la peticion de la interfaz de resultados
 * enviando los siguientes datos
 * 
 * @version 1.0.0
 * @since 10/03/2020
 * @author javila
 * @see Creación
 */
@ApiObject(
        group = "Integración",
        name = "Resultados de la orden",
        description = "Consultar orden con sus respectivos demograficos codificados, no codificados, dinamicos, y fijos"
)
@Getter
@Setter
@NoArgsConstructor
public class RequestOrdersResultados
{
    @ApiObjectField(name = "fromOrder", description = "Orden inicial", required = true, order = 1)
    private long fromOrder;
    @ApiObjectField(name = "untilOrder", description = "Orden final", required = true, order = 2)
    private long untilOrder;
    @ApiObjectField(name = "centralSystem", description = "Sistema central", required = true, order = 3)
    private int centralSystem;
    @ApiObjectField(name = "idAmbito", description = "Id del Ambito", required = true, order = 4)
    private Integer idAmbito;
    @ApiObjectField(name = "pacNum", description = "Id del pac número", required = true, order = 5)
    private Integer pacNum;
    @ApiObjectField(name = "cuentaKey", description = "Id de la cuenta", required = true, order = 6)
    private Integer cuentaKey;
    @ApiObjectField(name = "type", description = "tipo de ingreso de la orden 0 - interfaz, 1 - manual", required = true, order = 7)
    private Integer type;
    
    public RequestOrdersResultados(long fromOrder, long untilOrder, int centralSystem, int idAmbito, int pacNum, int cuentaKey)
    {
        this.fromOrder = fromOrder;
        this.untilOrder = untilOrder;
        this.centralSystem = centralSystem;
        this.idAmbito = idAmbito;
        this.pacNum = pacNum;
        this.cuentaKey = cuentaKey;
    }
    public RequestOrdersResultados(long fromOrder, long untilOrder, int centralSystem)
    {
        this.fromOrder = fromOrder;
        this.untilOrder = untilOrder;
        this.centralSystem = centralSystem;

    }
}
