(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('TipoSituacaoController', TipoSituacaoController);

    TipoSituacaoController.$inject = ['TipoSituacao'];

    function TipoSituacaoController(TipoSituacao) {

        var vm = this;

        vm.tipoSituacaos = [];

        loadAll();

        function loadAll() {
            TipoSituacao.query(function(result) {
                vm.tipoSituacaos = result;
                vm.searchQuery = null;
            });
        }
    }
})();
