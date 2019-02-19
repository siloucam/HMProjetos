(function() {
    'use strict';

    angular
        .module('hmProjetosApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('link-externo', {
            parent: 'entity',
            url: '/link-externo',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'hmProjetosApp.linkExterno.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/link-externo/link-externos.html',
                    controller: 'LinkExternoController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('linkExterno');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('link-externo-detail', {
            parent: 'link-externo',
            url: '/link-externo/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'hmProjetosApp.linkExterno.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/link-externo/link-externo-detail.html',
                    controller: 'LinkExternoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('linkExterno');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'LinkExterno', function($stateParams, LinkExterno) {
                    return LinkExterno.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'link-externo',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('link-externo-detail.edit', {
            parent: 'link-externo-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/link-externo/link-externo-dialog.html',
                    controller: 'LinkExternoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['LinkExterno', function(LinkExterno) {
                            return LinkExterno.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('link-externo.new', {
            parent: 'link-externo',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/link-externo/link-externo-dialog.html',
                    controller: 'LinkExternoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nome: null,
                                link: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('link-externo', null, { reload: 'link-externo' });
                }, function() {
                    $state.go('link-externo');
                });
            }]
        })
        .state('link-externo.edit', {
            parent: 'link-externo',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/link-externo/link-externo-dialog.html',
                    controller: 'LinkExternoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['LinkExterno', function(LinkExterno) {
                            return LinkExterno.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('link-externo', null, { reload: 'link-externo' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('link-externo.delete', {
            parent: 'link-externo',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/link-externo/link-externo-delete-dialog.html',
                    controller: 'LinkExternoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['LinkExterno', function(LinkExterno) {
                            return LinkExterno.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('link-externo', null, { reload: 'link-externo' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
