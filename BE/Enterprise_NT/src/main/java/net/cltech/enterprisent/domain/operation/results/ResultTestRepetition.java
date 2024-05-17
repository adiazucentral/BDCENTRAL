package net.cltech.enterprisent.domain.operation.results;

import java.util.Date;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la repetición o modificación del resultado de un examen
 *
 * @version 1.0.0
 * @author jblanco
 * @since Mar 2, 2018
 * @see Creación
 */
@ApiObject(
        group = "Operación - Resultados",
        name = "Repetición Resultados Examen",
        description = "Representa la repetición o modificación de un resultado de examen para el registro de resultados"
)
public class ResultTestRepetition
{

    @ApiObjectField(name = "order", description = "Número de orden", required = true, order = 1)
    private long order;
    @ApiObjectField(name = "testId", description = "Identificador del examen", required = true, order = 2)
    private int testId;
    @ApiObjectField(name = "type", description = "Tipo de registro (Modificación o Repetición)", required = true, order = 3)
    private char type;
    @ApiObjectField(name = "reasonId", description = "Identificador del motivo de las repeticiones", required = true, order = 4)
    private int reasonId;
    @ApiObjectField(name = "reason", description = "Motivo de repetición", required = true, order = 5)
    private String reason;
    @ApiObjectField(name = "reasonComment", description = "Justificación de la repetición", required = true, order = 6)
    private String reasonComment;
    @ApiObjectField(name = "repetitionDate", description = "Fecha del comentario del resultado", required = true, order = 7)
    private Date repetitionDate;
    @ApiObjectField(name = "resultUser", description = "Usuario que realizo el resultado", required = true, order = 8)
    private AuthorizedUser resultUser;
    @ApiObjectField(name = "repeatUser", description = "Usuario que realiza la repetición o modificación", required = true, order = 9)
    private AuthorizedUser repeatUser;
    @ApiObjectField(name = "result", description = "Resultado", required = true, order = 10)
    private String result;
    @ApiObjectField(name = "resultDate", description = "Fecha del resultado anterior", required = true, order = 11)
    private Date resultDate;
    @ApiObjectField(name = "resultPathology", description = "Patología del resultado anterios", required = true, order = 12)
    private Integer resultPathology;

    public ResultTestRepetition()
    {
    }

    public long getOrder()
    {
        return order;
    }

    public void setOrder(long order)
    {
        this.order = order;
    }

    public int getTestId()
    {
        return testId;
    }

    public void setTestId(int testId)
    {
        this.testId = testId;
    }

    public char getType()
    {
        return type;
    }

    public void setType(char type)
    {
        this.type = type;
    }

    public int getReasonId()
    {
        return reasonId;
    }

    public void setReasonId(int reasonId)
    {
        this.reasonId = reasonId;
    }

    public String getReasonComment()
    {
        return reasonComment;
    }

    public void setReasonComment(String reasonComment)
    {
        this.reasonComment = reasonComment;
    }

    public Date getRepetitionDate()
    {
        return repetitionDate;
    }

    public void setRepetitionDate(Date repetitionDate)
    {
        this.repetitionDate = repetitionDate;
    }

    public AuthorizedUser getResultUser()
    {
        return resultUser;
    }

    public void setResultUser(AuthorizedUser resultUser)
    {
        this.resultUser = resultUser;
    }

    public AuthorizedUser getRepeatUser()
    {
        return repeatUser;
    }

    public void setRepeatUser(AuthorizedUser repeatUser)
    {
        this.repeatUser = repeatUser;
    }

    public String getReason()
    {
        return reason;
    }

    public void setReason(String reason)
    {
        this.reason = reason;
    }

    public String getResult()
    {
        return result;
    }

    public void setResult(String result)
    {
        this.result = result;
    }

    public Date getResultDate()
    {
        return resultDate;
    }

    public void setResultDate(Date resultDate)
    {
        this.resultDate = resultDate;
    }

    public Integer getResultPathology()
    {
        return resultPathology;
    }

    public void setResultPathology(Integer resultPathology)
    {
        this.resultPathology = resultPathology;
    }

}
