(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('OrcamentoDialogController', OrcamentoDialogController);

    OrcamentoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Orcamento', 'Servico', 'Parcela'];

    function OrcamentoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Orcamento, Servico, Parcela) {
        var vm = this;

        vm.orcamento = entity;

       

        vm.clear = clear;
        vm.save = save;
        vm.servicos = Servico.query({filter: 'orcamento-is-null'});
        $q.all([vm.orcamento.$promise, vm.servicos.$promise]).then(function() {
            if (!vm.orcamento.servico || !vm.orcamento.servico.id) {
                return $q.reject();
            }
            return Servico.get({id : vm.orcamento.servico.id}).$promise;
        }).then(function(servico) {
            vm.servicos.push(servico);
        });
        vm.parcelas = Parcela.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.orcamento.id !== null) {
                Orcamento.update(vm.orcamento, onSaveSuccess, onSaveError);
            } else {
                Orcamento.save(vm.orcamento, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('hmProjetosApp:orcamentoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
