package be.pxl.erisontavares.watchedit.data;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;

public class WatchedItQueryHandler extends AsyncQueryHandler {
    public WatchedItQueryHandler(ContentResolver cr) {
        super(cr);
    }
}
