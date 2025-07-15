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

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

import com.marklogic.xcc.DocumentFormat;

/**
 * Type of supported document format.
 * 
 * @author jchen
 */
public enum ContentType {
    XML {
        public DocumentFormat getDocumentFormat() {
            return DocumentFormat.XML;
        }

        @Override
        public Class<? extends Writable> getWritableClass() {
            return Text.class;
        }
    },
    JSON {
        public DocumentFormat getDocumentFormat() {
            return DocumentFormat.JSON;
        }

        @Override
        public Class<? extends Writable> getWritableClass() {
            return Text.class;
        }
    },
    TEXT {
        public DocumentFormat getDocumentFormat() {
            return DocumentFormat.TEXT;
        }

        @Override
        public Class<? extends Writable> getWritableClass() {
            return Text.class;
        }
    },
    BINARY {
        public DocumentFormat getDocumentFormat() {
            return DocumentFormat.BINARY;
        }

        @Override
        public Class<? extends Writable> getWritableClass() {
            return BytesWritable.class;
        }
    },
    /**
     * Type to be derived and set from the type of the first value passed
     * to writer.
     */
    UNKNOWN { 
        @Override
        public DocumentFormat getDocumentFormat() {
            return DocumentFormat.NONE;
        }

        @Override
        public Class<? extends Writable> getWritableClass() {
            return BytesWritable.class;
        }       
    },
    /**
     * Content contains mixed type, and the eventual type stored in 
     * MarkLogic Server will be determined by the MIME-type mapping.
     */
    MIXED { 
        @Override
        public DocumentFormat getDocumentFormat() {
            return DocumentFormat.NONE;
        }

        @Override
        public Class<? extends Writable> getWritableClass() {
            return BytesWritable.class;
        }       
    };
    
    public abstract DocumentFormat getDocumentFormat();
    
    public abstract Class<? extends Writable> getWritableClass();
    
    public static ContentType forName(String typeName) {
        if (typeName.equalsIgnoreCase(XML.name())) {
            return XML;
        } else if (typeName.equalsIgnoreCase(JSON.name())) {
            return JSON;
        } else if (typeName.equalsIgnoreCase(TEXT.name())) {
            return TEXT;
        } else if (typeName.equalsIgnoreCase(BINARY.name())) {
            return BINARY;
        } else if (typeName.equalsIgnoreCase(UNKNOWN.name())) {
            return UNKNOWN;
        } else if (typeName.equalsIgnoreCase(MIXED.name())) {
            return MIXED;
        } else {
            throw new IllegalArgumentException("Unknown content type: " + 
                    typeName);
        }
    }

    public static ContentType valueOf(int ordinal) {
        switch (ordinal) {
            case 0: return XML;
            case 1: return JSON;
            case 2: return TEXT;
            case 3: return BINARY;
            case 4: return UNKNOWN;
            case 5: return MIXED;
        }
        return null;
    }
    
    public static ContentType fromFormat(DocumentFormat format) {
        if (format == DocumentFormat.BINARY) {
            return BINARY;
        } else if (format == DocumentFormat.JSON) {
            return JSON;
        } else if (format == DocumentFormat.NONE) {
            return UNKNOWN;
        } else if (format == DocumentFormat.TEXT) {
            return TEXT;
        } else if (format == DocumentFormat.XML) {
            return XML;
        } else {
            throw new IllegalArgumentException("Unknown document format: " + 
                    format);
        }
    }
}
