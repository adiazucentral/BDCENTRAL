(function() {
  'use strict';

  angular
    .module('app.remission')
    .run(appRun);

  appRun.$inject = ['routerHelper'];
  /* @ngInject */
  function appRun(routerHelper) {
    routerHelper.configureStates(getStates());
  }

  function getStates() {
    return [
      {
        state: 'remission',
        config: {
          url: '/remission',
          templateUrl: 'app/modules/samplesmanagement/remission/remission.html',
          controller: 'remissionController',
          controllerAs: 'vm',
          authorize: false,
          title: 'remission',
          idpage: 252
        }
      }
    ];
  }
})();
