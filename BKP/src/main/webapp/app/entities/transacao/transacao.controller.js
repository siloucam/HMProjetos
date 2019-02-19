(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('TransacaoController', TransacaoController);

    TransacaoController.$inject = ['Transacao'];

    function TransacaoController(Transacao) {

        var vm = this;

        vm.transacaos = [];

        loadAll();

        function loadAll() {
            Transacao.query(function(result) {
                vm.transacaos = result;
                vm.searchQuery = null;
            });
        }
    }
})();
