/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.masters.test;

import java.util.List;
import net.cltech.enterprisent.domain.masters.test.Diagnostic;
import net.cltech.enterprisent.domain.masters.test.DiagnosticByTest;
import net.cltech.enterprisent.domain.masters.test.ListDiagnosticByTest;
import net.cltech.enterprisent.domain.masters.test.Test;
import net.cltech.enterprisent.domain.masters.test.TestByDiagnostic;

/**
 * Interfaz de servicios a la informacion del maestro Diagnostico
 *
 * @version 1.0.0
 * @author enavas
 * @since 21/06/2017
 * @see Creación
 */
public interface DiagnosticService
{

    /**
     * Lista los diagnosticos desde la base de datos
     *
     * @return Lista de diagnosticos
     * @throws Exception Error en base de datos
     */
    public List<Diagnostic> list() throws Exception;

    /**
     * Obtener informacion de un diagnostico en la base de datos
     *
     * @param id del diagnostico consultado.
     * @param code codigo del diagnostico consultado.
     * @param name nombre del diagnostico consultado.
     * @param type tipo del diagnostico consultado.
     * @param state estado del diagnostico consultado.
     * @return Lista de diagnosticos
     * @throws Exception Error en base de datos
     */
    public List<Diagnostic> get(Integer id, String code, String name, Integer type, Boolean state) throws Exception;

    /**
     * Registra una Lista de diagnosticos en la base de datos
     *
     * @param diagnostics Instancia con los datos de los diagnosticos.
     * @return numero de registros insertados
     * @throws Exception Error en base de datos
     */
    public int createAll(List<Diagnostic> diagnostics) throws Exception;

    /**
     * Registra un nuevo diagnostico en la base de datos
     *
     * @param diagnostic Instancia con los datos del diagnostico.
     * @return Instancia con los datos del diagnostico.
     * @throws Exception Error en base de datos
     */
    public Diagnostic create(Diagnostic diagnostic) throws Exception;

    /**
     * Actualiza la informacion de un diagnostico en la base de datos
     *
     * @param diagnostic Instancia con los datos del diagnostico.
     * @return Instancia con los datos del diagnostico actualizado.
     * @throws Exception Error en base de datos
     */
    public Diagnostic update(Diagnostic diagnostic) throws Exception;

    /**
     * Realiza la asignación de de los diagnosticos al examen
     *
     * @param assign informacion para la asignacion de diagnosticos
     * @return Registros afectados
     * @throws Exception Error en el servicio
     */
    public int assignToTest(DiagnosticByTest assign) throws Exception;

    /**
     * Realiza la asignación de los diagnosticos al examen
     *
     * @param assign informacion para la asignacion de diagnosticos
     * @return Registros afectados
     * @throws Exception Error en el servicio
     */
    public int assignToTest(TestByDiagnostic assign) throws Exception;

    /**
     * Lista los antibiocticos por un exámen
     *
     * @param idTest id del examen
     * @return Lista de diagnosticos
     * @throws Exception Error en el servicio
     */
    public List<Diagnostic> listByTest(int idTest) throws Exception;

    /**
     * Lista las pruebas asociadas a un diagnostico
     *
     * @param id Id del diagnostico
     * @return Lista de pruebas
     * @throws Exception Error en base de datos
     */
    public List<Test> listTests(Integer id) throws Exception;

    /**
     * Lista las pruebas con seleccion si estan asignadas a un diagnostico
     *
     * @param id Id del diagnostico
     * @return Lista de pruebas
     * @throws Exception Error en base de datos
     */
    public List<Test> listTestWithDiagnostic(int id) throws Exception;

    /**
     * Lista las pruebas asociadas a un diagnostico
     *
     * @param listDiagnosticByTest
     * @return Lista de pruebas
     * @throws Exception Error en base de datos
     */
    public List<DiagnosticByTest> listTest(ListDiagnosticByTest listDiagnosticByTest) throws Exception;

    /**
     * Lista los diagnosticos por orden.
     *
     * @param order Numero de la orden
     * @return Lista de diagnosticos.
     * @throws Exception Error en base de datos
     */
    public List<Diagnostic> ListDiagnostics(long order) throws Exception;
}
