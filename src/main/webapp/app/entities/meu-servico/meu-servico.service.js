(function() {
    'use strict';
    angular
        .module('hmProjetosApp')
        .factory('MeuServico', MeuServico);

    MeuServico.$inject = ['$resource'];

    function MeuServico ($resource) {
        var resourceUrl =  'api/meu-servicos/:id';

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
