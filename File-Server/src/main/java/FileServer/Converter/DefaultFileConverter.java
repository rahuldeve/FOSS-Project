package FileServer.Converter;

import logger.Converter.TikaConfig.CollectingEmbeddedResourceHandler;
import logger.DataWrappers.FileMetadataWrapper;
import logger.DataWrappers.MetadataWrapper;
import org.apache.tika.exception.TikaException;
import org.apache.tika.extractor.ParserContainerExtractor;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by rahul on 25/12/15.
 *
 * This class is used as the default FileConverter by MIMEResolver. It uses the Apache Tika library to detect the
 * type of document and parse the it accordingly. The complete text from the file is extracted using the builtin parsers
 * from tika
 */
public class DefaultFileConverter implements FileConverter {

    //TODO update doc
    /**
     * Automatically detect the type of document an its contents using the appropriate builtin
     * parsers supported by tika. Returns a FileMetadataWrapper containing the extracted text as a string.
     *
     * @param stream        The InputStream of the MultiPartFile received from the request
     * @param filename      The name of the file
     * @param mimeType      The MIME type of the file
     * @param tags          The list of tags the file has been associated with
     * @param from          Name of the sender of the file
     * @param to            Name of the recipient of the file
     * @return              A FileMetadataWrapper containing the whole text inside the document as a string.
     */
    @Override
    public List<MetadataWrapper> extractContent(InputStream stream, String filename, String mimeType, List<String> tags, String from, String to, boolean recursive) {

        Logger.getLogger("DefaultFileConverter").log(Level.INFO, filename + ": " + mimeType + " :recursive = " + recursive);
        try (InputStream fileStream = stream) {

            List<MetadataWrapper> contents =new ArrayList<>();


            //parse Document
            AutoDetectParser parser = new AutoDetectParser();
            Metadata metadata = new Metadata();
            BodyContentHandler handler = new BodyContentHandler();
            TikaInputStream tikaInputStream = TikaInputStream.get(fileStream);
            parser.parse(tikaInputStream, handler, metadata);

            contents.add(new FileMetadataWrapper(filename, mimeType, handler.toString(), tags, from, to));

            if(recursive) {
                //parse embedded contents
                List<MetadataWrapper> extracted = extractEmbeddedContent(tikaInputStream, filename, from, to);
                contents.addAll(extracted);
            }

            Logger.getLogger("DefaultFileConverter").log(Level.INFO, filename + ": " + "contents: " + contents);
            return contents;

        } catch (IOException | SAXException | TikaException e) {
            e.printStackTrace();
        }

        return null;
    }

    private List<MetadataWrapper> extractEmbeddedContent(TikaInputStream tikaInputStream, String filename, String from, String to) throws IOException, TikaException {

        ParserContainerExtractor parserContainerExtractor = new ParserContainerExtractor();
        CollectingEmbeddedResourceHandler embeddedResourceHandler = new CollectingEmbeddedResourceHandler();

        parserContainerExtractor.extract(tikaInputStream, null, embeddedResourceHandler);
        return embeddedResourceHandler.embeddedFiles.entrySet().stream()
                                        .map(entry -> parseEmbeddedContent(entry.getKey(), entry.getValue(), filename, from, to))
                                        .collect(Collectors.toList());
    }


    private MetadataWrapper parseEmbeddedContent(InputStream stream, String mimeType, String filename, String from, String to){

        MIMEResolver mimeResolver = new MIMEResolver();
        FileConverter fileConverter = mimeResolver.getFileConverter(mimeType);
        List<MetadataWrapper> metadataWrappers = fileConverter.extractContent(stream, filename, mimeType, null, from, to, false);
        return metadataWrappers.get(0);
    }

    @Override
    public String getType(){
        return "Default";
    }


}
