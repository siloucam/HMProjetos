(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('ClienteTelefoneDialogController', ClienteTelefoneDialogController);


    ClienteTelefoneDialogController.$inject = ['$log','$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Telefone', 'Cliente'];

    function ClienteTelefoneDialogController ($log, $timeout, $scope, $stateParams, $uibModalInstance, entity, Telefone, Cliente) {
        var vm = this;

        vm.telefone = entity;
        vm.clear = clear;
        vm.save = save;
        vm.clientes = Cliente.query();

        vm.telefone.cliente = vm.telefone.cliente;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.telefone.id !== null) {
                Telefone.update(vm.telefone, onSaveSuccess, onSaveError);
            } else {
                Telefone.save(vm.telefone, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('hmProjetosApp:telefoneUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }



        


    }
})();
