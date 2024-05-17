package net.cltech.enterprisent.domain.operation.results;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa un examen para el registro de resultados
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 07/04/2020
 * @see Creación
 */
@ApiObject(
        group = "Operación - Resultados",
        name = "Resultados",
        description = "Representa los perfiles de una orden"
)
public class PerfilOrder {

    @ApiObjectField(name = "profileId", description = "Identificador del perfil", required = true, order = 1)
    private int profileId;

    public PerfilOrder() {
    }

    public PerfilOrder(int profileId) {
        this.profileId = profileId;
    }

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

}
