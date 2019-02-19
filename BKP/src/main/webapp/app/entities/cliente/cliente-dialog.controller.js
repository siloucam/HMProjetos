(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('ClienteDialogController', ClienteDialogController);

    ClienteDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Cliente', 'Telefone', 'Servico'];

    function ClienteDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Cliente, Telefone, Servico) {
        var vm = this;

        vm.cliente = entity;
        vm.clear = clear;
        vm.save = save;
        // vm.telefones = Telefone.query();

        vm.telefones = [];

        vm.newtelcontato;
        vm.newtelnumero;

        vm.cepinvalido = false;

        if(vm.cliente.id!=null)
            loadAll();

        vm.servicos = Servico.query();

        var limpa_formulario_cep = function(){
            vm.cliente.endereco = "";
            vm.cliente.bairro = "";
            vm.cliente.cidade = "";
            vm.cliente.estado = "";
        }

        $scope.buscaCEP = function(){
                //Nova variável "cep" somente com dígitos.
                var cep = vm.cliente.cep;

                //Verifica se campo cep possui valor informado.
                if (cep != "") {

                    //Expressão regular para validar o CEP.
                    var validacep = /^[0-9]{8}$/;

                    //Valida o formato do CEP.
                    if(validacep.test(cep)) {

                        //Preenche os campos com "..." enquanto consulta webservice.
                        vm.cliente.endereco = "...";
                        vm.cliente.bairro = "...";
                        vm.cliente.cidade = "...";
                        vm.cliente.estado = "..";

                        //Consulta o webservice viacep.com.br/
                        $.getJSON("https://viacep.com.br/ws/"+ cep +"/json/?callback=?", function(dados) {

                            console.log(dados);

                            if (!("erro" in dados)) {
                                //Atualiza os campos com os valores da consulta.
                                vm.cepinvalido = false;
                                vm.cliente.endereco = dados.logradouro;
                                vm.cliente.bairro = dados.bairro;
                                vm.cliente.cidade = dados.localidade;
                                vm.cliente.estado = dados.uf;
                                $scope.$apply();
                            } //end if.
                            else {
                                //CEP pesquisado não foi encontrado.
                                limpa_formulario_cep();
                                //alert("CEP não encontrado.");
                                vm.cepinvalido = true;
                                $scope.$apply();
                            }
                        });
                    } //end if.
                    else {
                        //cep é inválido.
                        limpa_formulario_cep();
                        vm.cepinvalido = true;
                        $scope.$apply();
                        //alert("Formato de CEP inválido.");
                    }
                } //end if.
                else {
                    //cep sem valor, limpa formulário.
                    limpa_formulario_cep();
                    $scope.$apply()
                }

            }

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
            $scope.$emit('hmProjetosApp:clienteUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;

            if(vm.contato!=null && vm.numero!=null){
                    var telefone = new Telefone();
                    telefone.contato = vm.contato;
                    telefone.numero = vm.numero;
                    telefone.cliente = result;
                    Telefone.save(telefone);
                }

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
