import axios from "axios";
import moment from "moment";

export const getValueKey = async (value: string, token: string) => {
    let data: any;
    const urlServices = process.env.URL_NT || "http://localhost:8081/Enterprise_NT/api";
    await axios.get(urlServices + `/configuration/${value}`, { headers: {"Authorization" : `${token}`} } )
        .then(function (response) {
            data = response.data.value;
        }).catch(function (error) {
            console.log("Problemas con el servicio de consulta de llaves " + error);
        });
    return data;
}

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


export const htmlEntities = (str: any) => {
    let valorhtml = String(str).replace(/&ntilde;/g, 'ñ')
        .replace(/\n/g, "  ")
        .replace(/&nbsp;/g, ' ')
        .replace(/['"]+/g, '')
        .replace(/<[^>]*>?/g, '')
        .replace(/&Ntilde;/g, 'Ñ')
        .replace(/&amp;/g, '&')
        .replace(/&Ntilde;/g, 'Ñ')
        .replace(/&ntilde;/g, 'ñ')
        .replace(/&Ntilde;/g, 'Ñ')
        .replace(/&Agrave;/g, 'À')
        .replace(/&Aacute;/g, 'Á')
        .replace(/&Acirc;/g, 'Â')
        .replace(/&Atilde;/g, 'Ã')
        .replace(/&Auml;/g, 'Ä')
        .replace(/&Aring;/g, 'Å')
        .replace(/&AElig;/g, 'Æ')
        .replace(/&Ccedil;/g, 'Ç')
        .replace(/&Egrave;/g, 'È')
        .replace(/&Eacute;/g, 'É')
        .replace(/&Ecirc;/g, 'Ê')
        .replace(/&Euml;/g, 'Ë')
        .replace(/&Igrave;/g, 'Ì')
        .replace(/&Iacute;/g, 'Í')
        .replace(/&Icirc;/g, 'Î')
        .replace(/&Iuml;/g, 'Ï')
        .replace(/&ETH;/g, 'Ð')
        .replace(/&Ntilde;/g, 'Ñ')
        .replace(/&Ograve;/g, 'Ò')
        .replace(/&Oacute;/g, 'Ó')
        .replace(/&Ocirc;/g, 'Ô')
        .replace(/&Otilde;/g, 'Õ')
        .replace(/&Ouml;/g, 'Ö')
        .replace(/&Oslash;/g, 'Ø')
        .replace(/&Ugrave;/g, 'Ù')
        .replace(/&Uacute;/g, 'Ú')
        .replace(/&Ucirc;/g, 'Û')
        .replace(/&Uuml;/g, 'Ü')
        .replace(/&Yacute;/g, 'Ý')
        .replace(/&THORN;/g, 'Þ')
        .replace(/&szlig;/g, 'ß')
        .replace(/&agrave;/g, 'à')
        .replace(/&aacute;/g, 'á')
        .replace(/&acirc;/g, 'â')
        .replace(/&atilde;/g, 'ã')
        .replace(/&auml;/g, 'ä')
        .replace(/&aring;/g, 'å')
        .replace(/&aelig;/g, 'æ')
        .replace(/&ccedil;/g, 'ç')
        .replace(/&egrave;/g, 'è')
        .replace(/&eacute;/g, 'é')
        .replace(/&ecirc;/g, 'ê')
        .replace(/&euml;/g, 'ë')
        .replace(/&igrave;/g, 'ì')
        .replace(/&iacute;/g, 'í')
        .replace(/&icirc;/g, 'î')
        .replace(/&iuml;/g, 'ï')
        .replace(/&eth;/g, 'ð')
        .replace(/&ntilde;/g, 'ñ')
        .replace(/&ograve;/g, 'ò')
        .replace(/&oacute;/g, 'ó')
        .replace(/&ocirc;/g, 'ô')
        .replace(/&otilde;/g, 'õ')
        .replace(/&ouml;/g, 'ö')
        .replace(/&oslash;/g, 'ø')
        .replace(/&ugrave;/g, 'ù')
        .replace(/&uacute;/g, 'ú')
        .replace(/&ucirc;/g, 'û')
        .replace(/&uuml;/g, 'ü')
        .replace(/&yacute;/g, 'ý')
        .replace(/&thorn;/g, 'þ')
        .replace(/&yuml;/g, 'ÿ')
        .replace(/<[^>]+>/gm, '')
        .replace(/<br\s*\/?>/g, '');

    return replaceAll(valorhtml, ";", '');
}

export const replaceAll = (str: any, find: any, replace: any) => {
    let escapedFind = find.replace(/([.*+?^=!:${}()|\[\]\/\\])/g, "\\$1");
    return str.replace(new RegExp(escapedFind, 'g'), replace);
}

export const toISOStringLocal = (d:any) =>{
    function z(n:any){return (n<10?'0':'') + n}
    return d.getFullYear() + '-' + z(d.getMonth()+1) + '-' +
           z(d.getDate()) + ' ' + z(d.getHours()) + ':' +
           z(d.getMinutes()) + ':' + z(d.getSeconds())
            
}

export const getAgeAsString = (date: any, format: any) => {
    var age = getAge(date, format);
    if (age !== '') {
        var ageFields = age.split(".");
        if (Number(ageFields[0]) !== 0) {
            if (Number(ageFields[0]) === 1) {
                //Año
                return ageFields[0] + " " + "Año";
            } else {
                //Años
                return ageFields[0] + " " + "Años";
            }
        } else if (Number(ageFields[1]) !== 0) {
            if (Number(ageFields[1]) === 1) {
                //Mes
                return ageFields[1] + " " + "Mes";
            } else {
                //Meses
                return ageFields[1] + " " + "Meses";
            }
        } else {
            if (Number(ageFields[2]) === 1) {
                //Dia
                return ageFields[2] + " " + "Día";
            } else {
                //Dias
                return ageFields[2] + " " + "Días";
            }
        }
    } else {
        return "Edad no valida";
    }
}

export const getAge = (date: any, format: any) => {
    if (!moment(date, format, true).isValid()) {
        return "";
    }
    var birthday = moment(date, format).toDate();
    var current = new Date();
    var diaActual = current.getDate();
    var mesActual = current.getMonth() + 1;
    var anioActual = current.getFullYear();
    var diaInicio = birthday.getDate();
    var mesInicio = birthday.getMonth() + 1;
    var anioInicio = birthday.getFullYear();
    var b = 0;
    var mes = mesInicio;
    if (mes === 2) {
        if ((anioActual % 4 === 0 && anioActual % 100 !== 0) || anioActual % 400 === 0) {
            b = 29;
        } else {
            b = 28;
        }
    } else if (mes <= 7) {
        if (mes === 0) {
            b = 31;
        } else if (mes % 2 === 0) {
            b = 30;
        } else {
            b = 31;
        }
    } else if (mes > 7) {
        if (mes % 2 === 0) {
            b = 31;
        } else {
            b = 30;
        }
    }

    var anios = -1;
    var meses = -1;
    var dies = -1;
    if ((anioInicio > anioActual) || (anioInicio === anioActual && mesInicio > mesActual) ||
        (anioInicio === anioActual && mesInicio === mesActual && diaInicio > diaActual)) {
        return "";
    } else if (mesInicio <= mesActual) {
        anios = anioActual - anioInicio;
        if (diaInicio <= diaActual) {
            meses = mesActual - mesInicio;
            dies = diaActual - diaInicio;
        } else {
            if (mesActual === mesInicio) {
                anios = anios - 1;
            }
            meses = (mesActual - mesInicio - 1 + 12) % 12;
            dies = b - (diaInicio - diaActual);
        }
    } else {
        anios = anioActual - anioInicio - 1;
        if (diaInicio > diaActual) {
            meses = mesActual - mesInicio - 1 + 12;
            dies = b - (diaInicio - diaActual);
        } else {
            meses = mesActual - mesInicio + 12;
            dies = diaActual - diaInicio;
        }
    }
    return (anios < 10 ? "0" + anios : anios) + "." + (meses < 10 ? "0" + meses : meses) + "." + (dies < 10 ? "0" + dies : dies);
}

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