package net.cltech.enterprisent.domain.integration.dashboard;

import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Agregar una descripción de la clase
 * @version 1.0.0
 * @author nmolina
 * @since 20/08/2020
 * @see [Para cuando se crea una clase incluir la palabra Creación, en caso de que sea una modificación colocar los cambios. Se pueden usar varias veces esta etiqueta]
 */
public class DashBoardSample {
    @ApiObjectField(name = "id", description = "Id del demografico", order = 1)
    private Integer id;
    @ApiObjectField(name = "Code", description = "Codigo de la muestra", order = 2)
    private String code;
    @ApiObjectField(name = "Name", description = "Nombre de la muestra", order = 3)
    private String name;
    @ApiObjectField(name = "printable", description = "Opcion si imprime el cogido de barras", order = 3)
    private boolean printable;
    @ApiObjectField(name = "count", description = "Cantidad de stike", order = 4)
    private Integer count;

    public DashBoardSample(int id, String code, String name, boolean printable, int count)
    {
        this.id = id;
        this.code = code;
        this.name = name;
        this.printable = printable;
        this.count = count;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean isPrintable()
    {
        return printable;
    }

    public void setPrintable(boolean printable)
    {
        this.printable = printable;
    }

    public Integer getCount()
    {
        return count;
    }

    public void setCount(Integer count)
    {
        this.count = count;
    }
}
