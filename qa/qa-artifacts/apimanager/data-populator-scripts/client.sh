SERVER=http://10.100.3.229:9783
curl -X POST -c cookies $SERVER/publisher/site/blocks/user/login/ajax/login.jag -d 'action=login&username=user1-creator&password=wso2@123';
for ((i=10; i <= 1000; i++))
do
   echo "********Create API $i********"
   curl -X POST -b cookies $SERVER/publisher/site/blocks/item-add/ajax/add.jag -d "action=addAPI&name=C_API$i&version=1.0.$i&description=Customer JAX-RS&endpoint=http://10.100.3.238:9843/jaxrs_basic/services/customers/customerservice&wadl=http://10.100.3.238:9843/jaxrs_basic/services/customers?_wadl&tags=cust,jax-rs,open,social&tier=Unlimited&thumbUrl=https://lh6.ggpht.com/RNc8dD2hXG_rWGzlj09ZwAe1sXVvLWkeYT3ePx7zePCy4ZVV2XMGIxAzup4cKM85NFtL=w124&context=/customerservice$i&tiersCollection=Gold&resourceCount=0&resourceMethod-0=GET&uriTemplate-0=/*";

   echo "********Publish API $i********"
curl -X POST -b cookies $SERVER/publisher/site/blocks/life-cycles/ajax/life-cycles.jag -d "name=C_API$i&version=1.0.$i&provider=user1-creator&status=PUBLISHED&publishToGateway=true&action=updateStatus"
   
done

