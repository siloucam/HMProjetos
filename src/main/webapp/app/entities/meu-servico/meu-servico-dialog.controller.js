(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('MeuServicoDialogController', MeuServicoDialogController);

    MeuServicoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'MeuServico'];

    function MeuServicoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, MeuServico) {
        var vm = this;

        vm.meuServico = entity;
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
            if (vm.meuServico.id !== null) {
                MeuServico.update(vm.meuServico, onSaveSuccess, onSaveError);
            } else {
                MeuServico.save(vm.meuServico, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('hmProjetosApp:meuServicoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
