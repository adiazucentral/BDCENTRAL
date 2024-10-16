<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <title>Enterprise Outreach - CLTech</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="icon" href="${pageContext.request.contextPath}/front-end/img/cltech_logo.ico">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/front-end/css/plugins/bootstrap.min.css" >
        <link rel="stylesheet" href="${pageContext.request.contextPath}/front-end/css/plugins/jsondoc.css" >
        <script type="text/javascript" src="${pageContext.request.contextPath}/front-end/js/plugins/jquery-2.1.4.min.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/front-end/js/plugins/bootstrap.min.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/front-end/js/plugins/handlebars-1.0.0.beta.6.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/front-end/js/plugins/jlinq.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/front-end/js/plugins/prettify.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/front-end/js/plugins/jsondoc.js"></script>
    </head>
    <body>
        <nav class="navbar navbar-default navbar-fixed-top">
            <div class="container-fluid">
                <div class="navbar-header">
                    <a class="navbar-brand" href="#">Enterprise Outreach - Rest API</a>
                </div>
                <form class="navbar-form navbar-left col-md-8" role="search" hidden>
                    <div class="form-group" hidden>
                        <input id="jsondocfetch" type="text" class="form-control" style="width:350px" placeholder="Insert here the JSONDoc URL" value="${jsondoc.path}" autocomplete="off" hidden>
                    </div>
                    <button id="getDocButton" class="btn btn-default" hidden>Get documentation</button>
                </form>
            </div>
        </nav>

        <div class="container-fluid">
            <div class="row">
                <div class="col-md-3">
                    <div id="maindiv" style="display:none;"></div>
                    <div class="panel-group" id="side-accordion" aria-multiselectable="true" style="display: none;">
                        <div id="globaldiv"></div>
                        <div id="apidiv"></div>
                        <div id="objectdiv"></div>
                        <div id="flowdiv"></div>
                    </div>
                </div>
                <div class="col-md-5">
                    <div id="content"></div>
                </div>

                <div class="col-md-4">
                    <div id="testContent"></div>
                </div>
            </div>
        </div>

        <script id="main" type="text/x-handlebars-template">
            <blockquote>
            <p style="text-transform: uppercase;">API info</span></p>
            <small>Base path: {{basePath}}</small>
            <small>Version: {{version}}</small>
            </blockquote>
        </script>

        <script id="global" type="text/x-handlebars-template">
            {{#if global}}
            <div class="panel-heading" id="panel-global">
            <h4 class="panel-title">
            <a id="panel-global" href="#_panel-global" data-toggle="collapse" data-parent="#side-accordion" aria-controls="_panel-global" aria-expanded="true">Overview</a>
            </h4>
            </div>
            <div id="_panel-global" class="panel-collapse collapse in" aria-labelledby="panel-global">
            <div class="panel-body">
            <ul class="list-unstyled">
            <li><a href="#" id="_overview" rel="global">Global documentation</a></li>
            {{#if global.changelogset.changelogs}}
            <li><a href="#" id="_changelogs" rel="global">Changelog</a></li>
            {{/if}}
            {{#if global.migrationset.migrations}}
            <li><a href="#" id="_migrations" rel="global">Migration notes</a></li>
            {{/if}}
            </ul>
            </div>
            </div>
            {{/if}}
        </script>

        <script id="apis" type="text/x-handlebars-template">
            {{#if apis}}
            <div class="panel-heading" id="panel-apis">
            <h4 class="panel-title">
            <a id="panel-apis" href="#_panel-apis" data-toggle="collapse" data-parent="#side-accordion" aria-controls="_panel-apis" aria-expanded="true">API</a>
            </h4>
            </div>
            <div id="_panel-apis" class="panel-collapse collapse in" aria-labelledby="panel-apis">
            <div class="panel-body">
            {{#eachInMap apis}}
            <ul class="list-unstyled">
            {{#if key}}
            <li style="text-transform: uppercase;">{{key}}</li>
            {{/if}}
            {{#each value}}
            <li><a href="#" id="{{jsondocId}}" rel="api">{{name}}</a></li>
            {{/each}}
            </ul>
            {{/eachInMap}}
            </div>
            </div>
            {{/if}}
        </script>

        <script id="objects" type="text/x-handlebars-template">
            {{#if objects}}
            <div class="panel-heading" id="panel-objects">
            <h4 class="panel-title">
            <a id="panel-objects" href="#_panel-objects" data-toggle="collapse" data-parent="#side-accordion" aria-controls="_panel-objects" aria-expanded="true">Objects</a>
            </h4>
            </div>
            <div id="_panel-objects" class="panel-collapse collapse" aria-labelledby="panel-objects">
            <div class="panel-body">
            {{#eachInMap objects}}
            <ul class="list-unstyled">
            {{#if key}}
            <li style="text-transform: uppercase;">{{key}}</li>
            {{/if}}
            {{#each value}}
            <li><a href="#" id="{{jsondocId}}" rel="object">{{name}}</a></li>
            {{/each}}
            </ul>
            {{/eachInMap}}
            </div>
            </div>
            {{/if}}
        </script>

        <script id="flows" type="text/x-handlebars-template">
            {{#if flows}}
            <div class="panel-heading" id="panel-flows">
            <h4 class="panel-title">
            <a id="panel-flows" href="#_panel-flows" data-toggle="collapse" data-parent="#side-accordion" aria-controls="_panel-flows" aria-expanded="true">Flows</a>
            </h4>
            </div>
            <div id="_panel-flows" class="panel-collapse collapse" aria-labelledby="panel-flows">
            <div class="panel-body">
            {{#eachInMap flows}}
            <ul class="list-unstyled">
            {{#if key}}
            <li style="text-transform: uppercase;">{{key}}</li>
            {{/if}}
            {{#each value}}
            <li><a href="#" id="{{jsondocId}}" rel="flow">{{name}}</a></li>
            {{/each}}
            </ul>
            {{/eachInMap}}
            </div>
            </div>
            {{/if}}
        </script>

        <script id="overviewdiv" type="text/x-handlebars-template">
            <blockquote>
            <p style="text-transform: uppercase;"><span id="apiName">Global documentation</span></p>
            <small><span id="apiDescription">Documentation valid for the whole application</span></small>
            </blockquote>

            <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">

            {{#if this}}
            {{#eachInMap this.sections}}
            <div class="panel panel-default">
            <div class="panel-heading" id="_{{value.jsondocId}}">
            <h4 class="panel-title">
            <a id="_{{value.jsondocId}}" href="#__{{value.jsondocId}}" rel="method" data-toggle="collapse" data-parent="#accordion" aria-controls="__{{value.jsondocId}}" aria-expanded="true">{{value.title}}</a><br/>
            </h4>
            </div>
            <div id="__{{value.jsondocId}}" class="panel-collapse collapse" role="tabpanel" aria-labelledby="_{{value.jsondocId}}">
            <div class="panel-body" rel="overviewpanel">
            {{#if value.paragraphs}}
            {{#each value.paragraphs}}
            <p>{{{this}}}</p>
            {{/each}}
            {{/if}}
            </div>
            </div>
            </div>
            {{/eachInMap}}
            {{/if}}

            </div>
        </script>

        <script id="changelogsdiv" type="text/x-handlebars-template">
            <blockquote>
            <p style="text-transform: uppercase;"><span id="apiName">Changelog</span></p>
            <small><span id="apiDescription">Global list of changes</span></small>
            </blockquote>

            <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
            {{#changelogs}}
            <div class="panel panel-default">
            <div class="panel-heading" id="{{jsondocId}}">
            <h4 class="panel-title">
            <a id="{{jsondocId}}" href="#_{{jsondocId}}" rel="method" data-toggle="collapse" data-parent="#accordion" aria-controls="_{{jsondocId}}" aria-expanded="true">Version {{version}}</a><br/>
            </h4>
            </div>
            <div id="_{{jsondocId}}" class="panel-collapse collapse" role="tabpanel" aria-labelledby="{{jsondocId}}">
            <div class="panel-body">
            <ul style="padding-left: 20px;">
            {{#each changes}}
            <li>{{{this}}}</li>
            {{/each}}
            </ul>
            </div>
            </div>
            </div>
            {{/changelogs}}
            </div>
        </script>

        <script id="migrationsdiv" type="text/x-handlebars-template">
            <blockquote>
            <p style="text-transform: uppercase;"><span id="apiName">Migration notes</span></p>
            <small><span id="apiDescription">List of steps to migrate from one version to another</span></small>
            </blockquote>

            <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
            {{#migrations}}
            <div class="panel panel-default">
            <div class="panel-heading" id="{{jsondocId}}">
            <h4 class="panel-title">
            <a id="{{jsondocId}}" href="#_{{jsondocId}}" rel="method" data-toggle="collapse" data-parent="#accordion" aria-controls="_{{jsondocId}}" aria-expanded="true">From version {{fromVersion}} to version {{toVersion}}</a><br/>
            </h4>
            </div>
            <div id="_{{jsondocId}}" class="panel-collapse collapse" role="tabpanel" aria-labelledby="{{jsondocId}}">
            <div class="panel-body">
            <ul style="padding-left: 20px;">
            {{#each steps}}
            <li>{{{this}}}</li>
            {{/each}}
            </ul>
            </div>
            </div>
            </div>
            {{/migrations}}
            </div>
        </script>

        <script id="methods" type="text/x-handlebars-template">
            <blockquote>
            <p style="text-transform: uppercase;"><span id="apiName">{{name}}</span></p>
            <small><span id="apiDescription">{{description}}</span></small>
            <small><span id="apiSupportedVersions"></span></small>
            </blockquote>

            {{#if preconditions}}
            <div class="alert alert-info border-radius-none">
            <p><strong>Preconditions: </strong></p>
            <ul class="list-unstyled">
            {{#each preconditions}}
            <li>{{this}}</li>
            {{/each}}
            </ul>
            </div>
            {{/if}}

            <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
            {{#methods}}
            <div class="panel panel-default">
            <div class="panel-heading" id="{{jsondocId}}">
            <h4 class="panel-title">
            {{#each verb}}
            <span class="label pull-right verb {{this}}">{{this}}</span>
            {{/each}}
            {{#each path}}
            <a id="{{../jsondocId}}" href="#_{{../jsondocId}}" rel="method" data-toggle="collapse" data-parent="#accordion" aria-controls="_{{../jsondocId}}" aria-expanded="true">{{../displayedMethodString}}</a><br/>
            {{/each}}
            </h4>
            </div>
            <div id="_{{jsondocId}}" class="panel-collapse collapse" role="tabpanel" aria-labelledby="{{jsondocId}}">
            <div class="panel-body">
            {{#if jsondocerrors}}
            <div class="alert alert-danger border-radius-none">
            <p><strong>The following errors prevent a correct functionality of the playground and do not provide enough documentation data for API users:</strong></p>
            <ul class="list-unstyled">
            {{#each jsondocerrors}} <li>- {{this}}</li> {{/each}}
            </ul>
            </div>
            {{/if}}
            <table class="table table-condensed table-bordered" style="table-layout: fixed;">
            <tr>
            <th style="width:18%;">Path</th>
            <td>
            {{#each path}}
            <code>{{this}}</code><br/>
            {{/each}}
            </td>
            </tr>
            {{#if supportedversions}}
            <tr>
            <td>Since version</td>
            <td>{{supportedversions.since}}</td>
            </tr>
            {{#if supportedversions.until}}
            <tr>
            <td>Until version</td>
            <td>{{supportedversions.until}}</td>
            </tr>
            {{/if}}
            {{/if}}

            {{#if visibility}}
            {{#equal visibility "UNDEFINED"}}
            {{else}}
            <tr>
            <th>Visibility</th>
            <td>{{visibility}}</td>
            </tr>
            {{/equal}}
            {{/if}}

            {{#if stage}}
            {{#equal stage "UNDEFINED"}}
            {{else}}
            <tr>
            <th>Stage</th>
            <td>{{stage}}</td>
            </tr>
            {{/equal}}
            {{/if}}

            {{#if description}}
            <tr>
            <th>Description</th>
            <td>{{{description}}}</td>
            </tr>
            {{/if}}

            {{#if auth}}
            <tr>
            <th>Auth</th>
            <td>{{auth.type}}, Roles: {{auth.roles}}</td>
            {{#if auth.scheme}}
            <tr><td></td><td>Scheme: {{auth.scheme}}</td></tr>
            {{/if}}
            </tr>
            {{/if}}

            {{#if produces}}
            <tr>
            <th colspan=2>Produces</th>
            </tr>
            <tr>
            <td colspan=2>
            {{#each produces}} <code>{{this}}</code> {{/each}}
            </td>
            </tr>
            {{/if}}
            {{#if consumes}}
            <tr>
            <th colspan=2>Consumes</th>
            </tr>
            <tr>
            <td colspan=2>
            {{#each consumes}} <code>{{this}}</code> {{/each}}
            </td>
            </tr>
            {{/if}}
            {{#if headers}}
            <tr>
            <th colspan=2>Headers</th>
            </tr>
            {{#each headers}}
            {{#if this.description}}
            <tr>
            <td><code>{{this.name}}</code></td>
            <td>{{{this.description}}}</td>
            </tr>
            {{#if this.allowedvalues}}
            <tr>
            <td></td>
            <td>Allowed values: {{this.allowedvalues}}</td>
            </tr>
            {{/if}}
            {{else}}
            <tr>
            <td><code>{{this.name}}</code></td>
            {{#if this.allowedvalues}}
            <td>Allowed values: {{this.allowedvalues}}</td>
            {{/if}}
            </tr>
            {{/if}}
            {{/each}}
            {{/if}}
            {{#if pathparameters}}
            <tr>
            <th colspan=2>Path parameters</th>
            </tr>
            {{#each pathparameters}}
            <tr>
            <td><code>{{this.name}}</code></td>
            <td>Required: {{this.required}}</td>
            </tr>
            {{#if this.description}}
            <tr>
            <td></td>
            <td>Description: {{{this.description}}}</td>
            </tr>
            {{/if}}
            <tr>
            <td></td>
            <td>Type: <code>{{this.jsondocType.oneLineText}}</code></td>
            </tr>
            {{#if this.allowedvalues}}
            <tr>
            <td></td>
            <td>Allowed values: {{this.allowedvalues}}</td>
            </tr>
            {{/if}}
            {{#if this.format}}
            <tr>
            <td></td>
            <td>Format: {{this.format}}</td>
            </tr>
            {{/if}}
            {{/each}}
            {{/if}}
            {{#if queryparameters}}
            <tr>
            <th colspan=2>Query parameters</th>
            </tr>
            {{#each queryparameters}}
            <tr>
            <td><code>{{this.name}}</code></td>
            <td>Required: {{this.required}}</td>
            </tr>
            {{#if this.description}}
            <tr>
            <td></td>
            <td>Description: {{{this.description}}}</td>
            </tr>
            {{/if}}
            <tr>
            <td></td>
            <td>Type: <code>{{this.jsondocType.oneLineText}}</code></td>
            </tr>
            {{#if this.allowedvalues}}
            <tr>
            <td></td>
            <td>Allowed values: {{this.allowedvalues}}</td>
            </tr>
            {{/if}}
            {{#if this.format}}
            <tr>
            <td></td>
            <td>Format: {{this.format}}</td>
            </tr>
            {{/if}}
            {{#if this.defaultvalue}}
            <tr>
            <td></td>
            <td>Default value: {{this.defaultvalue}}</td>
            </tr>
            {{/if}}
            {{/each}}
            {{/if}}
            {{#if bodyobject}}
            <tr>
            <th colspan=2>Body object</th>
            </tr>
            <tr>
            <td colspan=2><code>{{bodyobject.jsondocType.oneLineText}}</code></td>
            </tr>
            {{/if}}
            {{#if responsestatuscode}}
            <tr>
            <th colspan=2>Response status code</th>
            </tr>
            <tr>
            <td colspan=2><code>{{responsestatuscode}}</code></td>
            </tr>
            {{/if}}
            {{#if response}}
            <tr>
            <th colspan=2>Response object</th>
            </tr>
            <tr>
            <td colspan=2><code>{{response.jsondocType.oneLineText}}</code></td>
            </tr>
            {{/if}}
            {{#if apierrors}}
            <tr>
            <th colspan=2>Errors</th>
            </tr>
            {{#each apierrors}}
            <tr>
            <td><code>{{this.code}}</code></td>
            <td>{{{this.description}}}</td>
            </tr>
            {{/each}}
            {{/if}}
            </table>
            {{#if jsondocwarnings}}
            <div class="alert alert-warning alert-dismissible border-radius-none">
            <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            <p><strong>Warnings that may prevent a correct playground functionality:</strong></p>
            <ul class="list-unstyled">
            {{#each jsondocwarnings}} <li>- {{this}}</li> {{/each}}
            </ul>
            </div>
            {{/if}}
            {{#if jsondochints}}
            <div class="alert alert-info alert-dismissible border-radius-none">
            <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            <p><strong>Hints to provide a better understanding of your API:</strong></p>
            <ul class="list-unstyled">
            {{#each jsondochints}} <li>- {{this}}</li> {{/each}}
            </ul>
            </div>
            {{/if}}
            </div>
            </div>
            </div>
            {{/methods}}
            </div>
        </script>

        <script id="test" type="text/x-handlebars-template">
            <blockquote>
            <p style="text-transform: uppercase;">Playground</span></p>
            <small>{{#each path}}{{this}}<br/>{{/each}}</small>
            </blockquote>

            <div class="row">
            {{#if path}}
            <div class="col-md-12" {{#compare path.length 1 operator="=="}} style="display:none;" {{/compare}}>
            <h4>Request path</h4>
            <div class="form-group">
            <select class="form-control" id="requestPath">
            {{#each path}}
            <option value="{{this}}">{{this}}</option>
            {{/each}}
            </select>
            </div>
            </div>
            {{/if}}

            {{#if verb}}
            <div class="col-md-12" {{#compare verb.length 1 operator="=="}} style="display:none;" {{/compare}}>
            <h4>Request method</h4>
            <div class="form-group">
            <select class="form-control" id="requestMethod">
            {{#each verb}}
            <option value="{{this}}">{{this}}</option>
            {{/each}}
            </select>
            </div>
            </div>
            {{/if}}

            {{#if auth}}
            {{#equal auth.type "BASIC_AUTH"}}
            <div class="col-md-12">
            <h4>Basic Authentication</h4>
            <div class="form-group">
            <select class="form-control" id="basicAuthSelect" onchange="fillBasicAuthFields(); return false;">
            <option disabled="disabled" selected="selected">Select a test user or fill inputs below</option>
            {{#eachInMap auth.testusers}}
            <option value="{{value}}">{{key}}</option>
            {{/eachInMap}}
            <option value="a-wrong-password">invalidate-credentials-cache-user</option>
            </select>
            </div>
            <div class="form-group" style="margin-bottom:5px;">
            <label for="basicAuthUsername">Username</label>
            <input class="form-control" type="text" id="basicAuthUsername" name="basicAuthUsername" placeholder="Username">
            </div>
            <div class="form-group">
            <label for="basicAuthPassword">Password</label>
            <input class="form-control" type="text" id="basicAuthPassword" name="basicAuthPassword" placeholder="Password">
            </div>
            </div>
            {{/equal}}
            {{#equal auth.type "TOKEN"}}
            <div class="col-md-12">
            <h4>Token Authentication</h4>
            {{#if auth.testtokens}}
            <div class="form-group">
            <select class="form-control" id="tokenAuthSelect" onchange="fillTokenAuthFields(); return false;">
            <option disabled="disabled" selected="selected">Select a test token or fill input below</option>
            {{#each auth.testtokens}}
            <option value="{{this}}">{{this}}</option>
            {{/each}}
            </select>
            </div>
            {{/if}}
            <div class="form-group" style="margin-bottom:5px;">
            <label for="tokenAuthToken">Token</label>
            <input class="form-control" type="text" id="tokenAuthToken" name="tokenAuthToken" placeholder="Token">
            </div>
            </div>
            {{/equal}}
            {{/if}}

            {{#if headers}}
            <div class="col-md-12">
            <div id="headers">
            <h4>Headers</h4>
            {{#headers}}
            <div class="form-group">
            <label for="i_{{name}}">{{name}}</label>
            {{#compare allowedvalues.length 1 operator="=="}}
            <input type="text" class="form-control" name="{{name}}" placeholder="{{name}}" value="{{allowedvalues}}">
            {{/compare}}
            {{#compare allowedvalues.length 1 operator="!="}}
            <input type="text" class="form-control" name="{{name}}" placeholder="{{name}}">
            {{/compare}}
            </div>
            {{/headers}}
            </div>
            </div>
            {{/if}}

            {{#if produces}}
            <div class="col-md-6" style="margin-left:0px">
            <div id="produces" class="playground-spacer">
            <h4>Accept</h4>
            {{#produces}}
            <label><input type="radio" name="produces" value="{{this}}"> {{this}}</label><br/>
            {{/produces}}
            </div>
            </div>
            {{/if}}

            {{#if bodyobject}}
            {{#if consumes}}
            <div class="col-md-6" style="margin-left:0px">
            <div id="consumes" class="playground-spacer">
            <h4>Content type</h4>
            {{#consumes}}
            <label><input type="radio" name="consumes" value="{{this}}"> {{this}}</label>
            {{/consumes}}
            </div>
            </div>
            {{/if}}
            {{/if}}

            <div class="col-md-12">
            <div id="validationerrors" class="alert alert-danger" style="display:none">
            <strong>Validation errors:</strong>
            <ul class="list-unstyled"></ul>
            </div>
            </div>

            {{#if pathparameters}}
            <div class="col-md-12">
            <div id="pathparameters" class="playground-spacer">
            <h4>Path parameters</h4>
            {{#pathparameters}}
            <div class="form-group">
            <label class="control-label" for="i_{{name}}">{{name}}</label>
            <input type="text" class="form-control" id="i_{{name}}" name="{{name}}" placeholder="{{name}}">
            </div>
            {{/pathparameters}}
            </div>
            </div>
            {{/if}}

            {{#if queryparameters}}
            <div class="col-md-12">
            <div id="queryparameters" class="playground-spacer">
            <h4>Query parameters</h4>
            {{#queryparameters}}
            <div class="form-group">
            <label for="i_{{name}}">{{name}}</label>
            {{#compare required "true" operator="=="}}
            {{#compare allowedvalues.length 1 operator="=="}}
            <input type="text" class="form-control" id="i_{{name}}" name="{{name}}" placeholder="{{name}}" value="{{allowedvalues}}" required>
            {{/compare}}
            {{#compare allowedvalues.length 1 operator="!="}}
            <input type="text" class="form-control" id="i_{{name}}" name="{{name}}" placeholder="{{name}}" required>
            {{/compare}}
            {{/compare}}
            {{#compare required "false" operator="=="}}
            {{#compare allowedvalues.length 1 operator="=="}}
            <input type="text" class="form-control" id="i_{{name}}" name="{{name}}" placeholder="{{name}}" value="{{allowedvalues}}">
            {{/compare}}
            {{#compare allowedvalues.length 1 operator="!="}}
            <input type="text" class="form-control" id="i_{{name}}" name="{{name}}" placeholder="{{name}}">
            {{/compare}}
            {{/compare}}
            </div>
            {{/queryparameters}}
            </div>
            </div>
            {{/if}}

            {{#if bodyobject}}
            <div class="col-md-12">
            <div id="bodyobject" class="playground-spacer">
            <h4>Body object</h4>
            <textarea class="form-control" id="inputJson" rows=10 />
            </div>
            </div>
            {{/if}}

            <div class="col-md-12 playground-spacer">
            <button class="btn btn-primary col-md-12" id="testButton" data-loading-text="Loading...">Submit</button>
            </div>
            </div>
            </div>

            <div class="row">
            <div class="col-md-12">
            <div class="tabbable" id="resInfo" style="display:none; margin-top: 20px;">
            <ul class="nav nav-tabs">
            <li class="active"><a href="#tab1" data-toggle="tab">Response text</a></li>
            <li><a href="#tab2" data-toggle="tab">Response info</a></li>
            <li><a href="#tab3" data-toggle="tab">Request info</a></li>
            </ul>
            <div class="tab-content" style="margin-top: 20px">
            <div class="tab-pane active" id="tab1">
            <pre id="response" class="prettyprint">
            </pre>
            </div>
            <div class="tab-pane" id="tab2">
            <h5 style="padding:0px">Response code</p>
            <pre id="responseStatus" class="prettyprint">
            </pre>
            <h5 style="padding:0px">Response headers</p>
            <pre id="responseHeaders" class="prettyprint">
            </pre>
            </div>
            <div class="tab-pane" id="tab3">
            <h5 style="padding:0px">Request URL</p>
            <pre id="requestURL" class="prettyprint">
            </pre>
            </div>
            </div>
            </div>
            </div>

        </script>

        <script id="object" type="text/x-handlebars-template">
            <table class="table table-condensed table-striped table-bordered" style="table-layout: fixed;">
            <tr><th style="width:18%;">Name</th><td><code>{{name}}</code></td></tr>
            {{#if description}}
            <tr><th>Description</th><td>{{{description}}}</td></tr>
            {{/if}}
            {{#if supportedversions}}
            <tr>
            <td>Since version</td>
            <td>{{supportedversions.since}}</td>
            </tr>
            {{#if supportedversions.until}}
            <tr>
            <td>Until version</td>
            <td>{{supportedversions.until}}</td>
            </tr>
            {{/if}}
            {{/if}}
            {{#if visibility}}
            {{#equal visibility "UNDEFINED"}}
            {{else}}
            <tr>
            <th>Visibility</th>
            <td>{{visibility}}</td>
            </tr>
            {{/equal}}
            {{/if}}
            {{#if stage}}
            {{#equal stage "UNDEFINED"}}
            {{else}}
            <tr>
            <th>Stage</th>
            <td>{{stage}}</td>
            </tr>
            {{/equal}}
            {{/if}}
            {{#if allowedvalues}}
            <tr><td></td><td>Allowed values: {{allowedvalues}}</td></tr>
            {{/if}}
            {{#if fields}}
            <tr><th colspan=2>Fields</th></tr>
            {{#each fields}}
            {{#if description}}
            <tr><td><code>{{name}}</code></td><td>{{{description}}}</td></tr>
            <tr><td></td><td>Type: <code>{{jsondocType.oneLineText}}</code></td></tr>
            {{else}}
            <tr><td><code>{{name}}</code></td><td>Type: <code>{{jsondocType.oneLineText}}</code></td></tr>
            {{/if}}
            {{#equal required "UNDEFINED"}}
            {{else}}
            <tr><td></td><td>Required: {{required}}</td></tr>
            {{/equal}}
            {{#if format}}
            <tr><td></td><td>Format: {{displayedFormat}}</td></tr>
            {{/if}}
            {{#if allowedvalues}}
            <tr><td></td><td>Allowed values: {{allowedvalues}}</td></tr>
            {{/if}}
            {{#if supportedversions}}
            <tr><td></td><td>Since version {{supportedversions.since}}</td></tr>
            {{#if supportedversions.until}}
            <tr><td></td><td>Until version {{supportedversions.until}}</td></tr>
            {{/if}}
            {{/if}}
            {{/each}}
            {{/if}}
            </table>
            {{#if jsondochints}}
            <div class="alert alert-info alert-dismissible border-radius-none">
            <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            <p><strong>Hints to provide a better understanding of your API:</strong></p>
            <ul class="list-unstyled">
            {{#each jsondochints}} <li>- {{this}}</li> {{/each}}
            </ul>
            </div>
            {{/if}}
        </script>

        <script id="objecttemplate" type="text/x-handlebars-template">
            <pre class="prettyprint">
            {{#json jsondocTemplate}}
            {{/json}}
            </pre>
        </script>

        <script>
            var model;
            var jsondoc = JSON.stringify('_JSONDOC_OFFLINE_PLACEHOLDER_');

            $(document).ready(function () {
                // This is to check if the _JSONDOC_OFFLINE_PLACEHOLDER_ has been replaced with content coming from
                // the result of the jsondoc-maven-plugin
                if (jsondoc != "\"_JSONDOC_OFFLINE_PLACEHOLDER_\"") {
                    $("#jsondocfetch").hide();
                    $("#getDocButton").hide();
                    buildFromJSONDoc($.parseJSON(jsondoc));
                }

                if ($.urlParam('url') != null) {
                    $("#jsondocfetch").val($.urlParam('url'));
                    checkURLExistence();
                }
            });

            Handlebars.registerHelper('compare', function (lvalue, rvalue, options) {
                if (arguments.length < 3)
                    throw new Error("Handlerbars Helper 'compare' needs 2 parameters");

                operator = options.hash.operator || "==";

                var operators = {
                    '==': function (l, r) {
                        return l == r;
                    },
                    '===': function (l, r) {
                        return l === r;
                    },
                    '!=': function (l, r) {
                        return l != r;
                    },
                    '<': function (l, r) {
                        return l < r;
                    },
                    '>': function (l, r) {
                        return l > r;
                    },
                    '<=': function (l, r) {
                        return l <= r;
                    },
                    '>=': function (l, r) {
                        return l >= r;
                    },
                    'typeof': function (l, r) {
                        return typeof l == r;
                    }
                }

                if (!operators[operator])
                    throw new Error("Handlerbars Helper 'compare' doesn't know the operator " + operator);

                var result = operators[operator](lvalue, rvalue);

                if (result) {
                    return options.fn(this);
                } else {
                    return options.inverse(this);
                }

            });

            Handlebars.registerHelper('equal', function (lvalue, rvalue, options) {
                if (lvalue != rvalue) {
                    return options.inverse(this);
                } else {
                    return options.fn(this);
                }
            });

            Handlebars.registerHelper('eachInMap', function (map, block) {
                var out = '';
                Object.keys(map).map(function (prop) {
                    out += block.fn({key: prop, value: map[ prop ]});
                });
                return out;
            });

            function replacer(key, value) {
                if (value == null)
                    return undefined;
                else
                    return value;
            }

            Handlebars.registerHelper('json', function (context) {
                return JSON.stringify(context, replacer, 2);
            });

            function checkURLExistence() {
                var value = $("#jsondocfetch").val();
                if (value.trim() == '') {
                    alert("Please insert a valid URL");
                    return false;
                } else {
                    return fetchdoc(value);
                }
            }

            $("#jsondocfetch").keypress(function (event) {
                if (event.which == 13) {
                    checkURLExistence();
                    return false;
                }
            });

            $("#getDocButton").click(function () {
                checkURLExistence();
                return false;
            });

            function fillBasicAuthFields() {
                $("#basicAuthPassword").val($("#basicAuthSelect").val());
                $("#basicAuthUsername").val($("#basicAuthSelect").find(":selected").text());
            }

            function fillTokenAuthFields() {
                $("#tokenAuthToken").val($("#tokenAuthSelect").val());
            }

            function printResponse(data, res, url) {
                if (res.responseXML != null) {
                    $("#response").text(formatXML(res.responseText));
                } else {
                    $("#response").text(JSON.stringify(data, undefined, 2));
                }

                prettyPrint();
                $("#responseStatus").text(res.status);
                $("#responseHeaders").text(res.getAllResponseHeaders());
                $("#requestURL").text(url);
                $('#testButton').button('reset');
                $("#resInfo").show();
            }

            function formatXML(xml) {
                var formatted = '';
                var reg = /(>)(<)(\/*)/g;
                xml = xml.replace(reg, '$1\r\n$2$3');
                var pad = 0;
                jQuery.each(xml.split('\r\n'), function (index, node) {
                    var indent = 0;
                    if (node.match(/.+<\/\w[^>]*>$/)) {
                        indent = 0;
                    } else if (node.match(/^<\/\w/)) {
                        if (pad != 0) {
                            pad -= 1;
                        }
                    } else if (node.match(/^<\w[^>]*[^\/]>.*$/)) {
                        indent = 1;
                    } else {
                        indent = 0;
                    }

                    var padding = '';
                    for (var i = 0; i < pad; i++) {
                        padding += '  ';
                    }

                    formatted += padding + node + '\r\n';
                    pad += indent;
                });

                return formatted;
            }

            function buildObjectContent(item, playgroundEnabled) {
                resetLayoutClasses();

                var object = Handlebars.compile($("#object").html());
                var objectHTML = object(item);
                $("#content").html(objectHTML);
                $("#content").show();
                $("#testContent").hide();

                if (playgroundEnabled) {
                    var objecttemplate = Handlebars.compile($("#objecttemplate").html());
                    var objecttemplateHTML = objecttemplate(item);
                    $("#testContent").html(objecttemplateHTML);
                    $("#testContent").show();
                    prettyPrint();
                } else {
                    $("#content").parent().removeClass();
                    $("#content").parent().addClass("col-md-9");
                }
            }

            function buildMethodsContent(items, playgroundEnabled) {
                resetLayoutClasses();

                if (playgroundEnabled) {
                    $('#content a[rel="method"]').each(function () {
                        $(this).click(function () {
                            var method = jlinq.from(items).equals("jsondocId", this.id).first();
                            var test = Handlebars.compile($("#test").html());
                            var testHTML = test(method);
                            $("#testContent").html(testHTML);
                            $("#testContent").show();

                            // if bodyobject is not empty then put jsondocTemplate into textarea
                            if (method.bodyobject) {
                                $("#inputJson").text(JSON.stringify(method.bodyobject.jsondocTemplate, undefined, 2));
                            }

                            $("#produces input:first").attr("checked", "checked");
                            $("#consumes input:first").attr("checked", "checked");

                            $("#testButton").click(function () {
                                var requestMethod = $("#requestMethod").val();
                                var requestPath = $("#requestPath").val();

                                var headers = new Object();
                                $("#headers input").each(function () {
                                    headers[this.name] = $(this).val();
                                });

                                headers["Accept"] = $("#produces input:checked").val();

                                if (method.auth) {
                                    if (method.auth.type == "BASIC_AUTH") {
                                        headers["Authorization"] = "Basic " + window.btoa($('#basicAuthUsername').val() + ":" + $('#basicAuthPassword').val());
                                    }

                                    if (method.auth.type == "TOKEN") {
                                        if (typeof (method.auth.scheme) != "undefined" && method.auth.scheme != "") {
                                            headers["Authorization"] = method.auth.scheme + " " + $('#tokenAuthToken').val();
                                        } else {
                                            headers["Authorization"] = $('#tokenAuthToken').val();
                                        }
                                    }
                                }

                                var replacedPath = requestPath;
                                var tempReplacedPath = replacedPath; // this is to handle more than one parameter on the url

                                var validationErrors = [];
                                $('#validationererrors').hide();
                                $('#validationerrors ul').empty();

                                $("#pathparameters input").each(function () {
                                    $('#' + this.id).parent().removeClass('has-error');

                                    if ($(this).val()) {
                                        tempReplacedPath = replacedPath.replace("{" + this.name + "}", $(this).val());
                                        replacedPath = tempReplacedPath;
                                    } else {
                                        validationErrors.push(this.name + ' must not be empty');
                                        $('#' + this.id).parent().addClass('has-error');
                                    }
                                });

                                var queryParametersMap = {};
                                $("#queryparameters input").each(function () {
                                    $('#' + this.id).parent().removeClass('has-error');

                                    if ($(this).val()) {
                                        queryParametersMap[this.name] = $(this).val();
                                    } else {
                                        if ($(this).attr("required")) {
                                            validationErrors.push(this.name + ' must not be empty');
                                            $('#' + this.id).parent().addClass('has-error');
                                        }
                                    }
                                });

                                var encodedQueryParameters = $.param(queryParametersMap);
                                if (encodedQueryParameters) {
                                    replacedPath = replacedPath + "?" + encodedQueryParameters;
                                }

                                if (validationErrors.length > 0) {
                                    for (var k = 0; k < validationErrors.length; k++) {
                                        $('#validationerrors ul').append($('<li/>').text(validationErrors[k]));

                                    }
                                    $('#validationerrors').show();
                                    validationErrors = [];
                                    return;
                                }

                                $('#testButton').button('loading');

                                var res = $.ajax({
                                    url: model.basePath + replacedPath,
                                    type: requestMethod,
                                    data: $("#inputJson").val(),
                                    headers: headers,
                                    contentType: $("#consumes input:checked").val(),
                                    success: function (data) {
                                        printResponse(data, res, this.url);
                                    },
                                    error: function (data) {
                                        printResponse(data, res, this.url);
                                    }
                                });

                            });

                        });
                    });
                } else {
                    $("#content").parent().removeClass();
                    $("#content").parent().addClass("col-md-9");
                }
            }

            function buildFromJSONDoc(data) {
                model = data;
                var main = Handlebars.compile($("#main").html());
                var mainHTML = main(data);
                $("#maindiv").html(mainHTML);

                if (data.global) {
                    if (data.global.sections) {
                        var global = Handlebars.compile($("#global").html());
                        var globalHTML = global(data);
                        $("#globaldiv").html(globalHTML);
                        $("#globaldiv").addClass("panel panel-default");

                        $("#_overview").click(function () {
                            var overview = Handlebars.compile($("#overviewdiv").html());
                            var overviewHTML = overview(data.global);
                            loadGlobalContent(overviewHTML, data.playgroundEnabled);

                            $("#content div[rel='overviewpanel'] table").each(function () {
                                $(this).removeClass();
                                $(this).addClass("table table-condensed table-bordered");
                            });

                        });
                    }

                    if (data.global.changelogset.changelogs) {
                        $("#_changelogs").click(function () {
                            var changelogs = Handlebars.compile($("#changelogsdiv").html());
                            var changelogsHTML = changelogs(data.global.changelogset);
                            loadGlobalContent(changelogsHTML, data.playgroundEnabled);
                        });
                    }
                    if (data.global.migrationset.migrations) {
                        $("#_migrations").click(function () {
                            var migrations = Handlebars.compile($("#migrationsdiv").html());
                            var migrationsHTML = migrations(data.global.migrationset);
                            loadGlobalContent(migrationsHTML, data.playgroundEnabled);
                        });
                    }
                }

                if (data.apis) {
                    var apis = Handlebars.compile($("#apis").html());
                    var apisHTML = apis(data);
                    $("#apidiv").html(apisHTML);
                    $("#apidiv").addClass("panel panel-default");

                    if (data.global) {
                        // if global documentation is found, then it needs to be expanded by default, this is why the "in" class is removed from the api panel.
                        $("#_panel-apis").removeClass("in");
                    }

                    // this builds an plain array out of the apis map, that makes selecting with jlinq much easier
                    var plainApis = [];
                    $.each(data.apis, function (i, v) {
                        $.each(v, function (j, p) {
                            plainApis.push(p);
                        });
                    });

                    $("#apidiv a[rel='api']").each(function () {
                        $(this).click(function () {
                            var api = jlinq.from(plainApis).equals("jsondocId", this.id).first();
                            var methods = Handlebars.compile($("#methods").html());
                            var methodsHTML = methods(api);
                            $("#content").html(methodsHTML);
                            $("#content").show();
                            if (api.supportedversions) {
                                $("#apiSupportedVersions").text("Since version: " + api.supportedversions.since);
                                if (api.supportedversions.until) {
                                    $("#apiSupportedVersions").text($("#apiSupportedVersions").text() + " - Until version: " + api.supportedversions.until);
                                }
                            }
                            $("#testContent").hide();

                            buildMethodsContent(api.methods, data.playgroundEnabled);
                        });
                    });
                }

                if (data.objects) {
                    var objects = Handlebars.compile($("#objects").html());
                    var objectsHTML = objects(data);
                    $("#objectdiv").html(objectsHTML);
                    $("#objectdiv").addClass("panel panel-default");

                    // this builds an plain array out of the objects map, that makes selecting with jlinq much easier
                    var plainObjects = [];
                    $.each(data.objects, function (i, v) {
                        $.each(v, function (j, p) {
                            plainObjects.push(p);
                        });
                    });

                    $("#objectdiv a[rel='object']").each(function () {
                        $(this).click(function () {
                            var o = jlinq.from(plainObjects).equals("jsondocId", this.id).first();
                            buildObjectContent(o, data.playgroundEnabled);
                        });
                    });
                }

                if (data.flows) {
                    var flows = Handlebars.compile($("#flows").html());
                    var flowsHTML = flows(data);
                    $("#flowdiv").html(flowsHTML);
                    $("#flowdiv").addClass("panel panel-default");

                    // this builds an plain array out of the flows map, that makes selecting with jlinq much easier
                    var plainFlows = [];
                    $.each(data.flows, function (i, v) {
                        $.each(v, function (j, p) {
                            plainFlows.push(p);
                        });
                    });

                    $("#flowdiv a[rel='flow']").each(function () {
                        $(this).click(function () {
                            var flow = jlinq.from(plainFlows).equals("jsondocId", this.id).first();
                            var methods = Handlebars.compile($("#methods").html());
                            var methodsHTML = methods(flow);
                            $("#content").html(methodsHTML);
                            $("#content").show();
                            $("#testContent").hide();

                            buildMethodsContent(flow.methods, data.playgroundEnabled);
                        });
                    });
                }

                // display sidebar
                $('#maindiv').show();
                $('#side-accordion').show();
            }

            function loadGlobalContent(contentHTML, playgroundEnabled) {
                $("#content").html(contentHTML);
                $("#content").show();
                $("#testContent").hide();

                $("#content").parent().removeClass();
                $("#content").parent().addClass("col-md-9");
            }

            function resetLayoutClasses() {
                $("#content").parent().removeClass();
                $("#content").parent().addClass("col-md-5");
            }

            function fetchdoc(jsondocurl) {
                $.ajax({
                    url: jsondocurl,
                    type: 'GET',
                    dataType: 'json',
                    contentType: "application/json; charset=utf-8",
                    success: function (data) {
                        buildFromJSONDoc(data);
                    },
                    error: function (msg) {
                        alert("Error " + msg);
                    }
                });
            }
        </script>

    </body>
</html>