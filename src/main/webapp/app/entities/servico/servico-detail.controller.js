(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('ServicoDetailController', ServicoDetailController);

    ServicoDetailController.$inject = ['$window','$timeout','$uibModal','$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Servico', 'Situacao', 'Transacao', 'Orcamento', 'Cliente'];

    function ServicoDetailController($window,$timeout, $uibModal, $scope, $rootScope, $stateParams, previousState, entity, Servico, Situacao, Transacao, Orcamento, Cliente) {
        var vm = this;

        vm.servico = entity;
        vm.previousState = previousState.name;

        vm.orcamentos = [];
        vm.transacaos = [];
        vm.situacaos = [];

        $scope.Back = function(){
            $window.location.href = '/#/servico';
        }

        function calculaSaldo(){

            vm.saldo = 0;

            //console.log("Ajustando Saldo")
        
            if(vm.orcamentos.length > 0){
                vm.saldo = vm.saldo - vm.orcamentos[0].valor;
            }

            if(vm.transacaos.length > 0){

                //console.log("Adicionando valor de " + vm.transacaos.length + " transacoes");
                //console.log(vm.transacaos);

                for(var i = 0; i < vm.transacaos.length;i++){

                    if(vm.transacaos[i].operacao == "CREDITO" || vm.transacaos[i].operacao == "PARCELA"){
                        vm.saldo = vm.saldo + vm.transacaos[i].valor;
                    }
                    if(vm.transacaos[i].operacao == "DEBITO"){
                        vm.saldo = vm.saldo - vm.transacaos[i].valor;   
                    }
                }

            }
        }

        loadAll();

        function loadAll() {

            vm.orcamentos = [];
            vm.transacaos = [];
            vm.situacaos = [];

            Orcamento.queryByServico({id: vm.servico.id}, function(result) {
                vm.orcamentos = result;
                vm.searchQuery = null;

                Transacao.queryByServico({id: vm.servico.id}, function(result) {
                vm.transacaos = result;
                vm.searchQuery = null;

                calculaSaldo();
                
                });
            });
            
            Situacao.queryByServico({id: vm.servico.id}, function(result) {
            vm.situacaos = result;

            console.log(vm.situacaos);

            vm.searchQuery = null;
            });

        }

        $scope.DEBUG = function(){


            console.log(vm.orcamentos);

            console.log(vm.transacaos);

        }

        $scope.TemOrcamento = function() {
            var tem = false;
            if(vm.orcamentos.length > 0) tem = true;
            return tem;
        };

        $scope.TemTransacao = function() {
            var tem = false;
            if(vm.transacaos.length > 0) tem = true;
            return tem;

        };
        $scope.TemSituacao = function() {
            var tem = false;
            if(vm.situacaos.length > 0) tem = true;
            return tem;

        };

        $scope.newSituacao = function(){
            $uibModal.open({
                    templateUrl: 'app/entities/situacao/situacao-dialog.html',
                    controller: 'SituacaoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                servico: vm.servico,
                                tipo: null,
                                descricao: null,
                                terceiro: null,
                                dtinicio: null,
                                dtfim: null,
                                dtestipulada: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    
                }, function() {
                   
                });
        }

        $scope.newOrcamento = function(){
            $uibModal.open({
                    templateUrl: 'app/entities/orcamento/orcamento-dialog.html',
                    controller: 'OrcamentoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'md',
                    resolve: {
                        entity: function () {
                            return {
                                servico: vm.servico,
                                valor: null,
                                entrada: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
            
                    loadAll();
                    //$state.go('orcamento', null, { reload: 'orcamento' });
                }, function() {
                    //$state.go('orcamento');
                });
        }

        $scope.newTransacao = function(){
            $uibModal.open({
                    templateUrl: 'app/entities/transacao/transacao-dialog.html',
                    controller: 'TransacaoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                servico: vm.servico,
                                valor: null,
                                data: null,
                                descricao: null,
                                operacao: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
         
                    loadAll();

                    //$state.go('transacao', null, { reload: 'transacao' });
                }, function() {
                    //$state.go('transacao');
                });
        }

       
        $scope.deleteTransacao = function(Id) {
            $uibModal.open({
                    templateUrl: 'app/entities/transacao/transacao-delete-dialog.html',
                    controller: 'TransacaoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Transacao', function(Transacao) {
                            return Transacao.get(Id).$promise;
                        }]
                    }
                }).result.then(function() {

                    //Transacao.delete(Id);
                    loadAll();
                }, function() {
                    
                });
        };

        $scope.editarTransacao = function(Id) {
            $uibModal.open({
                    templateUrl: 'app/entities/transacao/transacao-dialog.html',
                    controller: 'TransacaoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Transacao', function(Transacao) {
                            return Transacao.get(Id).$promise;
                        }]
                    }
                }).result.then(function() {
    
                    loadAll();
                }, function() {
                    
                });
        }

        $scope.editarSituacao = function(Id){

             $uibModal.open({
                    templateUrl: 'app/entities/situacao/situacao-dialog.html',
                    controller: 'SituacaoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Situacao', function(Situacao) {
                            return Situacao.get(Id).$promise;
                        }]
                    }
                }).result.then(function() {
                    loadAll();
                }, function() {
                    
                });

        }

        $scope.newSituacao = function(){
            $uibModal.open({
                    templateUrl: 'app/entities/situacao/situacao-dialog.html',
                    controller: 'SituacaoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                servico: vm.servico,
                                tipo: null,
                                descricao: null,
                                terceiro: null,
                                dtinicio: null,
                                dtfim: null,
                                dtestipulada: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    loadAll();
                }, function() {
                    
                });
        }

        $scope.editSituacao = function(Id){
            $uibModal.open({
                    templateUrl: 'app/entities/situacao/situacao-dialog.html',
                    controller: 'SituacaoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Situacao', function(Situacao) {
                            return Situacao.get(Id).$promise;
                        }]
                    }
                }).result.then(function() {
                    loadAll();
                }, function() {
                    
                });
        }

        $scope.delSituacao = function(Id){
            $uibModal.open({
                    templateUrl: 'app/entities/situacao/situacao-delete-dialog.html',
                    controller: 'SituacaoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Situacao', function(Situacao) {
                            return Situacao.get(Id).$promise;
                        }]
                    }
                }).result.then(function() {
                    loadAll();
                }, function() {
                    
                });
        }



        var unsubscribe = $rootScope.$on('hmProjetosApp:servicoUpdate', function(event, result) {
            vm.servico = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
