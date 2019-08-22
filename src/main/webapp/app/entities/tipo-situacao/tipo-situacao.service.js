(function() {
    'use strict';
    angular
        .module('hmProjetosApp')
        .factory('TipoSituacao', TipoSituacao);

    TipoSituacao.$inject = ['$resource'];

    function TipoSituacao ($resource) {
        var resourceUrl =  'api/tipo-situacaos/:id';

        return $resource(resourceUrl, {}, {
            'queryById' : {
                url: 'api/tipo-situacaos/?id.equals=:iid',
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
