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

import ca.uqac.lif.cep.Processor;
import ca.uqac.lif.cep.propman.MultiEvent;
import ca.uqac.lif.cep.propman.Valuation;
import ca.uqac.lif.cep.propman.ValuationIterator;
import ca.uqac.lif.synthia.Picker;
import ca.uqac.lif.synthia.util.ElementPicker;
import java.util.HashSet;
import java.util.Set;
import propmanlab.scenarios.cart.CartEvent.CartAdd;
import propmanlab.scenarios.cart.CartEvent.CartCreate;
import propmanlab.scenarios.cart.CartEvent.CartRemove;
import propmanlab.scenarios.cart.CartEvent.LoginEvent;
import propmanlab.scenarios.cart.CartEvent.LogoutEvent;
import propmanlab.scenarios.cart.CartEvent.SearchRequest;
import propmanlab.scenarios.cart.CartEvent.SearchResponse;
import propmanlab.source.MultiEventSource;

public class CartEventSource extends MultiEventSource
{
  protected static String[] s_domain = getDomainArray(CartEvent.getDomain());
  
  protected static Set<String> s_itemVars = getItemVars();
  
  protected Set<Integer> m_cartItems;
  
  protected Set<Integer> m_responseItems;
  
  protected boolean m_logged;
  
  protected boolean m_hasCart;
  
  protected String m_lastAction;
  
  protected Picker<Float> m_floatSource;
  
  public CartEventSource(int length, Picker<Float> float_source)
  {
    super(length, s_domain);
    m_floatSource = float_source;
    m_cartItems = new HashSet<Integer>(CartEvent.NUM_ITEMS);
    m_responseItems = new HashSet<Integer>(CartEvent.NUM_ITEMS);
    m_logged = false;
    m_hasCart = false;
    m_lastAction = "";
  }
  
  protected static Set<String> getItemVars()
  {
    Set<String> item_vars = new HashSet<String>(CartEvent.NUM_ITEMS);
    for (int i = 0; i < CartEvent.NUM_ITEMS; i++)
    {
      item_vars.add(Integer.toString(i));
    }
    return item_vars;
  }
  
  /**
   * Converts a set of variable names into an array of variable names
   * @param set The set of variable names
   * @return The array of variable names
   */
  protected static String[] getDomainArray(Set<String> set)
  {
    String[] dom = new String[set.size()];
    int i = 0;
    for (String s : set)
    {
      dom[i++] = s;
    }
    return dom;
  }
  
  @Override
  protected MultiEvent getEvent()
  {
    Set<CartEvent> options = new HashSet<CartEvent>();
    if (!m_logged)
    {
      if (m_lastAction.compareTo(SearchRequest.NAME) == 0)
      {
        options.addAll(getSearchResponses());
      }
      else
      {
        options.add(LoginEvent.instance);
        options.add(SearchRequest.instance);
      }
    }
    else
    {
      // Logged
      if (m_lastAction.compareTo(SearchRequest.NAME) == 0)
      {
        options.addAll(getSearchResponses());
      }
      if (m_lastAction.compareTo(SearchRequest.NAME) != 0)
      {
        if (m_hasCart && m_lastAction.compareTo(SearchResponse.NAME) == 0)
        {
          options.addAll(getCartAdds());
        }
      }
      else
      {
        options.add(LogoutEvent.instance);
        options.add(SearchRequest.instance);
        if (m_hasCart)
        {
          options.addAll(getCartRemoves());
        }
        if (!m_hasCart)
        {
          options.add(CartCreate.instance);
        }
      }
    }
    ElementPicker<CartEvent> picker = new ElementPicker<CartEvent>(m_floatSource);
    float prob = 1 / options.size();
    for (CartEvent e : options)
    {
      picker.add(e, prob);
    }
    CartEvent chosen = picker.pick();
    updateState(chosen);
    return new CartMultiEvent(chosen);
  }
  
  protected Set<CartAdd> getCartAdds()
  {
    Set<CartAdd> set = new HashSet<CartAdd>(m_responseItems.size());
    for (int i : m_responseItems)
    {
      set.add(new CartAdd(i));
    }
    return set;
  }
  
  protected Set<CartRemove> getCartRemoves()
  {
    Set<CartRemove> set = new HashSet<CartRemove>(m_cartItems.size());
    for (int i : m_cartItems)
    {
      set.add(new CartRemove(i));
    }
    return set;
  }
  
  protected Set<SearchResponse> getSearchResponses()
  {
    Set<SearchResponse> set = new HashSet<SearchResponse>();
    ValuationIterator resp_it_1 = new ValuationIterator(s_itemVars);
    while (resp_it_1.hasNext())
    {
      Valuation resp_v_1 = resp_it_1.next();
      Set<Integer> new_response_items = CartLifecycleMonitor.valuationToItems(resp_v_1);
      set.add(new SearchResponse(new_response_items));
    }
    return set;
  }
  
  protected void updateState(CartEvent e)
  {
    m_responseItems.clear();
    if (e instanceof LoginEvent)
    {
      m_logged = true;
    }
    if (e instanceof LogoutEvent)
    {
      m_logged = false;
      m_hasCart = false;
      m_cartItems.clear();
    }
    if (e instanceof CartCreate)
    {
      m_hasCart = true;
    }
    if (e instanceof CartAdd)
    {
      Set<Integer> added_items = CartEvent.itemSet(e.getValuation());
      m_cartItems.addAll(added_items);
    }
    if (e instanceof CartRemove)
    {
      Set<Integer> removed_items = CartEvent.itemSet(e.getValuation());
      m_cartItems.removeAll(removed_items);
    }
    if (e instanceof SearchResponse)
    {
      Set<Integer> response_items = CartEvent.itemSet(e.getValuation());
      m_responseItems.addAll(response_items);
    }
  }

  @Override
  public String getFilename()
  {
    return "cart.txt";
  }

  @Override
  public Processor duplicate(boolean with_state)
  {
    // TODO Auto-generated method stub
    return null;
  }

}
