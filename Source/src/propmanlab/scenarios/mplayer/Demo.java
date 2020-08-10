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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import propmanlab.BigMath;
import ca.uqac.lif.cep.Connector;
import ca.uqac.lif.cep.Pullable;
import ca.uqac.lif.cep.Pushable;
import ca.uqac.lif.cep.io.Print;
import ca.uqac.lif.cep.propman.AccessControlledMonitor;
import ca.uqac.lif.cep.propman.ConcreteMultiEvent;
import ca.uqac.lif.cep.propman.DotMachineRenderer;
import ca.uqac.lif.cep.propman.StatelessPropositionalMachine;
import ca.uqac.lif.cep.propman.SymbolicMultiEvent;

import static propmanlab.scenarios.mplayer.MPlayerSource.BUFFER;
import static propmanlab.scenarios.mplayer.MPlayerSource.DECODE;
import static propmanlab.scenarios.mplayer.MPlayerSource.PAUSE;
import static propmanlab.scenarios.mplayer.MPlayerSource.PLAY;
import static propmanlab.scenarios.mplayer.MPlayerSource.STOP;

@SuppressWarnings("unused")
public class Demo
{
  public static void main(String[] args) throws FileNotFoundException
  {
    LifecycleProperty p1 = new LifecycleProperty();
    DotMachineRenderer renderer = new DotMachineRenderer();
    PrintStream ps = new PrintStream(new FileOutputStream(new File("/tmp/mach.dot")));
    renderer.setNickname(MPlayerSource.s_buffer, BUFFER);
    renderer.setNickname(MPlayerSource.s_decode, DECODE);
    renderer.setNickname(MPlayerSource.s_play, PLAY);
    renderer.setNickname(MPlayerSource.s_pause, PAUSE);
    renderer.setNickname(MPlayerSource.s_stop, STOP);
    renderer.render(ps, p1);
    ps.close();
    Pushable p = p1.getPushableInput();
    Print print = new Print();
    Connector.connect(p1, print);
    p.push(MPlayerSource.s_play);
    p.push(MPlayerSource.s_buffer);
    p.push(MPlayerSource.s_stop);
    p.push(MPlayerSource.s_play);
    p.push(MPlayerSource.s_pause);
    p.push(MPlayerSource.s_pause);
  }
}
