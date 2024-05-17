(function() {
    'use strict';
    angular
        .module('app.core')
        .factory('specimenDS', specimenDS);
    specimenDS.$inject = ['$http', '$q', 'exception', 'logger', 'settings'];

    /* @ngInject */
    //** Método que define los metodos a usar*/
    function specimenDS($http, $q, exception, logger, settings) {
        var service = {
            getspecimensorder: getspecimensorder,
            getByStatus: getByStatus
        };
        return service;

        //** Obtiene la lista de examenes con muestras de patologia de una orden*/
        function getspecimensorder(token, order) {
          return $http({
                  hideOverlay: true,
                  method: 'GET',
                  headers: { 'Authorization': token },
                  url: settings.serviceUrl + '/pathology/order/specimens/' + order
              })
              .then(success);
          function success(response) {
              return response;
          }
        }

        //** Obtiene los especimenes activos*/
        function getByStatus(token) {
            return $http({
                    hideOverlay: true,
                    method: 'GET',
                    headers: { 'Authorization': token },
                    url: settings.serviceUrl + '/pathology/specimen/filter/state/1'
                })
                .then(success);

            function success(response) {
                return response;
            }
        }
    }
})();
