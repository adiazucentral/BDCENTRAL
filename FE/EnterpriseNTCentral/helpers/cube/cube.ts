import { decryptData, encryptData, getValueKey, toISOStringLocal } from "../../tools/common";
import { getMatch, mapData, mapQuery } from "../../tools/cube";
import mongoose from "mongoose";
import OrderSchema from "../../models/central/order";


export const getDataCube = async (body: any, token: any) => {

    //Digitos de la orden
    let orderDigits = await getValueKey('DigitosOrden', token);
    let centralSystem = await getValueKey('SistemaCentralListados', token);
    let entity = await getValueKey('Entidad', token);;
    let nit = '';

    const info = { entity, nit };

    let orderInit = body.init + "0".repeat(orderDigits);
    let orderEnd = body.end + "9".repeat(orderDigits);

    const startYear = body.init.substring(0, 4);
    const endYear = body.end.substring(0, 4);

    //Historicos
    let years = [];
    let yearTraveled = +startYear;
    while (yearTraveled <= +endYear) {
        years.push(yearTraveled);
        yearTraveled++;
    }

    const filterByEncriptData = body.demographics.filter((demo: any) => demo.demographic === -100 || demo.demographic === -101 || demo.demographic === -102 || demo.demographic === -103 || demo.demographic === -109);
    if (filterByEncriptData.length) {
        const encryptDataFilter: any = {};
        filterByEncriptData.forEach((demo: any) => {
            encryptDataFilter[`${demo.demographic}`] = demo.value;
        });
        if (Object.values(encryptDataFilter).length > 0) {
            let data: any = await encryptData(encryptDataFilter);
            if (data !== null && data !== undefined && Object.keys(data).length > 0) {
                filterByEncriptData.forEach((demo: any) => {
                    const findDemo = body.demographics.find((demoFilter: any) => demoFilter.demographic === demo.demographic);
                    if (findDemo) {
                        findDemo.value = data[`${demo.demographic}`];
                    }
                });
            }
        }
    }

    let list: any = [];
    await Promise.all(
        years.map(async (year:any) => {

            //Filtrso
            let filters = {
                profiles: body.profiles,
                areas: body.listAreas,
                demographics: body.demographics,
                tests: body.listTests
            }

            let { projections, relations } = await mapQuery(body, year, centralSystem, filters);
            let match = await getMatch(filters, orderInit, orderEnd);

            const nameCollectionOrders = `lab22_${year}`;
            const CollectionOrders = mongoose.model(nameCollectionOrders, OrderSchema);

            await CollectionOrders.aggregate([
                {
                    $match: match,
                },
                ...relations,
                ...projections
            ]).exec()
                .then(async (response) => {
                    if (response.length > 0) {

                        if (body.listDemosH !== null && body.listDemosH !== undefined && body.listDemosH.length > 0) {
                            response =  response.filter( orders => orders.patient !== undefined && orders.patient !== null );
                        }

                        if (body.listDemosR !== null && body.listDemosR !== undefined && body.listDemosR.length > 0) {
                            response =  response.filter( orders => orders.results.length > 0 );
                        }

                        const result = await mapData(body, response, centralSystem, info);
                        if (list.length > 0) {
                            list = [{ ...list }, { ...result }];
                        } else {
                            list = result;
                        }
                    }
                })
                .catch((err) => {
                    console.error(err);
                });
        })
    );
    let finalData = list;
    const decrypt: any = {};
    if (finalData.length > 0) {
        finalData.forEach((data: any) => {
            if (data.patient) {
                if (data.patient.patientId) {
                    decrypt[`${data.order}_document`] = data.patient.patientId;
                }
                if (data.patient.name1) {
                    decrypt[`${data.order}_name1`] = data.patient.name1;
                }
                if (data.patient.name2) {
                    decrypt[`${data.order}_name2`] = data.patient.name2;
                }
                if (data.patient.lastName) {
                    decrypt[`${data.order}_lastName`] = data.patient.lastName;
                }
                if (data.patient.surName) {
                    decrypt[`${data.order}_surName`] = data.patient.surName;
                }
            }
            if (data.results !== undefined && data.results !== null && data.results.length > 0) {
                data.results.forEach((result: any) => {
                    if (result.result !== undefined && result.result !== null && result.result !== "") {
                        decrypt[`${data.order}_${result.idTest}`] = result.result;
                    }
                });
            }
        });
        if (Object.values(decrypt).length > 0) {
            let listOfData: any = await decryptData(decrypt);
            if (listOfData !== null && listOfData !== undefined && Object.keys(listOfData).length > 0) {
                finalData.forEach((data: any) => {
                    if (data.patient) {
                        if (data.patient.patientId) {
                            data.patient.patientId = listOfData[`${data.order}_document`];
                        }
                        if (data.patient.name1) {
                            data.patient.name1 = listOfData[`${data.order}_name1`];
                            data.patient.name1 = data.patient.name1 === null || data.patient.name1 === undefined || data.patient.name1 === "" ? "" : data.patient.name1.trim();
                        }
                        if (data.patient.name2) {
                            data.patient.name2 = listOfData[`${data.order}_name2`];
                            data.patient.name2 = data.patient.name2 === null || data.patient.name2 === undefined || data.patient.name2 === "" ? "" : data.patient.name2.trim();
                        }
                        if (data.patient.lastName) {
                            data.patient.lastName = listOfData[`${data.order}_lastName`];
                            data.patient.lastName = data.patient.lastName === null || data.patient.lastName === undefined || data.patient.lastName === "" ? "" : data.patient.lastName.trim();
                        }
                        if (data.patient.surName) {
                            data.patient.surName = listOfData[`${data.order}_surName`];
                            data.patient.surName = data.patient.surName === null || data.patient.surName === undefined || data.patient.surName === "" ? "" : data.patient.surName.trim();
                        }
                    }
                    if (data.results !== undefined && data.results !== null && data.results.length > 0) {
                        data.results.forEach((result: any) => {
                            if (result.result !== undefined && result.result !== null && result.result !== "") {
                                result.result = listOfData[`${data.order}_${result.idTest}`];
                                let resultDes = result.result === null || result.result === undefined || result.result === "" ? "" : result.result.replace(/<br\s*\/?>/g, '');
                                resultDes = resultDes.replaceAll(',', '');
                                resultDes = resultDes.replace(/(\r\n|\n|\r)/gm, "");
                                result.result = resultDes;
                            }
                        });
                    }
                });
            } else {
                return 501;
            }
        }
    }
    return finalData;
};