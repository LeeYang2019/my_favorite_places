import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Place implements Comparable<Place>{
	private String name;
	private String address;
	private double latitude;
	private double longitude;

	private static double distance;
	private static Place currentPlace;

	public Place(String name, String address, double latitude, double longitude) {
		this.name = name;
		this.address = address;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public String getName() {
		return this.name;
	}

	public String getAddress() {
		return this.address;
	}

	public double getLatitude() {
		return this.latitude;
	}

	public double getLongitude() {
		return this.longitude;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public static void setCurrentPlace(Place place) {
		Place.currentPlace = place;
	}

	public static Place getCurrentPlace() {
		return Place.currentPlace;
	}

	private static String URL = "https://www.google.com/maps/place/";
	private static String CHARSET = "UTF-8"; 

	public String getURL() throws UnsupportedEncodingException {
		return URL + URLEncoder.encode(address, CHARSET);
	}

	public static double getDistance(double num1, double num2) {
		distance = Geocoding.distance(Place.currentPlace.getLatitude(), 
				Place.currentPlace.getLongitude(), num1, num2);
		return distance;
	}

	public String toString() {
		return this.name + "\n" + 
				this.address + "\n" + 
				this.latitude + ", " + 
				this.longitude;
	}

	@Override
	public int compareTo(Place o) {
		if (Place.currentPlace == null) {
			return this.getName().compareToIgnoreCase(o.getName());
		} else {
			double distance1 = Place.getDistance(this.getLatitude(), this.longitude);
			double distance2 = Place.getDistance(o.getLatitude(), o.getLongitude());

			if (distance1 > distance2) {
				return 1;
			}
			else if (distance1 < distance2) {
				return -1;
			}
			else {
				return 0;
			}
		}
	}


}
