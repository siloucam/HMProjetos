(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('SituacaoDeleteController',SituacaoDeleteController);

    SituacaoDeleteController.$inject = ['$uibModalInstance', 'entity', 'Situacao'];

    function SituacaoDeleteController($uibModalInstance, entity, Situacao) {
        var vm = this;

        vm.situacao = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Situacao.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
