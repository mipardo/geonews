package es.uji.geonews.model.services;

import android.os.Build;
import androidx.annotation.RequiresApi;
import java.time.LocalDate;

public class CoordsSearchService extends Service  {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public CoordsSearchService() {
        super("Geocode", "Coordinates Search Service", LocalDate.now());
    }
}
