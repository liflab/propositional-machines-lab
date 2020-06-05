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
package propmanlab.scenarios.simple;

import ca.uqac.lif.cep.Processor;
import ca.uqac.lif.labpal.Random;
import ca.uqac.lif.synthia.Picker;
import ca.uqac.lif.synthia.util.Constant;

/**
 * Generates a trace of randomly generated multi-events over a given domain.
 */
public class RandomConcreteUniEventSource extends RandomConcreteMultiEventSource
{  
  /**
   * Creates a new random concrete uni-event source.
   * @param r A random source
   * @param num_events The number of events to generate
   * @param boolean_source A picker that assigns a Boolean value for each
   * propositional variable in the domain
   * @param variables The domain (list of variable names) inside each multi-event
   */
  public RandomConcreteUniEventSource(Random r, int num_events, 
      /*@ non_null @*/ Picker<Boolean> boolean_source, String ... variables)
  {
    super(r, num_events, new Constant<Integer>(1), boolean_source, variables);
  }
  
  @Override
  public String getFilename()
  {
    return "random-" + m_variables.length + "-" + m_random.getSeed() + ".txt";
  }

  @Override
  public Processor duplicate(boolean with_state)
  {
    // TODO Auto-generated method stub
    return null;
  }
}