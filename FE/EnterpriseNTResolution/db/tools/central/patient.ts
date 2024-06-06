import { sendPatientsCentral } from "../../../helpers/central/patient/patient";
import { createDemographicsQuery, toISOStringLocal } from "../../../tools/common";
import { db } from "../../conection";

export const sendPatients = async( demographics:any ) => {
    let response = await getPatients(demographics);
    if(response.length > 0) {
        let patients: Patient[] = [];
        response.forEach((resp: any) => {
            let listDemoPatient: Demographic[] = [];
            if(demographics) {
                demographics.forEach( (demo:any) => {
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
                    listDemoPatient.push(dataDemo);
                });
            }

            patients.push({
                lab21c1         : resp.lab21c1,
                lab21c2         : resp.lab21c2,
                lab21c3         : resp.lab21c3,
                lab21c4         : resp.lab21c4,
                lab21c5         : resp.lab21c5,
                lab21c6         : resp.lab21c6,
                lab21c7         : resp.lab21c7,
                lab21c8         : resp.lab21c8,
                lab21c9         : resp.lab21c9,
                lab21c10        : resp.lab21c10,
                lab21c11        : resp.lab21c11,
                lab21c12        : resp.lab21c12,
                lab21c13        : resp.lab21c13,
                lab21c14        : resp.lab21c14,
                lab21c15        : resp.lab21c15,
                lab21c16        : resp.lab21c16,
                lab21c17        : resp.lab21c17,
                lab21c18        : resp.lab21c18,
                lab21c19        : resp.lab21c19,
                lab21c20        : resp.lab21c20,
                lab21c21        : resp.lab21c21,
                lab21c22        : resp.lab21c22,
                lab21c23        : resp.lab21c23,
                lab21c24        : resp.lab21c24,
                lab80:          {
                    lab80c1     : resp.lab80c1,
                    lab80c2     : resp.lab80c2,
                    lab80c3     : resp.lab80c3,
                    lab80c4     : resp.lab80c4,
                    lab80c5     : resp.lab80c5
                },
                lab04c1:        {
                    lab04c1     : resp.lab21lab04c1lab04c1,
                    lab04c2     : resp.lab21lab04c1lab04c2,
                    lab04c3     : resp.lab21lab04c1lab04c3,
                    lab04c4     : resp.lab21lab04c1lab04c4,
                    lab04c10    : "",
                    lab04c13    : ""
                },
                lab08:          {
                    lab08c1     : resp.lab08c1,
                    lab08c2     : resp.lab08c2,
                    lab08c5     : resp.lab08c5
                },
                lab54:          {
                    lab54c1     : resp.lab54c1,
                    lab54c2     : resp.lab54c2,
                    lab54c3     : resp.lab54c3
                },
                lab04c1_2:      {
                    lab04c1     : resp.lab21lab04c1_2lab04c1,
                    lab04c2     : resp.lab21lab04c1_2lab04c2,
                    lab04c3     : resp.lab21lab04c1_2lab04c3,
                    lab04c4     : resp.lab21lab04c1_2lab04c4,
                    lab04c10    : "",
                    lab04c13    : ""
                },
                lab62           : listDemoPatient
            });
        });

        sendPatientsCentral(patients);
    } else {
        console.log('No existen pacientes para enviar');
    }
}

const getPatients = async( demographics:any ) => {

    console.log(`Inicia consulta de pacientes para envió a bd central: `, toISOStringLocal(new Date()));

    let query = " ";

    query += ` SELECT TOP 100 lab21c1, lab21c2, lab21c3, lab21c4, lab21c5, lab21c6, lab21c7, lab21c8, lab21c9, lab21c10, lab21c11, lab21c12, lab21c13, lab21c14, 
        lab21c15, lab21c16, lab21c17, lab21c18, lab21c19, lab21c20, lab21c21, lab21c22, lab21c23, lab21c24,
        lab80.lab80c1, lab80c2, lab80c3, lab80c4, lab80c5,
        lab21lab04c1.lab04c1 as lab21lab04c1lab04c1, lab21lab04c1.lab04c2 as lab21lab04c1lab04c2, lab21lab04c1.lab04c3 as lab21lab04c1lab04c3, lab21lab04c1.lab04c4 as lab21lab04c1lab04c4,
        lab08.lab08c1, lab08c2, lab08c5,
        lab54.lab54c1, lab54c2, lab54c3,
        lab21lab04c1_2.lab04c1 as lab21lab04c1_2lab04c1, lab21lab04c1_2.lab04c2 as lab21lab04c1_2lab04c2, lab21lab04c1_2.lab04c3 as lab21lab04c1_2lab04c3, lab21lab04c1_2.lab04c4 as lab21lab04c1_2lab04c4
    `;
    
    let from = ` `;

    from += ` FROM lab21
        LEFT JOIN lab80 ON lab80.lab80c1 = lab21.lab80c1
        LEFT JOIN lab04 lab21lab04c1 ON lab21lab04c1.lab04c1 = lab21.lab04c1
        LEFT JOIN lab08 ON lab08.lab08c1 = lab21.lab08c1
        LEFT JOIN lab54 ON lab54.lab54c1 = lab21.lab54c1
        LEFT JOIN lab04 lab21lab04c1_2 ON lab21lab04c1_2.lab04c1 = lab21.lab04c1_2
    `;

    if(demographics !== null && demographics !== undefined && demographics.length > 0) {
        let querysDemos:any = await createDemographicsQuery(demographics.filter((demo:any) => demo.id > 0));
        query += querysDemos.select;
        from += querysDemos.from;
    }

    from += " WHERE lab21c25 = 0 OR lab21c25 IS NULL ";

    const [patients] = await db.query(query + from);

    console.log(`Respondio consulta de pacientes a las: `, toISOStringLocal(new Date()));

    return patients;
}

export const changeFlagPatient = async( docsInserted: any[] ) => {
    const query = `UPDATE lab21 SET lab21c25 = 1, lab21c26 = :date WHERE lab21c1 = :idPatientDB` 
    docsInserted.forEach( async(doc) => {
        let date = doc.created;
        if(date < doc.updated) {
            date = doc.updated;
        }
        const [numFiles] = await db.query(query,
            {
              replacements: { date: date, idPatientDB: doc.lab21c1 }
            }
          );
    });
}
