import * as _ from "lodash";
import mongoose from 'mongoose';
import OrderSchema from "../../models/central/order";
import { decryptData, encryptData } from "../../tools/common";

export const listorderfilter = async (filter: Outreachfilterorder) => {
    let ordersInserted: any[] = [];

    const nameCollectionOrders = `lab22_` + filter.year;
    const CollectionOrders = mongoose.model(nameCollectionOrders, OrderSchema);
    let filters: any = { };

    if(filter.order != null){
        filters["lab22c1"] = parseInt(filter.order.toString()) 
    }
    if(filter.dateNumber != null){
        filters["lab22c2"] = {
            $eq: parseInt(filter.dateNumber.toString())
        }
    } 
    if(filter.documentType != null && filter.documentType  > 0){
        filters["lab21.lab54.lab54c1"] = filter.documentType
    } 
    if(filter.patientId != null){
        let patientId: any = await encryptData({"patientId":filter.patientId})
        filters["lab21.lab21c2"] = patientId["patientId"]
    } 
    if(filter.lastName != null && filter.lastName != ""){
        let lastName: any = await encryptData({"lastName":filter.lastName})
        filters["lab21.lab21c5"] = lastName["lastName"]
    } 
    if(filter.surName != null && filter.surName != ""){
        let surName: any = await encryptData({"surName":filter.surName})
        filters["lab21.lab21c6"] =  surName["surName"]
    }
    if(filter.name1 != null && filter.name1 != ""){
        let name1: any = await encryptData({"name1":filter.name1})
        filters["lab21.lab21c3"] =  name1["name1"]
    }
    if(filter.name2 != null && filter.name2  != ""){
        let name2: any = await encryptData({"name2":filter.name2})
        filters["lab21.lab21c4"] = name2["name2"]
    }
    if(filter.dateNumberInit){
        filters["lab22c2"] = {
            $gte: parseInt(filter.dateNumberInit.toString()),
            $lte: parseInt(filter.dateNumberEnd.toString())
        }
    }
    if(filter.year != null){
        filters["lab22c2"] = {
            $gte: parseInt(filter.year + "0101")
        }
    }
    if(filter.area != null && filter.area != 0) {
        filters["lab39.lab39.lab43.lab43c1"] = {
            $gte: parseInt(filter.area.toString())
        }
    }

    
    try {
        await CollectionOrders.aggregate([
            {
                $lookup: {
                    from: "lab21",
                    let: { lab21c2: "$lab21c2", lab54c1: "$lab54.lab54c1" },
                    pipeline: [
                        {
                        $match: {
                            $expr: {
                            $and: [
                                { $eq: [
                                    "$lab21c2",
                                    "$$lab21c2"
                                ]
                                },
                                { $eq: [
                                    "$lab54.lab54c1",
                                    "$$lab54c1"
                                ]
                                }
                            ]
                            }
                        }
                        }
                    ],
                    as: "lab21" 
                },
            },
            {
                $lookup: {
                    from: "lab57_" + filter.year, 
                    localField: 'lab22c1', 
                    foreignField: 'lab22c1', 
                    as: 'lab39' 
                },
            },
            {
                $match: filters 
            },
            {
                $project: {
                    order: "$lab22c1",
                    patientIdDB: "$lab21.lab21c1",
                    patientId: "$lab21c2",
                    documentTypeId: { $arrayElemAt: ["$lab21.lab54.lab54c1", 0] } ,
                    lastName: { $arrayElemAt: ["$lab21.lab21c5", 0] }, 
                    surName: { $arrayElemAt: ["$lab21.lab21c6", 0] },
                    name1: { $arrayElemAt: ["$lab21.lab21c3", 0] },
                    name2 : { $arrayElemAt: ["$lab21.lab21c4", 0] },
                    sex: { $arrayElemAt: ["$lab21.lab80.lab80c3", 0] },
                    birthday : { $arrayElemAt: ["$lab21.lab21c7", 0] }
                }
            }

        ]).then(doc => {
            if (doc) {
                ordersInserted = doc;
            }
        }).catch(err => {
            console.error('Error al insertar la orden:', err);
        });

        } catch (error) {
            console.log("Error al guardar ordenes: ", error);
        }
    

    const decrypt: any = {};
    ordersInserted.forEach((patient: any) => {
        decrypt[patient.patientIdDB + "_document"] = patient.patientId;
        decrypt[patient.patientIdDB + "_name1"] = patient.name1;
        decrypt[patient.patientIdDB + "_name2"] = patient.name2;
        decrypt[patient.patientIdDB + "_lastName"] = patient.lastName;
        decrypt[patient.patientIdDB + "_surName"] = patient.surName;
    });

    let listOfData: any = await decryptData(decrypt);

    if (listOfData !== null && listOfData !== undefined) {
        ordersInserted.forEach((data: any) => {
            data.patientId = listOfData[data.patientIdDB + "_document"];
            data.name1 = listOfData[data.patientIdDB + "_name1"];
            data.name2 = listOfData[data.patientIdDB + "_name2"];
            data.lastName = listOfData[data.patientIdDB + "_lastName"];
            data.surName = listOfData[data.patientIdDB + "_surName"];
        });
    }


    return ordersInserted;
}

export const listResults = async (orderNumber: Number, area: Number ) => {
    let ordersInserted: any[] = [];

    const nameCollectionOrders = `lab57_2024`;
    const CollectionOrders = mongoose.model(nameCollectionOrders, OrderSchema);
    let filters: any = { };
    filters["lab22c1"] = parseInt(orderNumber.toString()) 

    
    if(area != null && area != 0) {
        filters["lab57.lab39.lab43.lab43c1"] = {
            $gte: parseInt(area.toString())
        }
    }
    
    try {
        await CollectionOrders.aggregate([
     
            
            {
                $match: filters 
            },
            {
                $project: {
                    order : "$lab22c1" ,
                    testId : "$lab39.lab39c1",
                    testCode : "$lab39.lab39c2",
                    testName : "$lab39.lab39c4",
                    testType : "$lab39.lab39c37",
                    entry : "$lab57c14",
                    sampleId : "$lab24c1.lab24c1",
                    sampleCode : "$lab24c1.lab24c9",
                    sampleName : "$lab24c1.lab24c2",
                    entryDate: "$lab57c4",
                    entryUserId : "$lab57c5",
                    verificationDate : "$lab57c37",
                    verificationUserId : "$lab57c38",
                    result: "$lab57c1",
                    dateResult: "$lab57c2",
                    resultUserId : "$lab57c3",
                    validationDate : "$lab57c18",
                    validationUserId : "$lab57c19",
                    printDate: "$lab57c22",
                    printUserId : "$lab57c23",
                    takenDate:"$lab57c39",
                    takenUserId : "$lab57c40",
                    entryType : "$lab57c35",
                    state : "$lab57c8",
                    sampleState : "$lab57c16",
                    areaId : "$lab39.lab43.lab43c1",
                    areaCode : "$lab39.lab43.lab43c2",
                    areaAbbr : "$lab39.lab43.lab43c3",
                    areaName : "$lab39.lab43.lab43c4",
                    resultType :"$lab39.lab39c11",
                    pathology : "$lab57c9",
                    //refLiteral : "$lab50c2",
                    //PanicLiteral : "$lab50c2p")),
                   // Confidential : "$lab39c27") == 1),
                    repeatAmmount : "$lab57c33",
                    modificationAmmount : "$lab57c24",
                    refMin: "$lab48c12",
                    refMax: "$lab48c13",
                    refInterval: "$lab48c12" + " - " + "$lab48c13",
                    panicMin: "$lab48c5",
                    panicMax: "$lab48c6",
                    panicInterval: "$lab48c5" + " - " + "$lab48c6",
                    reportedMin: "lab48c14",
                    reportedMax: "lab48c15",
                    reportedInterval: "$lab48c14" + " - " + "$lab48c15",
                    deltaMin: "$lab57c27",
                    deltaMax:"$lab57c28",
                    deltaInterval : "$lab57c27" + " - " + "$lab57c28",
                    lastResult : "$lab57c6",
                    lastResultDate: "$lab57c7",
                    secondLastResult : "$lab57c30",
                    secondLastResultDate : "$lab57c31",
                    critic:  "$lab48c16",
                    unit : "$lab45c2",
                    abbreviation : "$lab39c3",
                    digits : "$lab39c12",
                    hasComment: "$lab57c32",
                    entryTestType: "lab57c36",
                    
                    //ProfileId : "$lab39c1p")),
                    //ProfileName : "$lab39c4p")),
                    hasAntibiogram : "$lab57c26",
                    
                    resultComment: {
                        "$map": {
                          "input": "$lab95",
                          "as": "lab95",
                          "in": {
                            "comment": "$$lab95.lab95c1",
                            "commentDate": "$$lab95.lab95c2"
                          }
                        }
                      }
                }
            }

        ]).then(doc => {
            if (doc) {
                ordersInserted = doc;
            }
        }).catch(err => {
            console.error('Error al insertar la orden:', err);
        });

        } catch (error) {
            console.log("Error al guardar ordenes: ", error);
        }
    

    const decrypt: any = {};
    ordersInserted.forEach((test: any) => {
        decrypt[test.order + test.testId + "_result"] = test.result;
        decrypt[test.order + test.testId + "_lastResult"] = test.lastResult;
        decrypt[test.order + test.testId + "_secondLastResult"] = test.secondLastResult;
    });

    let listOfData: any = await decryptData(decrypt);

    if (listOfData !== null && listOfData !== undefined) {
        ordersInserted.forEach((test: any) => {

            test.result = listOfData[test.order + test.testId + "_result"];
            test.lastResult = listOfData[test.order + test.testId + "_lastResult"];
            test.secondLastResult = listOfData[test.order + test.testId + "_secondLastResult"];
            

            test.entry = test.entry == null
            test.entryUserId = test.entryUserId == 0 ? null : test.entryUserId;
            test.entryTestType = test.entryTestType == 0 ? null : test.entryTestType;
        });
    }


    return ordersInserted;
}