(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('orcamento', {
            parent: 'entity',
            url: '/orcamento',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'hmProjetosApp.orcamento.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/orcamento/orcamentos.html',
                    controller: 'OrcamentoController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('orcamento');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('orcamento-detail', {
            parent: 'orcamento',
            url: '/orcamento/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'hmProjetosApp.orcamento.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/orcamento/orcamento-detail.html',
                    controller: 'OrcamentoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('orcamento');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Orcamento', function($stateParams, Orcamento) {
                    return Orcamento.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'orcamento',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('orcamento-detail.edit', {
            parent: 'orcamento-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/orcamento/orcamento-dialog.html',
                    controller: 'OrcamentoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Orcamento', function(Orcamento) {
                            return Orcamento.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('orcamento.new', {
            parent: 'orcamento',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/orcamento/orcamento-dialog.html',
                    controller: 'OrcamentoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                valor: null,
                                entrada: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('orcamento', null, { reload: 'orcamento' });
                }, function() {
                    $state.go('orcamento');
                });
            }]
        })
        .state('orcamento.edit', {
            parent: 'orcamento',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/orcamento/orcamento-dialog.html',
                    controller: 'OrcamentoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Orcamento', function(Orcamento) {
                            return Orcamento.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('orcamento', null, { reload: 'orcamento' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('orcamento.delete', {
            parent: 'orcamento',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/orcamento/orcamento-delete-dialog.html',
                    controller: 'OrcamentoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Orcamento', function(Orcamento) {
                            return Orcamento.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('orcamento', null, { reload: 'orcamento' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
