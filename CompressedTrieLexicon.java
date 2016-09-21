import java.util.*;


public class CompressedTrieLexicon extends TrieLexicon
{
    private List<Node> leaves = new ArrayList<Node>();


    /**
     * Loads the words into the lexicon.
     * @param list of words
     */
    @Override
    public void load( ArrayList<String> list )
    {
        super.load( list );
        compress();
    }


    /**
     * Determines the state of the string.
     * @param s stringbuilder
     * @return not word, word, prefix
     */
    @Override
    public LexStatus wordStatus( StringBuilder s )
    {
        Node t = myRoot;
        for ( int k = 0; k < s.length(); k++ )
        {
            char ch = s.charAt( k );
            t = t.children.get( ch );
            if ( t == null )
                return LexStatus.NOT_WORD; // no path below? done

            String a = t.info;
            String b = s.substring( k );
            if ( a.length() > 1 )
            {
                if ( a.equals( b ) )
                {
                    return LexStatus.WORD;
                }
                else if ( a.contains( b ) )
                {
                    return LexStatus.PREFIX;
                }
                else
                {
                    return LexStatus.NOT_WORD;
                }
            }
        }

        if ( t.isWord )
        {
            return LexStatus.WORD;
        }
        else
        {
            return LexStatus.PREFIX;
        }
    }


    /**
     * Compresses the trie.
     */
    public void compress()
    {
        findLeaves( myRoot );
        for ( Node l : leaves )
        {
            String suffix = "";
            while ( l.parent != null && !l.parent.isWord
                && l.parent.children.size() == 1 )
            {
                suffix = l.info + suffix;
                l = l.parent;
            }

            if ( suffix == "" )
                continue;
            suffix = l.info + suffix;

            Node toAdd = new Node( suffix.charAt( 0 ), l );
            l.isWord = true;
            toAdd.info = suffix;
            l.children.clear();
            l.parent.children.put( suffix.charAt( 0 ), toAdd );
        }
    }


    /**
     * Finds the leaves in the trie.
     * @param n node
     */
    public void findLeaves( Node n )
    {
        if ( n.children.isEmpty() )
        {
            leaves.add( n );
        }
        else
        {
            for ( Node c : n.children.values() )
            {
                findLeaves( c );
            }
        }
    }

}
