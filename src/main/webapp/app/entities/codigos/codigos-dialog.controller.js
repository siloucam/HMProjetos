(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('CodigosDialogController', CodigosDialogController);

    CodigosDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Codigos'];

    function CodigosDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Codigos) {
        var vm = this;

        vm.codigos = entity;
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
            if (vm.codigos.id !== null) {
                Codigos.update(vm.codigos, onSaveSuccess, onSaveError);
            } else {
                Codigos.save(vm.codigos, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('hmProjetosApp:codigosUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
