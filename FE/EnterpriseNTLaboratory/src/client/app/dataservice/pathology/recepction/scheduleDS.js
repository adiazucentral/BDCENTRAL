(function() {
  'use strict';
  angular
      .module('app.core')
      .factory('scheduleDS', scheduleDS);
  scheduleDS.$inject = ['$http', '$q', 'exception', 'logger', 'settings'];

  /* @ngInject */
  //** Método que define los metodos a usar*/
  function scheduleDS($http, $q, exception, logger, settings) {
      var service = {
          getEvents: getEvents,
      };
      return service;
      //** Obtiene los casetes activos*/
      function getEvents(token) {
          return $http({
                  hideOverlay: true,
                  method: 'GET',
                  headers: { 'Authorization': token },
                  url: settings.serviceUrl + '/pathology/event/filter/state/1'
              })
              .then(success);

          function success(response) {
              return response;
          }
      }
  }
})();
