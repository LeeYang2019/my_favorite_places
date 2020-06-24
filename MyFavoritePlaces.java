import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

public class MyFavoritePlaces {
	public static void main(String [] args) {

		//create scanner
		Scanner input = new Scanner(System.in);

		//local variables
		ListOfPlaces listing = new ListOfPlaces();
		String selection = null;
		
		do {
			displayMenu(listing);
			selection = getUserInput(input, listing);

			if (selection.equalsIgnoreCase("A")) {
				handleAddMethod(input, listing);	
			}
			else if (selection.equalsIgnoreCase("S")) {
				handleShowMethod(input, listing);
			} 
			else if (selection.equalsIgnoreCase("E")) {
				handleEditMethod(input,listing);
			} 
			else if (selection.equalsIgnoreCase("D")) {
				handleDeleteMethod(input, listing);
			} 
			else if (selection.equalsIgnoreCase("C")) {
				handleCurrentMethod(input,listing);
			} 
			else if (selection.equalsIgnoreCase("W")) {
				handleWriteMethod(input, listing);
			} 
			else if (selection.equalsIgnoreCase("R")) {
				handleReadMethod(listing, input);
			}

		} while (!selection.equalsIgnoreCase("Q"));
		System.out.println("Thank you for using My Favorite Places 2016!");
		input.close();
	}

	private static void handleEditMethod(Scanner input, ListOfPlaces listing) {
		System.out.print("Enter number of a place to edit: ");
		int option = getOptionInput(input, listing);

		System.out.println(listing.getList().get(option-1).toString());
	}

	private static void handleWriteMethod(Scanner input, ListOfPlaces listing) {

		if (listing.getCount() == 0) {
			System.out.println("No files written");
		}
		else {
			System.out.print("Enter file name: ");
			String fileName = input.nextLine();
			listing.writeFile(fileName);
		}
	}

	private static void handleDeleteMethod(Scanner input, ListOfPlaces listing) {
		System.out.print("Enter number of a place to delete: ");
		int option = getOptionInput(input, listing);
		listing.remove(option-1);
	}

	private static void handleCurrentMethod(Scanner input, ListOfPlaces listing) {

		System.out.print("Enter number of place to be Current place: ");
		int option = getOptionInput(input, listing)-1;

		Place place1 = listing.getList().get(option);

		Place.setCurrentPlace(place1);
		
		System.out.println(Place.getCurrentPlace().getName() + " set as the current place.");
		System.out.print("Press Enter to continue.");
		input.nextLine();

		listing.retrieve();
	}

	private static void handleReadMethod(ListOfPlaces listing,
			Scanner input) {

		String fileStatus = "Available";
		readFromDir(fileStatus);

		System.out.println();
		System.out.print("Enter filename: ");
		String fileName = input.nextLine() + ".mfp";
		listing.readFile(fileName);
	}

	private static void handleShowMethod(Scanner input, 
			ListOfPlaces listing) {

		System.out.print("Enter number of a place to show: ");
		int option = getOptionInput(input, listing);

		System.out.println(listing.getList().get(option-1).toString());

		try {
			String formattedAddress = listing.getList().get(option-1).getURL();
			openBrowser(formattedAddress);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void handleAddMethod(Scanner input, ListOfPlaces listing) {

		System.out.print("Enter the name: ");
		String name = getUserInput(input, listing).toUpperCase();

		System.out.print("Enter the address: ");
		String address = getUserInput(input, listing);

		try {
			String jsonString = Geocoding.find(address);

			GResponse gResponse = GeocodeResponse.parse(jsonString);
			
			String formattedAddress = gResponse.getFormattedAddress();
			double newLatitude = gResponse.getLatitude();
			double newLongitude = gResponse.getLongitude();

			listing.verifyDuplicate(name, formattedAddress, newLatitude, newLongitude);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	private static int getOptionInput(Scanner input, ListOfPlaces list) {

		int option = 0;

		try {
			option = Integer.parseInt(input.nextLine());

			if (option < 1 || option > list.getCount()) {
				System.out.println("Please enter a number between 1 and " + 
						list.getCount());
			}

		} catch(IndexOutOfBoundsException e) {
			System.out.println("Invalid value: " + option);
		} catch (NumberFormatException e) {
			System.out.println("Invalid entry. Please enter an integer.");
		}
		return option;
	}

	private static void readFromDir(String fileStatus) {

		File folder = new File(System.getProperty("user.dir"));

		System.out.println(fileStatus + " Files: ");
		for (File file: folder.listFiles()) {
			if (file.getName().endsWith(".mfp")) {
				System.out.println("       " + file.getName());
			}
		}
	}

	private static String getUserInput(Scanner input, ListOfPlaces listing) {
		
		String selection = null;
		selection = input.nextLine();
		
		if (listing.getCount() == 0) {
			while (selection.equalsIgnoreCase("S") || 
					selection.equalsIgnoreCase("E") || 
					selection.equalsIgnoreCase("D") || 
					selection.equalsIgnoreCase("C") ||  
					selection.equalsIgnoreCase("W")) {
				System.out.print("Invalid selection. Choose again ");
				selection = input.nextLine();
			}
		}
		
		return selection;
	}

	private static void displayMenu(ListOfPlaces listing) {

		System.out.println();
		System.out.println("My Favorite Places 2016");
		System.out.println("--------------------------");

		if (listing.getCount() == 0) {
			System.out.println("No places loaded.");
			System.out.println("--------------------------");
			System.out.print("A)dd R)ead Q)uit : ");
		} 
		else {
			listing.retrieve();
			System.out.println("----------------------------");
			System.out.print("A)dd S)how E)dit D)elete C)urrent W)rite R)ead Q)uit : ");
		}
	}

	public static void openBrowser(String url) throws IOException, URISyntaxException {
		if( Desktop.isDesktopSupported()) {
			Desktop.getDesktop().browse(new URI( url));
		}		
	}	
}
