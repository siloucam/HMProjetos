(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('TransacaoDeleteController',TransacaoDeleteController);

    TransacaoDeleteController.$inject = ['$uibModalInstance', 'entity', 'Transacao'];

    function TransacaoDeleteController($uibModalInstance, entity, Transacao) {
        var vm = this;

        vm.transacao = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Transacao.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
