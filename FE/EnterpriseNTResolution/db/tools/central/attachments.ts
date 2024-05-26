import { dbDocs } from "../../conection";
import * as fs from 'fs';

export const getAttachmentsByTest = async (results: Result[], year: number) => {

    for (const result of results) {

        let attachmentsResponse: any = await getByTest(result.lab22c1, result.lab39.lab39c1);

        if (attachmentsResponse.length > 0) {

            let attachments: Attachment[] = [];

            for (const response of attachmentsResponse) {
                await processAttachment(response).then( resolve => {
                    attachments.push(resolve);
                });
            }

            result.doc02 = attachments;
        }

    }

}

const getByTest = async (idOrder: bigint, idTest: number) => {

    let query = " ";

    query += ` SELECT lab22c1, lab39c1, doc02c1, doc02c3, doc02c4, doc02c5, doc02c6, lab04c1, doc02c7 `;

    let from = ` `;

    from += ` FROM doc02 `;

    from += ` WHERE lab22c1 = ${idOrder} AND lab39c1 = ${idTest} `;

    const [templates] = await dbDocs.query(query + from);

    return templates;
}

const processAttachment = async (response: Attachment) => {
    return new Promise<Attachment>((resolve, reject) => {
        const filePath: string = response.doc02c7;

        fs.readFile(filePath, (err: NodeJS.ErrnoException | null, data: Buffer) => {
            let attachment: Attachment = {
                lab22c1 : response.lab22c1,
                lab39c1 : response.lab39c1,
                doc02c1 : response.doc02c1,
                doc02c3 : response.doc02c3,
                doc02c4 : response.doc02c4,
                doc02c5 : response.doc02c5,
                doc02c6 : response.doc02c6,
                lab04c1 : response.lab04c1,
                doc02c7 : response.doc02c7,
                file    : "",
                routeBD : ""
            };
            if (err) {
                console.error('Error al leer el archivo:', err);
            } else {
                const base64Data: string = data.toString('base64');
                attachment.file = base64Data;
            }
            resolve(attachment);
        });
  
    });
}
