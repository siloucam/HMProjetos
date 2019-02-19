(function() {
    'use strict';
    angular
        .module('hmProjetosApp')
        .factory('DescricaoSituacao', DescricaoSituacao);

    DescricaoSituacao.$inject = ['$resource'];

    function DescricaoSituacao ($resource) {
        var resourceUrl =  'api/descricao-situacaos/:id';

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
