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
import opt.ga.GenericGeneticAlgorithmProblem;
import opt.ga.GeneticAlgorithmProblem;
import opt.ga.MutationFunction;
import opt.ga.StandardGeneticAlgorithm;
import opt.ga.UniformCrossOver;
import opt.prob.GenericProbabilisticOptimizationProblem;
import opt.prob.MIMIC;
import opt.prob.ProbabilisticOptimizationProblem;
import shared.FixedIterationTrainer;

import java.util.*;
import java.io.*;
import java.text.*;

/**
 * 
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class CountOnesTest {
    /** The n value */
    private static final int N = 80;
    
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
            EvaluationFunction ef = new CountOnesEvaluationFunction();
            Distribution odd = new DiscreteUniformDistribution(ranges);
            NeighborFunction nf = new DiscreteChangeOneNeighbor(ranges);
            MutationFunction mf = new DiscreteChangeOneMutation(ranges);
            CrossoverFunction cf = new UniformCrossOver();
            Distribution df = new DiscreteDependencyTree(.1, ranges); 
            HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
            GeneticAlgorithmProblem gap = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf);
            ProbabilisticOptimizationProblem pop = new GenericProbabilisticOptimizationProblem(ef, odd, df);
            
            RandomizedHillClimbing rhc = new RandomizedHillClimbing(hcp);      
            FixedIterationTrainer fit = new FixedIterationTrainer(rhc, 200);
            double start = System.nanoTime(), end, trainingTime;
            fit.train();
            end = System.nanoTime();
            trainingTime = end - start;
            trainingTime /= Math.pow(10,9);
            rhctimeList.add(trainingTime);
            rhcList.add(ef.value(rhc.getOptimal()));
            /*System.out.println(ef.value(rhc.getOptimal()));*/
            
            SimulatedAnnealing sa = new SimulatedAnnealing(100, .95, hcp);
            fit = new FixedIterationTrainer(sa, 200);
            start = System.nanoTime();
            fit.train();
            end = System.nanoTime();
            trainingTime = end - start;
            trainingTime /= Math.pow(10,9);
            satimeList.add(trainingTime);
            saList.add(ef.value(sa.getOptimal()));
            /*System.out.println(ef.value(sa.getOptimal()));*/
            
            StandardGeneticAlgorithm ga = new StandardGeneticAlgorithm(20, 20, 0, gap);
            fit = new FixedIterationTrainer(ga, 300);
            start = System.nanoTime();
            fit.train();
            end = System.nanoTime();
            trainingTime = end - start;
            trainingTime /= Math.pow(10,9);
            gatimeList.add(trainingTime);
            /*System.out.println(ef.value(ga.getOptimal()));*/
            gaList.add(ef.value(ga.getOptimal()));
            
            MIMIC mimic = new MIMIC(50, 10, pop);
            fit = new FixedIterationTrainer(mimic, 100);
            start = System.nanoTime();
            fit.train();
            end = System.nanoTime();
            trainingTime = end - start;
            trainingTime /= Math.pow(10,9);
            mimictimeList.add(trainingTime);
            /*System.out.println(ef.value(mimic.getOptimal()));*/
            mimicList.add(ef.value(mimic.getOptimal()));
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