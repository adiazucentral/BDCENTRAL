  (function () {
    'use strict';

    angular
    .module('app.exception')
    .controller('ExceptionController', ExceptionController);

    ExceptionController.$inject = ['exceptionDS', 'configurationDS', '$http', 'localStorageService', 'logger',
    'authService', '$filter', '$state', 'moment','$window'];
    function ExceptionController(exceptionDS, configurationDS, $http, localStorageService, logger, authService,
      $filter, $state, moment,$window) {
        var vm = this;
        vm.init = init;
        vm.title = 'Exception';
        vm.sortReverse = true;
        vm.sortType = 'date';
        vm.selected = -1;
        vm.isAuthenticate = isAuthenticate;
        vm.get = get;
        vm.detail = detail;
        var auth;
        vm.seach = seach;
        vm.modalError = modalError;
        vm.File = File;
        vm.print = false;
        vm.getConfigurationFormatDate = getConfigurationFormatDate;
        vm.date = {
            startDate: moment().format(vm.formatDate),
            endDate: moment()
        }; 
        vm.date2 = {
            startDate: moment().format(vm.formatDate),
            endDate: moment()
        }; 
        vm.inicial = moment().subtract(29, 'days'), 
                     moment();
        vm.final = moment().format(vm.formatDate);
        vm.opts = {
            locale: {
                applyClass: 'btn-green',
                applyLabel: $filter('translate')('0031'),
                cancelLabel: $filter('translate')('0008'),
                daysOfWeek: [$filter('translate')('0145'),
                             $filter('translate')('0146'),
                             $filter('translate')('0147'),
                             $filter('translate')('0148'),
                             $filter('translate')('0149'),
                             $filter('translate')('0150'),
                             $filter('translate')('0151')],
                firstDay: 1,
                monthNames: [$filter('translate')('0152'),
                             $filter('translate')('0153'),
                             $filter('translate')('0154'),
                             $filter('translate')('0155'),
                             $filter('translate')('0156'),
                             $filter('translate')('0157'),
                             $filter('translate')('0158'),
                             $filter('translate')('0159'),
                             $filter('translate')('0160'),
                             $filter('translate')('0161'),
                             $filter('translate')('0162'),
                             $filter('translate')('0163')
                ]
            },
            eventHandlers: {
                'apply.daterangepicker': function (ev, picker) { vm.seach(); }
            }
        };
        //** Metodo configuración formato**//
        function getConfigurationFormatDate() {
            var auth = localStorageService.get('Enterprise_NT.authorizationData');
            return configurationDS.getConfigurationKey(auth.authToken, 'FormatoFecha').then(function (data) {
                if (data.status === 200) {
                    vm.formatDate = data.data.value.toUpperCase();
                }
            }, function (error) {
                if (vm.errorservice === 0) {
                    vm.modalError(error);
                    vm.errorservice = vm.errorservice + 1;
                }
            });
        }
        //** Metodo que valida la autenticación**//
        function isAuthenticate() {
            var auth = localStorageService.get('Enterprise_NT.authorizationData');
            if (auth.token) {
                $state.go('login');
            }
        }

        function detail(data, index) {
            vm.selected = index;
            vm.error = data;
            vm.print = false;
        }
        //Método que cambia el formato de las fechas
        function formatDates(fecha) {
            fecha.forEach(function (error, index) {
                error.date = moment(error.date).format(vm.formatDate + ' h:mm:ss a');
                if (error.code === 0) { error.code = $filter('translate')('0361'); }
                if (error.code === 1) { error.code = $filter('translate')('0362'); }
                if (error.code === 2) { error.code = $filter('translate')('0363'); }

            });
            return fecha;
        }
        //Método para sacar el popup de error//
        function modalError(error) {
            vm.Error = error;
            vm.ShowPopupError = true;
        }
        //** Método que obtiene una lista de errores del dia**//
        function get() {
            var currentdate = moment().format('YYYYMMDD');
            auth = localStorageService.get('Enterprise_NT.authorizationData');
            return exceptionDS.getexception(auth.authToken, currentdate, currentdate)
            .then(function (data) {
                if (data.data.length === 0)
                { vm.data = data.data; }
                else { vm.data = formatDates(data.data); }
                return vm.data;
            }, function (error) {
                vm.modalError(error);
            });
        }
        //** Método que obtiene una lista de errores**//
        function seach() {
            var initialdate = moment(vm.date.startDate).format('YYYYMMDD');
            var finaldate = moment(vm.date.endDate).format('YYYYMMDD');
            auth = localStorageService.get('Enterprise_NT.authorizationData');
            return exceptionDS.getexception(auth.authToken, initialdate, finaldate).then(function (data) {
                if (data.data.length === 0)
                { vm.data = data.data; }
                else
                { vm.data = formatDates(data.data); }

                return vm.data;
            }, function (error) {
                vm.modalError(error);
            });
        }
        function File() {

            vm.print = true;

            var options = new $window.Stimulsoft.Viewer.StiViewerOptions();
            options.height = '100%';
            options.appearance.scrollbarsMode = true;
            options.toolbar.showDesignButton = false;
            options.toolbar.showSendEmailButton = true;
            options.toolbar.printDestination = $window.Stimulsoft.Viewer.StiPrintDestination.Direct;
            options.appearance.htmlRenderMode = $window.Stimulsoft.Report.Export.StiHtmlExportMode.Table;
            vm.viewer = new $window.Stimulsoft.Viewer.StiViewer(options, 'StiViewer', false);


            vm.viewer.onEmailReport = function (e) {
                e.settings.from = '******@gmail.com';
                e.settings.host = 'smtp.gmail.com';
                e.settings.login = '******';
                e.settings.password = '******';
                //vm.viewer = new Stimulsoft.Viewer.StiViewer(options, 'StiViewer', false);
            };
            vm.viewer.renderHtml('viewerContent');

            vm.pathreport = '';
            var Labels = [{
                '0100': $filter('translate')('0100'),
                '0035': $filter('translate')('0035'),
                '0101': $filter('translate')('0101'),
                '0102': $filter('translate')('0102'),
                '0103': $filter('translate')('0103'),
                '0104': $filter('translate')('0104'),
                '0105': $filter('translate')('0105'),
                '0001': $filter('translate')('0001'),
                '0074': $filter('translate')('0074'),
                '0272': $filter('translate')('0272'),
                '0273': $filter('translate')('0273'),
                '0344': $filter('translate')('0344')
            }];
            vm.listreport = vm.error;
            vm.labelsreport = Labels;

            vm.viewer.showProcessIndicator();
            // Timeout need for immediate display loading report indicator
            var report = new $window.Stimulsoft.Report.StiReport();

            report.loadFile('/report/utilities/exception/exception.mrt');


            // Load reports from JSON object
            var jsonData = { 'data': [vm.listreport], 'Labels': [vm.labelsreport] };

            var dataSet = new $window.Stimulsoft.System.Data.DataSet();
            dataSet.readJson(jsonData);

            // Remove all connections from the report template
            report.dictionary.databases.clear();
            // Register DataSet object
            report.regData('Demo', 'Demo', dataSet);
            // Render report with registered data
            report.render();
            vm.viewer.report = report;
        }
        //** Método que carga los metodos que inicializa la pagina*// 
        function init() {
            vm.isAuthenticate();
            vm.getConfigurationFormatDate();
            vm.get();
        }

        vm.init();
    }

})();