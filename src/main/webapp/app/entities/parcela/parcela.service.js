(function() {
    'use strict';
    angular
        .module('hmProjetosApp')
        .factory('Parcela', Parcela);

    Parcela.$inject = ['$resource', 'DateUtils'];

    function Parcela ($resource, DateUtils) {
        var resourceUrl =  'api/parcelas/:id';

        return $resource(resourceUrl, {}, {
            'queryByOrcamento': {
                url: 'api/parcelas/orcamentos/:id',
                method: 'GET',
                isArray: true
            },
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dtestipulada = DateUtils.convertLocalDateFromServer(data.dtestipulada);
                        data.dtefetuada = DateUtils.convertLocalDateFromServer(data.dtefetuada);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dtestipulada = DateUtils.convertLocalDateToServer(copy.dtestipulada);
                    copy.dtefetuada = DateUtils.convertLocalDateToServer(copy.dtefetuada);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.dtestipulada = DateUtils.convertLocalDateToServer(copy.dtestipulada);
                    copy.dtefetuada = DateUtils.convertLocalDateToServer(copy.dtefetuada);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
