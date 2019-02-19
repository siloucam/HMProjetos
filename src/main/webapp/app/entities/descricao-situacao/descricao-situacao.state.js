(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('descricao-situacao', {
            parent: 'entity',
            url: '/descricao-situacao',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'hmProjetosApp.descricaoSituacao.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/descricao-situacao/descricao-situacaos.html',
                    controller: 'DescricaoSituacaoController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('descricaoSituacao');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('descricao-situacao-detail', {
            parent: 'descricao-situacao',
            url: '/descricao-situacao/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'hmProjetosApp.descricaoSituacao.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/descricao-situacao/descricao-situacao-detail.html',
                    controller: 'DescricaoSituacaoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('descricaoSituacao');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'DescricaoSituacao', function($stateParams, DescricaoSituacao) {
                    return DescricaoSituacao.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'descricao-situacao',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('descricao-situacao-detail.edit', {
            parent: 'descricao-situacao-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/descricao-situacao/descricao-situacao-dialog.html',
                    controller: 'DescricaoSituacaoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DescricaoSituacao', function(DescricaoSituacao) {
                            return DescricaoSituacao.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('descricao-situacao.new', {
            parent: 'descricao-situacao',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/descricao-situacao/descricao-situacao-dialog.html',
                    controller: 'DescricaoSituacaoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nome: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('descricao-situacao', null, { reload: 'descricao-situacao' });
                }, function() {
                    $state.go('descricao-situacao');
                });
            }]
        })
        .state('descricao-situacao.edit', {
            parent: 'descricao-situacao',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/descricao-situacao/descricao-situacao-dialog.html',
                    controller: 'DescricaoSituacaoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DescricaoSituacao', function(DescricaoSituacao) {
                            return DescricaoSituacao.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('descricao-situacao', null, { reload: 'descricao-situacao' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('descricao-situacao.delete', {
            parent: 'descricao-situacao',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/descricao-situacao/descricao-situacao-delete-dialog.html',
                    controller: 'DescricaoSituacaoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['DescricaoSituacao', function(DescricaoSituacao) {
                            return DescricaoSituacao.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('descricao-situacao', null, { reload: 'descricao-situacao' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
