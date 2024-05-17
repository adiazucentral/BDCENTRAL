(function() {
  'use strict';

  angular
    .module('app.core')
    .factory('reviewofresultDS', reviewofresultDS);

  reviewofresultDS.$inject = ['$http', 'settings'];
  /* @ngInject */

  //** Método que define los metodos a usar*/
  function reviewofresultDS($http,settings) {
    var service = {
      getresultspending: getresultspending,
      getresultManagement: getresultManagement,
      getPanicInterview: getPanicInterview,
      getCriticalValues: getCriticalValues
    };

    return service;

    function getresultspending(token, json) { 
      return $http({
        hideOverlay: true,
          method: 'PATCH',
              headers: {'Authorization': token},
              url: settings.serviceUrl  +'/checkresults/pending',
              data: json
          }).then(function(response) {
              return response;
          });

    }

    function getresultManagement(token, json) {
      return $http({
        hideOverlay: true,
          method: 'PATCH', 
              headers: {'Authorization': token},
              url: settings.serviceUrl  +'/checkresults',
              data: json
          }).then(function(response) {
              return response;
          });

    }

    function getPanicInterview(token, json) {


      
      return $http({
        hideOverlay: true,
          method: 'PATCH',
              headers: {'Authorization': token},
              url: settings.serviceUrl  +'/checkresults/panicinterview',
              data: json
          }).then(function(response) {
              return response;
          });
    }

    function getCriticalValues(token, json) {
      return $http({
        hideOverlay: true,
          method: 'PATCH',
              headers: {'Authorization': token},
              url: settings.serviceUrl  +'/checkresults/criticalvalues',
              data: json
          }).then(function(response) {
              return response;
          });
    }




  }
})();

