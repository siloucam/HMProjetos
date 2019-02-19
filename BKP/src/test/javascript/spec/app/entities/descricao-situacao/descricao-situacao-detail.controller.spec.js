'use strict';

describe('Controller Tests', function() {

    describe('DescricaoSituacao Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockDescricaoSituacao, MockTipoSituacao;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockDescricaoSituacao = jasmine.createSpy('MockDescricaoSituacao');
            MockTipoSituacao = jasmine.createSpy('MockTipoSituacao');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'DescricaoSituacao': MockDescricaoSituacao,
                'TipoSituacao': MockTipoSituacao
            };
            createController = function() {
                $injector.get('$controller')("DescricaoSituacaoDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'hmProjetosApp:descricaoSituacaoUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
