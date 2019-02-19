(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('TipoSituacaoDeleteController',TipoSituacaoDeleteController);

    TipoSituacaoDeleteController.$inject = ['$uibModalInstance', 'entity', 'TipoSituacao'];

    function TipoSituacaoDeleteController($uibModalInstance, entity, TipoSituacao) {
        var vm = this;

        vm.tipoSituacao = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TipoSituacao.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
