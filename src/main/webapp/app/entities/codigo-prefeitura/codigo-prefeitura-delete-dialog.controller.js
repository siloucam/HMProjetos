(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('CodigoPrefeituraDeleteController',CodigoPrefeituraDeleteController);

    CodigoPrefeituraDeleteController.$inject = ['$uibModalInstance', 'entity', 'CodigoPrefeitura'];

    function CodigoPrefeituraDeleteController($uibModalInstance, entity, CodigoPrefeitura) {
        var vm = this;

        vm.codigoPrefeitura = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            CodigoPrefeitura.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
