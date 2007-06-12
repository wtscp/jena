/*
 * (c) Copyright 2007 Hewlett-Packard Development Company, LP
 * All rights reserved.
 * [See end of file]
 */

package dev.pattern;

import static com.hp.hpl.jena.sdb.util.StrUtils.sqlList;

import java.util.ArrayList;
import java.util.List;

import arq.cmd.CmdUtils;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.sdb.compiler.PatternTable;
import com.hp.hpl.jena.sdb.layout2.hash.LoaderHashLJ;
import com.hp.hpl.jena.sdb.layout2.hash.TupleLoaderOneHash;
import com.hp.hpl.jena.sdb.layout2.index.LoaderIndexLJ;
import com.hp.hpl.jena.sdb.layout2.index.TupleLoaderOneIndex;
import com.hp.hpl.jena.sdb.store.Store;
import com.hp.hpl.jena.sdb.store.StoreFactory;
import com.hp.hpl.jena.sdb.store.TableDesc;
import com.hp.hpl.jena.sdb.store.TupleLoader;
import com.hp.hpl.jena.sparql.sse.SSE;

/// OLD code - to go
public class PatternTableLoader
{
    static { CmdUtils.setLog4j() ; }
    
    public static void main(String...argv)
    {
        boolean reset = true ;
        Store store = StoreFactory.create("sdb.ttl") ;
        if ( reset )
            store.getTableFormatter().create() ;
        PatternTable pTable = new PatternTable("PAT") ;
        
        List<String> colNames = new ArrayList<String>() ;
        colNames.add("col1") ;
        colNames.add("col2") ;
        colNames.add("col3") ;
        
        if ( reset )
        {
            store.getConnection().execSilent("DROP TABLE "+pTable.getTableName()) ;
            List<String> decls = new ArrayList<String>() ;
            for (String x : colNames )
                decls.add(x+" BIGINT") ;
            store.getConnection().execSilent("CREATE TABLE "+pTable.getTableName()+" ("+sqlList(decls)+" )") ;
        }
        
        PatternTableLoader loader = new PatternTableLoader(store, "PAT", colNames) ;
        
        List<Node> row = new ArrayList<Node>() ;
        row.add(Node.createLiteral("5")) ;
        row.add(SSE.parseNode("<http://example.org/>")) ;
        row.add(SSE.parseNode("<http://example.org/ns#>")) ;
        
        loader.prepareRow(row) ;
        System.out.println("**** Finished") ;
    }
    
    // Fake implemenetation.
    // Sometime : convert to a batchloader
    // Better - expose the bulk loader's node management
    // Better, better - a N-wide Node loader.
    
    private TupleLoader nodeControl = null ;

    public PatternTableLoader(Store store, String tableName, List<String> colNames)
    {
        TableDesc tableDesc = new TableDesc(tableName, colNames) ;

        if ( store.getLoader() instanceof LoaderHashLJ )
            nodeControl = new TupleLoaderOneHash(store, tableDesc) ;
        if ( store.getLoader() instanceof LoaderIndexLJ )
            nodeControl = new TupleLoaderOneIndex(store, tableDesc) ;
        if ( nodeControl == null )
        {
            System.err.println("Can't make TupleLoader") ;
            System.exit(1) ;
        }
    }
    
    public void prepareRow(List<Node> row)
    {
        nodeControl.start() ;
        nodeControl.load(row.toArray(new Node[0])) ;
        nodeControl.finish();
    }

}

/*
 * (c) Copyright 2007 Hewlett-Packard Development Company, LP
 * All rights reserved.
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