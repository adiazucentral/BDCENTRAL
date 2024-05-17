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
        .directive('reportend', reportend)
        .factory('reportend', reportend);

    reportend.$inject = ['$filter', 'reportsDS', 'localStorageService',
        '$translate', 'demographicDS', 'reportadicional', 'common', '$rootScope', 'logger', '$state', 'commentDS'
    ];

    /* @ngInject */
    function reportend($filter, reportsDS, localStorageService,
        $translate, demographicDS, reportadicional, common, $rootScope, logger, $state, commentDS) {
        var directive = {
            restrict: 'EA',
            templateUrl: 'app/widgets/userControl/results-reportend.html',
            scope: {
                order: '=?order',
                openreport: '=?openreport',
                filterarea: '=?filterarea',
                area: '=?area',
                dataprint: '=?dataprint',
                filteritems: '=?filteritems',
                sendautomaticemail: '=?sendautomaticemail',
                receiveresult: '=?receiveresult',
                functionexecute: '=?functionexecute'
            },

            controller: ['$scope', function ($scope) {
                var vm = this;
                vm.formatDate = localStorageService.get('FormatoFecha').toUpperCase();
                vm.demographicTitle = localStorageService.get('DemograficoTituloInforme');
                vm.namePdfConfig = localStorageService.get('GenerarPDFCon');
                vm.AddTestEditreports = localStorageService.get('AgregaPruebaEditoreportes') === 'True';
                vm.AddTestEditorArea = localStorageService.get('AgregaPruebaEditorArea');
                vm.AddTestEditorbranch = localStorageService.get('AgregaPruebaEditorDemo');


                vm.EmailBody = localStorageService.get('EmailBody');
                vm.EmailSubjectPatient = localStorageService.get('EmailSubjectPatient');
                vm.EmailSubjectPhysician = localStorageService.get('EmailSubjectPhysician');

                vm.UrlNodeJs = localStorageService.get('UrlNodeJs');
                vm.printOrder = printOrder;
                vm.getTemplateReport = getTemplateReport;
                vm.getDemographicsALL = getDemographicsALL;
                vm.getlistReportFile = getlistReportFile;
                vm.modalError = modalError;
                vm.message = '';
                vm.addreport = addreport;
                vm.setReport = setReport;
                vm.copyPages = copyPages;
                vm.sendbuffer = sendbuffer;
                vm.getComment = getComment;
                vm.sendEmailInternal = sendEmailInternal;
                vm.changestatetestInternal = changestatetestInternal;
                vm.getDemographicsALL();
                vm.getlistReportFile();
                vm.getComment();
                vm.querySearch = querySearch;
                vm.pdfPatient = '';
                vm.pdfUrlPatient = '';
                vm.defaulttypeprint = '1';
                vm.areall = [];
                vm.arelist = [];
                vm.loadImages = loadImages;
                vm.isAuxPhysicians = localStorageService.get('MedicosAuxiliares') === 'True';
                vm.configprint = localStorageService.get('GuardarConfiguracionImpresion') === 'True';

                $scope.$watch('sendautomaticemail', function () {
                    if ($scope.sendautomaticemail) {
                        vm.order = $scope.order;
                        vm.dataprint = [];
                        var parameters = {
                            'typeprint': "1",
                            'attachments': true,
                            'printingMedium': "3",
                            'serial': $rootScope.serialprint,
                            'NumberCopies': 0,
                            'sendEmail': "1",
                            'quantityCopies': 1,
                            'destination': "3"
                        }
                        printOrder(parameters);

                        $scope.sendautomaticemail = false;

                    }
                });

                $scope.$watch('receiveresult', function () {
                    vm.receiveresult = $scope.receiveresult === 1 ? true : false;
                });

                $scope.$watch('order', function () {
                    if ($scope.order !== undefined && $scope.order !== 0 && $scope.order !== null && $scope.order !== '') {
                        vm.order = $scope.order;
                    }
                });

                $scope.$watch('openreport', function () {
                    if ($scope.openreport) {
                        vm.loading = true;
                        vm.order = $scope.order;
                        vm.idpage = $state.$current.self.controller === 'orderentryController';
                        vm.update = !vm.update;
                        vm.namereceiveresult = "";
                        if (vm.configprint) {
                            vm.getconfigPrint()
                        } else if ($scope.filteritems) {
                            vm.ListConfigPrint = '';
                            vm.Itemsdemo();
                        } else {
                            vm.ListConfigPrint = '';
                            vm.dataprint = $scope.filterarea === 1 ? $scope.dataprint : [];
                            vm.filterarea = $scope.filterarea === 1 && vm.dataprint.length === 0 ? true : false;
                            if (vm.filterarea) {
                                vm.areall = $filter('orderBy')($scope.area, 'name');
                                vm.arelist = JSON.parse(JSON.stringify(vm.areall));
                                var areapackage = {
                                    'id': 0,
                                    'name': ''
                                }
                                vm.arelist.push(areapackage);
                            }
                            $scope.openreport = false;
                            UIkit.modal('#modalfinalreport', { modal: false }).show()
                            $scope.openreport = false;
                            vm.loading = false;
                        }

                    }
                });

                vm.saveconfig = saveconfig;
                function saveconfig(parameters) {
                    parameters.arelist = vm.arelist;
                    parameters.emailist = vm.emailist;
                    var order = {
                        orderNumber: vm.order,
                        configPrint: JSON.stringify(parameters)
                    }
                    var auth = localStorageService.get('Enterprise_NT.authorizationData');
                    return reportsDS.updateConfig(auth.authToken, order).then(function (data) {
                        if (data.status === 200) {
                            UIkit.modal('#modalfinalreport').hide();
                            logger.success($filter('translate')('0149'));
                        }
                    }, function (error) {
                        vm.modalError(error);
                    });
                }

                //metodo para consultar una lista de cometarios
                function getComment() {
                    var auth = localStorageService.get('Enterprise_NT.authorizationData');
                    return commentDS.getComment(auth.authToken).then(function (data) {
                        vm.loadingdata = false;
                        vm.listComment = data.data;
                    }, function (error) {
                        vm.modalError(error);
                    });
                }

                vm.getconfigPrint = getconfigPrint;
                function getconfigPrint() {
                    var auth = localStorageService.get('Enterprise_NT.authorizationData');
                    return reportsDS.getConfig(auth.authToken, vm.order).then(function (data) {
                        if (data.status === 200) {
                            if (data.data.configPrint === undefined) {
                                vm.getItemsdemo();
                                vm.ListConfigPrint = '';
                            } else if (data.data.configPrint === '' || data.data.configPrint === null) {
                                vm.getItemsdemo();
                                vm.ListConfigPrint = '';
                            } else {
                                vm.ListConfigPrint = JSON.parse(data.data.configPrint);
                                vm.arelist = vm.ListConfigPrint.arelist;
                                vm.emailistConfig = vm.ListConfigPrint.emailist.length === 0 ? [] : JSON.parse(JSON.stringify(vm.ListConfigPrint.emailist));
                                vm.getItemsdemo();
                            }
                        }
                    }, function (error) {
                        vm.modalError(error);
                    });
                }


                vm.Itemsdemo = Itemsdemo;
                function Itemsdemo() {
                    vm.filteritems = false;
                    var auth = localStorageService.get('Enterprise_NT.authorizationData');
                    return reportsDS.getdemo(auth.authToken, vm.order).then(function (data) {
                        if (data.status === 200) {
                            vm.dataprint = $scope.filterarea === 1 ? $scope.dataprint : [];
                            vm.filterarea = $scope.filterarea === 1 && vm.dataprint.length === 0 ? true : false;
                            if (vm.filterarea) {
                                vm.areall = $filter('orderBy')($scope.area, 'name');
                                vm.arelist = JSON.parse(JSON.stringify(vm.areall));
                                var areapackage = {
                                    'id': 0,
                                    'name': ''
                                }
                                vm.arelist.push(areapackage);
                            }
                            $scope.openreport = false;
                            UIkit.modal('#modalfinalreport', { modal: false }).show()
                            $scope.openreport = false;
                            var email = _.concat(data.data.demographics, data.data.patient.demographics);
                            if (data.data.auxiliaryPhysicians !== undefined) {
                                if (vm.isAuxPhysicians && data.data.auxiliaryPhysicians.length !== 0) {
                                    var indexes = [-201, -202, -203, -204, -205];
                                    for (var t = 0; t < data.data.auxiliaryPhysicians.length; t++) {
                                        if (data.data.auxiliaryPhysicians[t].idDemoAux === undefined || data.data.auxiliaryPhysicians[t].idDemoAux === 0 || data.data.auxiliaryPhysicians[t].idDemoAux === null || data.data.auxiliaryPhysicians[t].idDemoAux === '') {
                                            data.data.auxiliaryPhysicians[t].idDemographic = indexes[t];
                                            data.data.auxiliaryPhysicians[t].idDemoAux = indexes[t];
                                        } else {
                                            data.data.auxiliaryPhysicians[t].idDemographic = data.data.auxiliaryPhysicians[t].idDemoAux;
                                        }
                                    }
                                    var email = _.concat(email, data.data.auxiliaryPhysicians);
                                }
                            }
                            var data = [];
                            var dataList = [];
                            if (email !== undefined) {
                                if (email.length !== 0) {
                                    email.forEach(function (element, index) {
                                        if (element.email !== undefined && element.email !== '' && element.email !== null) {
                                            if (element.idDemoAux === -201) {
                                                element.demographic = $filter('translate')('0086') + ' 1';
                                            }
                                            if (element.idDemoAux === -202) {
                                                element.demographic = $filter('translate')('0086') + ' 2';
                                            }
                                            if (element.idDemoAux === -203) {
                                                element.demographic = $filter('translate')('0086') + ' 3';
                                            }
                                            if (element.idDemoAux === -204) {
                                                element.demographic = $filter('translate')('0086') + ' 4';
                                            }
                                            if (element.idDemoAux === -205) {
                                                element.demographic = $filter('translate')('0086') + ' 5';
                                            }
                                            if (element.demographic === "ORDERTYPE") {
                                                element.demographic = $filter('translate')('0088');
                                            }
                                            if (element.demographic === "SERVICE") {
                                                element.demographic = $filter('translate')('0090');
                                            }
                                            if (element.demographic === "ACOUNT") {
                                                element.demographic = $filter('translate')('0085');
                                            }
                                            if (element.demographic === "RATE") {
                                                element.demographic = $filter('translate')('0087');
                                            }
                                            if (element.demographic === "RACE") {
                                                element.demographic = $filter('translate')('0091');
                                            }
                                            if (element.demographic === "BRANCH") {
                                                element.demographic = $filter('translate')('0003');
                                            }
                                            if (element.demographic === "DOCUMENTYPE") {
                                                element.demographic = $filter('translate')('0233');
                                            }
                                            if (element.demographic !== $filter('translate')('0085')) {
                                                dataList.add(element);
                                            }
                                            data.add(element);
                                        }
                                    });
                                }
                            }
                            vm.emailist = dataList;
                            vm.loading = false;
                        } else {
                            vm.loading = false;
                        }
                    }, function (error) {
                        vm.modalError(error);
                    });
                }

                vm.getItemsdemo = getItemsdemo;
                function getItemsdemo() {
                    vm.filteritems = false;
                    vm.emailist = [];
                    vm.emailall = [];
                    var auth = localStorageService.get('Enterprise_NT.authorizationData');
                    return reportsDS.getdemo(auth.authToken, vm.order).then(function (data) {
                        if (data.status === 200) {
                            vm.dataprint = $scope.filterarea === 1 ? $scope.dataprint : [];
                            vm.filterarea = $scope.filterarea === 1 && vm.dataprint.length === 0 ? true : false;
                            if (vm.filterarea) {
                                vm.areall = $filter('orderBy')($scope.area, 'name');
                                if (vm.ListConfigPrint.arelist === undefined) {
                                    vm.arelist = JSON.parse(JSON.stringify(vm.areall));
                                    var areapackage = {
                                        'id': 0,
                                        'name': ''
                                    }
                                    vm.arelist.push(areapackage);
                                } else if (vm.ListConfigPrint.arelist.length === 0) {
                                    vm.arelist = JSON.parse(JSON.stringify(vm.areall));
                                    var areapackage = {
                                        'id': 0,
                                        'name': ''
                                    }
                                    vm.arelist.push(areapackage);
                                } else {
                                    vm.arelist = vm.ListConfigPrint.arelist;
                                }
                            }
                            $scope.openreport = false;
                            UIkit.modal('#modalfinalreport', { modal: false }).show();
                            var email = _.concat(data.data.demographics, data.data.patient.demographics);
                            if (data.data.auxiliaryPhysicians !== undefined) {
                                if (vm.isAuxPhysicians && data.data.auxiliaryPhysicians.length !== 0) {
                                    var indexes = [-201, -202, -203, -204, -205];
                                    for (var t = 0; t < data.data.auxiliaryPhysicians.length; t++) {
                                        if (data.data.auxiliaryPhysicians[t].idDemoAux === undefined || data.data.auxiliaryPhysicians[t].idDemoAux === 0 || data.data.auxiliaryPhysicians[t].idDemoAux === null || data.data.auxiliaryPhysicians[t].idDemoAux === '') {
                                            data.data.auxiliaryPhysicians[t].idDemographic = indexes[t];
                                            data.data.auxiliaryPhysicians[t].idDemoAux = indexes[t];
                                        } else {
                                            data.data.auxiliaryPhysicians[t].idDemographic = data.data.auxiliaryPhysicians[t].idDemoAux;
                                        }
                                    }
                                    var email = _.concat(email, data.data.auxiliaryPhysicians);
                                }
                            }
                            var data = [];
                            var dataList = [];
                            if (email !== undefined) {
                                if (email.length !== 0) {
                                    email.forEach(function (element, index) {
                                        if (element.email !== undefined && element.email !== '' && element.email !== null) {
                                            if (element.idDemoAux === -201) {
                                                element.demographic = $filter('translate')('0086') + ' 1';
                                            }
                                            if (element.idDemoAux === -202) {
                                                element.demographic = $filter('translate')('0086') + ' 2';
                                            }
                                            if (element.idDemoAux === -203) {
                                                element.demographic = $filter('translate')('0086') + ' 3';
                                            }
                                            if (element.idDemoAux === -204) {
                                                element.demographic = $filter('translate')('0086') + ' 4';
                                            }
                                            if (element.idDemoAux === -205) {
                                                element.demographic = $filter('translate')('0086') + ' 5';
                                            }
                                            if (element.demographic === "ORDERTYPE") {
                                                element.demographic = $filter('translate')('0088');
                                            }
                                            if (element.demographic === "SERVICE") {
                                                element.demographic = $filter('translate')('0090');
                                            }
                                            if (element.demographic === "ACOUNT") {
                                                element.demographic = $filter('translate')('0085');
                                            }
                                            if (element.demographic === "RATE") {
                                                element.demographic = $filter('translate')('0087');
                                            }
                                            if (element.demographic === "RACE") {
                                                element.demographic = $filter('translate')('0091');
                                            }
                                            if (element.demographic === "BRANCH") {
                                                element.demographic = $filter('translate')('0003');
                                            }
                                            if (element.demographic === "DOCUMENTYPE") {
                                                element.demographic = $filter('translate')('0233');
                                            }
                                            if (element.demographic !== $filter('translate')('0085')) {
                                                dataList.add(element);
                                            }
                                            data.add(element);
                                        }
                                    });
                                }
                            }
                            if (data.length !== 0) {
                                vm.filteritems = true;
                                vm.emailall = data;
                                if (vm.ListConfigPrint.emailist === undefined) {
                                    vm.emailist = dataList;
                                } else if (vm.ListConfigPrint.emailist.length === 0) {
                                    vm.emailist = dataList;
                                } else {
                                    if (vm.ListConfigPrint.destination === '3') {
                                        vm.emailistConfig.forEach(function (item) {
                                            var emailsdataList = _.filter(JSON.parse(JSON.stringify(dataList)), function (o) {
                                                return o.idDemographic === item.idDemographic || o.demographic === item.demographic;
                                            })
                                            if (emailsdataList.length !== 0) {
                                                item.email = emailsdataList[0].email;
                                            }
                                        });
                                        vm.emailist = vm.emailistConfig;
                                    } else {
                                        vm.emailist = dataList;
                                    }
                                }
                            }
                            vm.loading = false;
                        } else {
                            vm.loading = false;
                        }
                    }, function (error) {
                        vm.modalError(error);
                    });
                }

                function querySearch(criteria) {
                    if (criteria === '?') {
                        return vm.areall;
                    } else {
                        if (vm.areall.length === vm.arelist.length) {
                            return [];
                        } else {
                            var search = $filter("filter")(vm.areall, function (e) {
                                var validated = $filter("filter")(vm.arelist, function (f) {
                                    return f.id === e.id;
                                })
                                return e.name.indexOf(criteria.toLowerCase()) !== -1 && validated.length === 0 || e.name.indexOf(criteria.toUpperCase()) !== -1 && validated.length === 0;
                            });
                            return criteria ? search : [];
                        }
                    }

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
                                    return f.idDemographic === e.idDemographic;
                                })
                                return e.demographic.indexOf(criteria.toLowerCase()) !== -1 && validated.length === 0 || e.demographic.indexOf(criteria.toUpperCase()) !== -1 && validated.length === 0;
                            });
                            return criteria ? search : [];
                        }
                    }

                }

                function printOrder(parameters) {
                    var auth = localStorageService.get('Enterprise_NT.authorizationData');
                    vm.parameter = parameters;
                    vm.loading = true;
                    UIkit.modal('#modalfinalreport').hide();
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
                                'typeReport': vm.parameter.typeprint,
                                'isAttached': vm.parameter.attachments,
                                'typeNameFile': vm.namePdfConfig
                            };
                            //getOrderPreliminary
                            return reportsDS.getOrderPreliminaryend(auth.authToken, data).then(function (data) {
                                if (data.data !== '') {
                                    vm.datareport = data.data.listOrders[0];
                                    var dataOrder = data.data.listOrders[0].order;
                                    dataOrder.languageReport = parameters.language == "1" ? "es" : "en";

                                    var listechnique = [];
                                    if (dataOrder.resultTest.length > 0) {
                                        dataOrder.resultTest.forEach(function (value) {
                                            value.id = value.testId;
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
                                                    var dataperfil = $filter("filter")(dataOrder.resultTest, function (e) {
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
                                            value.refMinview = parseFloat(value.refMin).toFixed(value.digits);
                                            value.refMaxview = parseFloat(value.refMax).toFixed(value.digits);
                                            value.panicMinview = parseFloat(value.panicMin).toFixed(value.digits);
                                            value.panicMaxview = parseFloat(value.panicMax).toFixed(value.digits);
                                            value.reportedMinview = parseFloat(value.reportedMin).toFixed(value.digits);
                                            value.reportedMaxview = parseFloat(value.reportedMax).toFixed(value.digits);

                                            if (dataOrder.languageReport == 'en' && value.resultComment != undefined) {
                                                if (value.resultComment.comment != null) {
                                                    var findfirm = _.filter(vm.listComment, function (o) {
                                                        return o.message.replace(' ', '') === value.resultComment.comment.replace(' ', '');
                                                    });
                                                    if (findfirm.length > 0) {
                                                        value.resultComment.comment = findfirm[0].messageEnglish;
                                                    }
                                                }

                                            }
                                            if (dataOrder.languageReport == 'en' && value.commentResult != undefined) {

                                                var findfirm = _.filter(vm.listComment, function (o) {
                                                    return o.message.replace(' ', '') === value.commentResult.replace(' ', '');
                                                });

                                                if (findfirm.length > 0) {
                                                    value.commentResult = findfirm[0].messageEnglish;
                                                }

                                            }

                                        });

                                        if (dataOrder.allDemographics.length > 0) {
                                            dataOrder.allDemographics.forEach(function (value2) {
                                                dataOrder['demo_' + value2.idDemographic + '_name'] = value2.demographic;
                                                //dataOrder['demo_' + value2.idDemographic + '_value'] = value2.encoded === false ? value2.notCodifiedValue : value2.codifiedName;
                                                dataOrder['demo_' + value2.idDemographic + '_value'] = value2.encoded === false ? value2.notCodifiedValue : dataOrder.languageReport == 'es' ? value2.codifiedName : (value2.codifiedNameEnglish == null || value2.codifiedNameEnglish == undefined ? "NO DATA LANGUAGE" : value2.codifiedNameEnglish);
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
                                        dataOrder.attachments = vm.datareport.attachments === undefined ? [] : vm.datareport.attachments;


                                        return reportsDS.getUserValidate(vm.order).then(function (datafirm) {
                                            dataOrder.listfirm = [];
                                            for (var i = 0; i < dataOrder.resultTest.length; i++) {
                                                dataOrder.resultTest[i].resultDate = moment(dataOrder.resultTest[i].resultDate).format(vm.formatDate + ' hh:mm:ss a.');

                                                dataOrder.resultTest[i].validationDate = moment(dataOrder.resultTest[i].validationDate).format(vm.formatDate + ' hh:mm:ss a.');
                                                dataOrder.resultTest[i].entryDate = moment(dataOrder.resultTest[i].entryDate).format(vm.formatDate + ' hh:mm:ss a.');
                                                dataOrder.resultTest[i].takenDate = moment(dataOrder.resultTest[i].takenDate).format(vm.formatDate + ' hh:mm:ss a.');
                                                dataOrder.resultTest[i].verificationDate = moment(dataOrder.resultTest[i].verificationDate).format(vm.formatDate + ' hh:mm:ss a.');
                                                dataOrder.resultTest[i].printDate = moment(dataOrder.resultTest[i].printDate).format(vm.formatDate + ' hh:mm:ss a.');

                                                if (dataOrder.resultTest[i].microbiologyGrowth !== undefined) {
                                                    dataOrder.resultTest[i].microbiologyGrowth.lastTransaction = moment(dataOrder.resultTest[i].microbiologyGrowth.lastTransaction).format(vm.formatDate + ' hh:mm:ss a.');
                                                }
                                                if (dataOrder.resultTest[i].hasAntibiogram) {
                                                    dataOrder.resultTest[i].antibiogram = dataOrder.resultTest[i].microbialDetection.microorganisms;
                                                }
                                                if (dataOrder.resultTest[i].commentResult === undefined || dataOrder.resultTest[i].commentResult === null) {
                                                    dataOrder.resultTest[i].commentResult = "";
                                                }

                                                if (dataOrder.resultTest[i].profileNameEnglish === undefined || dataOrder.resultTest[i].profileNameEnglish === null) {
                                                    dataOrder.resultTest[i].profileNameEnglish = "NO DATA LANGUAGE";
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

                                            var dataReportPatient = null;

                                            if (auth.confidential === true && vm.parameter.destination === '3' && vm.parameter.sendEmail === "3") {
                                                var dataReportPatient = JSON.parse(JSON.stringify(dataOrder))
                                                dataReportPatient.resultTest = _.filter(dataReportPatient.resultTest, function (o) {
                                                    return !o.confidential;
                                                });

                                                if (dataReportPatient.resultTest.length === 0 || dataReportPatient.resultTest.length === dataOrder.resultTest.length) {
                                                    vm.addreport(dataOrder, null);
                                                }
                                                else {
                                                    dataOrder.resultTest = _.orderBy(dataOrder.resultTest, ['printSort'], ['asc']);
                                                    dataReportPatient.resultTest = _.orderBy(dataReportPatient.resultTest, ['printSort'], ['asc']);
                                                    vm.addreport(dataOrder, dataReportPatient);
                                                }
                                            }
                                            else if (auth.confidential === true && vm.parameter.destination === '3' && vm.parameter.sendEmail === "1") {
                                                dataOrder.resultTest = _.filter(dataOrder.resultTest, function (o) {
                                                    return !o.confidential;
                                                });

                                                if (dataOrder.resultTest.length === 0) {
                                                    vm.message = $filter('translate')('0152');
                                                    UIkit.modal('#logNoDataFinal').show();
                                                    vm.loading = false;
                                                }
                                                else {
                                                    dataOrder.resultTest = _.orderBy(dataOrder.resultTest, ['printSort'], ['asc']);
                                                    vm.addreport(dataOrder, null);
                                                }
                                            }
                                            else {
                                                dataOrder.resultTest = _.orderBy(dataOrder.resultTest, ['printSort'], ['asc']);
                                                vm.addreport(dataOrder, dataReportPatient);
                                            }

                                        })
                                    }
                                    else {
                                        vm.message = $filter('translate')('0152');
                                        UIkit.modal('#logNoDataFinal').show();
                                        vm.loading = false;
                                    }
                                } else {
                                    vm.message = $filter('translate')('0152');
                                    UIkit.modal('#logNoDataFinal').show();
                                    vm.loading = false;
                                }
                            });


                        } else {
                            vm.message = $filter('translate')('0152');
                            UIkit.modal('#logNoDataFinal').show();
                            vm.loading = false;
                        }
                    });
                }

                function addreport(order, orderpatient) {
                    if (order.resultTest.length > 0) {
                        if (vm.dataprint.length !== 0) {
                            var test = [];
                            vm.dataprint.forEach(function (value) {
                                var validated = $filter("filter")(JSON.parse(JSON.stringify(order.resultTest)), function (e) {
                                    return value.testId === e.testId;
                                })
                                if (validated.length !== 0) {
                                    test.add(validated[0]);
                                }
                            });
                            order.resultTest = test;

                        } else if (order.resultTest.length !== 0) {
                            if (vm.arelist.length > 0) {
                                var test = []
                                order.resultTest.forEach(function (value) {
                                    var validated = $filter("filter")(JSON.parse(JSON.stringify(vm.arelist)), function (e) {
                                        return value.areaId === e.id;
                                    })
                                    if (validated.length !== 0) {
                                        test.add(value);
                                    }
                                });
                                order.resultTest = test;
                            }
                        }

                        if (orderpatient !== null) {
                            if (vm.dataprint.length !== 0) {
                                var test = [];
                                vm.dataprint.forEach(function (value) {
                                    var validated = $filter("filter")(JSON.parse(JSON.stringify(orderpatient.resultTest)), function (e) {
                                        return value.testId === e.testId;
                                    })
                                    if (validated.length !== 0) {
                                        test.add(validated[0]);
                                    }
                                });
                                orderpatient.resultTest = test;

                            } else if (orderpatient.resultTest.length !== 0) {
                                if (vm.arelist.length > 0) {
                                    var test = []
                                    orderpatient.resultTest.forEach(function (value) {
                                        var validated = $filter("filter")(JSON.parse(JSON.stringify(vm.arelist)), function (e) {
                                            return value.areaId === e.id;
                                        })
                                        if (validated.length !== 0) {
                                            test.add(value);
                                        }
                                    });
                                    orderpatient.resultTest = test;
                                }
                            }

                        }


                        var auth = localStorageService.get('Enterprise_NT.authorizationData');
                        var parameterReport = {};
                        var titleReport = (vm.parameter.typeprint === '1' || vm.parameter.typeprint === '0') ? $filter('translate')('0399') : (vm.parameter.typeprint === '3' ? $filter('translate')('1065') : $filter('translate')('1066'));

                        var textpending = '';
                        if (vm.parameter.language == "1" && vm.datareport.listTestPending.length > 0) {
                            textpending = 'Pruebas pendientes por reportar';
                        }
                        else if (vm.parameter.language == "1" && vm.datareport.listTestPending.length == 0) {
                            textpending = 'Esta orden no tiene pruebas pendientes por reportar';
                        }
                        else if (vm.parameter.language != "1" && vm.datareport.listTestPending.length > 0) {
                            textpending = 'Pending tests to report';
                        }
                        else if (vm.parameter.language != "1" && vm.datareport.listTestPending.length == 0) {
                            textpending = 'This order has no pending Test to report';
                        }

                        var testpendingtext = ""
                        vm.datareport.listTestPending.forEach(function (t) {
                            testpendingtext = testpendingtext + " " + (order.languageReport == "en" ? t.nameTestEnglish : t.name + ".");
                        });

                        parameterReport.variables = {
                            'entity': vm.nameCustomer,
                            'abbreviation': vm.abbrCustomer,
                            'username': auth.userName,
                            'titleReport': titleReport,
                            'date': moment().format(vm.formatDate + ' hh:mm:ss a.'),
                            'formatDate': vm.formatDate,
                            'destination': vm.parameter.destination,
                            'codeorder': "/orqrm:" + btoa(vm.order),
                            'typeprint': vm.parameter.typeprint,
                            'stamp': vm.parameter.stamp === null || vm.parameter.stamp === undefined ? false : vm.parameter.stamp,
                            'testpending': vm.datareport.listTestPending.length > 0,
                            'textpending': textpending,
                            'testpendingtext': testpendingtext
                        };

                        var labelsreport = JSON.stringify($translate.getTranslationTable());
                        labelsreport = JSON.parse(labelsreport);


                        parameterReport.pathreport = '/Report/reportsandconsultations/reports/' + vm.getTemplateReport(order);
                        parameterReport.labelsreport = labelsreport;
                        vm.dataorder = order;
                        vm.datapatient = orderpatient;
                        order.testpending = vm.datareport.listTestPending;
                        if (vm.parameter.destination === '3') {
                            if (!vm.demo) {
                                vm.cont = 0;
                                vm.listemail = []
                                var data = {
                                    'parameterReport': parameterReport,
                                    'order': order,
                                    'orderpatient': orderpatient,
                                }
                                vm.listemail.push(data);
                                vm.printall();
                            } else {
                                var printphysician = _.filter(JSON.parse(JSON.stringify(vm.emailist)), function (o) {
                                    return o.idDemoAux == -201 || o.idDemoAux == -202 || o.idDemoAux == -203 || o.idDemoAux == -204 || o.idDemoAux == -205;
                                })
                                vm.demoemail = _.filter(JSON.parse(JSON.stringify(vm.emailist)), function (o) {
                                    return o.idDemoAux !== -201 && o.idDemoAux !== -202 && o.idDemoAux !== -203 && o.idDemoAux !== -204 && o.idDemoAux !== -204
                                })

                                if (printphysician.length === 0) {
                                    vm.cont = 0;
                                    vm.listemail = []
                                    var data = {
                                        'parameterReport': parameterReport,
                                        'order': order,
                                        'orderpatient': orderpatient,
                                    }
                                    vm.listemail.push(data);
                                    vm.printall();
                                } else {
                                    vm.cont = 0;
                                    vm.listemail = [];
                                    if (vm.parameter.physician || vm.parameter.patient || vm.parameter.branch || vm.parameter.additionalMail.trim() !== '' || vm.demoemail.length !== 0 && vm.demo) {
                                        var data = {
                                            'parameterReport': parameterReport,
                                            'order': JSON.parse(JSON.stringify(order)),
                                            'orderpatient': orderpatient,
                                        }
                                        vm.listemail.push(data);
                                    }
                                    for (var index = 0; index < printphysician.length; index++) {
                                        var orderchangue = JSON.parse(JSON.stringify(order))
                                        orderchangue.physician = printphysician[index];
                                        var data = {
                                            'parameterReport': parameterReport,
                                            'order': orderchangue,
                                            'orderpatient': orderpatient,
                                            'physician': printphysician[index]
                                        }
                                        vm.listemail.push(data);
                                    }
                                    vm.printall();
                                }
                            }
                        } else {
                            vm.cont = 0;
                            vm.listemail = []
                            var data = {
                                'parameterReport': parameterReport,
                                'order': order,
                                'orderpatient': orderpatient,
                            }
                            vm.listemail.push(data);
                            vm.printall();
                        }
                    }
                    else {
                        vm.message = $filter('translate')('0152');
                        UIkit.modal('#logNoDataFinal').show();
                        vm.loading = false;
                    }
                }

                vm.printall = printall;
                function printall() {
                    vm.setReport(vm.listemail[vm.cont].parameterReport, vm.listemail[vm.cont].order, vm.listemail[vm.cont].orderpatient);
                }


                function setReport(parameterReport, datareport, datareportpatient) {
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

                        //condicional para armar pdf de paciente sin confidenciales
                        if (datareportpatient !== null) {
                            var reportPatient = new Stimulsoft.Report.StiReport();
                            reportPatient.loadFile(parameterReport.pathreport);

                            var jsonDataPatient = {
                                'order': datareportpatient,
                                'Labels': [parameterReport.labelsreport],
                                'variables': [parameterReport.variables]
                            };

                            var dataSetPatient = new Stimulsoft.System.Data.DataSet();
                            dataSetPatient.readJson(jsonDataPatient);

                            reportPatient.dictionary.databases.clear();
                            reportPatient.regData('Demo', 'Demo', dataSetPatient);

                            reportPatient.renderAsync(function () {
                                var settings = new Stimulsoft.Report.Export.StiPdfExportSettings();
                                var service = new Stimulsoft.Report.Export.StiPdfExportService();
                                var stream = new Stimulsoft.System.IO.MemoryStream();
                                service.exportToAsync(function () {
                                    // Export to PDF (#1)
                                    var data = stream.toArray();
                                    var buffer = new Uint8Array(data);

                                    var idsTests = _.map(datareport.resultTest, function (o) { return o.testId });
                                    var listAttachments = _.filter(datareport.attachments, function (o) { return idsTests.includes(o.idTest) });

                                    vm.copyPages(buffer, listAttachments, true);

                                    report.renderAsync(function () {
                                        var settings = new Stimulsoft.Report.Export.StiPdfExportSettings();
                                        var service = new Stimulsoft.Report.Export.StiPdfExportService();
                                        var stream = new Stimulsoft.System.IO.MemoryStream();
                                        service.exportToAsync(function () {
                                            // Export to PDF (#1)
                                            var data = stream.toArray();
                                            var buffer = new Uint8Array(data);
                                            var idsTests = _.map(datareport.resultTest, function (o) { return o.testId });
                                            var listAttachments = _.filter(datareport.attachments, function (o) { return idsTests.includes(o.idTest) });
                                            vm.copyPages(buffer, listAttachments, false);

                                        }, report, stream, settings);
                                    });
                                }, report, stream, settings);
                            });

                        }
                        else {
                            report.renderAsync(function () {
                                var settings = new Stimulsoft.Report.Export.StiPdfExportSettings();
                                var service = new Stimulsoft.Report.Export.StiPdfExportService();
                                var stream = new Stimulsoft.System.IO.MemoryStream();
                                service.exportToAsync(function () {
                                    // Export to PDF (#1)
                                    var data = stream.toArray();
                                    var buffer = new Uint8Array(data);
                                    var idsTests = _.map(datareport.resultTest, function (o) { return o.testId });
                                    var listAttachments = _.filter(datareport.attachments, function (o) { return idsTests.includes(o.idTest) });
                                    vm.copyPages(buffer, listAttachments, false);

                                }, report, stream, settings);


                            });
                        }
                    }, 50);

                }

                function copyPages(reportpreview, attachments, patient) {

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
                                if (vm.parameter.attachments && attachments.length > 0) {

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
                                                            vm.loadImages(images, calcula, pdfDoc, patient);
                                                        } else {
                                                            pdfDoc.save().then(function (pdf) {
                                                                var pdfUrl = URL.createObjectURL(new Blob([pdf], {
                                                                    type: 'application/pdf'
                                                                }));
                                                                if (patient === true) {
                                                                    vm.pdfPatient = pdf;
                                                                    vm.pdfUrlPatient = pdfUrl;
                                                                }
                                                                else {
                                                                    sendbuffer(pdf, pdfUrl);
                                                                }
                                                            });
                                                        }
                                                    });
                                                });
                                            }
                                        });
                                    } else if (images.length > 0) {
                                        vm.loadImages(images, calcula, pdfDoc, patient);
                                    }
                                } else {
                                    pdfDoc.save().then(function (pdf) {
                                        var pdfUrl = URL.createObjectURL(new Blob([pdf], {
                                            type: 'application/pdf'
                                        }));
                                        if (patient === true) {
                                            vm.pdfPatient = pdf;
                                            vm.pdfUrlPatient = pdfUrl;
                                        }
                                        else {
                                            sendbuffer(pdf, pdfUrl);
                                        }
                                    });
                                }
                            });
                        }, function (reason) {
                            alert('Failed: ' + reason);
                        });
                    });
                }

                function loadImages(images, calcula, pdfDoc, patient) {
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
                                        if (patient === true) {
                                            vm.pdfPatient = pdf;
                                            vm.pdfUrlPatient = pdfUrl;
                                        }
                                        else {
                                            sendbuffer(pdf, pdfUrl);
                                        }
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
                                        if (patient === true) {
                                            vm.pdfPatient = pdf;
                                            vm.pdfUrlPatient = pdfUrl;
                                        }
                                        else {
                                            sendbuffer(pdf, pdfUrl);
                                        }
                                    });
                                }
                            });
                        }
                    }
                }

                vm.sendbuffer = sendbuffer;
                function sendbuffer(buffer, pdfUrl) {

                    var byteArray = new Uint8Array(buffer);
                    var byteString = '';
                    for (var i = 0; i < byteArray.byteLength; i++) {
                        byteString += String.fromCharCode(byteArray[i]);
                    }
                    var b64 = window.btoa(byteString);

                    var physicianemails = [];
                    var physicianEmail = '';
                    vm.auditEmail = [];

                    if (vm.demoemail != undefined && vm.demoemail.length !== 0 && vm.demo) {
                        vm.demoemail.forEach(function (value) {
                            physicianemails.push(value.email);
                            vm.auditEmail.push({
                                type: 'Item ' + value.codifiedCode,
                                email: value.email
                            });
                        });
                    }
                    if (vm.parameter.physician && vm.dataorder.physician !== undefined && vm.dataorder.physician.email !== "") {
                        physicianemails.push(vm.dataorder.physician.email);
                        vm.auditEmail.push({
                            type: 'Medico',
                            email: vm.dataorder.physician.email
                        });
                    }
                    if (vm.parameter.branch && vm.dataorder.branch !== undefined && vm.dataorder.branch !== null && vm.dataorder.branch.email !== "") {
                        physicianemails.push(vm.dataorder.branch.email);
                        vm.auditEmail.push({
                            type: 'Sede',
                            email: vm.dataorder.branch.email
                        });
                    }
                    if (vm.parameter.additionalMail !== undefined && vm.parameter.additionalMail !== null && vm.parameter.additionalMail !== "") {
                        physicianemails.push(vm.parameter.additionalMail);
                        vm.auditEmail.push({
                            type: 'Adicional',
                            email: vm.parameter.additionalMail
                        });
                    }

                    if (vm.listemail[vm.cont].physician === undefined) {
                        physicianEmail = _.uniq(physicianemails).join(";");
                    } else {
                        physicianEmail = vm.listemail[vm.cont].physician.email;
                        vm.auditEmail.push({
                            type: vm.listemail[vm.cont].physician.demographic,
                            email: vm.listemail[vm.cont].physician.email
                        });
                    }



                    if (vm.pdfPatient !== '') {
                        var byteArray = new Uint8Array(vm.pdfPatient);
                        var byteString = '';
                        for (var i = 0; i < byteArray.byteLength; i++) {
                            byteString += String.fromCharCode(byteArray[i]);
                        }
                        var b64patient = window.btoa(byteString);

                        var finalData = {
                            nameFile: vm.datareport.nameFile,
                            order: vm.dataorder.orderNumber,
                            bufferReport: b64patient,
                            encrypt: vm.datareport.encrypt,
                            branch: vm.dataorder.branch.id,
                            service: vm.dataorder.service !== undefined ? vm.dataorder.service.id : "",
                            printingMedium: vm.parameter.destination,
                            patientEmail: vm.datareport.patientEmail,
                            serial: vm.parameter.serial,
                            NumberCopies: vm.parameter.quantityCopies,
                            physicianEmail: physicianEmail,
                            sendEmail: vm.parameter.sendEmail,
                        }

                        var auth = localStorageService.get('Enterprise_NT.authorizationData');

                        return reportsDS.getSendPrintFinalReport(auth.authToken, finalData).then(function (data) {
                            vm.loading = false;
                            if (data.status === 200) {

                            }
                        })
                    }

                    if (vm.parameter.destination === '3') {
                        var patientName = vm.dataorder.patient.name1 + ' ' + vm.dataorder.patient.name2 + ' ' + vm.dataorder.patient.lastName + ' ' + vm.dataorder.patient.surName;
                        vm.sendEmailInternal(b64, physicianEmail, patientName, physicianemails)
                    }
                    else {
                        var auth = localStorageService.get('Enterprise_NT.authorizationData');

                        var finalData = {
                            nameFile: vm.datareport.nameFile,
                            order: vm.dataorder.orderNumber,
                            patientName: vm.dataorder.patient.name1 + ' ' + vm.dataorder.patient.name2 + ' ' + vm.dataorder.patient.lastName + ' ' + vm.dataorder.patient.surName,
                            bufferReport: b64,
                            encrypt: vm.datareport.encrypt,
                            branch: vm.dataorder.branch.id,
                            service: vm.dataorder.service !== undefined ? vm.dataorder.service.id : "",
                            printingMedium: vm.parameter.destination,
                            patientEmail: vm.datareport.patientEmail,
                            serial: vm.parameter.serial,
                            NumberCopies: vm.parameter.quantityCopies,
                            physicianEmail: physicianEmail,
                            sendEmail: vm.parameter.sendEmail,
                        }

                        return reportsDS.getSendPrintFinalReport(auth.authToken, finalData).then(function (data) {
                            if (data.status === 200) {
                                vm.changestatetestInternal(physicianEmail, pdfUrl);
                            }
                        }, function (error) {
                            vm.loading = false;
                            if (error.data.errorFields[0] === "0| the client is not connected" && vm.parameter.destination === '1') {
                                vm.message = $filter('translate')('1074');
                                UIkit.modal('#logNoDataFinal').show();
                            } else if (error.data.errorFields[0] === "0| the client is not connected" && vm.parameter.destination === '2') {
                                //vm.message = "No se encuentra conectado al cliente de impresion por lo tanto los pdf no se descargaran";
                                //UIkit.modal('#logNoDataFinal').show();
                                vm.changestatetestInternal(physicianEmail, pdfUrl);
                            } else {
                                vm.modalError(error);
                            }
                        });
                    }


                }
                //hay se envia  se hace envio simultaneo o lo divide
                function sendEmailInternal(b64, physicianEmail, patientName, physicianemails) {
                    var destination = false;
                    vm.listsendemailaudit = [];

                    if (vm.parameter.patient && vm.datareport.patientEmail !== null && vm.datareport.patientEmail !== undefined && vm.datareport.patientEmail !== '') {
                        var send = [];
                        send = send.concat(vm.datareport.patientEmail.split(";"));
                        destination = true;
                        var EmailBody = vm.EmailBody.replace("||PATIENT||", patientName);
                        var EmailSubjectPatient = vm.EmailSubjectPatient.replace("||PATIENT||", patientName);
                        var auth = localStorageService.get('Enterprise_NT.authorizationData');
                        var data = {
                            "recipients": send,
                            "subject": EmailSubjectPatient,
                            "body": EmailBody,
                            "user": auth.id,
                            "order": vm.datareport.orderNumber,
                            "attachment": [
                                {
                                    path: b64,
                                    type: "1",
                                    filename: vm.datareport.nameFile + '.pdf'
                                }
                            ]
                        }
                        reportadicional.sendEmailV2(auth.authToken, data).then(function (response) {
                            if (response.data === 'Se a generado un error a la hora de hacer el envio') {
                                logger.error("Se a generado un error a la hora de hacer el envio");
                                vm.loading = false;
                            }
                            else {
                                vm.auditEmail.push({
                                    type: 'Paciente',
                                    email: vm.datareport.patientEmail
                                });
                                vm.listsendemailaudit = vm.listsendemailaudit.concat(vm.datareport.patientEmail.split(";"));
                                if (physicianEmail !== null && physicianEmail !== undefined && physicianEmail !== '') {
                                    var send = [];
                                    send = send.concat(physicianEmail.split(";"));
                                    var EmailBody = vm.EmailBody.replace("||PATIENT||", patientName);
                                    var auth = localStorageService.get('Enterprise_NT.authorizationData');
                                    var data = {
                                        "recipients": send,
                                        "subject": vm.EmailSubjectPhysician.replace("||PATIENT||", patientName),
                                        "body": EmailBody,
                                        "user": auth.id,
                                        "order": vm.datareport.orderNumber,
                                        "attachment": [
                                            {
                                                path: b64,
                                                type: "1",
                                                filename: vm.datareport.nameFile + '.pdf'
                                            }
                                        ]
                                    }
                                    reportadicional.sendEmailV2(auth.authToken, data).then(function (response) {
                                        if (response.data === 'Se a generado un error a la hora de hacer el envio') {
                                            logger.error("Se a generado un error a la hora de hacer el envio");
                                        }
                                        else {
                                            vm.listsendemailaudit = vm.listsendemailaudit.concat(physicianEmail.split(";"));
                                            vm.changestatetestInternal(physicianEmail);
                                        }
                                    });
                                }
                                else {
                                    vm.changestatetestInternal(physicianEmail, null);
                                }

                            }
                        });
                    }
                    else if (physicianEmail !== null && physicianEmail !== undefined && physicianEmail !== '') {
                        var send = [];
                        send = send.concat(physicianEmail.split(";"));
                        var EmailBody = vm.EmailBody.replace("||PATIENT||", patientName);
                        var auth = localStorageService.get('Enterprise_NT.authorizationData');
                        var data = {
                            "recipients": send,
                            "subject": vm.EmailSubjectPhysician.replace("||PATIENT||", patientName),
                            "user": auth.id,
                            "order": vm.datareport.orderNumber,
                            "body": EmailBody,
                            "attachment": [
                                {
                                    path: b64,
                                    type: "1",
                                    filename: vm.datareport.nameFile + '.pdf'
                                }
                            ]
                        }
                        reportadicional.sendEmailV2(auth.authToken, data).then(function (response) {
                            if (response.data === 'Se a generado un error a la hora de hacer el envio') {
                                vm.loading = false;
                                logger.error("Se a generado un error a la hora de hacer el envio");
                            }
                            else {
                                vm.listsendemailaudit = vm.listsendemailaudit.concat(physicianEmail.split(";"));
                                vm.changestatetestInternal(physicianEmail);
                            }
                        });
                    }
                    else {
                        if (!destination) {
                            logger.warning("No existen destinatarios para el envio");
                            vm.loading = false;
                        }

                    }


                }

                function changestatetestInternal(physicianEmail, pdfUrl) {
                    var auth = localStorageService.get('Enterprise_NT.authorizationData');
                    var personRecive = "";
                    if (vm.parameter.destination === '1' && vm.parameter.addperson) {
                        personRecive = $filter('translate')('3263') + " " + vm.parameter.namereceiveresult;
                    }


                    if (vm.parameter.destination === '2' && vm.parameter.addperson) {
                        personRecive = $filter('translate')('3263') + " " + vm.parameter.namereceiveresult;
                    }

                    if (vm.parameter.destination === '2') {
                        window.open(pdfUrl, '_blank');
                    }
                    else if (vm.parameter.destination === '3') {

                        if (vm.auditEmail.length > 0) {
                            vm.auditEmail.forEach(function (value) {
                                personRecive += value.type + ": " + value.email + "; ";
                            });
                        }
                        // personRecive = vm.listsendemailaudit.join();

                    }

                    vm.datareport.order.createdDate = '';
                    vm.datareport.order.patient.birthday = '';
                    for (var i = 0; i < vm.datareport.order.resultTest.length; i++) {
                        vm.datareport.order.resultTest[i].resultDate = '';
                        vm.datareport.order.resultTest[i].validationDate = '';
                        vm.datareport.order.resultTest[i].entryDate = '';
                        vm.datareport.order.resultTest[i].takenDate = '';
                        vm.datareport.order.resultTest[i].verificationDate = '';
                        vm.datareport.order.resultTest[i].printDate = '';
                        if (vm.datareport.order.resultTest[i].microbiologyGrowth !== undefined) {
                            vm.datareport.order.resultTest[i].microbiologyGrowth.lastTransaction = '';
                        }
                    }
                    var datachange = {
                        filterOrderHeader: { printingMedium: vm.parameter.destination, typeReport: vm.parameter.typeprint, personReceive: personRecive },
                        order: vm.datareport.order,
                        user: auth.id
                    }

                    return reportsDS.changeStateTest(auth.authToken, datachange).then(function (data) {
                        if (data.status === 200) {
                            if (vm.listemail.length === vm.cont + 1) {
                                $scope.functionexecute();
                                vm.loading = false;
                            } else {
                                vm.cont++;
                                vm.printall();
                            }
                        }
                    }, function (error) {
                        vm.loading = false;
                        vm.modalError(error);
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
                //** Método para sacar el popup de error**//
                function modalError(error) {
                    vm.Error = error;
                    vm.ShowPopupError = true;
                }


            }],
            controllerAs: 'vmd'
        };
        return directive;
    }
})();
/* jshint ignore:end */
