SERVER=http://localhost:9765

for ((i=1; i <= 100; i++))
do
   echo "********Adding subscribers$i********"
curl -X POST  $SERVER/store/site/blocks/user/sign-up/ajax/user-add.jag -d"action=addUser&username=subscriber$i&password=evanthika123#"
#curl -X POST  $SERVER/store/site/pages/sign-up.jag -d 'action=addUser&username=subscriber$i&password=evanthika123#'
done
