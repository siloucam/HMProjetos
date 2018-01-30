(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('ServicoDetailController', ServicoDetailController);

    ServicoDetailController.$inject = ['$uibModal','$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Servico', 'Situacao', 'Transacao', 'Orcamento', 'Cliente'];

    function ServicoDetailController($uibModal, $scope, $rootScope, $stateParams, previousState, entity, Servico, Situacao, Transacao, Orcamento, Cliente) {
        var vm = this;

        vm.servico = entity;
        vm.previousState = previousState.name;

        vm.orcamentos = [];
        vm.transacaos = [];

        loadAll();

        function loadAll() {
            Orcamento.queryByServico({id: vm.servico.id}, function(result) {
                vm.orcamentos = result;
                vm.searchQuery = null;
            });
            Transacao.queryByServico({id: vm.servico.id}, function(result) {
                vm.transacaos = result;
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
                    vm.orcamentos = [];
                    vm.transacaos = [];
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
                    vm.orcamentos = [];
                    vm.transacaos = [];
                    loadAll();
                    //$state.go('transacao', null, { reload: 'transacao' });
                }, function() {
                    //$state.go('transacao');
                });
        }

        var unsubscribe = $rootScope.$on('hmProjetosApp:servicoUpdate', function(event, result) {
            vm.servico = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
