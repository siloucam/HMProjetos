'use strict';

describe('Controller Tests', function() {

    describe('Orcamento Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockOrcamento, MockServico, MockParcela;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockOrcamento = jasmine.createSpy('MockOrcamento');
            MockServico = jasmine.createSpy('MockServico');
            MockParcela = jasmine.createSpy('MockParcela');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Orcamento': MockOrcamento,
                'Servico': MockServico,
                'Parcela': MockParcela
            };
            createController = function() {
                $injector.get('$controller')("OrcamentoDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'hmProjetosApp:orcamentoUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
