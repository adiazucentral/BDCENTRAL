(function() {
  'use strict';
  angular
      .module('app.core')
      .factory('pathologistDS', pathologistDS);
  pathologistDS.$inject = ['$http', '$q', 'exception', 'logger', 'settings'];

  /* @ngInject */
  //** Método que define los metodos a usar*/
  function pathologistDS($http, $q, exception, logger, settings) {
      var service = {
          getPathologistsFilters: getPathologistsFilters,
      };
      return service;
      //** Obtiene la lista de patologos*/
      function getPathologistsFilters(token, filter) {
        return $http({
          method: 'PATCH',
          headers: { 'Authorization': token },
          url: settings.serviceUrl + '/pathology/pathologist/filter/schedule',
          data: filter,
          hideOverlay: true
        })

          .then(function (response) {
            return response;
          });
      }
  }
})();
