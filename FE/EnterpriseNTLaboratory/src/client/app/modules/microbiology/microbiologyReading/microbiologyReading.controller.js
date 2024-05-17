/* jshint ignore:start */
(function () {
    'use strict';
    angular
        .module('app.microbiologyReading')
        .filter('trust', ['$sce', function ($sce) {
            return function (htmlCode) {
                return $sce.trustAsHtml(htmlCode);
            }
        }])
        .controller('microbiologyReadingController', microbiologyReadingController);
    microbiologyReadingController.$inject = ['localStorageService', 'common', 'microbiologyDS',
        'resultsentryDS', 'testDS', 'patientDS', 'mediaculturesDS', 'proceduresDS', 'logger',
        'sampletrackingsDS', '$filter', '$state', 'moment', '$rootScope', '$translate', 'LZString', '$hotkey', 'commentDS'
    ];

    function microbiologyReadingController(localStorageService, common, microbiologyDS,
        resultsentryDS, testDS, patientDS, mediaculturesDS, proceduresDS, logger,
        sampletrackingsDS, $filter, $state, moment, $rootScope, $translate, LZString, $hotkey, commentDS) {
        var vm = this;
        vm.init = init;
        vm.title = 'microbiologyReading';
        $rootScope.pageview = 3;
        vm.state = true;
        $rootScope.menu = true;
        $rootScope.NamePage = $filter('translate')('0026');
        $rootScope.helpReference = '03.Microbiology/microbiologyReading.htm';
        vm.state = true;
        vm.selectTest = selectTest;
        vm.getOrderlist = getOrderlist;
        vm.selectOrder = selectOrder;
        vm.getDetailTest = getDetailTest;
        vm.updateTest = updateTest;
        vm.resultKeyEvent = resultKeyEvent;
        vm.changechecktest = changechecktest;
        vm.saveunblocktest = saveunblocktest;
        vm.saveblocktest = saveblocktest;
        vm.setBlockedTest = setBlockedTest;
        vm.blockTest = blockTest;
        vm.editResult = editResult;
        vm.canceleditresult = canceleditresult;
        vm.getCodeComment = getCodeComment;

        vm.previewButton = false;
        vm.countresult = 0;


        vm.loadphotopatient = loadphotopatient;
        vm.postponement = postponement;
        vm.trackingmicrobiologytask = trackingmicrobiologytask;
        vm.tasksperpatient = tasksperpatient;
        vm.getprocedure = getprocedure;
        vm.getmediacultures = getmediacultures;
        vm.windowOpenReport = windowOpenReport;
        vm.generateDataReport = generateDataReport;
        vm.restartTask = restartTask;
        vm.savecommenttest = savecommenttest;
        vm.updateinformationtest = updateinformationtest;
        vm.saverepeattest = saverepeattest;

        vm.getIndicatorOrderCount = getIndicatorOrderCount;


        vm.getPathologyIcon = getPathologyIcon;
        vm.getStateIcon = getStateIcon;
        vm.getTestMicrobiology = getTestMicrobiology;
        vm.orderlistTest = orderlistTest;
        vm.getSampleOrder = getSampleOrder;
        vm.getDataFilter = getDataFilter;
        vm.reloadgridresult = reloadgridresult;
        vm.getLiteralResult = getLiteralResult;
        vm.getLiteralData = getLiteralData;

        vm.formatDate = localStorageService.get('FormatoFecha') + ', hh:mm:ss a';
        vm.formatDateHours = localStorageService.get('FormatoFecha').toUpperCase() + ', hh:mm:ss a';
        vm.formatDateFilter = localStorageService.get('FormatoFecha').toUpperCase();
        vm.abbrCustomer = localStorageService.get('Abreviatura').toUpperCase();
        vm.nameCustomer = localStorageService.get('Entidad').toUpperCase();
        vm.serviceManagement = localStorageService.get('ManejoServicio');
        vm.permissionuser = localStorageService.get('user');
        vm.reasonResultEdit = localStorageService.get('MotivoModificacionResultado') === 'True';


        vm.modalError = modalError;
        vm.toggleStyleSwitcher = toggleStyleSwitcher;
        vm.styleSwitcherActive = true;
        vm.rangeInit = moment().format('YYYYMMDD');
        vm.rangeEnd = moment().format('YYYYMMDD');
        vm.filterResultsPending = false;
        vm.filterValidationPending = false;
        vm.filterPreValidationPending = false;
        vm.readOnlyComment = true;

        vm.filterRange = '0';
        vm.patient = {};
        vm.photopatient = '';
        vm.selectedOrder = '';
        vm.tasksreport = tasksreport;
        vm.adddate = adddate;
        vm.messagerestartTask = '';
        vm.getpendingverificationreport = getpendingverificationreport;
        vm.typefilterorder = 0;
        vm.orderfilter = null;
        vm.sortType = 'orderNumber';
        vm.sortReverse = false;
        vm.getPanicSurvey = getPanicSurvey;
        vm.validateTests = validateTests;
        vm.validateTestsWarnings = validateTestsWarnings;
        vm.validateTestsBase = validateTestsBase;
        vm.desvalidateTests = desvalidateTests;
        vm.listOrder = [];
        vm.listtestselected = [];
        vm.indicatorOrder = null;
        vm.validateTestsPreview = validateTestsPreview;
        vm.testInformation = testInformation;
        vm.hotkeysvalidate = hotkeysvalidate;
        vm.calcFormula = calcFormula;

        vm.isAuthenticate = isAuthenticate;
        vm.getResultTypeIcon = getResultTypeIcon;

        vm.filterListresultliteral = function (search, listdemographics) {
            var listtestfilter = [];
            if (vm.savelistcurrentTest.state > vm.testState.REPORTED) {
            } else if (search.length === 1 && search.substring(0, 1) === "") {
                listtestfilter = $filter("orderBy")(listdemographics, "name");
            } else {
                listtestfilter = _.filter(listdemographics, function (color) {
                    return (
                        color.name.toUpperCase().indexOf(search.toUpperCase()) !== -1 || (color.name).toUpperCase()
                            .indexOf(search.toUpperCase()) !== -1
                    );
                })
            }
            vm.listfilterliteralResult = JSON.parse(JSON.stringify(listtestfilter));
            return listtestfilter;
        };


        vm.selectresult = selectresult;
        function selectresult(select) {
            if (select !== undefined) {
                vm.mouselist = true;
                vm.savelistcurrentTest.result = select.title;
                vm.resultKeyEventlist(vm.savelistcurrentTest);
            }
        }

        vm.selectTestlist = selectTestlist;
        function selectTestlist(currentTest, check) {
            vm.mouselist = false;
            vm.savelistcurrentTest = currentTest;
            if (currentTest.literalResult === undefined) {
                currentTest.literalResult = [];
            }
            vm.listfilterliteralResult = JSON.parse(JSON.stringify(currentTest.literalResult));
            currentTest.resultedit = document.getElementById('input_result-' + currentTest.testId + '_value').value;
            vm.selectTest(currentTest, check, 'input');
        }

        vm.testState = {
            ORDERED: 0,
            RERUN: 1,
            REPORTED: 2,
            PREVIEW: 3,
            VALIDATED: 4,
            PRINTED: 5
        };

        // //Asigna eventos a los hotkeys
        $hotkey.bind('F4', function (event) {
            if (vm.selectedTest.length !== 0 && $state.$current.controller === 'microbiologyReadingController') {
                vm.openmodalresultstemplate = true;
            }
        });

        $hotkey.bind('F3', function (event) {
            if (vm.selectedTest.length !== 0 && $state.$current.controller === 'microbiologyReadingController') {
                vm.openmodalsensitivities = true;
            }
        });

        $hotkey.bind('F7', function (event) {
            if ($state.$current.controller === 'microbiologyReadingController') {
                vm.hotkeysvalidate();
            }
        });

        $hotkey.bind('F8', function (event) {
            if ($state.$current.controller === 'microbiologyReadingController') {
                vm.openreportfinal = true;
            }
        });


        function init() {
            vm.filterinfo = {
                'textinit': $filter('translate')('0075'),
                'valueinit': 'N/A',
                'textend': $filter('translate')('0076'),
                'valueend': 'N/A'
            };

            vm.reportfilter = [{
                'id': '0',
                'name': $filter('translate')('0602') + ' / ' + $filter('translate')('0603')
            }, {
                'id': '1',
                'name': $filter('translate')('0602')
            },
            {
                'id': '2',
                'name': $filter('translate')('0603')
            }
            ];

            vm.report = {
                'id': '1',
                'name': $filter('translate')('0602')
            };

            vm.getPanicSurvey();
            vm.getTestMicrobiology();
            vm.getLiteralResult();
            vm.getCodeComment();
            vm.notes = [];
        }

        function modalError(error) {
            vm.Error = error;
            vm.ShowPopupError = true;
        }

        function hotkeysvalidate() {
            vm.validateTestsWarnings(false);
        }

        ///////////////  Metodos de los indicadores y filtros de las ordenes ////

        //Metodo para cargar el json para el filtro de ordenes
        function getDataFilter(typefilter, rangeInit, rangeEnd) {
            var data = {
                'rangeType': typefilter,
                'init': rangeInit,
                'end': rangeEnd,
                'test': vm.testselected.id,
                'report': vm.report.id,
                'codeSample': vm.sample,
                'pendingStates': [],
            };

            if (vm.filterResultsPending) {
                data.pendingStates.push(1);
            }
            if (vm.filterPreValidationPending) {
                data.pendingStates.push(2);
            }
            if (vm.filterValidationPending) {
                data.pendingStates.push(3);
            }

            return data;
        }

        vm.selectobjectall = selectobjectall;

        function selectobjectall(all, data) {
            for (var j = 0; j < data.length; j++) {
                if (data[j].block.blocked || !data[j].grantAccess || data[j].sampleState === 1 || data[j].blockdays === true) { } else {
                    data[j].isSelected = all;
                }
            }
            vm.selectedTest = !vm.selectall ? [] : data;
            updateButtonBarByTest();
        }

        //Metodo que consulta los examenes que pertenecen a muestras de microbiologia para el filtro de pruebas.
        function getTestMicrobiology() {
            vm.testselected = {
                'id': null,
                'name': $filter('translate')('0215')
            };

            vm.listTestMicrobiology = [{
                'id': null,
                'name': $filter('translate')('0215')
            }];

            var auth = localStorageService.get('Enterprise_NT.authorizationData');
            return testDS.getTestmicrobiology(auth.authToken).then(function (data) {
                vm.listTestMicrobiology = vm.listTestMicrobiology.concat(data.data.sort(vm.orderlistTest));
            }, function (error) {
                vm.modalError(error);
            });
        }

        //Metodo para ordenar la lista de examenes.
        function orderlistTest(a, b) {
            return a.code.toString().length - b.code.toString().length || a.code.toString().localeCompare(b.code.toString());
        }

        vm.resultKeyEventlist = resultKeyEventlist;

        function resultKeyEventlist(currentTest, event, index) {
            if (!vm.prueba) {
                if (currentTest.state > vm.testState.REPORTED) {
                    if (event.keyCode !== 8 || event.keyCode !== 9 || event.keyCode !== 40 || event.keyCode !== 38 || event.keyCode !== 13) {
                        event.preventDefault();
                    }
                }

                currentTest.resultRepetition = null;
                if (currentTest.literalResult === undefined) {
                    currentTest.literalResult = [];
                }

                var keyCode = event !== undefined ? event.keyCode : 100000;
                if (keyCode == 40 && vm.listfilterliteralResult.length <= 0 && currentTest.result !== '' || keyCode == 38 && vm.listfilterliteralResult.length <= 1 && currentTest.result !== '' || keyCode == 13 || keyCode == 100000) {
                    if (currentTest.literalResult.length !== 0) {
                        if (!vm.mouselist) {
                            currentTest.result = document.getElementById('input_result-' + currentTest.testId + '_value').value;
                        }
                    }
                    if (currentTest.result === ' ' || currentTest.result === '  ') {
                        currentTest.result = currentTest.result.trim();
                    }

                    if (currentTest.result === '' && currentTest.resultedit !== '' && currentTest.state == vm.testState.REPORTED ||
                        currentTest.result === '' && currentTest.resultedit !== null && currentTest.state == vm.testState.REPORTED) {
                        vm.testedit = currentTest;
                        UIkit.modal('#modaldeleteresult', {
                            bgclose: false,
                            escclose: false,
                            modal: false
                        }).show();
                    } else if (currentTest.result === '' || currentTest.result === null && currentTest.state == vm.testState.REPORTED) {
                        logger.warning('El resultado no puede estar vacio');
                        $scope.$broadcast("angucomplete-alt:changeInput", "input_result-" + currentTest.testId, currentTest.resultedit);
                        currentTest.result = currentTest.resultedit
                    }

                    var validar = currentTest.result === null ? '' : currentTest.result;

                    if (currentTest.result !== undefined && validar.length > 0 && currentTest.state <= vm.testState.REPORTED) {
                        if (currentTest.resultType === 1) {
                            if (isNaN(parseFloat(currentTest.result)) && currentTest.result !== vm.separator + vm.separator) {
                                currentTest.result = currentTest.resultedit
                                logger.error('Este resultado debe ser númerico');
                            } else if (currentTest.reportedMax === currentTest.reportedMin) {
                                if (currentTest.resultedit !== null && currentTest.resultedit !== currentTest.result && currentTest.state === vm.testState.REPORTED) {

                                    vm.testedit = currentTest;
                                    UIkit.modal('#modaleditresult', {
                                        bgclose: false,
                                        escclose: false,
                                        modal: false
                                    }).show();


                                } else if (currentTest.state < vm.testState.REPORTED) {
                                    if (vm.comparedtes !== currentTest.testId) {
                                        vm.comparedtes = currentTest.testId;
                                        vm.updateTest(currentTest, vm.testState.REPORTED);
                                    }
                                    vm.moveNextTest(keyCode, currentTest);
                                } else if (keyCode !== 100000) {
                                    vm.moveNextTest(keyCode, currentTest);
                                }

                            } else if (currentTest.reportedMin < parseFloat(currentTest.result) && parseFloat(currentTest.result) < currentTest.reportedMax) {
                                if (currentTest.resultedit !== null && currentTest.resultedit !== currentTest.result && currentTest.state === vm.testState.REPORTED) {

                                    vm.testedit = currentTest;
                                    UIkit.modal('#modaleditresult', {
                                        bgclose: false,
                                        escclose: false,
                                        modal: false
                                    }).show();

                                } else if (currentTest.state < vm.testState.REPORTED) {
                                    if (vm.comparedtes !== currentTest.testId) {
                                        vm.comparedtes = currentTest.testId;
                                        vm.updateTest(currentTest, vm.testState.REPORTED);
                                    }
                                    vm.moveNextTest(keyCode, currentTest);
                                } else if (keyCode !== 100000) {
                                    vm.moveNextTest(keyCode, currentTest);
                                }

                            } else {
                                currentTest.result = currentTest.resultedit;
                                logger.warning($filter('translate')('1444'));
                            }
                        } else {
                            if (currentTest.resultedit !== null && currentTest.resultedit !== currentTest.result && currentTest.state === vm.testState.REPORTED) {

                                vm.testedit = currentTest;
                                UIkit.modal('#modaleditresult', {
                                    bgclose: false,
                                    escclose: false,
                                    modal: false
                                }).show();

                            } else if (currentTest.state < vm.testState.REPORTED) {
                                if (vm.comparedtes !== currentTest.testId) {
                                    vm.comparedtes = currentTest.testId;
                                    vm.updateTest(currentTest, vm.testState.REPORTED);
                                }
                                vm.moveNextTest(keyCode, currentTest);
                            } else if (keyCode !== 100000) {
                                vm.moveNextTest(keyCode, currentTest);
                            }
                        }

                    } else {
                        var addview = currentTest.result === '' && currentTest.resultedit !== '' && currentTest.state == vm.testState.REPORTED ||
                            currentTest.result === '' && currentTest.resultedit !== null && currentTest.state == vm.testState.REPORTED;
                        if (keyCode !== 100000 && !addview) {
                            vm.moveNextTest(keyCode, currentTest);
                        };
                    }

                }
            }
            else {
                vm.prueba = false;
            }
        }

        function getCodeComment() {
            var auth = localStorageService.get('Enterprise_NT.authorizationData');
            vm.codeCommentList = [];
            return commentDS.getComment(auth.authToken).then(function (data) {
                vm.codeCommentList = $filter('filter')(data.data, function (o) {
                    return (o.apply == 1 || o.apply == 3) && o.state;
                });
            }, function (error) {
                vm.modalError();
            });
        }


        //Metodo para validar que la orden y la muestra existan.
        function getSampleOrder(type) {
            vm.listSample = [];

            vm.menssageinformative = '';
            var auth = localStorageService.get('Enterprise_NT.authorizationData');
            if (vm.orderfilter !== undefined) {
                return sampletrackingsDS.sampleorder(auth.authToken, vm.orderfilter).then(function (data) {
                    if (data.status === 200) {
                        var sample = [];
                        if (vm.sample !== '' && vm.sample !== -1) {

                            sample = $filter('filter')(data.data, {
                                id: vm.sample
                            });
                            if (sample.length === 0) {
                                vm.menssageinformative = $filter('translate')('0180');
                                return;
                            }
                        }

                        vm.getOrderlist(sample);
                        setTimeout(function () {
                            if (vm.listOrder.length > 0) {
                                vm.selectOrder(1, vm.listOrder[0], 0);
                                setTimeout(function () {
                                    if (vm.listtest.length > 0) {
                                        UIkit.modal('#modaldate').hide();
                                        vm.styleSwitcherActive = !vm.styleSwitcherActive;
                                        vm.getIndicatorOrderCount();
                                    } else {
                                        vm.menssageinformative = $filter('translate')('0596');
                                    }
                                }, 100);
                            } else {
                                vm.menssageinformative = $filter('translate')('0595');
                            }
                        }, 100);
                    } else {
                        vm.menssageinformative = $filter('translate')('0179');
                    }
                }, function (error) {
                    if (error.data === null) {
                        vm.modalError(error);
                    }
                });
            }
        }

        //Metodo que realiza la consulta de las ordenes segun los filtros escogidos
        function getOrderlist(sample, type) {
            vm.loading = true;

            switch (type) {
                case 1:
                    vm.filterResultsPending = !vm.filterResultsPending;
                    break;
                case 2:
                    vm.filterPreValidationPending = !vm.filterPreValidationPending;
                    break;
                case 3:
                    vm.filterValidationPending = !vm.filterValidationPending;
                    break;
                default:
                    break;
            }

            vm.messagerestartTask = $filter('translate')('0590').replace('@@@@', vm.rangeInit).replace('@@@@', vm.rangeEnd);

            vm.data = vm.getDataFilter(vm.typefilterorder === 0 ? vm.filterRange : 1, vm.typefilterorder === 0 ? vm.rangeInit : vm.orderfilter, vm.typefilterorder === 0 ? vm.rangeEnd : vm.orderfilter);

            if (vm.typefilterorder === 0) {
                vm.filterinfo = {
                    'textinit': vm.filterRange === '0' ? $filter('translate')('0075') : $filter('translate')('0073'),
                    'valueinit': vm.filterRange === '0' ? moment(vm.rangeInit).format(vm.formatDateFilter) : vm.rangeInit,
                    'textend': vm.filterRange === '0' ? $filter('translate')('0076') : $filter('translate')('0074'),
                    'valueend': vm.filterRange === '0' ? moment(vm.rangeEnd).format(vm.formatDateFilter) : vm.rangeEnd
                };
            } else {
                vm.filterinfo = {
                    'textinit': $filter('translate')('0110'),
                    'valueinit': vm.orderfilter,
                    'textend': $filter('translate')('0111'),
                    'valueend': sample !== undefined && sample.length > 0 ? sample[0].name : 'N/A'
                };
            }

            vm.listOrder = [{
                'orderinformation': -1
            }];

            var auth = localStorageService.get('Enterprise_NT.authorizationData');
            return microbiologyDS.getmicrobiologyresultorder(auth.authToken, vm.data).then(function (data) {
                if (data.status === 200) {
                    vm.listOrder = data.data;
                    vm.listOrder.forEach(function (value, key) {
                        value.orderinformation = value.orderNumber.toString().substring(3);
                    });
                } else {
                    vm.listOrder = [];
                }
                vm.loading = false;
            }, function (error) {
                vm.modalError(error);
            });
        }

        /**
            Funcion  Obtiene el nombre de la imagen para representar tipo de resultado
            @author  jblanco
            @param   resultType: código del tipo de resultado
            @return  string:     clase que representa el ícono
            @version 0.0.1
          */
        function getResultTypeIcon(resultType, literalResult, formula) {
            switch (resultType) {
                case 1:
                    if (formula !== '') {
                        return 'uk-icon-superscript';
                    } else {
                        return 'uk-icon-hashtag';
                    }

                case 2:
                    if (literalResult === 0) {
                        return 'uk-icon-text-width';
                    } else {
                        return 'uk-icon-list';
                    }

                default:
                    return 'uk-icon-superscript';
            }
        }

        //Metodo que realiza la consulta de los examenes de la orden escogida
        function selectOrder(filter, order, index) {
            vm.selectall = false;
            vm.selectedOrder = order === undefined ? vm.selectedOrder : order;
            vm.selectedTest = [];
            vm.styleTestActive = false;
            vm.selectedTestId = null;
            vm.microbiologyGrowth = null;
            vm.orderselect = order.orderNumber;
            vm.readOnlyComment = true;
            vm.repetitionButton = false;
            vm.patient = {
                'inconsistency': vm.selectedOrder.inconsistency,
                'order': (vm.selectedOrder.orderNumber).toString().substring(3),
                'branch': vm.selectedOrder.branch.name,
                'service': vm.selectedOrder.service.name,
                'race': vm.selectedOrder.patient.race,
                'raceid': vm.selectedOrder.patient.race,
                'size': vm.selectedOrder.patient.size === undefined ? null : vm.selectedOrder.patient.size,
                'weight': vm.selectedOrder.patient.weight === undefined ? null : vm.selectedOrder.patient.weight,
                'genderobject': vm.selectedOrder.patient.sex,
                'id': vm.selectedOrder.patient.id,
                'name': vm.selectedOrder.patient.name1 + ' ' + vm.selectedOrder.patient.name2 + ' ' + vm.selectedOrder.patient.lastName,
                'patientId': vm.selectedOrder.patient.id,
                'document': (vm.selectedOrder.patient.documentType.id !== 0 ? vm.selectedOrder.patient.documentType.abbr : '') + ' ' + vm.selectedOrder.patient.patientId,
                'birthday': vm.selectedOrder.patient.birthday,
                'age': common.getAgeAsString(moment(vm.selectedOrder.patient.birthday).format(vm.formatDateFilter), vm.formatDateFilter),
                'gender': ($filter('translate')('0000') === 'enUsa' ? vm.selectedOrder.patient.sex.enUsa : vm.selectedOrder.patient.sex.esCo),
                'typeordercolor': vm.selectedOrder.type.color !== '' ? vm.selectedOrder.type.color : vm.selectedOrder.type.code === 'R' ? '#a5d6a7' : vm.selectedOrder.type.code === 'S' ? '#ef5350' : vm.selectedOrder.type.code === 'P' ? '#ce93d8' : vm.selectedOrder.type.code === 'A' ? '#90caf9' : vm.selectedOrder.type.code === 'C' ? '#fff176' : '#90caf9',
                'typeordercode': vm.selectedOrder.type.code
            }
            vm.patientDateValues = {};
            vm.patientDateValues[-99] = vm.selectedOrder.patient.id;
            vm.patientDateValues[-105] = vm.selectedOrder.patient.birthday;
            vm.patientDateValues[-104] = {
                code: vm.selectedOrder.patient.sex.code
            };

            vm.notes = [];
            vm.data = vm.getDataFilter(1, vm.selectedOrder.orderNumber, vm.selectedOrder.orderNumber);

            vm.detailButton = vm.selectedOrder.orderNumber > 0;
            vm.previewButton = vm.detailButton;


            var auth = localStorageService.get('Enterprise_NT.authorizationData');
            return microbiologyDS.getmicrobiologyresult(auth.authToken, vm.data).then(function (data) {
                if (data.status === 200) {
                    vm.listtest = data.data;
                    var itemiLast = {};
                    vm.listtest.forEach(function (itemi) {
                        itemi.literalResult = vm.getLiteralData(itemi.testId);
                        if (!itemi.block.blocked) {
                            if (itemiLast !== undefined) {
                                itemiLast.next = itemi.testId;
                                itemi.last = itemiLast.testId;
                            }
                            itemiLast = itemi;
                        }

                        itemi.blockdays = false;
                        if (!auth.administrator) {
                            if (itemi.state === vm.testState.VALIDATED) {
                                var datevalid = moment(itemi.validationDate).add(itemi.maxDays, 'days');
                                var diffdate = moment(datevalid).diff(moment(), 'days');
                                if (diffdate === 0) {
                                    itemi.blockdaysMessage = $filter('translate')('1409');
                                    itemi.blockdays = true;
                                }
                            } else if (itemi.state === vm.testState.PRINTED) {
                                var datevalid = moment(itemi.printDate).add(itemi.maxPrintDays, 'days');
                                var diffdate = moment(datevalid).diff(moment(), 'days');
                                if (diffdate === 0) {
                                    itemi.blockdaysMessage = $filter('translate')('1410');
                                    itemi.blockdays = true;
                                }
                            }
                        }
                    });
                    vm.listtest = _.orderBy(vm.listtest, 'printSort', 'asc');
                    var index = 0;
                    vm.listtest = _.sortBy(vm.listtest, [function (o) {
                        if (!o.block.blocked) {
                            o.idindex = index;
                            index++;
                            if (itemiLast !== undefined) {
                                itemiLast.next = o.testId;
                                o.last = itemiLast.testId;
                            }
                            itemiLast = o;
                        }
                        return o.printSort;
                    }]);
                    updateButtonBarByTest();
                    vm.photopatient = '';
                    vm.loadphotopatient(vm.selectedOrder.patient.id);
                }
            }, function (error) {
                vm.modalError(error);
            });
        }

        //Metodo para cargar actualizaciones en la grilla de resultados
        function reloadgridresult() {
            vm.loading = true;
            var auth = localStorageService.get('Enterprise_NT.authorizationData');
            return microbiologyDS.getmicrobiologyresult(auth.authToken, vm.data).then(function (data) {
                if (data.status === 200) {
                    vm.listtest = data.data;
                    var itemiLast = {};
                    vm.listtest.forEach(function (itemi) {
                        itemi.literalResult = vm.getLiteralData(itemi.testId);
                        if (!itemi.block.blocked) {
                            if (itemiLast !== undefined) {
                                itemiLast.next = itemi.testId;
                                itemi.last = itemiLast.testId;
                            }
                            itemiLast = itemi;
                        }

                        itemi.blockdays = false;
                        if (!auth.administrator) {
                            if (itemi.state === vm.testState.VALIDATED) {
                                var datevalid = moment(itemi.validationDate).add(itemi.maxDays, 'days');
                                var diffdate = moment(datevalid).diff(moment(), 'days');
                                if (diffdate === 0) {
                                    itemi.blockdaysMessage = $filter('translate')('1409');
                                    itemi.blockdays = true;
                                }
                            } else if (itemi.state === vm.testState.PRINTED) {
                                var datevalid = moment(itemi.printDate).add(itemi.maxPrintDays, 'days');
                                var diffdate = moment(datevalid).diff(moment(), 'days');
                                if (diffdate === 0) {
                                    itemi.blockdaysMessage = $filter('translate')('1410');
                                    itemi.blockdays = true;
                                }
                            }
                        }
                    });
                    vm.listtest = _.orderBy(vm.listtest, 'printSort', 'asc');
                    var index = 0;
                    vm.listtest = _.sortBy(vm.listtest, [function (o) {
                        if (!o.block.blocked) {
                            o.idindex = index;
                            index++;
                            if (itemiLast !== undefined) {
                                itemiLast.next = o.testId;
                                o.last = itemiLast.testId;
                            }
                            itemiLast = o;
                        }
                        return o.printSort;
                    }]);
                    vm.loading = false;
                    updateButtonBarByTest();
                    vm.photopatient = '';
                    vm.loadphotopatient(vm.selectedOrder.patient.id);
                }
            }, function (error) {
                vm.modalError(error);
            });
        }

        //Metodo para abrir y cerrar la ventana del filtro de ordenes.
        function toggleStyleSwitcher($event) {
            $event.preventDefault();
            vm.styleSwitcherActive = !vm.styleSwitcherActive;
        }

        //Metodo para consultar indicadores de las ordenes
        function getIndicatorOrderCount() {
            var data = {
                'rangeType': vm.typefilterorder === 0 ? vm.filterRange : 1,
                'init': vm.typefilterorder === 0 ? vm.rangeInit : vm.orderfilter,
                'end': vm.typefilterorder === 0 ? vm.rangeEnd : vm.orderfilter,
                'test': vm.testselected.id,
                'report': vm.report.id,
            };
            var auth = localStorageService.get('Enterprise_NT.authorizationData');
            return microbiologyDS.getIndicatorOrderCount(auth.authToken, data).then(function (data) {
                if (data.status === 200) {
                    vm.indicatorOrder = data.data;
                    vm.getOrderlist();
                }
            }, function (error) {
                vm.modalError(error);
            });
        }

        function updateinformationtest(test) {
            var auth = localStorageService.get('Enterprise_NT.authorizationData');
            return resultsentryDS.getinformationtestorder(auth.authToken, vm.orderselect, vm.selectedTest[0].testId).then(function (data) {
                if (data.status === 200) {
                    vm.selectedTest.result = data.data.result;
                    vm.selectedTest.state = data.data.state;
                }
            }, function (error) {
                vm.modalError(error);
            });
        }

        function calcFormula(datatest) {
            vm.eventkey = false;
            if (datatest.state === vm.testState.REPORTED) {
                datatest.resultedit = datatest.result;
            } else if (datatest.resultType === 1 && datatest.formula !== '' && datatest.state < vm.testState.REPORTED) {

                var test = '';
                var indexini = 0;
                vm.output = '';

                for (var i = 0; i < datatest.formula.length; i++) {
                    if (datatest.formula[i] === '|' && indexini === 0) {
                        test = test + datatest.formula[i + 2];
                        indexini = i + 2;
                        i = i + 2;
                    } else if (datatest.formula[i] !== '|' && indexini > 0) {
                        test = test + datatest.formula[i];

                    } else if (datatest.formula[i] === '|' && indexini > 0) {
                        if (angular.equals('GENDER', test)) {
                            if (vm.selectedOrder.sex.id === 8) {
                                vm.output = vm.output + parseInt(vm.femaleValueFormula);
                            } else if (vm.selectedOrder.sex.id === 7) {
                                vm.output = vm.output + parseInt(vm.maleValueFormula);
                            } else if (vm.selectedOrder.sex.id === 9) {
                                vm.output = vm.output + parseInt(vm.undefinedValueFormula)
                            }
                        } else if (angular.equals('RACE', test)) {
                            if (vm.selectedOrder.race !== null && vm.selectedOrder.race !== '' && vm.selectedOrder.race !== undefined) {
                                vm.output = vm.output + vm.selectedOrder.race.value;
                            } else {
                                vm.output = '';
                                break;
                            }
                        } else if (angular.equals('WEIGHT', test)) {
                            if (vm.selectedOrder.weight !== null && vm.selectedOrder.weight !== '' && vm.selectedOrder.weight !== undefined) {
                                vm.output = vm.output + vm.selectedOrder.weight
                            } else {
                                vm.output = '';
                                break;
                            }
                        } else if (angular.equals('SIZE', test)) {
                            if (vm.selectedOrder.size !== null && vm.selectedOrder.size !== '' && vm.selectedOrder.size !== undefined) {
                                vm.output = vm.output + vm.selectedOrder.size
                            }
                        } else {


                            var findtest = [];
                            angular.forEach(vm.orderTests, function (item) {
                                angular.forEach(item, function (e) {
                                    if (e.testId === parseInt(test) && e.state >= vm.testState.REPORTED) {
                                        findtest.push(e);
                                    }
                                });
                            });

                            if (findtest.length > 0) {
                                vm.output = vm.output + findtest[0].result
                            } else {
                                vm.output = '';
                                break;
                            }
                        }

                        test = '';
                        indexini = 0;
                        i = i + 1;
                    } else if (datatest.formula[i] === '(') {
                        vm.output = vm.output + datatest.formula[i];
                    } else if (datatest.formula[i] === ')') {
                        vm.output = vm.output + datatest.formula[i];
                    } else {
                        vm.output = vm.output + datatest.formula[i];
                    }

                }

                if (vm.output !== '') {
                    vm.output = $filter('lowercase')(vm.output);
                    datatest.result = math.evaluate(vm.output);
                    datatest.result = parseFloat(datatest.result).toFixed(datatest.digits);
                }
            }
            datatest.result = datatest.result === null || datatest.result === undefined ? '' : datatest.result
            if (datatest.result.trim() === '') {
                document.getElementById('input_result-' + datatest.testId).value = '';
            }
        }

        vm.getkeys = getkeys;
        function getkeys(event, currentTest) {

            if (currentTest.state > vm.testState.REPORTED) {
                if (event.keyCode !== 8 || event.keyCode !== 9 || event.keyCode !== 40 || event.keyCode !== 38 || event.keyCode !== 13) {
                    event.preventDefault();
                }
            }
            if (event.keyCode == 9) {

                vm.prueba = true;
                currentTest.resultRepetition = null;
                if (currentTest.literalResult === undefined) {
                    currentTest.literalResult = [];
                }

                if (currentTest.literalResult.length !== 0) {
                    if (!vm.mouselist) {
                        currentTest.result = document.getElementById('input_result-' + currentTest.testId + '_value').value;
                    }
                }
                var keyCode = event !== undefined ? event.keyCode : 100000;


                if (currentTest.result === '' && currentTest.resultedit !== '' && currentTest.state == vm.testState.REPORTED ||
                    currentTest.result === '' && currentTest.resultedit !== null && currentTest.state == vm.testState.REPORTED) {
                    vm.testedit = currentTest;
                    UIkit.modal('#modaldeleteresult', {
                        bgclose: false,
                        escclose: false,
                        modal: false
                    }).show();
                } else if (currentTest.result === '' || currentTest.result === null && currentTest.state == vm.testState.REPORTED) {
                    logger.warning('El resultado no puede estar vacio');
                    currentTest.result = currentTest.resultedit
                    $scope.$broadcast("angucomplete-alt:changeInput", "input_result-" + currentTest.testId, currentTest.resultedit);
                }

                var validar = currentTest.result === null ? '' : currentTest.result;
                if (currentTest.result !== undefined && validar.length > 0 && currentTest.state <= vm.testState.REPORTED) {
                    if (currentTest.resultType === 1) {
                        if (isNaN(parseFloat(currentTest.result)) && currentTest.result !== vm.separator + vm.separator) {
                            currentTest.result = currentTest.resultedit
                            logger.error('Este resultado debe ser númerico');
                        } else if (currentTest.reportedMax === currentTest.reportedMin) {
                            if (currentTest.resultedit !== null && currentTest.resultedit !== currentTest.result && currentTest.state === vm.testState.REPORTED) {

                                vm.testedit = currentTest;
                                UIkit.modal('#modaleditresult', {
                                    bgclose: false,
                                    escclose: false,
                                    modal: false
                                }).show();


                            } else if (currentTest.state < vm.testState.REPORTED) {

                                vm.updateTest(currentTest, vm.testState.REPORTED);
                                if (keyCode !== 100000) {
                                    vm.moveNextTest(keyCode, currentTest);
                                }
                            } else if (keyCode !== 100000) {
                                vm.moveNextTest(keyCode, currentTest);
                            }

                        } else if (currentTest.reportedMin < parseFloat(currentTest.result) && parseFloat(currentTest.result) < currentTest.reportedMax) {
                            if (currentTest.resultedit !== null && currentTest.resultedit !== currentTest.result && currentTest.state === vm.testState.REPORTED) {

                                vm.testedit = currentTest;
                                UIkit.modal('#modaleditresult', {
                                    bgclose: false,
                                    escclose: false,
                                    modal: false
                                }).show();

                            } else if (currentTest.state < vm.testState.REPORTED) {

                                vm.updateTest(currentTest, vm.testState.REPORTED);
                                if (keyCode !== 100000) {
                                    vm.moveNextTest(keyCode, currentTest);
                                }
                            } else if (keyCode !== 100000) {
                                vm.moveNextTest(keyCode, currentTest);
                            }

                        } else {
                            currentTest.result = currentTest.resultedit;
                            logger.warning($filter('translate')('1444'));
                        }
                    } else {
                        if (currentTest.resultedit !== null && currentTest.resultedit !== currentTest.result && currentTest.state === vm.testState.REPORTED) {

                            vm.testedit = currentTest;
                            UIkit.modal('#modaleditresult', {
                                bgclose: false,
                                escclose: false,
                                modal: false
                            }).show();

                        } else if (currentTest.state < vm.testState.REPORTED) {

                            vm.updateTest(currentTest, vm.testState.REPORTED);
                            if (keyCode !== 100000) {
                                vm.moveNextTest(keyCode, currentTest);
                            }
                        } else if (keyCode !== 100000) {
                            vm.moveNextTest(keyCode, currentTest);
                        }
                    }

                } else {
                    var addview = currentTest.result === '' && currentTest.resultedit !== '' && currentTest.state == vm.testState.REPORTED ||
                        currentTest.result === '' && currentTest.resultedit !== null && currentTest.state == vm.testState.REPORTED;

                    if (keyCode !== 100000 && !addview) {
                        vm.moveNextTest(keyCode, currentTest);
                    };
                }
                event.preventDefault();
            }
        }

        vm.keyselect = keyselect;

        function keyselect(currentTest, $event) {
            if (currentTest.resultType === 1) {
                var keyCode = $event.which || $event.keyCode;
                if (keyCode == 40 || keyCode == 38 || keyCode == 13) {
                    if (currentTest.result !== '' || currentTest.result !== null) {
                        if (currentTest.result[currentTest.result.length - 1] === ".") {
                            currentTest.result = currentTest.result.replace(".", "");
                        }
                    }
                } else {
                    var validated = false;
                    if (keyCode === 45) {
                        if (currentTest.result !== '' || currentTest.result !== null) {
                            if (currentTest.result[currentTest.result.length - 1] === "-") {
                            } else {
                                currentTest.result = '';
                            }
                        }
                        var expreg = new RegExp('^[' + vm.separator + ']{0,2}$');
                        if (!expreg.test(currentTest.result + String.fromCharCode(keyCode))) {
                            //detener toda accion en la caja de texto
                            $event.preventDefault();
                        }
                    } else {
                        if (keyCode === 48 || keyCode === 49 || keyCode === 50 || keyCode === 51 || keyCode === 52 ||
                            keyCode === 53 || keyCode === 54 || keyCode === 55 || keyCode === 56 || keyCode === 57) {
                            if (currentTest.result !== '' || currentTest.result !== null) {
                                if (currentTest.result[currentTest.result.length - 1].search("-") === -1) {
                                } else {
                                    currentTest.result = '';
                                }
                            }
                        }
                        var expreg = new RegExp('^\\d+[.]?\\d*$');
                        if (!expreg.test(currentTest.result + String.fromCharCode(keyCode))) {
                            //detener toda accion en la caja de texto
                            $event.preventDefault();
                        }
                    }
                }
            }
        }


        ///////////////  Metodos de la botonera para acciones con las ordenes ////

        //Función que imprime el reporte de tareas
        function tasksreport() {
            var json = {
                'rangeType': vm.filterRange,
                'init': vm.rangeInit,
                'end': vm.rangeEnd
            };

            var auth = localStorageService.get('Enterprise_NT.authorizationData');
            vm.variables = {
                'username': auth.userName,
                'now': moment().format(vm.formatDateHours)
            };

            return microbiologyDS.getlisttasksperpatient(auth.authToken, json).then(function (data) {
                vm.pathreport = '/Report/post-analitic/tasks/tasks.mrt';
                vm.orientation = 'vertical';
                vm.datareport = data.data.length === 0 ? data.data : vm.adddate(data.data);
                vm.progressPrint = false;
                vm.openreport = false;
                vm.windowOpenReport();
            });
        }

        function adddate(data) {
            data.forEach(function (value, key) {
                value.dateRegister = moment(value.dateRegister).format(vm.formatDateHours);
            });
            return data;
        }

        //funcion para imprimir el reporte de tareas por paciente
        function tasksperpatient() {
            vm.getmediacultures();
            vm.getprocedure();
            var json = {
                'rangeType': vm.filterRange,
                'init': vm.rangeInit,
                'end': vm.rangeEnd
            };
            var auth = localStorageService.get('Enterprise_NT.authorizationData');
            var customer = vm.abbrCustomer + ' (' + vm.nameCustomer + ')'
            vm.variables = {
                'customer': customer,
                'username': auth.userName,
                'now': moment().format(vm.formatDateHours)
            };
            return microbiologyDS.getlisttasksperpatient(auth.authToken, json).then(function (data) {
                vm.pathreport = '/Report/post-analitic/tasksperpatient/tasksperpatient.mrt';
                vm.orientation = 'vertical';
                vm.datareport = vm.generateDataReport(data.data);
                vm.progressPrint = false;
                vm.openreport = false;
                vm.windowOpenReport();
            });
        }

        function generateDataReport(data) {
            var dataReport = [];
            if (data.length > 0) {
                data.forEach(function (value) {
                    var nameRecord = value.type === 1 ? vm.mediacultures : vm.procedure;
                    for (var i = 0; i < value.tasks.length; i++) {

                        dataReport.push({
                            'lastTransaction': moment(value.lastTransaction).format(vm.formatDateHours),
                            'userId': value.user.id,
                            'userName': value.user.userName,
                            'lastName': value.user.lastName,
                            'firshName': value.user.name,
                            'id': value.id,
                            'order': value.order,
                            'test': value.idTest,
                            'testName': value.nameTest,
                            'testAbbr': value.abbrTest,
                            'idPatient': value.patient !== undefined ? value.patient.id : null,
                            'patientId': value.patient !== undefined ? value.patient.patientId : null,
                            'name1': value.patient !== undefined ? value.patient.name1 : null,
                            'name2': value.patient !== undefined ? value.patient.name2 : null,
                            'lastNameP': value.patient !== undefined ? value.patient.lastName : null,
                            'surName': value.patient !== undefined ? value.patient.surName : null,
                            'idRecord': value.idRecord,
                            'nameRecord': $filter('filter')(nameRecord, {
                                id: value.idRecord
                            }, true)[0].name,
                            'comment': value.comment,
                            'type': value.type,
                            'nameType': $filter('translate')('0' + (485 + value.type).toString()),
                            'tasksId': value.tasks[i].id,
                            'tasksDescription': value.tasks[i].description,
                            'tasksState': value.tasks[i].state,
                            'reported': value.reported,
                            'userRegisterId': value.userRegister.id,
                            'userRegisterName': value.userRegister.userName,
                            'lastNameR': value.userRegister.lastName,
                            'firshNameR': value.userRegister.name,
                            'dateRegister': moment(value.dateRegister).format(vm.formatDateHours)
                        });
                    }

                });
            }
            return dataReport;
        }

        //Función que imprime el reporte de pendientes de verificación
        function getpendingverificationreport() {
            var json = {
                'rangeType': vm.filterRange,
                'init': vm.rangeInit,
                'end': vm.rangeEnd
            };

            var auth = localStorageService.get('Enterprise_NT.authorizationData');
            vm.variables = {
                'username': auth.userName,
                'now': moment().format(vm.formatDateHours)
            };

            return microbiologyDS.getpendingverification(auth.authToken, json).then(function (data) {
                vm.pathreport = '/Report/post-analitic/Pendingverification/Pendingverification.mrt';
                vm.orientation = 'vertical';
                vm.datareport = data.data.length === 0 ? data.data : vm.adddate(data.data);
                vm.progressPrint = false;
                vm.openreport = false;
                vm.windowOpenReport();
            });
        }

        //Funcion para abrir ventana de reportes
        function windowOpenReport() {
            if (vm.datareport.length > 0) {
                var parameterReport = {};
                parameterReport.variables = vm.variables;
                parameterReport.pathreport = vm.pathreport;
                parameterReport.labelsreport = JSON.stringify($translate.getTranslationTable());
                var datareport = LZString.compressToUTF16(JSON.stringify(vm.datareport));
                localStorageService.set('parameterReport', parameterReport);
                localStorageService.set('dataReport', datareport);
                window.open('/viewreport/viewreport.html');
            } else {
                UIkit.modal('#modalReportError').show();
            }
        }

        // función para  lista de medios de cultivo
        function getmediacultures() {
            var auth = localStorageService.get('Enterprise_NT.authorizationData');
            return mediaculturesDS.getmediaculturesAll(auth.authToken).then(function (data) {
                if (data.status === 200) {
                    vm.mediacultures = data.data;
                }
            }, function (error) {
                vm.modalError(error);

            });
        }

        // función para  lista de procedimientos
        function getprocedure() {
            var auth = localStorageService.get('Enterprise_NT.authorizationData');
            return proceduresDS.getproceduresAll(auth.authToken).then(function (data) {
                if (data.status === 200) {
                    vm.procedure = data.data;
                }
            }, function (error) {
                vm.modalError(error);

            });
        }

        //funcion para reiniciar las tareas de un rango especifico de ordenes
        function restartTask() {

            var detail = {
                'rangeType': vm.filterRange,
                'init': vm.rangeInit,
                'end': vm.rangeEnd,
            }

            var auth = localStorageService.get('Enterprise_NT.authorizationData');
            return microbiologyDS.restarttask(auth.authToken, detail).then(function (data) {
                if (data.status === 200) {
                    logger.success($filter('translate')('0149'));

                }
            }, function (error) {
                vm.modalError(error);

            });
        }

        ///////////////  Metodos grilla de resultados  ////

        function getPathologyIcon(pathology, smallSize) {
            var size = smallSize ? ' uk-icon-small' : '';
            switch (pathology) {
                case 0:
                    return '';
                case 1:
                    return 'uk-icon-exclamation uk-text-warning' + size;
                case 2:
                    return 'uk-icon-chevron-down uk-text-warning';
                case 3:
                    return 'uk-icon-chevron-up uk-text-warning';
                case 4:
                case 7:
                    return 'uk-icon-exclamation-triangle uk-text-danger';
                case 5:
                case 8:
                    return 'uk-icon-angle-double-down uk-text-danger uk-icon-small';
                case 6:
                case 9:
                    return 'uk-icon-angle-double-up uk-text-danger uk-icon-small';
                default:
                    return '';
            }
        }

        function getStateIcon(state) {
            switch (state) {
                case 0:
                    return 'uk-icon-circle-thin uk-text-primary';
                case 1:
                    return 'uk-icon-repeat uk-text-primary';
                case 2:
                    return 'uk-icon-file-text-o uk-text-primary';
                case 3:
                    return 'uk-icon-check uk-text-success';
                case 4:
                    return 'uk-icon-check-circle-o uk-text-success';
                case 5:
                    return 'uk-icon-print uk-text-success';
                default:
                    return '';
            }
        }

        function getLiteralResult() {
            var auth = localStorageService.get('Enterprise_NT.authorizationData');

            return resultsentryDS.getLiterals(auth.authToken).then(function (data) {
                if (data.status === 200) {
                    vm.literalResult = data.data;
                }
            }, function (error) {
                vm.modalError(error);
            });
        }

        function getLiteralData(p_testId) {
            //TODO: Filtrar la lista de literales por el id del examen
            var literalFilter = $filter('filter')(vm.literalResult, function (e) {
                return e.testId === p_testId
            });
            return literalFilter;
        }

        //Metodo seleccion de examenes
        function getDetailTest($event, currentTest) {
            $event.preventDefault();
            var auth = localStorageService.get('Enterprise_NT.authorizationData');
            vm.selectedTest = [];
            vm.selectedTestId = null;
            vm.microbiologyGrowth = '';

            if (currentTest.block.blocked === false && currentTest.grantAccess) {

                var listtestselected = $filter('filter')(vm.listtest, {
                    'isSelected': true
                });

                if (listtestselected.length > 0) {
                    angular.forEach(listtestselected, function (item) {
                        item.isSelected = false;
                    });
                }

                var listtestselectedunique = $filter('filter')(vm.listtest, {
                    'isSelectedUnique': true,
                    'testId': '!' + currentTest.testId
                });
                if (listtestselectedunique.length > 0) {
                    listtestselectedunique[0].isSelectedUnique = false;
                }

                currentTest.isSelectedUnique = !currentTest.isSelectedUnique;

                if (currentTest.isSelectedUnique === true) {

                    vm.selectedTest = currentTest;
                    vm.repetitionButton = vm.selectedTest.state > 1;

                    vm.selectedTestId = currentTest.testId;
                    vm.selectedTest.resultComment.pathology2 = vm.selectedTest.resultComment.pathology === 1 ? true : false;
                    if (currentTest.entryTestType === 1) {
                        vm.microbiologyGrowth = moment(currentTest.microbiologyGrowth.dateGrowth).format(vm.formatDateHours) + ' - ' + currentTest.microbiologyGrowth.userGrowth.userName;
                    } else {
                        vm.microbiologyGrowth = 'N/A'
                    }

                    if (currentTest.block.blocked) {
                        vm.enabledunblockedtest = (auth.id == currentTest.block.user.id) || auth.user.administrator;
                        currentTest.block.dateFormatted = moment(currentTest.block.date).format(vm.formatDateHours);
                    }
                }
            }


            vm.listtestselected = $filter('filter')(vm.listtest, {
                'isSelected': true
            });
        }

        function changechecktest(event, Test) {
            var listtestselectedunique = $filter('filter')(vm.listtest, {
                'isSelectedUnique': true
            });
            if (listtestselectedunique.length > 0) {
                listtestselectedunique[0].isSelectedUnique = false;
            }

            vm.listtestselected = $filter('filter')(vm.listtest, {
                'isSelected': true
            });
            if (vm.listtestselected.length === 0) {
                vm.selectedTest = [];
                vm.selectedTestId = null;
                vm.microbiologyGrowth = '';
            } else if (vm.listtestselected.length === 1) {
                vm.selectedTest = vm.listtestselected[0];

                vm.selectedTestId = vm.selectedTest.testId;
                vm.selectedTest.resultComment.pathology2 = vm.selectedTest.resultComment.pathology === 1 ? true : false;
                if (vm.selectedTest.entryTestType === 1) {
                    vm.microbiologyGrowth = moment(vm.selectedTest.microbiologyGrowth.dateGrowth).format(vm.formatDateHours) + ' - ' + vm.selectedTest.microbiologyGrowth.userGrowth.userName;
                } else {
                    vm.microbiologyGrowth = 'N/A'
                }
            }
        }

        //Metodo actualizacion del resultado y estado de una examen
        function updateTest(Test, newState) {
            if (vm.countresult === 0) {
                vm.tempresult = true;
                vm.referenceButton = false;
                vm.loading = true;
                vm.countresult = 1;
                setTimeout(function () { vm.countresult = 0; }, 500);
                var auth = localStorageService.get('Enterprise_NT.authorizationData');

                Test.newState = newState;
                Test.resultChanged = true;
                Test.entryType = 0;


                if (Test.resultChanged && Test.resultType == 1 && !isNaN(Test.result)) {
                    //var unmaskResult = Inputmask.unmask(Test.result, { alias: 'numeric'});
                    //Test.result = unmaskResult;
                }

                if (Test.resultType === 1 && Test.result.indexOf(',') !== -1) {
                    Test.result = Test.result.replace(',', '.');
                }

                if (Test.resultChanged && Test.resultType == 2) {
                    vm.tempresult = Test.result !== 'MEMO';
                    //Validar resultado por comentario codificado.
                    var codeComment = $filter('filter')(vm.codeCommentList, function (e) {
                        return e.code === Test.result
                    });
                    if (codeComment != undefined && codeComment != null && codeComment.length > 0) {
                        if (codeComment[0].message.length > 16) {
                            if (Test.resultComment.hasComment) {
                                var TestresultComment = Test.resultComment.comment === undefined || Test.resultComment.comment === '' ? '' : Test.resultComment.comment.replace(/span/g, 'font');
                                TestresultComment = TestresultComment === undefined || TestresultComment === '' ? '' : TestresultComment.replace(new RegExp("<p", 'g'), "<div");
                                TestresultComment = TestresultComment === undefined || TestresultComment === '' ? '' : TestresultComment.replace(new RegExp("</p>", 'g'), "</div>");

                                Test.resultComment.comment = TestresultComment + ' ' + codeComment[0].message;
                            } else {
                                Test.resultComment.comment = codeComment[0].message;
                            }
                            Test.result = 'MEMO';
                            Test.resultComment.commentChanged = true;
                        } else {
                            Test.result = codeComment[0].message;
                        }
                    }
                }

                if (Test.resultComment.comment === '' || Test.resultComment.comment === null) {
                    Test.resultComment.comment = null;
                } else {
                    var TestresultComment = Test.resultComment.comment === undefined || Test.resultComment.comment === '' ? '' : Test.resultComment.comment.replace(/span/g, 'font');
                    TestresultComment = TestresultComment === undefined || TestresultComment === '' ? '' : TestresultComment.replace(new RegExp("<p", 'g'), "<div");
                    TestresultComment = TestresultComment === undefined || TestresultComment === '' ? '' : TestresultComment.replace(new RegExp("</p>", 'g'), "</div>");
                    Test.resultComment.comment = TestresultComment;
                }
                Test.idUser = auth.id
                return resultsentryDS.updateTest(auth.authToken, Test).then(function (data) {
                    if (data.status === 200) {
                        Test.resultRepetition = {};
                        Test.result = data.data.result;
                        Test.state = data.data.state;
                        Test.pathology = data.data.pathology;
                        Test.repeatedResultValue = data.data.repeatedResultValue;
                        Test.orderPathology = data.data.orderPathology;

                        Test.resultChanged = data.data.resultChanged;
                        Test.resultComment.commentChanged = data.data.resultComment.commentChanged;
                        Test.resultComment.hasComment = data.data.hasComment;
                        Test.resultDate = data.data.resultDate;
                        Test.hasComment = data.data.hasComment
                        Test.resultedit = null;
                        Test.print = data.data.print;
                        vm.testedit = null;
                        vm.loading = false;
                        updateButtonBarByTest();

                    }
                }, function (error) {
                    vm.modalError(error);
                });
            }
            else {
                vm.countresult = 0
            }


        }

        /**
           Funcion  Selecciona el examen activo
           @author  jblanco
         */
        function selectTest(currentTest, check, control) {
            //$event.preventDefault();
            //currentTest.resultComment.pathology2 = currentTest.resultComment.pathology2 == 1;


            /*  var listtestselectedunique = $filter('filter')(vm.listtest, function(e) {
                 return e.isSelectedUnique === true && e.testId !== currentTest.testId
             });
             if (listtestselectedunique.length > 0) {
                 listtestselectedunique[0].isSelectedUnique = false;
             }
 
             if (check) {
 
                 vm.selectedTest = $filter('filter')(vm.selectedTest, {
                     'testId': '!' + currentTest.testId
                 });
                 if (currentTest.isSelected === true) {
                     vm.selectedTest.push(currentTest);
                 }
             } else {
 
                 if (currentTest.block.blocked === false && currentTest.grantAccess) {
                     var listtestselected = $filter('filter')(vm.listtest, function(e) {
                         return e.isSelected === true || e.isSelectedUnique === true
                     });
 
                     if (listtestselected.length > 0) {
                         angular.forEach(listtestselected, function(item) {
                             item.isSelected = false;
                             item.isSelectedUnique = false;
                         });
                     }
                     vm.selectedTest = []
                     currentTest.isSelectedUnique = !currentTest.isSelectedUnique;
                     if (currentTest.isSelectedUnique === true) {
                         vm.selectedTest.push(currentTest)
                     }
                 }
             } */

            if (control === 'checkbox') {
                var listtestselected = [];
                var listtestselected = $filter('filter')(vm.listtest, function (e) {
                    return e.isSelectedUnique === true
                });
                if (listtestselected.length > 0) {
                    angular.forEach(listtestselected, function (item) {
                        item.isSelectedUnique = false;
                    });
                }
                vm.selectedTest = $filter("filter")(vm.listtest, function (e) {
                    if (e.isSelected) {
                        e.isSelectedUnique = true;
                    }
                    return e.isSelected === true;
                })

            } else {
                if (currentTest.block.blocked === false && currentTest.grantAccess) {
                    var listtestselected = [];
                    var listtestselected = $filter('filter')(vm.listtest, function (e) {
                        return e.isSelected === true || e.isSelectedUnique === true
                    });
                    if (listtestselected.length > 0) {
                        angular.forEach(listtestselected, function (item) {
                            // item.isSelected = false;
                            item.isSelectedUnique = false;
                        });
                    }
                    vm.selectedTest = []
                    currentTest.isSelectedUnique = !currentTest.isSelectedUnique;
                    //  currentTest.isSelected = true;
                    if (currentTest.isSelectedUnique === true) {
                        vm.selectedTest.push(currentTest)
                    }
                }
            }

            if (currentTest.literalResult !== undefined) {
                vm.listfilterliteralResult = JSON.parse(JSON.stringify(currentTest.literalResult));
            }


            updateButtonBarByTest();
        }

        vm.deletedResult = deletedResult;
        function deletedResult() {
            vm.moveNextTest(40, vm.testedit);
            vm.updateTest(vm.testedit, vm.testState.ORDERED);
        }



        /**
          Funcion  Actualiza el estado de la barra de botones.
          @author  jblanco
        */
        function updateButtonBarByTest() {
            vm.commentButton = false;
            vm.repetitionlistButton = false;
            vm.repetitionButton = false;
            vm.microbiologytaskButton = false;
            var cantdata = vm.selectedTest.length;
            if (cantdata == 1) {
                vm.repetitionlistButton = !vm.selectedTest[0].block.blocked;
                vm.commentButton = !vm.selectedTest[0].block.blocked && vm.selectedTest[0].state <= vm.testState.PREVIEW;
                vm.repetitionButton = vm.selectedTest[0].state === vm.testState.REPORTED && vm.permissionuser.quitValidation === true ||
                    vm.selectedTest[0].state > vm.testState.REPORTED && vm.permissionuser.quitValidation === true;
                vm.microbiologytaskButton = !vm.selectedTest[0].block.blocked && vm.selectedTest[0].entryTestType !== 2

            }
            var authuser = localStorageService.get('Enterprise_NT.authorizationData');
            var administrator = authuser.administrator;
            vm.desvalidateButton = vm.listtest.length !== 0 ? ((_.filter(vm.listtest, function (o) {
                return (o.state === vm.testState.VALIDATED && !o.block.blocked && o.grantAccess && administrator) ||
                    (o.state === vm.testState.PRINTED && !o.block.blockdays && !o.block.blocked && o.grantAccess && administrator) ||
                    (o.state === vm.testState.PRINTED && !o.block.blockdays && !o.block.blocked && o.grantAccess && !administrator && user === o.validationUserId) ||
                    (o.state === vm.testState.VALIDATED && !o.block.blocked && o.grantAccess && !administrator && user === o.validationUserId)
            })).length > 0) : false;
            vm.microbiologyButton = vm.selectedTest.length == 1 && vm.selectedTest[0].laboratoryType.indexOf('3') !== -1 && !vm.selectedTest[0].block.blocked && vm.selectedTest[0].state <= vm.testState.PREVIEW;
            vm.historyButton = vm.selectedTest.length > 0 && vm.selectedTest[0].state > vm.testState.RERUN;
        }

        /**
              Funcion  Desbloquea la prueba seleccionada
              @author  jblanco
              @version 0.0.1
            */

        function resultKeyEvent(currentTest, event) {
            currentTest.resultRepetition = null;
            if (currentTest.state > vm.testState.REPORTED) {
                if (event.keyCode !== 8 || event.keyCode !== 9 || event.keyCode !== 40 || event.keyCode !== 38 || event.keyCode !== 13) {
                    event.preventDefault();
                }
            }
            currentTest.resultRepetition = null;
            if (currentTest.literalResult === undefined) {
                currentTest.literalResult = [];
            }
            if (currentTest.literalResult.length !== 0) {
                currentTest.result = document.getElementById('input_result-' + currentTest.testId + '_value').value;
            }
            var keyCode = event !== undefined ? event.keyCode : 100000;
            if (keyCode == 40 || keyCode == 38 || keyCode == 13 || keyCode == 100000) {
                if (currentTest.result === ' ' || currentTest.result === '  ') {
                    currentTest.result = currentTest.result.trim();
                }
                if (currentTest.result === '' && currentTest.resultedit !== '' && currentTest.state == vm.testState.REPORTED ||
                    currentTest.result === '' && currentTest.resultedit !== null && currentTest.state == vm.testState.REPORTED) {
                    vm.testedit = currentTest;
                    UIkit.modal('#modaldeleteresult', {
                        bgclose: false,
                        escclose: false,
                        modal: false
                    }).show();
                } else if (currentTest.result === '' || currentTest.result === null && currentTest.state == vm.testState.REPORTED) {
                    logger.warning('El resultado no puede estar vacio');
                    currentTest.result = currentTest.resultedit
                }
                var validar = currentTest.result === null ? '' : currentTest.result;
                if (currentTest.result !== undefined && validar.length > 0 && currentTest.state <= vm.testState.REPORTED) {
                    if (currentTest.resultType === 1) {
                        if (currentTest.result !== '' || currentTest.result !== null) {
                            if (currentTest.result[currentTest.result.length - 1] === ".") {
                                currentTest.result = currentTest.result.replace(".", "");
                            }
                        }
                        if (isNaN(parseFloat(currentTest.result)) && currentTest.result !== vm.separator + vm.separator) {
                            currentTest.result = currentTest.resultedit
                            logger.error('Este resultado debe ser númerico');
                        } else if (currentTest.reportedMax === currentTest.reportedMin) {
                            if (currentTest.resultedit !== null && currentTest.resultedit !== currentTest.result && currentTest.state === vm.testState.REPORTED) {

                                vm.testedit = currentTest;
                                UIkit.modal('#modaleditresult', {
                                    bgclose: false,
                                    escclose: false,
                                    modal: false
                                }).show();


                            } else if (currentTest.state < vm.testState.REPORTED) {
                                if (keyCode !== 100000) {
                                    vm.comparedtes = currentTest.testId;
                                    vm.updateTest(currentTest, vm.testState.REPORTED);
                                    vm.moveNextTest(keyCode, currentTest);
                                } else {
                                    if (vm.comparedtes !== currentTest.testId) {
                                        vm.updateTest(currentTest, vm.testState.REPORTED);
                                    }
                                }
                            } else if (keyCode !== 100000) {
                                vm.moveNextTest(keyCode, currentTest);
                            }

                        } else if (currentTest.reportedMin < parseFloat(currentTest.result) && parseFloat(currentTest.result) < currentTest.reportedMax) {
                            if (currentTest.resultedit !== null && currentTest.resultedit !== currentTest.result && currentTest.state === vm.testState.REPORTED) {

                                vm.testedit = currentTest;
                                UIkit.modal('#modaleditresult', {
                                    bgclose: false,
                                    escclose: false,
                                    modal: false
                                }).show();

                            } else if (currentTest.state < vm.testState.REPORTED) {
                                if (keyCode !== 100000) {
                                    vm.comparedtes = currentTest.testId;
                                    vm.updateTest(currentTest, vm.testState.REPORTED);
                                    vm.moveNextTest(keyCode, currentTest);
                                } else {
                                    if (vm.comparedtes !== currentTest.testId) {
                                        vm.updateTest(currentTest, vm.testState.REPORTED);
                                    }
                                }
                            } else if (keyCode !== 100000) {
                                vm.moveNextTest(keyCode, currentTest);
                            }

                        } else {
                            currentTest.result = currentTest.resultedit;
                            logger.warning($filter('translate')('1444'));
                        }
                    } else {
                        if (currentTest.resultedit !== null && currentTest.resultedit !== currentTest.result && currentTest.state === vm.testState.REPORTED) {

                            vm.testedit = currentTest;
                            UIkit.modal('#modaleditresult', {
                                bgclose: false,
                                escclose: false,
                                modal: false
                            }).show();

                        } else if (currentTest.state < vm.testState.REPORTED) {
                            if (keyCode !== 100000) {
                                vm.comparedtes = currentTest.testId;
                                vm.updateTest(currentTest, vm.testState.REPORTED);
                                vm.moveNextTest(keyCode, currentTest);
                            } else {
                                if (vm.comparedtes !== currentTest.testId) {
                                    vm.updateTest(currentTest, vm.testState.REPORTED);
                                }
                            }
                        } else if (keyCode !== 100000) {
                            vm.moveNextTest(keyCode, currentTest);
                        }
                    }

                } else {
                    var addview = currentTest.result === '' && currentTest.resultedit !== '' && currentTest.state == vm.testState.REPORTED ||
                        currentTest.result === '' && currentTest.resultedit !== null && currentTest.state == vm.testState.REPORTED;

                    if (keyCode !== 100000 && !addview) {
                        vm.moveNextTest(keyCode, currentTest);
                    };
                }

            }
        }

        vm.moveNextTest = moveNextTest;
        function moveNextTest(keyCode, currentTest) {
            var direction = undefined;
            var obj = {};
            var done = false;
            var last = null;
            var next = null;
            var tempnext = null;
            if (tempnext === null) {
                tempnext = _.filter(vm.listtest, function (e) { return e.idindex === 0 })[0];
            }
            if (currentTest.idindex !== 0) {
                last = _.filter(vm.listtest, function (e) { return e.idindex === currentTest.idindex - 1 })[0];
            }
            next = _.filter(vm.listtest, function (e) { return e.idindex === currentTest.idindex + 1 })[0];
            direction = keyCode == 38 ? last.testId : next === undefined ? tempnext.testId : next.testId === undefined ? last.testId : next.testId;
            if (direction != undefined && event !== undefined) {
                angular.forEach(vm.listtest, function (itemi) {
                    if (itemi.testId == direction) {
                        obj = itemi;
                        done = true;
                        return;
                    }
                });
                if (obj.literalResult === undefined) {
                    obj.literalResult = [];
                }
                vm.listfilterliteralResult = JSON.parse(JSON.stringify(obj.literalResult));
                setTimeout(function () {
                    if (obj.literalResult.length !== 0) {
                        angular.element('#input_result-' + direction + '_value').focus();
                        angular.element('#input_result-' + direction + '_value').select();
                    } else {
                        angular.element('#input_result-' + direction).focus();
                        angular.element('#input_result-' + direction).select();
                    }
                    vm.selectTest(obj, false, 'input');
                }, 100);
            }
        }



        /**
     Funcion  Selecciona la prueba actual y permite visualizar la información
     @author  jblanco
   */
        function testInformation(currentTest) {
            vm.currentTest = currentTest;
            vm.openinformation = true;
        }

        /**
          Funcion  Selecciona la prueba actual y permite visualizar la ventana de bloqueo
          @author  jblanco
        */
        function blockTest(currentTest, blocked) {
            var auth = localStorageService.get('Enterprise_NT.authorizationData');
            vm.currentTest = currentTest;
            vm.opentestblock = blocked;
            vm.enabledunblockedtest = false;
            if (!blocked) {
                vm.enabledunblockedtest = (auth.id == vm.currentTest.block.user.id) || auth.administrator;
                vm.currentTest.block.dateFormatted = moment(vm.currentTest.block.date).format(vm.formatDateHours);
            }
        }

        /**
            Funcion  Registrel bloqueo o desbloqueo de una prueba
            @author  jblanco
            @version 0.0.1
        */
        function setBlockedTest(blocked) {
            var auth = localStorageService.get('Enterprise_NT.authorizationData');

            var blockedData = {};

            blockedData.order = vm.currentTest.order;
            blockedData.testId = vm.currentTest.testId;
            blockedData.blocked = blocked;

            if (blocked) {
                if (vm.motiveBlockComment == '')
                    vm.motiveBlockComment = null;

                blockedData.reasonId = vm.motiveBlock.id;
                blockedData.reasonComment = vm.motiveBlockComment;

                blockedData.user = {};
                blockedData.user.id = auth.id;
                blockedData.user.userName = auth.userName;
                blockedData.user.lastName = auth.lastName;
                blockedData.user.name = auth.name;
            }

            return resultsentryDS.blockTest(auth.authToken, blockedData).then(function (data) {
                if (data.status === 200) {
                    vm.currentTest.block = data.data;
                }
            }, function (error) {
                vm.modalError(error);
            });
        }

        /**
          Funcion  Bloquea la prueba seleccionada
          @author  jblanco
          @version 0.0.1
        */
        function saveblocktest() {
            setBlockedTest(true);
        }

        /**
          Funcion  Desbloquea la prueba seleccionada
          @author  jblanco
          @version 0.0.1
        */
        function saveunblocktest() {
            setBlockedTest(false);
        }

        //Metodo para cargar la foto del paciente.
        function loadphotopatient(idpatient) {
            var auth = localStorageService.get('Enterprise_NT.authorizationData');
            patientDS.getPhotoPatient(auth.authToken, idpatient).then(
                function (response) {
                    if (response.statusText === 'OK') {
                        vm.photopatient = response.data.photoInBase64;
                    }
                },
                function (error) {
                    if (error.data === null) {
                        vm.Error = error;
                        vm.ShowPopupError = true;
                    }

                });
        }

        /**
              Funcion  Realiza el guardado de la edicion de una resultado
              @author  adiaz
              @version 0.0.1
            */
        function editResult() {
            vm.testedit.resultRepetition = {};
            vm.testedit.resultRepetition.result = vm.testedit.resultedit;
            vm.testedit.resultRepetition.order = vm.testedit.order;
            vm.testedit.resultRepetition.testId = vm.testedit.testId;
            vm.testedit.resultRepetition.type = 'M';
            vm.testedit.resultRepetition.reasonId = vm.reasonResultEdit === true ? vm.motiveEditResult.id : null;
            vm.testedit.resultRepetition.reasonComment = vm.reasonResultEdit === true ? vm.reasonCommentEditResult : null;
            vm.testedit.resultRepetition.repetitionDate = null;
            vm.moveNextTest(40, vm.testedit);
            vm.updateTest(vm.testedit, vm.testState.REPORTED);
        }

        /**
          Funcion  Realiza el guardado de la edicion de una resultado
          @author  adiaz
          @version 0.0.1
        */
        function canceleditresult() {
            vm.testedit.result = vm.testedit.resultedit;
            if (vm.testedit.literalResult === undefined) {
                vm.testedit.literalResult = [];
            }
            if (vm.testedit.literalResult.length !== 0) {
                $scope.$broadcast("angucomplete-alt:changeInput", "input_result-" + vm.testedit.testId, vm.testedit.result);
            }
            vm.moveNextTest(40, vm.testedit);
        }


        ///////////////  Metodos botonera grilla de resultados  ////

        //** Método para validar cuando se retoma una muestra**//
        function postponement() {
            logger.success('La muestra a sido retomada');
            vm.reloadgridresult();
        }

        function validateTestsPreview() {
            var newState = vm.testState.PREVIEW;
            var auth = localStorageService.get('Enterprise_NT.authorizationData');
            var obj = {};
            var tests = [];

            vm.loading = true;

            var listTestUnique = _.filter(vm.listtest, function (itemi) {
                return itemi.isSelected;
            });

            tests = listTestUnique.length > 0 ? listTestUnique : vm.listtest

            var tests = _.filter(tests, function (itemi) {
                itemi.newState = newState
                return !itemi.block.blocked && itemi.grantAccess && itemi.grantValidate && itemi.state === vm.testState.REPORTED && itemi.preliminaryValidation === true;
            });


            obj.finalValidate = false;
            obj.orderId = vm.orderselect;
            obj.sex = vm.patient.genderobject;
            obj.race = vm.patient.raceid;
            obj.size = vm.patient.size;
            obj.weight = vm.patient.weight;
            obj.tests = tests;
            obj.questions = [];
            obj.alarms = [];

            if (tests.length > 0) {

                obj.questions = vm.panicQuestions;
                return resultsentryDS.validateTests(auth.authToken, obj).then(function (data) {
                    if (data.status === 200) {
                        vm.reloadgridresult();
                        updateButtonBarByTest();
                        vm.loading = false;
                    }
                }, function (error) {
                    vm.loading = false;
                    vm.modalError(error);
                });

            } else {
                logger.warning('¡Ninguna prueba puede ser pre-validada!')
                vm.loading = false;

            }
        }

        //Metodo trazabilidad de la muestra
        function trackingmicrobiologytask() {
            vm.getmediacultures();
            vm.getprocedure();
            var auth = localStorageService.get('Enterprise_NT.authorizationData');
            var customer = vm.abbrCustomer + ' (' + vm.nameCustomer + ')'
            vm.variables = {
                'customer': customer,
                'username': auth.userName,
                'now': moment().format(vm.formatDateHours)
            };
            return microbiologyDS.getlisttrackingmicrobiologytasks(auth.authToken, vm.orderselect, vm.selectedTest[0].testId).then(function (data) {
                vm.pathreport = '/Report/post-analitic/trackingmicrobiologytask/trackingmicrobiologytask.mrt';
                vm.orientation = 'vertical';
                vm.datareport = vm.generateDataReport(data.data);
                vm.progressPrint = false;
                vm.openreport = false;
                vm.windowOpenReport();
            });
        }

        function savecommenttest() {
            vm.selectedTest[0].resultChanged = false;
            vm.selectedTest[0].resultComment.commentChanged = true;
            if (vm.selectedTest[0].result === undefined) {
                vm.selectedTest[0].result = 'MEMO';
                vm.selectedTest[0].resultChanged = true;
            }
            vm.selectedTest[0].resultRepetition = null;
            vm.selectedTest[0].entryType = 0;
            vm.selectedTest[0].resultComment.pathology = vm.selectedTest[0].resultComment.pathology2 ? 1 : 0;

            vm.updateTest(vm.selectedTest[0], vm.testState.REPORTED);

        }


        /**
     Funcion  Realiza la repeticion del resultado de los examenes seleccionados
     @author  adiaz
     @version 0.0.1
   */
        function saverepeattest() {
            if (vm.reasonComment == '')
                vm.reasonComment = null;
            vm.loading = true;
            vm.selectedTest.forEach(function (item, key) {
                if (item.state >= vm.testState.REPORTED) {
                    item.resultRepetition = {};
                    item.resultRepetition.order = item.order;
                    item.resultRepetition.testId = item.testId;
                    item.resultRepetition.type = 'R';
                    item.resultRepetition.reasonId = vm.motiveRepeat.id;
                    item.resultRepetition.reasonComment = vm.reasonComment;
                    item.resultRepetition.repetitionDate = null;
                    vm.updateTest(item, vm.testState.RERUN);
                    item.isSelected = false;

                }
                if (key === vm.selectedTest.length - 1) {
                    vm.loading = false;
                }
            });
        }

        function getPanicSurvey() {
            var auth = localStorageService.get('Enterprise_NT.authorizationData');

            return resultsentryDS.getPanicSurvey(auth.authToken).then(function (data) {
                if (data.status === 200) {
                    vm.panicSurvey = data.data;
                }
            }, function (error) {
                vm.modalError(error);
            });
        }

        /**
              Funcion  desvalida los resultados de las pruebas
              @author  jblanco
              @return  data Objeto de tipo ResultTest
              @version 0.0.1
        */
        function desvalidateTests() {
            vm.loading = true;
            var datavalidate = [];
            var listTestUnique = [];

            var listTestUnique = _.filter(vm.listtest, function (itemi) {
                return itemi.isSelected;
            });
            datavalidate = listTestUnique.length > 0 ? listTestUnique : vm.selectedTest.length == 0 ? vm.listtest : JSON.parse(JSON.stringify(vm.selectedTest));
            var auth = localStorageService.get('Enterprise_NT.authorizationData');
            var administrator = auth.administrator;
            var user = auth.id;
            var validated = _.filter(datavalidate, function (o) {
                return (o.state === vm.testState.VALIDATED && !o.block.blocked && o.grantAccess && administrator) ||
                    (o.state === vm.testState.PRINTED && !o.block.blockdays && !o.block.blocked && o.grantAccess && administrator) ||
                    (o.state === vm.testState.PRINTED && !o.block.blockdays && !o.block.blocked && o.grantAccess && !administrator && user === o.validationUserId) ||
                    (o.state === vm.testState.VALIDATED && !o.block.blocked && o.grantAccess && !administrator && user === o.validationUserId)
            });
            if (validated.length !== 0) {
                vm.tempresult = true;
                vm.validatedbutton = true;
                vm.size = validated.length;
                vm.count = 0;
                vm.datadevalidated = validated;
                vm.testdevalidated();
            } else {
                vm.loading = false;
            }
        }
        vm.testdevalidated = testdevalidated;
        function testdevalidated() {
            vm.tempresult = true;
            vm.validatedbutton = true;
            vm.tempresult = true;
            var auth = localStorageService.get('Enterprise_NT.authorizationData');
            vm.datadevalidated[vm.count].newState = vm.testState.REPORTED;
            vm.datadevalidated[vm.count].resultChanged = true;
            vm.datadevalidated[vm.count].entryType = 0;
            vm.datadevalidated[vm.count].resultComment.pathology = vm.datadevalidated[vm.count].resultComment.pathology2 ? 1 : 0;
            if (vm.datadevalidated[vm.count].resultType === 1 && vm.datadevalidated[vm.count].result.indexOf(',') !== -1) {
                vm.datadevalidated[vm.count].result = vm.datadevalidated[vm.count].result.replace(',', '.');
            }
            if (vm.datadevalidated[vm.count].resultChanged && vm.datadevalidated[vm.count].resultType == 2) {
                vm.tempresult = vm.datadevalidated[vm.count].result !== 'MEMO';
                var codeComment = $filter('filter')(vm.codeCommentList, function (e) {
                    return e.code === vm.datadevalidated[vm.count].result
                });
                if (codeComment != undefined && codeComment != null && codeComment.length > 0) {
                    if (codeComment[0].message.length > 16) {
                        if (vm.datadevalidated[vm.count].resultComment.hasComment) {
                            vm.datadevalidated[vm.count].resultComment.comment = vm.datadevalidated[vm.count].resultComment.comment + ' ' + codeComment[0].message;
                        } else {
                            vm.datadevalidated[vm.count].resultComment.comment = codeComment[0].message;
                        }
                        vm.datadevalidated[vm.count].result = 'MEMO';
                        validated[vm.count].resultComment.commentChanged = true;
                    } else {
                        vm.datadevalidated[vm.count].result = codeComment[0].message;
                    }
                }
            }
            if (vm.datadevalidated[vm.count].resultComment.comment == '') {
                vm.datadevalidated[vm.count].resultComment.comment = null;
            }
            vm.datadevalidated[vm.count].idUser = auth.id
            if (vm.tempresult || vm.validatedbutton) {
                var auth = localStorageService.get('Enterprise_NT.authorizationData');
                return resultsentryDS.updateTest(auth.authToken, vm.datadevalidated[vm.count]).then(function (data) {
                    if (data.status === 200) {
                        vm.datadevalidated[vm.count].resultRepetition = {};
                        vm.datadevalidated[vm.count].result = data.data.result;
                        vm.datadevalidated[vm.count].state = data.data.state;
                        vm.datadevalidated[vm.count].pathology = data.data.pathology;
                        vm.datadevalidated[vm.count].repeatedResultValue = data.data.repeatedResultValue;
                        vm.datadevalidated[vm.count].orderPathology = data.data.orderPathology;
                        vm.datadevalidated[vm.count].resultChanged = data.data.resultChanged;
                        vm.datadevalidated[vm.count].resultComment.commentChanged = data.data.resultComment.commentChanged;
                        vm.datadevalidated[vm.count].resultComment.hasComment = data.data.hasComment;
                        vm.datadevalidated[vm.count].resultDate = data.data.resultDate;
                        vm.datadevalidated[vm.count].hasComment = data.data.hasComment
                        vm.datadevalidated[vm.count].resultedit = null;
                        vm.testedit = null;
                        if (vm.size - 1 === vm.count) {
                            vm.desvalidateButton = false;
                            vm.selectedTest = [];
                            vm.reloadgridresult();
                            vm.loading = false;
                        } else {
                            vm.count++;
                            vm.testdevalidated();
                        }
                    }
                }, function (error) {
                });
            }
        }


        function validateTestsWarnings(next) {
            vm.validateAndNext = next;
            vm.validateTestsBase(true);
        }


        function validateTests() {
            vm.validateTestsBase(false);
        }



        function validateTestsBase(isAlarms) {
            var newState = vm.testState.VALIDATED;
            var auth = localStorageService.get('Enterprise_NT.authorizationData');
            var obj = {};
            var tests = [];

            vm.loading = true;

            var listTestUnique = _.filter(vm.listtest, function (itemi) {
                return itemi.isSelected;
            });

            tests = listTestUnique.length > 0 ? listTestUnique : vm.listtest

            var tests = _.filter(tests, function (itemi) {
                itemi.newState = newState
                return !itemi.block.blocked && itemi.grantAccess && itemi.grantValidate && (itemi.state === vm.testState.REPORTED && itemi.preliminaryValidation === false) || (itemi.state === vm.testState.PREVIEW);
            });


            obj.finalValidate = true;
            obj.orderId = vm.orderselect;
            obj.sex = vm.patient.genderobject;
            obj.race = vm.patient.raceid;
            obj.size = vm.patient.size;
            obj.weight = vm.patient.weight;
            obj.tests = tests;
            obj.questions = [];
            obj.alarms = [];

            if (tests.length > 0) {
                if (isAlarms) {
                    return resultsentryDS.validateTestsAlarms(auth.authToken, obj).then(function (data) {
                        if (data.status === 200) {
                            // vm.orderTests = _.groupBy(data.data.tests, 'areaName');
                            // updateButtonBarByTest();
                            //TODO: Filtrar examenes con panico y delta.
                            var panicTest = $filter('filter')(data.data.tests, function (o) {
                                return (o.newState == 4 && o.pathology > 3);
                            });
                            //var panicTest = data.data.tests;
                            if (data.data.alarms.length > 0 || (vm.panicSurvey.length > 0 && panicTest.length > 0)) {
                                vm.alarms = data.data.alarms;
                                vm.panicTest = panicTest;
                                vm.panicQuestions = angular.copy(vm.panicSurvey);
                                vm.openalarmvalidate = true;
                                vm.loading = false;
                            } else {
                                vm.validateTestsBase(false);
                            }
                        }
                    }, function (error) {
                        vm.loading = false;
                        vm.modalError(error);
                    });
                } else {
                    obj.questions = vm.panicQuestions;
                    return resultsentryDS.validateTests(auth.authToken, obj).then(function (data) {
                        if (data.status === 200) {
                            if (vm.validateAndNext) {
                                vm.listOrder = $filter('orderBy')(vm.listOrder, 'orderNumber')
                                var elementindex = _.findIndex(vm.listOrder, {
                                    'orderNumber': vm.orderselect
                                })
                                if (vm.listOrder[elementindex + 1] !== undefined) {
                                    vm.selectOrder(1, vm.listOrder[elementindex + 1], vm.listOrder[elementindex + 1].orderNumber);
                                }
                            } else {
                                vm.reloadgridresult();

                            }
                            //vm.selectedTest = vm.selectedTest.length === 0 ? vm.orderTestsNotgroup : vm.selectedTest;
                            vm.selectedTest = [];
                            updateButtonBarByTest();
                            vm.loading = false;
                        }
                    }, function (error) {
                        vm.loading = false;
                        vm.modalError(error);
                    });
                }
            } else {
                logger.warning($filter('translate')('1411'))
                vm.loading = false;

            }
        }


        function isAuthenticate() {
            //var auth = null
            var auth = localStorageService.get('Enterprise_NT.authorizationData');
            if (auth === null || auth.token) {
                $state.go('login');
            } else {
                vm.init();
            }
        }

        vm.isAuthenticate();

    }

})();
/* jshint ignore:end */