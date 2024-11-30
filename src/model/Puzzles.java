package model;

/**
 * Represents a Puzzle toy with a specific type, such as Mechanical, Cryptic,
 * Logic, Trivia, or Riddle. Extends the Toy superclass and adds the
 * `puzzleType` attribute specific to puzzles.
 * 
 * @author Justin, Fatema, Manveet
 * @version 2.0
 */
public class Puzzles extends Toy {

	/**
	 * Type of puzzle: 'M' for Mechanical, 'C' for Cryptic, 'L' for Logic, 'T' for
	 * Trivia, 'R' for Riddle.
	 */
	private char puzzleType;

	/**
	 * Constructs a Puzzle toy with the specified attributes.
	 *
	 * @param sn             Unique serial number; must start with 4, 5, or 6 and be
	 *                       10 digits.
	 * @param name           Name of the puzzle.
	 * @param brand          Brand of the puzzle.
	 * @param price          Price of the puzzle; must be non-negative.
	 * @param availableCount Stock count for the puzzle.
	 * @param ageAppropriate Minimum age appropriate for the puzzle.
	 * @param puzzleType     Type of puzzle ('M' for Mechanical, 'C' for Cryptic,
	 *                       'L' for Logic, 'T' for Trivia, 'R' for Riddle).
	 * @throws IllegalArgumentException if the serial number or puzzle type is
	 *                                  invalid.
	 */
	public Puzzles(String sn, String name, String brand, double price, int availableCount, int ageAppropriate,
			char puzzleType) {
		super(sn, name, brand, price, availableCount, ageAppropriate);

		// Validate that the serial number starts with 4, 5, or 6 and is 10 digits
		if (!sn.matches("^[456]\\d{9}$")) {
			throw new IllegalArgumentException("Serial number must start with 4, 5, or 6 and be 10 digits long.");
		}

		// Validate that puzzle type is 'M', 'C', 'L', 'T', or 'R'
		if (puzzleType != 'M' && puzzleType != 'C' && puzzleType != 'L' && puzzleType != 'T' && puzzleType != 'R') {
			throw new IllegalArgumentException(
					"Puzzle type must be 'M' (Mechanical), 'C' (Cryptic), 'L' (Logic), 'T' (Trivia), or 'R' (Riddle).");
		}

		this.puzzleType = puzzleType;
	}

	/**
	 * Gets the puzzle type of the puzzle toy.
	 * 
	 * @return the puzzle type as a character ('M', 'C', 'L', 'T', or 'R').
	 */
	public char getPuzzleType() {
		return puzzleType;
	}

	/**
	 * Sets the puzzle type of the puzzle toy.
	 *
	 * @param puzzleType the puzzle type to set; must be 'M', 'C', 'L', 'T', or 'R'.
	 * @throws IllegalArgumentException if the puzzle type is not 'M', 'C', 'L',
	 *                                  'T', or 'R'.
	 */
	public void setPuzzleType(char puzzleType) {
		if (puzzleType != 'M' && puzzleType != 'C' && puzzleType != 'L' && puzzleType != 'T' && puzzleType != 'R') {
			throw new IllegalArgumentException(
					"Puzzle type must be 'M' (Mechanical), 'C' (Cryptic), 'L' (Logic), 'T' (Trivia), or 'R' (Riddle).");
		}
		this.puzzleType = puzzleType;
	}

	/**
	 * Returns the toy type as "Puzzle".
	 *
	 * @return a string indicating the toy type as "Puzzle".
	 */
	@Override
	public String getToyType() {
		return "Puzzle";
	}

	/**
	 * Returns a string formatted for saving the puzzle toy's data to a file. The
	 * format is compatible with the `toys.txt` file and includes all relevant
	 * details about the toy, separated by semicolons.
	 *
	 * @return a string representation of the toy's data for file storage.
	 */
	@Override
	public String toDataString() {
		return String.join(";", getSn(), getName(), getBrand(), String.valueOf(getPrice()),
				String.valueOf(getAvailableCount()), String.valueOf(getAgeAppropriate()), String.valueOf(puzzleType));
	}

	/**
	 * Returns a formatted string representing the puzzle toy’s details, including
	 * puzzle type. This format is useful for displaying puzzle-specific details
	 * alongside general toy information.
	 *
	 * @return A string representation of the puzzle's details in the format:
	 *         "Category: [Toy Type], Serial Number: [SN], Name: [Name], Price:
	 *         [Price], Available Count: [Count], Age Appropriate: [Age], Puzzle
	 *         Type: [Type]"
	 */
	@Override
	public String toString() {
		return super.toString() + String.format(", Puzzle Type: %s", puzzleType);
	}

}
