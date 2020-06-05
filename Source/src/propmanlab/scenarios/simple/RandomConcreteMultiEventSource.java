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
import ca.uqac.lif.cep.ltl.Troolean;
import ca.uqac.lif.cep.propman.ConcreteMultiEvent;
import ca.uqac.lif.cep.propman.MultiEvent;
import ca.uqac.lif.cep.propman.Valuation;
import ca.uqac.lif.labpal.Random;
import ca.uqac.lif.synthia.Picker;
import java.util.HashSet;
import java.util.Set;
import propmanlab.source.RandomMultiEventSource;

/**
 * Generates a trace of randomly generated multi-events over a given domain.
 */
public class RandomConcreteMultiEventSource extends RandomMultiEventSource
{
  /**
   * A picker that determines the number of valuations inside each generated
   * multi-event
   */
  /*@ non_null @*/ protected transient Picker<Integer> m_eventSize;
  
  /**
   * A picker that assigns a Boolean value for each propositional variable
   * in the domain
   */
  /*@ non_null @*/ protected transient Picker<Boolean> m_booleanSource;
  
  /**
   * Creates a new random concrete multi-event source.
   * @param r A random source
   * @param num_events The number of events to generate
   * @param event_size A picker that determines the number of valuations inside each
   * generated multi-event
   * @param boolean_source A picker that assigns a Boolean value for each
   * propositional variable in the domain
   * @param variables The domain (list of variable names) inside each multi-event
   */
  public RandomConcreteMultiEventSource(Random r, int num_events, /*@ non_null @*/ Picker<Integer> event_size, 
      /*@ non_null @*/ Picker<Boolean> boolean_source, String ... variables)
  {
    super(r, num_events, variables);
    m_eventSize = event_size;
    m_booleanSource = boolean_source;
  }

  @Override
  protected MultiEvent getEvent()
  {
    int size = m_eventSize.pick();
    Set<Valuation> valuations = new HashSet<Valuation>(size);
    for (int i = 0; i < size; i++)
    {
      Valuation v = new Valuation();
      for (int x = 0; x < m_variables.length; x++)
      {
        boolean b = m_booleanSource.pick();
        v.put(m_variables[x], Troolean.trooleanValue(b));
      }
      valuations.add(v);
    }
    MultiEvent e = new ConcreteMultiEvent(valuations);
    return e;
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
