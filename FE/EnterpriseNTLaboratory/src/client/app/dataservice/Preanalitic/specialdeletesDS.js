(function() {
  'use strict';

  angular
  .module('app.core')
  .factory('specialdeletesDS', specialdeletesDS);

  specialdeletesDS.$inject = ['$http','settings'];
  /* @ngInject */

  function specialdeletesDS($http, settings) {
    var service = {
      deletespecialrange: deletespecialrange,
      rangedeletespecial:rangedeletespecial,
      queryspecialdeletes: queryspecialdeletes
    };

    return service;

     function deletespecialrange(token, order) {
      return $http({
        hideOverlay: true,
                 method: 'PUT',
                 headers: {'Authorization': token},
                 url: settings.serviceUrl  +'/specialdeletes',
                data: order 
            })
          .then(success);
          
        function success(response) {
          return response;
        }     
    }
    function rangedeletespecial(token, order) {
      return $http({
        hideOverlay: true,
                 method: 'PUT',
                 headers: {'Authorization': token},
                 url: settings.serviceUrl  +'/specialdeletes/specialdeletesbyorder',
                data: order 
            })
          .then(success);
          
        function success(response) {
          return response;
        }     
    }

    function queryspecialdeletes(token, order) {
      return $http({
        hideOverlay: true,
                 method: 'PATCH',
                 headers: {'Authorization': token},
                 url: settings.serviceUrl  +'/specialdeletes/query',
                data: order 
            })
          .then(success);
          
        function success(response) {
          return response;
        }     
    }

  }
})();
