(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('MeuServicoDetailController', MeuServicoDetailController);

    MeuServicoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'MeuServico'];

    function MeuServicoDetailController($scope, $rootScope, $stateParams, previousState, entity, MeuServico) {
        var vm = this;

        vm.meuServico = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('hmProjetosApp:meuServicoUpdate', function(event, result) {
            vm.meuServico = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
