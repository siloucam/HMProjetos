(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('TransacaoDialogController', TransacaoDialogController);

    TransacaoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Transacao', 'Servico'];

    function TransacaoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Transacao, Servico) {
        var vm = this;

        vm.transacao = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
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
            if (vm.transacao.id !== null) {
                Transacao.update(vm.transacao, onSaveSuccess, onSaveError);
            } else {
                Transacao.save(vm.transacao, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('hmProjetosApp:transacaoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.data = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
