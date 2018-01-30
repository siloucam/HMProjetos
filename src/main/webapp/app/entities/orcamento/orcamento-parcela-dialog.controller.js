(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('OrcamentoParcelaDialogController', OrcamentoParcelaDialogController);

    OrcamentoParcelaDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Parcela', 'Orcamento'];

    function OrcamentoParcelaDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Parcela, Orcamento) {
        var vm = this;

        var repetir = 0;

        vm.parcela = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.orcamentos = Orcamento.query();

        if(vm.parcela.status == null) vm.parcela.status = "PENDENTE";

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        $scope.TemId = function(){
            if(vm.parcela.id==null) return true;
        };

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.parcela.id !== null) {
                Parcela.update(vm.parcela, onSaveSuccess, onSaveError);
            } else {
                console.log(repetir);
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
