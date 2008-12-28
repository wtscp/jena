/*
 *  (c) Copyright 2001, 2002, 2003, 2004, 2005, 2006, 2007, 2008, 2009 Hewlett-Packard Development Company, LP
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
 *
 * $Id: AnonId.java,v 1.12 2008-12-28 19:31:54 andy_seaborne Exp $
 */

package com.hp.hpl.jena.rdf.model;

import java.rmi.server.UID;

import com.hp.hpl.jena.shared.impl.JenaParameters;

/** Create a new id for an anonymous node.
 *
 * <p>This id is guaranteed to be unique on this machine.</p>
 *
 * @author bwm
 * @version $Name: not supported by cvs2svn $ $Revision: 1.12 $ $Date: 2008-12-28 19:31:54 $
 */

// This version contains experimental modifications by der to 
// switch off normal UID allocation for bNodes to assist tracking
// down apparent non-deterministic behaviour.

public class AnonId extends java.lang.Object {
    
    protected String id = null;

    /** 
        Support for debugging: global anonID counter. The intial value is just to
        make the output look prettier if it has lots (but not lots and lots) of bnodes
        in it.
    */
    private static int idCount = 100000;
    
    public static AnonId create()
        { return new AnonId(); }
    
    public static AnonId create( String id )
        { return new AnonId( id ); }
    
    /** 
        Creates new AnonId. Normally this id is guaranteed to be unique on this 
        machine: it is time-dependant. However, sometimes [incorrect] code is
        sensitive to bnode ordering and produces bizarre bugs (both Dave and
        Chris have been bitten by this, as have some users, I think). Hence the
        disableBNodeUIDGeneration flag, which allows bnode IDs to be predictable.
    */
    public AnonId() {
        if (JenaParameters.disableBNodeUIDGeneration) {
            synchronized (AnonId.class) {
                id = "A" + idCount++; // + rand.nextLong();
            }
        } else {
            id = (new UID()).toString();
        }
    }
    
    /** Create a new AnonId from the string argument supplied
     * @param id A string representation of the id to be created.
     */    
    public AnonId( String id ) {
        this.id = id;
    }
    
    /** Test whether two id's are the same
        @param o the object to be compared
        @return true if and only if the two id's are the same
    */    
    public boolean equals( Object o ) {
        return o instanceof AnonId && id.equals( ((AnonId) o).id );
    }
    
    /** return a string representation of the id
     * @return a string representation of the id
     */    
    public String toString() {
        return id;
    }
    
    /**
        Answer the label string of this AnonId. To be used in preference to
        toString().
    */
    public String getLabelString() {
        return id;
    }
    
    /** return a hashcode for this id
     * @return the hash code
     */    
    public int hashCode() {
        return id.hashCode();
    }
}
