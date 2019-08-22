package org.migrador;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import com.opencsv.CSVReader;

public class Servicos {

	private static final String SAMPLE_CSV_FILE_PATH = "Servicos.csv";

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		@SuppressWarnings("deprecation")
		CSVReader reader = new CSVReader(new FileReader(SAMPLE_CSV_FILE_PATH), ';', '"', 0);
		String[] nextLine;

		File file = new File("Servicos.sql");
		
		String id_projeto;
		
		int id_link_externo = 1;
		int id_link_prefeitura = 1;

		BufferedWriter fr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));

		while ((nextLine = reader.readNext()) != null) {
            if (nextLine != null) {
            	
            	if(!nextLine[0].isEmpty()) {
            	
            		/**
            		Mapeamento
            		0 - Codigo;
            		1 - Cliente;
            		2 - Descrição;
            		3 - endereço;
            		4 - bairro;
            		5 - cidade;
            		6 - estado;
            		7 - CEP;
            		8 - Início;
            		9 - Fim;
            		10 - IPTU;
            		11 - Link;
            		12 - Orçamento;
            		13 - Pagamento;
            		14 - Pago;
            		15 - Taxas;
            		16 - Pendente;
            		17 - Mask;
            		18 - Link2;
            		19 - IPTU;
            		20 - área;
            		21 - Mask2;
            		22 - Proprietário;
            		23 - CPF/CNPJ;
            		**/            
            		            		            		
            		//id
            		//variavel incremental id_projeto
            		
            		//Tipo e Codigo
            		String codigo = nextLine[0];
            		String tipo = codigo.substring(0,3);
            		
            		id_projeto = codigo.substring(6);
            		id_projeto = id_projeto.replace(".", "");
            		
            		//Cliente
            		String id_cliente = nextLine[1];
            		id_cliente = id_cliente.substring(5);
            		id_cliente = id_cliente.replace(".", "");
            		
            		//CEP
            		String CEP = nextLine[7];
                    CEP = CEP.replace(".", "");
                    CEP = CEP.replaceAll("-","");
                    CEP = CEP.replaceAll(" ","");
            		
            		//Data Inicio dd/mm/aaaa
                    String dtinicio = nextLine[8];
                    if(!nextLine[8].isEmpty()) {
                    String dia = dtinicio.substring(0,2);
                    String mes = dtinicio.substring(3,5);
                    String ano = dtinicio.substring(6);
            		dtinicio = "'" + ano + "-" + mes + "-" + dia + "'";
                    }else {
                    	dtinicio = "null";
                    }
                    
            		//Data Fim
                    String dtfim = nextLine[9];
                    if(!nextLine[9].isEmpty()) {
            		String dia = dtfim.substring(0,2);
            		String mes = dtfim.substring(3,5);
            		String ano = dtfim.substring(6);
            		dtfim = "'" + ano + "-" + mes + "-" + dia + "'";
                    }else {
                    	dtfim = "null";
                    }
            		
            		fr.write("INSERT INTO hmprojetos.servico values (" 
                            + id_projeto
                            + ",'" + tipo
                            + "','" + codigo
                            + "','" + nextLine[2] //observacao
                            + "','" + nextLine[12] //valor
                            + "','" + nextLine[13] //forma de pagamento
                            + "','" + nextLine[3] //endereco
                            + "','" + nextLine[4] //bairro
                            + "','" + nextLine[5] //cidade
                            + "','" + nextLine[6] //estado
                            + "','" + CEP //CEP
                            + "'," + dtinicio
                            + "," + dtfim
                            + "," + "null"
                            + "," + id_cliente
                            +  ");\n");
            		
            		//CRIAÇÃO DOS IPTUS E CNDS
                    String codigo_iptu = nextLine[10];
                    
                    if(!codigo_iptu.isEmpty()) {
                    	String[] codigos_iptus = nextLine[10].split("/");
//                        System.out.println(codigos_iptus.length);
                        for(int i = 0; i < codigos_iptus.length; i++) {
                        	
//                        	System.out.println(codigos_iptus[i]);
                        	
                        	//IPTU
                            fr.write("INSERT INTO hmprojetos.link_externo values (" 
                    		+ id_link_externo
                    		+ ",'IPTU "+ codigos_iptus[i] + "'" 
                    		+ ",'http://www2.cachoeiro.es.gov.br:8080/ZimWeb/servlet/ZII?connection=producao&program=pwci001&vudTipoDocum=9&vudNoDocum="+codigos_iptus[i]+"&vexercicio=2019'"
                    		+ "," + id_projeto
                    		+  ");\n");
                            id_link_externo++;
                            //CND
                            fr.write("INSERT INTO hmprojetos.link_externo values (" 
                            + id_link_externo
                            + ",'CND "+ codigos_iptus[i] + "'" 
                            + ",'http://www2.cachoeiro.es.gov.br:8080/ZimWeb/servlet/ZII?vudTipoDocum=3&vudNoDocum="+codigos_iptus[i]+"&debug=true&connection=producao&program=pwud001&template=%28vudTipoDocum%2CvudNoDocum%2CvudFinalidade%29&B1=Pesquisar'"
                            + "," + id_projeto
                            +  ");\n");
                            id_link_externo++;
                            
                        } //fim do for
                    }
                    
                    
                    
                    //CRIAÇÃO DOS LINKS DA PREFEITURA
                    //LINK: 
                    String link = nextLine[11];
                    
                    //Verifica se o link é da prefeitura mesmo ou de outro lugar
                    if(link.contains("http://www2.cachoeiro.es.gov.br:8080/ZimWeb/servlet/ZII?tipo_processo=1")) {
                    	
                    	String[] quebrada = link.split("&");
                    	String codigo_pref = quebrada[1].replace("numero_processo=", "");
//                    	System.out.println(codigo_pref);
                    	String ano_pref = quebrada[2].replace("ano_processo=", "");
//                    	System.out.println(ano_pref);
                    	
                    	fr.write("INSERT INTO hmprojetos.codigo_prefeitura values (" 
                        		+ id_link_prefeitura
                        		+ ",'"+ codigo_pref + "'" 
                        		+ ",'" + ano_pref + "'"
                        		+ "," + id_projeto
                        		+  ");\n");
                                id_link_prefeitura++;
                    	
                    }else{
                    //É algum outro link, tipo do TJ
                    	
                    	
                    }
                    
                    String link2 = nextLine[18];
                    
//                    System.out.println(link2);
                    
                    //Verifica se o link é da prefeitura mesmo ou de outro lugar
                    if(link2.contains("http://www2.cachoeiro.es.gov.br:8080/ZimWeb/servlet/ZII?tipo_processo=1")) {
                    	
                    	String[] quebrada = link2.split("&");
                    	String codigo_pref = quebrada[1].replace("numero_processo=", "");
//                    	System.out.println(codigo_pref);
                    	String ano_pref = quebrada[2].replace("ano_processo=", "");
//                    	System.out.println(ano_pref);
                    	
                    	fr.write("INSERT INTO hmprojetos.codigo_prefeitura values (" 
                        		+ id_link_prefeitura
                        		+ ",'"+ codigo_pref + "'" 
                        		+ ",'" + ano_pref + "'"
                        		+ "," + id_projeto
                        		+  ");\n");
                                id_link_prefeitura++;
                    	
                    }else{
                    //É algum outro link, tipo do TJ
                    	
                    	
                    }
                    
                    
                    
            	}
            	
            }
		}  
	
		fr.close();
		System.out.println("Done");
	}

}
