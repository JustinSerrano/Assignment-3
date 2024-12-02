package model;

import java.util.Arrays;

import exceptions.PlayerCountException;

/**
 * Represents a Board Game toy with a player range and a list of designers.
 * Extends the Toy superclass and includes additional attributes specific to
 * board games.
 * 
 * @author Justin, Fatema, Manveet
 * @version 3.0
 */
public class BoardGames extends Toy {

	private int minPlayers; // Minimum number of players
	private int maxPlayers; // Maximum number of players
	private String[] designers; // Array of designers for the board game

	/**
	 * Constructs a Board Game with the specified attributes.
	 *
	 * @param sn             Unique serial number; must start with 7, 8, or 9 and be
	 *                       10 digits.
	 * @param name           Name of the board game.
	 * @param brand          Brand of the board game.
	 * @param price          Price of the board game; must be non-negative.
	 * @param availableCount Stock count for the board game.
	 * @param ageAppropriate Minimum age appropriate for the board game.
	 * @param minPlayers     Minimum number of players for the game.
	 * @param maxPlayers     Maximum number of players for the game.
	 * @param designer       A comma-separated string of designer names.
	 */
	public BoardGames(String sn, String name, String brand, double price, int availableCount, int ageAppropriate,
			int minPlayers, int maxPlayers, String designer) {
		super(sn, name, brand, price, availableCount, ageAppropriate);

		this.minPlayers = minPlayers;
		this.maxPlayers = maxPlayers;

		// Split the designers by comma and trim any leading/trailing whitespace from
		// each name
		this.designers = Arrays.stream(designer.split(",")).map(String::trim).toArray(String[]::new);
	}

	/**
	 * Gets the minimum number of players for the board game.
	 * 
	 * @return the minimum number of players as an integer.
	 */
	public int getMinPlayers() {
		return minPlayers;
	}

	/**
	 * Gets the maximum number of players for the board game.
	 * 
	 * @return the maximum number of players as an integer.
	 */
	public int getMaxPlayers() {
		return maxPlayers;
	}

	/**
	 * Gets the list of designers for the board game.
	 * 
	 * @return an array of designer names.
	 */
	public String[] getDesigners() {
		return designers;
	}

	/**
	 * Sets the list of designers for the board game.
	 *
	 * @param designers an array of designer names.
	 */
	public void setDesigners(String[] designers) {
		this.designers = designers;
	}

	/**
	 * Returns the toy type as "BoardGame".
	 *
	 * @return a string indicating the toy type as "BoardGame".
	 */
	@Override
	public String getToyType() {
		return "BoardGame";
	}

	/**
	 * Returns a string formatted for saving the board game toy's data to a file.
	 * The format is compatible with the `toys.txt` file and includes all relevant
	 * details about the toy, separated by semicolons.
	 *
	 * @return a string representation of the toy's data for file storage.
	 */
	@Override
	public String toDataString() {
		return String.join(";", getSn(), getName(), getBrand(), String.valueOf(getPrice()),
				String.valueOf(getAvailableCount()), String.valueOf(getAgeAppropriate()), minPlayers + "-" + maxPlayers,
				String.join(",", designers));
	}

	/**
	 * Returns a formatted string representing the board game’s details, including
	 * minimum and maximum players and designers. This format is designed to be
	 * useful for displaying board game-specific details in addition to the general
	 * toy information.
	 *
	 * @return A string representation of the board game's details in the format:
	 *         "Category: [Toy Type], Serial Number: [SN], Name: [Name], Price:
	 *         [Price], Available Count: [Count], Age Appropriate: [Age], Players:
	 *         [Min-Max], Designers: [Designers]"
	 */
	@Override
	public String toString() {
		return super.toString() + String.format(", Players: %d-%d, Designers: %s", minPlayers, maxPlayers,
				String.join(", ", designers));
	}
}
