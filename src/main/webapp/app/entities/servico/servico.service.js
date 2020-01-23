(function() {
    'use strict';
    angular
        .module('hmProjetosApp')
        .factory('Servico', Servico);

    Servico.$inject = ['$resource', 'DateUtils'];

    function Servico ($resource, DateUtils) {
        var resourceUrl =  'api/servicos/:id';

        return $resource(resourceUrl, {}, {
            'queryFilterCodigo': {
                url: 'api/servicos/?codigo.contains=:filtro',
                method: 'GET',
                isArray: true
            },
            'queryFilterCliente': {
                url: 'api/servicos/?clienteNome.contains=:nome',
                method: 'GET',
                isArray: true
            },
            'queryByCodigo': {
                url: 'api/servicos/?codigo.equals=:codigo',
                method: 'GET',
                isArray: true
            },
            'queryByCliente': {
                url: 'api/servicos/?clienteId.equals=:Cid',
                method: 'GET',
                isArray: true
            },
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.inicio = DateUtils.convertLocalDateFromServer(data.inicio);
                        data.fim = DateUtils.convertLocalDateFromServer(data.fim);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.inicio = DateUtils.convertLocalDateToServer(copy.inicio);
                    copy.fim = DateUtils.convertLocalDateToServer(copy.fim);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.inicio = DateUtils.convertLocalDateToServer(copy.inicio);
                    copy.fim = DateUtils.convertLocalDateToServer(copy.fim);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
