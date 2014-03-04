package fi.left2die.io; 

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fi.left2die.model.Record;

public class ScoreRecorder {	
	
	private String fileName;
	
	private String highscoreServerURL = "";
	
	//This limit how many top records are saved
	private final int RECORD_SIZE_LIMIT = 15;
	
	public ScoreRecorder( String fileName ) {
		this.fileName = fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	/**
	 * Check if there is a old record on a hard disk 
	 * if there is it load and joins the records.
	 * After that it saves records back to the hard disk. 
	 * 
	 * @param records
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void saveScores( List<Record>records ) throws IOException, ClassNotFoundException {
		
		boolean isThereAfile = true;
		
		File file = new File(fileName);
		
		List<Record>oldRecords = null;
		
		//If there is old records we are adding new scores to old ones
		if( file.isFile() ) {
			records = joinTopScores(records, loadScores() );
			file.delete();
		}
		
		isThereAfile = file.createNewFile();
				
		if( isThereAfile ) {
			FileOutputStream fileOut = new FileOutputStream(fileName);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject( records );
			out.close();
			fileOut.close();
		}
	}	
	
	/**
	 * It takes new and old records joins 
	 * them together and returns result.
	 * 
	 * @param newRecords New records can hold too old records from old score list.
	 * @param oldRecords
	 * @return Joined records
	 */
	private List<Record> joinTopScores( List<Record> newRecords, 
									List<Record> oldRecords ) {
		
		//New Records are added to list only if they are really new
		for( Record record : newRecords ) {
			
			boolean isOnList = false;
			
			for( Record oldRecord : oldRecords ) {
				if( oldRecord.equals(record) )
					isOnList = true;
			}
			
			if( isOnList == false )
				oldRecords.add(record);
		}
		
		Collections.sort( oldRecords );		
		
		//Next lines drop excess records off		
		if( oldRecords.size() > RECORD_SIZE_LIMIT ) {
			newRecords = new ArrayList<Record>();
			
			for( int i=0; i< RECORD_SIZE_LIMIT; i++ ) {
				newRecords.add( oldRecords.get(i) );
			}

						
			return newRecords;
		}
		
		return oldRecords;
	}
	
	/**
	 * Load scores from a serialized record file.
	 * 
	 * @return If file contains records it returns those records, else return null;
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public List<Record> loadScores() throws IOException, ClassNotFoundException, FileNotFoundException {
	
	   File file = new File(fileName);
	   
	   List<Record>loadedRecords = null;
	   
	   if( file.exists() ) {
		   FileInputStream fileIn = new FileInputStream( fileName );
		   ObjectInputStream in = new ObjectInputStream(fileIn);
		   loadedRecords = (List<Record>) in.readObject();
		   in.close();
		   fileIn.close();  
	   }
       return loadedRecords;
	}
	
	public static List<Record> loadScoresFromNetwork() {
		return null;
	}
	
	public static void uploadScoresToNetwork( List<Record>recods ) {
		
	}
	
	/**
	 * Test main
	 * @param args
	 */
	public static void main(String [] args)	{
		
		
		ArrayList<Record>records = new ArrayList<Record>();
		
		//records.add( new Record("Teemu Tuhoaja", 1200 ));
		//records.add( new Record("Teemu Tuhoaja", 1300 ));
		
		String scoreFileName = "scores.src";
		
		//Saving 
		ScoreRecorder recorder = new ScoreRecorder( scoreFileName );
			
		try {
			recorder.saveScores( records );
			recorder.saveScores( records );
			recorder.saveScores( records );
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		//Loading
		List<Record>records2 = new ArrayList<Record>();
		
		ScoreRecorder loader = new ScoreRecorder(scoreFileName);
				
		try {
			records2 = loader.loadScores();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		System.out.println("Starting to printing the records:");
		
		int index = 1;
		
		for( Record record : records2 ) {
			System.out.println(  String.format("%-10s %-10d %10.1f", record.getName(), record.getScore(), record.getSurvivingTime() ));
		}
		
		//Testing loading an empty file
		loader = new ScoreRecorder("pallo.srz");
		
		try {
			loader.loadScores();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
