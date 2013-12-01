/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.jena.fuseki.servlets;

import org.apache.jena.fuseki.server.DatasetRef ;
import org.apache.jena.fuseki.server.DatasetRegistry ;

/** Operations related to servlets */

public class ActionLib {

    /** Map request to uri in the registry.
     * A possible implementation for mapRequestToDataset(String)
     *  that assumes the form /dataset/service 
     *  Returning null means no mapping found. 
     */
    
    public static String mapRequestToDataset(String uri)
    {
        // Chop off trailing part - the service selector
        // e.g. /dataset/sparql => /dataset 
        int i = uri.lastIndexOf('/') ;
        if ( i == -1 )
            return null ;
        if ( i == 0 )
        {
            // started with '/' - leave.
            return uri ;
        }
        
        return uri.substring(0, i) ;
    }

    public static String mapRequestToService(DatasetRef dsRef, String uri, String datasetURI)
    {
        if ( dsRef == null )
            return "" ;
        if ( dsRef.name.length() >= uri.length() )
            return "" ;
        return uri.substring(dsRef.name.length()+1) ;   // Skip the separating "/"
        
    }
    
    /** Implementation of mapRequestToDataset(String) that looks for
     * the longest match in the registry.
     * This includes use in direct naming GSP. 
     */
    public static String mapRequestToDatasetLongest$(String uri) 
    {
        if ( uri == null )
            return null ;
        
        // This covers local, using the URI as a direct name for
        // a graph, not just using the indirect ?graph= or ?default 
        // forms.

        String ds = null ;
        for ( String ds2 : DatasetRegistry.get().keys() ) {
            if ( ! uri.startsWith(ds2) )
                continue ;

            if ( ds == null )
            {
                ds = ds2 ;
                continue ; 
            }
            if ( ds.length() < ds2.length() )
            {
                ds = ds2 ;
                continue ;
            }
        }
        return ds ;
    }

}
