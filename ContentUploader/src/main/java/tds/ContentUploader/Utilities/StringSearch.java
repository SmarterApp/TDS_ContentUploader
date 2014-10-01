/*******************************************************************************
 * Educational Online Test Delivery System 
 * Copyright (c) 2014 American Institutes for Research
 *     
 * Distributed under the AIR Open Source License, Version 1.0 
 * See accompanying file AIR-License-1_0.txt or at
 * http://www.smarterapp.org/documents/American_Institutes_for_Research_Open_Source_Software_License.pdf
 ******************************************************************************/
package tds.ContentUploader.Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Aho-Corasick text search algorithm implementation
 * 
 * For more information visit
 *    - http://www.cs.uku.fi/~kilpelai/BSA05/lectures/slides04.pdf
 *    
 * Class for searching string for one or multiple 
 * keywords using efficient Aho-Corasick search algorithm
 * 
 * Class currently not used and not tested
 *  
 * @author jmambo
 *
 */
public class StringSearch implements IStringSearchAlgorithm
{

  /**
   * Root of keyword tree
   */
  private TreeNode _root;

  /**
   * Keywords to search for
   */
  private String[] _keywords;

  /**
   * Initialize search algorithm with no keywords (Use Keywords property)
   */
  public StringSearch () {
  }

  /**
   * Initialize search algorithm (Build keyword tree)
   * 
   * @param keywords
   *          Keywords to search for
   */
  public StringSearch (String[] keywords) {
    _keywords = keywords;
  }

  /**
   * Build tree from specified keywords
   */
  void buildTree ()
  {
    // Build keyword tree and transition function
    _root = new TreeNode (null, ' ');
    for (String p : _keywords)
    {
      // add pattern to tree
      TreeNode nd = _root;
      int pLength = p.length ();
      for (int i = 0; i < pLength; i++)
      {
        char c = p.charAt (i);

        TreeNode ndNew = null;
        for (TreeNode trans : nd.getTransitions ())
          if (trans.getChar () == c) {
            ndNew = trans;
            break;
          }

        if (ndNew == null)
        {
          ndNew = new TreeNode (nd, c);
          nd.addTransition (ndNew);
        }
        nd = ndNew;
      }
      nd.addResult (p);
    }

    // Find failure functions
    List<TreeNode> nodes = new ArrayList<> ();
    // level 1 nodes - fail to root node
    for (TreeNode nd : _root.getTransitions ())
    {
      nd.setFailure (_root);
      for (TreeNode trans : nd.getTransitions ())
        nodes.add (trans);
    }
    // other nodes - using BFS
    while (nodes.size () != 0)
    {
      List<TreeNode> newNodes = new ArrayList<> ();
      for (TreeNode nd : nodes)
      {
        TreeNode r = nd.getParent ().getFailure ();
        char c = nd.getChar ();

        while (r != null && !r.containsTransition (c)) {
          r = r.getFailure ();
        }
        if (r == null) {
          nd.setFailure (_root);
        }

        else
        {
          nd.setFailure (r.getTransition (c));
          for (String result : nd.getFailure ().getResults ())
            nd.addResult (result);
        }

        // add child nodes to BFS list
        for (TreeNode child : nd.getTransitions ())
          newNodes.add (child);
      }
      nodes = newNodes;
    }
    _root.setFailure (_root);
  }

  @Override
  public String[] getKeywords () {
    return _keywords;
  }

  @Override
  public void setKeywords (String[] value) {
    _keywords = value;
    buildTree ();
  }

  @Override
  public StringSearchResult[] findAll (String text) {
    List<StringSearchResult> ret = new ArrayList<> ();
    TreeNode ptr = _root;
    int index = 0;

    while (index < text.length ())
    {
      TreeNode trans = null;
      while (trans == null)
      {
        trans = ptr.getTransition (text.charAt (index));
        if (ptr == _root) {
          break;
        }
        if (trans == null) {
          ptr = ptr.getFailure ();
        }
      }
      if (trans != null) {
        ptr = trans;
      }

      for (String found : ptr.getResults ())
        ret.add (new StringSearchResult (index - found.length () + 1, found));
      index++;
    }
    return ret.toArray (new StringSearchResult[ret.size ()]);
  }

  @Override
  public StringSearchResult findFirst (String text) {

    // List ret=new ArrayList();
    TreeNode ptr = _root;
    int index = 0;
    while (index < text.length ()) {

    }
    while (index < text.length ()) {
      TreeNode trans = null;
      while (trans == null) {
        trans = ptr.getTransition (text.charAt (index));
        if (ptr == _root) {
          break;
        }
        if (trans == null) {
          ptr = ptr.getFailure ();
        }
      }
      if (trans != null) {
        ptr = trans;
      }

      for (String found : ptr.getResults ())
        return new StringSearchResult (index - found.length () + 1, found);
      index++;
    }
    return StringSearchResult.empty ();
  }

  @Override
  public boolean containsAny (String text) {
    TreeNode ptr = _root;
    int index = 0;

    while (index < text.length ())
    {
      TreeNode trans = null;
      while (trans == null)
      {
        trans = ptr.getTransition (text.charAt (index));
        if (ptr == _root)
          break;
        if (trans == null) {
          ptr = ptr.getFailure ();
        }
      }
      if (trans != null)
        ptr = trans;

      if (ptr.getResults ().length > 0) {
        return true;
      }
      index++;
    }
    return false;
  }

}

/**
 * Tree node representing character and its transition and failure function
 */
class TreeNode
{

  private char                     _char;
  private TreeNode                 _parent;
  private TreeNode                 _failure;
  private List<String>             _results;
  private TreeNode[]               _transitionsArray;
  private String[]                 _resultsArray;
  private Map<Character, TreeNode> _transHash;

  /**
   * Initialize tree node with specified character
   * 
   * @param parent
   *          Parent node
   * @param c
   *          a character
   */
  public TreeNode (TreeNode parent, char c) {
    _char = c;
    _parent = parent;
    _results = new ArrayList<> ();
    _resultsArray = new String[] {};

    _transitionsArray = new TreeNode[] {};
    _transHash = new HashMap<> ();
  }

  /**
   * Adds pattern ending in this node
   * 
   * @param result
   *          Pattern
   */
  public void addResult (String result) {
    if (_results.contains (result))
      return;
    _results.add (result);
    _resultsArray = _results.toArray (new String[_results.size ()]);
  }

  /**
   * Adds transition node
   * 
   * @param node
   */
  public void addTransition (TreeNode node) {
    _transHash.put (node.getChar (), node);
    TreeNode[] treeNodeArray = _transHash.values ().toArray (new TreeNode[_transHash.size ()]);
    _transitionsArray = treeNodeArray;
  }

  /**
   * Returns transition to specified character (if exists)
   * 
   * @param c
   *          a character
   * @return TreeNode or null
   */
  public TreeNode getTransition (char c) {
    return _transHash.get (c);
  }

  /**
   * Returns true if node contains transition to specified character
   * 
   * @param c
   *          a character
   * @return True if transition exists
   */
  public boolean containsTransition (char c) {
    return getTransition (c) != null;
  }

  /**
   * Get a character
   * 
   * @return a character
   */
  public char getChar () {
    return _char;
  }

  /**
   * Parent tree node
   * 
   * @return TreeNode
   */
  public TreeNode getParent () {
    return _parent;
  }

  /**
   * Failure function - descendant node
   * 
   * @return TreeNode
   */
  public TreeNode getFailure () {
    return _failure;
  }

  public void setFailure (TreeNode failure) {
    _failure = failure;
  }

  /**
   * Transition function - list of descendant nodes
   * 
   * @return TreeNode array
   */
  public TreeNode[] getTransitions () {
    return _transitionsArray;
  }

  /**
   * Returns list of patterns ending by this letter
   * 
   * @return results array
   */
  public String[] getResults () {
    return _resultsArray;
  }

}
