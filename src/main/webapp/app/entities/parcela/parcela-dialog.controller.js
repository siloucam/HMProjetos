(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('ParcelaDialogController', ParcelaDialogController);

    ParcelaDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Parcela', 'Transacao', 'Orcamento'];

    function ParcelaDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Parcela, Transacao, Orcamento) {
        var vm = this;

        vm.parcela = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.transacaos = Transacao.query();
        vm.orcamentos = Orcamento.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.parcela.id !== null) {
                Parcela.update(vm.parcela, onSaveSuccess, onSaveError);
            } else {
                Parcela.save(vm.parcela, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('hmProjetosApp:parcelaUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dtestipulada = false;
        vm.datePickerOpenStatus.dtefetuada = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
