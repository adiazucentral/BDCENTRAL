package net.cltech.enterprisent.domain.operation.statistic;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import net.cltech.enterprisent.domain.masters.opportunity.Bind;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa los tiempos entre estados de los resultados
 *
 * @version 1.0.0
 * @author eacuna
 * @since 19/02/2018
 * @see Creacion
 */
@ApiObject(
        group = "Estadisticas",
        name = "Histograma",
        description = "Representa datos para el histograma "
)
@JsonInclude(Include.NON_NULL)
public class Histogram
{

    @ApiObjectField(name = "mean", description = "Promedio", required = false, order = 2)
    private Double mean;
    @ApiObjectField(name = "median", description = "Mediana", required = false, order = 3)
    private Double median;
    @ApiObjectField(name = "mode", description = "Moda", required = false, order = 2)
    private double[] mode;
    @ApiObjectField(name = "standardDeviation", description = "Desviación estandar", required = false, order = 4)
    private Double standardDeviation;
    @ApiObjectField(name = "percentile25", description = "Percentil 25", required = false, order = 4)
    private Double percentile25;
    @ApiObjectField(name = "percentile75", description = "Percentil 75", required = false, order = 4)
    private Double percentile75;
    private List<Bind> binds;
    private List<HistogramData> detail;

    public Histogram()
    {

    }

    public Double getMean()
    {
        return mean;
    }

    public void setMean(Double mean)
    {
        this.mean = mean;
    }

    public Double getMedian()
    {
        return median;
    }

    public void setMedian(Double median)
    {
        this.median = median;
    }

    public double[] getMode()
    {
        return mode;
    }

    public void setMode(double[] mode)
    {
        this.mode = mode;
    }

    public Double getStandardDeviation()
    {
        return standardDeviation;
    }

    public void setStandardDeviation(Double standardDeviation)
    {
        this.standardDeviation = standardDeviation;
    }

    public Double getPercentile25()
    {
        return percentile25;
    }

    public void setPercentile25(Double percentile25)
    {
        this.percentile25 = percentile25;
    }

    public Double getPercentile75()
    {
        return percentile75;
    }

    public void setPercentile75(Double percentile75)
    {
        this.percentile75 = percentile75;
    }

    public List<Bind> getBinds()
    {
        return binds;
    }

    public void setBinds(List<Bind> binds)
    {
        this.binds = binds;
    }

    public List<HistogramData> getDetail()
    {
        return detail;
    }

    public void setDetail(List<HistogramData> detail)
    {
        this.detail = detail;
    }

}
