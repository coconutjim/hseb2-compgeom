import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Lev on 08.03.14.
 */
public class Face {

    /** Полуребро на внешней границе */
    private HalfEdge outerComponent;

    /** Список полуребер на "дыры" */
    private ArrayList<HalfEdge> innerComponents;

    /** Цвет */
    private Color color;

    public Face() {

    }

    public Face(HalfEdge outerComponent, ArrayList<HalfEdge> innerComponents) {
        this.outerComponent = outerComponent;
        this.innerComponents = innerComponents;
    }

    public Face(Face o) {
        this.outerComponent = o.getOuterComponent();
        this.innerComponents = o.getInnerComponents();
        this.color = o.getColor();
    }

    public HalfEdge getOuterComponent() {
        return outerComponent;
    }

    public void setOuterComponent(HalfEdge outerComponent) {
        this.outerComponent = outerComponent;
    }

    public ArrayList<HalfEdge> getInnerComponents() {
        return innerComponents;
    }

    public void setInnerComponents(ArrayList<HalfEdge> innerComponents) {
        this.innerComponents = innerComponents;
    }

    public void addInnerComponent(HalfEdge innerComponent) {
        if (innerComponents == null) {
            innerComponents =  new ArrayList<HalfEdge>();
        }
        innerComponents.add(innerComponent);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
