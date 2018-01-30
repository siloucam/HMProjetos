(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('parcela', {
            parent: 'entity',
            url: '/parcela',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'hmProjetosApp.parcela.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/parcela/parcelas.html',
                    controller: 'ParcelaController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('parcela');
                    $translatePartialLoader.addPart('statusParcela');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('parcela-detail', {
            parent: 'parcela',
            url: '/parcela/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'hmProjetosApp.parcela.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/parcela/parcela-detail.html',
                    controller: 'ParcelaDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('parcela');
                    $translatePartialLoader.addPart('statusParcela');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Parcela', function($stateParams, Parcela) {
                    return Parcela.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'parcela',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('parcela-detail.edit', {
            parent: 'parcela-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/parcela/parcela-dialog.html',
                    controller: 'ParcelaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Parcela', function(Parcela) {
                            return Parcela.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('parcela.new', {
            parent: 'parcela',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/parcela/parcela-dialog.html',
                    controller: 'ParcelaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                descricao: null,
                                status: null,
                                valor: null,
                                dtestipulada: null,
                                dtefetuada: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('parcela', null, { reload: 'parcela' });
                }, function() {
                    $state.go('parcela');
                });
            }]
        })
        .state('parcela.edit', {
            parent: 'parcela',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/parcela/parcela-dialog.html',
                    controller: 'ParcelaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Parcela', function(Parcela) {
                            return Parcela.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('parcela', null, { reload: 'parcela' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('parcela.delete', {
            parent: 'parcela',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/parcela/parcela-delete-dialog.html',
                    controller: 'ParcelaDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Parcela', function(Parcela) {
                            return Parcela.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('parcela', null, { reload: 'parcela' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
