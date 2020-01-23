(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('ClienteController', ClienteController);

    ClienteController.$inject = ['Cliente', 'ParseLinks', 'AlertService', 'paginationConstants','$scope'];

    function ClienteController(Cliente, ParseLinks, AlertService, paginationConstants,$scope) {

        var vm = this;

        vm.clientes = [];
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

        /* function */
         $scope.Filtrar = function(){

            console.log(vm.filtro);

            Cliente.queryFilter({filtro: vm.filtro}, function(result){
                vm.clientes = result;                
            })


            // Cliente.query(function(result) {

            //     var clientesfiltrados = [];

            //     if(vm.filtro!=null){
            //     for(var i=0;i<result.length;i++){

            //     // console.log(result[i].nome + " inclui " + vm.filtro + "?");
            //     // console.log(result[i].nome.includes(vm.filtro));

            //     if(result[i].nome.toLowerCase().includes(vm.filtro.toLowerCase())){
            //         clientesfiltrados.push(result[i]);
            //     }
            //     }
            // }

            //     vm.clientes = clientesfiltrados;
            //     vm.searchQuery = null;
            // });
        }


        

        function loadAll () {
            Cliente.query({
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

            // sort();

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                for (var i = 0; i < data.length; i++) {
                    vm.clientes.push(data[i]);
                }
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function reset () {
            vm.page = 0;
            vm.clientes = [];
            loadAll();
        }

        function loadPage(page) {
            vm.page = page;
            loadAll();
        }
    }
})();
