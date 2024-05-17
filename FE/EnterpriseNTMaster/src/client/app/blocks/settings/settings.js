/* global toastr:false, moment:false */
(function () {
    "use strict";
    angular
        .module("blocks.settings")
        .constant("siteSettings", {
            useTestData: true,
            websiteRootUrl: "http://localhost:3000",
            environment: "dev",
            // apiBaseUrl: 'http://localhost:19428',
            apiBaseUrl: "https://stage-a.careerscore.com",
        })
        .factory("settings", function () {
            return {
                serviceUrl: 'http://localhost:8080/Enterprise_NT/api',
                serviceUrlSocketIO: "http://localhost:5001",
                serviceUrlApi: 'http://192.168.1.22:5200/api'
            };
        });
})();
