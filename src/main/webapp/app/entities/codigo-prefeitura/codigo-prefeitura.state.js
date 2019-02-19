(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('codigo-prefeitura', {
            parent: 'entity',
            url: '/codigo-prefeitura',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'hmProjetosApp.codigoPrefeitura.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/codigo-prefeitura/codigo-prefeituras.html',
                    controller: 'CodigoPrefeituraController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('codigoPrefeitura');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('codigo-prefeitura-detail', {
            parent: 'codigo-prefeitura',
            url: '/codigo-prefeitura/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'hmProjetosApp.codigoPrefeitura.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/codigo-prefeitura/codigo-prefeitura-detail.html',
                    controller: 'CodigoPrefeituraDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('codigoPrefeitura');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CodigoPrefeitura', function($stateParams, CodigoPrefeitura) {
                    return CodigoPrefeitura.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'codigo-prefeitura',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('codigo-prefeitura-detail.edit', {
            parent: 'codigo-prefeitura-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/codigo-prefeitura/codigo-prefeitura-dialog.html',
                    controller: 'CodigoPrefeituraDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CodigoPrefeitura', function(CodigoPrefeitura) {
                            return CodigoPrefeitura.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('codigo-prefeitura.new', {
            parent: 'codigo-prefeitura',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/codigo-prefeitura/codigo-prefeitura-dialog.html',
                    controller: 'CodigoPrefeituraDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                numero: null,
                                ano: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('codigo-prefeitura', null, { reload: 'codigo-prefeitura' });
                }, function() {
                    $state.go('codigo-prefeitura');
                });
            }]
        })
        .state('codigo-prefeitura.edit', {
            parent: 'codigo-prefeitura',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/codigo-prefeitura/codigo-prefeitura-dialog.html',
                    controller: 'CodigoPrefeituraDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CodigoPrefeitura', function(CodigoPrefeitura) {
                            return CodigoPrefeitura.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('codigo-prefeitura', null, { reload: 'codigo-prefeitura' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('codigo-prefeitura.delete', {
            parent: 'codigo-prefeitura',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/codigo-prefeitura/codigo-prefeitura-delete-dialog.html',
                    controller: 'CodigoPrefeituraDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CodigoPrefeitura', function(CodigoPrefeitura) {
                            return CodigoPrefeitura.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('codigo-prefeitura', null, { reload: 'codigo-prefeitura' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
