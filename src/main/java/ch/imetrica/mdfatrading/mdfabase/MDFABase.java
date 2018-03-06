package ch.imetrica.mdfatrading.mdfabase;

public class MDFABase {

	
	/**
	 * 
	 * The common shared MDFA object that contains the basic 
	 * Meta information of computing the in-sample MDFA 
	 * filtering process 
	 * 
	 */
	
	int N;       /* insample length of time series */
	int L;       /* length of the filter */
	int nseries; /* number of series used for signal estimation */
	double lag; 
	
	public MDFABase(int N, int nseries, int L, double lag) {
		
		this.N = N;
		this.nseries = nseries;
		this.L = L;
		this.lag = lag; 
		
	}

	public int getFilterLength() {
		return this.L;
	}

	public int getNSeries() {
		return this.nseries;
	}

	public double getLag() {
		return this.lag;
	}

	public int getSeriesLength() {
		return this.N;
	}
	
	
	
}
