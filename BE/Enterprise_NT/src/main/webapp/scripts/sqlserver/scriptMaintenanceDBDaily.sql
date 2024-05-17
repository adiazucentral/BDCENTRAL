/********************************************************************************
  ENTERPRISE - Todos los derechos reservados CLTech Ltda.
  PROPOSITO:    Mantenimiento de indices 
  AUTOR:        Jcastaneda http://blogs.solidq.com/jloria/Post.aspx?ID=20
  FECHA:        2011-06-16
********************************************************************************/

/********************************************************************************
  Propuesta de ejecución:
    
  1. Mantenimiento de índices grandes y pequeños, una vez al día en el horario de menos ocupación del servidor. 
     EXEC dbo.DefragmentaIndices 'Grandes'
     EXEC dbo.DefragmentaIndices 'Pequenos'
     
  2. Mantenimiento de índices medianos, una vez cada hora, validando que la ejecución no se realice al mismo tiempo que otra tarea del motor. 
     EXEC dbo.DefragmentaIndices 'Medianos'

 3. Se deben programar las tareas de mantenimiento para las siguientes bases de datos:
     * Enterprise
     * EnterpriseD
     * EnterpriseH
     * EnterpriseEstadisticas (En caso de que se tenga)
********************************************************************************/

if exists (select * from dbo.sysobjects where id = object_id(N'FilteredIndexFragmentation'))
    DROP FUNCTION FilteredIndexFragmentation
--SEGMENT

CREATE FUNCTION [dbo].[FilteredIndexFragmentation](
      @DatabaseID                INT
    , @ObjectID                    INT
    , @IndexID                        INT
    , @PartitionNumber        INT=NULL
    , @AverageFragmentation INT=0 
    , @FragmentCount        BIGINT=0)

-- Author:                         Javier Loria, Solid Quality Mentors 
-- Create date:                    5/Dic/2008 
-- Description:                    Funcion que lista los indices con un porcentaje de fragmentacion LOGICA mayor al indicado, 
-- y con una cantidad mayor de fragmentos. 
-- Encapsula dm_db_index_physical_stats., se requiere para poder hacer CROSS APPLY. 
-- No reporta fragmentacion de tablas sin indices, indices XML o Geograficos. 
-- Emplea el modo limitado 'LIMITED', por el alto costo y mal desempeno del modo 'DETAILED' 

RETURNS @IndexStats TABLE(
      DatabaseID            SMALLINT 
    , ObjectID              INT 
    , IndexID               INT 
    , PartitionNumber            INT 
    , IndexDepth                    TINYINT 
    , FragmentationRate        FLOAT 
    , FragmentCount            BIGINT 
    , AverageFragmentSize FLOAT 
    ,PageCount                    BIGINT)
BEGIN
    INSERT INTO @IndexStats(DatabaseID, ObjectID, IndexID, PartitionNumber, IndexDepth,FragmentationRate 
        , FragmentCount, AverageFragmentSize,PageCount)
    SELECT database_id,object_id, index_id, partition_number,
         index_depth, avg_fragmentation_in_percent, fragment_count, avg_fragment_size_in_pages, page_count 
    FROM sys.dm_db_index_physical_stats(@DatabaseID, @ObjectID, @IndexID, @PartitionNumber,'LIMITED')
    WHERE index_type_desc IN('CLUSTERED INDEX','NONCLUSTERED INDEX')
    AND avg_fragmentation_in_percent > @AverageFragmentation 
    AND fragment_count>@FragmentCount 
    RETURN 
END
--SEGMENT

-- Author:              Javier Loria, Solid Quality Mentors 
-- Create date:         5/Dic/2008 
-- Description:         Procedimiento que defragmenta indices, de una base de datos, de acuerdo al tamaño del indices. 
-- Indices Grandes:     10 Indices de cualquier tamaño, con mas de 30% de Fragmentacion y 10 o más segmentos 
-- Indices Medianos:    10 Indices entre 8192 y 32 páginas, mas del 20% de Fragmentacion y 3 o más segmentos 
-- Indices Pequenos:    100 Indices entre 256 y 32 páginas, , mas del 20% de Fragmentacion y 3 o más segmentos 
-- Parametros:          @Tipo= Grandes, Medianos y Pequenos. Default=Grandes

if exists (select * from dbo.sysobjects where id = object_id(N'DefragmentaIndices'))
    DROP PROCEDURE DefragmentaIndices
	print('paso 1');
--SEGMENT

CREATE PROCEDURE [dbo].[DefragmentaIndices]( @Tipo    VARCHAR(10))        -- Medianos, Pequenos 
AS
BEGIN
    DECLARE @NumPages     BIGINT; 
    DECLARE @NumIndexes INT; 
    DECLARE @Comando NVARCHAR(MAX); 
    DECLARE @DB INT 
    SET NOCOUNT ON;
    SET @DB=DB_ID()        --Requerido por modo de compatibilidad 80. 
    IF (@Tipo NOT IN('Grandes','Medianos','Pequenos')) 
    BEGIN 
        RAISERROR('Parametro @Tipo Invalido, use: Grandes, Medianos o Pequenos', 16,1); 
        RETURN; 
    END
    
    SET @Comando='';
    IF @Tipo='Grandes' 
    BEGIN 
        -- Reindexa las 10 mas grandes sin importar el tamano 
        SELECT TOP 10 @Comando=@Comando+CHAR(13)+CHAR(10)+'ALTER INDEX ' 
        +Indexes.Name 
        +' ON '+OBJECT_NAME(ObjectID)+' REBUILD;'
        FROM FilteredIndexFragmentation(@DB,NULL,NULL,NULL, 30,10)AS FIF 
        JOIN SYS.INDEXES AS Indexes ON INDEXES.OBJECT_ID=ObjectID 
        AND INDEXES.INDEX_ID=IndexID 
        ORDER BY (IndexDepth*IndexDepth*FragmentationRate*FragmentCount/100)DESC
    END 
    ELSE
    BEGIN 
        -- Reindexa las 10 si es Medianos, 50 si es Pequenos 
        SELECT TOP (CASE WHEN @Tipo='Medianos'THEN 10 ELSE 50 END) 
        @Comando=@Comando+CHAR(13)+CHAR(10)+'ALTER INDEX ' 
        + IndexPages.Name 
        +' ON '+OBJECT_NAME(ObjectID)+' REBUILD;'
        FROM (SELECT indexes.object_id 
                ,indexes.index_id 
                ,Indexes.Name 
                ,sum(allocation_units.total_pages)as totalPages 
                FROM sys.indexes AS indexes 
                JOIN sys.partitions AS partitions 
                ON indexes.object_id=partitions.object_id 
                and indexes.index_id =partitions.index_id 
                JOIN sys.allocation_units AS allocation_units 
                ON partitions.partition_id =allocation_units.container_id 
                WHERE indexes.index_id >0 
                AND allocation_units.total_pages>0 
                GROUP BY indexes.object_id,indexes.index_id,Indexes.Name 
                HAVING sum(allocation_units.total_pages)BETWEEN 32 AND
                (CASE WHEN @Tipo='Medianos'THEN 8192 ELSE 256 END)         
            -- Medianos si tienen menos de 8192 paginas, Pequenos si tienen menos de 256 paginas 
            )AS IndexPages 

        -- No se emplea el CROSS APPLY por compatibilidad con nivel de compatibilidad 80 (SQL 2000), 
        -- es posible que tenga un importante impacto en desempeno usar el CROSS APPLY. 
        -- se recomienda usar CROSS APPLY para compatibilidad 90 o 100. 
        --    CROSS APPLY FilteredIndexFragmentation(@DB, IndexPages.object_id, IndexPages.index_id, NULL, 20,3) AS FIF 
        JOIN FilteredIndexFragmentation(@DB,NULL,NULL,NULL, 20,3)AS FIF 
        ON IndexPages.object_id=FIF.ObjectID 
        AND IndexPages.index_id=FIF.IndexID 
        -- La columna IndexDepth esta deliberadamente 2 veces, para dar prioridad a indices mas profundos. 
        ORDER BY (IndexDepth*IndexDepth*FragmentationRate*FragmentCount/100)DESC
    END 
    PRINT 'Intrucciones a ejecutar: '
	PRINT @Comando
	EXEC sp_executesql @Comando 
	PRINT 'Proceso terminado.'
END
--SEGMENT