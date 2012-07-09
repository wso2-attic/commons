====================================================================================================
                        Siddhi Complex Event Processing Engine 1.0.0 M1
====================================================================================================

Siddhi CEP 1.0.0 - Milestone 1 Release Notes

Date : 9th July 2012

Siddhi CEP is a lightweight and easy-to-use Open Source Complex Event Processing
Engine (CEP) under the Apache Software License v2.0. Siddhi CEP processes
events which are triggered by various event sources and notify appropriate complex events
according to user specified queries.

This project was started as a research project initiated at University of Moratuwa, Sri Lanka,
and now improved by WSO2 Inc.

This milestone is available at - http://people.wso2.com/~suho/siddhi/m1/siddhi-1.0.0-SNAPSHOT.zip

Note: This release is NOT compatible with older versions, and its a complete revamp.
 This release can be tested end-end.
 Please check out the samples located in the $SIDDHI_HOME/samples directory to try out its basic features.

Main highlights of this release
===============================

Rewriting Siddhi
- This to support a fully set of CEP fictionalises
- to make its architecture extensible

Defining new Siddhi CEP Language
- Improved logical language to support CEP functions

Features Supported
==================
 - Filter
     of type window and filter
 - Join
    - Only upto two streams at a time
    - Matching stream treating can be configured (making left or right or both to trigger matting operation)
 - Aggregation
    - By default shipped with Avg, Sum , Min, Max
    - Supports Custom Aggregations via the pliable architecture
 - Group by
    - Supports Group by basted on more than one attribute
    - Supported for all type if queries
 - Having
    - Supported for all type if queries
 - Stream handlers
    - Supports multiple filter handlers in a row
    - By default shipped with  Filter and Window
    - Default implementations to windows are Time window, Time Batch window, Length window
    - Supports Custom Stream handlers via the pliable architecture
 - Conditions and Expressions
    - Implemented from scratch
    - Mvel2 support removed
    - Conditions supported and,or,not,true/false, ==,!=, >=, >, <=, <
    - Expressions supported boolean,string,int,long,float,double
 - Query Language
    - Impended on Antlr
    - Supports Query, Stream Definition and Query Plan compilation
 - Pattern processing
    - Identifies pattern occurrences with in steams
    - Supports "every" conditions
    - Can two stream at a time via "and" and "or" conditions, currently only supports two stream competition
    - Can collect events with min and max limit using collect condition
 - Sequence processing
    - Identifies continuous sequence with in steams
    - Supports "or" conditions on streams, currently only supports two stream competition
    - Supports zero to many, one to many, and zero to one


System Requirements
===================

1. Minimum memory - 1 GB
2. Processor      - Pentium 800MHz or equivalent at minimum
3. Java SE Development Kit 1.6.0_21 or higher
4. To build Siddi CEP from the Source distribution, it is necessary that you have
   JDK 1.6 or higher version and Maven 2.1.0 or later


Support
=======

Support is provided via WSO2 Inc.
dev@wso2.org


--------------------------------

We welcome your feedback and would love to hear your thoughts on this release of Siddhi.

Siddhi Team


