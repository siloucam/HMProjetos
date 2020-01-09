(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('terceiro', {
            parent: 'entity',
            url: '/terceiro',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'hmProjetosApp.terceiro.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/terceiro/terceiros.html',
                    controller: 'TerceiroController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('terceiro');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('terceiro-detail', {
            parent: 'terceiro',
            url: '/terceiro/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'hmProjetosApp.terceiro.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/terceiro/terceiro-detail.html',
                    controller: 'TerceiroDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('terceiro');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Terceiro', function($stateParams, Terceiro) {
                    return Terceiro.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'terceiro',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('terceiro-detail.edit', {
            parent: 'terceiro-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/terceiro/terceiro-dialog.html',
                    controller: 'TerceiroDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Terceiro', function(Terceiro) {
                            return Terceiro.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('terceiro.new', {
            parent: 'terceiro',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/terceiro/terceiro-dialog.html',
                    controller: 'TerceiroDialogController',
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
                    $state.go('terceiro', null, { reload: 'terceiro' });
                }, function() {
                    $state.go('terceiro');
                });
            }]
        })
        .state('terceiro.edit', {
            parent: 'terceiro',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/terceiro/terceiro-dialog.html',
                    controller: 'TerceiroDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Terceiro', function(Terceiro) {
                            return Terceiro.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('terceiro', null, { reload: 'terceiro' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('terceiro.delete', {
            parent: 'terceiro',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/terceiro/terceiro-delete-dialog.html',
                    controller: 'TerceiroDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Terceiro', function(Terceiro) {
                            return Terceiro.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('terceiro', null, { reload: 'terceiro' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
