package com.smartlab.wpqrscanner;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class dbActivities {
	
	DatabaseHelper dbHelper;
    SQLiteDatabase db;


    
    
    public int checkDB(DatabaseHelper helper)
	 {		
		int primary = 0;
		 dbHelper = helper;
		 SQLiteDatabase db = this.dbHelper.getReadableDatabase();
	    	Cursor cur = null ;
	    	cur = db.query("wpnewsmd",
	                new String[] {"_ID"},
	                null,
	                null ,
	                null,
	                null,
	                null);
			 if (cur.moveToFirst())
		        {
		            do
		            {
		            	primary = cur.getInt(0);
		            } while (cur.moveToNext());
		        }
			 else
			 {
			     primary = 0; 
			 }
			 
			 db.close();
			 return primary;
	 }
    
    public int countConfigDB(DatabaseHelper helper)
	 {
		Log.i("countConfigDB", "start");
		
		int iCount = 0;
		 SQLiteDatabase db = helper.getReadableDatabase();
			Cursor cur = null ;
			cur = db.query("wpconfigmd",
			new String[] {"_ID"},
		        null,
		        null ,
		        null,
		        null,
		        null);
		 if (cur.moveToFirst())
		    {
		        do
		        {
		        	iCount = iCount + 1;
		        	Log.i("countConfigDB", "_ID=" + Integer.toString(cur.getInt(0)));
		        	
		        } while (cur.moveToNext());
		    }
		 else
		 {
			 iCount = 0; 
		     //insertLabel();
		 }
		 
		 Log.i("countConfigDB", "end");
			
		 db.close();
		 return iCount;
	 }
    
    public int checkConfigDB(DatabaseHelper helper)
	 {
		Log.i("checkConfigDB", "start");
		
		int primary = 0;
		 SQLiteDatabase db = helper.getReadableDatabase();
			Cursor cur = null ;
			cur = db.query("wpconfigmd",
			new String[] {"_ID"},
		        null,
		        null ,
		        null,
		        null,
		        null);
		 if (cur.moveToFirst())
		    {
		        do
		        {
		        	primary = cur.getInt(0);
		        	Log.i("checkConfigDB", "_ID=" + Integer.toString(cur.getInt(0)));
		        	//Log.i("checkConfigDB", "_ID=" + Integer.toString(cur.getInt(0)) + "; domain='"+cur.getString(3)+"'" + "; active=" + Integer.toString(cur.getInt(4)));
		            //Log.i("checkConfigDB", cur.getInt(0) + "; " + cur.getString(1) + "; " + cur.getString(2) + "; " + cur.getString(3) + "; " + cur.getInt(4));
		        } while (cur.moveToNext());
		    }
		 else
		 {
		     primary = 0; 
		     //insertLabel();
		 }
		 
		 Log.i("checkConfigDB", "end");
			
		 db.close();
		 return primary;
	 }
    
    public int checkCategoryDB(DatabaseHelper helper)
	 {
		int primary = 0;
		 SQLiteDatabase db = helper.getReadableDatabase();
	    	Cursor cur = null ;
	    	cur = db.query("wpnewscatmd",
	                new String[] {"_ID"},
	                null,
	                null ,
	                null,
	                null,
	                null);
			 if (cur.moveToFirst())
		        {
		            do
		            {
		            	primary = cur.getInt(0);
			            //Log.d("DB get primary",cur.getString(0) + " " + cur.getString(1)+ " " + cur.getString(2));
		            } while (cur.moveToNext());
		        }
			 else
			 {
			     primary = 0; 
			     //insertLabel();
			 }
			 
			 db.close();
			 return primary;
	 }
    
	public byte[] GetBlob(int primary, int pos)
	 {		
		 SQLiteDatabase db = this.dbHelper.getReadableDatabase();
		 		 
	    	Cursor cur = null ;
	    	byte[] label = null;
	    	try{
	    		cur = db.query("wpnewsmd",
	                new String[] {},
	                "_ID = '"+ primary +"'",
	                null ,
	                null,
	                null,
	                null);
	    	
			 if (cur.moveToFirst())
		        {
		            do
		            {
		            	label = cur.getBlob(pos);
		            	//Log.i("DB Bitmap"," "+cur.getString(0));
		            } while (cur.moveToNext());
		        }
			 
			 db.close();}catch(Exception e)
			 {
				 Log.d("Delete error"," "+e.getMessage());
			 }
			 return label;
	 }
	
	public byte[] GetBlob(int primary, int pos, DatabaseHelper Helper)
	 {		
		 SQLiteDatabase db = Helper.getReadableDatabase();
		 		 
	    	Cursor cur = null ;
	    	byte[] label = null;
	    	try{
	    		cur = db.query("wpnewsmd",
	                new String[] {},
	                "_ID = '"+ primary +"'",
	                null ,
	                null,
	                null,
	                null);
	    	
			 if (cur.moveToFirst())
		        {
		            do
		            {
		            	label = cur.getBlob(pos);
		            	//Log.d("DB Bitmap"," "+cur.getString(0));
		            } while (cur.moveToNext());
		        }
			 
			 db.close();}catch(Exception e)
			 {
				 Log.d("Delete error"," "+e.getMessage());
			 }
			 return label;
	 }
   
    public String getResult(DatabaseHelper helper, String att, String selection)
	 {		
		 String value;
		 dbHelper = helper;
		 
		 SQLiteDatabase db = this.dbHelper.getReadableDatabase();
		 
	    	Cursor cur = null ;
	    	cur = db.query("wpnewsmd",
	                new String[] {att},
	                selection,
	                null,
	                null,
	                null,
	                null);
			 if (cur.moveToFirst())
		        {
		            do
		            {
		            	value= cur.getString(0);
		            	//Log.i("DB Get Icon name",cur.getString(0));
		            } while (cur.moveToNext());
		        }
			 else
			 {
				 value=null;
			     //insertLabel();
			 }
			 db.close();
			 return value;
	 }
    
    public String getConfigValue(DatabaseHelper helper, String att, String selection)
	 {		
		 String value;
		 dbHelper = helper;
		 
		 SQLiteDatabase db = this.dbHelper.getReadableDatabase();
		 
	    	Cursor cur = null ;
	    	cur = db.query("wpconfigmd",
	                new String[] {att},
	                selection,
	                null,
	                null,
	                null,
	                null);
			 if (cur.moveToFirst())
		        {
		            do
		            {
		            	value= cur.getString(0);
		            	//Log.i("DB Get Icon name",cur.getString(0));
		            } while (cur.moveToNext());
		        }
			 else
			 {
				 value=null;
			     //insertLabel();
			 }
			 db.close();
			 return value;
	 }
    
    public int getResultInt(DatabaseHelper helper, String att, String selection)
	 {
		Log.e("Get Result from DB", "Get" + att);
		 int value;
		 dbHelper = helper;
		 
		 SQLiteDatabase db = this.dbHelper.getReadableDatabase();
		 
	    	Cursor cur = null ;
	    	cur = db.query("wpnewsmd",
	                new String[] {att},
	                selection,
	                null,
	                null,
	                null,
	                null);
			 if (cur.moveToFirst())
		        {
		            do
		            {
		            	value= cur.getInt(0);
		            	Log.d("DB getResultInt = ",cur.getInt(0) + "");
		            } while (cur.moveToNext());
		        }
			 else
			 {
				 value=0;
			     //insertLabel();
			 }
			 db.close();
			 return value;
	 }
      
    public String[] getResultArray(DatabaseHelper helper, String att, String selection)
	 {
		Log.i("getResultArray", "Get: " + att);
		
		 String[] value;
		 int i = 0;
		 dbHelper = helper;
		 
		 SQLiteDatabase db = this.dbHelper.getReadableDatabase();
		 
	    	Cursor cur = null ;
	    	cur = db.query("wpnewsmd",
	                new String[] {"*"},
	                selection,
	                null,
	                null,
	                null,
	                null);
	    	 value = new String[cur.getColumnCount()];
	    	 
	    	 if (cur.moveToFirst())
		        {
		            do
		            {
		            	for (i=0;i<5;i++)
		            	{
		            		value[i]= cur.getString(i);
		            		Log.i("DB Get news values","value = "+cur.getString(i));
		            	}
		            	
		            } while (cur.moveToNext());
		        }
			 else
			 {
        		 Log.d("DB Get news values","no val");
			 }
			 db.close();
			 return value;
	 }
    
    public String[] getResultConfig(DatabaseHelper helper, String att, String selection)
	{
		Log.i("getResultConfig", "Get: " + att);

		String[] value;
		int i = 0;

		SQLiteDatabase db = helper.getReadableDatabase();

		Cursor cur = null ;
		cur = db.query("wpconfigmd",
				new String[] {"*"},
				selection,
				null,
				null,
				null,
				null);
		value = new String[cur.getColumnCount()];

		if (cur.moveToFirst())
		{
			do
			{
				for (i=0;i<5;i++)
				{
					value[i]= cur.getString(i);
					Log.i("DB getResultConfig","val = "+cur.getString(i));
				}

			} while (cur.moveToNext());
		}
		else
		{
			Log.d("DB getResultConfig","no val");
		}
		db.close();
		return value;
	}
    
    public boolean SaveDB(DatabaseHelper helper, int primary, String Title, String Info, String NType, byte[] Image1, String SynCA, String sDomain, String sImgLoc)
    {   	
    	
    	/*
    	 public boolean SaveDB(DatabaseHelper helper, int primary, String Author, String AuthorMail,
    		String Title, String Info, String NType, byte[] Image1, String SynCB, String SynCA)
    	 */
    	
		 boolean success;
		 
    	// Open the database for writing
    	db = dbHelper.getWritableDatabase();
    	    	
    	// Loop over the time line and print it out
    	ContentValues values = new ContentValues();
    	    	
    	// Insert into database
    	values.clear(); 
    	values.put(DatabaseHelper.COLUMN_ID, primary);
    	//values.put(DatabaseHelper.NewsID, null);
    	//values.put(DatabaseHelper.Author, Author);
    	//values.put(DatabaseHelper.AuthorMail, AuthorMail);

    	    	
    	try {
	    	db.insertOrThrow(DatabaseHelper.TABLE_NAME, null, values);
	    	//Log.i("dbActivities - SaveDB : try.", "Insert news");
	    	success = true;
    	}catch (SQLException e) {
    		//Ignore exception
    		Log.e("dbActivities - SaveDB.", e.getMessage());
    		success = false;
    	}
    	
    	db.close();
    	    	
    	return success;
    }
    
    public boolean SaveConfigDB(DatabaseHelper helper, int primary, String username, String password, String server, boolean active,String version)
    {

		boolean success;
		long lReturn;
		int iActive = (active)? 1 : 0;
		
    	// Open the database for writing
    	db = helper.getWritableDatabase();
    	
    	// Loop over the time line and print it out
    	ContentValues values = new ContentValues();
    	
    	// Insert into database
    	values.clear(); 
    	values.put("_ID", primary);
    	values.put("username", username);
    	values.put("password", password);
    	values.put("server", server);
    	values.put("active", iActive);
    	values.put("version",version);
    	
    	try {
    		lReturn=db.insertOrThrow("wpconfigmd", null, values); 
	    	//Log.i("dbActivities - SaveConfigDB : Insert into database; primary='"+primary+"'", "Save Configuration");

	    	if (lReturn > 0)
	    		success = true;
	    	else
	    		success = false;
	    	
    	}catch (SQLException e) {
    		//Ignore exception
    		Log.e("Error SaveConfigDB", e.getMessage());
    		success = false;
    	}
    	db.close();
    	return success;
    }
    
    public boolean SaveNewsCatDB(DatabaseHelper helper, int primary, String username, String password, String server)
    {

		 boolean success;
    	// Open the database for writing
    	db = helper.getWritableDatabase();
    	// Loop over the time line and print it out
    	ContentValues values = new ContentValues();
    	
    	// Insert into database
    	values.clear(); 
    	values.put("_ID", primary);
    	values.put("CatID", username);
    	values.put("Category", password);
    	
    	try {
    		db.delete("wpnewscatmd", "_ID="+primary, null); 
	    	db.insertOrThrow("wpnewscatmd", null, values); 
	    	//Log.i("dbActivities - SaveConfigDB : Insert into database", "Insert user configuration");
	    	success = true;
    	}catch (SQLException e) {
    		//Ignore exception
    		Log.e("Error insert", e.getMessage());
    		success = false;
    	}
    	db.close();
    	return success;
    }

    public void deletebyId(int id)
    {
    	db = dbHelper.getWritableDatabase();
    	try {
    		db.delete(DatabaseHelper.TABLE_NAME, "_ID="+id, null);
	    	Log.d("Delete from db", "Insert new property");
    	}catch (SQLException e) {
    		//Ignore exception
    		Log.e("Error delete", e.getMessage());
    	}
    	db.close();
    }
    
    public void deleteConfigById(int id)
    {
    	db = dbHelper.getWritableDatabase();
    	try {
    		db.delete("wpconfigmd", "_ID='"+id+"'", null); 
    	}catch (SQLException e) {
    		//Ignore exception
    		Log.e("Error delete", e.getMessage());
    	}
    	db.close();
    }
    
    public boolean updateDB(DatabaseHelper helper, int primary, String Title, String Info, String NType, byte[] Image1, String SynCA, String sDomain, String sImgLoc)
	 {
		
		 db = dbHelper.getWritableDatabase();
		 ContentValues values = new ContentValues();
	    	
		 // Insert into database
		 List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
	     //nameValuePair.add((NameValuePair) new BasicNameValuePair("Author", Author));
	     //nameValuePair.add((NameValuePair) new BasicNameValuePair("AuthorMail", AuthorMail));
	     //nameValuePair.add((NameValuePair) new BasicNameValuePair(DatabaseHelper.Title, Title));
	     //nameValuePair.add((NameValuePair) new BasicNameValuePair(DatabaseHelper.Info, Info));
	     //nameValuePair.add((NameValuePair) new BasicNameValuePair(DatabaseHelper.NType, NType));
	     //nameValuePair.add((NameValuePair) new BasicNameValuePair("SynCB", SynCB));
	     //nameValuePair.add((NameValuePair) new BasicNameValuePair(DatabaseHelper.SynCA, SynCA));
	     //nameValuePair.add((NameValuePair) new BasicNameValuePair(DatabaseHelper.Domain, sDomain));
	     //nameValuePair.add((NameValuePair) new BasicNameValuePair(DatabaseHelper.imgLoc, sImgLoc));
	    	
	     //values.clear();
	     //values.put(DatabaseHelper.Pic, Image1);
	     
	     for(int index=0; index < nameValuePair.size(); index++) {
	    	 //Log.d("Update "+index+"= "+nameValuePair.get(index).getName(), nameValuePair.get(index).getValue());
	    	 values.put(nameValuePair.get(index).getName(), nameValuePair.get(index).getValue());
	     }
	     
		 try 
		 {
		 	db.update(DatabaseHelper.TABLE_NAME, values, "_ID = "+primary, null);
	    	//Log.i("Update database", "Update the label. Rows updated = "+rowUpdate);
		 	db.close();
	    	return true;
		 }
		 catch (SQLException e)
		 { 
    		//Ignore exception
    		Log.e("Error update", e.getMessage());
    		db.close();
    		return false;
		 }
	 }
    
    public boolean updateDB(String Pid, int primary)
	 {
		
		 db = dbHelper.getWritableDatabase();
		 ContentValues values = new ContentValues();
	    	
	    	// Insert into database
	    	//values.clear();
	    	//values.put(DatabaseHelper.NewsID, Pid);
	    	
		
		 try {
			 	@SuppressWarnings("unused")
				int rowUpdate = db.update(DatabaseHelper.TABLE_NAME, values, "_ID = "+primary, null);
		    	//Log.i("Update database", "Update the label.Rows updated = "+rowUpdate);
		    	db.close();
		    	return true;
	    	}catch (SQLException e) {
	    		//Ignore exception
	    		Log.e("Error update", e.getMessage());
	    		db.close();
	    		return false;
	    	}
	 }
    
    public boolean UpdateConfigDB(DatabaseHelper helper, int primary, String username, String password, String server, boolean active,String version)
    {
		boolean success;
		int iActive = (active)? 1 : 0;

		// Open the database for writing
		db = helper.getWritableDatabase();
		// Loop over the time line and print it out
		ContentValues values = new ContentValues();
		
		// Insert into database
		values.clear(); 
		values.put("_ID", primary);
		values.put("username", username);
		values.put("password", password);
		values.put("server", server);
		values.put("active", iActive);
        values.put("version", iActive);
	     
		try 
		{
			db.update("wpconfigmd", values, "_ID = "+primary, null);
			Log.i("UpdateConfigDB", "server='"+server+"'; ID = "+primary);
		 	success = true;
		}
		catch (SQLException e)
		{ 
			//Ignore exception
			Log.e("Error UpdateConfigDB", e.getMessage());
			success = false;
		}
		 
    	db.close();
    	return success;
    }

	public boolean UpdateConfigDBVersion(DatabaseHelper helper, int primary,String version)
	{
		boolean success;


		// Open the database for writing
		db = helper.getWritableDatabase();
		// Loop over the time line and print it out
		ContentValues values = new ContentValues();

		// Insert into database
		values.clear();

		values.put("version", version);

		try
		{
			db.update("wpconfigmd", values, "_ID = "+primary, null);

			success = true;
		}
		catch (SQLException e)
		{
			//Ignore exception
			Log.e("Error UpdateConfigDB", e.getMessage());
			success = false;
		}

		db.close();
		return success;
	}

    public boolean UpdateAllUnActive(DatabaseHelper helper)
    {
		boolean success;
		int iRetVal;

		// Open the database for writing
		db = helper.getWritableDatabase();
		
		// Loop over the time line and print it out
		ContentValues values = new ContentValues();
		
		// Insert into database
		values.clear(); 
		values.put("active", 0);
	     
		try 
		{
			iRetVal = db.update("wpconfigmd", values, "active='1'", null);
			Log.i("UpdateAllUnActive", "iRetVal='"+iRetVal+"'");
			
			if (iRetVal > 0)
				success = true;
			else
				success = false;
		}
		catch (SQLException e)
		{ 
			//Ignore exception
			Log.e("Error UpdateAllUnActive", e.getMessage());
			success = false;
		}
		 
    	db.close();
    	return success;
    }
    
    public boolean UpdateActiveStat(DatabaseHelper helper, int iprimary)
    {
		boolean success;
		int iRetVal;

		// Open the database for writing
		db = helper.getWritableDatabase();
		
		// Loop over the time line and print it out
		ContentValues values = new ContentValues();
		
		// Insert into database
		values.clear(); 
		values.put("active", 1);
	     
		try 
		{
			iRetVal = db.update("wpconfigmd", values, "_ID='"+iprimary+"'", null);
			Log.i("UpdateActiveStat", "iRetVal='"+iRetVal+"'; _ID='"+iprimary+"'.");
			
			if (iRetVal > 0)
				success = true;
			else
				success = false;
		}
		catch (SQLException e)
		{ 
			//Ignore exception
			Log.e("Error UpdateActiveStat", e.getMessage());
			success = false;
		}
		 
    	db.close();
    	return success;
    }
}
