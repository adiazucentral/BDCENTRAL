/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.outreach.domain.info;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion de la base de datos
 *
 * @version 1.0.0
 * @author dcortes
 * @since 05/04/2017
 * @see Creación
 */
@ApiObject(
        group = "Información",
        name = "DatabaseInfo",
        description = "Muestra informacion de la base de datos que usa el API"
)
public class DatabaseInfo
{

    @ApiObjectField(name = "name", description = "Nombre del motor de base de datos", required = true, order = 1)
    private String name;
    @ApiObjectField(name = "version", description = "Versión de la base de datos", required = true, order = 2)
    private String version;
    @ApiObjectField(name = "db", description = "Nombre de la base de datos", required = true, order = 1)
    private String db;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    /**
     * @return the db
     */
    public String getDb()
    {
        return db;
    }

    /**
     * @param db the db to set
     */
    public void setDb(String db)
    {
        this.db = db;
    }
}
