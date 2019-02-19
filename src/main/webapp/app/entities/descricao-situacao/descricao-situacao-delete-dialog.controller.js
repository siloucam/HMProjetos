(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('DescricaoSituacaoDeleteController',DescricaoSituacaoDeleteController);

    DescricaoSituacaoDeleteController.$inject = ['$uibModalInstance', 'entity', 'DescricaoSituacao'];

    function DescricaoSituacaoDeleteController($uibModalInstance, entity, DescricaoSituacao) {
        var vm = this;

        vm.descricaoSituacao = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            DescricaoSituacao.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
