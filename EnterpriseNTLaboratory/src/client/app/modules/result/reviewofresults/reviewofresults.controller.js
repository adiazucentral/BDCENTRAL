/* jshint ignore:start */
(function () {
  'use strict';

  angular
    .module('app.reviewofresults')
    .controller('reviewofresultsController', reviewofresultsController);


  reviewofresultsController.$inject = ['LZString', 'common', 'localStorageService',
    '$filter', '$state', 'moment', '$rootScope', 'reviewofresultDS', 'ordertypeDS', '$translate', 'laboratoryDS', 'reportadicional'];

  function reviewofresultsController(LZString, common, localStorageService,
    $filter, $state, moment, $rootScope, reviewofresultDS, ordertypeDS, $translate, laboratoryDS, reportadicional) {

    var vm = this;
    vm.isAuthenticate = isAuthenticate;
    vm.init = init;
    vm.title = 'Listed';
    $rootScope.menu = true;
    $rootScope.NamePage = $filter('translate')('0023').toUpperCase();
    $rootScope.helpReference = '03.Result/reviewofresults.htm';
    $rootScope.blockView = true;
    vm.tabActive = 1;
    $rootScope.pageview = 3;
    vm.printReport = printReport;
    vm.getOrderType = getOrderType;
    vm.printPending = printPending;
    vm.printManagement = printManagement;
    vm.changeTypeReport = changeTypeReport;
    vm.windowOpenReport = windowOpenReport;
    vm.commentpending = '0';
    vm.formatDate = localStorageService.get('FormatoFecha');
    vm.formatDateAge = localStorageService.get('FormatoFecha').toUpperCase();
    vm.trazability = localStorageService.get('Trazabilidad');
    vm.takesample = localStorageService.get('TomaMuestra');
    vm.pruebareport = false;
    vm.openreport = true;
    vm.itemcomment = 2;
    vm.typereport = 2;
    vm.Resultfilter = 0;
    vm.demographics = [];
    vm.isOpenReport = false;
    vm.rangeInit = moment().format('YYYYMMDD');
    vm.rangeEnd = moment().format('YYYYMMDD');
    vm.filterRange = '0';
    vm.printPanic = printPanic;
    vm.printCritical = printCritical;
    vm.groupProfiles = true;
    vm.flasheaTexto = flasheaTexto;
    vm.abbrCustomer = localStorageService.get("Abreviatura");
    vm.nameCustomer = localStorageService.get("Entidad");

    vm.listcomment1 = [
      { name: $filter('translate')('0315'), id: 1 },
      { name: $filter('translate')('0316'), id: 2 }
    ];

    vm.listcomment = [
      { name: $filter('translate')('0315'), id: 1 },
      { name: $filter('translate')('0316'), id: 2 }
    ];

    vm.listtype = [
      { name: $filter('translate')('0320'), id: 1 },
      { name: $filter('translate')('0321'), id: 2 },
      { name: "Pivot", id: 3 }
    ];

    vm.listypepanic = [
      { name: $filter('translate')('1839'), id: 1 },
      { name: $filter('translate')('1840'), id: 2 }
    ];

    vm.listResultstate = [
      { id: 0, name: $filter('translate')('0353') },//Todos
      // { id: 1, name: $filter('translate')('0354') },
      { id: 2, name: $filter('translate')('0355') },//Repetición
      { id: 3, name: $filter('translate')('0323') },//Patológico
      { id: 4, name: $filter('translate')('0356') },//Pánico
      //  { id: 5, name: $filter('translate')('0357') }
    ];

    vm.typepanic = vm.listypepanic[0];
    vm.durations = [5, 10, 15, 30, 60];

    function getOrderType() {
      var auth = localStorageService.get('Enterprise_NT.authorizationData');
      return ordertypeDS.getOrderTypeActive(auth.authToken).then(function (data) {
        vm.getlaboratory();
        var all = [
          {
            'id': null,
            'name': $filter('translate')('0215')
          }
        ];

        if (data.data.length > 0) {
          vm.listordertype = all.concat(data.data);
          vm.itemordertype = { id: null };
        }
      });
    }

    vm.getlaboratory = getlaboratory;
    function getlaboratory() {
      vm.listlaboratory = [];
      var auth = localStorageService.get('Enterprise_NT.authorizationData');
      return laboratoryDS
        .getLaboratoryActive(auth.authToken)
        .then(function (data) {
          var all = [
            {
              'id': null,
              'name': $filter('translate')('0215')
            }
          ];
          if (data.data.length > 0) {
            vm.listlaboratory = all.concat(data.data);
            vm.laboratoryid = { id: null };
          }
        });
    }

    function printReport(type) {
      vm.type = type;
      var filterstate = [];
      vm.typeFilter = [];
      vm.outputBrowsers.forEach(function (value, key) {
        filterstate.push(value.id);
        vm.typeFilter.push(' ' + value.name);
      });


      var filter = {
        'rangeType': vm.filterRange === '0' ? 3 : vm.filterRange,
        'init': vm.rangeInit, 
        'end': vm.rangeEnd,
        'orderType': vm.itemordertype.id,
        'filterState': filterstate,
        'demographics': vm.demographics,
        'testFilterType': vm.numFilterAreaTest,
        'tests': vm.numFilterAreaTest === 1 ? vm.listAreas : vm.listTests,
        'attended': vm.typepanic ? vm.typepanic.id : null,
        'laboratory': vm.laboratoryid.id,
        'groupProfiles': vm.groupProfiles,
        'resultState': [0],
        'userId': 0
      };

      if (vm.Resultfilter !== -1 && vm.Resultfilter !== 0) {
        filter.resultState = [vm.Resultfilter];
      }

      vm.datareport = [];

      if (vm.tabActive === 1) {
        vm.printPending(filter);
      } else if (vm.tabActive === 2) {
        vm.printManagement(filter);
      } else if (vm.tabActive === 3) {
        vm.printPanic(filter);
      } else if (vm.tabActive === 4) {
        vm.printCritical(filter);
      }
    }

    function printCritical(filter) {
      var auth = localStorageService.get('Enterprise_NT.authorizationData');
      return reviewofresultDS.getCriticalValues(auth.authToken, filter).then(function (data) {
        if (data.data !== '') {

          data.data = _.orderBy(data.data, ['order'], ['asc']);
          data.data.forEach(function (value, key) {

            //Tiempo total
            var init = moment(value.entryDate);
            var end = null;
            var duration = 0;
            var hours = 0;
            var minutes = 0;
            var seconds = 0;

            if (value.reportedDoctor) {
              end = moment(value.reportedDoctor);
              duration = moment.duration(end.diff(init));
              hours = duration.get("hours");
              minutes = duration.get("minutes");
              seconds = duration.get("seconds");
              value.reportedDoctor = moment(value.reportedDoctor).format(vm.formatDateAge + ', h:mm:ss a');
            }
            value.hours = hours === 0 ? '00:' : hours <= 9 ? '0' + hours + ":" : hours + ":";
            value.minutes = minutes === 0 ? '00:' : minutes <= 9 ? '0' + minutes + ":" : minutes + ":";
            value.seconds = seconds === 0 ? '00' : seconds <= 9 ? '0' + seconds : seconds + "";
            value.totalMinutes = minutes;
            value.totalHours = hours;
            value.totalSeconds = seconds;
            value.entryDate = moment(value.entryDate).format(vm.formatDateAge + ', h:mm:ss a');
          });

          var services = _.filter(_.map(_.uniqBy(data.data, 'serviceId'), function (x) {
            return {
              'id': x.serviceId,
              'name': x.service
            }
          }), function (a) { return !_.isUndefined(a.name) });

          //Chart
          var chart = [];
          services.forEach(function (value) {

            var criticalValue = {};
            criticalValue.name = value.name;
            criticalValue.data = [];

            for (var i = 0; i <= vm.durations.length; i++) {

              var duration = vm.durations[i];
              var total = 0;
              var init = '';

              if (i === 0) {
                init = $filter('translate')('1847') + " ";
              } else if (i === vm.durations.length) {
                duration = vm.durations[i - 1] + 1;
                init = $filter('translate')('1846');
              } else {
                init = vm.durations[i - 1] + 1 + " " + $filter('translate')('0646');
              }

              if (duration <= 60) {
                if (i === 0) {
                  total = _.filter(data.data, function (o) { return o.serviceId === value.id && (o.totalHours === 0 && ((o.totalMinutes === 0 && o.totalSeconds > 0) || (o.totalMinutes > 0 && o.totalMinutes <= duration))) }).length;
                } else if (i === vm.durations.length) {
                  total = _.filter(data.data, function (o) { return o.serviceId === value.id && (o.totalHours === 0 && o.totalMinutes > 0 && o.totalMinutes >= duration) }).length;
                } else {
                  total = _.filter(data.data, function (o) { return o.serviceId === value.id && o.totalHours === 0 && (o.totalMinutes >= vm.durations[i - 1] + 1 && o.totalMinutes > 0 && o.totalMinutes <= duration) }).length;
                }
              } else {
                total = _.filter(data.data, function (o) { return o.serviceId === value.id && (o.totalHours >= 1) }).length;
              }

              criticalValue.data.push({
                'duration': init + " " + duration,
                'total': total
              });
            }

            chart.push(criticalValue);

          });

          //Totales Por Columna
          var criticalValues = JSON.parse(JSON.stringify(chart));

          criticalValues.forEach(function (value) {
            var total = _.sumBy(value.data, function (o) { return o.total });
            value.data.push({
              'duration': "Total",
              'total': total
            });
          });

          var total = [];
          var percentage = [];

          for (var i = 0; i <= vm.durations.length + 1; i++) {
            var col = 0;
            criticalValues.forEach(function (value) {
              col += value.data[i].total;
            });
            total.push({
              'duration': criticalValues[0].data[i].duration,
              'total': col
            });
            percentage.push({
              'duration': criticalValues[0].data[i].duration,
              'total': Math.round((col / _.filter(data.data, function (a) { return a.serviceId !== 0 }).length * 100) * 100) / 100
            });
          }

          criticalValues.push({
            'name': 'Total',
            'data': total
          });

          criticalValues.push({
            'name': $filter('translate')('0827'),
            'data': percentage
          });

          vm.datareport = {
            data: data.data,
            chart: chart,
            criticalValues: criticalValues
          }

          vm.variables = {
            'entity': vm.nameCustomer,
            'abbreviation': vm.abbrCustomer,
            'rangeInit': vm.rangeInit,
            'rangeEnd': vm.rangeEnd,
            'rangeType': vm.filterRange,
            'username': auth.userName,
            'date': moment().format(vm.formatDateAge + ', h:mm:ss a'),
            'critics': _.filter(data.data, function (o) { return o.totalHours === 0 && ((o.totalMinutes === 0 && o.totalSeconds > 0) || (o.totalMinutes > 0 && o.totalMinutes <= 30)) }).length
          }

          vm.pathreport = '/Report/post-analitic/reviewofresult/criticalvalues/criticalvalues.mrt';
          vm.dowload();
        }
        else {
          UIkit.modal('#modalReportError').show();
          vm.pruebareport = false;
        }
      });
    }

    function printPanic(filter) {
      var auth = localStorageService.get('Enterprise_NT.authorizationData');
      return reviewofresultDS.getPanicInterview(auth.authToken, filter).then(function (data) {
        if (data.data !== '') {
          var finalData = [];
          data.data.forEach(function (value, key) {
            if (vm.typepanic.id === 1) {
              value.date = value.date === null || value.date === undefined ? '' : moment(value.date).format(vm.formatDateAge + ', h:mm:ss a');
              value.dateValidated = value.dateValidated === null || value.dateValidated === undefined ? '' : moment(value.dateValidated).format(vm.formatDateAge + ', h:mm:ss a');
              if (value.answerId !== 0) {
                var final = _.filter(data.data, function (o) { return o.order === value.order && o.testId === value.testId && o.questionId === value.questionId });
                var answer = [];
                final.forEach(function (val) {
                  answer.push(val.answerClose);
                });
                value.answerClose = answer.join();
              }
              finalData.push(value);
            } else {
              finalData.push(value);
            }
          });
          vm.datareport = finalData;
          vm.variables = {
            'entity': vm.nameCustomer,
            'abbreviation': vm.abbrCustomer,
            'rangeInit': vm.rangeInit,
            'rangeEnd': vm.rangeEnd,
            'rangeType': vm.filterRange,
            'username': auth.userName,
            'typeFilter': vm.typeFilter.toString(),
            'date': moment().format(vm.formatDateAge + ', h:mm:ss a')
          };

          if (vm.typepanic.id === 1) {
            vm.pathreport = '/Report/post-analitic/reviewofresult/panicinterview/panicinterview.mrt';
          } else {
            vm.pathreport = '/Report/post-analitic/reviewofresult/unattendedpanic/unattendedpanic.mrt';
          }
          vm.dowload();
        }
        else {
          UIkit.modal('#modalReportError').show();
          vm.pruebareport = false;
        }
      });
    }

    function printPending(filter) {
      var auth = localStorageService.get('Enterprise_NT.authorizationData');
      return reviewofresultDS.getresultspending(auth.authToken, filter).then(function (data) {
        if (data.data !== '') {
          data.data.forEach(function (value, key) {
            value.createdDate = moment(value.createdDate).format(vm.formatDateAge);
            value.patient.sex = $filter('translate')('0000') === 'enUsa' ? value.patient.sex.enUsa : value.patient.sex.esCo;
            value.patient.birthday = common.getAgeAsString(moment(value.patient.birthday).format(vm.formatDateAge), vm.formatDateAge);
            if (value.dateOfDeath !== undefined) {
              value.dateOfDeath = moment(value.dateOfDeath).format(vm.formatDateAge);
            }
            if (value.allDemographics.length > 0) {
              value.allDemographics.forEach(function (value2) {
                value["demo_" + value2.idDemographic + "_name"] =
                  value2.demographic;
                value["demo_" + value2.idDemographic + "_value"] =
                  value2.encoded === false
                    ? value2.notCodifiedValue
                    : value2.codifiedName;
              });
            }
            value.comment = '';
            value.comments = $filter('filter')(value.comments, { print: true });
            if (value.comments.length > 0) {
              for (var i = 0; i < value.comments.length; i++) {
                var comment = value.comments[i].comment.replace(/'/g, '');
                comment = JSON.parse(comment).content.replace(/span/g, 'font');
                try {
                  value.comment = (value.comment === '' ? + '' : + '<br/>') + JSON.parse(comment);
                } catch (e) {
                  value.comment = (value.comment === '' ? + '' : + '<br/>') + '<br/>' + comment;
                }
              }
            }

            var filterstate = null;
            filterstate = _.filter(value.tests, function (o) { return o.result.state === 0 });
            value.testconcat0 = _.uniq(
              _.map(filterstate, "abbr")
            ).toString();

            filterstate = _.filter(value.tests, function (o) { return o.result.state === 1 });
            value.testconcat1 = _.uniq(
              _.map(filterstate, "abbr")
            ).toString();

            filterstate = _.filter(value.tests, function (o) { return o.result.state === 2 });
            value.testconcat2 = _.uniq(
              _.map(filterstate, "abbr")
            ).toString();

            filterstate = _.filter(value.tests, function (o) { return o.result.state === 3 });
            value.testconcat3 = _.uniq(
              _.map(filterstate, "abbr")
            ).toString();

            filterstate = _.filter(value.tests, function (o) { return o.result.state === 4 });
            value.testconcat4 = _.uniq(
              _.map(filterstate, "abbr")
            ).toString();




            for (var i = 0; i < value.tests.length; i++) {
              if (value.tests[i].hasTemplate === false) {
                value.tests[i].optionsTemplate = [{
                  idTest: value.tests[i].testId,
                  order: value.orderNumber,
                  option: null
                }]
              }
              value.tests[i].hasObservationResult = value.tests[i].hasComment === true ? true : value.tests[i].hasTemplate === true ? true : false;
            }
          });
          vm.datareport = data.data;
          vm.variables = {
            'rangeInit': vm.rangeInit,
            'rangeEnd': vm.rangeEnd,
            'rangeType': vm.filterRange,
            'username': auth.userName,
            'typeFilter': vm.typeFilter.toString(),
            'date': moment().format(vm.formatDateAge + ', h:mm:ss a')
          };

          if (vm.itemcomment === 3) {
            vm.pathreport = '/Report/post-analitic/reviewofresult/templateresult.mrt';
          } else if (vm.itemcomment === 2) {
            vm.pathreport = '/Report/post-analitic/reviewofresult/PendingResult.mrt';
          }
          else {
            vm.pathreport = '/Report/post-analitic/reviewofresult/PendingResultComment.mrt';
          }
          vm.dowload();
          //vm.windowOpenReport();
        }
        else {
          UIkit.modal('#modalReportError').show();
          vm.pruebareport = false;
        }
      });
    }

    function printManagement(filter) {
      var auth = localStorageService.get('Enterprise_NT.authorizationData');
      return reviewofresultDS.getresultManagement(auth.authToken, filter).then(function (data) {
        if (data.data !== '') {
          data.data.forEach(function (value, key) {
            if (value.allDemographics.length > 0) {
              value.allDemographics.forEach(function (value2) {
                value["demo_" + value2.idDemographic + "_name"] =
                  value2.demographic;
                value["demo_" + value2.idDemographic + "_codifiedCode"] =
                  value2.codifiedCode;
                value["demo_" + value2.idDemographic + "_value"] =
                  value2.encoded === false
                    ? value2.notCodifiedValue
                    : value2.codifiedName;
              });
            }

            value.patient.sexcode = value.patient.sex.code;
            value.patient.sex = $filter('translate')('0000') === 'enUsa' ? value.patient.sex.enUsa : value.patient.sex.esCo;
            value.createdDate = moment(value.createdDate).format(vm.formatDateAge + ' hh:mm ');
            value.patient.birthdaydate = moment(value.patient.birthday).format(vm.formatDateAge);
            value.patient.birthday = common.getAgeAsString(moment(value.patient.birthday).format(vm.formatDateAge), vm.formatDateAge);


            var filterstate = null;
            filterstate = _.filter(value.tests, function (o) { return o.result.state === 0 });
            value.testconcat0 = _.uniq(
              _.map(filterstate, "abbr")
            ).toString();

            filterstate = _.filter(value.tests, function (o) { return o.result.state === 1 });
            value.testconcat1 = _.uniq(
              _.map(filterstate, "abbr")
            ).toString();

            filterstate = _.filter(value.tests, function (o) { return o.result.state === 2 });
            value.testconcat2 = _.uniq(
              _.map(filterstate, "abbr")
            ).toString();

            filterstate = _.filter(value.tests, function (o) { return o.result.state === 3 });
            value.testconcat3 = _.uniq(
              _.map(filterstate, "abbr")
            ).toString();

            filterstate = _.filter(value.tests, function (o) { return o.result.state === 4 });
            value.testconcat4 = _.uniq(
              _.map(filterstate, "abbr")
            ).toString();

            filterstate = _.filter(value.tests, function (o) { return o.result.state === 5 });
            value.testconcat5 = _.uniq(
              _.map(filterstate, "abbr")
            ).toString();

            //pendiente.
            value.comment = '';
            value.comments = $filter('filter')(value.comments, { print: true });
            if (value.comments.length > 0) {
              for (var i = 0; i < value.comments.length; i++) {
                value.comments[i].comment = JSON.parse(value.comments[i].comment).content.replace(/span/g, 'font')
                try {
                  value.comment = (value.comment === '' ? + '' : + '<br/>') + '<br/>' + JSON.parse(value.comments[i].comment);
                } catch (e) {
                  value.comment = (value.comment === '' ? + '' : + '<br/>') + '<br/>' + value.comments[i].comment;
                }
              }
            }

            //pendiente
            for (var i = 0; i < value.tests.length; i++) {
              if (value.tests[i].hasTemplate === false) {
                value.tests[i].optionsTemplate = [{
                  idTest: value.tests[i].testId,
                  order: value.orderNumber
                }]
              }


              value.tests[i].validationDate = value.tests[i].result.dateValidation === undefined ? '' : moment(value.tests[i].result.dateValidation).format(vm.formatDateAge + ' hh:mm ')
              value.tests[i].verificationDate = value.tests[i].result.dateVerific === undefined ? '' : moment(value.tests[i].result.dateVerific).format(vm.formatDateAge + ' hh:mm ')
              value.tests[i].resultDate = value.tests[i].result.dateResult === undefined ? '' : moment(value.tests[i].result.dateResult).format(vm.formatDateAge + ' hh:mm ')
              value.tests[i].entryDate = value.tests[i].result.dateOrdered === undefined ? '' : moment(value.tests[i].result.dateOrdered).format(vm.formatDateAge + ' hh:mm ')
              value.tests[i].datePrint = value.tests[i].result.datePrint === undefined ? '' : moment(value.tests[i].result.datePrint).format(vm.formatDateAge + ' hh:mm ')
              value.tests[i].pathology = value.tests[i].result.pathology === 0 ? 0 : 1;
            }

          });

          vm.datareport = data.data;

          vm.variables = {
            'entity': vm.nameCustomer,
            'abbreviation': vm.abbrCustomer,
            'rangeInit': vm.rangeInit,
            'rangeEnd': vm.rangeEnd,
            'rangeType': vm.filterRange,
            'username': auth.userName,
            'typeFilter': vm.typeFilter.toString(),
            'date': moment().format(vm.formatDateAge + ', h:mm:ss a')
          };
          if (vm.typereport === 1) {
            vm.pathreport = '/Report/post-analitic/reviewofresult/ResumeManagement.mrt';
          } else if (vm.typereport === 3) {
            vm.pathreport = '/Report/post-analitic/reviewofresult/pivot.mrt';
          } else {
            if (vm.itemcomment === 2) {
              vm.pathreport = '/Report/post-analitic/reviewofresult/ManagementResult.mrt';
            }
            else {
              vm.pathreport = '/Report/post-analitic/reviewofresult/ManagementResultComment.mrt';
            }
          }
          vm.dowload();
        }
        else {
          UIkit.modal('#modalReportError').show();
          vm.pruebareport = false;
        }
      });
    }

    vm.dowload = dowload;

    function dowload() {
      if (vm.datareport.length > 0) {
        var labelsreport = JSON.stringify($translate.getTranslationTable());
        labelsreport = JSON.parse(labelsreport);

        var parameterReport = {};
        parameterReport.datareport = vm.datareport;
        parameterReport.variables = vm.variables;
        parameterReport.pathreport = vm.pathreport;
        parameterReport.labelsreport = labelsreport;
        parameterReport.type = vm.type;
        vm.ind = 1;
        vm.total = vm.datareport.length / 3;
        vm.porcent = 0;
        UIkit.modal('#modalprogress', { bgclose: false, escclose: false, modal: false }).show();
        var nIntervId;
        nIntervId = setInterval(vm.flasheaTexto, 200);

        reportadicional.reportRender(parameterReport).then(function (data) {
          UIkit.modal('#modalprogress', { bgclose: false, escclose: false, modal: false }).hide();
          vm.porcent = 0;
          clearInterval(nIntervId);
        });

        vm.pruebareport = false;
      } else {
        UIkit.modal("#modalReportError").show();
        vm.pruebareport = false;
      }
    }

    function flasheaTexto() {
      vm.ind = vm.ind + 1;
      if (vm.ind === vm.total) {
        vm.total = vm.total + 10;
      }
      vm.porcent = Math.round((vm.ind * 100) / vm.total);
    }

    function changeTypeReport() {
      if (vm.typereport === 3) {
        vm.Resultfilter = -1;
        vm.Resultfilter = 0;
      } else if (vm.typereport === 1) {
        vm.Resultfilter = -1;
      }
      else {
        vm.Resultfilter = 0;
      }
    }

    vm.changeReport = changeReport;
    function changeReport() {
      vm.modernBrowsers = [];
      vm.rangeInit = moment().format('YYYYMMDD');
      vm.rangeEnd = moment().format('YYYYMMDD');
      vm.filterRange = '0';
      
      if(vm.tabActive === 1){

        if (vm.takesample === 'True') {
          vm.modernBrowsers.push({ id: 1, name: $filter('translate')('0165'), ticked: true });
        }
  
        if (vm.trazability === '2' || vm.trazability === '3') {
          vm.modernBrowsers.push({ id: 2, name: $filter('translate')('0208'), ticked: true });
        }
  
        vm.modernBrowsers.push(
          { id: 3, name: $filter('translate')('0289'), ticked: true },
          { id: 4, name: $filter('translate')('0312'), ticked: true },
          { id: 5, name: $filter('translate')('0310'), ticked: true }
        );
      }
      else {
        if (vm.itemcomment === 3) {
          
          vm.modernBrowsers.push({ id: 3, name: $filter('translate')('0289'), ticked: true });
        } else {
          vm.modernBrowsers = [];

          // if (vm.takesample === 'True') {
          //   vm.modernBrowsers.push({ id: 1, name: $filter('translate')('0165'), ticked: true });
          // }

          // if (vm.trazability === '2' || vm.trazability === '3') {
          //   vm.modernBrowsers.push({ id: 2, name: $filter('translate')('0208'), ticked: true });
          // }

          vm.modernBrowsers.push(
            { id: 0, name: $filter('translate')('0767'), ticked: true },//Ordenado
            { id: 1, name: $filter('translate')('0355'), ticked: true },//Repetición
            { id: 2, name: $filter('translate')('0289'), ticked: true },//Resultado
            { id: 3, name: $filter('translate')('0768'), ticked: true },//Preliminar
            { id: 4, name: $filter('translate')('0312'), ticked: true },//Validación
            { id: 5, name: $filter('translate')('0310'), ticked: true }//Impresión
          );
        }
      }
    }


    function windowOpenReport() {
      var parameterReport = {};
      parameterReport.variables = vm.variables;
      parameterReport.pathreport = vm.pathreport;
      parameterReport.labelsreport = JSON.stringify($translate.getTranslationTable());
      var datareport = LZString.compressToUTF16(JSON.stringify(vm.datareport));
      localStorageService.set('parameterReport', parameterReport);
      localStorageService.set('dataReport', datareport);
      window.open('/viewreport/viewreport.html');
      vm.pruebareport = false;
    }


    function init() {

      vm.getOrderType();

      if (($filter('translate')('0000')) === 'esCo') {
        moment.locale('es');
      }
      else {
        moment.locale('en');
      }
      vm.modernBrowsers = [];

      if (vm.takesample === 'True') {
        vm.modernBrowsers.push({ id: 1, name: $filter('translate')('0165'), ticked: true });
      }

      if (vm.trazability === '2' || vm.trazability === '3') {
        vm.modernBrowsers.push({ id: 2, name: $filter('translate')('0208'), ticked: true });
      }

      vm.modernBrowsers.push(
        { id: 3, name: $filter('translate')('0289'), ticked: true },
        { id: 4, name: $filter('translate')('0312'), ticked: true },
        { id: 5, name: $filter('translate')('0310'), ticked: true }
      );


    }
    function isAuthenticate() {
      var auth = localStorageService.get('Enterprise_NT.authorizationData');
      if (auth === null || auth.token) {
        $state.go('login');
      }
      else {
        vm.init();
      }
    }
    vm.isAuthenticate();
  }
})();
/* jshint ignore:end */
