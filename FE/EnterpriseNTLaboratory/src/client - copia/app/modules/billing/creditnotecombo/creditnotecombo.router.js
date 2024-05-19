(function() {
  'use strict';

  angular
    .module('app.creditnotecombo')
    .run(appRun);

  appRun.$inject = ['routerHelper'];
  /* @ngInject */
  function appRun(routerHelper) {
    routerHelper.configureStates(getStates());
  }

  function getStates() {
    return [
      {
        state: 'creditnotecombo',
        config: {
          url: '/creditnotecombo',
          templateUrl: 'app/modules/billing/creditnotecombo/creditnotecombo.html',
          controller: 'creditnotecomboController',
          controllerAs: 'vm',
          authorize: false,
          title: 'creditnotecombo',
          idpage: 513
        }
      }
    ];
  }
})();
