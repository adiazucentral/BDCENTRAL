package net.cltech.enterprisent.domain.integration.ingresoLIH;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Interface para integracion de interfaz de ingreso
 *
 * @version 1.0.0
 * @author BValero
 * @since 23/04/2020
 * @see Creación
 */
@ApiObject(
        group = "Integración",
        name = "Paciente",
        description = "DatosGenerales del paciente"
)
public class Paciente {

    @ApiObjectField(name = "tipoIdentificacion", description = "Tipo de Identificacion", required = true, order = 1)
    private String tipoIdentificacion;
    @ApiObjectField(name = "numeroIdentificacion", description = "Numero de Identificacion", required = true, order = 2)
    private String numeroIdentificacion;
    @ApiObjectField(name = "nombres", description = "Nombres del paciente", required = true, order = 3)
    private String nombres;
    @ApiObjectField(name = "apellidos", description = "Apellidos del paciene", required = true, order = 4)
    private String apellidos;
    @ApiObjectField(name = "genero", description = "Genero", required = true, order = 5)
    private String genero;
    @ApiObjectField(name = "fechaNacimiento", description = "Fecha de Nacimiento", required = true, order = 5)
    private String fechaNacimiento;
    @ApiObjectField(name = "urgente", description = "Urgente", required = true, order = 5)
    private String urgente;
    @ApiObjectField(name = "enembarazo", description = "En embarazo", required = true, order = 5)
    private String enembarazo;
    
    public Paciente()
    {
    }

    public Paciente(String tipoIdentificacion, String numeroIdentificacion, String nombres, String apellidos, String genero, String fechaNacimiento, String urgente, String enembarazo)
    {
        this.tipoIdentificacion = tipoIdentificacion;
        this.numeroIdentificacion = numeroIdentificacion;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.genero = genero;
        this.fechaNacimiento = fechaNacimiento;
        this.urgente = urgente;
        this.enembarazo = enembarazo;
    }

    public String getTipoIdentificacion()
    {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(String tipoIdentificacion)
    {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public String getNumeroIdentificacion()
    {
        return numeroIdentificacion;
    }

    public void setNumeroIdentificacion(String numeroIdentificacion)
    {
        this.numeroIdentificacion = numeroIdentificacion;
    }

    public String getNombres()
    {
        return nombres;
    }

    public void setNombres(String nombres)
    {
        this.nombres = nombres;
    }

    public String getApellidos()
    {
        return apellidos;
    }

    public void setApellidos(String apellidos)
    {
        this.apellidos = apellidos;
    }

    public String getGenero()
    {
        return genero;
    }

    public void setGenero(String genero)
    {
        this.genero = genero;
    }

    public String getFechaNacimiento()
    {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento)
    {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getUrgente()
    {
        return urgente;
    }

    public void setUrgente(String urgente)
    {
        this.urgente = urgente;
    }

    public String getEnembarazo()
    {
        return enembarazo;
    }

    public void setEnembarazo(String enembarazo)
    {
        this.enembarazo = enembarazo;
    }
}
