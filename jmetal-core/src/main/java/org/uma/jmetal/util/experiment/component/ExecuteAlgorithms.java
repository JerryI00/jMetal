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
//

package org.uma.jmetal.util.experiment.component;

import org.apache.commons.lang3.SerializationUtils;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.experiment.ExperimentComponent;
import org.uma.jmetal.util.experiment.ExperimentConfiguration;
import org.uma.jmetal.util.experiment.util.MultithreadedExperimentExecutor;
import org.uma.jmetal.util.experiment.util.TaggedAlgorithm;

import java.io.File;

/**
 * This class executes the algorithms the have been configured with a instance of class
 * {@link ExperimentConfiguration}. For each combination algorithm + problem + runId an instance
 * of {@link TaggedAlgorithm} is created and inserted as a task of a {@link MultithreadedExperimentExecutor},
 * which runs all the algorithms.
 *
 * The result of the execution is a pair of files FUNrunId.tsv and VARrunID.tsv per configuration, which are
 * stored in the directory {@link ExperimentConfiguration #getExperimentBaseDirectory()}/algorithmName/problemName.
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public class ExecuteAlgorithms<S extends Solution<?>, Result> implements ExperimentComponent {
  private ExperimentConfiguration<S, Result> configuration ;

  /** Constructor */
  public ExecuteAlgorithms(ExperimentConfiguration<S, Result> configuration) {
    this.configuration = configuration ;
  }

  @Override
  public void run() {
    JMetalLogger.logger.info("ExecuteAlgorithms: Preparing output directory");
    prepareOutputDirectory() ;

    MultithreadedExperimentExecutor<S, Result> parallelExecutor ;



    for (TaggedAlgorithm<Result> algorithm : configuration.getAlgorithmList()) {
      parallelExecutor = new MultithreadedExperimentExecutor<S, Result>(configuration.getNumberOfCores()) ;
      parallelExecutor.start(this);

      for (int i = 0; i < configuration.getIndependentRuns(); i++) {
        TaggedAlgorithm<Result> clonedAlgorithm = SerializationUtils.clone(algorithm) ;
        parallelExecutor.addTask(new Object[]{clonedAlgorithm, i, configuration});
      }

      parallelExecutor.parallelExecution();
      parallelExecutor.stop();
    }


  }

  private void prepareOutputDirectory() {
    if (experimentDirectoryDoesNotExist()) {
      createExperimentDirectory() ;
    }
  }

  private boolean experimentDirectoryDoesNotExist() {
    boolean result;
    File experimentDirectory;

    experimentDirectory = new File(configuration.getExperimentBaseDirectory());
    if (experimentDirectory.exists() && experimentDirectory.isDirectory()) {
      result = false;
    } else {
      result = true;
    }

    return result;
  }

  private void createExperimentDirectory() {
    File experimentDirectory;
    experimentDirectory = new File(configuration.getExperimentBaseDirectory());

    if (experimentDirectory.exists()) {
      experimentDirectory.delete() ;
    }

    boolean result ;
    result = new File(configuration.getExperimentBaseDirectory()).mkdirs() ;
    if (!result) {
      throw new JMetalException("Error creating experiment directory: " +
          configuration.getExperimentBaseDirectory()) ;
    }
  }
}
