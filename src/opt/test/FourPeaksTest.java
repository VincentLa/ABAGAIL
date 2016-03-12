package opt.test;

import java.util.Arrays;

import dist.DiscreteDependencyTree;
import dist.DiscreteUniformDistribution;
import dist.Distribution;

import opt.DiscreteChangeOneNeighbor;
import opt.EvaluationFunction;
import opt.GenericHillClimbingProblem;
import opt.HillClimbingProblem;
import opt.NeighborFunction;
import opt.RandomizedHillClimbing;
import opt.SimulatedAnnealing;
import opt.example.*;
import opt.ga.CrossoverFunction;
import opt.ga.DiscreteChangeOneMutation;
import opt.ga.SingleCrossOver;
import opt.ga.GenericGeneticAlgorithmProblem;
import opt.ga.GeneticAlgorithmProblem;
import opt.ga.MutationFunction;
import opt.ga.StandardGeneticAlgorithm;
import opt.prob.GenericProbabilisticOptimizationProblem;
import opt.prob.MIMIC;
import opt.prob.ProbabilisticOptimizationProblem;
import shared.FixedIterationTrainer;

import java.util.*;
import java.io.*;
import java.text.*;


/**
 * Copied from ContinuousPeaksTest
 * @version 1.0
 */
public class FourPeaksTest {
    /** The n value */
    private static final int N = 20;
    /** The t value */
    private static final int T = N / 5;
    
    public static void main(String[] args) {
        List<Double> rhctimeList = new ArrayList<Double>();
        List<Double> satimeList = new ArrayList<Double>();
        List<Double> gatimeList = new ArrayList<Double>();
        List<Double> mimictimeList = new ArrayList<Double>();
        List<Double> rhcList = new ArrayList<Double>();
        List<Double> saList = new ArrayList<Double>();
        List<Double> gaList = new ArrayList<Double>();
        List<Double> mimicList = new ArrayList<Double>();

        for(int i = 0; i < 10; i++) {        
            int[] ranges = new int[N];
            Arrays.fill(ranges, 2);
            EvaluationFunction ef = new FourPeaksEvaluationFunction(T);
            Distribution odd = new DiscreteUniformDistribution(ranges);
            NeighborFunction nf = new DiscreteChangeOneNeighbor(ranges);
            MutationFunction mf = new DiscreteChangeOneMutation(ranges);
            CrossoverFunction cf = new SingleCrossOver();
            Distribution df = new DiscreteDependencyTree(.1, ranges); 
            HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
            GeneticAlgorithmProblem gap = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf);
            ProbabilisticOptimizationProblem pop = new GenericProbabilisticOptimizationProblem(ef, odd, df);
            
            RandomizedHillClimbing rhc = new RandomizedHillClimbing(hcp);      
            FixedIterationTrainer fit = new FixedIterationTrainer(rhc, 200000);
            double start = System.nanoTime(), end, trainingTime;
            fit.train();
            end = System.nanoTime();
            trainingTime = end - start;
            trainingTime /= Math.pow(10,9);
            rhctimeList.add(trainingTime);
            rhcList.add(ef.value(rhc.getOptimal()));
            
            /*System.out.println("RHC: " + ef.value(rhc.getOptimal()));*/
            
            SimulatedAnnealing sa = new SimulatedAnnealing(1E11, .95, hcp);
            fit = new FixedIterationTrainer(sa, 200000);
            start = System.nanoTime();
            fit.train();
            end = System.nanoTime();
            trainingTime = end - start;
            trainingTime /= Math.pow(10,9);
            satimeList.add(trainingTime);
            saList.add(ef.value(sa.getOptimal()));

            /*System.out.println("SA: " + ef.value(sa.getOptimal()));*/
            
            StandardGeneticAlgorithm ga = new StandardGeneticAlgorithm(200, 100, 10, gap);
            fit = new FixedIterationTrainer(ga, 1000);
            start = System.nanoTime();
            fit.train();
            end = System.nanoTime();
            trainingTime = end - start;
            trainingTime /= Math.pow(10,9);
            gatimeList.add(trainingTime);
            gaList.add(ef.value(ga.getOptimal()));
            /*System.out.println("GA: " + ef.value(ga.getOptimal()));*/
            
            MIMIC mimic = new MIMIC(200, 20, pop);
            fit = new FixedIterationTrainer(mimic, 1000);
            start = System.nanoTime();           
            fit.train();
            end = System.nanoTime();
            trainingTime = end - start;
            trainingTime /= Math.pow(10,9);
            mimictimeList.add(trainingTime);
            mimicList.add(ef.value(mimic.getOptimal()));
            /*System.out.println("MIMIC: " + ef.value(mimic.getOptimal()));*/
        }

        /*Average Value RHC*/
        Double sum = 0d;
        Double avg_value =0d;
        for (Double vals : rhcList) {
            sum += vals;
        }
         avg_value =  sum / rhcList.size();
         System.out.println("RHC");
         System.out.println(avg_value);

        /*Average Value SA*/
        sum = 0d;
        avg_value =0d;
        for (Double vals : saList) {
            sum += vals;
        }
         avg_value =  sum / saList.size();
         System.out.println("SA");
         System.out.println(avg_value);

        /*Average Value GA*/
        sum = 0d;
        avg_value =0d;
        for (Double vals : gaList) {
            sum += vals;
        }
         avg_value =  sum / gaList.size();
         System.out.println("GA");
         System.out.println(avg_value);

        /*Average Value MIMIC*/
        sum = 0d;
        avg_value =0d;
        for (Double vals : mimicList) {
            sum += vals;
        }
         avg_value =  sum / mimicList.size();
         System.out.println("MIMIC");
         System.out.println(avg_value);

/*
*
*Training time comparison
*
*/

        /*Average Value RHC*/
        sum = 0d;
        avg_value =0d;
        for (Double vals : rhctimeList) {
            sum += vals;
        }
         avg_value =  sum / rhctimeList.size();
         System.out.println("RHC");
         System.out.println(avg_value);

        /*Average Value SA*/
        sum = 0d;
        avg_value =0d;
        for (Double vals : satimeList) {
            sum += vals;
        }
         avg_value =  sum / satimeList.size();
         System.out.println("SA");
         System.out.println(avg_value);

        /*Average Value GA*/
        sum = 0d;
        avg_value =0d;
        for (Double vals : gatimeList) {
            sum += vals;
        }
         avg_value =  sum / gatimeList.size();
         System.out.println("GA");
         System.out.println(avg_value);

        /*Average Value MIMIC*/
        sum = 0d;
        avg_value =0d;
        for (Double vals : mimictimeList) {
            sum += vals;
        }
         avg_value =  sum / mimictimeList.size();
         System.out.println("MIMIC");
         System.out.println(avg_value);
    }
}
