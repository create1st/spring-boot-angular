var services = angular.module('app.services', []);

services.service('globalService', function($http) {
	var update = function(globalEnabled) {
		console.log('update', globalEnabled);
		$http.post('/global/status/update', {
			value : globalEnabled
		}).success(function(data) {
			console.log('success', data);
		}).error(function(data, status, headers, config) {
			throw "Update failed with error: " + status;
		});
	};
	var getStatus = function(callback) {
		console.log('getStatus', callback);
		$http.get('/global/status/get').success(function(data) {
			console.log('success', data);
			(callback || angular.noop)(data.value);
		}).error(function(data, status, headers, config) {
			throw "getStatus failed with error: " + status;
		});
	};

	return {
		getStatus : getStatus,
		update : update
	};
});
services.service('projectDataService', function() {
	var getProjects = function(callback) {
		console.log('getProjects', callback);
	};

	return {
		getProjects : getProjects
	}
});
services.filter('yearFormat', function() {
	return function(year) {
		return year.value;
	};
});
services.directive('code', function() {
	console.log('code');
	return {
		restrict : 'A',
		require : 'ngModel',
		link : function(scope, element, attrs, ngModel) {
			function setErrorState(element, errorState) {
				console.log('setErrorState', element, errorState);
				
				var parent = element.parent('div.form-group');
				var infoIcon = element.siblings('span.form-control-feedback');

				if (parent) {
					if (errorState) {
						parent.addClass('has-error');
						parent.addClass('has-feedback');
					} else {
						parent.removeClass('has-error');
					}
				}
				if (infoIcon) {
					if (errorState) {
						infoIcon.addClass('glyphicon-remove');
					} else {
						infoIcon.removeClass('glyphicon-remove');
					}
				}
			}
			if (ngModel) {
				ngModel.$parsers.push(function(value) {
					console.log('parser', value);
					ngModel.$error.notCode = value == "undefined" || value.indexOf('-') != 1;
					setErrorState(element, ngModel.$error.notCode);
					
					return value;
				});
				ngModel.$formatters.push(function(value){
					console.log('formatter', value);
					
					setErrorState(element, typeof value === undefined)
					
					return value;
				}); 
			}
		}
	};
});