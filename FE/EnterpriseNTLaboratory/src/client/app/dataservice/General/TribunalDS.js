/* jshint ignore:start */
(function () {
  "use strict";
  angular.module("app.core").factory("TribunalDS", TribunalDS);
  TribunalDS.$inject = ["$http", "settings"];
  /* @ngInject */
  function TribunalDS($http, settings) {
    var service = {
      tokentribunal: tokentribunal,
      gettokentribunal: gettokentribunal,
      validatedrights: validatedrights,
      getdatatribunal: getdatatribunal,
      getListpacient: getListpacient,
      updateinconsistence: updateinconsistence,
      validatePatient: validatePatient
    };

    return service;
    function tokentribunal(parameters) {
      var view = { hideOverlay: true, }
      return $http
        .post("/api/datatribunal", parameters, view)
        .then(success);
      function success(response) {
        return response;
      }
    }

    function gettokentribunal(parameters) {
      var view = { hideOverlay: true, }
      return $http
        .post("/api/authenticationtribunal", parameters, view)
        .then(success);
      function success(response) {
        return response;
      }
    }

    function getdatatribunal(parameters) {
      var view = { hideOverlay: true, }
      return $http
        .post("/api/patienttribunal", parameters, view)
        .then(success);
      function success(response) {
        return response;
      }
    }

    function validatedrights(parameters) {
      var view = { hideOverlay: true, }
      return $http
        .post("/api/validatedrights", parameters, view)
        .then(success);
      function success(response) {
        return response;
      }
    }

    function getListpacient(token, initialDate, endDate) {
      return $http({
        hideOverlay: true,
        method: "GET",
        headers: { Authorization: token },
        url:
          settings.serviceUrl +
          "/patients/getBasicPatientInformation/initialDate/" +
          initialDate +
          "/endDate/" +
          endDate +
          "/patientStatus/2/filterType/0",
      }).then(function (response) {
        return response;
      });
    }

    function updateinconsistence(token, inconsistency) {
      return $http({
        hideOverlay: true,
        method: "PUT",
        headers: { Authorization: token },
        url: settings.serviceUrl + "/patients/updateBasicPatientInformation",
        data: inconsistency
      }).then(function (response) {
        return response;
      });
    }

    function validatePatient(data) {
      return $http({
        hideOverlay: true,
        method: 'POST',
        url: settings.serviceUrlApi + '/validatepatient',
        data: data
      })
        .then(success);

      function success(response) {
        return response;
      }
    }
  }
})();
