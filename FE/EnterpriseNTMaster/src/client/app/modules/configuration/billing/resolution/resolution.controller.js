(function () {
  'use strict';

  angular
    .module('app.resolution')
    .controller('ResolutionController', ResolutionController)
    .controller('dependeceresolutionController', dependeceresolutionController)
    .controller('saveController', saveController);


  ResolutionController.$inject = ['resolutionDS', 'configurationDS', 'ModalService', 'localStorageService', 'logger',
    '$filter', '$state', 'moment', '$rootScope', 'providerDS', 'LZString', '$translate'
  ];

  function ResolutionController(resolutionDS, configurationDS, ModalService, localStorageService, logger,
    $filter, $state, moment, $rootScope, providerDS, LZString, $translate) {
    var vm = this;
    $rootScope.menu = true;
    $rootScope.blockView = true;
    vm.init = init;
    vm.title = 'Resolution';
    vm.resolutionDIAN = ['resolutionDIAN', 'prefix', 'state'];
    vm.prefix = ['prefix', 'resolutionDIAN', 'state'];
    vm.state = ['-state', '+resolutionDIAN', '+prefix'];
    vm.sortReverse = false;
    vm.sortType = vm.resolutionDIAN;
    vm.selected = -1;
    vm.Detail = [];
    vm.isDisabled = true;
    vm.isDisabledAdd = false;
    vm.isDisabledEdit = true;
    vm.isDisabledSave = true;
    vm.isDisabledCancel = true;
    vm.isDisabledPrint = false;
    vm.isDisabledState = true;
    vm.isAuthenticate = isAuthenticate;
    vm.get = get;
    vm.getId = getId;
    vm.New = New;
    vm.Edit = Edit;
    vm.changeState = changeState;
    vm.cancel = cancel;
    vm.insert = insert;
    vm.update = update;
    vm.save = save;
    vm.modalError = modalError;
    vm.removeData = removeData;
    vm.stateButton = stateButton;
    vm.generateFile = generateFile;
    var auth;
    vm.getConfigurationFormatDate = getConfigurationFormatDate;
    vm.errorservice = 0;
    vm.modalrequired = modalrequired;
    vm.getlistprovider = getlistprovider;
    vm.listprovider = [];
    vm.windowOpenReport = windowOpenReport;
    vm.loadingdata = true;
    vm.Detail = {
      'fromNumber': 1,
      'Number': 2,
      'toNumber': 1,
      'initialNumber': 1,
    }
    //** Metodo que elimina los elementos sobrantes en la grilla**//
    function removeData(data) {
      var auth = localStorageService.get('Enterprise_NT.authorizationData');
      data.data.forEach(function (value, key) {
        delete value.user;
        delete value.lastTransaction;
        data.data[key].username = auth.userName;
        data.data[key].username = auth.userName;
      });
      return data.data;
    }
    //** Metodo configuración formato**//
    function getConfigurationFormatDate() {
      vm.get();
      var auth = localStorageService.get('Enterprise_NT.authorizationData');
      return configurationDS.getConfigurationKey(auth.authToken, 'FormatoFecha').then(function (data) {
        if (data.status === 200) {
          vm.formatDate = data.data.value.toUpperCase();
        }
      }, function (error) {
        vm.modalError(error);
      });
    }
    //** Metodo trae un lista de entidades activas**//
    function getlistprovider() {
      var auth = localStorageService.get('Enterprise_NT.authorizationData');
      return providerDS.getstate(auth.authToken).then(function (data) {
        if (data.status === 200) {
          vm.listprovider = data.data;
          vm.getConfigurationFormatDate();
        } else {
          vm.modalrequired();
        }
      }, function (error) {
        vm.modalError(error);
      });
    }
    //** Metodo que valida la autenticación**//
    function isAuthenticate() {
      var auth = localStorageService.get('Enterprise_NT.authorizationData');
      if (auth === null || auth.token) {
        $state.go('login');
      } else {
        vm.init();
      }
    }
    //** Metodo que habilita y desabilita los botones**//
    function stateButton(state) {
      if (state === 'init') {
        vm.isDisabledAdd = false;
        vm.isDisabledEdit = vm.Detail.id === null || vm.Detail.id === undefined ? true : false;
        vm.isDisabledSave = true;
        vm.isDisabledCancel = true;
        vm.isDisabledPrint = false;
        vm.isDisabled = true;
        vm.isDisabledState = true;
      }
      if (state === 'add') {
        vm.isDisabledAdd = true;
        vm.isDisabledEdit = true;
        vm.isDisabledSave = false;
        vm.isDisabledCancel = false;
        vm.isDisabledPrint = true;
        vm.isDisabled = false;
        setTimeout(function () {
          document.getElementById('name').focus()
        }, 100);
      }
      if (state === 'edit') {
        vm.isDisabledState = false;
        vm.isDisabledAdd = true;
        vm.isDisabledEdit = true;
        vm.isDisabledSave = false;
        vm.isDisabledCancel = false;
        vm.isDisabledPrint = true;
        vm.isDisabled = false;
        setTimeout(function () {
          document.getElementById('name').focus()
        }, 100);
      }
      if (state === 'insert') {
        vm.isDisabledAdd = false;
        vm.isDisabledEdit = false;
        vm.isDisabledSave = true;
        vm.isDisabledCancel = true;
        vm.isDisabledPrint = false;
        vm.isDisabled = true;
      }
      if (state === 'update') {
        vm.isDisabledAdd = false;
        vm.isDisabledEdit = false;
        vm.isDisabledSave = true;
        vm.isDisabledCancel = true;
        vm.isDisabledPrint = false;
        vm.isDisabled = true;
        vm.isDisabledState = true;
      }
    }
    //** Método que  inicializa y habilita los controles cuando se da click en el botón nuevo**//
    function New(form) {
      form.$setUntouched();
      vm.usuario = '';
      vm.selected = -1;
      vm.Detail = {
        'user': {
          'id': auth.id
        },
        'id': null,
        'resolutionDIAN': '',
        'fromNumber': 1,
        'Number': 2,
        'toNumber': 0,
        'prefix': '',
        'provider': {},
        'initialNumber': 1,
        'state': true
      };
      vm.stateButton('add');
    }
    //** Método que habilita  o desabilita los controles cuando se da click en el botón cancelar**//
    function cancel(Form) {
      Form.$setUntouched();
      if (vm.Detail.id === null || vm.Detail.id === undefined) {
        vm.Detail = [];
      } else {
        vm.getId(vm.Detail.id, vm.selected, Form);
      }
      vm.stateButton('init');
    }
    //** Método que habilita  o desabilita los controles cuando se da click en el botón editar**//
    function Edit() {
      vm.stateButton('edit');
    }
    //** Método que evalua si se  va crear o actualizar**//
    function save(Form) {
      vm.loadingdata = true;
      Form.$setUntouched();
      var validated = [];
      if (vm.Detail.state === true) {
        validated = $filter("filter")(vm.data, function (e) {
          return e.provider.id === vm.Detail.provider.id && e.state === true && e.id !== vm.Detail.id;
        })
      }
      if (validated.length === 0) {
        if (vm.Detail.id === null) {
          vm.insert();
        } else {
          vm.update();
        }
      } else {
        ModalService.showModal({
          templateUrl: 'confirmsave.html',
          controller: 'saveController',
        }).then(function (modal) {
          modal.element.modal();
          modal.close.then(function (result) {
            if (result.page === 'Yes') {
              validated.forEach(function (value, key) {
                value.state = false;
                var auth = localStorageService.get('Enterprise_NT.authorizationData');
                value.user={'id':auth.id};
                return resolutionDS.update(auth.authToken, value).then(function (data) {
                  if(data.data.id===validated[validated.length-1].id){
                    if (vm.Detail.id === null) {
                      vm.insert();
                    } else {
                      vm.update();
                    }
                  }
                }, function (error) {
                  vm.modalError(error);
                });
              });
            } else {
              vm.loadingdata = false;
            }
          });
        });
      }
    }
    //** Método se comunica con el dataservice e inserta**//
    function insert() {
      var auth = localStorageService.get('Enterprise_NT.authorizationData');
      vm.Detail.toNumber = vm.Detail.Number;
      return resolutionDS.New(auth.authToken, vm.Detail).then(function (data) {
        if (data.status === 200) {
          vm.get();
          vm.Detail = data.data;
          vm.Detail.Number = vm.Detail.toNumber;
          vm.stateButton('insert');
          logger.success($filter('translate')('0042'));
          return data;
        }
      }, function (error) {
        vm.modalError(error);
      });
    }
    //** Método se comunica con el dataservice y actualiza**//
    function update() {
      vm.Detail.toNumber = vm.Detail.Number;
      var auth = localStorageService.get('Enterprise_NT.authorizationData');
      vm.Detail.user.id = auth.id;
      return resolutionDS.update(auth.authToken, vm.Detail).then(function (data) {
        if (data.status === 200) {
          vm.get();
          logger.success($filter('translate')('0042'));
          vm.stateButton('update');
          return data;
        }
      }, function (error) {
        vm.modalError(error);

      });
    }
    //** Método para sacar el popup de error**//
    function modalError(error) {
      vm.loadingdata = false;
      vm.Error = error;
      vm.ShowPopupError = true;
    }
    //** Método muestra un popup de confirmación para el cambio de estado**//
    function changeState() {
      if (!vm.isDisabledState) {
        vm.ShowPopupState = true;
      }
    }
    //** Método que obtiene la lista para llenar la grilla**//
    function get() {
      auth = localStorageService.get('Enterprise_NT.authorizationData');
      return resolutionDS.get(auth.authToken).then(function (data) {
        vm.data = data.data.length === 0 ? data.data : removeData(data);
        vm.loadingdata = false;
        return vm.data;
      }, function (error) {
        vm.modalError(error);
      });
    }
    //** Método se comunica con el dataservice y trae los datos por el id**//
    function getId(id, index, Form) {
      var auth = localStorageService.get('Enterprise_NT.authorizationData');
      vm.selected = id;
      vm.Detail = [];
      Form.$setUntouched();
      vm.loadingdata = true;
      return resolutionDS.getId(auth.authToken, id).then(function (data) {
        if (data.status === 200) {
          vm.usuario = $filter('translate')('0017') + ' ';
          vm.usuario = vm.usuario + moment(data.data.lastTransaction).format(vm.formatDate) + ' - ';
          vm.usuario = vm.usuario + data.data.user.userName;
          vm.Detail = data.data;
          vm.Detail.Number = vm.Detail.toNumber;
          vm.loadingdata = false;
          vm.stateButton('update');
        }
      }, function (error) {
        vm.modalError(error);
      });
    }
    //** Ventana modal de los requeridos**//
    function modalrequired() {
      if (vm.listprovider.length === 0) {
        ModalService.showModal({
          templateUrl: 'Requerido.html',
          controller: 'dependeceresolutionController',
          inputs: {
            providerhide: vm.listprovider.length
          }
        }).then(function (modal) {
          modal.element.modal();
          modal.close.then(function (result) {
            $state.go(result.page);
          });
        });

      }
    }
    //** Método  para imprimir el reporte**//
    function generateFile() {
      if (vm.filtered.length === 0) {
        vm.open = true;
      } else {
        vm.variables = {};
        vm.datareport = vm.filtered;
        vm.pathreport = '/report/configuration/billing/resolution/resolution.mrt';
        vm.openreport = false;
        vm.report = false;
        vm.windowOpenReport();
      }
    }
    // función para ver pdf el reporte detallado del error
    function windowOpenReport() {
      var parameterReport = {};
      parameterReport.variables = vm.variables;
      parameterReport.pathreport = vm.pathreport;
      parameterReport.labelsreport = JSON.stringify($translate.getTranslationTable());
      var datareport = LZString.compressToUTF16(JSON.stringify(vm.datareport));
      localStorageService.set('parameterReport', parameterReport);
      localStorageService.set('dataReport', datareport);
      window.open('/viewreport/viewreport.html');
    }
    //** Método que carga los metodos que inicializa la pagina*//
    function init() {
      vm.getlistprovider();
    }
    vm.isAuthenticate();
  }
  //** Controller de la vetana modal de requerido*//
  function dependeceresolutionController($scope, providerhide, close) {
    $scope.providerhide = providerhide;
    $scope.close = function (page) {
      close({
        page: page
      }, 500); // close, but give 500ms for bootstrap to animate
    };
  }
  function saveController($scope, close) {
    $scope.close = function (page) {
      close({
        page: page
      }, 500); // close, but give 500ms for bootstrap to animate
    };
  }
})();
