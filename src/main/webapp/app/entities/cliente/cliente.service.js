(function() {
    'use strict';
    angular
        .module('hmProjetosApp')
        .factory('Cliente', Cliente);

    Cliente.$inject = ['$log','$resource'];

    function Cliente ($log, $resource) {
        var resourceUrl =  'api/clientes/:id';

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
