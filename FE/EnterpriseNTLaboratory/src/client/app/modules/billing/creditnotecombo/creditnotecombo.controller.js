/* jshint ignore:start */
(function () {
  'use strict';

  angular
    .module('app.creditnotecombo')
    .controller('creditnotecomboController', creditnotecomboController);
  creditnotecomboController.$inject = ['$translate', 'localStorageService',
    '$filter', '$state', 'moment', '$rootScope', 'cashboxDS', 'logger'];

  function creditnotecomboController($translate, localStorageService,
    $filter, $state, moment, $rootScope, cashboxDS, logger) {
    var vm = this;
    $rootScope.menu = true;
    $rootScope.NamePage = $filter('translate')('1932');
    $rootScope.helpReference = '08.billing/creditnotecombo.htm';
    $rootScope.pageview = 3;
    vm.title = 'creditnotecombo';

    vm.formatDate = localStorageService.get('FormatoFecha').toUpperCase();
    vm.formatDateHours = localStorageService.get('FormatoFecha').toUpperCase() + ', h:mm:ss a';
    vm.comment = '';
    vm.detail = [];
    vm.customMenu = {
      menubar: false,
      language: $filter('translate')('0000') === 'esCo' ? 'es' : 'en',
      br_newline_selector: true,
      force_br_newlines: true,
      force_p_newlines: false,
      forced_root_block: false,
      convert_newlines_to_brs: true,
      plugins: [
        'link',
        'lists',
        'autolink',
        'anchor',
        'textcolor',
        'charmap'

      ],
      toolbar: [
        'undo redo | bold italic underline superscript | fontselect fontsizeselect forecolor backcolor charmap | alignleft aligncenter alignright alignfull | numlist bullist outdent indent code '
      ],
      height: 300
    };

    vm.getinvoice = getinvoice;
    vm.isAuthenticate = isAuthenticate;
    vm.init = init;
    vm.createCreditnote = createCreditnote;
    vm.modalError = modalError;

    function createCreditnote() {
      vm.loading = true;
      var creditnote = {
        "idInvoice": vm.bill,
        "cancellationReason": vm.comment,
        "totalOrders": vm.detail.orders.length
      }
      var auth = localStorageService.get('Enterprise_NT.authorizationData');
      return cashboxDS.creditnotecombo(auth.authToken, creditnote).then(function (data) {
        vm.loading = false;
        if (data.status === 200) {
          logger.info($filter('translate')('1617') + ' ' + data.data.id);
          vm.comment = "";
          vm.bill = "";
          vm.detail = [];
        }
      },
      function (error) {
        vm.loading = false;
        vm.dataerror = '';
        if (vm.dataerror === '') {
          vm.modalError(error);
        };
      });
    }

    function getinvoice($event) {
      var keyCode = $event !== undefined ? $event.which || $event.keyCode : 13;
      if (keyCode === 13) {
        if (vm.bill !== '' && vm.bill !== undefined) {
          vm.loading = true;
          vm.comment = "";
          vm.detail = [];
          var auth = localStorageService.get('Enterprise_NT.authorizationData');
          return cashboxDS.getinvoicecombo(auth.authToken, vm.bill).then(function (data) {
            if (data.status === 200) {
              data.data.dateOfInvoice = moment(data.data.dateOfInvoice).format(vm.formatDate);
              vm.detail = data.data;
            } else {
              UIkit.modal('#nofoundfilter').show();
            }
            vm.loading = false;
          },
            function (error) {
              vm.loading = false;
              vm.modalError(error);
            });
        }
      }
    }

    function init() {
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
