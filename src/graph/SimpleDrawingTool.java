package graph;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import com.bric.swing.ColorPicker;

public class SimpleDrawingTool extends JFrame implements ActionListener, KeyListener

{
	private static final int kControlA = 65;
	private static final int kControlD = 68;
	private static final int kControlC = 67;
	private static final int kControlO = 79;
	private static final int kControlL = 76;
	private static final int kControlN = 78;
	private static final int kControlS = 83;
	private static final int kControlR = 82;
	private static final int kControlP = 80;
	private static final int kControlT = 84;
	private static final int kControlX = 88;

	private static final String TITLE = "Paint++";
	private JScrollPane sp;

	JFileChooser fileChooser = new JFileChooser();

	private String fileName = null;
	private File f = null;
	private Boolean dataChanged = false;

	private RectangleShape rectangle = new RectangleShape();
	private OvalShape oval = new OvalShape();
	private PolygonShape polygon = new PolygonShape();
	private TriangleShape triangle = new TriangleShape();

	private DrawingPanel panel;

	JFileChooser fc = new JFileChooser(new File("."));
	final String[] EXTENSIONS = { ".png", ".jpg", ".gif", ".bmp", ".tif" };

	public SimpleDrawingTool() {
		super("Simple Drawing Tool");
		fc.addChoosableFileFilter(new MyFileFilter(EXTENSIONS));
		addMenu();
		addPanel();
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		this.setSize(1500, 1000);
		this.setVisible(true);
		this.setTitle("untitled");
	}

	public static void main(String[] args) {
		SimpleDrawingTool simpleDrawingTool = new SimpleDrawingTool();
	}

	private void addMenu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenu shape = new JMenu("Shapes");
		JMenu colour = new JMenu("Colour");
		JMenu text = new JMenu("Text");
		JMenu about = new JMenu("About");

		//File
		JMenuItem neww = new JMenuItem("New");
		neww.setMnemonic(KeyEvent.VK_N);
		neww.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, Event.CTRL_MASK));
		neww.addActionListener(this);
		JMenuItem open = new JMenuItem("Open");
		open.setMnemonic(KeyEvent.VK_O);
		open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Event.CTRL_MASK));
		open.addActionListener(this);
		JMenuItem save = new JMenuItem("Save");
		save.setMnemonic(KeyEvent.VK_S);
		save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK));
		save.addActionListener(this);
		JMenuItem saveAs = new JMenuItem("SaveAs");
		saveAs.addActionListener(this);
		JMenuItem exit = new JMenuItem("Exit");
		exit.setMnemonic(KeyEvent.VK_X);
		exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, Event.CTRL_MASK));
		exit.addActionListener(this);	
		file.add(neww);
		file.add(open);
		file.add(save);
		file.add(saveAs);
		file.addSeparator();
		file.add(exit);
		
		//Text
		JMenuItem size = new JMenuItem("Size");
		size.addActionListener(this);
		JMenuItem font = new JMenuItem("Font");
		font.addActionListener(this);
		JMenuItem style = new JMenuItem("Style");
		style.addActionListener(this);
		JMenuItem customT = new JMenuItem("Custom");
		customT.addActionListener(this);
		text.add(size);
		text.add(font);
		text.add(style);
		text.addSeparator();
		text.add(customT);
		
		//S
		JMenuItem line = new JMenuItem("Line");
		line.setMnemonic(KeyEvent.VK_L);
		line.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, Event.CTRL_MASK));
		line.addActionListener(this);
		JMenuItem rect = new JMenuItem("Rectangle");
		rect.setMnemonic(KeyEvent.VK_R);
		rect.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, Event.CTRL_MASK));
		rect.addActionListener(this);
		JMenuItem eclip = new JMenuItem("Circle");
		eclip.setMnemonic(KeyEvent.VK_C);
		eclip.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, Event.CTRL_MASK));
		eclip.addActionListener(this);
		JMenuItem tri = new JMenuItem("Triangle");
		tri.setMnemonic(KeyEvent.VK_T);
		tri.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, Event.CTRL_MASK));
		tri.addActionListener(this);
		JMenuItem poly = new JMenuItem("Polygon");
		poly.setMnemonic(KeyEvent.VK_P);
		poly.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, Event.CTRL_MASK));
		poly.addActionListener(this);
		JMenuItem dpoly = new JMenuItem("Draw Polygon");
		dpoly.setMnemonic(KeyEvent.VK_D);
		dpoly.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, Event.CTRL_MASK));
		dpoly.addActionListener(this);
		shape.add(line);
		shape.add(rect);
		shape.add(eclip);
		shape.add(tri);
		shape.add(poly);
		shape.add(dpoly);

		//Color
		JMenuItem black = new JMenuItem("Black");
		black.addActionListener(this);
		JMenuItem blue = new JMenuItem("Blue");
		blue.addActionListener(this);
		JMenuItem red = new JMenuItem("Red");
		red.addActionListener(this);
		JMenuItem green = new JMenuItem("Green");
		green.addActionListener(this);
		JMenuItem yellow = new JMenuItem("Yellow");
		yellow.addActionListener(this);
		JMenuItem cyan = new JMenuItem("Cyan");
		cyan.addActionListener(this);
		JMenuItem custom = new JMenuItem("Custom");
		custom.addActionListener(this);
		colour.add(black);
		colour.add(blue);
		colour.add(red);
		colour.add(green);
		colour.add(yellow);
		colour.add(cyan);
		colour.addSeparator();
		colour.add(custom);

		JMenuItem aboutT = new JMenuItem("About");
		aboutT.setMnemonic(KeyEvent.VK_A);
		aboutT.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, Event.CTRL_MASK));
		about.add(aboutT);
		
		menuBar.add(file);
		menuBar.add(shape);
		menuBar.add(colour);
		menuBar.add(about);
		if (null == this.getMenuBar()) {
			this.setJMenuBar(menuBar);
		}
	}

	private void addPanel() {
		panel = new DrawingPanel();
		Dimension d = this.getSize();
		Insets ins = this.insets();
		d.height = d.height - ins.top - ins.bottom;
		d.width = d.width - ins.left - ins.right;
		panel.setSize(d);
		panel.setLocation(ins.left, ins.top);
		panel.setBackground(Color.white);
		panel.addMouseListener(panel);
		this.add(panel);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equalsIgnoreCase("exit")) {
			System.exit(0);
		} 
		else if (e.getActionCommand().equalsIgnoreCase("New")) {
			int option = saveProgress();
			if (option !=2){
				if (option == 0){
					if (f != null)
						saveToFile(f);
					else {
						if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
							f = fc.getSelectedFile();
							saveToFile(f);
						}
					}
				}
				this.repaint();
				this.setTitle("untitled");
				f = null;
			}
		} 
		else if (e.getActionCommand().equalsIgnoreCase("Open")) {
			
			if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				f = fc.getSelectedFile();
				openFile(f);
			}
		} 
		else if (e.getActionCommand().equalsIgnoreCase("Save")) {

			if (f != null)
				saveToFile(f);
			else {
				if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
					f = fc.getSelectedFile();
					saveToFile(f);
				}
			}
		}
		else if (e.getActionCommand().equalsIgnoreCase("SaveAs")) {
			if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
				f = fc.getSelectedFile();
				saveToFile(f);
			}
		}
		else if (e.getActionCommand().equalsIgnoreCase("Rectangle")) {
			panel.drawShape(rectangle);

		} else if (e.getActionCommand().equalsIgnoreCase("Circle"))

		{
			panel.drawShape(oval);
		} 
		else if (e.getActionCommand().equalsIgnoreCase("Triangle"))
		{
			panel.drawShape(triangle);
		} 
		else if (e.getActionCommand().equalsIgnoreCase("Polygon"))

		{
			panel.drawShape(polygon);
		} 
		else if (e.getActionCommand().equalsIgnoreCase("Draw Polygon"))

		{
			panel.repaint();
		} 
		else if (e.getActionCommand().equalsIgnoreCase("Black"))

		{
			panel.setCol(Color.BLACK);
		} 
		else if (e.getActionCommand().equalsIgnoreCase("Blue"))

		{
			panel.setCol(Color.BLUE);
		} 
		else if (e.getActionCommand().equalsIgnoreCase("Red"))

		{
			panel.setCol(Color.RED);
		} 
		else if (e.getActionCommand().equalsIgnoreCase("Green"))

		{
			panel.setCol(Color.GREEN);
		} 
		else if (e.getActionCommand().equalsIgnoreCase("Yellow"))

		{
			panel.setCol(Color.YELLOW);
		} 
		else if (e.getActionCommand().equalsIgnoreCase("Cyan"))

		{
			panel.setCol(Color.CYAN);
		} 
		else if (e.getActionCommand().equalsIgnoreCase("Custom"))

		{
			panel.setCol(ColorPicker.showDialog(this, "ColorPalet", Color.BLACK, true));
			panel.repaint();
		} 
		else if (e.getActionCommand().equalsIgnoreCase("About")) {

		}
	}

	private int changesShouldBeSaved() {
		final Object[] options = { "Yes", "No", "Cancel" };
		return JOptionPane.showOptionDialog(this, "Save changes before closing?", "Close",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
	}
	
	private int saveProgress() {
		final Object[] options = {"Yes", "No", "Cancel"};
		return JOptionPane.showOptionDialog(this, "Save changes?", "New",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
	}

	private boolean okToReplace(File f) {
		final Object[] options = { "Yes", "No", "Cancel" };
		return JOptionPane.showOptionDialog(this,
				"The file '" + f.getName() + "' already exists.  " + "Replace existing file?", "Warning",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null, options,
				options[2]) == JOptionPane.YES_OPTION;
	}

	private void openFile(File fArg) {
		BufferedImage imo;
		
		try {
			imo = ImageIO.read(new FileInputStream(fArg));
		} catch (IOException e) {
			popupError("Error reading file '" + fArg.getName() + "'!");
			return;
		}
		
		panel.repaint();
		panel.prepareImage(imo, this);
		panel.setImage(imo);

		panel.addKeyListener(this);
		dataChanged = false;

		this.setTitle(fArg.getName() + " - " + TITLE);
		return;
	}

	private void popupError(String s) {
		System.out.print("\07");
		System.out.flush();
		JOptionPane.showMessageDialog(this, s, "Error", JOptionPane.ERROR_MESSAGE);
		return;
	}

	private void saveToFile(File fArg) {
			
		BufferedImage img = getImage(panel);
		
		try {
			ImageIO.write(img, "jpg", fArg);
		} catch (IOException e) {
			popupError("Cannot save  file '" + fArg.getName() + "'.");
			return;
		}

		this.setTitle(fArg.getName() + " - " + TITLE);
		dataChanged = false;
		panel.addKeyListener(this);
	}
	
	public static BufferedImage getImage(JPanel pan) {
		BufferedImage img = new BufferedImage(pan.getWidth(), pan.getHeight(), BufferedImage.TYPE_INT_RGB);
		
		pan.paint(img.getGraphics());
		return img;
	}

	@Override
	public void keyPressed(KeyEvent ke) {
	}

	@Override
	public void keyReleased(KeyEvent ke) {
	}

	@Override
	public void keyTyped(KeyEvent ke) {
		dataChanged = true;
		panel.removeKeyListener(this);
	}

	class MyFileFilter extends FileFilter {
		private String[] s;

		MyFileFilter(String[] sArg) {
			s = sArg;
		}

		@Override
		public boolean accept(File fArg) {
			if (fArg.isDirectory())
				return true;

			for (int i = 0; i < s.length; ++i)
				if (fArg.getName().toLowerCase().indexOf(s[i].toLowerCase()) > 0)
					return true;

			return false;
		}

		@Override
		public String getDescription() {
			String tmp = "";
			for (int i = 0; i < s.length; ++i)
				tmp += "*" + s[i] + " ";

			return tmp;
		}
	}

}

class DrawingPanel extends JPanel implements MouseListener {

	private Point sPoint = null;
	private Point ePoint = null;
	private Shapes shape = new RectangleShape();
	private Color color = null;
	private java.util.ArrayList list = new java.util.ArrayList();
	Image img;
	int width, height;
	
	public void setImage(BufferedImage img) {
		this.width = img.getWidth();
		this.height = img.getHeight();
		this.img = img;
		this.setSize(width, height);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		paintImage(g);
		g.setColor(color);
		// g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		// RenderingHints.VALUE_ANTIALIAS_ON);
		// g2.setStroke(new BasicStroke(2));
		// g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
		// 0.50f));
		shape.draw(list,g);
	}

	public void setCol(Color c) {
		this.color = c;
	}

	public void drawShape(Shapes shape) {
		this.shape = shape;
	}
	
	public void paintImage(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		
		g2.drawImage(img, 0, 0, this);
	}

	public void mouseClicked(MouseEvent e) {
		if (shape instanceof TriangleShape) {
			list.add(e.getPoint());
			if (list.size() > 2) {
				repaint();
			}
		} else if (shape instanceof PolygonShape) {
			list.add(e.getPoint());
		}
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		sPoint = new Point(e.getX(), e.getY());
		ePoint = sPoint;
	}

	public void mouseReleased(MouseEvent e) {
		ePoint = e.getPoint();
		if (ePoint.getX() < sPoint.getX()) {
			Point temp = ePoint;
			ePoint = sPoint;
			sPoint = temp;
		}
		if (ePoint.getY() < sPoint.getY()) {
			int temp = (int) ePoint.getY();
			ePoint.y = (int) sPoint.getY();
			sPoint.y = temp;
		}
		if (shape instanceof RectangleShape || shape instanceof OvalShape) {
			list.clear();
			list.add(sPoint);
			list.add(ePoint);
			repaint();
		}
	}
}
