(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('LinkExternoDialogController', LinkExternoDialogController);

    LinkExternoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'LinkExterno', 'Servico'];

    function LinkExternoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, LinkExterno, Servico) {
        var vm = this;

        vm.linkExterno = entity;
        vm.clear = clear;
        vm.save = save;
        vm.servicos = Servico.query();
        vm.iptu = null;

        var iptu;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {

            //cria link pro IPTU
            if(vm.linkExterno.nome){

                vm.iptu = vm.linkExterno.nome;

                vm.linkExterno.link = "http://www2.cachoeiro.es.gov.br:8080/ZimWeb/servlet/ZII?connection=producao&program=pwci001&vudTipoDocum=9&vudNoDocum="+iptu+"&vexercicio=2019";
                vm.linkExterno.nome = "IPTU " +  vm.iptu;
            }else{
                vm.linkExterno.nome = vm.linkExterno.link;
            }

            vm.isSaving = true;
            if (vm.linkExterno.id !== null) {
                LinkExterno.update(vm.linkExterno, onSaveSuccess, onSaveError);
            } else {
                LinkExterno.save(vm.linkExterno, onSaveSuccess2, onSaveError);
            }
        }

        function onSaveSuccess2 (result) {
            $scope.$emit('hmProjetosApp:linkExternoUpdate', result);

            vm.linkExterno.link = "http://www2.cachoeiro.es.gov.br:8080/ZimWeb/servlet/ZII?vudTipoDocum=3&vudNoDocum="+iptu+"&debug=true&connection=producao&program=pwud001&template=%28vudTipoDocum%2CvudNoDocum%2CvudFinalidade%29&B1=Pesquisar";
            vm.linkExterno.nome = "CND " +  vm.iptu;

            LinkExterno.save(vm.linkExterno, onSaveSuccess, onSaveError);

            // $uibModalInstance.close(result);
            // vm.isSaving = false;
        }

        function onSaveSuccess (result) {
            $scope.$emit('hmProjetosApp:linkExternoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
