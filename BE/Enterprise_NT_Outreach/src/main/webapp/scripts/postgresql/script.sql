--Script para la creacion y actualizacion de la base de datos transaccional de PostgreSQL
CREATE OR REPLACE FUNCTION script() RETURNS integer AS $$
BEGIN
    --Configuración
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab99')
    THEN
        CREATE TABLE lab99 (
            lab99c1   VARCHAR(64) NOT NULL,
            lab99c2   VARCHAR
        );
        ALTER TABLE lab99 ADD CONSTRAINT lab99_pk PRIMARY KEY ( lab99c1 );
    END IF;
    IF NOT EXISTS(SELECT 1 FROM lab99 WHERE lab99c1 = 'Titulo')
    THEN
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
    END IF;
     --USUARIOS
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'lab88')
    THEN
        CREATE TABLE lab88 (
            lab88c1   INTEGER NOT NULL,
            lab88c2   VARCHAR(1024) NOT NULL,
            lab88c3   INTEGER NOT NULL,
            lab88c4   VARCHAR NOT NULL,
            lab88c5   smallint NOT NULL
        );
        ALTER TABLE lab88 ADD CONSTRAINT lab88_pk PRIMARY KEY ( lab88c1 );
    END IF;
    ALTER TABLE lab99 ALTER COLUMN lab99c2 TYPE TEXT;
    -- EMAIL PARA CONSULTA WEB
    IF NOT EXISTS(SELECT 1 FROM lab99 WHERE lab99c1 = 'SmtpAuthUser')
    THEN
        INSERT INTO lab99 (lab99c1, lab99c2) VALUES
            ('SmtpAuthUser', 'testing.cltech@gmail.com'),
            ('SmtpHostName', 'smtp.gmail.com'),
            ('SmtpPasswordUser', 'testingcltech'),
            ('SmtpPort', '465'),
            ('SmtpSSL', 'SSL');
    END IF;
    -- ROLES
    IF NOT EXISTS(SELECT 1 FROM lab88 WHERE lab88c1 = 1)
    THEN
        INSERT INTO lab88 (lab88c1, lab88c2, lab88c3, lab88c4, lab88c5) VALUES
            (1, '', 0, '', 0), --Medico
            (2, '', 0, '', 0), --Paciente
            (3, '', 0, '', 0), --Cliente
            (4, '', 0, '', 0); --Usuario
    END IF;

    IF NOT EXISTS(SELECT 1 FROM lab88 WHERE lab88c1 = 6)
    THEN
        INSERT INTO lab88 (lab88c1, lab88c2, lab88c3, lab88c4, lab88c5) VALUES
            (6, '', 0, '', 0); --Usuario
    END IF;
    -- PLANTILLA DEL CORREO
    IF NOT EXISTS(SELECT 1 FROM lab99 WHERE lab99c1 = 'CuerpoEmail')
    THEN
        INSERT INTO lab99 (lab99c1, lab99c2) VALUES
            ('CuerpoEmail', '');
    END IF;
    -- PLANTILLA DEL CORREO
    IF NOT EXISTS(SELECT 1 FROM lab88 WHERE lab88c1 = 5)
    THEN
        INSERT INTO lab88 (lab88c1, lab88c2, lab88c3, lab88c4, lab88c5) VALUES
            (5, '', 0, '', 0); --Demografico dinamico
    END IF;
    ALTER TABLE lab19 ALTER COLUMN lab19c17 TYPE VARCHAR(256);
    IF NOT EXISTS(SELECT 1 FROM lab99 WHERE lab99c1 = 'ServidorCorreo')
    THEN
        INSERT INTO lab99 (lab99c1, lab99c2) VALUES
            ('ServidorCorreo', '');
    END IF;
    IF NOT EXISTS(SELECT 1 FROM lab99 WHERE lab99c1 = 'PathFE')
    THEN
        INSERT INTO lab99 (lab99c1, lab99c2) VALUES
            ('PathFE', '');
    END IF;
    IF NOT EXISTS(SELECT 1 FROM lab99 WHERE lab99c1 = 'FondoLogin')
    THEN
        INSERT INTO lab99 (lab99c1, lab99c2) VALUES
            ('FondoLogin', '');
    END IF;
  --- Protocolo Smtp
    IF NOT EXISTS(SELECT 1 FROM lab99 WHERE lab99c1 = 'SmtpProtocol')
    THEN
        INSERT INTO lab99 (lab99c1, lab99c2) VALUES
            ('SmtpProtocol', 'smtp');
    END IF;
    IF NOT EXISTS(SELECT 1 FROM lab99 WHERE lab99c1 = 'directReportPrint')
    THEN
        INSERT INTO lab99 (lab99c1, lab99c2) VALUES
            ('directReportPrint', 'False');
    END IF;

    --Permiso para ver confidenciales
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab88' AND column_name='lab88c6')
    THEN
        ALTER TABLE lab88 ADD lab88c6 smallint not null default 0;
    END IF;

    IF NOT EXISTS(SELECT 1 FROM lab99 WHERE lab99c1 = 'FiltroRangoFecha')
    THEN
        INSERT INTO lab99 (lab99c1, lab99c2) VALUES
            ('FiltroRangoFecha', 'False');
    END IF;

    IF NOT EXISTS(SELECT 1 FROM lab99 WHERE lab99c1 = 'EmpaquetarOrdenesCliente')
    THEN
        INSERT INTO lab99 (lab99c1, lab99c2) VALUES
            ('EmpaquetarOrdenesCliente', 'False');
    END IF;
   
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='lab88' AND column_name='lab88c7')
    THEN
        ALTER TABLE lab88 ADD lab88c7 smallint not null default 0;
    END IF;

    RETURN 0;
END;
$$ LANGUAGE plpgsql;
select script();
