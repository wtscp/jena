/*
 * (c) Copyright 2001, 2002, 2003, 2004 Hewlett-Packard Development Company, LP
 * [See end of file]
 */

/* Generated By:JJTree: Do not edit this line. Q_URI.java */

package com.hp.hpl.jena.rdql.parser;

import com.hp.hpl.jena.rdql.Query;

public class Q_QuotedURI extends Q_URI {
    // Also supports old-style "quoted qnames" (i.e. qnames inside <>)
    // Old RDQL used to always use quoted items for qnames and full URIs.  

    // The form actually coming from the parser.
    String seen = "" ;

    // This is set false until the Q_URI is transformed into absolute form
    // or it is known to be.

    boolean isAbsolute = false ;

    Q_QuotedURI(int id)
    {
        super(id);
    }

    Q_QuotedURI(RDQLParser p, int id)
    {
        super(p, id);
    }

    void set(String s)
    {
        seen = s ;
    }

    public void jjtClose()
    {
        // Can't convert to absolute form until the entire query is parsed.
        // Don't need to do %-escape processing here because we work in string-space.
        // Easier to work on the version of URIs with the escape sequences still in.
        //seen = processEscapes(seen) ;
        super._setURI(seen);
    }

    public void postParse(Query query)
    {
        super.postParse(query) ;
        if ( ! isAbsolute )
            absolute(query) ;
    }

    static final String prefixOperator = ":" ;

    private void absolute(Query query)
    {
        if ( query == null )
        {
            // Only occurs during testing when we jump straight into the parser.
            isAbsolute = true ;
            return ;
        }
            
        int i = seen.indexOf(prefixOperator) ;
        if ( i < 0 )
        {
            isAbsolute = true ;
            return ;
        }

        String prefix = seen.substring(0,i) ;
        
        String full = query.getPrefix(prefix) ;

        if ( full == null )
        {
            isAbsolute = true ;
            return ;
        }

        String remainder = seen.substring(i+prefixOperator.length()) ;
        super._setURI(full+remainder) ;
        isAbsolute = true ;
    }
    
    public static Q_URI makeURI(String s)
    {
        Q_URI uri = new Q_URI(0) ;
        uri._setURI(s) ;
        return uri ;
    }


    // Override these to retain prefix (old style qnames in <> quotes)
    
    // But be aware of effects on URIs in expressions
    public String asQuotedString()    { return "<"+seen+">" ; }
    public String asUnquotedString()  { return seen ; }
    // Must return the expanded form
    public String valueString()       { return super.getURI() ; }

    // Displyable form
    public String toString() { return seen ; }
}

/*
 *  (c) Copyright 2001, 2002, 2003, 2004 2004 Hewlett-Packard Development Company, LP
 *  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
