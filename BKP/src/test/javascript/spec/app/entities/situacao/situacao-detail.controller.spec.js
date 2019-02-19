'use strict';

describe('Controller Tests', function() {

    describe('Situacao Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockSituacao, MockDescricaoSituacao, MockTipoSituacao, MockServico, MockExtendUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockSituacao = jasmine.createSpy('MockSituacao');
            MockDescricaoSituacao = jasmine.createSpy('MockDescricaoSituacao');
            MockTipoSituacao = jasmine.createSpy('MockTipoSituacao');
            MockServico = jasmine.createSpy('MockServico');
            MockExtendUser = jasmine.createSpy('MockExtendUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Situacao': MockSituacao,
                'DescricaoSituacao': MockDescricaoSituacao,
                'TipoSituacao': MockTipoSituacao,
                'Servico': MockServico,
                'ExtendUser': MockExtendUser
            };
            createController = function() {
                $injector.get('$controller')("SituacaoDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'hmProjetosApp:situacaoUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
