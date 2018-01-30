(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('OrcamentoController', OrcamentoController);

    OrcamentoController.$inject = ['Orcamento'];

    function OrcamentoController(Orcamento) {

        var vm = this;

        vm.orcamentos = [];

        loadAll();

        function loadAll() {
            Orcamento.query(function(result) {
                vm.orcamentos = result;
                vm.searchQuery = null;
            });
        }
    }
})();
