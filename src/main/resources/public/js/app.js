var app = angular.module('app', [ 'angular.atmosphere', 'ngSanitize', 'ui.bootstrap', 'scrollable-table', 'app.services' ]);

/**
 * Main controller.
 */
app.controller('mainCtrl', function($scope, $timeout, atmosphereService, globalService, projectDataService) {
	$scope.selected = undefined;
	$scope.globalEnabled = undefined;
	$scope.connection = {
			transport : undefined,
			content : 'Connecting...',
			connected : false
	}

	$scope.enableGlobal = function(globalEnabled) {
		console.log('enableGlobal', globalEnabled);
		globalService.update(globalEnabled);
	};
	
	$scope.resetSelected = function() {
		console.log('resetSelected');

		$scope.selected = undefined;
	};
	
	$scope.setSelected = function(proj) {
		console.log('setSelected', proj);

		$scope.selected = proj;
	};

	$scope.modifyProject = function(proj) {
		console.log('modifyProject', proj);
	};
	
	$scope.changeProjectYear = function(selected, year) {
		console.log('changeProjectYear', selected, year);
	};
	
	$scope.sendUpdate = function() {
		console.log('sendUpdate');
	}

	$scope.projectYears = [ {
		code : 0,
		value : 2014
	}, {
		code : 1,
		value : 2015
	} ];

	$scope.visibleProjects = [ {
		facility : "Atlanta",
		code : "C-RD34",
		cost : 540000,
		conditionRating : 52,
		extent : 100,
		planYear : $scope.projectYears[0]
	}, {
		facility : "Atlanta",
		code : "C-RD34",
		cost : 540000,
		conditionRating : 52,
		extent : 100,
		planYear : $scope.projectYears[0]
	}, {
		facility : "Atlanta",
		code : "C-RD34",
		cost : 540000,
		conditionRating : 52,
		extent : 100,
		planYear : $scope.projectYears[0]
	}];
	var onGlobalStatus = function(globalEnabled) {
		console.log('onGlobalStatus', globalEnabled);
		$timeout(function() {
			$scope.globalEnabled = globalEnabled;
		}, 0);
	};
	
	var onProjectsReady = function(projects) {
		console.log('onProjectsReady', projects);
		$timeout(function() {
			$scope.visibleProjects = projects;
		}, 0);
	};
	
	var socket;
	var request = {
			url : '/live/update',
			contentType : 'application/json',
			logLevel : 'debug',
			transport : 'websocket',
			fallbackTransport : 'long-polling',
			trackMessageLength : true,
			reconnectInterval : 5000,
			enableXDR : true,
			timeout : -1
	};
	
	request.onOpen = function(response) {
		$scope.connection = {
				transport : response.transport,
				content : 'Connected',
				connected : true
		};
		globalService.getStatus(onGlobalStatus);
		projectDataService.getProjects(onProjectsReady);
	}
	request.onClientTimeout = function(response) {
		$scope.connection = {
				transport : response.transport,
				content : 'Timeout occured. Reconnecting...',
				connected : false
		};
		setTimeout(function() {
			socket = atomsphereService.subscribe(request);
		}, request.reconnectInterval);
	};
	request.onReopen = function(response) {
		request.onOpen(response);
	};
	request.onReconnect = function(request, response) {
		$scope.connection = {
				transport : undefined,
				content : 'Connection lost. Reconnecting...',
				connected : false
		};
	};
	request.onMessage = function(response) {
		console.log('onMessage', response);
		var message = atmosphere.util.parseJSON(response.responseBody);
		
		if (message.type == 'STATUS') {
			onGlobalStatus(message.value);
		} else if (message.type == 'ADD') {
			$scope.visibleProjects[$scope.visibleProjects.length] = message.value;
		} else if (message.type == 'REMOVE') {
			var index = -1;
			var id = message.value.id;
			
			for (i = 0; i < $scope.visibleProjects.length; i++) {
				if ($scope.visibleProjects[i].id == id) {
					index = i;
					break;
				}
			}
			if (index > -1) {
				$scope.visibleProjects.splice(index, 1);
				if ($scope.selected.id == id) {
					$scope.resetSelected();
				}
			}
		} else {
			console.log('Unhandled message', message);
		}
	};
	request.onClose = function(error, request) {
		console.log('onClose', error, request);
		$scope.connection = {
				transport : undefined,
				content : 'Connection closed',
				connected : false
		};
	};
	request.onError = function(error, request) {
		console.log('onError', error, request);
		$scope.connection = {
				transport : undefined,
				content : 'Connection error',
				connected : false
		};
	};
	request.onTransportFailure = function(error, request) {
		console.log('onTransportFailure', error, request);
		$scope.connection = {
				transport : undefined,
				content : 'Executing fallback...',
				connected : false
		};
	};
	socket = atmosphereService.subscribe(request);
});