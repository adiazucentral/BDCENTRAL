/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.reports;


import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import org.jsondoc.core.annotation.ApiObjectField;

@Getter
@Setter
public class DeliveryDetail {
    @ApiObjectField(name = "user", description = "Usuario", order = 4)
    private AuthorizedUser user = new AuthorizedUser();
    @ApiObjectField(name = "receivesPerson", description = "Persona que Recibe", order = 5)
    private String receivesPerson;
    @ApiObjectField(name = "date", description = "Fecha de Entrega", order = 6)
    private Date date;
    @ApiObjectField(name = "idTest", description = "Id de examen", order = 7)
    private Integer idTest;
    @ApiObjectField(name = "codeTest", description = "Codigo del examen", order = 7)
    private String codeTest;
    @ApiObjectField(name = "nameTest", description = "Nombre del examen", order = 7)
    private String nameTest;
    @ApiObjectField(name = "idProfile", description = "Id del perfil", order = 8)
    private Integer idProfile;
    @ApiObjectField(name = "codeProfile", description = "Codigo del perfil", order = 8)
    private String codeProfile;
    @ApiObjectField(name = "nameProfile", description = "Nombre del perfil", order = 8)
    private String nameProfile;
    @ApiObjectField(name = "idMediumDelivery", description = "medio de entrega", order = 9)
    private Integer idMediumDelivery;
    @ApiObjectField(name = "sendingType", description = "Tipo de envio", order = 10)
    private String sendingType;
    
}
