/*
 * Copyright (c) 1996-2001
 * Logica Mobile Networks Limited
 * All rights reserved.
 *
 * This software is distributed under Logica Open Source License Version 1.0
 * ("Licence Agreement"). You shall use it and distribute only in accordance
 * with the terms of the License Agreement.
 *
 */
package com.logica.smscsim;

import java.util.Vector;

/**
 * Simple container to hold set of somehow related processors.
 *
 * @author Logica Mobile Networks SMPP Open Source Team
 * @version 1.0, 21 Jun 2001
 * @see PDUProcessor
 */
public class PDUProcessorGroup
{
    private Vector processors = null;
    
    /**
     * Initialises the underlying container.
     */
    public PDUProcessorGroup()
    {
        processors = new Vector();
    }
    
    /**
     * Initialises the underlying container to the given size.
     */
    public PDUProcessorGroup(int initSize)
    {
        processors = new Vector(initSize);
    }
    
    /**
     * Adds single processor to the group.
     * @param p the processor to add
     * @see #remove(PDUProcessor)
     */
    public void add(PDUProcessor p)
    {
        synchronized (processors) {
            if (!processors.contains(p)) {
                processors.add(p);
            }
        }
    }
    
    /**
     * Removes single processor from the group.
     * @param p the processor to remove
     * @see #add(PDUProcessor)
     */
    public void remove(PDUProcessor p)
    {
        synchronized (processors) {
            processors.remove(p);
        }
    }
    
    /**
     * Returns the count of the processors currently in the group.
     * @return current count of processors in the group
     */
    public int count()
    {
        synchronized (processors) {
            return processors.size();
        }
    }
    
    /**
     * Returns <code>i</code>th processor in the group.
     * @param i index of the processor to return
     * @return the processor on the given position
     */
    public PDUProcessor get(int i)
    {
        synchronized (processors) {
            return (PDUProcessor)processors.get(i);
        }
    }

}
