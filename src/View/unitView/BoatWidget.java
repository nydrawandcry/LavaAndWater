package View.unitView;
import Model.events.units.BoatActionListener;
import Model.units.interactive.Boat;

import java.awt.*;

public class BoatWidget extends UnitWidget{

    private static int WIDTH = 50;
    private static int HEIGHT = 25;
    private final BoatActionListener _listener = new BoatActionHandler();
    public BoatWidget(Boat boat) {
        super(boat);
        boat.addBoatActionListener(_listener);

        setOpaque(false);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        changeColorByActivity();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color color = _unit.isActive() ? getActiveColor() : getInactiveColor();

        //главное тело
        g2d.setColor(color);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        //рамка посветлее
        g2d.setColor(new Color(65,145,75));
        g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
    }

    @Override
    public int getRenderPriority() {
        return 150;
    }

    @Override
    protected Color getActiveColor() {
        return new Color(55,75,55);
    }

    @Override
    protected Color getInactiveColor() {
        return new Color( 45,35,45);
    }

    private class BoatActionHandler implements BoatActionListener {
        @Override
        public void boatMoved() {
            //
        }
    }
}
