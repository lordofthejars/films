var app = angular.module('hello', [ 'ngRoute' ]).config(function($routeProvider, $httpProvider) {
	$routeProvider.when('/home', {
		templateUrl : 'home.html',
		controller : 'home'
	}).when('/login', {
		templateUrl : 'login.html',
		controller : 'navigation'
	}).when('/', {
		templateUrl : 'login.html',
		controller : 'navigation'
	}).otherwise('/');

	$httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';

}).controller(
		'navigation',

		function($rootScope, $scope, $http, $location, $route, loggedToken) {

			$scope.tab = function(route) {
				return $route.current && route === $route.current.controller;
			};

			var authenticate = function(credentials, callback) {

				$http.post('rest/app', {
					username:credentials.username, password:credentials.password
				}).success(function(data) {
					if (data.token) {
						$rootScope.authenticated = true;
					} else {
						$rootScope.authenticated = false;
					}
					callback && callback($rootScope.authenticated, data);
				}).error(function() {
					$rootScope.authenticated = false;
					callback && callback(false);
				});

			}

			$scope.credentials = {};
			$scope.login = function() {
				authenticate($scope.credentials, function(authenticated, data) {
					if (authenticated) {
						console.log("Login succeeded")
						$http.defaults.headers.common['x-access-token'] = data.token;
						loggedToken.setToken(data.token)
						$rootScope.user = data.user.username
						$location.path("/home");
						$scope.error = false;
						$rootScope.authenticated = true;
					} else {
						console.log("Login failed")
						$http.defaults.headers.common['x-access-token'] = ""
						$location.path("/login");
						$scope.error = true;
						$rootScope.authenticated = false;
					}
				})
			};

		}).controller('home', function($scope, $http, $location, loggedToken) {
			$http.get('rest/app', {
				params: {
					xaccesstoken: loggedToken.getToken()
				}
			}).success(function(data) {
				$scope.movies = data;
				var absUrl = $location.absUrl();
				var host = absUrl.substring(0, absUrl.indexOf('#')-1)
				$scope.url = host + '/rest/app?xaccesstoken='+ loggedToken.getToken()
			}).error(function(data){
				$scope.error = true;
			})
		});

app.service('loggedToken', function () {
	var token = "";
	return {
		getToken: function () {
		return token;
		},
		setToken: function (value) {
		token = value;
		}
	};
});
