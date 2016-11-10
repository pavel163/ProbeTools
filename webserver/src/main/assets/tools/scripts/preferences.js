var app = angular.module('app', ['lumx', 'ngRoute']);

app.config(["$routeProvider", "$locationProvider", function ($routeProvider, $locationProvider) {
    $locationProvider.html5Mode({
        enabled: true,
        requireBase: false
    });

    $routeProvider
        .when("/preferences", {
            action: "preferences"
        })

}]);

app.controller('AppController',
    function ($http, $scope, $location, $route, $rootScope, LxDialogService, LxProgressService, LxNotificationService) {

        $scope.active = $scope.before = "preferences";

        $scope.preferences = {
            loading: false,
            list: [],
            onChange: function (data) {
                LxProgressService.linear.show('#00BFA5', '#progress');
                $http({"url": '/template', "params": {"id": data.newValue.id}}).
                    success(function (data, status, headers, config) {
                        LxProgressService.linear.hide();
                        if ($scope.active !== "preferences") return;
                    }).
                    error(function (data, status, headers, config) {
                        LxProgressService.linear.hide();
                        LxNotificationService.error('При получении шаблона, произошла ошибка');
                    });
            },

            loadAllPreferences: function (data, callback) {
                LxProgressService.linear.show('#00BFA5', '#progress');
                $http.get("/preferences/loadAll").
                    success(function (response) {
                        LxProgressService.linear.hide();
//                        for (var i = 0; i < response.length; i++) {
//                            response[i].title = response[i].title ? response[i].title : response[i].name
//                        }
//                        $scope.preferences.list = response

//                        $scope.nameParam = document.createElement("li")
//                        $scope.nameParam.className = "ist-row list-row--has-separator"


                        $scope.nameParam = document.createElement("li")
                        $scope.nameParam.className = 'list-row list-row--has-separator'
                        $scope.nameParam.innerHTML = '<lx-text-field lx-label="First name"><input type="text" ng-model="vm.textFields.floating.firstName"></lx-text-field>'
                        angular.element(document.getElementById('name_param')).append($scope.nameParam);
                        callback(response[0]);
                    }).
                    error(function () {
                        LxProgressService.linear.hide();
                        callback();
                    });
            },
            reset: function () {
                $scope.preferences.list = [];
                $scope.preferences.loadAllPreferences({}, function (data) {
                    if ($scope.active !== "preferences") return;
                    $scope.preferences.onChange({newValue: data});
                });
            },

            init: function () {}
        };

        $rootScope.$on('$locationChangeSuccess', function (event, $currentRoute, $previousRoute) {
            $scope.before = $scope.active;
            $scope.active = $route.current.action || "preferences";
        });

        $scope.$watch('active', function (newValue, oldValue) {
            $scope[newValue].reset();
            $scope[newValue].init();
        });
    })
;
