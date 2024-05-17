(function () {
    'use strict';

    angular
        .module('app.core')
        .factory('sigaDS', sigaDS);

    sigaDS.$inject = ['$http', 'settings'];
    /* @ngInject */

    function sigaDS($http, settings) {
        var service = {
            getUserHistory: getUserHistory,
            patientappointment:patientappointment,
            getPoint: getPoint,
            getPointall: getPointall,
            getorderforturn: getorderforturn,
            withoutturn: withoutturn,
            searchpatient: searchpatient,
            getturnsActive: getturnsActive,
            getturncall: getturncall,
            withoutturnappointment:withoutturnappointment,
            startWork: startWork,
            gettransferservicies: gettransferservicies,
            stopWork: stopWork,
            pauseWork: pauseWork,
            transfers: transfers,
            endturn: endturn,
            getReasonBreak: getReasonBreak,
            getReasonPostponement: getReasonPostponement,
            getReasonCancel: getReasonCancel,
            turnmanual: turnmanual,
            attendturn: attendturn,
            cancelturn: cancelturn,
            postponeturn: postponeturn,
            reserverturn: reserverturn,
            shiftorders: shiftorders,
            turnAutomatic: turnAutomatic,
            turnorder: turnorder
        };

        return service;


        function getUserHistory(token, userName) {
            return $http({
                hideOverlay: true,
                method: 'GET',
                headers: { 'Authorization': token },
                url: settings.serviceUrl + '/integration/siga/getUserHistory/userName/' + userName
            }).then(function (response) {
                return response;
            });
        }


        function getPoint(token, branch, service) {
            return $http({
                hideOverlay: true,
                method: 'GET',
                headers: { 'Authorization': token },
                url: settings.serviceUrl + '/integration/siga/pointOfCares/' + branch + '/' + service
            }).then(function (response) {
                return response;
            });
        }

        function getPointall(token) {
            return $http({
                hideOverlay: true,
                method: 'GET',
                headers: { 'Authorization': token },
                url: settings.serviceUrl + '/integration/siga/getAllPointsOfCare'
            }).then(function (response) {
                return response;
            });
        }
        function getorderforturn(token, turn) {
            return $http({
                hideOverlay: true,
                method: 'GET',
                headers: { 'Authorization': token },
                url: settings.serviceUrl + '/orders/ordersbyturn/' + turn
            }).then(function (response) {
                return response;
            });
        }
        function withoutturn(token, history, type) {
            return $http({
                hideOverlay: true,
                method: 'GET',
                headers: { 'Authorization': token },
                url: settings.serviceUrl + '/orders/withoutturn/' + history + '/' + type
            }).then(function (response) {
                return response;
            });
        }

        function withoutturnappointment(token, history, type) {
            return $http({
                hideOverlay: true,
                method: 'GET',
                headers: { 'Authorization': token },
                url: settings.serviceUrl + '/orders/appointment/withoutturn/' + history + '/' + type
            }).then(function (response) {
                return response;
            });
        }

        function patientappointment(token, history) {
            return $http({
                hideOverlay: true,
                method: 'GET',
                headers: { 'Authorization': token },
                url: settings.serviceUrl + '/orders/appointment/withoutturnbyhistory/' + history 
            }).then(function (response) {
                return response;
            });
        }
        function searchpatient(token, history) {
            return $http({
                hideOverlay: true,
                method: 'GET',
                headers: { 'Authorization': token },
                url: settings.serviceUrl + '/orders/withoutturnbyhistory/' + history
            }).then(function (response) {
                return response;
            });
        }
        function getturnsActive(token, branch, service, point) {
            return $http({
                hideOverlay: true,
                method: 'GET',
                headers: { 'Authorization': token },
                url: settings.serviceUrl + '/integration/siga/turnsActive/' + branch + '/' + service + '/' + point
            }).then(function (response) {
                return response;
            });
        }
        function getturncall(token, turn, service) {
            return $http({
                hideOverlay: true,
                method: 'GET',
                headers: { 'Authorization': token },
                url: settings.serviceUrl + '/integration/siga/turncall/' + turn + '/' + service
            }).then(function (response) {
                return response;
            });
        }
        function gettransferservicies(token, branch, service, turn) {
            return $http({
                hideOverlay: true,
                method: 'GET',
                headers: { 'Authorization': token },
                url: settings.serviceUrl + '/integration/siga/transferservicies/' + branch + '/' + service + '/' + turn
            }).then(function (response) {
                return response;
            });
        }
        function startWork(token, data) {
            return $http({
                hideOverlay: true,
                method: 'POST',
                headers: { 'Authorization': token },
                url: settings.serviceUrl + '/integration/siga/startWork',
                data: data
            }).then(function (response) {
                return response;
            });
        }
        function stopWork(token, data) {
            return $http({
                hideOverlay: true,
                method: 'POST',
                headers: { 'Authorization': token },
                url: settings.serviceUrl + '/integration/siga/stopWork',
                data: data
            }).then(function (response) {
                return response;
            });
        }

        function pauseWork(token, data) {
            return $http({
                hideOverlay: true,
                method: 'POST',
                headers: { 'Authorization': token },
                url: settings.serviceUrl + '/integration/siga/pauseWork',
                data: data
            }).then(function (response) {
                return response;
            });
        }
        function transfers(token, data) {
            return $http({
                hideOverlay: true,
                method: 'POST',
                headers: { 'Authorization': token },
                url: settings.serviceUrl + '/integration/siga/transfers',
                data: data
            }).then(function (response) {
                return response;
            });
        }
        function endturn(token, data) {
            return $http({
                hideOverlay: true,
                method: 'POST',
                headers: { 'Authorization': token },
                url: settings.serviceUrl + '/integration/siga/endturn',
                data: data
            }).then(function (response) {
                return response;
            });
        }

        function getReasonBreak(token) {
            return $http({
                hideOverlay: true,
                method: 'GET',
                headers: { 'Authorization': token },
                url: settings.serviceUrl + '/integration/siga/reasons/break'
            }).then(function (response) {
                return response;
            });
        }

        function getReasonPostponement(token) {
            return $http({
                hideOverlay: true,
                method: 'GET',
                headers: { 'Authorization': token },
                url: settings.serviceUrl + '/integration/siga/reasons/postponement'
            }).then(function (response) {
                return response;
            });
        }

        function getReasonCancel(token) {
            return $http({
                hideOverlay: true,
                method: 'GET',
                headers: { 'Authorization': token },
                url: settings.serviceUrl + '/integration/siga/reasons/cancel'
            }).then(function (response) {
                return response;
            });
        }
        function turnmanual(token, data) {
            return $http({
                hideOverlay: true,
                method: 'POST',
                headers: { 'Authorization': token },
                url: settings.serviceUrl + '/integration/siga/turnmanual',
                data: data
            }).then(function (response) {
                return response;
            });
        }

        function attendturn(token, data) {
            return $http({
                hideOverlay: true,
                method: 'POST',
                headers: { 'Authorization': token },
                url: settings.serviceUrl + '/integration/siga/attendturn',
                data: data
            }).then(function (response) {
                return response;
            });
        }

        function cancelturn(token, data) {
            return $http({
                hideOverlay: true,
                method: 'POST',
                headers: { 'Authorization': token },
                url: settings.serviceUrl + '/integration/siga/cancelturn',
                data: data
            }).then(function (response) {
                return response;
            });
        }

        function postponeturn(token, data) {
            return $http({
                hideOverlay: true,
                method: 'POST',
                headers: { 'Authorization': token },
                url: settings.serviceUrl + '/integration/siga/postponeturn',
                data: data
            }).then(function (response) {
                return response;
            });
        }

        function reserverturn(token, data) {
            return $http({
                hideOverlay: true,
                method: 'POST',
                headers: { 'Authorization': token },
                url: settings.serviceUrl + '/integration/siga/reserverturn',
                data: data
            }).then(function (response) {
                return response;
            });
        }
        function shiftorders(token, data) {
            return $http({
                hideOverlay: true,
                method: 'POST',
                headers: { 'Authorization': token },
                url: settings.serviceUrl + '/orders/shiftorders',
                data: data
            }).then(function (response) {
                return response;
            });
        }
        function turnAutomatic(token, branch, service, point) {
            return $http({
                hideOverlay: true,
                method: 'GET',
                headers: { 'Authorization': token },
                url: settings.serviceUrl + '/integration/siga/turnAutomatic/' + branch + '/' + service + '/' + point
            }).then(function (response) {
                return response;
            });
        }
        function turnorder(token, data) {
            return $http({
                hideOverlay: true,
                method: 'POST',
                headers: { 'Authorization': token },
                url: settings.serviceUrl + '/integration/siga/turnorder',
                data: data
            }).then(function (response) {
                return response;
            });
        }
    }
})();
