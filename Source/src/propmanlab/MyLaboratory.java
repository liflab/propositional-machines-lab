/*
    A benchmark for propositional machines in BeepBeep 3
    Copyright (C) 2020 Laboratoire d'informatique formelle

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package propmanlab;

import ca.uqac.lif.labpal.Laboratory;
import ca.uqac.lif.labpal.LatexNamer;
import ca.uqac.lif.labpal.Region;
import ca.uqac.lif.labpal.TitleNamer;
import propmanlab.macros.LabStats;

public class MyLaboratory extends Laboratory
{
  /**
   * The maximum length of a trace
   */
  public static int MAX_TRACE_LENGTH = 10000;

  /**
   * The interval, in number of events, between updates of the experiment's
   * measurements
   */
  public static int s_eventStep = 10;

  /**
   * A nicknamer
   */
  public static transient LatexNamer s_nicknamer = new LatexNamer();

  /**
   * A title namer
   */
  public static transient TitleNamer s_titleNamer = new TitleNamer();

  /**
   * An experiment factory
   */
  public transient StreamExperimentFactory m_factory = new StreamExperimentFactory(this);
  
  /**
   * The name of the region dimension "problem instance"
   */
  public static transient final String PROB_INSTANCE = "Problem instance";
  
  /**
   * The name of the problem instance "simple"
   */
  public static transient final String SIMPLE = "Simple";

  @Override
  public void setup()
  {
    setTitle("Benchmark for propositional machines in BeepBeep");
    setDoi("TODO");
    setAuthor("Rania Taleb, Sylvain Hallé");

    Region big_r = new Region();
    big_r.add(PROB_INSTANCE, SIMPLE);
    
    // Plot entropy for a single trace
    for (Region r : big_r.all(PROB_INSTANCE))
    {
      
    }

    // Macros
    add(new LabStats(this));
  }

  /**
   * Initializes the lab
   * @param args Command line arguments
   */
  public static void main(String[] args)
  {
    // Nothing else to do here
    MyLaboratory.initialize(args, MyLaboratory.class);
  }
  
}
