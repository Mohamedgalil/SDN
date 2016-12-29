package org.opendaylight.controller.temperature.northbound;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.opendaylight.controller.configuration.ConfigurationObject;

/**
 * The Interface provides methods to manipulate user configured string. - type:
 * POWER(1) - ENERGY(0) - value: 500 - Operator: “<" or “>=" - UDP-Port: “50001"
 * - filter: 1 or 0
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class TemperatureUserStringConfig extends ConfigurationObject implements Serializable {
	private static final long serialVersionUID = 1L;

	public enum STATUS {
		SUCCESS("Success"), STRINGDOWN("String Down"), INCORRECT("Incorrect Connection");
		private STATUS(String name) {
			this.name = name;
		}

		private final String name;

		@Override
		public String toString() {
			return name;
		}

		public static STATUS fromString(String str) {
			if (str == null) {
				return STRINGDOWN;
			}
			if (str.equals(SUCCESS.toString())) {
				return SUCCESS;
			}
			if (str.equals(STRINGDOWN.toString())) {
				return STRINGDOWN;
			}
			if (str.equals(INCORRECT.toString())) {
				return INCORRECT;
			}
			return STRINGDOWN;
		}
	}

	@XmlElement
	private String status;
	/**
	 * Username
	 */
	@XmlElement
	private String name;
	@XmlElement // int
	private int portNum;
	@XmlElement // RequestType
	private RequestType type;
	@XmlElement // boolean
	private boolean filter;
	@XmlElement // Operator
	private Operator operator;
	@XmlElement
	private int referenceValue;

	public TemperatureUserStringConfig() {
		super();
		status = STATUS.STRINGDOWN.toString();
	}

	public TemperatureUserStringConfig(String name, String portNum, String type, String filter, String operator,
			String refValue) {
		super();
		this.name = name;
		this.setPortNum(Integer.parseInt(portNum));
		this.setType(RequestType.fromString(type));
		this.setFilter((Integer.parseInt(filter) == 1));
		this.setOperator(Operator.fromString(operator));
		this.setReferenceValue(Integer.parseInt(refValue));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public STATUS getStatus() {
		return STATUS.fromString(status);
	}

	public void setStatus(STATUS s) {
		this.status = s.toString();
	}

	public boolean isValidParameter(String parameter) {
		return (parameter != null);
	}

	public int getPortNum() {
		return portNum;
	}

	public void setPortNum(int portNum) {
		this.portNum = portNum;
	}

	public RequestType getType() {
		return type;
	}

	public void setType(RequestType type) {
		this.type = type;
	}

	public boolean isFilter() {
		return filter;
	}

	public void setFilter(boolean filter) {
		this.filter = filter;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public int getReferenceValue() {
		return referenceValue;
	}

	public void setReferenceValue(int referenceValue) {
		this.referenceValue = referenceValue;
	}
}