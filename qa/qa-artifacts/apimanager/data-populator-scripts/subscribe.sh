SERVER=http://localhost:9765
curl -X POST -c cookies $SERVER/store/site/blocks/user/login/ajax/login.jag -d "action=login&username=subscriber1&password=evanthika123#";
for (i=1; i <= 100; i++)
do
   echo "********Subscriber to API $i********"
curl -X POST -b cookies $SERVER/store/site/blocks/subscription/subscription-add/ajax/subscription-add.jag -d "action=addSubscription&applicationId=1&name=C_API$i&version=1.0.$i&provider=evanProvider&tier=Unlimited";
done
