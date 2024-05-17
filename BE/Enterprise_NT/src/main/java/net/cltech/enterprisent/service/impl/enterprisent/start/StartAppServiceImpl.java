/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.impl.enterprisent.start;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import net.cltech.enterprisent.dao.interfaces.start.StartAppDao;
import net.cltech.enterprisent.service.interfaces.start.StartAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios de inicio de la aplicacion
 *
 * @version 1.0.0
 * @author dcortes
 * @since 12/04/2017
 * @see Creacion
 */
@Service
public class StartAppServiceImpl implements StartAppService
{

    @Autowired
    private StartAppDao dao;
    @Autowired
    private ServletContext servletContext;
    private static final Logger LOGGER = Logger.getLogger("EnterpriseNT - App");

    @Override
    public void execStartScript(String sqlEngine) throws Exception
    {
        File scriptFile = new File(servletContext.getRealPath(StartAppService.POSTGRESQL_PATH + "/script.sql"));
        String source = "Actualizaciòn";
        if (sqlEngine.equals(StartAppService.SQL_SERVER))
        {
            scriptFile = new File(servletContext.getRealPath(StartAppService.SQL_SERVER_PATH + "/script.sql"));
        }
        if (scriptFile.exists())
        {
            LOGGER.log(Level.INFO, "[EnterpriseNT - App] : Ejecutando el script de {0}", source);
            String script = readFile(scriptFile, source);
            if (!script.trim().isEmpty())
            {
                dao.execStartScript(script);
            } else
            {
                LOGGER.log(Level.SEVERE, "[EnterpriseNT - App] : El archivo de script de {0} encuentra vacio", source);
            }
        } else
        {
            LOGGER.log(Level.SEVERE, "[EnterpriseNT - App] : El archivo de script de actualizacion no ha sido encontrado");
        }
    }

    @Override
    public void execStartScriptData(String sqlEngine) throws Exception
    {
        File scriptFile = new File(servletContext.getRealPath(StartAppService.POSTGRESQL_PATH + "/scriptData.sql"));
        String source = "Actualización (Datos)";
        if (sqlEngine.equals(StartAppService.SQL_SERVER))
        {
            scriptFile = new File(servletContext.getRealPath(StartAppService.SQL_SERVER_PATH + "/scriptData.sql"));
        }
        if (scriptFile.exists())
        {
            LOGGER.log(Level.INFO, "[EnterpriseNT - App] : Ejecutando el script de {0}", source);
            String script = readFile(scriptFile, source);
            if (!script.trim().isEmpty())
            {
                dao.execStartScript(script);
            } else
            {
                LOGGER.log(Level.SEVERE, "[EnterpriseNT - App] : El archivo de script de {0} encuentra vacio", source);
            }
        } else
        {
            LOGGER.log(Level.SEVERE, "[EnterpriseNT - App] : El archivo de script de {0} no ha sido encontrado", source);
        }
    }

    @Override
    public void execStartScriptDocs(String sqlEngine) throws Exception
    {
        File scriptFile = new File(servletContext.getRealPath(StartAppService.POSTGRESQL_PATH + "/scriptDocs.sql"));
        String source = "Documentos";
        if (sqlEngine.equals(StartAppService.SQL_SERVER))
        {
            scriptFile = new File(servletContext.getRealPath(StartAppService.SQL_SERVER_PATH + "/scriptDocs.sql"));
        }
        if (scriptFile.exists())
        {
            LOGGER.log(Level.INFO, "[EnterpriseNT - App] : Ejecutando el script de {0}", source);
            String script = readFile(scriptFile, source);
            if (!script.trim().isEmpty())
            {
                dao.execStartScriptDocs(script);
            } else
            {
                LOGGER.log(Level.SEVERE, "[EnterpriseNT - App] : El archivo de script de {0} se encuentra vacio", source);
            }
        } else
        {
            LOGGER.log(Level.SEVERE, "[EnterpriseNT - App] : El archivo de script de {0} no ha sido encontrado", source);
        }
    }

    @Override
    public void execStartScriptStat(String sqlEngine) throws Exception
    {
        File scriptFile = new File(servletContext.getRealPath(StartAppService.POSTGRESQL_PATH + "/scriptEst.sql"));
        String source = "Estadísticas";
        if (sqlEngine.equals(StartAppService.SQL_SERVER))
        {
            scriptFile = new File(servletContext.getRealPath(StartAppService.SQL_SERVER_PATH + "/scriptEst.sql"));
        }
        if (scriptFile.exists())
        {
            LOGGER.log(Level.INFO, "[EnterpriseNT - App] : Ejecutando el script de {0}", source);
            if (scriptFile.exists())
            {
                LOGGER.log(Level.INFO, "[EnterpriseNT - App] : Ejecutando el script de {0}", source);
                String script = readFile(scriptFile, source);
                if (!script.isEmpty())
                {
                    dao.execStartScriptStat(script);
                    dao.execStartScriptStatAgile();
                } else
                {
                    LOGGER.log(Level.SEVERE, "[EnterpriseNT - App] : El archivo de script de {0} se encuentra vacio", source);
                }
            }
        } else
        {
            LOGGER.log(Level.SEVERE, "[EnterpriseNT - App] : El archivo de script de {0} no ha sido encontrado", source);
        }
    }

    @Override
    public void execStartScriptControl(String sqlEngine) throws Exception
    {
        File scriptFile = new File(servletContext.getRealPath(StartAppService.POSTGRESQL_PATH + "/scriptControl.sql"));
        String source = "Tablas de Control";
        if (sqlEngine.equals(StartAppService.SQL_SERVER))
        {
            scriptFile = new File(servletContext.getRealPath(StartAppService.SQL_SERVER_PATH + "/scriptControl.sql"));
        }
        if (scriptFile.exists())
        {
            LOGGER.log(Level.INFO, "[EnterpriseNT - App] : Ejecutando el script de {0}", source);
            if (scriptFile.exists())
            {
                LOGGER.log(Level.INFO, "[EnterpriseNT - App] : Ejecutando el script de {0}", source);
                String script = readFile(scriptFile, source);
                if (!script.isEmpty())
                {
                    dao.execStartScriptControl(script);
                } else
                {
                    LOGGER.log(Level.SEVERE, "[EnterpriseNT - App] : El archivo de script de {0} se encuentra vacio", source);
                }
            }
        } else
        {
            LOGGER.log(Level.SEVERE, "[EnterpriseNT - App] : El archivo de script de {0} no ha sido encontrado", source);
        }
    }

    @Override
    public void execStartScriptRepo(String sqlEngine) throws Exception
    {
        File scriptFile = new File(servletContext.getRealPath(StartAppService.POSTGRESQL_PATH + "/scriptReportHis.sql"));
        String source = "Reporte HIS";
        if (sqlEngine.equals(StartAppService.SQL_SERVER))
        {
            scriptFile = new File(servletContext.getRealPath(StartAppService.SQL_SERVER_PATH + "/scriptReportHis.sql"));
        }
        if (scriptFile.exists())
        {
            LOGGER.log(Level.INFO, "[EnterpriseNT - App] : Ejecutando el script de {0}", source);
            String script = readFile(scriptFile, source);
            if (!script.isEmpty())
            {
                dao.execStartScriptRep(script);
            } else
            {
                LOGGER.log(Level.SEVERE, "[EnterpriseNT - App] : El archivo de script de {0} se encuentra vacio", source);
            }
        } else
        {
            LOGGER.log(Level.SEVERE, "[EnterpriseNT - App] : El archivo de script de {0} no ha sido encontrado", source);
        }
    }

    @Override
    public void execStartScriptPat(String sqlEngine) throws Exception
    {
        File scriptFile = new File(servletContext.getRealPath(StartAppService.POSTGRESQL_PATH + "/scriptPat.sql"));
        String source = "Patología";
        if (sqlEngine.equals(StartAppService.SQL_SERVER))
        {
            scriptFile = new File(servletContext.getRealPath(StartAppService.SQL_SERVER_PATH + "/scriptPat.sql"));
        }
        if (scriptFile.exists())
        {
            LOGGER.log(Level.INFO, "[EnterpriseNT - App] : Ejecutando el script de {0}", source);
            String script = readFile(scriptFile, source);
            if (!script.isEmpty())
            {
                dao.execStartScriptPat(script);
            } else
            {
                LOGGER.log(Level.SEVERE, "[EnterpriseNT - App] : El archivo de script de {0} se encuentra vacio", source);
            }
        } else
        {
            LOGGER.log(Level.SEVERE, "[EnterpriseNT - App] : El archivo de script de {0} no ha sido encontrado", source);
        }
    }

    private String readFile(File scriptFile, String source) throws Exception
    {
        FileInputStream reader = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try
        {
            reader = new FileInputStream(scriptFile);
            isr = new InputStreamReader(reader, "UTF-8");
            br = new BufferedReader(isr);
            StringBuilder script = new StringBuilder();
            String line = br.readLine();
            while (line != null)
            {
                script.append(line).append("\n");
                line = br.readLine();
            }
            return script.toString();
        } catch (FileNotFoundException fileNotFoundException)
        {
            LOGGER.log(Level.SEVERE, "[EnterpriseNT - App] : El archivo de script de {0} no ha sido encontrado", source);
        } catch (IOException iOException)
        {
            LOGGER.log(Level.SEVERE, "[EnterpriseNT - App] : Error de lectura del archivo de script de {0}", source);
        } finally
        {
            if (br != null)
            {
                br.close();
            }
            if (reader != null)
            {
                reader.close();
            }
            if (isr != null)
            {
                isr.close();
            }
        }
        return "";
    }

    @Override
    public void executeAnyScript(String sqlEngine, String fileName, String operation) throws Exception
    {
        File scriptFile = new File(servletContext.getRealPath(StartAppService.POSTGRESQL_PATH + "/" + fileName));
        String source = operation;
        if (sqlEngine.equals(StartAppService.SQL_SERVER))
        {
            scriptFile = new File(servletContext.getRealPath(StartAppService.SQL_SERVER_PATH + "/" + fileName));
        }
        if (scriptFile.exists())
        {
            LOGGER.log(Level.INFO, "[EnterpriseNT - App] : Ejecutando el script de {0}", source);
            String script = readFile(scriptFile, source);
            if (!script.isEmpty())
            {
                if (fileName.contains("Stat"))
                {
                    dao.execStartScriptStat(script);
                } else
                {
                    dao.execStartScript(script);
                }
            } else
            {
                LOGGER.log(Level.SEVERE, "[EnterpriseNT - App] : El archivo de script de {0} se encuentra vacio", source);
            }
        } else
        {
            LOGGER.log(Level.SEVERE, "[EnterpriseNT - App] : El archivo de script de {0} no ha sido encontrado", source);
        }
    }

    @Override
    public void execStartScriptMaintenanceDBDaily() throws Exception
    {
        dao.execStartScriptMaintenanceDB(true, "", true);
    }

    @Override
    public void execScriptMaintenanceDBDaily() throws Exception
    {
        File scriptFile = new File(servletContext.getRealPath(StartAppService.SQL_SERVER_PATH + "/scriptMaintenanceDBDaily.sql"));
        String source = "Actualizaciòn";
        if (scriptFile.exists())
        {
            LOGGER.log(Level.INFO, "[EnterpriseNT - App] : Ejecutando el script de {0}", source);
            String script = readFile(scriptFile, source);
            if (!script.trim().isEmpty())
            {
                dao.execStartScriptMaintenanceDB(true, script, false);
            } else
            {
                LOGGER.log(Level.SEVERE, "[EnterpriseNT - App] : El archivo de script de {0} encuentra vacio", source);
            }
        } else
        {
            LOGGER.log(Level.SEVERE, "[EnterpriseNT - App] : El archivo de script de actualizacion no ha sido encontrado");
        }
    }

    @Override
    public void sendEmailAutomaticResult() throws Exception
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
