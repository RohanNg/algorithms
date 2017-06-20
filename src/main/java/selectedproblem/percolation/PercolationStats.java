package selectedproblem.percolation;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;

public class PercolationStats {
	private final double mean;
	private final double stddev;
	private final double confidenceLo;
	private final double confidenceHi;
	public PercolationStats(int n, int trials){
		if(n<=0 || trials <=0){
			throw new IllegalArgumentException("Invalid input for the constructor");
		}
		double[] trialResult = new double[trials];
		for(int x = 0 ; x < trials; x++){
			int count = 0;
			PercolationRecursive sys = new PercolationRecursive(n);
			while(!sys.percolates()){
				int row = StdRandom.uniform(n) + 1;
				int col = StdRandom.uniform(n) + 1; // return random integer uniformly in [1.n]
				while(sys.isOpen(row, col)){ // looking for blocked site
					col = StdRandom.uniform(n) + 1;
					row = StdRandom.uniform(n) + 1;
				}
				sys.open(row, col);
				count++;
			}
			trialResult[x] = (count*1.0/(n*n)); 
		}
		stddev = StdStats.stddev(trialResult);
		mean = StdStats.mean(trialResult);
		confidenceLo = mean - 1.96*stddev/Math.sqrt(trials);
		confidenceHi = mean + 1.96*stddev/Math.sqrt(trials);
	}
	
	public double mean(){
		return mean;
	}
	public double stddev(){
		return stddev;
	}
	public double confidenceLo(){
		return confidenceLo;
	}
	public double confidenceHi(){
		return confidenceHi;
	}
	
	public static void main(String[] args){
		System.out.println("Recursive solution:");
		Stopwatch watch = new Stopwatch();
		PercolationStats percolationStats = new PercolationStats(500, 2000);
		System.out.println("elapsed time: " + watch.elapsedTime());
		System.out.println("mean                     = " + percolationStats.mean());
		System.out.println("stddev                   = " + percolationStats.stddev());
		System.out.println("95% confidence interval  = " + percolationStats.confidenceLo() + ", " + percolationStats.confidenceHi);
		
	}
}
