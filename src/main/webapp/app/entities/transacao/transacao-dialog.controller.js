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

        // var parcelas_servico = [];

        // for(var i=0;i<vm.parcelas.length;i++){

        //         if(vm.parcelas[i].servicoid == vm.servico.id){
        //             parcelas_servico.push(vm.parcelas[i]);
        //             }
        //         }

        // vm.parcelas = parcelas_servico;
        
        vm.orcamento = Orcamento.queryByServico(vm.transacao.servico);

        // $scope.loadParcelas = function(){
        
        // console.log("D");

        // console.log(vm.transacao.servico.id);

        // console.log(vm.orcamento);

     

     $timeout(function () {
        vm.parcelas = Parcela.queryByOrcamento(vm.orcamento[0]);
    }, 1000);

        // console.log(vm.parcelas);

        // }



        // Promise.resolve(Orcamento.queryByServico(vm.transacao.servico)).then(function(value) {
        //     console.log(value); // "Success"
        //     console.log("Success for Orcamento");
        //     vm.orcamento = value;

        //     Promise.resolve(Parcela.queryByOrcamento(vm.orcamento)).then(function(value2) {
        //     console.log(value2); // "Success"
        //     console.log("Success for Parcela");

        //     vm.parcelas = value2;
        // }, function(value2) {
        //  // not called
        // });


        // }, function(value) {
        //  // not called
        // });



        

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.transacao.id !== null) {
                Transacao.update(vm.transacao, onSaveSuccess, onSaveError);
            } else {
                Transacao.save(vm.transacao, onSaveSuccess, onSaveError);
            }
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
