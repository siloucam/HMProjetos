(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('TerceiroController', TerceiroController);

    TerceiroController.$inject = ['Terceiro'];

    function TerceiroController(Terceiro) {

        var vm = this;

        vm.terceiros = [];

        loadAll();

        function loadAll() {
            Terceiro.query(function(result) {
                vm.terceiros = result;
                vm.searchQuery = null;
            });
        }
    }
})();
