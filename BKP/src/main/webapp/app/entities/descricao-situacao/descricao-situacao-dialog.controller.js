(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('DescricaoSituacaoDialogController', DescricaoSituacaoDialogController);

    DescricaoSituacaoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'DescricaoSituacao', 'TipoSituacao'];

    function DescricaoSituacaoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, DescricaoSituacao, TipoSituacao) {
        var vm = this;

        vm.descricaoSituacao = entity;
        vm.clear = clear;
        vm.save = save;
        vm.tiposituacaos = TipoSituacao.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.descricaoSituacao.id !== null) {
                DescricaoSituacao.update(vm.descricaoSituacao, onSaveSuccess, onSaveError);
            } else {
                DescricaoSituacao.save(vm.descricaoSituacao, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('hmProjetosApp:descricaoSituacaoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
