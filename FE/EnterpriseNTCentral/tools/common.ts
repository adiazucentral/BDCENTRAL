import axios from "axios";

//Funcion para desencriptar datos
export const decryptData = async (json : any) => {
    let data : any[] = [];
    const urlServices = process.env.URL_NT || "http://192.168.1.6:8080/Enterprise_NT_PRU/api";
    await axios.patch(urlServices + '/encode/decrypt', json)
        .then(function (response) {
            data = response.data;
            console.log('Respondio encriptación: ', toISOStringLocal(new Date()));
        }).catch(function (error) {
            console.log("Problemas con el servicio de desencriptar datos " + error);
        });
    return data;
}

//Funcion para encriptar datos
export const encryptData = async (json : any) => {
    let data : any[] = [];
    const urlServices = process.env.URL_NT || "http://192.168.1.6:8080/Enterprise_NT_PRU/api";
    await axios.patch(urlServices + '/encode/encrypt', json)
        .then(function (response) {
            data = response.data;
            console.log('Respondio encriptación: ', toISOStringLocal(new Date()));
        }).catch(function (error) {
            console.log("Problemas con el servicio de desencriptar datos " + error);
        });
    return data;
}

export function toISOStringLocal(d:any) {
    function z(n:any){return (n<10?'0':'') + n}
    return d.getFullYear() + '-' + z(d.getMonth()+1) + '-' +
           z(d.getDate()) + ' ' + z(d.getHours()) + ':' +
           z(d.getMinutes()) + ':' + z(d.getSeconds())
            
}