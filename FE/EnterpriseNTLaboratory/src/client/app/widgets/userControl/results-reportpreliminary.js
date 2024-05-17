/********************************************************************************
  ENTERPRISENT - Todos los derechos reservados CLTech Ltda.
  PROPOSITO:   Generacion del reporte preliminar de una orden.
  PARAMETROS:   order @numero de orden para generar el informe preliminar.
                tests @lista de pruebas que aplican para el reporte


  AUTOR:        Angelica Diaz
  FECHA:        2019-05-29
  IMPLEMENTADA EN:
  1.02_EnterpriseNT_FE/EnterpriseNTLaboratory/src/client/app/modules/analytical/resultsentry/resultsentry.html

  MODIFICACIONES:

  1. aaaa-mm-dd. Autor
     Comentario...

********************************************************************************/

(function () {
  'use strict';

  angular
    .module('app.widgets')
    .directive('resultreportpreliminary', resultreportpreliminary);

  resultreportpreliminary.$inject = ['$filter', 'reportsDS', 'localStorageService',
    '$translate', 'demographicDS', 'reportadicional', 'common'
  ];

  /* @ngInject */
  function resultreportpreliminary($filter, reportsDS, localStorageService,
    $translate, demographicDS, reportadicional, common) {
    var directive = {
      restrict: 'EA',
      templateUrl: 'app/widgets/userControl/results-reportpreliminary.html',
      scope: {
        order: '=?order',
        tests: '=?tests',
        openreport: '=?openreport'
      },

      controller: ['$scope', function ($scope) {
        var vm = this;
        vm.formatDate = localStorageService.get('FormatoFecha').toUpperCase();
        vm.preliminaryComplete = localStorageService.get('VerPreliminarRegistro') === 'True';
        vm.demographicTitle = localStorageService.get('DemograficoTituloInforme');
        vm.preliminaryAttachment = localStorageService.get('ImprimirAdjuntosPreliminar');
        vm.AddTestEditreports = localStorageService.get('AgregaPruebaEditoreportes') === 'True';
        vm.AddTestEditorArea = localStorageService.get('AgregaPruebaEditorArea');
        vm.AddTestEditorbranch = localStorageService.get('AgregaPruebaEditorDemo');

        vm.getOrder = getOrder;
        vm.printOrder = printOrder;
        vm.getTemplateReport = getTemplateReport;
        vm.getDemographicsALL = getDemographicsALL;
        vm.getlistReportFile = getlistReportFile;
        vm.setReport = setReport;
        vm.copyPages = copyPages;
        vm.loadImages = loadImages;
        vm.getDemographicsALL();
        vm.getlistReportFile();

        $scope.$watch('openreport', function () {
          if ($scope.openreport) {
            vm.loading = true;
            vm.order = $scope.order;
            vm.listTest = $scope.tests;
            vm.getOrder();
            $scope.openreport = false;
          }

        });
        function getOrder() {
          vm.listTestComplete = [];
          for (var prop in vm.listTest) {
            vm.listTestComplete = vm.listTestComplete.concat(vm.listTest[prop]);
          }
          var auth = localStorageService.get('Enterprise_NT.authorizationData');
          var json = {
            'rangeType': 1,
            'init': vm.order,
            'end': vm.order
          };

          return reportsDS.getOrderHeader(auth.authToken, json).then(function (data) {
            if (data.data.length > 0) {
              var data = {
                'printOrder': [{
                  'physician': null,
                  'listOrders': [{
                    order: data.data[0]
                  }]
                }],
                'typeReport': 2,
                'isAttached': true
              };

              return reportsDS.getOrderPreliminary(auth.authToken, data).then(function (data) {
                if (data.data !== '') {
                  if (!vm.preliminaryComplete) {
                    data.data.resultTest = _.intersectionBy(data.data.resultTest, vm.listTestComplete, 'testId');
                  }
                  var listechnique = [];
                  if (data.data.resultTest.length !== 0) {
                    data.data.resultTest.forEach(function (value) {
                      value.technique = value.technique === undefined ? '' : value.technique;
                      if (value.profileId === 0) {
                        value.viewvalidationUser = true;
                        value.viewtechnique = true;
                        value.techniqueprofile = value.technique
                      } else {
                        var filtertecnique = $filter("filter")(JSON.parse(JSON.stringify(listechnique)), function (e) {
                          return e.profileId === value.profileId;
                        })
                        if (filtertecnique.length !== 0) {
                          if (value.technique === filtertecnique[0].technique) {
                            value.viewtechnique = false;
                          } else {
                            value.viewtechnique = true;
                          }
                          value.techniqueprofile = filtertecnique[0].technique;
                        } else {
                          var dataperfil = $filter("filter")(data.data.resultTest, function (e) {
                            return e.profileId === value.profileId;
                          })
                          var dataperfil = _.orderBy(dataperfil, 'printSort', 'asc');
                          if (dataperfil.length !== 0) {
                            dataperfil.forEach(function (value, key) {
                              if (dataperfil[key].validationUserId != undefined) {
                                if (key === dataperfil.length - 1) {
                                  value.viewvalidationUser = true;
                                } else if (dataperfil[key].validationUserId !== dataperfil[key + 1].validationUserId) {
                                  value.viewvalidationUser = true;
                                } else {
                                  value.viewvalidationUser = false;
                                }
                              } else {
                                value.viewvalidationUser = false;
                              }
                            });
                          }
                          var find = _.map(dataperfil, 'technique').reduce(function (acc, curr) {
                            if (typeof acc[curr] == 'undefined') {
                              acc[curr] = 1;
                            } else {
                              acc[curr] += 1;
                            } return acc;
                          }, {})
                          var find2 = []
                          for (var propiedad in find) {
                            var object = {
                              technique: [propiedad][0],
                              occurrence: find[propiedad]
                            };
                            find2.add(object);
                          }
                          var find = _.orderBy(find2, 'occurrence', 'desc');
                          if (find[0].technique === value.technique) {
                            value.viewtechnique = false;
                          } else {
                            value.viewtechnique = true;
                          }
                          value.techniqueprofile = find[0].technique;
                          var resulttechnique = {
                            "profileId": value.profileId,
                            "technique": value.techniqueprofile,
                          }
                          listechnique.add(resulttechnique);
                        }
                      }
                      value.refMin = value.refMin === null || value.refMin === '' || value.refMin === undefined ? 0 : value.refMin;
                      value.refMax = value.refMax === null || value.refMax === '' || value.refMax === undefined ? 0 : value.refMax;
                      value.panicMin = value.panicMin === null || value.panicMin === '' || value.panicMin === undefined ? 0 : value.panicMin;
                      value.panicMax = value.panicMax === null || value.panicMax === '' || value.panicMax === undefined ? 0 : value.panicMax;
                      value.reportedMin = value.reportedMin === null || value.reportedMin === '' || value.reportedMin === undefined ? 0 : value.reportedMin;
                      value.reportedMax = value.reportedMax === null || value.reportedMax === '' || value.reportedMax === undefined ? 0 : value.reportedMax;
                      value.digits = value.digits === null || value.digits === '' || value.digits === undefined ? 0 : value.digits;
                      value.refMinview = (parseFloat(value.refMin).toFixed(value.digits)).toString();
                      value.refMaxview = (parseFloat(value.refMax).toFixed(value.digits)).toString();
                      value.panicMinview = parseFloat(value.panicMin).toFixed(value.digits);
                      value.panicMaxview = parseFloat(value.panicMax).toFixed(value.digits);
                      value.reportedMinview = parseFloat(value.reportedMin).toFixed(value.digits);
                      value.reportedMaxview = parseFloat(value.reportedMax).toFixed(value.digits);
                    });
                  }
                  var dataOrder = data.data;
                  if (dataOrder.allDemographics.length > 0) {
                    dataOrder.allDemographics.forEach(function (value2) {
                      dataOrder['demo_' + value2.idDemographic + '_name'] = value2.demographic;
                      dataOrder['demo_' + value2.idDemographic + '_value'] = value2.encoded === false ? value2.notCodifiedValue : value2.codifiedName;
                    });
                  }

                  if (dataOrder.comments.length > 0) {
                    dataOrder.comments.forEach(function (value) {
                      try {
                        var comment = JSON.parse(value.comment);

                        comment = comment.content;
                        value.comment = comment.substring(1, comment.length - 1)
                      }
                      catch (e) {
                        value.comment = value.comment;
                      }
                    });
                  }


                  dataOrder.createdDate = moment(dataOrder.createdDate).format(vm.formatDate + ' hh:mm:ss a.');
                  dataOrder.patient.birthday = moment(dataOrder.patient.birthday).format(vm.formatDate);
                  dataOrder.patient.age = common.getAgeAsString(dataOrder.patient.birthday, vm.formatDate);


                  return reportsDS.getUserValidate(vm.order).then(function (datafirm) {

                    dataOrder.listfirm = [];
                    for (var i = 0; i < dataOrder.resultTest.length; i++) {
                      dataOrder.resultTest[i].resultDate = moment(dataOrder.resultTest[i].resultDate).format(vm.formatDate + ' hh:mm:ss a.');
                      dataOrder.resultTest[i].validationDate = dataOrder.resultTest[i].validationDate !== null ? moment(dataOrder.resultTest[i].validationDate).format(vm.formatDate + ' hh:mm:ss a.') : null;
                      dataOrder.resultTest[i].entryDate = moment(dataOrder.resultTest[i].entryDate).format(vm.formatDate + ' hh:mm:ss a.');
                      dataOrder.resultTest[i].takenDate = moment(dataOrder.resultTest[i].takenDate).format(vm.formatDate + ' hh:mm:ss a.');
                      dataOrder.resultTest[i].verificationDate = moment(dataOrder.resultTest[i].verificationDate).format(vm.formatDate + ' hh:mm:ss a.');
                      dataOrder.resultTest[i].printDate = moment(dataOrder.resultTest[i].printDate).format(vm.formatDate + ' hh:mm:ss a.');

                      if (dataOrder.resultTest[i].microbiologyGrowth !== undefined) {
                        dataOrder.resultTest[i].microbiologyGrowth.lastTransaction = moment(dataOrder.resultTest[i].microbiologyGrowth.lastTransaction).format(vm.formatDate + ' hh:mm:ss a.');
                      }

                      if(dataOrder.resultTest[i].commentResult === undefined || dataOrder.resultTest[i].commentResult === null) {
                        dataOrder.resultTest[i].commentResult = "";
                      }

                      if(dataOrder.resultTest[i].profileNameEnglish === undefined || dataOrder.resultTest[i].profileNameEnglish === null) {
                        dataOrder.resultTest[i].profileNameEnglish = "NO DATA LANGUAGE";
                      }

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
                            'firm': user.length === 0 ? "" : user[0].photo
                          };
                          dataOrder.listfirm.push(firm);
                        }

                      }
                    }
                    dataOrder.resultTest = _.orderBy(dataOrder.resultTest, ['printSort'], ['asc']);
                    vm.printOrder(dataOrder);
                  })
                } else {
                  UIkit.modal('#logNoDataPreliminary').show();
                  vm.loading = false;
                }
              });

            } else {
              UIkit.modal('#logNoDataPreliminary').show();
              vm.loading = false;
            }
          });

        }

        function printOrder(order) {
          var auth = localStorageService.get('Enterprise_NT.authorizationData');
          var parameterReport = {};

          parameterReport.variables = {
            'username': auth.userName,
            'titleReport': $filter('translate')('0395'),
            'date': moment().format(vm.formatDate + ' hh:mm:ss a.'),
            'formatDate': vm.formatDate,
            'reportOrder': true,
            'destination': "2",
            'typeprint': "4",
            'codeorder': "/orqrm:" + btoa(vm.order)
          };
          order.languageReport = localStorageService.get('IdiomaReporteResultados');
          var labelsreport = JSON.stringify($translate.getTranslationTable());
          labelsreport = JSON.parse(labelsreport);
          parameterReport.pathreport = '/Report/reportsandconsultations/reports/' + vm.getTemplateReport(order);
          parameterReport.labelsreport = labelsreport;
          vm.setReport(parameterReport, order);
        }


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
                if (vm.preliminaryAttachment === 'True' && attachments.length > 0) {
                  var mergepdfs = '';
                  var calcula = 0;

                  var pdfs = _.filter(attachments, function (o) { return o.extension === 'pdf'; });
                  var images = _.filter(attachments, function (o) { return o.extension !== 'pdf'; });

                  if (pdfs.length > 0) {
                    reportsDS.mergepdf(pdfs).then(function (response) {
                      if (response.status === 200) {
                        var reportbasee64 = _base64ToArrayBuffer(response.data);
                        mergepdfs = PDFDocument.load(reportbasee64, {
                          ignoreEncryption: true
                        });
                        mergepdfs.then(function (listbufferelement) {
                          var copiedPagesRes = pdfDoc.copyPages(listbufferelement, listbufferelement.getPageIndices());
                          copiedPagesRes.then(function (copiedPages) {
                            copiedPages.forEach(function (page) {
                              pdfDoc.addPage(page);
                            });
                            if (pdfs.length === 1) {
                              var totalpages = pdfDoc.getPages().length;
                              pdfDoc.removePage(totalpages - 1);
                            }
                            if (images.length > 0) {
                              vm.loadImages(images, calcula, pdfDoc);
                            } else {
                              pdfDoc.save().then(function (pdf) {
                                var pdfUrl = URL.createObjectURL(new Blob([pdf], {
                                  type: 'application/pdf'
                                }));
                                vm.loading = false;
                                window.open(pdfUrl, '_blank');
                              });
                            }
                          });
                        });
                      }
                    });
                  } else if (images.length > 0) {
                    vm.loadImages(images, calcula, pdfDoc);
                  }
                } else {
                  pdfDoc.save().then(function (pdf) {
                    var pdfUrl = URL.createObjectURL(new Blob([pdf], {
                      type: 'application/pdf'
                    }));
                    vm.loading = false;
                    window.open(pdfUrl, '_blank');
                  });
                }

              });
            }, function (reason) {
              alert('Failed: ' + reason);
            });
          });
        }

        function loadImages(images, calcula, pdfDoc) {
          for (var i = 0; i < images.length; i++) {
            var reportbasee64 = _base64ToArrayBuffer(images[i].file);
            if (images[i].extension === 'jpg' || images[i].extension === 'jpeg') {
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
                if (calcula === images.length) {
                  pdfDoc.save().then(function (pdf) {
                    var pdfUrl = URL.createObjectURL(new Blob([pdf], {
                      type: 'application/pdf'
                    }));
                    vm.loading = false;
                    window.open(pdfUrl, '_blank');
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
                  y: page.getHeight() / 2 - pngDims.height / 2,
                  width: pngDims.width,
                  height: pngDims.height,
                });
                calcula++;
                if (calcula === images.length) {
                  pdfDoc.save().then(function (pdf) {
                    var pdfUrl = URL.createObjectURL(new Blob([pdf], {
                      type: 'application/pdf'
                    }));
                    vm.loading = false;
                    window.open(pdfUrl, '_blank');
                  });
                }
              });
            }
          }
        }

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
                // Export to PDF (#1)
                var data = stream.toArray();
                var buffer = new Uint8Array(data);

                vm.copyPages(buffer, datareport.attachments);

                // Export to PDF (#2)


              }, report, stream, settings);

              //  service.exportTo(report, stream, settings);

            });
          }, 50);
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

        function getTemplateReport(order) {
          var template = '';
          if (vm.demographicTemplate !== null) {
            if (vm.demographicTemplate.encoded && vm.demographicTemplate.id > 0) {
              order.demographicItemTemplate = _.filter(order.allDemographics, function (o) {
                return o.idDemographic === vm.demographicTemplate.id;
              })[0];
              template = vm.nameDemographic + '_' + order.demographicItemTemplate.codifiedCode + '.mrt';
            } else if (vm.demographicTemplate.encoded && vm.demographicTemplate.id === -7 || vm.demographicTemplate.encoded && vm.demographicTemplate.id === -10) {
              if (vm.demographicTemplate.id === -7) {
                order.demographicItemTemplate = order.patient.race;
                template = vm.nameDemographic + '_' + order.demographicItemTemplate.code + '.mrt';
              } else {
                order.demographicItemTemplate = order.patient.documentType;
                template = vm.nameDemographic + '_' + order.demographicItemTemplate.abbr + '.mrt';

              }
            } else if (vm.demographicTemplate.encoded && vm.demographicTemplate.id < 0) {
              order.demographicItemTemplate = order[vm.referenceDemographic];
              if (vm.AddTestEditreports && parseInt(vm.AddTestEditorbranch) === order.demographicItemTemplate.id) {
                if (order.resultTest.length !== 0) {
                  template = vm.nameDemographic + '_' + order.demographicItemTemplate.code + '.mrt';
                  order.resultTest.some(function (element) {
                    var demo = vm.nameDemographic + '_' + order.demographicItemTemplate.code + '_' + element.abbreviation + '.mrt';
                    if (_.filter(vm.listreports, function (o) { return o.name === demo }).length > 0) {
                      template = demo;
                      return true;
                    }
                    return false;
                  });
                } else {
                  template = vm.nameDemographic + '_' + order.demographicItemTemplate.code + '.mrt';
                }

              } else {
                template = vm.nameDemographic + '_' + order.demographicItemTemplate.code + '.mrt';
              }
            } else {
              template = vm.nameDemographic + '.mrt';
            }

            if (_.filter(vm.listreports, function (o) {
              return o.name === template
            }).length > 0) {
              return template;
            } else {
              return 'reports.mrt';
            }
          } else {
            return 'reports.mrt';
          }
        }

        //Método que devuelve la lista de demofgráficos
        function getDemographicsALL() {
          var auth = localStorageService.get('Enterprise_NT.authorizationData');
          if (parseInt(vm.demographicTitle) !== 0) {
            return demographicDS.getDemographicsALL(auth.authToken).then(function (data) {
              vm.demographicTemplate = _.filter(data.data, function (v) {
                return v.id === parseInt(vm.demographicTitle);
              })[0];
              vm.nameDemographic = 'reports_' + vm.demographicTemplate.name;
              vm.referenceDemographic = vm.demographicTemplate.name;

              if (parseInt(vm.demographicTitle) < 0) {
                switch (parseInt(vm.demographicTitle)) {
                  case -1:
                    vm.demographicTemplate.name = $filter('translate')('0085');
                    vm.referenceDemographic = 'account';
                    break; //Cliente
                  case -2:
                    vm.demographicTemplate.name = $filter('translate')('0086');
                    vm.referenceDemographic = 'physician';
                    break; //Médico
                  case -3:
                    vm.demographicTemplate.name = $filter('translate')('0087');
                    vm.referenceDemographic = 'rate';
                    break; //Tarifa
                  case -4:
                    vm.demographicTemplate.name = $filter('translate')('0088');
                    vm.referenceDemographic = 'type';
                    break; //Tipo de orden
                  case -5:
                    vm.demographicTemplate.name = $filter('translate')('0003');
                    vm.referenceDemographic = 'branch';
                    break; //Sede
                  case -6:
                    vm.demographicTemplate.name = $filter('translate')('0090');
                    vm.referenceDemographic = 'service';
                    break; //Servicio
                  case -7:
                    vm.demographicTemplate.name = $filter('translate')('0091');
                    vm.referenceDemographic = 'race';
                    break; //Raza
                }
                vm.nameDemographic = 'reports_' + vm.demographicTemplate.name;
              }

            }, function (error) {
              vm.modalError();
            });
          } else {
            vm.demographicTemplate = null;
            vm.nameDemographic = 'reports';
          }
        }

        function getlistReportFile() {
          reportadicional.getlistReportFile().then(function (response) {
            if (response.status === 200) {
              vm.listreports = response.data;
            } else {
              vm.listreports = [];
            }
          }, function (error) {
            return false;
          });
        }

      }],
      controllerAs: 'vmd'
    };
    return directive;
  }
})();
/* jshint ignore:end */
