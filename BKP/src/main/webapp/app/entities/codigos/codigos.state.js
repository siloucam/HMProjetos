(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('codigos', {
            parent: 'entity',
            url: '/codigos',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'hmProjetosApp.codigos.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/codigos/codigos.html',
                    controller: 'CodigosController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('codigos');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('codigos-detail', {
            parent: 'codigos',
            url: '/codigos/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'hmProjetosApp.codigos.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/codigos/codigos-detail.html',
                    controller: 'CodigosDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('codigos');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Codigos', function($stateParams, Codigos) {
                    return Codigos.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'codigos',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('codigos-detail.edit', {
            parent: 'codigos-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/codigos/codigos-dialog.html',
                    controller: 'CodigosDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Codigos', function(Codigos) {
                            return Codigos.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('codigos.new', {
            parent: 'codigos',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/codigos/codigos-dialog.html',
                    controller: 'CodigosDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                tipo: null,
                                ano: null,
                                numero: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('codigos', null, { reload: 'codigos' });
                }, function() {
                    $state.go('codigos');
                });
            }]
        })
        .state('codigos.edit', {
            parent: 'codigos',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/codigos/codigos-dialog.html',
                    controller: 'CodigosDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Codigos', function(Codigos) {
                            return Codigos.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('codigos', null, { reload: 'codigos' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('codigos.delete', {
            parent: 'codigos',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/codigos/codigos-delete-dialog.html',
                    controller: 'CodigosDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Codigos', function(Codigos) {
                            return Codigos.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('codigos', null, { reload: 'codigos' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
