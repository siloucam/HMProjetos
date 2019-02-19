(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('transacao', {
            parent: 'entity',
            url: '/transacao',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'hmProjetosApp.transacao.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/transacao/transacaos.html',
                    controller: 'TransacaoController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('transacao');
                    $translatePartialLoader.addPart('oP');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('transacao-detail', {
            parent: 'transacao',
            url: '/transacao/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'hmProjetosApp.transacao.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/transacao/transacao-detail.html',
                    controller: 'TransacaoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('transacao');
                    $translatePartialLoader.addPart('oP');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Transacao', function($stateParams, Transacao) {
                    return Transacao.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'transacao',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('transacao-detail.edit', {
            parent: 'transacao-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/transacao/transacao-dialog.html',
                    controller: 'TransacaoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Transacao', function(Transacao) {
                            return Transacao.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('transacao.new', {
            parent: 'transacao',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/transacao/transacao-dialog.html',
                    controller: 'TransacaoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                valor: null,
                                data: null,
                                descricao: null,
                                operacao: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('transacao', null, { reload: 'transacao' });
                }, function() {
                    $state.go('transacao');
                });
            }]
        })
        .state('transacao.edit', {
            parent: 'transacao',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/transacao/transacao-dialog.html',
                    controller: 'TransacaoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Transacao', function(Transacao) {
                            return Transacao.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('transacao', null, { reload: 'transacao' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('transacao.delete', {
            parent: 'transacao',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/transacao/transacao-delete-dialog.html',
                    controller: 'TransacaoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Transacao', function(Transacao) {
                            return Transacao.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('transacao', null, { reload: 'transacao' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
