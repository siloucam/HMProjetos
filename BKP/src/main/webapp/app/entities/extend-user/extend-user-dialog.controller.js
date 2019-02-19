(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('ExtendUserDialogController', ExtendUserDialogController);

    ExtendUserDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'ExtendUser', 'User', 'Situacao'];

    function ExtendUserDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, ExtendUser, User, Situacao) {
        var vm = this;

        vm.extendUser = entity;
        vm.clear = clear;
        vm.save = save;
        vm.users = User.query();
        vm.situacaos = Situacao.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.extendUser.id !== null) {
                ExtendUser.update(vm.extendUser, onSaveSuccess, onSaveError);
            } else {
                ExtendUser.save(vm.extendUser, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('hmProjetosApp:extendUserUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
