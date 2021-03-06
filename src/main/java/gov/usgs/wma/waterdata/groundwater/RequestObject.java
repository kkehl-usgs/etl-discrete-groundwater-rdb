package gov.usgs.wma.waterdata.groundwater;

/**
 * AWS lambda injection interface object.
 * This action requires the AQTS location folder to write to RDB file.
 * @author duselman
 */
public class RequestObject {
	String locationFolder;

	public String getLocationFolder() {
		return locationFolder;
	}

	public void setLocationFolder(String locationFolder) {
		this.locationFolder = locationFolder;
	}

	@Override
	public String toString() {
		return "RequestObject{" +
				"locationFolder='" + locationFolder + '\'' +
				'}';
	}
}
