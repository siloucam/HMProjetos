(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('ClienteDialogController', ClienteDialogController);

    ClienteDialogController.$inject = ['$window','$state','$uibModal','$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Cliente', 'Telefone'];

    function ClienteDialogController ($window, $state, $uibModal, $timeout, $scope, $stateParams, $uibModalInstance, entity, Cliente, Telefone) {
        var vm = this;

        vm.cliente = entity;
        vm.clear = clear;
        vm.save = save;
        vm.telefones = [];

        vm.newtelcontato;
        vm.newtelnumero;

        if(vm.cliente.id!=null)
        loadAll();

        function loadAll() {
            Telefone.queryByCliente({id: vm.cliente.id}, function(result) {
                vm.telefones = result;
                console.log(vm.cliente.id);
                console.log(vm.telefones);
                vm.searchQuery = null;
            });
        }

        $scope.TemTelefone = function() {
            var tem = false;
            if(vm.telefones.length > 0) tem = true;
            return tem;
        };

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {

           
            vm.isSaving = true;
            if (vm.cliente.id !== null) {
                Cliente.update(vm.cliente, onSaveSuccess, onSaveError);
            } else {
                Cliente.save(vm.cliente, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            console.log("cliente salvo");

            $scope.$emit('hmProjetosApp:clienteUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;


            console.log("!");
        }

        function onSaveError () {
            vm.isSaving = false;
        }


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
                    vm.telefones = [];
                    loadAll();
                }, function() {
                    
                });
        }

        function saveTel () {
            console.log("AAAAAAAAAAA");

            vm.isSaving = true;
            
            var newtel;
            newtel.cliente = vm.cliente;
            newtel.contato =  vm.newtelcontato;
            newtel.numero =  vm.newtelnumero;
            newtel.id = null;

            console.log(newtel);

            Telefone.save(newtel, onSaveSuccessTel, onSaveErrorTel);
        }

        function onSaveSuccessTel (result) {
            $scope.$emit('hmProjetosApp:telefoneUpdate', result);
            //$uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveErrorTel () {
            vm.isSaving = false;
        }




    }
})();
