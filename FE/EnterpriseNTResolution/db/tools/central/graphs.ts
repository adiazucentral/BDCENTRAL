import { dbDocs } from "../../conection";

export const getAnalyzerGraphs = async (results: Result[], year: number) => {

    for (const result of results) {

        let graphsResponse: any = await getByTest(result.lab22c1, result.lab39.lab39c1);

        if (graphsResponse.length > 0) {

            let graphs: Graphs[] = [];

            for (const response of graphsResponse) {
                await processGraphs(response).then( resolve => {
                    graphs.push(resolve);
                });
            }

            result.doc03 = graphs;
        }

    }
}

const getByTest = async (idOrder: bigint, idTest: number) => {

    let query = " ";

    query += ` SELECT lab22c1, lab39c1, doc03c1, doc03c2, doc03c3, doc03c4, doc03c5, doc03c6, doc03c7, doc03c8, doc03c9, doc03c10 `;

    let from = ` `;

    from += ` FROM doc03 `;

    from += ` WHERE lab22c1 = ${idOrder} AND lab39c1 = ${idTest} `;

    const [templates] = await dbDocs.query(query + from);

    return templates;
}

const processGraphs = async (response: Graphs) => {
    return new Promise<Graphs>((resolve, reject) => {
        let graph: Graphs = {
            lab22c1     : response.lab22c1, 
            lab39c1     : response.lab39c1, 
            doc03c1     : response.doc03c1, 
            doc03c2     : response.doc03c2, 
            routeBD1    : "",
            doc03c3     : response.doc03c3, 
            doc03c4     : response.doc03c4,
            routeBD2    : "", 
            doc03c5     : response.doc03c5, 
            doc03c6     : response.doc03c6, 
            routeBD3    : "",
            doc03c7     : response.doc03c7, 
            doc03c8     : response.doc03c8, 
            routeBD4    : "",
            doc03c9     : response.doc03c9, 
            doc03c10    : response.doc03c10,
            routeBD5    : ""
        };
        resolve(graph);
    });
}