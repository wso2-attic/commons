In this scenario, a JMS message will be placed by the PurchasingClient.java in a queue with name 'PurchasingProxy'.

Then the PurchasingProxy proxy service will pick up the message and send it to the relevant back end. This is a one way message and therefore, a response will not be received by the client.
