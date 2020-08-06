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

import ca.uqac.lif.json.JsonElement;
import ca.uqac.lif.json.JsonFalse;
import ca.uqac.lif.json.JsonNumber;
import ca.uqac.lif.labpal.provenance.ExperimentValue;
import ca.uqac.lif.mtnp.table.Table;
import ca.uqac.lif.mtnp.table.TableEntry;
import ca.uqac.lif.mtnp.table.TempTable;
import ca.uqac.lif.petitpoucet.NodeFunction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static propmanlab.AccessControlledStreamExperiment.BEST_EFFORT;
import static propmanlab.StreamExperiment.NB_UNITRACES_FALSE;
import static propmanlab.StreamExperiment.NB_UNITRACES_INCONCLUSIVE;
import static propmanlab.StreamExperiment.NB_UNITRACES_TRUE;
import static propmanlab.scenarios.Scenario.SCENARIO;

public class VerdictPurityTable extends Table
{
  protected static final transient JsonNumber s_zero = new JsonNumber(0);
  
  protected Map<String,AccessControlledStreamExperiment> m_entriesBest;
  
  protected Map<String,AccessControlledStreamExperiment> m_entriesProxy;
  
  protected List<String> m_scenarios;
  
  public VerdictPurityTable()
  {
    super();
    m_entriesBest = new HashMap<String,AccessControlledStreamExperiment>();
    m_entriesProxy = new HashMap<String,AccessControlledStreamExperiment>();
    m_scenarios = new ArrayList<String>();
    setTitle("Verdict purity for proxy vs. best effort");
    setNickname("tVerdictPurity");
  }
  
  public void add(AccessControlledStreamExperiment ... experiments)
  {
    for (AccessControlledStreamExperiment e : experiments)
    {
      String scenario = e.readString(SCENARIO);
      if (!m_scenarios.contains(scenario))
      {
        m_scenarios.add(scenario);
      }
      if (e.read(BEST_EFFORT) instanceof JsonFalse)
      {
        m_entriesProxy.put(scenario, e);
      }
      else
      {
        m_entriesBest.put(scenario, e);
      }
    }
  }
  
  @Override
  public TempTable getDataTable(boolean arg0)
  {
    TempTable tt = new TempTable(m_id, SCENARIO, "TP", "IP", "FP", "TB", "IB", "FB");
    for (String scenario : m_scenarios)
    {
      AccessControlledStreamExperiment e_best = m_entriesBest.get(scenario);
      AccessControlledStreamExperiment e_prox = m_entriesProxy.get(scenario);
      TableEntry te = new TableEntry();
      te.put(SCENARIO, scenario);
      te.put("TB", zeroIfNull(e_best.read(NB_UNITRACES_TRUE)));
      te.put("IB", zeroIfNull(e_best.read(NB_UNITRACES_INCONCLUSIVE)));
      te.put("FB", zeroIfNull(e_best.read(NB_UNITRACES_FALSE)));
      te.put("TP", zeroIfNull(e_prox.read(NB_UNITRACES_TRUE)));
      te.put("IP", zeroIfNull(e_prox.read(NB_UNITRACES_INCONCLUSIVE)));
      te.put("FP", zeroIfNull(e_prox.read(NB_UNITRACES_FALSE)));
      tt.add(te);
    }
    return tt;
  }

  @Override
  protected TempTable getDataTable(boolean arg0, String... arg1)
  {
    // Ignore column names
    return getDataTable(arg0);
  }

  @Override
  public NodeFunction getDependency(int row, int col)
  {
    if (row < 0 || row > m_scenarios.size() || col < 0 || col > 6)
    {
      return null;
    }
    String scenario = m_scenarios.get(row);
    switch (col)
    {
    case 1:
      return new ExperimentValue(m_entriesProxy.get(scenario), NB_UNITRACES_TRUE);
    case 2:
      return new ExperimentValue(m_entriesProxy.get(scenario), NB_UNITRACES_INCONCLUSIVE);
    case 3:
      return new ExperimentValue(m_entriesProxy.get(scenario), NB_UNITRACES_FALSE);
    case 4:
      return new ExperimentValue(m_entriesBest.get(scenario), NB_UNITRACES_TRUE);
    case 5:
      return new ExperimentValue(m_entriesBest.get(scenario), NB_UNITRACES_INCONCLUSIVE);
    case 6:
      return new ExperimentValue(m_entriesBest.get(scenario), NB_UNITRACES_FALSE);
    }
    return null;
  }
  
  protected static JsonNumber zeroIfNull(JsonElement e)
  {
    if (e == null || !(e instanceof JsonNumber))
    {
      return s_zero;
    }
    return (JsonNumber) e;
  }

}