(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('LinkExternoController', LinkExternoController);

    LinkExternoController.$inject = ['LinkExterno'];

    function LinkExternoController(LinkExterno) {

        var vm = this;

        vm.linkExternos = [];

        loadAll();

        function loadAll() {
            LinkExterno.query(function(result) {
                vm.linkExternos = result;
                vm.searchQuery = null;
            });
        }
    }
})();
