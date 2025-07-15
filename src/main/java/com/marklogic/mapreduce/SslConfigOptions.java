/*
 * Copyright (c) 2011-2020 Progress Software Corporation and/or its subsidiaries or affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.marklogic.mapreduce;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

/**
 * Interface used to get options for SSL connection.
 * 
 * <p>
 *   You must implement this interface to use a secure connection to
 *   your input or output MarkLogic Server instance, and then supply
 *   the class as the value of the configuration property
 *   {@link MarkLogicConstants#INPUT_SSL_OPTIONS_CLASS input.usessloptionsclass}
 *   or {@link MarkLogicConstants#OUTPUT_SSL_OPTIONS_CLASS output.usessloptionsclass}.
 * </p>
 * <p>
 *   For details, see the <em>Hadoop MapReduce Connector Developer's Guide</em>.
 *   For an example, see {@link com.marklogic.mapreduce.examples.ContentReader}.
 * </p>
 * 
 * @see MarkLogicConstants
 * @see com.marklogic.mapreduce.examples.ContentReader
 * 
 * @author jchen
 */
public interface SslConfigOptions {
    
    public SSLContext getSslContext() 
        throws NoSuchAlgorithmException, KeyManagementException;
    
    public String[] getEnabledProtocols();
    
    public String[] getEnabledCipherSuites();
}
