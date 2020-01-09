(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('TerceiroDetailController', TerceiroDetailController);

    TerceiroDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Terceiro'];

    function TerceiroDetailController($scope, $rootScope, $stateParams, previousState, entity, Terceiro) {
        var vm = this;

        vm.terceiro = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('hmProjetosApp:terceiroUpdate', function(event, result) {
            vm.terceiro = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
