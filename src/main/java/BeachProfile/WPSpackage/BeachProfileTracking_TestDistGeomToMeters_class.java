package BeachProfile.WPSpackage;

import org.geotools.process.factory.*;
import org.geotools.text.Text;
import org.geotools.feature.FeatureCollection;
import org.opengis.feature.simple.*;
import java.awt.image.BufferedImage;

import com.vividsolutions.jts.geom.*;

import tools.BeachProfileTracking;

public class BeachProfileTracking_TestDistGeomToMeters_class extends StaticMethodsProcessFactory<BeachProfileTracking_TestDistGeomToMeters_class> {
	
	protected static BeachProfileTracking callObject;

	public BeachProfileTracking_TestDistGeomToMeters_class() {
		super(Text.text("beach profile analysis"),"BeachProfile",BeachProfileTracking_TestDistGeomToMeters_class.class);
		callObject = new BeachProfileTracking();
	}

	@DescribeProcess(title="BeachProfileTracking_TestDistGeomToMeters",description="test the convertion method that return distance between 2 coordinates in meters")
	@DescribeResult(name="result",description="return distance between 2 points in meters")
	public static String BeachProfileTracking_TestDistGeomToMeters(@DescribeParameter(name="lat1",description=" lat of point 1") Double lat1,@DescribeParameter(name="lon1",description=" long of point 1") Double lon1,@DescribeParameter(name="lat2",description=" lat of point 2") Double lat2,@DescribeParameter(name="lon2",description=" lon of point 2") Double lon2) {
		String result;
		result = callObject.TestDistGeomToMeters( lat1, lon1, lat2, lon2);

		return result;
	}
}
