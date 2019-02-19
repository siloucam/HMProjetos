(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('descricao-servico', {
            parent: 'entity',
            url: '/descricao-servico',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'hmProjetosApp.descricaoServico.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/descricao-servico/descricao-servicos.html',
                    controller: 'DescricaoServicoController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('descricaoServico');
                    $translatePartialLoader.addPart('tipoServico');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('descricao-servico-detail', {
            parent: 'descricao-servico',
            url: '/descricao-servico/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'hmProjetosApp.descricaoServico.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/descricao-servico/descricao-servico-detail.html',
                    controller: 'DescricaoServicoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('descricaoServico');
                    $translatePartialLoader.addPart('tipoServico');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'DescricaoServico', function($stateParams, DescricaoServico) {
                    return DescricaoServico.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'descricao-servico',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('descricao-servico-detail.edit', {
            parent: 'descricao-servico-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/descricao-servico/descricao-servico-dialog.html',
                    controller: 'DescricaoServicoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DescricaoServico', function(DescricaoServico) {
                            return DescricaoServico.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('descricao-servico.new', {
            parent: 'descricao-servico',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/descricao-servico/descricao-servico-dialog.html',
                    controller: 'DescricaoServicoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nome: null,
                                tipo: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('descricao-servico', null, { reload: 'descricao-servico' });
                }, function() {
                    $state.go('descricao-servico');
                });
            }]
        })
        .state('descricao-servico.edit', {
            parent: 'descricao-servico',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/descricao-servico/descricao-servico-dialog.html',
                    controller: 'DescricaoServicoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DescricaoServico', function(DescricaoServico) {
                            return DescricaoServico.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('descricao-servico', null, { reload: 'descricao-servico' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('descricao-servico.delete', {
            parent: 'descricao-servico',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/descricao-servico/descricao-servico-delete-dialog.html',
                    controller: 'DescricaoServicoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['DescricaoServico', function(DescricaoServico) {
                            return DescricaoServico.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('descricao-servico', null, { reload: 'descricao-servico' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
