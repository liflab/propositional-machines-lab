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
package propmanlab.scenarios.cpuload;

import ca.uqac.lif.cep.ltl.Troolean;
import ca.uqac.lif.cep.propman.ConcreteMultiEvent;
import ca.uqac.lif.cep.propman.MultiEvent;
import ca.uqac.lif.cep.propman.MultiEventFunction;
import ca.uqac.lif.cep.propman.Valuation;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Adds imprecision to the temperature readings by setting multiple contiguous
 * variables to <tt>true</tt>.
 * <p>
 * For example, suppose that the following vector represents the values of
 * Boolean variables, each corresponding to an interval of 1 degree F; the
 * first variable represents 70F, and the last represents 79F:
 * <pre>
 * 0001000000
 * </pre> 
 * In this case, the uni-event encodes a temperature of 73F. The
 * {@link BlurLoad} function, if given a blur amount of 1, will produce
 * this multi-event as its output:
 * <pre>
 * 0010000000,0001000000,0000100000
 * </pre>
 * This multi-event encodes the fact that the temperature is either 72F, 73F
 * or 74F --modelling an uncertainty of +/- 1F over the original reading.
 */
public class BlurLoad extends MultiEventFunction
{
  /**
   * The number of contiguous variables, on both sides of the actual
   * <tt>true</tt> variable, to also set to <tt>true</tt>
   */
  protected int m_blurAmount;
  
  /**
   * Creates a new instance of the function
   * @param blur_amount The number of contiguous variables, on both sides of 
   * the actual <tt>true</tt> variable, to also set to <tt>true</tt>
   * @param num_intervals The total number of intervals used to represent
   * the temperature
   */
  public BlurLoad(int blur_amount)
  {
    super();
    m_blurAmount = blur_amount;
  }
  
  @Override
  public MultiEvent getValue(MultiEvent x)
  {
    Set<Valuation> vals = x.getValuations();
    Set<Valuation> new_vals = new HashSet<Valuation>();
    Valuation v = null;
    for (Valuation val : vals)
    {
      // Pick the only valuation from the input multi-event
      v = val;
      break;
    }
    if (v == null)
    {
      // Not supposed to happen
      return null;
    }
    int true_variable = 0;
    for (Map.Entry<String,Troolean.Value> entry : v.entrySet())
    {
      if (entry.getValue() == Troolean.Value.TRUE)
      {
        String key = entry.getKey();
        true_variable = Integer.parseInt(key.substring(1));
        break;
      }
    }
    for (int i = Math.max(0, true_variable - m_blurAmount); i < Math.min(100, true_variable + m_blurAmount + 1); i++)
    {
      Valuation v2 = new Valuation();
      for (int j = 0; j < 101; j++)
      {
        if (j != i)
        {
          v2.put("t" + j, Troolean.Value.FALSE);
        }
        else
        {
          v2.put("t" + j, Troolean.Value.TRUE);
        }
      }
      new_vals.add(v2);
    }
    return new ConcreteMultiEvent(new_vals);
  }
}