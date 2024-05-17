(function() {
    'use strict';
  
    angular
      .module('app.comboinvoice')
      .run(appRun);
  
    appRun.$inject = ['routerHelper'];
    /* @ngInject */
    function appRun(routerHelper) {
      routerHelper.configureStates(getStates());
    }
  
    function getStates() {
      return [
        {
          state: 'comboinvoice',
          config: {
            url: '/comboinvoice',
            templateUrl: 'app/modules/billing/comboinvoice/comboinvoice.html',
            controller: 'comboinvoiceController',
            controllerAs: 'vm',
            authorize: false,
            title: 'comboinvoice',
            idpage: 511
          }
        }
      ];
    }
  })();
  