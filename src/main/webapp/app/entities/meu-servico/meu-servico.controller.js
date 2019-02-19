(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('MeuServicoController', MeuServicoController);

    MeuServicoController.$inject = ['MeuServico'];

    function MeuServicoController(MeuServico) {

        var vm = this;

        vm.meuServicos = [];

        loadAll();

        function loadAll() {
            MeuServico.query(function(result) {
                vm.meuServicos = result;
                vm.searchQuery = null;
            });
        }
    }
})();
