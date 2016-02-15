//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
//
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

package org.uma.jmetal.experiment;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.algorithm.multiobjective.smpso.SMPSOBuilder;
import org.uma.jmetal.algorithm.multiobjective.spea2.SPEA2Builder;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.multiobjective.dtlz.*;
import org.uma.jmetal.problem.multiobjective.wfg.*;
import org.uma.jmetal.problem.multiobjective.zdt.*;
import org.uma.jmetal.qualityindicator.impl.*;
import org.uma.jmetal.qualityindicator.impl.hypervolume.PISAHypervolume;
import org.uma.jmetal.qualityindicator.impl.hypervolume.WFGHypervolume;
import org.uma.jmetal.referencePoint.ReferencePoint;
import org.uma.jmetal.referencePoint.impl.IdealPoint;
import org.uma.jmetal.referencePoint.impl.NadirPoint;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.archive.BoundedArchive;
import org.uma.jmetal.util.archive.impl.CrowdingDistanceArchive;
import org.uma.jmetal.util.archive.impl.HypervolumeArchive;
import org.uma.jmetal.util.cosinedistance.CosineDistanceArchive;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;
import org.uma.jmetal.util.experiment.Experiment;
import org.uma.jmetal.util.experiment.ExperimentBuilder;
import org.uma.jmetal.util.experiment.component.*;
import org.uma.jmetal.util.experiment.util.TaggedAlgorithm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Example of experimental study based on solving the ZDT problems with five algorithms: NSGAII, SPEA2, MOCell,
 * SMPSO and AbYSS
 *
 * This experiment assumes that the reference Pareto front are known, so the names of files containing
 * them and the directory where they are located must be specified.
 *
 * Six quality indicators are used for performance assessment.
 *
 * The steps to carry out the experiment are:
 * 1. Configure the experiment
 * 2. Execute the algorithms
 * 3. Compute que quality indicators
 * 4. Generate Latex tables reporting means and medians
 * 5. Generate R scripts to produce latex tables with the result of applying the Wilcoxon Rank Sum Test
 * 6. Generate Latex tables with the ranking obtained by applying the Friedman test
 * 7. Generate R scripts to obtain boxplots
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */

public class ANTs16Experiment {
  public static void main(String[] args) throws IOException, ClassNotFoundException {
    if (args.length != 1) {
      throw new JMetalException("Missing argument: experiment base directory") ;
    }
    String experimentBaseDirectory = args[0] ;

    List<Problem<DoubleSolution>> problemList = Arrays.<Problem<DoubleSolution>>asList(
        new ZDT1(), new ZDT2(), new ZDT3(), new ZDT4(), new ZDT6()) ;
    /*    new DTLZ1(7, 2), new DTLZ2(12, 2), new DTLZ3(12, 2), new DTLZ4(12, 2), new DTLZ5(12, 2),
        new DTLZ6(12,2 ), new DTLZ7(22, 2),
        new WFG1(), new WFG2(), new WFG3(), new WFG4(), new WFG5(),new WFG6(), new WFG7(),
        new WFG8(), new WFG9()) ;*/

    List<String> referenceFrontFileNames = Arrays.asList(
        "ZDT1.pf", "ZDT2.pf", "ZDT3.pf", "ZDT4.pf", "ZDT6.pf",
        "DTLZ1.2D.pf", "DTLZ2.2D.pf", "DTLZ3.2D.pf", "DTLZ4.2D.pf", "DTLZ5.2D.pf", "DTLZ6.2D.pf", "DTLZ7.2D.pf",
        "WFG1.2D.pf", "WFG2.2D.pf", "WFG3.2D.pf", "WFG4.2D.pf", "WFG5.2D.pf", "WFG6.2D.pf",
        "WFG7.2D.pf", "WFG8.2D.pf", "WFG9.2D.pf") ;

    List<TaggedAlgorithm<List<DoubleSolution>>> algorithmList = configureAlgorithmList(problemList) ;

    Experiment<DoubleSolution, List<DoubleSolution>> experiment =
        new ExperimentBuilder<DoubleSolution, List<DoubleSolution>>("SMPSO200")
            .setAlgorithmList(algorithmList)
            .setProblemList(problemList)
            .setReferenceFrontDirectory("/pareto_fronts")
            .setReferenceFrontFileNames(referenceFrontFileNames)
            .setExperimentBaseDirectory(experimentBaseDirectory)
            .setOutputParetoFrontFileName("FUN")
            .setOutputParetoSetFileName("VAR")
            .setIndicatorList(Arrays.asList(
                new Epsilon<DoubleSolution>(), new Spread<DoubleSolution>(), new GenerationalDistance<DoubleSolution>(),
                new PISAHypervolume<DoubleSolution>(),
                new InvertedGenerationalDistance<DoubleSolution>(),
                new InvertedGenerationalDistancePlus<DoubleSolution>()))
            .setIndependentRuns(25)
            .setNumberOfCores(7)
            .build();

 //   new ExecuteAlgorithms<>(experiment).run();

    new ComputeQualityIndicators<>(experiment).run() ;
    new GenerateLatexTablesWithStatistics(experiment).run() ;
    new GenerateWilcoxonTestTablesWithR<>(experiment).run() ;
    new GenerateFriedmanTestTables<>(experiment).run();
    new GenerateBoxplotsWithR<>(experiment).setRows(3).setColumns(3).setDisplayNotch().run() ;

  }

  /**
   * The algorithm list is composed of pairs {@link Algorithm} + {@link Problem} which form part of a
   * {@link TaggedAlgorithm}, which is a decorator for class {@link Algorithm}.
   *
   * @param problemList
   * @return
   */
  static List<TaggedAlgorithm<List<DoubleSolution>>> configureAlgorithmList(List<Problem<DoubleSolution>> problemList) {
    List<TaggedAlgorithm<List<DoubleSolution>>> algorithms = new ArrayList<>() ;

    for (int i = 0 ; i < problemList.size(); i++) {
      double mutationProbability = 1.0 / problemList.get(i).getNumberOfVariables() ;
      double mutationDistributionIndex = 20.0 ;
      Algorithm<List<DoubleSolution>> algorithm = new SMPSOBuilder((DoubleProblem)problemList.get(i),
          new CrowdingDistanceArchive<DoubleSolution>(200))
          .setMutation(new PolynomialMutation(mutationProbability, mutationDistributionIndex))
          .setMaxIterations(300)
          .setSwarmSize(200)
          .setSolutionListEvaluator(new SequentialSolutionListEvaluator<DoubleSolution>())
          .build() ;
      algorithms.add(new TaggedAlgorithm<List<DoubleSolution>>(algorithm, problemList.get(i))) ;
    }

    for (int i = 0 ; i < problemList.size(); i++) {
      double mutationProbability = 1.0 / problemList.get(i).getNumberOfVariables() ;
      double mutationDistributionIndex = 20.0 ;
      Algorithm<List<DoubleSolution>> algorithm = new SMPSOBuilder((DoubleProblem)problemList.get(i),
          new HypervolumeArchive<DoubleSolution>(200, new WFGHypervolume<DoubleSolution>()))
          .setMutation(new PolynomialMutation(mutationProbability, mutationDistributionIndex))
          .setMaxIterations(300)
          .setSwarmSize(200)
          .setSolutionListEvaluator(new SequentialSolutionListEvaluator<DoubleSolution>())
          .build() ;
      algorithms.add(new TaggedAlgorithm<List<DoubleSolution>>(algorithm, "SMPSOhv", problemList.get(i))) ;
    }
/*
    for (int i = 0 ; i < problemList.size(); i++) {
      double mutationProbability = 1.0 / problemList.get(i).getNumberOfVariables() ;
      double mutationDistributionIndex = 20.0 ;
      ReferencePoint referencePoint = new IdealPoint(problemList.get(i).getNumberOfObjectives()) ;
      BoundedArchive<DoubleSolution> archive = new CosineDistanceArchive<>(100, referencePoint) ;

      Algorithm<List<DoubleSolution>> algorithm = new SMPSOBuilder((DoubleProblem)problemList.get(i),
          archive)
          .setMutation(new PolynomialMutation(mutationProbability, mutationDistributionIndex))
          .setMaxIterations(250)
          .setSwarmSize(100)
          .setSolutionListEvaluator(new SequentialSolutionListEvaluator<DoubleSolution>())
          .build() ;
      algorithms.add(new TaggedAlgorithm<List<DoubleSolution>>(algorithm, "SMPSOId", problemList.get(i))) ;
    }

    for (int i = 0 ; i < problemList.size(); i++) {
      double mutationProbability = 1.0 / problemList.get(i).getNumberOfVariables() ;
      double mutationDistributionIndex = 20.0 ;
      ReferencePoint referencePoint = new NadirPoint(problemList.get(i).getNumberOfObjectives()) ;
      BoundedArchive<DoubleSolution> archive = new CosineDistanceArchive<>(100, referencePoint) ;

      Algorithm<List<DoubleSolution>> algorithm = new SMPSOBuilder((DoubleProblem)problemList.get(i),
          archive)
          .setMutation(new PolynomialMutation(mutationProbability, mutationDistributionIndex))
          .setMaxIterations(250)
          .setSwarmSize(100)
          .setSolutionListEvaluator(new SequentialSolutionListEvaluator<DoubleSolution>())
          .build() ;
      algorithms.add(new TaggedAlgorithm<List<DoubleSolution>>(algorithm, "SMPSONa", problemList.get(i))) ;
    }
*/
    return algorithms ;
  }
}
