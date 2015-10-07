package mpt.is416.com.teamup;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

/**
 * Created by JunHong on 2/10/2015.
 * Modified by Elyza on 6/10/2015.
 */
public class FragmentQRCode extends Fragment {

    private Bitmap bitmap;
    private final String ANDROID_ID = "android_id";
    private final String PREFS_NAME = "preferences";
    private final String TAG = FragmentQRCode.class.getSimpleName();

    @Nullable
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_qrcode, container, false);

        // Get androidId
        final String androidId = (getActivity().getSharedPreferences(PREFS_NAME, 0))
                .getString(ANDROID_ID, null);
        if (androidId != null) {
            // Get appropriate dimensions
            Point point = new Point();
            ((WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE))
                    .getDefaultDisplay().getSize(point);
            int dimension = point.x < point.y ? point.x : point.y;
            dimension = dimension * 3 / 4;
            // Encode a QR Code image
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            try {
                BitMatrix matrix = qrCodeWriter.encode(androidId, BarcodeFormat.QR_CODE,
                        dimension, dimension);

                bitmap = Bitmap.createBitmap(matrix.getWidth(), matrix.getHeight(),
                        Bitmap.Config.RGB_565);
                for (int x = 0; x < matrix.getWidth(); x++) {
                    for (int y = 0; y < matrix.getHeight(); y++) {
                        bitmap.setPixel(x, y, matrix.get(x, y) ? Color.BLACK : Color.WHITE);
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        } else {
            Log.e(TAG, "android_id is not set in preferences - if this is not the first launch, " +
                    "please wipe data from emulator");
        }

        // Show QR Code
        ((ImageView) v.findViewById(R.id.image)).setImageBitmap(bitmap);
        return v;
    }
}