(function() {
    'use strict';
    angular
        .module('app.core')
        .factory('areaPathologyDS', areaPathologyDS);
    areaPathologyDS.$inject = ['$http', '$q', 'exception', 'logger', 'settings'];

    /* @ngInject */
    //** Método que define los metodos a usar*/
    function areaPathologyDS($http, $q, exception, logger, settings) {
        var service = {
            getByStatus: getByStatus,
        };
        return service;
        //** Obtiene los organos activos*/
        function getByStatus(token) {
            return $http({
                    hideOverlay: true,
                    method: 'GET',
                    headers: { 'Authorization': token },
                    url: settings.serviceUrl + '/pathology/areas/filter/state/1'
                })
                .then(success);

            function success(response) {
                return response;
            }
        }
    }
})();