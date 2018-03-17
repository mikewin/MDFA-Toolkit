package ch.imetrica.mdfa.mdfa;

import static org.junit.Assert.*;

import java.io.IOException;

import org.joda.time.format.DateTimeFormat;
import org.junit.Test;

import ch.imetrica.mdfa.datafeeds.CsvFeed;
import ch.imetrica.mdfa.series.MultivariateSeries;
import ch.imetrica.mdfa.series.SignalSeries;
import ch.imetrica.mdfa.series.TargetSeries;
import ch.imetrica.mdfa.series.TimeSeriesEntry;

public class TestMdfaFactory {

	@Test
	public void test() throws Exception {
		
		int nobs 				= 300;
		int nseries 			= 3;
		int f_length			= 50;
		int i1					= 1;
		int i2					= 0;
		double lag				= -2.0;		
		double cutoff			= .33;	
		double alpha			= 10.0;	
		double lambda			= 1.0;
		double smooth			= 0.10;		
		double decayStrength	= 0.01;
		double decayStart		= 0.01;
		double crossCorr		= 0.1;
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
		
		MDFAFactory testMdfa = new MDFAFactory(anyMDFA);
		MDFASolver mySolver = new MDFASolver(testMdfa);
		
		
		String[] dataFiles = new String[3];
		dataFiles[0] = "data/AAPL.IB.dat";
		dataFiles[1] = "data/QQQ.IB.dat";
		dataFiles[2] = "data/SPY.IB.dat";
		
		CsvFeed marketFeed = new CsvFeed(dataFiles, "dateTime", "close");
		
		/* Create empty target series */
		SignalSeries aaplSignal = new SignalSeries(new TargetSeries(0.9, true, "AAPL"), "yyyy-MM-dd");	
		SignalSeries qqqSignal = new SignalSeries(new TargetSeries(0.9, true, "QQQ"), "yyyy-MM-dd");
		SignalSeries spySignal = new SignalSeries(new TargetSeries(0.9, true, "SPY"), "yyyy-MM-dd");
				
		MultivariateSeries multiSeries = new MultivariateSeries(anyMDFA, mySolver);
		multiSeries.setDateFormat(DateTimeFormat.forPattern("yyyy-MM-dd"));

		multiSeries.addSeries(aaplSignal);
		multiSeries.addSeries(qqqSignal);
		multiSeries.addSeries(spySignal);

		for(int i = 0; i < 400; i++) {
			
			TimeSeriesEntry<double[]> observation = marketFeed.getNextMultivariateObservation();
			multiSeries.addValue(observation.getValue(), observation.getDateTime());
			
		}
		
		multiSeries.computeFilterCoefficients();
		multiSeries.getMDFAFactory().setFilterLength(1);
		multiSeries.computeFilterCoefficients();
		assertEquals(2, multiSeries.getMDFAFactory().getFilterLength());
	
	}

}
