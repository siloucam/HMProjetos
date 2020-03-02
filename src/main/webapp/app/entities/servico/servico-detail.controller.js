(function() {
    'use strict';

    angular
    .module('hmProjetosApp')
    .controller('ServicoDetailController', ServicoDetailController);

    ServicoDetailController.$inject = ['$timeout','$window','$uibModal','$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Servico', 'Situacao', 'Transacao', 'DescricaoServico', 'Cliente', 'TipoSituacao', 'CodigoPrefeitura','LinkExterno','Telefone'];

    function ServicoDetailController($timeout, $window, $uibModal,$scope, $rootScope, $stateParams, previousState, entity, Servico, Situacao, Transacao, DescricaoServico, Cliente, TipoSituacao, CodigoPrefeitura, LinkExterno, Telefone) {
        var vm = this;

        vm.naoachou = true;

        vm.servico = entity;

        vm.iptu = null;

        console.log(vm.servico);

        vm.telefones = [];

        vm.previousState = previousState.name;

        vm.codigozao =  vm.servico.codigo;

        vm.transacaos = [];
        vm.situacaos = [];
        vm.codigoPrefeituras = [];

        vm.saldotaxas = 0;
        vm.pagoservico = 0;


        vm.prefeiturasearch = false;
        vm.prefeituraOk = false;
        vm.prefeituraFail = false;

        vm.arrFromMyObj;

        $scope.date = new Date();

        // $scope.arrFromMyObj = Object.keys(myObj).map(function(key) {
        // return myObj[key];
        // });


        $scope.Back = function(){
            $window.location.href = '/#/servico';
        }

        loadAll();

        // puxaPrefeitura();



        function verificaSeJaTem(texto){

            //True se já tem
            //False se nao tem

            var result = false;

            for(var i = 0; i < vm.situacaos.length; i++){

                // console.log(vm.situacaos[i].observacao.includes(texto));

                if(vm.situacaos[i].observacao){
                    if(vm.situacaos[i].observacao.includes(texto)) result = true;  
                }
            }

            return result;

        }

        $scope.sitePrefeitura = function(numero,ano) {

            // var p_codigo;
            // var p_ano;

            // var n = vm.servico.link.indexOf("/");

            // p_ano = vm.servico.link.slice(n+1);
            // p_codigo = vm.servico.link.slice(0,n);

            $window.open('http://www2.cachoeiro.es.gov.br:8080/ZimWeb/servlet/ZII?tipo_processo=1&numero_processo='+numero+'&ano_processo='+ano+'&connection=producao&program=pwcd001&Procurar=Processar', '_blank');
        }

        $scope.puxaPrefeitura = function(numero,ano) {

            // var p_codigo;
            // var p_ano;

            // var n = vm.servico.link.indexOf("/");

            // p_ano = vm.servico.link.slice(n+1);
            // p_codigo = vm.servico.link.slice(0,n);

            vm.prefeituraOk = false;
            vm.prefeituraFail = false;
            vm.prefeiturasearch = true;

            var jobj = [];

            var processo = numero;
            var ano = ano;

            $.ajax({
                type: "GET",
                url: "http://www2.cachoeiro.es.gov.br:8080/ZimWeb/servlet/ZII?tipo_processo=1&numero_processo="+processo+"&ano_processo="+ano+"&connection=producao&program=pwcd001&Procurar=Processar"
            // headers: {
            //     'Access-Control-Allow-Origin' : '*',
            //     'Content-Type':'text/html'
            //  },
            // dataTye: 'jsonp'
        }).done(function(data) {

            // console.log(data.split("�").join(""));

            data = data.split("�").join("");

            $("#_host").html(data);
            $("#host").html($("#_host table").eq(2));

            $("#host tr").each(function(i){
                var _p = [];
                if(i > 0){
                    _p = [];
                    $(this).find("td").each(function(j){
                        _p[j] = $(this).text();
                    });
                    // console.log(_p);
                    jobj.push({
                        'guia' : _p[0],
                        'recebimento' : _p[1],
                        'localizacao' : _p[2],
                        'despacho' : _p[3]
                    });
                }
            });

            console.log(jobj);

            vm.situsprefeitura = [];

            // var tipo = new TipoSituacao();

            TipoSituacao.queryById({iid: 8}, function(result) {
                    // console.log(result);
                    vm.searchQuery = null;

                    for(var i = 0; i < jobj.length; i++){

                        var situ = new Situacao();

                        var data = jobj[i].recebimento;

                        var dia = data.slice(0,2);
                        var mes = data.slice(3,5);
                        var ano = data.slice(6);

                        situ.dtinicio = ano + "-" + mes + "-" + dia;

                        var guia = jobj[i].guia.split(" ").join("");
                        var local = jobj[i].localizacao.split(" ").join("");

                        situ.observacao = "Guia " + guia + " - " + jobj[i].despacho;
                        situ.terceiro = local;

                        situ.tipo = result[0];

                        situ.servico = vm.servico;

                        vm.situsprefeitura.push(situ);

                    }

                    for(var i = 0; i < vm.situsprefeitura.length; i++){

                        // console.log(vm.situsprefeitura[i].observacao);

                        if(!verificaSeJaTem(vm.situsprefeitura[i].observacao)){

                            //Se não tiver ainda vai criar as situações

                            console.log("Não tem, vai tentar salvar hein");

                            // console.log(vm.situsprefeitura[i]);

                            Situacao.save(vm.situsprefeitura[i], function(){
                                console.log("salvou");
                            }, function(){
                                console.log("eroooou");
                            });
                        }

                    }

                    console.log(vm.situsprefeitura);

                    var timer = function() {
                        if(jobj.length == 0){
                            console.log("ACHO NADA!");
                            vm.prefeiturasearch = false;
                            vm.prefeituraFail = true;
                        }else{
                           vm.prefeiturasearch = false;
                           vm.prefeituraOk = true;
                       }
                   }


                   $timeout(timer, 2000);
                   $timeout(loadAll, 2000);

               });

            // console.log(JSON.stringify(jobj));
        }).fail( function(xhr, textStatus, errorThrown) {
            console.log(xhr);
            console.log(textStatus);
        });

    }

    var calculaSaldo = function(){

        vm.saldotaxas = 0;
        vm.pagoservico = 0;

        for(var i = 0; i < vm.transacaos.length; i++){

            if(vm.transacaos[i].operacao == "DEBITO"){
               vm.saldotaxas = vm.saldotaxas - vm.transacaos[i].valor;
           }

           if(vm.transacaos[i].operacao == "CREDITO"){
               vm.saldotaxas = vm.saldotaxas + vm.transacaos[i].valor;
           }

           if(vm.transacaos[i].operacao == "SERVICO"){
               vm.pagoservico = vm.pagoservico + vm.transacaos[i].valor;
           }

       }

   }


   $scope.mudaServico = function(){

            // console.log("mudouu");

            Servico.queryByCodigo({codigo: vm.codigozao}, function(result) {
                // vm.transacaos = result;
                // console.log(result.length);

                if(result.length == 1){

                    vm.naoachou = true;

                    console.log(result[0]);

                    $window.location.href = '/#/servico/servico/'+result[0].id;

                    // vm.servico = result[0];
                    // console.log(vm.servico.id);1
                    // loadAll();
                }
                if(result.length == 0){
                    vm.naoachou = false;
                }

                vm.searchQuery = null;
                // console.log(result);
            });

        }

        function loadAll() {

            // vm.orcamentos = null;
            vm.transacaos = null;
            vm.situacaos = null;
            vm.codigoPrefeituras = null;
            vm.linkExternos = null;

            LinkExterno.queryByServico({Cid: vm.servico.id}, function(result) {
                vm.linkExternos = result;

                for(var i = 0; i < vm.linkExternos.length; i++){

                    var array = vm.linkExternos[i].nome.split(" ");

                    if(array[0] == "IPTU"){
                        vm.iptu = array[1];
                    }
                }

                vm.searchQuery = null;
            });

            CodigoPrefeitura.queryByServico({Cid: vm.servico.id}, function(result) {
                vm.codigoPrefeituras = result;
                vm.searchQuery = null;
            });

            Transacao.queryByServico({Cid: vm.servico.id}, function(result) {
                vm.transacaos = result;
                vm.searchQuery = null;
                // console.log(result);
                calculaSaldo();
            });

            Telefone.queryByCliente({Cid: vm.servico.cliente.id}, function(result) {
                vm.telefones = result;
                vm.searchQuery = null;
            });
            
            Situacao.queryByServico({Cid: vm.servico.id}, function(result) {

                vm.situacaos = result;

                // $scope.arrFromMyObj = Object.keys(vm.situacaos).map(function(key) {
                // return vm.situacaos[key];
                // });


                vm.searchQuery = null;
            // console.log(result);

            console.log(vm.situacaos);
            console.log(vm.servico.cliente);

        });

        }

        

        $scope.newLinkExterno = function(){

            $uibModal.open({
                templateUrl: 'app/entities/link-externo/link-externo-dialog.html',
                controller: 'LinkExternoDialogController',
                controllerAs: 'vm',
                backdrop: 'static',
                size: 'md',
                resolve: {
                    entity: function () {
                        return {
                            servico: vm.servico,
                            nome: null,
                            link: null,
                            id: null
                        };
                    }
                }
            }).result.then(function() {

                loadAll();

                    //$state.go('transacao', null, { reload: 'transacao' });
                }, function() {
                    //$state.go('transacao');
                });
        }

        $scope.newPrefeitura = function(){

            $uibModal.open({
                templateUrl: 'app/entities/codigo-prefeitura/codigo-prefeitura-dialog.html',
                controller: 'CodigoPrefeituraDialogController',
                controllerAs: 'vm',
                backdrop: 'static',
                size: 'md',
                resolve: {
                    entity: function () {
                        return {
                            servico: vm.servico,
                            numero: null,
                            ano: null,
                            id: null
                        };
                    }
                }
            }).result.then(function() {

                loadAll();

                    //$state.go('transacao', null, { reload: 'transacao' });
                }, function() {
                    //$state.go('transacao');
                });
        }


        $scope.deleteLinkExterno = function(Id) {
            $uibModal.open({
                templateUrl: 'app/entities/link-externo/link-externo-delete-dialog.html',
                controller: 'LinkExternoDeleteController',
                controllerAs: 'vm',
                size: 'md',
                resolve: {
                    entity: ['LinkExterno', function(LinkExterno) {
                        return LinkExterno.get({id : Id}).$promise;
                    }]
                }
            }).result.then(function() {
                    loadAll();
                }, function() {

                });
        };

        $scope.deletePrefeitura = function(Id) {
            $uibModal.open({
                templateUrl: 'app/entities/codigo-prefeitura/codigo-prefeitura-delete-dialog.html',
                controller: 'CodigoPrefeituraDeleteController',
                controllerAs: 'vm',
                size: 'md',
                resolve: {
                    entity: ['CodigoPrefeitura', function(CodigoPrefeitura) {
                        return CodigoPrefeitura.get({id : Id}).$promise;
                    }]
                }
            }).result.then(function() {

                    //Transacao.delete(Id);
                    loadAll();
                }, function() {

                });
        };



        $scope.newTransacao = function(){
            $uibModal.open({
                templateUrl: 'app/entities/transacao/transacao-dialog.html',
                controller: 'TransacaoDialogController',
                controllerAs: 'vm',
                backdrop: 'static',
                size: 'md',
                resolve: {
                    entity: function () {
                        return {
                            servico: vm.servico,
                            valor: null,
                            data: null,
                            descricao: null,
                            operacao: null,
                            id: null
                        };
                    }
                }
            }).result.then(function() {

                loadAll();

                    //$state.go('transacao', null, { reload: 'transacao' });
                }, function() {
                    //$state.go('transacao');
                });
        }

        $scope.deleteTransacao = function(Id) {
            $uibModal.open({
                templateUrl: 'app/entities/transacao/transacao-delete-dialog.html',
                controller: 'TransacaoDeleteController',
                controllerAs: 'vm',
                size: 'md',
                resolve: {
                    entity: ['Transacao', function(Transacao) {
                        return Transacao.get(Id).$promise;
                    }]
                }
            }).result.then(function() {

                    //Transacao.delete(Id);
                    loadAll();
                }, function() {

                });
        };

        $scope.editarTransacao = function(Id) {
            $uibModal.open({
                templateUrl: 'app/entities/transacao/transacao-dialog.html',
                controller: 'TransacaoDialogController',
                controllerAs: 'vm',
                backdrop: 'static',
                size: 'lg',
                resolve: {
                    entity: ['Transacao', function(Transacao) {
                        return Transacao.get(Id).$promise;
                    }]
                }
            }).result.then(function() {

                loadAll();
            }, function() {

            });
        }

        $scope.newSituacao = function(){
            $uibModal.open({
                templateUrl: 'app/entities/situacao/situacao-dialog.html',
                controller: 'SituacaoDialogController',
                controllerAs: 'vm',
                backdrop: 'static',
                size: 'lg',
                resolve: {
                    entity: function () {
                        return {
                            servico: vm.servico,
                            tipo: null,
                            descricao: null,
                            terceiro: null,
                            dtinicio: null,
                            dtfim: null,
                            dtestipulada: null,
                            id: null
                        };
                    }
                }
            }).result.then(function() {
                loadAll();
            }, function() {

            });
        }

        $scope.editSituacao = function(Id){
            $uibModal.open({
                templateUrl: 'app/entities/situacao/situacao-dialog.html',
                controller: 'SituacaoDialogController',
                controllerAs: 'vm',
                backdrop: 'static',
                size: 'lg',
                resolve: {
                    entity: ['Situacao', function(Situacao) {
                        return Situacao.get(Id).$promise;
                    }]
                }
            }).result.then(function() {
                loadAll();
            }, function() {

            });
        }

        $scope.delSituacao = function(Id){
            $uibModal.open({
                templateUrl: 'app/entities/situacao/situacao-delete-dialog.html',
                controller: 'SituacaoDeleteController',
                controllerAs: 'vm',
                size: 'md',
                resolve: {
                    entity: ['Situacao', function(Situacao) {
                        return Situacao.get(Id).$promise;
                    }]
                }
            }).result.then(function() {
                loadAll();
            }, function() {

            });
        }

        $scope.printToCart = function(printSectionId) {


        if(printSectionId == 'requerimento'){

        document.getElementById(printSectionId).style.display = 'block';
        var innerContents = document.getElementById(printSectionId).outerHTML;

        var popupWinindow = window.open('', '_blank', 'scrollbars=no,menubar=no,toolbar=no,location=no,status=no,titlebar=no');
        popupWinindow.document.open();
        popupWinindow.document.write("<html><head><link rel='stylesheet' href='content/css/main.css'></head><body>" + innerContents + '</html>');
        popupWinindow.document.close();

        document.getElementById(printSectionId).style.display = 'none';

        }else{

        document.getElementById(printSectionId).style.display = 'block';
        var innerContents = document.getElementById(printSectionId).outerHTML;

        var popupWinindow = window.open('', '_blank', 'scrollbars=no,menubar=no,toolbar=no,location=no,status=no,titlebar=no');
        popupWinindow.document.open();
        popupWinindow.document.write("<html><head><link rel='stylesheet' href='content/css/main.css'><link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css'></head><body onload='window.print()'>" + innerContents + '</html>');
        popupWinindow.document.close();

        document.getElementById(printSectionId).style.display = 'none';

        }
        // window.print();
      }

       $scope.printRequerimento = function(printSectionId) {

        document.getElementById(printSectionId).style.display = 'block';
        var innerContents = document.getElementById(printSectionId).outerHTML;

        var popupWinindow = window.open('', '_blank', 'scrollbars=no,menubar=no,toolbar=no,location=no,status=no,titlebar=no');
        popupWinindow.document.open();
        popupWinindow.document.write("<html><head><link rel='stylesheet' href='content/css/main.css'></head><body>" + innerContents + '</html>');
        popupWinindow.document.close();

        document.getElementById(printSectionId).style.display = 'none';
        // window.print();
      }

        var unsubscribe = $rootScope.$on('hmProjetosApp:servicoUpdate', function(event, result) {
            vm.servico = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
