(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('ParcelaDeleteController',ParcelaDeleteController);

    ParcelaDeleteController.$inject = ['$uibModalInstance', 'entity', 'Parcela'];

    function ParcelaDeleteController($uibModalInstance, entity, Parcela) {
        var vm = this;

        vm.parcela = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Parcela.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
