/**
 * AppletJuego
 *
 * Personaje para juego previo Examen
 *
 * @author Luis Alberto Lamadrid - A01191158  
 * @author Jeronimo Martinez - A01191487
 * @version 1.00 2008/6/13
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;
import java.util.LinkedList;
import javax.swing.JFrame;

public class JFrameExamen extends JFrame implements Runnable, KeyListener {

    /* objetos para manejar el buffer del Applet y este no parpadee */
    private Image    imaImagenApplet;   // Imagen a proyectar en Applet	
    private Graphics graGraficaApplet;  // Objeto grafico de la Imagen
    private int iVidas;                 // Vidas del juego
    private int iScore;                 // Score del juego
    private int iDireccion;             // Direccion de la Nena
    private int iAcumCorredores;        // Corredores acumulados en la colision
    private Personaje perNena;          // Objeto Nena de la clase personaje
    private LinkedList lstCaminadores;  // Lista de 8 a 10 caminadores
    private LinkedList lstCorredores;   // Lista de 10 a 15 corredores
    
    /* objetos de audio */
    private SoundClip aucSonidoSuccess; // Objeto AudioClip sonido Caminador
    private SoundClip aucSonidoFailure; // Objeto AudioClip sonido Corredor
    
    public JFrameExamen(){
        init();
        start();
    }
    	
    /** 
     * init
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>.<P>
     * En este metodo se inizializan las variables o se crean los objetos
     * a usarse en el <code>Applet</code> y se definen funcionalidades.
     */
    public void init() {
        // hago el applet de un tamaño 500,500
        setSize(800, 600);
        
        // Se inicializan las vidas al azar de 3 a 5
        iVidas = (int) ((Math.random() * 2) + 3);
        
        // inicializa el score con 0
        iScore = 0;
        
        // inicializa el acumulado en cero
        iAcumCorredores = 0;
        
	//creo el sonido del caminador
        aucSonidoSuccess = new SoundClip ("success.wav");
        
	//creo el sonido del corredor
        aucSonidoFailure = new SoundClip ("failure.wav");
        
        // cargo la imagen de Nena
	URL urlImagenNena = this.getClass().getResource("nena.gif");
        
        // se crea el objeto Nena 
	perNena = new Personaje(0,0,
                Toolkit.getDefaultToolkit().getImage(urlImagenNena));
        
        // se posiciona Nena en el centro de la pantalla con velocidad 3
	int posX = (getWidth() / 2) - perNena.getAncho();    
        int posY = (getHeight() / 2) - perNena.getAlto();
        perNena.setX(posX);
        perNena.setY(posY);
        perNena.setVelocidad(3);
        
        // se crea la lista de caminadores y su numero de 8 a 10
        lstCaminadores = new LinkedList();
        int iNUM_CAMINADORES = (int) ((Math.random() * 2) + 8);
        
        // creando la imagen del caminador
        URL urlImagenCaminador = this.getClass().getResource("alien1Camina.gif");

        // se crean los caminadores de acuerdo con el iNUM_CAMINADORES
        for (int iI = 0; iI < iNUM_CAMINADORES ; iI++){ 
            // creando el caminador
            Personaje perCaminador = new Personaje(0,0,
                Toolkit.getDefaultToolkit().getImage(urlImagenCaminador));
            
            // creando las posiciones alazar del caminador
            int iRandomX = (int) ((Math.random() * 150) + 50);
            posX = (0 - perCaminador.getAncho()) - iRandomX;    
            posY = (int) (Math.random() * (getHeight()  -  
                          perCaminador.getAlto()));
            perCaminador.setX(posX);
            perCaminador.setY(posY);
            
            // poniendo velocidad entre 3 y 5
            int iVel = (int) ((Math.random() * 2) + 3);
            perCaminador.setVelocidad(iVel);
            
            // se agregan a la lista
            lstCaminadores.add(perCaminador);
        }
        
        // se crea la lista de corredores y su numero de 10 a 15
        lstCorredores = new LinkedList();
        int iNUM_C0RREDORES = (int) ((Math.random() * 5) + 10);
        
        // creando la imagen del corredor
        URL urlImagenCorredor = this.getClass().getResource("alien2Corre.gif");

        // se crean los caminadores de acuerdo con el iNUM_CORREDORES
        for (int iI = 0; iI < iNUM_C0RREDORES ; iI++){ 
            // creando el caminador
            Personaje perCorredor = new Personaje(0,0,
                Toolkit.getDefaultToolkit().getImage(urlImagenCorredor));
            
            // creando las posiciones alazar del corredor
            int iRandomY = (int) ((Math.random() * 150) + 50);               
            posX = (int) (Math.random() * (getWidth()  -  
                          perCorredor.getAncho()));
            posY = (0 - perCorredor.getAlto()) - iRandomY; 
            perCorredor.setX(posX);
            perCorredor.setY(posY);
            
            // poniendo velocidad ependiendo de las vidas
            perCorredor.setVelocidad(11/iVidas); 
            
            // se agregan a la lista
            lstCorredores.add(perCorredor);
        }
              
        // introducir instrucciones para iniciar juego
        // se define el background en color amarillo
	setBackground (Color.yellow);
        addKeyListener(this);
    }
	
    /** 
     * start
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>.<P>
     * En este metodo se crea e inicializa el hilo
     * para la animacion este metodo es llamado despues del init o 
     * cuando el usuario visita otra pagina y luego regresa a la pagina
     * en donde esta este <code>Applet</code>
     * 
     */
    public void start () {
        // Declaras un hilo
        Thread th = new Thread (this);
        // Empieza el hilo
        th.start ();
    }
	
    /** 
     * run
     * 
     * Metodo sobrescrito de la clase <code>Thread</code>.<P>
     * En este metodo se ejecuta el hilo, que contendrá las instrucciones
     * de nuestro juego.
     * 
     */
    public void run () {
        // se realiza el ciclo del juego en este caso nunca termina
        while (iVidas > 0) {
            /* mientras dure el juego, se actualizan posiciones de jugadores
               se checa si hubo colisiones para desaparecer jugadores o corregir
               movimientos y se vuelve a pintar todo
            */ 
            actualiza();
            checaColision();
            repaint();
            try	{
                // El thread se duerme.
                Thread.sleep (20);
            }
            catch (InterruptedException iexError)	{
                System.out.println("Hubo un error en el juego " + 
                        iexError.toString());
            }
	}
    }
	
    /** 
     * actualiza
     * 
     * Metodo que actualiza la posicion del objeto elefante 
     * 
     */
    public void actualiza(){
        
        // mueve a la nena de direccion
        switch (iDireccion){
            case 1: {
                perNena.arriba();
                }
                break;
            case 2: {
                perNena.abajo();
                }
                break;
            case 3: {
                perNena.izquierda();
                }
                break;
            case 4: {
                perNena.derecha();
                }
                break;
        }
        
        // se mueven los caminadores
        for (Object lstCaminador : lstCaminadores){
            Personaje perCaminador = (Personaje) lstCaminador;
            
            // poniendo velocidad entre 3 y 5 cada vez que avanza
            int iVel = (int) ((Math.random() * 2) + 3);
            perCaminador.setVelocidad(iVel);
            
            // moviendo al personaje
            perCaminador.derecha();
        }
        
        // se mueven los corredores
        for (Object lstCorredor : lstCorredores){
            Personaje perCorredor = (Personaje) lstCorredor;
            
            // poniendo velocidad ependiendo de las vidas
            if (iVidas > 0) { 
                perCorredor.setVelocidad(11/iVidas);
            }
            else {
                perCorredor.setVelocidad(0);
            }
                           
            // moviendo al personaje
            perCorredor.abajo();
        }
    }
    
    /**
     * reposicionaCaminador
     * 
     * Metodo usado para reposicionar al caminador a la 
     * izquierda y fuera de la pantalla.
     * 
     * @param perCaminador
     */  
    public void reposicionaCaminador(Personaje perCaminador){
        int iRandomX = (int) ((Math.random() * 150) + 50);
        int posX = (0 - perCaminador.getAncho()) - iRandomX;    
        int posY = (int) (Math.random() * (getHeight()  -  
                          perCaminador.getAlto()));
        perCaminador.setX(posX);
        perCaminador.setY(posY);     
    }
    
    /**
     * reposicionaCaminador
     * 
     * Metodo usado para reposicionar al corredor arriba y fuera de la pantalla.
     * 
     * @param perCorredor
     */  
    public void reposicionaCorredor(Personaje perCorredor){
            int iRandomY = (int) ((Math.random() * 150) + 50);               
            int posX = (int) (Math.random() * (getWidth()  -  
                          perCorredor.getAncho()));
            int posY = (0 - perCorredor.getAlto()) - iRandomY; 
            perCorredor.setX(posX);
            perCorredor.setY(posY);    
    }
	
    /**
     * checaColision
     * 
     * Metodo usado para checar la colision del objeto elefante
     * con las orillas del <code>Applet</code>.
     * 
     */
    public void checaColision(){
        
        // checando que nena no se salga de la pantalla
        if (perNena.getX() < 0){
            perNena.setX(0);
        }
        else if ((perNena.getX() + perNena.getAncho()) > getWidth()){
            perNena.setX(getWidth() - perNena.getAncho());
        }
        if (perNena.getY() < 0){
            perNena.setY(0);
        }
        else if ((perNena.getY() + perNena.getAlto()) > getHeight()){
            perNena.setY(getHeight() - perNena.getAlto());
        }
        
        // Checa colision de caminadores con la nena
        for (Object lstCaminador : lstCaminadores){
            Personaje perCaminador = (Personaje) lstCaminador;
            if (perCaminador.colisiona(perNena)){
                iScore += 1;
                aucSonidoSuccess.play();
                reposicionaCaminador(perCaminador);
            }
            if (perCaminador.getX() > (getWidth() - perCaminador.getAncho())){
                reposicionaCaminador(perCaminador);
            }
        }
        
        // Checa colision de corredores con la nena
        for (Object lstCorredor : lstCorredores){
            Personaje perCorredor = (Personaje) lstCorredor;
            if (perCorredor.colisiona(perNena)){
                reposicionaCorredor(perCorredor);
                aucSonidoFailure.play();
                iAcumCorredores++;
                
                // checa el acumulado de corredores para ver s ya son 5
                if (iAcumCorredores == 5){
                    iAcumCorredores = 0;
                    iVidas -= 1; // decrementa las vidas
                }
            }
            if (perCorredor.getY() > (getHeight() - perCorredor.getAlto())){
                reposicionaCorredor(perCorredor);
            }
        }
    }
	
    /**
     * paint
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>,
     * heredado de la clase Container.<P>
     * En este metodo lo que hace es actualizar el contenedor y 
     * define cuando usar ahora el paint
     * @param graGrafico es el <code>objeto grafico</code> usado para dibujar.
     * 
     */
    public void paint (Graphics graGrafico){
        // Inicializan el DoubleBuffer
        if (imaImagenApplet == null){
                imaImagenApplet = createImage (this.getSize().width, 
                        this.getSize().height);
                graGraficaApplet = imaImagenApplet.getGraphics ();
        }
        
        // creo imagen para el background
        URL urlImagenEspacio = this.getClass().getResource("espacio.jpg");
        Image imaImagenEspacio = Toolkit.getDefaultToolkit().getImage(urlImagenEspacio);
        
        // Despliego la imagen
        graGraficaApplet.drawImage(imaImagenEspacio, 0, 0, 
                getWidth(), getHeight(), this);

        // Actualiza la imagen de fondo.
        graGraficaApplet.setColor (getBackground ());

        // Actualiza el Foreground.
        graGraficaApplet.setColor (getForeground());
        paint_buffer(graGraficaApplet);
        

        // Dibuja la imagen actualizada
        graGrafico.drawImage (imaImagenApplet, 0, 0, this);
    }
    
    /**
     * paint
     * 
     * Metodo sobrescrito de la clase <code>Applet</code>,
     * heredado de la clase Container.<P>
     * En este metodo se dibuja la imagen con la posicion actualizada,
     * ademas que cuando la imagen es cargada te despliega una advertencia.
     * @param g es el <code>objeto grafico</code> usado para dibujar.
     * 
     */
    public void paint_buffer(Graphics g) {
        
        // le pone color a todos los textos
        g.setColor(Color.red);
        
        // checa si es GAME OVER para desplegar la imagen :)
        if (iVidas == 0){
            // creo imagen para el background
            URL urlImagenOVER = this.getClass().getResource("game_over.png");
            Image imaImagenOVER = Toolkit.getDefaultToolkit().getImage(urlImagenOVER);
            graGraficaApplet.drawImage(imaImagenOVER, ((getWidth() / 2) - 283), 
                    ((getHeight() / 2) - 121), 566, 243, this);
            return;
        }
        
        // si la imagen ya se cargo
        if (perNena != null && lstCaminadores.size() > 0 
            && lstCorredores.size() > 0) {
                //Dibuja la imagen de Nena en la posicion actualizada
                g.drawImage(perNena.getImagen(), perNena.getX(),
                        perNena.getY(), this);
                // dibuja a los caminadores
                for (Object lstCaminador : lstCaminadores){
                    Personaje perCaminador = (Personaje) lstCaminador;
                     g.drawImage(perCaminador.getImagen(), perCaminador.getX(),
                        perCaminador.getY(), this);
                }
                // dibuja a los corredores
                for (Object lstCorredor : lstCorredores){
                    Personaje perCorredor = (Personaje) lstCorredor;
                     g.drawImage(perCorredor.getImagen(), perCorredor.getX(),
                        perCorredor.getY(), this);
                }
        } // sino se ha cargado se dibuja un mensaje 
        else {
                //Da un mensaje mientras se carga el dibujo	
                g.drawString("No se cargo la imagen..", 20, 20);
        }
        
        // dibuja el score y las vidas
        g.drawString("Vidas: " + String.valueOf(iVidas), 10, 40);
        g.drawString("Score: " + String.valueOf(iScore), 10, 60);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        // si presiono W  (arriba)
        if(keyEvent.getKeyCode() == KeyEvent.VK_W){
            iDireccion = 1;
        }   
        // si presiono S  (abajo)
        if(keyEvent.getKeyCode() == KeyEvent.VK_S){
            iDireccion = 2;

        } 
        // si presiono A  (izquierda)
        if(keyEvent.getKeyCode() == KeyEvent.VK_A) { 
            iDireccion = 3;
        }
        // si presiono D  (derecha)
        if(keyEvent.getKeyCode() == KeyEvent.VK_D) { 
            iDireccion = 4;
        }
    }
}