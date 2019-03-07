package BeachProfile.WPSpackage;

import org.geotools.process.factory.*;
import org.geotools.text.Text;
import org.geotools.feature.FeatureCollection;
import org.opengis.feature.simple.*;
import java.awt.image.BufferedImage;

import com.vividsolutions.jts.geom.*;

import tools.FeatureCollectionValidation;

public class FeatureCollectionValidation_calculWithErrorManager_class extends StaticMethodsProcessFactory<FeatureCollectionValidation_calculWithErrorManager_class> {
	
	protected static FeatureCollectionValidation callObject;

	public FeatureCollectionValidation_calculWithErrorManager_class() {
		super(Text.text("beach profile analysis"),"BeachProfile",FeatureCollectionValidation_calculWithErrorManager_class.class);
		callObject = new FeatureCollectionValidation();
	}

	@DescribeProcess(title="FeatureCollectionValidation_calculWithErrorManager",description="Add a description of FeatureCollectionValidation_calculWithErrorManager")
	@DescribeResult(name="result",description="a feature collection containing the results of the treatment or the errors")
	public static FeatureCollection<SimpleFeatureType, SimpleFeature> FeatureCollectionValidation_calculWithErrorManager(@DescribeParameter(name="fc",description=" the feature collection containing geometries we want to interpolate") FeatureCollection<SimpleFeatureType, SimpleFeature> fc,@DescribeParameter(name="interpolationValue",description=" distance between coordinates of the geometry") Double interpolationValue,@DescribeParameter(name="useSmallestDistance",description=" if useSmallestDistance is true, use the smallest distance between all features, else ignore the feature shorter than the first one") Boolean useSmallestDistance,@DescribeParameter(name="minDist",description=" specifie the minimum distance of the interval of calculation") Double minDist,@DescribeParameter(name="maxDist",description=" specifie the maximum distance of the interval of calculation") Double maxDist) {
		FeatureCollection<SimpleFeatureType, SimpleFeature> result;
		result = callObject.calculWithErrorManager( fc, interpolationValue, useSmallestDistance, minDist, maxDist);

		return result;
	}
}
