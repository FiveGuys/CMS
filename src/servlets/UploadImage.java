package servlets;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

/**
 * This class uploads images to the Blobstore service.
 * @author 5guys
 */
@SuppressWarnings("serial")
public class UploadImage extends HttpServlet {

	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
		
		List<BlobKey> blobKey = blobs.get("myFile");

		if (blobKey == null) {
			
			res.sendRedirect("/");
			
		} else {
			
			res.sendRedirect("/serve?upload=yes&blob-key=" + blobKey.get(0).getKeyString());
		}
	}
}