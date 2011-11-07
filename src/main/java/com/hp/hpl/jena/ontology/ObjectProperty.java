/*
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

// Package
///////////////
package com.hp.hpl.jena.ontology;


// Imports
///////////////


/**
 * <p>
 * Interface encapsulating properties whose range values are restricted to
 * individuals (as distinct from datatype valued {@link DatatypeProperty
 * properties}). Some ontology languages, such as OWL Full, do not distinguish
 * between object and datatype properties.
 * </p>
 *
 * @author Ian Dickinson, HP Labs
 *         (<a  href="mailto:ian_dickinson@users.sourceforge.net" >email</a>)
 * @version CVS $Id: ObjectProperty.java,v 1.2 2009-10-06 13:04:34 ian_dickinson Exp $
 */
public interface ObjectProperty
    extends OntProperty
{
    // Constants
    //////////////////////////////////


    // External signature methods
    //////////////////////////////////


}
