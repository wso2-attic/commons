====================================================================================================
                        Siddhi Complex Event Processing Engine 2.0.0
====================================================================================================

Siddhi CEP 2.0.0 - Release Notes

Date : 25th October 2013

This source code is available at : http://svn.wso2.org/repos/wso2/branches/commons/siddhi/2.0.0/


Siddhi CEP is a lightweight, easy-to-use Open Source Complex Event Processing
Engine (CEP) under  Apache Software License v2.0. Siddhi CEP processes
events which are triggered by various event sources and notifies appropriate complex events
according to the user specified queries.

This project was started as a research project initiated at University of Moratuwa, Sri Lanka,
and now being improved by WSO2 Inc.

Main highlights of this release
===============================

Partitioning support for Siddhi
- This is to support partitioning of CEP queries according to a predefined criteria

Event Tables
- This is to support sending events directly to a database
- Can access event tables from database to perform complex queries

Output Rate Limiting
- Can limit the rate of output for a given query

Features Supported
==================
 - Filter
    - Uses stream handlers to filter events
 - Join
    - Supports only upto two streams at a time
    - Match operation triggering can be configured (making "left" or "right" or both streams to trigger)
 - Aggregation
    - By default shipped with Avg, Sum , Min, Max, Count
    - Supports Custom Aggregations via the plugable architecture
 - Group by
    - Supports Group by based on more than one attribute
    - Supported for all type of queries
 - Having
    - Supported for all type of queries
 - Stream handlers
    - Supports multiple handlers in a row per stream
    - By default shipped with  Filter and Window
    - Default implementations to windows are: Time window, Time Batch window, Length window
    - Supports External Time window which processes a stream according to a specified timestamp attribute of the stream
    - Supports Custom Stream handlers via the plugable architecture
 - Conditions and Expressions
    - Implemented from scratch
    - Mvel2 support removed
    - Conditions supported are: and, or, not, true/false, ==,!=, >=, >, <=, <
    - Expressions supported are: boolean, string, int, long, float, double
 - Pattern processing
    - Identifies pattern occurrences within streams
    - Supports "every" conditions
    - Can process two stream at a time via "and" and "or" conditions (currently only works on two simple streams)
    - Can collect events, with min and max limit, using "collect" condition (currently only works on a simple stream)
 - Sequence processing
    - Identifies continuous sequences with in streams
    - Supports "or" conditions on streams (currently only works on two simple streams)
    - Supports zero to many, one to many, and zero to one  (currently only works on a simple stream)
 - Partitioning
    - Supports partitioning "by variable" and partitioning "by range"
    - Supported for all type of queries
 - Event tables
    - Creates tables in a database to persist events of a stream
    - Can be used as a simple stream to perform supported query operations on event tables
 - Output Rate Limiting
    - Supports limiting output rate based on time, no. of events etc.
    - Can specify whether to send all events, last event or first event
    - Supports getting output as snapshots of the state of a query
 - Query Language
    - Implemented on Antlr
    - Supports Query, Stream Definition and Query Plan compilation
    - Supports Partition Definition and Event Table Definition compilation

System Requirements
===================

1. Minimum memory - 1 GB
2. Processor      - Pentium 800MHz or equivalent at minimum
3. Java SE Development Kit 1.6.0_21 or higher
4. To build Siddhi CEP from the Source distribution, it is necessary that you have
   JDK 1.6 or higher version and Maven 3.0.0 or later


Support
=======

Support is provided by WSO2 Inc. dev@wso2.org


--------------------------------

We welcome your feedback and would love to hear your thoughts on this release of Siddhi.

Siddhi Team


