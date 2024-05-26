import { createDemographicsQuery, toISOStringLocal } from "../../../tools/common";
import { db } from "../../conection";

export const getOrderByYear = (parameters: any, index: any, final: any) => {
    return new Promise((resolve) => {
        setTimeout(async () => {
            let response = await getOrders(parameters.years[index], parameters.demographics);
            if(response.length > 0) {
                let orders: Order[] = [];
                response.forEach((resp: any) => {

                    if (orders[resp.lab22c1] === undefined || orders[resp.lab22c1] === null) {

                        let listDemoOrder: Demographic[] = [];
                        if(parameters.demographics) {
                            parameters.demographics.forEach( (demo:any) => {
                                let dataDemo:Demographic = {
                                    lab62c1: demo.id,
                                    lab62c2: demo.name,
                                    lab62c3: demo.origin,
                                    lab62c4: demo.type,
                                    lab62c5: demo.obligatory,
                                    lab62c6: demo.ordering,
                                    value: "",
                                    lab63: undefined
                                }
                                if(demo.encoded) {
                                    dataDemo.lab63 = {
                                        lab63c1: resp[`demo${demo.id}_id`],
                                        lab63c2: resp[`demo${demo.id}_code`],
                                        lab63c3: resp[`demo${demo.id}_name`],
                                    }
                                } else {
                                    dataDemo.value = resp[`lab_demo_${demo.id}`];
                                }
                                listDemoOrder.push(dataDemo);
                            });
                        }

                        orders[resp.lab22c1] = {
                            lab22c1         : resp.lab22c1,
                            lab22c2         : resp.lab22c2,
                            lab22c3         : resp.lab22c3,
                            lab21c2         : resp.lab21c2,
                            lab22c4         : resp.lab22c4,
                            lab22c5         : resp.lab22c5,
                            lab22c6         : resp.lab22c6,
                            lab22c7         : resp.lab22c7,
                            lab22c8         : resp.lab22c8,
                            lab22c9         : resp.lab22c9,
                            lab22c10        : resp.lab22c10,
                            lab22c11        : resp.lab22c11,
                            lab22c12        : resp.lab22c12,
                            lab22c13        : resp.lab22c13,
                            lab22c14        : resp.lab22c14,
                            lab22c15        : resp.lab22c15,
                            lab22c16        : resp.lab22c16,
                            lab22c17        : resp.lab22c17,
                            lab22c18        : resp.lab22c18,
                            lab22c19        : resp.lab22c19,
                            lab62           : listDemoOrder,
                            lab54:          {
                                lab54c1     : resp.lab54c1,
                                lab54c2     : resp.lab54c2,
                                lab54c3     : resp.lab54c3
                            },
                            lab103          :  {
                                lab103c1    : resp.lab103c1,
                                lab103c2    : resp.lab103c2,
                                lab103c3    : resp.lab103c3
                            },
                            lab04c1         : {
                                lab04c1     : resp.lab22lab04c1lab04c1,
                                lab04c2     : resp.lab22lab04c1lab04c2,
                                lab04c3     : resp.lab22lab04c1lab04c3,
                                lab04c4     : resp.lab22lab04c1lab04c4
                            },
                            lab07c1         : resp.statusOrder,
                            lab05           : {
                                lab05c1     : resp.lab05c1, 
                                lab05c2     : resp.lab05c2,
                                lab05c4     : resp.lab05c4,
                                lab05c10    : resp.lab05c10
                            },
                            lab10           : {
                                lab10c1     : resp.lab10c1, 
                                lab10c2     : resp.lab10c2, 
                                lab10c7     : resp.lab10c7
                            },
                            lab19           : {
                                lab19c1     : resp.lab19c1,
                                lab19c2     : resp.lab19c2,
                                lab19c3     : resp.lab19c3,
                                lab19c22    : resp.lab19c22
                            },
                            lab14           : {
                                lab14c1     : resp.lab14c1,
                                lab14c2     : resp.lab14c2,
                                lab14c3     : resp.lab14c3,
                                lab14c21    : resp.lab14c21
                            },
                            lab904          : {
                                lab904c1    : resp.lab904c1,
                                lab904c2    : resp.lab904c2,
                                lab904c3    : resp.lab904c3
                            },
                            lab57c9         : resp.lab57c9,
                            lab04c1_1       : {
                                lab04c1     : resp.lab22lab04c1_1lab04c1,
                                lab04c2     : resp.lab22lab04c1_1lab04c2,
                                lab04c3     : resp.lab22lab04c1_1lab04c3,
                                lab04c4     : resp.lab22lab04c1_1lab04c4
                            },
                            lab930c1        : resp.lab930c1,
                            lab60           : []
                        }
                    }


                    if (orders[resp.lab22c1].lab60 === undefined && orders[resp.lab22c1].lab60 === null) {
                        orders[resp.lab22c1].lab60 = [];
                    } else {
                        orders[resp.lab22c1].lab60.push({
                            lab60c1: resp.lab60c1,
                            lab60c3: resp.lab60c3,
                            lab60c4: resp.lab60c4,
                            lab60c6: resp.lab60c6
                        });
                    }
                });
    
                const listByYear = {
                    year: parameters.years[index],
                    orders: Object.values(orders)
                }
                final.push(listByYear);
            }
            if (index < (parameters.years.length - 1)) {
                resolve(getOrderByYear(parameters.years, index + 1, final));
            } else {
                resolve(final);
            }
        });
    });
}

const getOrders = async (year: number, demographics: any) => {

    console.log(`Inicia consulta de ordenes para envio a bd central a las: `, toISOStringLocal(new Date()));

    let currentYear = new Date().getFullYear();

    let lab22 = "lab22";
    let lab60 = "lab60";

    if(year !== currentYear) {
        lab22 = "lab22_" + year;
        lab60 = "lab60_" + year;
    }

    let query = " ";
 
    query += ` SELECT TOP 100 lab22c1, lab22c2, lab22c3, lab22c4, lab22c5, lab22c6, lab22c7, lab22c8, lab22c9, lab22c10, 
        lab22c11, lab22c12, lab22c13, lab22c14, lab22c15, lab22c16, lab22c17, lab22c18, lab22c19,
        lab103.lab103c1, lab103c2, lab103c3,
        lab22lab04c1.lab04c1 as lab22lab04c1lab04c1, lab22lab04c1.lab04c2 as lab22lab04c1lab04c2, lab22lab04c1.lab04c3 as lab22lab04c1lab04c3, lab22lab04c1.lab04c4 as lab22lab04c1lab04c4,
        lab22.lab07c1 as statusOrder,
        lab05.lab05c1, lab05c2, lab05c4, lab05c10,
        lab10.lab10c1, lab10c2, lab10c7,
        lab19.lab19c1, lab19c2, lab19c3, lab19c22,
        lab14.lab14c1, lab14c2, lab14c3, lab14c21,
        lab904.lab904c1, lab904c2, lab904c3,
        lab57c9,
        lab22lab04c1_1.lab04c1 as lab22lab04c1_1lab04c1, lab22lab04c1_1.lab04c2 as lab22lab04c1_1lab04c2, lab22lab04c1_1.lab04c3 as lab22lab04c1_1lab04c3, lab22lab04c1_1.lab04c4 as lab22lab04c1_1lab04c4,
        lab930c1,
        lab60.lab60c1, lab60c3, lab60c4, lab60c6,
        lab21c2, lab54.lab54c1, lab54c2, lab54c3
    `;
    
    let from = ` `;

    from += ` FROM ${lab22} as lab22
        LEFT JOIN lab103 ON lab103.lab103c1 = lab22.lab103c1
        LEFT JOIN lab04 lab22lab04c1 ON lab22lab04c1.lab04c1 = lab22.lab04c1
        LEFT JOIN lab05 ON lab05.lab05c1 = lab22.lab05c1
        LEFT JOIN lab10 ON lab10.lab10c1 = lab22.lab10c1
        LEFT JOIN lab19 ON lab19.lab19c1 = lab22.lab19c1
        LEFT JOIN lab14 ON lab14.lab14c1 = lab22.lab14c1
        LEFT JOIN lab904 ON lab904.lab904c1 = lab22.lab904c1
        LEFT JOIN lab04 lab22lab04c1_1 ON lab22lab04c1_1.lab04c1 = lab22.lab04c1_1
        LEFT JOIN ${lab60} as lab60 ON lab60.lab60c2 = lab22.lab22c1
        LEFT JOIN lab21 ON lab21.lab21c1 = lab22.lab21c1
        LEFT JOIN lab54 ON lab54.lab54c1 = lab21.lab54c1
    `;

    if(demographics !== null && demographics !== undefined && demographics.length > 0) {
        let querysDemos:any = await createDemographicsQuery(demographics.filter((demo:any) => demo.id > 0));
        query += querysDemos.select;
        from += querysDemos.from;
    }

    from += " WHERE lab22c20 = 0 OR lab22c20 IS NULL ";

    const [orders] = await db.query(query + from);

    console.log(`Respondio consulta de ordenes a las: `, toISOStringLocal(new Date()));

    return orders;
}

export const changeFlagOrder = async( docsInserted: any[] ) => {
    if(docsInserted !== null && docsInserted !== undefined && docsInserted.length > 0) {
        let currentYear = new Date().getFullYear();
        let lab22 = "";
        for (const listDocs of docsInserted) {
            lab22 = currentYear === listDocs.year ? "lab22" : "lab22_" + listDocs.year;
            const query = `UPDATE ${lab22} SET lab22c20 = 1, lab22c21 = :date WHERE lab22c1 = :idOrder`; 
            for (const docs of listDocs.orders) {
                let date = docs.created;
                if(date < docs.updated) {
                    date = docs.updated;
                }
                const [numFiles] = await db.query(query,
                    {
                      replacements: { date: date, idOrder: docs.lab22c1 }
                    }
                );
            }
        }
    }
}