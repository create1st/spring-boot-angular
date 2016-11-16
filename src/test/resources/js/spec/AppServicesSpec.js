describe('App service tests', function() {
	beforeEach(module('app.services'));

	describe('Year formatter test', function() {
		var year = {
			code : 0,
			value : 2014
		};

		var formatter;

		beforeEach(inject(function($filter) {
			formatter = $filter('yearFormat');
		}));

		it('Format year', inject(function() {
			var result = formatter(year);

			expect(result).toEqual(2014);
		}));
	});

	describe('Global service', function() {
		var service;
		var http;

		beforeEach(inject(function($httpBackend, globalService) {
			http = $httpBackend;
			service = globalService;
		}));

		it('Get status', inject(function() {
			var value = jasmine.createSpy('value').and.identity();
			var data = {
				value : value
			};
			http.expectGET('/global/status/get').respond(200, data); // expectPOST(url,
																		// {
																		// value
																		// :
																		// 'xxx'})

			var callback = jasmine.createSpy('callback');
			var result = service.getStatus(callback);

			expect(result).toBeUndefined();
			http.flush();
			expect(callback).toHaveBeenCalledWith(value);
		}));
	});
});