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

package com.hp.hpl.jena.reasoner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Default implementation of ValidityReport which simply stores a list
 * of precomputed Report records.
 * 
 * @author <a href="mailto:der@hplb.hpl.hp.com">Dave Reynolds</a>
 * @version $Revision: 1.1 $ on $Date: 2009-06-29 08:55:50 $
 */
public class StandardValidityReport implements ValidityReport {

    /** The total set of error reports */
    protected List<Report> reports = new ArrayList<Report>();
    
    /** Flag to indicate if there are any error reports so far */
    protected boolean isError;
    
    /**
     * Add a new error report
     * @param error true if the report is an error, false if it is just a warning
     * @param type a string giving a reasoner-dependent classification for the report
     * @param description a textual description of the problem
     */
    public void add(boolean error, String type, String description) {
        add(error, type, description, null);
    }
    
    /**
     * Add a new error report
     * @param error true if the report is an error, false if it is just a warning
     * @param type a string giving a reasoner-dependent classification for the report
     * @param description a textual description of the problem
     * @param extension Optional argument with extension data about the reported error
     */
    public void add(boolean error, String type, String description, Object extension) {
        reports.add(new Report(error, type, description, extension));
        if (error) {
            isError = true;
        }
    }
    
    /**
     * Add a new error report
     * @param report a ValidityReport.Report to add, can be null 
     */
    public void add(ValidityReport.Report report) {
        if (report == null) return;
        reports.add(report);
        if (report.isError) {
            isError = true;
        }
    }
    
    
    /**
     * Returns true if no logical inconsistencies were detected (in which case
     * there will be at least one error Report included). Warnings may still
     * be present. As of Jena 2.2 we regard classes which can't be instantiated
     * as warnings rather than errors. 
     */
    @Override
    public boolean isValid() {
        return !isError;
    }
    
    /**
     * Returns true if the model is both valid (logically consistent) and no
     * warnings were generated. 
     */
    @Override
    public boolean isClean() {
        return reports.isEmpty();
    }
    
    /**
     * Return a count of the number of warning or error reports
     * generated by the validation.
     */
    public int size() {
        return reports.size();
    }
    
    
    /**
     * Return an iterator over the separate ValidityReport.Report records.
     */
    @Override
    public Iterator<Report> getReports() {
        return reports.iterator();
    }


}
