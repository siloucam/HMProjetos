(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('CodigosController', CodigosController);

    CodigosController.$inject = ['Codigos'];

    function CodigosController(Codigos) {

        var vm = this;

        vm.codigos = [];

        loadAll();

        function loadAll() {
            Codigos.query(function(result) {
                vm.codigos = result;
                vm.searchQuery = null;
            });
        }
    }
})();
