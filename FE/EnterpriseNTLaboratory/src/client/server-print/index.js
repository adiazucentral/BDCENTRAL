/* jshint ignore:start */
// simple express server
var express = require('express');
var app = express();
var router = express.Router();
var request = require('request');
var bodyParser = require('body-parser');
var moment = require('moment');

var fs = require('fs');

// Stimulsoft Reports module
var Stimulsoft = require('stimulsoft-reports-js');
// Loading fonts
Stimulsoft.Base.StiFontCollection.addOpentypeFontFile("Roboto-Black.ttf");
Stimulsoft.Base.StiLicense.key = "6vJhGtLLLz2GNviWmUTrhSqnOItdDwjBylQzQcAOiHkLGVcN+bpBmun+R2BRIKs8z3U9Q3qMl9QZwpdoqVEoeTR5v9" +
	"9NEVUdMFq0E8GmiSgDVUpjdje4mRPlh9ChdF5D/dsLbKw4o0RukpfbtVI2RxkdSpv9D35WHQUGab9FX4+LJdMJQvlh" +
	"5KvpRTwawH95BNVJAMNn6wPznC+40WjzxgXipSOKnjMGArxsoAynOnZlNFJ4GGdnaz7Hwxqz091IWnszsoCxqIG+5e" +
	"x5Ztsj4y6KQ5Dfu1E5g8atl99q7fBRZDXZNG3CNnb/jXuRHB6mwS88AmejPFdPkkaNiMeUDliFoiKivbylMnTh7Gnl" +
	"QyV+BtOH0xQ/BwnAOmtkMTI0QcAujH9IRJ50ClaaHhJxZFVLaYZLWV/oAWrk2TIQGhVCl48g58hZ72XmMNUgMtBEhz" +
	"5J6gyjEGKwZQfL8GDqMc1JqftgW6ONC4BFwM+evU7QUfnF6Qlr+dYdBMhBwfFOyM71leMi9q2fA3VQHbVwXkPTFahY" +
	"KgBSOEI2nk1Y99FUGk0zkDjx6duWpy98jAxd5Nzu4VCitvXK14mGo88O1A==";



app.use(bodyParser.json({ limit: '100mb' }));
app.use(bodyParser.urlencoded({ limit: '100mb', extended: true }));
app.use(bodyParser.json());

app.use(function (req, res, next) {
	res.header("Access-Control-Allow-Origin", "*");
	res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
	next();
});

app.get('/testServerPrint', function (req, res) {
	res.json('OK')
});

app.post("/printReportOrders", (req, res, next) => {
	var send = false;
	var message = {};

	console.log(req.body);


	var dataOrder = JSON.parse(req.body.order);
	var dataVariables = JSON.parse(req.body.variables)

	console.log(dataOrder)


	dataOrder.createdDate = moment(dataOrder.createdDate).format(dataVariables.formatDate + ' hh:mm:ss a.')


	dataOrder.listfirm = [];
	for (var i = 0; i < dataOrder.resultTest.length; i++) {
		dataOrder.resultTest[i].resultDate = moment(dataOrder.resultTest[i].resultDate).format(dataVariables.formatDate + ' hh:mm:ss a.');
		dataOrder.resultTest[i].validationDate = moment(dataOrder.resultTest[i].validationDate).format(dataVariables.formatDate + ' hh:mm:ss a.');
		dataOrder.resultTest[i].entryDate = moment(dataOrder.resultTest[i].entryDate).format(dataVariables.formatDate + ' hh:mm:ss a.');
		dataOrder.resultTest[i].takenDate = moment(dataOrder.resultTest[i].takenDate).format(dataVariables.formatDate + ' hh:mm:ss a.');
		dataOrder.resultTest[i].verificationDate = moment(dataOrder.resultTest[i].verificationDate).format(dataVariables.formatDate + ' hh:mm:ss a.');
		dataOrder.resultTest[i].printDate = moment(dataOrder.resultTest[i].printDate).format(dataVariables.formatDate + ' hh:mm:ss a.');

		if (dataOrder.resultTest[i].hasAntibiogram) {
			dataOrder.resultTest[i].antibiogram = dataOrder.resultTest[i].microbialDetection.microorganisms;
		}

		if (dataOrder.resultTest[i].validationUserId !== undefined) {
			var findfirm = dataOrder.listfirm.find(order => (order.areaId === dataOrder.resultTest[i].areaId && order.validationUserId === dataOrder.resultTest[i].validationUserId));
			if (findfirm === undefined) {
				var firm = {
					"areaId": dataOrder.resultTest[i].areaId,
					"areaName": dataOrder.resultTest[i].areaName,
					"validationUserId": dataOrder.resultTest[i].validationUserId,
					"validationUserIdentification": dataOrder.resultTest[i].validationUserIdentification,
					"validationUserName": dataOrder.resultTest[i].validationUserName,
					"validationUserLastName": dataOrder.resultTest[i].validationUserLastName,
					"firm": dataOrder.resultTest[i].validationUserSignature
				}
				dataOrder.listfirm.push(firm)
			}
		}

	}

	var parameterReport = {
		"order": dataOrder,
		"Labels": [JSON.parse(req.body.labelsreport)],
		"Variables": [dataVariables]
	};


	var template = dataOrder.templateorder === undefined ? 'reports.mrt' : dataOrder.templateorder;

	var report = new Stimulsoft.Report.StiReport();
	var reportTemplate = fs.readFileSync("../Report/reportsandconsultations/reports/" + template, "utf8")

	report.load(reportTemplate);

	var dataSet = new Stimulsoft.System.Data.DataSet();
	dataSet.readJson(parameterReport);

	// Remove all connections from the report template
	report.dictionary.databases.clear();
	// Register DataSet object
	report.regData('Demo', 'Demo', dataSet);
	// Render report with registered data
	report.render();

	report.renderAsync(function () {
		// export to memory stream
		var settings = new Stimulsoft.Report.Export.StiPdfExportSettings();
		var service = new Stimulsoft.Report.Export.StiPdfExportService();

		var stream = new Stimulsoft.System.IO.MemoryStream();

		service.exportTo(report, stream, settings);

		var data = stream.toArray();

		var buffer = new Buffer(data, "utf-8");

		res.json(buffer);

	});



});

app.listen(6060);
/* jshint ignore:end */



