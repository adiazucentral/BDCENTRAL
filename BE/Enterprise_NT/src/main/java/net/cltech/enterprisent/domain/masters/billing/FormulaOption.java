package net.cltech.enterprisent.domain.masters.billing;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa opciónes para aplicar formulas a tarifas
 *
 * @version 1.0.0
 * @author eacuna
 * @since 17/08/217
 * @see Creación
 */
@ApiObject(
        group = "Facturacion",
        name = "Opciones Formula",
        description = "opciónes para aplicar formulas a tarifas"
)
public class FormulaOption
{

    @ApiObjectField(name = "from", description = "Id de la tarifa desde la cual se desea calcular", required = true, order = 1)
    private int from;
    @ApiObjectField(name = "to", description = "Id de la tarifa en la cual se guardara el calculo", required = true, order = 2)
    private int to;
    @ApiObjectField(name = "operator", description = "Operación que se desea realizar:<br> (+, - , *, /)", required = true, order = 3)
    private String operator;
    @ApiObjectField(name = "value", description = "Valor con el cual se va  a operar", required = true, order = 4)
    private BigDecimal value;
    @ApiObjectField(name = "roundMode", description = "Tipo de redondeo <br>0-Trunca<br>1-Redondeo hacia arriba", required = true, order = 5)
    private int roundMode;
    @ApiObjectField(name = "writeType", description = "Tipo de escritura <br>0-Total <br>1-Parcial", required = true, order = 6)
    private int writeType;
    @ApiObjectField(name = "feeSchedule", description = "Id de la vigencia", required = true, order = 7)
    private int feeSchedule;

    public FormulaOption()
    {
    }

    public int getFrom()
    {
        return from;
    }

    public void setFrom(int from)
    {
        this.from = from;
    }

    public int getTo()
    {
        return to;
    }

    public void setTo(int to)
    {
        this.to = to;
    }

    public String getOperator()
    {
        return operator;
    }

    public void setOperator(String operator)
    {
        this.operator = operator;
    }

    public BigDecimal getValue()
    {
        return value;
    }

    public void setValue(BigDecimal value)
    {
        this.value = value;
    }

    public int getRoundMode()
    {
        return roundMode;
    }

    public void setRoundMode(int roundMode)
    {
        this.roundMode = roundMode;
    }

    public int getWriteType()
    {
        return writeType;
    }

    public void setWriteType(int writeType)
    {
        this.writeType = writeType;
    }

    public int getFeeSchedule()
    {
        return feeSchedule;
    }

    public void setFeeSchedule(int feeSchedule)
    {
        this.feeSchedule = feeSchedule;
    }

    public static final List<String> operators = Arrays.asList("+", "-", "*", "/");
    public static final int WRITE_TOTAL = 0;
    public static final int WRITE_PARTIAL = 1;
}
