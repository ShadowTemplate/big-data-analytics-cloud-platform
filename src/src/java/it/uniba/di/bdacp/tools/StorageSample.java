package it.uniba.di.bdacp.tools;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.storage.Storage;
import com.google.api.services.storage.StorageScopes;
import com.google.api.services.storage.model.Bucket;
import com.google.api.services.storage.model.StorageObject;

import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Main class for the Cloud Storage API command line sample.
 * Demonstrates how to make an authenticated API call using OAuth 2 helper classes.
 */
public class StorageSample {
    /**
     * Be sure to specify the name of your application. If the application name is {@code null} or
     * blank, the application will log a warning. Suggested format is "MyCompany-ProductName/1.0".
     */
    private static final String APPLICATION_NAME = "";
    private static final String BUCKET_NAME = "wordcountex-bucket";
    /**
     * Directory to store user credentials.
     */
    private static final java.io.File DATA_STORE_DIR =
            new java.io.File(System.getProperty("user.home"), ".store/storage_sample");
    /**
     * Global instance of the {@link DataStoreFactory}. The best practice is to make it a single
     * globally shared instance across your application.
     */
    private static FileDataStoreFactory dataStoreFactory;
    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    /**
     * Global instance of the HTTP transport.
     */
    private static HttpTransport httpTransport;
    private static Storage client;

    /**
     * Authorizes the installed application to access user's protected data.
     */
    private static Credential authorize() throws Exception {
// Load client secrets.
        GoogleClientSecrets clientSecrets = null;
        try {
            clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
                    new InputStreamReader(StorageSample.class.getResourceAsStream("/client_secrets.json")));
            if (clientSecrets.getDetails().getClientId() == null ||
                    clientSecrets.getDetails().getClientSecret() == null) {
                throw new Exception("client_secrets not well formed.");
            }
        } catch (Exception e) {
            System.out.println("Problem loading client_secrets.json file. Make sure it exists, you are " +
                    "loading it with the right path, and a client ID and client secret are " +
                    "defined in it.\n" + e.getMessage());
            System.exit(1);
        }
// Set up authorization code flow.
// Ask for only the permissions you need. Asking for more permissions will
// reduce the number of users who finish the process for giving you access
// to their accounts. It will also increase the amount of effort you will
// have to spend explaining to users what you are doing with their data.
// Here we are listing all of the available scopes. You should remove scopes
// that you are not actually using.
        Set<String> scopes = new HashSet<String>();
        scopes.add(StorageScopes.DEVSTORAGE_FULL_CONTROL);
        scopes.add(StorageScopes.DEVSTORAGE_READ_ONLY);
        scopes.add(StorageScopes.DEVSTORAGE_READ_WRITE);
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientSecrets, scopes)
                .setDataStoreFactory(dataStoreFactory)
                .build();
// Authorize.
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }

    public static void main(String[] args) throws IOException {
        try {
// Initialize the transport.
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
// Initialize the data store factory.
            dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
// Authorization.
            Credential credential = authorize();
// Set up global Storage instance.
            client = new Storage.Builder(httpTransport, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME).build();
// Get metadata about the specified bucket.
            Storage.Buckets.Get getBucket = client.buckets().get(BUCKET_NAME);
            getBucket.setProjection("full");
            Bucket bucket = getBucket.execute();

            System.out.println("name: " + BUCKET_NAME);
            System.out.println("location: " + bucket.getLocation());
            System.out.println("timeCreated: " + bucket.getTimeCreated());
            System.out.println("owner: " + bucket.getOwner());
            System.out.println();
            System.out.println("Content: ");
            System.out.println();
// List the contents of the bucket.
            Storage.Objects.List listObjects = client.objects().list(BUCKET_NAME);
            com.google.api.services.storage.model.Objects objects;
            do {
                objects = listObjects.execute();
                List<StorageObject> items = objects.getItems();
                if (null == items) {
                    System.out.println("There were no objects in the given bucket; try adding some and re-running.");
                    break;
                }
                for (StorageObject object : items) {
                    System.out.println(object.getName() + " (" + object.getSize() + " bytes)");
                }
                listObjects.setPageToken(objects.getNextPageToken());
            } while (null != objects.getNextPageToken());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }

        System.out.println("Trying to upload a file...");
        InputStream is = StorageSample.class.getResourceAsStream("/text");
        File temp = new File(StorageSample.class.getResource("/text").getFile());
        //File temp = new File("/media/win/Users/Gianvito/Desktop/TESI/CODE/BDACP/target/classes/text");
        long size = temp.length();
        //uploadFile(is, size, client, "magic.txt");
    }

    public static void uploadFile(InputStream inputStream, long byteCount, Storage storage, String storageFile) throws IOException {
        InputStreamContent mediaContent = new InputStreamContent("application/octet-stream", inputStream);
// Knowing the stream length allows server-side optimization, and client-side progress
// reporting with a MediaHttpUploaderProgressListener.
        mediaContent.setLength(byteCount);

        StorageObject objectMetadata = null;

        /*
        if (useCustomMetadata) {
            // If you have custom settings for metadata on the object you want to set
            // then you can allocate a StorageObject and set the values here. You can
            // leave out setBucket(), since the bucket is in the insert command's
            // parameters.
            objectMetadata = new StorageObject()
                    .setName("myobject")
                    .setMetadata(ImmutableMap.of("key1", "value1", "key2", "value2"))
                    .setAcl(ImmutableList.of(
                            new ObjectAccessControl().setEntity("domain-example.com").setRole("READER"),
                            new ObjectAccessControl().setEntity("user-administrator@example.com").setRole("OWNER")
                    ))
                    .setContentDisposition("attachment");
        }
        */

        Storage.Objects.Insert insertObject = storage.objects().insert(BUCKET_NAME, objectMetadata,
                mediaContent);

        //if (!useCustomMetadata) {
            // If you don't provide metadata, you will have specify the object
            // name by parameter. You will probably also want to ensure that your
            // default object ACLs (a bucket property) are set appropriately:
            // https://developers.google.com/storage/docs/json_api/v1/buckets#defaultObjectAcl

        insertObject.setName(storageFile);
        //}

// For small files, you may wish to call setDirectUploadEnabled(true), to
// reduce the number of HTTP requests made to the server.
        if (mediaContent.getLength() > 0 && mediaContent.getLength() <= 2 * 1000 * 1000 /* 2MB */) {
            insertObject.getMediaHttpUploader().setDirectUploadEnabled(true);
        }

        insertObject.execute();
    }

}
