var router = require('express').Router();
var four0four = require('./utils/404')();
var nodemailer = require('nodemailer');
var WebSocket = require('ws');
var fs = require('fs');
var moment = require('moment');
var common = require('./common')();
var Request = require('request');
//var axios = require('axios');


router.get('/language', (req, res) => {
    res.send(fs.readFileSync("./src/client/languages/locale-es.json", "utf8"));
});

router.post("/validatedrights", rights);

router.get('/*', four0four.notFoundMiddleware);
router.post('/uploadFile', uploadFile);
router.post('/uploadAudio', uploadAudio);
router.post('/getlistReportFile', getlistReportFile);
router.post('/printReportOrders', printReportOrders);
router.post('/renderReport', renderReport);
router.post("/datatribunal", datatribunal);
router.post("/patienttribunal", patienttribunal);


module.exports = router;

//////////////  WEB SOCKET  ////////////////////////

var wss = new WebSocket.Server({ port: 5051 });
var connections = [];

wss.on('connection', function connection(ws, req) {

    var ip = req.connection.remoteAddress;
    var client = {
        ip: ip,
        ws: ws
    }

    connections.push(client);

    ws.on('close', function close() {
        connections.forEach(function each(value, key) {
            if (ws === value.ws) {
                delete connections[key];
            }
        })
    });
});

function printReportOrders(req, res) {
    var send = false;
    var message = {};
    var parameterReport = req.body;
    var path = "./src/client/Report/reportsandconsultations/reports/";
    var variables = JSON.parse(parameterReport.variables);
    var nameTemplate = "reports.mrt";
    var dataOrder = JSON.parse(parameterReport.order);
    var userlist = JSON.parse(parameterReport.userlist);
    var attachments = JSON.parse(parameterReport.attachments);

    if (fs.existsSync(path + variables.templateorder)) {
        nameTemplate = variables.templateorder;
    }

    if (dataOrder.allDemographics.length > 0) {
        dataOrder.allDemographics.forEach(function (value2) {
            dataOrder['demo_' + value2.idDemographic + '_name'] = value2.demographic;
            dataOrder['demo_' + value2.idDemographic + '_value'] = value2.encoded === false ? value2.notCodifiedValue : value2.codifiedName;
        });
    }

    dataOrder.createdDate = moment(dataOrder.createdDate).format(variables.formatDate + ' hh:mm:ss a.');
    dataOrder.patient.birthday = moment(dataOrder.patient.birthday).format(variables.formatDate);
    dataOrder.patient.age = common.getAgeAsString(dataOrder.patient.birthday, variables.formatDate);
    dataOrder.attachments = attachments === undefined ? [] : attachments;

    dataOrder.listfirm = [];
    for (var i = 0; i < dataOrder.resultTest.length; i++) {
        dataOrder.resultTest[i].resultDate = moment(dataOrder.resultTest[i].resultDate).format(variables.formatDate + ' hh:mm:ss a.');
        dataOrder.resultTest[i].validationDate = moment(dataOrder.resultTest[i].validationDate).format(variables.formatDate + ' hh:mm:ss a.');
        dataOrder.resultTest[i].entryDate = moment(dataOrder.resultTest[i].entryDate).format(variables.formatDate + ' hh:mm:ss a.');
        dataOrder.resultTest[i].takenDate = moment(dataOrder.resultTest[i].takenDate).format(variables.formatDate + ' hh:mm:ss a.');
        dataOrder.resultTest[i].verificationDate = moment(dataOrder.resultTest[i].verificationDate).format(variables.formatDate + ' hh:mm:ss a.');
        dataOrder.resultTest[i].printDate = moment(dataOrder.resultTest[i].printDate).format(variables.formatDate + ' hh:mm:ss a.');

        if (dataOrder.resultTest[i].hasAntibiogram) {
            dataOrder.resultTest[i].antibiogram = dataOrder.resultTest[i].microbialDetection.microorganisms;
        }

        if (dataOrder.resultTest[i].validationUserId !== undefined) {
            var findfirm = dataOrder.listfirm.find(order => (order.areaId === dataOrder.resultTest[i].areaId && order.validationUserId === dataOrder.resultTest[i].validationUserId));
            var user = userlist.find(user => user.id === dataOrder.resultTest[i].validationUserId);
            if (findfirm === undefined) {
                var firm = {
                    "areaId": dataOrder.resultTest[i].areaId,
                    "areaName": dataOrder.resultTest[i].areaName,
                    "validationUserId": dataOrder.resultTest[i].validationUserId,
                    "validationUserIdentification": dataOrder.resultTest[i].validationUserIdentification,
                    "validationUserName": dataOrder.resultTest[i].validationUserName,
                    "validationUserLastName": dataOrder.resultTest[i].validationUserLastName,
                    "firm": user.photo
                }
                dataOrder.listfirm.push(firm)
            }
        }
    }

    var report = new Stimulsoft.Report.StiReport();
    var reportTemplate = fs.readFileSync(path + nameTemplate, "utf8")
    report.load(reportTemplate);

    var jsonData = {
        "order": dataOrder,
        "Labels": [JSON.parse(parameterReport.labelsreport)],
        "Variables": [parameterReport.variables]
    };

    var dataSet = new Stimulsoft.System.Data.DataSet();
    dataSet.readJson(jsonData);

    // Remove all connections from the report template
    report.dictionary.databases.clear();
    // Register DataSet object
    report.regData('Demo', 'Demo', dataSet);
    // Render report with registered data
    report.render();
    var pdfData = report.exportDocument(Stimulsoft.Report.StiExportFormat.Pdf);
    var buffer = new Buffer(pdfData, "utf-8");

    res.json(buffer);
}

function getlistReportFile(req, res) {
    var listReports = [];
    fs.readdir("./src/client/Report", function (err, files) {
        files.forEach(function (element) {
            fs.readdir("./src/client/Report/" + element, function (err, files1) {
                if (files1 !== undefined) {
                    files1.forEach(function (element1) {
                        fs.readdir("./src/client/Report/" + element + "/" + element1, function (err, files2) {
                            if (files2 !== undefined) {
                                files2.forEach(function (element2) {
                                    var valid = element2.split(".");
                                    if (valid[1] === "mrt") {
                                        var report = { "name": element2 }
                                        listReports.push(report)
                                    } else if (valid.length === 1) {
                                        fs.readdir("./src/client/Report/" + element + "/" + element1 + "/" + element2, function (err, files3) {
                                            if (files3 !== undefined) {
                                                files3.forEach(function (element3) {
                                                    var valid2 = element3.split(".");
                                                    if (valid2[1] === "mrt") {
                                                        var report = { "name": element3 }
                                                        listReports.push(report)
                                                    }
                                                })
                                            }
                                        })
                                    }

                                })
                            }
                        })
                    })
                }
            })
        });

    });
    setTimeout(function () {
        res.send(listReports);
    }, 3000)
}

function uploadFile(req, res) {

    var path = './src/client' + req.body.path;
    fs.writeFile(path, req.body.file, 'utf-8', function (err) {
        if (err) {
            console.log(err);
            res.send(err);
        } else {
            console.log('The file has been saved!');
            res.send(200);
        }
    });



}

function renderReport(req, res) {
    var parameterReport = req.body;

    var Stimulsoft = require('stimulsoft-reports-js');
    Stimulsoft.Base.StiFontCollection.addOpentypeFontFile(parameterReport.letter);

    var report = new Stimulsoft.Report.StiReport();
    report.loadFile(parameterReport.pathreport);

    // Load reports from JSON object
    var jsonData = { 'data': [parameterReport.datareport], 'Labels': [parameterReport.labelsreport], 'Variables': [parameterReport.variables] };

    var dataSet = new Stimulsoft.System.Data.DataSet();
    dataSet.readJson(jsonData);

    // Remove all connections from the report template
    report.dictionary.databases.clear();
    // Register DataSet object
    report.regData('Demo', 'Demo', dataSet);
    report.render();

    if (parameterReport.type === 'pdf') {
        report.renderAsync(() => {
            // Export to PDF
            report.exportDocumentAsync((pdfData) => {
                // Converting Array into buffer


                var pdfData = report.exportDocument(Stimulsoft.Report.StiExportFormat.Pdf);
                var buffer = new Buffer(pdfData, "utf-8");
                /* var buffer = new Buffer(pdfData, "utf-8"); */
                res.json(buffer);
                // File System module
                /*  var fs = require('fs');
     
                 // Saving string with rendered report in PDF into a file
                 fs.writeFileSync('report/SimpleList.pdf', buffer);
                 console.log("Rendered report saved into PDF-file."); */
            }, Stimulsoft.Report.StiExportFormat.Pdf);
        });

    } else {
        report.renderAsync(() => {
            // Export to PDF
            report.exportDocumentAsync((pdfData) => {
                // Converting Array into buffer


                var pdfData = report.exportDocument(Stimulsoft.Report.StiExportFormat.Excel2007);
                var buffer = new Buffer(pdfData, "utf-8");
                /* var buffer = new Buffer(pdfData, "utf-8"); */
                res.json(buffer);
                // File System module
                /*  var fs = require('fs');
     
                 // Saving string with rendered report in PDF into a file
                 fs.writeFileSync('report/SimpleList.pdf', buffer);
                 console.log("Rendered report saved into PDF-file."); */
            }, Stimulsoft.Report.StiExportFormat.Excel2007);
        });
    }
}

////////////   PRINT BARCODE  ////////////////////////////
function uploadAudio(req, res) {

    var path = './src/client' + req.body.path;
    var buf = new Buffer(req.body.file, 'base64'); // decode
    fs.writeFile(path, buf, function (err) {
        if (err) {
            console.log("err", err);
            res.send(err);
        } else {
            res.send(200);
        }
    });
}
function datatribunal(req, res, next) {
    var data = req.body;
    Request.post(
        {
            headers: {
                "content-type": "application/json",
                Authorization: 'Basic ' + Buffer.from(data.username + ':' + data.password).toString('base64'),
            },
            url: data.url,
            hideOverlay: true,
            body: JSON.stringify({
                personalId: data.personalId,
                fullNameApproximation: data.fullNameApproximation,
            }),
        },
        (error, response, body) => {
            if (!error && response.statusCode === 200) {
                console.log('El servicio está arriba y funcionando correctamente.');
                try {
                    res.send(JSON.parse(body));

                } catch (error) {
                    console.log('paciente no existe');
                    res.send('paciente no existe');
                }

            } else {
                res.send('Error al intentar acceder al servicio');
            }
        }
    );
}
function patienttribunal(req, res, next) {
    var data = req.body;
    Request.post(
        {
            headers: {
                "content-type": "application/json",
                Authorization: 'Basic ' + Buffer.from(data.username + ':' + data.password).toString('base64'),
            },
            url: data.url,
            hideOverlay: true,
            body: JSON.stringify({
                personalId: data.personalId,
                fullNameApproximation: data.fullNameApproximation,
            }),
        },
        (error, response, body) => {
            if (!error && response.statusCode === 200) {
                try {
                    console.log('El servicio está arriba y funcionando correctamente.');
                    res.send(JSON.parse(body));

                } catch (error) {
                    console.log('paciente no existe');
                    res.send('paciente no existe');
                }

            } else {
                res.send('Error al intentar acceder al servicio');
            }
        }
    );
}

function rights(req, res) {
    var data = req.body.url + req.body.identificacion;
    //console.log(res.req.query.parameters)
    Request.get(data, (error, response, body) => {
        console.log(JSON.stringify(response))
        if (response.statusCode === 200) {
            console.log(body)
            res.send(body);
        } else {
            console.log('validador' + error)
            res.send('Error al intentar acceder al servicio');
        }
    });
}


function rights1(req, res, next) {
	console.log("servicio validador");
    console.log("parametro" + req.body)
	var data = req.body;
	
	var instance = axios.create({
		httpsAgent: new https.Agent({ rejectUnauthorized: false })
	});
	
	// Hacer una solicitud GET a otro servicio
    instance.get(data.url + data.identificacion)
        .then(response => {
            // Aquí puedes procesar la respuesta como desees
            const responseData = response.data;
            
            // Responder al cliente con los datos obtenidos del servicio GET
            res.json(responseData);
        })
        .catch(error => {
            // Manejar errores
            console.error('Error al hacer la solicitud GET:', error);
            res.status(500).send('Error interno del servidor');
        });
    
}







