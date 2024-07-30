package io.azraein.paperfx.system.locations;

public enum Direction {

	NORTH, SOUTH, EAST, WEST;

	public Direction opposite() {
		switch (this) {
			case EAST:
				return WEST;
			case NORTH:
			default:
				return SOUTH;
			case SOUTH:
				return NORTH;
			case WEST:
				return EAST;

		}
	}
	
}
