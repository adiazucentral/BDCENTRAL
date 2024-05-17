/* jshint ignore:start */
(function () {
  'use strict';
  angular
    .module('app.remission')
    .controller('remissionController', remissionController);


  remissionController.$inject = ['localStorageService', '$filter', '$state', '$rootScope', 'moment', 'common', 'remissionDS', 'logger', '$translate', 'LZString', '$scope'];

  function remissionController(localStorageService, $filter, $state, $rootScope, moment, common, remissionDS, logger, $translate, LZString, $scope) {

    var vm = this;
    vm.isAuthenticate = isAuthenticate;
    vm.init = init;
    vm.title = 'remission';
    $rootScope.menu = true;
    $rootScope.NamePage = $filter('translate')('1024');
    $rootScope.helpReference = '04.reportsandconsultations/patientconsultation.htm';
    $rootScope.pageview = 3;
    vm.modalError = modalError;
    vm.loadingdata = true;
    vm.getListYear = getListYear;
    vm.keyselect = keyselect;
    vm.detailOrder = detailOrder;
    vm.listYear = [];
    vm.getseach = getseach;
    vm.isopenreport = false;
    vm.isopenreport1 = false;
    vm.saveRemission = saveRemission;
    vm.selectall = selectall;
    vm.openconfirm = openconfirm;
    vm.printreport = printreport;
    vm.consultRemission = consultRemission;
    vm.printreportdate = printreportdate;
    vm.printreportone = printreportone;
    vm.generateFile = generateFile;
    vm.verification = verification;
    vm.save = save;

    $scope.$on('selection-changed', function (e, node) {
      vm.numberOrder = node.name;
      vm.remisionesList = vm.listRemission[vm.numberOrder];
    });

    //** Método para sacar el popup de error**//
    function modalError(error) {
      vm.Error = error;
      vm.ShowPopupError = true;
      vm.loadingdata = false;
    }
    //** Método para obtener lista de años**//
    function getListYear() {
      var dateMin = moment().year() - 4;
      var dateMax = moment().year();
      vm.listYear = [];
      for (var i = dateMax; i >= dateMin; i--) {
        vm.listYear.push({
          id: i,
          name: i
        });
      }
      vm.listYear.id = moment().year();
      return vm.listYear;
    }
    //** Método para completar el número de la orden**//
    function keyselect($event, type) {
      var keyCode =
        $event !== undefined ? $event.which || $event.keyCode : undefined;
      if (keyCode === 13 || keyCode === undefined) {
        if (vm.codeordernumberorden.length < vm.cantdigit) {
          vm.codeordernumberorden =
            vm.codeordernumberorden === '' ? 0 : vm.codeordernumberorden;
          if (vm.codeordernumberorden.length === parseInt(vm.orderdigit) + 1) {
            vm.numberordensearch = vm.listYear.id + moment().format('MM') + '0' + vm.codeordernumberorden;
          } else if (vm.codeordernumberorden.length === parseInt(vm.orderdigit) + 2) {
            vm.numberordensearch = vm.listYear.id + moment().format('MM') + vm.codeordernumberorden;
          } else if (vm.codeordernumberorden.length === parseInt(vm.orderdigit) + 3) {
            vm.numberordensearch = vm.listYear.id + '0' + vm.codeordernumberorden;
          } else {
            vm.numberordensearch = vm.listYear.id + common.getOrderComplete(vm.codeordernumberorden, vm.orderdigit).substring(4);
          }
          vm.codeordernumberorden = vm.numberordensearch.substring(4);
          if (type === 1) {
            vm.detailOrder();
          } else {
            logger.info("No hay conexión con la central");
          }
        } else if (vm.codeordernumberorden.length === vm.cantdigit) {
          vm.numberordensearch = vm.listYear.id + vm.numberorden;
          if (type === 1) {
            vm.detailOrder();
          } else {
            logger.info("No hay conexión con la central");
          }
        }
      } else {
        if (!(keyCode >= 48 && keyCode <= 57)) {
          $event.preventDefault();
        }
      }
    }
    //** Método para consultar el detalle de la orden**//
    function detailOrder() {
      if (vm.codeordernumberorden === '') {
        return true;
      } else {
        setTimeout(function () {
          angular.element('#numberordersearch').select();
        }, 100);
        vm.getseach();
      }
    }    
    //** Método para consultar el detalle de la orden**//
    function verification() {
      logger.info("No hay conexión con la central");
    }
    //** Método para los examenes de la orden**//
    function getseach() {
      vm.dataremisiones = [];
      vm.order = vm.listYear.id + vm.codeordernumberorden;
      var auth = localStorageService.get("Enterprise_NT.authorizationData");
      return remissionDS.getRemission(auth.authToken, vm.order).then(
        function (data) {
          if (data.status === 200) {
            vm.datapatient;
            data.data.forEach(function (value) {
              value.check = value.remission
            });
            vm.dataremisiones = data.data;
          } else {
            logger.info($filter("translate")("0392"));
          }
        },
        function (error) {
          logger.error(error);
        }
      );
    }
    //** Método seleccionar todos los examenes**//
    function selectall() {
      if (vm.dataremisiones.length > 0) {
        vm.dataremisiones.forEach(function (value, key) {
          if (value.testState === 0 && value.remission === 0) {
            vm.dataremisiones[key].check = vm.selectAllcheck;
          }
        });
      }
    }
    //** Método que abre modal para confirmar la remision**//
    function openconfirm() {
      UIkit.modal('#modalConfirmCancel').show();
    }
    //** Método para salvar la remision**//
    function saveRemission() {
      UIkit.modal('#modalConfirmCancel').hide();
      vm.loadingdata = true;
      var auth = localStorageService.get("Enterprise_NT.authorizationData");
      var dataremisiones = [];
      if (vm.dataremisiones.length > 0) {
        vm.dataremisiones.forEach(function (value, key) {
          if (value.check === true) {
            value.remission = 1;
            dataremisiones.add(value);
          }
        });
      }
      return remissionDS.saveRemission(auth.authToken, dataremisiones).then(
        function (data) {
          if (data.status === 200) {
            vm.printreport(dataremisiones);
            logger.info($filter("translate")("0149"));
            vm.getseach();
          }
        },
        function (error) {
          logger.error(error);
          vm.loadingdata = false;
        }
      );
    }
    //** Método para imprimir la remision**//
    function printreport(data) {
      var auth = localStorageService.get("Enterprise_NT.authorizationData");
      vm.variables = [{
        'entity': 'CLTECH',
        'abbreviation': 'CLT',
        'username': auth.userName,
        'date': moment().format('' + vm.formatDate.toUpperCase() + ' ' + 'HH:mm:ss'),
        'orderNumber': data[0].order,
        'patientId': vm.datapatient.Id,
        'lastName': vm.datapatient.lastName,
        'surName': vm.datapatient.surName === undefined ? '' : vm.datapatient.surName,
        'name1': vm.datapatient.patientName,
        'birthday': vm.datapatient.birthday,
        'sex': vm.datapatient.sex,
        'age': vm.datapatient.age,
        'email': vm.datapatient.email,
        'phone': vm.datapatient.phone,
        'completeName': vm.datapatient.completeName,
        'address': vm.datapatient.address,
        'documentType': vm.datapatient.documentType
      }];

      vm.pathreport = '/Report/post-analitic/remission/remission.mrt';
      var parameterReport = {};
      parameterReport.variables = vm.variables;
      parameterReport.pathreport = vm.pathreport;
      parameterReport.labelsreport = JSON.stringify($translate.getTranslationTable());
      var datareport = LZString.compressToUTF16(JSON.stringify(data));
      localStorageService.set('parameterReport', parameterReport);
      localStorageService.set('dataReport', datareport);
      window.open('/viewreport/viewreport.html');
      vm.loadingdata = false;
    }
    //** Método para imprimir la remision**//
    function printreportone() {
      var auth = localStorageService.get("Enterprise_NT.authorizationData");
      vm.variables = [{
        'entity': 'CLTECH',
        'abbreviation': 'CLT',
        'username': auth.userName,
        'date': moment().format('' + vm.formatDate.toUpperCase() + ' ' + 'HH:mm:ss'),
        'orderNumber': vm.remisionesList[0].order,
        'patientId': vm.datapatientlist.Id,
        'lastName': vm.datapatientlist.lastName,
        'surName': vm.datapatientlist.surName === undefined ? '' : vm.datapatientlist.surName,
        'name1': vm.datapatientlist.patientName,
        'birthday': vm.datapatientlist.birthday,
        'sex': vm.datapatientlist.sex,
        'age': vm.datapatientlist.age,
        'email': vm.datapatientlist.email,
        'phone': vm.datapatientlist.phone,
        'completeName': vm.datapatientlist.completeName,
        'address': vm.datapatientlist.address,
        'documentType': vm.datapatientlist.documentType
      }];

      vm.pathreport = '/Report/post-analitic/remission/remission.mrt';
      var parameterReport = {};
      parameterReport.variables = vm.variables;
      parameterReport.pathreport = vm.pathreport;
      parameterReport.labelsreport = JSON.stringify($translate.getTranslationTable());
      var datareport = LZString.compressToUTF16(JSON.stringify(vm.remisionesList));
      localStorageService.set('parameterReport', parameterReport);
      localStorageService.set('dataReport', datareport);
      window.open('/viewreport/viewreport.html');
      vm.loadingdata = false;
    }
    //** Método para validar la autenticacion del usuario**//
    function isAuthenticate() {
      var auth = localStorageService.get('Enterprise_NT.authorizationData');
      if (auth === null || auth.token) {
        $state.go('login');
        vm.loadingdata = false;
      } else {
        vm.init();
      }
    }
    function generateFile() {
      var consult = {
        'rangeType': vm.filterRange1,
        'init': vm.rangeInit1,
        'end': vm.rangeEnd1,
        'testFilterType': vm.numFilterAreaTest,
        'tests': vm.numFilterAreaTest === 1 ? vm.listAreas : vm.listTests,
        'demographics': vm.demographics,
        "userId": -1,
        "orderType": null,
        "check": 0,
        "listType": 0,
        "printAddLabel": false,
        "apply": true,
        "samples": [],
        "remission": 0,
        "laboratory": 0
      }
      var auth = localStorageService.get('Enterprise_NT.authorizationData');
      return remissionDS.remissionCentralOrders(auth.authToken, consult).then(function (data) {
        if (data.status === 200) {
          vm.dataremisiones = data.data;
          vm.dataremisiones = _.filter(vm.dataremisiones, function (o) {
            o.tests = _.filter(o.tests, function (p) {
              return p.selected = true;
            });
            return o.tests.length > 0
          })
          vm.searchorder = '';
          UIkit.modal('#referralsmodal').show();
          vm.progressPrint = false;
        } else {
          logger.error($filter("translate")("0152"));
          vm.progressPrint = false;
        }
      },
        function (error) {
          vm.modalError(error);
        });
    }
    function save() {
      vm.loadingdata = true;
      var data = [];
      var datafilter = _.filter(_.clone(vm.dataremisiones), function (o) {
        o.tests = _.filter(o.tests, function (p) {
          if (p.selected === true) {
            var remision = { "order": o.orderNumber, "test": p.id }
            data.add(remision);
          }
          return p.selected === true;
        });
        return o.tests.length > 0
      })
      if (datafilter.length === 0) {
        vm.loadingdata = false;
        logger.info($filter("translate")("1867"));
      } else {
        var auth = localStorageService.get("Enterprise_NT.authorizationData");
        return remissionDS.saveRemission(auth.authToken, data).then(
          function (data) {
            if (data.status === 200) {
              vm.loadingdata = false;
              logger.success($filter("translate")("0149"));
              UIkit.modal('#referralsmodal').hide();
            } else {
              logger.error($filter("translate")("0152"));
              vm.loadingdata = false;
            }
          },
          function (error) {
            vm.loadingdata = false;
            logger.error(error);
          }
        );
      }
    }
    //** Método para inicializar la pagina**//
    function init() {
      vm.filterRange1 = "1";
      vm.rangeInit1 = "";
      vm.rangeend1 = "";
      vm.menssageinformative = "";
      vm.loadingdata = false;
      vm.options5 == { expandOnClick: true, filter: {} };
      vm.dataremisiones = [];
      vm.remisionesList = [];
      vm.numberOrder = '';
      vm.basicTree = [];
      vm.orderdigit = localStorageService.get('DigitosOrden');
      vm.formatDate = localStorageService.get('FormatoFecha');
      vm.cantdigit = parseInt(vm.orderdigit) + 4;
      vm.getListYear();
    }
    //** Método para salvar la remision**//
    function consultRemission() {
      vm.remisionesList = [];
      vm.numberOrder = '';
      vm.listremissionprint = [];
      vm.basicTree = [];
      vm.loadingdata = true;
      var auth = localStorageService.get("Enterprise_NT.authorizationData");
      var date = {
        "initDate": moment(vm.dateInit).format('YYYYMMDD'),
        "endDate": moment(vm.dateEnd).format('YYYYMMDD'),
      }
      return remissionDS.consultRemission(auth.authToken, date).then(
        function (data) {
          if (data.status === 200) {
            vm.loadingdata = false;
            vm.listremissionprint = data.data;
            vm.listRemission = data.data.length === 0 ? [] : _.groupBy(data.data, function (o) {
              return o.order;
            });
            vm.basicTree = removeconsultpatient(vm.listRemission);
          } else {
            logger.info($filter("translate")("0392"));
            vm.loadingdata = false;
          }
        },
        function (error) {
          logger.error(error);
          vm.loadingdata = false;
        }
      );
    }
    function removeconsultpatient(data) {
      var basicTree = [];
      for (var propiedad in data) {
        if (data.hasOwnProperty(propiedad)) {
          var object1 = {
            name: data[propiedad][0].order,
            image: 'images/form.png',
            children: []
          };
          basicTree.push(object1);
        }
      }
      return basicTree;
    }
    //** Método para imprimir la remision**//
    function printreportdate() {
      var auth = localStorageService.get("Enterprise_NT.authorizationData");
      vm.variables = [{
        'entity': 'CLTECH',
        'abbreviation': 'CLT',
        'username': auth.userName,
        'date': moment().format('' + vm.formatDate.toUpperCase() + ' ' + 'HH:mm:ss')
      }];
      vm.pathreport = '/Report/post-analitic/remission/consultremission.mrt';
      var parameterReport = {};
      parameterReport.variables = vm.variables;
      parameterReport.pathreport = vm.pathreport;
      parameterReport.labelsreport = JSON.stringify($translate.getTranslationTable());
      var datareport = LZString.compressToUTF16(JSON.stringify(vm.listremissionprint));
      localStorageService.set('parameterReport', parameterReport);
      localStorageService.set('dataReport', datareport);
      window.open('/viewreport/viewreport.html');
      vm.loadingdata = false;
    }
    vm.init();
    vm.isAuthenticate();
  }
})();
/* jshint ignore:end */

