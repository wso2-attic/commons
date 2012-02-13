package org.wso2.usermanager.verification.email;

public class EmailVerifierConfig {
        
    public final static String PORT = "mail.smtp.port";
    public final static String HOST = "mail.smtp.host";
    public final static String FROM_ADDRESS = "from";
    public final static String USERNAME = "username";
    public final static String PASSWORD = "password";
      
    public final static String DEFAULT_VALUE_SUBJECT = "EmailVerification";
    public final static String DEFAULT_VALUE_MESSAGE = "Please point your browser to : ";
    /**
     * Mail server port
     */
    private int port = 25;
    
    /**
     * Mail server IP or host name
     */
    private String host;
    
    /**
     * The from address
     */
    private String fromAddress = "usermanager@commons.wso2.org";
    
    /**
     * Username of the mail server account
     * This is optional
     */
    private String username;
    
    /**
     * Password of the mail server account
     * This is optional
     */
    private String password;

    private String registrationServiceEPR;
    
    private String subject = DEFAULT_VALUE_SUBJECT;
    
    private String emailBody = DEFAULT_VALUE_MESSAGE;
    
    private String replyTo = null;
    
    private String emailFooter = null;
    
	public String getFromAddress() {
		return fromAddress;
	}

	public String getHost() {
		return host;
	}

	public String getPassword() {
		return password;
	}

	public int getPort() {
		return port;
	}

	public String getUsername() {
		return username;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRegistrationServiceEPR() {
		return registrationServiceEPR;
	}

	public void setRegistrationServiceEPR(String registrationServiceEPR) {
		this.registrationServiceEPR = registrationServiceEPR;
	}

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject.trim();
    }

    public String getEmailBody() {
        return emailBody;
    }

    public void setEmailBody(String emailMessage) {
        this.emailBody = emailMessage.trim();
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public String getEmailFooter() {
        return emailFooter;
    }

    public void setEmailFooter(String emailFooter) {
        this.emailFooter = emailFooter;
    }
    
    

}
