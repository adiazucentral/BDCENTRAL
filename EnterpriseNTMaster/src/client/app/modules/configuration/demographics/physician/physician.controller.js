(function () {
  "use strict";
  angular
    .module("app.physician")
    .controller("PhysicianController", PhysicianController)
    .controller("RequeridoController", RequeridoController);
  PhysicianController.$inject = [
    "physicianDS",
    "configurationDS",
    "specialtyDS",
    "localStorageService",
    "logger",
    "$filter",
    "$state",
    "moment",
    "ModalService",
    "$rootScope",
    "LZString",
    "$translate",
  ];

  function PhysicianController(
    physicianDS,
    configurationDS,
    specialtyDS,
    localStorageService,
    logger,
    $filter,
    $state,
    moment,
    ModalService,
    $rootScope,
    LZString,
    $translate
  ) {
    var vm = this;
    $rootScope.menu = true;
    $rootScope.blockView = true;
    vm.init = init;
    vm.title = "Physician";
    vm.code = ["code", "namephysician", "specialty.name", "active"];
    vm.namephysician = ["namephysician", "code", "specialty.name", "active"];
    vm.specialty = ["specialty.name", "code", "namephysician", "active"];
    vm.active = ["-active", "+code", "+namephysician", "+specialty.name"];
    vm.sortReverse = false;
    vm.sortType = vm.code;
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
    var auth;
    vm.Repeatident = false;
    vm.Repeatname = false;
    vm.Repeatmmis = false;
    vm.Repeatcode = false;
    vm.emailInvalid = false;
    vm.Repeatlicense = false;
    vm.getListSpecialty = getListSpecialty;
    vm.errorservice = 0;
    vm.getConfiguration = getConfiguration;
    vm.getConfigurationmask = getConfigurationmask;
    vm.maskphone = "";
    vm.haspKey = true;
    vm.requeridKey = true;
    vm.getConfigurationFormatDate = getConfigurationFormatDate;
    vm.viewpaswword = false;
    vm.generateFile = generateFile;
    vm.windowOpenReport = windowOpenReport;
    vm.loadingdata = true;
    vm.afterSelection = afterSelection;
    // función que consulta la llave de configuración
    function getConfiguration() {
      var auth = localStorageService.get("Enterprise_NT.authorizationData");
      return configurationDS
        .getConfigurationKey(auth.authToken, "Facturacion")
        .then(
          function (data) {
            if (data.data.value === "2") {
              vm.haspKey = false;
              vm.requeridKey = true;
            } else {
              vm.haspKey = true;
              vm.requeridKey = false;
            }
            vm.getConfigurationmask();
          },
          function (error) {
            vm.modalError(error);
          }
        );
    }
    //** Metodo para evaluar que los escrito sea email**//
    function afterSelection(item) {
      if (
        !new RegExp(
          "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$"
        ).test(item)
      ) {
        vm.alternativeMails = $filter("filter")(
          vm.alternativeMails,
          "!" + item,
          true
        );
      }
    }
    //** Metodo configuración formato**//
    function getConfigurationFormatDate() {
      var auth = localStorageService.get("Enterprise_NT.authorizationData");
      return configurationDS
        .getConfigurationKey(auth.authToken, "FormatoFecha")
        .then(
          function (data) {
            if (data.status === 200) {
              vm.formatDate = data.data.value.toUpperCase();
              vm.getConfiguration();
            }
          },
          function (error) {
            vm.modalError(error);
          }
        );
    }
    // función que consulta la llave de configuración
    function getConfigurationmask() {
      var auth = localStorageService.get("Enterprise_NT.authorizationData");
      return configurationDS
        .getConfigurationKey(auth.authToken, "FormatoTelefono")
        .then(
          function (data) {
            if (data.data.value !== "") {
              vm.maskphone = data.data.value;
            }
            vm.getListSpecialty();
          },
          function (error) {
            vm.modalError(error);
          }
        );
    }
    // función que consulta el listado de especialidad
    function getListSpecialty() {
      vm.ListSpecialty = [];
      var auth = localStorageService.get("Enterprise_NT.authorizationData");
      return specialtyDS.Liststate(auth.authToken, true).then(
        function (data) {
          if (data.data.length === 0) {
            ModalService.showModal({
              templateUrl: "Requerido.html",
              controller: "RequeridoController",
            }).then(function (modal) {
              modal.element.modal();
              modal.close.then(function (result) {
                if (result === "No") {
                  $state.go("specialty");
                }
              });
            });
            vm.loadingdata = false;
          } else {
            vm.ListSpecialty = $filter("orderBy")(data.data, "name");
            vm.idSpecialty = vm.ListSpecialty[0];
            vm.get();
          }
        },
        function (error) {
          vm.modalError(error);
        }
      );
    }
    //** Metodo que elimina los elementos sobrantes en la grilla**//
    function removeData(data) {
      data.data.forEach(function (value, key) {
        value.namephysician = value.lastName + " " + value.name;
        value.username = auth.userName;
        value.searchall =
          value.code + value.namephysician + value.specialty.name;
      });
      return data.data;
    }
    //** Metodo que valida la autenticación**//
    function isAuthenticate() {
      var auth = localStorageService.get("Enterprise_NT.authorizationData");
      if (auth === null || auth.token) {
        $state.go("login");
      } else {
        vm.init();
      }
    }
    //** Metodo que habilita y desabilita los botones**//
    function stateButton(state) {
      if (state === "init") {
        vm.isDisabledAdd = false;
        vm.isDisabledEdit =
          vm.Detail.id === null || vm.Detail.id === undefined ? true : false;
        vm.isDisabledSave = true;
        vm.isDisabledCancel = true;
        vm.isDisabledPrint = false;
        vm.isDisabled = true;
        vm.isDisabledState = true;
      }
      if (state === "add") {
        vm.isDisabledAdd = true;
        vm.isDisabledEdit = true;
        vm.isDisabledSave = false;
        vm.isDisabledCancel = false;
        vm.isDisabledPrint = true;
        vm.isDisabled = false;
        setTimeout(function () {
          document.getElementById("identification").focus();
        }, 100);
      }
      if (state === "edit") {
        vm.isDisabledState = false;
        vm.isDisabledAdd = true;
        vm.isDisabledEdit = true;
        vm.isDisabledSave = false;
        vm.isDisabledCancel = false;
        vm.isDisabledPrint = true;
        vm.isDisabled = false;
        setTimeout(function () {
          document.getElementById("identification").focus();
        }, 100);
      }
      if (state === "insert") {
        vm.isDisabledAdd = false;
        vm.isDisabledEdit = false;
        vm.isDisabledSave = true;
        vm.isDisabledCancel = true;
        vm.isDisabledPrint = false;
        vm.isDisabled = true;
      }
      if (state === "update") {
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
      vm.usuario = "";
      vm.selected = -1;
      vm.Detail = {
        user: {
          id: auth.id,
        },
        id: null,
        identification: "",
        name: "",
        lastName: "",
        phone: "",
        fax: "",
        address1: "",
        address2: "",
        city: "",
        zipCode: "",
        obs: "",
        mmis: "",
        license: "",
        npi: "",
        institutional: "",
        additionalReport: true,
        userName: "",
        password: "",
        providerId: "",
        specialty: vm.idSpecialty,
        email: "",
        state: "",
        active: true,
      };
      vm.viewpaswword = true;
      vm.stateButton("add");
    }
    //** Método que habilita  o desabilita los controles cuando se da click en el botón cancelar**//
    function cancel(Form) {
      vm.Repeatident = false;
      vm.Repeatname = false;
      vm.Repeatmmis = false;
      vm.Repeatcode = false;
      vm.emailInvalid = false;
      vm.Repeatlicense = false;
      vm.viewpaswword = false;
      Form.$setUntouched();
      if (vm.Detail.id === null || vm.Detail.id === undefined) {
        vm.Detail = [];
      } else {
        vm.getId(vm.Detail.id, vm.selected, Form);
      }
      vm.stateButton("init");
    }
    //** Método que habilita  o desabilita los controles cuando se da click en el botón editar**//
    function Edit() {
      vm.stateButton("edit");
      vm.viewpaswword = false;
    }
    //** Método que evalua si se  va crear o actualizar**//
    function save(Form) {
      Form.$setUntouched();
      Form.identification.$touched = true;
      Form.code.$touched = true;
      Form.specialty.$touched = true;
      Form.name.$touched = true;
      Form.lastName.$touched = true;
      Form.email.$touched = true;
      vm.viewpaswword = false;
      vm.Detail.alternativeMails = JSON.stringify(vm.alternativeMails);
      if (!vm.haspKey) {
        Form.license.$touched = true;
      }
      if (
        !Form.identification.$invalid &&
        !Form.code.$invalid &&
        !Form.name.$invalid &&
        !Form.lastName.$invalid &&
        !Form.email.$invalid &&
        vm.haspKey
      ) {
        if (vm.Detail.id === null) {
          vm.insert();
        } else {
          vm.update();
        }
      } else if (
        !Form.identification.$invalid &&
        !Form.code.$invalid &&
        !Form.name.$invalid &&
        !Form.lastName.$invalid &&
        !Form.email.$invalid &&
        !vm.haspKey &&
        !Form.license.$invalid
      ) {
        if (vm.Detail.id === null) {
          vm.insert();
        } else {
          vm.update();
        }
      }
    }
    //** Método se comunica con el dataservice e inserta**//
    function insert() {
      var auth = localStorageService.get("Enterprise_NT.authorizationData");
      vm.Detail.mmis = vm.Detail.identification;
      vm.loadingdata = true;
      return physicianDS.New(auth.authToken, vm.Detail).then(
        function (data) {
          if (data.status === 200) {
            vm.loadingdata = false;
            vm.get();
            vm.Detail = data.data;
            vm.stateButton("insert");
            logger.success($filter("translate")("0042"));
            return data;
          }
        },
        function (error) {
          vm.modalError(error);
        }
      );
    }
    //** Método se comunica con el dataservice y actualiza**//
    function update() {
      vm.loadingdata = true;
      var auth = localStorageService.get("Enterprise_NT.authorizationData");
      vm.Detail.user.id = auth.id;
      vm.Detail.mmis = vm.Detail.identification;
      return physicianDS.update(auth.authToken, vm.Detail).then(
        function (data) {
          vm.loadingdata = false;
          if (data.status === 200) {
            vm.get();
            logger.success($filter("translate")("0042"));
            vm.stateButton("update");
            return data;
          }
        },
        function (error) {
          vm.modalError(error);
        }
      );
    }
    //** Método para sacar el popup de error**//
    function modalError(error) {
      vm.loadingdata = false;
      if (error.data !== null) {
        if (error.data.code === 2) {
          error.data.errorFields.forEach(function (value) {
            var item = value.split("|");
            if (item[0] === "1" && item[1] === "identification") {
              vm.Repeatident = true;
            }
            if (item[0] === "1" && item[1] === "userName") {
              vm.Repeatname = true;
            }
            if (item[0] === "1" && item[1] === "mmis") {
              vm.Repeatmmis = true;
            }
            if (item[0] === "1" && item[1] === "license") {
              vm.Repeatlicense = true;
            }
            if (item[0] === "1" && item[1] === "code") {
              vm.Repeatcode = true;
            }
            if (item[0] === "1" && item[1] === "email") {
              vm.emailInvalid = true;
            }
          });
        }
      }
      if (
        vm.Repeatident === false &&
        vm.Repeatname === false &&
        vm.Repeatmmis === false &&
        vm.Repeatcode === false &&
        vm.Repeatlicense === false &&
        vm.emailInvalid === false
      ) {
        vm.Error = error;
        vm.ShowPopupError = true;
      }
    }
    //** Método muestra un popup de confirmación para el cambio de estado**//
    function changeState() {
      if (!vm.isDisabledState) {
        vm.ShowPopupState = true;
      }
    }
    //** Método que obtiene la lista para llenar la grilla**//
    function get() {
      auth = localStorageService.get("Enterprise_NT.authorizationData");
      return physicianDS.get(auth.authToken).then(
        function (data) {
          vm.loadingdata = false;
          vm.data = data.data.length === 0 ? data.data : removeData(data);
          return vm.data;
        },
        function (error) {
          vm.modalError(error);
        }
      );
    }
    //** Método  para imprimir el reporte**//
    function generateFile() {
      if (vm.filtered.length === 0) {
        vm.open = true;
      } else {
        vm.variables = {};
        vm.datareport = vm.filtered;
        vm.pathreport =
          "/report/configuration/demographics/physician/physician.mrt";
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
      parameterReport.labelsreport = JSON.stringify(
        $translate.getTranslationTable()
      );
      var datareport = LZString.compressToUTF16(JSON.stringify(vm.datareport));
      localStorageService.set("parameterReport", parameterReport);
      localStorageService.set("dataReport", datareport);
      window.open("/viewreport/viewreport.html");
    }
    //** Método se comunica con el dataservice y trae los datos por el id**//
    function getId(id, index, Form) {
      vm.Repeatident = false;
      vm.Repeatname = false;
      vm.Repeatmmis = false;
      vm.Repeatcode = false;
      vm.Repeatlicense = false;
      vm.emailInvalid = false;
      vm.loadingdata = true;
      var auth = localStorageService.get("Enterprise_NT.authorizationData");
      vm.selected = id;
      vm.Detail = [];
      Form.$setUntouched();
      return physicianDS.getId(auth.authToken, id).then(
        function (data) {
          vm.loadingdata = false;
          if (data.status === 200) {
            vm.usuario = $filter("translate")("0017") + " ";
            vm.usuario =
              vm.usuario +
              moment(data.data.lastTransaction).format(vm.formatDate) +
              " - ";
            vm.usuario = vm.usuario + data.data.user.userName;
            vm.Detail = data.data;
            if (vm.Detail.alternativeMails !== undefined) {
              vm.alternativeMails = JSON.parse(vm.Detail.alternativeMails);
            }
            vm.stateButton("update");
          }
        },
        function (error) {
          vm.modalError(error);
        }
      );
    }
    //** Método que carga los metodos que inicializa la pagina*//
    function init() {
      vm.getConfigurationFormatDate();
    }
    vm.isAuthenticate();
  }
  //** Controller de la vetana modal de requerido*//
  function RequeridoController($scope, close) {
    $scope.close = function (result) {
      close(result, 500);
    };
  }
})();