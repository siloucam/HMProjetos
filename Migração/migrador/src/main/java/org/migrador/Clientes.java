package org.migrador;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class Clientes {

	private static final String SAMPLE_CSV_FILE_PATH = "Clientes.csv";
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		//ID DO ULTIMO TELEFONE DO BANCO
		int id_telefone = 4;
		
		@SuppressWarnings("deprecation")
		CSVReader reader = new CSVReader(new FileReader(SAMPLE_CSV_FILE_PATH), ';' , '"' , 0);
    	String[] nextLine;
    	
//    	Writer writer = Files.newBufferedWriter(Paths.get("saida.csv"));
//    	CSVWriter csvWriter = new CSVWriter(writer,
//                CSVWriter.DEFAULT_SEPARATOR,
//                CSVWriter.NO_QUOTE_CHARACTER,
//                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
//                CSVWriter.DEFAULT_LINE_END);
    	
    	File file = new File("Clientes.sql");
        
        BufferedWriter fr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF-8"));
    	
    	while ((nextLine = reader.readNext()) != null) {
    		
            if (nextLine != null) {
            	
//            	System.out.println(nextLine.length);
            	
            	String id = nextLine[0];
            	if(!id.isEmpty()) {
            	
            		id = id.substring(5);
            		id = id.replace(".", "");
            		
            		boolean temCEP = false;
            		
            		if(nextLine.length > 12) temCEP = true;
            		
            		String CEP = "";
            		if(temCEP) {
            		 CEP = nextLine[12];
                     CEP = CEP.replace(".", "");
                     CEP = CEP.replaceAll("-","");
                     CEP = CEP.replaceAll(" ","");
            		};
                     
                     
            	fr.write("INSERT INTO hmprojetos.cliente values (" 
                        + id 
                        + ",'" + nextLine[1]
                        		+ "','" + nextLine[2]
                        				+ "','" + nextLine[3]
                        						+ "','" + nextLine[4]
                        								+ "','" + nextLine[5]
                        										+ "','" + CEP
                        												+ "','" + nextLine[10]
                        														+ "','" + nextLine[11]
                        																+ "','" + nextLine[6]
                        +  "');\n");
            	
            	//Criação dos INSERTS dos telefones
            	if(!nextLine[7].isEmpty()) {
            	fr.write("INSERT INTO hmprojetos.telefone values ("
            			+ id_telefone
            			+ ",'','" + nextLine[7]
            			+ "',"
            			+ id 
            			+ ");\n"
            			);
            		id_telefone++;
            	}
            	if(!nextLine[8].isEmpty()) {
                	fr.write("INSERT INTO hmprojetos.telefone values ("
                			+ id_telefone
                			+ ",'','" + nextLine[8]
                			+ "',"
                			+ id 
                			+ ");\n"
                			);
                		id_telefone++;
                	}
            	if(!nextLine[9].isEmpty()) {
                	fr.write("INSERT INTO hmprojetos.telefone values ("
                			+ id_telefone
                			+ ",'','" + nextLine[9]
                			+ "',"
                			+ id 
                			+ ");\n"
                			);
                		id_telefone++;
                	}
            	
            	}
            }
    	}
    	
    	fr.close();
    	System.out.println("Done");
	}

}
