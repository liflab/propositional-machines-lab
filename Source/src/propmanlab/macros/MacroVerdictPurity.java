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
package propmanlab.macros;

import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonNumber;
import ca.uqac.lif.json.JsonString;
import ca.uqac.lif.labpal.LatexNamer;
import ca.uqac.lif.labpal.macro.MacroMap;
import ca.uqac.lif.mtnp.table.TableEntry;
import ca.uqac.lif.mtnp.table.TempTable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import propmanlab.BigMath;
import propmanlab.MyLaboratory;
import propmanlab.VerdictPurityTable;
import propmanlab.scenarios.Scenario;

public class MacroVerdictPurity extends MacroMap
{
  /**
   * The table from which the macro fetches the results
   */
  protected VerdictPurityTable m_table;
  
  /**
   * Creates a new instance of the macro.
   * @param lab The lab from which to fetch the values
   * @param t The table from which the macro fetches the results
   */
  public MacroVerdictPurity(MyLaboratory lab, VerdictPurityTable t)
  {
    super(lab);
    m_table = t;
  }
  
  @Override
  public void computeValues(Map<String,JsonElement> map)
  {
    TempTable table = m_table.getDataTable();
    for (TableEntry te : table.getEntries())
    {
      String scenario = te.get(Scenario.SCENARIO).toString();
      String macro_name = "purity" + LatexNamer.latexify(scenario);
      if (!m_descriptions.containsKey(scenario))
      {
        add(macro_name, "Verdict purity for the over-approximation of the scenario " + scenario);
        map.put(macro_name, new JsonNumber(getPurityFor(te)));
      }
    }
  }
  
  protected int getExponent(float first, float second)
  {
    BigDecimal b_first = BigMath.powBig(10, first);
    BigDecimal b_second = BigMath.powBig(10, second);
    BigDecimal b_diff = b_first.subtract(b_second).abs().divide(b_first.add(b_second));
    return (int) (BigMath.logBigDecimal(b_diff) / BigMath.LOG_10);
  }
  
  protected int getPurityFor(TableEntry te)
  {
    List<Float> list = new ArrayList<Float>(3);
    Object o = te.get("TB");
    list.add(te.get("TB").numberValue().floatValue());
    list.add(te.get("FB").numberValue().floatValue());
    list.add(te.get("IB").numberValue().floatValue());
    Collections.sort(list);
    float first = list.get(2);
    float second = list.get(1);
    return getExponent(first, second);
  }
}
