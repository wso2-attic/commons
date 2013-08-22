Checkin Client and Checkout Client testing
==========================================

Role -Admin
===========

1. To checkout via admin, use the following command in bin.

sh checkin-client.sh co https://localhost:9443/registry/ -u admin -p admin

2. It will create a folder called _system.

3. To checkin a file you create inside governance, use the following steps.

First to create a file, go in to governance inside _system and,
vi readme_admin

It will create a file named readme_admin.

4. To add it to the repository, (you have to run checkin-client from the bin)

sh ../../checkin-client.sh add readme_admin https://localhost:9443/registry/ -u admin -p admin

or

 sh ../../checkin-client.sh add readme_admin -u admin -p admin

5. To check the status,

 sh ../../checkin-client.sh status

6. To commit the file,

 


For a different tenant
=======================

E.g., Tenant is created as ushani.com
A role inside ushani.com - ushaniR
users inside ushaniR - ushani/pwrd-ushani, ushani1/pwrd-ushani1

1. To checkout via a tenant, use the following command in bin.

sh checkin-client.sh co https://localhost:9443/t/ushani.com/registry -u ushani1	-p ushani1

2. To add a file in to repository,

sh ../../checkin-client.sh add readme_ushani1 

3. To commit.,

sh ../../checkin-client.sh ci -u ushani1 -p ushani1
