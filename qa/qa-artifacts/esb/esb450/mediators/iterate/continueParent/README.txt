1. Use the given synapse.xml
2. Send a messages and if continueParent=true, the following two logs should be printed

**************CONTINUE PARENT OF ITERATE #2 (LEAF NODE) - three times since three child messages are created in first iteration
 **************CONTINUE PARENT OF ITERATE #1 (TOP LEVEL)********* - only once

3. Then set continueParent=false and send a message. Both above messages should not be printed.
