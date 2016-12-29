package org.opendaylight.controller.temperature.northbound;

/**
 * Packet built using builder for cleanliness. Check the main method in this
 * class for an example
 * 
 */
public class DEBSPacket {
	private short measurement;
	private RequestType type;

	public static class Builder {
		short measurement;
		RequestType type;

		public Builder measurement(short measurement) {
			this.measurement = measurement;
			return this;
		}

		public Builder type(RequestType type) {
			this.type = type;
			return this;
		}

		public DEBSPacket build() {
			return new DEBSPacket(this);
		}
	}

	private DEBSPacket(Builder b) {
		this.measurement = b.measurement;
		this.type = b.type;
	}

	public short getMeasurement() {
		return measurement;
	}

	public void setMeasurement(short measurement) {
		this.measurement = measurement;
	}

	public RequestType getType() {
		return type;
	}

	public void setType(RequestType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Measurement: " + measurement + ", type: " + type.name();
	}

	public static void main(String[] args) {
		/** Example **/
		DEBSPacket p = new DEBSPacket.Builder().measurement((short) 21)
				.type(RequestType.ENERGY).build();
		System.out.println(p);

	}
}
