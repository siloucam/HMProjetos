(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('LinkExternoDeleteController',LinkExternoDeleteController);

    LinkExternoDeleteController.$inject = ['$uibModalInstance', 'entity', 'LinkExterno'];

    function LinkExternoDeleteController($uibModalInstance, entity, LinkExterno) {
        var vm = this;

        vm.linkExterno = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            LinkExterno.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
