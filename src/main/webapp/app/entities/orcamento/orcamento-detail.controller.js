(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('OrcamentoDetailController', OrcamentoDetailController);

    OrcamentoDetailController.$inject = ['$uibModal','$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Orcamento', 'Servico', 'Parcela'];

    function OrcamentoDetailController($uibModal, $scope, $rootScope, $stateParams, previousState, entity, Orcamento, Servico, Parcela) {
        var vm = this;

        vm.orcamento = entity;
        vm.previousState = previousState.name;

        vm.parcelas = [];

        loadAll();

        var style;
 
        function loadAll() {
            Parcela.queryByOrcamento({id: vm.orcamento.id}, function(result) {
                vm.parcelas = result;
                vm.searchQuery = null;
            });
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
                    Parcela.delete(Id);
                    loadAll();
                }, function() {
                    
                });
        }






    }
})();
