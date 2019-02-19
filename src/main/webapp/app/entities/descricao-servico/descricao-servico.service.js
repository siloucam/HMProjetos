(function() {
    'use strict';
    angular
        .module('hmProjetosApp')
        .factory('DescricaoServico', DescricaoServico);

    DescricaoServico.$inject = ['$resource'];

    function DescricaoServico ($resource) {
        var resourceUrl =  'api/descricao-servicos/:id';

        return $resource(resourceUrl, {}, {
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
