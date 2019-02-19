'use strict';

describe('Controller Tests', function() {

    describe('Servico Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockServico, MockCodigoPrefeitura, MockLinkExterno, MockSituacao, MockTransacao, MockDescricaoServico, MockCliente;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockServico = jasmine.createSpy('MockServico');
            MockCodigoPrefeitura = jasmine.createSpy('MockCodigoPrefeitura');
            MockLinkExterno = jasmine.createSpy('MockLinkExterno');
            MockSituacao = jasmine.createSpy('MockSituacao');
            MockTransacao = jasmine.createSpy('MockTransacao');
            MockDescricaoServico = jasmine.createSpy('MockDescricaoServico');
            MockCliente = jasmine.createSpy('MockCliente');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Servico': MockServico,
                'CodigoPrefeitura': MockCodigoPrefeitura,
                'LinkExterno': MockLinkExterno,
                'Situacao': MockSituacao,
                'Transacao': MockTransacao,
                'DescricaoServico': MockDescricaoServico,
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
