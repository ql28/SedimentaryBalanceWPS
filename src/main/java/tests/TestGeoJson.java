package tests;

import java.io.File;
import java.io.IOException;

import org.geotools.feature.FeatureCollection;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import tools.BeachProfileTracking;
import tools.BeachProfileUtils;
import tools.FeatureCollectionValidation;
import tools.GeoJsonUtils;

public class TestGeoJson {

	public static void main(String[] args) {
		long startTime = System.nanoTime();

		File dataDir = new File("data");
		File beachProfileFile = new File(dataDir, "profil_test3.json");
	
		
		
		FeatureCollectionValidation fcv = new FeatureCollectionValidation();
		
		fcv.FileValidation(beachProfileFile);
		
//		
//		BeachProfileTracking bp = new BeachProfileTracking();
//		
//		// check if file contains featurecollection
//		FeatureCollection<SimpleFeatureType, SimpleFeature> fc = bp.FileValidation(beachProfileFile);
//		try {
//			GeoJsonUtils.featureCollectionToGeoJsonFile(fc, dataDir, "testError");
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//
//			
//		// check if feature contains the good format : date and LineString
//		System.out.println(bp.FeatureCollectionValidation(fc));
//		// do the interpolation and return a new featurecollection with all the new coordinates
//		//long startTime = System.nanoTime();
//		FeatureCollection<SimpleFeatureType, SimpleFeature> fc2 = bp.InterpolateFeatureCollection(fc, 0.1);
//		//long endTime = System.nanoTime();
//
//		try {
//			GeoJsonUtils.featureCollectionToGeoJsonFile(bp.InterpolateFeatureCollection(fc, 0.1), dataDir, "test");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		// do the area calculation	
//		//System.out.println(bp.sedimentaryBalanceCalc(fc2, false, 0, 0));
//		BeachProfileUtils.createCSVFile(bp.sedimentaryBalanceCalc(fc2, false, 0, 0), dataDir, "bilan_sedimentaire.csv");
//
//		long endTime = System.nanoTime();
//		long duration = (endTime - startTime)/1000000 ;
//		System.out.println(duration);
		
	}
}
