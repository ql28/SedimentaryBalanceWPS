package BeachProfile.WPSpackage;

import org.geotools.process.factory.*;
import org.geotools.text.Text;
import org.geotools.feature.FeatureCollection;
import org.opengis.feature.simple.*;
import java.awt.image.BufferedImage;

import com.vividsolutions.jts.geom.*;

import tools.BeachProfileTracking;

public class BeachProfileTracking_sedimentaryBalanceCalc_class extends StaticMethodsProcessFactory<BeachProfileTracking_sedimentaryBalanceCalc_class> {
	
	protected static BeachProfileTracking callObject;

	public BeachProfileTracking_sedimentaryBalanceCalc_class() {
		super(Text.text("beach profile analysis"),"BeachProfile",BeachProfileTracking_sedimentaryBalanceCalc_class.class);
		callObject = new BeachProfileTracking();
	}

	@DescribeProcess(title="BeachProfileTracking_sedimentaryBalanceCalc",description="Add a description of BeachProfileTracking_sedimentaryBalanceCalc")
	@DescribeResult(name="result",description="the result of the beach profile calcul")
	public static String BeachProfileTracking_sedimentaryBalanceCalc(@DescribeParameter(name="profile",description=" the beach profile") FeatureCollection<SimpleFeatureType, SimpleFeature> profile) {
		String result;
		result = callObject.sedimentaryBalanceCalc( profile);

		return result;
	}
}
