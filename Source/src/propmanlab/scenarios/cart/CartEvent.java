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

import ca.uqac.lif.cep.ltl.Troolean;
import ca.uqac.lif.cep.ltl.Troolean.Value;
import ca.uqac.lif.cep.propman.Valuation;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class CartEvent
{
  /**
   * The number of distinct shopping items modeled by the scenario
   */
  public static final transient int NUM_ITEMS = 1;
  
  /**
   * The valuation represented by this cart event
   */
  protected Valuation m_valuation = new Valuation();
  
  /**
   * A set containing the variable names used to encode a cart event
   */
  protected static final Set<String> s_domain = createDomain();
  
  /**
   * Creates the set of variable names used to encode a cart event
   * @return The set of variable names
   */
  protected static Set<String> createDomain()
  {
    Set<String> domain = new HashSet<String>(NUM_ITEMS + 7);
    domain.add("Login");
    domain.add("Logout");
    domain.add("CartCreate");
    domain.add("CartAdd");
    domain.add("CartRemove");
    domain.add("SearchRequest");
    domain.add("SearchResponse");
    for (int i = 0; i < NUM_ITEMS; i++)
    {
      domain.add("i" + i);
    }
    return domain;
  }
  
  /**
   * Gets a set of items based on the valuation of an event
   * @param v The valuation
   * @return The set of items
   */
  public static Set<Integer> itemSet(Valuation v)
  {
    Set<Integer> items = new HashSet<Integer>(NUM_ITEMS);
    for (int i = 0; i < NUM_ITEMS; i++)
    {
      if (v.get("i" + i) == Troolean.Value.TRUE)
      {
        items.add(i);
      }
    }
    return items;
  }
  
  protected static Set<Integer> toSet(int ... ints)
  {
    Set<Integer> set = new HashSet<Integer>(ints.length);
    for (int i : ints)
    {
      set.add(i);
    }
    return set;
  }
  
  protected static String printItems(Valuation v)
  {
    StringBuilder out = new StringBuilder();
    boolean first = true;
    for (int i = 0; i < NUM_ITEMS; i++)
    {
      if (v.get("i" + i) == Troolean.Value.TRUE)
      {
        if (first)
        {
          first = false;
        }
        else
        {
          out.append(",");
        }
        out.append(i);
      }
    }
    return out.toString();
  }
  
  /**
   * Gets the set of variable names used to encode a cart event
   * @return The set of variable names
   */
  public static Set<String> getDomain()
  {
    return s_domain;
  }
  
  /**
   * Creates a new cart event
   */
  public CartEvent()
  {
    super();
  }
  
  public static void fillValuation(Valuation v, boolean ... items)
  {
    for (int i = 0; i < items.length; i++)
    {
      Troolean.Value val = Troolean.Value.FALSE;
      if (items[i])
      {
        val = Value.TRUE;
      }
      v.put("i" + i, val);
    }
  }
  
  public static void fillValuation(Valuation v, Collection<Integer> items)
  {
    boolean[] b_items = new boolean[NUM_ITEMS];
    for (int i = 0; i < b_items.length; i++)
    {
      b_items[i] = false;
    }
    for (int i : items)
    {
      b_items[i] = true;
    }
    fillValuation(v, b_items);
  }
  
  /**
   * Gets the Boolean valuation that encodes this event
   * @return The valuation
   */
  public final Valuation getValuation()
  {
    return m_valuation;
  }
  
  /**
   * An event representing a user login
   */
  public static class LoginEvent extends CartEvent
  {
    /**
     * The name given to this event
     */
    public static final transient String NAME = "Login";
    
    /**
     * A single visible instance of the event
     */
    public static final transient LoginEvent instance = new LoginEvent();
    
    /**
     * Creates a new login event
     */
    private LoginEvent()
    {
      super();
      m_valuation = new Valuation();
      m_valuation.put(LoginEvent.NAME, Value.TRUE);
      m_valuation.put(LogoutEvent.NAME, Value.FALSE);
      m_valuation.put(SearchRequest.NAME, Value.FALSE);
      m_valuation.put(SearchResponse.NAME, Value.FALSE);
      m_valuation.put(CartCreate.NAME, Value.FALSE);
      m_valuation.put(CartAdd.NAME, Value.FALSE);
      m_valuation.put(CartRemove.NAME, Value.FALSE);
      fillValuation(m_valuation);
    }
    
    @Override
    public boolean equals(Object o)
    {
      if (o == null || !(o instanceof LoginEvent))
      {
        return false;
      }
      return true;
    }
    
    @Override
    public int hashCode()
    {
      return 1;
    }
    
    @Override
    public String toString()
    {
      return NAME;
    }
  }
  
  /**
   * An event representing a user login
   */
  public static class LogoutEvent extends CartEvent
  {
    /**
     * The name given to this event
     */
    public static final transient String NAME = "Logout";
    
    /**
     * A single visible instance of the event
     */
    public static final transient LogoutEvent instance = new LogoutEvent();
    
    /**
     * Creates a new login event
     */
    private LogoutEvent()
    {
      super();
      m_valuation = new Valuation();
      m_valuation.put(LoginEvent.NAME, Value.FALSE);
      m_valuation.put(LogoutEvent.NAME, Value.TRUE);
      m_valuation.put(SearchRequest.NAME, Value.FALSE);
      m_valuation.put(SearchResponse.NAME, Value.FALSE);
      m_valuation.put(CartCreate.NAME, Value.FALSE);
      m_valuation.put(CartAdd.NAME, Value.FALSE);
      m_valuation.put(CartRemove.NAME, Value.FALSE);
      fillValuation(m_valuation);
    }
    
    @Override
    public boolean equals(Object o)
    {
      if (o == null || !(o instanceof LogoutEvent))
      {
        return false;
      }
      return true;
    }
    
    @Override
    public int hashCode()
    {
      return 2;
    }
    
    @Override
    public String toString()
    {
      return NAME;
    }
  }
  
  /**
   * An event representing a search request
   */
  public static class CartCreate extends CartEvent
  {
    /**
     * The name given to this event
     */
    public static final transient String NAME = "CartCreate";
    
    /**
     * A single visible instance of the event
     */
    public static final transient CartCreate instance = new CartCreate();
    
    /**
     * Creates a new search request event
     */
    private CartCreate()
    {
      super();
      m_valuation = new Valuation();
      m_valuation.put(LoginEvent.NAME, Value.FALSE);
      m_valuation.put(LogoutEvent.NAME, Value.FALSE);
      m_valuation.put(SearchRequest.NAME, Value.FALSE);
      m_valuation.put(SearchResponse.NAME, Value.FALSE);
      m_valuation.put(CartCreate.NAME, Value.TRUE);
      m_valuation.put(CartAdd.NAME, Value.FALSE);
      m_valuation.put(CartRemove.NAME, Value.FALSE);
      fillValuation(m_valuation);
    }
    
    @Override
    public boolean equals(Object o)
    {
      if (o == null || !(o instanceof CartCreate))
      {
        return false;
      }
      return true;
    }
    
    @Override
    public int hashCode()
    {
      return 3;
    }
    
    @Override
    public String toString()
    {
      return NAME;
    }
  }
  
  /**
   * An event representing a search request
   */
  public static class SearchRequest extends CartEvent
  {
    /**
     * The name given to this event
     */
    public static final transient String NAME = "SearchRequest";
    
    /**
     * A single visible instance of the event
     */
    public static final transient SearchRequest instance = new SearchRequest();
    
    /**
     * Creates a new search request event
     */
    private SearchRequest()
    {
      super();
      m_valuation = new Valuation();
      m_valuation.put(LoginEvent.NAME, Value.FALSE);
      m_valuation.put(LogoutEvent.NAME, Value.FALSE);
      m_valuation.put(SearchRequest.NAME, Value.TRUE);
      m_valuation.put(SearchResponse.NAME, Value.FALSE);
      m_valuation.put(CartCreate.NAME, Value.FALSE);
      m_valuation.put(CartAdd.NAME, Value.FALSE);
      m_valuation.put(CartRemove.NAME, Value.FALSE);
      fillValuation(m_valuation);
    }
    
    @Override
    public boolean equals(Object o)
    {
      if (o == null || !(o instanceof SearchRequest))
      {
        return false;
      }
      return true;
    }
    
    @Override
    public int hashCode()
    {
      return 4;
    }
    
    @Override
    public String toString()
    {
      return NAME;
    }
  }
  
  /**
   * An event representing a search response
   */
  public static class SearchResponse extends CartEvent
  {
    /**
     * The name given to this event
     */
    public static final transient String NAME = "SearchResponse";
    
    /**
     * Creates a new search response event
     */
    public SearchResponse(Set<Integer> items)
    {
      super();
      m_valuation = new Valuation();
      m_valuation.put(LoginEvent.NAME, Value.FALSE);
      m_valuation.put(LogoutEvent.NAME, Value.FALSE);
      m_valuation.put(SearchRequest.NAME, Value.FALSE);
      m_valuation.put(SearchResponse.NAME, Value.TRUE);
      m_valuation.put(CartCreate.NAME, Value.FALSE);
      m_valuation.put(CartAdd.NAME, Value.FALSE);
      m_valuation.put(CartRemove.NAME, Value.FALSE);
      fillValuation(m_valuation, items);
    }
    
    @Override
    public boolean equals(Object o)
    {
      if (o == null || !(o instanceof SearchResponse))
      {
        return false;
      }
      return m_valuation.equals(((SearchResponse) o).m_valuation);
    }
    
    @Override
    public int hashCode()
    {
      return 5 * m_valuation.hashCode();
    }
    
    @Override
    public String toString()
    {
      StringBuilder out = new StringBuilder();
      out.append(NAME).append(" <").append(printItems(m_valuation)).append(">");
      return out.toString();
    }
  }
  
  /**
   * An event representing a search response
   */
  public static class CartAdd extends CartEvent
  {
    /**
     * The name given to this event
     */
    public static final transient String NAME = "CartAdd";
    
    /**
     * Creates a new cart add event
     */
    public CartAdd(Set<Integer> items)
    {
      super();
      m_valuation = new Valuation();
      m_valuation.put(LoginEvent.NAME, Value.FALSE);
      m_valuation.put(LogoutEvent.NAME, Value.FALSE);
      m_valuation.put(SearchRequest.NAME, Value.FALSE);
      m_valuation.put(SearchResponse.NAME, Value.FALSE);
      m_valuation.put(CartCreate.NAME, Value.FALSE);
      m_valuation.put(CartAdd.NAME, Value.TRUE);
      m_valuation.put(CartRemove.NAME, Value.FALSE);
      fillValuation(m_valuation, items);
    }
    
    /**
     * Creates a new cart add event
     */
    public CartAdd(int ... items)
    {
      this(toSet(items));
    }
    
    @Override
    public boolean equals(Object o)
    {
      if (o == null || !(o instanceof CartAdd))
      {
        return false;
      }
      return m_valuation.equals(((CartAdd) o).m_valuation);
    }
    
    @Override
    public int hashCode()
    {
      return 7 * m_valuation.hashCode();
    }
    
    @Override
    public String toString()
    {
      StringBuilder out = new StringBuilder();
      out.append(NAME).append(" <").append(printItems(m_valuation)).append(">");
      return out.toString();
    }
  }
  
  /**
   * An event representing a search response
   */
  public static class CartRemove extends CartEvent
  {
    /**
     * The name given to this event
     */
    public static final transient String NAME = "CartRemove";
    
    /**
     * Creates a new search response event
     */
    public CartRemove(Set<Integer> items)
    {
      super();
      m_valuation = new Valuation();
      m_valuation.put(LoginEvent.NAME, Value.FALSE);
      m_valuation.put(LogoutEvent.NAME, Value.FALSE);
      m_valuation.put(SearchRequest.NAME, Value.FALSE);
      m_valuation.put(SearchResponse.NAME, Value.FALSE);
      m_valuation.put(CartCreate.NAME, Value.FALSE);
      m_valuation.put(CartAdd.NAME, Value.TRUE);
      m_valuation.put(CartRemove.NAME, Value.FALSE);
      fillValuation(m_valuation, items);
    }
    
    /**
     * Creates a new cart remove event
     */
    public CartRemove(int ... items)
    {
      this(toSet(items));
    }
    
    @Override
    public boolean equals(Object o)
    {
      if (o == null || !(o instanceof CartRemove))
      {
        return false;
      }
      return m_valuation.equals(((CartRemove) o).m_valuation);
    }
    
    @Override
    public int hashCode()
    {
      return 11 * m_valuation.hashCode();
    }
    
    @Override
    public String toString()
    {
      StringBuilder out = new StringBuilder();
      out.append(NAME).append(" <").append(printItems(m_valuation)).append(">");
      return out.toString();
    }
  }
}