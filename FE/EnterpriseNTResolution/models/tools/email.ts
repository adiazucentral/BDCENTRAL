import nodemailer from 'nodemailer';
import moment from 'moment';
import { getValueKey } from '../../tools/common';
import { insertAudit } from '../../db/tools/audit/auditemail';

export const sendEmailResults = async (email: Email) => {
    return new Promise(async (resolve, reject) => {

        const typeServer = await getValueKey('ServidorCorreo');
        const host = await getValueKey('SmtpHostName');
        const port = await getValueKey('SmtpPort');
        const auth = await getValueKey('SmtpAuthUser');
        const passwordEmail = await getValueKey('SmtpPasswordUser');

        const attachments: any = [];
        if (email.attachment !== undefined && email.attachment.length > 0) {
            email.attachment.forEach((value: Image) => {
                if (value.type === '1') {
                    attachments.push({
                        filename: value.filename,
                        content: Buffer.from(value.path, 'base64'),
                        encoding: 'base64'
                    });
                }
            });
        }

        const recipients: string[] = [];

        email.recipients.forEach((value: string) => {
            if (value) {
                recipients.push(value.replace(/\s/g, '').replace(/\[|\]/g, ''))
            }
        });

        const message = {
            from: auth,
            to: recipients.join(', '),
            subject: email.subject,
            html: email.body,
            attachments: attachments,
        };

        if (typeServer !== 'OUTLOOK') {
            let transporter = nodemailer.createTransport({
                host: host,
                port: port,
                secure: false, 
                auth: {
                    user: auth,
                    pass: passwordEmail,
                },
            });

            transporter.sendMail(message, (error, info) => {
                if (error) {
                    console.error('Error al enviar el correo:', error);
                    resolve("Se a generado un error a la hora de hacer el envio");
                } else {
                    insertAuditEmail(email);
                    console.log('Correo enviado con éxito:', info.response);
                    resolve(true);
                }
            });
        } else {
            let transporter = nodemailer.createTransport({
                service: "Outlook365",
                auth: {
                    user: auth,
                    pass: passwordEmail,
                },
            });

            transporter.sendMail(message, (error, info) => {
                if (error) {
                    console.error('Error al enviar el correo:', error);
                    resolve("Se a generado un error a la hora de hacer el envio");
                } else {
                    insertAuditEmail(email);
                    console.log('Correo enviado con éxito:', info.response);
                    resolve(true);
                }
            });
        }

    });
}

export const sendEmailAppointment = async (email: Email) => {
    return new Promise(async (resolve, reject) => {

        const typeServer = await getValueKey('ServidorCorreoAppointment');
        const host = await getValueKey('SmtpHostNameAppointment');
        const port = await getValueKey('SmtpPortAppointment');
        const auth = await getValueKey('SmtpAuthUserAppointment');
        const passwordEmail = await getValueKey('SmtpPasswordUserAppointment');

        const attachments: any = [];
        if (email.attachment !== undefined && email.attachment.length > 0) {
            email.attachment.forEach((value: Image) => {
                if (value.type === '1') {
                    attachments.push({
                        filename: value.filename,
                        content: Buffer.from(value.path, 'base64'),
                        encoding: 'base64'
                    });
                }
            });
        }

        const recipients: string[] = [];

        email.recipients.forEach((value: string) => {
            if (value) {
                recipients.push(value.replace(/\s/g, '').replace(/\[|\]/g, ''))
            }
        });

        const message = {
            from: auth,
            to: recipients.join(', '),
            subject: email.subject,
            html: email.body,
            attachments: attachments,
        };

        if (typeServer !== 'OUTLOOK') {
            let transporter = nodemailer.createTransport({
                host: host,
                port: port,
                secure: false, 
                auth: {
                    user: auth,
                    pass: passwordEmail,
                },
            });

            transporter.sendMail(message, (error, info) => {
                if (error) {
                    console.error('Error al enviar el correo:', error);
                    resolve("Se a generado un error a la hora de hacer el envio");
                } else {
                    insertAuditEmail(email);
                    console.log('Correo enviado con éxito (citas):', info.response);
                    resolve(true);
                }
            });
        } else {
            let transporter = nodemailer.createTransport({
                service: "Outlook365",
                auth: {
                    user: auth,
                    pass: passwordEmail,
                },
            });

            transporter.sendMail(message, (error, info) => {
                if (error) {
                    console.error('Error al enviar el correo:', error);
                    resolve("Se a generado un error a la hora de hacer el envio");
                } else {
                    insertAuditEmail(email);
                    console.log('Correo enviado con éxito:', info.response);
                    resolve(true);
                }
            });
        }

    });
}

export const insertAuditEmail = async (email: Email) => {
    const audit: AuditEmail = {
        recipients: email.recipients.join(','),
        body: email.body,
        attachments: email.attachment !== undefined && email.attachment.length > 0 ? 1 : 0,
        subject: email.subject,
        date: moment().format('YYYY-MM-DD HH:mm:ss'),
        user: email.user,
        order: email.order
    }
    await insertAudit(audit);
}