Mahups Performance testing scripts
-----------------------------------

Content
-------

1. Pre-requisites
2. Stepst to recreate
3. Sample graphs



1. Pre-requisites
----------------

1. Download and install WSO2 Mashup Server
2. Download and install jakarta-jmeter


2. Stepst to recreate
--------------------

1. Deploy hi, SearchRank,BlogMetaData,BlogAggregator services which are inside PerformanceTestingScripts folder in to WSO2 Mashup Server's scripts and to the user folder.
2. Start Mashup Server
3. Start jakarta-jmeter
4. Open each .jmx script via jmeter
5. Run each script by changing number of thread group, ramp up time etc.
6. Get recorded the results by one of the listeners as preferred.

 
3. Sample graphs
---------------

There are few sample graphs included in each folder, taken for various number of thread groups.



NOTE:

As service performance is concerned only on try-it function and since try-it page doesn’t detect by jmeter the service is captured as REST invocation.
