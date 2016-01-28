package perfcharts.common;

import java.util.List;

/**
 * An AddTransformSelector is {@link BinarySelector} that adds the values
 * extracted by the other two {@link FieldSelector}s and regards the sum as its
 * extracted value.
 * 
 * @author Rayson Zhu
 *
 */
public class AddTransformSelector<T extends Number, U extends Number, V extends Number>
		extends BinarySelector<T, U, V> {

	public AddTransformSelector() {

	}

	public AddTransformSelector(FieldSelector<T> firstOperand,
			FieldSelector<U> secondOperand) {
		super(firstOperand, secondOperand);
	}

	/**
	 * Add the values extracted by two other {@link FieldSelector}s, and return
	 * the sum.
	 * 
	 * @see #setFirstOperand(FieldSelector)
	 * @see #setSecondOperand(FieldSelector)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public V select(List<?> row) {
		Number a = getFirstOperand().select(row);
		Number b = getSecondOperand().select(row);
		Number r;
		if (a instanceof Double || b instanceof Double)
			r = a.doubleValue() + b.doubleValue();
		else if (a instanceof Float || b instanceof Float)
			r = a.floatValue() + b.floatValue();
		else if (a instanceof Long || b instanceof Long)
			r = a.longValue() + b.longValue();
		else if (a instanceof Integer || b instanceof Integer)
			r = a.intValue() + b.intValue();
		else
			throw new RuntimeException("Cannot add " + a + " and " + b + ".");
		return (V) r;
	}

}
