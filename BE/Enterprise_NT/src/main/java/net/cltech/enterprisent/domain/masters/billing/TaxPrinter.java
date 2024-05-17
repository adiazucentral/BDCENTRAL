package net.cltech.enterprisent.domain.masters.billing;

import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro impresoras fiscaless
 *
 * @version 1.0.0
 * @author adiaz
 * @since 15/04/2021
 * @see Creación
 */
@ApiObject(
        group = "Facturacion",
        name = "Impresora fiscal",
        description = "Muestra informacion del maestro impresoras fiscales que usa el API"
)
public class  TaxPrinter extends MasterAudit
{

    @ApiObjectField(name = "id", description = "Id de la impresora fiscal", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "code", description = "codigo de la impresora fiscal", required = true, order = 2)
    private String code;
    @ApiObjectField(name = "name", description = "Nombre de la impresora fiscal", required = true, order = 2)
    private String name;
    @ApiObjectField(name = "pathXml", description = "Ruta donde se dejaran los archivos XML", required = true, order = 2)
    private String pathXml;
    @ApiObjectField(name = "clientIp", description = "Ip del cliente asociado a la impresora", required = true, order = 2)
    private String clientIp;
    @ApiObjectField(name = "state", description = "Estado del banco", required = true, order = 3)
    private boolean state;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPathXml() {
        return pathXml;
    }

    public void setPathXml(String pathXml) {
        this.pathXml = pathXml;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    

}
