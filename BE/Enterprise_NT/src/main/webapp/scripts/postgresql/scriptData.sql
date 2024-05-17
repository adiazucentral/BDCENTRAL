--Script para la creacion y actualizacion de la base de datos transaccional de PostgreSQL
CREATE OR REPLACE FUNCTION script() RETURNS integer AS $$
BEGIN
    --CONFIGURACION
    IF NOT EXISTS(SELECT 1 FROM lab98)
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            --GENERAL
            ('Abreviatura', ''),
            ('Entidad', ''),
            ('Cliente', '104F'),
            ('Nombres', ''),
            ('Apellidos', ''),
            ('FormatoHistoria', ''),
            ('EntregaInformesSede', 'False'),
            ('DiasClave', '30'),
            ('DemograficoExcluirPrueba', ''),
            ('SeparadorMuestra', '.'),
            ('ImprimirEtiquetaAdicional', 'False'),
            ('ImprimirEtiquetaAutomaticamente', 'False'),
            ('TipoImpresion', '1'), -- 1: Por equipo, 2: Demografico Servicio
            ('ResolverImpresora', '1'), -- 1: IP de Equipos, 2: Nombre
            ('TrabajoPorSede', 'True'),
            ('ManejoPeso', 'True'),
            ('ManejoTalla', 'True'),
            ('ManejoRaza', 'False'),
            ('ManejoServicio', 'False'),
            ('ManejoMedico', 'False'),
            ('FormatoTelefono', ''),
            ('DigitoA�o', '4'),
            ('HistoriaAutomatica', 'False'),
            ('ManejoTipoDocumento', 'False'),
            --INGRESO DE ORDENES
            ('DigitosOrden', '4'),
            ('FormatoFecha', 'dd/MM/yyyy'),
            ('NumeroOrdenAutomatico', 'False'),
            ('TipoNumeroOrden', 'General'),
            ('DiasMaximoModificarOrden', '3'),
            ('MotivoModificacionOrden', 'False'),
            ('AgregarExamenesMuestras', 'False'),
            ('VerPreliminarTalon', 'True'),
            ('Entrevista', 'True'),
            ('ContrasenaPaciente', '1'), -- 1: Cuatro ultimos digitos de la cedula, 2: Aleatoria
            ('ValorInicialTipoOrden', '0'),
            ('DemograficoInconsistensias', '0'),
            ('TurnoIngresoOrden', 'True'),
            ('DemograficoManejoTurno', '0'),
            ('TurnoEntradaMuestra', 'True'),
            ('AlarmaUltimaOrden', 'False'),
            ('PeriodoDiasAlarma', '0'),
            ('LectorCedulas', 'False'),
            ('AnularOrdenValidada', 'False'),
            --RESULTADOS E INFORMES
            ('ComentarioRegistroRestringido', 'Restringido'),
            ('ComentarioResultadoPendiente', 'Pendiente'),
            ('VerPreliminarRegistro', 'False'),
            ('MotivoModificacionResultado', 'False'),
            ('GeneraNombrePDFHistoria', 'False'),
            ('IdentificarCopiasInformes', 'True'),
            ('ImprimirAdjuntos', 'False'),
            ('ImprimirAdjuntosPreliminar', 'False'),
            ('MostrarReLlamadoColumna', 'False'),
            ('OrdenamientoRegistroResultados', '1'), -- 1: Numero de Orden, 2: Tipo de Orden
            ('ImprimirInformeFinal', '1'), -- 1: Examenes Validados, 2: Ordenes Completas
            ('EnviarCorreo', ''), -- Se envia a 1: Paciente, 2: Medico, 3: Los Dos, 0: No Enviar
            ('PedirCorreoAdicional', 'False'),
            ('SolicitarAsunto', 'False'),
            ('SolicitarCuerpoCorreo', 'False'),
            ('DemograficoTituloInforme', '0'),
            ('EntrevistaPanicoObligatoria', 'False'),
            ('VisualizarFotoPaciente', 'False'),
            ('ExamenesProcesarAnalizador', 'False'),
            ('RegistroCMIM', 'False'),
            ('DemograficoHistograma', '0'),
            ('DemograficoItemHistograma', ''),
            ('RutaReportes', ''),
            ('NombreLaboratorioTOP', 'False'),
            --TARIFA
            ('ManejoTarifa', 'False'),
            ('Facturacion', '2'),-- 0 - Sin facturaci?n, 1 - General, 2 - USA
            ('ManejoCliente', 'False'),
            ('Caja', 'False'),
            ('Impuesto', '0'),
            ('Moneda', 'Pesos'),
            ('SimboloMonetario', '$'),
            ('RestarImpuesto', 'False'),
            ('ManejoCentavos', 'False'),
            ('TarifaPorDefecto', '0'),
            --TRAZABILIDAD DE LA MUESTRA
            ('Trazabilidad', '1'), -- 1: No Aplica, 2: Entrada Muestra, 3: Completa, 4: Lista
            ('Trazabilidades', '1'), -- 1: General, 2: Tipo Orden
            ('TomaMuestra', 'False'),
            ('DestinoVerificaMicrobiologia', '0'),
            ('SiembraMicrobiologia', 'False'),
            ('DestinoVerificaCentralMuestras', '0'),
            ('GradillaGeneral', '#FFFFFF'),
            ('GradillaPendiente', 'True'),
            ('GradillaPendienteColor', '#FFFFFF'),
            ('GradillaConfidenciales', 'True'),
            ('GradillaCondifencialesColor', '#FFFFFF'),
            ('Fila', '10'),
            ('Columna', '10'),
            ('AlarmaCalidadMuestra', '0'),
            ('DestinoVerificaDesecho', '0'),
            --UTILIDADES - POKA YOQUE
            ('IngresoEntrevista', '0'),
            ('VerificacionMuestra', '0'),
            ('RegistroResultados', '0'),
            --UTILIDADES - RE LLAMADO
            ('RangoInicial', '0'),
            ('RangoFinal', '0'),
            --CITAS
            ('HorarioAtencionDesde', ''),
            ('HorarioAtencionHasta', ''),
            ('IntervalosCitas', ''),
            ('CantidadCitasIntervalo', ''),
            ('ReiniciarContador', ''),
            ('RolCaja', '0'),
            --LDAP
            ('ServidorLDAP', ''),
            ('UsuarioLDAP', ''),
            ('ClaveLDAP', ''),
            ('GrupoLIS', ''),
            --
            ('SeparadorLista', ';'),
            ('DemograficoPyP', ''),
            --WIDGETS
            ('WidgetMuestraTiempo', '1'),
            ('WidgetResultados', '5'),
            --MICROBIOLOGIA
            ('WhonetTipo', '1'),
            ('WhonetTHM', ''),
            ('WhonetEDTA', ''),
            ('WhonetAPB', ''),
            --UTILIDADES
            ('HorarioReinicioOrdenes', '0000'),
           
            ('UrlLIS', '');
    END IF;
    --LISTAS
    IF NOT EXISTS(SELECT 1 FROM lab80)
    THEN
        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (1, null, null, 'Tipos de Area', 'Area Types');
        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (2, 1, null, 'Quimica Clinica', 'Clinical Chemistry');
        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (3, 1, null, 'Microbiolog?a', 'Microbiology');
        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (4, 1, null, 'Patolog?a', 'Pathology');
        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (5, 1, null, 'Citolog?a', 'Cytology');

        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (6, null, null, 'Sexos', 'Sex');
        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (7, 6, '1', 'Masculino', 'Male');
        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (8, 6, '2', 'Femenino', 'Female');
        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (9, 6, '3', 'Indefinido', 'Undefined');
        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (42, 6, '4', 'Ambos', 'Both');

        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (10, null, null, 'Tipo de Usuario', 'User Type');
        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (11, 10, null, 'Laboratorio', 'Laboratory');
        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (12, 10, null, 'Analizador', 'Analyzer');
        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (13, 10, null, 'Integraci?n HIS', 'HIS Integration');

        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (14, null, null, 'Tipo de Motivo', 'Motive Type');
        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (15, 14, null, 'Modificaci?n de Ingreso', 'Entry Modification');
        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (16, 14, null, 'Eliminaci?n de Ingreso', 'Entry Delete');
        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (17, 14, null, 'Modificaci?n de Resultado', 'Result Modification');
        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (18, 14, null, 'Repetici?n Resultado', 'Result Repeat');
        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (19, 14, null, 'Rechazo Muestra', 'Reject Sample');
        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (20, 14, null, 'Retoma Muestra', 'Retake Sample');
        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (21, 14, null, 'Aplazamiento Muestra', 'Postponement Sample');
        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (22, 14, null, 'Rechazo', 'Reject');
        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (23, 14, null, 'Bloqueo', 'Block');
        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (24, 14, null, 'Repetici?n Analizador', 'Analyzer Repeat');
        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (25, 14, null, 'Modificaci?n Paciente', 'Patient Modification');
        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (26, 14, null, 'Modificaci?n Kardex', 'Kardex Modification');
        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (27, 14, null, 'Retraso Llamado Turno', 'Call Delay Time');
        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (28, 14, null, 'Retraso Validaci?n', 'Validation Delay Time');
        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (29, 14, null, 'Retraso Verificaci?n', 'Check Delay Time');
        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (30, 14, null, 'Reprogramaci?n Toma de Muestra', 'Reprogramming Sampling');
        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (31, 14, null, 'Motivos de rechazo QC', 'QC Reject Reasons');
        --Patologia
        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (63, 14, null, 'Rechazo Muestra de Patolog?a', 'Pathology Sample Rejection');
      
        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (32, null, null, 'Niveles de Complejidad', 'Levels Complex');
        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (33, 32, null, 'Nivel 1', 'Level 1');
        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (34, 32, null, 'Nivel 2', 'Level 2');
        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (35, 32, null, 'Nivel 3', 'Level 3');
        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (36, 32, null, 'Nivel 4', 'Level 4');
        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (37, 32, null, 'Nivel 5', 'Level 5');

        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (38, null, null, 'Tipo de diagnostico', 'Type of diagnostic');
        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (39, 38, '1', 'ICD10', 'ICD10');
        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (40, 38, '2', 'ICD9', 'ICD9');
        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (41, 38, '3', 'Ninguno', 'Ninguno');

        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (43, null, null, 'Tipo de Destino', 'Destination Types');
        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (44, 43, null, 'Inicial', 'Initial');
        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (45, 43, null, 'Proceso Interno', 'Internal Process');
        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (46, 43, null, 'Proceso Externo', 'External Process');
        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (47, 43, null, 'Control', 'Control');
        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (48, 43, null, 'Final', 'Final');

        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (49, null, null, 'Signos', 'Signs');
        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (50, 49, null, '=', '=');
        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (51, 49, null, '>=', '>=');
        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (52, 49, null, '<=', '<=');
        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (53, 49, null, '<', '<');
        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (54, 49, null, '>', '>');
        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (55, 49, null, '<>', '<>');
        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (56, 49, null, 'Entre', 'Between');
        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (57, 49, null, 'No Entre', 'Not Between');
       

    END IF;
    --USUARIO
    IF NOT EXISTS(SELECT 1 FROM lab04)
    THEN
        INSERT INTO lab04 (lab04c2, lab04c3, lab04c4, lab04c5, lab07c1, lab04c6, lab04c7, lab04c8, lab04c9, lab04c1_1, lab04c10, lab04c11, lab04c12, lab04c13, lab04c14, lab04c15, lab04c16, lab04c17, lab04c18, lab04c19, lab04c20, lab04c21, lab04c22, lab04c23, lab04c24, lab103c1) VALUES('Super', 'User', 'lismanager', '9AVAz9/hWlkxiGca+0hOTQ==', 1, now(), now(), now(), now(), null, null, null, null, null, 0, 11, null, 0, 0, 0, 0, 0, 0, 0, 0, null);
        INSERT INTO lab04 (lab04c2, lab04c3, lab04c4, lab04c5, lab07c1, lab04c6, lab04c7, lab04c8, lab04c9, lab04c1_1, lab04c10, lab04c11, lab04c12, lab04c13, lab04c14, lab04c15, lab04c16, lab04c17, lab04c18, lab04c19, lab04c20, lab04c21, lab04c22, lab04c23, lab04c24, lab103c1) VALUES('System', 'User', 'system', '9AVAz9/hWlkxiGca+0hOTQ==', 1, now(), now(), now(), now(), null, null, null, null, null, 0, 11, null, 0, 0, 0, 0, 0, 0, 0, 0, null);
        INSERT INTO lab04 (lab04c2, lab04c3, lab04c4, lab04c5, lab07c1, lab04c6, lab04c7, lab04c8, lab04c9, lab04c1_1, lab04c10, lab04c11, lab04c12, lab04c13, lab04c14, lab04c15, lab04c16, lab04c17, lab04c18, lab04c19, lab04c20, lab04c21, lab04c22, lab04c23, lab04c24, lab103c1) VALUES('Administrator', 'User', 'admin', '9AVAz9/hWlkxiGca+0hOTQ==', 0, now(), now(), now(), now(), null, 'Cltech1', null, null, null, 0, 11, null, 0, 0, 0, 0, 0, 0, 0, 0, null);
    END IF;
    --USUARIO INTEGRATION PARA LAS LICENCIAS
    IF NOT EXISTS(SELECT 1 FROM lab04 WHERE lab04c4 = 'integration')
    THEN
        INSERT INTO lab04 (lab04c2, lab04c3, lab04c4, lab04c5, lab07c1, lab04c6, lab04c7, lab04c8, lab04c9, lab04c1_1, lab04c10, lab04c11, lab04c12, lab04c13, lab04c14, lab04c15, lab04c16, lab04c17, lab04c18, lab04c19, lab04c20, lab04c21, lab04c22, lab04c23, lab04c24, lab103c1) VALUES('Licenses', 'User', 'integration', 'L1QZeF2KfPPqqFh0Knz1RnN/9i1jEiaV', 1, now(), now(), now(), now(), null, null, null, null, null, 0, 11, null, 0, 0, 0, 0, 0, 0, 0, 0, null);
    END IF;
    --TIPO DE ORDEN
    IF NOT EXISTS(SELECT 1 FROM lab103)
    THEN
        INSERT INTO lab103 (lab103c2,lab103c3,lab103c5,lab04c1,lab07c1) values ('R','Rutine',now(),1,1);
        INSERT INTO lab103 (lab103c2,lab103c3,lab103c5,lab04c1,lab07c1) values ('S','Urgencias',now(),1,1);
        INSERT INTO lab103 (lab103c2,lab103c3,lab103c5,lab04c1,lab07c1) values ('A','As soon as posible',now(),1,1);
        INSERT INTO lab103 (lab103c2,lab103c3,lab103c5,lab04c1,lab07c1) values ('C','Re-llamado',now(),1,1);
        INSERT INTO lab103 (lab103c2,lab103c3,lab103c5,lab04c1,lab07c1) values ('P','Prioritario',now(),1,1);
    END IF;
    --ROL
    IF NOT EXISTS(SELECT 1 FROM lab82)
    THEN
        INSERT INTO lab82 (lab82c2, lab82c3, lab82c4, lab04c1, lab07c1) VALUES('CLTechAdministrator', 1, now(), 1, 1);
        INSERT INTO lab82 (lab82c2, lab82c3, lab82c4, lab04c1, lab07c1) VALUES('ConfigurationAdmin', 1, now(), 1, 1);
    END IF;
    --AREA
    IF NOT EXISTS(SELECT 1 FROM lab43)
    THEN
        INSERT INTO lab43 (lab43c2, lab43c3, lab43c4, lab43c5, lab43c6, lab43c7, lab04c1, lab07c1, lab43c8) VALUES(0, 'S0', 'SECCION CERO', '#000000', 0, 'now()', 1, 1, 0);
    END IF;
    --ROL POR USUARIO
    IF NOT EXISTS(SELECT 1 FROM lab84)
    THEN
        INSERT INTO lab84 VALUES (1, 1, 1);
        INSERT INTO lab84 VALUES (1, 2, 2);
    END IF;
    IF NOT EXISTS(SELECT 1 FROM lab80 WHERE  lab80c1 = 58) 
    THEN
        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (58, null, null, 'Tipo de entrega', 'Tipo de entrega');
        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (59, 58, 1, 'Impreso', 'print');
        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (60, 58, 2, 'Correo', 'Mail');
        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (61, 58, 3, 'Consulta web', 'Consulta web');
        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (62, 58, 4, 'Pdf', 'Pdf');    
    END IF;

    IF EXISTS(SELECT 1 FROM lab80 WHERE  lab80c1 = 62) 
    THEN
        UPDATE LAB80 SET lab80c4 = 'Pdf', lab80c5 = 'Pdf' WHERE lab80c1 = 62;
    END IF;
    IF EXISTS(SELECT 1 FROM lab80 WHERE  lab80c1 = 65) 
    THEN
        --impresion via app de pacientes
        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (64, 14, null, 'App', 'App');
    END IF;
    IF NOT EXISTS(SELECT 1 FROM lab80 WHERE  lab80c1 = 64) 
    THEN
      --Banda
        INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (64, 14, null, 'Motivo de la Banda', 'Band motive');
    END IF;
    
    --MODULOS
    TRUNCATE TABLE lab85;
    IF NOT EXISTS(SELECT 1 FROM lab85)
    THEN
        INSERT INTO lab85 (lab85c1, lab85c2,lab85c3) VALUES
            --Maestros
            (1,null,'Managment'),
        --Usuario
            (2,1,'Usuario'),
            (12,2,'Usuario'),
            (13,2,'Rol'),
            (108,2,'Analizador-integracion'),
        --Facturacion
            (3,1,'Facturacion'),
            (14,3,'Asignacion de precios por tarifa'),
            (15,3,'Vigencia'),
            (16,3,'Impuesto por prueba'),
            (17,3,'Protocolo EDI'),
            (18,3,'Receptor EDI'),
            (19,3,'Dias alarma pendientes de pago'),
            (20,3,'Tarifa por cliente'),
            (21,3,'Resoluciones'),
            (22,3,'Tarifa'),
            (23,3,'Cliente'),
            (24,3,'Entidad'),
            (25,3,'Tarjeta de credito'),
            (26,3,'Banco'),
            (27,3,'Tipos de pago'),
            (150,3,'Configuraci?n Rips'),
            (151,3,'Tipo de impuesto'),
            (152,3,'Impresoras fiscales'),
            (153,3,'Contratos'),
        --Integracion
            (4,1,'Integracion'),
            (28,4,'Homologacion de pruebas'),
            (29,4,'Homologacion de demograficos'),
            (30,4,'Homologacion de usuarios'),
            (31,4,'Sistema central'),
            (32,4,'Integracion de Middleware por laboratorio.'),
            (110,4,'Demografico consulta web'),
        --Oportunidad
            (5,1,'Oportunidad'),
            (33,5,'Oportunidad de la prueba'),
            (34,5,'Oportunidad de la muestra'),
            (35,5,'Muestra por servicio'),
            (36,5,'Histograma'),
        --Microbiologia
            (6,1,'Microbiologia'),
            (37,6,'Submuestra'),
            (38,6,'Medio de cultivo por prueba'),
            (39,6,'Medio de cultivo'),
            (40,6,'Procedimiento por prueba'),
            (41,6,'Procedimiento'),
            (42,6,'Metodo de recoleccion'),
            (43,6,'Tarea'),
            (44,6,'Sitio anatomico'),
            (45,6,'Destino de microbiologia'),
            (46,6,'Valores de referencia de antibiotico'),
            (47,6,'Antibiograma'),
            (48,6,'Antibiotico'),
            (49,6,'Microorganismo'),
            (117,6,'Agente etiologico'),
        --Trazabilidad
            (7,1,'Trazabilidad'),
            (52,7,'Motivo'),
            (53,7,'Nevera'),
        --Trazabilidad completa
            (109,7,'Trazabilidad completa'),
            (50,109,'Asignacion de destino'),
            (51,109,'Destino'),
        --Prueba
            (8,1,'Prueba'),
            (54,8,'Valores de Referencia'),
            (55,8,'Delta check'),
            (61,8,'Edicion de pruebas en lista'),
            (62,8,'Edicion de pruebas en lista'),
            --Resultados
            (56,8,'Resultados'),
            (75,56,'Resultado literal por prueba'),
            (76,56,'Resultado literal'),
            (77,56,'Prueba automatica'),
            (78,56,'Relacion de resultados'),
            (79,56,'Plantilla de resultado'),
            (80,56,'Contador Hematologico'),
            (81,56,'Hojas de trabajo'),
            --Excluir pruebas por
            (57,8,'Excluir pruebas por'),
            (82,57,'Demograficos'),
            (83,57,'Usuario'),
            --
            (58,8,'Pruebas por demografico PyP'),
            --Laboratorios
            (59,8,'Laboratorios'),
            (84,59,'Prueba por laboratorio'),
            (85,59,'Laboratorio'),
            (111,59,'sedes por laboratorio'),
            --Diagnosticos
            (60,8,'Diagnosticos'),
            (63,8,'Orden de impresion'),
            (64,8,'Dias de procesamiento'),
            (65,8,'Perfil'),
            (66,8,'Prueba'),
            --Muestras
            (67,8,'Muestras'),
            (88,67,'Muestra'),
            (89,67,'Recipiente'),
            --
            (154,8,'Alarma examen desde ingreso'),
            (68,8,'Comentario'),
            (69,8,'Alarma para resultados'),
            (70,8,'Grupos etarios'),
            (71,8,'Requisito'),
            (72,8,'Tecnica'),
            (73,8,'Unidad'),
            (74,8,'Area'),
        --Entervista
            (9,1,'Entervista'),
            (90,9,'Entervista'),
            (91,9,'Preguntas'),
            (92,9,'Respuestas'),
        --Demograficos
            (10,1,'Demograficos'),
            (116,10,'Dependencia de demograficos'),
            (93,10,'Configuracion Dependencia de demograficos'),
            (94,10,'Items demograficos'),
            (95,10,'Demograficos'),
            (96,10,'Medico'),
            (97,10,'Especialidad'),
            (98,10,'Tipo de orden'),
            (99,10,'Sede'),
            (100,10,'Tipo de documento'),
            (101,10,'Raza'),
            (107,10,'Servicios'),
            (112,10,'Encriptacion de reportes'),
            (113,10,'Demograficos por sede'),
            (114,10,'Demografico item por sede'),
            (115,10,'Valores por demografico'),
            (155,10,'Ex�menes por demogr�ficos'),
            (156,10,'Ordenamiento demograficos'),
        --Configuracion
            (11,1,'Configuracion'),
            (102,11,'Instalacion'),
            (103,11,'Impresion por servicio'),
            (104,11,'Agrupacion de ordenes'),
            (105,11,'Festivos'),
            (106,11,'General'),      
            (86,60,'Diagnostico por prueba'),
            (87,60,'Diagnostico'),
        --Patologia
            (120,1,'Patolog?a'),
            (121,120,'Areas'),
            (122,120,'Contenedores'),
            (123,120,'Submuestras'),
            (124,120,'Organos'),
            (125,120,'Tipo de Estudio'),
            (126,120,'Casetes'),
            (127,120,'Fijadores'),
            (128,120,'Coloraciones'),
            (129,120,'Especialidades'),
            (130,120,'Protocolo'),
            (131,120,'Agenda'),
            (132,120,'Eventos'),
            (133,120,'Campos'),
            (134,120,'Plantillas'),
            (135,120,'Horarios de procesamiento'),
        --Citas
            (190,1,'Citas'),
            (191,190,'Jornadas'),
            (192,190,'Jornadas por sede'),
	--LIS
            (200,null,'LIS'),
            --Gestion de ordenes
            (201,200,'Gestion de ordenes'),
            (210,201,'Ingreso de ordenes'),
            (212,201,'Listados'),
            (213,201,'Ordenes sin historia'),
            (214,201,'Activacion de ordenes'),
            (306,201,'Citas'),
            
            --Gestion de pacientes
            (202,200,'Gestion de pacientes'),
            (216,202,'Historias clinicas'),
            (217,202,'Asignacion de historias'),
            (218,202,'Reasignacion de historias'),
            (219,202,'Inconsistencias'),
            (220,202,'Desbloqueo de historia/orden'),
            --Gestion de muestras
            (204,200,'Gestion de muestras'),
            (211,204,'Ruta de la muestra'),
            (243,204,'Almacen de muestras'),
            (251,204,'Trazabilidad de muestras'),
            (252,204,'Remisiones'),
            --Resultados
            (203,200,'Resultados'),
            (221,203,'Registro de resultados'),
            (222,203,'Registro de resultados por lote'),
            (223,203,'Revision de resultados'),
            (224,203,'Hojas de trabajo'),
            (225,203,'Reenvio a middleware'),
            --Informes y consultas
            (205,200,'Informes y consultas'),
            (229,205,'Informes'),
            (230,205,'Control entrega de informes'),
            (231,205,'Consultas'),
            (232,205,'Consulta de pacientes'),
            (249,205,'Consentimiento informado'),
            --Auditoria
            (206,200,'Auditoria'),
            (233,206,'Auditoria de ordenes'),
            (234,206,'Auditoria de maestros'),
            (235,206,'Auditoria de usuarios'),
            
            --Estadisticas
            (207,200,'Estadisticas'),
            (236,207,'Estadisticas'),
            (237,207,'Estadisticas especiales'),
            (238,207,'Estadisticas con precios'),
            (239,207,'Muestras en destinos'),
            --Indicadores
            (208,200,'Indicadores'),
            (240,208,'Alerta temprana'),
            (241,208,'Indicadores'),
            (242,208,'Histograma'),
            (305,208,'Reportes adicionales'),
            --Utilidades
            (209,200,'Utilidades'),
            (244,209,'Visor de sucesos'),
            (245,209,'Visor de sesiones'),
            (246,209,'Borrados especiales'),
            (247,209,'Editor de informes'),
            (248,209,'Editor de codigos de barras'),
            (215,209,'Reiniciar contador'),
            --Microbiologia
            (300,null,'Microbiologia'),
            (301,300,'Verificacion de microbiologia'),
            (302,300,'Lectura de microbiologia'),
            (303,300,'Plano whonet'),
            (304,300,'Siembra de microbiologia'),
        --Patologia
            (400,null,'Patolog?a'),
            --Recepcion de muestras
            (401,400,'Recepcion de muestras'),
            (410,401,'Ingreso de muestras'),
            (411,401,'Activacion de muestras'),
            --Macroscopia
            (402,400,'Macroscopia'),
            (420,402,'Descripciones Macrosc�picas'),
            (421,402,'Transcripciones'),    
            --Histotecnologia
            (403,400,'Histotecnologia'),
            (430,403,'Procesador de tejidos'),
            (431,403,'Central de inclusion'),
            (432,403,'Corte'),
            (433,403,'Coloracion'),
            --Utilidades
            (408,400,'Utilidades'),
            (470,408,'Editor de codigo de barras'),
        --Facturaci?n
            (500,null,'Facturacion'),
            --Gestion de ordenes
            (501,500,'Generar factura'),
            (510,501,'Facturaci�n por cliente'),
            (511,501,'Facturas combos'),
            (502,500,'Nota credito'),
            (512,502,'Nota credito clientes'),
            (513,502,'Nota credito facturas combos'),
            (503,500,'Recalculo'),
            (508,503,'Recalculo de tarifas'),
            (509,503,'Cambiar precios'),
            (504,500,'Reporte de caja'),
            (505,500,'Reimprimir factura'),
            (506,500,'RIPS'),
            (507,500,'Auditoria de facturas');

    END IF;

    --RELACION DE MODULOS/ROL
    IF NOT EXISTS(SELECT 1 FROM lab86)
    THEN
        INSERT INTO lab86(lab85c1,lab82c1) VALUES(2,1);
        INSERT INTO lab86(lab85c1,lab82c1) VALUES(12,1);
        INSERT INTO lab86(lab85c1,lab82c1) VALUES(13,1);
        INSERT INTO lab86 SELECT lab85c1, 2 FROM lab85 WHERE lab85c1 <= 110;
    END IF;
    --PACIENTE
    IF NOT EXISTS(SELECT 1 FROM lab21)
    THEN
        INSERT INTO lab21 (lab21c1,lab21c2,lab21c3,lab21c5,lab21c12,lab80c1,lab04c1) VALUES (0, 'bygy+9615m4=', 'kCneGglFisE=', 'kCneGglFisE=',now(), 9, 1);
    END IF;
    --TIPO DE PAGO
    IF NOT EXISTS(SELECT 1 FROM lab01)
    THEN
        INSERT INTO lab01 (lab01c2,lab01c3,lab01c4,lab01c5,lab01c6,lab01c7,lab04c1,lab07c1) values('Efectivo',0,0,0,0,now(),1,1);
    END IF;
    --VERSION ENTERPRISE NT
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'Version')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('Version', '1.0.0'),
            ('FechaVersion', '20180601'),
            ('FechaActualizaci?n', '');
    END IF;
    --DIAGNOSTIC ENTERPRISE NT
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'Diagnostics')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('Diagnostics', 'False');
    END IF;
    --ENVIO DE RESULTADOS AUTOMATICOS POR MEDIO DE EMAIL
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'SendAutomaticResultEmail')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('SendAutomaticResultEmail', 'False');
    END IF;
    --INTEGRACION CON DASHBOARD
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'IntegracionDashBoard')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('IntegracionDashBoard', 'False');
    END IF;
    --URL DASHBOARD
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'UrlDashBoard')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('UrlDashBoard', '');
    END IF;
    --INTEGRACION CON SIGA
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'IntegracionSIGA')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('IntegracionSIGA', 'False');
    END IF;
    --URL, SEDE, ORDENES Y VERIFICACIOM - SIGA
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'UrlSIGA')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('UrlSIGA', '');
    END IF;
    -- SEDE - SIGA
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'SedeSIGA')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('SedeSIGA', '');
    END IF;
    -- ORDENES - SIGA
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'OrdenesSIGA')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('OrdenesSIGA', '');
    END IF;
    -- VERIFICACION - SIGA
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'VerificacionSIGA')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('VerificacionSIGA', '');
    END IF;
    --INTEGRACION CON MIDDLEWARE
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'IntegracionMiddleware')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('IntegracionMiddleware', 'False');
    END IF;
    --URL MIDDLEWARE
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'UrlMiddleware')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('UrlMiddleware', '');
    END IF;
    --DESTINO DESECHO
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'DestinoVerificaDesecho')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('DestinoVerificaDesecho', '0');
    END IF;
    --AGRUPACION DE ORDENES
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'AgrupacionOrdenes')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('AgrupacionOrdenes', '1');
    END IF;
    --SEPARADOR DE MIDDLEWARE
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'SeparadorMiddleware')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('SeparadorMiddleware', '-');
    END IF;
    --HORARIO DE REINICIO NUMERO DE ORDENES
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'HorarioReinicioOrdenes')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('HorarioReinicioOrdenes', '0000');
     END IF;
    ---PLANTILLA PARA RECUPERACION DE CONTRASE?AS
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'TemplateMail')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('TemplateMail', '');
    END IF;
    --URL LIS
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'UrlLIS')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('UrlLIS', '');
    END IF;
    --CALCULO DE TIEMPOS DE OPORTUNIDAD Y GENERACION DEL NOMBRE DE LABORATORIO
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'NombreLaboratorioTOP')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('NombreLaboratorioTOP', 'False');
    END IF;
    --MANEJO EVENTOS
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'manejoEventos')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES ('manejoEventos', 'False');
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES ('UrlEventos', '');
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES ('crearOrden', 'False');
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES ('modificarOrden', 'False');
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES ('anularOrden', 'False');
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES ('tomarMuestra', 'False');
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES ('verificarMuestra', 'False');
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES ('ingresarResultado', 'False');
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES ('modificarResultado', 'False');
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES ('repetirResultado', 'False');
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES ('validaPorExamen', 'False');
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES ('validaOrden', 'False');
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES ('desvalidaExamen', 'False');
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES ('imprimirExamen', 'False');
    END IF;
    --DEMOGRAFICOS HOMOLOGADOS
    IF NOT EXISTS(SELECT 1 FROM Lab117 WHERE Lab117C1=-10 AND Lab117C2=2 AND Lab117C3='PN' AND Lab118C1=7)
    THEN
    INSERT INTO Lab117 (Lab118C1, Lab117C1, Lab117C2, Lab117C3) VALUES
    (7,-10,2,'PN');
    END IF;
    --EMAIL
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'SmtpAuthUser')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('SmtpAuthUser', 'testing.cltech@gmail.com'),
            ('SmtpHostName', 'smtp.gmail.com'),
            ('SmtpPasswordUser', 'hdbtvgmuosqaoiyt'),
            ('SmtpPort', '465'),
            ('SmtpSSL', 'SSL');
    END IF;
    --CONTENIDO DEL CORREO
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'EmailBody')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('EmailBody', ''),
            ('EmailSubjectPatient', ''),
            ('EmailSubjectPhysician', '');
    END IF;
    --TIEMPO DE EXPIRACION DEL TOKEN
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'TokenExpirationTime')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('TokenExpirationTime', '60');
    END IF;
    --TIEMPO DE EXPIRACION DE LA SESION
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'SessionExpirationTime')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('SessionExpirationTime', '60');
    END IF;
    --URL DEL APLICATIVO DE LICENCIMIENTO Y DEL APLICATIVO DE SEGURIDAD
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'UrlSecurity')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('UrlSecurity', 'http://192.168.1.6:8080/Security_NT_PRU'),
            ('UrlDischarge', '');
    END IF;
    --TIPO DE DOCUMENTO GENERAL
    IF NOT EXISTS(SELECT 1 FROM lab54 WHERE lab54c1 = 1)
    THEN
        INSERT INTO lab54(lab54c2, lab54c3, lab54c4, lab04c1, lab07c1) VALUES ('00', 'Predeterminado', now(), 1, 1);
    END IF;
    --VALORES DADOS AL GENERO PARA EL CALCULO DE LA FORMULA
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'MaleValue')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('MaleValue', '0'),
            ('FemaleValue', '0'),
            ('UndefinedValue', '0');
    END IF;
    --POLITICAS DE SEGURIDAD
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'SecurityPolitics')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('SecurityPolitics', 'False');
    END IF;
    --AGREGA: DIAS DE VALIDACION PARA DESACTIVACION DE USUARIOS POR INACTIVIDAD
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'DaysValidationInactivity')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('DaysValidationInactivity', '59');
    END IF;
   --URL DE NODEJS
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'UrlNodeJs')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES ('UrlNodeJs', '');
    END IF;
    --AGREGA: DOS NUEVOS CAMPOS EN LA TABLA LAB98
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'ManejoDemograficoConsultaWeb')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('ManejoDemograficoConsultaWeb', 'False'),
            ('DemograficoConsultaWeb','');
    END IF;

    --AGREGA: UN CAMPOS EN LA TABLA LAB98
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'Contrase�aDemograficoConsultaWeb')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('Contrase�aDemograficoConsultaWeb', '');
    END IF;

    --Notificacion obligatoria de resultados
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'PlantillaNotificacionObligatoria')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('PlantillaNotificacionObligatoria', '');
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('AsuntoNotificacionObligatoria', '');
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('CorreosNotificacionObligatoria', '');
    END IF;

    --integracion databank
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'IntegracionDatabank')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('IntegracionDatabank', 'False');
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('UrlDatabank', '');
    END IF;
  

    ----------------------Nuevas Llaves de Configuracion------------------------
    ----PARA LA IMPRESION DE CODIGO DE BARRAS
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'PrintPointBarCode')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('PrintPointBarCode', '1'),
            ('InicialesPaciente', '0'),
            ('HIS', '1');
    END IF;

    --SEGMENT
    --CHECK-IN
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'serverHIS')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
        ('serverHIS', '10.1.0.95');
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
        ('portHIS', '4223');
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
        ('centralSystemHis', '1');
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
        ('rutaLogHis', 'C:\\FSFB\\logCheck-IN\\');
    END IF;

    --integracion HomeBound
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'Homebound')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('Homebound', 'False');
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('TomaMuestraHospitalaria', 'False');
    END IF;


    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'IntegracionHomebound')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('IntegracionHomebound', 'False');
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('UrlHomebound', '');
    END IF;
    --- Protocolo Smtp
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'SmtpProtocol')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('SmtpProtocol', 'smtp');
    END IF;

    --- Notificaion Obligatoria para unas pruebas determinadas
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'PruebasNotificacionObligatoria')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('PruebasNotificacionObligatoria', '');
    END IF;

    --- Seccion Dos de notificaciones obligatorias:
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'CorreosNotificacionObligatoriaUno')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('CorreosNotificacionObligatoriaUno', '');
    END IF;
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'AsuntoNotificacionObligatoriaUno')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('AsuntoNotificacionObligatoriaUno', '');
    END IF;
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'PlantillaNotificacionObligatoriaUno')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('PlantillaNotificacionObligatoriaUno', '');
    END IF;
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'PruebasNotificacionObligatoriaUno')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('PruebasNotificacionObligatoriaUno', '');
    END IF;

    --- Seccion Tres de notificaciones obligatorias:
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'CorreosNotificacionObligatoriaDos')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('CorreosNotificacionObligatoriaDos', '');
    END IF;
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'AsuntoNotificacionObligatoriaDos')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('AsuntoNotificacionObligatoriaDos', '');
    END IF;
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'PlantillaNotificacionObligatoriaDos')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('PlantillaNotificacionObligatoriaDos', '');
    END IF;
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'PruebasNotificacionObligatoriaDos')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('PruebasNotificacionObligatoriaDos', '');
        END IF;

    --- Seccion Cuatro de notificaciones obligatorias:
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'CorreosNotificacionObligatoriaTres')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('CorreosNotificacionObligatoriaTres', '');
    END IF;
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'AsuntoNotificacionObligatoriaTres')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('AsuntoNotificacionObligatoriaTres', '');
    END IF;
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'PlantillaNotificacionObligatoriaTres')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('PlantillaNotificacionObligatoriaTres', '');
    END IF;
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'MuestrasNotificacionObligatoriaTres')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('MuestrasNotificacionObligatoriaTres', '');
        END IF;    

        IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'ServidorCorreo')
        THEN
            INSERT INTO lab98 (lab98c1, lab98c2) VALUES
                   ('ServidorCorreo', 'OUTLOOK');
        END IF;
        IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'DemograficoEncriptacionCorreo')
        THEN
            INSERT INTO lab98 (lab98c1, lab98c2) VALUES
                   ('DemograficoEncriptacionCorreo', '0');
        END IF;
        IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'ManejoTemperatura')
        THEN
            INSERT INTO lab98 (lab98c1, lab98c2) VALUES
                   ('ManejoTemperatura', 'False');
        END IF;
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'UbicacionHospitalaria')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('UbicacionHospitalaria', '');
    END IF;
    ---Llave de cofiguracion para multi sedes
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'ManejoMultiSedes')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('ManejoMultiSedes', '');
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('SedeCentral', '');
    END IF;
    ---Llave de cofiguracion para multi sedes
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'SedeCentral')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('SedeCentral', '');
    END IF;
    -- ENVIO DEL CORREO AUTOMATICO SEGUN DEMOGRAFICO
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'DemograficoEnvioCorreoAutomatico')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('DemograficoEnvioCorreoAutomatico', '');
    END IF;
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'ItemDemograficoEnvioCorreoAutomatico')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('ItemDemograficoEnvioCorreoAutomatico', '');
    END IF;
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'SeccionEnvioDatabank')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('SeccionEnvioDatabank', '');
    END IF;
    --INTEGRACION CON SIGA
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'RangoInicialCitas')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('RangoInicialCitas', '0');
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('RangoFinalCitas', '0');
    END IF;
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'DomainLDAP')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('DomainLDAP', '');
    END IF;
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'LoginActiveDirectory')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('LoginActiveDirectory', 'False');
    END IF;
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'DemographicsByBranch')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('DemographicsByBranch', 'False');
    END IF;
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'UrlCargaOrdenesERR')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('UrlCargaOrdenesERR', '');
    END IF;
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'DemograficosCargaOrdenesERR')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('DemograficosCargaOrdenesERR', '');
    END IF;
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'CodigosCupsIngreso')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('CodigosCupsIngreso', '');
    END IF;
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'CodigoDemograficoIngreso')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('CodigoDemograficoIngreso', '');
    END IF;
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'Entidad')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('Entidad', '');
    END IF;
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'notificacionUsuarios')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('notificacionUsuarios', '');
    END IF;

    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'facturacionCentral')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('facturacionCentral', '');
    END IF;
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'facturacionAutomatica')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('facturacionAutomatica', '');
    END IF;
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'facturaPorCorreo')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('facturaPorCorreo', '');
    END IF;
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'reporteCajaPorHora')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('reporteCajaPorHora', '');
    END IF;
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'ItemDemograficoFacturacionEstado')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('ItemDemograficoFacturacionEstado', '');
    END IF;
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'ItemDemograficoFacturacionMunicipio')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('ItemDemograficoFacturacionMunicipio', '');
    END IF;
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'CodigoPruebaCruzada')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('CodigoPruebaCruzada', '');
    END IF;
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'SedeVerificacionDatabank')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('SedeVerificacionDatabank', '');
    END IF;
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'IntegracionQM')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('IntegracionQM', 'False');
    END IF;
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'UrlQM')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('UrlQM', '');
    END IF;

    --CONFIGURACION RIPS
    IF NOT EXISTS(SELECT 1 FROM lab980)
    THEN
        INSERT INTO lab980 (lab980c1, lab980c2, lab980c3, lab980c4) VALUES
            ('ConcatenarPrefijo', '', null, '' ),
            ('Entidad', '', null, ''),
            ('CodigoEntidad', '', null, ''),
            ('TipoIdentificacionPrestador', '', null, ''),
            ('TipoIdentificacion', '', null, ''),
            ('Ambito', '', null, ''),
            ('Proposito', '', null, ''),
            ('Residencia', '', null, ''),
            ('Municipio', '', null, ''),
            ('Departamento', '', null, ''),
            ('Afiliacion', '', null, ''),
            ('Autorizacion', '', null, ''),
            ('Diagnostico', '', null, ''),
            ('CodigosPruebas', '', null, '');
    END IF;
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'TableroTomaDeMuestra')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('TableroTomaDeMuestra', 'False');
    END IF;
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'TableroProductividadPorSeccion')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('TableroProductividadPorSeccion', 'False');
    END IF;
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'TableroTiempoDeOportunidad')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('TableroTiempoDeOportunidad', 'False');
    END IF;
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'TableroValidacion')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('TableroValidacion', 'False');
    END IF;
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'TableroCalificacionDelServicio')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('TableroCalificacionDelServicio', 'False');
    END IF;
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'TableroSeguimientoDePruebas')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('TableroSeguimientoDePruebas', 'False');
    END IF;
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'UrlNodeJsPatient')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('UrlNodeJsPatient', '');
    END IF;
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'IdUsuarioHis')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('IdUsuarioHis', '3');
    END IF;

    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'DigitosCaso')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('DigitosCaso', '4');
    END IF;

    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'TipoNumeroCaso')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('TipoNumeroCaso', 'Diario');
    END IF;
    -- LLAVES DE CONFIGURACI?N PARA INTEGRACI?N CON SIGO
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'IntegracionSiigo')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES ('IntegracionSiigo', 'False');
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES ('UrlSiigo', '');
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES ('SiigoUserName', '');
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES ('SiigoAccessKey', '');
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES ('SiigoCentralSystem', '');
    END IF;
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'UrlApiFacturacion')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('UrlApiFacturacion', '');
    END IF;
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'SiigoIdPagoContado')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('SiigoIdPagoContado', '');
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('SiigoIdPagoCredito', '');
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('SiigoCodigoComprobante', ''); -- Id del tipo de comprobante para la factura
    END IF;
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'SiigoCodigoComprobanteNC')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('SiigoCodigoComprobanteNC', ''); -- Id del tipo de comprobante para la nota credito
    END IF;


    IF NOT EXISTS(SELECT 1 FROM lab82 WHERE lab82c2 = 'Pat�logo')
    THEN
        INSERT INTO lab82 (lab82c2, lab82c3, lab82c4, lab04c1, lab07c1) VALUES('Pat�logo', 1, now(), 1, 1);
    END IF;
   
  

    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'DiasLaborales')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('DiasLaborales', '1,2,3,4,5');
    END IF;

    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'WidgetProcesador')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('WidgetProcesador', '5');
    END IF;
    
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'AdicionClientesIngreso')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('AdicionClientesIngreso', 'False');
    END IF;

    -- INTEGRACI?N FACTURACION EXTERNA
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'IntegracionFacturacionExterna')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('IntegracionFacturacionExterna', 'False');
    END IF;
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'UrlFacturacionExterna')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('UrlFacturacionExterna', '');
    END IF;
    -- USUARIO QUE FACTURA DESDE EL API DE FACTURACION
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'ApiFacturacionIdUsuario')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('ApiFacturacionIdUsuario', '');
    END IF;
    -- DESCUENTOS POR PRUEBA
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'DescuentoPorPrueba')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('DescuentoPorPrueba', 'False');
    END IF;
    -- MANEJO DE IMPUESTO GENERAL
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'ManejoDescuentoGeneral')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('ManejoDescuentoGeneral', 'True');
    END IF;
    -- ITEM DEMOGRAFICO DE GESTACI?N - RESOLUCI?N 4505
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'idItemDemoGestacion')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('idItemDemoGestacion', '0');
    END IF;
    -- ITEM DEMOGRAFICO DE EPS_ARS - RESOLUCI?N 4505
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'idItemDemoEpsArs')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('idItemDemoEpsArs', '0');
    END IF;
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'verAnalitosDelPerfil')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('verAnalitosDelPerfil', 'False');
    END IF;
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'verPaquetePorAreaInforme')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('verPaquetePorAreaInforme', 'False');
    END IF;
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'validarSesiones')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('validarSesiones', 'False');
    END IF;

    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'verOrdenHis')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('verOrdenHis', 'False');
    END IF;

    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'clientePorDefecto')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('clientePorDefecto', '');
    END IF;
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'SedeHomebound')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('SedeHomebound', '');
    END if;
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'rutaLogHisHmb')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('rutaLogHisHmb', '');
    END if;
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'FechaActualizaci�n')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('FechaActualizaci�n', '');
    END if;
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'EntrevistaEnDestinos')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('EntrevistaEnDestinos', '');
    END if;
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'correoModificacionResultados')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('correoModificacionResultados', '');
    END if;
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'usuarioTribunalElectoral')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('usuarioTribunalElectoral', '');
    END if;
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'urlTribunalElectoral')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('urlTribunalElectoral', '');
    END if;
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'contrasenaTribunalElectoral')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('contrasenaTribunalElectoral', '');
    END if;
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'DiasAlarmaUltimaOrden')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('DiasAlarmaUltimaOrden', '');
    END IF;

    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'RestarCopago')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('RestarCopago', 'False');
    END IF;

    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'ExamenesPorDemografico')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('ExamenesPorDemografico', 'False');
    END IF;

    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'ExamenesDemograficos')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('ExamenesDemograficos', '');
    END IF;

    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'HojaTrabajoIngresoOrden')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('HojaTrabajoIngresoOrden', 'False');
    END IF;

   IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'RecuperarContrase�a')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('RecuperarContrase�a', 'False');
    END IF;

   IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'NotificacionAsuntoRecuperacion')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('NotificacionAsuntoRecuperacion', '');
    END IF;

   IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'NotificacionPlantillaRecuperacion')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('NotificacionPlantillaRecuperacion', '');
    END IF;

    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'ConsultarSistemaCentral')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('ConsultarSistemaCentral', 'False');
    END IF;

    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'SistemaCentralListados')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('SistemaCentralListados', '');
    END IF;
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'EnviarCopiaInformes')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('EnviarCopiaInformes', 'False');
    END IF;

    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'PermisoPorUsuarioReportPreliminar')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('PermisoPorUsuarioReportPreliminar', 'False');
    END IF;
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'AutomaticoEliminarFormula')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('AutomaticoEliminarFormula', 'False');
    END IF;
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'AutomaticoModificaFormula')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('AutomaticoModificaFormula', 'False');
    END IF;
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'TemperaturaReporteRemisiones')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('TemperaturaReporteRemisiones', 'False');
    END IF;
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'IntegracionMINSA')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('IntegracionMINSA', 'False');
    END IF;
    -- Idioma reporte resultados
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'IdiomaReporteResultados')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('IdiomaReporteResultados', 'es');
    END IF;
    -- Precios en talon
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'PreciosServicioTalon')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('PreciosServicioTalon', 'False');
    END IF; 
    --Agregar permiso en  ingresos de ordenes de edicion examenes por usuario
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'PermisosEdicionUsuarioIngreso')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('PermisosEdicionUsuarioIngreso', 'False');
    END IF;  
    --Agregar tipo de precio con el que se genera la factura
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'PrecioFactura')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('PrecioFactura', '1');
    END IF;         
    --Agregar permiso de editar ordenes con caja en Tarifas y caja
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'EditarOrdenCaja')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('EditarOrdenCaja', 'False');
    END IF;

     --Agregar permiso para editar tipo de orden
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'EditarTipoOrden')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('EditarTipoOrden', 'False');
    END IF;

    --Agregar caja automaticamente en ingreso de ordenes
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'CajaAutomatica')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('CajaAutomatica', 'False');
    END IF;
   --Agregar permiso para ordenar las entrevistas por la prioridad de la entrevista 
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'OrdenEntrevistaPorPrioridad')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('OrdenEntrevistaPorPrioridad', 'False');
    END IF;
    --Agregar permiso para agregar notas al examen
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'AddNotes')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('AddNotes', 'False');
    END IF; 
    --Agregar permiso para modificar empresa y tarifa de una orden que ya tiene caja
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'EditarEmpresa')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('EditarEmpresa', 'False');
    END IF;   
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'AreaEpidemiologica')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('AreaEpidemiologica', '');
    END IF;
    --Imprimir resultados desde ingreso de ordenes
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'ImprimirIngresoOrdenes')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('ImprimirIngresoOrdenes', 'False');
    END IF; 
    --Permitir ingresar el copago en porcentaje
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'CopagoPorcentaje')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('CopagoPorcentaje', 'False');
    END IF;

    --Permitir ingresar el copago en porcentaje
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'DestinoVerificaMicrobiologia2')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('DestinoVerificaMicrobiologia2', '');
    END IF;

    --Permitir identificar como se genera el pdf
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'GenerarPDFCon')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('GenerarPDFCon', '1');
    END IF;

    --INVERTIR GRADILLA
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'invertirGradilla')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('invertirGradilla', 'True');
    END IF;

  --PERMITIR AGREGAR Y QUITAR EXAMENES EN SERVICIOS
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'AgregarExamenesServicios')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('AgregarExamenesServicios', 'False');
    END IF;

    --A?OS DE CONSULTA
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'AniosConsultas')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('AniosConsultas', '0');
    END IF;

    --PERMITE MOFICAR LOS EXAMENES EN ENTRADA 
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'ModificarExamenesEntrada')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('ModificarExamenesEntrada', 'False');
    END IF;
   
    -- DEMOGRAFICO TABLEROS
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'demoTableros')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('demoTableros', '0');
    END IF;

    -- ITEM DEMOGRAFICO TABLEROS
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'itemDemoTableros')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('itemDemoTableros', '0');
    END IF;    
    -- Integraci�n tribunal
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'IntegracionTribunal')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('IntegracionTribunal', 'False');
    END IF;
    -- ENVIO DE CORREO A CLIENTE EN INFORMES
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'EnvioCorreoCliente')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('EnvioCorreoCliente', 'False');
    END IF;
    -- CREACION DE ARCHIVO POR EMAIL ENVIADO
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'crearArchivoPorEmail')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('crearArchivoPorEmail', '0');
    END IF;

    -- EDITAR CANTIDAD DE ETIQUETAS EN INGRESO DE ORDENES
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'EditarCantidadEtiquetas')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('EditarCantidadEtiquetas', 'False');
    END IF;

    -- URL EXPORT DATA VIEW
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'UrlExportarInformacionExcel')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('UrlExportarInformacionExcel', '');
    END IF;

    -- SIN SELECCION DE SEDE EN LOGIN
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'SinSeleccionSede')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('SinSeleccionSede', 'False');
    END IF;

    -- QUITAR EL FILTRO DE SEDES EN INGREDO DE ORDENES EN BUSQUEDA DE ORDENES
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'SinFiltroSedeBusquedaOrdenes')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('SinFiltroSedeBusquedaOrdenes', 'False');
    END IF;

    -- SELLO FRESCO
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'SelloFresco')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('SelloFresco', 'False');
    END IF;

    -- CARGO DE DOMICILIO
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'CargoDomicilio')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('CargoDomicilio', 'False');
    END IF;

    -- MEDICOS AUXILIARES
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'MedicosAuxiliares')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('MedicosAuxiliares', 'False');
    END IF;

    -- TOTAL MEDICOS AUXILIARES
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'TotalMedicosAuxiliares')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('TotalMedicosAuxiliares', '');
    END IF;
   -- AGREGAR OBSERVACIONES
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'VerObservaciones')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('VerObservaciones', 'False');
    END IF;

    -- TRANSMITIR EN COMENTARIO
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'TransmitirComentario')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('TransmitirComentario', 'False');
    END IF;

    --conteno login fallido
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'ConteoLoginFallido')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('ConteoLoginFallido', 'True');
    END IF;

    -- Demografico de lector de cedula
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'Lectordecedulademografico')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('Lectordecedulademografico', '0');
    END IF;

    -- Historico de demograficos en ingreso de ordenes
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'HistoricoDemograficos')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('HistoricoDemograficos', 'False');
    END IF;

   -- Direccion Obligatorio
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'Direccionobligatoria')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('Direccionobligatoria', 'False');
    END IF;
   --Literales obligatorio
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'Literalesobligatorio')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('Literalesobligatorio', 'False');
    END IF;
   -- Validación en Ingreso cuando se da un resultado
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'Validacioningreso')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('Validacioningreso', 'False');
    END IF;

    -- Validación permiso de edicion de resultados en Usuarios
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'Agregaredicionresultadosusuario')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('Agregaredicionresultadosusuario', 'False');
    END IF;

    -- Deshabilitar pestañas en listados Impresión de codigo de barras
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'Ptcodigobarras')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('Ptcodigobarras', 'False');
    END IF;


    -- Deshabilitar pestañas en listados laboratorios Externos
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'Ptlaboratoriosexternos')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('Ptlaboratoriosexternos', 'False');
    END IF;

    -- Deshabilitar pestañas en listados MuestrasPtlaboratoriosexternos
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'Ptmuestras')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('Ptmuestras', 'False');
    END IF;
   -- Deshabilitar pestañas en listados Exámenes Pendientes
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'Ptexamenespendientes')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('Ptexamenespendientes', 'False');
    END IF;
    -- Deshabilitar pestañas en listados Pruebas por Sede
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'Ptpruebasporsede')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('Ptpruebasporsede', 'False');
    END IF;
   -- Deshabilitar pestañas en listados Remisiones
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'Ptremisiones')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('Ptremisiones', 'False');
    END IF;

   -- Deshabilitar botones en registro de resultados Inconsistencias
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'Btninconsistencias')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('Btninconsistencias', 'False');
    END IF;

   -- Deshabilitar botones en registro de resultados Adjuntos
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'Btnadjuntos')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('Btnadjuntos', 'False');
    END IF;

    -- Deshabilitar botones en registro de resultados Repeticiones
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'Btnrepeticiones')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('Btnrepeticiones', 'False');
    END IF;
    -- Deshabilitar botones en registro de resultados Consulta de repeticiones
   IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'Btnconsultarepeticiones')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('Btnconsultarepeticiones', 'False');
    END IF;
    
   -- ITEM POR DEFEECTO DE SERVICIO
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'servicioPorDefecto')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('servicioPorDefecto', 'False');
    END IF; 

   -- Deshabilitar botones en registro de resultados Resultados retrasados
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'Btnresultadosretrasados')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('Btnresultadosretrasados', 'False');
    END IF;

   -- Deshabilitar botones en registro de resultados Antibiograma
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'Btnantibiograma')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('Btnantibiograma', 'False');
    END IF;
   -- Deshabilitar botones en registro de resultados Código de barras
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'Btncodigodebarras')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('Btncodigodebarras', 'False');
    END IF;

     -- GUARDAR LA CONFIGURACION DE LA IMPRESION
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'GuardarConfiguracionImpresion')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('GuardarConfiguracionImpresion', 'False');
    END IF;
    -- MANEJA CUOTA MODERADORA
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'CuotaModeradora')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('CuotaModeradora', 'False');
    END IF;

    -- MANEJA SALDO COMPAÑIA
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'SaldoCompania')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('SaldoCompania', 'False');
    END IF;
    
   --INTEGRACION SISTEMA CENTRAL PANAMA
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'ActivarCentralPanama')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('ActivarCentralPanama', 'False');
    END IF;
    --URL DASHBOARD
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'UrlsistemaCentralPanama')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('UrlsistemaCentralPanama', '');
    END IF;

    --Validar Paciente
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'ValidarPaciente')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('ValidarPaciente', 'False');
    END IF;

    --URL Cubo Estadistico
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'URLCuboEstadistico')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('URLCuboEstadistico', '');
    END IF;

    --ActivarCitas
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'ActivarCitas')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('ActivarCitas', 'False');
    END IF;

    -- servicio del cita
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'CitaServicio')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('CitaServicio', '0');
    END IF;
    
    -- tipo de oden de la cita
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'CitaTipodeorden')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('CitaTipodeorden', '0');
    END IF; 

     --LLAVES PARA RANGO DE FECHAS PARA AGENDAR CITAS
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'RangoInicialCitas')
    THEN
       INSERT INTO lab98 (lab98c1, lab98c2) VALUES
        ('RangoInicialCitas', '00:00'),
        ('RangoFinalCitas', '23:30');
    END IF;  

    --Citas  
    IF NOT EXISTS(SELECT 1 FROM lab80 where lab80c1 = 66)      
    THEN
      INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (66, 14, null, 'Reprogramación de la cita', 'Reschedule the appointment');
    END IF;  

    IF NOT EXISTS(SELECT 1 FROM lab80 where lab80c1 = 67)      
    THEN
     INSERT INTO lab80(lab80c1, lab80c2, lab80c3, lab80c4, lab80c5) VALUES (67, 14, null, 'Cancelación de la cita', 'Appointment cancellation');   
    END IF;    

    -- Agregar pruebas al reporte de resultado
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'AgregaPruebaEditoreportes')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('AgregaPruebaEditoreportes', 'False');
    END IF;

    -- Sede en que se van Agregar pruebas al reporte resultados en el editopr de reportes
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'AgregaPruebaEditorDemo')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('AgregaPruebaEditorDemo', '0');
    END IF; 

    -- area en que se van Agregar pruebas al reporte resultados en el editopr de reportes
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'AgregaPruebaEditorArea')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('AgregaPruebaEditorArea', '0');
    END IF; 

    --EMAIL
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'SmtpAuthUserAppointment')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('SmtpAuthUserAppointment', 'testing.cltech@gmail.com'),
            ('SmtpHostNameAppointment', 'smtp.gmail.com'),
            ('SmtpPasswordUserAppointment', 'hdbtvgmuosqaoiyt'),
            ('SmtpPortAppointment', '465'),
            ('SmtpSSLAppointment', 'SSL'),
            ('SmtpProtocolAppointment', 'smtp'),
            ('ServidorCorreoAppointment', 'OUTLOOK');
    END IF;

    -- Area en que se van Agregar pruebas al reporte resultados en el editor de reportes
    IF NOT EXISTS(SELECT 1 FROM lab98 WHERE lab98c1 = 'ComentarioRegistroBloqueado')
    THEN
        INSERT INTO lab98 (lab98c1, lab98c2) VALUES
            ('ComentarioRegistroBloqueado', 'Bloqueado');
    END IF;
   

    --ACTUALIZACION DE VERSION
    UPDATE lab98 SET lab98c2 = '1.0.2' WHERE lab98c1 = 'Version';
    UPDATE lab98 SET lab98c2 = '20240314' WHERE lab98c1 = 'FechaVersion';

    RETURN 0;
END;
$$ LANGUAGE plpgsql;
select script();