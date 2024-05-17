(function () {
  'use strict';
  angular.module('app.orderingdemographic')
    .controller('OrderingdemographicController', OrderingdemographicController)
    .controller('validateorderController', validateorderController);
  OrderingdemographicController.$inject = ['demographicDS', 'localStorageService',
    '$stateParams', 'logger', '$filter', '$state', '$rootScope', 'ModalService', 'configurationDS'
  ];

  function OrderingdemographicController(demographicDS, localStorageService,
    $stateParams, logger, $filter, $state, $rootScope, ModalService, configurationDS) {
    var vm = this;
    $rootScope.menu = true;
    $rootScope.blockView = true;
    vm.iddemo = $stateParams.id;
    vm.origin = 'H';
    vm.init = init;
    vm.title = 'orderingdemographic';
    vm.isAuthenticate = isAuthenticate;
    vm.getDemographics = getDemographics;
    vm.modalError = modalError;
    var auth;
    vm.filterOrigin = filterOrigin;
    vm.loadingdata = true;
    vm.updateDemographicsorder = updateDemographicsorder;
    vm.updateDemographicsordering = updateDemographicsordering;
    vm.getConfigurationFormatDate = getConfigurationFormatDate;
    vm.sortableOptions = {
      items: "li:not(.not-sortable)",
      cancel: ".not-sortable",
      update: function (e, ui) {
        ModalService.showModal({
          templateUrl: 'validateorder.html',
          controller: 'validateorderController',
        }).then(function (modal) {
          modal.element.modal();
          modal.close.then(function (result) {
            if (result === 'Yes') {
              vm.updateDemographicsorder();
            }
            if (result === 'No') {
              vm.getDemographics();
            }
          });
        });
      }
    }
    vm.isAuthenticate();
    //** Método para válidar que el usuario se encuentra logueado**//
    function isAuthenticate() {
      var auth = localStorageService.get('Enterprise_NT.authorizationData');
      if (auth === null || auth.token) {
        $state.go('login');
      } else {
        vm.init();
      }
    }
    //** Metodo configuración formato**//
    function getConfigurationFormatDate() {
      var auth = localStorageService.get('Enterprise_NT.authorizationData');
      return configurationDS.getConfigurationKey(auth.authToken, 'FormatoFecha').then(function (data) {
        vm.getDemographics();
        if (data.status === 200) {
          vm.formatDate = data.data.value.toUpperCase();
        }
      }, function (error) {
        vm.modalError(error);
      });
    }

    //** Método que carga los metodos que inicializa la pagina*//
    function init() {
      vm.getConfigurationFormatDate();
      
    }
    //** Método que obtiene una lista de demográficos**//
    function getDemographics() {
      auth = localStorageService.get('Enterprise_NT.authorizationData');
      vm.usuario = "";
      return demographicDS.getorderingAll(auth.authToken, vm.origin).then(function (data) {
        vm.dataDemographics = data.data;
        vm.usuario = $filter('translate')('0017') + ' ';

        var listTransactions = $filter('filter')(data.data, function (e) {
          return e.lastTransaction !== null && e.lastTransaction !== undefined
        });

        var lastTransaction = null;
        var date = moment(new Date()).format(vm.formatDate);
        var user = auth.userName;

        if (listTransactions) {
          lastTransaction = $filter('orderBy')(listTransactions, 'lastTransaction', 'desc')[0];
        }

        if (lastTransaction !== null && lastTransaction !== undefined) {
          date = lastTransaction.lastTransaction !== null ? moment(lastTransaction.lastTransaction).format(vm.formatDate) : moment(new Date()).format(vm.formatDate);
          user = lastTransaction.user.userName == null ? auth.userName : lastTransaction.user.userName;
        }

        vm.usuario = vm.usuario + date + ' - ';
        vm.usuario = vm.usuario + user;
        vm.dataDemographics = $filter('orderBy')(vm.dataDemographics, 'orderingDemo');
        vm.loadingdata = false;
        return vm.dataDemographics;
      }, function (error) {
        vm.modalError(error);
      });
    }
    //** Método que válida el cambio de tipo demográfico**//
    function filterOrigin(origin) {
      vm.loadingdata = true;
      vm.origin = origin;
      vm.getDemographics();
    }
    //** Método para sacar el popup de error**//
    function modalError(error) {
      vm.loadingdata = false;
      vm.Error = error;
      vm.ShowPopupError = true;
    }
    //** Método que guarda el orden de los demográficos**//
    function updateDemographicsorder() {
      var auth = localStorageService.get('Enterprise_NT.authorizationData');
      vm.loadingdata = true;
      vm.data = []
      for (var i = 0; i < vm.dataDemographics.length; i++) {
        vm.demographicsDetail = {
          'user': {
            'id': auth.id
          },
          'id': vm.dataDemographics[i].id,
          'orderingDemo': i + 1
        };
        vm.data.add(vm.demographicsDetail);
      }
      vm.updateDemographicsordering();
    }
    //** Método que Actualiza un demográfico**//
    function updateDemographicsordering() {
      var auth = localStorageService.get('Enterprise_NT.authorizationData');
      return demographicDS.demographicsorderingAll(auth.authToken, vm.data).then(function (data) {
        if (data.status === 200) {
          vm.getDemographics();
          vm.loadingdata = false;
          logger.success($filter('translate')('0042'));
        }
      }, function (error) {
        vm.modalError(error);
      });
    }
  }
  //**modal para confirmar si se guarda el orden de los demograficos*//
  function validateorderController($scope, close) {
    $scope.close = function (result) {
      close(result, 500);
    };
  }
})();
