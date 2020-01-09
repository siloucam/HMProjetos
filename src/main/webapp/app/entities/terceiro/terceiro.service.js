(function() {
    'use strict';
    angular
        .module('hmProjetosApp')
        .factory('Terceiro', Terceiro);

    Terceiro.$inject = ['$resource'];

    function Terceiro ($resource) {
        var resourceUrl =  'api/terceiros/:id';

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
