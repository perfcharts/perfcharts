package perfcharts.chart;

/**
 * Represents a 2-dimensional point of charts
 * 
 * @author Rayson Zhu
 *
 */
public class Point2D {
	/**
	 * x-value
	 */
	private Comparable<?> x;
	/**
	 * y-value
	 */
	private double y;
	/**
	 * weight
	 */
	private int weight = 1;

	/**
	 * Initialize a 2-dimensional point (0,0) with default weight (1).
	 */
	public Point2D() {

	}

	/**
	 * Initialize a 2-dimensional point with specified parameters and default
	 * weight (1).
	 * 
	 * @param x
	 *            x-value
	 * @param y
	 *            y-value
	 */
	public Point2D(Comparable<?> x, double y) {
		this(x, y, 1);
	}

	/**
	 * Initialize a 2-dimensional point with specified parameters
	 * 
	 * @param x
	 *            x-value
	 * @param y
	 *            y-value
	 * @param w
	 *            weight
	 */
	public Point2D(Comparable<?> x, double y, int w) {
		this.x = x;
		this.y = y;
		this.weight = w;
	}

	/**
	 * Get x-value.
	 * 
	 * @return x-value
	 */
	public Comparable<?> getX() {
		return x;
	}

	/**
	 * Set x-value.
	 * 
	 * @param x
	 *            x-value
	 */
	public void setX(Comparable<?> x) {
		this.x = x;
	}

	/**
	 * Get y-value
	 * 
	 * @return y-value
	 */
	public double getY() {
		return y;
	}

	/**
	 * Set y-value
	 * 
	 * @param y
	 *            y-value
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * Get weight.
	 * 
	 * @return weight
	 */
	public int getWeight() {
		return weight;
	}

	/**
	 * Set weight
	 * 
	 * @param w
	 *            weight
	 */
	public void setWeight(int w) {
		this.weight = w;
	}
}
