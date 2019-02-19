(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('TelefoneDetailController', TelefoneDetailController);

    TelefoneDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Telefone', 'Cliente'];

    function TelefoneDetailController($scope, $rootScope, $stateParams, previousState, entity, Telefone, Cliente) {
        var vm = this;

        vm.telefone = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('hmProjetosApp:telefoneUpdate', function(event, result) {
            vm.telefone = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
