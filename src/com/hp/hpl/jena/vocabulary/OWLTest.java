/******************************************************************
 * File:        OWLTest.java
 * Created by:  Dave Reynolds
 * Created on:  12-Sep-2003
 * 
    (c) Copyright 2002, 2003, 2004, 2005, 2006, 2007, 2008, 2009 Hewlett-Packard Development Company, LP
* [See end of file]
 * $Id: OWLTest.java,v 1.10 2008-12-28 19:32:07 andy_seaborne Exp $
 *****************************************************************/
package com.hp.hpl.jena.vocabulary;

import com.hp.hpl.jena.rdf.model.*;

/**
 * The vocabulary used by the WebOnt working group to define test manifests.
 * <p>
 * Vocabulary definitions from file:data/testOntology.rdf 
 * @author Auto-generated by schemagen on 12 Sep 2003 17:16 
 */
public class OWLTest  {

	/** <p>The RDF model that holds the vocabulary terms</p> */
    private static Model m_model = ModelFactory.createDefaultModel();
    
    /** <p>The namespace of the vocabalary as a string ({@value})</p> */
    public static final String NS = "http://www.w3.org/2002/03owlt/testOntology#";
    
    /** <p>The namespace of the vocabalary as a string</p>
     *  @see #NS */
    public static String getURI() {return NS;}
    
    /** <p>The namespace of the vocabalary as a resource</p> */
    public static final Resource NAMESPACE = m_model.createResource( NS );
    
    /** <p>This property relates a test to a language feature. The language feature is 
     *  usually indicated by a class or property.</p>
     */
    public static final Property feature = m_model.createProperty( "http://www.w3.org/2002/03owlt/testOntology#feature" );
    
    /** <p>The object is a datatype that appears in one of the test files in the subject 
     *  test.</p>
     */
    public static final Property usedDatatype = m_model.createProperty( "http://www.w3.org/2002/03owlt/testOntology#usedDatatype" );
    
    /** <p>The subject test is valid only when the object datatype is included in the 
     *  datatype theory.</p>
     */
    public static final Property supportedDatatype = m_model.createProperty( "http://www.w3.org/2002/03owlt/testOntology#supportedDatatype" );
    
    /** <p>Despite the property URI, the document indicated by this property may or may 
     *  not be imported into the test.</p>
     */
    public static final Property importedPremiseDocument = m_model.createProperty( "http://www.w3.org/2002/03owlt/testOntology#importedPremiseDocument" );
    
    /** <p>Indicates the conformance level of a document or test in the OWL test suite.</p> */
    public static final Property level = m_model.createProperty( "http://www.w3.org/2002/03owlt/testOntology#level" );

	/** <p>One of the conformance levels  in the OWL test suite.</p> */
	public static final Resource Lite = m_model.createResource( "http://www.w3.org/2002/03owlt/testOntology#Lite" );
	/** <p>One of the conformance levels  in the OWL test suite.</p> */
	public static final Resource DL = m_model.createResource( "http://www.w3.org/2002/03owlt/testOntology#DL" );
	/** <p>One of the conformance levels  in the OWL test suite.</p> */
	public static final Resource Full = m_model.createResource( "http://www.w3.org/2002/03owlt/testOntology#Full" );
	
    /** <p>A string valued property that gives a numeral (or some other quasi-numeric 
     *  string) associated with an issue.</p>
     */
    public static final Property issueNumber = m_model.createProperty( "http://www.w3.org/2002/03owlt/testOntology#issueNumber" );
	public static final Property size = m_model.createProperty( "http://www.w3.org/2002/03owlt/testOntology#size" );
	public static final Resource Large = m_model.createProperty( "http://www.w3.org/2002/03owlt/testOntology#Large" );
    
    public static final Resource Test = m_model.createResource( "http://www.w3.org/2002/03owlt/testOntology#Test" );
    
    /** <p>This is a positive entailment test according to the OWL entailment rules.</p> */
    public static final Resource PositiveEntailmentTest = m_model.createResource( "http://www.w3.org/2002/03owlt/testOntology#PositiveEntailmentTest" );
    
    /** <p>This is a negative entailment test according to the OWL entailment rules.</p> */
    public static final Resource NegativeEntailmentTest = m_model.createResource( "http://www.w3.org/2002/03owlt/testOntology#NegativeEntailmentTest" );
    
    /** <p>The conclusions follow from the empty premises.</p> */
    public static final Resource TrueTest = m_model.createResource( "http://www.w3.org/2002/03owlt/testOntology#TrueTest" );
    
    /** <p>Illustrative of the use of OWL to describe OWL Full.</p> */
    public static final Resource OWLforOWLTest = m_model.createResource( "http://www.w3.org/2002/03owlt/testOntology#OWLforOWLTest" );
    
    /** <p>These tests use two documents. One is named importsNNN.rdf, the other is named 
     *  mainNNN.rdf. These tests indicate the interaction between owl:imports and 
     *  the sublanguage levels of the main document.</p>
     */
    public static final Resource ImportLevelTest = m_model.createResource( "http://www.w3.org/2002/03owlt/testOntology#ImportLevelTest" );
    
    /** <p>This is a negative test. The input document contains some use of the OWL namespace 
     *  which is not a feature of OWL. These typically show DAML+OIL features that 
     *  are not being carried forward into OWL.</p>
     */
    public static final Resource NotOwlFeatureTest = m_model.createResource( "http://www.w3.org/2002/03owlt/testOntology#NotOwlFeatureTest" );
    
    /** <p>The premise document, and its imports closure, entails the conclusion document.</p> */
    public static final Resource ImportEntailmentTest = m_model.createResource( "http://www.w3.org/2002/03owlt/testOntology#ImportEntailmentTest" );
    
    /** <p>An inconsistent OWL document. (One that entails falsehood).</p> */
    public static final Resource InconsistencyTest = m_model.createResource( "http://www.w3.org/2002/03owlt/testOntology#InconsistencyTest" );
    
    /** <p>A consistent OWL document. (One that does not entail falsehood).</p> */
    public static final Resource ConsistencyTest = m_model.createResource( "http://www.w3.org/2002/03owlt/testOntology#ConsistencyTest" );
    
    /** <p>A member of this class is an issue in some issue list.</p> */
    public static final Resource Issue = m_model.createResource( "http://www.w3.org/2002/03owlt/testOntology#Issue" );
    
}

/*
   (c) Copyright 2002, 2003, 2004, 2005, 2006, 2007, 2008, 2009 Hewlett-Packard Development Company, LP
    All rights reserved.

    Redistribution and use in source and binary forms, with or without
    modification, are permitted provided that the following conditions
    are met:

    1. Redistributions of source code must retain the above copyright
       notice, this list of conditions and the following disclaimer.

    2. Redistributions in binary form must reproduce the above copyright
       notice, this list of conditions and the following disclaimer in the
       documentation and/or other materials provided with the distribution.

    3. The name of the author may not be used to endorse or promote products
       derived from this software without specific prior written permission.

    THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
    IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
    OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
    IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
    INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
    NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
    DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
    THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
    (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
    THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/