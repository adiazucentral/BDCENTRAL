/* jshint ignore:start */
(function () {
    "use strict";

    angular
        .module("app.account")
        .controller("logintController", logintController);

    logintController.$inject = [
        "userDS",
        "$state",
        "$translate",
        "localStorageService",
        "$filter",
        "$rootScope",
        "configurationDS",
        "sigaDS",
        "$location"
    ];

    /* @ngInject */
    function logintController(
        userDS,
        $state,
        $translate,
        localStorageService,
        $filter,
        $rootScope,
        configurationDS,
        sigaDS,
        $location
    ) {
        var vm = this;
        vm.title = "logint";
        $rootScope.menu = false;
        $rootScope.pageview = 1
        vm.modalError = modalError;
        vm.afterLogin = afterLogin;
        vm.init = init;
        vm.getkeyconfigure = getkeyconfigure;
        vm.getuserdetail = getuserdetail;
        vm.getPointall = getPointall;
        vm.getdatasiga = getdatasiga;
        vm.menssageInvalid = "";
        vm.init();
        //** Método para sacar el popup de error**//
        function modalError(error) {
            vm.loadingdata = false;
            vm.PopupError = true;
            vm.Error = error;
        }
        /*performs login operation 
         http://localhost:3000/LW:D + base 64   
        */
        function afterLogin(data) {
            vm.Alerts = [];
            vm.cantmessage = 0;
            vm.alertcant = 0;
            $rootScope.photo = "";
            vm.photo = "";
            vm.info = data;
            // validación e las llaves
            if (vm.info.user.licenses.product === false) {
                vm.menssageInvalid = $filter("translate")("1445");
            } else if (
                vm.info.user.licenses.LIS === false &&
                vm.info.user.licenses.PAT === false &&
                vm.info.user.licenses.MMB === false
            ) {
                vm.menssageInvalid = $filter("translate")("1446");
            } else if (vm.info.user.licenses.branch === false) {
                vm.menssageInvalid = $filter("translate")("1447");
            } else if (vm.info.user.licenses.user === false) {
                vm.menssageInvalid = $filter("translate")("1448");
            } else {
                localStorageService.set("LIS", vm.info.user.licenses.LIS);
                localStorageService.set("PAT", vm.info.user.licenses.PAT);
                localStorageService.set("MMB", vm.info.user.licenses.MMB);
                localStorageService.set("NUM", vm.info.user.licenses.NUM);
                localStorageService.set("FAC", vm.info.user.licenses.FAC);
                vm.Alerts = [];
                vm.cantmessage = 0;
                vm.alertcant = 0;
                $rootScope.photo = "";
                vm.photo = "";
                vm.user = vm.info.user;
                $state.go("dashboard");
            }
        }
        function init() {
            vm.loadingdata = true;
            localStorage.clear();
            vm.pointSigaSelected = null;
            vm.reasonBreakSelected = null;
            vm.stateTurnSiga = 0;
            $rootScope.dataturn = null;
            vm.orderHis = $location.$$url.replace("/LW:D", "");
            try {
                var orderHis = JSON.parse(atob(unescape(encodeURIComponent(vm.orderHis))));
                localStorageService.set('Enterprise_NT.authorizationData', {
                    authToken: orderHis.token,
                    userName: orderHis.user.userName,
                    id: orderHis.user.id,
                    photo: orderHis.user.photo,
                    confidential: orderHis.user.confidential,
                    branch: orderHis.user.branch,
                    name: orderHis.user.name,
                    lastName: orderHis.user.lastName,
                    administrator: orderHis.user.administrator,
                    maxDiscount: orderHis.user.maxDiscount,
                    orderType: orderHis.user.orderType,
                    creatingItems: orderHis.user.creatingItems
                });
                vm.Branch = orderHis.branches;
                localStorageService.set("Branchname", vm.Branch.name);
                if (orderHis.languaje === 'en') {
                    $translate.use('en');
                }
                vm.loadingdata = false;
                vm.user = orderHis.user;
                vm.user.location = vm.Branch.id;
                vm.getkeyconfigure(orderHis.token);
                setTimeout(function () {
                    vm.afterLogin(orderHis);
                }, 200);
            } catch (error) {
                vm.menssageInvalid = "Los parametros no son validos";
                vm.loadingdata = false;
            }
        }
        function getkeyconfigure(token) {
            return configurationDS.getConfiguration(token).then(
                function (data) {
                    data.data.forEach(function (value, key) {
                        localStorageService.set("stateTurnSig", 0);
                        if (value.key === 'IntegracionSIGA' && value.value === "True") {
                            vm.getdatasiga(token);
                        }
                        localStorageService.set(value.key, value.value);
                    });
                    vm.getuserdetail(token);
                },
                function (error) {
                    vm.modalError(error);
                }
            );
        }
        function getuserdetail(token) {
            return userDS.getUsersId(token, vm.user.id).then(
                function (data) {
                    if (data.status === 200) {
                        localStorageService.set("user", data.data);
                    }
                },
                function (error) {
                    vm.modalError(error);
                }
            );
        }
        function getdatasiga(token) {
            return sigaDS.getUserHistory(token, vm.user.userName).then(
                function (data) {
                    if (data.status === 200) {
                        if (data.data.logUserInPoint === undefined) {
                            localStorageService.set("dataviewsiga", false);
                            localStorageService.set("stateTurnSiga", 0);
                        } else {
                            localStorageService.set("dataviewsiga", true);
                            localStorageService.set("viewsiga", data.data);
                            if (localStorageService.get("viewsiga").turnInPoint !== undefined) {
                                var pointSiga = localStorageService.get("viewsiga").turnInPoint.point;
                                localStorageService.set("pointSiga", pointSiga);
                                localStorageService.set("stateTurnSiga", 1);
                                if (localStorageService.get("OrdenesSIGA") !== '') {
                                    if (parseInt(localStorageService.get("OrdenesSIGA")) === pointSiga.service.id) {
                                        localStorageService.set("moduleSiga", 1);
                                    }
                                }
                                if (localStorageService.get("VerificacionSIGA") !== '') {
                                    if (parseInt(localStorageService.get("VerificacionSIGA")) === pointSiga.service.id) {
                                        localStorageService.set("moduleSiga", 2);
                                    }
                                }
                            } else {
                                vm.getPointall(token);
                            }
                        }
                    }
                },
                function (error) {
                    vm.modalError(error);
                }
            );
        }
        function getPointall(token) {
            return sigaDS.getPointall(token).then(
                function (data) {
                    if (data.status === 200) {
                        var point = localStorageService.get("viewsiga").logUserInPoint.point.id;
                        var pointSiga = _.filter(data.data, function (o) { return o.id === point; })[0];
                        localStorageService.set("pointSiga", pointSiga);
                        localStorageService.set("stateTurnSiga", 1);
                        if (localStorageService.get("OrdenesSIGA") !== '') {
                            if (parseInt(localStorageService.get("OrdenesSIGA")) === pointSiga.service.id) {
                                localStorageService.set("moduleSiga", 1);
                            }
                        }
                        if (localStorageService.get("VerificacionSIGA") !== '') {
                            if (parseInt(localStorageService.get("VerificacionSIGA")) === pointSiga.service.id) {
                                localStorageService.set("moduleSiga", 2);
                            }
                        }
                    }
                },
                function (error) {
                    vm.modalError(error);
                }
            );
        }
    }
})();
/* jshint ignore:end */
