package net.cltech.enterprisent.domain.integration.generalMinsa;

import java.sql.Timestamp;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa un paciente general
 *
 * /**
 *
 * @author hpoveda
 * @since 2022-04-14
 * @see Creación
 */
@ApiObject(
        group = "Integración (General)",
        name = "Paciente General",
        description = "Representa un paciente general"
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GeneralPatient
{

    @ApiObjectField(name = "identification", description = "Identificación del paciente (Historia)", required = true, order = 1)
    private String identification;
    @ApiObjectField(name = "names", description = "Nombres", required = true, order = 2)
    private String names;
    @ApiObjectField(name = "lastName", description = "Apellidos", required = true, order = 3)
    private String lastName;
    @ApiObjectField(name = "sex", description = "Genero", required = true, order = 4)
    private String sex;
    @ApiObjectField(name = "birthDay", description = "Fecha de nacimiento", required = true, order = 5)
    private Timestamp birthDay;
    @ApiObjectField(name = "demographics", description = "Demográficos del paciente", required = true, order = 6)
    private List<GeneralDemographic> demographics;
}
