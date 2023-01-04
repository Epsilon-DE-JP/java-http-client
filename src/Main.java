import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.util.Base64;

public class Main {
    public static void main(String[] args) throws IOException {
        String URL_Param = "http://localhost:5500/v1/object-detection";
        String imagePath = "image/dirt-road-5975935_1920.jpg";
        String potholeId = "dfs6aacc-e2bd-4775-8f44-6cc693c5baaff";

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(URL_Param);
        File file = new File(imagePath);
        InputStream input = new FileInputStream(file);

        // --- START: ADD THIS ---
        byte[] buffer = new byte[1024];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
        input.close();
        String base64File = Base64.getEncoder().encodeToString(output.toByteArray());
        // --- END: ADD THIS ---

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE); // -- CHANGE THIS --
        builder.addBinaryBody("file", base64File.getBytes()); // -- CHANGE THIS --
        builder.addTextBody("id", potholeId);

        HttpEntity entity = builder.build();
        post.setEntity(entity);
        HttpResponse response = client.execute(post);
        System.out.println(response);
    }
}