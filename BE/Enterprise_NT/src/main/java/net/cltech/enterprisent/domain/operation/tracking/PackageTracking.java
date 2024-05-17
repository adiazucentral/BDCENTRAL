/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.tracking;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa los datos de la auditoria de los paquetes
 * 
 * @version 1.0.0
 * @author omendez
 * @since 09/03/2023
 * @see Creación
 */

@ApiObject(
        name = "Auditoria de los paquetes",
        group = "Trazabilidad",
        description = "Representa los datos de la auditoria de los paquetes"
)
@Getter
@Setter
@NoArgsConstructor
public class PackageTracking extends MasterAudit {
    
    @ApiObjectField(name = "id", description = "Id", required = false, order = 1)
    private Integer id;
    @ApiObjectField(name = "idPackage", description = "Id paquete", required = false, order = 2)
    private Integer idPackage;
    @ApiObjectField(name = "idChilds", description = "Id hijos", required = false, order = 3)
    private String idChilds;
    @ApiObjectField(name = "partial", description = "Si el paquete es completo o parcial", required = false, order = 4)
    private Integer partial;
    @ApiObjectField(name = "namePackage", description = "Nombre del paquete", required = false, order = 5)
    private String namePackage;
    @ApiObjectField(name = "childs", description = "Nombres de los hijos", required = false, order = 6)
    private List<String> childs;
    @ApiObjectField(name = "order", description = "Id orden", required = false, order = 7)
    private Long order;
    @ApiObjectField(name = "codePackage", description = "Código del paquete", required = false, order = 8)
    private String codePackage;
  
}
