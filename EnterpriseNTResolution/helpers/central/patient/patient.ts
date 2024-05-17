import axios from "axios";
import { changeFlagPatient } from "../../../db/tools/central/patient";
const urlCentral = process.env.URL_CENTRAL || "http://localhost:5200/api";

export const sendPatientsCentral = async ( patients: Patient[] ) => {
    await axios.patch(urlCentral + '/patient', patients).then( (response) => {
        changeFlagPatient(response.data);
    }).catch( (error) => {
        console.log("Problemas con el servicio de pacientes a bd central" + error);
    });
}
