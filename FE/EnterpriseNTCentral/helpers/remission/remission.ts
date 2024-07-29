
import RemissionCollection from "../../models/results/remission";
import { ObjectId } from "mongodb";
import mongoose from "mongoose";
import OrderSchema from "../../models/central/order";
import PatientCollection from "../../models/central/patient";
import ResultSchema from "../../models/central/result";
import moment from "moment";
import { decryptData } from "../../tools/common";

export const insertRemission = async (body: any) => {
    const remission = await RemissionCollection.create({
        lab22c1     : body.order,
        lab39c1     : body.test,
        lab24c1     : body.sample,
        lab05c1_1   : body.branchOrigin,
        lab05c1_2   : body.branchDestination,
        lab57rc1    : 0

    });
    return remission;
}

export const getRemission = async (body: any) => {
    const remission = await RemissionCollection.find({
        lab22c1     : body.order,
        lab24c1     : body.sample,
        lab05c1_2   : body.branch,
    });
    
    if( remission.length > 0 ) {

        const orderNumer = String(body.order);
        const year: string = orderNumer.substring(0, 4);
        const nameCollectionOrders = `lab22_${year}`;
        const CollectionOrders = mongoose.model(nameCollectionOrders, OrderSchema);

        const order = await CollectionOrders.findOne({
            lab22c1     : body.order,
        });

        const patient = await PatientCollection.findOne({
            "lab21c2": order?.lab21c2,
            "lab54.lab54c1": order?.lab54.lab54c1
        });

        const nameCollectionResults = `lab57_${year}`;
        const CollectionResults = mongoose.model(nameCollectionResults, ResultSchema);
        const testsIds = remission.map( rem => rem.lab39c1 );

        const tests = await CollectionResults.find({
            "lab39.lab39c1": { $in: testsIds },
            "lab22c1": body.order
          }, { _id: 0, "lab39.lab39c1": 1, "lab39.lab39c2": 1, "lab39.lab39c3": 1, "lab39.lab39c4": 1  }).exec();

        const json = setJsonOrder(order, patient, tests);
        return json;
    } else {
        return null;
    }

}

export const setJsonOrder = async( order:any, patient:any, tests:any ) => {

    const decrypt: any = {};

    if (patient) {
        if (patient.lab21c2) {
            decrypt[`${order.lab22c1}_document`] = patient.lab21c2;
        }
        if (patient.lab21c3) {
            decrypt[`${order.lab22c1}_name1`] = patient.lab21c3;
        }
        if (patient.lab21c4) {
            decrypt[`${order.lab22c1}_name2`] = patient.lab21c4;
        }
        if (patient.lab21c5) {
            decrypt[`${order.lab22c1}_lastName`] = patient.lab21c5;
        }
        if (patient.lab21c6) {
            decrypt[`${order.lab22c1}_surName`] = patient.lab21c6;
        }
    }

    if (Object.values(decrypt).length > 0) {
        let listOfData: any = await decryptData(decrypt);
        if (listOfData !== null && listOfData !== undefined && Object.keys(listOfData).length > 0) {
            if (patient) {
                if (patient.lab21c2) {
                    patient.lab21c2 = listOfData[`${order.lab22c1}_document`];
                }
                if (patient.lab21c3) {
                    patient.lab21c3 = listOfData[`${order.lab22c1}_name1`];
                    patient.lab21c3 = patient.lab21c3 === null || patient.lab21c3 === undefined || patient.lab21c3 === "" ? "" : patient.lab21c3.trim();
                }
                if (patient.lab21c4) {
                    patient.lab21c4 = listOfData[`${order.lab22c1}_name2`];
                    patient.lab21c4 = patient.lab21c4 === null || patient.lab21c4 === undefined || patient.lab21c4 === "" ? "" : patient.lab21c4.trim();
                }
                if (patient.lab21c5) {
                    patient.lab21c5 = listOfData[`${order.lab22c1}_lastName`];
                    patient.lab21c5 = patient.lab21c5 === null || patient.lab21c5 === undefined || patient.lab21c5 === "" ? "" : patient.lab21c5.trim();
                }
                if (patient.lab21c6) {
                    patient.lab21c6 = listOfData[`${order.lab22c1}_surName`];
                    patient.lab21c6 = patient.lab21c6 === null || patient.lab21c6 === undefined || patient.lab21c6 === "" ? "" : patient.lab21c6.trim();
                }
            }
        }
    }

    const demoOrder:any = [];
    order.lab62.forEach( (demo:any) => {
        demoOrder.push({
            idDemographic: demo.lab62c1,
            encoded: demo.lab62c4 === 1,
            notCodifiedValue: demo.value,
            codifiedId: demo.lab63?.lab63c1
        });
    });


    const demoPatient:any = [];
    patient.lab62.forEach( (demo:any) => {
        demoPatient.push({
            idDemographic: demo.lab62c1,
            encoded: demo.lab62c4 === 1,
            notCodifiedValue: demo.value,
            codifiedId: demo.lab63?.lab63c1
        });
    });

    const listTests:any = [];

    tests.forEach( (test:any) => {
        listTests.push({
            id: test.lab39.lab39c1,
            code: test.lab39.lab39c2,
            name: test.lab39.lab39c4
        });
    });

    const json = {
        createdDateShort: moment().format('YYYYMMDD'),
        orderNumber: order.lab22c1,
        date: "",
        type: {
            id: order.lab103.lab103c1,
            code: order.lab103.lab103c2,
            name: order.lab103.lab103c3
        },
        branch: {
            id: order.lab05.lab05c1,
            name: order.lab05.lab05c4
        },
        account: {
            id: order.lab14.lab14c1
        },
        rate: {
            id: order.lab904.lab904c1,
            name: order.lab904.lab904c3
        },
        physician: {
            id: order.lab19.lab19c1,
        },
        demographics: demoOrder,
        listDiagnostic: [],
        patient: {
            documentType: {
                id: patient.lab54.lab54c1
            },
            patientId: patient.lab21c2,
            lastName: patient.lab21c3,
            surName: patient.lab21c4,
            name1: patient.lab21c5,
            name2: patient.lab21c6,
            sex: {
                id: patient.lab80.lab80c1,
            },
            birthday: patient.lab21c7,
            email: patient.lab21c8,
            phone: patient.lab21c16,
            weight: patient.lab21c10,
            size: patient.lab21c9,
            address: patient.lab21c17,
            demographics: demoPatient,
            updatePatient: false
        },
        tests: listTests,
        deleteTests: [],
        turn: ""
    };

    return json;
}