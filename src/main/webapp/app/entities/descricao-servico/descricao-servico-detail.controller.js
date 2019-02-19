(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('DescricaoServicoDetailController', DescricaoServicoDetailController);

    DescricaoServicoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'DescricaoServico'];

    function DescricaoServicoDetailController($scope, $rootScope, $stateParams, previousState, entity, DescricaoServico) {
        var vm = this;

        vm.descricaoServico = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('hmProjetosApp:descricaoServicoUpdate', function(event, result) {
            vm.descricaoServico = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
