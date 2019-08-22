package org.migrador;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
	
	private static final String SAMPLE_CSV_FILE_PATH = "Clientes.csv";
	private static final String STRING_ARRAY_SAMPLE = "string-array-sample.csv";

	
    public static void main( String[] args ) throws IOException
    {
//        System.out.println( "Hello World!" );
    	
    	@SuppressWarnings("deprecation")
		CSVReader reader = new CSVReader(new FileReader(SAMPLE_CSV_FILE_PATH), ';' , '"' , 0);
    	String[] nextLine;
    	
    	File file = new File("ClientesSQL.txt");
        
        BufferedWriter fr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF-8"));
        
        
    	while ((nextLine = reader.readNext()) != null) {
            if (nextLine != null) {
               //Verifying the read data here
            	
            	String id = nextLine[0];
            	
            	//Verifica se não é uma linha vazia
            	if(!id.isEmpty()) {
            		
            		id = id.substring(5);
            		id = id.replace(".", "");
            	
            		System.out.println("Codigo: " + id);
                    System.out.println("Nome: " + nextLine[1]);
                    System.out.println("Endereco: " + nextLine[2]);
                    System.out.println("Bairro: " + nextLine[3]);
                    System.out.println("Cidade: " + nextLine[4]);
                    System.out.println("Estado: " + nextLine[5]);
                    
                    String CEP = nextLine[12];
                    CEP = CEP.replace(".", "");
                    CEP = CEP.replaceAll("-","");
                    CEP = CEP.replaceAll(" ","");
                    System.out.println("CEP: " + CEP);
                      
                    System.out.println("Email: " + nextLine[10]);
                    System.out.println("Indicação: " + nextLine[11]);
                    System.out.println("Documento: " + nextLine[6]);
                    System.out.println("=========");
                    
                    //Preparação do SQL
                    //INSERT INTO hmprojetos.cliente values (15001,'Nome','Endereco','Bairro','Cidade','ES','1111','Email','Indicacao','Doc');

                    try {
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
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    
                    
                    
            	}else {
            		System.out.println("Linha Vazia");
            		System.out.println("=========");
            	}
            	
            	
//            	id = id.substring(5);
            	
               
            }
          }
    	
    	
    	fr.close();
    	
    }
}
