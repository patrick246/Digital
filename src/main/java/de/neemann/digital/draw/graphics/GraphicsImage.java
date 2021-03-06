package de.neemann.digital.draw.graphics;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Creates an image
 *
 * @author hneemann
 */
public final class GraphicsImage extends GraphicSwing implements Closeable {

    private final OutputStream out;
    private final String format;
    private final float scale;
    private BufferedImage bi;

    /**
     * Creates a new instance
     *
     * @param out    the output stream
     * @param format the format to write
     * @param scale  the scaling
     */
    public GraphicsImage(OutputStream out, String format, float scale) {
        super(null);
        this.out = out;
        this.format = format;
        this.scale = scale;
    }

    @Override
    public Graphic setBoundingBox(Vector min, Vector max) {
        int thickness = Style.MAXLINETHICK;
        bi = new BufferedImage(
                Math.round((max.x - min.x + thickness * 2) * scale),
                Math.round((max.y - min.y + thickness * 2) * scale),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D gr = bi.createGraphics();
        gr.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        gr.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gr.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        gr.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

        gr.setColor(new Color(255, 255, 255, 0));
        gr.fillRect(0, 0, bi.getWidth(), bi.getHeight());

        gr.scale(scale, scale);
        gr.translate(thickness - min.x, thickness - min.y);
        setGraphics2D(gr);
        return this;
    }

    @Override
    public void close() throws IOException {
        if (out != null) {
            ImageIO.write(bi, format, out);
            out.close();
        }
    }

    /**
     * @return the created image
     */
    public BufferedImage getBufferedImage() {
        return bi;
    }
}
