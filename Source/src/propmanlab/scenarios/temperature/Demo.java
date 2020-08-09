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
import java.math.BigDecimal;
import java.math.RoundingMode;
import propmanlab.BigMath;
import ca.uqac.lif.cep.Connector;
import ca.uqac.lif.cep.Pullable;
import ca.uqac.lif.cep.propman.AccessControlledMonitor;
import ca.uqac.lif.cep.propman.ConcreteMultiEvent;
import ca.uqac.lif.cep.propman.DotMachineRenderer;
import ca.uqac.lif.cep.propman.StatelessPropositionalMachine;
import ca.uqac.lif.cep.propman.SymbolicMultiEvent;

@SuppressWarnings("unused")
public class Demo
{
  public static void main(String[] args) throws FileNotFoundException
  {
    ConcreteMultiEvent tis = TemperatureSource.getTemperatureIsUnder(TemperatureSource.s_minTemp, TemperatureSource.s_maxTemp, TemperatureSource.s_interval, 74);
    ConcreteMultiEvent tio = TemperatureSource.getTemperatureIsOver(TemperatureSource.s_minTemp, TemperatureSource.s_maxTemp, TemperatureSource.s_interval, 74);
    OverThresholdWithinInterval otwi = new OverThresholdWithinInterval(tis, tio, 2, 2);
    DotMachineRenderer renderer = new DotMachineRenderer();
    PrintStream ps = new PrintStream(new FileOutputStream(new File("/tmp/mach.dot")));
    renderer.setNickname(tis, "below");
    renderer.setNickname(tio, "above");
    renderer.render(ps, otwi);
    ps.close();
    
    SimulatedTemperatureSource source = new SimulatedTemperatureSource(70, 95, 1, 72, 72, 74, 78, 78, 78);
    StatelessPropositionalMachine proxy = new StatelessPropositionalMachine();
    proxy.addCondition(SymbolicMultiEvent.ALL, new BlurTemperature(0));
    AccessControlledMonitor acm = new AccessControlledMonitor(proxy, otwi);
    Connector.connect(source, acm);
    Pullable p = acm.getPullableOutput();
    while (p.hasNext())
    {
      System.out.println(p.next());
    }
    
    // A simple calculation
    int v_x = 51996, v_y = 51993, v_z = 48665;
    BigDecimal x = BigMath.powBig(10, v_x);
    BigDecimal y = BigMath.powBig(10, v_y);
    BigDecimal z = BigMath.powBig(10, v_z);
    BigDecimal sum = x.add(y).add(z);
    BigDecimal f = x.divide(sum, 1000, RoundingMode.HALF_EVEN);
    System.out.println(f);
    double log_f = BigMath.logBigDecimal(f) / BigMath.LOG_10;
    System.out.println(log_f);
  }
}
