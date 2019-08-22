(function() {
    'use strict';
    angular
        .module('hmProjetosApp')
        .factory('ExtendUser', ExtendUser);

    ExtendUser.$inject = ['$resource'];

    function ExtendUser ($resource) {
        var resourceUrl =  'api/extend-users/:id';

        return $resource(resourceUrl, {}, {
            'queryByUser':{
                url: 'api/extend-users?userId.equals=:Uid',
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
