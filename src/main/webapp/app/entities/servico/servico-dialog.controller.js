(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('ServicoDialogController', ServicoDialogController);

    ServicoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Servico', 'Situacao', 'Transacao', 'Orcamento', 'Cliente'];

    function ServicoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Servico, Situacao, Transacao, Orcamento, Cliente) {
        var vm = this;

        vm.servico = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.situacaos = Situacao.query();
        vm.transacaos = Transacao.query();
        vm.orcamentos = Orcamento.query();
        vm.clientes = Cliente.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.servico.id !== null) {
                Servico.update(vm.servico, onSaveSuccess, onSaveError);
            } else {
                Servico.save(vm.servico, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('hmProjetosApp:servicoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.inicio = false;
        vm.datePickerOpenStatus.fim = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
