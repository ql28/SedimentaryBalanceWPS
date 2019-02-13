package tests;

import java.io.File;

import tools.BeachProfileTracking;
import tools.FeatureCollectionValidation;

public class TestGeoJson {

	public static void main(String[] args) {
		long startTime = System.nanoTime();

		File dataDir = new File("data");
		File beachProfileFile = new File(dataDir, "profil_test3.json");
	
		
		BeachProfileTracking bp = new BeachProfileTracking();
		
		FeatureCollectionValidation fcv = new FeatureCollectionValidation();
		bp.createCSVFile(fcv.fileValidation(beachProfileFile), dataDir, "result.csv");

		long endTime = System.nanoTime();
		long duration = (endTime - startTime)/1000000 ;
		System.out.println(duration + "ms");
		
	}
}
