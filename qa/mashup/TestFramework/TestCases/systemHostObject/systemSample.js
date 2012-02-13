system.include("concatscript.js");
function add(first, last) {
	print(system.localhostname);
	//waits for 1 second
	system.wait(1000);
	// concat is a function in the included concatscript.js javascript
	return concat(first,last);
}