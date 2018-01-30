(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('TransacaoDetailController', TransacaoDetailController);

    TransacaoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Transacao', 'Parcela', 'Servico'];

    function TransacaoDetailController($scope, $rootScope, $stateParams, previousState, entity, Transacao, Parcela, Servico) {
        var vm = this;

        vm.transacao = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('hmProjetosApp:transacaoUpdate', function(event, result) {
            vm.transacao = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
