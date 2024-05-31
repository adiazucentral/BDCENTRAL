import moment from "moment";
import { demosH, demosO, demosR } from "../helpers/configuration/demographics";
import { getAgeAsString, htmlEntities } from "./common";
import { Constants } from "./constants";

export const mapQuery = async (body: any, year: any, centralSystem: any, filters: any) => {

    const projections = [];
    const relations = [];

    let mapResult = null;
    if (filters.profiles || (filters.tests !== null && filters.tests !== undefined && filters.tests.length > 0)) {

        const filtersResults = [];

        if (filters.profiles) {
            filtersResults.push({ $eq: ["$$lab57.lab57c14.lab39c1", null] })
        }

        if (filters.tests !== null && filters.tests !== undefined && filters.tests.length > 0) {
            filtersResults.push({ $in: ["$$lab57.lab39.lab39c1", filters.tests] })
        }

        mapResult = {
            "$filter": {
                "input": "$lab57",
                "as": "lab57",
                "cond": {
                    $and: [...filtersResults]
                }
            }
        }
    } else {
        mapResult = '$lab57';
    }

    const projection: any = {
        $project: {
            order: '$lab22c1',
            results: {
                $map: {
                    input: mapResult,
                    as: 'lab57',
                    in: {
                        idTest: '$$lab57.lab39.lab39c1',
                        idProfile: '$$lab57.lab57c14.lab39c1'
                    },
                },
            }
        }
    };

    const nameCollectionResults = `lab57_${year}`;

    relations.push({
        $lookup: {
            from: nameCollectionResults,
            localField: 'lab22c1',
            foreignField: 'lab22c1',
            as: 'lab57',
        }
    });

    //Demográficos orden
    if (body.listDemosO !== null && body.listDemosO !== undefined && body.listDemosO.length > 0) {

        const idsDemosO = body.listDemosO.map((demo: any) => +demo.id);
        const demosOAll = demosO.filter((demo: any) => demo.id !== -101 && demo.id !== -118 && demo.id !== -119).filter((demo: any) => idsDemosO.includes(demo.id)).map((demo: any) => { return { value: demo.value, alias: demo.alias } });
        if (demosOAll) {
            demosOAll.forEach((field: any) => {
                if (field.value === 'lab19') {
                    projection.$project[field.alias] = {
                        name: `$lab19.lab19c2`,
                        lastname: `$lab19.lab19c3`
                    }
                } else {
                    projection.$project[field.alias] = `$${field.value}`;
                }
            });
        }

        const idsDemosOFixed = body.listDemosO.filter((demo: any) => demo.id > 0).map((demo: any) => +demo.id);
        if (idsDemosOFixed.length > 0) {
            projection.$project['demographics'] = {
                "$map": {
                    "input": {
                        "$filter": {
                            "input": "$lab62",
                            "as": "demographic",
                            cond: { $in: ["$$demographic.lab62c1", idsDemosOFixed] },
                        }
                    },
                    "as": "demographic",
                    "in": {
                        "id": "$$demographic.lab62c1",
                        "name": "$$demographic.lab62c2",
                        "encoded": "$$demographic.lab62c4",
                        "value": "$$demographic.value",
                        "item": {
                            "id": "$$demographic.lab63.lab63c1",
                            "name": "$$demographic.lab63.lab63c2",
                            "value": "$$demographic.lab63.lab63c3"
                        }
                    }
                }
            }
        }

    }

    if (body.listDemosH !== null && body.listDemosH !== undefined && body.listDemosH.length > 0) {

    //Demográficos historia
        let demosHAll = [];

        const filterDemosH = filters.demographics.filter((demo: any) => demo.origin === 'H');

        let mapHistory = null; 
        const filtersHistory:any = [];

        if (filterDemosH) {

            filterDemosH.forEach( (demo:any) => {
                if(demo.demographicItems.length > 0) {
                    switch (demo.demographic) {
                        case Constants.RACE:
                            if (demo.demographicItems != null && demo.demographicItems.length > 0)
                            {
                                filtersHistory.push({ $in: ["$$lab21.lab08.lab08c1", demo.demographicItems] });
                            }
                            break;
                        case Constants.PATIENT_SEX:
                            if (demo.demographicItems != null && demo.demographicItems.length > 0)
                            {
                                filtersHistory.push({ $in: ["$$lab21.lab80.lab80c1", demo.demographicItems] });
                            }
                            break;
                        case Constants.DOCUMENT_TYPE:
                            if (demo.demographicItems != null && demo.demographicItems.length > 0)
                            {
                                filtersHistory.push({ $in: ["$$lab21.lab54.lab54c1", demo.demographicItems] });
                            }
                            break;
                        default:
                            if (demo.demographic > 0) {
                                if (demo.origin != null) {
                                    if(demo.demographicItems !== null && demo.demographicItems.length > 0 ) {                                        
                                        filtersHistory.push({ $in: ["$$lab21.lab62.lab63.lab63c1", demo.demographicItems] });
                                    } else {
                                        filtersHistory.push({ $eq: ["$$lab21.lab62.value", demo.value] });
                                    }
                                }
                            }
                            break;
                    }
                } else {
                    switch (demo.demographic) {
                        case Constants.PATIENT_ID:
                            filtersHistory.push({ $eq: ["$$lab21.lab21c2", demo.value] });
                            break;

                        case Constants.PATIENT_LAST_NAME:
                            filtersHistory.push({ $eq: ["$$lab21.lab21c5", demo.value] });
                            break;

                        case Constants.PATIENT_SURNAME:
                            filtersHistory.push({ $eq: ["$$lab21.lab21c6", demo.value] });
                            break;

                        case Constants.PATIENT_NAME:
                            filtersHistory.push({ $eq: ["$$lab21.lab21c3", demo.value] });
                            break;

                        case Constants.PATIENT_SECOND_NAME:
                            filtersHistory.push({ $eq: ["$$lab21.lab21c4", demo.value] });
                            break;
                    }
                }
                
            });

            if(filtersHistory.length > 0) {

                mapHistory = {
                    "$filter": {
                        "input": "$lab21",
                        "as": "lab21",
                        "cond": {
                            $and: [...filtersHistory]
                        }
                    }
                }
                
            } else {
                mapHistory = '$lab21';
            }

        } else {
            mapHistory = '$lab21';
        }

        const mapPatient: any = {
            "$map": {
                "input": mapHistory,
                "as": "lab21",
                "in": {}
            }
        };
        
        relations.push({
            // $lookup: {
            //     from: 'lab21',
            //     localField: 'lab21c2',
            //     foreignField: 'lab21c2',
            //     as: 'lab21',
            // },
            $lookup: {
                from: 'lab21',
                let: { lab21c2: '$lab21c2', lab54c1: '$lab54.lab54c1' },
                pipeline: [
                    {
                        $match: {
                            $expr: {
                                $and: [
                                    { $eq: ['$lab21c2', '$$lab21c2'] },
                                    { $eq: ['$lab54.lab54c1', '$$lab54c1'] },
                                ],
                            },
                        },
                    },
                ],
                as: 'lab21',
            }
        });

        const idsDemosH = body.listDemosH.map((demo: any) => demo.id);
        demosHAll = demosH.filter((demo: any) => idsDemosH.includes(demo.id)).map((demo: any) => { return { value: demo.value, alias: demo.alias } });
        if (demosHAll) {
            demosHAll.forEach((field: any) => {
                mapPatient.$map.in[field.alias] = `$$lab21.${field.value}`;
            });
        }

        const idsDemosHFixed = body.listDemosH.filter((demo: any) => demo.id > 0).map((demo: any) => +demo.id);
        if (idsDemosHFixed.length > 0) {
            mapPatient.$map.in['demographics'] = {
                "$map": {
                    "input": {
                        "$filter": {
                            "input": `$$lab21.lab62`,
                            "as": "demographic",
                            "cond": {
                                "$in": ["$$demographic.lab62c1", idsDemosHFixed]
                            }
                        }
                    },
                    "as": "demographic",
                    "in": {
                        "id": "$$demographic.lab62c1",
                        "name": "$$demographic.lab62c2",
                        "encoded": "$$demographic.lab62c4",
                        "value": "$$demographic.value",
                        "item": {
                            "id": "$$demographic.lab63.lab63c1",
                            "name": "$$demographic.lab63.lab63c2",
                            "value": "$$demographic.lab63.lab63c3"
                        }
                    }
                }
            }
        }

        projection.$project['patient'] = {
            "$arrayElemAt": [ mapPatient, 0 ]
        };
    }

    //Demográficos resultados

    if (body.listDemosR !== null && body.listDemosR !== undefined && body.listDemosR.length > 0) {

        const idsDemosR = body.listDemosR.map((demo: any) => demo.id);
        const demosRAll = demosR.filter((demo: any) => idsDemosR.includes(demo.id)).map((demo: any) => { return { value: demo.value, alias: demo.alias } });
        if (demosRAll) {
            demosRAll.forEach((field: any) => {
                projection.$project['results'].$map.in[field.alias] = `$$lab57.${field.value}`;
            });
        }
    }

    projections.push(projection);
    return { projections, relations };
}

export const getMatch = async (filters: any, orderInit:any, orderEnd:any) => {
    const match:any = {};

    match['lab22c1'] = { $gte: +orderInit, $lte: +orderEnd };
    
    const filterDemosO = filters.demographics.filter((demo: any) => demo.origin === 'O');

    if(filterDemosO) {

        filterDemosO.forEach( (demo:any) => {
            if(demo.demographicItems.length > 0) {
                switch (demo.demographic) {
                    case Constants.BRANCH:
                        if (demo.demographicItems != null && demo.demographicItems.length > 0)
                        {
                            match['lab05.lab05c1'] = { $in: demo.demographicItems };
                        }
                        break;
                    case Constants.SERVICE:
                        if (demo.demographicItems != null && demo.demographicItems.length > 0)
                        {
                            match['lab10.lab10c1'] = { $in: demo.demographicItems };
                        }
                        break;
                    case Constants.PHYSICIAN:
                        if (demo.demographicItems != null && demo.demographicItems.length > 0)
                        {
                            match['lab19.lab19c1'] = { $in: demo.demographicItems };
                        }
                        break;
                    case Constants.ACCOUNT:
                        if (demo.demographicItems != null && demo.demographicItems.length > 0)
                        {
                            match['lab14.lab14c1'] = { $in: demo.demographicItems };
                        }
                        break;
                    case Constants.RATE:
                        if (demo.demographicItems != null && demo.demographicItems.length > 0)
                        {
                            match['lab904.lab904c1'] = { $in: demo.demographicItems };
                        }
                        break;
                    case Constants.ORDERTYPE:
                        if (demo.demographicItems != null && demo.demographicItems.length > 0)
                        {
                            match['lab103.lab103c1'] = { $in: demo.demographicItems };
                        }
                        break;
                    default:
                        if (demo.demographic > 0) {
                            if (demo.origin != null) {
                                if(demo.demographicItems !== null && demo.demographicItems.length > 0 ) {
                                    match['lab62.lab63.lab63c1'] = { $in: demo.demographicItems };
                                } else {
                                    match['lab62.value'] = { $eq: demo.value };
                                }
                            }
                        }
                        break;
                }
            }
        });

    }

    return match;
}

export const mapData = async (body: any, data: any, centralSystem: any, info: any) => {
    data.forEach((order: any) => {

        const demosHFixed = body.listDemosH.filter((demo: any) => demo.id < 0);
        const demosOFixed = body.listDemosO.filter((demo: any) => demo.id < 0);

        if (demosHFixed.length > 0 && order.patient) {

            let birthdayOriginal = null;
            if (order.patient.birthday !== undefined && order.patient.birthday !== null && order.patient.birthday !== "") {
                birthdayOriginal = order.patient.birthday;
            }

            demosHFixed.forEach((demo: any) => {
                switch (demo.id) {
                    case -5:
                        order.patient.creationDate = order.patient.creationDate === null || order.patient.creationDate === undefined ? '' : moment(order.patient.creationDate).format('DD/MM/YYYY HH:mm:ss');
                        break;
                    case -6:
                        order.patient.updateDate = order.patient.updateDate === null || order.patient.updateDate === undefined ? '' : moment(order.patient.updateDate).format('DD/MM/YYYY HH:mm:ss');
                        break;
                    case -7:
                        const birthday = (new Date(birthdayOriginal)).toISOString().split("T");
                        order.patient.birthday = birthday[0];
                        break;
                    case -16:
                        let address = order.patient.address === null || order.patient.address === undefined || order.patient.address === "" ? "" : order.patient.address.replace(/<br\s*\/?>/g, '');
                        address = address === null || address === undefined || address === "" ? "" : htmlEntities(address);
                        address = address.replaceAll(',', '');
                        address = address.replace(/(\r\n|\n|\r)/gm, "");
                        order.patient.address = address;
                        break;
                    case -19:
                        const age = getAgeAsString(moment(birthdayOriginal).format('DD/MM/YYYY'), 'DD/MM/YYYY');
                        order.patient.age = age;
                        break;
                    case -20:
                        order.patient.sexCode = order.patient.sexCode[0];
                        break;
                }
            });
        }

        //Demograficos orden
        if (demosOFixed.length > 0) {
            let date = null;
            demosOFixed.forEach((demo: any) => {
                switch (demo.id) {
                    case -103:
                        order.creationDate = order.creationDate === null || order.creationDate === undefined ? '' : moment(order.creationDate).format('DD/MM/YYYY HH:mm:ss');
                        break;
                    case -104:
                        order.updateDate = order.updateDate === null || order.updateDate === undefined ? '' : moment(order.updateDate).format('DD/MM/YYYY HH:mm:ss');
                        break;
                    case -107:
                        date = moment(order.year, 'DD/MM/YYYY');
                        order.year = date.year();
                        break;
                    case -108:
                        date = moment(order.month, 'DD/MM/YYYY');
                        order.month = date.month() + 1;
                        break;
                    case -109:
                        date = moment(order.day, 'DD/MM/YYYY');
                        order.day = date.date();
                        break;
                    case -116:
                        const physician = (order.physician.name === undefined || order.physician.name === null ? "" : order.physician.name) + " " + (order.physician.lastname === undefined || order.physician.lastname === null ? "" : order.physician.lastname)
                        order.physician = physician;
                        break;
                    case -118:
                        order.entity = info.entity;
                        break;
                    case -119:
                        order.nit = info.nit;
                        break;
                }
            });
        }

        body.listDemosR.forEach((demo: any) => {

            order.results.forEach((result: any) => {

                switch (demo.id) {
                    case -207:
                        result.dateResult = result.dateResult === null || result.dateResult === undefined ? '' : moment(result.dateResult).format('DD/MM/YYYY HH:mm:ss');
                        break;
                    case -209:
                        result.dateEntry = result.dateEntry === null || result.dateEntry === undefined ? '' : moment(result.dateEntry).format('DD/MM/YYYY HH:mm:ss');
                        break;
                    case -216:
                        result.dateBlocking = result.dateBlocking === null || result.dateBlocking === undefined ? '' : moment(result.dateBlocking).format('DD/MM/YYYY HH:mm:ss');
                        break;
                    case -226:
                        result.dateValidation = result.dateValidation === null || result.dateValidation === undefined ? '' : moment(result.dateValidation).format('DD/MM/YYYY HH:mm:ss');
                        break;
                    case -228:
                        result.datePrevalidation = result.datePrevalidation === null || result.datePrevalidation === undefined ? '' : moment(result.datePrevalidation).format('DD/MM/YYYY HH:mm:ss');
                        break;
                    case -230:
                        result.datePrint = result.datePrint === null || result.datePrint === undefined ? '' : moment(result.datePrint).format('DD/MM/YYYY HH:mm:ss');
                        break;
                    case -232:
                        let unit = null;
                        unit = result.unit === null || result.unit === undefined || result.unit === "" ? "" : result.unit.replace(/<br\s*\/?>/g, '')
                        unit = unit === null || unit === undefined || unit === "" ? "" : htmlEntities(unit);
                        unit = unit.replaceAll(',', '');
                        unit = unit.replace(/(\r\n|\n|\r)/gm, "");
                        result.unit = unit;
                        break;
                    case -233:
                        let method = null;
                        method = result.method === null || result.method === undefined || result.method === "" ? "" : result.method.replace(/<br\s*\/?>/g, '')
                        method = method === null || method === undefined || method === "" ? "" : htmlEntities(method);
                        method = method.replaceAll(',', '');
                        method = method.replace(/(\r\n|\n|\r)/gm, "");
                        result.method = method;
                        break;
                    case -245:
                        result.dateVerification = result.dateVerification === null || result.dateVerification === undefined ? '' : moment(result.dateVerification).format('DD/MM/YYYY HH:mm:ss');
                        break;
                    case -247:
                        result.dateTake = result.dateTake === null || result.dateTake === undefined ? '' : moment(result.dateTake).format('DD/MM/YYYY HH:mm:ss');
                        break;
                    case -255:
                        result.dateRetake = result.dateRetake === null || result.dateRetake === undefined ? '' : moment(result.dateRetake).format('DD/MM/YYYY HH:mm:ss');
                        break;
                    case -257:
                        result.dateRepetition = result.dateRepetition === null || result.dateRepetition === undefined ? '' : moment(result.dateRepetition).format('DD/MM/YYYY HH:mm:ss');
                        break;
                    case -260:
                        result.remission = result.remission === null || result.remission === undefined ? 0 : result.remission;
                        break;
                    case -261:
                        //Comentario del resultado
                        let comment = null;
                        comment = result.comment === null || result.comment === undefined || result.comment === "" ? "" : result.comment.replace(/<br\s*\/?>/g, '')
                        comment = comment === null || comment === undefined || comment === "" ? "" : htmlEntities(comment);
                        comment = comment.replaceAll(',', '');
                        comment = comment.replace(/(\r\n|\n|\r)/gm, "");
                        result.comment = comment;
                        break;
                    case -262:
                        result.servicePrice = result.servicePrice === null || result.servicePrice === undefined ? 0 : result.servicePrice;
                        break;
                    case -263:
                        result.patientPrice = result.patientPrice === null || result.patientPrice === undefined ? 0 : result.patientPrice;
                        break;
                    case -264:
                        result.insurancePrice = result.insurancePrice === null || result.insurancePrice === undefined ? 0 : result.insurancePrice;
                        break;
                    case -265:
                        result.invoiceClient = order.invoiceClient === null || order.invoiceClient === undefined ? '' : order.invoiceClient;
                        break;
                }
            });

        });

    });

    return data;
}