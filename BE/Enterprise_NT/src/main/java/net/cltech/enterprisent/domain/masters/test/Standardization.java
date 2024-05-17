package net.cltech.enterprisent.domain.masters.test;

import java.util.ArrayList;
import java.util.List;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la unidad para un examen
 *
 * @version 1.0.0
 * @author eacuna
 * @since 26/07/2017
 * @see Creación
 */
@ApiObject(
        group = "PRUEBA",
        name = "Homologación",
        description = "Muestra informacion de la homologacion"
)
public class Standardization extends TestBasic
{

    @ApiObjectField(name = "centralSystem", description = "Informacion del sistema central asignado", required = false, order = 1)
    private CentralSystem centralSystem;
    @ApiObjectField(name = "codes", description = "Lista de códigos homologados para un examen", required = true, order = 2)
    private List<String> codes;

    public Standardization()
    {
        centralSystem = new CentralSystem();
        codes = new ArrayList<>();
    }

    public CentralSystem getCentralSystem()
    {
        return centralSystem;
    }

    public void setCentralSystem(CentralSystem centralSystem)
    {
        this.centralSystem = centralSystem;
    }

    public List<String> getCodes()
    {
        return codes;
    }

    public void setCodes(List<String> codes)
    {
        this.codes = codes;
    }

}
