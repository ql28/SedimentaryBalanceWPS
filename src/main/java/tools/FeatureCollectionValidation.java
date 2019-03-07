package tools;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;

import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.CRS;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class FeatureCollectionValidation {

	private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	public FeatureCollectionValidation(){}
		
	public FeatureCollection<SimpleFeatureType, SimpleFeature> calculWithErrorManager(FeatureCollection<SimpleFeatureType, SimpleFeature> fc, double interpolationValue, boolean useSmallestDistance, double minDist, double maxDist){
				
		SimpleFeatureTypeBuilder b = new SimpleFeatureTypeBuilder();
		b.setName("ErrorFeature");
		b.add("error", String.class);
		SimpleFeatureType type = b.buildFeatureType();
		SimpleFeatureBuilder builder = new SimpleFeatureBuilder(type);		
		DefaultFeatureCollection dfc = new DefaultFeatureCollection();
		FeatureCollection<SimpleFeatureType, SimpleFeature> fcInterpolation, fcResult;
						
		//we want a specific format to our featureCollection : multiple features each with a date as parameter and a geometry of type LineString	
		FeatureIterator<SimpleFeature> iterator = fc.features();
		//check if the FeatureCollection contains features
		if(iterator.hasNext() == false){
			builder.set("error", "The FeatureCollection is empty. No features found");
			SimpleFeature sf = builder.buildFeature(null);
			dfc.add(sf);
		}
		int i = 0;
		while (iterator.hasNext()) {
			i++;
			SimpleFeature feature = iterator.next();
			//check if each feature has a Geometry of type LineString
			if(!feature.getProperty("geometry").getType().getBinding().getSimpleName().equals("LineString")){
				builder.set("error", "The feature " + i + " doesn't contains a Geometry of type LineString");
				SimpleFeature sf = builder.buildFeature(null);
				dfc.add(sf);
			}
			//check if each feature has a date at the good format			
			dateFormat.setLenient(false);			
			Collection<Property> properties = feature.getProperties();
			boolean hasDate = false;
			for (Property property : properties){
				try {
					dateFormat.parse(property.getValue().toString());
					hasDate = true;
				} catch (ParseException e) { }
			}
			if(!hasDate){
				builder.set("error", "Date of the feature " + i + " not found");
				SimpleFeature sf = builder.buildFeature(null);
				dfc.add(sf);
			}
		}
		
		// check if the file contains a CoordinateReferenceSystem. if not, add a feature with an error message
		try {
			CoordinateReferenceSystem myCRS = fc.getSchema().getCoordinateReferenceSystem();
			//CoordinateReferenceSystem refCRS = CRS.decode("EPSG:4326");
			if(myCRS == null){
				builder.set("error", "Impossible to find the CoordinateReferenceSystem of the file");
				SimpleFeature sf = builder.buildFeature(null);
				dfc.add(sf);
			}
//			else if(!CRS.equalsIgnoreMetadata(object1, object2).equalsIgnoreMetadata(myCRS, refCRS)){
//				builder.set("error", "The CoordinateReferenceSystem is not of type WGS 84 (EPSG 4326).. : " + refCRS.toString() + " _____________ " + myCRS.toString());
//				SimpleFeature sf = builder.buildFeature(null);
//				dfc.add(sf);
//			}
		} catch (Exception e) {
			builder.set("error", "Impossible to find the CoordinateReferenceSystem of the file");
			SimpleFeature sf = builder.buildFeature(null);
			dfc.add(sf);
		}
				
		//other parameters checking
		if(interpolationValue < 0){
			
		}		
		//minDist et max Dist à 0 si le calcul se fait sur toute la longueur du profil
		if(minDist < 0)
		{
			
		}
		if(maxDist < 0)
		{
			
		}
		if(minDist >= maxDist && minDist != 0){
			
		}
	
		
		//if the file doesn't have any errors, do the treatment
		if(!dfc.features().hasNext()){			
			//do the interpolation
			BeachProfileTracking bp = new BeachProfileTracking();
			fcInterpolation = bp.InterpolateFeatureCollection(fc, interpolationValue);
			if(!fcInterpolation.features().hasNext()){
				builder.set("error", "Interpolation failed");
				SimpleFeature sf = builder.buildFeature(null);
				dfc.add(sf);
			}
			//do the calculation
			fcResult = bp.sedimentaryBalanceCalc(fcInterpolation, useSmallestDistance, minDist, maxDist);
			if(!fcResult.features().hasNext()){
				builder.set("error", "Sedimentary balance calcul failed");
				SimpleFeature sf = builder.buildFeature(null);
				dfc.add(sf);
			}
			else{
				return fcResult; //if the treatment worked, return the result FC 	
			}
		}	
		//return the error feature
		return dfc;
	}
}
