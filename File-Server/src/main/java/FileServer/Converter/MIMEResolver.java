package FileServer.Converter;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by rahul on 17/12/15.
 *
 * This class is used to get the FileResolver for each specific MIME type. Use the resolverFile() function to get the
 * appropriate FileConverter. If no explicit mapping for a MIME type is found, the DefaultFileConverter is used.
 *
 * This class is denoted as a spring bean by the @Service annotation and injected into the MessageController class.
 */
@Service
public class MIMEResolver {

    public static String DefaultFileConverterHandle = "TIKA";
    private HashMap<String, FileConverter> mimeTable;


    public MIMEResolver() {


        //somehow load stuff from a file like a plugin ?
        mimeTable = new HashMap<>();

        //mimeTable.put("application/pdf", new PdfFileConverter());
        mimeTable.put("text/plain", new TextFileConverter());

        //entries to handle .jpg files
        mimeTable.put("image/jpeg", new ImageConverter());
        mimeTable.put("image/pjpeg", new ImageConverter());
        mimeTable.put("image/png", new ImageConverter());



        mimeTable.put(DefaultFileConverterHandle, new DefaultFileConverter());

    }

    /**
     * Gets the appropriate FileConverter by looking for the appropriate MIME type mapping in the mimeTable. Returns
     * the DefaultFileConverter if no mapping is found.
     *
     * @param file      The file to be indexed passed as a MultiPartFile object.
     * @return          The appropriate FileConverter object
     */
    public FileConverter getFileConverter(MultipartFile file) {
        FileConverter converter = mimeTable.get(file.getContentType());
        //MetadataWrapper fileMetadata = converter.convertFile(file);
        if (converter == null) {
            Logger.getLogger("MIMEResolver").log(Level.INFO, "Choosing default file converter");
            return mimeTable.get(DefaultFileConverterHandle);
        }
        return converter;
    }

    public FileConverter getFileConverter(String mimeType) {
        FileConverter converter = mimeTable.get(mimeType);
        //MetadataWrapper fileMetadata = converter.convertFile(file);
        if (converter == null)
            return mimeTable.get(DefaultFileConverterHandle);
        return converter;
    }
}
