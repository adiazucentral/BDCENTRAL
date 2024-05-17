/* jshint ignore:start */
(function () {
  'use strict';

  angular
    .module('app.comboinvoice')
    .controller('comboinvoiceController', comboinvoiceController);
  comboinvoiceController.$inject = ['$translate', 'localStorageService',
    '$filter', '$state', 'moment', '$rootScope', 'branchDS', 'cashboxDS', 'reportadicional'];

  function comboinvoiceController($translate, localStorageService,
    $filter, $state, moment, $rootScope, branchDS, cashboxDS, reportadicional) {
    var vm = this;
    $rootScope.menu = true;
    $rootScope.NamePage = $filter('translate')('1920');
    $rootScope.helpReference = '08.billing/invoicecombo.htm';
    $rootScope.pageview = 3;
    vm.title = 'comboinvoice';

    vm.rangeInit = moment().format("YYYYMMDD");
    vm.rangeEnd = moment().format("YYYYMMDD");
    vm.demosmask = "-110||-104||-100||-101||-102||-103||-109";
    vm.comment = '';
    vm.calculate = true;
    vm.filterTpye = 1;
    vm.abbrCustomer = localStorageService.get('Abreviatura');
    vm.nameCustomer = localStorageService.get('Entidad');
    vm.formatDate = localStorageService.get('FormatoFecha').toUpperCase();
    vm.formatDateHours = localStorageService.get('FormatoFecha').toUpperCase() + ', h:mm:ss a';
    vm.type = 'pdf';

    vm.customMenu = {
      menubar: false,
      language: $filter('translate')('0000') === 'esCo' ? 'es' : 'en',
      plugins: [
        'link',
        'lists',
        'autolink',
        'anchor',
        'textcolor',
        'charmap'
      ],
      toolbar: [
        'undo redo | bold italic underline superscript | fontselect fontsizeselect forecolor backcolor charmap | alignleft aligncenter alignright alignfull | numlist bullist outdent indent'
      ],
      powerpaste_word_import: 'clean',
      powerpaste_html_import: 'clean',
      height : '310'
    };

    vm.isAuthenticate = isAuthenticate;
    vm.init = init;
    vm.getbranch = getbranch;
    vm.modalError = modalError;
    vm.print = print;
    vm.generateFile = generateFile;
    vm.windowOpenReport = windowOpenReport;

    function generateFile() {
      var auth = localStorageService.get('Enterprise_NT.authorizationData');
      var branch = $filter("filter")(JSON.parse(JSON.stringify(vm.branch)), function (e) {
        return e.id === vm.databranch;
      });

      var customer = vm.abbrCustomer + ' (' + vm.nameCustomer + ')';
      vm.variables = {
        "ACCOUNT": customer,
        "period": moment(vm.rangeInit).format(vm.formatDate) + ' al ' + moment(vm.rangeEnd).format(vm.formatDate),
        "branch": branch[0].name,
        "comment": vm.comment,
        "USERNAME": auth.userName,
        'invoice': vm.invoiceNumber
      };

      if(vm.calculate) {
        vm.pathreport = '/Report/billing/preinvoicecombo/preinvoicecombo.mrt';
      } else {
        vm.pathreport = '/Report/billing/invoicecombo/invoicecombo.mrt';
      }
      vm.openreport = false;
      vm.report = false;
      vm.windowOpenReport();
    }

    function getbranch() {
      vm.branch = [];
      var auth = localStorageService.get('Enterprise_NT.authorizationData');
      return branchDS.getBranchstate(auth.authToken).then(function (data) {
        if (data.status === 200) {
          var all = {
            "id": -1,
            "name": $filter('translate')('0215')
          }
          data.data.push(all);
          vm.branch = $filter('orderBy')(data.data, 'name');
          vm.databranch = -1;
        }
      },
      function (error) {
        vm.modalError(error);
      });
    }

    function print() {
      vm.loading = true;
      var preinvoice = {
        "branchId": vm.databranch,
        "startDate": vm.rangeInit,
        "endDate": vm.rangeEnd,
        'demographics': vm.demographics,
        "comment": vm.comment,
        "filterTpye": vm.calculate === false ? 1 : 2
      }

      var auth = localStorageService.get('Enterprise_NT.authorizationData');

      if(vm.calculate) {
        return cashboxDS.preinvoicecombo(auth.authToken, preinvoice).then(function (data) {
          if (data.status === 200) {
            if(data.data.orders.length > 0) {
              vm.datareport = data.data.orders;
              vm.generateFile();
            } else {
              UIkit.modal('#nofoundfilter').show();
            }
          } else {
            UIkit.modal('#nofoundfilter').show();
          }
          vm.loading = false;
        }, function (error) {
          vm.loading = false;
        });
      } else {
        return cashboxDS.invoicecombo(auth.authToken, preinvoice).then(function (data) {
          if (data.status === 200) {
            if(data.data.orders.length > 0) {
              vm.datareport = data.data.orders;
              vm.invoiceNumber = data.data.invoiceId;
              vm.generateFile();
            } else {
              UIkit.modal('#nofoundfilter').show();
            }
          } else {
            UIkit.modal('#nofoundfilter').show();
          }
          vm.loading = false;
        }, function (error) {
          vm.loading = false;
        });
      }

    }

    function windowOpenReport() {
      if (vm.datareport.length > 0) {
        var labelsreport = JSON.stringify($translate.getTranslationTable());
        labelsreport = JSON.parse(labelsreport);
        var parameterReport = {};
        parameterReport.datareport = vm.datareport;
        parameterReport.variables = vm.variables;
        parameterReport.pathreport = vm.pathreport;
        parameterReport.labelsreport = labelsreport;
        parameterReport.type = vm.type;
        vm.ind = 1;
        vm.total = vm.datareport.length / 3;
        vm.porcent = 0;
        UIkit.modal('#modalprogress', { bgclose: false, escclose: false, modal: false }).show();
        var nIntervId;
        nIntervId = setInterval(vm.flasheaTexto, 200);
        reportadicional.reportRender(parameterReport).then(function (data) {
          UIkit.modal('#modalprogress', { bgclose: false, escclose: false, modal: false }).hide();
          vm.porcent = 0;
          clearInterval(nIntervId);
        });
        vm.report = false;

      } else {
        UIkit.modal('#modalReportError').show();
      }
    }

    function init() {
      vm.getbranch();
    }

    function modalError(error) {
      vm.Error = error;
      vm.ShowPopupError = true;
    }

    function isAuthenticate() {
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
