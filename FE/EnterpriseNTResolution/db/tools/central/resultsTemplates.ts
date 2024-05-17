import { db } from "../../conection";

export const getGeneralTemplate = async( results:Result[], year: number ) => {

    for ( const result of results ) {

        let templatesResponse:any = await getByTest(result.lab22c1, result.lab39.lab39c1, year);

        if(templatesResponse.length > 0) {
    
            let optionsTemplate: OptionTemplate[] = [];

            for ( const response of templatesResponse ) {

                let optionTemplate: OptionTemplate = {
                    lab51c1 : 0,
                    lab51c2 : '',
                    lab51c3 : '',
                    lab51c4 : 0,
                    lab39c1 : 0,
                    lab58c1 : response.lab58c1,
                    lab58c3 : response.lab58c3,
                    lab58c4 : response.lab58c4,
                    lab146  : []
                };

                if(response.lab58c4 === null || response.lab58c4 === undefined || response.lab58c4 === '' ) {
                    optionTemplate.lab51c1  = response.lab51c1;
                    optionTemplate.lab51c2  = response.lab51c2;
                    optionTemplate.lab51c3  = response.lab51c3;
                    optionTemplate.lab51c4  = response.lab51c4;
                    optionTemplate.lab39c1  = response.lab39c1;

                    const listOptions:any = await getOptions(optionTemplate.lab51c1);
                    if( listOptions.length > 0 ) {
                        for ( const option of listOptions ) {
                            optionTemplate.lab146.push({
                                lab146c1: option.lab146c1,
                                lab146c2: option.lab146c2,
                                lab146c3: option.lab146c3,
                                lab146c4: option.lab146c4
                            });                       
                        }
                    }

                } else {
                    const beforeTemplate    = JSON.parse(response.lab58c4);
                    optionTemplate.lab51c1  = beforeTemplate.id;
                    optionTemplate.lab51c2  = beforeTemplate.option;
                    optionTemplate.lab51c3  = beforeTemplate.comment;
                    optionTemplate.lab51c4  = beforeTemplate.sort;
                    optionTemplate.lab39c1  = beforeTemplate.idTest;
                    optionTemplate.lab146   = beforeTemplate.results;
                }

                optionsTemplate.push(optionTemplate);
            }

            result.lab58 = optionsTemplate;
        }

    }

}

const getByTest = async( idOrder: bigint, idTest: number, year: number ) => {

    let currentYear = new Date().getFullYear();

    let lab58 = "lab58";

    if(year !== currentYear) {
        lab58 = "lab58_" + year;
    }

    let query = " ";

    query += ` SELECT lab51.lab51c1, lab51.lab51c2, lab51.lab51c3, lab51.lab51c4, lab51.lab39c1, 
    lab58.lab58c1, lab58.lab58c2, lab58.lab58c3, lab58.lab58c4, lab58.lab22c1 `;
    
    let from = ` `;

    from += ` FROM lab51 
        FULL JOIN ${lab58} as lab58 ON lab58.lab22c1 = ${idOrder} AND lab58.lab39c1 = lab51.lab39c1 
    `;

    from += ` WHERE lab51.lab39c1 = ${idTest};`;

    const [templates] = await db.query(query + from);

    return templates;
}



const getOptions = async( idTemplate: number ) => {

    let query = " ";

    query += ` SELECT lab146.lab146c1, lab146.lab146c2, lab146.lab146c3, lab146.lab146c4 `;
    
    let from = ` `;

    from += ` FROM lab146 
    `;

    from += ` WHERE lab146.lab51c1 = ${idTemplate}  `;

    const [options] = await db.query(query + from);

    return options;
}
