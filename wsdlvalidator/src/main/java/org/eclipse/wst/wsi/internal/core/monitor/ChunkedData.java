/*******************************************************************************
 * Copyright (c) 2002-2005 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsi.internal.core.monitor;

/**
 * This class will process chunked data.
 * 
 * @author Peter  Brittenham (peterbr@us.ibm.com)
 * @version 1.0.1
 */
public class ChunkedData
{
  protected boolean moreChunkedData = false;
  protected String chunkedData = "";
  protected SocketHandler socketHandler = null;

  /**
   * Constructs a ChunkedData object.
   */
  public ChunkedData()
  {
  }

  /**
   * Constructs a ChunkedData object.
   * @param socketHandler socket handler.
   * @param moreChunkedData chunked data.
   */
  public ChunkedData(SocketHandler socketHandler, boolean moreChunkedData)
  {
    this.socketHandler = socketHandler;
    this.moreChunkedData = moreChunkedData;
  }

  /**
   * Constructs a ChunkedData object.
   * @param socketHandler socket handler.
   * @param chunkedData chunked data.
   */
  public ChunkedData(SocketHandler socketHandler, String chunkedData)
  {
    this.socketHandler = socketHandler;
    this.chunkedData = chunkedData;
    this.moreChunkedData = checkData(chunkedData);
  }

  /**
   * Add the chunked data to the buffer.
   * @param chunkedData chunked data.
   */
  public void addData(String chunkedData)
  {
    this.chunkedData += chunkedData;
    this.moreChunkedData = checkData(chunkedData);
  }

  /**
   * Get data buffer.
   * @return  data buffer.
   */
  public String getData()
  {
    return this.chunkedData;
  }

  /**
   * Clear data buffer.
   */
  public void clearData()
  {
    this.chunkedData = "";
  }

  /**
   * Returns true if there is more chunked data.
   * @return true if there is more chunked data.
   */
  public boolean isMoreChunkedData()
  {
    return this.moreChunkedData;
  }

  /**
   * Decode and add data to the buffer.
   * @param messageBuffer a StringBuffer object.
   * @return true if data is added to the buffer.
   */
  public boolean decodeAndAddDataToBuffer(StringBuffer messageBuffer)
  {
    boolean moreChunkedData = false;
    if (!this.moreChunkedData)
    {
      // DEBUG:
      debug("decodeAndAddDataToBuffer", "chunkedData: " + chunkedData);

      moreChunkedData = decodeData(chunkedData, messageBuffer);
    }

    return moreChunkedData;
  }

  /**
   * Check data.
   */
  private boolean checkData(String chunkedData)
  {
    boolean moreChunkedData = false;
    // Look for [CRLF][0][CRLF] in the chunked data      
    if (chunkedData.indexOf(SocketHandler.CRLF + "0" + SocketHandler.CRLF) != -1)
    {
      moreChunkedData = false;

      // DEBUG:
      debug(
        "checkData",
        "Chunk contains [CRLF][0][CRLF], so there is no more data.");
    }

    // Look for [0][CRLF] at start of chunked data      
    else if (chunkedData.startsWith("0" + SocketHandler.CRLF))
    {
      moreChunkedData = false;

      // DEBUG:
      debug(
        "checkData",
        "Chunk data starts with [0][CRLF], so there is no more data.");
    }

    // Otherwise step through it to see if we have all of the data
    else
    {
      moreChunkedData = true;
    }

    return moreChunkedData;
  }

  /**
   * Decode chunked data.
   * 
   * Data format:
   * <pre>
   *   [Chunk Size][CRLF][Data Chunk][CRLF][Chunk Size][CRLF][Data Chunk][CRLF]...[0][CRLF][Optional Footer][CRLF]
   * </pre>
   * 
   * @param chunkedData
   * @param messageLoggingBuffer
   */
  private boolean decodeData(String chunkedData, StringBuffer messageBuffer)
  {
    boolean endOfData = false;
    int nextIndex = 0;
    int prevIndex = 0;
    int chunkSize = 0;

    String nextChunkData = null;

    // Process each chunk of data
    while (!(endOfData)
      && (nextIndex < chunkedData.length())
      && (nextIndex = chunkedData.indexOf(SocketHandler.CRLF, prevIndex)) != -1)
    {
      // Get the data length which is right after this
      String hexChunkedSize = chunkedData.substring(prevIndex, nextIndex);

      // Decode length
      chunkSize = Integer.decode("0x" + hexChunkedSize).intValue();

      // DEBUG:
      debug("decodeData", "chunkedSize: " + chunkSize);

      // If size is zero, then we have hit the end of the chunked data
      if (chunkSize == 0)
      {
        endOfData = true;
      }

      // If we don't have all of the data then stop
      else if (nextIndex + 2 + chunkSize > chunkedData.length())
      {
        break;
      }

      else
      {
        nextChunkData =
          chunkedData.substring(nextIndex + 2, nextIndex + 2 + chunkSize);

        // DEBUG:
        debug("decodeData", "nextChunkData: " + nextChunkData);

        // Get message based on length
        if (messageBuffer != null)
          messageBuffer.append(nextChunkData);
      }

      // Point at the next chunk size
      prevIndex = nextIndex + 2 + chunkSize + 2;
    }

    return endOfData;
  }

  /**
   * Debug.
   */
  private void debug(String method, String message)
  {
    this.socketHandler.debug("ChunkedData", method, message);
  }

}
