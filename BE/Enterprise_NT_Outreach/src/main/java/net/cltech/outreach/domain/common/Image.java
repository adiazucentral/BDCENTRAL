/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.outreach.domain.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la informacion de uan imagen
 *
 * @version 1.0.0
 * @author jrodriguez
 * @since 29/11/2018
 * @see Creación
 */
@ApiObject(
        group = "Imagen",
        name = "Imagen",
        description = "Datos correspondientes a la imagen"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Image
{

    @ApiObjectField(name = "cid", description = "Ide de la imagen", required = true, order = 1)
    private String cid;
    @ApiObjectField(name = "filename", description = "Nombre de la imagen", required = true, order = 2)
    private String filename;
    @ApiObjectField(name = "path", description = "Ruta de la imagen", required = true, order = 3)
    private String path;

    public String getCid()
    {
        return cid;
    }

    public void setCid(String cid)
    {
        this.cid = cid;
    }

    public String getFilename()
    {
        return filename;
    }

    public void setFilename(String filename)
    {
        this.filename = filename;
    }

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

}
