package org.opendaylight.controller.temperature.northbound;

public enum RequestType {
	ENERGY(false), POWER(true);
	private boolean type;

	private RequestType(boolean type) {
		this.setType(type);
	}

	/**
	 * default is energy
	 * 
	 * @param type
	 * @return
	 */
	public static RequestType fromBoolean(boolean type) {
		for (RequestType b : RequestType.values())
			if (type == b.isPower())
				return b;
		return ENERGY;
	}

	public static RequestType fromString(String type) {
		switch (type) {
		case "0":
			return ENERGY;
		case "1":
			return POWER;
		default:
			return null;
		}
	}

	public boolean isPower() {
		return type;
	}

	public void setType(boolean type) {
		this.type = type;
	}
}
