(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('situacao', {
            parent: 'entity',
            url: '/situacao',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'hmProjetosApp.situacao.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/situacao/situacaos.html',
                    controller: 'SituacaoController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('situacao');
                    $translatePartialLoader.addPart('tipoSituacao');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('situacao-detail', {
            parent: 'situacao',
            url: '/situacao/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'hmProjetosApp.situacao.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/situacao/situacao-detail.html',
                    controller: 'SituacaoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('situacao');
                    $translatePartialLoader.addPart('tipoSituacao');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Situacao', function($stateParams, Situacao) {
                    return Situacao.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'situacao',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('situacao-detail.edit', {
            parent: 'situacao-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/situacao/situacao-dialog.html',
                    controller: 'SituacaoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Situacao', function(Situacao) {
                            return Situacao.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('situacao.new', {
            parent: 'situacao',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/situacao/situacao-dialog.html',
                    controller: 'SituacaoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                tipo: null,
                                descricao: null,
                                terceiro: null,
                                dtinicio: null,
                                dtfim: null,
                                dtestipulada: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('situacao', null, { reload: 'situacao' });
                }, function() {
                    $state.go('situacao');
                });
            }]
        })
        .state('situacao.edit', {
            parent: 'situacao',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/situacao/situacao-dialog.html',
                    controller: 'SituacaoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Situacao', function(Situacao) {
                            return Situacao.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('situacao', null, { reload: 'situacao' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('situacao.delete', {
            parent: 'situacao',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/situacao/situacao-delete-dialog.html',
                    controller: 'SituacaoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Situacao', function(Situacao) {
                            return Situacao.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('situacao', null, { reload: 'situacao' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
