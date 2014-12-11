package com.hx.android.ehentai.util;

import android.util.Log;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XMLParser {
	private Document asyncDocument;

	public String ISO2UNICODE(char[] paramArrayOfChar) {
		String ret;
		try {
			ret = new String(new String(paramArrayOfChar).getBytes("ISO88591"),
					"UTF8");
		} catch (Exception localException) {
			ret = "undefined";
		}
		
		return ret;
	}

	public void asyncCreateDocument(final String path, final AsyncCreateComplete createComplete) {
		new Thread() {
			@Override
			public void run() {
				try {
					asyncDocument = getDomElement(getXmlFromUrl(path, null),
							false);
				} catch (Exception e) {
					e.printStackTrace();
				}finally
				{
					createComplete.createComplete(asyncDocument);
				}
			}
		}.start();

	}

	public Document getDomElement(String paramString, Boolean paramBoolean) {
		Document localDocument = null;
		DocumentBuilderFactory localDocumentBuilderFactory = DocumentBuilderFactory
				.newInstance();
		localDocumentBuilderFactory.setCoalescing(true);
		try {
			DocumentBuilder localDocumentBuilder = localDocumentBuilderFactory
					.newDocumentBuilder();
			InputSource localInputSource = new InputSource();
			if (paramBoolean)
				localInputSource.setCharacterStream(new StringReader(
						ISO2UNICODE(paramString.toCharArray())));
			else {
				localInputSource.setCharacterStream(new StringReader(
						paramString));
			}
			localDocument = localDocumentBuilder.parse(localInputSource);
		} catch (ParserConfigurationException localParserConfigurationException) {
			Log.e("Error: ", localParserConfigurationException.getMessage());
		} catch (SAXException localSAXException) {
			Log.e("Error: ", localSAXException.getMessage());
		} catch (IOException localIOException) {
			Log.e("Error: ", localIOException.getMessage());
		} catch (NullPointerException localNullPointerException) {
		}
		return localDocument;
	}

	public final String getElementValue(Node paramNode) {
		String ret = "";
		if ((paramNode != null) && (paramNode.hasChildNodes())) {
			Node localNode = paramNode.getFirstChild();
			if (localNode != null) {
				if ((localNode.getNodeType() == 3)
						|| (localNode.getNodeType() == 4)) {
					ret = localNode.getNodeValue();
				}
			}

		}
		return ret;
	}

	public String getValue(Element paramElement, String paramString) {
		return getElementValue(
				paramElement.getElementsByTagName(paramString).item(0)).trim();
	}

	public String getXmlFromUrl(String paramString,
			List<NameValuePair> paramList) {
		try {
			DefaultHttpClient localDefaultHttpClient = new DefaultHttpClient();
			HttpPost localHttpPost = new HttpPost(paramString);
			if (paramList != null)
				localHttpPost.setEntity(new UrlEncodedFormEntity(paramList));
			String str = EntityUtils.toString(localDefaultHttpClient.execute(
					localHttpPost).getEntity());
			return str;
		} catch (UnsupportedEncodingException localUnsupportedEncodingException) {
			while (true)
				localUnsupportedEncodingException.printStackTrace();
		} catch (ClientProtocolException localClientProtocolException) {
			while (true)
				localClientProtocolException.printStackTrace();
		} catch (IOException localIOException) {
			while (true)
				localIOException.printStackTrace();
		}
	}
	
	public static interface AsyncCreateComplete
	{
		public void createComplete(Document document);
	}
}