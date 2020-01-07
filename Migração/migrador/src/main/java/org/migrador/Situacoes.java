package org.migrador;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

import com.opencsv.CSVReader;

public class Situacoes {

	private static final String SAMPLE_CSV_FILE_PATH = "Situacoes.csv";
	
	public static void main(String[] args) throws IOException  {
		// TODO Auto-generated method stub

		@SuppressWarnings("deprecation")
		CSVReader reader = new CSVReader(new FileReader(SAMPLE_CSV_FILE_PATH), ';' , '"' , 0);
    	String[] nextLine;
		
    	File file = new File("Situacoes.sql");
        
        BufferedWriter fr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF-8"));
    	
        while ((nextLine = reader.readNext()) != null) {
            if (nextLine != null) {
            	
            	if(!nextLine[0].isEmpty()) {
            	
            		/**
            		Mapeamento
            		
            		0 - Codigo S
            		1 - Código do Projeto (PAQ ..)
            		2 - Situação
            		3 - Comentario
            		4 - Responsável
            		5 - inicio
            		6 - fim
            		7 - Prazo
            		
            		**/
            		
            		String id = nextLine[0];
            		
            		//Codigo do Projeto
            		
            		String id_projeto;
            		
            		String codigo = nextLine[1];
            		
            		String tipo = codigo.substring(0,3);
            		id_projeto = codigo.substring(6);
            		id_projeto = id_projeto.replace(".", "");
            		if(tipo.equals("PRS")) id_projeto = "1" + id_projeto;
            		if(tipo.equals("ASS")) id_projeto = "2" + id_projeto;
            		
//            		System.out.println(id_projeto);
            		
            		
            		//Tipo de Situacao
            		
            		int id_situ = 20;
            		
            		String situ = nextLine[2];
            		if(situ.equals("Fazer Medição")) id_situ = 1;
            		if(situ.equals("Fazer Desenho")) id_situ = 2;
            		if(situ.equals("Desenhando")) id_situ = 3;
            		if(situ.equals("Revisar")) id_situ = 4;
            		if(situ.equals("Fazer Contato Terceiro")) id_situ = 5;
            		if(situ.equals("Fazer Contato Cliente")) id_situ = 6;
            		if(situ.equals("Aguardando Resposta Cliente")) id_situ = 7;
            		if(situ.equals("Aguardando Resposta Terceiros")) id_situ = 8;
            		if(situ.equals("Atividades Internas")) id_situ = 9;
            		if(situ.equals("Montar Processo")) id_situ = 10;
            		if(situ.equals("Aguardando")) id_situ = 11;
            		if(situ.equals("Entregar")) id_situ = 12;
            		if(situ.equals("Arquivar")) id_situ = 13;
            		if(situ.equals("Cobrar Pagamento")) id_situ = 14;
            		if(situ.equals("Acompanhamento de Notificações")) id_situ = 15;
            		if(situ.equals("Acompanhamento de Usucapião")) id_situ = 16;
            		if(situ.equals("Elaborando Processo")) id_situ = 17;
            		if(situ.equals("Acompanhamento de Processos")) id_situ = 18;
            		
            		String descricao;
            		//Descricao
            		if(!nextLine[3].isEmpty())
            		descricao = nextLine[3];
            		else descricao = "";
            		
            		//Terceiro
            		String terceiro;
            		if(!nextLine[4].isEmpty())
            		terceiro = nextLine[4];
            		else terceiro = "";
            		
            		//Dt Inicio
            		String dtinicio = nextLine[5];
                    if(!nextLine[5].isEmpty()) {
                    String dia = dtinicio.substring(0,2);
                    String mes = dtinicio.substring(3,5);
                    String ano = dtinicio.substring(6);
            		dtinicio = "'" + ano + "-" + mes + "-" + dia + "'";
                    }else {
                    	dtinicio = "null";
                    }
            		
            		//Dt Fim
                    String dtfim = nextLine[6];
                    if(!nextLine[6].isEmpty()) {
            		String dia = dtfim.substring(0,2);
            		String mes = dtfim.substring(3,5);
            		String ano = dtfim.substring(6);
            		dtfim = "'" + ano + "-" + mes + "-" + dia + "'";
                    }else {
                    	dtfim = "null";
                    }
                    
            		//Dt Prevista
                    String dtprazo = nextLine[7];
                    if(!nextLine[7].isEmpty()) {
                    	//System.out.println(id + " - " + nextLine[7]);
                    	//Tem que fazer o replace de '; ;' por ';;'
            		String dia = dtprazo.substring(0,2);
            		String mes = dtprazo.substring(3,5);
            		String ano = dtprazo.substring(6);
            		dtprazo = "'" + ano + "-" + mes + "-" + dia + "'";
                    }else {
                    	dtprazo = "null";
                    }
                    
                    //System.out.println(id_projeto + ";" + id_situ + ";" + descricao + ";" + terceiro + ";" + dtinicio + ";" + dtfim + ";" + dtprazo + ";");
            		
                    
                    //Fazer o fr write agora beleza? 
                    fr.write("INSERT INTO hmprojetos.situacao values (" 
                            + id
                            + ",'" + descricao
                            + "','" + terceiro
                            + "'," + dtinicio
                            + "," + dtfim
                            + "," + dtprazo
                            + ",null"   
                            + "," + id_situ
                            + "," + id_projeto
                            + ",null" 
                            +  ");\n");
                    
                    
                    
            	}
            }
        }
		
        fr.close();
		System.out.println("Done");
        
	}

}
