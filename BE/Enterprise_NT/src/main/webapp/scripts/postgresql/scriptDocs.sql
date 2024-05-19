--Script para la creacion y actualizacion de la base de datos transaccional de PostgreSQL
CREATE OR REPLACE FUNCTION script() RETURNS integer AS $$
BEGIN
    --Documentos de la Orden
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'doc01') 
    THEN  
        CREATE TABLE doc01 
        (
            lab22c1 BIGINT NOT NULL , 
            doc01c1 VARCHAR(128) NOT NULL , 
            doc01c2 BYTEA NOT NULL , 
            doc01c3 TIMESTAMP NOT NULL , 
            doc01c4 VARCHAR(16) NOT NULL , 
            doc01c5 VARCHAR(4) NOT NULL , 
            lab04c1 INTEGER NOT NULL 
        );

        ALTER TABLE doc01 ADD CONSTRAINT doc01_pk PRIMARY KEY ( lab22c1,doc01c1 );    
    END IF;

    --Documentos de Resultados
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'doc02') 
    THEN 
        CREATE TABLE doc02 
        (
            lab22c1 BIGINT NOT NULL , 
            lab39c1 INTEGER NOT NULL , 
            doc02c1 VARCHAR(128) NOT NULL , 
            doc02c2 BYTEA NOT NULL , 
            doc02c3 TIMESTAMP NOT NULL , 
            doc02c4 VARCHAR(16) NOT NULL , 
            doc02c5 VARCHAR(4) NOT NULL , 
            lab04c1 INTEGER NOT NULL 
        );

        ALTER TABLE doc02 ADD CONSTRAINT doc02_pk PRIMARY KEY ( lab22c1,doc02c1,lab39c1 );
    END IF;

    --Imagenes de analizador
    IF NOT EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'doc03') 
    THEN 
        CREATE TABLE doc03 
        (
            lab22c1 BIGINT NOT NULL , -- Numero de orden
            lab39c1 INTEGER NOT NULL , -- Id de examen
            doc03c1 VARCHAR(8) NOT NULL , -- Nombre de imagen 1
            doc03c2 BYTEA NOT NULL , -- Imagen 1
            doc03c3 VARCHAR(8) , -- Nombre de imagen 2
            doc03c4 BYTEA , -- Imagen 2
            doc03c5 VARCHAR(8) , -- Nombre de imagen 3
            doc03c6 BYTEA , -- Imagen 3
            doc03c7 VARCHAR(8) , -- Nombre de imagen 4
            doc03c8 BYTEA  , -- Imagen 4
            doc03c9 VARCHAR(8) , -- Nomnbre de imagen 5
            doc03c10 BYTEA  -- Imagen 5
        );

        ALTER TABLE doc03 ADD CONSTRAINT doc03_pk PRIMARY KEY ( lab22c1,lab39c1 );
    END IF;
    --Imagenes de analizador
    IF EXISTS(SELECT 1 FROM information_schema.tables WHERE table_name = 'doc03') 
    THEN 
        ALTER TABLE doc03 ALTER COLUMN doc03c1 TYPE varchar(64);
        ALTER TABLE doc03 ALTER COLUMN doc03c3 TYPE varchar(64);
        ALTER TABLE doc03 ALTER COLUMN doc03c5 TYPE varchar(64);
        ALTER TABLE doc03 ALTER COLUMN doc03c7 TYPE varchar(64);
        ALTER TABLE doc03 ALTER COLUMN doc03c9 TYPE varchar(64);
    END IF;
     --Modificacion de la tabla doc01 se adiciona el campo para validar si se imprime o nop
   IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='doc01' AND column_name='doc01c6')
    THEN      
         ALTER TABLE doc01 ADD COLUMN doc01c6 smallint;
    END IF;
  --Modificacion de la tabla doc02 se adiciona el campo para validar si se imprime o nop
   IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='doc02' AND column_name='doc02c6')
    THEN      
         ALTER TABLE doc02 ADD COLUMN doc02c6 smallint;
    END IF;
    -- Adicion campo para almacenar la ruta del adjunto
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='doc02' AND column_name='doc02c7')
    THEN      
         ALTER TABLE doc02 ADD COLUMN doc02c7 varchar(200);
    END IF;

     -- Adicion campo para almacenar la ruta del adjunto
    IF NOT EXISTS (SELECT * FROM information_schema.columns WHERE table_name='doc01' AND column_name='doc01c7')
    THEN      
         ALTER TABLE doc01 ADD COLUMN doc01c7 varchar(200);
    END IF;

    ALTER TABLE doc01 ALTER COLUMN doc01c2 BYTEA NULL

    RETURN 0;
END;
$$ LANGUAGE plpgsql;
select script();


