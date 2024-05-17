import { toISOStringLocal } from "../../../tools/common";
import { db } from "../../conection";

export const getResultsByYear = (parameters: any, index: any, final: any) => {
    return new Promise((resolve) => {
        setTimeout(async () => {
            let response = await getResults(parameters.years[index]);
            if(response.length > 0) {
                let results: Result[] = [];
                response.forEach((resp: any) => {
                    const resultExists = results.find( result => result.lab22c1 === resp.lab22c1 && result.lab39.lab39c1 === resp.lab39c1);
                    if(!resultExists) {
                        results.push({
                            lab22c1         : resp.lab22c1,
                            lab57c1         : resp.lab57c1,
                            lab57c2         : resp.lab57c2,
                            lab57c4         : resp.lab57c4,
                            lab57c6         : resp.lab57c6,
                            lab57c7         : resp.lab57c7,
                            lab57c8         : resp.lab57c8,
                            lab57c9         : resp.lab57c9,
                            lab57c10        : resp.lab57c10,
                            lab57c11        : resp.lab57c11,
                            lab57c16        : resp.lab57c16,
                            lab57c17        : resp.lab57c17,
                            lab57c18        : resp.lab57c18,
                            lab57c20        : resp.lab57c20,
                            lab57c22        : resp.lab57c22,
                            lab45c2         : resp.lab45c2,
                            lab48c1         : resp.lab48c1,
                            lab57c24        : resp.lab57c24,
                            lab57c25        : resp.lab57c25,
                            lab57c26        : resp.lab57c26,
                            lab57c27        : resp.lab57c27,
                            lab57c28        : resp.lab57c28,
                            lab57c29        : resp.lab57c29,
                            lab48c5         : resp.lab48c5,
                            lab48c6         : resp.lab48c6,
                            lab48c12        : resp.lab48c12,
                            lab48c13        : resp.lab48c13,
                            lab48c14        : resp.lab48c14,
                            lab48c15        : resp.lab48c15,
                            lab57c30        : resp.lab57c30,
                            lab57c31        : resp.lab57c31,
                            lab57c32        : resp.lab57c32,
                            lab57c33        : resp.lab57c33,
                            lab57c34        : resp.lab57c34,
                            lab57c35        : resp.lab57c35,
                            lab57c36        : resp.lab57c36,
                            lab57c37        : resp.lab57c37,
                            lab57c39        : resp.lab57c39,
                            lab158c1        : resp.lab158c1,
                            lab201c1        : resp.lab201c1,
                            lab57c41        : resp.lab57c41,
                            lab57c42        : resp.lab57c42,
                            lab57c43        : resp.lab57c43,
                            lab57c44        : resp.lab57c44,
                            lab57c46        : resp.lab57c46,
                            lab57c48        : resp.lab57c48,
                            lab57c49        : resp.lab57c49,
                            lab57c50        : resp.lab57c50,
                            lab57c51        : resp.lab57c51,
                            lab57c52        : resp.lab57c52,
                            lab57c53        : resp.lab57c53,
                            lab57c54        : resp.lab57c54,
                            lab57c55        : resp.lab57c55,
                            lab57c56        : resp.lab57c56,
                            lab57c57        : resp.lab57c57,
                            lab57c59        : resp.lab57c59,
                            lab57c61        : resp.lab57c61,
                            lab57c62        : resp.lab57c62,
                            lab57c63        : resp.lab57c63,
                            lab57c64        : resp.lab57c64,
                            lab57c65        : resp.lab57c65,
                            lab57c67        : resp.lab57c67,
                            lab57c68        : resp.lab57c68,
                            lab57c69        : resp.lab57c69,
                            lab57c70        : resp.lab57c70,
                            lab57c71        : resp.lab57c71,
                            lab57c72        : resp.lab57c72,
                            lab57c73        : resp.lab57c73,
                            lab57c74        : resp.lab57c74,
                            lab57c75        : resp.lab57c75,
                            lab39           :  {
                                lab39c1     : resp.lab39c1,     
                                lab39c2     : resp.lab39c2,
                                lab39c3     : resp.lab39c3,
                                lab39c4     : resp.lab39c4,
                                lab39c5     : resp.lab39c5,
                                lab39c6     : resp.lab39c6,
                                lab39c7     : resp.lab39c7,
                                lab39c8     : resp.lab39c8,
                                lab39c9     : resp.lab39c9,
                                lab39c11    : resp.lab39c11,
                                lab39c12    : resp.lab39c12,
                                lab39c19    : resp.lab39c19,
                                lab39c20    : resp.lab39c20,
                                lab39c21    : resp.lab39c21,
                                lab39c22    : resp.lab39c22,
                                lab39c23    : resp.lab39c23,
                                lab39c37    : resp.lab39c37,
                                lab43       : {
                                    lab43c1 : resp.lab43c1,
                                    lab43c2 : resp.lab43c2,
                                    lab43c3 : resp.lab43c3,
                                    lab43c4 : resp.lab43c4,
                                    lab43c6 : resp.lab43c6,
                                    lab43c9 : resp.lab43c9
                                }
                            },
                            lab57c3         : {
                                lab04c1     : resp.lab57lab57c3lab04c1,
                                lab04c2     : resp.lab57lab57c3lab04c2,
                                lab04c3     : resp.lab57lab57c3lab04c3,
                                lab04c4     : resp.lab57lab57c3lab04c4
                            },
                            lab57c5         : {
                                lab04c1     : resp.lab57lab57c5lab04c1,
                                lab04c2     : resp.lab57lab57c5lab04c2,
                                lab04c3     : resp.lab57lab57c5lab04c3,
                                lab04c4     : resp.lab57lab57c5lab04c4
                            },
                            lab57c12         : {
                                lab04c1     : resp.lab57lab57c12lab04c1,
                                lab04c2     : resp.lab57lab57c12lab04c2,
                                lab04c3     : resp.lab57lab57c12lab04c3,
                                lab04c4     : resp.lab57lab57c12lab04c4
                            },
                            lab57c14        : {
                                lab39c1     : resp.lab57lab57c14lab39c1,     
                                lab39c2     : resp.lab57lab57c14lab39c2,
                                lab39c3     : resp.lab57lab57c14lab39c3,
                                lab39c4     : resp.lab57lab57c14lab39c4
                            },
                            lab57c15        : {
                                lab39c1     : resp.lab57lab57c15lab39c1,     
                                lab39c2     : resp.lab57lab57c15lab39c2,
                                lab39c3     : resp.lab57lab57c15lab39c3,
                                lab39c4     : resp.lab57lab57c15lab39c4
                            },
                            lab57c19         : {
                                lab04c1     : resp.lab57lab57c19lab04c1,
                                lab04c2     : resp.lab57lab57c19lab04c2,
                                lab04c3     : resp.lab57lab57c19lab04c3,
                                lab04c4     : resp.lab57lab57c19lab04c4
                            },
                            lab57c21         : {
                                lab04c1     : resp.lab57lab57c21lab04c1,
                                lab04c2     : resp.lab57lab57c21lab04c2,
                                lab04c3     : resp.lab57lab57c21lab04c3,
                                lab04c4     : resp.lab57lab57c21lab04c4
                            },
                            lab57c23         : {
                                lab04c1     : resp.lab57lab57c23lab04c1,
                                lab04c2     : resp.lab57lab57c23lab04c2,
                                lab04c3     : resp.lab57lab57c23lab04c3,
                                lab04c4     : resp.lab57lab57c23lab04c4
                            },
                            lab64           : {
                                lab64c1     : resp.lab64c1,
                                lab64c2     : resp.lab64c2,
                                lab64c3     : resp.lab64c3,
                            },
                            lab40c1         : {
                                lab40c1     : resp.lab40c1lab40c1,
                                lab40c2     : resp.lab40c1lab40c2,
                                lab40c3     : resp.lab40c1lab40c3
                            },
                            lab05           : {
                                lab05c1     : resp.lab05c1,
                                lab05c2     : resp.lab05c2,
                                lab05c4     : resp.lab05c4,
                                lab05c10    : resp.lab05c10
                            },
                            lab50c1_1       : resp.lab50c1_1,
                            lab50c1_2       : resp.lab50c2p,
                            lab50c1_3       : resp.lab50c1_3,
                            lab50c1_4       : resp.lab50c2n,
                            lab57c38         : {
                                lab04c1     : resp.lab57lab57c38lab04c1,
                                lab04c2     : resp.lab57lab57c38lab04c2,
                                lab04c3     : resp.lab57lab57c38lab04c3,
                                lab04c4     : resp.lab57lab57c38lab04c4
                            },
                            lab57c40         : {
                                lab04c1     : resp.lab57lab57c40lab04c1,
                                lab04c2     : resp.lab57lab57c40lab04c2,
                                lab04c3     : resp.lab57lab57c40lab04c3,
                                lab04c4     : resp.lab57lab57c40lab04c4
                            },
                            lab24c1         : {
                                lab24c1     : resp.lab57lab24c1lab24c1,
                                lab24c2     : resp.lab57lab24c1lab24c2,
                                lab24c9     : resp.lab57lab24c1lab24c9
                            },
                            lab24c1_1       : {
                                lab24c1     : resp.lab57lab24c1_1lab24c1,
                                lab24c2     : resp.lab57lab24c1_1lab24c2,
                                lab24c9     : resp.lab57lab24c1_1lab24c9
                            },
                            lab57c45         : {
                                lab04c1     : resp.lab57lab57c45lab04c1,
                                lab04c2     : resp.lab57lab57c45lab04c2,
                                lab04c3     : resp.lab57lab57c45lab04c3,
                                lab04c4     : resp.lab57lab57c45lab04c4
                            },
                            lab57c47        : {
                                lab04c1     : resp.lab57lab57c47lab04c1,
                                lab04c2     : resp.lab57lab57c47lab04c2,
                                lab04c3     : resp.lab57lab57c47lab04c3,
                                lab04c4     : resp.lab57lab57c47lab04c4
                            },
                            lab40c1a        : {
                                lab40c1     : resp.lab40c1alab40c1,
                                lab40c2     : resp.lab40c1alab40c2,
                                lab40c3     : resp.lab40c1alab40c3
                            },
                            lab57c58        : {
                                lab04c1     : resp.lab57lab57c58lab04c1,
                                lab04c2     : resp.lab57lab57c58lab04c2,
                                lab04c3     : resp.lab57lab57c58lab04c3,
                                lab04c4     : resp.lab57lab57c58lab04c4
                            },
                            lab57c60        : {
                                lab04c1     : resp.lab57lab57c60lab04c1,
                                lab04c2     : resp.lab57lab57c60lab04c2,
                                lab04c3     : resp.lab57lab57c60lab04c3,
                                lab04c4     : resp.lab57lab57c60lab04c4
                            },
                            lab57c66        : {
                                lab04c1     : resp.lab57lab57c66lab04c1,
                                lab04c2     : resp.lab57lab57c66lab04c2,
                                lab04c3     : resp.lab57lab57c66lab04c3,
                                lab04c4     : resp.lab57lab57c66lab04c4
                            },
                            lab95           : [],
                            lab206          : [],
                            lab58           : []
                        });
                    } else {
                        resultExists.lab95.push(resp.lab95c1);
                    }
                });
    
                const listByYear = {
                    year        : parameters.years[index],
                    results     : Object.values(results)
                }

                final.push(listByYear);
            }
            if (index < (parameters.years.length - 1)) {
                resolve(getResultsByYear(parameters.years, index + 1, final));
            } else {
                resolve(final);
            }
        });
    });
}

const getResults = async (year: number) => {

    console.log(`Inicia consulta de resultados para envio a bd central a las: `, toISOStringLocal(new Date()));


    let currentYear = new Date().getFullYear();

    let lab57 = "lab57";
    let lab95 = "lab95";

    if(year !== currentYear) {
        lab57 = "lab57_" + year;
        lab95 = "lab95_" + year;
    }

    let query = " ";
 
    query += ` SELECT lab57.lab22c1, lab57c1, lab57c2, lab57c4, lab57c6, lab57c7, lab57c8, lab57c9, lab57c10, lab57c11, lab57c16, lab57c17, lab57c18, 
    lab57c20, lab57c22, lab45c2, lab48c1, lab57c24, lab57c25, lab57c26, lab57c27, lab57c28, lab57c29, lab48c5, lab48c6, lab48c12, 
    lab48c13, lab48c14, lab48c15, lab57c30, lab57c31, lab57c32, lab57c33, lab57c34, lab57c35, lab57c36, lab57c37, lab57c39, 
    lab158c1, lab201c1, lab57c41, lab57c42, lab57c43, lab57c44, lab57c46, lab57c48, lab57c49, lab57c50, lab57c51, lab57c52, 
    lab57c53, lab57c54, lab57c55, lab57c56, lab57c57, lab57c59, lab57c61, lab57c62, lab57c63, lab57c64, lab57c65, lab57c67, 
    lab57c68, lab57c69, lab57c70, lab57c71, lab57c72, lab57c73, lab57c74, lab57c75,
    lab39.lab39c1, lab39.lab39c2, lab39.lab39c3, lab39.lab39c4, lab39.lab39c5, lab39.lab39c6, lab39.lab39c7, lab39.lab39c8, lab39.lab39c9, lab39.lab39c11, lab39.lab39c12, lab39.lab39c19, lab39.lab39c20, lab39.lab39c21, lab39.lab39c22, lab39.lab39c23, lab39.lab39c37, lab39.lab39c58,
    lab43.lab43c1, lab43c2, lab43c3, lab43c4, lab43c6, lab43c9,
    lab57lab57c3.lab04c1 as lab57lab57c3lab04c1, lab57lab57c3.lab04c2 as lab57lab57c3lab04c2, lab57lab57c3.lab04c3 as lab57lab57c3lab04c3, lab57lab57c3.lab04c4 as lab57lab57c3lab04c4,
    lab57lab57c5.lab04c1 as lab57lab57c5lab04c1, lab57lab57c5.lab04c2 as lab57lab57c5lab04c2, lab57lab57c5.lab04c3 as lab57lab57c5lab04c3, lab57lab57c5.lab04c4 as lab57lab57c5lab04c4,
    lab57lab57c12.lab04c1 as lab57lab57c12lab04c1, lab57lab57c12.lab04c2 as lab57lab57c12lab04c2, lab57lab57c12.lab04c3 as lab57lab57c12lab04c3, lab57lab57c12.lab04c4 as lab57lab57c12lab04c4,
    lab57lab57c14.lab39c1 as lab57lab57c14lab39c1, lab57lab57c14.lab39c2 as lab57lab57c14lab39c2, lab57lab57c14.lab39c3 as lab57lab57c14lab39c3, lab57lab57c14.lab39c4 as lab57lab57c14lab39c4,
    lab57lab57c15.lab39c1 as lab57lab57c15lab39c1, lab57lab57c15.lab39c2 as lab57lab57c15lab39c2, lab57lab57c15.lab39c3 as lab57lab57c15lab39c3, lab57lab57c15.lab39c4 as lab57lab57c15lab39c4,
    lab57lab57c19.lab04c1 as lab57lab57c19lab04c1, lab57lab57c19.lab04c2 as lab57lab57c19lab04c2, lab57lab57c19.lab04c3 as lab57lab57c19lab04c3, lab57lab57c19.lab04c4 as lab57lab57c19lab04c4,
    lab57lab57c21.lab04c1 as lab57lab57c21lab04c1, lab57lab57c21.lab04c2 as lab57lab57c21lab04c2, lab57lab57c21.lab04c3 as lab57lab57c21lab04c3, lab57lab57c21.lab04c4 as lab57lab57c21lab04c4,
    lab57lab57c23.lab04c1 as lab57lab57c23lab04c1, lab57lab57c23.lab04c2 as lab57lab57c23lab04c2, lab57lab57c23.lab04c3 as lab57lab57c23lab04c3, lab57lab57c23.lab04c4 as lab57lab57c23lab04c4,
    lab64.lab64c1, lab64c2, lab64c3,
    lab40c1.lab40c1 as lab40c1lab40c1, lab40c1.lab40c2 as lab40c1lab40c2, lab40c1.lab40c3 as lab40c1lab40c3,
    lab05.lab05c1, lab05c2, lab05c4, lab05c10,
    lab57.lab50c1_1, lab50p.lab50c2 as lab50c2p,
    lab57.lab50c1_3, lab50n.lab50c2 as lab50c2n,
    lab57lab57c38.lab04c1 as lab57lab57c38lab04c1, lab57lab57c38.lab04c2 as lab57lab57c38lab04c2, lab57lab57c38.lab04c3 as lab57lab57c38lab04c3, lab57lab57c38.lab04c4 as lab57lab57c38lab04c4,
    lab57lab57c40.lab04c1 as lab57lab57c40lab04c1, lab57lab57c40.lab04c2 as lab57lab57c40lab04c2, lab57lab57c40.lab04c3 as lab57lab57c40lab04c3, lab57lab57c40.lab04c4 as lab57lab57c40lab04c4,
    lab24c1.lab24c1 as lab57lab24c1lab24c1, lab24c1.lab24c2 as lab57lab24c1lab24c2, lab24c1.lab24c9 as lab57lab24c1lab24c9,
    lab24c1_1.lab24c1 as lab57lab24c1_1lab24c1, lab24c1_1.lab24c2 as lab57lab24c1_1lab24c2, lab24c1_1.lab24c9 as lab57lab24c1_1lab24c9,
    lab57lab57c45.lab04c1 as lab57lab57c45lab04c1, lab57lab57c45.lab04c2 as lab57lab57c45lab04c2, lab57lab57c45.lab04c3 as lab57lab57c45lab04c3, lab57lab57c45.lab04c4 as lab57lab57c45lab04c4,
    lab57lab57c47.lab04c1 as lab57lab57c47lab04c1, lab57lab57c47.lab04c2 as lab57lab57c47lab04c2, lab57lab57c47.lab04c3 as lab57lab57c47lab04c3, lab57lab57c47.lab04c4 as lab57lab57c47lab04c4,
    lab40c1a.lab40c1 as lab40c1alab40c1, lab40c1a.lab40c2 as lab40c1alab40c2, lab40c1a.lab40c3 as lab40c1alab40c3,
    lab57lab57c58.lab04c1 as lab57lab57c58lab04c1, lab57lab57c58.lab04c2 as lab57lab57c58lab04c2, lab57lab57c58.lab04c3 as lab57lab57c58lab04c3, lab57lab57c58.lab04c4 as lab57lab57c58lab04c4,
    lab57lab57c60.lab04c1 as lab57lab57c60lab04c1, lab57lab57c60.lab04c2 as lab57lab57c60lab04c2, lab57lab57c60.lab04c3 as lab57lab57c60lab04c3, lab57lab57c60.lab04c4 as lab57lab57c60lab04c4,
    lab57lab57c66.lab04c1 as lab57lab57c66lab04c1, lab57lab57c66.lab04c2 as lab57lab57c66lab04c2, lab57lab57c66.lab04c3 as lab57lab57c66lab04c3, lab57lab57c66.lab04c4 as lab57lab57c66lab04c4,
    lab95c1   
    `;
    
    let from = ` `;

    from += ` FROM ${lab57} as lab57
    INNER JOIN lab39 ON lab39.lab39c1 = lab57.lab39c1
    LEFT JOIN lab43 ON lab43.lab43c1 = lab39.lab43c1
    LEFT JOIN lab04 lab57lab57c3 ON lab57lab57c3.lab04c1 = lab57.lab57c3
    LEFT JOIN lab04 lab57lab57c5 ON lab57lab57c5.lab04c1 = lab57.lab57c5
    LEFT JOIN lab04 lab57lab57c12 ON lab57lab57c12.lab04c1 = lab57.lab57c12
    LEFT JOIN lab39 lab57lab57c14 ON lab57lab57c14.lab39c1 = lab57.lab57c14
    LEFT JOIN lab39 lab57lab57c15 ON lab57lab57c15.lab39c1 = lab57.lab57c15
    LEFT JOIN lab04 lab57lab57c19 ON lab57lab57c19.lab04c1 = lab57.lab57c19
    LEFT JOIN lab04 lab57lab57c21 ON lab57lab57c21.lab04c1 = lab57.lab57c21
    LEFT JOIN lab04 lab57lab57c23 ON lab57lab57c23.lab04c1 = lab57.lab57c23
    LEFT JOIN lab64 ON lab64.lab64c1 = lab57.lab64c1
    LEFT JOIN lab40 lab40c1 ON lab40c1.lab40c1 = lab57.lab40c1
    LEFT JOIN lab05 ON lab05.lab05c1 = lab57.lab05c1
    LEFT JOIN lab50 lab50p ON lab50p.lab50c1 = lab57.lab50c1_1
    LEFT JOIN lab50 lab50n ON lab50n.lab50c1 = lab57.lab50c1_3
    LEFT JOIN lab04 lab57lab57c38 ON lab57lab57c38.lab04c1 = lab57.lab57c38
    LEFT JOIN lab04 lab57lab57c40 ON lab57lab57c40.lab04c1 = lab57.lab57c40
    LEFT JOIN lab24 lab24c1 ON lab24c1.lab24c1 = lab57.lab24c1
    LEFT JOIN lab24 lab24c1_1 ON lab24c1.lab24c1 = lab57.lab24c1_1
    LEFT JOIN lab04 lab57lab57c45 ON lab57lab57c45.lab04c1 = lab57.lab57c45
    LEFT JOIN lab04 lab57lab57c47 ON lab57lab57c47.lab04c1 = lab57.lab57c47
    LEFT JOIN lab40 lab40c1a ON lab40c1a.lab40c1 = lab57.lab40c1a
    LEFT JOIN lab04 lab57lab57c58 ON lab57lab57c58.lab04c1 = lab57.lab57c58
    LEFT JOIN lab04 lab57lab57c60 ON lab57lab57c60.lab04c1 = lab57.lab57c60
    LEFT JOIN lab04 lab57lab57c66 ON lab57lab57c66.lab04c1 = lab57.lab57c66
    LEFT JOIN ${lab95} as lab95 ON lab95.lab22c1 = lab57.lab22c1 AND lab95.lab39c1 = lab57.lab39c1
    `;

    from += " WHERE lab57c8 >= 4 AND (lab57c74 = 0 OR lab57c74 IS NULL) ";

    const [orders] = await db.query(query + from);

    console.log(`Respondio consulta de resultados a las: `, toISOStringLocal(new Date()));

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