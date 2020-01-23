(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('ServicoController', ServicoController);

    ServicoController.$inject = ['Servico', 'ParseLinks', 'AlertService', 'paginationConstants','$scope'];

    function ServicoController(Servico, ParseLinks, AlertService, paginationConstants,$scope) {

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

        loadAll();



        $scope.Filtrar = function(){

            Servico.queryFilterCodigo({filtro: vm.filtro}, function(result){
                vm.servicos = result;                
            })

            // Servico.query(function(result) {

            //     var clientesfiltrados = [];

            //     if(vm.filtro!=null){
            //     for(var i=0;i<result.length;i++){

            //     // console.log(result[i].nome + " inclui " + vm.filtro + "?");
            //     // console.log(result[i].nome.includes(vm.filtro));

            //     if(result[i].codigo.toLowerCase().includes(vm.filtro.toLowerCase()) || result[i].cliente.nome.toLowerCase().includes(vm.filtro.toLowerCase())){
            //         clientesfiltrados.push(result[i]);
            //     }
            //     }
            // }

            //     vm.servicos = clientesfiltrados;
            //     vm.searchQuery = null;
            // });
        }

        $scope.FiltrarCliente = function(){



            // Servico.queryFilterCliente({nome: vm.filtrocliente}, function(result){


            //      console.log(result);
            //     vm.servicos = result;                
            // })

            // Servico.query(function(result) {

            //     var clientesfiltrados = [];

            //     if(vm.filtro!=null){
            //     for(var i=0;i<result.length;i++){

            //     // console.log(result[i].nome + " inclui " + vm.filtro + "?");
            //     // console.log(result[i].nome.includes(vm.filtro));

            //     if(result[i].codigo.toLowerCase().includes(vm.filtro.toLowerCase()) || result[i].cliente.nome.toLowerCase().includes(vm.filtro.toLowerCase())){
            //         clientesfiltrados.push(result[i]);
            //     }
            //     }
            // }

            //     vm.servicos = clientesfiltrados;
            //     vm.searchQuery = null;
            // });
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
