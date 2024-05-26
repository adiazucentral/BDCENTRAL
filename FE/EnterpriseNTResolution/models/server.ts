
import express from 'express';
import cors from "cors";
import * as https from 'https';
import * as fs from 'fs';
import * as util from 'util';
import cron from "node-cron";

import demographicsRoutes from "../routes/masters/demographics/demographics";
import validatePatientRoutes from "../routes/tools/validatepatient";
import statisticsRoutes from "../routes/statistics/statistics";
import cubeRoutes from "../routes/statistics/statisticalcube";
import emailRoutes from "../routes/tools/emai";

import { db, dbStat, dbDocs } from '../db/conection';
import { toISOStringLocal } from '../tools/common';
import { sendDataCentralBD } from '../db/tools/central/central';

const readFile = util.promisify(fs.readFile);

class Server {

    private app; 
    private port: number;
    private apiPaths = {
        statistics:             '/api/statistics',
        validatepatient:        '/api/validatepatient',
        cube:                   '/api/cube',
        demographics:           '/api/demographics',
        email:           '/api/email',
    }

    constructor() {
        this.app = express();
        this.port = process.env.PORT === undefined ? 5201 : +process.env.PORT;

        //Conexion a la bd
        this.dbConnection();
        this.dbConnectionStat();
        this.dbConnectionDocs();

        //Middlewares;
        this.middlewares();

        //Rutas
        this.routes();
    }

    middlewares() {
        //CORS
        this.app.use( cors() );
        this.app.use(express.json({limit: '50mb'}));
    }

    routes() {
        this.app.use( this.apiPaths.statistics, statisticsRoutes );
        this.app.use( this.apiPaths.validatepatient, validatePatientRoutes );
        this.app.use( this.apiPaths.cube, cubeRoutes );
        this.app.use( this.apiPaths.demographics, demographicsRoutes );
        this.app.use( this.apiPaths.email, emailRoutes );
    }

    async dbConnection() {
        try {
            await db.authenticate();
            console.log('Conexión exitosa con la base de datos');
            //cron
            this.loadCron();
        } catch(error) {
            console.log('Problemas de conexión con la base de datos', error);
        }
    }

    async dbConnectionStat() {
        try {
            await dbStat.authenticate();
            console.log('Conexión exitosa con la base de datos de estadisticas');
        } catch(error) {
            console.log('Problemas de conexión con la base de datos de estadisticas', error);
        }
    }

    async dbConnectionDocs() {
        try {
            await dbDocs.authenticate();
            console.log('Conexión exitosa con la base de datos de documentos');
        } catch(error) {
            console.log('Problemas de conexión con la base de datos de documentos', error);
        }
    }

    

    loadCron()  {
        cron.schedule('* * * * *', () => {
            console.log(`Ejecución de cron : `, toISOStringLocal(new Date()));
            sendDataCentralBD()
        });
    }

    async listen() {
        /*Con certificado*/
        // const [key, cert] = await Promise.all([
        //     readFile('./certificado/lab.laboratorioadl.com-key.pem'),
        //     readFile('./certificado/lab.laboratorioadl.com-cert.pem')
        // ]);

        // https.createServer({ key, cert }, this.app).listen(this.port, () => {
        //     console.log('Servidor corriendo en puerto', this.port );
        // });

        /*Sin certificado*/
        this.app.listen( this.port, () => {
            console.log('Servidor corriendo en puerto', this.port );
        });
 
    }
}

export default Server;
