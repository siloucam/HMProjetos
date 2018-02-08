(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('OrcamentoParcelaDialogController', OrcamentoParcelaDialogController);

    OrcamentoParcelaDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Parcela', 'Orcamento'];

    function OrcamentoParcelaDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Parcela, Orcamento) {
        var vm = this;

        vm.repetir = 0;

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
                console.log(vm.parcela.dtestipulada.getMonth());

                var parcelas = [];
                var mes = [];

                for(var i=(vm.repetir);i>=0;i--){

                    parcelas[i] = angular.copy(vm.parcela);

                    mes[i] = angular.copy(vm.parcela.dtestipulada.getMonth()) + i;

                    parcelas[i].dtestipulada.setMonth(mes[i]);

                    parcelas[i].descricao = "(" + (i+1) + "/" + (vm.repetir+1) + ")";

                console.log(parcelas[i].dtestipulada);
                console.log(parcelas[i]);
                Parcela.save(parcelas[i], onSaveSuccess, onSaveError);
                }
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
