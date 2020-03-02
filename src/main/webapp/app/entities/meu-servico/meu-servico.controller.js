(function() {
    'use strict';

    angular
    .module('hmProjetosApp')
    .controller('MeuServicoController', MeuServicoController);

    MeuServicoController.$inject = ['Principal','MeuServico','ExtendUser','Situacao','$scope'];

    function MeuServicoController(Principal, MeuServico, ExtendUser, Situacao, $scope) {

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

        $scope.printToCart = function(printSectionId) {
            var innerContents = document.getElementById(printSectionId).outerHTML;
            var popupWinindow = window.open('', '_blank', 'scrollbars=no,menubar=no,toolbar=no,location=no,status=no,titlebar=no');
            popupWinindow.document.open();
            popupWinindow.document.write("<html><head><link rel='stylesheet' href='content/css/main.css'><link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css'><link rel='stylesheet' href='bower_components/angular-loading-bar/build/loading-bar.css'></head><body onload='window.print()'><h2>Serviços "+ vm.account.firstName +"</h2>" + innerContents + '</html>');
            popupWinindow.document.close();
            // window.print();
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
                vm.situacaos = data;
            }, function(){
                    console.log("Erro");
                });

            // Situacao.queryByResponsavel({Rid: vm.extendUser.id, sort: sort()}, function(result){
            //     console.log(result);            
            // })

            
        }, function(){
                    console.log("Erro");
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
