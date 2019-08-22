(function() {
    'use strict';

    angular
    .module('hmProjetosApp')
    .controller('MeuServicoController', MeuServicoController);

    MeuServicoController.$inject = ['Principal','MeuServico','ExtendUser','Situacao'];

    function MeuServicoController(Principal, MeuServico, ExtendUser, Situacao) {

        var vm = this;

        vm.situacaos = [];

        // vm.loadPage = loadPage;
        // vm.itemsPerPage = paginationConstants.itemsPerPage;
        // vm.page = 0;
        // vm.links = {
        //     last: 0
        // };
        vm.predicate = 'id';
        vm.reset = reset;
        vm.reverse = true;

        // vm.meuServicos = [];

        // loadAll();

        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                console.log(account);
                vm.account = account;
                
                loadAll();

            });
        }

        function loadAll() {

           ExtendUser.queryByUser({
            Uid:vm.account.id
        }, function(data){
            console.log(data[0]);
            vm.extendUser = data[0];
            vm.searchQuery = null;


            Situacao.queryByResponsavel({
                Rid:vm.extendUser.id,
                sort: sort()
            }, function(data){
                console.log(data);
                vm.situacaos = data;
            }, function(){
                    // console.log("Erro");
                });

            
        }, function(){
                    // console.log("Erro");
                });

           function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }

       }


       function reset () {
            // vm.page = 0;
            vm.situacaos = [];
            loadAll();
        }


   }
})();
