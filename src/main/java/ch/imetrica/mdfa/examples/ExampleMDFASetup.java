package ch.imetrica.mdfa.examples;

import ch.imetrica.mdfa.customization.Customization;
import ch.imetrica.mdfa.customization.SmoothingWeight;
import ch.imetrica.mdfa.datafeeds.CsvFeed;
import ch.imetrica.mdfa.mdfa.MDFABase;
import ch.imetrica.mdfa.mdfa.MDFAFactory;
import ch.imetrica.mdfa.mdfa.MDFASolver;
import ch.imetrica.mdfa.regularization.Regularization;
import ch.imetrica.mdfa.series.MultivariateSeries;
import ch.imetrica.mdfa.series.SignalSeries;
import ch.imetrica.mdfa.series.TargetSeries;
import ch.imetrica.mdfa.series.TimeSeries;
import ch.imetrica.mdfa.targetfilter.TargetFilter;

public class ExampleMDFASetup {

	
	public static void main(String[] args) throws Exception {
		
		
		
		int nobs 				= 300;
		int nseries 			= 2;
		int f_length			= 20;
		int i1					= 0;
		int i2					= 0;
		double lag				= 0.0;		
		double cutoff			= Math.PI/6;	
		double alpha			= 1.0;	
		double lambda			= 1.0;
		double smooth			= 0.01;		
		double decayStrength	= 0.1;
		double decayStart		= 0.1;
		double crossCorr		= 0.9;
		double shift_const		= 1.0;
		
		MDFABase anyMDFA = new MDFABase(nobs, 
		        nseries, 
		        f_length, 
		        i1,
		        i2,
		        lag, 
		        cutoff,
		        alpha,
		        lambda,
		        smooth,
		        decayStrength,
		        decayStart,
		        crossCorr,
		        shift_const);
		
		
		
		
		
		/*Create raw price time series*/
		TimeSeries<Double> appleSeries = CsvFeed.getChunkOfData(0, 500, "data/AAPL.IB.dat", "dateTime", "close");
		TimeSeries<Double> qqqSeries   = CsvFeed.getChunkOfData(0, 500, "data/QQQ.IB.dat", "dateTime", "close");
		
		MDFAFactory anyMDFAFactory = new MDFAFactory(anyMDFA);
		MDFASolver mySolver = new MDFASolver(anyMDFAFactory);
		
		MultivariateSeries multiSeries = new MultivariateSeries(mySolver);
		
		SignalSeries aapl_signal = new SignalSeries(new TargetSeries(appleSeries, 0.4, true), "AAPL");	
		SignalSeries qqq_signal = new SignalSeries(new TargetSeries(qqqSeries, 0.4, true), "QQQ");	
		multiSeries.addSeries(aapl_signal);
		multiSeries.addSeries(qqq_signal);
		
		multiSeries.computeFilterCoefficients();
		multiSeries.printMDFACoeffs();
	
	}
	

	
	
}
