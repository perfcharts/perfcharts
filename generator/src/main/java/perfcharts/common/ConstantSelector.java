package perfcharts.common;

import java.util.List;

/**
 * A ConstantSelector is a special {@link FieldSelector} that always returns the
 * specified constant value (regardless of the given data row)
 * 
 * @author Rayson Zhu
 * @see FieldSelector
 */
public class ConstantSelector<T> implements FieldSelector<T> {
	/**
	 * the constant
	 * 
	 * @see #select(List)
	 */
	private T constant;

	public ConstantSelector() {

	}

	/**
	 * the constant
	 * 
	 * @param constant
	 *            a constant
	 */
	public ConstantSelector(T constant) {
		this.constant = constant;
	}

	/**
	 * This method just returns the specified constant, regardless of the
	 * value of data row
	 * @param row ignored data row
	 * @see #setConstant(Object)
	 * @see #getConstant()
	 */
	@Override
	public T select(List<?> row) {
		return constant;
	}

	/**
	 * get the constant
	 * 
	 * @return a constant
	 */
	public T getConstant() {
		return constant;
	}

	/**
	 * set the constant
	 * 
	 * @param constant
	 *            a constant
	 */
	public void setConstant(T constant) {
		this.constant = constant;
	}

}
