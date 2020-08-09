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
package propmanlab.scenarios.temperature;

import ca.uqac.lif.cep.propman.ExplicitPropositionalMachine;

import static ca.uqac.lif.cep.propman.MultiMonitor.EMPTY;
import static ca.uqac.lif.cep.propman.MultiMonitor.EPSILON;
import static ca.uqac.lif.cep.propman.MultiMonitor.NU;

import ca.uqac.lif.cep.propman.MultiEvent;
import java.util.List;

/**
 * Encodes the property: "for the first x units of time, whenever the
 * temperature falls below a certain threshold, it will again be above
 * the threshold within y units of time".
 * 
 * @author Rania Taleb, Sylvain Hallé
 */
public class OverThresholdWithinInterval extends ExplicitPropositionalMachine
{ 
  /**
   * Creates a new instance of the property.
   * @param below_threshold The condition specifying that the temperature
   * is below the threshold
   * @param over_threshold The condition specifying that the temperature
   * is over the threshold
   * @param until The number of units of time during which the condition
   * must be respected (i.e. x)
   * @param interval The number of readings before which the temperature
   * must return above the threshold (i.e. y) 
   */
  public OverThresholdWithinInterval(MultiEvent below_threshold, MultiEvent over_threshold, int until, int interval)
  {
    super();
    int state_cnt = 0;
    for (int i = 0; i < until; i++)
    {
      if (i > 0)
      {
        addTransition(i - 1, new Transition(i, over_threshold, EPSILON));
      }
      state_cnt++;
    }
    addTransition(state_cnt - 1, new TransitionOtherwise(state_cnt, NU));
    addTransition(state_cnt, new TransitionOtherwise(state_cnt, NU));
    int true_sink = state_cnt;
    int false_sink = ++state_cnt;
    addTransition(false_sink, new TransitionOtherwise(false_sink, EMPTY));
    for (int i = 0; i <= until - interval; i++)
    {
      int previous = i;
      for (int j = 0; j < interval; j++)
      {
        state_cnt++;        
        if (j < interval - 1)
        {
          addTransition(previous, new Transition(state_cnt, below_threshold, EPSILON));
        }
        else
        {
          addTransition(previous, new Transition(false_sink, below_threshold, EMPTY));
        }
        if (previous != i)
        {
          addTransition(previous, new Transition(i + j + 1, over_threshold, EPSILON));
        }
        previous = state_cnt;
      }
    }
    int error_sink = state_cnt + 1;
    for (int i = 0; i < state_cnt; i++)
    {
      if (!hasOtherwise(i))
      {
        addTransition(i, new TransitionOtherwise(error_sink, EPSILON));
      }
    }
    addTransition(error_sink, new TransitionOtherwise(error_sink, EPSILON));
    removeUnreachableStates();
  }
  
  protected boolean hasOtherwise(int state_num)
  {
    List<Transition> trans = m_delta.get(state_num);
    if (trans == null)
    {
      return true;
    }
    for (Transition t : trans)
    {
      if (t instanceof TransitionOtherwise)
      {
        return true;
      }
    }
    return false;
  }
}