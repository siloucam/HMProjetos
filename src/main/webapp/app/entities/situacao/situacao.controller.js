(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('SituacaoController', SituacaoController);

    SituacaoController.$inject = ['User','ExtendUser','$scope','TipoSituacao','Situacao', 'ParseLinks', 'AlertService', 'paginationConstants','Terceiro'];

    function SituacaoController(User, ExtendUser, $scope, TipoSituacao, Situacao, ParseLinks, AlertService, paginationConstants, Terceiro) {

        var vm = this;

        vm.users = [];
        vm.user;
        vm.responsavel = null;

        vm.situacaos = [];
        vm.tipoSituacaos = [];
        vm.loadPage = loadPage;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.page = 0;
        vm.links = {
            last: 0
        };
        vm.predicate = 'id';
        vm.reset = reset;
        vm.reverse = true;

        // loadAll();

        loadAllUsers();
        loadFiltros();

        // loadAll();


        $scope.printToCart = function(printSectionId) {
        var innerContents = document.getElementById(printSectionId).outerHTML;
        var popupWinindow = window.open('', '_blank', 'scrollbars=no,menubar=no,toolbar=no,location=no,status=no,titlebar=no');
        popupWinindow.document.open();
        popupWinindow.document.write("<html><head><link rel='stylesheet' href='content/css/main.css'><link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css'><link rel='stylesheet' href='bower_components/angular-loading-bar/build/loading-bar.css'></head><body onload='window.print()'>" + innerContents + '</html>');
        popupWinindow.document.close();
        // window.print();
      }



        function loadAllUsers () {
            User.query({
            }, onUserSuccess, onUserError);
        }

        vm.terceiros = Terceiro.query();

        function onUserSuccess(data, headers) {
            vm.users = data;
            console.log(data);
        }

        function onUserError(error) {
            AlertService.error(error.data.message);
        }

        vm.listEnabled = false;
        if(vm.terceiro) vm.listEnabled = true;

        vm.disableList = function(){
            vm.listEnabled = true;
        }
        vm.enableList = function(){
            vm.listEnabled = false;
        }

        $scope.gerarRelatorio = function(){

            vm.situacaos = [];

            console.log("Gerando relatório com:")
            console.log("Tipo:");
            console.log(vm.tipo);
            console.log("Responsavel:");
            console.log(vm.responsavel);
            console.log("Terceiro:");
            console.log(vm.terceiro);

            if(vm.tipo && !vm.responsavel && !vm.terceiro){

                Situacao.queryByTipo({
                    Tid:vm.tipo.id,
                    sort: sort()
                }, function(data){
                    console.log(data);
                    vm.situacaos = data;
                }, function(){
                    console.log("Erro");
                });
                
                console.log("Busca por tipo só");

                function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
                }

            }
            if(!vm.tipo && vm.responsavel){
                // console.log("Busca por responsavel só");

                     ExtendUser.queryByUser({
            Uid:vm.responsavel.id
        }, function(data){
            console.log(data[0]);
            vm.extendUser = data[0];
            vm.searchQuery = null;


            Situacao.queryByResponsavel({
                page: vm.page,
                size: vm.itemsPerPage,
                Rid:vm.extendUser.id,
                sort: sort()
            }, function(data){
                console.log(data)
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

                // Situacao.queryByResponsavel({
                //     Rid:vm.responsavel.id,
                //     sort: sort()
                // }, function(data){
                //     console.log(data);
                //     vm.situacaos = data;
                // }, function(){
                //     // console.log("Erro");
                // });

                function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
                }

            }
            if(vm.tipo && vm.responsavel){
                // console.log("Busca pelos dois");


                     ExtendUser.queryByUser({
            Uid:vm.responsavel.id
        }, function(data){
            console.log(data[0]);
            vm.extendUser = data[0];
            vm.searchQuery = null;


            Situacao.queryByTipoResponsavel({
                page: vm.page,
                size: vm.itemsPerPage,
                Rid:vm.extendUser.id,
                Tid:vm.tipo.id,
                sort: sort()
            }, function(data){
                console.log(data)
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

                // Situacao.queryByTipoResponsavel({
                //     page: vm.page,
                //     size: vm.itemsPerPage,
                //     Rid:vm.responsavel.id,
                //     Tid:vm.tipo.id,
                //     sort: sort()
                // }, function(data){
                //     console.log(data);
                //     vm.situacaos = data;
                // }, function(){
                //     // console.log("Erro");
                // });

                function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
                }

            }
            if(!vm.tipo && vm.terceiro){
                //Query por terceiro
                Situacao.queryByTerceiro({
                    page: vm.page,
                    size: vm.itemsPerPage,
                    Terceiro:vm.terceiro,
                    sort: sort()
                }, function(data){
                    console.log(data);
                    vm.situacaos = data;
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
            if(vm.tipo && vm.terceiro){
                //Query por tipo e terceiro
                Situacao.queryByTipoTerceiro({
                    page: vm.page,
                    size: vm.itemsPerPage,
                    Terceiro:vm.terceiro,
                    Tid:vm.tipo.id,
                    sort: sort()
                }, function(data){
                    console.log(data);
                    vm.situacaos = data;
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


            if(!vm.tipo && !vm.responsavel && !vm.terceiro){
                vm.situacaos = [];
            }



        }

        function loadFiltros(){

            TipoSituacao.query(function(result) {
                vm.tipoSituacaos = result;
                vm.searchQuery = null;
            });
            ExtendUser.query(function(result) {
                vm.extendUsers = result;
                vm.searchQuery = null;
            });
        }

        function loadAll () {

            Situacao.queryAtuais({
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

                console.log(data);

                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                for (var i = 0; i < data.length; i++) {
                    vm.situacaos.push(data[i]);
                }
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function reset () {
            vm.page = 0;
            vm.situacaos = [];
            // loadAll();
            $scope.gerarRelatorio();
        }

        function loadPage(page) {
            vm.page = page;
            // loadAll();
            $scope.gerarRelatorio();
        }
    }
})();
