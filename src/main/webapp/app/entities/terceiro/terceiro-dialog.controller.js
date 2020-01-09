(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('TerceiroDialogController', TerceiroDialogController);

    TerceiroDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Terceiro'];

    function TerceiroDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Terceiro) {
        var vm = this;

        vm.terceiro = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.terceiro.id !== null) {
                Terceiro.update(vm.terceiro, onSaveSuccess, onSaveError);
            } else {
                Terceiro.save(vm.terceiro, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('hmProjetosApp:terceiroUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
