(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('ServicoController', ServicoController);

    ServicoController.$inject = ['Servico', 'ParseLinks', 'AlertService', 'paginationConstants'];

    function ServicoController(Servico, ParseLinks, AlertService, paginationConstants) {

        var vm = this;

        vm.servicos = [];
        vm.loadPage = loadPage;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.page = 0;
        vm.links = {
            last: 0
        };
        vm.predicate = 'id';
        vm.reset = reset;
        vm.reverse = true;

        vm.filtronumero = "";
        vm.filtrotipo = "";

        loadAll();


        /* function */

        document.getElementsByName("filtrotipo")[0].addEventListener('change', Filtrar);
        document.getElementsByName("filtronumero")[0].addEventListener('change', Filtrar);

        function Filtrar(){

            Servico.query(function(result) {

                var servicosfiltrados = [];

                console.log("Filtrando por: " + vm.filtrotipo + " e " + vm.filtronumero)

                
                for(var i=0;i<result.length;i++){

                // console.log(result[i].nome + " inclui " + vm.filtro + "?");
                // console.log(result[i].nome.includes(vm.filtro));

                if(result[i].codigo.includes(vm.filtronumero) && result[i].codigo.includes(vm.filtrotipo)){
                    servicosfiltrados.push(result[i]);
                    }
                }
            

                vm.servicos = servicosfiltrados;
                vm.searchQuery = null;
            });
        }


        function loadAll () {
            Servico.query({
                page: vm.page,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                for (var i = 0; i < data.length; i++) {
                    vm.servicos.push(data[i]);
                }
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function reset () {
            vm.page = 0;
            vm.servicos = [];
            loadAll();
        }

        function loadPage(page) {
            vm.page = page;
            loadAll();
        }
    }
})();
