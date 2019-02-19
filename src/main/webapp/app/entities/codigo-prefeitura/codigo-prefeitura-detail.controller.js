(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('CodigoPrefeituraDetailController', CodigoPrefeituraDetailController);

    CodigoPrefeituraDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'CodigoPrefeitura', 'Servico'];

    function CodigoPrefeituraDetailController($scope, $rootScope, $stateParams, previousState, entity, CodigoPrefeitura, Servico) {
        var vm = this;

        vm.codigoPrefeitura = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('hmProjetosApp:codigoPrefeituraUpdate', function(event, result) {
            vm.codigoPrefeitura = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
