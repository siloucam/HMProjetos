(function() {
    'use strict';

    angular
    .module('hmProjetosApp')
    .controller('ClienteDetailController', ClienteDetailController);

    ClienteDetailController.$inject = ['$http','$uibModal','$log','$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Cliente', 'Telefone', 'Servico'];

    function ClienteDetailController($http, $uibModal, $log, $scope, $rootScope, $stateParams, previousState, entity, Cliente, Telefone, Servico) {
        var vm = this;

        vm.cliente = entity;
        vm.previousState = previousState.name;

        vm.telefones = [];
        vm.servicos = [];

        loadAll();

        function loadAll() {

            Telefone.queryByCliente({Cid: vm.cliente.id}, function(result) {
                vm.telefones = result;
                vm.searchQuery = null;
            });

            Servico.queryByCliente({Cid: vm.cliente.id}, function(result) {
                vm.servicos = result;
                vm.searchQuery = null;
            });

        }

        var unsubscribe = $rootScope.$on('hmProjetosApp:clienteUpdate', function(event, result) {
            vm.cliente = result;
        });
        $scope.$on('$destroy', unsubscribe);

        $scope.TemTelefone = function() {
            var tem = false;
            if(vm.telefones.length > 0) tem = true;
            return tem;
        };

        $scope.TemServico = function() {
            var tem = false;
            if(vm.servicos.length > 0) tem = true;
            return tem;
        };

        $scope.deleteTel = function(Id) {
            $uibModal.open({
                templateUrl: 'app/entities/telefone/telefone-delete-dialog.html',
                controller: 'TelefoneDeleteController',
                controllerAs: 'vm',
                size: 'md',
                resolve: {
                    entity: ['Telefone', function(Telefone) {
                        return Telefone.get(Id).$promise;
                    }]
                }
            }).result.then(function() {
                vm.telefones = [];
                Telefone.delete(Id);
                loadAll();
            }, function() {

            });
        };

        $scope.newTel = function(){
            $uibModal.open({
                templateUrl: 'app/entities/cliente/cliente-telefone-dialog.html',
                controller: 'ClienteTelefoneDialogController',
                controllerAs: 'vm',
                backdrop: 'static',
                size: 'md',
                resolve: {
                    entity: function () {
                        return {
                            cliente: vm.cliente,
                            contato: null,
                            numero: null,
                            id: null
                        };
                    }
                }
            }).result.then(function() {
                vm.servicos = [];
                vm.telefones = [];
                loadAll();
            }, function() {

            });
        }

        $scope.newServico = function(){
            $uibModal.open({
                templateUrl: 'app/entities/servico/servico-dialog.html',
                controller: 'ServicoDialogController',
                controllerAs: 'vm',
                backdrop: 'static',
                size: 'lg',
                resolve: {
                    entity: function () {
                        return {
                            cliente: vm.cliente,
                            tipo: null,
                            codigo: null,
                            descricao: null,
                            endereco: null,
                            bairro: null,
                            cidade: null,
                            estado: null,
                            cep: null,
                            inicio: null,
                            fim: null,
                            iptu: null,
                            link: null,
                            id: null
                        };
                    }
                }
            }).result.then(function($stateParams) {
              vm.servicos = [];
              vm.telefones = [];
              loadAll();
          }, function() {

          });
        }




    }
})();
