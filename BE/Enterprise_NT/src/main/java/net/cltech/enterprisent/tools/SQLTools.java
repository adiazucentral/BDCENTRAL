/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.tools;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;
import net.cltech.enterprisent.domain.masters.demographic.Demographic;
import net.cltech.enterprisent.domain.operation.list.FilterDemographic;
import org.joda.time.DateTime;
import java.util.Date;
import net.cltech.enterprisent.dao.interfaces.masters.test.LaboratorysByBranchesDao;
import net.cltech.enterprisent.service.interfaces.masters.test.LaboratorysByBranchesService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Metodos de utilidad de sql para toda la aplicacion
 *
 * @version 1.0.0
 * @author cmartin
 * @since 18/04/2018
 * @see Creación
 */
public class SQLTools {
    
    @Autowired
    private LaboratorysByBranchesDao laboratorysByBranchesDao;

    /**
     * Construir el filtro SQL
     *
     * @param demographic Filtros de Demograficos
     * @param params Parametros
     * @return Where de la consulta SQL
     */
    public static String buildSQLDemographicFilter(FilterDemographic demographic, List<Object> params) {
        String where = "";
        switch (demographic.getDemographic()) {
            case Constants.BRANCH:
                if (demographic.getDemographicItems() != null && !demographic.getDemographicItems().isEmpty()) {
                    if (demographic.getDemographicItems().size() == 1) {
                        where += " AND lab22.lab05c1 = ?";
                        params.add(demographic.getDemographicItems().get(0));
                    } else {
                        where += " AND lab22.lab05c1 in (" + demographic.getDemographicItems().stream().map(demo -> demo.toString()).collect(Collectors.joining(",")) + ") ";
                    }
                }
                break;
            case Constants.SERVICE:
                if (demographic.getDemographicItems() != null && !demographic.getDemographicItems().isEmpty()) {
                    if (demographic.getDemographicItems().size() == 1) {
                        where += " AND lab22.lab10c1 = ?";
                        params.add(demographic.getDemographicItems().get(0));
                    } else {
                        where += " AND lab22.lab10c1 in (" + demographic.getDemographicItems().stream().map(demo -> demo.toString()).collect(Collectors.joining(",")) + ") ";
                    }
                }
                break;
            case Constants.PHYSICIAN:
                if (demographic.getDemographicItems() != null && !demographic.getDemographicItems().isEmpty()) {
                    if (demographic.getDemographicItems().size() == 1) {
                        where += " AND lab22.lab19c1 = ?";
                        params.add(demographic.getDemographicItems().get(0));
                    } else {
                        where += " AND lab22.lab19c1 in (" + demographic.getDemographicItems().stream().map(demo -> demo.toString()).collect(Collectors.joining(",")) + ") ";
                    }
                }
                break;
            case Constants.ACCOUNT:
                if (demographic.getDemographicItems() != null && !demographic.getDemographicItems().isEmpty()) {
                    if (demographic.getDemographicItems().size() == 1) {
                        where += " AND lab22.lab14c1 = ?";
                        params.add(demographic.getDemographicItems().get(0));
                    } else {
                        where += " AND lab22.lab14c1 in (" + demographic.getDemographicItems().stream().map(demo -> demo.toString()).collect(Collectors.joining(",")) + ") ";
                    }
                }
                break;
            case Constants.RATE:
                if (demographic.getDemographicItems() != null && !demographic.getDemographicItems().isEmpty()) {
                    if (demographic.getDemographicItems().size() == 1) {
                        where += " AND lab22.lab904c1 = ?";
                        params.add(demographic.getDemographicItems().get(0));
                    } else {
                        where += " AND lab22.lab904c1 in (" + demographic.getDemographicItems().stream().map(demo -> demo.toString()).collect(Collectors.joining(",")) + ") ";
                    }
                }
                break;
            case Constants.RACE:
                if (demographic.getDemographicItems() != null && !demographic.getDemographicItems().isEmpty()) {
                    if (demographic.getDemographicItems().size() == 1) {
                        where += " AND lab21.lab08c1 = ?";
                        params.add(demographic.getDemographicItems().get(0));
                    } else {
                        where += " AND lab21.lab08c1 in (" + demographic.getDemographicItems().stream().map(demo -> demo.toString()).collect(Collectors.joining(",")) + ") ";
                    }
                }
                break;
            case Constants.DOCUMENT_TYPE:
                if (demographic.getDemographicItems() != null && !demographic.getDemographicItems().isEmpty()) {
                    if (demographic.getDemographicItems().size() == 1) {
                        where += " AND lab21.lab54c1 = ?";
                        params.add(demographic.getDemographicItems().get(0));
                    } else {
                        where += " AND lab21.lab54c1 in (" + demographic.getDemographicItems().stream().map(demo -> demo.toString()).collect(Collectors.joining(",")) + ") ";
                    }
                }
                break;
            case Constants.ORDERTYPE:
                if (demographic.getDemographicItems() != null && !demographic.getDemographicItems().isEmpty()) {
                    if (demographic.getDemographicItems().size() == 1) {
                        where += " AND lab22.lab103c1 = ?";
                        params.add(demographic.getDemographicItems().get(0));
                    } else {
                        where += " AND lab22.lab103c1 in (" + demographic.getDemographicItems().stream().map(demo -> demo.toString()).collect(Collectors.joining(",")) + ") ";
                    }
                }
                break;
            case Constants.PATIENT_SEX:
                if (demographic.getDemographicItems() != null && !demographic.getDemographicItems().isEmpty()) {
                    if (demographic.getDemographicItems().size() == 1) {
                        where += " AND lab21.lab80c1 = ?";
                        params.add(demographic.getDemographicItems().get(0));
                    } else {
                        where += " AND lab21.lab80c1 in (" + demographic.getDemographicItems().stream().map(demo -> demo.toString()).collect(Collectors.joining(",")) + ") ";
                    }
                }
                break;
            case Constants.ORDER_HIS:
                where += " AND lab22c7 = ?";
                params.add(demographic.getValue());
                break;
            case Constants.PATIENT_EMAIL:
                where += " AND lab21c8 = ?";
                params.add(demographic.getValue());
                break;
            case Constants.PATIENT_ID:
                where += " AND lab21c2 = ?";
                params.add(Tools.encrypt(demographic.getValue()));
                break;
            case Constants.PATIENT_LAST_NAME:
                where += " AND lab21c6 = ?";
                params.add(Tools.encrypt(demographic.getValue()));
                break;
            case Constants.PATIENT_SURNAME:
                where += " AND lab21c5 = ?";
                params.add(Tools.encrypt(demographic.getValue()));
                break;
            case Constants.PATIENT_NAME:
                where += " AND lab21c3 = ?";
                params.add(Tools.encrypt(demographic.getValue()));
                break;
            case Constants.PATIENT_SECOND_NAME:
                where += " AND lab21c4 = ?";
                params.add(Tools.encrypt(demographic.getValue()));
                break;
            case Constants.PATIENT_AGE:
                try {
                    DateTime datetime = new DateTime();
                    int year = Integer.parseInt(datetime.toString("YYYY"));
                    int month = Integer.parseInt(datetime.toString("MM"));
                    int day = Integer.parseInt(datetime.toString("dd"));
                    String operator = "";
                    switch (demographic.getOperator()) {
                        case "2":
                            operator = "<=";
                            break;
                        case "3":
                            operator = ">=";
                            break;
                        default:
                            break;
                    }
                    where += " AND lab21c7  " + operator + " ? ";
                    String date = "";
                    switch (demographic.getUnidAge())//edad 1 -> años, 2 -> meses, 3 -> dias
                    {
                        case "1":
                            date = demographic.getValue() + "/" + month + "/" + day;
                            break;
                        case "2":
                            date = year + "/" + demographic.getValue() + "/" + day;
                            break;
                        default:
                            date = year + "/" + month + "/" + demographic.getValue();
                            break;
                    }
                    
                    DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
                    Date startDate = (Date)formatter.parse(date);  
                    params.add(startDate);
                 
                } catch (Exception e) {
                }
                break;

            default:
                if (demographic.getDemographic() > 0) {
                    if (demographic.getDemographicItems() != null && !demographic.getDemographicItems().isEmpty()) {
                        if (demographic.getDemographicItems().size() == 1) {
                            where += " AND lab_demo_" + demographic.getDemographic() + " = ? ";
                            params.add(demographic.getDemographicItems().get(0));
                        } else {
                            where += " AND lab_demo_" + demographic.getDemographic() + " in (" + demographic.getDemographicItems().stream().map(demo -> demo.toString()).collect(Collectors.joining(",")) + ") ";
                        }
                    } else {
                        if (demographic.isPromiseTime()) {
                            where += " AND lab_demo_" + demographic.getDemographic() + " BETWEEN '" + demographic.getInitDate() + "' AND '" + demographic.getEndDate() + "'";
                        } else {
                            where += " AND lab_demo_" + demographic.getDemographic() + " = ? ";
                            params.add(demographic.getValue());
                        }
                    }
                }
                break;
        }
        return where;
    }
    
    /**
     * Construir el filtro SQL
     * @param laboratorys
     * @return Where de la consulta SQL
     * @throws java.lang.Exception
    */
    public static String buildSQLLaboratoryFilter(List<Integer> laboratorys, int idBranch) throws Exception {
        
      
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" AND ( lab22.lab05c1 = ").append(idBranch).append( " OR ");
        if(laboratorys.size() > 0){
            
            for (int i = 0; i < laboratorys.size(); i++) {
                stringBuilder.append( " lab57.lab40c1 = " );
                stringBuilder.append(laboratorys.get(i));
                if (i != laboratorys.size() - 1) {
                    stringBuilder.append(" or ");
                }
            }
            // Agregamos el paréntesis de cierre
            stringBuilder.append(") ");
        }
        else {
            stringBuilder.append( " lab57.lab40c1 = -1 )" );
        }
        
        return stringBuilder.toString();
    }

    /**
     * Metodo para obtener los campos y relaciones relacionadas a los
     * demograficos
     *
     * @param demographics Lista de demograficos ha agregar a la consulta
     * @param select seccion de la consulta del select
     * @param from seccion de la consulta del from
     */
    public static void buildSQLDemographicSelect(List<Demographic> demographics, StringBuilder select, StringBuilder from) {
        if (demographics != null && !demographics.isEmpty()) {
            demographics.forEach((demographic)
                    -> {
                if (demographic.getOrigin() == null) {
                    switch (demographic.getId()) {
                        case Constants.BRANCH:
                            select.append(", lab22.lab05c1, lab05c10, lab05c4, lab05c12");
                            from.append(" LEFT JOIN lab05 ON lab05.lab05c1 = lab22.lab05c1 ");
                            break;
                        case Constants.SERVICE:
                            select.append(", lab22.lab10c1, lab10c2, lab10c7");
                            from.append(" LEFT JOIN lab10 ON lab10.lab10c1 = lab22.lab10c1 ");
                            break;
                        case Constants.PHYSICIAN:
                            select.append(", lab22.lab19c1, lab19c2, lab19c3, lab19c21, lab19c22, lab19c24");
                            from.append(" LEFT JOIN lab19 ON lab19.lab19c1 = lab22.lab19c1  ");
                            break;
                        case Constants.ACCOUNT:
                            select.append(", lab22.lab14c1, lab14c2, lab14c3");
                            from.append(" LEFT JOIN lab14 ON lab14.lab14c1 = lab22.lab14c1 ");
                            break;
                        case Constants.RATE:
                            select.append(", lab22.lab904c1, lab904c2, lab904c3");
                            from.append(" LEFT JOIN lab904 ON lab904.lab904c1 = lab22.lab904c1 ");
                            break;
                        case Constants.RACE:
                            select.append(", lab21.lab08c1 AS lab21lab08lab08c1, lab21lab08.lab08c2 AS lab21lab08lab08c2, lab21lab08.lab08c4 AS lab21lab08lab08c4, lab21lab08.lab08c5 AS lab21lab08lab08c5");
                            from.append(" LEFT JOIN lab08 lab21lab08 ON lab21lab08.lab08c1 = lab21.lab08c1 ");
                            break;
                        case Constants.DOCUMENT_TYPE:
                            select.append(", lab21.lab54c1, lab21lab54.lab54c1 AS lab21lab54lab54c1, lab21lab54.lab54c2 AS lab21lab54lab54c2, lab21lab54.lab54c3 AS lab21lab54lab54c3");
                            from.append(" LEFT JOIN lab54 lab21lab54 ON lab21lab54.lab54c1 = lab21.lab54c1 ");
                            break;
                        case Constants.ORDERTYPE:
                            select.append(", lab22.lab103c1, lab103c2, lab103c3, lab103c4, lab103.lab07c1 AS lab103lab07c1");
                            from.append(" LEFT JOIN lab103 ON lab103.lab103c1 = lab22.lab103c1 ");
                            break;
                        case Constants.ORDER_HIS:
                            select.append(", lab22.lab22c7");
                            break;
                        case Constants.PATIENT_EMAIL:
                            //Va en al informacion del paciente
                            break;
                        default:
                            break;
                    }
                } else {
                    if (demographic.getOrigin().equals("O")) {
                        if (demographic.isEncoded()) {
                            select.append(", demo").append(demographic.getId()).append(".lab63c1 as demo").append(demographic.getId()).append("_id ");
                            select.append(", demo").append(demographic.getId()).append(".lab63c2 as demo").append(demographic.getId()).append("_code ");
                            select.append(", demo").append(demographic.getId()).append(".lab63c3 as demo").append(demographic.getId()).append("_name ");
                            select.append(", demo").append(demographic.getId()).append(".lab63c8 as demo").append(demographic.getId()).append("_nameenglish ");

                            from.append(" LEFT JOIN Lab63 demo").append(demographic.getId()).append(" ON lab22.lab_demo_").append(demographic.getId()).append(" = demo").append(demographic.getId()).append(".lab63c1 ");
                        } else {
                            select.append(", lab22.lab_demo_").append(demographic.getId());
                        }
                    } else {
                        if (demographic.getOrigin().equals("H")) {
                            if (demographic.isEncoded()) {
                                select.append(", demo").append(demographic.getId()).append(".lab63c1 as demo").append(demographic.getId()).append("_id ");
                                select.append(", demo").append(demographic.getId()).append(".lab63c2 as demo").append(demographic.getId()).append("_code ");
                                select.append(", demo").append(demographic.getId()).append(".lab63c3 as demo").append(demographic.getId()).append("_name ");
                                select.append(", demo").append(demographic.getId()).append(".lab63c8 as demo").append(demographic.getId()).append("_nameenglish ");

                                from.append(" LEFT JOIN Lab63 demo").append(demographic.getId()).append(" ON lab21.lab_demo_").append(demographic.getId()).append(" = demo").append(demographic.getId()).append(".lab63c1 ");
                            } else {
                                select.append(", lab21.lab_demo_").append(demographic.getId());
                            }
                        }
                    }
                }
            });
        }
    }

    /**
     * Metodo para obtener orden en los demograficos fijos
     *
     * @param id id del demografico
     * @param demos Lista de demograficos ha ordenar
     */
    public static void buildSQLDemographicOrder(int id, List<String> demos) {
        switch (id) {
            case Constants.BRANCH:
                demos.add("lab22.lab05c1");
                break;
            case Constants.SERVICE:
                demos.add("lab22.lab10c1");
                break;
            case Constants.PHYSICIAN:
                demos.add("lab22.lab19c1");
                break;
            case Constants.ACCOUNT:
                demos.add("lab22.lab14c1");
                break;
            case Constants.RATE:
                demos.add("lab22.lab904c1");
                break;
            case Constants.RACE:
                demos.add("lab21.lab08c1");
                break;
            case Constants.DOCUMENT_TYPE:
                demos.add("lab21.lab54c1");
                break;
            case Constants.ORDERTYPE:
                demos.add("lab22.lab103c1");
                break;
            case Constants.ORDER_HIS:
                demos.add("lab22.lab22c7");
                break;
            case Constants.PATIENT_EMAIL:
                //Va en al informacion del paciente
                demos.add("lab21.lab21c8");
                break;
            default:
                break;
        }
    }

    /**
     * Construir el filtro SQL para estadisticas
     *
     * @param demographics Filtros de Demograficos
     * @param params Parametros
     * @param select seccion de la consulta del select
     * @param where seccion de la consulta del where
     *
     */
    public static void buildSQLDemographicFilterStatistics(List<FilterDemographic> demographics, List<Object> params, StringBuilder select, StringBuilder where) {
        if (demographics != null && !demographics.isEmpty()) {
            for (FilterDemographic demographic : demographics) {
                if (demographic.getDemographic() != null) {
                    switch (demographic.getDemographic()) {
                        case Constants.BRANCH:
                            if (demographic.getDemographicItems() != null && !demographic.getDemographicItems().isEmpty()) {
                                if (demographic.getDemographicItems().size() == 1) {
                                    where.append(" AND sta2.sta2c5 = ?");

                                    params.add(demographic.getDemographicItems().get(0));

                                } else {
                                    where.append(" AND sta2.sta2c5 in (").append(demographic.getDemographicItems().stream().map(demo -> demo.toString()).collect(Collectors.joining(","))).append(") ");
                                }
                            }
                            break;
                        case Constants.SERVICE:
                            if (demographic.getDemographicItems() != null && !demographic.getDemographicItems().isEmpty()) {
                                if (demographic.getDemographicItems().size() == 1) {
                                    where.append(" AND sta2.sta2c8 = ?");
                                    params.add(demographic.getDemographicItems().get(0));
                                } else {
                                    where.append(" AND sta2.sta2c8 in (").append(demographic.getDemographicItems().stream().map(demo -> demo.toString()).collect(Collectors.joining(","))).append(") ");
                                }
                            }
                            break;
                        case Constants.PHYSICIAN:
                            if (demographic.getDemographicItems() != null && !demographic.getDemographicItems().isEmpty()) {
                                if (demographic.getDemographicItems().size() == 1) {
                                    where.append(" AND sta2.sta2c11 = ?");
                                    params.add(demographic.getDemographicItems().get(0));
                                } else {
                                    where.append(" AND sta2.sta2c11 in (").append(demographic.getDemographicItems().stream().map(demo -> demo.toString()).collect(Collectors.joining(","))).append(") ");
                                }
                            }
                            break;
                        case Constants.ACCOUNT:
                            if (demographic.getDemographicItems() != null && !demographic.getDemographicItems().isEmpty()) {
                                if (demographic.getDemographicItems().size() == 1) {
                                    where.append(" AND sta2.sta2c14 = ?");
                                    params.add(demographic.getDemographicItems().get(0));
                                } else {
                                    where.append(" AND sta2.sta2c14 in (").append(demographic.getDemographicItems().stream().map(demo -> demo.toString()).collect(Collectors.joining(","))).append(") ");
                                }
                            }
                            break;
                        case Constants.RATE:
                            if (demographic.getDemographicItems() != null && !demographic.getDemographicItems().isEmpty()) {
                                if (demographic.getDemographicItems().size() == 1) {
                                    where.append(" AND sta2.sta2c17 = ?");
                                    params.add(demographic.getDemographicItems().get(0));
                                } else {
                                    where.append(" AND sta2.sta2c17 in (").append(demographic.getDemographicItems().stream().map(demo -> demo.toString()).collect(Collectors.joining(","))).append(") ");
                                }
                            }
                            break;
                        case Constants.RACE:
                            if (demographic.getDemographicItems() != null && !demographic.getDemographicItems().isEmpty()) {
                                if (demographic.getDemographicItems().size() == 1) {
                                    where.append(" AND sta1.sta1c8 = ?");
                                    params.add(demographic.getDemographicItems().get(0));
                                } else {
                                    where.append(" AND sta1.sta1c8 in (").append(demographic.getDemographicItems().stream().map(demo -> demo.toString()).collect(Collectors.joining(","))).append(") ");
                                }
                            }
                            break;

                        case Constants.PATIENT_SEX:
                            if (demographic.getDemographicItems() != null && !demographic.getDemographicItems().isEmpty()) {
                                if (demographic.getDemographicItems().size() == 1) {
                                    where.append(" AND sta1.sta1c2 = ?");
                                    params.add(demographic.getDemographicItems().get(0));
                                } else {
                                    where.append(" AND sta1.sta1c2 in (").append(demographic.getDemographicItems().stream().map(demo -> demo.toString()).collect(Collectors.joining(","))).append(") ");
                                }
                            }
                            break;

                        case Constants.DOCUMENT_TYPE:
                            if (demographic.getDemographicItems() != null && !demographic.getDemographicItems().isEmpty()) {
                                if (demographic.getDemographicItems().size() == 1) {
                                    where.append(" AND sta1.sta1c5 = ?");
                                    params.add(demographic.getDemographicItems().get(0));
                                } else {
                                    where.append(" AND sta1.sta1c5 in (").append(demographic.getDemographicItems().stream().map(demo -> demo.toString()).collect(Collectors.joining(","))).append(") ");
                                }
                            }
                            break;
                        case Constants.ORDERTYPE:
                            if (demographic.getDemographicItems() != null && !demographic.getDemographicItems().isEmpty()) {
                                if (demographic.getDemographicItems().size() == 1) {
                                    where.append(" AND sta2.sta2c2 = ?");
                                    params.add(demographic.getDemographicItems().get(0));
                                } else {
                                    where.append(" AND sta2.sta2c2 in (").append(demographic.getDemographicItems().stream().map(demo -> demo.toString()).collect(Collectors.joining(","))).append(") ");
                                }
                            }
                            break;
                        case Constants.PATIENT_ID:
                            if (demographic.getValue() != "") {
                                where.append(" AND sta1.sta1c11 = ?");
                                params.add(Tools.encrypt(demographic.getValue()));
                            }
                            break;
                        default:
                            if (demographic.getDemographic() > 0) {
                                if (demographic.getOrigin() != null) {
                                    if (demographic.getOrigin().equalsIgnoreCase("O")) {
                                        select.append(", sta2.sta_demo_").append(demographic.getDemographic());
                                        select.append(", sta2.sta_demo_").append(demographic.getDemographic()).append("_code");
                                        select.append(", sta2.sta_demo_").append(demographic.getDemographic()).append("_name");
                                    } else {
                                        select.append(", sta1.sta_demo_").append(demographic.getDemographic());
                                        select.append(", sta1.sta_demo_").append(demographic.getDemographic()).append("_code");
                                        select.append(", sta1.sta_demo_").append(demographic.getDemographic()).append("_name");
                                    }

                                    if (demographic.getDemographicItems() != null && !demographic.getDemographicItems().isEmpty()) {
                                        if (demographic.getDemographicItems().size() == 1) {
                                            where.append(" AND sta_demo_" + demographic.getDemographic()).append(" = ? ");
                                            params.add(demographic.getDemographicItems().get(0));
                                        } else {
                                            where.append(" AND sta_demo_").append(demographic.getDemographic()).append(" in (").append(demographic.getDemographicItems().stream().map(demo -> demo.toString()).collect(Collectors.joining(","))).append(") ");
                                        }
                                    } else {
                                        where.append(" AND sta_demo_").append(demographic.getDemographic()).append(" = ? ");
                                        params.add(demographic.getValue());
                                    }
                                }
                            }
                            break;
                    }
                }
            }
        }
    }

    /**
     * Construir el filtro SQL
     *
     * @param demographics Filtros de Demograficos
     * @param params Parametros
     * @param select seccion de la consulta del select
     * @param where seccion de la consulta del where
     * @param from seccion de la consulta del from
     */
    public static void buildSQLDemographicFilterStatisticsLab(List<FilterDemographic> demographics, List<Object> params, StringBuilder select, StringBuilder from, StringBuilder where) {

        if (demographics != null && !demographics.isEmpty()) {
            for (FilterDemographic demographic : demographics) {
                if (demographic.getDemographic() != null) {
                    switch (demographic.getDemographic()) {
                        case Constants.BRANCH:
                            if (demographic.getDemographicItems() != null && !demographic.getDemographicItems().isEmpty()) {
                                if (demographic.getDemographicItems().size() == 1) {
                                    where.append(" AND lab22.lab05c1 = ?");
                                    params.add(demographic.getDemographicItems().get(0));
                                } else {
                                    where.append(" AND lab22.lab05c1 in (").append(demographic.getDemographicItems().stream().map(demo -> demo.toString()).collect(Collectors.joining(","))).append(") ");
                                }
                            }
                            break;
                        case Constants.SERVICE:
                            if (demographic.getDemographicItems() != null && !demographic.getDemographicItems().isEmpty()) {
                                if (demographic.getDemographicItems().size() == 1) {
                                    where.append(" AND lab22.lab10c1 = ?");
                                    params.add(demographic.getDemographicItems().get(0));
                                } else {
                                    where.append(" AND lab22.lab10c1 in (").append(demographic.getDemographicItems().stream().map(demo -> demo.toString()).collect(Collectors.joining(","))).append(") ");
                                }
                            }
                            break;
                        case Constants.PHYSICIAN:
                            if (demographic.getDemographicItems() != null && !demographic.getDemographicItems().isEmpty()) {
                                if (demographic.getDemographicItems().size() == 1) {
                                    where.append(" AND lab22.lab19c1 = ?");
                                    params.add(demographic.getDemographicItems().get(0));
                                } else {
                                    where.append(" AND lab22.lab19c1 in (").append(demographic.getDemographicItems().stream().map(demo -> demo.toString()).collect(Collectors.joining(","))).append(") ");
                                }
                            }
                            break;
                        case Constants.ACCOUNT:
                            if (demographic.getDemographicItems() != null && !demographic.getDemographicItems().isEmpty()) {
                                if (demographic.getDemographicItems().size() == 1) {
                                    where.append(" AND lab22.lab14c1 = ?");
                                    params.add(demographic.getDemographicItems().get(0));
                                } else {
                                    where.append(" AND lab22.lab14c1 in (").append(demographic.getDemographicItems().stream().map(demo -> demo.toString()).collect(Collectors.joining(","))).append(") ");
                                }
                            }
                            break;
                        case Constants.RATE:
                            if (demographic.getDemographicItems() != null && !demographic.getDemographicItems().isEmpty()) {
                                if (demographic.getDemographicItems().size() == 1) {
                                    where.append(" AND lab22.lab904c1 = ?");
                                    params.add(demographic.getDemographicItems().get(0));
                                } else {
                                    where.append(" AND lab22.lab904c1 in (").append(demographic.getDemographicItems().stream().map(demo -> demo.toString()).collect(Collectors.joining(","))).append(") ");
                                }
                            }
                            break;
                        case Constants.RACE:
                            if (demographic.getDemographicItems() != null && !demographic.getDemographicItems().isEmpty()) {
                                if (demographic.getDemographicItems().size() == 1) {
                                    where.append(" AND lab21.lab08c1 = ?");
                                    params.add(demographic.getDemographicItems().get(0));
                                } else {
                                    where.append(" AND lab21.lab08c1 in (").append(demographic.getDemographicItems().stream().map(demo -> demo.toString()).collect(Collectors.joining(","))).append(") ");
                                }
                            }
                            break;
                        case Constants.DOCUMENT_TYPE:
                            if (demographic.getDemographicItems() != null && !demographic.getDemographicItems().isEmpty()) {
                                if (demographic.getDemographicItems().size() == 1) {
                                    where.append(" AND lab21.lab54c1 = ?");
                                    params.add(demographic.getDemographicItems().get(0));
                                } else {
                                    where.append(" AND lab21.lab54c1 in (").append(demographic.getDemographicItems().stream().map(demo -> demo.toString()).collect(Collectors.joining(","))).append(") ");
                                }
                            }
                            break;
                        case Constants.ORDERTYPE:
                            if (demographic.getDemographicItems() != null && !demographic.getDemographicItems().isEmpty()) {
                                if (demographic.getDemographicItems().size() == 1) {
                                    where.append(" AND lab22.lab103c1 = ?");
                                    params.add(demographic.getDemographicItems().get(0));
                                } else {
                                    where.append(" AND lab22.lab103c1 in (").append(demographic.getDemographicItems().stream().map(demo -> demo.toString()).collect(Collectors.joining(","))).append(") ");
                                }
                            }
                            break;
                        case Constants.ORDER_HIS:
                            where.append(" AND lab22c7 = ?");
                            params.add(demographic.getValue());
                            break;
                        case Constants.PATIENT_EMAIL:
                            where.append(" AND lab21c8 = ?");
                            params.add(demographic.getValue());
                            break;
                        default:
                            if (demographic.getDemographic() > 0) {
                                if (demographic.getOrigin() != null) {
                                    if (demographic.getOrigin().equals("O")) {
                                        if (demographic.isEncoded()) {
                                            select.append(", demo").append(demographic.getDemographic()).append(".lab63c1 as demo").append(demographic.getDemographic()).append("_id");
                                            select.append(", demo").append(demographic.getDemographic()).append(".lab63c2 as demo").append(demographic.getDemographic()).append("_code");
                                            select.append(", demo").append(demographic.getDemographic()).append(".lab63c3 as demo").append(demographic.getDemographic()).append("_name");

                                            from.append(" LEFT JOIN Lab63 demo").append(demographic.getDemographic()).append(" ON Lab22.lab_demo_").append(demographic.getDemographic()).append(" = demo").append(demographic.getDemographic()).append(".lab63c1");
                                        } else {
                                            select.append(", Lab22.lab_demo_").append(demographic.getDemographic());
                                        }
                                    } else {
                                        if (demographic.getOrigin().equals("H")) {
                                            if (demographic.isEncoded()) {
                                                select.append(", demo").append(demographic.getDemographic()).append(".lab63c1 as demo").append(demographic.getDemographic()).append("_id");
                                                select.append(", demo").append(demographic.getDemographic()).append(".lab63c2 as demo").append(demographic.getDemographic()).append("_code");
                                                select.append(", demo").append(demographic.getDemographic()).append(".lab63c3 as demo").append(demographic.getDemographic()).append("_name");

                                                from.append(" LEFT JOIN Lab63 demo").append(demographic.getDemographic()).append(" ON Lab21.lab_demo_").append(demographic.getDemographic()).append(" = demo").append(demographic.getDemographic()).append(".lab63c1");
                                            } else {
                                                select.append(", Lab21.lab_demo_").append(demographic.getDemographic());
                                            }
                                        }
                                    }
                                }

                                if (demographic.getDemographicItems() != null && !demographic.getDemographicItems().isEmpty()) {
                                    if (demographic.getDemographicItems().size() == 1) {
                                        where.append(" AND lab_demo_").append(demographic.getDemographic()).append(" = ? ");
                                        params.add(demographic.getDemographicItems().get(0));
                                    } else {
                                        where.append(" AND lab_demo_").append(demographic.getDemographic()).append(" in (").append(demographic.getDemographicItems().stream().map(demo -> demo.toString()).collect(Collectors.joining(","))).append(") ");
                                    }
                                } else {
                                    where.append(" AND lab_demo_").append(demographic.getDemographic()).append(" = ? ");
                                    params.add(demographic.getValue());
                                }
                            }
                            break;
                    }
                }
            }
        }
    }
    
     /**
     * Construir el filtro SQL
     *
     * @param demographics Filtros de Demograficos
     * @param params Parametros
     * @param select seccion de la consulta del select
     * @param where seccion de la consulta del where
     * @param from seccion de la consulta del from
     */
    public static void buildSQLDemographicUpdateOrders(List<FilterDemographic> demographics, List<Object> params, StringBuilder where) {

        if (demographics != null && !demographics.isEmpty()) {
            for (FilterDemographic demographic : demographics) {
                if (demographic.getDemographic() != null) {
                    switch (demographic.getDemographic()) {
                        case Constants.BRANCH:
                            if (demographic.getDemographicItems() != null && !demographic.getDemographicItems().isEmpty()) {
                                if (demographic.getDemographicItems().size() == 1) {
                                    where.append(" AND lab22.lab05c1 = ?");
                                    params.add(demographic.getDemographicItems().get(0));
                                } else {
                                    where.append(" AND lab22.lab05c1 in (").append(demographic.getDemographicItems().stream().map(demo -> demo.toString()).collect(Collectors.joining(","))).append(") ");
                                }
                            }
                            break;
                        case Constants.SERVICE:
                            if (demographic.getDemographicItems() != null && !demographic.getDemographicItems().isEmpty()) {
                                if (demographic.getDemographicItems().size() == 1) {
                                    where.append(" AND lab22.lab10c1 = ?");
                                    params.add(demographic.getDemographicItems().get(0));
                                } else {
                                    where.append(" AND lab22.lab10c1 in (").append(demographic.getDemographicItems().stream().map(demo -> demo.toString()).collect(Collectors.joining(","))).append(") ");
                                }
                            }
                            break;
                        case Constants.PHYSICIAN:
                            if (demographic.getDemographicItems() != null && !demographic.getDemographicItems().isEmpty()) {
                                if (demographic.getDemographicItems().size() == 1) {
                                    where.append(" AND lab22.lab19c1 = ?");
                                    params.add(demographic.getDemographicItems().get(0));
                                } else {
                                    where.append(" AND lab22.lab19c1 in (").append(demographic.getDemographicItems().stream().map(demo -> demo.toString()).collect(Collectors.joining(","))).append(") ");
                                }
                            }
                            break;
                        case Constants.ACCOUNT:
                            if (demographic.getDemographicItems() != null && !demographic.getDemographicItems().isEmpty()) {
                                if (demographic.getDemographicItems().size() == 1) {
                                    where.append(" AND lab22.lab14c1 = ?");
                                    params.add(demographic.getDemographicItems().get(0));
                                } else {
                                    where.append(" AND lab22.lab14c1 in (").append(demographic.getDemographicItems().stream().map(demo -> demo.toString()).collect(Collectors.joining(","))).append(") ");
                                }
                            }
                            break;
                        case Constants.RATE:
                            if (demographic.getDemographicItems() != null && !demographic.getDemographicItems().isEmpty()) {
                                if (demographic.getDemographicItems().size() == 1) {
                                    where.append(" AND lab22.lab904c1 = ?");
                                    params.add(demographic.getDemographicItems().get(0));
                                } else {
                                    where.append(" AND lab22.lab904c1 in (").append(demographic.getDemographicItems().stream().map(demo -> demo.toString()).collect(Collectors.joining(","))).append(") ");
                                }
                            }
                            break;
                        case Constants.RACE:
                            if (demographic.getDemographicItems() != null && !demographic.getDemographicItems().isEmpty()) {
                                if (demographic.getDemographicItems().size() == 1) {
                                    where.append(" AND lab21.lab08c1 = ?");
                                    params.add(demographic.getDemographicItems().get(0));
                                } else {
                                    where.append(" AND lab21.lab08c1 in (").append(demographic.getDemographicItems().stream().map(demo -> demo.toString()).collect(Collectors.joining(","))).append(") ");
                                }
                            }
                            break;
                        case Constants.DOCUMENT_TYPE:
                            if (demographic.getDemographicItems() != null && !demographic.getDemographicItems().isEmpty()) {
                                if (demographic.getDemographicItems().size() == 1) {
                                    where.append(" AND lab21.lab54c1 = ?");
                                    params.add(demographic.getDemographicItems().get(0));
                                } else {
                                    where.append(" AND lab21.lab54c1 in (").append(demographic.getDemographicItems().stream().map(demo -> demo.toString()).collect(Collectors.joining(","))).append(") ");
                                }
                            }
                            break;
                        case Constants.ORDERTYPE:
                            if (demographic.getDemographicItems() != null && !demographic.getDemographicItems().isEmpty()) {
                                if (demographic.getDemographicItems().size() == 1) {
                                    where.append(" AND lab22.lab103c1 = ?");
                                    params.add(demographic.getDemographicItems().get(0));
                                } else {
                                    where.append(" AND lab22.lab103c1 in (").append(demographic.getDemographicItems().stream().map(demo -> demo.toString()).collect(Collectors.joining(","))).append(") ");
                                }
                            }
                            break;
                        case Constants.ORDER_HIS:
                            where.append(" AND lab22c7 = ?");
                            params.add(demographic.getValue());
                            break;
                        case Constants.PATIENT_EMAIL:
                            where.append(" AND lab21c8 = ?");
                            params.add(demographic.getValue());
                            break;
                        default:
                            if (demographic.getDemographic() > 0) {
                                if (demographic.getDemographicItems() != null && !demographic.getDemographicItems().isEmpty()) {
                                    if (demographic.getDemographicItems().size() == 1) {
                                        where.append(" AND lab_demo_").append(demographic.getDemographic()).append(" = ? ");
                                        params.add(demographic.getDemographicItems().get(0));
                                    } else {
                                        where.append(" AND lab_demo_").append(demographic.getDemographic()).append(" in (").append(demographic.getDemographicItems().stream().map(demo -> demo.toString()).collect(Collectors.joining(","))).append(") ");
                                    }
                                } else {
                                    where.append(" AND lab_demo_").append(demographic.getDemographic()).append(" = ? ");
                                    params.add(demographic.getValue());
                                }
                            }
                            break;
                    }
                }
            }
        }
    }
}
