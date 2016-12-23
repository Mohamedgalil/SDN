package org.opendaylight.controller.temperature.northbound;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.opendaylight.controller.configuration.ConfigurationObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Interface provides methods to manipulate user configured string.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class TemperatureUserStringConfig extends ConfigurationObject implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(TemperatureUserStringConfig.class);

    public enum STATUS {
        SUCCESS("Success"), STRINGDOWN("String Down"), INCORRECT(
                "Incorrect Connection");
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
    @XmlElement
    private String name;
    @XmlElement
    private String parameterOne;
    @XmlElement
    private String parameterTwo;

    public TemperatureUserStringConfig() {
        super();
        status = STATUS.STRINGDOWN.toString();
    }

    public TemperatureUserStringConfig(String name, String parameterOne, String parameterTwo) {
        super();
        this.name = name;
        this.parameterOne = parameterOne;
        this.parameterTwo = parameterTwo;
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

    public String getParameterOne() {
        return parameterOne;
    }

    public void setParameterOne(String parameterOne) {
        this.parameterOne = parameterOne;
    }

    public String getparameterTwo() {
        return parameterTwo;
    }

    public void setParameterTwo(String parameterTwo) {
        this.parameterTwo = parameterTwo;
    }

    public boolean isValidParameter(String parameter) {
        return (parameter != null);
    }

    public boolean isValid() {
        if (!isValidResourceName(name)) {
            logger.debug("Invalid name in user string: {}", name);
            return false;
        }

        if (!isValidParameter(parameterOne) ||
                !isValidParameter(parameterTwo)) {
            logger.debug("Invalid Parameter in user string: {}", name);
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime
                * result
                + ((parameterTwo == null) ? 0 : parameterTwo.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime
                * result
                + ((parameterOne == null) ? 0 : parameterOne.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        TemperatureUserStringConfig other = (TemperatureUserStringConfig) obj;
        if (parameterTwo == null) {
            if (other.parameterTwo != null) {
                return false;
            }
        } else if (!parameterTwo.equals(other.parameterTwo)) {
            return false;
        }
        if (parameterOne == null) {
            if (other.parameterOne != null) {
                return false;
            }
        } else if (!parameterOne.equals(other.parameterOne)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TemperatureUserStringConfig [status=" + status + ", name=" + name
                + ", parameterOne=" + parameterOne
                + ", parameterTwo=" + parameterTwo + "]";
    }
}