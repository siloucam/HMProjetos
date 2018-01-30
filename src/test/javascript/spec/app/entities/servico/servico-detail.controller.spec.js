'use strict';

describe('Controller Tests', function() {

    describe('Servico Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockServico, MockSituacao, MockTransacao, MockOrcamento, MockCliente;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockServico = jasmine.createSpy('MockServico');
            MockSituacao = jasmine.createSpy('MockSituacao');
            MockTransacao = jasmine.createSpy('MockTransacao');
            MockOrcamento = jasmine.createSpy('MockOrcamento');
            MockCliente = jasmine.createSpy('MockCliente');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Servico': MockServico,
                'Situacao': MockSituacao,
                'Transacao': MockTransacao,
                'Orcamento': MockOrcamento,
                'Cliente': MockCliente
            };
            createController = function() {
                $injector.get('$controller')("ServicoDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'hmProjetosApp:servicoUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
