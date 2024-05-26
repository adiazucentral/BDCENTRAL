
import express from 'express';
import cors from "cors";
import * as https from 'https';
import * as fs from 'fs';
import * as util from 'util';
import dbConnection from '../database/config';
import orderRoutes from "../routes/central/order";
import patientRoutes from "../routes/central/patient";
import resultRoutes from "../routes/central/result";

const readFile = util.promisify(fs.readFile);

class Server {

    private app; 
    private port: number;
    private apiPaths = {
        order       : '/api/order',
        patient     : '/api/patient',
        result      : '/api/result',
    }

    constructor() {
        this.app = express();
        this.port = process.env.PORT === undefined ? 5200 : +process.env.PORT;

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
