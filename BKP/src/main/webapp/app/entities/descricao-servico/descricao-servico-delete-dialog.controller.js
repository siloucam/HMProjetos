(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('DescricaoServicoDeleteController',DescricaoServicoDeleteController);

    DescricaoServicoDeleteController.$inject = ['$uibModalInstance', 'entity', 'DescricaoServico'];

    function DescricaoServicoDeleteController($uibModalInstance, entity, DescricaoServico) {
        var vm = this;

        vm.descricaoServico = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            DescricaoServico.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
