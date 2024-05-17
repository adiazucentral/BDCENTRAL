
/* jshint ignore:start */
(function () {
  'use strict';
  angular
    .module('app.widgets')
    .directive('filterprintreports', filterprintreports);

  filterprintreports.$inject = ['$filter', '$rootScope', 'localStorageService', 'reportadicional', 'logger'];
  function filterprintreports($filter, $rootScope, localStorageService, reportadicional, logger) {
    var directive = {
      restrict: 'EA',
      templateUrl: 'app/widgets/userControl/filter-printreports.html',
      scope: {
        id: '=id',
        visible: '=?visible',
        destination: '=destination',
        defaulttypeprint: '=defaultypeprint',
        viewpreliminary: '=?viewpreliminary',
        viewpreviou: '=?viewpreviou',
        viewordering: '=?viewordering',
        controlordersprint: '=controlordersprint',
        activateexecution: '=?activateexecution',
        isactivemail: '=?isactivemail',
        viewtypeprint: '=?viewtypeprint',
        functionexecute: '=functionexecute',
        functionexecuteconfig: '=functionexecuteconfig',
        language: '=language',
        showadditionalmail: '=?showadditionalmail',
        receiveresult: '=?receiveresult',
        update: '=?update',
        filteritems: '=?filteritems',
        demo: '=demo',
        configprint: '=?configprint',
        saveconfig: '=?saveconfig',
        paramFilterPrint: '=?paramFilterPrint'
      },



      controller: ['$scope', function ($scope) {
        var vm = this;
        vm.id = $scope.id === undefined ? '' : $scope.id;
        vm.viewPreliminary = $scope.viewpreliminary;
        vm.viewPreviou = $scope.viewpreviou;
        vm.viewordering = $scope.viewordering;
        var auth = localStorageService.get('Enterprise_NT.authorizationData');
        var language = $filter('translate')('0000');
        vm.all = '-- ' + $filter('translate')('0353') + ' --';
        vm.isPrintCopies = localStorageService.get('IdentificarCopiasInformes') === 'True';
        vm.isPrintAttached = localStorageService.get('ImprimirAdjuntos') === 'True';

        vm.UrlNodeJs = localStorageService.get('UrlNodeJs');
        vm.isActiveMail = localStorageService.get('EnviarCorreo') !== '';
        // vm.sendEmail = localStorageService.get('EnviarCorreo') === '1,2' ? '3' : localStorageService.get('EnviarCorreo');
        vm.language = localStorageService.get("IdiomaReporteResultados") === 'es' ? '1' : '2';
        vm.isStamp = localStorageService.get('SelloFresco') === 'True';
        $scope.isactivemail = vm.isActiveMail;
        vm.visible = $scope.visible !== undefined && $scope.visible;
        vm.controlOrdersPrint = $scope.controlordersprint;
        vm.reprint = false;
        vm.attachments = true;
        vm.stamp = false;
        $scope.attachments = true;
        vm.destination = '1';
        $scope.destination = '1';
        vm.update = false;
        $scope.update = false;
        vm.typePrint = vm.viewPreviou === false ? '1' : '4';
        vm.quantityCopies = 1;
        vm.changeDestination = changeDestination;
        vm.orderingPrint = '1';
        vm.progressPDF = false;
        vm.progressEmail = false;
        vm.progressDownload = false;
        vm.progressDownloadAll = false;
        vm.dataAttachmentsImage = [];
        vm.dataAttachmentsPDF = [];
        vm.searchSerial = searchSerial;
        vm.viewTypePrint = $scope.viewtypeprint !== undefined && $scope.viewtypeprint;
        vm.modalInstallSerialPrint = UIkit.modal('#modalInstallSerialPrint', { modal: false, keyboard: false, bgclose: false, center: true });
        vm.eventCloseModal = eventCloseModal;
        vm.filterDemo = false;
        vm.changeLanguage = changeLanguage;
        vm.patientPrint = false;
        vm.physicianPrint = false;
        vm.branchPrint = false;
        vm.additionalMailPrint = false;
        vm.additionalMail = '';
        vm.showadditionalmail = $scope.showadditionalmail;
        vm.validatePrint = validatePrint;
        vm.validatePrint();

        $scope.$watch('activateexecution', function () {
          if ($scope.activateexecution) {
            vm.searchSerial(1);
          }
          $scope.activateexecution = false;
        });


        $scope.$watch('saveconfig', function () {
          if ($scope.saveconfig) {
            var paramFilterPrint = {
              'attachments': vm.attachments,
              'destination': vm.destination,
              'typePrint': vm.typePrint,
              'language': vm.language,
              'serial': $rootScope.serialprint,
              'quantityCopies': vm.quantityCopies,
              'stamp': vm.stamp,
              'patient': vm.patient,
              'physician': vm.physician,
              'branch': vm.branch,
              'demo': vm.demo,
              'additionalMail': vm.additionalMail,
              'filterDemo': vm.filterDemo,
              'orderingPrint': vm.orderingPrint
            };
            $scope.functionexecuteconfig(paramFilterPrint);
          }
          $scope.saveconfig = false;

        });


        $scope.$watch('update', function () {
          vm.update = $scope.update;
          vm.namereceiveresult = '';
          vm.patient = false;
          vm.physician = false;
          vm.branch = false;
          vm.demo = false;
          vm.addperson = false;
          vm.viewlabel = false;
        });

        $scope.$watch('configprint', function () {
          if ($scope.configprint !== undefined && $scope.configprint !== '') {
            vm.attachments = $scope.configprint.attachments;
            vm.destination = $scope.configprint.destination;
            vm.typePrint = $scope.configprint.typePrint;
            vm.language = $scope.configprint.language;
            vm.serial = $scope.configprint.serial;
            vm.quantityCopies = $scope.configprint.quantityCopies;
            vm.stamp = $scope.configprint.stamp;
            vm.patient = $scope.configprint.patient;
            vm.physician = $scope.configprint.physician;
            vm.branch = $scope.configprint.branch;
            $scope.demo = $scope.configprint.demo;
            vm.demo = $scope.configprint.demo;
            vm.additionalMail = $scope.configprint.additionalMail;
            vm.filterDemo = $scope.configprint.filterDemo;
            vm.orderingPrint = $scope.configprint.orderingPrint;
            vm.attachments = $scope.configprint.attachments;
          } else {
            vm.attachments = true;
            vm.destination = '1';
            vm.typePrint = '1';
            vm.language = '1';
            vm.quantityCopies = 1;
            vm.stamp = false;
            vm.patient = false;
            vm.physician = false;
            vm.branch = false;
            vm.demo = false;
            $scope.demo = vm.demo;
            vm.additionalMail = '';
            vm.filterDemo = false;
            vm.orderingPrint = '1';
          }
        });


        $scope.$watch('filteritems', function () {
          vm.filteritems = $scope.filteritems === true ? true : false;
        });



        $scope.$watch('destination', function () {
          vm.requerid = false;
          vm.receiveresult = $scope.receiveresult;
          vm.attachments = true;
          vm.progressPDF = false;
          vm.progressEmail = false;
          vm.quantityCopies = 1;
          $scope.controlordersprint = false;

          if (document.getElementById('copies' + vm.id).checked) {
            document.getElementById('endings' + vm.id).checked = true;
            vm.typePrint = '1';
          }
          if (vm.destination.toString() === '3') {
            document.getElementById('ordersPrint' + vm.id).checked = true;
            vm.orderingPrint = '1';
            $scope.controlordersprint = true;
          }
        });

        vm.changue = changue;
        function changue() {
          $scope.demo = vm.demo;
        }

        function validatePrint() {
          if (localStorageService.get('EnviarCorreo') === '1,2') {
            vm.patientPrint = true;
            vm.physicianPrint = true;
            vm.branchPrint = true;
            vm.additionalMailPrint = true;
          } else if (localStorageService.get('EnviarCorreo') === '1') {
            vm.patientPrint = true;
            vm.branchPrint = false;
            vm.physicianPrint = false;
            vm.additionalMailPrint = false;
          }
        }

        function changeLanguage() {
          $scope.language = vm.language;
        }

        function changeDestination() {
          $scope.demo = vm.demo;
          if (vm.destination !== '3') {
            vm.patient = false;
            vm.physician = false;
            vm.branch = false;
            vm.additionalMail='';
          }
          $scope.destination = vm.destination;
        }

        function eventCloseModal() {
          vm.modalInstallSerialPrint.hide();
        }

        function searchSerial(transaction) {
          vm.requerid = false;
          vm.loading = false;
          if ($rootScope.serialprint === '' && vm.destination.toString() === '1') {
            vm.messageError = $filter('translate')('1067');
            UIkit.modal('#logNoSerial').show();
          }
          else {
            if (vm.namereceiveresult === '' && vm.receiveresult && vm.addperson ||
              vm.namereceiveresult === null && vm.receiveresult && vm.addperson ||
              vm.namereceiveresult === undefined && vm.receiveresult && vm.addperson) {
              vm.requerid = true;
            } else {
              if (vm.destination === '3' && vm.demo) {
                vm.sendEmail = '2';
                if (vm.patient && vm.physician) {
                  vm.sendEmail = '3';
                } else if (vm.patient && !vm.physician) {
                  vm.sendEmail = '1';
                } else if ((!vm.patient && vm.physician) || vm.branch || vm.additionalMail) {
                  vm.sendEmail = '2';
                }
                var paramFilterPrint = {
                  'attachments': vm.attachments,
                  'orderssend': vm.ordersSend,
                  'historysend': vm.historySend,
                  'typeprint': vm.typePrint,
                  'serial': $rootScope.serialprint,
                  'quantitycopies': vm.quantityCopies,
                  'filterdemo': vm.filterDemo,
                  'orderingprint': vm.orderingPrint,
                  'destination': vm.destination,
                  'sendEmail': 2,
                  'language': vm.language,
                  'branch': vm.branch,
                  'patient': vm.patient,
                  'physician': vm.physician,
                  'additionalMail': vm.additionalMail,
                  'namereceiveresult': vm.namereceiveresult,
                  'addperson': vm.addperson,
                  'stamp': vm.stamp
                };
                vm.additionalMail = "";
                $scope.functionexecute(paramFilterPrint);
              } else if (vm.destination === '3' && !vm.patient && !vm.physician && !vm.branch && vm.additionalMail === "") {
                logger.error($filter('translate')('1888'));
              } else {
                if (vm.patient && vm.physician) {
                  vm.sendEmail = '3';
                } else if (vm.patient && !vm.physician) {
                  vm.sendEmail = '1';
                } else if ((!vm.patient && vm.physician) || vm.branch || vm.additionalMail) {
                  vm.sendEmail = '2';
                }
                var paramFilterPrint = {
                  'attachments': vm.attachments,
                  'orderssend': vm.ordersSend,
                  'historysend': vm.historySend,
                  'typeprint': vm.typePrint,
                  'serial': $rootScope.serialprint,
                  'quantitycopies': vm.quantityCopies,
                  'filterdemo': vm.filterDemo,
                  'orderingprint': vm.orderingPrint,
                  'destination': vm.destination,
                  'sendEmail': vm.sendEmail,
                  'language': vm.language,
                  'branch': vm.branch,
                  'patient': vm.patient,
                  'physician': vm.physician,
                  'additionalMail': vm.additionalMail,
                  'namereceiveresult': vm.namereceiveresult,
                  'addperson': vm.addperson,
                  'stamp': vm.stamp
                };
                vm.additionalMail = "";
                $scope.functionexecute(paramFilterPrint);
              }
            }
          }
        }
      }],
      controllerAs: 'filterprintreports'
    };
    return directive;
  }
})();
/* jshint ignore:end */
