package opt.test;

import java.util.Arrays;
import java.util.Random;

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
 * A test of the knap sack problem
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class KnapsackTest {
    /** Random number generator */
    private static final Random random = new Random();
    /** The number of items */
    private static final int NUM_ITEMS = 100;
    /** The number of copies each */
    private static final int COPIES_EACH = 4;
    /** The maximum weight for a single element */
    private static final double MAX_WEIGHT = 50;
    /** The maximum volume for a single element */
    private static final double MAX_VOLUME = 50;
    /** The volume of the knapsack */
    private static final double KNAPSACK_VOLUME =
         MAX_VOLUME * NUM_ITEMS * COPIES_EACH * .4;
    /**
     * The test main
     * @param args ignored
     */
    public static void main(String[] args) {
        int[] copies = new int[NUM_ITEMS];
        Arrays.fill(copies, COPIES_EACH);
        double[] weights = new double[NUM_ITEMS];
        double[] volumes = new double[NUM_ITEMS];

        List<Double> rhctimeList = new ArrayList<Double>();
        List<Double> satimeList = new ArrayList<Double>();
        List<Double> gatimeList = new ArrayList<Double>();
        List<Double> mimictimeList = new ArrayList<Double>();
        List<Double> rhcList = new ArrayList<Double>();
        List<Double> saList = new ArrayList<Double>();
        List<Double> gaList = new ArrayList<Double>();
        List<Double> mimicList = new ArrayList<Double>();

        for(int j = 0; j < 10; j++) {
            for (int i = 0; i < NUM_ITEMS; i++) {
                weights[i] = random.nextDouble() * MAX_WEIGHT;
                volumes[i] = random.nextDouble() * MAX_VOLUME;
            }
             int[] ranges = new int[NUM_ITEMS];
            Arrays.fill(ranges, COPIES_EACH + 1);
            EvaluationFunction ef = new KnapsackEvaluationFunction(weights, volumes, KNAPSACK_VOLUME, copies);
            Distribution odd = new DiscreteUniformDistribution(ranges);
            NeighborFunction nf = new DiscreteChangeOneNeighbor(ranges);
            MutationFunction mf = new DiscreteChangeOneMutation(ranges);
            CrossoverFunction cf = new UniformCrossOver();
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
            /*System.out.println(ef.value(rhc.getOptimal()));*/

            SimulatedAnnealing sa = new SimulatedAnnealing(100, .95, hcp);
            fit = new FixedIterationTrainer(sa, 200000);
            start = System.nanoTime();
            fit.train();
            end = System.nanoTime();
            trainingTime = end - start;
            trainingTime /= Math.pow(10,9);
            satimeList.add(trainingTime);
            saList.add(ef.value(sa.getOptimal()));
            /*System.out.println(ef.value(sa.getOptimal()));*/

            StandardGeneticAlgorithm ga = new StandardGeneticAlgorithm(200, 150, 25, gap);
            fit = new FixedIterationTrainer(ga, 1000);
            start = System.nanoTime();
            fit.train();
            end = System.nanoTime();
            trainingTime = end - start;
            trainingTime /= Math.pow(10,9);
            gatimeList.add(trainingTime);
            gaList.add(ef.value(ga.getOptimal()));
            /*System.out.println(ef.value(ga.getOptimal()));*/

            MIMIC mimic = new MIMIC(200, 100, pop);
            fit = new FixedIterationTrainer(mimic, 1000);
            start = System.nanoTime();
            fit.train();
            end = System.nanoTime();
            trainingTime = end - start;
            trainingTime /= Math.pow(10,9);
            mimictimeList.add(trainingTime);
            mimicList.add(ef.value(mimic.getOptimal()));
            /*System.out.println(ef.value(mimic.getOptimal()));*/
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
         System.out.println("RHC Max Value");
         System.out.println(Collections.max(rhcList));

        /*Average Value SA*/
        sum = 0d;
        avg_value =0d;
        for (Double vals : saList) {
            sum += vals;
        }
         avg_value =  sum / saList.size();
         System.out.println("SA");
         System.out.println(avg_value);
         System.out.println("SA Max Value");
         System.out.println(Collections.max(saList));

        /*Average Value GA*/
        sum = 0d;
        avg_value =0d;
        for (Double vals : gaList) {
            sum += vals;
        }
         avg_value =  sum / gaList.size();
         System.out.println("GA");
         System.out.println(avg_value);
         System.out.println("GA Max Value");
         System.out.println(Collections.max(gaList));

        /*Average Value MIMIC*/
        sum = 0d;
        avg_value =0d;
        for (Double vals : mimicList) {
            sum += vals;
        }
         avg_value =  sum / mimicList.size();
         System.out.println("MIMIC");
         System.out.println(avg_value);
         System.out.println("MIMIC Max Value");
         System.out.println(Collections.max(mimicList));

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
