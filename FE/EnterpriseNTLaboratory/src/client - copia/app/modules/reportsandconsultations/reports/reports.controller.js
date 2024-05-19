/* jshint ignore:start */
(function () {
  'use strict';
  angular
    .module('app.reports')
    .controller('ReportsController', ReportsController);
  ReportsController.$inject = ['$filter', '$rootScope', 'localStorageService', 'LZString', '$translate', 'logger',
    'reportsDS', 'demographicDS', 'ordertypeDS', '$state', 'moment', 'reportadicional', 'common', 'customerDS', '$q'];

  function ReportsController($filter, $rootScope, localStorageService, LZString, $translate, logger,
    reportsDS, demographicDS, ordertypeDS, $state, moment, reportadicional, common, customerDS, $q) {


    var vm = this;
    var auth = localStorageService.get('Enterprise_NT.authorizationData');
    var language = $filter('translate')('0000');

    vm.isAuthenticate = isAuthenticate;
    vm.init = init;
    vm.title = 'Reports';
    vm.modalError = modalError;
    $rootScope.pageview = 3;
    $rootScope.menu = true;
    $rootScope.NamePage = $filter('translate')('0031').toUpperCase();
    $rootScope.helpReference = '04.reportsandconsultations/reports.htm';
    $rootScope.blockView = true;

    vm.EmailBody = localStorageService.get('EmailBody');
    vm.EmailSubjectPatient = localStorageService.get('EmailSubjectPatient');
    vm.EmailSubjectPhysician = localStorageService.get('EmailSubjectPhysician');
    vm.formatDate = localStorageService.get('FormatoFecha').toUpperCase();
    vm.isPrintAttached = localStorageService.get('ImprimirAdjuntos') === 'True';
    vm.historySend = true; vm.ordersSend = true;
    vm.demographicTitle = localStorageService.get('DemograficoTituloInforme');
    vm.pathReports = localStorageService.get('RutaReportes');
    vm.namePdfConfig = localStorageService.get('GenerarPDFCon');
    vm.finalReport = localStorageService.get('ImprimirInformeFinal')
    vm.isAuxPhysicians = localStorageService.get('MedicosAuxiliares') === 'True';
    vm.totalAuxPhysicians = localStorageService.get('TotalMedicosAuxiliares') === null || localStorageService.get('TotalMedicosAuxiliares') === '' ? 0 : parseInt(localStorageService.get('TotalMedicosAuxiliares'));
    vm.getOrderType = getOrderType;
    vm.getDemographicsALL = getDemographicsALL;
    vm.getlistReportFile = getlistReportFile;
    vm.getTemplateReport = getTemplateReport;

    vm.printControlOrder = printControlOrder;

    vm.groupOrderByPhysician = groupOrderByPhysician;
    vm.jsonPrint = jsonPrint;
    vm.variableReport = variableReport;
    vm.generateFile = generateFile;
    vm.directImpression = directImpression;
    vm.generationPDF = generationPDF;
    vm.sendEmailProcess = sendEmailProcess;



    vm.changeListReportPrint = changeListReportPrint;
    vm.changeStatePrint = changeStatePrint;

    vm.listreportzip = [];
    vm.listreportEmailPatient = [];
    vm.listreportEmailphysician = [];
    ///CORREO PARA CLIENTES
    vm.listreportXlsx = [];
    vm.customerMail = "";
    vm.iddCustome = 0;
    vm.listreportzipXlsx = [];
    vm.totalEmailPatien = 0;




    vm.all = '-- ' + $filter('translate')('0353') + ' --';

    vm.controlOrdersPrint = false;
    vm.printAddLabel = false;
    vm.reprint = false;
    vm.attachments = true;
    vm.orderingPrint = true;
    vm.progressPrint = false;
    vm.isOpenReport = false;
    vm.activateExecution = false;

    vm.filterRange = '1';
    vm.rangeInit = '';
    vm.rangeEnd = '';
    vm.numFilterAreaTest = 0;
    vm.listAreas = [];
    vm.listTests = [];
    vm.listLaboratories = [];
    vm.demographics = [];
    vm.listSamples = [];

    vm.typeSample = 'true';
    vm.dataReport = [];
    vm.listemails = [];
    vm.fileReport = '';
    vm.getOrderType();

    vm.destination = '1';
    vm.typePrint = '1';
    vm.quantityCopies = '1';
    vm.prepotition = $filter('translate')('0000') === 'esCo' ? 'de' : 'of';

    $rootScope.$watch('ipUser', function () {
      vm.ipUser = $rootScope.ipUser;
    });


    function jsonPrint(prmfilterprint) {
      vm.listreportzip = [];
      vm.attachments = prmfilterprint.attachments;
      vm.typePrint = prmfilterprint.typeprint;
      vm.filterDemo = prmfilterprint.filterdemo;
      vm.historySend = prmfilterprint.historysend;
      vm.ordersSend = prmfilterprint.orderssend;
      vm.quantityCopies = prmfilterprint.quantitycopies;
      vm.orderingPrint = prmfilterprint.orderingprint;
      vm.destination = prmfilterprint.destination;
      vm.sendEmail = prmfilterprint.sendEmail
      vm.serial = prmfilterprint.serial
      var json = {
        'rangeType': 1,
        'init': vm.rangeInit,
        'end': vm.rangeEnd,
        'orderType': vm.modelOrderType.id, // Agregar en lista de demográficos
        'check': null,
        'testFilterType': vm.numFilterAreaTest,
        'tests': vm.numFilterAreaTest === 1 || vm.flange === 2 ? vm.listAreas : vm.listTests,
        'demographics': vm.demographics,
        'packageDescription': false,
        'listType': 0,
        'laboratories': [],
        'apply': true,
        'samples': vm.listSamples,
        'printerId': vm.ipUser,
        'printAddLabel': true,
        'basic': true,
        'reprintFinalReport': vm.typePrint.toString() === '0',
        'attached': vm.attachments,
        'typeReport': vm.typePrint,
        'numberCopies': vm.quantityCopies,
        'serial': prmfilterprint.serial,
        'printingType': 1,
        'orderingPrint': prmfilterprint.orderingprint,
        'orderingfilterDemo': prmfilterprint.filterdemo
      }
      var str_json = JSON.stringify(json);
      return json;
    }

    function variableReport() {
      var titleReport = (vm.typePrint === '1' || vm.typePrint === '0') ? $filter('translate')('0399') : (vm.typePrint === '3' ? $filter('translate')('1065') : $filter('translate')('1066'));

      vm.variables = {
        'entity': vm.nameCustomer,
        'abbreviation': vm.abbrCustomer,
        'username': auth.userName,
        'titleReport': titleReport,
        'date': moment().format(vm.formatDate + ' hh:mm:ss a.'),
        'formatDate': vm.formatDate,
        'templateReport': 'reports.mrt',
        'typePrint': parseInt(vm.typePrint)
      }
    }

    function generateFile(prmfilterprint) {

      if (!vm.isOpenReport) { return }

      if (prmfilterprint.destination === '3' && prmfilterprint.physician !== true && prmfilterprint.branch !== true && prmfilterprint.patient !== true && vm.demo !== true) {
        logger.error($filter('translate')('1888'));
        return
      }

      vm.progressPrint = true;

      vm.parameter = prmfilterprint;
      var json = vm.jsonPrint(prmfilterprint);

      var demographics = [{}]
      demographics = json.demographics;
      if (demographics.length > 0) {
        if (demographics[0].demographic == -1) {
          vm.iddCustome = demographics[0].demographicItems[0];
        }
      }
      vm.variableReport();
      vm.totalorder = 0;
      vm.porcent = 0;
      vm.totalorderemail = 0;
      vm.porcentemail = 0;
      vm.count = 0;
      vm.listreportzip = [];
      vm.listreportEmailPatient = [];
      vm.listreportEmailphysician = [];
      return reportsDS.getOrderHeader(auth.authToken, json).then(function (data) {
        if (data.data.length > 0) {
          //VARIABLES PARA CORREO DE CLEINTE
          vm.totalEmailPatien = 0;
          for (var i = vm.listreportzipXlsx.length; i > 0; i--) {
            vm.listreportzipXlsx.pop();
          }

          for (var i = vm.listreportXlsx.length; i > 0; i--) {
            vm.listreportXlsx.pop();
          }

          vm.progressPrint = false;
          vm.listOrderHead = data.data
          UIkit.modal('#modalprogressprint', { bgclose: false, escclose: false, modal: false }).show();

          vm.datageneral = {
            'printOrder': [],
            'labelsreport': JSON.stringify($translate.getTranslationTable()),
            'variables': JSON.stringify(vm.variables),
            'numberCopies': vm.quantityCopies,
            'attached': vm.attachments,
            'rangeType': 2,
            'typeReport': vm.typePrint,
            'printingType': 1,//tipo reporte o codigo de barras
            'printingMedium': vm.destination,
            'serial': $rootScope.serialprint,
            'sendEmail': vm.sendEmail,
            'completeOrder': vm.finalReport,
            'typeNameFile': vm.namePdfConfig
          }

          switch (vm.destination) {
            case '1':
              vm.directImpression();
              break;
            case '2':
              vm.count = 0;
              vm.printOrder(vm.listOrderHead[vm.count]);
              break;
            case '3':
              vm.listOrderHead = _.filter(vm.listOrderHead, function (o) { return o.patient.email !== "" });
              vm.printOrder(vm.listOrderHead[vm.count]);
              break;
          }

          vm.progressPrint = false;
        } else {
          vm.message = $filter('translate')('0962');
          UIkit.modal('#logNoData').show();
          vm.progressPrint = false;
        }
      }, function (error) {
        vm.progressPrint = false;
        vm.modalError(error);
      });
    }


    vm.printOrder = printOrder;
    function printOrder(order) {
      vm.listemails = [];
      vm.loading = true;
      var data = {
        'printOrder': [{
          'physician': null,
          'listOrders': [{
            'order': order
          }]
        }],
        'typeReport': vm.typePrint,
        'isAttached': vm.attachments
      };

      //getOrderPreliminary
      return reportsDS.getOrderPreliminaryend(auth.authToken, data).then(function (data) {
        if (data.data !== '') {
          vm.datareport = data.data.listOrders[0];
          var dataOrder = data.data.listOrders[0].order;
          if (dataOrder.resultTest.length !== 0) {
            dataOrder.resultTest.forEach(function (value) {
              value.refMin = value.refMin === null || value.refMin === '' || value.refMin === undefined ? 0 : value.refMin;
              value.refMax = value.refMax === null || value.refMax === '' || value.refMax === undefined ? 0 : value.refMax;
              value.panicMin = value.panicMin === null || value.panicMin === '' || value.panicMin === undefined ? 0 : value.panicMin;
              value.panicMax = value.panicMax === null || value.panicMax === '' || value.panicMax === undefined ? 0 : value.panicMax;
              value.reportedMin = value.reportedMin === null || value.reportedMin === '' || value.reportedMin === undefined ? 0 : value.reportedMin;
              value.reportedMax = value.reportedMax === null || value.reportedMax === '' || value.reportedMax === undefined ? 0 : value.reportedMax;
              value.digits = value.digits === null || value.digits === '' || value.digits === undefined ? 0 : value.digits;
              value.refMinview = parseFloat(value.refMin).toFixed(value.digits);
              value.refMaxview = parseFloat(value.refMax).toFixed(value.digits);
              value.panicMinview = parseFloat(value.panicMin).toFixed(value.digits);
              value.panicMaxview = parseFloat(value.panicMax).toFixed(value.digits);
              value.reportedMinview = parseFloat(value.reportedMin).toFixed(value.digits);
              value.reportedMaxview = parseFloat(value.reportedMax).toFixed(value.digits);
              //Objeto para reporte en excel CLIENTE
              var resultExcel = {
                "orderNumber": value.order,
                "createdDate:": moment(dataOrder.createdDate).format(vm.formatDate + ' hh:mm:ss a.'),
                "documentType": dataOrder.patient.documentType.name,
                "patientName": vm.datareport.patientName,
                "patientId": dataOrder.patient.patientId,
                "testName": value.testName,
                "result": value.result
              }
              vm.listreportXlsx.push(resultExcel)
            });
          }
          if (dataOrder.allDemographics.length > 0) {
            dataOrder.allDemographics.forEach(function (value2) {
              dataOrder['demo_' + value2.idDemographic + '_name'] = value2.demographic;
              dataOrder['demo_' + value2.idDemographic + '_value'] = value2.encoded === false ? value2.notCodifiedValue : value2.codifiedName;
            });
          }
          dataOrder.createdDate = moment(dataOrder.createdDate).format(vm.formatDate + ' hh:mm:ss a.');
          dataOrder.patient.birthday = moment(dataOrder.patient.birthday).format(vm.formatDate);
          dataOrder.patient.age = common.getAgeAsString(dataOrder.patient.birthday, vm.formatDate);
          dataOrder.attachments = vm.datareport.attachments === undefined ? [] : vm.datareport.attachments;



          if (vm.parameter.physician && dataOrder.physician !== undefined && dataOrder.physician.email !== "") {
            vm.listemails.push(dataOrder.physician.email);
            if (vm.isAuxPhysicians) {
              if (dataOrder.auxiliaryPhysicians.length > 0) {
                var emailsAuxPhysicians = _.map(dataOrder.auxiliaryPhysicians, 'email');
                if (emailsAuxPhysicians) {
                  vm.listemails = _.concat(emailsAuxPhysicians, vm.listemails);
                }
              }
            }
          }
          if (vm.parameter.branch && dataOrder.branch !== undefined && dataOrder.branch !== null && dataOrder.branch.email !== "") {
            vm.listemails.push(dataOrder.branch.email);
          }
          if (vm.parameter.additionalMail !== undefined && vm.parameter.additionalMail !== null && vm.parameter.additionalMail !== "") {
            vm.listemails.push(vm.parameter.additionalMail);
          }



          return reportsDS.getUserValidate(order.orderNumber).then(function (datafirm) {
            dataOrder.listfirm = [];
            for (var i = 0; i < dataOrder.resultTest.length; i++) {
              dataOrder.resultTest[i].resultDate = moment(dataOrder.resultTest[i].resultDate).format(vm.formatDate + ' hh:mm:ss a.');
              dataOrder.resultTest[i].validationDate = moment(dataOrder.resultTest[i].validationDate).format(vm.formatDate + ' hh:mm:ss a.');
              dataOrder.resultTest[i].entryDate = moment(dataOrder.resultTest[i].entryDate).format(vm.formatDate + ' hh:mm:ss a.');
              dataOrder.resultTest[i].takenDate = moment(dataOrder.resultTest[i].takenDate).format(vm.formatDate + ' hh:mm:ss a.');
              dataOrder.resultTest[i].verificationDate = moment(dataOrder.resultTest[i].verificationDate).format(vm.formatDate + ' hh:mm:ss a.');
              dataOrder.resultTest[i].printDate = moment(dataOrder.resultTest[i].printDate).format(vm.formatDate + ' hh:mm:ss a.');

              if (dataOrder.resultTest[i].hasAntibiogram) {
                dataOrder.resultTest[i].antibiogram = dataOrder.resultTest[i].microbialDetection.microorganisms;
              }
              if (dataOrder.resultTest[i].validationUserId !== undefined) {
                var findfirm = _.filter(dataOrder.listfirm, function (o) {
                  return o.areaId === dataOrder.resultTest[i].areaId && o.validationUserId === dataOrder.resultTest[i].validationUserId;
                })[0];

                var user = _.filter(datafirm.data, function (o) { return o.id === dataOrder.resultTest[i].validationUserId });

                if (findfirm === undefined) {
                  var firm = {
                    'areaId': dataOrder.resultTest[i].areaId,
                    'areaName': dataOrder.resultTest[i].areaName,
                    'validationUserId': dataOrder.resultTest[i].validationUserId,
                    'validationUserIdentification': dataOrder.resultTest[i].validationUserIdentification,
                    'validationUserName': dataOrder.resultTest[i].validationUserName,
                    'validationUserLastName': dataOrder.resultTest[i].validationUserLastName,
                    'firm': user[0].photo
                  };
                  dataOrder.listfirm.push(firm);
                }
              }
            }
            dataOrder.resultTest = _.orderBy(dataOrder.resultTest, ['printSort'], ['asc']);
            if (vm.filteritems && vm.demo) {
              var auth = localStorageService.get('Enterprise_NT.authorizationData');
              return reportsDS.getdemo(auth.authToken, order.orderNumber).then(function (data) {
                var dataDemo = [];
                if (data.status === 200) {
                  var email = _.concat(data.data.demographics, data.data.patient.demographics);
                  /*   if (data.data.auxiliaryPhysicians !== undefined) {
                      if (vm.isAuxPhysicians && data.data.auxiliaryPhysicians.length !== 0) {
                        var indexes = [-201, -202, -203, -204, -205];
                        for (var t = 0; t < data.data.auxiliaryPhysicians.length; t++) {
                          if (data.data.auxiliaryPhysicians[t].idDemoAux === undefined || data.data.auxiliaryPhysicians[t].idDemoAux === 0 || data.data.auxiliaryPhysicians[t].idDemoAux === null || data.data.auxiliaryPhysicians[t].idDemoAux === '') {
                            data.data.auxiliaryPhysicians[t].idDemoAux = indexes[t];
                          }
                          if (data.data.auxiliaryPhysicians[t].idDemoAux === -201) {
                            data.data.auxiliaryPhysicians[t].demographics = $filter('translate')('0086') + ' 1';
                          }
                          if (data.data.auxiliaryPhysicians[t].idDemoAux === -202) {
                            data.data.auxiliaryPhysicians[t].demographics = $filter('translate')('0086') + ' 2';
                          }
                          if (data.data.auxiliaryPhysicians[t].idDemoAux === -203) {
                            data.data.auxiliaryPhysicians[t].demographics = $filter('translate')('0086') + ' 3';
                          }
                          if (data.data.auxiliaryPhysicians[t].idDemoAux === -204) {
                            data.data.auxiliaryPhysicians[t].demographics = $filter('translate')('0086') + ' 4';
                          }
                          if (data.data.auxiliaryPhysicians[t].idDemoAux === -205) {
                            data.data.auxiliaryPhysicians[t].demographics = $filter('translate')('0086') + ' 5';
                          }
                        }
                        var email = _.concat(email, data.data.auxiliaryPhysicians);
                      }
                    } */
                  if (email !== undefined) {
                    if (email.length !== 0) {
                      email.forEach(function (element, index) {
                        if (element.email !== undefined && element.email !== '' && element.email !== null) {
                          var comparated = $filter("filter")(vm.emailist, function (e) {
                            return e.name === element.demographic;
                          })
                          if (comparated.length !== 0) {
                            vm.listemails.push(element.email);
                          }
                        }
                      });
                    }
                  }
                  vm.addreport(dataOrder);
                } else {
                  dataOrder.EmailDemo = dataDemo;
                  vm.addreport(dataOrder);
                }
              }, function (error) {
                vm.modalError(error);
              });
            } else {
              vm.addreport(dataOrder);
            }

            /*  vm.addreport(dataOrder); */
          })
        } else {
          vm.count = vm.count === undefined ? 0 : vm.count;
          vm.listOrderHead[vm.count].printing = false;
          vm.totalorder = vm.totalorder + 1;
          vm.count = vm.count + 1;
          if (vm.listOrderHead.length === vm.count) {
            vm.porcent = Math.round((vm.totalorder * 100) / vm.listOrderHead.length);
            UIkit.modal('#modalprogressprint', { bgclose: false, keyboard: false }).hide();
            vm.listOrderPrint = [];

            if (vm.destination == '2') {
              if (vm.listreportzip.length > 0) {
                reportadicional.saveReportPdfAll(vm.listreportzip);
              }
              vm.changeListReportPrint(1)
            }
            else if (vm.destination == '3') {
              if (vm.listreportEmailPatient.length > 0) {
                vm.totalorder = 0;
                vm.porcent = 0;
                vm.count = 0;
                UIkit.modal('#modalprogressprintEmail', { bgclose: false, keyboard: false }).show();
                vm.sendEmailProcess()


              }
            }
            else {
              vm.changeListReportPrint(1)
            }
          } else {
            vm.porcent = Math.round((vm.totalorder * 100) / vm.listOrderHead.length)
            vm.printOrder(vm.listOrderHead[vm.count]);
          }

        }
      });
    }

    //REPORTE DE EXCEL PARA CLIENTES
    vm.createreportExcel = createreportExcel;
    function createreportExcel(data) {
      return $q(function (resolve, reject) {
        setTimeout(function () {
          var parameterReport = {};
          parameterReport.datareport = data
          parameterReport.variables = {
            "entity": "CLTECH",
            "abbreviation": "CLT",
            "date": moment().format(vm.formatDate + " hh:mm:ss a."),
            "title": "COMPILADO DE RESULTADOS",
            "username": auth.userName
          };
          parameterReport.pathreport = '/Report/reportsandconsultations/reports/recordOflOrdersXLSX/reports.mrt';
          parameterReport.labelsreport = labelsreport;
          var labelsreport = JSON.stringify($translate.getTranslationTable());
          labelsreport = JSON.parse(labelsreport);
          var report = new Stimulsoft.Report.StiReport();
          report.loadFile(parameterReport.pathreport);
          // Load reports from JSON object
          var jsonData = { 'data': [parameterReport.datareport], 'Labels': [labelsreport], 'Variables': [parameterReport.variables] };
          var dataSet = new Stimulsoft.System.Data.DataSet();
          dataSet.readJson(jsonData);
          // Remove all connections from the report template
          report.dictionary.databases.clear();
          // Register DataSet object
          report.regData('Demo', 'Demo', dataSet);
          report.renderAsync(function () {
            var data = report.exportDocument(Stimulsoft.Report.StiExportFormat.Excel2007);
            resolve(new Uint8Array(data))
          })

        }, 5000);
      })
    }


    vm.addreport = addreport;
    function addreport(order) {
      var auth = localStorageService.get('Enterprise_NT.authorizationData');
      var parameterReport = {};
      var titleReport = (vm.datageneral.typeReport === '1' || vm.datageneral.typeReport === '0') ? $filter('translate')('0399') : (vm.datageneral.typeReport === '3' ? $filter('translate')('1065') : $filter('translate')('1066'));
      parameterReport.variables = {
        'entity': vm.nameCustomer,
        'abbreviation': vm.abbrCustomer,
        'username': auth.userName,
        'titleReport': titleReport,
        'date': moment().format(vm.formatDate + ' hh:mm:ss a.'),
        'formatDate': vm.formatDate,
        'destination': vm.destination
      };
      parameterReport.pathreport = '/Report/reportsandconsultations/reports/' + vm.getTemplateReport(order);
      parameterReport.labelsreport = $translate.getTranslationTable();
      vm.dataorder = order;
      vm.setReport(parameterReport, order);

    }

    vm.setReport = setReport;
    function setReport(parameterReport, datareport) {
      setTimeout(function () {
        Stimulsoft.Base.StiLicense.key = "6vJhGtLLLz2GNviWmUTrhSqnOItdDwjBylQzQcAOiHmThaUC9O4WIvthEnnI4JdCFJitv7JqpZrafYACHBzqByeQgdRB2Y0f1fPCqWyqMixc9WzW4Sv/5CPkYzFMnjbjoUCnrAeqsNnqHaHuQ1W7rg86AEHor9p68M7+xlwPg89JGg6XsDnSwnP1/JVoHE5OSwb27KjWJDCmZHKRgnBEeyjYZ/kKDDnEK6TK/43/HprWgL2VoEVNdm6HCbWQiFKRfAt2f/UYfbHVGOHi0l+3wpIXT5KbZdKno2CaJggJWezFBpGxntTPs5XMQ5YEyyCHaXWPw8LGdz1rpUGBv4Idek9W3wLAHVTpMkMx53MYm++luIRniPcXmkuaOV9mLLR5jnY/gPVd1TIr8uEKLoJlf6GSq12JoJ/OeKdf+dTya+7O5LNnnt7m+lfDYQsYKZ5RQU+eo+8X3zuS4XswRa20nOIx3QnLNOwbKtvRtnaMH7cEf8B0AiRB0umNqy4WZQAP+FU329w/QC0/0oOwJV9Oo/xFe11Q8nU1wd5Arsb9nFdgK+8yPWgvtVJr4PKI7XD8";
        var report = new Stimulsoft.Report.StiReport();
        report.loadFile(parameterReport.pathreport);
        var jsonData = {
          'order': datareport,
          'Labels': [parameterReport.labelsreport],
          'variables': [parameterReport.variables]
        };

        var dataSet = new Stimulsoft.System.Data.DataSet();
        dataSet.readJson(jsonData);

        report.dictionary.databases.clear();
        report.regData('Demo', 'Demo', dataSet);

        report.renderAsync(function () {
          var settings = new Stimulsoft.Report.Export.StiPdfExportSettings();
          var service = new Stimulsoft.Report.Export.StiPdfExportService();
          var stream = new Stimulsoft.System.IO.MemoryStream();
          service.exportToAsync(function () {
            var data = stream.toArray();
            var buffer = new Uint8Array(data);
            vm.copyPages(buffer, datareport.attachments);
          }, report, stream, settings);
        });
      }, 50);
    }

    vm.copyPages = copyPages;
    function copyPages(reportpreview, attachments) {

      var PDFDocument = PDFLib.PDFDocument;
      var pdfDocRes = PDFDocument.create();

      pdfDocRes.then(function (pdfDoc) {
        var firstDonorPdfDoc = PDFDocument.load(reportpreview, {
          ignoreEncryption: true
        });
        firstDonorPdfDoc.then(function (greeting) {
          var firstDonorPageRes = pdfDoc.copyPages(greeting, greeting.getPageIndices());

          firstDonorPageRes.then(function (firstDonorPage) {

            firstDonorPage.forEach(function (page) {
              pdfDoc.addPage(page);
            });
            if (vm.attachments && attachments.length > 0) {
              var listbuffer = [];
              var calcula = 0;
              for (var i = 0; i < attachments.length; i++) {
                var reportbasee64 = _base64ToArrayBuffer(attachments[i].file);
                if (attachments[i].extension === 'pdf') {
                  listbuffer[i] = PDFDocument.load(reportbasee64, {
                    ignoreEncryption: true
                  });
                  listbuffer[i].then(function (listbufferelement) {
                    var copiedPagesRes = pdfDoc.copyPages(listbufferelement, listbufferelement.getPageIndices());
                    copiedPagesRes.then(function (copiedPages) {
                      copiedPages.forEach(function (page) {
                        pdfDoc.addPage(page);
                      });
                      calcula++;
                      if (calcula === attachments.length) {
                        pdfDoc.save().then(function (pdf) {
                          var pdfUrl = URL.createObjectURL(new Blob([pdf], {
                            type: 'application/pdf'
                          }));
                          //window.open(pdfUrl, '_blank');
                          sendbuffer(pdf, pdfUrl);
                        });
                      }
                    });
                  });
                } else if (attachments[i].extension === 'jpg' || attachments[i].extension === 'jpeg') {
                  var jpgImageRes = pdfDoc.embedJpg(reportbasee64);
                  jpgImageRes.then(function (jpgImage) {
                    var jpgDims;
                    var xwidth = false;
                    if (jpgImage.scale(0.5).width <= 576) {
                      jpgDims = jpgImage.scale(0.5);
                    } else if (jpgImage.scale(0.4).width <= 576) {
                      jpgDims = jpgImage.scale(0.4);
                    } else if (jpgImage.scale(0.3).width <= 576) {
                      jpgDims = jpgImage.scale(0.3);
                      xwidth = true;
                    } else {
                      jpgDims = jpgImage.scale(0.2);
                      xwidth = true;
                    }
                    var page = pdfDoc.addPage();
                    page.drawImage(jpgImage, {
                      x: xwidth ? 10 : page.getWidth() / 2 - jpgDims.width / 2,
                      y: page.getHeight() / 2 - jpgDims.height / 2,
                      width: jpgDims.width,
                      height: jpgDims.height,
                    });
                    calcula++;
                    if (calcula === attachments.length) {
                      pdfDoc.save().then(function (pdf) {
                        var pdfUrl = URL.createObjectURL(new Blob([pdf], {
                          type: 'application/pdf'
                        }));
                        //window.open(pdfUrl, '_blank');
                        sendbuffer(pdf, pdfUrl);
                      });
                    }
                  });
                } else {
                  var pngImageRes = pdfDoc.embedPng(reportbasee64);
                  pngImageRes.then(function (pngImage) {
                    var pngDims;
                    var xwidth = false;
                    if (pngImage.scale(0.5).width <= 576) {
                      pngDims = pngImage.scale(0.5);
                    } else if (pngImage.scale(0.4).width <= 576) {
                      pngDims = pngImage.scale(0.4);
                    } else if (pngImage.scale(0.3).width <= 576) {
                      pngDims = pngImage.scale(0.3);
                      xwidth = true;
                    } else {
                      pngDims = pngImage.scale(0.2);
                      xwidth = true;
                    }
                    var page = pdfDoc.addPage();
                    // Draw the PNG image near the lower right corner of the JPG image
                    page.drawImage(pngImage, {
                      x: xwidth ? 10 : page.getWidth() / 2 - pngDims.width / 2,
                      y: page.getHeight() / 2 - pngDims.height,
                      width: pngDims.width,
                      height: pngDims.height,
                    });
                    calcula++;
                    if (calcula === attachments.length) {
                      pdfDoc.save().then(function (pdf) {
                        var pdfUrl = URL.createObjectURL(new Blob([pdf], {
                          type: 'application/pdf'
                        }));
                        sendbuffer(pdf, pdfUrl);
                      });
                    }
                  });
                }
              }
            } else {
              pdfDoc.save().then(function (pdf) {
                var pdfUrl = URL.createObjectURL(new Blob([pdf], {
                  type: 'application/pdf'
                }));
                sendbuffer(pdf, pdfUrl);
              });
            }

          });
        }, function (reason) {
          alert('Failed: ' + reason);
        });
      });
    }

    function _base64ToArrayBuffer(base64) {
      var binary_string = window.atob(base64);
      var len = binary_string.length;
      var bytes = new Uint8Array(len);
      for (var i = 0; i < len; i++) {
        bytes[i] = binary_string.charCodeAt(i);
      }
      return bytes.buffer;
    }

    vm.sendbuffer = sendbuffer;
    function sendbuffer(buffer) {
      var auth = localStorageService.get('Enterprise_NT.authorizationData');
      var byteArray = new Uint8Array(buffer);
      var byteString = '';
      for (var i = 0; i < byteArray.byteLength; i++) {
        byteString += String.fromCharCode(byteArray[i]);
      }

      var b64 = window.btoa(byteString);
      var patienthistory = vm.datareport.patientName.replace(/ /g, "").toUpperCase()
      var finalData = {
        nameFile: vm.namePdfConfig === '1' ? vm.datareport.patientHistory : patienthistory,
        order: vm.dataorder.orderNumber,
        bufferReport: b64,
        encrypt: vm.datareport.encrypt,
        branch: vm.dataorder.branch.id,
        service: vm.dataorder.service !== undefined ? vm.dataorder.service.id : "",
        printingMedium: vm.destination,
        patientEmail: vm.datareport.patientEmail,
        serial: vm.serial,
        NumberCopies: vm.quantityCopies,
        physicianEmail: vm.dataorder.physician !== undefined ? vm.dataorder.physician.email : "",
        sendEmail: vm.sendEmail,
      }

      if (vm.destination == '2') {
        var listreport = {
          'nameReport': finalData.nameFile + "_" + vm.dataorder.orderNumber,
          'buffer': byteString
        }
        vm.listreportzip.push(listreport);
        vm.changeStatePrint();
      }
      else if (vm.destination == '3') {
        var listreport = {
          'nameReport': finalData.nameFile + "_" + vm.dataorder.orderNumber,
          'buffer': byteString
        }






        if (vm.parameter.patient) {
          if (vm.listreportEmailPatient.length === 0) {
            var patientemail = {
              patientid: vm.datareport.patientHistory,
              correo: vm.datareport.patientEmail,
              listreportzip: [listreport]
            }
            vm.listreportEmailPatient.push(patientemail);
          }
          else {
            var findreport = _.filter(vm.listreportEmailPatient, function (v) { return v.patientid === vm.datareport.patientHistory });
            if (findreport.length > 0) {
              findreport[0].listreportzip.push(listreport)
            }
            else {
              var patientemail = {
                patientid: vm.datareport.patientHistory,
                correo: vm.datareport.patientEmail,
                listreportzip: [listreport]
              }
              vm.listreportEmailPatient.push(patientemail);
            }
          }
        }
        if (vm.listemails.length > 0) {
          vm.listemails.forEach(function (physician) {
            if (physician !== undefined) {
              if (vm.listreportEmailPatient.length === 0) {
                var patientemail = {
                  correo: physician,
                  listreportzip: [listreport]
                }
                vm.listreportEmailPatient.push(patientemail);
              }
              else {
                var findreport = _.filter(vm.listreportEmailPatient, function (v) { return v.correo === physician });
                if (findreport.length > 0) {
                  findreport[0].listreportzip.push(listreport)
                }
                else {
                  var patientemail = {
                    correo: physician,
                    listreportzip: [listreport]
                  }
                  vm.listreportEmailPatient.push(patientemail);
                }
              }
            }
          });
        }




        vm.changeStatePrint();
      }
      else {
        return reportsDS.getSendPrintFinalReport(auth.authToken, finalData).then(function (data) {
          if (data.status === 200) {
            vm.changeStatePrint()
          }
        }, function (error) {
          vm.loading = false;
          if (error.data.errorFields[0] === "0| the client is not connected") {
            vm.message = $filter('translate')('1074');
            UIkit.modal('#logNoDataFinal').show();
          } else {
            vm.modalError(error);
          }
        });

      }
    }

    function changeStatePrint() {
      var personRecive = "";
      if (vm.destination === '3') {

        switch (vm.sendEmail) {
          case '1':
            personRecive = vm.datareport.patientEmail;
            break;
          case '2':
            personRecive = vm.dataorder.physician !== undefined ? vm.dataorder.physician.email : "";
            break;
          case '3':
            personRecive = vm.datareport.patientEmail + " " + vm.dataorder.physician !== undefined ? "; " + vm.dataorder.physician.email : ""
            break;
        }

      }
      var patientName = vm.datareport.patientName;
      vm.datareport.order.createdDate = '';
      vm.datareport.order.patient.birthday = '';
      for (var i = 0; i < vm.datareport.order.resultTest.length; i++) {
        vm.datareport.order.resultTest[i].resultDate = '';
        vm.datareport.order.resultTest[i].validationDate = '';
        vm.datareport.order.resultTest[i].entryDate = '';
        vm.datareport.order.resultTest[i].takenDate = '';
        vm.datareport.order.resultTest[i].verificationDate = '';
        vm.datareport.order.resultTest[i].printDate = '';
      }
      var datachange = {
        filterOrderHeader: { printingMedium: vm.destination, typeReport: vm.typePrint, personReceive: personRecive },
        order: vm.datareport.order,
        user: auth.id
      }
      return reportsDS.changeStateTest(auth.authToken, datachange).then(function (data) {
        vm.listOrderHead[vm.count].printing = true;
        vm.totalorder = vm.totalorder + 1;
        vm.count = vm.count + 1;
        if (vm.listOrderHead.length === vm.count) {
          vm.porcent = Math.round((vm.totalorder * 100) / vm.listOrderHead.length);
          UIkit.modal('#modalprogressprint', { bgclose: false, keyboard: false }).hide();
          vm.listOrderPrint = [];

          if (vm.destination == '2') {
            reportadicional.saveReportPdfAll(vm.listreportzip);
            vm.changeListReportPrint(1)
          }
          else if (vm.destination == '3') {
            if (vm.listreportEmailPatient.length > 0) {
              vm.totalorder = 0;
              vm.porcent = 0;
              vm.count = 0;
              UIkit.modal('#modalprogressprintEmail', { bgclose: false, keyboard: false }).show();
              vm.sendEmailProcess(patientName);
            }
          }
          else {
            vm.changeListReportPrint(1)
          }
        } else {
          vm.porcent = Math.round((vm.totalorder * 100) / vm.listOrderHead.length)
          vm.printOrder(vm.listOrderHead[vm.count]);
        }
      }, function (error) {
        vm.loading = false;
        vm.modalError(error);
      });

    }



    function directImpression() {
      vm.listOrderHead[vm.totalorder].templateorder = vm.getTemplateReport(vm.listOrderHead[vm.totalorder]);
      vm.datageneral.printOrder[0] = { 'listOrders': [{ 'order': vm.listOrderHead[vm.totalorder] }] };
      vm.datageneral.variables = JSON.stringify(vm.variables);

      return reportsDS.printOrderBody(auth.authToken, vm.datageneral).then(function (data) {
        if (data.status === 200) {
          vm.listOrderHead[vm.totalorder].printing = data.data[0].printing;
          vm.totalorder = vm.totalorder + 1;
          vm.porcent = Math.round((vm.totalorder * 100) / vm.listOrderHead.length)
          if (vm.totalorder < vm.listOrderHead.length) {
            vm.directImpression();
          }
          else {
            setTimeout(function () {
              UIkit.modal('#modalprogressprint', { bgclose: false, keyboard: false }).hide();
              vm.listOrderPrint = [];
              vm.changeListReportPrint(1)
            }, 3000);
          }
        }
      }, function (error) {
        vm.loading = false;
        if (error.data.code === 2) {
          UIkit.modal('#modalprogressprint').hide();
          vm.message = $filter('translate')('1074');
          setTimeout(function () { UIkit.modal('#logNoData').show() }, 1000);
        }
        else {
          vm.modalError(error);
        }

      });
    }

    function generationPDF() {

      vm.listOrderHead.forEach(function (element) {
        element.templateorder = vm.getTemplateReport(element);
      });

      vm.datageneral.printOrder[0] = { 'physician': { 'id': 0 }, 'listOrders': _.flatMap(vm.listOrderHead, function (e) { return { 'order': e } }) };
      vm.datageneral.variables = JSON.stringify(vm.variables);

      return reportsDS.printOrderBody(auth.authToken, vm.datageneral).then(function (data) {
        if (data.status === 200) {
          vm.listOrderHead = _.zipWith(data.data, vm.listOrderHead, function (a, b) {
            return { 'orderNumber': b.orderNumber, 'patient': b.patient, 'printing': a.printing };
          });
          vm.totalorder = vm.listOrderHead.length;
          vm.porcent = 100

          setTimeout(function () {
            UIkit.modal('#modalprogressprint', { bgclose: false, keyboard: false }).hide();
            vm.listOrderPrint = [];
            vm.changeListReportPrint(1)
          }, 3000);

        }
      }, function (error) {
        vm.loading = false;
        if (error.data.code === 2) {
          UIkit.modal('#modalprogressprint').hide();
          vm.message = $filter('translate')('1074');
          setTimeout(function () { UIkit.modal('#logNoData').show() }, 1000);
        }
        else {
          vm.modalError(error);
        }
      });
    }


    function sendEmailProcess(patientName) {
      var auth = localStorageService.get('Enterprise_NT.authorizationData');

      if (vm.listreportEmailPatient.length === vm.count) {
        UIkit.modal('#modalprogressprintEmail', { bgclose: false, keyboard: false }).hide();
        vm.changeListReportPrint(1)
      }
      else {

        var zip = new JSZip();
        vm.totalEmailPatien++;
        vm.listreportEmailPatient[vm.count].listreportzip.forEach(function (value2) {
          vm.listreportzipXlsx.push(value2);//AGREGAR FILE  ZIP CLIENTE
          zip.file(value2.nameReport + ".pdf", value2.buffer, { binary: true });
        });

        var nameZip = $filter('translate')('0360') + '[' + vm.listreportEmailPatient[vm.count].listreportzip.length.toString() + '].zip';
        var blob = zip.generate();

        var EmailBody = vm.EmailBody.replace("||PATIENT||", patientName);
        var EmailSubjectPatient = vm.EmailSubjectPatient.replace("||PATIENT||", patientName);

        var data = {
          "recipients": [vm.listreportEmailPatient[vm.count].correo],
          "subject": EmailSubjectPatient,
          "body": EmailBody,
          "attachment": [
            {
              path: blob,
              type: "1",
              filename: nameZip
            }
          ]
        }
        reportadicional.sendEmailCompress(auth.authToken, data).then(function (response) {
          if (response.status === 200) {

            vm.totalorderemail = vm.totalorderemail + 1;//CONTEO EMAIL DE PACIENTES
            vm.porcentemail = Math.round((vm.totalorderemail * 100) / vm.listreportEmailPatient.length)
            vm.count = vm.count + 1;
            UIkit.modal('#modalprogressprintEmail', { bgclose: false, keyboard: false }).hide();
            vm.sendEmailProcess(patientName);

          }
        }, function (error) {
          return false;
        })

        ///ENVIO DE CORREO A CLIENTE
        var sendClientEmail = localStorageService.get("EnvioCorreoCliente") === "True";

        if (vm.iddCustome > 0 && vm.totalEmailPatien === vm.listreportEmailPatient.length && sendClientEmail) {
          var promise = createreportExcel(vm.listreportXlsx);
          promise.then(function (buffer) {
            var listreport = {
              'nameReport': "Reportxls",
              'buffer': buffer
            }
            vm.listreportzipXlsx.push(listreport)
            var zip = new JSZip();
            vm.listreportzipXlsx.forEach(function (archivo) {
              if (archivo.nameReport == "Reportxls") {
                zip.file(archivo.nameReport + ".xlsx", archivo.buffer, { binary: true });
              } else {
                zip.file(archivo.nameReport + ".pdf", archivo.buffer, { binary: true });
              }
            });
            nameZip = $filter('translate')('0360') + '[' + vm.listreportzipXlsx.length.toString() + '].zip';
            blob = zip.generate();
            return customerDS.getCustomerId(auth.authToken, vm.iddCustome);
          }).then(function (data) {
            var auth = localStorageService.get('Enterprise_NT.authorizationData');
            var datacL = {
              "recipients": [data.data.email],
              "subject": vm.EmailSubjectPatient,
              "body": vm.EmailBody,
              "attachment": [
                {
                  path: blob,
                  type: "1",
                  filename: nameZip
                }
              ]
            }
            return reportadicional.sendEmailCompress(auth.authToken, datacL)
          }).then(function (response) {
            if (response.status === 200) {
              vm.totalorderemail = vm.totalorderemail + 1;
              vm.porcentemail = Math.round((vm.totalorderemail * 100) / vm.listreportEmailPatient.length)
              vm.count = vm.count + 1;
              UIkit.modal('#modalprogressprintEmail', { bgclose: false, keyboard: false }).hide();
              vm.sendEmailProcess(patientName);
            }
          }).catch(function (error) {
            return false;
          })
        }


      } saveAs(blob, nameZip);


    }
    function groupOrderByPhysician(data) {
      vm.listordernogroup = data;
      data.forEach(function (element) {
        element.templateorder = vm.getTemplateReport(element);
      });

      var groupOrder = _.groupBy(data, function (b) { return b.physician.name });
      var listgroupphisician = [];
      for (var physician in groupOrder) {
        var order = _.flatMap(groupOrder[physician], function (e) { return { 'order': e } });
        var prueba = groupOrder[physician]
        listgroupphisician.push(
          {
            'physician': prueba[0].physician,
            'listOrders': order
          })
      }
      return listgroupphisician;
    }

    function changeListReportPrint(type) {
      if (type === 1) {
        vm.listOrderPrint = $filter('filter')(vm.listOrderHead, function (e) { return e.printing === true });
        vm.titleReport = $filter('translate')('0383');
      }
      else {
        vm.listOrderPrint = $filter('filter')(vm.listOrderHead, function (e) { return e.printing === false });
        vm.titleReport = $filter('translate')('1072');
      }
      UIkit.modal('#modallistorder').show();
    }

    function printControlOrder(type) {
      var parameterReport = {};

      parameterReport.variables = {
        'entity': vm.nameCustomer,
        'abbreviation': vm.abbrCustomer,
        'date': moment().format(vm.formatDate + ', h:mm:ss a'),
        'title': vm.titleReport,
        'username': auth.userName,
        'typePrint': vm.typePrint
      }

      parameterReport.pathreport = '/Report/reportsandconsultations/reports/reportprintedorders.mrt';
      parameterReport.labelsreport = JSON.stringify($translate.getTranslationTable());

      var datareport = LZString.compressToUTF16(JSON.stringify(vm.listOrderPrint));

      localStorageService.set('parameterReport', parameterReport);
      localStorageService.set('dataReport', datareport);

      window.open('/viewreport/viewreport.html');
    }

    function getOrderType() {
      vm.listOrderType = [{ 'id': 0, 'name': vm.all }];
      vm.modelOrderType = { 'id': 0, 'name': vm.all };
      return ordertypeDS.getlistOrderType(auth.authToken).then(function (data) {
        if (data.status === 200) {
          data.data.forEach(function (value) {
            vm.listOrderType.push({ 'id': value.id, 'name': value.name });
          });
          vm.getDemographicsALL();
          vm.getlistReportFile();
        }

        $rootScope.blockView = false;
      });
    }

    //Método que devuelve la lista de demofgráficos
    function getDemographicsALL() {
      var auth = localStorageService.get('Enterprise_NT.authorizationData');
      if (parseInt(vm.demographicTitle) !== 0) {
        return demographicDS.getDemographicsALL(auth.authToken).then(function (data) {
          vm.demographicTemplate = _.filter(data.data, function (v) { return v.id === parseInt(vm.demographicTitle) })[0];
          vm.nameDemographic = 'reports_' + vm.demographicTemplate.name;
          vm.referenceDemographic = vm.demographicTemplate.name;

          if (parseInt(vm.demographicTitle) < 0) {
            switch (parseInt(vm.demographicTitle)) {
              case -1: vm.demographicTemplate.name = $filter('translate')('0085'); vm.referenceDemographic = 'account'; break; //Cliente
              case -2: vm.demographicTemplate.name = $filter('translate')('0086'); vm.referenceDemographic = 'physician'; break; //Médico
              case -3: vm.demographicTemplate.name = $filter('translate')('0087'); vm.referenceDemographic = 'rate'; break; //Tarifa
              case -4: vm.demographicTemplate.name = $filter('translate')('0088'); vm.referenceDemographic = 'type'; break; //Tipo de orden
              case -5: vm.demographicTemplate.name = $filter('translate')('0003'); vm.referenceDemographic = 'branch'; break; //Sede
              case -6: vm.demographicTemplate.name = $filter('translate')('0090'); vm.referenceDemographic = 'service'; break; //Servicio
              case -7: vm.demographicTemplate.name = $filter('translate')('0091'); vm.referenceDemographic = 'race'; break; //Raza
            }
            vm.nameDemographic = 'reports_' + vm.demographicTemplate.name
          }

        }, function (error) {
          vm.modalError();
        });
      }
      else {
        vm.demographicTemplate = null;
        vm.nameDemographic = 'reports';
      }
    }

    function getlistReportFile() {
      reportadicional.getlistReportFile().then(function (response) {
        if (response.status === 200) {
          vm.listreports = response.data;
        }
        else {
          vm.listreports = [];
        }
      }, function (error) {
        return false;
      })
    }

    function getTemplateReport(order) {
      var template = '';
      if (vm.demographicTemplate === null) {
        return 'reports.mrt'
      }
      else {
        if (vm.demographicTemplate.encoded && vm.demographicTemplate.id > 0) {
          order.demographicItemTemplate = _.filter(order.allDemographics, function (o) { return o.idDemographic === vm.demographicTemplate.id })[0]
          template = vm.nameDemographic + '_' + order.demographicItemTemplate.codifiedCode + '.mrt';
        }
        else if (vm.demographicTemplate.encoded && vm.demographicTemplate.id < 0) {
          order.demographicItemTemplate = order[vm.referenceDemographic];
          template = vm.nameDemographic + '_' + order.demographicItemTemplate.code + '.mrt';
        }
        else {
          template = vm.nameDemographic + '.mrt'
        }

        if (vm.demographicTemplate !== null) {
          if (_.filter(vm.listreports, function (o) { return o.name === template }).length > 0) {
            return template
          }
          else {
            return 'reports.mrt'
          }
        }
      }

    }

    //** Método para sacar el popup de error**//
    function modalError(error) {
      vm.Error = {};
      vm.Error = error.data;
      vm.ShowPopupError = true;
    }

    function isAuthenticate() {
      var auth = localStorageService.get('Enterprise_NT.authorizationData');
      if (auth === null || auth.token) {
        $state.go('login');
      }
      else {
        vm.init();
      }
    }

    vm.Itemsdemo = Itemsdemo;
    function Itemsdemo() {
      vm.filteritems = false;
      vm.emailall = [{
        'id': -4,
        'demographic': $filter('translate')('0088'),
        'name': 'ORDERTYPE'
      }];

      if (localStorageService.get('ManejoTipoDocumento') === 'True') {
        var demo = {
          'id': -10,
          'demographic': $filter('translate')('0233'),
          'name': 'DOCUMENTYPE'
        }
        vm.emailall.add(demo);
      }

      if (localStorageService.get('ManejoServicio') === 'True') {
        var demo = {
          'id': -6,
          'demographic': $filter('translate')('0090'),
          'name': "SERVICE"
        }
        vm.emailall.add(demo);
      }

      if (localStorageService.get('ManejoCliente') === 'True') {
        var demo = {
          'id': -1,
          'demographic': $filter('translate')('0085'),
          'name': "ACOUNT"
        }
        vm.emailall.add(demo);
      }

      if (localStorageService.get('ManejoTarifa') === 'True') {
        var demo = {
          'id': -3,
          'demographic': $filter('translate')('0087'),
          'name': "RATE"
        }
        vm.emailall.add(demo);
      }

      if (localStorageService.get('ManejoRaza') === 'True') {
        var demo = {
          'id': -7,
          'demographic': $filter('translate')('0091'),
          'name': "RACE"
        }
        vm.emailall.add(demo);
      }

      /*  if (vm.isAuxPhysicians) {
         for (var i = 0; i < vm.totalAuxPhysicians; i++) {
           var count = i + 1;
           var demo = {
             'id': -201,
             'demographic': $filter('translate')('0086') + ' ' + count + '',
             'name': $filter('translate')('0086') + ' ' + count + '',
           }
           vm.emailall.add(demo);
         }
       } */

      var auth = localStorageService.get('Enterprise_NT.authorizationData');
      return demographicDS.getDemoEncodeds(auth.authToken).then(function (data) {
        if (data.status === 200) {
          if (data.data.length !== 0) {
            data.data.forEach(function (element, index) {
              element.demographic = element.name;
              vm.emailall.add(element);
            });
          }
          vm.filteritems = true;
          vm.emailist = JSON.parse(JSON.stringify(vm.emailall));

        } else {
          vm.emailist = JSON.parse(JSON.stringify(vm.emailall));
          vm.filteritems = true;
        }
      }, function (error) {
        vm.modalError(error);
      });
    }


    vm.querySearch1 = querySearch1;
    function querySearch1(criteria) {
      if (criteria === '?') {
        return vm.emailall;
      } else {
        if (vm.emailall.length === vm.emailist.length) {
          return [];
        } else {
          var search = $filter("filter")(vm.emailall, function (e) {
            var validated = $filter("filter")(vm.emailist, function (f) {
              return f.id === e.id;
            })
            return e.demographic.indexOf(criteria.toLowerCase()) !== -1 && validated.length === 0 || e.demographic.indexOf(criteria.toUpperCase()) !== -1 && validated.length === 0;
          });
          return criteria ? search : [];
        }
      }

    }


    function init() {
      vm.Itemsdemo()
      if (($filter('translate')('0000')) === 'esCo') {
        moment.locale('es');
      } else {
        moment.locale('en');
      }
    }


    vm.isAuthenticate();

  }

})();
/* jshint ignore:end */
