(function() {
  'use strict';
  angular
      .module('app.core')
      .factory('chartpathologyDS', chartpathologyDS);
  chartpathologyDS.$inject = ['$http', '$q', 'exception', 'logger', 'settings'];

  /* @ngInject */
  //** Método que define los metodos a usar*/
  function chartpathologyDS($http, $q, exception, logger, settings) {
      var service = {
          getTissueProcess: getTissueProcess,
      };
      return service;
      //** Obtiene los organos activos*/
      function getTissueProcess(token) {
          return $http({
                  hideOverlay: true,
                  method: 'GET',
                  headers: { 'Authorization': token },
                  url: settings.serviceUrl + '/pathology/chart/tissueprocessor'
              })
              .then(success);

          function success(response) {
              return response;
          }
      }
  }
})();
