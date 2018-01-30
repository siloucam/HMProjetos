(function() {
    'use strict';
    angular
        .module('hmProjetosApp')
        .factory('Transacao', Transacao);

    Transacao.$inject = ['$resource', 'DateUtils'];

    function Transacao ($resource, DateUtils) {
        var resourceUrl =  'api/transacaos/:id';

        return $resource(resourceUrl, {}, {
            'queryByServico': {
                url: 'api/transacaos/servicos/:id',
                method: 'GET',
                isArray: true
            },
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.data = DateUtils.convertLocalDateFromServer(data.data);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.data = DateUtils.convertLocalDateToServer(copy.data);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.data = DateUtils.convertLocalDateToServer(copy.data);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
