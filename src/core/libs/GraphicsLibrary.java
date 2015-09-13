package core.libs;

import java.awt.*;

public class GraphicsLibrary implements AbstractLibrary {

    public static final int DRAW_ARC = -48486646;//"Graphics.drawArc(IIIIII)V"
    public static final int DRAW_IMAGE = -890300255;//"Graphics.drawImage(#Graphics.Image;II)V"
    public static final int DRAW_LINE = 626243098;//"Graphics.drawLine(IIII)V"
    public static final int DRAW_ROUND_RECT = 1248661386;//"Graphics.drawRoundRect(IIIIII)V"
    public static final int DRAW_STRING = -1512805968;//"Graphics.drawString(SII)V"
    public static final int DRAW_POLYGON = -332462178;//"Graphics.drawPolygon([I[I)V"
    public static final int DRAW_POLYLINE = 486333482;//"Graphics.drawPolyline([I[I)V"
    public static final int FILL_ARC = 208636105;//"Graphics.fillArc(IIIIII)V"
    public static final int FILL_RECT = 917825675;//"Graphics.fillRect(IIII)V"
    public static final int FILL_ROUND_RECT = 62514697;//"Graphics.fillRoundRect(IIIIII)V"
    public static final int FILL_OVAL = 1230311005;//"Graphics.fillOval(IIII)V"
    public static final int FILL_POLYGON = 1949365661;//"Graphics.fillPolygon([I[I)V"
    public static final int LOAD_IMAGE = -199811316;//"Graphics.loadImage(S)#Graphics.Image;"
    public static final int REPAINT = -1512416531;//"Graphics.repaint(IIII)V"
    public static final int SET_CLIP = -1951187898;//"Graphics.setClip(IIII)V"
    public static final int SET_COLOR = -809966574;//"Graphics.setColor(III)V"
    public static final int SET_DEFAULT_FONT = 1927614404;//"Graphics.setDefaultFont()V"
    public static final int SET_FONT = -110902593;//"Graphics.setFont(#Graphics.Font;)V"
    public static final int GET_IMAGE_WIDTH = 1176489994;//"Graphics.getImageWidth(#Graphics.Image;)I"
    public static final int GET_IMAGE_HEIGHT = -985293639;//"Graphics.getImageHeight(#Graphics.Image;)I"
    public static final int GET_STRING_WIDTH = -226865331;//"Graphics.getStringWidth(S)I"
    public static final int GET_CLIP_X = -1447661037;//"Graphics.getClipX()I"
    public static final int GET_CLIP_Y = -1447631246;//"Graphics.getClipY()I"
    public static final int GET_CLIP_WIDTH = -1811361659;//"Graphics.getClipWidth()I"
    public static final int GET_CLIP_HEIGHT = -1879263074;//"Graphics.getClipHeight()I"
    public static final int GET_FONT = 383125840;//"Graphics.getFont(SII)#Graphics.Font;"
    public static final int GET_FONT_HEIGHT = 361821869;//"Graphics.getFontHeight(#Graphics.Font;)I"
    public static final int GET_DEFAULT_FONT = -51628178;//"Graphics.getDefaultFont()#Graphics.Font;"
    private static Graphics2D graphics;
    private static int ascent;

    public void constructor() {
    }

    public Object invoke(int functionId, Object[] args) {
        switch (functionId) {
            case DRAW_ARC: {
                int x = ((int[]) args[0])[0];
                int y = ((int[]) args[1])[0];
                int width = ((int[]) args[2])[0];
                int height = ((int[]) args[3])[0];
                int startAngle = ((int[]) args[4])[0];
                int arcAngle = ((int[]) args[5])[0];
                graphics.drawArc(x, y, width, height, startAngle, arcAngle);
                return null;
            }
            case DRAW_IMAGE: {
            }
            case DRAW_LINE: {
                int x1 = ((int[]) args[0])[0];
                int x2 = ((int[]) args[1])[0];
                int x3 = ((int[]) args[2])[0];
                int x4 = ((int[]) args[3])[0];
                graphics.drawLine(x1, x2, x3, x4);
                return null;
            }
            case DRAW_ROUND_RECT: {
                int x = ((int[]) args[0])[0];
                int y = ((int[]) args[1])[0];
                int width = ((int[]) args[2])[0];
                int height = ((int[]) args[3])[0];
                int arcWidth = ((int[]) args[4])[0];
                int arcHeight = ((int[]) args[5])[0];
                graphics.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
                return null;
            }
            case DRAW_STRING: {
                String str = ((String[]) args[0])[0];
                int x = ((int[]) args[1])[0];
                int y = ((int[]) args[2])[0];
                graphics.drawString(str, x, y + ascent);
                return null;
            }
            case DRAW_POLYGON: {
                int[] xPoints = (int[]) args[0];
                int[] yPoints = (int[]) args[1];
                if (xPoints.length != yPoints.length) {
                    throw new RuntimeException("length(x) != length(y)");
                }
                graphics.drawPolygon(xPoints, yPoints, xPoints.length);
                return null;
            }
            case DRAW_POLYLINE: {
                int[] xPoints = (int[]) args[0];
                int[] yPoints = (int[]) args[1];
                if (xPoints.length != yPoints.length) {
                    throw new RuntimeException("length(x) != length(y)");
                }
                graphics.drawPolyline(xPoints, yPoints, xPoints.length);
                return null;
            }
            case FILL_ARC: {
                int x = ((int[]) args[0])[0];
                int y = ((int[]) args[1])[0];
                int width = ((int[]) args[2])[0];
                int height = ((int[]) args[3])[0];
                int startAngle = ((int[]) args[4])[0];
                int arcAngle = ((int[]) args[5])[0];
                graphics.fillArc(x, y, width, height, startAngle, arcAngle);
                return null;
            }
            case FILL_RECT: {
                int x = ((int[]) args[0])[0];
                int y = ((int[]) args[1])[0];
                int width = ((int[]) args[2])[0];
                int height = ((int[]) args[3])[0];
                graphics.fillRect(x, y, width, height);
                return null;
            }
            case FILL_ROUND_RECT: {
                int x = ((int[]) args[0])[0];
                int y = ((int[]) args[1])[0];
                int width = ((int[]) args[2])[0];
                int height = ((int[]) args[3])[0];
                int arcWidth = ((int[]) args[4])[0];
                int arcHeight = ((int[]) args[5])[0];
                graphics.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
                return null;
            }
            case FILL_OVAL: {
                int x = ((int[]) args[0])[0];
                int y = ((int[]) args[1])[0];
                int width = ((int[]) args[2])[0];
                int height = ((int[]) args[3])[0];
                graphics.fillOval(x, y, width, height);
                return null;
            }
            case FILL_POLYGON: {
                int[] xPoints = (int[]) args[0];
                int[] yPoints = (int[]) args[1];
                if (xPoints.length != yPoints.length) {
                    throw new RuntimeException("length(x) != length(y)");
                }
                graphics.fillPolygon(xPoints, yPoints, xPoints.length);
                return null;
            }
            case LOAD_IMAGE: {
            }
            case REPAINT: {
            }
            case SET_CLIP: {
                int x = ((int[]) args[0])[0];
                int y = ((int[]) args[1])[0];
                int width = ((int[]) args[2])[0];
                int height = ((int[]) args[3])[0];
                graphics.setClip(x, y, width, height);
                return null;
            }
            case SET_COLOR: {
                int red = ((int[]) args[0])[0];
                int green = ((int[]) args[1])[0];
                int blue = ((int[]) args[2])[0];
                graphics.setColor(new Color(red, green, blue));
                return null;
            }
            case SET_DEFAULT_FONT: {
            }
            case SET_FONT: {
            }
            case GET_IMAGE_WIDTH: {
            }
            case GET_IMAGE_HEIGHT: {
            }
            case GET_STRING_WIDTH: {
                String str = ((String[]) args[0])[0];
                return new int[]{graphics.getFontMetrics().stringWidth(str)};
            }
            case GET_CLIP_X: {
                return new int[]{graphics.getClipBounds().x};
            }
            case GET_CLIP_Y: {
                return new int[]{graphics.getClipBounds().y};
            }
            case GET_CLIP_WIDTH: {
                return new int[]{graphics.getClipBounds().width};
            }
            case GET_CLIP_HEIGHT: {
                return new int[]{graphics.getClipBounds().height};
            }
            case GET_FONT: {
            }
            case GET_FONT_HEIGHT: {
            }
            case GET_DEFAULT_FONT: {
            }
        }
        throw new RuntimeException(getName() + "[" + functionId + "]");
    }

    public String getName() {
        return "Graphics";
    }

    public void destructor() {
    }
}
