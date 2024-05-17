(function() {
  'use strict';

  angular
    .module('app.logint')
    .run(appRun);

  appRun.$inject = ['routerHelper'];
  /* @ngInject */
  function appRun(routerHelper) {
    routerHelper.configureStates(getStates());
  }

  function getStates() {
    return [
      {
        state: 'logint',
        config: {
          url: '/LW:D',
          templateUrl: 'app/modules/account/logint/logint.html',
          controller: 'logintController',
          controllerAs: 'vm',
          authorize: true,
          title: 'logint'
        }
      }
    ];
  }
})();
