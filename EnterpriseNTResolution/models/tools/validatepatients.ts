import axios, { AxiosRequestConfig } from "axios";
export const validatePatient = async (info: ValidatePatient) => {
    try {
        let data = {};
        const body = {
            personalId: info.personalId,
            fullNameApproximation: info.fullNameApproximation,
        }

        const axiosConfig: AxiosRequestConfig = {
            headers: {
                "content-type": "application/json",
                Authorization: 'Basic ' + Buffer.from(info.username + ':' + info.password).toString('base64'),
            },
        };

        await axios.post(info.url, JSON.stringify(body), axiosConfig)
            .then(function (response) {
                if(typeof response.data === 'object') {
                    data = response.data;
                } else if(typeof response.data === 'string' && response.data.search('<html>') !== -1) {
                    data = "";
                } else {
                    data = response.data;
                }
            }).catch(function (error) {
                data = "";
            });
        return data;
    } catch (error) {
        return "Error en el sistema";
    }
}