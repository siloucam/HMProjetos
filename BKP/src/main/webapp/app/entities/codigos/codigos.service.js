(function() {
    'use strict';
    angular
        .module('hmProjetosApp')
        .factory('Codigos', Codigos);

    Codigos.$inject = ['$resource'];

    function Codigos ($resource) {
        var resourceUrl =  'api/codigos/:id';

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
