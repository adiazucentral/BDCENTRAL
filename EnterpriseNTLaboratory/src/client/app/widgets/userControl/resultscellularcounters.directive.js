
(function () {
    'use strict';

    angular
        .module('app.widgets')
        .directive('resultscellularcounters', resultscellularcounters);

    resultscellularcounters.$inject = ['$filter', 'commentsDS', 'localStorageService'];

    /* @ngInject */
    function resultscellularcounters($filter, commentsDS, localStorageService) {
        var directive = {
            restrict: 'EA',
            templateUrl: 'app/widgets/userControl/resultscellularcounters.html',
            scope: {
                openmodal: '=openmodal',
                order: '=order',
                idtest: '=idtest',
                testcode: '=testcode',
                testname: '=testname',
                patientinformation: '=patientinformation',
                photopatient: '=photopatient'
            },

            controller: ['$scope', function ($scope) {
                var vm = this;
                var auth = localStorageService.get('Enterprise_NT.authorizationData');
                vm.save = save;
                vm.viewcomment = false;
                vm.closemodal = closemodal;
                vm.getcellularcounters = getcellularcounters;



                $scope.$watch('openmodal', function () {
                    if ($scope.openmodal) {
                        vm.loadingdata = true;
                        vm.selectedMotive = null;
                        vm.reasonComment = '';
                        vm.order = $scope.order;
                        vm.testId = $scope.idtest;
                        vm.testcode = $scope.testcode;
                        vm.testname = $scope.testname;
                        vm.patient = $scope.patientinformation;
                        vm.photopatient = $scope.photopatient;
                        vm.getcellularcounters();
                        $scope.openmodal = false;
                    }
                });

                /**
                  Funcion  Obtiene la lista de motivos para la repetición del resultado de los exámenes
                  @author  jblanco
                  @return  Lista de motivos
                  @version 0.0.1
                */
                function getcellularcounters() {
                    var auth = localStorageService.get('Enterprise_NT.authorizationData');
                    return commentsDS.getResultscellularcounters(auth.authToken, vm.testId).then(function (data) {
                        if (data.status === 200) {
                            vm.resultscellularcounters = data.data;
                            if (vm.resultscellularcounters.length === 0) {
                                UIkit.modal('#advertencia').show();
                                vm.loadingdata = false;

                            } else {
                                vm.loadingdata = false;
                                UIkit.modal('#rs-modal-cellularcounters').show();
                            }

                        } else {
                            UIkit.modal('#advertencia').show();
                            vm.loadingdata = false;
                        }
                    }, function (error) {
                        vm.modalError(error);
                    });
                }
                function save() {
                    UIkit.modal('#rs-modal-cellularcounters').hide();
                   /*  $scope.motive = vm.selectedMotive;
                    $scope.comment = vm.reasonComment;
                    setTimeout(function () {
                        $scope.functionexecute();
                    }, 1000); */
                }
                function closemodal() {
                    UIkit.modal('#rs-modal-cellularcounters').hide();
                }
            }],
            controllerAs: 'resultscellularcounters'
        };
        return directive;
    }
})();
/* jshint ignore:end */


