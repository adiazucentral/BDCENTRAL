import * as _ from "lodash";
import mongoose from 'mongoose';
import OrderSchema from "../../models/central/order";
import ResultSchema from "../../models/central/result";
import { decryptData, encryptData } from "../../tools/common";
import fs from 'fs';

export const listorderfilter = async (filter: Outreachfilterorder) => {
    let ordersInserted: any[] = [];
 
    //const nameCollectionOrders = `lab22_` + filter.year;
    const nameCollectionOrders = `lab22_2024`;
    const CollectionOrders = mongoose.model(nameCollectionOrders, OrderSchema);

    let filters: any = {} 

    if(filter.order != null){
        filters["lab22c1"] =  { $eq:Number(filter.order.toString()) }
    }
    if(filter.year != undefined){
        filters["lab22c2"] = {
            $gte: parseInt(filter.year + "0101")
        }
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
    
    if(filter.area != 0 && filter.area != null ) {
        filters["lab39.lab39.lab43.lab43c1"] = {
            $eq: parseInt(filter.area.toString())
        }
    }
    filters["lab39.lab39.lab39c37"] =  { $eq: 0 }
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
                    from: "lab57_2024", 
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

export const listResults = async (orderNumber: String, area: String ) => {
    let ordersInserted: any[] = [];

    const nameCollectionResult = `lab57_2024`;
    const CollectionOrders = mongoose.model(nameCollectionResult, ResultSchema);
    let filters: any = { };

    filters["lab22c1"] = {
        $eq: parseInt(orderNumber.toString()) 
    }

    filters["lab39.lab39c37"] = {
        $eq: 0
    }

    if(area != null && parseInt(area.toString()) != 0) {
        filters["lab39.lab43.lab43c1"] = {
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
                    abbreviation: "$lab39.lab39c3",
                    testType : "$lab39.lab39c37",
                    entry : "$lab57c14",
                    sampleId : "$lab24c1.lab24c1",
                    sampleCode : "$lab24c1.lab24c9",
                    sampleName : "$lab24c1.lab24c2",
                    entryDate: "$lab57c4",
                    entryUserId : "$lab57c5.lab04c1",
                    verificationDate : "$lab57c37",
                    verificationUserId : "$lab57c38.lab04c1",
                    result: "$lab57c1",
                    dateResult: "$lab57c2",
                    resultUserId : "$lab57c3.lab04c1",
                    validationDate : "$lab57c18",
                    validationUserId : "$lab57c19.lab04c1",
                    printDate: "$lab57c22",
                    printUserId : "$lab57c23.lab04c1",
                    takenDate:"$lab57c39",
                    takenUserId : "$lab57c40.lab04c1",
                    entryType : "$lab57c35",
                    state : "$lab57c8",
                    sampleState : "$lab57c16",
                    areaId : "$lab39.lab43.lab43c1",
                    areaCode : "$lab39.lab43.lab43c2",
                    areaAbbr : "$lab39.lab43.lab43c3",
                    areaName : "$lab39.lab43.lab43c4",
                    resultType :"$lab39.lab39c11",
                    pathology : "$lab57c9",
                    printSort: "$lab39.lab39.lab39c42",
                    refLiteral : "$lab50c1_4",
                    PanicLiteral : "$lab50c1_2",
                    confidential : "$lab39.lab39.lab39c27",
                    repeatAmmount : "$lab57c33",
                    modificationAmmount : "$lab57c24",
                    refMin: "$lab48c12",
                    refMax: "$lab48c13",
                    panicMin: "$lab48c5",
                    panicMax: "$lab48c6",
                    reportedMin: "$lab48c14",
                    reportedMax: "$lab48c15",
                    deltaMin: "$lab57c27",
                    deltaMax:"$lab57c28",
                    lastResult : "$lab57c6",
                    lastResultDate: "$lab57c7",
                    secondLastResult : "$lab57c30",
                    secondLastResultDate : "$lab57c31",
                    critic:  "$lab48c16",
                    unit : "$lab45c2",
                    digits : "$lab39c12",
                    hasComment: "$lab57c32",
                    entryTestType: "$lab57c36",
                    profileId : "$lab57c14.lab39c1",
                    profileName : "$lab57c14.lab39c4",
                    hasAntibiogram : "$lab57c26",
                    resultComment:  { "comment": "$lab95c1" },
                   
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
            test.hasComment = test.hasComment  == 1;
            test.hasAntibiogram = test.hasAntibiogram  == 1;
            test.hasTemplate = test.hasTemplate  == 1;
            test.confidential = test.confidential == 1;

            if (test.refMin != null) {
                test.refInterval =  test.refMin + " - " + test.refMax;
            } else {
                test.refMin = 0;
                test.refMax = 0;
                test.refInterval = test.refLiteral;
            }

            if (test.panicMin != null) {
                test.panicInterval = test.panicMin + " - " + test.panicMax;
            } else {
                test.panicMin = 0;
                test.panicMax = 0;
                test.panicInterval = test.panicMin + " - " + test.panicMax;
            }

            
            if (test.reportedMin != null) {
                test.reportedInterval = test.reportedMin + " - " + test.reportedMax;
            } else {
                test.reportedMin = 0;
                test.reportedMax = 0;
            }


        });
    }


    return ordersInserted;
}

export const listResultHistory = async (filter: Outreachhistoryfilter) => {
    let ordersInserted: any[] = [];
 
    //const nameCollectionOrders = `lab22_` + filter.year;
    const nameCollectionOrders = `lab22_2024`;
    const CollectionOrders = mongoose.model(nameCollectionOrders, OrderSchema);

    let filters: any = {} 

    let patientId: any = await encryptData({"patientId":filter.patientId})
    filters["lab39.lab39.lab39c1"] = { $in: filter.testId  }
    filters["lab21.lab21c2"] = patientId["patientId"]
    filters["lab21.lab54.lab54c1"] = filter.documentType

    try {
        await CollectionOrders.aggregate([{
            $lookup: {
                from: "lab57_2024", 
                localField: 'lab22c1', 
                foreignField: 'lab22c1', 
                as: 'lab39' 
            },
        },
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
        $unwind: "$lab39"
      },
      {
        $match: filters
      },
      {
        $group: {
          _id:  {
            testId: "$lab39.lab39.lab39c1",
            testCode: "$lab39.lab39.lab39c2",
            abbr: "$lab39.lab39.lab39c3",
            testName: "$lab39.lab39.lab39c4",
            resultType: "$lab39.lab39.lab39c11"
          },
          history: { $push: {
                entryDate: "$lab39.lab57c4",
                order: "$lab22c1",
                pathology: "$lab39.lab57c9",
                refMin: "$lab39.lab48c12",
                refMax: "$lab39.lab48c13",
                refInterval: "$lab39.lab48c12" + " - " + "$lab39.lab48c13",
                result: "$lab39.lab57c1",
                validateDate: "$lab39.lab57c18",
                resultComment:  { "comment": "$$lab39.lab95c1" },
              
            } 
        }
          
          // Otros campos que quieras incluir
        }
      },
      {
        $sort: {
            validateDate: -1 // Orden ascendente por campo1, -1 para orden descendente
        }
      },
    {
        $project: {
          _id: 0, 
          testId: "$_id.testId", 
          testCode: "$_id.testCode", 
          abbr: "$_id.abbr",
          testName: "$_id.testName",
          resultType: "$_id.resultType",
          history: 1
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
        patient.history.forEach((result: any) => {
            decrypt[patient.testId + result.order + "_result"] = result.result;
        });
    });

    let listOfData: any = await decryptData(decrypt);

    if (listOfData !== null && listOfData !== undefined) {
        ordersInserted.forEach((patient: any) => {
            patient.history.forEach((result: any) => {
                result.result = listOfData[patient.testId + result.order + "_result"];
                if(patient.resultType == 1 && !isNaN(Number(result.result))){
                    result.resultNumber = Number(result.result)
                }
            }); 
            patient.history = _.orderBy(patient.history, 'validateDate', 'asc');
            patient.history = _.slice(patient.history , 0, 20);
        });
    }


    return ordersInserted;
}

export const listreport = async (orderNumber: String) => {
    let ordersInserted: any[] = [];
 
    //const nameCollectionOrders = `lab22_` + filter.year;
    const nameCollectionOrders = `lab22_2024`;
    const CollectionOrders = mongoose.model(nameCollectionOrders, OrderSchema);

    let filters: any = {} 

    if(orderNumber != null){
        filters["lab22c1"] =  { $eq:Number(orderNumber.toString()) }
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
                    from: "lab57_2024", 
                    localField: "lab22c1", 
                    foreignField: "lab22c1", 
                    as: "lab39"
                },
            },
            {
                $addFields: {
                    lab39: "$lab39"
                }
              },
            {
                $match: filters
            },
            {
                
                "$project": {
                  "orderNumber": "$lab22c1",
                  "createdDateShort": "$lab22c2",
                  "createdDate": "$lab22c3",
                  "physician": {
                    "name": "$lab19.lab19c2",
                    "lastname": "$lab19.lab19c3"
                  },
                  "branch": {
                    "name": "$lab05.lab05c4",
                    "Code": "$lab05.lab05c10"
                  },
                  "service": {
                    "name": "$lab10.lab10c2",
                    "code": "$lab10.lab10c7"
                  },
                  "account": {
                    "nit": "$lab14.lab14c2",
                    "name": "$lab14.lab14c3"
                  },
                  "rate": {
                    "name": "$lab904.lab904c3",
                    "Code": "$lab904.lab904c2"
                  },
                  "comments": [],
                  "attachment": [],
                  "demographics": {
                    "$map": {
                        "input": {
                          "$filter": {
                            "input": "$lab62",
                            "as": "demographic",
                            "cond": {}
                          }
                        },
                      "as": "demographic",
                      "in": {
                        "idDemographic": "$$demographic.lab62c1",
                        "demographic": "$$demographic.lab62c2",
                        "encoded": {
                            $cond: {
                                if: { $eq: ["$$demographic.lab62c4", 1] }, 
                                then: true, 
                                else: false 
                            }
                        },
                        "notCodifiedValue": "$$demographic.value",
                        "codifiedId": "$$demographic.lab63.lab63c1",
                        "codifiedCode": "$$demographic.lab63.lab63c2",
                        "codifiedName": "$$demographic.lab63.lab63c3"
                      }
                    }
                  },
                  "patient": {
                    "lastName": {
                      "$arrayElemAt": [
                        "$lab21.lab21c5",
                        0
                      ]
                    },
                    "surName": {
                      "$arrayElemAt": [
                        "$lab21.lab21c6",
                        0
                      ]
                    },
                    "name1": {
                        "$arrayElemAt": [
                          "$lab21.lab21c3",
                          0
                        ]
                      },
                    "name2": {
                        "$arrayElemAt": [
                            "$lab21.lab21c4",
                            0
                        ]
                    },
                    "birthday": {
                      "$arrayElemAt": [
                        "$lab21.lab21c7",
                        0
                      ]
                    },
                    "email": {
                        "$arrayElemAt": [
                            "$lab21.lab21c8",
                            0
                        ]
                    },
                    "address": {
                        "$arrayElemAt": [
                            "$lab21.lab21c17",
                            0
                        ]
                    },
                    "patientId": {
                      "$arrayElemAt": [
                        "$lab21.lab21c2",
                        0
                      ]
                    },
                    "documentType": {
                        "name": {
                            "$arrayElemAt": [
                              "$lab21.lab54.lab54c3",
                              0
                            ]
                          },
                        "abbr": {
                            "$arrayElemAt": [
                                "$lab21.lab54.lab54c2",
                              0
                            ]
                          }
                      },
                    "sex": {
                        "code":{
                            "$arrayElemAt": [
                              "$lab21.lab80.lab80c3",
                              0
                            ]
                          },
                        "esCo": {
                            "$arrayElemAt": [
                              "$lab21.lab80.lab80c4",
                              0
                            ]
                          },
                        "enUsa": {
                            "$arrayElemAt": [
                              "$lab21.lab80.lab80c5",
                              0
                            ]
                          }
                    },
                    "demographics": {
                      "$map": {
                        "input": {
                          "$filter": {
                            "input": {
                              "$arrayElemAt": [
                                "$lab21.lab62",
                                0
                              ]
                            },
                            "as": "demographic",
                            "cond": { }
                          }
                        },
                        "as": "demographic",
                        "in": {
                            "idDemographic": "$$demographic.lab62c1",
                            "demographic": "$$demographic.lab62c2",
                            "encoded": {
                                $cond: {
                                    if: { $eq: ["$$demographic.lab62c4", 1] }, 
                                    then: true, 
                                    else: false 
                                }
                            },
                            "notCodifiedValue": "$$demographic.value",
                            "codifiedId": "$$demographic.lab63.lab63c1",
                            "codifiedCode": "$$demographic.lab63.lab63c2",
                            "codifiedName": "$$demographic.lab63.lab63c3"
                            
                        }
                      }
                    },
                  },
                  resultTest: {
                    $map: {
                        input: {
                            $filter: {
                                input: "$lab39",
                                as: "lab39",
                                cond: { $eq: ["$$lab39.lab39.lab39c37", 0] } 
                            }
                        },
                        as: "lab39",
                        in: {
                            testId : "$$lab39.lab39.lab39c1",
                            testCode : "$$lab39.lab39.lab39c2",
                            testName : "$$lab39.lab39.lab39c4",
                            abbreviation: "$$lab39.lab39.lab39c3",
                            testType : "$$lab39.lab39.lab39c37",
                            sampleId : "$$lab39.lab24c1.lab24c1",
                            sampleCode : "$$lab39.lab24c1.lab24c9",
                            sampleName : "$$lab39.lab24c1.lab24c2",
                            entryDate: "$$lab39.lab57c4",
                            entryUserId : "$$lab39.lab57c5.lab04c1",
                            attachmentTest : "$$lab39.lab57c41",
                            technique: "$$lab39.lab64.lab64c3",
                            codetechnique: "$$lab39.lab64.lab64c2",
                            verificationDate : "$$lab39.lab57c37",
                            verificationUserId : "$$lab39.lab57c38.lab04c1",
                            result: "$$lab39.lab57c1",
                            resultDate: "$$lab39.lab57c2",
                            resultUserId : "$$lab39.lab57c3.lab04c1",
                            printDate: "$$lab39.lab57c22",
                            printUserId : "$$lab39.lab57c23.lab04c1",
                            takenDate:"$$lab39.lab57c39",
                            takenUserId : "$$lab39.lab57c40.lab04c1",
                            entryType : "$$lab39.lab57c35",
                            state : "$$lab39.lab57c8",
                            sampleState : "$$lab39.lab57c16",
                            areaId : "$$lab39.lab39.lab43.lab43c1",
                            areaCode : "$$lab39.lab39.lab43.lab43c2",
                            areaAbbr : "$$lab39.lab39.lab43.lab43c3",
                            areaName : "$$lab39.lab39.lab43.lab43c4",
                            resultType :"$$lab39.lab39.lab39c11",
                            Confidential : "$$lab39.lab39.lab39c27",
                            pathology : "$$lab39.lab57c9",
                            printSort: "$$lab39.lab39.lab39c42",
                            refLiteral : "$$lab39.lab50c1_4",
                            PanicLiteral : "$$lab39.lab50c1_2",
                            repeatAmmount : "$$lab39.lab57c33",
                            modificationAmmount : "$$lab39.lab57c24",
                            hasAntibiogram : "$$lab39.lab57c26",
                            hasTemplate: "$$lab39.lab57c42",
                            refMin: "$$lab39.lab48c12",
                            refMax: "$$lab39.lab48c13",
                            refComment: "$$lab39.lab48c9",
                            panicMin: "$$lab39.lab48c5",
                            panicMax: "$$lab39.lab48c6",
                            reportedMin: "$$lab39.lab48c14",
                            reportedMax: "$$lab39.lab48c15",
                            deltaMin: "$$lab39.lab57c27",
                            deltaMax:"$$lab39.lab57c28",
                            lastResult : "$$lab39.lab57c6",
                            lastResultDate: "$$lab39.lab57c7",
                            secondLastResult : "$$lab39.lab57c30",
                            secondLastResultDate : "$$lab39.lab57c31",
                            critic:  "$$lab39.lab48c16",
                            unit : "$$lab39.lab45c2",
                            digits : "$$lab39.lab39c12",
                            hasComment: "$$lab39.lab57c32",
                            printComment: "$$lab39.lab57c63",
                            validationDate : "$$lab39.lab57c18",
                            validationUserId : "$$lab39.lab57c19.lab04c1",
                            validationUserName: "$$lab39.lab57c19.lab04c2",
                            validationUserLastName: "$$lab39.lab57c19.lab04c3",
                            validationUserIdentification:"$$lab39.lab57c19.lab04c10",
                            validationUserSignatureCode:"$$lab39.lab57c19.lab04c13",
                            microbiologyGrowth:{
                                sample:{
                                    "id": "$$lab39.lab24c1_1.lab24c1",
                                    "name": "$$lab39.lab24c1_1.lab24c2",
                                    "codesample": "$$lab39.lab24c1_1.lab24c9"
                                },
                                anatomicalSite: {
                                    "id": "$$lab39.lab158c1",
                                    "name": "$$lab39.lab158c2",
                                    "Abbr": "$$lab39.lab158c3"
                                },
                                collectionMethod:{
                                    "id": "$$lab39.lab201c1",
                                    "name": "$$lab39.lab201c2",
                                }
                            },
                            microbialDetection: 
                            {
                                microorganisms: "$$lab39.lab206"
                            },
                            entryTestType: "$$lab39.lab57c36",
                            profileId : "$$lab39.lab57c14.lab39c1",
                            profileName : "$$lab39.lab57c14.lab39c4",
                            printSortProfile: "$$lab39.lab57c14.lab39c42",
                            resultComment:  { "comment": "$$lab39.lab95c1" },
                            optionsTemplate: "$$lab39.lab58",
                            imageTest: "$$lab39.doc03",
                            attachments: "$$lab39.doc02"
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
    let listimagetest: any[] = [];
    let listattachmenttest: any[] = [];
    ordersInserted.forEach((order: any) => {

        decrypt[order.patient.patientIdDB + "_document"] = order.patient.patientId;
        decrypt[order.patient.patientIdDB + "_name1"] = order.patient.name1;
        decrypt[order.patient.patientIdDB + "_name2"] = order.patient.name2;
        decrypt[order.patient.patientIdDB + "_lastName"] = order.patient.lastName;
        decrypt[order.patient.patientIdDB + "_surName"] = order.patient.surName;
        order.resultTest.forEach((result: any) => {
            decrypt[order.orderNumber + result.testId + "_result"] = result.result;
            decrypt[order.orderNumber + result.testId + "_lastResult"] = result.lastResult;
            decrypt[order.orderNumber + result.testId + "_secondLastResult"] = result.secondLastResult;
        });
    });

    let listOfData: any = await decryptData(decrypt);

    if (listOfData !== null && listOfData !== undefined) {
        ordersInserted.forEach((data: any) => {
            data.allDemographics =  _.concat(data.patient.demographics, data.demographics);
            data.patient.patientId = listOfData[data.patient.patientIdDB + "_document"];
            data.patient.name1 = listOfData[data.patient.patientIdDB + "_name1"];
            data.patient.name2 = listOfData[data.patient.patientIdDB + "_name2"];
            data.patient.lastName = listOfData[data.patient.patientIdDB + "_lastName"];
            data.patient.surName = listOfData[data.patient.patientIdDB + "_surName"];

            data.resultTest.forEach((result: any) => {
                result.result = listOfData[data.orderNumber + result.testId + "_result"];
                result.lastResult = listOfData[data.orderNumber + result.testId + "_lastResult"];
                result.secondLastResult = listOfData[data.orderNumber + result.testId + "_secondLastResult"];
                result.critic = 0;
                result.hasComment = result.hasComment  == 1;
                result.hasAntibiogram = result.hasAntibiogram  == 1;
                result.hasTemplate = result.hasTemplate  == 1;
                result.confidential = result.confidential == 1;
                result.orderPathology = 0;
                result.deltaMin = 0;
                result.deltaMax = 0;
                result.refCommentEnglish = "";
                result.nameAreaEnglish = "";
                result.nameTestEnglish = "";
                result.fixedCommentEnglish = "";
                result.templateComment = "";
                result.printCommentEnglish = "";
                result.resultEnglish = "";
                result.profileNameEnglish = "";

                if (result.refMin != null) {
                    result.refInterval =  result.refMin + " - " + result.refMax;
                } else {
                    result.refMin = 0;
                    result.refMax = 0;
                    result.refInterval = result.refLiteral;
                }
    
                if (result.panicMin != null) {
                    result.panicInterval = result.panicMin + " - " + result.panicMax;
                } else {
                    result.panicMin = 0;
                    result.panicMax = 0;
                    result.panicInterval = result.panicMin + " - " + result.panicMax;
                }
                
                if (result.reportedMin != null) {
                    result.reportedInterval = result.reportedMin + " - " + result.reportedMax;
                } else {
                    result.reportedMin = 0;
                    result.reportedMax = 0;
                }

                if(result.optionsTemplate.length > 0){
                    let listtemplate: any[] = [];
                    result.optionsTemplate.forEach((data: any) => {
                        var template = {
                            "option": data.lab51c2,
                            "result": data.lab58c1,
                            "comment": data.lab58c3
                        }
                        listtemplate.push(template)
                    })
                    result.optionsTemplate = listtemplate;
                }

           
                if(result.microbialDetection.microorganisms.length > 0){

                    let listmicroorganism: any[] = [];
                    result.microbialDetection.microorganisms.forEach((data: any) => {
                        let microorganism: any = {};
                        microorganism = {
                            "id": data.lab76c1,
                            "name": data.lab76c2,
                            "idMicrobialDetection": data.lab204c1,
                            "comment":  data.lab204c2,
                            "recount": data.lab204c4,
                            "complementations": data.lab204c6,
                            "resultsMicrobiology": [] 
                        }
                        let listresultsMicrobiology: any[] = [];
                        data.lab204.forEach((data1: any) => {
                            let resultsMicrobiology: any = {};
                            resultsMicrobiology =  {
                                                            "idSensitivity": data1.lab77c1 ,
                                                            "idAntibiotic": data1.lab79c1 ,
                                                            "nameAntibiotic": data1.lab79c2 ,
                                                            "line": data1.lab78c2 ,
                                                            "unit": data1.lab45c1 ,
                                                            "cmi": data1.lab205c1 ,
                                                            "interpretationCMI": data1.lab205c2 ,
                                                            "cmiM": data1.lab205c3,
                                                            "interpretationCMIM": data1.lab205c4,
                                                            "cmiMPrint": data1.lab205c5 ,
                                                            "disk": data1.lab205c6 ,
                                                            "interpretationDisk": data1.lab205c7 ,
                                                            "diskPrint": data1.lab205c8 
                                                        }

                            listresultsMicrobiology.push(resultsMicrobiology)
                        })
                        microorganism.resultsMicrobiology = listresultsMicrobiology;
                        listmicroorganism.push(microorganism)
                    })
                    result.microbialDetection.microorganisms = listmicroorganism;
                }

                if(result.imageTest.length > 0){
                    result.imageTest.forEach((data: any) => {
                        let image: any = {};   
                        image = {   
                            "order": data.lab22c1,
                            "testId": data.lab39c1,
                            "imageName1": data.doc03c1,
                            "image1": data.routeBD1,
                            "imageName2": data.doc03c3,
                            "image2": data.routeBD2,
                            "imageName3": data.doc03c5,
                            "image3": data.routeBD3,
                            "imageName4": data.doc03c7,
                            "image4": data.routeBD4,
                            "imageName5": data.doc03c9,
                            "image5": data.routeBD5
                        }
                        listimagetest.push(image);
                    })
                }
                if(result.attachments.length > 0){
                    result.attachments.forEach((data: any) => {
                        let attachment: any = {};   
                        attachment = {   
                            "order": data.lab22c1,
                            "testId": data.lab39c1,
                            "name": data.doc02c1,
                            "file" : data.routeBD,
                            "date" : data.doc02c3,
                            "fileType" : data.doc02c4,
                            "extension" : data.doc02c5,
                            "viewresul" : data.doc02c6 == 1
                        }
                        listattachmenttest.push(attachment);
                    })
                }

            }); 
            data.imageTest = listimagetest;
            data.attachments = listattachmenttest;
        });
    }

    for (let j = 0; j < listimagetest.length; j++) {
        listimagetest[j].image1 = await convertImagesToBase64(listimagetest[j].image1);
        if(listimagetest[j].image2 != ""){
            listimagetest[j].image2 = await convertImagesToBase64(listimagetest[j].image2);
        }
        if(listimagetest[j].image3 != ""){
            listimagetest[j].image3 = await convertImagesToBase64(listimagetest[j].image3);
        }
        if(listimagetest[j].image4 != ""){
            listimagetest[j].image4 = await convertImagesToBase64(listimagetest[j].image4);
        }
        if(listimagetest[j].image5 != ""){
            listimagetest[j].image5 = await convertImagesToBase64(listimagetest[j].image5);
        }
    }

    for (let j = 0; j < listattachmenttest.length; j++) {
        listattachmenttest[j].file = await convertImagesToBase64(listattachmenttest[j].file);
        
    }

    return ordersInserted;
}

// Función asincrónica para leer y convertir cada imagen a base64
const convertImagesToBase64 = async (imagePath: string) => {
    return new Promise<string>((resolve, reject) => {
        fs.readFile(imagePath, (err, data) => {
            if (err) {
                reject(`Error al leer la imagen ${imagePath}: ${err}`);
                return;
            }
            // Convierte la imagen a base64
            const base64Image = Buffer.from(data).toString('base64');
            resolve(base64Image);
        });
    });
};

