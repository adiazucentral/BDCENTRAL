package net.cltech.enterprisent.domain.DTO.integration.resultados;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.cltech.enterprisent.domain.integration.resultados.ResponseDetailMicroorganisms;
import net.cltech.enterprisent.domain.integration.resultados.ResponseOrderDetailResult;

/**
 * cLASE REDULTADO HIS CON ATIBIOGRAMA
 *
 * @author hpoveda
 */
@Getter
@Setter
//@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseOrderDetailResultHIS extends ResponseOrderDetailResult
{

    private List<ResponseDetailMicroorganisms> microorganisms;

    public ResponseOrderDetailResultHIS(ResponseOrderDetailResult detailResult)
    {
        super(detailResult);
    }

}
