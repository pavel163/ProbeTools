var app = angular.module('app', ['lumx', 'ngRoute']);

app.config(["$routeProvider", "$locationProvider", function ($routeProvider, $locationProvider) {
    $locationProvider.html5Mode({
        enabled: true,
        requireBase: false
    });

    $routeProvider
        .when("/", {
            action: "templates"
        })
        .when("/templates", {
            action: "templates"
        })
        .when("/variables", {
            action: "variables"
        })
        .when("/containers", {
            action: "containers"
        })
        .when("/rules", {
            action: "rules"
        })
        .when("/styles", {
            action: "styles"
        })
        .when("/scripts", {
            action: "scripts"
        })
        .when("/http", {
            action: "http"
        })
        .when("/preferences", {
                    action: "http"
                })
        .otherwise({
            redirectTo: "/templates"
        });
}]);

app.controller('AppController',
    function ($http, $scope, $location, $route, $rootScope, LxDialogService, LxProgressService, LxNotificationService) {

        $scope.active = $scope.before = "templates";
        $scope.json = "";

        var formatJson = function(){
            editor.session.setValue(JSON.stringify(JSON.parse(editor.session.getValue()), null, 4));
        }

        $scope.variables = {
            "var": "",
            executeExpression: function () {
                LxProgressService.linear.show('#00BFA5', '#progress');
                $scope.json = "";
                if (typeof $scope.variables.var === "string" && $scope.variables.var.length === 0) {
                    $scope.variables.var = undefined;
                }
                $http({"url": '/variable', "params": {var: $scope.variables.var}}).
                    success(function (data, status, headers, config) {
                        $scope.json = data;
                        LxProgressService.linear.hide();
                    }).
                    error(function (data, status, headers, config) {
                        LxProgressService.linear.hide();
                        LxNotificationService.error('При выполнении выражения, произошла ошибка');
                    });
            },
            reset: function () {
                $scope.variables.var = undefined;
                $scope.variables.executeExpression();
            },

            init: function () {
                editor.getSession().setMode("ace/mode/json");
            }
        };

        $scope.templates = {
            loading: false,
            list: [],
            selected: {},
            forcedSelectedPosition: false,
            onChange: function (data) {
                $scope.json = "";
                LxProgressService.linear.show('#00BFA5', '#progress');
                $http({"url": '/template', "params": {"id": data.newValue.id}}).
                    success(function (data, status, headers, config) {
                        LxProgressService.linear.hide();
                        if ($scope.active !== "templates") return;
                        $scope.json = data;
                    }).
                    error(function (data, status, headers, config) {
                        LxProgressService.linear.hide();
                        LxNotificationService.error('При получении шаблона, произошла ошибка');
                    });
            },

            toSelection: function (data, callback) {
                if ($scope.templates.forcedSelectedPosition !== false) {
                    var selectedModel = $scope.templates.list[$scope.templates.forcedSelectedPosition];
                    selectedModel.title = selectedModel.title ? selectedModel.title : selectedModel.name
                    callback(selectedModel);
                    $scope.templates.forcedSelectedPosition = false;
                    return;
                }

                LxProgressService.linear.show('#00BFA5', '#progress');
                $http.get("/templates/list").
                    success(function (response) {
                        LxProgressService.linear.hide();
                        for (var i = 0; i < response.length; i++) {
                            response[i].title = response[i].title ? response[i].title : response[i].name
                        }
                        $scope.templates.list = response
                        callback(response[0]);
                    }).
                    error(function () {
                        LxProgressService.linear.hide();
                        callback();
                    });
            },
            save: function () {
                var val = editor.session.getValue();
                if (val != null && val.length > 0) {
                    try {
                        var template = JSON.parse(val);
                    } catch (error) {
                        LxNotificationService.error("Шаблон не может быть сохранён, в JSON обнаружена ошибка");
                        return;
                    }
                } else {
                    LxNotificationService.error("Нельзя сохранить пустой шаблон");
                    return;
                }

                LxProgressService.linear.show('#00BFA5', '#progress');
                $http.put('/template', template).
                    success(function (data, status, headers, config) {
                        LxProgressService.linear.hide();
                        LxNotificationService.success('Шаблон сохранён');
//                        formatJson();
                    }).
                    error(function (data, status, headers, config) {
                        LxProgressService.linear.hide();
                        LxNotificationService.error('При сохранении шаблона, произошла ошибка');
                    });
            },
            downloadTemplate: function () {
                var val = editor.session.getValue();
                if (val != null && val.length > 0) {
                    try {
                        var template = JSON.parse(val);
                    } catch (error) {
                        LxNotificationService.error("Шаблон не может быть сохранён, в JSON обнаружена ошибка");
                        return;
                    }

                } else {
                    LxNotificationService.error("Нельзя сохранить пустой шаблон");
                    return;
                }

                LxProgressService.linear.show('#00BFA5', '#progress');
                $http.post('/template/download', template).
                    success(function (data, status, headers, config) {
                        LxProgressService.linear.hide();
                        var element = angular.element('<a/>');
                        element.attr({
                            href: 'data:text/json;charset=utf-8,' + encodeURIComponent(JSON.stringify(data, null, 4)),
                            target: '_blank',
                            download: data['name'] + '.json'
                        })[0].click();
                    }).
                    error(function (data, status, headers, config) {
                        LxProgressService.linear.hide();
                        LxNotificationService.error('При сохранении шаблона, произошла ошибка');
                    });
            },
            downloadXml: function () {
                var val = editor.session.getValue();
                if (val != null && val.length > 0) {
                    try {
                        var template = JSON.parse(val);
                    } catch (error) {
                        LxNotificationService.error("Шаблон не может быть сохранён, в JSON обнаружена ошибка");
                        return;
                    }

                } else {
                    LxNotificationService.error("Нельзя сохранить пустой шаблон");
                    return;
                }

                LxProgressService.linear.show('#00BFA5', '#progress');
                $http.post('/template/downloadXml', template).
                    success(function (data, status, headers, config) {
                        LxProgressService.linear.hide();
                        var element = angular.element('<a/>');
                        element.attr({
                            href: 'data:text/xml;charset=utf-8,' + encodeURIComponent(formatXml(data)),
                            target: '_blank',
                            download: template['name'] + '.xml'
                        })[0].click();
                    }).
                    error(function (data, status, headers, config) {
                        LxProgressService.linear.hide();
                        LxNotificationService.error('При сохранении шаблона, произошла ошибка');
                    });
            },
            reset: function () {
                $scope.templates.list = [];
                $scope.templates.selected = {};
                $scope.templates.toSelection({}, function (data) {
                    if ($scope.active !== "templates") return;
                    $scope.templates.onChange({newValue: data});
                });
            },

            init: function () {
                editor.getSession().setMode("ace/mode/json");
            }
        };

        $scope.containers = {
            "container": '',
            "from": '',
            "tumbler": true,
            onSwitch: function () {
                $scope.containers.from = '';
            },
            findContainer: function () {
                $scope.json = "";
                LxProgressService.linear.show('#00BFA5', '#progress');
                var container = undefined;
                if (typeof $scope.containers.container === "string" && $scope.containers.container.length > 0)
                    container = $scope.containers.container;

                var from = undefined;
                if (typeof $scope.containers.from === "string" && $scope.containers.from.length > 0)
                    from = $scope.containers.from;

                $http({"url": '/containers/data', "params": {"container": container, "from": from}}).
                    success(function (data, status, headers, config) {
                        $scope.json = data;
                        LxProgressService.linear.hide();
                    }).
                    error(function (data, status, headers, config) {
                        LxProgressService.linear.hide();
                        LxNotificationService.error('При получении значений контейнера, произошла ошибка');
                    });
            },
            reset: function () {
                $scope.containers.container = '';
                $scope.containers.from = '';
                $scope.containers.tumbler = true;
                $scope.containers.findContainer();
            },

            init: function () {
                editor.getSession().setMode("ace/mode/json");
            }
        };

        $scope.scripts = {
            loading: false,
            list: [],
            selected: {},
            forcedSelectedPosition: false,
            onChange: function (data) {
                if ($scope.active !== "scripts") return;
                $scope.json = data.newValue.source;
                $scope.scripts.selected = data.newValue;
            },

            toSelection: function (data, callback) {
                if ($scope.scripts.forcedSelectedPosition !== false) {
                    var selectedModel = $scope.scripts.list[$scope.scripts.forcedSelectedPosition];
                    selectedModel.title = selectedModel.title ? selectedModel.title : selectedModel.name
                    callback(selectedModel);
                    $scope.scripts.forcedSelectedPosition = false;
                    return;
                }

                LxProgressService.linear.show('#00BFA5', '#progress');
                $http.get("/scripts/list").
                    success(function (response) {
                        LxProgressService.linear.hide();
                        for (var i = 0; i < response.length; i++) {
                            response[i].title = response[i].title ? response[i].title : response[i].name
                        }
                        $scope.scripts.list = response
                        callback(response[0]);
                    }).
                    error(function () {
                        LxProgressService.linear.hide();
                        callback();
                    });
            },

            save: function () {
                var val = editor.session.getValue();
                if (val === null || val.length === 0) {
                    LxNotificationService.error("Нельзя сохранить пустой скрипт");
                    return;
                } else if (_.findWhere(editor.getSession().getAnnotations(), {"type": "error"})) {
                    LxNotificationService.error("Скрипт не может быть сохранён, обнаружена ошибка");
                    return;
                }

                LxProgressService.linear.show('#00BFA5', '#progress');
                $scope.scripts.selected.source = val;
                $http.put('/script', $scope.scripts.selected).
                    success(function (data, status, headers, config) {
                        LxProgressService.linear.hide();
                        LxNotificationService.success('Скрипт сохранён');
                    }).
                    error(function (data, status, headers, config) {
                        LxProgressService.linear.hide();
                        LxNotificationService.error('При сохранении скрипта, произошла ошибка');
                    });
            },

            download: function () {
                var val = editor.session.getValue();
                var element = angular.element('<a/>');
                element.attr({
                    href: 'data:text/json;charset=utf-8,' + encodeURIComponent(val),
                    target: '_blank',
                    download: $scope.scripts.selected.type + "_" + $scope.scripts.selected.name + '.js'
                })[0].click();
            },

            reset: function () {
                $scope.scripts.list = [];
                $scope.scripts.selected = {};
                $scope.scripts.toSelection({}, function (data) {
                    if ($scope.active !== "scripts") return;
                    $scope.scripts.onChange({newValue: data});
                });
            },

            init: function () {
                editor.getSession().setMode("ace/mode/javascript");
            }
        };

        $scope.rules = {
            loading: false,
            list: [],
            selected: {},
            forcedSelectedPosition: false,
            onChange: function (data) {
                $scope.json = "";
                $scope.rules.selected = data.newValue;
                LxProgressService.linear.show('#00BFA5', '#progress');
                $http({"url": '/rule', "params": {"id": data.newValue.id, "type": data.newValue.type}}).
                    success(function (data, status, headers, config) {
                        LxProgressService.linear.hide();
                        if ($scope.active !== "rules") return;
                        $scope.json = data;
                    }).
                    error(function (data, status, headers, config) {
                        LxProgressService.linear.hide();
                        LxNotificationService.error('При получении правила, произошла ошибка');
                    });
            },

            toSelection: function (data, callback) {
                if ($scope.rules.forcedSelectedPosition !== false) {
                    var selectedModel = $scope.rules.list[$scope.rules.forcedSelectedPosition];
                    selectedModel.title = selectedModel.title ? selectedModel.title : selectedModel.name;
                    selectedModel.title = selectedModel.comment ? selectedModel.title + " " + selectedModel.comment : selectedModel.title;
                    callback(selectedModel);
                    $scope.rules.forcedSelectedPosition = false;
                    return;
                }

                LxProgressService.linear.show('#00BFA5', '#progress');
                $http.get("/rules/list").
                    success(function (response) {
                        LxProgressService.linear.hide();
                        for (var i = 0; i < response.length; i++) {
                            response[i].title = response[i].title ? response[i].title : response[i].name;
                            response[i].title = response[i].comment ? response[i].title + " " + response[i].comment : response[i].title;
                        }
                        $scope.rules.list = response
                        callback(response[0]);
                    }).
                    error(function () {
                        LxProgressService.linear.hide();
                        callback();
                    });
            },

            save: function () {
                var val = editor.session.getValue();
                if (val != null && val.length > 0) {
                    try {
                        var rule = JSON.parse(val);
                    } catch (error) {
                        LxNotificationService.error("Правило не может быть сохранён, в JSON обнаружена ошибка");
                        return;
                    }
                } else {
                    LxNotificationService.error("Нельзя сохранить пустое правило");
                    return;
                }

                LxProgressService.linear.show('#00BFA5', '#progress');
                rule.ruleType = $scope.rules.selected.type;

                if($scope.rules.selected._id !== undefined)
                    rule._id = $scope.rules.selected._id;

                $http.put('/rule', rule).
                    success(function (data, status, headers, config) {
                        LxProgressService.linear.hide();
                        LxNotificationService.success('Правило сохранёно');
                        formatJson();
                    }).
                    error(function (data, status, headers, config) {
                        LxProgressService.linear.hide();
                        LxNotificationService.error('При сохранении правила, произошла ошибка');
                    });
            },

            download: function () {
                var val = editor.session.getValue();
                var element = angular.element('<a/>');
                element.attr({
                    href: 'data:text/json;charset=utf-8,' + encodeURIComponent(val),
                    target: '_blank',
                    download: $scope.rules.selected.name + '_' + $scope.rules.selected.type + '.json'
                })[0].click();
            },

            reset: function () {
                $scope.rules.list = [];
                $scope.rules.selected = {};
                $scope.rules.toSelection({}, function (data) {
                    if ($scope.active !== "rules") return;
                    $scope.rules.onChange({newValue: data});
                });
            },

            init: function () {
                editor.getSession().setMode("ace/mode/json");
            }
        };


        $scope.styles = {
            loading: false,
            list: [],
            selected: {},
            forcedSelectedPosition: false,
            onChange: function (data) {
                $scope.json = data.newValue.data;
                $scope.styles.selected = data.newValue;
            },

            toSelection: function (data, callback) {
                if ($scope.styles.forcedSelectedPosition !== false) {
                    var selectedModel = $scope.styles.list[$scope.styles.forcedSelectedPosition];
                    callback(selectedModel);
                    $scope.styles.forcedSelectedPosition = false;
                    return;
                }

                LxProgressService.linear.show('#00BFA5', '#progress');
                $http.get("/styles/list").
                    success(function (response) {
                        LxProgressService.linear.hide();
                        $scope.styles.list = response
                        callback(response[0]);
                    }).
                    error(function () {
                        LxProgressService.linear.hide();
                        callback();
                    });
            },

            save: function () {
                var val = editor.session.getValue();
                if (val != null && val.length > 0) {
                    try {
                        var style = JSON.parse(val);
                    } catch (error) {
                        LxNotificationService.error("Стиль не может быть сохранён, в JSON обнаружена ошибка");
                        return;
                    }
                } else {
                    LxNotificationService.error("Нельзя сохранить пустой стиль");
                    return;
                }

                LxProgressService.linear.show('#00BFA5', '#progress');
                $scope.styles.selected.data = style;
                $http.put('/style', $scope.styles.selected).
                    success(function (data, status, headers, config) {
                        LxProgressService.linear.hide();
                        LxNotificationService.success('Стиль сохранён');
                        formatJson();
                    }).
                    error(function (data, status, headers, config) {
                        LxProgressService.linear.hide();
                        LxNotificationService.error('При сохранении стиля, произошла ошибка');
                    });
            },

            download: function () {
                var val = editor.session.getValue();
                var element = angular.element('<a/>');
                element.attr({
                    href: 'data:text/json;charset=utf-8,' + encodeURIComponent(val),
                    target: '_blank',
                    download: $scope.styles.selected.name + '_' + $scope.styles.selected.type + '.json'
                })[0].click();
            },

            reset: function () {
                $scope.styles.list = [];
                $scope.styles.selected = {};
                $scope.styles.toSelection({}, function (data) {
                    if ($scope.active !== "styles") return;
                    $scope.styles.onChange({newValue: data});
                });
            },

            init: function () {
                editor.getSession().setMode("ace/mode/json");
            }
        };

        $scope.checkHome = function () {
            return $location.path() === '/';
        };

        ace.require("ace/ext/language_tools");
        editor = ace.edit('editor');
        editor.getSession().setMode("ace/mode/json");
        editor.setShowPrintMargin(false);
        editor.setOptions({
            scrollPastEnd: true,
            enableBasicAutocompletion: true,
            enableSnippets: true
        });
        editor.setHighlightActiveLine(false);
        editor.commands.addCommand({
            name: "format",
            bindKey: {win: "Ctrl-L", mac: "Ctrl-L"},
            exec: function(editor) {
                formatJson();
            }
        });
        editor.commands.addCommand({
            name: "save",
            bindKey: {win: "Ctrl-S", mac: "Ctrl-S"},
            exec: function(editor) {
                $scope[$scope.active].save();
            }
        });

        $rootScope.$on('$locationChangeSuccess', function (event, $currentRoute, $previousRoute) {
            $scope.before = $scope.active;
            $scope.active = $route.current.action || "templates";
        });

        $scope.$watch('active', function (newValue, oldValue) {
            $scope.json = ""
            $scope[newValue].reset();
            $scope[newValue].init();
        });

        $scope.$watch('json', function (newValue, oldValue) {
            if (typeof newValue === "string")
                editor.session.setValue(newValue);
            else
                editor.session.setValue(JSON.stringify(newValue, null, 4));
        });

        $scope.newScript = {};
        $scope.newRule = {};
        $scope.newStyle = {};
        $scope.newTemplate = {};

        $scope.opendDialog = function (dialogId) {
            LxDialogService.open(dialogId);
        };

        $scope.saveDialog = function (dialogId) {
            if(dialogId === "create_script") {
                console.log($scope.newScript)
                $http.post('/script', $scope.newScript).
                    success(function (data, status, headers, config) {
                        LxDialogService.close(dialogId);
                        LxNotificationService.info('Скрипт создан.');
                        $scope.scripts.list.push(data);
                        $scope.scripts.forcedSelectedPosition = $scope.scripts.list.length - 1;
                        $scope.scripts.selected = data;
                        $scope.json = data.source;
                    }).
                    error(function (data, status, headers, config) {
                        LxNotificationService.info('Ошибка. Скрипт не создан.');
                    });
                $scope.newScript = {};
            }else if (dialogId === "create_rules"){
                console.log($scope.newRule)
                $http.post('/rule', $scope.newRule).
                    success(function (data, status, headers, config) {
                        $scope.newRule._id = data._id;
                        $scope.rules.list.push($scope.newRule);
                        $scope.rules.forcedSelectedPosition = $scope.rules.list.length - 1;
                        $scope.rules.selected = $scope.newRule
                        $scope.newRule.id = data.id;
                        if ($scope.rules.selected.type === "view"){
                            $scope.json = JSON.parse(data);
                        }else{
                            $scope.newRule.id = data;
                        }

                        $scope.newRule = {};

                        LxDialogService.close(dialogId);
                        LxNotificationService.info('Правило создано.');
                    }).
                    error(function (data, status, headers, config) {
                        LxNotificationService.info('Ошибка. Правило не создано.');
                    });
            }else if(dialogId ==="create_styles"){
                console.log($scope.newStyle)
                $http.post('/style', $scope.newStyle).
                    success(function (data, status, headers, config) {

                        $scope.json = data.data;
                        $scope.styles.list.push(data);
                        $scope.styles.forcedSelectedPosition = $scope.styles.list.length - 1;
                        $scope.styles.selected = data;
                        $scope.newStyle = {};

                        LxDialogService.close(dialogId);
                        LxNotificationService.info('Стиль создан.');
                    }).
                    error(function (data, status, headers, config) {
                        LxNotificationService.info('Ошибка. стиль не создан.');
                    });

            }else if(dialogId ==="create_template"){
                console.log($scope.newTemplate)
                $http.post('/template', $scope.newTemplate).
                    success(function (data, status, headers, config) {
                        $scope.newTemplate.id = data.id;
                        $scope.newTemplate.title = data.title;
                        $scope.newTemplate.name = data.params.name;
                        if(data.params.class === "doc"){
                            $scope.newTemplate.type = "doc";
                        }else{
                            $scope.newTemplate.type = "view";
                        }
                        $scope.templates.list.push($scope.newTemplate);
                        $scope.json = data.params;
                        $scope.templates.forcedSelectedPosition = $scope.templates.list.length - 1;
                        $scope.templates.selected = $scope.newTemplate;
                        $scope.newTemplate = {};
                        LxDialogService.close(dialogId);
                        LxNotificationService.info('Шаблон создан.');
                    }).
                    error(function (data, status, headers, config) {
                        LxNotificationService.info('Ошибка. шаблон не создан.');
                    });

            }
        };

        $scope.closingDialog = function () {
            console.log($scope.newScript)
            $scope.newScript = {};
        };

        window.onresize = function () {
            $("#editor").height(window.innerHeight - $("#editor").offset().top - 30);
            $(".card").height(window.innerHeight - 20);
            editor.resize()
        }
        window.onresize()
    })
;
