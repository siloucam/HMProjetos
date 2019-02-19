(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('CodigoPrefeituraDialogController', CodigoPrefeituraDialogController);

    CodigoPrefeituraDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CodigoPrefeitura', 'Servico'];

    function CodigoPrefeituraDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, CodigoPrefeitura, Servico) {
        var vm = this;

        vm.codigoPrefeitura = entity;
        vm.clear = clear;
        vm.save = save;
        vm.servicos = Servico.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.codigoPrefeitura.id !== null) {
                CodigoPrefeitura.update(vm.codigoPrefeitura, onSaveSuccess, onSaveError);
            } else {
                CodigoPrefeitura.save(vm.codigoPrefeitura, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('hmProjetosApp:codigoPrefeituraUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
