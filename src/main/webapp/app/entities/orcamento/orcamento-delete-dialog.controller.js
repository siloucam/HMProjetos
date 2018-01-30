(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('OrcamentoDeleteController',OrcamentoDeleteController);

    OrcamentoDeleteController.$inject = ['$uibModalInstance', 'entity', 'Orcamento'];

    function OrcamentoDeleteController($uibModalInstance, entity, Orcamento) {
        var vm = this;

        vm.orcamento = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Orcamento.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
