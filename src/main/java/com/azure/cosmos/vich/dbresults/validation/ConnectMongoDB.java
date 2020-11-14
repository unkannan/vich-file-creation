package com.azure.cosmos.vich.dbresults.validation;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoDatabase;

public class ConnectMongoDB {

	static String uristr=null;
	static String cosmosDB=null;
	static char[] passwd=null;
	static String dbtobeconnected=null;
	MongoClientURI uri=null;
	MongoClient mongo =null;
	MongoCredential credential=null;
	MongoDatabase database =null;
	
  public ConnectMongoDB() {
	    uristr="mongodb://test-adrdata-00004-cosmos:IhJ7p6mgqlhCoVN4BqZwwtNoVnsJ2qWQnHrHVS7jnMhBWbFwzXZdqLXULLTDivnMPBqnGN25VIam0Alt3KhaDw==@test-adrdata-00004-cosmos.mongo.cosmos.azure.com:10255/?ssl=true&replicaSet=globaldb&retrywrites=false&maxIdleTimeMS=120000&appName=@test-adrdata-00004-cosmos@";
		cosmosDB="test-adrdata-00004-cosmos";
		passwd="IhJ7p6mgqlhCoVN4BqZwwtNoVnsJ2qWQnHrHVS7jnMhBWbFwzXZdqLXULLTDivnMPBqnGN25VIam0Alt3KhaDw==".toCharArray();
		dbtobeconnected="adr-data";
  }
  public DB connectdb() {
	  uri = new MongoClientURI(uristr);
		mongo=new MongoClient(uri);
		 // Creating Credentials 
		credential = MongoCredential.createCredential(cosmosDB, dbtobeconnected,passwd);
		System.out.println("Connected to the database successfully");
		
		//Accessing the database
		@SuppressWarnings("deprecation")
		DB db = mongo.getDB(dbtobeconnected); 
		
		System.out.println("Credentials ::"+ credential);
		return db;
  }
}
