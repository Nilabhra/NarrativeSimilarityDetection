package com.missionarogya.logic;

import java.util.Vector;

import org.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;

public class DBHandler {
	private static MongoCollection<BasicDBObject> collection;
	private static MongoClient mongoClient;
	private static MongoDatabase db;
	static {
		mongoClient = new MongoClient("localhost", 27017);
		db = mongoClient.getDatabase("narratives");
		collection = db.getCollection("demo", BasicDBObject.class);
		// collection.deleteMany(new BasicDBObject());
	}

	public static void insertXMLString(String s) {

		String json = "{";
		String[] temp = s.split("\n");
		for (String item : temp) {
			String[] idval = item.split("#");
			json += "\"" + idval[0] + "\":\"" + idval[1] + "\",";
		}
		json = json.substring(0, json.length() - 1) + "}";
		// System.out.println(json);
		collection.insertOne((BasicDBObject) JSON.parse(json));
	}

	public static Vector<JSONObject> retriveAll() {
		FindIterable<BasicDBObject> c = collection.find();
		Vector<JSONObject> ret = new Vector<>();
		MongoCursor<BasicDBObject> it = c.iterator();
		while (it.hasNext()) {
			ret.add(new JSONObject(it.next().toString()));
		}
		return ret;
	}

	public static long getCount() {
		return collection.count();
	}
}
