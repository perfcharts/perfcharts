package perfcharts.common;

import java.util.List;

/**
 * A BinarySelector combines the results of two other FieldSelectors. It calls
 * specified {@link FieldSelector}s, collects the values, and gives out a result after
 * performing some calculations on them.
 * 
 * @author Rayson Zhu
 *
 */
public abstract class BinarySelector<T, U, V> implements FieldSelector<V> {
	/**
	 * the first FieldSelector
	 */
	private FieldSelector<T> firstOperand;
	/**
	 * the second FieldSelector
	 */
	private FieldSelector<U> secondOperand;

	/**
	 * constructor
	 */
	public BinarySelector() {

	}

	/**
	 * constructor
	 * 
	 * @param firstOperand
	 *            The first FieldSelector
	 * @param secondOperand
	 *            The second FieldSelector
	 */
	public BinarySelector(FieldSelector<T> firstOperand,
			FieldSelector<U> secondOperand) {
		this.firstOperand = firstOperand;
		this.secondOperand = secondOperand;
	}

	/**
	 * Perform some operations on the values extracted by two other {@link FieldSelector}s, and return
	 * the outcome value.
	 * 
	 */
	@Override
	public abstract V select(List<?> row);

	/**
	 * get the first FieldSelector
	 * 
	 * @return the first FieldSelector
	 */
	public FieldSelector<T> getFirstOperand() {
		return firstOperand;
	}

	/**
	 * set the first FieldSelector
	 * 
	 * @param firstOperand
	 *            the first FieldSelector
	 */
	public void setFirstOperand(FieldSelector<T> firstOperand) {
		this.firstOperand = firstOperand;
	}

	/**
	 * get the second FieldSelector
	 * 
	 * @return the second FieldSelector
	 */
	public FieldSelector<U> getSecondOperand() {
		return secondOperand;
	}

	/**
	 * set the second FieldSelector
	 * 
	 * @param secondOperand
	 *            the second FieldSelector
	 */
	public void setSecondOperand(FieldSelector<U> secondOperand) {
		this.secondOperand = secondOperand;
	}
}
