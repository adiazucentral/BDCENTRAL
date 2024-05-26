import * as fs from 'fs';
import * as path from 'path';

export const processAttachments = async (result: ResultDB) => {
    if (result.lab57c41 > 0 && result.doc02.length > 0) {
        for (let attachment of result.doc02) {
            await saveAttachment(attachment).then(resolve => {
                attachment = resolve;
            });
        }
        return result;
    } else {
        return result;
    }
}


const saveAttachment = async (attachment: Attachment) => {
    return new Promise<Attachment>((resolve, reject) => {
        const base64Data = attachment.file;

        const fileName: string = attachment.doc02c1;

        const route = `Adjuntos/${attachment.lab22c1}/${attachment.lab39c1}`;

        const outputFolder: string = path.join(__dirname, route);

        if (!fs.existsSync(outputFolder)) {
            fs.mkdirSync(outputFolder, { recursive: true });
        }

        const outputPath: string = path.join(outputFolder, fileName);

        const bufferData: Buffer = Buffer.from(base64Data, 'base64');

        fs.writeFile(outputPath, bufferData, (err: NodeJS.ErrnoException | null) => {
            if (err) {
                console.error('Error al escribir el archivo:', err);
                resolve(attachment);
            } else {
                attachment.routeBD = outputPath;
                console.log('Archivo guardado con éxito en:', outputPath);
                resolve(attachment);
            }
        });

    });
}