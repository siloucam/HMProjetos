(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('LinkExternoDetailController', LinkExternoDetailController);

    LinkExternoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'LinkExterno', 'Servico'];

    function LinkExternoDetailController($scope, $rootScope, $stateParams, previousState, entity, LinkExterno, Servico) {
        var vm = this;

        vm.linkExterno = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('hmProjetosApp:linkExternoUpdate', function(event, result) {
            vm.linkExterno = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
