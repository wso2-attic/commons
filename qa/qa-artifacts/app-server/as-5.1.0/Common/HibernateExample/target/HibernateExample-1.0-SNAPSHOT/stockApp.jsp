<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
    <BODY>
        <H1>Stock Collector</H1>
        <FORM ACTION="addStock" METHOD="GET">
            Please enter your text:

<table width="400">
  <tr>
    <td>CODE</td>
    <td><input type="text" name="code"></td>
  </tr>
  <tr>
    <td>NAME</td>
    <td><input type="text" name="name"></td>
  </tr>
  
    <tr>
    <td></td>
    <td><INPUT TYPE="SUBMIT" VALUE="Submit"></td>
  </tr>

</table>

        </FORM>

<table border="1" width="400">
<tr>
<th>Code</th>
<th>Name</th>


</tr>
<c:forEach items="${StockList}" var="stock">
<tr>
<td><c:out value="${stock.stockCode}"></c:out>   </td>
<td><c:out value="${stock.stockName}"></c:out></td>
</tr>
</c:forEach>
</table> 
    </BODY>
</html>