(function() {
    'use strict';
    angular
        .module('hmProjetosApp')
        .factory('LinkExterno', LinkExterno);

    LinkExterno.$inject = ['$resource'];

    function LinkExterno ($resource) {
        var resourceUrl =  'api/link-externos/:id';

        return $resource(resourceUrl, {}, {
            'queryByServico': {
                url: 'api/link-externos/?servicoId.equals=:Cid',
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
