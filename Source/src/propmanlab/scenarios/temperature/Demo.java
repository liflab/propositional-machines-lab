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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import ca.uqac.lif.cep.Connector;
import ca.uqac.lif.cep.Pullable;
import ca.uqac.lif.cep.propman.AccessControlledMonitor;
import ca.uqac.lif.cep.propman.DotMachineRenderer;
import ca.uqac.lif.cep.propman.StatelessPropositionalMachine;
import ca.uqac.lif.cep.propman.SymbolicMultiEvent;

@SuppressWarnings("unused")
public class Demo
{
  public static void main(String[] args) throws FileNotFoundException
  {
    TemperatureIsUnder tis = new TemperatureIsUnder(74, 70, 1);
    OverThresholdWithinInterval otwi = new OverThresholdWithinInterval(tis, 5, 2);
    /*
    DotMachineRenderer renderer = new DotMachineRenderer();
    PrintStream ps = new PrintStream(new FileOutputStream(new File("/tmp/mach.dot")));
    renderer.setNickname(tis, "f");
    renderer.render(ps, otwi);
    ps.close();
    */
    SimulatedTemperatureSource source = new SimulatedTemperatureSource(70, 90, 1, 74, 74, 74, 78, 78, 78);
    StatelessPropositionalMachine proxy = new StatelessPropositionalMachine();
    proxy.addCondition(SymbolicMultiEvent.ALL, new BlurTemperature(2, 20));
    AccessControlledMonitor acm = new AccessControlledMonitor(proxy, otwi);
    Connector.connect(source, acm);
    Pullable p = acm.getPullableOutput();
    while (p.hasNext())
    {
      System.out.println(p.next());
    }
  }
}
