package tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import net.sf.geographiclib.Geodesic;
import net.sf.geographiclib.GeodesicData;
import net.sf.geographiclib.GeodesicLine;
import net.sf.geographiclib.GeodesicMask;

public class BeachProfileUtils {
	
	private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * @param coordinates Array of coordinates
	 * @return Area of the profile in m²/m.l.
	 */
	public static double getProfileArea(Coordinate[] coordinates, double minDist, double maxDist){
		double area = 0;
		double totalDist = 0;
		double tempDist = 0;
		Geodesic geod = Geodesic.WGS84;
		for(int i = 0; i < coordinates.length; i++){
			if(i+1 < coordinates.length){
				GeodesicData d = geod.Inverse(coordinates[i].y, coordinates[i].x, coordinates[i+1].y, coordinates[i+1].x);
				tempDist = Math.round(totalDist * 1000.0) / 1000.0;
				totalDist += d.s12;
				//System.out.println(tempDist + " : " + Math.round(totalDist * 1000.0) / 1000.0);
				if(minDist <= tempDist && maxDist+0.01 >= Math.round(totalDist * 1000.0) / 1000.0){
					//System.out.println(Math.round(totalDist * 1000.0) / 1000.0 + " : " + coordinates[i].z + " : " + coordinates[i+1].z + " : " + ((coordinates[i].z + coordinates[i+1].z )*d.s12)/2);
					area += ((coordinates[i].z + coordinates[i+1].z)*d.s12)/2;					
				}
			}
		}
		area = area/(maxDist-minDist);
		//System.out.println((maxDist-minDist) + " : " + area);
		return area;
	}
	
	/**
	 * Get the distance between a list of coordinates
	 * @param coordinates Array of Coordinate
	 * @return The total distance traveled between the points in meters
	 */
	public static double getDistanceFromCoordinates(Coordinate[] coordinates){
		if(coordinates.length < 1) return 0;
		double totalDist = 0;
		Geodesic geod = Geodesic.WGS84;
		for(int i = 1; i < coordinates.length; i++){
			GeodesicData d = geod.Inverse(coordinates[i-1].y, coordinates[i-1].x, coordinates[i].y, coordinates[i].x);
			totalDist += d.s12;
		}
		//System.out.println(totalDist);
		return totalDist;
	}
	
	/**
	 * Transform degree value to radiant
	 * @param deg
	 * @return a radiant
	 */
	public static double deg2rad(double deg) {
		return deg * (Math.PI/180);
	}
	
	/**
	 * @param featureCollection
	 * @return all LineString of the featureCollection
	 */
	public static Map<String, LineString> getProfilesFromFeature(FeatureCollection<SimpleFeatureType, SimpleFeature> featureCollection) {
		Map<String, LineString> linestrings = new TreeMap<String,LineString>();
		FeatureIterator<SimpleFeature> iterator = featureCollection.features();
		// get LineString from Feature
		while (iterator.hasNext()) {
			SimpleFeature feature = iterator.next();
			Geometry geometry = (Geometry) feature.getDefaultGeometry();
			if (geometry instanceof LineString){
				LineString ls = (LineString) geometry;
				Collection<Property> properties = feature.getProperties();
				String dateStr = "";
				for (Property property : properties){
					try {
						dateFormat.parse(property.getValue().toString());
						dateStr = property.getValue().toString();
					} catch (ParseException e) { }
				}					
				linestrings.put(dateStr, ls);
			}
		}
		return linestrings;
	}	
	
	//interpolate between two points
	public static LinkedList<Coordinate> InterpolateCoordinates(double offset, double distInterval, Coordinate c1, Coordinate c2){
		Geodesic geod = Geodesic.WGS84;
		GeodesicData d = geod.Inverse(c1.y, c1.x, c2.y, c2.x);		 
		GeodesicLine line = new GeodesicLine(geod, d.lat1, d.lon1, d.azi1);
		// The max number of points
		int num = (int)(d.s12 / distInterval)+1;
		//the height coefficient
		double diff = (c1.z-c2.z)/d.s12;
		
		LinkedList<Coordinate> l = new LinkedList<Coordinate>();	
		l.add(new Coordinate(c1.x,c1.y,c1.z));
		for (int i = 1; i <= num; i++) {
			if((i * distInterval)-offset < d.s12){
				GeodesicData g = line.Position((i * distInterval)-offset, GeodesicMask.LATITUDE | GeodesicMask.LONGITUDE);
				l.add(new Coordinate(g.lon2,g.lat2,c1.z - diff*((distInterval*i)-offset)));
			}
		}
		l.add(new Coordinate(c2.x,c2.y,c2.z));		
		return l;
	}
	
	public static void createCSVFile(String csvString, File dataDir, String fileName) {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(new File(dataDir, fileName)));
			bw.write(csvString);
		} catch (IOException e) {
			System.out.println("erreur entrées sorties");
		} finally {
			try {
				bw.close();
			} catch (IOException e) {
				System.out.println("erreur entrées sorties");
			}
		}
	}
	
//	public static FeatureCollection<SimpleFeatureType, SimpleFeature> createFeatureError() {	
//	}
}
