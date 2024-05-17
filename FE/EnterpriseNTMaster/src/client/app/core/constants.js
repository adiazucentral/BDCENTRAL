/* global toastr:false, moment:false */
(function () {
  'use strict';

  angular
    .module('app.core')
    .constant('toastr', toastr)
    .constant('moment', moment)
    .constant('productVersion', { master: '1.0.3' });
})();
