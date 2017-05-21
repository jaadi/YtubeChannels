package netconnections;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;

public class HttpCallManagerMT {

	int threadId = 1;

	public HttpCallManagerMT() {
		super();
	}

	public void makeCall(String s_url) {
		// Create an HttpClient with the MultiThreadedHttpConnectionManager.
		// This connection manager must be used if more than one thread will
		// be using the HttpClient.
		HttpClient httpClient = new HttpClient(
				new MultiThreadedHttpConnectionManager());
		// Set the default host/protocol for the methods to connect to.
		// This value will only be used if the methods are not given an absolute
		// URI
		// httpClient.getHostConfiguration().setHost("hc.apache.org", 80,
		// "http");

		// create an array of URIs to perform GETs on
		// nj String[] urisToGet = {
		// "/",
		// "/httpclient-3.x/status.html",
		// "/httpclient-3.x/methods/",
		// "http://svn.apache.org/viewvc/httpcomponents/oac.hc3x/"
		// };

		// create a thread for each URI
		// GetThread[] threads = new GetThread[urisToGet.length];
		// for (int i = 0; i < threads.length; i++) {
		// GetMethod get = new GetMethod(urisToGet[i]);
		// get.setFollowRedirects(true);
		// threads[i] = new GetThread(httpClient, get, i + 1);
		// }
		//
		// // start the threads
		// for (int j = 0; j < threads.length; j++) {
		// threads[j].start();
		// }

		GetMethod get = new GetMethod(s_url);
		get.setFollowRedirects(true);
		GetThread thread = new GetThread(httpClient, get, threadId);
		threadId++;
		thread.start();
	}

	/**
	 * A thread that performs a GET.
	 */
	static class GetThread extends Thread {

		private HttpClient httpClient;
		private GetMethod method;
		private int id;
		
		public GetThread(HttpClient httpClient, GetMethod method, int id) {
			this.httpClient = httpClient;
			this.method = method;
			this.id = id;
		}

		/**
		 * Executes the GetMethod and prints some satus information.
		 */
		public void run() {

			try {

				System.out.println(id + " - about to get something from "
						+ method.getURI());
				// execute the method
				httpClient.executeMethod(method);
				

				System.out.println(id + " - get executed");
				// get the response body as an array of bytes
				byte[] bytes = method.getResponseBody();

				String s = new String(bytes);

				System.out.println(id + " - " + bytes.length
						+ " bytes read --  " + s);

			} catch (Exception e) {
				System.out.println(id + " - error: " + e);
			} finally {
				// always release the connection after we're done
				method.releaseConnection();
				System.out.println(id + " - connection released");
			}
		}

	}

}
