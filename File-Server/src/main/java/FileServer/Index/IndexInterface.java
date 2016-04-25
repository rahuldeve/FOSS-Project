package FileServer.Index;

import FileServer.DataWrappers.MetadataWrapper;
import org.springframework.http.ResponseEntity;


/**
 * Created by rahul on 16/12/15.
 */

public interface IndexInterface {

    public ResponseEntity<String> indexFile(MetadataWrapper metadataWrapper);
}
