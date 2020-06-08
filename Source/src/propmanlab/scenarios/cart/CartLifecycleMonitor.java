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

import ca.uqac.lif.cep.propman.PropositionalMachine;
import ca.uqac.lif.cep.propman.Valuation;
import ca.uqac.lif.cep.propman.ValuationIterator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import propmanlab.scenarios.cart.CartEvent.CartAdd;
import propmanlab.scenarios.cart.CartEvent.CartCreate;
import propmanlab.scenarios.cart.CartEvent.CartRemove;
import propmanlab.scenarios.cart.CartEvent.LoginEvent;
import propmanlab.scenarios.cart.CartEvent.LogoutEvent;
import propmanlab.scenarios.cart.CartEvent.SearchRequest;
import propmanlab.scenarios.cart.CartEvent.SearchResponse;

import static ca.uqac.lif.cep.propman.MultiMonitor.EMPTY;
import static ca.uqac.lif.cep.propman.MultiMonitor.EPSILON;

import ca.uqac.lif.cep.ltl.Troolean;

public class CartLifecycleMonitor extends PropositionalMachine
{
  protected int m_stateCount;

  Map<CartState,Integer> map = new HashMap<CartState,Integer>();

  public CartLifecycleMonitor()
  {
    super();

    //int start = 0, sink = 1;
    Set<String> item_vars = new HashSet<String>(CartEvent.NUM_ITEMS);
    for (int i = 0; i < CartEvent.NUM_ITEMS; i++)
    {
      item_vars.add(Integer.toString(i));
    }
    String[] actions = new String[] {"", LoginEvent.NAME, LogoutEvent.NAME, 
        CartCreate.NAME, CartAdd.NAME, CartRemove.NAME, SearchRequest.NAME, SearchResponse.NAME};
    //String[] actions = new String[] {CartAdd.NAME};
    for (boolean logged : new boolean[] {false, true})
    {
      for (boolean has_cart : new boolean[] {false, true})
      {
        for (String last_action : actions)
        {
          ValuationIterator cart_it = new ValuationIterator(item_vars);
          while (cart_it.hasNext())
          {
            Valuation cart_v = cart_it.next();
            Set<Integer> cart_items = valuationToItems(cart_v);
            ValuationIterator resp_it = new ValuationIterator(item_vars);
            while (resp_it.hasNext())
            {
              Valuation resp_v = resp_it.next();
              Set<Integer> response_items = valuationToItems(resp_v);
              int is = getState(map, logged, has_cart, last_action, cart_items, response_items);
              if (is < 0)
              {
                // Invalid combination of value for a state
                continue;
              }
              if (last_action.compareTo("") == 0)
              {
                {
                  // Next: search request
                  int n_cs = getState(map, false, false, SearchRequest.NAME, cart_items, response_items);
                  if (n_cs < 0)
                    continue;
                  addTransition(is, new Transition(n_cs, new CartMultiEvent(SearchRequest.instance), EPSILON));
                }
                {
                  // Next: login
                  int n_cs = getState(map, true, false, LoginEvent.NAME, cart_items, response_items);
                  if (n_cs < 0)
                    continue;
                  addTransition(is, new Transition(n_cs, new CartMultiEvent(LoginEvent.instance), EPSILON));
                }
              }
              else if (last_action.compareTo(LoginEvent.NAME) == 0)
              {
                {
                  // Next: search request
                  int n_cs = getState(map, true, false, SearchRequest.NAME, cart_items, response_items);
                  if (n_cs < 0)
                    continue;
                  addTransition(is, new Transition(n_cs, new CartMultiEvent(SearchRequest.instance), EPSILON));
                }
                {
                  // Next: cart create
                  int n_cs = getState(map, true, true, CartCreate.NAME, cart_items, response_items);
                  if (n_cs < 0)
                    continue;
                  addTransition(is, new Transition(n_cs, new CartMultiEvent(CartCreate.instance), EPSILON));
                }
                {
                  // Next: logout
                  int n_cs = getState(map, false, false, LogoutEvent.NAME, cart_items, response_items);
                  if (n_cs < 0)
                    continue;
                  addTransition(is, new Transition(n_cs, new CartMultiEvent(LogoutEvent.instance), EPSILON));
                }
              }
              else if (last_action.compareTo(LogoutEvent.NAME) == 0)
              {
                {
                  // Next: search request
                  int n_cs = getState(map, logged, false, SearchRequest.NAME, cart_items, response_items);
                  if (n_cs < 0)
                    continue;
                  addTransition(is, new Transition(n_cs, new CartMultiEvent(SearchRequest.instance), EPSILON));
                }
                {
                  // Next: login
                  int n_cs = getState(map, true, false, LoginEvent.NAME, cart_items, response_items);
                  if (n_cs < 0)
                    continue;
                  addTransition(is, new Transition(n_cs, new CartMultiEvent(LoginEvent.instance), EPSILON));
                }
              }
              else if (last_action.compareTo(SearchRequest.NAME) == 0)
              {
                {
                  // Next: search response
                  ValuationIterator resp_it_1 = new ValuationIterator(item_vars);
                  while (resp_it_1.hasNext())
                  {
                    Valuation resp_v_1 = resp_it_1.next();
                    Set<Integer> new_response_items = valuationToItems(resp_v_1);
                    int n_cs = getState(map, logged, has_cart, SearchResponse.NAME, cart_items, new_response_items);
                    if (n_cs < 0)
                      continue;
                    addTransition(is, new Transition(n_cs, new CartMultiEvent(new SearchResponse(new_response_items)), EPSILON));
                  }
                }
              }
              else if (last_action.compareTo(SearchResponse.NAME) == 0)
              {
                {
                  // Next: search request
                  int n_cs = getState(map, logged, has_cart, SearchRequest.NAME, cart_items, null);
                  if (n_cs < 0)
                    continue;
                  addTransition(is, new Transition(n_cs, new CartMultiEvent(SearchRequest.instance), EPSILON));
                }
                if (logged && has_cart)
                {
                  // Next: cart add
                  for (int item : response_items)
                  {
                    Set<Integer> item_to_add = new HashSet<Integer>();
                    item_to_add.add(item);
                    if (cart_items.contains(item)) // Cannot add item already in cart
                      continue;
                    Set<Integer> new_cart_items = new HashSet<Integer>();
                    new_cart_items.addAll(cart_items);
                    new_cart_items.add(item);
                    int n_cs = getState(map, logged, has_cart, CartAdd.NAME, new_cart_items, null);
                    if (n_cs < 0)
                      continue;
                    addTransition(is, new Transition(n_cs, new CartMultiEvent(new CartAdd(item_to_add)), EPSILON));                    
                  }
                  // Next: cart remove
                  for (int item : cart_items)
                  {
                    Set<Integer> item_to_remove = new HashSet<Integer>();
                    item_to_remove.add(item);
                    Set<Integer> new_cart_items = new HashSet<Integer>();
                    new_cart_items.addAll(cart_items);
                    new_cart_items.remove(item);
                    int n_cs = getState(map, logged, has_cart, CartRemove.NAME, new_cart_items, null);
                    if (n_cs < 0)
                      continue;
                    addTransition(is, new Transition(n_cs, new CartMultiEvent(new CartRemove(item_to_remove)), EPSILON));                    
                  }
                }
                if (logged && !has_cart)
                {
                  // Next: cart create
                  int n_cs = getState(map, true, true, CartCreate.NAME, null, null);
                  if (n_cs < 0)
                    continue;
                  addTransition(is, new Transition(n_cs, new CartMultiEvent(CartCreate.instance), EPSILON));
                }
                if (!logged)
                {
                  // Next: login
                  int n_cs = getState(map, true, false, LoginEvent.NAME, cart_items, response_items);
                  if (n_cs < 0)
                    continue;
                  addTransition(is, new Transition(n_cs, new CartMultiEvent(LoginEvent.instance), EPSILON));
                }
                {
                  // Next: logout
                  if (logged)
                  {
                    int n_cs = getState(map, logged, has_cart, LogoutEvent.NAME, null, null);
                    if (n_cs < 0)
                      continue;
                    addTransition(is, new Transition(n_cs, new CartMultiEvent(LogoutEvent.instance), EPSILON));
                  }
                }
              }
              else if (last_action.compareTo(CartCreate.NAME) == 0)
              {
                {
                  // Next: search request
                  int n_cs = getState(map, true, true, SearchRequest.NAME, cart_items, response_items);
                  if (n_cs < 0)
                    continue;
                  addTransition(is, new Transition(n_cs, new CartMultiEvent(SearchRequest.instance), EPSILON));
                }
                {
                  // Next: logout
                  int n_cs = getState(map, false, false, LogoutEvent.NAME, null, null);
                  if (n_cs < 0)
                    continue;
                  addTransition(is, new Transition(n_cs, new CartMultiEvent(LogoutEvent.instance), EPSILON));
                }
              }
              else if (last_action.compareTo(CartAdd.NAME) == 0)
              {
                {
                  // Next: cart remove
                  for (int item : cart_items)
                  {
                    Set<Integer> item_to_remove = new HashSet<Integer>();
                    item_to_remove.add(item);
                    Set<Integer> new_cart_items = new HashSet<Integer>();
                    new_cart_items.addAll(cart_items);
                    new_cart_items.remove(item);
                    int n_cs = getState(map, logged, has_cart, CartRemove.NAME, new_cart_items, null);
                    if (n_cs < 0)
                      continue;
                    addTransition(is, new Transition(n_cs, new CartMultiEvent(new CartRemove(item_to_remove)), EPSILON));                    
                  }
                }
                {
                  // Next: search request
                  int n_cs = getState(map, true, true, SearchRequest.NAME, cart_items, response_items);
                  if (n_cs < 0)
                    continue;
                  addTransition(is, new Transition(n_cs, new CartMultiEvent(SearchRequest.instance), EPSILON));
                }
                {
                  // Next: logout
                  int n_cs = getState(map, false, false, LogoutEvent.NAME, null, null);
                  if (n_cs < 0)
                    continue;
                  addTransition(is, new Transition(n_cs, new CartMultiEvent(LogoutEvent.instance), EPSILON));
                }
              }
              else if (last_action.compareTo(CartRemove.NAME) == 0)
              {
                {
                  // Next: search request
                  int n_cs = getState(map, true, true, SearchRequest.NAME, cart_items, response_items);
                  if (n_cs < 0)
                    continue;
                  addTransition(is, new Transition(n_cs, new CartMultiEvent(SearchRequest.instance), EPSILON));
                }
                {
                  // Next: logout
                  int n_cs = getState(map, false, false, LogoutEvent.NAME, null, null);
                  if (n_cs < 0)
                    continue;
                  addTransition(is, new Transition(n_cs, new CartMultiEvent(LogoutEvent.instance), EPSILON));
                }
              }
            }
          }
        }
      }
    }
    int start = getState(map, false, false, "", null, null);
    setInitialState(start);
    int sink = map.size() + 1;
    for (Map.Entry<CartState,Integer> e : map.entrySet())
    {
      addTransition(e.getValue(), new TransitionOtherwise(sink, EMPTY));
    }
    addTransition(sink, new TransitionOtherwise(sink, EMPTY));
    removeUnreachableStates();
  }

  /**
   * Determines if a state is valid, given its contents
   * @param state The state
   * @return <tt>true</tt> if the state is valid, <tt>false</tt> otherwise
   */
  protected static boolean isValid(CartState state)
  {
    if (!state.m_logged || !state.m_hasCart)
    {
      if (!state.m_cartItems.isEmpty())
      {
        return false;
      }
    }
    if (!state.m_logged && state.m_hasCart)
    {
      return false;
    }
    if (state.m_lastAction.compareTo("") == 0)
    {
      // Initial state: no login and no cart
      if (state.m_logged)
      {
        return false;
      }
      if (state.m_hasCart)
      {
        return false;
      }
      if (!state.m_responseItems.isEmpty())
      {
        return false;
      }
      if (!state.m_cartItems.isEmpty())
      {
        return false;
      }
    }
    else if (state.m_lastAction.compareTo(LoginEvent.NAME) == 0)
    {
      if (!state.m_logged)
      {
        // Just after a login, must be logged
        return false;
      }
      if (state.m_hasCart)
      {
        return false;
      }
      if (!state.m_responseItems.isEmpty())
      {
        return false;
      }
    }
    else if (state.m_lastAction.compareTo(LogoutEvent.NAME) == 0)
    {
      if (state.m_logged)
      {
        // Just after a login, must be logged
        return false;
      }
      if (state.m_hasCart)
      {
        return false;
      }
      if (!state.m_responseItems.isEmpty())
      {
        return false;
      }
      if (!state.m_cartItems.isEmpty())
      {
        return false;
      }
    }
    else if (state.m_lastAction.compareTo(CartRemove.NAME) == 0)
    {
      if (!state.m_logged)
      {
        return false;
      }
      if (!state.m_hasCart)
      {
        return false;
      }
      if (!state.m_responseItems.isEmpty())
      {
        return false;
      }
    }
    else if (state.m_lastAction.compareTo(CartAdd.NAME) == 0)
    {
      if (!state.m_logged)
      {
        return false;
      }
      if (!state.m_hasCart)
      {
        return false;
      }
      if (!state.m_responseItems.isEmpty())
      {
        return false;
      }
      if (state.m_cartItems.isEmpty())
      {
        return false;
      }
    }
    else if (state.m_lastAction.compareTo(CartCreate.NAME) == 0)
    {
      if (!state.m_logged)
      {
        return false;
      }
      if (!state.m_hasCart)
      {
        return false;
      }
      if (!state.m_responseItems.isEmpty())
      {
        return false;
      }
      if (!state.m_cartItems.isEmpty())
      {
        return false;
      }
    }
    else if (state.m_lastAction.compareTo(SearchRequest.NAME) == 0)
    {
      if (!state.m_responseItems.isEmpty())
      {
        return false;
      }
    }
    else if (state.m_lastAction.compareTo(SearchResponse.NAME) == 0)
    {
      // A search response contains at least one item
      if (state.m_responseItems.isEmpty())
      {
        return false;
      }
    }
    return true;
  }

  public static Set<Integer> valuationToItems(Valuation v)
  {
    Set<Integer> set = new HashSet<Integer>();
    for (Map.Entry<String,Troolean.Value> e : v.entrySet())
    {
      if (e.getValue() == Troolean.Value.TRUE)
      {
        set.add(Integer.parseInt(e.getKey()));
      }
    }
    return set;
  }

  /**
   * Gets the number given to a machine state, based on its contents.
   * @param states A map associating states to numerical IDs
   * @param logged Whether the user is logged or not in the current state
   * @param has_cart Whether the user has created a cart or not in the
   * current state
   * @param last_action The name of the last action performed in the machine
   * @param cart_items The contents of the cart in the current state
   * @param response_items The contents of the search response in the current
   * state
   * @return The ID of the state, or -1 if the state is invalid. If the state
   * is valid, as a side effect, the corresponding state
   * is added to <tt>states</tt> and given a unique ID if it was not already
   * present in the map
   * @see #isValid(CartState)
   */
  protected static int getState(Map<CartState,Integer> states, boolean logged, boolean has_cart, String last_action, Set<Integer> cart_items, Set<Integer> response_items)
  {
    CartState cs = new CartState(logged, has_cart, last_action, cart_items, response_items);
    if (!isValid(cs))
    {
      return -1;
    }
    if (!states.containsKey(cs))
    {
      // We add 2 to avoid clashes with the source and sink states,
      // which are given the first two IDs (0 and 1)
      int id = states.size() + 2; 
      states.put(cs, id);
    }
    return states.get(cs);
  }

  protected static class CartState
  {
    /**
     * Whether the user is logged or not
     */
    protected boolean m_logged;

    /**
     * Whether the user has created a cart or not
     */
    protected boolean m_hasCart;

    /**
     * The name of the last action performed
     */
    protected String m_lastAction;

    /**
     * The contents of the cart
     */
    protected Set<Integer> m_cartItems;

    /**
     * The contents of the search response
     */
    protected Set<Integer> m_responseItems;

    /**
     * Creates a new cart state
     * @param logged Whether the user is logged or not
     * @param has_cart Whether the user has created a cart or not
     * @param last_action The name of the last action performed
     * @param cart_items The contents of the cart
     * @param response_items The contents of the search response
     */
    public CartState(boolean logged, boolean has_cart, String last_action, Set<Integer> cart_items, Set<Integer> response_items)
    {
      super();
      m_logged = logged;
      m_hasCart = has_cart;
      m_lastAction = last_action;
      if (cart_items == null)
      {
        m_cartItems = new HashSet<Integer>(CartEvent.NUM_ITEMS);
      }
      else
      {
        m_cartItems = cart_items;
      }
      if (response_items == null)
      {
        m_responseItems = new HashSet<Integer>(CartEvent.NUM_ITEMS);
      }
      else
      {
        m_responseItems = response_items;
      }
    }

    @Override
    public int hashCode()
    {
      return m_lastAction.hashCode() + (m_cartItems.size() + 1) * (m_responseItems.size() + 1);
    }

    @Override
    public boolean equals(Object o)
    {
      if (o == null || !(o instanceof CartState))
      {
        return false;
      }
      CartState s = (CartState) o;
      if (s.m_lastAction.compareTo(m_lastAction) != 0)
      {
        return false;
      }
      if (s.m_logged != m_logged || s.m_hasCart != m_hasCart)
      {
        return false;
      }
      if (m_cartItems.size() != s.m_cartItems.size() || m_responseItems.size() != s.m_responseItems.size())
      {
        return false;
      }
      for (int item : m_cartItems)
      {
        if (!s.m_cartItems.contains(item))
        {
          return false;
        }
      }
      for (int item : m_responseItems)
      {
        if (!s.m_responseItems.contains(item))
        {
          return false;
        }
      }
      return true;
    }

    @Override
    public String toString()
    {
      StringBuilder out = new StringBuilder();
      out.append("<Last: ").append(m_lastAction).append(",");
      out.append("Logged: ").append(m_logged).append(",");
      out.append("Cart: ").append(m_hasCart).append(",");
      out.append("Items: ").append(m_cartItems).append(",");
      out.append("Response: ").append(m_responseItems).append(">");
      return out.toString();
    }
  }
}
