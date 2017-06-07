package mygame.chasecamera;

import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import java.util.ArrayList;
import java.util.List;
/** Sample 8 - how to let the user pick (select) objects in the scene
 * using the mouse or key presses. Can be used for shooting, opening doors, etc. */
public class TesteTiro extends SimpleApplication {  public static void main(String[] args) {
    
      new TesteTiro().start();
    
  }
  Node mundo;
  Geometry marca;
  List<String> CubeNames = new ArrayList();
  
  @Override
  public void simpleInitApp() {
    initCrossHairs(); // a "+" in the middle of the screen to help aiming
    initKeys();       // load custom key mappings
    initMark();       // a red sphere to mark the hit
    /** create four colored boxes and a floor to shoot at: */
    mundo = new Node("mesa");
    rootNode.attachChild(mundo);
    
    mundo.attachChild(makeFloor());
    createCubes();
  }
  
  private void initKeys() {
    inputManager.addMapping("Tiro", new KeyTrigger(KeyInput.KEY_SPACE));
    inputManager.addListener(actionListener, "Tiro");
  }
  
  private void createCubes(){
    mundo.attachChild(makeCube("Objeto 1", -2f, 0f, 1f));
    mundo.attachChild(makeCube("Objeto 2", 1f,-2f, 0f));
    mundo.attachChild(makeCube("Objeto 3", 0f, 1f,-2f));
    mundo.attachChild(makeCube("Objeto 4", 1f, 0f,-4f));
    CubeNames.add("Objeto 1");
    CubeNames.add("Objeto 2");
    CubeNames.add("Objeto 3");
    CubeNames.add("Objeto 4");
  }
  
  private void removeCubes(){
      mundo.detachChildNamed("Objeto 1");
      mundo.detachChildNamed("Objeto 2");
      mundo.detachChildNamed("Objeto 3");
      mundo.detachChildNamed("Objeto 4");
  }
  
  private void removeCube(String name){
      System.out.println(name);
      if(!"the Floor".equals(name))
      {
         mundo.detachChildNamed(name);
         CubeNames.remove(name);
         if(CubeNames.size() < 1)
             createCubes();
      }
  }
  
  private ActionListener actionListener = new ActionListener() {
    @Override
    public void onAction(String name, boolean keyPressed, float tpf) {
       if (name.equals("Tiro") && !keyPressed) {
         // 1. Reinicia lista de resultados
        CollisionResults results = new CollisionResults();
        // 2. Redife o raio em função da localicação e direção da camera.
        Ray ray = new Ray(cam.getLocation(), cam.getDirection());
        // 3. Verifica colisões e guarda em results
        mundo.collideWith(ray, results);
        // 4. Imprime colisões
        System.out.println("----- Colisões? " + results.size() + "-----");
        for (int i = 0; i < results.size(); i++) {
          float dist = results.getCollision(i).getDistance();
          Vector3f pt = results.getCollision(i).getContactPoint();
          String hit = results.getCollision(i).getGeometry().getName();
          System.out.println("O ray colidiu " + hit + " na posição " + pt + 
                  ", " + dist + " de distância.");
          removeCube(hit);
        }
        if (results.size() > 0){ // 5. Açao da colisão
          // Qual é a colisão mais próxima?
          CollisionResult closest = results.getClosestCollision();
          System.out.println("O mais proximo é: " + closest.getGeometry().getName());
          //Posiciona a marca.
          marca.setLocalTranslation(closest.getContactPoint());
          rootNode.attachChild(marca);
        } else {
        // Remove a marca
          rootNode.detachChild(marca);
        }
      }
    }
  };
  /** A cube object for target practice */
  protected Geometry makeCube(String name, float x, float y, float z) {
    Box box = new Box(new Vector3f(x, y, z), 1, 1, 1);
    Geometry cube = new Geometry(name, box);
    Material mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    mat1.setColor("Color", ColorRGBA.randomColor());
    cube.setMaterial(mat1);
    return cube;
  }
  /** A floor to show that the "shot" can go through several objects. */
  protected Geometry makeFloor() {
    Box box = new Box(new Vector3f(0,-4,-5), 15,.2f,15);
    Geometry floor = new Geometry("the Floor", box);
    Material mat1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    mat1.setColor("Color", ColorRGBA.Gray);
    floor.setMaterial(mat1);
    return floor;
  }
  /** A red ball that marks the last spot that was "hit" by the "shot". */
  protected void initMark() {
    Sphere sphere = new Sphere(10, 10, 0.1f);
    marca = new Geometry("BOOM!", sphere);
    Material mark_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
    mark_mat.setColor("Color", ColorRGBA.Red);
    marca.setMaterial(mark_mat);
  }
  /** A centred plus sign to help the player aim. */
  protected void initCrossHairs() {
    guiNode.detachAllChildren();
    guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
    BitmapText ch = new BitmapText(guiFont, false);
    ch.setSize(guiFont.getCharSet().getRenderedSize() * 2);
    ch.setText("+"); // crosshairs
    ch.setLocalTranslation( // center
      settings.getWidth()/2 - guiFont.getCharSet().getRenderedSize()/3*2,
      settings.getHeight()/2 + ch.getLineHeight()/2, 0);
    guiNode.attachChild(ch);
  }
  
  
}






