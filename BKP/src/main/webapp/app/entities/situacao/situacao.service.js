(function() {
    'use strict';
    angular
        .module('hmProjetosApp')
        .factory('Situacao', Situacao);

    Situacao.$inject = ['$resource', 'DateUtils'];

    function Situacao ($resource, DateUtils) {
        var resourceUrl =  'api/situacaos/:id';

        return $resource(resourceUrl, {}, {
            'queryByTipo':{
                url: 'api/situacaos/?tipoId.equals=:Tid&dtfim.specified=false',
                method: 'GET',
                isArray: true
            },
            'queryByResponsavel':{
                url: 'api/situacaos/?responsavelId.equals=:Rid&dtfim.specified=false',
                method: 'GET',
                isArray: true
            },
            'queryByTipoResponsavel':{
                url: 'api/situacaos/?tipoId.equals=:Tid&dtfim.specified=false&responsavelId.equals=:Rid',
                method: 'GET',
                isArray: true
            },
            'queryByServico': {
                url: 'api/situacaos/?servicoId.equals=:Cid',
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
