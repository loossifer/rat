/*
 * Licensed to the Apache Software Foundation (ASF) under one   *
 * or more contributor license agreements.  See the NOTICE file *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The ASF licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 *   http://www.apache.org/licenses/LICENSE-2.0                 *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 */ 
package org.apache.rat.analysis.generation;

import java.util.regex.Pattern;

import org.apache.rat.analysis.IHeaderMatcher;
import org.apache.rat.analysis.RatHeaderAnalysisException;
import org.apache.rat.document.IResource;
import org.apache.rat.report.RatReportFailedException;
import org.apache.rat.report.claim.IClaimReporter;
import org.apache.rat.report.claim.LicenseFamilyCode;
import org.apache.rat.report.claim.impl.LicenseHeaderClaim;

/**
 * JavaDocs are generated and so no license is required.
 * It is useful to not these separately.
 */
public class JavaDocLicenseNotRequired implements IHeaderMatcher {

    private static final String JAVADOC_REGEX_DEFN = ".*Generated by javadoc.*";
    
    private static final Pattern JAVADOC_REGEX = Pattern.compile(JAVADOC_REGEX_DEFN);
    
    public boolean match(IResource subject, String line, IClaimReporter reporter) throws RatHeaderAnalysisException {
        boolean result = JAVADOC_REGEX.matcher(line).matches();
        if (result) {
            reportOnLicense(subject, reporter);
        }
        return result;
    }

    private void reportOnLicense(IResource subject, IClaimReporter reporter) throws RatHeaderAnalysisException {
        try {
            reporter.claim(new LicenseHeaderClaim(subject, LicenseFamilyCode.GENERATED,
                    "JavaDocs are generated and so license header is optional"));
        } catch (RatReportFailedException e) {
            throw new RatHeaderAnalysisException("Cannot write claims", e);
        }
    }
    
    public void reset() {
        // stateless
    }
}
