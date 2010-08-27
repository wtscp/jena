/*
 * (c) Copyright 2008, 2009 Hewlett-Packard Development Company, LP
 * (c) Copyright 2010 Epimorphics Ltd.
 * All rights reserved.
 * [See end of file]
 */

package com.hp.hpl.jena.sparql.modify.submission;

import com.hp.hpl.jena.sparql.engine.binding.Binding ;
import com.hp.hpl.jena.sparql.modify.GraphStoreBasic ;
import com.hp.hpl.jena.sparql.modify.UpdateProcessorFactory ;
import com.hp.hpl.jena.update.GraphStore ;
import com.hp.hpl.jena.update.UpdateProcessor ;
import com.hp.hpl.jena.update.UpdateRequest ;
import com.hp.hpl.jena.update.UpdateRequestSubmission ;

/** General purpose UpdateProcessor for GraphStore objects (SPARQL/Update) */
// NO LONGER USED.
// WILL BE DELETED
public class UpdateProcessorSubmission implements UpdateProcessor
{
    private GraphStore graphStore ;
    private UpdateRequestSubmission request ;
    private Binding inputBinding ;

    public UpdateProcessorSubmission(GraphStore graphStore, UpdateRequestSubmission request, Binding inputBinding)
    {
        this.graphStore = graphStore ;
        this.request = request ;
        this.inputBinding = inputBinding ;
    }
    
    public void execute()
    {
        graphStore.startRequest() ;
        UpdateVisitorSubmission v = new UpdateProcessorSubmissionVisitor(graphStore, inputBinding) ;
        for ( UpdateSubmission update : request.getUpdates() )
            update.visit(v) ;
        graphStore.finishRequest() ;
    }
    
    public void execute(UpdateSubmission update)
    {
        UpdateVisitorSubmission v = new UpdateProcessorSubmissionVisitor(graphStore, inputBinding) ;
        update.visit(v) ;
    }

    public static UpdateProcessorFactory getFactory() { 
        return new UpdateProcessorFactory()
        {
            public boolean accept(UpdateRequest request, GraphStore graphStore)
            {
                return false &&  (graphStore instanceof GraphStoreBasic) ;
            }
        
            public UpdateProcessor create(UpdateRequestSubmission request, GraphStore graphStore, Binding inputBinding)
            {
                return new UpdateProcessorSubmission(graphStore, request, inputBinding) ;
            }

            public UpdateProcessor create(UpdateRequest request, GraphStore graphStore, Binding inputBinding)
            {
                return null ;
            }
        } ;
    }
}

/*
 * (c) Copyright 2008, 2009 Hewlett-Packard Development Company, LP
 * (c) Copyright 2010 Epimorphics Ltd.
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