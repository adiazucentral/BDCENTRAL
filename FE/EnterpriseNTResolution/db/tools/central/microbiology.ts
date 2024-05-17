import { db } from "../../conection";

export const getMicrobialDetection = async( results:Result[], year: number ) => {

    for ( const result of results ) {

        let microorganismsResponse:any = await getByTest(result.lab22c1, result.lab39.lab39c1, year);

        if(microorganismsResponse.length > 0) {
        } else {
            microorganismsResponse = await getMicroorganisms(result.lab22c1, result.lab39.lab39c1, year);
        }

        if(microorganismsResponse.length > 0) {
    
            let microorganisms: Microorganism[] = [];

            for ( const response of microorganismsResponse ) {
                  
                let microorganism: Microorganism = {
                    lab76c1     : response.lab76c1,
                    lab76c2     : response.lab76c2,
                    lab204c1    : response.lab204c1,
                    lab204c2    : response.lab204c2,
                    lab204c4    : response.lab204c4,
                    lab204c6    : response.lab204c6,
                    lab204      : []
                };

                const antibiotics:any = await listResultMicrobiologySensitivity(microorganism.lab204c1, year);
                if( antibiotics.length > 0 ) {

                    for ( const antibiotic of antibiotics ) {

                        microorganism.lab204.push({
                            lab77c1     : antibiotic.lab77c1,
                            lab79c1     : antibiotic.lab79c1,
                            lab79c2     : antibiotic.lab79c2,
                            lab78c2     : antibiotic.lab78c2,
                            lab45c1     : antibiotic.lab45c1,
                            lab205c1    : antibiotic.lab205c1,
                            lab205c2    : antibiotic.lab205c2,
                            lab205c3    : antibiotic.lab205c3,
                            lab205c4    : antibiotic.lab205c4,
                            lab205c5    : antibiotic.lab205c5,
                            lab205c6    : antibiotic.lab205c6,
                            lab205c7    : antibiotic.lab205c7,
                            lab205c8    : antibiotic.lab205c8,
                            lab205c9    : antibiotic.lab205c9,
                            lab205c10   : antibiotic.lab205c10,
                            lab205c11   : antibiotic.lab205c11
                        }); 
                                                   
                    }
                }
    
                microorganisms.push(microorganism);
            }

            result.lab206 = microorganisms;
        }

    }

}

const getByTest = async( idOrder: bigint, idTest: number, year: number ) => {

    let currentYear = new Date().getFullYear();

    let lab204 = "lab204";

    if(year !== currentYear) {
        lab204 = "lab204_" + year;
    }

    let query = " ";

    query += ` SELECT lab76.lab76c1, lab76c2, lab204c1, lab204c2, lab204c4, lab204c6 `;
    
    let from = ` `;

    from += ` FROM lab206 
    LEFT JOIN lab76 ON lab206.lab76c1 = lab76.lab76c1 
    LEFT JOIN ${lab204} as lab204 ON lab204.lab22c1 = ${idOrder} AND lab204.lab39c1 = ${idTest} AND lab206.lab76c1 = lab204.lab76c1 
    `;

    from += ` WHERE lab206.lab39c1 = ${idTest} OR lab206.lab39c1 IS NULL AND lab204c1 IS NOT NULL `;

    const [microorganisms] = await db.query(query + from);

    return microorganisms;
}



const getMicroorganisms = async( idOrder: bigint, idTest: number, year: number ) => {

    let currentYear = new Date().getFullYear();

    let lab204 = "lab204";

    if(year !== currentYear) {
        lab204 = "lab204_" + year;
    }

    let query = " ";

    query += ` SELECT lab76.lab76c1, lab76c2, lab204c1, lab204c2, lab204c4, lab204c6 `;
    
    let from = ` `;

    from += ` FROM lab206 
    LEFT JOIN lab76 ON lab206.lab76c1 = lab76.lab76c1
    LEFT JOIN ${lab204} as lab204 ON lab204.lab22c1 = ${idOrder} AND lab204.lab39c1 = ${idTest} AND lab206.lab76c1 = lab204.lab76c1 
    `;

    from += " WHERE lab206.lab39c1 IS NULL AND lab204c1 IS NOT NULL ";

    const [microorganisms] = await db.query(query + from);

    return microorganisms;
}


const listResultMicrobiologySensitivity = async( idMicrobialDetection: number, year: number ) => {

    let currentYear = new Date().getFullYear();

    let lab204 = "lab204";
    let lab205 = "lab205";

    if(year !== currentYear) {
        lab204 = "lab204_" + year;
        lab205 = "lab205_" + year;
    }

    let query = " ";

    query += ` SELECT lab77.lab77c1, lab78.lab78c2, lab78.lab45c1, 
    lab79.lab79c1, lab79.lab79c2, 
    lab205.lab205c1, lab205.lab205c2, lab205.lab205c3, lab205.lab205c4, lab205.lab205c5, lab205.lab205c6, lab205.lab205c7, lab205.lab205c8, lab205.lab205c9, lab205.lab205c10, lab205.lab205c11
    `;
    
    let from = ` `;

    from += ` FROM ${lab204}  as lab204 
        INNER JOIN lab77 ON lab77.lab77c1 = lab204.lab77c1 
        INNER JOIN lab78 ON lab78.lab77c1 = lab77.lab77c1 
        LEFT JOIN lab79 ON lab79.lab79c1 = lab78.lab79c1 
        LEFT JOIN  ${lab205} as lab205 ON lab205.lab204c1 = lab204.lab204c1 AND lab205.lab79c1 = lab79.lab79c1 
    `;

    from += ` WHERE lab204.lab204c1 = ${idMicrobialDetection} AND ( lab205c9 IS NOT NULL OR lab205c10 IS NOT NULL OR lab205c11 IS NOT NULL)`;


    const [response] = await db.query(query + from);

    return response;
}