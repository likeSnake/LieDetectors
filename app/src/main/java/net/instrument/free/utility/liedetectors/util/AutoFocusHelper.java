package net.instrument.free.utility.liedetectors.util;


import android.graphics.PointF;
import android.graphics.Rect;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.params.MeteringRectangle;

public class AutoFocusHelper {
    private static final MeteringRectangle[] ZERO_WEIGHT_3A_REGION = new MeteringRectangle[]{
            new MeteringRectangle(0, 0, 0, 0, 0)};

    private static final float REGION_WEIGHT = 0.022f;

    /**
     * Width of touch AF region in [0,1] relative to shorter edge of the current
     * crop region. Multiply this number by the number of pixels along the
     * shorter edge of the current crop region's width to get a value in pixels.
     * <p>
     * <p>
     * This value has been tested on Nexus 5 and Shamu, but will need to be
     * tuned per device depending on how its ISP interprets the metering box and weight.
     * </p>
     * <p>
     * <p>
     * Values prior to L release:
     * Normal mode: 0.125 * longest edge
     * Gcam: Fixed at 300px x 300px.
     * </p>
     */
    private static final float AF_REGION_BOX = 0.2f;

    /**
     * Width of touch metering region in [0,1] relative to shorter edge of the
     * current crop region. Multiply this number by the number of pixels along
     * shorter edge of the current crop region's width to get a value in pixels.
     * <p>
     * <p>
     * This value has been tested on Nexus 5 and Shamu, but will need to be
     * tuned per device depending on how its ISP interprets the metering box and weight.
     * </p>
     * <p>
     * <p>
     * Values prior to L release:
     * Normal mode: 0.1875 * longest edge
     * Gcam: Fixed at 300px x 300px.
     * </p>
     */
    private static final float AE_REGION_BOX = 0.3f;

    /**
     * camera2 API metering region weight.
     */
    private static final int CAMERA2_REGION_WEIGHT = (int)
            (lerp(MeteringRectangle.METERING_WEIGHT_MIN, MeteringRectangle.METERING_WEIGHT_MAX,
                    REGION_WEIGHT));

    private AutoFocusHelper() {

    }

    public static MeteringRectangle[] getZeroWeightRegion() {
        return ZERO_WEIGHT_3A_REGION;
    }

    public static float lerp(float a, float b, float t) {
        return a + t * (b - a);
    }

    /**
     * Calculates sensor crop region for a zoom level (zoom >= 1.0).
     *
     * @return Crop region.
     */
    public static Rect cropRegionForZoom(CameraCharacteristics characteristics, float zoom) {
        Rect sensor = characteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);
        int xCenter = sensor.width() / 2;
        int yCenter = sensor.height() / 2;
        int xDelta = (int) (0.5f * sensor.width() / zoom);
        int yDelta = (int) (0.5f * sensor.height() / zoom);
        return new Rect(xCenter - xDelta, yCenter - yDelta, xCenter + xDelta, yCenter + yDelta);
    }

}