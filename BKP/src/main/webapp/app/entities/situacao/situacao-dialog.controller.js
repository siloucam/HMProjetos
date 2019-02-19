(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('SituacaoDialogController', SituacaoDialogController);

    SituacaoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Situacao', 'DescricaoSituacao', 'TipoSituacao', 'Servico', 'ExtendUser'];

    function SituacaoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Situacao, DescricaoSituacao, TipoSituacao, Servico, ExtendUser) {
        var vm = this;

        vm.situacao = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.descricaosituacaos = DescricaoSituacao.query();
        vm.tiposituacaos = TipoSituacao.query();
        vm.servicos = Servico.query();
        vm.extendusers = ExtendUser.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.situacao.id !== null) {
                Situacao.update(vm.situacao, onSaveSuccess, onSaveError);
            } else {
                Situacao.save(vm.situacao, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('hmProjetosApp:situacaoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dtinicio = false;
        vm.datePickerOpenStatus.dtfim = false;
        vm.datePickerOpenStatus.dtestipulada = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
