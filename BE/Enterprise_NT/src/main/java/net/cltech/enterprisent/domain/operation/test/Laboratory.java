/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObjectField;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class Laboratory extends SuperLaboratory{
    @ApiObjectField(name = "address", description = "Direccion", required = false, order = 4)
    private String address;
    @ApiObjectField(name = "phone", description = "Telefono", required = false, order = 5)
    private String phone;
    @ApiObjectField(name = "contact", description = "Contacto", required = false, order = 6)
    private String contact;
    @ApiObjectField(name = "state", description = "Si esta activo", required = true, order = 7)
    private boolean state;
    @ApiObjectField(name = "type", description = "Tipo de laboratorio:<br>1-Interno<br>2-Externo", required = true, order = 8)
    private short type;
    @ApiObjectField(name = "path", description = "Ruta exportación archivos", required = false, order = 9)
    private String path;
    @ApiObjectField(name = "url", description = "Url del laboratorio", required = false, order = 10)
    private String url;
    @ApiObjectField(name = "entry", description = "Si es de ingreso", required = false, order = 11)
    private boolean entry;
    @ApiObjectField(name = "check", description = "Si es de verificacion", required = false, order = 12)
    private boolean check;
    @ApiObjectField(name = "middleware", description = "Si envía a Middleware externo:<br>1 - Si<br>2 - No", required = false, order = 13)
    private boolean middleware;
    @ApiObjectField(name = "winery", description = "Bodega", required = false, order = 14)
    private Integer winery;
}
