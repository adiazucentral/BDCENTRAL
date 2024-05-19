/* jshint ignore:start */
(function () {
    'use strict';

    angular
        .module('app.deletespecial')
        .controller('DeletespecialController', DeletespecialController);


    DeletespecialController.$inject = ['motiveDS', 'specialdeletesDS', 'testDS', 'localStorageService', 'logger',
        '$filter', '$state', 'moment', '$rootScope', 'common'];

    function DeletespecialController(motiveDS, specialdeletesDS, testDS, localStorageService,
        logger, $filter, $state, moment, $rootScope, common) {

        var vm = this;

        vm.isAuthenticate = isAuthenticate;
        vm.init = init;
        vm.title = 'Deletespecial';
        $rootScope.pageview = 3;
        $rootScope.menu = true;
        $rootScope.NamePage = $filter('translate')('0050');
        $rootScope.helpReference = '01. LaboratoryOrders/deletespecial.htm';
        vm.isOpenReport = true;
        vm.button = false;
        vm.testprocces = '3';
        vm.save = save;
        vm.getmotive = getmotive;
        vm.rangeInit = '';
        vm.rangeEnd = '';
        vm.rangeInit1 = '';
        vm.rangeEnd1 = '';
        vm.button2 = false;
        vm.rangeInit2 = '';
        vm.rangeEnd2 = '';
        vm.button1 = false;
        vm.gettest = gettest;
        vm.selectedIds = [];
        vm.prepotition = $filter("translate")("0000") === "esCo" ? "de" : "of";
        vm.openpopupmotive = openpopupmotive;
        vm.changetab = changetab;
        vm.formatDateAge = localStorageService.get('FormatoFecha').toUpperCase();
        vm.formatDate = localStorageService.get('FormatoFecha').toUpperCase();
        vm.columtest = columtest;
        vm.selectall = selectall;
        vm.Comment = '';
        vm.gettestdelete = gettestdelete;
        vm.selectalltest = selectalltest;
        vm.PopupError = false;
        vm.modalError = modalError;
        vm.listtest = [];
        vm.listtestdelete = [];
        vm.tab = 1;
        vm.button = button;

        function button(id) {
            vm.testprocces = vm.testprocces === null ? id : vm.testprocces;
        }

        // modal error
        function modalError(error) {
            vm.Error = error;
            vm.ShowPopupError = true;
        }
        // limpia los controles al cambiar de tab
        function changetab(id) {

            vm.tab = id;
            if (id === 1) {
                vm.getmotive(15);
            }
            if (id === 2) {
                vm.getmotive(17);
            }
            if (id === 3) {
                vm.getmotive(15);
            }

            vm.selectAllcheck = false;
            vm.selectAllcheck1 = false;
            vm.search = '';
            vm.search1 = '';
            vm.selectall();
            vm.selectalltest();

        }
        // selecciona todos las pruebas
        function selectall() {
            if (vm.listtest.length > 0) {
                vm.listtest.forEach(function (value, key) {
                    vm.listtest[key].check = vm.selectAllcheck;
                });
            }
        }
        // selecciona todas la pruebas
        function selectalltest() {
            if (vm.listtestdelete.length > 0) {
                vm.listtestdelete.forEach(function (value, key) {
                    vm.listtestdelete[key].check = vm.selectAllcheck1;
                });
            }
        }
        // obtiene los motivos para borrar la orden
        function getmotive(idmotive) {
            vm.typemotiveid = idmotive;
            var auth = localStorageService.get('Enterprise_NT.authorizationData');
            return motiveDS.getMotiveByState(auth.authToken).then(function (data) {
                if (data.status === 200) {
                    vm.typemotive = data.data;
                    if (data.data.length !== 0) {
                        vm.motive = { id: -1 };
                        vm.Listmotive = $filter('filter')(vm.typemotive, { type: { id: idmotive } });
                        if (vm.Listmotive.length === 0) {
                            UIkit.modal('#deletemodal').show();
                        }
                    }
                } else {
                    UIkit.modal('#deletemodal').show();
                }

            }, function (error) {

                vm.modalError(error);

            });
        }
        // muestra el popup de motivo y consulta cuantas ordenes se van eliminar
        function openpopupmotive(type) {
            vm.Comment = '';
            vm.motive.id = -1;
            // modificación de ingreso
            if (type === 1) {
                vm.getmotive(15);
                vm.deleteType = type;
                vm.orderinicial = vm.rangeInit;
                vm.orderfinal = vm.rangeEnd;
                var numberdelete = {
                    "deleteType": vm.deleteType,
                    "init": vm.orderinicial,
                    "end": vm.orderfinal,
                    "testsList": []
                }
            }
            // modificación de resultado
            if (type === 2) {
                vm.orderinicial = vm.rangeInit1;
                vm.orderfinal = vm.rangeEnd1;
                vm.getmotive(17);
                vm.deleteType = type;
                vm.Listtestresult = _.filter(vm.listtest, function (o) { return o.check; });
                vm.Listtestresult = _.map(vm.Listtestresult, 'id');
                var numberdelete = {
                    'deleteType': vm.deleteType,
                    'init': vm.orderinicial,
                    'end': vm.orderfinal,
                    'testsList': vm.Listtestresult
                };
            }
            if (type === 3) {
                // modificación de ingreso
                if (vm.testprocces === '3') {
                    vm.deleteType = 3;
                }
                if (vm.testprocces === '4') {
                    vm.deleteType = 4;
                }
                if (vm.testprocces === '5') {
                    vm.deleteType = 5;
                }
                vm.orderinicial = vm.rangeInit2;
                vm.orderfinal = vm.rangeEnd2;
                vm.getmotive(15);
                vm.Listtestresult = _.filter(vm.listtestdelete, function (o) { return o.check; });
                vm.Listtestresult = _.map(vm.Listtestresult, 'id');
                var numberdelete = {
                    'deleteType': vm.deleteType,
                    'init': vm.orderinicial,
                    'end': vm.orderfinal,
                    'testsList': vm.Listtestresult
                };
            }
            var auth = localStorageService.get('Enterprise_NT.authorizationData');
            return specialdeletesDS.queryspecialdeletes(auth.authToken, numberdelete).then(function (data) {
                if (data.status === 200) {
                    vm.numberdelete = data.data;
                    if (vm.numberdelete.length !== 0) { UIkit.modal('#deletemodal').show(); } else {

                        if (vm.tab === 1) {
                            logger.success($filter('translate')('0348'));
                        }
                        if (vm.tab === 2) {
                            logger.success($filter('translate')('0734'));
                        }
                        if (vm.tab === 3) {
                            logger.success($filter('translate')('0735'));
                        }

                    }
                }

            }, function (error) {
                vm.listtestinvalid = [];
                if (error.data !== null) {
                    if (error.data.code === 2) {
                        vm.Comment = '';
                        vm.selectAllcheck1 = false;
                        vm.selectalltest();
                        vm.motive.id = -1;
                        error.data.errorFields.forEach(function (value) {
                            var item = value.split('|');
                            if (item[0] === '3' && item[1] === 'tests') {
                                var test = {
                                    'order': item[2]
                                };
                                vm.listtestinvalid.push(test);
                            }
                        });
                        UIkit.modal('#testinvalid').show();
                    }
                } else {


                    vm.modalError(error);
                }

            });

        }
        // obtiene las pruebas para el 2 tab
        function gettest() {
            vm.listtest = [];
            var auth = localStorageService.get('Enterprise_NT.authorizationData');
            return testDS.getTestArea(auth.authToken, 0, 1, 0).then(function (data) {
                vm.gettestdelete();
                if (data.status === 200) {
                    vm.lisdatatest = [];
                    data.data.forEach(function (value) {
                        var test = {
                            'id': value.id,
                            'code': value.code,
                            'abbr': value.code,
                            'name': value.name
                        };
                        vm.lisdatatest.push(test);

                    });
                    vm.listtest = vm.lisdatatest;
                }
            }, function (error) {

                vm.modalError(error);

            });
        }
        //  // obtiene las pruebas para el 3 tab
        function gettestdelete() {
            vm.listtestdelete = [];
            var auth = localStorageService.get('Enterprise_NT.authorizationData');
            return testDS.getTestArea(auth.authToken, 0, 1, 0).then(function (data) {
                vm.getmotive(15);
                if (data.status === 200) {
                    vm.lisdatatest = [];
                    data.data.forEach(function (value) {
                        var test = {
                            'id': value.id,
                            'code': value.code,
                            'abbr': value.code,
                            'name': value.name
                        };
                        vm.lisdatatest.push(test);

                    });
                    vm.listtestdelete = vm.lisdatatest;
                }

            }, function (error) {

                vm.modalError(error);

            });
        }
        // funcion que consulta un servicio y borrar ségun el tab
        function save() {
            vm.Listtestresult = [];
            UIkit.modal('#advertencie').hide();
            if (vm.deleteType === 1) {
                vm.deletedata = {
                    'deleteType': vm.deleteType,
                    'comment': vm.Comment,
                    'motive': {
                        'id': vm.motive.id,
                        'name': vm.motive.name
                    }
                };
            }
            if (vm.deleteType === 2) {
                vm.deletedata = {
                    'deleteType': vm.deleteType,
                    'comment': vm.Comment,
                    'motive': {
                        'id': vm.motive.id,
                        'name': vm.motive.name
                    }
                };
            }
            if (vm.deleteType === 3 || vm.deleteType === 4 || vm.deleteType === 5) {
                vm.deletedata = {
                    'deleteType': vm.deleteType,
                    'comment': vm.Comment,
                    'motive': {
                        'id': vm.motive.id,
                        'name': vm.motive.name
                    }
                };
            }
            vm.loadingdata = true;
            vm.ind = 0;
            vm.total = vm.numberdelete.length;
            vm.porcent = 0;
            UIkit.modal('#modalprogressprintdelete', { bgclose: false, escclose: false, modal: false }).show();
            vm.countData = 0;
            vm.scandata();
        }

        vm.scandata = scandata;
        function scandata() {
            var auth = localStorageService.get('Enterprise_NT.authorizationData');
            if (vm.deleteType === 1) {
                vm.deletedata.orderNumber = vm.numberdelete[vm.countData].orderNumber;
            } else {
                vm.deletedata.orderNumber = vm.numberdelete[vm.countData].orderNumber;
                vm.deletedata.tests = vm.numberdelete[vm.countData].tests;
            }
            return specialdeletesDS.rangedeletespecial(auth.authToken, vm.deletedata).then(function (data) {
                if (data.status === 200) {
                    vm.countData = vm.countData + 1;
                    vm.ind = vm.ind + 1;
                    vm.porcent = Math.round((vm.ind * 100) / vm.total);
                    if (vm.countData === vm.numberdelete.length) {
                        vm.enddelete();
                    } else {
                        vm.scandata();
                    }
                }
            }, function (error) {
            });
        }


        vm.enddelete = enddelete;
        function enddelete() {
            UIkit.modal('#modalprogressprintdelete', { bgclose: false, escclose: false, modal: false }).hide();
            vm.porcent = 0;
            var auth = localStorageService.get('Enterprise_NT.authorizationData');
            vm.Detail = [
                {
                    'deleteDate': moment().format('DD/MM/YYYY'),
                    'userName': auth.userName,
                    'motive': vm.motive.name,
                    'comment': vm.Comment,
                    'order': vm.numberdelete,
                }
            ]
            UIkit.modal('#modallist').show();
            if (vm.deleteType === 1) { logger.success($filter('translate')('0349')); }
            if (vm.deleteType === 2) { logger.success($filter('translate')('0350')); }
            if (vm.deleteType === 3 || vm.deleteType === 4 || vm.deleteType === 5) { logger.success($filter('translate')('0351')); }
            vm.Listtestresult = [];
            vm.selectAllcheck = false;
            vm.selectAllcheck1 = false;
            vm.selectall();
            vm.selectalltest();
            vm.Comment = '';
            vm.motive.id = -1;
            vm.loadingdata = false;
        }



        //** Metodo que elimina los elementos sobrantes en la grilla**//
        function columtest(data) {
            vm.listtestresulttest = '';
            data.forEach(function (value, key) {
                vm.listtestresulttest = vm.listtestresulttest + '\n' + data[key].name + ',';
            });
            return vm.listtestresulttest;
        }

        function isAuthenticate() {
            //var auth = null
            var auth = localStorageService.get('Enterprise_NT.authorizationData');
            if (auth === null || auth.token) {
                $state.go('login');
            }
            else {
                vm.init();
            }
        }

        function init() {
            vm.gettest();

        }

        vm.isAuthenticate();

    }
})();
/* jshint ignore:end */