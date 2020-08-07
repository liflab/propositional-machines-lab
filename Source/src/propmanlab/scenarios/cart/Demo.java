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
package propmanlab.scenarios.cart;

import ca.uqac.lif.cep.Pullable;
import ca.uqac.lif.cep.propman.DotMachineRenderer;
import ca.uqac.lif.synthia.random.RandomFloat;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Map;

@SuppressWarnings("unused")
public class Demo
{

  public static void main(String[] args) throws FileNotFoundException
  {
    CartLifecycleMonitor monitor = new CartLifecycleMonitor();
    DotMachineRenderer renderer = new DotMachineRenderer();
    PrintStream ps = new PrintStream(new FileOutputStream(new File("/tmp/cart.dot")));
    renderer.render(ps, monitor);
    ps.close();
    /*for (Map.Entry<CartLifecycleMonitor.CartState,Integer> e : monitor.map.entrySet())
    {
      System.out.println(e);
    }*/
    System.out.println(monitor.getStateCount());
    System.out.println(monitor.getTransitionCount());
    CartEventSource ces = new CartEventSource(20, new RandomFloat());
    Pullable p = ces.getPullableOutput();
    while (p.hasNext())
    {
      System.out.println(p.next());
    }
  }
}
