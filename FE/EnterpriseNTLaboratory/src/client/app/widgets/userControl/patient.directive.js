/********************************************************************************
  ENTERPRISENT - Todos los derechos reservados CLTech Ltda.
  PROPOSITO:    ... 
  PARAMETROS:   iddb        @descripción
                order       @descripción
                datapatient @descripción
                heightline  @descripción

  AUTOR:        @autor
  FECHA:        2018-06-21
  IMPLEMENTADA EN: 
  1.02_EnterpriseNT_FE/EnterpriseNTLaboratory/src/client/app/modules/account/userprofile/userprofile.html
  2.02_EnterpriseNT_FE/EnterpriseNTLaboratory/src/client/app/modules/analytical/resultsentry/resultsentry.html
  3.02_EnterpriseNT_FE/EnterpriseNTLaboratory/src/client/app/modules/post-analitic/checkmicrobiology/checkmicrobiology.html
  4.02_EnterpriseNT_FE/EnterpriseNTLaboratory/src/client/app/modules/post-analitic/growtmicrobiology/growtmicrobiology.html
  5.02_EnterpriseNT_FE/EnterpriseNTLaboratory/src/client/app/modules/post-analitic/microbiologyReading/microbiologyReading.html
  6.02_EnterpriseNT_FE/EnterpriseNTLaboratory/src/client/app/modules/pre-analitic/activationorder/activationorder.html
  7.02_EnterpriseNT_FE/EnterpriseNTLaboratory/src/client/app/modules/pre-analitic/completeverify/completeverify.html
  8.02_EnterpriseNT_FE/EnterpriseNTLaboratory/src/client/app/modules/pre-analitic/deletespecial/deletespecial.html
  9.02_EnterpriseNT_FE/EnterpriseNTLaboratory/src/client/app/modules/pre-analitic/historyassignment/historyassignment.html
  10.02_EnterpriseNT_FE/EnterpriseNTLaboratory/src/client/app/modules/pre-analitic/historypatient/historypatient.html
  11.02_EnterpriseNT_FE/EnterpriseNTLaboratory/src/client/app/modules/pre-analitic/historyreassignment/historyreassignment.html
  12.02_EnterpriseNT_FE/EnterpriseNTLaboratory/src/client/app/modules/pre-analitic/inconsistency/inconsistency.html
  13.02_EnterpriseNT_FE/EnterpriseNTLaboratory/src/client/app/modules/pre-analitic/orderEntry/orderentry.html
  14.02_EnterpriseNT_FE/EnterpriseNTLaboratory/src/client/app/modules/pre-analitic/orderswithouthistory/orderswithouthistory.html
  15.02_EnterpriseNT_FE/EnterpriseNTLaboratory/src/client/app/modules/pre-analitic/simpleverification/simpleverification.html
  16.02_EnterpriseNT_FE/EnterpriseNTLaboratory/src/client/app/modules/reportsandconsultations/controldeliveryreports/controldeliveryreports.html
  17.02_EnterpriseNT_FE/EnterpriseNTLaboratory/src/client/app/modules/reportsandconsultations/queries/queries.html
  18.02_EnterpriseNT_FE/EnterpriseNTLaboratory/src/client/app/modules/reportsandconsultations/reports/reports.html
  19.02_EnterpriseNT_FE/EnterpriseNTLaboratory/src/client/app/modules/stadistics/destinationsample/destinationsample.html
  20.02_EnterpriseNT_FE/EnterpriseNTLaboratory/src/client/app/modules/tools/tuberack/tuberack.html

  MODIFICACIONES:

  1. aaaa-mm-dd. Autor
     Comentario...

********************************************************************************/

(function () {
    'use strict';
    angular
        .module('app.widgets')
        .directive('patient', patient);
    patient.$inject = ['common', 'patientDS', 'localStorageService', '$filter'];
    /* @ngInject */
    function patient(common, patientDS, localStorageService, $filter) {
        var directive = {
            templateUrl: 'app/widgets/userControl/patient.html',
            restrict: 'EA',
            scope: {
                iddb: '=?iddb',
                order: '=?order',
                datapatient:'=?datapatient',
                heightline: '=heightline'
            },
            controller: ['$scope', function ($scope) {
                var vm = this;

                //Variables de la directiva                
                vm.iddb = $scope.iddb;
                vm.order = $scope.order;
                vm.patient = {};
                vm.manageweight = localStorageService.get('ManejoPeso') === 'True';
                vm.managesize = localStorageService.get('ManejoTalla') === 'True';
                vm.showDocumentType = localStorageService.get('ManejoTipoDocumento') === 'True';
                vm.formatDate = localStorageService.get('FormatoFecha').toUpperCase();
                vm.height_line = $scope.heightline === undefined ? '0px !important' : $scope.heightline.toString() + 'px !important';

                //Variables de la directiva que no se muestran en la vista
                var auth = localStorageService.get('Enterprise_NT.authorizationData');

                //Metodos de la directiva
                vm.init = init;

                //Eventos de cambio
                $scope.$watch('iddb', function () {
                    if ($scope.iddb !== undefined && $scope.iddb !== null) {
                        vm.iddb = $scope.iddb;
                        loadPatient(false);
                    }
                });

                $scope.$watch('order', function () {
                    if ($scope.order !== undefined && $scope.order !== null) {
                        vm.order = $scope.order;
                        loadPatient(true);
                    }
                });

                /** Carga la informacion de un paciente tomando la variable vm.patientIdDB*/
                function loadPatient(searchByOrder) {
                    vm.patientdata = {};
                    $scope.datapatient = {};
                    if (!searchByOrder) {
                        if (vm.iddb !== undefined && vm.iddb !== null) {
                            patientDS.getPatientOnlyId(auth.authToken, vm.iddb).then(
                                function (response) {
                                    if (response.status === 200) {
                                        var data = response.data;
                                        vm.patientdata.documentType = (vm.showDocumentType && data.documentType !== undefined && data.documentType !== null ? data.documentType.abbr : '');
                                        vm.patientdata.Id = data.id;
                                        vm.patientdata.patientId = data.patientId;
                                        vm.patientdata.lastName = (data.lastName + (data.surName !== undefined && data.surName !== null ? ' ' + data.surName : '')).toUpperCase();
                                        vm.patientdata.patientName = (data.name1 + (data.name2 !== undefined && data.name2 !== null ? ' ' + data.name2 : '')).toUpperCase();
                                        vm.patientdata.sex = data.sex.esCo;
                                        vm.patientdata.birthday = moment(data.birthday).format(vm.formatDate);
                                        vm.patientdata.age = common.getAgeAsString(moment(data.birthday).format(vm.formatDate), vm.formatDate);
                                        vm.patientdata.email = data.email === undefined || data.email === '' ? $filter('translate')('0571') : data.email;
                                        vm.patientdata.size = data.size;
                                        vm.patientdata.address = data.address === undefined || data.address === '' ?  $filter('translate')('0681') : data.address;
                                        vm.patientdata.phone = data.phone === undefined || data.phone === '' ?  $filter('translate')('0681') : data.phone;
                                        vm.patientdata.weight = data.weight;
                                        vm.patientdata.race = (data.race === undefined || data.race === null ? $filter('translate')('0681') : data.race.name);
                                        vm.patientdata.demographics = data.demographics;
                                        vm.patientdata.completeName = data.lastName + (data.surName !== undefined && data.surName !== null ? ' ' + data.surName + ' ' : ' ') + data.name1 + (data.name2 !== undefined && data.name2 !== null ? ' ' + data.name2 : '');
                                        vm.patientdata.photo = data.photo !== undefined && data.photo !== null && data.photo !== '' ? data.photo : '';
                                        //vm.patient = patient;

                                        $scope.datapatient = vm.patientdata;
                                    }
                                }, function (error) {
                                    vm.Error = error;
                                    vm.ShowPopupError = true;
                                }
                            );
                        }
                    } else {
                        if (vm.order !== undefined && vm.order !== null) {
                            patientDS.getPatientObjectByOrder(auth.authToken, vm.order).then(
                                function (response) {
                                    if (response.status === 200) {
                                        var data = response.data;
                                        vm.patientdata.documentType = (vm.showDocumentType && data.documentType !== undefined && data.documentType !== null ? data.documentType.abbr : '');
                                        vm.patientdata.Id = data.id;
                                        vm.patientdata.patientId = data.patientId;
                                        vm.patientdata.lastName = (data.lastName + (data.surName !== undefined && data.surName !== null ? ' ' + data.surName : '')).toUpperCase();
                                        vm.patientdata.patientName = (data.name1 + (data.name2 !== undefined && data.name2 !== null ? ' ' + data.name2 : '')).toUpperCase();
                                        vm.patientdata.sex = data.sex.esCo;
                                        vm.patientdata.birthday = moment(data.birthday).format(vm.formatDate);
                                        vm.patientdata.age = common.getAgeAsString(moment(data.birthday).format(vm.formatDate), vm.formatDate);
                                        vm.patientdata.email = data.email === undefined || data.email === '' ? $filter('translate')('0571') : data.email;
                                        vm.patientdata.size = data.size;
                                        vm.patientdata.address = data.address === undefined || data.address === '' ?  $filter('translate')('0681') : data.address;
                                        vm.patientdata.phone = data.phone === undefined || data.phone === '' ?  $filter('translate')('0681') : data.phone;
                                        vm.patientdata.weight = data.weight;
                                        vm.patientdata.race = (data.race === undefined || data.race === null ? $filter('translate')('0681') : data.race.name);
                                        vm.patientdata.demographics = data.demographics;
                                        vm.patientdata.completeName = data.lastName + (data.surName !== undefined && data.surName !== null ? ' ' + data.surName + ' ' : ' ') + data.name1 + (data.name2 !== undefined && data.name2 !== null ? ' ' + data.name2 : '');
                                        vm.patientdata.photo = data.photo !== undefined && data.photo !== null && data.photo !== '' ? data.photo : '';
                               //  vm.patient = patient;

                                        $scope.datapatient = vm.patientdata;
                                    }
                                },
                                function (error) {
                                    vm.Error = error;
                                    vm.ShowPopupError = true;
                                }
                            );
                        }
                    }
                }

                /** 
                 * Funcion inicial de la directiva
                */
                function init() {
                    loadPatient();
                }
                vm.init();
            }],
            controllerAs: 'patient'
        };
        return directive;
    }
})();
