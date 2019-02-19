(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('SituacaoDetailController', SituacaoDetailController);

    SituacaoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Situacao', 'DescricaoSituacao', 'TipoSituacao', 'Servico', 'ExtendUser'];

    function SituacaoDetailController($scope, $rootScope, $stateParams, previousState, entity, Situacao, DescricaoSituacao, TipoSituacao, Servico, ExtendUser) {
        var vm = this;

        vm.situacao = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('hmProjetosApp:situacaoUpdate', function(event, result) {
            vm.situacao = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
