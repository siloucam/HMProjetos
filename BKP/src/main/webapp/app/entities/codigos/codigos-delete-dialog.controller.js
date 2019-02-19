(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('CodigosDeleteController',CodigosDeleteController);

    CodigosDeleteController.$inject = ['$uibModalInstance', 'entity', 'Codigos'];

    function CodigosDeleteController($uibModalInstance, entity, Codigos) {
        var vm = this;

        vm.codigos = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Codigos.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
