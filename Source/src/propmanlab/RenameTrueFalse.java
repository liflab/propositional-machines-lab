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

import ca.uqac.lif.mtnp.table.HardTable;
import ca.uqac.lif.mtnp.table.TableEntry;
import ca.uqac.lif.mtnp.table.TableTransformation;
import ca.uqac.lif.mtnp.table.TempTable;

/**
 * Renames the "true" and "false" columns of a table in "With" and "Without"
 */
public class RenameTrueFalse implements TableTransformation
{
  protected String m_column;
  
  public RenameTrueFalse(String column)
  {
    super();
    m_column = column;
  }
  
  @Override
  public TempTable transform(TempTable ... tables)
  {
    HardTable ht = new HardTable(m_column, "With", "Without");
    TempTable tt = tables[0];
    for (TableEntry te : tt.getEntries())
    {
      TableEntry new_te = new TableEntry();
      new_te.put("With", te.get("true"));
      new_te.put("Without", te.get("false"));
      new_te.put(m_column, te.get(m_column));
      ht.add(new_te);
    }
    return ht.getDataTable();
  }
}
