/********************************************************************************
  ENTERPRISENT - Todos los derechos reservados CLTech Ltda.
  PROPOSITO:    ...
  PARAMETROS:   openmodal @descripcion
                type      @descripcion
                vm        @descripcion

  AUTOR:        @autor
  FECHA:        2018-06-21
  IMPLEMENTADA EN:
  1.02_EnterpriseNT_FE/EnterpriseNTLaboratory/src/client/app/modules/analytical/resultsentry/resultsentry.html

  MODIFICACIONES:

  1. aaaa-mm-dd. Autor
     Comentario...

********************************************************************************/
/*
    type:
    1. Comentario de la orden
    2. Diagnóstico permanente
*/

(function () {
    'use strict';

    angular
        .module('app.widgets')
        .directive('resultsordercomment', resultsordercomment);

    resultsordercomment.$inject = ['$filter', 'localStorageService', 'resultsentryDS'];

    /* @ngInject */
    function resultsordercomment($filter, localStorageService, resultsentryDS) {
        var directive = {
            restrict: 'EA',
            templateUrl: 'app/widgets/userControl/results-ordercomment.html',
            scope: {
                openmodal: '=openmodal',
                type: '=type',
                vm: '=?vm'
            },

            controller: ['$scope', function ($scope) {
                var vm = this;
                var auth = localStorageService.get('Enterprise_NT.authorizationData');
                vm.load = load;
                vm.save = save;

                vm.customMenu = {
                    menubar: false,
                    br_newline_selector : true,
                    force_br_newlines : true,
                    force_p_newlines : false,
                    forced_root_block : false,
                    convert_newlines_to_brs : true,
                    language: $filter('translate')('0000') === 'esCo' ? 'es' : 'en',
                    plugins: [
                        'link',
                        'lists',
                        'autolink',
                        'anchor',
                        'textcolor',
                        'charmap'

                    ],
                    toolbar: [
                        'code | undo redo | bold italic underline superscript | fontselect fontsizeselect forecolor backcolor charmap | alignleft aligncenter alignright alignfull | numlist bullist outdent indent'
                    ]
                };

                $scope.$watch('type', function () {
                    vm.type = $scope.type;
                });

                $scope.$watch('openmodal', function () {
                    if ($scope.openmodal) {
                        vm.load();
                        $scope.openmodal = false;
                    }
                });

                $scope.$watch('vm', function () {
                    vm.vm = $scope.vm
                });

                $scope.$watch('temporalComment', function (oldValue, newValue) {
                    if (newValue === oldValue) {
                        return;
                    }
                    if (vm.type === 1 && vm.vm.orderDetail !== undefined) {
                        if ($scope.temporalComment !== undefined)
                            $scope.commentChanged = ($scope.temporalComment !== vm.vm.orderDetail.comment);
                    }
                });

                function load() {
                    $scope.temporalComment = (vm.type === 1 ? vm.vm.orderDetail.comment : vm.vm.orderDetail.diagnostic);
                    $scope.commentChanged = false;
                    UIkit.modal('#rs-modal-ordercomment').show();
                }

                function save() {
                    vm.vm.orderDetail.comment = $scope.temporalComment;
                    $scope.temporalComment = undefined;
                    //TODO: Actualizar el comentario...
                    //vm.vm.updateTest(vm.vm.selectedTest, vm.vm.testState.REPORTED);*/
                }

                function save() {
                    var auth = localStorageService.get('Enterprise_NT.authorizationData');

                    vm.vm.orderDetail.comment = $scope.temporalComment;
                    $scope.temporalComment = undefined;
                    vm.vm.orderDetail.commentDate = null;

                    return resultsentryDS.updateComment(auth.authToken, vm.vm.orderDetail).then(function (data) {
                        if (data.status === 200) {
                            vm.vm.orderDetail.commentDate = data.data.commentDate;
                        }
                    }, function (error) {
                        //vm.modalError(error);
                    });
                }

            }],
            controllerAs: 'vmd'
        };
        return directive;
    }
})();
/* jshint ignore:end */
