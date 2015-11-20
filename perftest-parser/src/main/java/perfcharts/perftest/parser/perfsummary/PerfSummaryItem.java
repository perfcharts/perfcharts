package perfcharts.perftest.parser.perfsummary;

public class PerfSummaryItem {
	private String transaction;
	private long samples;
	private double average;
	private double _90Line;
	private double error;
	private double throughput;

	public PerfSummaryItem() {
		this.transaction = null;
		this.samples = 0;
		this.average = Double.NaN;
		this._90Line = Double.NaN;
		this.error = Double.NaN;
		this.throughput = Double.NaN;
	}

	public PerfSummaryItem(String transaction, long samples, double average,
			double _90Line, double error, double throughput) {
		this.transaction = transaction;
		this.samples = samples;
		this.average = average;
		this._90Line = _90Line;
		this.error = error;
		this.throughput = throughput;
	}



	public String getTransaction() {
		return transaction;
	}

	public void setTransaction(String transaction) {
		this.transaction = transaction;
	}

	public long getSamples() {
		return samples;
	}

	public void setSamples(long samples) {
		this.samples = samples;
	}

	public double getAverage() {
		return average;
	}

	public void setAverage(double average) {
		this.average = average;
	}

	public double get90Line() {
		return _90Line;
	}

	public void set90Line(double _90Line) {
		this._90Line = _90Line;
	}

	public double getError() {
		return error;
	}

	public void setError(double error) {
		this.error = error;
	}

	public double getThroughput() {
		return throughput;
	}

	public void setThroughput(double throughput) {
		this.throughput = throughput;
	}

}
