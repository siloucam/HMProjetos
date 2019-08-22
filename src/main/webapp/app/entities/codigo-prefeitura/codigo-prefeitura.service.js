(function() {
    'use strict';
    angular
        .module('hmProjetosApp')
        .factory('CodigoPrefeitura', CodigoPrefeitura);

    CodigoPrefeitura.$inject = ['$resource'];

    function CodigoPrefeitura ($resource) {
        var resourceUrl =  'api/codigo-prefeituras/:id';

        return $resource(resourceUrl, {}, {
            'queryByServico': {
                url: 'api/codigo-prefeituras/?servicoId.equals=:Cid',
                method: 'GET',
                isArray: true
            },
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
