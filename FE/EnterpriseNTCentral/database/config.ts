import mongoose from 'mongoose';

const dbConnection = async () => {
    try {
        const url = `mongodb://${process.env.USER}:${process.env.PASSWORD}@${process.env.HOST}:${process.env.PORTDB}/${process.env.DATABASE}`;
        await mongoose.connect(url);
        console.log('Conexión a MongoDB exitosa');
    } catch (error) {
        console.error('Error al conectar a MongoDB:', error);
    }
};

export default dbConnection;