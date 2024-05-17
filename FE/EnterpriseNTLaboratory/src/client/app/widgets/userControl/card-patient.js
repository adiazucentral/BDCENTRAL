
(function () {
    'use strict';

    angular
        .module('app.widgets')
        .directive('cardpatient', cardpatient);

    cardpatient.$inject = ['localStorageService'];

    /* @ngInject */
    function cardpatient(localStorageService) {
        var directive = {
            restrict: 'EA',
            templateUrl: 'app/widgets/userControl/card-patient.html',
            scope: {
                patientname: '=?patientname',
                patientdocument: '=?patientdocument',
                patientage: '=?patientage',
                patientgender: '=?patientgender',
                patientid: '=?patientid',
                photopatient: '=?photopatient'
            },

            controller: ['$scope', function ($scope) {
                var vm = this;
                var auth = localStorageService.get('Enterprise_NT.authorizationData');
                vm.formatDate = localStorageService.get('FormatoFecha');


                $scope.$watch('patientname', function () {
                    if ($scope.patientname) {
                        vm.patientname = $scope.patientname;
                        vm.patientdocument = $scope.patientdocument.replace('undefined', '');
                        vm.patientage = $scope.patientage;
                        vm.patientgender = $scope.patientgender;
                        vm.patientid = $scope.patientid;
                        vm.photopatient = $scope.photopatient;
                    }
                });
            }],
            controllerAs: 'cardpatient'
        };
        return directive;
    }
})();
/* jshint ignore:end */

