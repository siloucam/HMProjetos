(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('CodigoPrefeituraController', CodigoPrefeituraController);

    CodigoPrefeituraController.$inject = ['CodigoPrefeitura'];

    function CodigoPrefeituraController(CodigoPrefeitura) {

        var vm = this;

        vm.codigoPrefeituras = [];

        loadAll();

        function loadAll() {
            CodigoPrefeitura.query(function(result) {
                vm.codigoPrefeituras = result;
                vm.searchQuery = null;
            });
        }
    }
})();
