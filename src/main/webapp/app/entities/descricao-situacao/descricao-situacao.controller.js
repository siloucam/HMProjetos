(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('DescricaoSituacaoController', DescricaoSituacaoController);

    DescricaoSituacaoController.$inject = ['DescricaoSituacao'];

    function DescricaoSituacaoController(DescricaoSituacao) {

        var vm = this;

        vm.descricaoSituacaos = [];

        loadAll();

        function loadAll() {
            DescricaoSituacao.query(function(result) {
                vm.descricaoSituacaos = result;
                vm.searchQuery = null;
            });
        }
    }
})();
