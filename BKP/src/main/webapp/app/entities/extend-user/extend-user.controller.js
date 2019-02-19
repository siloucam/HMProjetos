(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .controller('ExtendUserController', ExtendUserController);

    ExtendUserController.$inject = ['ExtendUser'];

    function ExtendUserController(ExtendUser) {

        var vm = this;

        vm.extendUsers = [];

        loadAll();

        function loadAll() {
            ExtendUser.query(function(result) {
                vm.extendUsers = result;
                vm.searchQuery = null;
            });
        }
    }
})();
