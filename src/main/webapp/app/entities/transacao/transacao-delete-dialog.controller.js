(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('TransacaoDeleteController',TransacaoDeleteController);

    TransacaoDeleteController.$inject = ['$uibModalInstance', 'entity', 'Transacao','Parcela'];

    function TransacaoDeleteController($uibModalInstance, entity, Transacao, Parcela) {
        var vm = this;

        vm.transacao = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {

            console.log(vm.transacao);

            if(vm.transacao.parcela != null){

                vm.transacao.parcela.dtefetuada = null;

                Parcela.update(vm.transacao.parcela);

            }


            Transacao.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }


       





    }
})();
