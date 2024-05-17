package net.cltech.enterprisent.domain.integration.generalMinsa;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 *
 * @author hpoveda
 */
@ApiObject(
        group = "Integración (General)",
        name = "Order General",
        description = "Representa una orden general"
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GeneralOrder
{

    @ApiObjectField(name = "order", description = "Id de order", required = true, order = 1)
    private String order;
    @ApiObjectField(name = "type", description = "type order", required = true, order = 2)
    private String type;
    @ApiObjectField(name = "regist", description = "feha de regist ", required = true, order = 3)
    private Date regist;
    @ApiObjectField(name = "patient", description = "patient general", required = true, order = 4)
    private GeneralPatient patient;
    @ApiObjectField(name = "demographics", description = "demographics  general", required = true, order = 5)
    private List<GeneralDemographic> demographics = new ArrayList<>();
    @ApiObjectField(name = "result", description = "result general", required = true, order = 6)
    private List<GeneralResult> result = new ArrayList<>();
}
