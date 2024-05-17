(function () {
  'use strict';

  angular
    .module('app.core')
    .factory('remissionDS', remissionDS);

  remissionDS.$inject = ['$http', 'settings'];
  /* @ngInject */

  function remissionDS($http, settings) {
    var service = {
      getRemission: getRemission,
      saveRemission: saveRemission,
      consultRemission: consultRemission,
      remissionCentralOrders: remissionCentralOrders

    };
    return service;

    function getRemission(token, order) {
      return $http({
        hideOverlay: true,
        method: 'GET',
        headers: { 'Authorization': token },
        url: settings.serviceUrl + '/listorders/remissionCentralOrders/order/' + order
      }).then(success);

      function success(response) {
        return response;
      }
    }

    function remissionCentralOrders(token, json) {
      return $http({
        hideOverlay: true,
        method: 'PATCH',
        headers: { 'Authorization': token },
        url: settings.serviceUrl + '/listorders/remissionCentralOrders',
        data: json
      }).then(function (response) {
        return response;
      });
    }

    function consultRemission(token, data) {
      return $http({
        hideOverlay: true,
        method: 'PATCH',
        headers: { 'Authorization': token },
        url: settings.serviceUrl + '/listorders/listremissionCentralOrders',
        data: data
      }).then(function (response) {
        return response;
      });
    }

    function saveRemission(token, data) {
      return $http({
        hideOverlay: true,
        method: 'POST',
        headers: { 'Authorization': token },
        url: settings.serviceUrl + '/listorders/remissionCentralOrders',
        data: data
      }).then(function (response) {
        return response;
      });
    }
  }
})();
