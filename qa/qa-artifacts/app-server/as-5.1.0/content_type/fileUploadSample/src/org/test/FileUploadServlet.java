package org.test;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class FileUploadServlet extends HttpServlet
{
  private boolean isMultipart;
  private String filePath;
  private int maxFileSize = 51200;
  private int maxMemSize = 4096;
  private File file;

  public void init()
  {
    this.filePath = getServletContext().getInitParameter("file-upload");
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    this.isMultipart = ServletFileUpload.isMultipartContent(request);
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    if (!this.isMultipart) {
      out.println("<html>");
      out.println("<head>");
      out.println("<title>Servlet upload</title>");
      out.println("</head>");
      out.println("<body>");
      out.println("<p>No file uploaded</p>");
      out.println("</body>");
      out.println("</html>");
      return;
    }
    DiskFileItemFactory factory = new DiskFileItemFactory();

    factory.setSizeThreshold(this.maxMemSize);

    factory.setRepository(new File("/var/tmp/"));

    ServletFileUpload upload = new ServletFileUpload(factory);

    upload.setSizeMax(this.maxFileSize);
    try
    {
      List fileItems = upload.parseRequest(request);

      Iterator i = fileItems.iterator();

      out.println("<html>");
      out.println("<head>");
      out.println("<title>Servlet upload</title>");
      out.println("</head>");
      out.println("<body>");
      while (i.hasNext())
      {
        FileItem fi = (FileItem)i.next();
        if (!fi.isFormField())
        {
          String fieldName = fi.getFieldName();
          String fileName = fi.getName();
          String contentType = fi.getContentType();
          boolean isInMemory = fi.isInMemory();
          long sizeInBytes = fi.getSize();

          if (fileName.lastIndexOf("//") >= 0) {
            this.file = new File(this.filePath + fileName.substring(fileName.lastIndexOf("//")));
          }
          else {
            this.file = new File(this.filePath + fileName.substring(fileName.lastIndexOf("//") + 1));
          }

          fi.write(this.file);
          out.println("Uploaded Filename: " + fileName + "<br>");
        }
      }
      out.println("</body>");
      out.println("</html>");
    } catch (Exception ex) {
      System.out.println(ex);
    }
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    throw new ServletException("GET method used with " + getClass().getName() + ": POST method required.");
  }
}