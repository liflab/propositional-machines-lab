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
package propmanlab.source;

import ca.uqac.lif.labpal.Random;

/**
 * A source of randomly-generated events.
 */
public abstract class RandomMultiEventSource extends MultiEventSource
{
  /**
   * The random number generator used to generate the numbers
   */
  protected transient /*@ non_null @*/ Random m_random;
  
  /**
   * Creates a new random source
   * @param r A random generator
   * @param num_events The number of events to produce
   */
  public RandomMultiEventSource(Random r, int num_events, String ... variables)
  {
    super(num_events, variables);
    m_random = r;
  }
}
