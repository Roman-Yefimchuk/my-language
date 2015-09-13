package dina.library.libs;

import ide.console.*;
import ide.console.terminal.*;
import dina.library.*;
import dina.runtime.*;
import dina.runtime.variables.*;
import dina.runtime.variables.record.*;
import sun.font.*;
import java.awt.*;

public class GraphicsLibrary extends Library {

    private static Graphics2D graphics;
    private static Terminal display;
    private static int ascent;
    public static final Toolkit toolkit = Toolkit.getDefaultToolkit();

    public static void updateLibrary() {
        display = Console.getTerminal();
        graphics = display.getTerminalGraphics();
        ascent = graphics.getFontMetrics().getAscent();
        graphics.setClip(0, 0, Console.width, Console.height);
    }

    @Override
    public void initLibrary() {
        updateLibrary();
    }

    @Override
    public void invoke(int functionID) {
        if (Terminal.state != Terminal.STATE_VGA) {
            invalidConsoleState();
        }
        switch (functionID) {
            case FunctionsID.DRAW_ARC: {
                int n1 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                int n2 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                int n3 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                int n4 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                int n5 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                int n6 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                graphics.drawArc(n1, n2, n3, n4, n5, n6);
                return;
            }
            case FunctionsID.DRAW_IMAGE: {
                int n1 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                int n2 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                Image image = getImage();
                graphics.drawImage(image, n1, n2, null);
                return;
            }
            case FunctionsID.DRAW_LINE: {
                int n1 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                int n2 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                int n3 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                int n4 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                graphics.drawLine(n1, n2, n3, n4);
                return;
            }
            case FunctionsID.DRAW_RECT: {
                int n1 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                int n2 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                int n3 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                int n4 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                graphics.drawRect(n1, n2, n3, n4);
                return;
            }
            case FunctionsID.DRAW_ROUND_RECT: {
                int n1 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                int n2 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                int n3 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                int n4 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                int n5 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                int n6 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                graphics.drawRoundRect(n1, n2, n3, n4, n5, n6);
                return;
            }
            case FunctionsID.DRAW_STRING: {
                String s1 = ((String[]) DinaVM.operands[DinaVM.pointer--])[0];
                int n1 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                int n2 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                graphics.drawString(s1, n1, n2 + ascent);
                return;
            }
            case FunctionsID.DRAW_SUBSTRING: {
                String s1 = ((String[]) DinaVM.operands[DinaVM.pointer--])[0];
                int n1 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                int n2 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                int n3 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                int n4 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                graphics.drawString(s1.substring(n1, n2), n3, n4);
                return;
            }
            case FunctionsID.FILL_ARC: {
                int n1 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                int n2 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                int n3 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                int n4 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                int n5 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                int n6 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                graphics.fillArc(n1, n2, n3, n4, n5, n6);
                return;
            }
            case FunctionsID.FILL_RECT: {
                int n1 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                int n2 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                int n3 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                int n4 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                graphics.fillRect(n1, n2, n3, n4);
                return;
            }
            case FunctionsID.FILL_ROUND_RECT: {
                int n1 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                int n2 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                int n3 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                int n4 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                int n5 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                int n6 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                graphics.fillRoundRect(n1, n2, n3, n4, n5, n6);
                return;
            }
            case FunctionsID.GET_WIDTH: {
                DinaVM.operands[++DinaVM.pointer] = new int[]{Console.width};
                return;
            }
            case FunctionsID.GET_HEIGHT: {
                DinaVM.operands[++DinaVM.pointer] = new int[]{Console.height};
                return;
            }
            case FunctionsID.GET_IMAGE_WIDTH: {
                int imageWidth = getImage().getWidth(null);
                DinaVM.operands[++DinaVM.pointer] = new int[]{imageWidth};
                return;
            }
            case FunctionsID.GET_IMAGE_HEIGHT: {
                int imageHeight = getImage().getHeight(null);
                DinaVM.operands[++DinaVM.pointer] = new int[]{imageHeight};
                return;
            }
            case FunctionsID.GET_STRING_WIDTH: {
                String s1 = ((String[]) DinaVM.operands[DinaVM.pointer--])[0];
                DinaVM.operands[++DinaVM.pointer] = new int[]{graphics.getFontMetrics().stringWidth(s1)};
                return;
            }
            case FunctionsID.GET_SUBSTRING_WIDTH: {
                int n1 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                int n2 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                String s1 = ((String[]) DinaVM.operands[DinaVM.pointer--])[0];
                DinaVM.operands[++DinaVM.pointer] = new int[]{graphics.getFontMetrics().stringWidth(s1.substring(n1, n2))};
                return;
            }
            case FunctionsID.GET_FONT_HEIGHT: {
                DinaVM.operands[++DinaVM.pointer] = new int[]{graphics.getFontMetrics().getHeight()};
                return;
            }
            case FunctionsID.LOAD_IMAGE: {
                String imageName = ((String[]) DinaVM.operands[DinaVM.pointer--])[0];
                Object imageContainer = loadImage(imageName);
                DinaVM.operands[++DinaVM.pointer] = imageContainer;
                return;
            }
            case FunctionsID.PLOT: {
                int n1 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                int n2 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                graphics.drawLine(n1, n2, n1, n2);
                return;
            }
            case FunctionsID.REPAINT: {
                display.repaint();
                return;
            }
            case FunctionsID.REPAINT_AREA: {
                int n1 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                int n2 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                int n3 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                int n4 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                display.repaint(n1, n2, n3, n4);
                return;
            }
            case FunctionsID.SET_CLIP: {
                int n1 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                int n2 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                int n3 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                int n4 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                graphics.setClip(n1, n2, n3, n4);
                return;
            }
            case FunctionsID.GET_CLIP_X: {
                DinaVM.operands[++DinaVM.pointer] = new int[]{graphics.getClipBounds().x};
                return;
            }
            case FunctionsID.GET_CLIP_Y: {
                DinaVM.operands[++DinaVM.pointer] = new int[]{graphics.getClipBounds().y};
                return;
            }
            case FunctionsID.GET_CLIP_WIDTH: {
                DinaVM.operands[++DinaVM.pointer] = new int[]{graphics.getClipBounds().width};
                return;
            }
            case FunctionsID.GET_CLIP_HEIGHT: {
                DinaVM.operands[++DinaVM.pointer] = new int[]{graphics.getClipBounds().height};
                return;
            }
            case FunctionsID.SET_COLOR: {
                int n1 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                int n2 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                int n3 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                graphics.setColor(new Color(n1, n2, n3));
                return;
            }
            case FunctionsID.SET_DEFAULT_FONT: {
                graphics.setFont(Terminal.DEFAULT_FONT);
                ascent = graphics.getFontMetrics().getAscent();
                return;
            }
            case FunctionsID.SET_FONT: {
                Font font = getFont();
                graphics.setFont(font);
                ascent = graphics.getFontMetrics().getAscent();
                return;
            }
            case FunctionsID.DRAW_OVAL: {
                int n1 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                int n2 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                int n3 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                int n4 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                graphics.drawOval(n1, n2, n3, n4);
                return;
            }
            case FunctionsID.FILL_OVAL: {
                int n1 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                int n2 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                int n3 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                int n4 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                graphics.fillOval(n1, n2, n3, n4);
                return;
            }
            case FunctionsID.DRAW_POLYGON: {
                int[] x = (int[]) DinaVM.operands[DinaVM.pointer--];
                int[] y = (int[]) DinaVM.operands[DinaVM.pointer--];
                if (x.length != y.length) {
                    throw new RuntimeException("length(x) != length(y)");
                }
                graphics.drawPolygon(x, y, x.length);
                return;
            }
            case FunctionsID.DRAW_POLYLINE: {
                int[] x = (int[]) DinaVM.operands[DinaVM.pointer--];
                int[] y = (int[]) DinaVM.operands[DinaVM.pointer--];
                if (x.length != y.length) {
                    throw new RuntimeException("length(x) != length(y)");
                }
                graphics.drawPolyline(x, y, x.length);
                graphics.drawPolygon(null);
                return;
            }
            case FunctionsID.FILL_POLYGON: {
                int[] x = (int[]) DinaVM.operands[DinaVM.pointer--];
                int[] y = (int[]) DinaVM.operands[DinaVM.pointer--];
                if (x.length != y.length) {
                    throw new RuntimeException("length(x) != length(y)");
                }
                graphics.fillPolygon(x, y, x.length);
                return;
            }
            case FunctionsID.GET_FONT: {
                String s1 = ((String[]) DinaVM.operands[DinaVM.pointer--])[0];
                int n1 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                int n2 = ((int[]) DinaVM.operands[DinaVM.pointer--])[0];
                Font font = new Font(s1, n1, n2);
                DinaVM.operands[++DinaVM.pointer] = new Object[]{new RecordContainer[]{new RecordContainer(font)}};
                return;
            }
            case FunctionsID.GET_FONT_HEIGHT_FROM_FONT: {
                Font font = getFont();
                DinaVM.operands[++DinaVM.pointer] = new int[]{FontDesignMetrics.getMetrics(font).getHeight()};
                return;
            }
        }
    }

    public static Image getImage() {
        RecordContainer imageContainer = (RecordContainer) DinaVM.operands[DinaVM.pointer--];
        Image image = (Image) imageContainer.getObject();
        if (image != null) {
            return image;
        }
        throw new RuntimeException("Изображение пусто");
    }

    public static Font getFont() {
        RecordContainer fontContainer = ((RecordContainer) ((Variable[]) ((Object[]) DinaVM.operands[DinaVM.pointer--])[Record.OBJECT])[0]);
        Font font = (Font) fontContainer.getObject();
        if (font == null) {
            return Terminal.DEFAULT_FONT;
        }
        return font;
    }

    public Object loadImage(String imageName) {
        Image image = toolkit.createImage(getClass().getResource(imageName));
        return new Object[]{new RecordContainer[]{new RecordContainer(image)}};
    }

    public String getLibratyName() {
        return "graphics";
    }
}
