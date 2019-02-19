(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('LinkExternoDialogController', LinkExternoDialogController);

    LinkExternoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'LinkExterno', 'Servico'];

    function LinkExternoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, LinkExterno, Servico) {
        var vm = this;

        vm.linkExterno = entity;
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
            if (vm.linkExterno.id !== null) {
                LinkExterno.update(vm.linkExterno, onSaveSuccess, onSaveError);
            } else {
                LinkExterno.save(vm.linkExterno, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('hmProjetosApp:linkExternoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
