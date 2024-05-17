--Script para la creacion y actualizacion de la base de datos transaccional de SQLServer
--Configuración
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[Lab99]') )
BEGIN
    CREATE TABLE lab99 (
        lab99c1 VARCHAR (64) NOT NULL ,
        lab99c2 TEXT
    )
    ALTER TABLE lab99 ADD CONSTRAINT lab99_PK PRIMARY KEY CLUSTERED (lab99c1) WITH (ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
--SEGMENT
--USUARIOS
IF NOT EXISTS (select 1 from dbo.sysobjects where id = object_id(N'[Lab88]') )
BEGIN
    CREATE TABLE lab88
    (
        lab88c1 INTEGER NOT NULL ,
        lab88c2 VARCHAR (1024) NOT NULL ,
        lab88c3 INTEGER NOT NULL ,
        lab88c4 TEXT NOT NULL ,
        lab88c5 TINYINT NOT NULL
    );
    ALTER TABLE lab88 ADD CONSTRAINT lab88_PK PRIMARY KEY CLUSTERED (lab88c1) WITH ( ALLOW_PAGE_LOCKS = ON , ALLOW_ROW_LOCKS = ON )
END
ALTER TABLE lab99 ALTER COLUMN lab99c2 TEXT;
IF NOT EXISTS(SELECT 1 FROM lab99 WHERE lab99c1 = 'Titulo') 
BEGIN
    INSERT INTO lab99 (lab99c1, lab99c2) VALUES
            --GENERAL
            ('Titulo', ''),
            ('Logo', ''),
            ('Banner', ''),
            ('Color', ''),
            ('Informacion', ''),
            ('Informacion2', ''),
            ('URL', ''),
            ('Historico', 'False'),
            ('HistoricoGrafica', 'False'),
            ('HistoricoCombinado', 'False'),
            ('Captcha', 'False'),
            ('CambioContraseña', 'False'),
            ('ValidaSaldoPendiente', 'False'),
            ('BusquedaOrden', 'False'),
            ('MostrarConfidenciales', 'False'),
            ('BloqueaPanicos', '0'),--0 - No bloquea, 1 - Bloquea Exámen, 2 - Bloquea Orden
            ('TerminosCondiciones', ''),
            ('MostrarResultado', '1'), -- 1 - Ingresados, 2 - Verificados
            ('LlaveCaptcha', ''),
            ('ServiciosLISUrl', ''),
            ('FondoLogin', '');
END
IF NOT EXISTS(SELECT 1 FROM lab88 WHERE lab88c1 = 1) 
BEGIN
    INSERT INTO lab88 (lab88c1, lab88c2, lab88c3, lab88c4, lab88c5) VALUES
        (1, '', 0, '', 0), --Medico
        (2, '', 0, '', 0), --Paciente
        (3, '', 0, '', 0), --Cliente
        (4, '', 0, '', 0); --Usuario
END

IF NOT EXISTS(SELECT 1 FROM lab88 WHERE lab88c1 = 6) 
BEGIN
    INSERT INTO lab88 (lab88c1, lab88c2, lab88c3, lab88c4, lab88c5) VALUES
        (6, '', 0, '', 0); --Usuario
END
IF NOT EXISTS(SELECT 1 FROM lab99 WHERE lab99c1 = 'SmtpAuthUser') 
BEGIN
    INSERT INTO lab99 (lab99c1, lab99c2) VALUES
            ('SmtpAuthUser', 'testing.cltech@gmail.com'),
            ('SmtpHostName', 'smtp.gmail.com'),
            ('SmtpPasswordUser', 'testingcltech'),
            ('SmtpPort', '465'),
            ('SmtpSSL', 'SSL');
END
IF NOT EXISTS(SELECT 1 FROM lab99 WHERE lab99c1 = 'CuerpoEmail') 
BEGIN
    INSERT INTO lab99 (lab99c1, lab99c2) VALUES
            ('CuerpoEmail', '');
END
IF NOT EXISTS(SELECT 1 FROM lab88 WHERE lab88c1 = 5) 
BEGIN
    INSERT INTO lab88 (lab88c1, lab88c2, lab88c3, lab88c4, lab88c5) VALUES
        (5, '', 0, '', 0); --Demografico dinamico
END
IF NOT EXISTS(SELECT 1 FROM lab99 WHERE lab99c1 = 'ServidorCorreo') 
BEGIN
    INSERT INTO lab99 (lab99c1, lab99c2) VALUES
            ('ServidorCorreo', '');
END
IF NOT EXISTS(SELECT 1 FROM lab99 WHERE lab99c1 = 'PathFE')
BEGIN
    INSERT INTO lab99 (lab99c1, lab99c2) VALUES
        ('PathFE', '');
END;
IF NOT EXISTS(SELECT 1 FROM lab99 WHERE lab99c1 = 'FondoLogin')
BEGIN
    INSERT INTO lab99 (lab99c1, lab99c2) VALUES
        ('FondoLogin', '');
END;
--- Protocolo Smtp
IF NOT EXISTS(SELECT 1 FROM lab99 WHERE lab99c1 = 'SmtpProtocol')
BEGIN
    INSERT INTO lab99 (lab99c1, lab99c2) VALUES
            ('SmtpProtocol', 'smtp');
END;

IF NOT EXISTS(SELECT 1 FROM lab99 WHERE lab99c1 = 'directReportPrint')
BEGIN
    INSERT INTO lab99 (lab99c1, lab99c2) VALUES
        ('directReportPrint', 'False');
END;

ALTER TABLE lab19 ALTER COLUMN lab19c17 VARCHAR(256);

--Ver confidenciales
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab88' AND column_name='lab88c6')
BEGIN
    ALTER TABLE lab88 ADD lab88c6 SMALLINT NOT NULL DEFAULT 0;
END

IF NOT EXISTS(SELECT 1 FROM lab99 WHERE lab99c1 = 'FiltroRangoFecha')
BEGIN
    INSERT INTO lab99 (lab99c1, lab99c2) VALUES
        ('FiltroRangoFecha', 'False');
END;

IF NOT EXISTS(SELECT 1 FROM lab99 WHERE lab99c1 = 'EmpaquetarOrdenesCliente')
BEGIN
    INSERT INTO lab99 (lab99c1, lab99c2) VALUES
        ('EmpaquetarOrdenesCliente', 'False');
END;

--Ver SOLO EXAMENES VALIDADOS
IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab88' AND column_name='lab88c7')
BEGIN
    ALTER TABLE lab88 ADD lab88c7 SMALLINT NOT NULL DEFAULT 0;
END
