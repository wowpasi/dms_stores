/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

/**
 *
 * @author wijay
 */
interface Shapes {

    void draw();

    void paint();

}

class Rectangle implements Shapes {

    @Override
    public void draw() {
        System.out.println("Rectangle");
    }

    @Override
    public void paint() {
        System.out.println("Red");
    }

}

class Square implements Shapes {

    @Override
    public void draw() {
        System.out.println("Square");
    }

    @Override
    public void paint() {
        System.out.println("Red");
    }

}

class Circle implements Shapes {

    @Override
    public void draw() {
        System.out.println("Circle");
    }

    @Override
    public void paint() {
        System.out.println("Red");
    }

}

class ShapeCreator {

    private Shapes square;
    private Shapes rectangle;
    private Shapes circle;

    public ShapeCreator() {
        square = new Square();
        this.rectangle = new Rectangle();
        this.circle = new Circle();
    }
    
   public void drawSquare(){
   square.draw();
   }
   public void paintSqure(){
   square.paint();
   }
}

public class FacadeTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ShapeCreator shapeCreator=new ShapeCreator();
        shapeCreator.drawSquare();
        shapeCreator.paintSqure();
    }

}
