(function () {
  'use strict';
  angular.module('app.configdemographics')
    .controller('configdemographicsController', configdemographicsController)
    .controller('ConfirmController', ConfirmController);

  configdemographicsController.$inject = ['configurationDS', 'authenticationsessionDS', 'demographicDS', 'demographicsItemDS', 'serviceDS',
    'ModalService', 'localStorageService', 'logger', '$filter', '$state', 'moment', '$rootScope', 'customerDS', 'centralsystemDS', 'areaDS'
  ];

  function configdemographicsController(configurationDS, authenticationsessionDS, demographicDS, demographicsItemDS, serviceDS,
    ModalService, localStorageService, logger, $filter, $state, moment, $rootScope, customerDS, centralsystemDS, areaDS) {
    var vm = this;
    //vm.viewulr = localStorageService.get('ManejoMultiSedes') === 'True';
    $rootScope.menu = true;
    $rootScope.blockView = true;
    vm.login = login;
    vm.visibleBranch = false;
    vm.invalidUser = false;
    vm.invalidDate = false;
    vm.user = {};
    vm.user.location = 1;
    vm.init = init;
    vm.title = 'generalconfig';
    vm.selected = -1;
    vm.isAuthenticate = isAuthenticate;
    vm.getListConfiguration = getListConfiguration;
    vm.changeState = changeState;
    vm.saveCancel = saveCancel;
    vm.cancel = cancel;
    vm.updateConfiguration = updateConfiguration;
    vm.getDemographicsALL = getDemographicsALL;
    vm.modalError = modalError;
    vm.getareactive = getareactive;
    vm.sectionId = 0;
    vm.getDemographicsItems = getDemographicsItems;
    vm.changewebquery = changewebquery;
    vm.version = localStorageService.get('Version');
    vm.getsystemcentralactive = getsystemcentralactive;
    vm.getcustomerhactive = getcustomerhactive;
    vm.requeridcentral = false;
    vm.date = {
      startDate: ''
    };
    vm.auth = localStorageService.get('Enterprise_NT.authorizationData');
    /*valida el usuario y la contraseña */
    function login(page) {
      vm.invalidUser = false;
      vm.invalidDate = false;
      vm.loadingdata = true;
      vm.menssageInvalid = '';
      if (vm.user.user && vm.user.password && vm.user.user === vm.auth.userName) {
        return authenticationsessionDS.loginlaboratory(vm.auth.authToken, vm.user).then(function (data) {
          if (data.data.success) {
            vm.loadingdata = false;
            $state.go(page);
            vm.sectionId = 1;
            vm.getListConfiguration();
          }
        }, function (error) {
          vm.loadingdata = false;
          if (error.data !== null) {
            if (error.data.message === 'timeout') {
              vm.menssageInvalid = $filter('translate')('1070');
            } else if (error.data.errorFields === null && error.data.message !== 'timeout') {
              vm.Error = error;
              vm.ShowPopupError = true;
            } else {
              if (error.data.errorFields[0] === 'La licencia registrada ha expirado.') {
                vm.menssageInvalid = $filter('translate')('1077');
              } else {
                error.data.errorFields.forEach(function (value) {
                  var item = value.split('|');
                  if (item[0] === '4') {
                    if (item[1] === 'inactive user') {
                      vm.menssageInvalid = $filter('translate')('1096');
                    } else {
                      vm.menssageInvalid = $filter('translate')('0097');
                    }
                  }
                  if (item[0] === '5') {
                    vm.menssageInvalid = $filter('translate')('0098');
                  }
                  if (item[0] === '3') {
                    vm.menssageInvalid = '';
                    vm.menssageInvalid = $filter('translate')('1095');
                  }
                  if (item[0] === '6') {
                    vm.Repeat = true;
                    if (item[1] === 'password expiration date') {
                      vm.menssageInvalid = 'la contraseña expiro debe cambiarla'
                      vm.administrator = item[3];
                    } else {
                      vm.menssageInvalid = $filter('translate')('1038');
                    }

                  }
                  if (item[0] === '7') {
                    if (item[1] === 'change password') {
                      vm.menssageInvalid = 'la contraseña expiro debe cambiarla'
                      vm.administrator = item[3];
                    }
                  }
                });
              }
            }
          }
        });
      } else {
        logger.info($filter('translate')('0097'));
        vm.menssageInvalid = $filter('translate')('0097');
        vm.loadingdata = false;
      }
    }
    //** Metodo configuración formato**//
    function getareactive() {
      var auth = localStorageService.get('Enterprise_NT.authorizationData');
      vm.user.location = auth.branch;
      return areaDS.getAreasActive(auth.authToken).then(function (data) {
        vm.getsystemcentralactive();
        if (data.status === 200) {
          vm.listarea = data.data;
        }
      }, function (error) {
        vm.modalError(error);
      });

    }

    //** Metodo que trae la sedes activas**//
    function getsystemcentralactive() {
      var auth = localStorageService.get('Enterprise_NT.authorizationData');
      vm.CentralSystem = [];
      return centralsystemDS.getCentralSystemActive(auth.authToken).then(function (data) {
        vm.getcustomerhactive();
        if (data.status === 200) {
          vm.CentralSystem = data.data;
        }
      }, function (error) {
        vm.modalError(error);
      });
    }
    //** Metodo que trae la sedes activas**//
    function getcustomerhactive() {
      var auth = localStorageService.get('Enterprise_NT.authorizationData');
      var noApply = '-- ' + $filter('translate')('0504') + ' --';
      vm.listcustomer = [{
        'id': 0,
        'name': noApply
      }];
      return customerDS.getCustomerstate(auth.authToken).then(function (data) {
        vm.getserviceactive();
        if (data.status === 200) {
          data.data.forEach(function (value, key) {
            vm.listcustomer.push({
              'id': value.id,
              'name': value.name
            });
          });
          vm.listcustomer = $filter('orderBy')(vm.listcustomer, 'name');
        }
      }, function (error) {
        vm.modalError(error);
      });
    }
    //** Metodo que trae los servicios activos**//
    vm.getserviceactive = getserviceactive;
    function getserviceactive() {
      var auth = localStorageService.get('Enterprise_NT.authorizationData');
      var noApply = '-- ' + $filter('translate')('0504') + ' --';
      vm.listservice = [{
        'id': 0,
        'name': noApply
      }];
      return serviceDS.getServiceActive(auth.authToken).then(function (data) {
        vm.getDemographicsALL();
        if (data.status === 200) {
          data.data.forEach(function (value, key) {
            vm.listservice.push({
              'id': value.id,
              'name': value.name
            });
          });
          vm.listservice = $filter('orderBy')(vm.listservice, 'name');
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
    //** Método que obtiene la lista para llenar la grilla**//
    function getListConfiguration() {
      vm.listdemographicItemstable = [];
      vm.loadingdata = true;
      var auth = localStorageService.get('Enterprise_NT.authorizationData');
      return configurationDS.getConfiguration(auth.authToken, 1).then(function (data) {
        if (data.status === 200) {
          vm.usuario = $filter('translate')('0017') + ' ';
          vm.usuario = vm.usuario + moment(data.data.lastTransaction).format(vm.formatDate) + ' - ';
          vm.usuario = vm.usuario + auth.userName;
          var DemograficoExcluirPrueba = $filter('filter')(data.data, {
            key: 'DemograficoExcluirPrueba'
          }, true)[0].value;
          var DemograficoInconsistensias = $filter('filter')(data.data, {
            key: 'DemograficoInconsistensias'
          }, true)[0].value;
          var Demograficencrypt = $filter('filter')(data.data, {
            key: 'DemograficoEncriptacionCorreo'
          }, true)[0].value;
          var DemograficoPyP = $filter('filter')(data.data, {
            key: 'DemograficoPyP'
          }, true)[0].value;
          var DemograficoTituloInforme = $filter('filter')(data.data, {
            key: 'DemograficoTituloInforme'
          }, true)[0].value;
          var UbicacionHospitalaria = $filter('filter')(data.data, {
            key: 'UbicacionHospitalaria'
          }, true)[0].value;
          var DemograficoHistograma = $filter('filter')(data.data, {
            key: 'DemograficoHistograma'
          }, true)[0].value;
          var DemograficoConsultaWeb = $filter('filter')(data.data, {
            key: 'DemograficoConsultaWeb'
          }, true)[0].value;

          var DemograficoEnvioCorreoAutomatico = $filter('filter')(data.data, {
            key: 'DemograficoEnvioCorreoAutomatico'
          }, true)[0].value;
          var ItemDemograficoEnvioCorreoAutomatico = $filter('filter')(data.data, {
            key: 'ItemDemograficoEnvioCorreoAutomatico'
          }, true)[0].value;
          var ItemDemograficoFacturacionEstado = $filter('filter')(data.data, {
            key: 'ItemDemograficoFacturacionEstado'
          }, true)[0].value;
          var ItemDemograficoFacturacionMunicipio = $filter('filter')(data.data, {
            key: 'ItemDemograficoFacturacionMunicipio'
          }, true)[0].value;
          var idItemDemoGestacion = $filter('filter')(data.data, {
            key: 'idItemDemoGestacion'
          }, true)[0].value;
          var idItemDemoEpsArs = $filter('filter')(data.data, {
            key: 'idItemDemoEpsArs'
          }, true)[0].value;
          var clientePorDefecto = $filter('filter')(data.data, {
            key: 'clientePorDefecto'
          }, true)[0].value;
          var servicioPorDefecto = $filter('filter')(data.data, {
            key: 'servicioPorDefecto'
          }, true)[0].value;
          var SistemaCentralListados = $filter('filter')(data.data, {
            key: 'SistemaCentralListados'
          }, true)[0].value;
          var demoTableros = $filter('filter')(data.data, {
            key: 'demoTableros'
          }, true)[0].value;
          var AgregaPruebaEditorDemo = $filter('filter')(data.data, {
            key: 'AgregaPruebaEditorDemo'
          }, true)[0].value;
          var AgregaPruebaEditorArea = $filter('filter')(data.data, {
            key: 'AgregaPruebaEditorArea'
          }, true)[0].value;

          vm.value = [{ //0
            'key': 'DemograficoExcluirPrueba',
            'value': {
              'id': DemograficoExcluirPrueba === null ||
                DemograficoExcluirPrueba === '' ||
                DemograficoExcluirPrueba === '0' ? 0 : DemograficoExcluirPrueba
            }
          }, //1
          {
            'key': 'DemograficoInconsistensias',
            'value': {
              'id': DemograficoInconsistensias === null ||
                DemograficoInconsistensias === '' ||
                DemograficoInconsistensias === '0' ? 0 : DemograficoInconsistensias
            }
          }, //2
          {
            'key': 'DemograficoPyP',
            'value': {
              'id': DemograficoPyP === null || DemograficoPyP === '' || DemograficoPyP === '0' ? 0 : DemograficoPyP
            }
          }, //3
          {
            'key': 'DemograficoTituloInforme',
            'value': {
              'id': DemograficoTituloInforme === null ||
                DemograficoTituloInforme === '' ||
                DemograficoTituloInforme === '0' ? 0 : DemograficoTituloInforme
            }
          }, //4
          {
            'key': 'ContraseñaDemograficoConsultaWeb',
            'value': $filter('filter')(data.data, {
              key: 'ContraseñaDemograficoConsultaWeb'
            }, true)[0].value
          }, //5
          {
            'key': 'DemograficoHistograma',
            'value': {
              'id': DemograficoHistograma === null ||
                DemograficoHistograma === '' ||
                DemograficoHistograma === '0' ? 0 : DemograficoHistograma
            }
          }, //6
          {
            'key': 'DemograficoItemHistograma',
            'value': {
              'id': $filter('filter')(data.data, {
                key: 'DemograficoItemHistograma'
              }, true)[0].value
            }
          }, //7
          {
            'key': 'ManejoDemograficoConsultaWeb',
            'value': $filter('filter')(data.data, {
              key: 'ManejoDemograficoConsultaWeb'
            }, true)[0].value === 'False' ? false : true
          }, //8
          {
            'key': 'DemograficoConsultaWeb',
            'value': DemograficoConsultaWeb === undefined ? undefined : {
              'id': DemograficoConsultaWeb
            }
          },
          { //9
            'key': 'DemograficoEncriptacionCorreo',
            'value': {
              'id': Demograficencrypt === null || Demograficencrypt === '' || Demograficencrypt === '0' ? 0 : Demograficencrypt
            }
          }, //10
          {
            'key': 'UbicacionHospitalaria',
            'value': {
              'id': UbicacionHospitalaria === null ||
                UbicacionHospitalaria === '' ||
                UbicacionHospitalaria === '0' ? 0 : UbicacionHospitalaria
            }
          },
          { //11
            'key': 'DemograficoEnvioCorreoAutomatico',
            'value': {
              'id': DemograficoEnvioCorreoAutomatico === null ||
                DemograficoEnvioCorreoAutomatico === '' ||
                DemograficoEnvioCorreoAutomatico === '0' ? 0 : DemograficoEnvioCorreoAutomatico
            }
          },
          { //12
            'key': 'ItemDemograficoEnvioCorreoAutomatico',
            'value': {
              'id': ItemDemograficoEnvioCorreoAutomatico === null ||
                ItemDemograficoEnvioCorreoAutomatico === '' ||
                ItemDemograficoEnvioCorreoAutomatico === '0' ? 0 : ItemDemograficoEnvioCorreoAutomatico
            }
          }, //13 SedeCentral
          {
            'key': 'ItemDemograficoFacturacionEstado',
            'value': {
              'id': ItemDemograficoFacturacionEstado === null ||
                ItemDemograficoFacturacionEstado === '' ||
                ItemDemograficoFacturacionEstado === '0' ? 0 : ItemDemograficoFacturacionEstado
            }
          },//14
          {
            'key': 'ItemDemograficoFacturacionMunicipio',
            'value': {
              'id': ItemDemograficoFacturacionMunicipio === null ||
                ItemDemograficoFacturacionMunicipio === '' ||
                ItemDemograficoFacturacionMunicipio === '0' ? 0 : ItemDemograficoFacturacionMunicipio
            }
          },//15
          {
            'key': 'idItemDemoGestacion',
            'value': {
              'id': idItemDemoGestacion === null || idItemDemoGestacion === '' || idItemDemoGestacion === '0' ? 0 : idItemDemoGestacion
            }
          },//16
          {
            'key': 'idItemDemoEpsArs',
            'value': {
              'id': idItemDemoEpsArs === null || idItemDemoEpsArs === '' || idItemDemoEpsArs === '0' ? 0 : idItemDemoEpsArs
            }
          },//17
          {
            'key': 'clientePorDefecto',
            'value': {
              'id': clientePorDefecto === null || clientePorDefecto === '' || clientePorDefecto === '0' ? 0 : clientePorDefecto
            }
          },//18
          {
            'key': 'ConsultarSistemaCentral',
            'value': $filter('filter')(data.data, {
              key: 'ConsultarSistemaCentral'
            }, true)[0].value === 'False' ? false : true
          },
          {//19
            'key': 'SistemaCentralListados',
            'value': SistemaCentralListados === undefined || SistemaCentralListados === '' || SistemaCentralListados === '0' ? undefined : {
              'id': SistemaCentralListados
            }
          },//20
          {
            'key': 'demoTableros',
            'value': {
              'id': demoTableros === null ||
                demoTableros === '' ||
                demoTableros === '0' ? 0 : demoTableros
            }
          }, //21
          {
            'key': 'itemDemoTableros',
            'value': {
              'id': $filter('filter')(data.data, {
                key: 'itemDemoTableros'
              }, true)[0].value
            }
          }, //22
          {
            'key': 'servicioPorDefecto',
            'value': {
              'id': servicioPorDefecto === null || servicioPorDefecto === '' || servicioPorDefecto === '0' ? 0 : servicioPorDefecto
            }
          }, //23
          {
            'key': 'AgregaPruebaEditoreportes',
            'value': $filter('filter')(data.data, {
              key: 'AgregaPruebaEditoreportes'
            }, true)[0].value === 'False' ? false : true
          },
          {//24
            'key': 'AgregaPruebaEditorDemo',
            'value': undefined  
          },
          {//25
            'key': 'AgregaPruebaEditorArea',
            'value': AgregaPruebaEditorArea === undefined || AgregaPruebaEditorArea === '' || AgregaPruebaEditorArea === '0' ? undefined : {
              'id': AgregaPruebaEditorArea
            }
          }
          ]

          if (vm.value[3].value.id !== null && vm.value[21].value.id !== 0 && vm.value[21].value.id !== '?') {
            vm.getDemographicsItemsreport(vm.value[3].value.id);
            vm.value[24] = {
              'key': 'AgregaPruebaEditorDemo',
              'value': AgregaPruebaEditorDemo === undefined || AgregaPruebaEditorDemo === '' || AgregaPruebaEditorDemo === '0' ? undefined : {
                'id': AgregaPruebaEditorDemo
              }
            }
          }

          if (vm.value[21].value.id !== null && vm.value[21].value.id !== 0 && vm.value[21].value.id !== '?') {
            vm.getDemographicsItems(vm.value[20].value.id);
            vm.value[21] = {
              'key': 'itemDemoTableros',
              'value': {
                'id': $filter('filter')(data.data, {
                  key: 'itemDemoTableros'
                }, true)[0].value
              }
            }
          }
          if (vm.value[6].value !== null) {
            if (vm.value[6].value.id !== null || vm.value[6].value.id !== 0 || vm.value[6].value.id !== '?') {
              vm.getDemographicsItems(vm.value[5].value.id);
              vm.value[6] = {
                'key': 'DemograficoItemHistograma',
                'value': {
                  'id': $filter('filter')(data.data, {
                    key: 'DemograficoItemHistograma'
                  }, true)[0].value
                }
              }
            }
          }
          if (vm.value[12].value.id !== null || vm.value[12].value.id !== 0 || vm.value[12].value.id !== '?') {
            vm.getDemographicsItems(vm.value[11].value.id);
            vm.value[6] = {
              'key': 'ItemDemograficoEnvioCorreoAutomatico',
              'value': {
                'id': $filter('filter')(data.data, {
                  key: 'ItemDemograficoEnvioCorreoAutomatico'
                }, true)[0].value
              }
            }
          }
          vm.loadingdata = false;
        }
      }, function (error) {
        vm.modalError(error);
      });
    }
    vm.changereport = changereport;
    //** Método que valida un valor y cambia el valor de los controles**//
    function changereport(Form) {
      Form.$setUntouched();
      if (vm.value[23].value === false) {
        vm.value[24].value = undefined
        vm.value[25].value = undefined
      }
    }
    //** Método que guarda o cancela la modificación de una llave de configuración**//
    function saveCancel(Form) {
      Form.$setUntouched();
      if (vm.value[18].value) {
        Form.centralsystem.$touched = true;
      }
      if (vm.value[22].value) {
        Form.AgregaPruebaEditorSede.$touched = true;
        Form.AgregaPruebaEditorArea.$touched = true;
      }
      if (vm.value[7].value) {
        Form.demographiwebquery.$touched = true;
        Form.password1changepassword.$touched = true;
      }
      if (Form.$valid) {
        if (vm.sectionId !== 0) {
          ModalService.showModal({
            templateUrl: 'Confirmation.html',
            controller: 'ConfirmController'
          }).then(function (modal) {
            modal.element.modal();
            modal.close.then(function (result) {
              if (result.execute === 'yes') {
                vm.updateConfiguration();
              } else {
                vm.getListConfiguration();
              }
            });
          });
        }
      }
    }
    //** Método que valida un valor y cambia el valor de los controles**//
    function changewebquery(Form) {
      Form.$setUntouched();
      if (vm.value[7].value === false) {
        vm.requeridquery = false;
        vm.value[8].value = undefined
      } else {
        vm.requeridquery = true;
      }
    }
    vm.changecentrasystem = changecentrasystem;
    //** Método que valida un valor y cambia el valor de los controles**//
    function changecentrasystem(Form) {
      Form.$setUntouched();
      if (vm.value[18].value === false) {
        vm.requeridcentral = false;
        vm.value[19].value = undefined
      } else {
        vm.requeridcentral = true;
      }
    }


    //** Método se comunica con el dataservice y actualiza**//
    function updateConfiguration() {
      vm.loadingdata = true;
      vm.value.forEach(function (valuekey, key) {
        if (key === 4) {
          valuekey.value = valuekey.value;
        } else if (key === 7 || key === 18 || key === 23) {
          valuekey.value = valuekey.value === false ? 'False' : 'True';
        } else if (key === 8) {
          valuekey.value = valuekey.value == "" || valuekey.value == undefined ? null : valuekey.value !== undefined ? valuekey.value.id : undefined;
        } else {
          valuekey.value = valuekey.value == "" || valuekey.value == undefined ? null : valuekey.value !== null ? valuekey.value.id : null;
        }
      })
      var auth = localStorageService.get('Enterprise_NT.authorizationData');
      return configurationDS.updateConfiguration(auth.authToken, vm.value).then(function (data) {
        if (data.status === 200) {
          data.data.forEach(function (value, key) {
            return localStorageService.set(value.key, value.value);
          });
          vm.getListConfiguration();
          logger.success($filter('translate')('0042'));
        }
      }, function (error) {
        vm.isDisabledSave = false;
        vm.loadingdata = false;
        vm.modalError(error);
      });
    }
    //** Método que cancela los cambios antes de guardar**//
    function cancel(form) {
      form.$setUntouched();
      vm.getListConfiguration();
    }
    //** Método para sacar el popup de error**//
    function modalError(error) {
      vm.Error = error;
      vm.ShowPopupError = true;
    }
    //** Método muestra un popup de confirmación para el cambio de estado**//
    function changeState() {
      if (!vm.isDisabledState) {
        vm.ShowPopupState = true;
      }
    }
    //** Método para consultar todos los demograficos y llenar los campos segun la necesidad**//
    function getDemographicsALL() {
      var auth = localStorageService.get('Enterprise_NT.authorizationData');
      return demographicDS.getDemographicsALL(auth.authToken).then(function (data) {
        var noApply = '-- ' + $filter('translate')('0504') + ' --';
        vm.listdemographicOnly = [{
          'id': 0,
          'name': noApply
        }];
        vm.listdemographic = [{
          'id': 0,
          'name': noApply
        }];
        vm.listdemographic2 = [{
          'id': 0,
          'name': noApply
        }];
        vm.listdemographicincosn = [{
          'id': 0,
          'name': noApply
        }];
        vm.listdemograpencored = [{
          'id': 0,
          'name': noApply
        }];
        vm.emailsend = [{
          'id': 0,
          'name': noApply
        }];
        vm.listdemographiwebquery = [];
        data.data.forEach(function (value, key) {
          if (value.id > 0) {
            vm.listdemographicOnly.push({
              'id': value.id,
              'name': value.name
            });
          }
          if (value.id > 0 && value.origin == 'H') {
            vm.listdemographicincosn.push({
              'id': value.id,
              'name': value.name
            });
          }

          switch (value.id) {
            case -1:
              data.data[key].name = $filter('translate')('0248');
              break;
            case -2:
              data.data[key].name = $filter('translate')('0225');
              break;
            case -3:
              data.data[key].name = $filter('translate')('0307');
              break;
            case -4:
              data.data[key].name = $filter('translate')('0133');
              break;
            case -5:
              data.data[key].name = $filter('translate')('0075');
              break;
            case -6:
              data.data[key].name = $filter('translate')('0175');
              break;
            case -7:
              data.data[key].name = $filter('translate')('0174');
              break;
            default:
              data.data[key].name = data.data[key].name;
          }
          vm.listdemographic.push({
            'id': value.id,
            'name': value.name
          });
          vm.listdemographic2.push({
            'id': value.id,
            'name': value.name
          });

          if (value.encoded && value.id !== -1 && value.id !== -2 && value.id !== -3 &&
            value.id !== -4 && value.id !== -5 && value.id !== -6 && value.id !== -7 &&
            value.id !== -10 && value.id !== -111 && value.id !== -106) {
            vm.listdemographiwebquery.push({
              'id': value.id,
              'name': value.name
            });
          }
          if (value.encoded) {
            vm.listdemograpencored.push({
              'id': value.id,
              'name': value.name
            });
          }
          if (value.encoded && value.state && value.id > 0) {
            vm.emailsend.push({
              'id': value.id,
              'name': value.name
            });
          }
        });
        vm.listdemographicOnly = $filter('orderBy')(vm.listdemographicOnly, 'name');
        vm.listdemographic = $filter('orderBy')(vm.listdemographic, 'name');
        vm.listdemographic2 = $filter('orderBy')(vm.listdemographic2, 'name');
        vm.listdemographicincosn = $filter('orderBy')(vm.listdemographicincosn, 'name');
        vm.listdemograpencored = $filter('orderBy')(vm.listdemograpencored, 'name');
        vm.listdemographiwebquery = $filter('orderBy')(vm.listdemographiwebquery, 'name');
      }, function (error) {
        vm.modalError();
      });
    }
    //** Método para consultar los item demográfico segun el padre**//
    function getDemographicsItems(id) {
      vm.value[6].value = null;
      var auth = localStorageService.get('Enterprise_NT.authorizationData');
      return demographicsItemDS.getDemographicsItemsFilter(auth.authToken, id).then(function (data) {
        vm.listdemographicItems = [];
        if (data === undefined) data.data = '';
        if (data.data.length > 0) {
          data.data.forEach(function (value, key) {
            vm.listdemographicItems.push({
              'id': value.id,
              'name': value.name
            });
          })
          vm.listdemographicItems = $filter('orderBy')(vm.listdemographicItems, 'name');
        } else {
          vm.listdemographicItems = undefined;
        }
      }, function (error) {
        vm.modalError();
      });
    }

    //** Método para consultar los item demográfico segun el padre**//
    vm.getDemographicsItemsreport = getDemographicsItemsreport;
    function getDemographicsItemsreport(id) {
      if (id !== 0) {
        vm.value[24].value = null;
        var auth = localStorageService.get('Enterprise_NT.authorizationData');
        return demographicsItemDS.getDemographicReportEncrypt(auth.authToken, id).then(function (data) {
          vm.listdemographicItemsreport = [];
          if (data === undefined) data.data = '';
          if (data.data.length > 0) {
            data.data.forEach(function (value, key) {
              vm.listdemographicItemsreport.push({
                'id': value.idDemographicItem,
                'name': value.name
              });
            })
            vm.listdemographicItemsreport = $filter('orderBy')(vm.listdemographicItemsreport, 'name');
          } else {
            vm.listdemographicItemsreport = undefined;
          }
        }, function (error) {
          vm.modalError();
        });
      } else {
        vm.value[23].value = false;
        vm.value[24].value = undefined
        vm.value[25].value = undefined
      }
    }

    vm.getDemographicsItemstable = getDemographicsItemstable;
    function getDemographicsItemstable(id) {
      vm.value[21].value = null;
      var auth = localStorageService.get('Enterprise_NT.authorizationData');
      return demographicsItemDS.getDemographicsItemsFilter(auth.authToken, id).then(function (data) {
        vm.listdemographicItemstable = [];
        if (data === undefined) data.data = '';
        if (data.data.length > 0) {
          data.data.forEach(function (value, key) {
            vm.listdemographicItemstable.push({
              'id': value.id,
              'name': value.name
            });
          })
          vm.listdemographicItemstable = $filter('orderBy')(vm.listdemographicItemstable, 'name');
        } else {
          vm.listdemographicItemstable = undefined;
        }
      }, function (error) {
        vm.modalError();
      });
    }
    //** Método que carga los metodos que inicializa la pagina*//
    function init() {
      vm.formatDate = localStorageService.get('FormatoFecha').toUpperCase();
      vm.getareactive();
    }
    vm.isAuthenticate();
  }
  //** Método para confirmar el guardado de la pagina*//
  function ConfirmController($scope, close) {
    $scope.close = function (execute) {
      close({
        execute: execute
      }, 500); // close, but give 500ms for bootstrap to animate
    };
  }
})();
