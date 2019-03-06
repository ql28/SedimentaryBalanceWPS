package BeachProfile.WPSpackage;

import java.io.IOException;
import java.util.*;

import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.process.factory.StaticMethodsProcessFactory;
import org.geotools.text.Text;
import org.geotools.feature.FeatureCollection;
import org.opengis.feature.simple.*;
import java.awt.image.BufferedImage;
import com.vividsolutions.jts.geom.*;

import com.vividsolutions.jts.io.ParseException;

import remoteWpsCall.*;

import tools.BeachProfileTracking;
import tools.BeachProfileTracking;
import tools.BeachProfileTracking;

public class BeachProfileTracking_class extends StaticMethodsProcessFactory<BeachProfileTracking_class>
{
		protected static BeachProfileTracking callObject_1;
		protected static BeachProfileTracking callObject_2;
		protected static BeachProfileTracking callObject_3;

	public BeachProfileTracking_class() {
		super(Text.text("beach profile analysis"),"BeachProfile",BeachProfileTracking_class.class);
		callObject_1 = new BeachProfileTracking();
		callObject_2 = new BeachProfileTracking();
		callObject_3 = new BeachProfileTracking();
	}

	@DescribeProcess(title="BeachProfileTracking",description="Workflow containing the Interpolation, SedimentaryBalance calculation and conversion to csv wps")
	@DescribeResult(name="result",description="A text containing feature collection informations formatted to csv format")
	public static String BeachProfileTracking(@DescribeParameter(name="fc",description=" featureCollection containing the profile geometries") FeatureCollection<SimpleFeatureType, SimpleFeature> fc,@DescribeParameter(name="interval",description=" interval used for the interpolation") Double interval,@DescribeParameter(name="useSmallestDistance",description=" default true. If useSmallestDistance is true, use the smallest distance between all features, else ignore the feature shorter than the first one") Boolean useSmallestDistance,@DescribeParameter(name="minDist",description=" specifie the minimum distance of the interval of calculation") Double minDist,@DescribeParameter(name="maxDist",description=" specifie the maximum distance of the interval of calculation") Double maxDist) {
		FeatureCollection<SimpleFeatureType, SimpleFeature> fc_wps1 ;	
		Double interval_wps1 = 0.0;	
		FeatureCollection<SimpleFeatureType, SimpleFeature> result_wps1 ;
		FeatureCollection<SimpleFeatureType, SimpleFeature> profile_wps2 ;	
		Boolean useSmallestDistance_wps2 = true;	
		Double minDist_wps2 = 0.0;	
		Double maxDist_wps2 = 0.0;	
		FeatureCollection<SimpleFeatureType, SimpleFeature> result_wps2 ;
		FeatureCollection<SimpleFeatureType, SimpleFeature> featureCollection_wps3 ;	
		String result_wps3 = "";
		String result;
		fc_wps1=fc;
		interval_wps1=interval;
		useSmallestDistance_wps2=useSmallestDistance;
		minDist_wps2=minDist;
		maxDist_wps2=maxDist;


		result_wps1 = callObject_1.InterpolateFeatureCollection(fc_wps1,interval_wps1);	
		profile_wps2=result_wps1;
		result_wps2 = callObject_2.sedimentaryBalanceCalc(profile_wps2,useSmallestDistance_wps2,minDist_wps2,maxDist_wps2);	
		featureCollection_wps3=result_wps2;
		result_wps3 = callObject_3.featureToCSV(featureCollection_wps3);	
		result=result_wps3;	
		return result;	
	}
}
