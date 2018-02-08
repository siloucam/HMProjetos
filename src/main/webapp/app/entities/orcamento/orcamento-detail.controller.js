(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('OrcamentoDetailController', OrcamentoDetailController);

    OrcamentoDetailController.$inject = ['$window','DateUtils','$timeout','$uibModal','$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Orcamento', 'Servico', 'Parcela'];

    function OrcamentoDetailController($window, DateUtils, $timeout, $uibModal, $scope, $rootScope, $stateParams, previousState, entity, Orcamento, Servico, Parcela) {


        var vm = this;

        vm.orcamento = entity;
        vm.previousState = previousState.name;

        vm.parcelas = [];

        loadAll();

        //parcelaCheck();

        var style;

        $scope.Back = function(){
            $window.location.href = '/#/servico/servico/' + vm.orcamento.servico.id;
        }
 
        function loadAll() {
            Parcela.queryByOrcamento({id: vm.orcamento.id}, function(result) {

                var today = new Date();
                today.setDate(today.getDate() - 1);

                for(var i = 0; i < result.length; i++){
                var dtestipulada = DateUtils.convertLocalDateFromServer(result[i].dtestipulada)
                if(result[i].dtefetuada != null && result[i].status != "EFETUADA"){
                    result[i].status = "EFETUADA";
                    Parcela.update(result[i], onParcelaSaveSuccess, onParcelaSaveError);
                }
                if(result[i].dtefetuada == null
                    && dtestipulada > today 
                    && result[i].status != "PENDENTE"){
                    result[i].status = "PENDENTE";
                    Parcela.update(result[i], onParcelaSaveSuccess, onParcelaSaveError);
                }
                if(result[i].dtefetuada == null 
                    && result[i].status != "ATRASADA" 
                    && dtestipulada < today
                    ){
                    console.log(today);
                    console.log(dtestipulada);

                    result[i].status = "ATRASADA";
                    Parcela.update(result[i], onParcelaSaveSuccess, onParcelaSaveError);
                }
            }

                result.sort(function(a, b) {
                var dateA = new Date(a.dtestipulada), dateB = new Date(b.dtestipulada);
                return dateA - dateB;
                });

                vm.parcelas = result;
                vm.searchQuery = null;
            });
        }

        $timeout(function () {
        //parcelaCheck();
        }, 1500);

        function parcelaCheck() {

            var today = new Date();
            console.log(today);

            for(var i = 0; i < vm.parcelas.length; i++){
                var dtestipulada = DateUtils.convertLocalDateFromServer(vm.parcelas[i].dtestipulada)
                if(vm.parcelas[i].dtefetuada != null && vm.parcelas[i].status != "EFETUADA"){
                    vm.parcelas[i].status = "EFETUADA";
                    Parcela.update(vm.parcelas[i], onParcelaSaveSuccess, onParcelaSaveError);
                }
                if(vm.parcelas[i].dtefetuada == null
                    && dtestipulada > today 
                    && vm.parcelas[i].status != "PENDENTE"){
                    vm.parcelas[i].status = "PENDENTE";
                    Parcela.update(vm.parcelas[i], onParcelaSaveSuccess, onParcelaSaveError);
                }


                if(vm.parcelas[i].dtefetuada == null 
                    && vm.parcelas[i].status != "ATRASADA" 
                    && vm.parcelas[i].dtestipulada < today
                    ){

                    console.log(vm.parcelas[i].dtestipulada);
                    vm.parcelas[i].status = "ATRASADA";

                    Parcela.update(vm.parcelas[i], onParcelaSaveSuccess, onParcelaSaveError);
                }


            }

            vm.parcelas = [];
            loadAll();
        }

        function onParcelaSaveSuccess (result) {
            $scope.$emit('hmProjetosApp:parcelaUpdate', result);
        }

        function onParcelaSaveError () {
        
        }

        var unsubscribe = $rootScope.$on('hmProjetosApp:orcamentoUpdate', function(event, result) {
            vm.orcamento = result;
        });
        $scope.$on('$destroy', unsubscribe);


         $scope.TemParcela = function() {
            var tem = false;
            if(vm.parcelas.length > 0) tem = true;
            return tem;
        };


        $scope.newParcela = function(){
            $uibModal.open({
                    templateUrl: 'app/entities/orcamento/orcamento-parcela-dialog.html',
                    controller: 'OrcamentoParcelaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'md',
                    resolve: {
                        entity: function () {
                            return {
                                orcamento: vm.orcamento,
                                status: null,
                                valor: null,
                                dtestipulada: null,
                                dtefetuada: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    vm.parcelas = [];
                    loadAll();
                }, function() {
                    
                });
        }

        $scope.editParcela = function(Id){
            $uibModal.open({
                    templateUrl: 'app/entities/orcamento/orcamento-parcela-dialog.html',
                    controller: 'OrcamentoParcelaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'md',
                    resolve: {
                       entity: ['Parcela', function(Parcela) {
                            return Parcela.get(Id).$promise;
                        }]
                        }
                }).result.then(function() {
                    vm.parcelas = [];
                    loadAll();
                }, function() {
                    
                });
        }

        $scope.delParcela = function(Id){

            $uibModal.open({
                    templateUrl: 'app/entities/parcela/parcela-delete-dialog.html',
                    controller: 'ParcelaDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Parcela', function(Parcela) {
                            return Parcela.get(Id).$promise;
                        }]
                    }
                }).result.then(function() {
                    vm.parcelas = [];
                    //Parcela.delete(Id);
                    loadAll();
                }, function() {
                    
                });
        }






    }
})();
