(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('servico', {
            parent: 'entity',
            url: '/servico',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'hmProjetosApp.servico.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/servico/servicos.html',
                    controller: 'ServicoController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('servico');
                    $translatePartialLoader.addPart('tipoServico');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('servico-detail', {
            parent: 'servico',
            url: '/servico/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'hmProjetosApp.servico.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/servico/servico-detail.html',
                    controller: 'ServicoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('servico');
                    $translatePartialLoader.addPart('tipoServico');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Servico', function($stateParams, Servico) {
                    return Servico.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'servico',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('servico-detail.edit', {
            parent: 'servico-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/servico/servico-dialog.html',
                    controller: 'ServicoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Servico', function(Servico) {
                            return Servico.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('servico.new', {
            parent: 'servico',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/servico/servico-dialog.html',
                    controller: 'ServicoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                tipo: null,
                                codigo: null,
                                descricao: null,
                                endereco: null,
                                bairro: null,
                                cidade: null,
                                estado: null,
                                cep: null,
                                inicio: null,
                                fim: null,
                                iptu: null,
                                link: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function($stateParams) {
                     $state.go('servico-detail', $stateParams);
                }, function() {
                    $state.go('servico');
                });
            }]
        })
        .state('servico.edit', {
            parent: 'servico',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/servico/servico-dialog.html',
                    controller: 'ServicoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Servico', function(Servico) {
                            return Servico.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('servico', null, { reload: 'servico' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('servico.delete', {
            parent: 'servico',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/servico/servico-delete-dialog.html',
                    controller: 'ServicoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Servico', function(Servico) {
                            return Servico.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('servico', null, { reload: 'servico' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
