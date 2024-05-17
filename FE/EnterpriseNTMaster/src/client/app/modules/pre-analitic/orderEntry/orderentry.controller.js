
(function () {
    'use strict';

    angular
      .module('app.orderentry')
      .controller('orderentryController', orderentryController);


    orderentryController.$inject = ['areaDS', 'listDS','configurationDS', 'localStorageService', 'logger',
                              '$filter','$state', 'moment','$rootScope'];

    function orderentryController(areaDS, listDS,configurationDS, localStorageService,
                            logger,  $filter, $state, moment, $rootScope) {

        var vm = this;
        $rootScope.menu = false;
        $rootScope.blockView = true;
       
        vm.init = init;
        vm.title = 'Area';
        vm.sortType = 'ordering';
        vm.sortReverse = true;
        vm.selected = -1;
        vm.areaDetail = [];
        vm.isDisabled = true;
        vm.isDisabledAdd = false;
        vm.isDisabledEdit = true;
        vm.isDisabledSave = true;
        vm.isDisabledCancel = true;
        vm.isDisabledPrint = false;
        vm.isDisabledState = true;
        vm.isAuthenticate = isAuthenticate;
        vm.getAreas = getAreas;
        vm.getAreaById = getAreaById;
        vm.getListLaboratory = getListLaboratory;
        vm.editArea = editArea;
        vm.addArea = addArea;
        vm.updateArea = updateArea;
        vm.insertArea = insertArea;
        vm.removeData = removeData;
        vm.cancelArea = cancelArea;
        vm.saveArea = saveArea;
        vm.stateButton = stateButton;
        vm.changeState = changeState;
        vm.modalError = modalError;
        vm.generateFile = generateFile;
        vm.orderingRepeat = false;
        vm.nameRepeat = false;
        vm.abbreviationRepeat = false;
        vm.getConfigurationFormatDate = getConfigurationFormatDate;
        vm.errorservice = 0;
     

        /**
          Accion que sirve para eliminar una columna de una tabla a partir de un objeto area
          @author adiaz
          @param data Objeto de tipo areas
          @return objeto de tipo areas con las columnas necesarias 
          @version 0.0.1
        */
        function removeData(data) {
            var dataArea = [];          
            var auth = localStorageService.get('Enterprise_NT.authorizationData');
            data.data.forEach(function (value, key) {
                delete value.user;
                delete value.abbreviation;
                delete value.color;
                delete value.partialValidation;
                delete value.lastTransaction;

                if (value.id === 1) {
                    delete data.data[key];
                }
                else {
                    data.data[key].username = auth.userName;
                    dataArea.push(data.data[key]);
                }

            });


            return dataArea;
        }
         //** Metodo configuración formato**//
         function getConfigurationFormatDate(){
          var auth = localStorageService.get('Enterprise_NT.authorizationData');
           return configurationDS.getConfigurationKey(auth.authToken,'FormatoFecha').then(function(data) {
              if(data.status === 200) {
                vm.formatDate = data.data.value.toUpperCase();
              }
           }, function(error) {
              if (vm.errorservice === 0) {
                    vm.modalError(error);
                    vm.errorservice = vm.errorservice + 1;
                }
           });
        }
        /**
           Funcion para consultar el listado de areas existentes en el sistema
           @author adiaz
           @return data Objeto de tipo areas
           @version <0 className="0 1"></0>
        */
        function getAreas() {
           
            var auth = localStorageService.get('Enterprise_NT.authorizationData');
            return areaDS.getAreas(auth.authToken).then(function (data) {
                vm.dataAreas = vm.removeData(data);
                vm.dataAreas.length = data.data.length - 1; 
                $rootScope.blockView = false;              
            }, function (error) {
                vm.errorservice = vm.errorservice + 1;
                vm.modalError(error);
            });
        }

        /**
          Funcion consultar el detalle de un area.
          @author adiaz
          @param id: identificador del area a consultar 
                 index: objeto seleccionado de la tabla. 
                 form: formulario de controles para cargar datos. 
          @return data Objeto de tipo areas
          @version 0.0.1
        */
        function getAreaById(id, index, form) {
            var auth = localStorageService.get('Enterprise_NT.authorizationData');
            vm.selected = index;
            vm.areaDetail = [];
            vm.orderingRepeat = false;
            vm.nameRepeat = false;
            vm.abbreviationRepeat = false;

            form.$setUntouched();
            return areaDS.getAreaById(auth.authToken, id).then(function (data) {
                if (data.status === 200) {
                    vm.usuario = $filter('translate')('0017') + ' ';
                    vm.usuario = vm.usuario + moment(data.data.lastTransaction).format(vm.formatDate) + ' - ';
                    vm.usuario = vm.usuario + data.data.user.userName;
                    vm.oldAreaDetail = data.data;
                    vm.areaDetail = data.data;
                    vm.stateButton('update');
                }
            }, function (error) {
                vm.modalError(error);
            });
        }

        /**
          Funcion consultar el listado de los tipos de laboratorio del sistema
          @author adiaz
          @return data Objeto de tipo laboratorios
          @version 0.0.1
        */
        function getListLaboratory() {
            var auth = localStorageService.get('Enterprise_NT.authorizationData');

            return listDS.getList(auth.authToken, 1).then(function (data) {
                if (data.status === 200) {
                    vm.ListLaboratory = data.data;
                }
            }, function (error) {
                if (vm.errorservice === 0) { vm.modalError(error); }
            });
        }

        /**
          Funcion para evaluar si un area se va a actualizar o a insertar
          @author adiaz
          @param form: formulario de controles para cargar datos. 
          @return data Objeto de tipo areas
          @version 0.0.1
        */
        function saveArea(form) {
            form.$setUntouched();
            vm.orderingRepeat = false;
            var seleccionado = vm.selected;
            if (vm.areaDetail.id === null) {
                vm.insertArea();
            }
            else {
                vm.updateArea();
            }
        }

        /**
          Funcion ejecutar el servicio que actualiza los datos de un area
          @author adiaz
          @param form: formulario de controles para cargar datos. 
          @return objeto de tipo areas o error devuelto por el servicio
          @version 0.0.1
        */
        function updateArea() {
            var auth = localStorageService.get('Enterprise_NT.authorizationData');
            vm.areaDetail.user.id = auth.id;

            return areaDS.updateArea(auth.authToken, vm.areaDetail).then(function (data) {
                if (data.status === 200) {
                    vm.getAreas();
                    logger.success($filter('translate')('0042'));
                    vm.stateButton('update');
                    return data;
                }
            }, function (error) {
                vm.modalError(error);
            });
        }

        /**
          Funcion ejecutar el servicio que inserta los datos de un area.
          @author adiaz
          @param form: formulario de controles para cargar datos. 
          @return Objeto de tipo areas o error devuelto por el servicio
          @version 0.0.1
        */
        function insertArea() {

            var auth = localStorageService.get('Enterprise_NT.authorizationData');
         
            return areaDS.insertArea(auth.authToken, vm.areaDetail).then(function (data) {

                if (data.status === 200) {
                    vm.areaDetail = data.data;
                    vm.getAreas();
                    vm.stateButton('insert');
                    logger.success($filter('translate')('0042'));
                   
                    return data;
                }

            }, function (error) {
                vm.modalError(error);
            });
        }

        /**
          Funcion ejecutar el servicio que inserta los datos de un area.
          @author adiaz
          @param form: formulario de controles para cargar datos. 
          @return Objeto de tipo areas o error devuelto por el servicio
          @version 0.0.1
        */
        function editArea() {
            vm.stateButton('edit');
        }

        /**
          Funcion para habilitar los controles del form.
          @author adiaz
          @param form: formulario de controles para cargar datos. 
          @version 0.0.1
        */
        function addArea(form) {
            form.$setUntouched();
            var auth = localStorageService.get('Enterprise_NT.authorizationData');

            vm.usuario = '';
            vm.selected = -1;
            vm.areaDetail = {
                'abbreviation': '',
                'color': '#FFFFFF',
                'id': null,
                'lastTransaction': '',
                'name': '',
                'ordering': null,
                'partialValidation': true,
                'state': true,
                'type': {
                    'id': 3
                },
                'user': {
                    'id': auth.id
                }
            };
            vm.stateButton('add');
        }

        /**
          funcion para reversas todos los cambios que haya realizado el usuario sobre los datos de un area.
          @author adiaz
          @param form: formulario de controles para cargar datos. 
          @version 0.0.1
        */
        function cancelArea(form) {
            form.$setUntouched();
            vm.orderingRepeat = false;
            vm.nameRepeat = false;
            vm.abbreviationRepeat = false;

            if (vm.areaDetail.id === null || vm.areaDetail.id === undefined) {
                vm.areaDetail = [];
            }
            else {
                vm.getAreaById(vm.areaDetail.id, vm.selected, form);
            }

            vm.stateButton('init');
        }

        /**
          funcion para confirmar el cambio del estado que se realice sobre un area.
          @author adiaz
          @version 0.0.1
        */
        function changeState() {
            if (!vm.isDisabledState) {
                vm.ShowPopupState = true;
            }
        }

        /**
          funcion para generar informe en PDF O EXEL de areas
          @author adiaz
          @version 0.0.1
        */      
        function generateFile() {
             vm.pathreport = '/report/configuration/test/area/area.mrt';
            var Labels = [{
                '0329': $filter('translate')('0329'),
                '0018': $filter('translate')('0018'),
                '0011': $filter('translate')('0011'),
                '0014': $filter('translate')('0014'),
                '0272': $filter('translate')('0272'),
                '0273': $filter('translate')('0273'),
                '0344': $filter('translate')('0344'),
                '0068': $filter('translate')('0068'),
                '0492': $filter('translate')('0492')
                         }];
            vm.listreport = vm.filtered;
            vm.labelsreport = Labels;
        }

        //** Metodo que valida la autenticación**//
        function isAuthenticate() {
            var auth = localStorageService.get('Enterprise_NT.authorizationData');
            if (auth  === null || auth.token ) {
                $state.go('login');
            }
            else {
                vm.init();
            }
        }

        function stateButton(state) {
            if (state === 'init') {
                vm.isDisabledAdd = false;
                vm.isDisabledEdit = vm.areaDetail.id === null || vm.areaDetail.id === undefined ? true : false;
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
            }
            if (state === 'edit') {
                vm.isDisabledState = false;
                vm.isDisabledAdd = true;
                vm.isDisabledEdit = true;
                vm.isDisabledSave = false;
                vm.isDisabledCancel = false;
                vm.isDisabledPrint = true;
                vm.isDisabled = false;
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

        //** Método para sacar el popup de error**//
        function modalError(error) {

            if (error.data !== null) {
                if (error.data.code === 2) {
                    error.data.errorFields.forEach(function (value) {
                        var item = value.split('|');
                        if (item[0] === '1' && item[1] === 'ordering') {
                            vm.orderingRepeat = true;
                        }
                        if (item[0] === '1' && item[1] === 'name') {
                            vm.nameRepeat = true;
                        }
                        if (item[0] === '1' && item[1] === 'abbreviation') {
                            vm.abbreviationRepeat = true;
                        }
                    });
                }
            }
            if (vm.orderingRepeat === false && vm.nameRepeat === false && vm.abbreviationRepeat === false) {
                vm.Error = error;
                vm.ShowPopupError = true;
            }
        }

        /**
          funcion inicial que se ejecuta cuando se carga el modulo
          @author adiaz
          @version 0.0.1
        */
        function init() {
          
            vm.getAreas();
            vm.getListLaboratory();
            vm.getConfigurationFormatDate();
            vm.stateButton('init');           
        }

        vm.isAuthenticate();

    }
})();
