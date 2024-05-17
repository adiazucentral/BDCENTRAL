package net.cltech.enterprisent.domain.integration.dashboard;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la informacion de un area para el Dashboard
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 14/08/2020
 * @see Creación
 */
@ApiObject(
        group = "DashBoard",
        name = "Test Valid Dashboard",
        description = "Examenes validados para dashboard"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Getter
public class TestValidDashboard
{

    @ApiObjectField(name = "id", description = "Id del area", required = true, order = 1)
    private List<DashBoardOpportunityTime> dashBoardOpportunityTime = new ArrayList<>();
    @ApiObjectField(name = "tableroValidacion", description = "envio de informacion al tablero de validacion", required = true, order = 2)
    private Boolean tableroValidacion;
    @ApiObjectField(name = "TableroTiempoDeOportunidad", description = "envio de informacion al tablero de TiempoDeOportunidad", required = true, order = 3)
    private Boolean TableroTiempoDeOportunidad;
    @ApiObjectField(name = "TableroSeguimientoDePruebas", description = "envio de informacion al tablero de Seguimiento Pruebas", required = true, order =4)
    private Boolean TableroSeguimientoDePruebas;
    @ApiObjectField(name = "uriValidate", description = "uriValidate", required = true, order = 5)
    private String uriValidate;
    @ApiObjectField(name = "uriOpportunityTime", description = "uriOpportunityTime", required = true, order = 6)
    private String uriOpportunityTime;
    @ApiObjectField(name = "uriTracing", description = "uriTracing", required = true, order = 7)
    private String uriTracing;
      

    public TestValidDashboard()
    {
    }
}
