(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('TipoSituacaoDetailController', TipoSituacaoDetailController);

    TipoSituacaoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TipoSituacao'];

    function TipoSituacaoDetailController($scope, $rootScope, $stateParams, previousState, entity, TipoSituacao) {
        var vm = this;

        vm.tipoSituacao = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('hmProjetosApp:tipoSituacaoUpdate', function(event, result) {
            vm.tipoSituacao = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
