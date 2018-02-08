(function() {
    'use strict';
    angular
        .module('hmProjetosApp')
        .factory('Situacao', Situacao);

    Situacao.$inject = ['$resource', 'DateUtils'];

    function Situacao ($resource, DateUtils) {
        var resourceUrl =  'api/situacaos/:id';

        return $resource(resourceUrl, {}, {
            'queryByServico': {
                url: 'api/situacaos/servicos/:id',
                method: 'GET',
                isArray: true
            },
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dtinicio = DateUtils.convertLocalDateFromServer(data.dtinicio);
                        data.dtfim = DateUtils.convertLocalDateFromServer(data.dtfim);
                        data.dtestipulada = DateUtils.convertLocalDateFromServer(data.dtestipulada);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dtinicio = DateUtils.convertLocalDateToServer(copy.dtinicio);
                    copy.dtfim = DateUtils.convertLocalDateToServer(copy.dtfim);
                    copy.dtestipulada = DateUtils.convertLocalDateToServer(copy.dtestipulada);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dtinicio = DateUtils.convertLocalDateToServer(copy.dtinicio);
                    copy.dtfim = DateUtils.convertLocalDateToServer(copy.dtfim);
                    copy.dtestipulada = DateUtils.convertLocalDateToServer(copy.dtestipulada);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
