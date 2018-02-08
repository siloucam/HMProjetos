(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('TransacaoDialogController', TransacaoDialogController);

    TransacaoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Transacao', 'Parcela', 'Servico', 'Orcamento'];

    function TransacaoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Transacao, Parcela, Servico, Orcamento) {
        var vm = this;

        vm.transacao = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        
        vm.save = save;
        vm.servicos = Servico.query();
        
        // vm.orcamento = Orcamento.queryByServico(vm.transacao.servico);
        vm.orcamento = [];

        Orcamento.queryByServico({id: vm.transacao.servico.id}, function(result) {
                vm.orcamento = result;

                Parcela.queryByOrcamento({id: result[0].id}, function(result) {
                
                var parcelas = [];

                //console.log(result);

                for(var i = 0; i < result.length; i++){
                    console.log(result[i]);
                    if(result[i].status != "EFETUADA"){
                        parcelas.push(result[i]);
                    }
                }

                vm.parcelas = parcelas;
                });

                vm.searchQuery = null;
            });


        $scope.chooseParcela = function(){
            vm.transacao.descricao = "Pagamento parcela " + vm.transacao.parcela.descricao;
            vm.transacao.valor = vm.transacao.parcela.valor;
        }
     

    //  $timeout(function () {
    //     vm.parcelas = Parcela.queryByOrcamento(vm.orcamento[0]);
    // }, 1000);
       

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.transacao.id !== null) {

                if(vm.transacao.parcela!=null){
                vm.transacao.parcela.dtefetuada = vm.transacao.data;

                Parcela.update(vm.transacao.parcela, onParcelaSaveSuccess, onParcelaSaveError);
                }

                Transacao.update(vm.transacao, onSaveSuccess, onSaveError);
            } else {

                //console.log(vm.transacao.parcela);
                if(vm.transacao.parcela!=null){
                vm.transacao.parcela.dtefetuada = vm.transacao.data;

                Parcela.update(vm.transacao.parcela, onParcelaSaveSuccess, onParcelaSaveError);
                }

                Transacao.save(vm.transacao, onSaveSuccess, onSaveError);
            }
        }

         function onParcelaSaveSuccess (result) {
            $scope.$emit('hmProjetosApp:parcelaUpdate', result);
        }

        function onParcelaSaveError () {
        
        }

        function onSaveSuccess (result) {
            $scope.$emit('hmProjetosApp:transacaoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.data = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
