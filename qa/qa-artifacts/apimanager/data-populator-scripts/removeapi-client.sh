SERVER=http://10.100.3.229:9783
curl -X POST -c cookies $SERVER/publisher/site/blocks/user/login/ajax/login.jag -d 'action=login&username=user1-creator&password=wso2@123';
for ((i=1; i <= 2500; i++))
do
   echo "********Delete API $i********"
 
curl -X POST -b cookies  $SERVER/publisher/site/blocks/item-add/ajax/remove.jag -d "action=removeAPI&name=C_API$i&version=1.0.$i&provider=user1-creator";


   
done

