'use strict';

describe('Controller Tests', function() {

    describe('CodigoPrefeitura Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockCodigoPrefeitura, MockServico;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockCodigoPrefeitura = jasmine.createSpy('MockCodigoPrefeitura');
            MockServico = jasmine.createSpy('MockServico');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'CodigoPrefeitura': MockCodigoPrefeitura,
                'Servico': MockServico
            };
            createController = function() {
                $injector.get('$controller')("CodigoPrefeituraDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'hmProjetosApp:codigoPrefeituraUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
