import { Sequelize } from "sequelize";
const port = process.env.PORTDB || '';
export const db = new Sequelize(process.env.DATABASE || 'EnterpriseNT_CSS', process.env.USER || 'SA', process.env.PASSWPORD || '123456', {
    host: process.env.HOST || 'localhost',
    dialect: process.env.ENGINE === 'sqlserver' ? 'mssql' : 'postgres',
    port: parseInt(port),
    dialectOptions: {
        options: { 
            requestTimeout: 500000,
            instanceName: process.env.INSTANCENAME || 'SQLEXPRESS',
			encrypt: false, 
			trustServerCertificate: true
        }
    },
    logging: process.env.LOGGING === 'true' || false
});

export const dbStat = new Sequelize(process.env.DATABASE_STAT || 'EnterpriseNT_Stat_CSS', process.env.USER || 'SA', process.env.PASSWPORD || '123456', {
    host: process.env.HOST || 'localhost',
    dialect: process.env.ENGINE === 'sqlserver' ? 'mssql' : 'postgres',
    port: parseInt(port),
    dialectOptions: {
        options: { 
            requestTimeout: 500000,
            instanceName: process.env.INSTANCENAME || 'SQLEXPRESS',
			encrypt: false, 
			trustServerCertificate: true
        }
    },
    logging: process.env.LOGGING === 'true' || false
});

export const dbDocs = new Sequelize(process.env.DATABASE_DOCS || 'EnterpriseNT_Docs_CSS', process.env.USER || 'SA', process.env.PASSWPORD || '123456', {
    host: process.env.HOST || 'localhost',
    dialect: process.env.ENGINE === 'sqlserver' ? 'mssql' : 'postgres',
    port: parseInt(port),
    dialectOptions: {
        options: { 
            requestTimeout: 500000,
            instanceName: process.env.INSTANCENAME || 'SQLEXPRESS',
			encrypt: false, 
			trustServerCertificate: true
        }
    },
    logging: process.env.LOGGING === 'true' || false
});

