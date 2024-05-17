import axios from "axios";
import { changeFlagOrder } from "../../../db/tools/central/order";
const urlCentral = process.env.URL_CENTRAL || "http://localhost:5200/api";

export const sendOrdersCentral = async ( orders: Order[] ) => {
    await axios.patch(urlCentral + '/order', orders).then( (response) => {
        changeFlagOrder(response.data);
    }).catch( (error) => {
        console.log("Problemas con el servicio de ordenes a bd central" + error);
    });
}
