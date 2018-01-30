(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('ExtendUserDetailController', ExtendUserDetailController);

    ExtendUserDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ExtendUser', 'User', 'Situacao'];

    function ExtendUserDetailController($scope, $rootScope, $stateParams, previousState, entity, ExtendUser, User, Situacao) {
        var vm = this;

        vm.extendUser = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('hmProjetosApp:extendUserUpdate', function(event, result) {
            vm.extendUser = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
