package tests;

import java.io.File;
import java.io.IOException;

import tools.BeachProfileTracking;
import tools.FeatureCollectionValidation;
import tools.GeoJsonUtils;

public class TestGeoJson {

	public static void main(String[] args) {
		long startTime = System.nanoTime();

		File dataDir = new File("data");
		File beachProfileFile = new File(dataDir, "profil_test3.json");
		
		BeachProfileTracking bp = new BeachProfileTracking();
		
		FeatureCollectionValidation fcv = new FeatureCollectionValidation();
		try {
			bp.createCSVFile(fcv.calculWithErrorManager(GeoJsonUtils.geoJsonToFeatureCollection(beachProfileFile), 0.1, true, 0, 10), dataDir, "result.csv");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		long endTime = System.nanoTime();
		long duration = (endTime - startTime)/1000000 ;
		System.out.println(duration + "ms");
		
	}
}
