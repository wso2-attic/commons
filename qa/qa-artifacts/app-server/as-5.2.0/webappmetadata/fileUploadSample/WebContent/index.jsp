<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head><title>File Upload Sample</title></head>

  <body>
  <h2>File upload Test</h2>
  <form action="http://localhost:9763/fileUploadSample/UploadServlet" method="post" enctype="multipart/form-data">
    <input type="file" name="file">
    <input type="text" name="paramName">
    <input type="submit" name="Submit" value="Upload File">
</form>

  </body>
</html>