/********************************************************************************
  ENTERPRISENT - Todos los derechos reservados CLTech Ltda.
  PROPOSITO:    ...
  PARAMETROS:   openmodal @descripción
                listener  @descripción
                order     @descripción
                date      @descripción

  AUTOR:        @autor
  FECHA:        2018-06-21
  IMPLEMENTADA EN:
  1.02_EnterpriseNT_FE/EnterpriseNTLaboratory/src/client/app/modules/pre-analitic/orderEntry/orderentry.html

  MODIFICACIONES:

  1. aaaa-mm-dd. Autor
     Comentario...

********************************************************************************/

(function () {
  'use strict';
  angular
    .module('app.widgets')
    .directive('validateresult', validateresult);
  validateresult.$inject = ['resultsentryDS', 'localStorageService', '$filter', 'logger','commentDS'];
  /* @ngInject */
  function validateresult(resultsentryDS, localStorageService, $filter, logger,commentDS) {
    var directive = {
      templateUrl: 'app/widgets/userControl/validateresult.html',
      restrict: 'EA',
      scope: {
        openmodal: '=?openmodal',
        resulttest: '=?resulttest',
        order: '=?order'

      },
      controller: ['$scope', function ($scope) {
        var vm = this;
        vm.init = init;
        $scope.date = moment().toDate();
        vm.save = save;
        vm.searchByDate = searchByDate;
        vm.decimaltest = 0;
        vm.format = 'n0';
        vm.loading = true;
        vm.saveresult = saveresult;
        vm.getLiteralResult = getLiteralResult;
        vm.getLiteralData = getLiteralData;
        vm.listresult = [];
        vm.getCodedComment=getCodedComment;
        vm.ListComment = [];

        vm.customMenu = {
          menubar: false,
          language: $filter('translate')('0000') === 'esCo' ? 'es' : 'en',
          br_newline_selector : true,
          force_br_newlines : true,
          force_p_newlines : false,
          forced_root_block : false,
          convert_newlines_to_brs : true,
          plugins: [
            'link',
            'lists',
            'autolink',
            'anchor',
            'textcolor',
            'charmap'

          ],
          toolbar: [
            'bold italic underline  | fontselect fontsizeselect forecolor backcolor'
          ]
        };
     
        $scope.$watch('openmodal', function () {
          if ($scope.openmodal) {
            vm.order = $scope.order;
            vm.listresult = $scope.resulttest;
            vm.searchByDate();
          }
          $scope.openmodal = false;
        });
        
        vm.changeComment = function (selected) {
          vm.commentselect.commentResult = vm.commentselect.commentResult === undefined || vm.commentselect.commentResult === null ? '' : vm.commentselect.commentResult;
          if (selected !== undefined) {
            if (!vm.commentselect.insertcomment) {
              vm.commentselect.commentResult = vm.commentselect.commentResult + ' ' + selected.description.message;
            }
            else {
              vm.commentselect.commentResult = selected.description.message + ' ' + vm.commentselect.commentResult;
            }
           // $scope.$broadcast('angucomplete-alt:clearInput', 'comment_'+ vm.commentselect.testId);
          }
        };

      
        function getCodedComment() {
          var auth = localStorageService.get('Enterprise_NT.authorizationData');
          vm.ListComment = [];
          return commentDS.getComment(auth.authToken).then(function (data) {
            if (data.status === 200) {
            vm.ListComment = data.data;
            }else{
              logger.warning('Debes crear comentarios codificados para poder agregarlos');
            }
          },
            function (error) {
              vm.modalError();
            });
        }

        function getLiteralResult() {
          var auth = localStorageService.get('Enterprise_NT.authorizationData');
          return resultsentryDS.getLiterals(auth.authToken).then(function (data) {
            vm.getCodedComment();
            if (data.status === 200) {
              vm.literalResult = data.data;
            }
          }, function (error) {
            vm.modalError(error);
          });
        }

        function getLiteralData(p_testId) {
          //TODO: Filtrar la lista de literales por el id del examen
          var literalFilter = $filter('filter')(vm.literalResult, {
            testId: p_testId
          }, true);
          return literalFilter;
        }
        /**
         * Evento cuando se busca ordenes por fecha
         */
        function searchByDate(callback) {
          vm.loading = true;
          var listtest = [];
          vm.listresult.forEach(function (value, key) {
            if (value.result === undefined || value.result === null || value.result === ' ' || value.result === '') {
              value.result = null;
              if (value.resultType === 1) {
                value.result = 0;
                value.decimaltest = value.digits;
                value.format = 'n' + value.digits;
              }
              if (value.resultType === 2) {
                value.literalResultsLiteral = vm.getLiteralData(value.testId);
              }
              listtest.add(value);
            }
          });
          if (listtest.length !== 0) {
            vm.listresult = listtest;
            UIkit.modal('#validateresult').show();
          }
          vm.loading = false;
        }
        /**
         * Evento cuando se selecciona una orden
         * @param {*} orderS
         */
        function save() {
          var auth = localStorageService.get('Enterprise_NT.authorizationData');
          for (var i = 0; i < vm.listresult.length; i++) {
            if (vm.listresult[i].result !== undefined) {
              if (vm.listresult[i].result !== '' && vm.listresult[i].result !== null && vm.listresult[i].result !== 0) {
                if (vm.listresult[i].resultType === 1 && vm.listresult[i].result.indexOf(',') !== -1) {
                  vm.listresult[i].result = vm.listresult[i].result.replace(',', '.');
                }
                if (vm.listresult[i].resultType === 2 && vm.listresult[i].literalResultsLiteral.length !== 0) {
                  var resultliteral = vm.listresult[i].result;
                  vm.listresult[i].result = resultliteral.name;
                  vm.listresult[i].resultEnglish = resultliteral.nameEnglish === undefined || resultliteral.nameEnglish === null || resultliteral.nameEnglish === '' ? '' : resultliteral.nameEnglish;
                }
                vm.listresult[i].newState = 2;
                vm.listresult[i].idUser = auth.id;
                vm.listresult[i].resultChanged = true;
                vm.saveresult(vm.listresult[i]);
              } else if (vm.listresult[i].commentResult !== '') {
                vm.listresult[i].result = 'MEMO';
                vm.listresult[i].newState = 2;
                vm.listresult[i].idUser = auth.id;
                vm.listresult[i].resultChanged = true;                
                vm.saveresult(vm.listresult[i]);
              }
            } else if (vm.listresult[i].commentResult !== '') {
              vm.listresult[i].result = 'MEMO';
              vm.listresult[i].newState = 2;
              vm.listresult[i].idUser = auth.id;
              vm.listresult[i].resultChanged = true;                
              vm.saveresult(vm.listresult[i]);
            }
          }
          logger.success($filter('translate')('0149'));
          UIkit.modal('#validateresult').hide();
        }

        function saveresult(dataComment) {
          if (dataComment.commentResult !== '' && dataComment.commentResult !== undefined && dataComment.commentResult !== null) {
            var auth = localStorageService.get('Enterprise_NT.authorizationData');
            return resultsentryDS.updateTestComment(auth.authToken, dataComment).then(function (data) {
              var auth = localStorageService.get('Enterprise_NT.authorizationData');
              return resultsentryDS.updateTest(auth.authToken, dataComment).then(function (data) {
                if (data.status === 200) {
                }
              }, function (error) {
                vm.modalError(error);
              });
            }, function (error) {
              vm.modalError(error);
            });
          } else {
            var auth = localStorageService.get('Enterprise_NT.authorizationData');
            return resultsentryDS.updateTest(auth.authToken, dataComment).then(function (data) {
              if (data.status === 200) {
              }
            }, function (error) {
              vm.modalError(error);
            });
          }
        }
        /**
         * Funcion inicial de la directiva
         */
        function init() {
          vm.getLiteralResult();
        }
        vm.init();
      }],
      controllerAs: 'validateresult'
    };
    return directive;
  }
})();
