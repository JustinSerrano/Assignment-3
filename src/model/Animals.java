package model;

/**
 * Represents an Animal toy with specific attributes including material and
 * size. Extends the Toy superclass, adding fields unique to animal toys.
 * Material and size indicate the type and scale of the toy, respectively.
 * 
 * @author Justin, Fatema, Manveet 
 * @version 2.0
 */
public class Animals extends Toy {

	private String material; // Material of the toy (e.g., "Plastic", "Fabric")
	private char size; // Size of the toy: 'S' for Small, 'M' for Medium, 'L' for Large

	/**
	 * Constructs an Animal toy with the specified attributes.
	 *
	 * @param sn             Unique serial number; must start with 2 or 3 and be 10
	 *                       digits.
	 * @param name           Name of the animal toy.
	 * @param brand          Brand of the animal toy.
	 * @param price          Price of the animal toy; must be non-negative.
	 * @param availableCount Stock count for the animal toy.
	 * @param ageAppropriate Minimum age appropriate for the animal toy.
	 * @param material       Material of the toy (e.g., "Plastic", "Fabric").
	 * @param size           Size of the toy ('S' for Small, 'M' for Medium, 'L' for
	 *                       Large).
	 * @throws IllegalArgumentException if serial number or size is invalid.
	 */
	public Animals(String sn, String name, String brand, double price, int availableCount, int ageAppropriate,
			String material, char size) {
		super(sn, name, brand, price, availableCount, ageAppropriate);

		// Validate that the serial number starts with 2 or 3 and is 10 digits
		if (!sn.matches("^[23]\\d{9}$")) {
			throw new IllegalArgumentException("Serial number must start with 2 or 3 and be 10 digits long.");
		}

		// Validate that size is 'S', 'M', or 'L'
		if (size != 'S' && size != 'M' && size != 'L') {
			throw new IllegalArgumentException("Size must be 'S' (Small), 'M' (Medium), or 'L' (Large).");
		}

		this.material = material;
		this.size = size;
	}

	/**
	 * Gets the material of the animal toy.
	 * 
	 * @return the material of the toy (e.g., "Plastic", "Fabric").
	 */
	public String getMaterial() {
		return material;
	}

	/**
	 * Sets the material of the animal toy.
	 *
	 * @param material the material to set, describing what the toy is made of
	 *                 (e.g., "Plastic", "Fabric").
	 */
	public void setMaterial(String material) {
		this.material = material;
	}

	/**
	 * Gets the size of the animal toy.
	 * 
	 * @return the size of the toy ('S' for Small, 'M' for Medium, 'L' for Large).
	 */
	public char getSize() {
		return size;
	}

	/**
	 * Sets the size of the animal toy.
	 *
	 * @param size the size to set; must be 'S', 'M', or 'L'.
	 * @throws IllegalArgumentException if size is not 'S', 'M', or 'L'.
	 */
	public void setSize(char size) {
		if (size != 'S' && size != 'M' && size != 'L') {
			throw new IllegalArgumentException("Size must be 'S' (Small), 'M' (Medium), or 'L' (Large).");
		}
		this.size = size;
	}

	/**
	 * Returns the toy type as "Animal".
	 *
	 * @return a string indicating the toy type as "Animal".
	 */
	@Override
	public String getToyType() {
		return "Animal";
	}

	/**
	 * Returns a string formatted for saving the animal toy's data to a file. The
	 * format is compatible with the `toys.txt` file and includes all relevant
	 * details about the toy, separated by semicolons.
	 *
	 * @return a string representation of the toy's data for file storage.
	 */
	@Override
	public String toDataString() {
		return String.join(";", getSn(), getName(), getBrand(), String.valueOf(getPrice()),
				String.valueOf(getAvailableCount()), String.valueOf(getAgeAppropriate()), material,
				String.valueOf(size));
	}

	/**
	 * Returns a formatted string representing the animal toy's details, including
	 * material and size.
	 *
	 * @return A string representation of the animal's details, appended to the base
	 *         Toy details.
	 */
	@Override
	public String toString() {
		return super.toString() + String.format(", Material: %s, Size: %s", material, size);
	}

}
