'use strict';

describe('Controller Tests', function() {

    describe('Parcela Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockParcela, MockTransacao, MockOrcamento;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockParcela = jasmine.createSpy('MockParcela');
            MockTransacao = jasmine.createSpy('MockTransacao');
            MockOrcamento = jasmine.createSpy('MockOrcamento');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Parcela': MockParcela,
                'Transacao': MockTransacao,
                'Orcamento': MockOrcamento
            };
            createController = function() {
                $injector.get('$controller')("ParcelaDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'hmProjetosApp:parcelaUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
