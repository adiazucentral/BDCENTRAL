package net.cltech.enterprisent.domain.integration.ingreso;

import java.util.Date;
import java.util.List;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Esta clase representa la respuesta con los datos necesarios
 * para la impresion del codigo de barras
 * 
 * @version 1.0.0
 * @author javila
 * @since 20/02/2020
 * @see Creación
 */
@ApiObject(
        group = "Integración",
        name = "Codigo de barras",
        description = "Este sera cargado con los datos necesarios para la impresion del codigo de barras"
)
public class ResponseBarCode
{
    @ApiObjectField(name = "lab22C1", description = "Id de la orden", required = true, order = 1)
    private long lab22C1;
    @ApiObjectField(name = "lab21C2", description = "Codigo historia", required = true, order = 2)
    private String lab21C2;
    @ApiObjectField(name = "lab22C12", description = "Tipo de orden", required = true, order = 3)
    private String lab22C12;
    @ApiObjectField(name = "lab21C4C5", description = "Nombres y Apellidos del paciente", required = true, order = 4)
    private String lab21C4C5;
    @ApiObjectField(name = "lab21C6", description = "Sexo", required = true, order = 5)
    private String lab21C6;
    @ApiObjectField(name = "lab22C14", description = "Fecha de ingreso", required = true, order = 6)
    private String lab22C14;
    @ApiObjectField(name = "lab21C10", description = "Fecha de nacimiento del paciente", required = true, order = 7)
    private String lab21C10;
    @ApiObjectField(name = "edad", description = "La edad del paciente", required = true, order = 8)
    private String edad;
    @ApiObjectField(name = "clave", description = "Contraseña para la consulta web (Temporal -> 123)", required = true, order = 9)
    private String clave;
    @ApiObjectField(name = "homonimo", description = "", required = true, order = 10)
    private String homonimo;
    @ApiObjectField(name = "inicialesPaciente", description = "Llave de configuración", required = true, order = 11)
    private int inicialesPaciente;
    @ApiObjectField(name = "his", description = "Llave de configuración", required = true, order = 12)
    private int his;
    @ApiObjectField(name = "dataSample", description = "Llave de configuración", required = true, order = 13)
    private List<ResponseItemBarCode> dataSample;
    
    public ResponseBarCode()
    {
    }

    public ResponseBarCode(long lab22C1, String lab21C2, String lab22C12, String lab21C4C5, String lab21C6, String lab22C14, String lab21C10, String edad, String clave, String homonimo, int inicialesPaciente, int his, List<ResponseItemBarCode> dataSample)
    {
        this.lab22C1 = lab22C1;
        this.lab21C2 = lab21C2;
        this.lab22C12 = lab22C12;
        this.lab21C4C5 = lab21C4C5;
        this.lab21C6 = lab21C6;
        this.lab22C14 = lab22C14;
        this.lab21C10 = lab21C10;
        this.edad = edad;
        this.clave = clave;
        this.homonimo = homonimo;
        this.inicialesPaciente = inicialesPaciente;
        this.his = his;
        this.dataSample = dataSample;
    }

    public long getLab22C1()
    {
        return lab22C1;
    }

    public void setLab22C1(long lab22C1)
    {
        this.lab22C1 = lab22C1;
    }

    public String getLab21C2()
    {
        return lab21C2;
    }

    public void setLab21C2(String lab21C2)
    {
        this.lab21C2 = lab21C2;
    }

    public String getLab22C12()
    {
        return lab22C12;
    }

    public void setLab22C12(String lab22C12)
    {
        this.lab22C12 = lab22C12;
    }

    public String getLab21C4C5()
    {
        return lab21C4C5;
    }

    public void setLab21C4C5(String lab21C4C5)
    {
        this.lab21C4C5 = lab21C4C5;
    }

    public String getLab21C6()
    {
        return lab21C6;
    }

    public void setLab21C6(String lab21C6)
    {
        this.lab21C6 = lab21C6;
    }

    public String getLab22C14()
    {
        return lab22C14;
    }

    public void setLab22C14(String lab22C14)
    {
        this.lab22C14 = lab22C14;
    }

    public String getLab21C10()
    {
        return lab21C10;
    }

    public void setLab21C10(String lab21C10)
    {
        this.lab21C10 = lab21C10;
    }

    public String getEdad()
    {
        return edad;
    }

    public void setEdad(String edad)
    {
        this.edad = edad;
    }

    public String getClave()
    {
        return clave;
    }

    public void setClave(String clave)
    {
        this.clave = clave;
    }

    public String getHomonimo()
    {
        return homonimo;
    }

    public void setHomonimo(String homonimo)
    {
        this.homonimo = homonimo;
    }

    public int getInicialesPaciente()
    {
        return inicialesPaciente;
    }

    public void setInicialesPaciente(int inicialesPaciente)
    {
        this.inicialesPaciente = inicialesPaciente;
    }

    public int getHis()
    {
        return his;
    }

    public void setHis(int his)
    {
        this.his = his;
    }

    public List<ResponseItemBarCode> getDataSample()
    {
        return dataSample;
    }

    public void setDataSample(List<ResponseItemBarCode> dataSample)
    {
        this.dataSample = dataSample;
    }   
}
