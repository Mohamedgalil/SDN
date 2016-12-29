package com.sdn.assignments;

public enum Operator {
	LESS_THAN_EQUAL("<="), GREAER_THAN(">");

	private String operation;

	private Operator(String operation) {
		this.operation = operation;
	}

	/**
	 * Create enum from String Operator
	 * 
	 * @param op
	 *            can only be < , > , <= , >=
	 * @return
	 */
	public static Operator fromString(String op) {
		if (op != null) {
			for (Operator b : Operator.values()) {
				if (op.equals(b.getOperation())) {
					return b;
				}
			}
		}
		return null;
	}

	/**
	 * Called in this way: boolean result = Operation.LESS_THAN.calculate(1, 2);
	 * 
	 * @param value
	 * @param referenceValue
	 * @return
	 */
	public boolean check(int value, int referenceValue) {
		switch (this) {
		case LESS_THAN_EQUAL:
			return value <= referenceValue;
		case GREAER_THAN:
			return value > referenceValue;
		default:
			throw new AssertionError("Unknown operation " + this);
		}
	}

	public String getOperation() {
		return operation;
	}



}