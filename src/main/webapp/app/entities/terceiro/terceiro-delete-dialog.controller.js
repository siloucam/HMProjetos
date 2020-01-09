(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('TerceiroDeleteController',TerceiroDeleteController);

    TerceiroDeleteController.$inject = ['$uibModalInstance', 'entity', 'Terceiro'];

    function TerceiroDeleteController($uibModalInstance, entity, Terceiro) {
        var vm = this;

        vm.terceiro = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Terceiro.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
