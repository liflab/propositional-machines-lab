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
package propmanlab;

import ca.uqac.lif.labpal.Laboratory;

public class MyLaboratory extends Laboratory
{
  /**
   * The maximum length of a trace
   */
  public static final int MAX_TRACE_LENGTH = 10000;
  
  /**
   * The interval, in number of events, between updates of the experiment's
   * measurements
   */
  public static final int s_eventStep = 10;
  
	@Override
	public void setup()
	{
		// Write your setup code here
	}
	
	public static void main(String[] args)
	{
		// Nothing else to do here
		MyLaboratory.initialize(args, MyLaboratory.class);
	}
}
