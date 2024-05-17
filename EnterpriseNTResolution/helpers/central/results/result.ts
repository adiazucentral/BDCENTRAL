import axios from "axios";
const urlCentral = process.env.URL_CENTRAL || "http://localhost:5200/api";

export const sendResultsCentral = async ( results: Result[] ) => {
    await axios.patch(urlCentral + '/result', results).then( (response) => {
        // changeFlagOrder(response.data);
    }).catch( (error) => {
        console.log("Problemas con el servicio de resultados a bd central" + error);
    });
}
