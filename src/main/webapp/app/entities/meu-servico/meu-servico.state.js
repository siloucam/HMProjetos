(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('meu-servico', {
            parent: 'entity',
            url: '/meu-servico',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'hmProjetosApp.meuServico.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/meu-servico/meu-servicos.html',
                    controller: 'MeuServicoController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('meuServico');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('meu-servico-detail', {
            parent: 'meu-servico',
            url: '/meu-servico/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'hmProjetosApp.meuServico.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/meu-servico/meu-servico-detail.html',
                    controller: 'MeuServicoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('meuServico');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'MeuServico', function($stateParams, MeuServico) {
                    return MeuServico.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'meu-servico',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('meu-servico-detail.edit', {
            parent: 'meu-servico-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/meu-servico/meu-servico-dialog.html',
                    controller: 'MeuServicoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MeuServico', function(MeuServico) {
                            return MeuServico.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('meu-servico.new', {
            parent: 'meu-servico',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/meu-servico/meu-servico-dialog.html',
                    controller: 'MeuServicoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('meu-servico', null, { reload: 'meu-servico' });
                }, function() {
                    $state.go('meu-servico');
                });
            }]
        })
        .state('meu-servico.edit', {
            parent: 'meu-servico',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/meu-servico/meu-servico-dialog.html',
                    controller: 'MeuServicoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MeuServico', function(MeuServico) {
                            return MeuServico.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('meu-servico', null, { reload: 'meu-servico' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('meu-servico.delete', {
            parent: 'meu-servico',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/meu-servico/meu-servico-delete-dialog.html',
                    controller: 'MeuServicoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['MeuServico', function(MeuServico) {
                            return MeuServico.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('meu-servico', null, { reload: 'meu-servico' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
