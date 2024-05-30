import * as _ from "lodash";
import mongoose from 'mongoose';
import OrderSchema from "../../models/central/order";
import { decryptData, encryptData } from "../../tools/common";

export const listorderfilter = async (filter: Outreachfilterorder) => {
    let ordersInserted: any[] = [];

    const nameCollectionOrders = `lab22_2024`;
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
            $gte: filter.dateNumberInit,
            $lte: filter.dateNumberEnd
        }
    }
    if(filter.year != null){
        filters["lab22c2"] = {
            $gte: parseInt(filter.year + "0101")
        }
    }
    
    try {
        await CollectionOrders.aggregate([
            {
            $lookup: {
                from: "lab21", 
                localField: 'lab21c2', 
                foreignField: 'lab21c2',
                as: 'lab21' 
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