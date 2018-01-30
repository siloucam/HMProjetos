(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('ParcelaDetailController', ParcelaDetailController);

    ParcelaDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Parcela', 'Transacao', 'Orcamento'];

    function ParcelaDetailController($scope, $rootScope, $stateParams, previousState, entity, Parcela, Transacao, Orcamento) {
        var vm = this;

        vm.parcela = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('hmProjetosApp:parcelaUpdate', function(event, result) {
            vm.parcela = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
