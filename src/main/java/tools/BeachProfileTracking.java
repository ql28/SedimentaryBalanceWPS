package tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.CRS;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.CoordinateSequenceFactory;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;

import net.sf.geographiclib.*;

public class BeachProfileTracking {

	private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	public BeachProfileTracking() {}
	
//	public FeatureCollection<SimpleFeatureType, SimpleFeature> FileValidation(File f){
//		
//		//TODO write the warning message in the featureCollection returned
//		SimpleFeatureTypeBuilder b = new SimpleFeatureTypeBuilder();
//		b.setName("ErrorFeature");
//		b.add("error", String.class);
//		SimpleFeatureType type = b.buildFeatureType();
//		SimpleFeatureBuilder builder = new SimpleFeatureBuilder(type);		
//		DefaultFeatureCollection dfc = new DefaultFeatureCollection();
//		FeatureCollection<SimpleFeatureType, SimpleFeature> fc;
//		
//		// check if the file contains a FeatureCollection. If it does, return it. Otherwise, return an empty FeatureCollection
//		try {
//			fc = GeoJsonUtils.geoJsonToFeatureCollection(f);
//		} catch (Exception e) {
//			builder.set("error", "Impossible to find the FeatureCollection of the file.");
//			SimpleFeature sf = builder.buildFeature(null);
//			dfc.add(sf);
//			return dfc;
//		}
//
//		// check if the file contains a CoordinateReferenceSystem and if it's a WGS 84
//		try {
//			CoordinateReferenceSystem myCRS = GeoJsonUtils.geoJsonToCoordinateReferenceSystem(f);
//			CoordinateReferenceSystem refCRS = CRS.decode("EPSG:4326");
//			if(!CRS.equalsIgnoreMetadata(myCRS, refCRS)){
//				builder.set("error", "The CoordinateReferenceSystem is not of type WGS 84 (EPSG 4326)..");
//				SimpleFeature sf = builder.buildFeature(null);
//				dfc.add(sf);		
//				FeatureIterator<SimpleFeature> iterator = fc.features();
//				while (iterator.hasNext()) {
//					SimpleFeature feature = iterator.next();
//					dfc.add(feature);
//				}
//				return dfc;
//			}
//		} catch (Exception e1) {
//			builder.set("error", "Impossible to find the CoordinateReferenceSystem of the file.");
//			SimpleFeature sf = builder.buildFeature(null);
//			dfc.add(sf);		
//			FeatureIterator<SimpleFeature> iterator = fc.features();
//			while (iterator.hasNext()) {
//				SimpleFeature feature = iterator.next();
//				dfc.add(feature);
//			}
//			return dfc;
//		}
//		
//		return fc;
//	}
	
//	public boolean FeatureCollectionValidation(FeatureCollection<SimpleFeatureType, SimpleFeature> fc){
//		//we want a specific format to our featureCollection : multiple features each with a date as parameter and a geometry of type LineString	
//		FeatureIterator<SimpleFeature> iterator = fc.features();
//		//check if the FeatureCollection contains features
//		if(iterator.hasNext() == false){
//			System.out.println("Warning : the FeatureCollection is empty.");
//			return false;
//		}
//		while (iterator.hasNext()) {
//			SimpleFeature feature = iterator.next();
//			if(feature.getName().getLocalPart() == "ErrorFeature") System.out.println(feature.getAttribute("error"));
//			//check if each feature has a Geometry of type LineString
//			if(!feature.getDefaultGeometry().getClass().getSimpleName().equals("LineString")){
//				System.out.println("Warning : A feature doesn't contains a Geometry of type LineString");
//				return false;
//			}
//			//check if each feature has a date at the good format			
//			dateFormat.setLenient(false);			
//			Collection<Property> properties = feature.getProperties();
//			boolean hasDate = false;
//			for (Property property : properties){
//				try {
//					dateFormat.parse(property.getValue().toString());
//					hasDate = true;
//				} catch (ParseException e) { }
//			}
//			if(!hasDate){
//				System.out.println("Warning : Date of feature not found");
//				return false;
//			}
//		}
//		return true;
//	}
	
	public FeatureCollection<SimpleFeatureType, SimpleFeature> InterpolateFeatureCollection(FeatureCollection<SimpleFeatureType, SimpleFeature> fc, double interval){
		
		GeometryFactory geometryFactory = new GeometryFactory();
		DefaultFeatureCollection resultFeatureCollection = null;
		Map<String, LineString> lineStrings = BeachProfileUtils.getProfilesFromFeature(fc);
		Map<String, LineString> interpolatedLineStrings = new HashMap<String,LineString>();
		
		lineStrings.forEach((a,b) -> {
			if(b.getNumPoints() > 1){
				Coordinate[] coordinates = b.getCoordinates();
				LinkedList<Coordinate> newCoordinates = new LinkedList<Coordinate>();				
				LinkedList<Coordinate> tempList;
				double offset = 0.0;
				double totalDist = 0.0;
				for (int i = 1; i < coordinates.length; i++) {
					Geodesic geod = Geodesic.WGS84;
					GeodesicData d = geod.Inverse(coordinates[i-1].y, coordinates[i-1].x, coordinates[i].y, coordinates[i].x);
					totalDist += d.s12;
					tempList = BeachProfileUtils.InterpolateCoordinates(offset, interval, coordinates[i-1], coordinates[i]);
					if(i != coordinates.length -1) tempList.removeLast();
					newCoordinates.addAll(tempList);
					offset = totalDist%interval;
			    }				
				//create new linestring containing all the coordinates
				LineString ls = geometryFactory.createLineString(newCoordinates.toArray(new Coordinate[newCoordinates.size()]));
				interpolatedLineStrings.put(a, ls);
			}
		});
		
		SimpleFeatureTypeBuilder simpleFeatureTypeBuilder = new SimpleFeatureTypeBuilder();
		simpleFeatureTypeBuilder.setName("featureType");
		simpleFeatureTypeBuilder.add("geometry", LineString.class);
		simpleFeatureTypeBuilder.add("date", String.class);
		
		// init DefaultFeatureCollection
		SimpleFeatureBuilder simpleFeatureBuilder = new SimpleFeatureBuilder(simpleFeatureTypeBuilder.buildFeatureType());
		resultFeatureCollection = new DefaultFeatureCollection(null, simpleFeatureBuilder.getFeatureType());

		// add geometrie to defaultFeatures
		for (Entry<String, LineString> entry : interpolatedLineStrings.entrySet())
		{
			simpleFeatureBuilder.add(entry.getValue());
			simpleFeatureBuilder.add(entry.getKey());
			resultFeatureCollection.add(simpleFeatureBuilder.buildFeature(entry.getKey() + ""));
		}
		return resultFeatureCollection;
	}
	
	public String sedimentaryBalanceCalc(FeatureCollection<SimpleFeatureType, SimpleFeature> profile, boolean ignoreDistLessThanFirstDate, double minDist, double maxDist) {
		Coordinate[] coordinates = null;
		String res = "result : \nDate ; Volume sedimentaire (m^3/m.l.) ; Difference avec date precedente ; Pourcentage evolution precedente ; Pourcentage evolution totale\n";		

		SimpleFeatureTypeBuilder simpleFeatureTypeBuilder = new SimpleFeatureTypeBuilder();
		simpleFeatureTypeBuilder.setName("featureType");
		simpleFeatureTypeBuilder.add("geometry", MultiPoint.class);
		simpleFeatureTypeBuilder.add("type", String.class);
		simpleFeatureTypeBuilder.add("name", String.class);

		Map<String, LineString> refProfile = BeachProfileUtils.getProfilesFromFeature(profile);
		double refProfileArea = 0;
		double lastProfileArea = 0;
		double tempProfileArea = 0;
		double tempProfileDist = 0;
		double totalEvolutionPercent = 0;
		double tempMaxDist = 0;
		for (Entry<String, LineString> entry : refProfile.entrySet()) {
			coordinates = entry.getValue().getCoordinates();
			if(refProfileArea == 0){
				//if we don't specify maxDist, check ignoreDateWithLessDist					
				//if ignoreDistLessThanFirstDate is true, ignore the feature with a distance less than the distance of the first date
				//else if ignoreDateWithLessDist is false, use the smallest distance of all features
				tempMaxDist = BeachProfileUtils.getDistanceFromCoordinates(coordinates);
				if(!ignoreDistLessThanFirstDate){
					for (Entry<String, LineString> entry2 : refProfile.entrySet()) {
						double dist = BeachProfileUtils.getDistanceFromCoordinates(entry2.getValue().getCoordinates());
						tempMaxDist = dist < tempMaxDist ? dist : tempMaxDist;						
					}
				}
				if(maxDist > tempMaxDist || maxDist <= 0) maxDist = tempMaxDist;
				//if minDist > maxDist
				if(minDist > maxDist) minDist = 0;					
			
				refProfileArea = lastProfileArea = BeachProfileUtils.getProfileArea(coordinates, minDist, maxDist);
				//System.out.println(refProfileArea);
				res += entry.getKey().toString() + " ; ";
				res += BeachProfileUtils.getProfileArea(coordinates, minDist, maxDist) + " ; 0 ; 0 % ; 0 %\n";
			}
			else{
				tempProfileDist = BeachProfileUtils.getDistanceFromCoordinates(coordinates);
				if(tempProfileDist < maxDist){
					System.out.println(entry.getKey().toString() + " | " + tempProfileDist + " | distance at this date is less than the distance wanted");
				}
				else{
					tempProfileArea = BeachProfileUtils.getProfileArea(coordinates, minDist, maxDist);
					totalEvolutionPercent += (tempProfileArea - lastProfileArea)/lastProfileArea*100;
					res += entry.getKey().toString() + " ; ";
					res += tempProfileArea + " ; ";
					res += (tempProfileArea - lastProfileArea) + " ; ";
					res += (tempProfileArea - lastProfileArea)/lastProfileArea*100 + " % ; ";
					res += totalEvolutionPercent + " % \n";
					//2.4324304 × 100 ÷ 2.5368383 = 95.884
					lastProfileArea = tempProfileArea;
				}		
			}
        }
		//System.out.println(res);
		return res;
	}
}
