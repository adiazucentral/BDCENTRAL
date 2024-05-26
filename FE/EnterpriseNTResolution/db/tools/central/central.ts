import { listDemographics } from "../../masters/demographics/demographics";
import { sendOrdersCentral } from "../../../helpers/central/order/order";
import { getValueKey } from "../../../tools/common";
import { sendPatients } from "./patient";
import { getOrderByYear } from "./order";
import { getResultsByYear } from "./results";
import { sendResultsCentral } from "../../../helpers/central/results/result";
import { getMicrobialDetection } from "./microbiology";
import { getGeneralTemplate } from "./resultsTemplates";
import { getAttachmentsByTest } from "./attachments";
import { getAnalyzerGraphs } from "./graphs";

export const sendDataCentralBD = async () => {
    try {

        //Pacientes
        const demographics  = await listDemographics(1);
        const demosPatient  = demographics.filter( (demo:any) => demo.origin === "H");
        sendPatients(demosPatient);

        //Ordenes
        let numberOfYears = await getValueKey('AniosConsultas');
        let years = getListYears(numberOfYears);
        const demosOrder  = demographics.filter( (demo:any) => demo.origin === "O");
        let parametersOrders = {
            years,
            demographics: demosOrder
        }
        getOrderByYear(parametersOrders, 0, []).then((resp: any) => {
            setTimeout(async () => {
                if(resp.length > 0) {
                    sendOrdersCentral(resp);
                } else {
                    console.log('No existen ordenes para enviar');
                }
            });
        });

        //Resultados 
        let parametersResults = {
            years
        }

        getResultsByYear(parametersResults, 0, []).then((resp: any) => {
            setTimeout(async () => {
                if(resp.length > 0) {

                    for (const listResultsByYear of resp) {
                        //Antibiograma
                        const resultsAntibiogram = listResultsByYear.results.filter( (result: Result) => result.lab57c26 === 1 );
                        if(resultsAntibiogram) {
                            await getMicrobialDetection(resultsAntibiogram, listResultsByYear.year);
                        }

                        //Plantillas
                        const resultsTemplates = listResultsByYear.results.filter( (result: Result) => result.lab57c42 === 1 );
                        if(resultsTemplates) {
                            await getGeneralTemplate(resultsTemplates, listResultsByYear.year);
                        }

                        //Adjuntos
                        const resultsAttachments = listResultsByYear.results.filter( (result: Result) => result.lab57c41 > 0 );
                        if(resultsAttachments) {
                            await getAttachmentsByTest(resultsAttachments, listResultsByYear.year);
                        }

                        //Graficas de resultados
                        await getAnalyzerGraphs(listResultsByYear.results, listResultsByYear.year);

                    }

                    sendResultsCentral(resp);
                } else {
                    console.log('No existen ordenes para enviar');
                }
            });
        });

    } catch (error) {
        console.log('error', error);
    }
}

const getListYears = (numberOfYears: number): number[] => {
    const actualYear = new Date().getFullYear();
    const years: number[] = [];
    if (numberOfYears > 0) {
        numberOfYears = 1;
        for (let i = actualYear - numberOfYears + 1; i <= actualYear; i++) {
            years.push(i);
        }
    } else {
        years.push(actualYear);
    }
    return years;
}
