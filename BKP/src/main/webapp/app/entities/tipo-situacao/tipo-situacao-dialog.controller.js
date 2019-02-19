(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('TipoSituacaoDialogController', TipoSituacaoDialogController);

    TipoSituacaoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TipoSituacao'];

    function TipoSituacaoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TipoSituacao) {
        var vm = this;

        vm.tipoSituacao = entity;
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
            if (vm.tipoSituacao.id !== null) {
                TipoSituacao.update(vm.tipoSituacao, onSaveSuccess, onSaveError);
            } else {
                TipoSituacao.save(vm.tipoSituacao, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('hmProjetosApp:tipoSituacaoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
