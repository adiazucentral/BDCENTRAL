/* jshint ignore:start */
(function () {
  "use strict";

  angular.module("app.listed").controller("ListedController", ListedController);

  ListedController.$inject = [
    "common",
    "reportadicional",
    "laboratoryDS",
    "reportsDS",
    "localStorageService",
    "logger",
    "listedOrderDS",
    "barcodeDS",
    "listedLaboratoryDS",
    "listedSampleDS",
    "areaDS",
    "sampleDS",
    "branchDS",
    "$filter",
    "$state",
    "moment",
    "$rootScope",
    "$translate",
    "LZString",
    "serviceDS",
    "userDS",
    "reviewofresultDS",
    "listDS"
  ];

  function ListedController(
    common,
    reportadicional,
    laboratoryDS,
    reportsDS,
    localStorageService,
    logger,
    listedOrderDS,
    barcodeDS,
    listedLaboratoryDS,
    listedSampleDS,
    areaDS,
    sampleDS,
    branchDS,
    $filter,
    $state,
    moment,
    $rootScope,
    $translate,
    LZString,
    serviceDS,
    userDS,
    reviewofresultDS,
    listDS
  ) {
    var vm = this;
    vm.isAuthenticate = isAuthenticate;
    $rootScope.pageview = 3;
    vm.init = init;
    vm.title = "Listed";
    $rootScope.menu = true;
    $rootScope.NamePage = $filter("translate")("0015").toUpperCase();
    $rootScope.helpReference = "01. LaboratoryOrders/Listed.htm";
    $rootScope.blockView = true;
    vm.getSample = getSample;
    vm.getArea = getArea;
    vm.getBranch = getBranch;
    vm.getservice = getservice;
    vm.changeCheckAreas = changeCheckAreas;
    vm.changeCheckSamples = changeCheckSamples;
    vm.samplesSelected = samplesSelected;
    vm.areasSelected = areasSelected;
    vm.clickFlange = clickFlange;
    vm.clickFilterListed = clickFilterListed;
    vm.getBarcode = getBarcode;
    vm.jsonPrint = jsonPrint;
    vm.generateDataReport = generateDataReport;
    vm.variablesReport = variablesReport;
    vm.generateFile = generateFile;
    vm.generateBarcodes = generateBarcodes;
    vm.windowOpenReport = windowOpenReport;
    vm.flasheaTexto = flasheaTexto;
    vm.isJsonString = isJsonString;
    vm.save = save;
    vm.formattakeDate = localStorageService.get("FormatoFecha").toUpperCase() + ", h:mm:ss a";
    vm.formatDateAge = localStorageService.get("FormatoFecha").toUpperCase();
    vm.formatDate = localStorageService.get("FormatoFecha").toUpperCase();
    vm.printStickerAditional =
      localStorageService.get("ImprimirEtiquetaAdicional") === "True";
    vm.viewremisiones =
      localStorageService.get("TemperaturaReporteRemisiones") === "True";
    vm.UrlNodeJs = localStorageService.get("UrlNodeJs");
    vm.abbrCustomer = localStorageService.get("Abreviatura");
    vm.nameCustomer = localStorageService.get("Entidad");
    vm.getlaboratory = getlaboratory;
    vm.clickFiltersample = clickFiltersample;
    vm.clickFiltersample1 = clickFiltersample1;
    vm.PopupError = false;
    vm.directImpression = directImpression;
    vm.prepotition = $filter("translate")("0000") === "esCo" ? "de" : "of";
    vm.separatorSample = localStorageService.get("SeparadorMuestra");
    vm.temperature = localStorageService.get("ManejoTemperatura") === "True";
    vm.getProcessPrint = getProcessPrint;
    vm.changeChecklaboratory = changeChecklaboratory;
    vm.printlabels = printlabels;
    vm.getsamples = getsamples;
    var auth = localStorageService.get("Enterprise_NT.authorizationData");
    var language = $filter("translate")("0000");
    vm.validateConfirmation = validateConfirmation;
    vm.closemodalconfirmation = closemodalconfirmation;
    vm.clickFilterstate = clickFilterstate;
    vm.clickFilterstatetest = clickFilterstatetest;
    vm.clickreport = clickreport;
    vm.search = 1;
    vm.order = '';
    vm.sample = '';
    vm.validage = validage;
    vm.getGender = getGender;
    vm.temperatureQuantityItems = 50;
    vm.number = [];
    vm.Environment = 1;
    vm.Frozen = 1;
    vm.Refrigerated = 1;
    vm.getUsers = getUsers;
    vm.printTests = true;

    $rootScope.$watch("ipUser", function () {
      vm.ipUser = $rootScope.ipUser;
    });

    function validateConfirmation(type) {
      vm.type = type;
      if (vm.flange === 3 && vm.remission) {
        var listfilter = _.filter(vm.listlaboratory, function (o) { return o.select === true });
        if (listfilter.length > 0) {
          if (vm.laboratory) {
            var auth = localStorageService.get("Enterprise_NT.authorizationData");
            var json = vm.jsonPrint();
            return listedOrderDS.getListedremmision(auth.authToken, json).then(
              function (data) {
                if (data.status === 200) {
                  vm.dataremisiones = data.data;
                  vm.dataremisiones = _.filter(vm.dataremisiones, function (o) {
                    o.tests = _.filter(o.tests, function (p) {
                      return p.selected = true;
                    });
                    return o.tests.length > 0
                  })
                  UIkit.modal('#referralsmodal').show();
                  vm.progressPrint = false;
                } else {
                  logger.error($filter("translate")("0152"));
                  vm.progressPrint = false;
                }
              },
              function (error) {
                vm.progressPrint = false;
                logger.error(error);
              }
            );
          } else {
            vm.progressPrint = false;
            logger.error($filter("translate")("1812"));
          }
        }
        else {
          vm.progressPrint = false;
          logger.error($filter("translate")("1866"));
        }
      } else {
        vm.generateFile();
      }
    }

    function save() {
      var datafilter = JSON.parse(JSON.stringify(vm.dataremisiones))
      datafilter = _.filter(vm.dataremisiones, function (o) {
        o.tests = _.filter(o.tests, function (p) {
          return p.selected === true;
        });
        return o.tests.length > 0
      })
      if (datafilter.length === 0) {
        logger.info($filter("translate")("1867"));
      } else {
        var data = {
          'laboratory': vm.laboratory.id,
          'orders': datafilter,
          'type': vm.laboratory.type
        }
        return listedOrderDS.savelaboratoryremmision(auth.authToken, data).then(

          function (data) {
            if (data.status === 200) {
              logger.success($filter("translate")("0149"));
              UIkit.modal('#referralsmodal').hide();
              switch (vm.filterListed) {
                case 2:
                  vm.fileReport =
                    "/Report/pre-analitic/listed/listedlaboratory/listedlabtest.mrt";
                  vm.orientation = "horizontal";
                  break;
                case 4:
                  vm.fileReport =
                    "/Report/pre-analitic/listed/listedlaboratory/listedlabcontainer.mrt";
                  vm.orientation = "horizontal";
                  break;
                case 5:
                  vm.fileReport =
                    "/Report/pre-analitic/listed/listedlaboratory/listedlabsample.mrt";
                  vm.orientation = "horizontal";
                  break;
              }

              vm.datareport = vm.generateDataReport(datafilter);
              vm.variables = vm.variablesReport();
              vm.pathreport = vm.fileReport;
              vm.windowOpenReport();

            } else {
              logger.error($filter("translate")("0152"));
              vm.progressPrint = false;
            }
          },
          function (error) {
            vm.progressPrint = false;
            logger.error(error);
          }
        );

      }
    }

    function clickFlange(id) {
      vm.laboratoryAll = true;
      vm.flange = id;
      vm.isOpenReport = false;
      vm.filterRange = "1";
      vm.rangeInit = "";
      vm.rangeend = "";

      vm.rangeInitAppoinment = "";
      vm.rangeEndAppoinment = "";

      vm.numFilterAreaTest = 0;
      vm.listAreas = [];
      vm.listTests = [];
      vm.listLaboratories = [];
      vm.demographics = [];
      vm.listSamples = [];
      vm.printAddLabel = false;
      vm.typeSample = "true";
      vm.appointmenttype = '0';
      vm.typetestpending = '1';
      vm.hl7 = false;
      vm.dataReport = [];
      vm.fileReport = "";
      vm.samplesAll = false;
      vm.areasAll = false;
      vm.progressPrint = false;
      vm.orientation = "vertical";
      vm.filterListed = id - 1;
      vm.filtersample = 0;
      vm.filtersample1 = 0;
      vm.stickerAditionals = 0;
      if (id === 1) {
        vm.laboratoryid = { id: null };
        vm.getlaboratory();
        vm.getGender();
        vm.getUsers();
      } else if (id === 2) {
        vm.getArea();
        vm.getSample();
        vm.samplesBarcode = [];
        vm.getBarcode();
      } else if (id === 3 || id === 6 || id === 7 || id === 1) {
        vm.filterstate = 0;
        vm.filterstatetest = 0;
        vm.filtereport = 0;
        vm.laboratoryid = { id: null };
        vm.getlaboratory();
      } else if (id === 4) {
        vm.getBranch();
        vm.filterRange = "0";
      } else if (id === 5) {
        vm.getUsersAnalyzers();
      }
    }

    function clickFilterListed(filter) {
      if (filter === 4) {
        vm.demosmask = "-110||-104"
      } else {
        vm.demosmask = "";
      }
      vm.filterListed = filter;
    }

    function clickFiltersample(filter) {
      vm.filtersample = filter;
    }

    function clickFiltersample1(filter) {
      vm.filtersample1 = filter;
    }

    function clickFilterstate(filter) {
      vm.filterstate = filter;
    }

    function clickFilterstatetest(filter) {
      vm.filterstatetest = filter;
    }

    function clickreport(filter) {
      vm.filtereport = filter;
    }

    function getBarcode() {
      var auth = localStorageService.get("Enterprise_NT.authorizationData");
      return barcodeDS.getBarcode(auth.authToken).then(
        function (data) {
          if (data.status === 200) {
            vm.listbarcodeadicional = $filter("filter")(data.data, {
              type: "2",
            });
            vm.stickerAditionals = $filter("filter")(vm.listbarcodeadicional, {
              active: true,
            }).length;
          }
        },
        function (error) {
          vm.modalError(error);
        }
      );
    }

    function getSample() {
      auth = localStorageService.get("Enterprise_NT.authorizationData");
      return sampleDS.getSample(auth.authToken).then(function (dataSample) {
        dataSample.data.sort(function (x, y) {
          return x.codesample == y.codesample
            ? 0
            : x.codesample > y.codesample
              ? 1
              : -1;
        });
        vm.dataSamples = [];
        dataSample.data.forEach(function (value) {
          vm.dataSamples.push({
            id: value.id,
            codesample: value.codesample,
            name: value.name,
            select: false,
            cansticker: value.canstiker,
          });
          //vm.dataSamples[key].select = false;
        });
      });
    }

    function getUsers() {
      vm.listusers = [];
      auth = localStorageService.get("Enterprise_NT.authorizationData");
      return userDS.getsimplelist(auth.authToken).then(function (data) {
        var all = {
          'id': -1,
          'name': $filter('translate')('0353')
        };
        if (data.data.length > 0) {
          data.data.forEach(function (value) {
            value.name = value.name + " " + value.lastName
          });
          vm.listusers = _.orderBy(data.data, ['name'], ['asc']);
          vm.listusers.unshift(all);
          vm.userid = { id: -1 };
        }
      });
    }

    function getlaboratory() {
      vm.listlaboratory = [];
      auth = localStorageService.get("Enterprise_NT.authorizationData");
      return laboratoryDS
        .getLaboratoryActive(auth.authToken)
        .then(function (data) {
          if (vm.flange === 1 || vm.flange === 7) {
            var all = [
              {
                'id': null,
                'name': $filter('translate')('0215')
              }
            ];
            if (data.data.length > 0) {
              vm.listlaboratory = all.concat(data.data);
              vm.laboratoryid = { id: null };
              vm.laboratoryremisiones = { id: null };
            }
          } else {
            vm.listlaboratory = data.data;
            vm.changeChecklaboratory();
          }

        });
    }

    function samplesSelected() {
      // Se arma el json para la lista de muestras
      vm.samplesBarcode = [];
      vm.dataSamples.forEach(function (value) {
        if (value.select) {
          vm.samplesBarcode.push({
            idSample: value.id,
            quantity: value.cansticker,
          });
        }
      });
      vm.listSamples = vm.samplesBarcode;
    }

    function changeCheckSamples() {
      vm.samplesBarcode = [];
      vm.dataSamples.forEach(function (value) {
        value.select = vm.samplesAll;
        if (value.select)
          vm.samplesBarcode.push({
            idSample: value.id,
            quantity: value.cansticker,
          });
      });
    }

    function getArea() {
      auth = localStorageService.get("Enterprise_NT.authorizationData");
      return areaDS.getAreas(auth.authToken).then(function (dataArea) {
        vm.dataAreas = dataArea.data;
        vm.dataAreas.forEach(function (value, key) {
          vm.dataAreas[key].select = false;
        });
      });
    }

    function areasSelected() {
      // Se arama el json para la lista de áreas
      var areas = [];
      vm.dataAreas.forEach(function (value) {
        if (value.id !== 1 && value.select) {
          areas.push(value.id);
        }
      });
      vm.listAreas = areas;
    }

    function changeCheckAreas() {
      vm.dataAreas.forEach(function (value) {
        if (value.id !== 1) {
          value.select = vm.areasAll;
        }
      });
    }

    function changeChecklaboratory() {
      if (vm.typelaboratory == '' || vm.typelaboratory == undefined) {
        vm.listlaboratory.forEach(function (value) {
          value.select = vm.laboratoryAll;
        });
      }
      else if (vm.typelaboratory == 2) {
        vm.listlaboratory.forEach(function (value) {
          if (value.type == 2) {
            value.select = vm.laboratoryAll;
          }
          else {
            value.select = false;
          }
        });
      }
      else if (vm.typelaboratory == 1) {
        vm.listlaboratory.forEach(function (value) {
          if (value.type == 1) {
            value.select = vm.laboratoryAll;
          }
          else {
            value.select = false;
          }
        });
      }
    }

    function getBranch() {
      var auth = localStorageService.get("Enterprise_NT.authorizationData");
      vm.listBranch = [
        {
          id: 0,
          name: "-- " + $filter("translate")("0162") + " --",
        },
      ];
      return branchDS.getBranch(auth.authToken).then(function (data) {
        if (vm.temperature) {
          vm.getservice();
        }
        if (data.status === 200) {
          data.data.forEach(function (value) {
            vm.listBranch.push({
              id: value.id,
              name: value.name,
            });
          });
          vm.listBranch.id = 0;
        }
        return vm.listBranch;
      });
    }

    function getservice() {
      var auth = localStorageService.get("Enterprise_NT.authorizationData");
      vm.listservice = [
        {
          id: 0,
          name: "-- " + $filter("translate")("0353") + " --",
        },
      ];
      return serviceDS.getServiceActive(auth.authToken).then(function (data) {
        vm.getsamples();
        if (data.status === 200) {
          data.data.forEach(function (value) {
            vm.listservice.push({
              id: value.id,
              name: value.name,
            });
          });
          vm.listservice.id = 0;
        }
      });
    }

    function getsamples() {
      var auth = localStorageService.get("Enterprise_NT.authorizationData");
      vm.sampletemperture = [
        {
          id: 0,
          name: "-- " + $filter("translate")("0353") + " --",
        },
      ];
      return sampleDS.getSample(auth.authToken).then(function (data) {
        if (data.status === 200) {
          data.data.forEach(function (value) {
            vm.sampletemperture.push({
              id: value.id,
              name: value.name,
            });
          });
          vm.sampletemperture.id = 0;
        }
      });
    }

    function jsonPrint() {
      vm.filterbranch = [];
      if (vm.demographics.length !== 0) {
        vm.filterbranch = $filter("filter")(vm.demographics, function (e) {
          return e.demographic === -5;
        })
      }


      if (vm.flange === 3 || vm.flange === 6) {
        var laboratory = $filter("filter")(vm.listlaboratory, {
          select: true,
        });
        vm.listLaboratories = _.uniq(_.map(laboratory, "id"));
      } else {
        var laboratory = vm.listlaboratory;
        vm.listLaboratories = _.uniq(_.map(laboratory, "id"));
      }

      if (vm.flange === 8) {
        vm.datajason = {
          "rangeType": vm.filterRange === '0' ? '3' : vm.filterRange,
          'init': vm.rangeInitAppoinment,
          'end': vm.rangeEndAppoinment,
          "appointmenttype": vm.appointmenttype
        }
      } else if (vm.flange === 4) {
        vm.datajason = {
          init: moment(vm.rangeInit).format("YYYYMMDD"),
          end: moment(vm.rangeEnd).format("YYYYMMDD"),
          rejectSample: vm.typeSample.toString() === "true",
          branch: vm.listBranch.id,
        };
      } else {

        if (vm.flange === 7) {
          if (vm.search === 2) {
            if (vm.filterstatetest === 1) {
              vm.datajason = {
                "rangeType": 1,
                'init': vm.order,
                'end': vm.order,
                "orderType": null,
                "check": vm.filterstate,
                'testFilterType': vm.listTests.length > 0 ? 5 : 4,
                'tests': vm.numFilterAreaTest === 1 ? vm.listAreas : vm.listTests,
                'demographics': vm.demographics,
                "filterState": [3, 4, 5],
                "listType": 2,
                "printAddLabel": false,
                "laboratories": [],
                "apply": true,
                "samples": [{ "codeSample": parseInt(vm.sample) }],
                "remission": null,
                "laboratory": vm.laboratoryremisiones.id,
                "userId": vm.userid.id
              }
            } else {
              vm.datajason = {
                "rangeType": 1,
                'init': vm.order,
                'end': vm.order,
                "orderType": null,
                "check": vm.filterstate,
                'testFilterType': vm.listTests.length > 0 ? 5 : 4,
                'tests': vm.numFilterAreaTest === 1 ? vm.listAreas : vm.listTests,
                'demographics': vm.demographics,
                "listType": 2,
                "printAddLabel": false,
                "laboratories": [],
                "apply": true,
                "samples": [{ "codeSample": parseInt(vm.sample) }],
                "remission": null,
                "laboratory": vm.laboratoryremisiones.id,
                "userId": vm.userid.id
              }
            }

          } else {
            if (vm.filterstatetest === 1) {
              vm.datajason = {
                "rangeType": vm.filterRange === '0' ? '3' : vm.filterRange,
                'init': vm.rangeInit,
                'end': vm.rangeEnd,
                "orderType": null,
                "filterState": [3, 4, 5],
                "check": vm.filterstate,
                'testFilterType': vm.listTests.length > 0 ? 5 : 4,
                'tests': vm.numFilterAreaTest === 1 ? vm.listAreas : vm.listTests,
                'demographics': vm.demographics,
                "listType": 2,
                "printAddLabel": false,
                "laboratories": [],
                "apply": true,
                "samples": [],
                "remission": null,
                "laboratory": vm.laboratoryremisiones.id,
                "userId": vm.userid.id
              }
            } else {
              vm.datajason = {
                "rangeType": vm.filterRange === '0' ? '3' : vm.filterRange,
                'init': vm.rangeInit,
                'end': vm.rangeEnd,
                "orderType": null,
                "check": vm.filterstate,
                'testFilterType': vm.listTests.length > 0 ? 5 : 4,
                'tests': vm.numFilterAreaTest === 1 ? vm.listAreas : vm.listTests,
                'demographics': vm.demographics,
                "listType": 2,
                "printAddLabel": false,
                "laboratories": [],
                "apply": true,
                "samples": [],
                "remission": null,
                "laboratory": vm.laboratoryremisiones.id,
                "userId": vm.userid.id
              }
            }
          }
        } else if (vm.filterListed === 4 && vm.flange === 1) {
          vm.datajason = {
            'rangeType': vm.filterRange === '0' ? '3' : vm.filterRange,
            'init': vm.rangeInit,
            'end': vm.rangeEnd,
            'orderType': null,
            'demographics': vm.demographics,
            'testFilterType': vm.numFilterAreaTest,
            'tests': vm.numFilterAreaTest === 1 ? vm.listAreas : vm.listTests,
            "initAge": common.getDOB(vm.maximumage.toString(), 'YYYY'),
            "endAge": common.getDOB(vm.minimumage.toString(), 'YYYY'),
            "userId": vm.userid.id
          };

          if (vm.gender !== 42) {
            vm.datajason.filterType = [1, 2];
            vm.datajason.sex = vm.gender;
          } else {
            vm.datajason.filterType = [1];
          }
        } else {
          vm.datajason = {
            rangeType: vm.filterRange == '0' ? '3' : vm.filterRange,
            init: vm.rangeInit,
            end: vm.rangeEnd,
            orderType: null,
            check: vm.flange === 3 ? vm.filtersample : vm.filtersample1,
            testFilterType: vm.numFilterAreaTest,
            tests:
              vm.numFilterAreaTest === 1 || vm.flange === 2
                ? vm.listAreas
                : vm.listTests,
            demographics: vm.demographics,
            packageDescription: vm.isPackage,
            listType: vm.filterListed,
            printAddLabel: vm.printAddLabel,
            laboratories: vm.listLaboratories,
            apply: true,
            samples: vm.listSamples,
            printerId: vm.ipUser,
            remission: vm.flange === 3 ? vm.remission === true ? 1 : 0 : 0,
            laboratory: vm.flange === 1 ? vm.laboratoryid.id : vm.flange === 3 ? vm.laboratory ? vm.laboratory.id : null : null,
            userId: vm.userid.id
          };
        }

      }
      return vm.datajason;
    }

    function isJsonString(str) {
      try {
        JSON.parse(str);
      } catch (e) {
        return false;
      }
      return true;
    }

    //Metodo par convertir las horas a militar
    vm.formathours = formathours;
    function formathours(hour) {
      hour = hour + '';
      if (hour === '0') {
        var hourInicial = '00:00';
        return hourInicial;
      } else if (hour.length === 2) {
        return '00:' + hour;
      } else {
        hour = hour.substring(0, hour.length - 2) + ':' + hour.substring(hour.length - 2, hour.length)
        return hour;
      }
    }

    function generateDataReport(data) {
      vm.numTests = 0;
      vm.numOrders = data.length.toString();
      if (vm.numOrders > 0) {
        data.forEach(function (value, key) {
          data[key].createdDate = moment(value.createdDate).format(
            '' + vm.formatDate.toUpperCase() + ' ' + 'HH:mm:ss'
          );
          var longbirthday = new Date(
            moment(value.patient.birthday).format()
          ).getTime();
          data[key].patient.birthday = moment(longbirthday).format(
            vm.formatDate
          );
          data[key].patient.age = common.getAgeAsString(
            moment(longbirthday).format(vm.formatDate),
            vm.formatDate
          );
          if (data[key].appointment !== undefined) {
            if (data[key].appointment.date !== undefined) {
              data[key].appointment.date = moment(data[key].appointment.date, 'YYYYMMDD').format(vm.formatDate.toUpperCase());
              data[key].appointment.shift.init = vm.formathours(data[key].appointment.shift.init);
              data[key].appointment.shift.end = vm.formathours(data[key].appointment.shift.end);
            }

          }
          data[key].tests.forEach(function (value, key) {
            if (value.sample.takeDate !== undefined) {
              value.sample.takeDate = moment(value.sample.takeDate).format(vm.formattakeDate);
            }
            if (value.result.dateOrdered !== undefined) {
              value.result.dateOrderedvalue = moment(value.result.dateOrdered).format(vm.formattakeDate);
            }
            if (value.result.dateResult !== undefined) {
              value.result.dateResultvalue = moment(value.result.dateResult).format(vm.formattakeDate);
            }
            if (value.result.dateTake !== undefined) {
              value.result.dateTakevalue = moment(value.result.dateTake).format(vm.formattakeDate);
            }
            if (value.result.dateVerific !== undefined) {
              value.result.dateVerificvalue = moment(value.result.dateVerific).format(vm.formattakeDate);
            }
          });
          data[key].testconcat = _.uniq(
            _.map(data[key].tests, "area.abbreviation")
          ).toString();
          data[key].testabreviature = _.uniq(
            _.map(data[key].tests, "abbr")
          ).toString();
          if (data[key].allDemographics.length > 0) {
            data[key].allDemographics.forEach(function (value2) {
              data[key]["demo_" + value2.idDemographic + "_name"] =
                value2.demographic;
              data[key]["demo_" + value2.idDemographic + "_value"] =
                value2.encoded === false
                  ? value2.notCodifiedValue
                  : value2.codifiedName;
            });
          }
          if (data[key].patient.diagnostic !== undefined) {
            if (data[key].patient.diagnostic.length > 0) {
              data[key].patient.diagnostic.forEach(function (value1, key1) {
                if (value1.comment.trim() !== "") {
                  var comment = '';
                  if (vm.isJsonString(value1.comment)) {
                    comment = JSON.parse(value1.comment).content;
                  } else {
                    comment = value1.comment;
                  }
                  var firshC = comment.substring(0, 1);
                  var content = comment;
                  var pos = firshC == '"' ? 1 : 0;
                  data[key].patient.diagnostic[
                    key1
                  ].comment = content.substring(pos, content.length - pos);
                }
              });
            }
          }
          if (data[key].comments.length > 0) {
            data[key].comments.forEach(function (value2, key2) {
              if (value2.comment.trim() !== "") {
                var firshC = JSON.parse(JSON.stringify(value2.comment)).substring(0, 1);
                var pos = firshC == '"' ? 1 : 0;
                var content = JSON.parse(JSON.stringify(value2.comment));
                var contentComment = '';
                if (vm.isJsonString(value2.comment)) {
                  contentComment = JSON.parse(value2.comment).content;
                  data[key].comments[key2].comment = contentComment.substring(
                    1,
                    contentComment.length - 1
                  );
                } else {
                  contentComment = value2.comment;
                  data[key].comments[key2].comment = contentComment;
                }
              }
            });
          }
        });
        return data;
      } else {
        return [];
      }
    }

    function variablesReport() {
      var rangeInit = "" + vm.rangeInit + "";
      var rangeEnd = "" + vm.rangeEnd + "";
      var titleSample =
        vm.typeSample.toString() === "true"
          ? $filter("translate")("0070")
          : $filter("translate")("0160");

      var user = "";

      if (vm.userid.id > 0) {
        var findUser = _.find(vm.listusers, function (o) { return o.id === vm.userid.id; });
        if (findUser) {
          user = findUser.name;
        }
      } else {
        user = $filter("translate")("0353");
      }

      var variables = [
        {
          type: parseInt(vm.filterRange),
          abbreviation: localStorageService.get("Entidad"),
          rangeInit: rangeInit, //Orden inicial o Fecha Inicial
          rangeEnd: rangeEnd, //Orden final o Fecha final
          date: moment().format(vm.formatDate + " hh:mm:ss a."),
          Username: auth.userName,
          userdata: auth.name + " " + auth.lastName,
          Label: titleSample, //Muestras rechazadas
          filterbranch: vm.filterbranch.length > 0 ? true : false,
          itembranch: vm.filterbranch.length > 0 ? vm.filterbranch[0].demographicItems : [],
          laboratoryRemission: vm.laboratory !== null && vm.laboratory !== undefined ? vm.laboratory.name : '',
          userFilter: user,
          printTests: vm.printTests === true ? 1 : 0
        },
      ];
      return variables;
    }

    function validage() {
      if (vm.minimumage > vm.maximumage) {
        vm.maximumage = vm.minimumage + 1;
      }
    }

    function getGender() {
      var auth = localStorageService.get('Enterprise_NT.authorizationData');
      return listDS.getList(auth.authToken, 6).then(function (data) {
        vm.dataGender = [];
        if (data.data.length > 0) {
          data.data.forEach(function (value) {
            if (value.id !== 9) {
              vm.dataGender.push({
                'id': value.id,
                'name': ($filter('translate')('0000') === 'enUsa' ? value.enUsa : value.esCo)
              });
            }
          });
        }
        vm.gender = 42;

      }, function (error) {
        vm.modalError(error);
      });
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

        vm.progressPrint = false;
      } else {
        UIkit.modal("#modalReportError").show();
        vm.progressPrint = false;

      }
    }

    function flasheaTexto() {
      vm.ind = vm.ind + 1;
      if (vm.ind === vm.total) {
        vm.total = vm.total + 10;
      }
      vm.porcent = Math.round((vm.ind * 100) / vm.total);
    }

    function closemodalconfirmation() {
      UIkit.modal("#confirmation").hide();
      vm.progressPrint = false;
    }

    function generateFile() {
      if (vm.search !== 2) {
        if (!vm.isOpenReport) {
          return;
        }
      }
      vm.progressPrint = true;
      auth = localStorageService.get("Enterprise_NT.authorizationData");
      var json = vm.jsonPrint();

      switch (vm.flange) {
        case 1:
          return listedOrderDS.getListedOrder(auth.authToken, json).then(
            function (data) {
              if (data.status === 200) {
                switch (vm.filterListed) {
                  case 0:
                    vm.fileReport =
                      "/Report/pre-analitic/listed/listedorder/listednormal.mrt";
                    vm.orientation = "vertical";
                    break;
                  case 1:
                    vm.fileReport =
                      "/Report/pre-analitic/listed/listedorder/listedarea.mrt";
                    vm.orientation = "vertical";
                    break;
                  case 2:
                    vm.fileReport =
                      "/Report/pre-analitic/listed/listedorder/listedtest.mrt";
                    vm.orientation = "vertical";
                    break;
                  case 3:
                    vm.fileReport =
                      "/Report/pre-analitic/listed/listedorder/listednotgroup.mrt";
                    vm.orientation = "horizontal";
                    break;
                  case 4:
                    vm.fileReport =
                      "/Report/pre-analitic/listed/listedorder/listedbyagerange.mrt";
                    vm.orientation = "vertical";
                    break;
                  case 5:
                    vm.fileReport =
                      "/Report/pre-analitic/listed/listedorder/testabbreviation.mrt";
                    vm.orientation = "vertical";
                    break;
                }
              }
              vm.pathreport = vm.fileReport;
              vm.datareport = vm.generateDataReport(data.data);
              vm.variables = vm.variablesReport();
              vm.windowOpenReport();
            },
            function (error) {
              vm.progressPrint = false;
              logger.error(error);
            }
          );
          /* } */
          break;
        case 3:
          if (!vm.hl7) {
            return listedLaboratoryDS
              .getListedLaboratory(auth.authToken, json)
              .then(
                function (data) {
                  if (data.status === 200) {
                    switch (vm.filterListed) {
                      case 2:
                        vm.fileReport =
                          "/Report/pre-analitic/listed/listedlaboratory/listedlabtest.mrt";
                        vm.orientation = "horizontal";
                        break;
                      case 4:
                        vm.fileReport =
                          "/Report/pre-analitic/listed/listedlaboratory/listedlabcontainer.mrt";
                        vm.orientation = "horizontal";
                        break;
                      case 5:
                        vm.fileReport =
                          "/Report/pre-analitic/listed/listedlaboratory/listedlabsample.mrt";
                        vm.orientation = "horizontal";
                        break;
                    }
                  }
                  vm.datareport = vm.generateDataReport(data.data);
                  vm.variables = vm.variablesReport();
                  vm.pathreport = vm.fileReport;
                  vm.windowOpenReport();
                },
                function (error) {
                  vm.progressPrint = false;
                  logger.error(error);
                }
              );
            break;
          } else {
            return listedLaboratoryDS
              .getListedLaboratoryHl7(auth.authToken, json)
              .then(
                function (data) {
                  if (data.status === 200) {
                    Object.saveAs(
                      data.data,
                      "HL7.txt",
                      "text/plain;charset=utf-8"
                    );
                  }
                  vm.progressPrint = false;
                },
                function (error) {
                  vm.progressPrint = false;
                  logger.error(error);
                }
              );
            break;
          }
        case 4:
          if (vm.typeSample === "0") {
            return listedSampleDS
              .gettempetature(
                auth.authToken,
                moment(vm.rangeInit).format("YYYYMMDD"),
                moment(vm.rangeEnd).format("YYYYMMDD"),
                vm.sampletemperture.id,
                vm.listservice.id
              )
              .then(
                function (data) {
                  if (data.status === 200) {
                    vm.fileReport =
                      "/Report/pre-analitic/listed/listedsample/listedsampletemperature.mrt";
                    vm.orientation = "vertical";
                  }
                  vm.datareport = vm.generateDataReport(data.data);
                  vm.variables = vm.variablesReport();
                  vm.pathreport = vm.fileReport;
                  vm.windowOpenReport();
                },
                function (error) {
                  vm.progressPrint = false;
                  logger.error(error);
                }
              );
            break;
          } else {
            return listedSampleDS.getListedSample(auth.authToken, json).then(
              function (data) {
                if (data.status === 200) {
                  vm.fileReport =
                    "/Report/pre-analitic/listed/listedsample/listedrejectsample.mrt";
                  vm.orientation = "vertical";
                }
                vm.datareport = vm.generateDataReport(data.data);
                vm.variables = vm.variablesReport();
                vm.pathreport = vm.fileReport;
                vm.windowOpenReport();
              },
              function (error) {
                vm.progressPrint = false;
                logger.error(error);
              }
            );
          }
          break;
        case 5:
          return listedOrderDS.getlistPendingExams(auth.authToken, json).then(
            function (data) {
              if (data.status === 200) {
                vm.fileReport =
                  "/Report/pre-analitic/listed/listPendingExams/listPendingExams.mrt";
                vm.orientation = "vertical";
              }
              vm.pathreport = vm.fileReport;
              vm.datareport = vm.generateDataReport(data.data);
              vm.variables = vm.variablesReport();
              vm.windowOpenReport();
            },
            function (error) {
              vm.progressPrint = false;
              logger.error(error);
            }
          );
          break;
        case 6:
          return listedOrderDS.gettestcheckbybranch(auth.authToken, json).then(
            function (data) {
              if (data.status === 200) {
                vm.fileReport =
                  "/Report/pre-analitic/listed/listedsamplebybranch/listedsamplebybranch.mrt";
                vm.orientation = "vertical";
              }
              vm.pathreport = vm.fileReport;

              var datareport = _.filter(data.data, function (o) {
                o.tests1 = _.filter(o.tests, function (p) {
                  return p.testCheckByBranch.length !== 0;
                });
                return o.tests1.length === 0 ? false : true;
              });
              vm.datareport = vm.generateDataReport(datareport);
              vm.variables = vm.variablesReport();
              vm.windowOpenReport();
            },
            function (error) {
              vm.progressPrint = false;
              logger.error(error);
            }
          );
          break;
        case 7:
          var auth = localStorageService.get('Enterprise_NT.authorizationData');
          return listedOrderDS.getListedreferrals(auth.authToken, json).then(
            function (data) {
              if (data.status === 200) {
                vm.data = data.data;
                vm.data.forEach(function (value) {
                  value.tests.forEach(function (value1) {
                    value1.tentativeDeliveryDate = value1.tentativeDeliveryDate == null ? "" : moment(value1.tentativeDeliveryDate).format(vm.formatDate);
                  });

                });
                if (vm.filterstatetest === 1) {
                  vm.fileReport = "/Report/pre-analitic/listed/listedorder/listremissionresult.mrt";
                } else if (vm.filtereport === 0) {
                  vm.fileReport = "/Report/pre-analitic/listed/listedorder/listremission.mrt";
                } else {
                  vm.fileReport = "/Report/pre-analitic/listed/listedorder/remission.mrt";
                }
                vm.orientation = "horizontal";
              }
              vm.pathreport = vm.fileReport;
              vm.datareport = vm.generateDataReport(data.data);
              vm.variables = vm.variablesReport();
              vm.variables[0].Environment = vm.Environment;
              vm.variables[0].Frozen = vm.Frozen;
              vm.variables[0].Refrigerated = vm.Refrigerated;
              vm.variables[0].laboratories = vm.laboratoryremisiones.name;
              vm.windowOpenReport();
            },
            function (error) {
              vm.progressPrint = false;
              logger.error(error);
            }
          );
          break;
        case 8:
          var auth = localStorageService.get('Enterprise_NT.authorizationData');
          return listedOrderDS.getListappointment(auth.authToken, json).then(
            function (data) {

              vm.fileReport = "/Report/pre-analitic/listed/listedorder/appoinmentList.mrt";
              vm.orientation = "horizontal";
              vm.pathreport = vm.fileReport;
              vm.datareport = vm.generateDataReport(data.data);
              var rangeInit = "" + vm.rangeInitAppoinment + "";
              var rangeEnd = "" + vm.rangeEndAppoinment + "";
              var titleappointmenttype = vm.appointmenttype === "0" ? $filter("translate")("3289") : vm.appointmenttype === "1" ? $filter("translate")("3290") : $filter("translate")("3291");

              vm.variables =
                [{
                  type: parseInt(vm.filterRange),
                  abbreviation: localStorageService.get("Entidad"),
                  rangeInit: rangeInit, //Orden inicial o Fecha Inicial
                  rangeEnd: rangeEnd, //Orden final o Fecha final
                  date: moment().format(vm.formatDate + " hh:mm:ss a."),
                  Username: auth.userName,
                  userdata: auth.name + " " + auth.lastName,
                  Label: titleappointmenttype, //tipo de citas    
                  typeapoinment: vm.appointmenttype
                }]
              vm.windowOpenReport();
            },
            function (error) {
              vm.progressPrint = false;
              logger.error(error);
            }
          );
      }
    }

    function generateBarcodes() {
      if (!vm.isOpenReport) {
        return;
      }
      vm.progressPrint = true;
      if ($rootScope.serialprint === "") {
        vm.message = $filter("translate")("1067");
        UIkit.modal("#logNoData").show();
        vm.progressPrint = false;
      } else {
        return reportadicional.testServerPrint(vm.UrlNodeJs).then(
          function (data) {
            if (data.status === 200) {
              vm.printlabels();
            }
          },
          function (error) {
            vm.message = $filter("translate")("1085");
            UIkit.modal("#logNoData").show();
            vm.progressPrint = false;
          }
        );
      }
    }

    function printlabels() {
      vm.progressPrint = false;
      vm.areasSelected();
      vm.samplesSelected();
      vm.PopupError = false;
      vm.totalorder = 0;
      vm.porcent = 0;
      vm.totalCanStickers = 0;
      vm.canSticker = 0;

      vm.printbarcode = {
        order: {},
        rangeType: vm.filterRange,
        init: vm.rangeInit,
        end: vm.rangeEnd,
        samples: vm.samplesBarcode,
        printAddLabel:
          vm.printAddLabel !== undefined && vm.printAddLabel === true,
        serial: $rootScope.serialprint,
        printingType: 2,
        test: [],
        demographics: [],
      };

      auth = localStorageService.get("Enterprise_NT.authorizationData");
      return reportsDS
        .getOrderHeaderBarcode(auth.authToken, vm.printbarcode)
        .then(
          function (data) {
            if (data.status === 200) {
              //vm.progressPrint = false;
              vm.listOrderHead = data.data;
              vm.arrayCanStickersOrder = [];
              vm.previewStickersOrder = [];
              vm.ind = 1;
              var stickerAditionals = vm.printAddLabel
                ? vm.stickerAditionals
                : 0;
              data.data.forEach(function (value, key) {
                vm.canStickersOrder = 0;
                value.samples.forEach(function (valuesample) {
                  var canstickers = valuesample.canstiker;
                  vm.totalCanStickers += canstickers;
                  vm.canStickersOrder += canstickers;
                  vm.previewStickersOrder.push({
                    ordersample:
                      value.orderNumber +
                      vm.separatorSample +
                      valuesample.codesample,
                    cansticker: canstickers,
                    cansamples: value.samples.length,
                  });
                });
                vm.arrayCanStickersOrder.push({
                  order: value.orderNumber,
                  cansticker: vm.canStickersOrder + stickerAditionals,
                });
              });
              vm.printbarcode.ordersprint = [{}];

              vm.totalCanStickers +=
                vm.listOrderHead.length * stickerAditionals;
              vm.progressPrint = false;
              if (data.data.length > 0) {
                UIkit.modal("#modallistorders", {
                  bgclose: false,
                  escclose: false,
                  modal: false,
                }).show();
              } else {
                vm.message = $filter("translate")("0574");
                setTimeout(function () {
                  UIkit.modal("#logNoData", {
                    modal: false,
                    keyboard: false,
                    bgclose: false,
                    center: true,
                  }).show();
                }, 1000);
              }
            }
          },
          function (error) {
            vm.progressPrint = false;
            if (error.data !== undefined && error.data !== null) {
              logger.error($filter("translate")("0787"));
            } else {
              vm.PopupError = true;
              vm.Error = error;
            }
          }
        );
    }

    function getProcessPrint() {
      UIkit.modal("#modalprogressprint", {
        bgclose: false,
        escclose: false,
        modal: false,
      }).show();
      vm.directImpression();
    }

    function directImpression() {
      vm.printbarcode.ordersprint[0] = vm.listOrderHead[vm.totalorder];
      var auth = localStorageService.get("Enterprise_NT.authorizationData");
      return reportsDS
        .printOrderBodyBarcode(auth.authToken, vm.printbarcode)
        .then(
          function (data) {
            if (data.status === 200) {
              if (data.data.length > 0) {
                vm.listOrderHead[vm.totalorder].printing = data.data[0].printing;
                var order = vm.listOrderHead[vm.totalorder].orderNumber;
                var can = _.filter(vm.arrayCanStickersOrder, function (v) {
                  return v.order == order;
                })[0].cansticker;
                var i = vm.ind;
                setTimeout(function () {
                  for (vm.ind; vm.ind < can + i; vm.ind++) {
                    vm.porcent = Math.round((vm.ind * 100) / vm.totalCanStickers);
                  }
                  vm.totalorder = vm.totalorder + 1;
                  if (vm.totalorder < vm.listOrderHead.length) {
                    vm.directImpression();
                  } else {
                    vm.ind--;
                    setTimeout(function () {
                      logger.success(
                        $filter("translate")("0211") +
                        ": " +
                        $filter("translate")("0259") +
                        ": " +
                        vm.rangeInit +
                        " - " +
                        vm.rangeEnd
                      );
                      UIkit.modal("#modalprogressprint", {
                        bgclose: false,
                        keyboard: false,
                      }).hide();
                      vm.listOrderPrint = [];
                    }, 1000);
                  }
                }, 30);
              }
            }
          },
          function (error) {
            vm.loading = false;
            if (error.data.code === 2) {
              UIkit.modal("#modalprogressprint").hide();
              vm.message = $filter("translate")("1074");
              setTimeout(function () {
                UIkit.modal("#logNoData", {
                  modal: false,
                  keyboard: false,
                  bgclose: false,
                  center: true,
                }).show();
              }, 1000);
            } else {
              vm.modalError(error);
              UIkit.modal("#modalprogressprint").hide();
              vm.message = $filter("translate")("1074");
              setTimeout(function () {
                UIkit.modal("#logNoData").show();
              }, 1000);
            }
          }
        );
    }

    function isAuthenticate() {
      var auth = localStorageService.get("Enterprise_NT.authorizationData");
      if (auth === null || auth.token) {
        $state.go("login");
      } else {
        vm.init();
      }
    }

    function init() {
      vm.clickFlange(1);
      vm.filterRange = "1";
      if ($filter("translate")("0000") === "esCo") {
        moment.locale("es");
      } else {
        moment.locale("en");
      }
      for (var i = 0; i <= vm.temperatureQuantityItems; i++) {
        vm.number.push({
          "id": i,
          "name": i
        })
      }
    }
    vm.isAuthenticate();
  }
})();
/* jshint ignore:end */
