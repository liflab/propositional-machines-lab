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

import static ca.uqac.lif.cep.propman.MultiMonitor.EMPTY;
import static ca.uqac.lif.cep.propman.MultiMonitor.EPSILON;
import static propmanlab.scenarios.mplayer.MPlayerSource.s_play;
import static propmanlab.scenarios.mplayer.MPlayerSource.s_pause;
import static propmanlab.scenarios.mplayer.MPlayerSource.s_stop;
import static propmanlab.scenarios.mplayer.MPlayerSource.s_buffer;
import static propmanlab.scenarios.mplayer.MPlayerSource.s_decode;

import ca.uqac.lif.cep.propman.PropositionalMachine;

public class Property1 extends PropositionalMachine
{
  public Property1()
  {
    super();
    addTransition(0, new Transition(1, s_play, EPSILON));
    addTransition(1, new Transition(2, s_pause, EPSILON));
    addTransition(1, new Transition(0, s_stop, EPSILON));
    addTransition(1, new Transition(3, s_buffer, EPSILON));
    addTransition(2, new Transition(2, s_pause, EPSILON));
    addTransition(2, new Transition(1, s_play, EPSILON));
    addTransition(2, new Transition(0, s_stop, EPSILON));
    addTransition(0, new Transition(1, s_play, EPSILON));
    addTransition(3, new Transition(4, s_decode, EPSILON));
    addTransition(3, new Transition(2, s_pause, EPSILON));
    addTransition(3, new Transition(1, s_stop, EPSILON));
    addTransition(4, new Transition(3, s_buffer, EPSILON));
    addTransition(4, new Transition(1, s_stop, EPSILON));
    addTransition(4, new Transition(2, s_pause, EPSILON));
    addTransition(0, new TransitionOtherwise(5, EMPTY));
    addTransition(1, new TransitionOtherwise(5, EMPTY));
    addTransition(2, new TransitionOtherwise(5, EMPTY));
    addTransition(3, new TransitionOtherwise(5, EMPTY));
    addTransition(4, new TransitionOtherwise(5, EMPTY));
    addTransition(5, new TransitionOtherwise(5, EMPTY));
  }
  
  @Override
  public Property1 duplicate(boolean with_state)
  {
    return new Property1();
  }
}
