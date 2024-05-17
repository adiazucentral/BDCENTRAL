import { Component } from '@angular/core';
import { NgbCalendar, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { environment } from 'src/environments/environment';
import Swal from 'sweetalert2'
import { BillingService } from './services/billing.service';
import { ExportExcelService } from './services/export-excel.service';
import * as moment from 'moment';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'exportDataView';

  active = 1;

  initDate: NgbDateStruct;
  endDate: NgbDateStruct;

  maxDate: NgbDateStruct;

  initOrder: String;
  endOrder: String;

  date: { year: number, month: number, day: number };

  customer: string = environment.customer;

  dataForExcel = [];
  headersForExcel = [];

  profiles = true;

  constructor(
    private calendar: NgbCalendar,
    public ete: ExportExcelService,
    public billingService: BillingService
  ) {
    const today = new Date();
    this.initDate = { 'year': today.getFullYear(), 'month': today.getMonth() + 1, 'day': today.getDate() }
    this.endDate = { 'year': today.getFullYear(), 'month': today.getMonth() + 1, 'day': today.getDate() }

    this.maxDate = {'year': today.getFullYear(), 'month': today.getMonth() + 1, 'day': today.getDate()};
  }

  generateFileDates() {

    const now = new Date();

    if (this.initDate && this.endDate) {

      let startDate = this.initDate.year + '-' + (this.initDate.month < 10 ? '0' + this.initDate.month : this.initDate.month) + '-' +
        (this.initDate.day < 10 ? '0' + this.initDate.day : this.initDate.day);
      let endDate = this.endDate.year + '-' + (this.endDate.month < 10 ? '0' + this.endDate.month : this.endDate.month) + '-' +
        (this.endDate.day < 10 ? '0' + this.endDate.day : this.endDate.day);

      let dateLimit = now.getFullYear() + '-' + ((now.getMonth() + 1) < 10 ? '0' + (now.getMonth() + 1) : (now.getMonth() + 1)) + '-' +
        (now.getDate() + 1 < 10 ? '0' + now.getDate() + 1 : now.getDate() + 1);

      if (startDate !== null && endDate !== null && startDate <= endDate) {
        if (startDate >= dateLimit) {
          this.generateMessage('Error!', 'error', 'La fecha inicial no puede ser mayor a la fecha actual');
        } else if (endDate >= dateLimit) {
          this.generateMessage('Error!', 'error', 'La fecha final no puede ser mayor a la fecha actual');
        } else {
          this.exportToExcel(startDate.replace(/-/g, ''), endDate.replace(/-/g, ''), false, this.profiles);
        }
      } else {
        this.generateMessage('Error!', 'error', 'La fecha final no puede ser mayor a la fecha inicial');
      }
    } else {
      this.generateMessage('Error!', 'error', 'Por favor seleccione una fecha inicial y una fecha final');
    }

  }

  generateFilesOrder() {
    if (this.initOrder && this.endOrder) {
      if (this.initOrder > this.endOrder) {
        this.generateMessage('Error!', 'error', 'La orden inicial no puede ser mayor a la orden final');
      } else {
        this.exportToExcel(this.initOrder, this.endOrder, true, this.profiles);
      }
    } else {
      this.generateMessage('Error!', 'error', 'Por favor digite una orden inicial y una orden final');
    }
  }

  exportToExcel(init, end, type, profile) {
    if (this.customer === '1') { //Labserving
      this.labserving(init, end, type, profile);
    }
    if(this.customer === '2') { //Aida Ascencio
      this.aida(init, end, type, profile);
    }
  }

  labserving(init, end, type, profile) {

    Swal.fire({
      icon: 'info',
      text: 'Generando Archivo...',
      allowOutsideClick: () => !Swal.isLoading()
    });

    Swal.showLoading();

    this.billingService.getLabserving(init, end, type, profile).subscribe((resp: any) => {
      if (resp === null) {
        Swal.close();
        this.generateMessage('Advertencia!', 'warning', 'No existen datos para generar el archivo');
      } else {
        if(resp.length > 30000) {
          this.generateMessage('Error!', 'error', 'La cantidad de datos supera el límite máximo (30.000 ordenes)');
        } else {
          this.headersForExcel = [
            'Tipo de documento',
            'Numero de documento',
            'Primer Nombre',
            'Segundo Nombre',
            'Primer Apellido',
            'Segundo Apellido',
            'Fecha de nacimiento',
            'Edad',
            'Genero',
            'Número de orden',
            'Fecha Orden',
            'Demo_Sede',
            'Demo_Cliente',
            'Codigo Examen',
            'Codigo Perfil',
            'Nombre Examen',
            'Resultado',
            'Comentario',
            'Fecha Resultado',
            'Usuario valida'
          ];

          resp.forEach(order => {

            const age = this.getAgeAsString(moment(order.birthday).format('DD/MM/YYYY'), 'DD/MM/YYYY');

            //Sede
            const branch = order.allDemographics.find(demo => demo.idDemographic === 12);
            let valueBranch = '';
            if (branch) {
              valueBranch = branch.codifiedName;
            }

            //Cliente
            const customer = order.allDemographics.find(demo => demo.idDemographic === 20);
            let valueCustomer = '';
            if (customer) {
              valueCustomer = customer.codifiedName;
            }

            order.tests.forEach(test => {

              const dateResult = test.dateResult !== null && test.dateResult !== undefined ? moment(test.dateResult).format('DD/MM/YYYY') : "";

              this.dataForExcel.push([
                order.documentTypeName,
                order.patientId,
                order.name1,
                order.name2,
                order.lastName,
                order.surName,
                moment(order.birthday).format('DD/MM/YYYY'),
                age,
                order.sex,
                order.orderNumber,
                moment(order.createdDateShort).format('DD/MM/YYYY'),
                valueBranch,
                valueCustomer,
                test.code,
                test.profileCode,
                test.name,
                test.result,
                test.comment,
                dateResult,
                test.userValidation
              ]);
            });
          });

          let reportData = {
            title: 'Labserving',
            data: this.dataForExcel,
            headers: this.headersForExcel
          }

          this.ete.exportExcel(reportData).then((resp: any) => {
            Swal.close();
            this.dataForExcel = [];
            this.headersForExcel = [];
          }).catch(err => {
            Swal.close();
          });
        }
      }
    }, error => {
      Swal.close();
      this.generateMessage('Error!', 'error', 'Ha ocurrido un error, comuniquese con el administrador');
    });

  }

  aida(init, end, type, profile) {

    Swal.fire({
      icon: 'info',
      text: 'Generando Archivo...',
      allowOutsideClick: () => !Swal.isLoading()
    });

    Swal.showLoading();

    this.billingService.get(init, end, type).subscribe((resp: any) => {
      if (resp === null) {
        Swal.close();
        this.generateMessage('Advertencia!', 'warning', 'No existen datos para generar el archivo');
      } else {
        if(resp.length > 30000) {
          this.generateMessage('Error!', 'error', 'La cantidad de datos supera el límite máximo (30.000 ordenes)');
        } else {

          this.headersForExcel = [
            'Codigo Empresa', 
            'Nombre Empresa',
            'Precio Empresa',
            'Codigo Examen', 
            'Nombre Examen',
            'Nombre Examen Estadistica',
            'Cups',
            'Codigo Area',
            'Nombre Area',
            'Codigo Nivel',
            'Nombre Nivel',
            'Codigo Lab',
            'Nombre Lab',
            'Total Examenes',
            'Total Hombres',
            'Total Mujeres',
            'Total Indefinido',
            'Total Antibiograma',
            'Total Patologicos',
            'Total Repeticiones',
            'Tipo Orden',
            'Orden',
            'Orden de Servicio',
            'Historia Clinica',
            'Tipo Documento',
            'Apellidos',
            'Nombres',
            'Sexo',
            'Fecha Nacimiento',
            'Edad',
            'Fecha Orden',
            'Fecha Ingreso Examen',
            'Fecha Resultado',
            'Fecha Validacion',
            'Fecha Impresion'
          ];

          resp.forEach(order => {

            const age = this.getAgeAsString(moment(order.birthday).format('DD/MM/YYYY'), 'DD/MM/YYYY');

            //Order de servicio
            const orderService = order.allDemographics.find(demo => demo.idDemographic === 15);
            let valueOrderService = '';
            if (orderService) {
              valueOrderService = orderService.codifiedName;
            }

            order.tests.forEach(test => {

              const entryDate = test.hasOwnProperty('dateOrdered') ? test.dateOrdered !== null && test.dateOrdered !== undefined ? moment(test.dateOrdered).format('DD/MM/YYYY') : "" : "";
              const dateResult = test.hasOwnProperty('dateResult') ?test.dateResult !== null && test.dateResult !== undefined ? moment(test.dateResult).format('DD/MM/YYYY') : "" : "";
              const dateValidation = test.hasOwnProperty('dateValidation') ? test.dateValidation !== null && test.dateValidation !== undefined ? moment(test.dateValidation).format('DD/MM/YYYY') : "" : "";
              const datePrint = test.hasOwnProperty('datePrint') ? test.datePrint !== null && test.datePrint !== undefined ? moment(test.datePrint).format('DD/MM/YYYY') : "" : "";

              const cups = test.hasOwnProperty('codeCups') ? test.codeCups : '';
              const codeLevel = test.level !== null && test.level !== undefined && test.level !== '' ? test.level.split('Nivel')[1] : '';
              
              this.dataForExcel.push([
                order.nit, 
                order.nameAccount, 
                test.priceAccount,
                test.code,
                test.name,
                test.nameStadistic,
                cups,
                test.codeArea,
                test.nameArea,
                codeLevel,
                test.level,
                test.codeLaboratory,
                test.laboratory,
                1,
                order.sexId === 7 ? 1 : 0,
                order.sexId === 8 ? 1 : 0,
                order.sexId === 9 ? 1 : 0,
                test.antibiogram,
                test.pathology,
                0,
                order.orderTypeName,
                order.orderNumber,
                valueOrderService,
                order.patientId,
                order.documentTypeName,
                order.lastName + " " + order.surName,
                order.name1 + " " + order.name2,
                order.sex,
                moment(order.birthday).format('DD/MM/YYYY'),
                age,
                moment(order.createdDateShort).format('DD/MM/YYYY'),
                entryDate,
                dateResult,
                dateValidation,
                datePrint
              ]);
            });
          });

          let reportData = {
            title: 'Aida Ascencio',
            data: this.dataForExcel,
            headers: this.headersForExcel
          }

          this.ete.exportExcel(reportData).then((resp: any) => {
            Swal.close();
            this.dataForExcel = [];
            this.headersForExcel = [];
          }).catch(err => {
            Swal.close();
          });
        }
      }
    }, error => {
      Swal.close();
      this.generateMessage('Error!', 'error', 'Ha ocurrido un error, comuniquese con el administrador');
    });

  }

  generateMessage(title, type, message) {
    Swal.fire({
      title: title,
      text: message,
      icon: type,
      confirmButtonText: 'Ok'
    })
  }

  getAge(date, format) {
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

  getAgeAsString(date, format) {
    var age = this.getAge(date, format);
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

}
