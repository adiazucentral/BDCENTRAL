(function () {
  'use strict';

  angular
    .module('app.core')
    .factory('listedOrderDS', listedOrderDS);

  listedOrderDS.$inject = ['$http', 'settings'];
  /* @ngInject */
  function listedOrderDS($http, settings) {
    var service = {
      getListedOrder: getListedOrder,
      getListedremmision: getListedremmision,
      savelaboratoryremmision: savelaboratoryremmision,
      getlistPendingExams: getlistPendingExams,
      gettestcheckbybranch: gettestcheckbybranch,
      getListedreferrals: getListedreferrals,
      getListappointment: getListappointment

    };

    return service;


    //** Método que  Verifica la muestra ingresada.*/
    function getListedOrder(token, json) {
      return $http({
        hideOverlay: true,
        method: 'PATCH',
        headers: {
          'Authorization': token
        },
        url: settings.serviceUrl + '/listorders',
        data: json
      }).then(function (response) {
        return response;
      });
    }
    //** Método que  Verifica la muestra ingresada.*/
    function getListedremmision(token, json) {
      return $http({
        hideOverlay: true,
        method: 'PATCH',
        headers: {
          'Authorization': token
        },
        url: settings.serviceUrl + '/listorders/laboratoryremmision',
        data: json
      }).then(function (response) {
        return response;
      });
    }

    //** Método que  Verifica la muestra ingresada.*/
    function savelaboratoryremmision(token, json) {
      return $http({
        hideOverlay: true,
        method: 'PATCH',
        headers: {
          'Authorization': token
        },
        url: settings.serviceUrl + '/listorders/savelaboratoryremmision',
        data: json
      }).then(function (response) {
        return response;
      });
    }

    //** Método que  Verifica la muestra ingresada.*/
    function getlistPendingExams(token, json) {
      return $http({
        hideOverlay: true,
        method: 'PATCH',
        headers: {
          'Authorization': token
        },
        url: settings.serviceUrl + '/listorders/filter/listPendingExams',
        data: json
      }).then(function (response) {
        return response;
      });
    }
    //** Método que  Verifica la muestra ingresada.*/
    function gettestcheckbybranch(token, json) {
      return $http({
        hideOverlay: true,
        method: 'PATCH',
        headers: {
          'Authorization': token
        },
        url: settings.serviceUrl + '/listorders/testcheckbybranch',
        data: json
      }).then(function (response) {
        return response;
      });
    }

    //** Método que  Verifica la muestra ingresada.*/
    function getListedreferrals(token, json) {
      return $http({
        hideOverlay: true,
        method: 'PATCH',
        headers: {
          'Authorization': token
        },
        url: settings.serviceUrl + '/listorders/remission',
        data: json
      }).then(function (response) {
        return response;
      });
    }

    //** Método que trae listados de citas*/
    function getListappointment(token, json) {
      return $http({
        hideOverlay: true,
        method: 'PATCH',
        headers: {
          'Authorization': token
        },
        url: settings.serviceUrl + '/listorders/appointment',
        data: json
      }).then(function (response) {
        return response;
      });
    }

  }
})();
