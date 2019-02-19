(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('DescricaoSituacaoDetailController', DescricaoSituacaoDetailController);

    DescricaoSituacaoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'DescricaoSituacao', 'TipoSituacao'];

    function DescricaoSituacaoDetailController($scope, $rootScope, $stateParams, previousState, entity, DescricaoSituacao, TipoSituacao) {
        var vm = this;

        vm.descricaoSituacao = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('hmProjetosApp:descricaoSituacaoUpdate', function(event, result) {
            vm.descricaoSituacao = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
