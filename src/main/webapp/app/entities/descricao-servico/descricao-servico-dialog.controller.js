(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('DescricaoServicoDialogController', DescricaoServicoDialogController);

    DescricaoServicoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'DescricaoServico'];

    function DescricaoServicoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, DescricaoServico) {
        var vm = this;

        vm.descricaoServico = entity;
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
            if (vm.descricaoServico.id !== null) {
                DescricaoServico.update(vm.descricaoServico, onSaveSuccess, onSaveError);
            } else {
                DescricaoServico.save(vm.descricaoServico, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('hmProjetosApp:descricaoServicoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
