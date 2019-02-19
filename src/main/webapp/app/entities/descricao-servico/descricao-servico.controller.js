(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('DescricaoServicoController', DescricaoServicoController);

    DescricaoServicoController.$inject = ['DescricaoServico'];

    function DescricaoServicoController(DescricaoServico) {

        var vm = this;

        vm.descricaoServicos = [];

        loadAll();

        function loadAll() {
            DescricaoServico.query(function(result) {
                vm.descricaoServicos = result;
                vm.searchQuery = null;
            });
        }
    }
})();
