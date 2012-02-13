/* *******************************************************************************************
 * Name	: mashup757.js	
 * Description : Sample service to test MASHUP-757 which excersises XML Lists.
 * Author	: Yumani Ranaweera
 ********************************************************************************************/


function children(){
var e = <employees>
      <employee id="0" ><name>Jim</name><age>25</age></employee>
      <employee id="1" ><name>Joe</name><age>20</age></employee>
      </employees>;
return e.employee[0].children();
}