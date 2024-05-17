
(function () {
  'use strict';

  angular
    .module('app.widgets')
    .directive('packages', packages);

  packages.$inject = ['localStorageService', 'common', 'testDS', 'orderDS' ,'logger', '$filter'];

  /* @ngInject */
  function packages(localStorageService, common, testDS, orderDS, logger, $filter) {
    var directive = {
      restrict: 'EA',
      templateUrl: 'app/widgets/userControl/packages.html',
      scope: {
        openmodal: '=openmodal',
        listener: '=?listener',
        listtests: '=listtests',
        orderdemos: '=orderdemos',
        patientdemos: '=patientdemos'
      },

      controller: ['$scope', function ($scope) {
        var vm = this;
        var auth = localStorageService.get('Enterprise_NT.authorizationData');
        vm.formatDate = localStorageService.get('FormatoFecha');
        vm.patientDemos = {};
        vm.orderDemos = {};
        vm.listpackages = {};
        vm.disabled =  true;
        vm.listtests = [];

        vm.closemodal = closemodal;
        vm.loadPatient = loadPatient;
        vm.loadOrder = loadOrder;
        vm.getSelectTest = getSelectTest;
        vm.loadTests = loadTests;
        vm.focusOutListTest = focusOutListTest;
        vm.selectall = selectall;
        vm.save = save;
        vm.selectchild = selectchild;
        vm.loadTracking = loadTracking;
        vm.sortData = sortData;

        vm.loadTests();

        $scope.$watch('openmodal', function () {
          if ($scope.openmodal) {
            vm.patientDemos = $scope.patientdemos;
            vm.orderDemos = $scope.orderdemos;
            vm.listpackages = {};
            vm.listtests = $scope.listtests;
            vm.loadPatient();
            vm.loadOrder();
            UIkit.modal('#package').show();
          }
          $scope.openmodal = false;
        });

        function save() {
          setTimeout(function () {
            $scope.listener(vm.listpackages);
            UIkit.modal('#package').hide();
          }, 100);
        }

        function selectchild(child, pack) {
          if(child.selected) {
            var find = _.find(vm.listtests, function(o) { return o.id === child.id; });
            if(find) {
              child.selected = false;
              logger.warning($filter('translate')('1936'));
            }
          }
          var all = _.filter(pack.childs, function(o) { return !o.selected; });
          if(all.length === 0) {
            var find = _.find(vm.listtests, function(o) { return o.id === pack.package.originalObject.id; });
            if(find) {
              pack.allchilds = false;
              pack.childs.forEach(function (value) {
                value.selected = false;
              });
              logger.warning($filter('translate')('1935'));
            } else {
              pack.allchilds = true;
            }
          } else {
            pack.allchilds = false;
          }
        }

        function selectall(selected) {
          if( selected ) {
            var find = _.find(vm.listtests, function(o) { return o.id === selected.package.originalObject.id; });
            if(find) {
              selected.allchilds = false;
              logger.warning($filter('translate')('1935'));
            } else {
              selected.childs.forEach(function (value) {
                value.selected = selected.allchilds;
              });
            }
          }
        }

        function focusOutListTest() {
          $scope.$broadcast('angucomplete-alt:clearInput', 'orderentry_tests');
        }

        /**
        * Carga en el autocomplete los examenes configurados
        */
        function loadTests() {
          testDS.getTestArea(auth.authToken, 2, 1, 0 ).then(function (response) {
            if (response.data.length > 0) {
              var tests = [];
              response.data.forEach(function (test, index) {
                tests.push({
                  'id': test.id,
                  'code': test.code,
                  'area': {
                    'id': 0,
                    'abbreviation': 'N/A',
                    'name': 'N/A'
                  },
                  'abbr': test.abbr.toUpperCase(),
                  'name': test.name.toUpperCase(),
                  'namesearch': _.deburr(test.name) + test.name,
                  'colorType': 'images/package.png',
                  'type': test.testType,
                  'confidential': test.confidential,
                  'gender': test.gender,
                  'minAge': test.minAge,
                  'maxAge': test.maxAge,
                  'unitAge': test.unitAge,
                  'testType': test.testType,
                  'showValue': test.code + '. ' + test.name.toUpperCase()
                });
              });
              tests = tests.sort(function (a, b) {
                if (a.code.trim().length > b.code.trim().length) {
                  return 1;
                } else if (a.code.trim().length < b.code.trim().length) {
                  return -1;
                } else {
                  return 0;
                }
              });
              vm.tests = tests;
            }
          }, function (error) {
            vm.Error = error;
            vm.ShowPopupError = true;
          });
        }

        vm.filterListTest = function (search, listtest) {
          var listtestfilter = [];
          if (search.length > 1 && search.substring(0, 1) === '-') {
            search = search.substring(1, search.length)
          }

          if (search.length === 1 && search.substring(0, 1) === '?') {
            listtestfilter = $filter('orderBy')(listtest, 'code');
          } else {
            listtestfilter = _.filter(listtest, function (color) {
              return color.code.toUpperCase().indexOf(search.toUpperCase()) !== -1 || color.namesearch.toUpperCase().indexOf(search.toUpperCase()) !== -1;
            });
          }
          listtestfilter = listtestfilter.splice(0, 100)
          return listtestfilter;
        };

        vm.inputChangedTest = function (text) {
          vm.searchtest = text;
        }


        function getSelectTest(selected) {
          if (vm.searchtest.length > 1 && vm.searchtest.substring(0, 1) === '-') {
            if(vm.listpackages !== null && vm.listpackages !== undefined) {
              vm.listpackages[selected.originalObject.id] = {};
            }
          } else {
            testDS.getProfileChilds(auth.authToken, selected.originalObject.id).then(function (response) {
              if ( response.status === 200 ) {
                vm.listpackages[selected.originalObject.id] = {
                  'package': selected,
                  'childs': response.data
                }
                vm.disabled = false;
              }
            }, function (error) {
              vm.Error = error;
              vm.ShowPopupError = true;
            });
          }
        }

        function sortData(data) {
          if(data.length > 0) {
            data.forEach(function(value) {
              value.date = moment(value.lastTransaction).format(vm.formatDate.toUpperCase() + ' hh:mm:ss ');
            });
          }
          return data;
        }

        function loadTracking() {
          if(vm.ordernumber !== null && vm.ordernumber !== undefined && vm.ordernumber !== "") {
            return orderDS.getPackageTracking(auth.authToken, vm.ordernumber).then(function (data) {
              if (data.status === 200) {
                vm.tracking = vm.sortData(data.data);
              }
            },
            function (error) {
              vm.loading = true;
              vm.modalError(error);
            });
          }
        }

        function loadOrder() {
          vm.tracking = [];
          vm.orderType = vm.orderDemos[-4].showValue;
          vm.service = vm.orderDemos[-6].showValue;
          vm.customer = vm.orderDemos[-1].showValue;
          vm.rate = vm.orderDemos[-3].showValue;
          vm.account = vm.orderDemos[17].showValue;
          vm.origin = vm.orderDemos[6].showValue;
          vm.ordernumber = vm.orderDemos[-998];
          vm.loadTracking();
        }

        function loadPatient() {
          vm.patientname = vm.patientDemos[-103] + (vm.patientDemos[-109] != "" ? " " + vm.patientDemos[-109] : "")
            + (vm.patientDemos[-101] != "" ? " " + vm.patientDemos[-101] : "") + (vm.patientDemos[-102] != "" ? " " + vm.patientDemos[-102] : "");
          vm.patientdocument = vm.patientDemos[-10].showValue;
          vm.patientage = common.getAgeTime(vm.patientDemos[-105], vm.formatDate.toUpperCase())
          vm.patientgender = vm.patientDemos[-104].name;
          vm.patientid = vm.patientDemos[-99] === "" || vm.patientDemos[-99] === undefined || vm.patientDemos[-99] === null ? undefined : vm.patientDemos[-99];
          vm.photopatient = '';
        }

        function closemodal() {
          UIkit.modal('#package').hide();
        }

      }],
      controllerAs: 'packages'
    };
    return directive;
  }
})();
/* jshint ignore:end */

