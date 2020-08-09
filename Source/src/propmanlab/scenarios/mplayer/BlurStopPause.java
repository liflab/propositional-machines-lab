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
package propmanlab.scenarios.mplayer;

import ca.uqac.lif.cep.functions.FunctionException;
import ca.uqac.lif.cep.ltl.Troolean.Value;
import ca.uqac.lif.cep.propman.ConcreteMultiEvent;
import ca.uqac.lif.cep.propman.MultiEvent;
import ca.uqac.lif.cep.propman.MultiEventFunction;
import ca.uqac.lif.cep.propman.Valuation;
import java.util.HashSet;
import java.util.Set;

public class BlurStopPause extends MultiEventFunction
{
  /**
   * A reference to a single publicly visible instance of the function
   */
  public static final transient BlurStopPause instance = new BlurStopPause();
  
  /**
   * An array with the {@link Troolean} values <tt>true</tt> and <tt>false</tt>.
   * Used only to avoid re-creating this array every time an event is processed
   * in {@link #getValue(MultiEvent)}.
   */
  protected static final transient Value[] s_values = new Value[] {Value.FALSE, Value.TRUE}; 
  
  /**
   * Creates a new instance of the function
   */
  protected BlurStopPause()
  {
    super();
  }
  
  @Override
  public MultiEvent getValue(MultiEvent x) throws FunctionException
  {
    Set<Valuation> valuations = new HashSet<Valuation>(4);
    Valuation v = null;
    for (Valuation val : x.getValuations())
    {
      // Gets the single valuation in the event
      v = val;
      break;
    }
    if (v == null)
    {
      throw new FunctionException("No valuation in the event");
    }
    if (v.get("stop") == Value.FALSE && v.get("pause") == Value.FALSE)
    {
      // Event is neither 'stop' nor 'pause'; leave it as is
      return x;
    }
    Valuation v1 = new Valuation(v);
    v1.put("stop", Value.TRUE);
    v1.put("pause", Value.FALSE);
    valuations.add(v1);
    Valuation v2 = new Valuation(v);
    v2.put("stop", Value.FALSE);
    v2.put("pause", Value.TRUE);
    valuations.add(v1);
    valuations.add(v2);
    ConcreteMultiEvent ce = new ConcreteMultiEvent(valuations);
    return ce;
  }
}
