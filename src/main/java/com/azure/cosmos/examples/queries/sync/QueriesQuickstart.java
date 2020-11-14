// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.cosmos.examples.queries.sync;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.client.MongoDatabase;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;



public class QueriesQuickstart {

    final String ExcelFilePath = "C:\\Users\\kannanu\\OneDrive\\VICH\\MandatoryRuleFile.xlsx";
    
    InputStream inp =null;
	Workbook wb =null;
	Sheet sheet=null;
	final int filename = 7;
	final int flagcheck = 0;
	final String uristr="mongodb://test-adrdata-00004-cosmos:IhJ7p6mgqlhCoVN4BqZwwtNoVnsJ2qWQnHrHVS7jnMhBWbFwzXZdqLXULLTDivnMPBqnGN25VIam0Alt3KhaDw==@test-adrdata-00004-cosmos.mongo.cosmos.azure.com:10255/?ssl=true&replicaSet=globaldb&retrywrites=false&maxIdleTimeMS=120000&appName=@test-adrdata-00004-cosmos@";
	final String cosmosDB="test-adrdata-00004-cosmos";
	char[] passwd="IhJ7p6mgqlhCoVN4BqZwwtNoVnsJ2qWQnHrHVS7jnMhBWbFwzXZdqLXULLTDivnMPBqnGN25VIam0Alt3KhaDw==".toCharArray();
	final String db="adr-data";
	MongoClientURI uri=null;
	MongoClient mongo =null;
	MongoCredential credential=null;
	//MongoDatabase database =null;
	
	@Before
	public void prerequisites() {
		uri = new MongoClientURI(uristr);
		mongo=new MongoClient(uri);
		 // Creating Credentials 
		credential = MongoCredential.createCredential(cosmosDB, db,passwd);
		System.out.println("Connected to the database successfully");
		//Accessing the database
		//database = mongo.getDatabase(db);
		System.out.println("Credentials ::"+ credential);
		//database.getCollection("reported.AdverseEventReportMessage"); 
        //System.out.println("Collection myCollection selected successfully");
	}
	@Test
    public void vich_dbdatavalidation() throws IOException {
            
        CreateObjects(ExcelFilePath);
        int Sheet=0;
		sheet = wb.getSheet("createvichtestfiles");//wb.getSheetAt(Sheet);
		
		for (int j = 1; j < 1000 - 1; j++) {
			try {
			if (sheet.getRow(j).getCell(flagcheck).toString().equalsIgnoreCase("y")) {	
				String newfilename = sheet.getRow(j).getCell(filename).getStringCellValue();
				 System.out.println(newfilename);

				    DBObject query = new BasicDBObject("batchSenderIdentifier",newfilename).append("aerVersion", "VICHAER1.0.0");
		            @SuppressWarnings("deprecation")
					DB db = mongo.getDB("adr-data"); 
		            DBCollection col = db.getCollection("reported.AdverseEventReportMessage"); 
		            DBCursor c1 = col.find(query).sort(new BasicDBObject("_id", -1)).limit(1);
		            try {
		            	
		            while (c1.hasNext()) {
	    				System.out.println(c1.next().toString());
	    				 Cell newpath = sheet.getRow(j).createCell(8);	
	    				 newpath.setCellValue("kannan");
	    				 newpath.setCellValue(c1.next().toString());
	    			}
	    			c1.close();
	    			FileOutputStream fileOut = new FileOutputStream(ExcelFilePath);
	    	        wb.write(fileOut);
	    	        fileOut.close();
		            }
	    			catch (Exception e1) {
	    				
	    			}
			}
		}catch(Exception e) {}            
		}
		
		
		
    }

    public void CreateObjects(String ExcelFilePath) throws IOException {
		inp = new FileInputStream(ExcelFilePath);
		wb = new XSSFWorkbook(inp);
	}
}
