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
            		
            		//Codigo do Projeto
            		
            		String id_projeto;
            		
            		String codigo = nextLine[1];
            		
            		id_projeto = codigo.substring(6);
            		id_projeto = id_projeto.replace(".", "");
            		
//            		System.out.println(id_projeto);
            		
            		
            		//Tipo de Situacao
            		
            		int id_situ = 0;
            		
            		String situ = nextLine[2];
            		if(situ.equals("Fazer Contato Terceiro")) id_situ = 5;
            		
            		//Descricao
            		String descricao = nextLine[3];
            		
            		//Terceiro
            		String terceiro = nextLine[4];
            		
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
            		String dia = dtprazo.substring(0,2);
            		String mes = dtprazo.substring(3,5);
            		String ano = dtprazo.substring(6);
            		dtprazo = "'" + ano + "-" + mes + "-" + dia + "'";
                    }else {
                    	dtprazo = "null";
                    }
                    
                    System.out.println(id_projeto + ";" + id_situ + ";" + descricao + ";" + terceiro + ";" + dtinicio + ";" + dtfim + ";" + dtprazo + ";");
            		
                    //Fazer o fr write agora beleza? 
                    
            	}
            }
        }
		
	}

}
