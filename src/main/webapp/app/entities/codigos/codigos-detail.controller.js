(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('CodigosDetailController', CodigosDetailController);

    CodigosDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Codigos'];

    function CodigosDetailController($scope, $rootScope, $stateParams, previousState, entity, Codigos) {
        var vm = this;

        vm.codigos = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('hmProjetosApp:codigosUpdate', function(event, result) {
            vm.codigos = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
