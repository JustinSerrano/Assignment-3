package model;

/**
 * Represents a Figure toy with a specific classification. Classification types
 * include 'A' for Action, 'D' for Doll, and 'H' for Historic. Extends the Toy
 * superclass by adding a classification field specific to Figure toys.
 * 
 * 
 * @author Justin, Fatema, Manveet
 * @version 3.0
 */
public class Figures extends Toy {

	private char classification; // Classification: 'A' for Action, 'D' for Doll, 'H' for Historic

	/**
	 * Constructs a Figure with the specified attributes.
	 *
	 * @param sn             Unique serial number; must start with 0 or 1 and be 10
	 *                       digits.
	 * @param name           Name of the figure.
	 * @param brand          Brand of the figure.
	 * @param price          Price of the figure; must be non-negative.
	 * @param availableCount Stock count for the figure.
	 * @param ageAppropriate Minimum age appropriate for the figure.
	 * @param classification Classification type ('A' for Action, 'D' for Doll, 'H'
	 *                       for Historic).
	 * @throws IllegalArgumentException if the serial number or classification is
	 *                                  invalid.
	 */
	public Figures(String sn, String name, String brand, double price, int availableCount, int ageAppropriate,
			char classification) {
		super(sn, name, brand, price, availableCount, ageAppropriate);

		// Validate that the serial number starts with 0 or 1 and is 10 digits
		if (!sn.matches("^[01]\\d{9}$")) {
			throw new IllegalArgumentException("Serial number must start with 0 or 1 and be 10 digits long.");
		}

		// Validate that classification is 'A', 'D', or 'H'
		if (classification != 'A' && classification != 'D' && classification != 'H') {
			throw new IllegalArgumentException("Classification must be 'A' (Action), 'D' (Doll), or 'H' (Historic).");
		}

		this.classification = classification;
	}

	/**
	 * Gets the classification type of the figure.
	 * 
	 * @return the classification type of the figure ('A', 'D', or 'H').
	 */
	public char getClassification() {
		return classification;
	}

	/**
	 * Sets the classification type for the figure.
	 *
	 * @param classification the classification to set ('A' for Action, 'D' for
	 *                       Doll, 'H' for Historic).
	 * @throws IllegalArgumentException if classification is not 'A', 'D', or 'H'.
	 */
	public void setClassification(char classification) {
		if (classification != 'A' && classification != 'D' && classification != 'H') {
			throw new IllegalArgumentException("Classification must be 'A' (Action), 'D' (Doll), or 'H' (Historic).");
		}
		this.classification = classification;
	}

	/**
	 * Returns the toy type as "Figure".
	 *
	 * @return a string indicating the toy type as "Figure".
	 */
	@Override
	public String getToyType() {
		return "Figure";
	}

	/**
	 * Returns a string formatted for saving the figure's data to a file. The format
	 * is compatible with the `toys.txt` file and includes all relevant details
	 * about the toy, separated by semicolons.
	 *
	 * @return a string representation of the toy's data for file storage.
	 */
	@Override
	public String toDataString() {
		return String.join(";", getSn(), getName(), getBrand(), String.valueOf(getPrice()),
				String.valueOf(getAvailableCount()), String.valueOf(getAgeAppropriate()),
				String.valueOf(classification));
	}

	/**
	 * Returns a formatted string representing the figure toy's details, including
	 * classification.
	 *
	 * @return A string representation of the figure's details, appended to the base
	 *         Toy details.
	 */
	@Override
	public String toString() {
		return super.toString() + String.format(", Classification: %s", classification);
	}
}
