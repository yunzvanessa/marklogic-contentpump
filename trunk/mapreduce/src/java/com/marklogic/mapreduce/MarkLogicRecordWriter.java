/*
 * Copyright (c) 2003-2011 MarkLogic Corporation. All rights reserved.
 */
package com.marklogic.mapreduce;

import java.io.IOException;
import java.net.URI;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

import com.marklogic.xcc.ContentSource;
import com.marklogic.xcc.Session;
import com.marklogic.xcc.Session.TransactionMode;
import com.marklogic.xcc.exceptions.RequestException;
import com.marklogic.xcc.exceptions.XccConfigException;

/**
 * A RecordWriter that persists MarkLogicRecord to MarkLogic server.
 * 
 * @author jchen
 *
 */
public abstract class MarkLogicRecordWriter<KEYOUT, VALUEOUT>
extends RecordWriter<KEYOUT, VALUEOUT> implements MarkLogicConstants {
	public static final Log LOG =
	    LogFactory.getLog(MarkLogicRecordWriter.class);

	/**
	 * Server URI.
	 */
	private URI serverUri;
	/**
	 * Session to the MarkLogic server.
	 */
	private Session session;
	private int batchSize;
	private int count = 0;
	private Configuration conf;
	
	public MarkLogicRecordWriter(URI serverUri, Configuration conf) {
		this.serverUri = serverUri;
		this.conf = conf;
		this.batchSize = conf.getInt(BATCH_SIZE, DEFAULT_BATCH_SIZE);
	}
	
	@Override
	public void close(TaskAttemptContext context) throws IOException,
			InterruptedException {
		if (session != null) {
			try {
				if (count > 0 && batchSize > 1) {
					session.commit();
				}
	            session.close();
            } catch (RequestException e) {
            	LOG.error(e);
            }
		}
	}
	
	/**
	 * Get the session for this writer.  One writer only maintains one session.
	 * @return Session for this writer.
	 * @throws IOException
	 */
	protected Session getSession() throws IOException {
		if (session == null) {
			getSession(0);
		}
		return session;
	}
	
	/**
	 * Get the session for this writer.  One writer only maintains one session.
	 * If a forest id is specified, this writer only talks to this forest
	 * for the entire task attempt.
	 * @param forestId Id of the targeted forest.
	 * @return Session for this writer.
	 * @throws IOException
	 */
	protected Session getSession(long forestId) throws IOException {
		if (session == null) {
			// start a session
			try {
				ContentSource cs = InternalUtilities.getOutputContentSource(
						conf, serverUri);
				if (forestId > 0) {
					// set forest id if provided
					String forest = "#" + Long.toString(forestId);
					session = cs.newSession(forest);
				} else {
			        session = cs.newSession();
				}
			    if (batchSize > 1) {
			        session.setTransactionMode(TransactionMode.UPDATE);
			    }
			} catch (XccConfigException e) {
				LOG.error("Error creating a new session: ", e);
				throw new IOException(e);
			}    
		}
		return session;
	}
	
	protected void commitIfNecessary() throws RequestException {
		if (++count == batchSize && batchSize > 1) {
			session.commit();
			count = 0;
		}
	}
}
