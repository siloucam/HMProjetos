(function() {
    'use strict';
    angular
        .module('hmProjetosApp')
        .factory('Orcamento', Orcamento);

    Orcamento.$inject = ['$resource'];

    function Orcamento ($resource) {
        var resourceUrl =  'api/orcamentos/:id';

        return $resource(resourceUrl, {}, {
            'queryByServico': {
                url: 'api/orcamentos/servicos/:id',
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
