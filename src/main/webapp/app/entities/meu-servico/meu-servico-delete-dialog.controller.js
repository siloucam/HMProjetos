(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('MeuServicoDeleteController',MeuServicoDeleteController);

    MeuServicoDeleteController.$inject = ['$uibModalInstance', 'entity', 'MeuServico'];

    function MeuServicoDeleteController($uibModalInstance, entity, MeuServico) {
        var vm = this;

        vm.meuServico = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            MeuServico.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
