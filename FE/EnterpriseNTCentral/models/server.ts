
import express from 'express';
import cors from "cors";
import * as https from 'https';
import * as fs from 'fs';
import * as util from 'util';
import dbConnection from '../database/config';
import orderRoutes from "../routes/central/order";
import patientRoutes from "../routes/central/patient";
import resultRoutes from "../routes/central/result";
import templateRoutes from "../routes/cube/templates";
import demographicsRoutes from "../routes/configuration/demographics";
import cubeRoutes from "../routes/cube/cube";
import outreachorderRoutes from "../routes/outreach/outreachorder";
import remissionRoutes from "../routes/remission/remission";

const readFile = util.promisify(fs.readFile);

class Server {

    private app; 
    private port: number;
    private apiPaths = {
        order           : '/api/order',
        patient         : '/api/patient',
        result          : '/api/result',
        template        : '/api/template',
        demographics    : '/api/demographics',
        cube            : '/api/cube',
        outreachorder   : '/api/outreachorder',
        remission       : '/api/remission'
    }

    constructor() {
        this.app = express();
        this.port = process.env.PORT === undefined ? 5200 : + process.env.PORT;

        //Conexion a la bd
        this.connectDatabase();

        //Middlewares;
        this.middlewares();

        //Rutas
        this.routes();
    }

    async connectDatabase() {
        await dbConnection();
    }

    middlewares() {
        //CORS
        this.app.use( cors() );
        this.app.use(express.json({limit: '50mb'}));
    }

    routes() {
        this.app.use( this.apiPaths.order, orderRoutes );
        this.app.use( this.apiPaths.patient, patientRoutes );
        this.app.use( this.apiPaths.result, resultRoutes );
        this.app.use( this.apiPaths.template, templateRoutes );
        this.app.use( this.apiPaths.demographics, demographicsRoutes );
        this.app.use( this.apiPaths.cube, cubeRoutes );
        this.app.use( this.apiPaths.outreachorder, outreachorderRoutes );
        this.app.use( this.apiPaths.remission, remissionRoutes );
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
