import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;
import java.util.List;

public class ListOfPlaces {
	private ArrayList<Place> list;
	private int count;
	private boolean isSet;

	public ListOfPlaces() {
		this.list = new ArrayList<Place>();
	}

	public void add(Place places) {
		this.list.add(places);
		this.count++;
	}

	public void remove(int number) {

		if (this.list.size() < 1) {
			System.out.println("The list is empty.");
		}
		else {
			this.list.remove(list.get(number));
		}
	}

	public void retrieve() {
		
		sortPlaces();
		
		if (Place.getCurrentPlace() != null) {
			this.isSet = true;

			if (this.isSet == true) {

				System.out.println("\nDistance from " + Place.getCurrentPlace().getName() + "\n");
				for (int j = 0; j < list.size(); j++) {
					System.out.print(j + 1 + ") " + list.get(j).getName() + " ("); 
					System.out.printf("%1.2f", Place.getDistance(list.get(j).getLatitude(), 
							list.get(j).getLongitude()));
					System.out.println(" miles)");
					
				}
			}
		}
		else {
			for (int i = 0; i < list.size(); i++) {
				System.out.println(i + 1 + ") " + list.get(i).getName());
			}
		}
	}

	public int getCount() {
		return this.count;
	}

	public ArrayList<Place> getList() {
		return this.list;
	}


	public boolean isOnList(String name) {

		//checks to see if the place matches the name of one already on list
		for (int i = 0; i < list.size(); i++) {
			if (name.equals(list.get(i).getName())) {
				return true;
			}
		}
		return false;
	}

	public void verifyDuplicate(String name, String address, double latitude,
			double longitude) {

		if (this.count > 0) {
			if (isOnList(name)) {
				System.out.println("Place " + name + " already on list");
			}
			else {
				add(new Place(name, address, latitude, longitude));
			}
		}
		else {
			add(new Place(name, address, latitude, longitude));
		}
	}

	public void sortPlaces() {
		Collections.sort(this.list);
	}

	public void readFile(String fileName) {

		//local variables
		String line = null;
		String[] tokens = null;
		String place = null;
		String address = null;
		double latitude = 0;
		double longitude = 0;

		File newFile = new File(fileName);

		try {
			Scanner fileReader = new Scanner(newFile);

			while (fileReader.hasNextLine()) {
				line = fileReader.nextLine();
				tokens = line.split(";");
				place = tokens[0];
				address = tokens[1];
				latitude = Double.parseDouble(tokens[2]);
				longitude = Double.parseDouble(tokens[3]);
				verifyDuplicate(place, address, latitude, longitude);
				this.count++;
			}
			fileReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("Unable to read from file: " + fileName);
		}
	}

	public void writeFile(String fileName) {

		//local variables
		int writtenFiles = 0;

		try {
			File file = new File(fileName);

			if (file.exists()) {
				System.out.println("File already exists");
			}
			else {
				PrintWriter output = new PrintWriter(file);

				for (int i = 0; i <list.size(); i++) {
					output.println(list.get(i).getName() + ";"
							+ list.get(i).getAddress() + ";" 
							+ list.get(i).getLatitude() + ";"
							+ list.get(i).getLongitude());
					writtenFiles++;
				}

				if (writtenFiles == 1) {
					System.out.println("Saved " + writtenFiles + 
							" places to file: " + fileName);
				}
				else {
					System.out.println("Saved " + writtenFiles + 
							" places to file: " + fileName);
				}
				output.close();
			}

		} catch(IOException e) {
			System.out.println("Unable to write to file: " + fileName);
		}
	}
}
