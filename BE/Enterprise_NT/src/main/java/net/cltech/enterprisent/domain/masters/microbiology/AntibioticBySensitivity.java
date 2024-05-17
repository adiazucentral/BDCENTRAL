package net.cltech.enterprisent.domain.masters.microbiology;

import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la combinación entre Antibiograma y antibiotico
 *
 * @author EAcuna
 * @version 1.0.0
 * @since 27/06/2017
 * @see Creación
 */
public class AntibioticBySensitivity
{

    @ApiObjectField(name = "id", description = "Id antibiograma", required = false, order = 6)
    private Integer id;
    @ApiObjectField(name = "selected", description = "Esta relacionado", required = true, order = 1)
    private boolean selected;
    @ApiObjectField(name = "line", description = "Linea de supresión", required = true, order = 2)
    private int line;
    @ApiObjectField(name = "unidad", description = "Unidad de medida", required = false, order = 4)
    private Integer unit;
    @ApiObjectField(name = "antibiotic", description = "Antibiotico", required = true, order = 5)
    private Antibiotic antibiotic;

    public AntibioticBySensitivity()
    {
        antibiotic = new Antibiotic();
    }

    public AntibioticBySensitivity(Integer id, int line, Integer unit, Antibiotic antibiotic)
    {
        this.id = id;
        this.line = line;
        this.unit = unit;
        this.antibiotic = antibiotic;
    }

    public boolean isSelected()
    {
        return selected;
    }

    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }

    public int getLine()
    {
        return line;
    }

    public void setLine(int line)
    {
        this.line = line;
    }

    public Integer getUnit()
    {
        return unit;
    }

    public void setUnit(Integer unit)
    {
        this.unit = unit;
    }

    public Antibiotic getAntibiotic()
    {
        return antibiotic;
    }

    public void setAntibiotic(Antibiotic antibiotic)
    {
        this.antibiotic = antibiotic;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

}
