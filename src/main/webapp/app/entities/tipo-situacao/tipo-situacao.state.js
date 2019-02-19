(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('tipo-situacao', {
            parent: 'entity',
            url: '/tipo-situacao',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'hmProjetosApp.tipoSituacao.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tipo-situacao/tipo-situacaos.html',
                    controller: 'TipoSituacaoController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('tipoSituacao');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('tipo-situacao-detail', {
            parent: 'tipo-situacao',
            url: '/tipo-situacao/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'hmProjetosApp.tipoSituacao.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tipo-situacao/tipo-situacao-detail.html',
                    controller: 'TipoSituacaoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('tipoSituacao');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'TipoSituacao', function($stateParams, TipoSituacao) {
                    return TipoSituacao.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'tipo-situacao',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('tipo-situacao-detail.edit', {
            parent: 'tipo-situacao-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tipo-situacao/tipo-situacao-dialog.html',
                    controller: 'TipoSituacaoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TipoSituacao', function(TipoSituacao) {
                            return TipoSituacao.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tipo-situacao.new', {
            parent: 'tipo-situacao',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tipo-situacao/tipo-situacao-dialog.html',
                    controller: 'TipoSituacaoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nome: null,
                                sigla: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('tipo-situacao', null, { reload: 'tipo-situacao' });
                }, function() {
                    $state.go('tipo-situacao');
                });
            }]
        })
        .state('tipo-situacao.edit', {
            parent: 'tipo-situacao',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tipo-situacao/tipo-situacao-dialog.html',
                    controller: 'TipoSituacaoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TipoSituacao', function(TipoSituacao) {
                            return TipoSituacao.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tipo-situacao', null, { reload: 'tipo-situacao' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tipo-situacao.delete', {
            parent: 'tipo-situacao',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tipo-situacao/tipo-situacao-delete-dialog.html',
                    controller: 'TipoSituacaoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TipoSituacao', function(TipoSituacao) {
                            return TipoSituacao.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tipo-situacao', null, { reload: 'tipo-situacao' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
