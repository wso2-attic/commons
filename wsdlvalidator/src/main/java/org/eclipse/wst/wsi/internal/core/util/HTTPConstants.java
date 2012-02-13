/*******************************************************************************
 * Copyright (c) 2002-2005 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.wsi.internal.core.util;

import java.util.Arrays;
import java.util.List;

/**
 * A set of HTTP values that are defined by Network Working Group.
 * http://www.ietf.org/
 *
 * @version 1.0
 * @author Ilya Kanonirov (kio@isg.axmor.com)
 */
public class HTTPConstants {
  // The standardized HTTP Header field-names
  // http://www.mnot.net/drafts/draft-nottingham-http-header-reg-00.txt

  // Hypertext Transfer Protocol -- HTTP/1.1 (obsoletes RFC2068)
  public static final String HEADER_ACCEPT = "Accept";
  public static final String HEADER_ACCEPT_CHARSET = "Accept-Charset";
  public static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
  public static final String HEADER_ACCEPT_LANGUAGE = "Accept-Language";
  public static final String HEADER_ACCEPT_RANGES = "Accept-Ranges";
  public static final String HEADER_AGE = "Age";
  public static final String HEADER_ALLOW = "Allow";
  public static final String HEADER_AUTHORIZATION = "Authorization";
  public static final String HEADER_CACHE_CONTROL = "Cache-Control";
  public static final String HEADER_CONNECT = "Connect";
  public static final String HEADER_CONTENT_ENCODING = "Content-Encoding";
  public static final String HEADER_CONTENT_LANGUAGE = "Content-Language";
  public static final String HEADER_CONTENT_LENGTH = "Content-Length";
  public static final String HEADER_CONTENT_LOCATION = "Content-Location";
  public static final String HEADER_CONTENT_MD5 = "Content-MD5";
  public static final String HEADER_CONTENT_RANGE = "Content-Range";
  public static final String HEADER_CONTENT_TYPE = "Content-Type";
  public static final String HEADER_DATE = "Date";
  public static final String HEADER_ETAG = "ETag";
  public static final String HEADER_EXPECT = "Expect";
  public static final String HEADER_EXPIRES = "Expires";
  public static final String HEADER_FROM = "From";
  public static final String HEADER_HOST = "Host";
  public static final String HEADER_IF_MATCH = "If-Match";
  public static final String HEADER_IF_MODIFIED_SINCE = "If-Modified-Since";
  public static final String HEADER_IF_NONE_MATCH = "If-None-Match";
  public static final String HEADER_IF_RANGE = "If-Range";
  public static final String HEADER_IF_UNMODIFIED_SINCE = "If-Unmodified-Since";
  public static final String HEADER_LAST_MODIFIED = "Last-Modified";
  public static final String HEADER_LOCATION = "Location";
  public static final String HEADER_MAX_FORWARDS = "Max-Forwards";
  public static final String HEADER_PRAGMA = "Pragma";
  public static final String HEADER_PROXY_AUTHENTICATE = "Proxy-Authenticate";
  public static final String HEADER_PROXY_AUTHORIZATION = "Proxy-Authorization";
  public static final String HEADER_RANGE = "Range";
  public static final String HEADER_REFERER = "Referer";
  public static final String HEADER_RETRY_AFTER = "Retry-After";
  public static final String HEADER_SERVER = "Server";
  public static final String HEADER_TE = "TE";
  public static final String HEADER_TRAILER = "Trailer";
  public static final String HEADER_TRANSFER_ENCODING = "Transfer-Encoding";
  public static final String HEADER_UPGRADE = "Upgrade";
  public static final String HEADER_USER_AGENT = "User-Agent";
  public static final String HEADER_VARY = "Vary";
  public static final String HEADER_VIA = "Via";
  public static final String HEADER_WARNING = "Warning";
  public static final String HEADER_WWW_AUTHENTICATE = "WWW-Authenticate";
  public static final String HEADER_MIME_VERSION = "MIME-Version";
  public static final String HEADER_CONTENT_DISPOSITION = "Content-Disposition";  

  // HTTP Authentication: Basic and Digest Access Authentication
  public static final String HEADER_AUTHENTICATION_INFO = "Authentication-Info";

  // HTTP State Management Mechanism
  public static final String HEADER_SET_COOKIE = "Set-Cookie";

  // HTTP State Management Mechanism (obsoletes RFC2109)
  public static final String HEADER_COOKIE = "Cookie";
  public static final String HEADER_COOKIE2 = "Cookie2";
  public static final String HEADER_SET_COOKIE2 = "Set-Cookie2";

  // Web Distributed Authoring and Versioning
  public static final String HEADER_DAV = "DAV";
  public static final String HEADER_DEPTH = "Depth";
  public static final String HEADER_DESTINATION = "Destination";
  public static final String HEADER_IF = "If";
  public static final String HEADER_LOCK_TOKEN = "Lock-Token";
  public static final String HEADER_OVERWRITE = "Overwrite";
  public static final String HEADER_STATUS_URI = "Status-URI";
  public static final String HEADER_TIMEOUT = "Timeout";

  // Hypertext Transfer Protocol -- HTTP/1.1 (Proposed Standard - these field-names are now considered obsolete)
  public static final String HEADER_CONTENT_BASE = "Content-Base";
  public static final String HEADER_PUBLIC = "Public";
  public static final String HEADER_CONTENT_VERSION = "Content-Version";
  public static final String HEADER_DERIVED_FROM = "Derived-From";
  public static final String HEADER_LINK = "Link";
  public static final String HEADER_URI = "URI";
  public static final String HEADER_KEEP_ALIVE = "Keep-Alive";

  // Delta Encoding in HTTP
  public static final String HEADER_A_IM = "A-IM";
  public static final String HEADER_DELTA_BASE = "Delta-Base";
  public static final String HEADER_IM = "IM";

  // Instance Digests in HTTP
  public static final String HEADER_DIGEST = "Digest";
  public static final String HEADER_WANT_DIGEST = "Want-Digest";

  // Simple Hit-Metering and Usage-Limiting for HTTP
  public static final String HEADER_METER = "Meter";

  // The Known Non-Standardized HTTP Header field-names

  // Transparent Content Negotiation in HTTP
  public static final String HEADER_ACCEPT_FEATURES = "Accept-Features";
  public static final String HEADER_ALTERNATES = "Alternates";
  public static final String HEADER_NEGOTIATE = "Negotiate";
  public static final String HEADER_TCN = "TCN";
  public static final String HEADER_VARIANT_VARY = "Variant-Vary";

  // The Safe Response Header Field
  public static final String HEADER_SAFE = "Safe";

  // Hyper Text Coffee Pot Control Protocol (HTCPCP/1.0)
  public static final String HEADER_ACCEPT_ADDITIONS = "Accept-Additions";

  // The Secure HyperText Transfer Protocol
  public static final String HEADER_CONTENT_PRIVACY_DOMAIN = "Content-Privacy-Domain";
  public static final String HEADER_MAC_INFO = "MAC-Info";
  public static final String HEADER_PREARRANGED_KEY_INFO = "Prearranged-Key-Info";

  // An HTTP Extension Framework
  public static final String HEADER_C_EXT = "C-Ext";
  public static final String HEADER_C_MAN = "C-Man";
  public static final String HEADER_C_OPT = "C-Opt";
  public static final String HEADER_EXT = "Ext";
  public static final String HEADER_MAN = "Man";
  public static final String HEADER_OPT = "Opt";

  // PICS Label Distribution Label Syntax and Communication Protocols
  public static final String HEADER_PICS_LABEL = "PICS-Label";

  // Platform For Privacy Preferences 1.0
  public static final String HEADER_P3P = "P3P";

  // PEP - an Extension Mechanism for HTTP
  public static final String HEADER_C_PEP = "C-PEP";
  public static final String HEADER_C_PEP_INFO = "C-PEP-Info";
  public static final String HEADER_PEP = "PEP";
  public static final String HEADER_PEP_INFO = "Pep-Info";

  // The HTTP Distribution and Replication Protocol
  public static final String HEADER_CONTENT_ID = "Content-ID";
  public static final String HEADER_DIFFERENTIAL_ID = "Differential-ID";

  // ESI Architecture
  public static final String HEADER_SURROGATE_CAPABILITY = "Surrogate-Capability";
  public static final String HEADER_SURROGATE_CONTROL = "Surrogate-Control";

  // Selecting Payment Mechanisms Over HTTP
  public static final String HEADER_PROTOCOL = "Protocol";
  public static final String HEADER_PROTOCOL_INFO = "Protocol-Info";
  public static final String HEADER_PROTOCOL_QUERY = "Protocol-Query";
  public static final String HEADER_PROTOCOL_REQUEST = "Protocol-Request";

  // Implementation of OPS Over HTTP
  public static final String HEADER_GETPROFILE = "GetProfile";
  public static final String HEADER_PROFILEOBJECT = "ProfileObject";
  public static final String HEADER_SETPROFILE = "SetProfile";

  // Notification for Proxy Caches
  public static final String HEADER_PROXY_FEATURES = "Proxy-Features";
  public static final String HEADER_PROXY_INSTRUCTION = "Proxy-Instruction";

  // Object Header lines in HTTP
  public static final String HEADER_CONTENT_TRANSFER_ENCODING = "Content-Transfer-Encoding";
  public static final String HEADER_COST = "Cost";
  public static final String HEADER_MESSAGE_ID = "Message-ID";
  public static final String HEADER_TITLE = "Title";
  public static final String HEADER_VERSION = "Version";

  // A Proposed Extension Mechanism for HTTP
  public static final String HEADER_EXTENSION = "Extension";

  // WIRE - W3 Identifier Resolution Extensions
  public static final String HEADER_OPTIONAL = "Optional";
  public static final String HEADER_RESOLUTION_HINT = "Resolution-Hint";

  // Duplicate Suppression in HTTP
  public static final String HEADER_SUBOK = "SubOK";
  public static final String HEADER_SUBST = "Subst";

  // Specification of HTTP/1.1 OPTIONS messages
  public static final String HEADER_COMPLIANCE = "Compliance";
  public static final String HEADER_NON_COMPLIANCE = "Non-Compliance";

  // Widely-used undocumented headers
  public static final String HEADER_REQUEST_RANGE = "Request-Range";
  public static final String HEADER_UA_COLOR = "UA-Color";
  public static final String HEADER_UA_CPU = "UA-CPU";
  public static final String HEADER_UA_OS = "UA-OS";
  public static final String HEADER_UA_PIXELS = "UA-Pixels";

  // Implementation errors
  public static final String HEADER_REFERRER = "Referrer";

  // Private features
  public static final String HEADER_COPYRIGHT = "Copyright";
  public static final String HEADER_CONTENT = "Content";
  public static final String HEADER_AUTHOR = "Author";
  public static final String HEADER_CONTACT = "Contact";
  public static final String HEADER_KEYWORDS = "Keywords";
  public static final String HEADER_GENERATOR = "Generator";
  public static final String HEADER_DESCRIPTION = "Description";
  public static final String HEADER_COMMAND = "Command";
  public static final String HEADER_SESSION = "Session";
  public static final String HEADER_TYPE = "Type";
  public static final String HEADER_MESSAGE = "Message";

  // Abandoned proposals
  public static final String HEADER_UNLESS_MODIFIED_SINCE = "Unless-Modified-Since";

  /**
   * Returns all the standardized HTTP header names presented as List
   * @return The list of standardized HTTP header names
   */
  public static List getStandardizedHeaderNames() {
    return Arrays.asList(new Object[] {
      HEADER_ACCEPT.toUpperCase(),
      HEADER_ACCEPT_CHARSET.toUpperCase(),
      HEADER_ACCEPT_ENCODING.toUpperCase(),
      HEADER_ACCEPT_LANGUAGE.toUpperCase(),
      HEADER_ACCEPT_RANGES.toUpperCase(),
      HEADER_AGE.toUpperCase(),
      HEADER_ALLOW.toUpperCase(),
      HEADER_AUTHORIZATION.toUpperCase(),
      HEADER_CACHE_CONTROL.toUpperCase(),
      HEADER_CONNECT.toUpperCase(),
      HEADER_CONTENT_ENCODING.toUpperCase(),
      HEADER_CONTENT_LANGUAGE.toUpperCase(),
      HEADER_CONTENT_LENGTH.toUpperCase(),
      HEADER_CONTENT_LOCATION.toUpperCase(),
      HEADER_CONTENT_MD5.toUpperCase(),
      HEADER_CONTENT_RANGE.toUpperCase(),
      HEADER_CONTENT_TYPE.toUpperCase(),
      HEADER_DATE.toUpperCase(),
      HEADER_ETAG.toUpperCase(),
      HEADER_EXPECT.toUpperCase(),
      HEADER_EXPIRES.toUpperCase(),
      HEADER_FROM.toUpperCase(),
      HEADER_HOST.toUpperCase(),
      HEADER_IF_MATCH.toUpperCase(),
      HEADER_IF_MODIFIED_SINCE.toUpperCase(),
      HEADER_IF_NONE_MATCH.toUpperCase(),
      HEADER_IF_RANGE.toUpperCase(),
      HEADER_IF_UNMODIFIED_SINCE.toUpperCase(),
      HEADER_LAST_MODIFIED.toUpperCase(),
      HEADER_LOCATION.toUpperCase(),
      HEADER_MAX_FORWARDS.toUpperCase(),
      HEADER_PRAGMA.toUpperCase(),
      HEADER_PROXY_AUTHENTICATE.toUpperCase(),
      HEADER_PROXY_AUTHORIZATION.toUpperCase(),
      HEADER_RANGE.toUpperCase(),
      HEADER_REFERER.toUpperCase(),
      HEADER_RETRY_AFTER.toUpperCase(),
      HEADER_SERVER.toUpperCase(),
      HEADER_TE.toUpperCase(),
      HEADER_TRAILER.toUpperCase(),
      HEADER_TRANSFER_ENCODING.toUpperCase(),
      HEADER_UPGRADE.toUpperCase(),
      HEADER_USER_AGENT.toUpperCase(),
      HEADER_VARY.toUpperCase(),
      HEADER_VIA.toUpperCase(),
      HEADER_WARNING.toUpperCase(),
      HEADER_WWW_AUTHENTICATE.toUpperCase(),
      HEADER_MIME_VERSION.toUpperCase(),
      HEADER_CONTENT_DISPOSITION.toUpperCase(),

      HEADER_AUTHENTICATION_INFO.toUpperCase(),

      HEADER_SET_COOKIE.toUpperCase(),

      HEADER_COOKIE.toUpperCase(),
      HEADER_COOKIE2.toUpperCase(),
      HEADER_SET_COOKIE2.toUpperCase(),

      HEADER_DAV.toUpperCase(),
      HEADER_DEPTH.toUpperCase(),
      HEADER_DESTINATION.toUpperCase(),
      HEADER_IF.toUpperCase(),
      HEADER_LOCK_TOKEN.toUpperCase(),
      HEADER_OVERWRITE.toUpperCase(),
      HEADER_STATUS_URI.toUpperCase(),
      HEADER_TIMEOUT.toUpperCase(),

      HEADER_CONTENT_BASE.toUpperCase(),
      HEADER_PUBLIC.toUpperCase(),
      HEADER_CONTENT_VERSION.toUpperCase(),
      HEADER_DERIVED_FROM.toUpperCase(),
      HEADER_LINK.toUpperCase(),
      HEADER_URI.toUpperCase(),
      HEADER_KEEP_ALIVE.toUpperCase(),

      HEADER_A_IM.toUpperCase(),
      HEADER_DELTA_BASE.toUpperCase(),
      HEADER_IM.toUpperCase(),

      HEADER_DIGEST.toUpperCase(),
      HEADER_WANT_DIGEST.toUpperCase(),

      HEADER_METER.toUpperCase()
    });
  }

  /**
   * Returns all the non-standardized HTTP header names presented as List
   * @return The list of non-standardized HTTP header names
   */
  public static List getNonStandardizedHeaderNames() {
    return Arrays.asList(new Object[] {
      HEADER_ACCEPT_FEATURES.toUpperCase(),
      HEADER_ALTERNATES.toUpperCase(),
      HEADER_NEGOTIATE.toUpperCase(),
      HEADER_TCN.toUpperCase(),
      HEADER_VARIANT_VARY.toUpperCase(),

      HEADER_SAFE.toUpperCase(),

      HEADER_ACCEPT_ADDITIONS.toUpperCase(),

      HEADER_CONTENT_PRIVACY_DOMAIN.toUpperCase(),
      HEADER_MAC_INFO.toUpperCase(),
      HEADER_PREARRANGED_KEY_INFO.toUpperCase(),

      HEADER_C_EXT.toUpperCase(),
      HEADER_C_MAN.toUpperCase(),
      HEADER_C_OPT.toUpperCase(),
      HEADER_EXT.toUpperCase(),
      HEADER_MAN.toUpperCase(),
      HEADER_OPT.toUpperCase(),

      HEADER_PICS_LABEL.toUpperCase(),
      HEADER_PROTOCOL.toUpperCase(),
      HEADER_PROTOCOL_REQUEST.toUpperCase(),

      HEADER_P3P.toUpperCase(),

      HEADER_C_PEP.toUpperCase(),
      HEADER_C_PEP_INFO.toUpperCase(),
      HEADER_PEP.toUpperCase(),
      HEADER_PEP_INFO.toUpperCase(),

      HEADER_CONTENT_ID.toUpperCase(),
      HEADER_DIFFERENTIAL_ID.toUpperCase(),

      HEADER_SURROGATE_CAPABILITY.toUpperCase(),
      HEADER_SURROGATE_CONTROL.toUpperCase(),

      HEADER_PROTOCOL.toUpperCase(),
      HEADER_PROTOCOL_INFO.toUpperCase(),
      HEADER_PROTOCOL_QUERY.toUpperCase(),
      HEADER_PROTOCOL_REQUEST.toUpperCase(),

      HEADER_GETPROFILE.toUpperCase(),
      HEADER_PROFILEOBJECT.toUpperCase(),
      HEADER_SETPROFILE.toUpperCase(),

      HEADER_PROXY_FEATURES.toUpperCase(),
      HEADER_PROXY_INSTRUCTION.toUpperCase(),

      HEADER_CONTENT_TRANSFER_ENCODING.toUpperCase(),
      HEADER_COST.toUpperCase(),
      HEADER_MESSAGE_ID.toUpperCase(),
      HEADER_TITLE.toUpperCase(),
      HEADER_VERSION.toUpperCase(),

      HEADER_EXTENSION.toUpperCase(),

      HEADER_OPTIONAL.toUpperCase(),
      HEADER_RESOLUTION_HINT.toUpperCase(),

      HEADER_SUBOK.toUpperCase(),
      HEADER_SUBST.toUpperCase(),

      HEADER_COMPLIANCE.toUpperCase(),
      HEADER_NON_COMPLIANCE.toUpperCase(),

      HEADER_REQUEST_RANGE.toUpperCase(),
      HEADER_UA_COLOR.toUpperCase(),
      HEADER_UA_CPU.toUpperCase(),
      HEADER_UA_OS.toUpperCase(),
      HEADER_UA_PIXELS.toUpperCase(),

      HEADER_REFERRER.toUpperCase(),

      HEADER_COPYRIGHT.toUpperCase(),
      HEADER_CONTENT.toUpperCase(),
      HEADER_AUTHOR.toUpperCase(),
      HEADER_CONTACT.toUpperCase(),
      HEADER_KEYWORDS.toUpperCase(),
      HEADER_GENERATOR.toUpperCase(),
      HEADER_DESCRIPTION.toUpperCase(),
      HEADER_COMMAND.toUpperCase(),
      HEADER_SESSION.toUpperCase(),
      HEADER_TYPE.toUpperCase(),
      HEADER_MESSAGE.toUpperCase(),

      HEADER_UNLESS_MODIFIED_SINCE.toUpperCase()
    });
  }

  /**
   * Returns all known HTTP header names presented as List
   * @return The list of all known HTTP header names
   */
  public static List getAllKnownHeaderNames() {
    return Arrays.asList(new Object[] {
      HEADER_ACCEPT.toUpperCase(),
      HEADER_ACCEPT_CHARSET.toUpperCase(),
      HEADER_ACCEPT_ENCODING.toUpperCase(),
      HEADER_ACCEPT_LANGUAGE.toUpperCase(),
      HEADER_ACCEPT_RANGES.toUpperCase(),
      HEADER_AGE.toUpperCase(),
      HEADER_ALLOW.toUpperCase(),
      HEADER_AUTHORIZATION.toUpperCase(),
      HEADER_CACHE_CONTROL.toUpperCase(),
      HEADER_CONNECT.toUpperCase(),
      HEADER_CONTENT_ENCODING.toUpperCase(),
      HEADER_CONTENT_LANGUAGE.toUpperCase(),
      HEADER_CONTENT_LENGTH.toUpperCase(),
      HEADER_CONTENT_LOCATION.toUpperCase(),
      HEADER_CONTENT_MD5.toUpperCase(),
      HEADER_CONTENT_RANGE.toUpperCase(),
      HEADER_CONTENT_TYPE.toUpperCase(),
      HEADER_DATE.toUpperCase(),
      HEADER_ETAG.toUpperCase(),
      HEADER_EXPECT.toUpperCase(),
      HEADER_EXPIRES.toUpperCase(),
      HEADER_FROM.toUpperCase(),
      HEADER_HOST.toUpperCase(),
      HEADER_IF_MATCH.toUpperCase(),
      HEADER_IF_MODIFIED_SINCE.toUpperCase(),
      HEADER_IF_NONE_MATCH.toUpperCase(),
      HEADER_IF_RANGE.toUpperCase(),
      HEADER_IF_UNMODIFIED_SINCE.toUpperCase(),
      HEADER_LAST_MODIFIED.toUpperCase(),
      HEADER_LOCATION.toUpperCase(),
      HEADER_MAX_FORWARDS.toUpperCase(),
      HEADER_PRAGMA.toUpperCase(),
      HEADER_PROXY_AUTHENTICATE.toUpperCase(),
      HEADER_PROXY_AUTHORIZATION.toUpperCase(),
      HEADER_RANGE.toUpperCase(),
      HEADER_REFERER.toUpperCase(),
      HEADER_RETRY_AFTER.toUpperCase(),
      HEADER_SERVER.toUpperCase(),
      HEADER_TE.toUpperCase(),
      HEADER_TRAILER.toUpperCase(),
      HEADER_TRANSFER_ENCODING.toUpperCase(),
      HEADER_UPGRADE.toUpperCase(),
      HEADER_USER_AGENT.toUpperCase(),
      HEADER_VARY.toUpperCase(),
      HEADER_VIA.toUpperCase(),
      HEADER_WARNING.toUpperCase(),
      HEADER_WWW_AUTHENTICATE.toUpperCase(),
      HEADER_MIME_VERSION.toUpperCase(),
      HEADER_CONTENT_DISPOSITION.toUpperCase(),

      HEADER_AUTHENTICATION_INFO.toUpperCase(),

      HEADER_SET_COOKIE.toUpperCase(),

      HEADER_COOKIE.toUpperCase(),
      HEADER_COOKIE2.toUpperCase(),
      HEADER_SET_COOKIE2.toUpperCase(),

      HEADER_DAV.toUpperCase(),
      HEADER_DEPTH.toUpperCase(),
      HEADER_DESTINATION.toUpperCase(),
      HEADER_IF.toUpperCase(),
      HEADER_LOCK_TOKEN.toUpperCase(),
      HEADER_OVERWRITE.toUpperCase(),
      HEADER_STATUS_URI.toUpperCase(),
      HEADER_TIMEOUT.toUpperCase(),

      HEADER_CONTENT_BASE.toUpperCase(),
      HEADER_PUBLIC.toUpperCase(),
      HEADER_CONTENT_VERSION.toUpperCase(),
      HEADER_DERIVED_FROM.toUpperCase(),
      HEADER_LINK.toUpperCase(),
      HEADER_URI.toUpperCase(),
      HEADER_KEEP_ALIVE.toUpperCase(),

      HEADER_A_IM.toUpperCase(),
      HEADER_DELTA_BASE.toUpperCase(),
      HEADER_IM.toUpperCase(),

      HEADER_DIGEST.toUpperCase(),
      HEADER_WANT_DIGEST.toUpperCase(),

      HEADER_METER.toUpperCase(),

      HEADER_ACCEPT_FEATURES.toUpperCase(),
      HEADER_ALTERNATES.toUpperCase(),
      HEADER_NEGOTIATE.toUpperCase(),
      HEADER_TCN.toUpperCase(),
      HEADER_VARIANT_VARY.toUpperCase(),

      HEADER_SAFE.toUpperCase(),

      HEADER_ACCEPT_ADDITIONS.toUpperCase(),

      HEADER_CONTENT_PRIVACY_DOMAIN.toUpperCase(),
      HEADER_MAC_INFO.toUpperCase(),
      HEADER_PREARRANGED_KEY_INFO.toUpperCase(),

      HEADER_C_EXT.toUpperCase(),
      HEADER_C_MAN.toUpperCase(),
      HEADER_C_OPT.toUpperCase(),
      HEADER_EXT.toUpperCase(),
      HEADER_MAN.toUpperCase(),
      HEADER_OPT.toUpperCase(),

      HEADER_PICS_LABEL.toUpperCase(),
      HEADER_PROTOCOL.toUpperCase(),
      HEADER_PROTOCOL_REQUEST.toUpperCase(),

      HEADER_P3P.toUpperCase(),

      HEADER_C_PEP.toUpperCase(),
      HEADER_C_PEP_INFO.toUpperCase(),
      HEADER_PEP.toUpperCase(),
      HEADER_PEP_INFO.toUpperCase(),

      HEADER_CONTENT_ID.toUpperCase(),
      HEADER_DIFFERENTIAL_ID.toUpperCase(),

      HEADER_SURROGATE_CAPABILITY.toUpperCase(),
      HEADER_SURROGATE_CONTROL.toUpperCase(),

      HEADER_PROTOCOL.toUpperCase(),
      HEADER_PROTOCOL_INFO.toUpperCase(),
      HEADER_PROTOCOL_QUERY.toUpperCase(),
      HEADER_PROTOCOL_REQUEST.toUpperCase(),

      HEADER_GETPROFILE.toUpperCase(),
      HEADER_PROFILEOBJECT.toUpperCase(),
      HEADER_SETPROFILE.toUpperCase(),

      HEADER_PROXY_FEATURES.toUpperCase(),
      HEADER_PROXY_INSTRUCTION.toUpperCase(),

      HEADER_CONTENT_TRANSFER_ENCODING.toUpperCase(),
      HEADER_COST.toUpperCase(),
      HEADER_MESSAGE_ID.toUpperCase(),
      HEADER_TITLE.toUpperCase(),
      HEADER_VERSION.toUpperCase(),

      HEADER_EXTENSION.toUpperCase(),

      HEADER_OPTIONAL.toUpperCase(),
      HEADER_RESOLUTION_HINT.toUpperCase(),

      HEADER_SUBOK.toUpperCase(),
      HEADER_SUBST.toUpperCase(),

      HEADER_COMPLIANCE.toUpperCase(),
      HEADER_NON_COMPLIANCE.toUpperCase(),

      HEADER_REQUEST_RANGE.toUpperCase(),
      HEADER_UA_COLOR.toUpperCase(),
      HEADER_UA_CPU.toUpperCase(),
      HEADER_UA_OS.toUpperCase(),
      HEADER_UA_PIXELS.toUpperCase(),

      HEADER_REFERRER.toUpperCase(),

      HEADER_COPYRIGHT.toUpperCase(),
      HEADER_CONTENT.toUpperCase(),
      HEADER_AUTHOR.toUpperCase(),
      HEADER_CONTACT.toUpperCase(),
      HEADER_KEYWORDS.toUpperCase(),
      HEADER_GENERATOR.toUpperCase(),
      HEADER_DESCRIPTION.toUpperCase(),
      HEADER_COMMAND.toUpperCase(),
      HEADER_SESSION.toUpperCase(),
      HEADER_TYPE.toUpperCase(),
      HEADER_MESSAGE.toUpperCase(),

      HEADER_UNLESS_MODIFIED_SINCE.toUpperCase()
    });
  }
}