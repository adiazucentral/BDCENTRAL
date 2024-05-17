package net.cltech.enterprisent.domain.integration.external.billing;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa el paciente que se enviará junto a la caja de la API de México
 * 
 * @version 1.0.0
 * @author javila
 * @since 12/07/2021
 * @see Creación
 */

@ApiObject(
        name = "Paciente De Facturación Externa",
        group = "Operación - Ordenes",
        description = "Representa el paciente que se enviará junto a la caja de la API de México"
)
@Getter
@Setter
@NoArgsConstructor
public class PatientExternalBillingApi
{
    @ApiObjectField(name = "Historia", description = "Historia del paciente (Número De Identificación)", required = true, order = 1)
    @JsonProperty("Historia")
    private String Historia;
    @ApiObjectField(name = "ApellidoPaterno", description = "Primer apellido", required = true, order = 2)
    @JsonProperty("ApellidoPaterno")
    private String ApellidoPaterno;
    @ApiObjectField(name = "ApellidoMaterno", description = "Segundo apellido", required = true, order = 3)
    @JsonProperty("ApellidoMaterno")
    private String ApellidoMaterno;
    @ApiObjectField(name = "Nombres", description = "Nombres", required = true, order = 4)
    @JsonProperty("Nombres")
    private String Nombres;
    @ApiObjectField(name = "FechaNacimiento", description = "Fecha de nacimiento", required = true, order = 5)
    @JsonProperty("FechaNacimiento")
    private String FechaNacimiento;
    @ApiObjectField(name = "CURP", description = "CURPS paciente", required = false, order = 6)
    @JsonProperty("CURP")
    private String CURP;
    @ApiObjectField(name = "Genero", description = "Genero del paciente", required = true, order = 7)
    @JsonProperty("Genero")
    private String Genero;
}
