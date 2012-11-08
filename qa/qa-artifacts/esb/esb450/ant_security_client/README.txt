========================================================================================================================
Security client for invoking a secured service in appserver or esb or similar service hosting product.
========================================================================================================================

You have to add the service.jks inside sample_keys directory into your server's Key Stores and use it to secure the serv
ices
The relevant keystore for client side is client.jks and it is used inside the code.
If you want to use your own keystores you have to import those keys to both of these keystores or set the custom keystor
s in the code.

How to Run
----------

Run ant from this directory.
The project will build and ask you for the security scenario number you want to try.
Enter the number and press enter. The scenario will be executed. 
