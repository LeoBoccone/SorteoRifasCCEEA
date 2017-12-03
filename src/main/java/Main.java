import java.io.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class Main extends JPanel
                             implements ActionListener {
    static private final String newline = "\n";
    JButton botonPedidosExtra, botonRifasDisponibles, botonSortear;
    JTextArea log;
    JFileChooser fc;
    boolean pedidosExtraDefinida = false;
    boolean rifasDisponiblesDefinida = false;
    public static String FILENAME_PEDIDOS_EXTRA_AUX = null;
    public static String FILENAME_RIFAS_DISPONIBLES_AUX = null;

    public Main() {
        super(new BorderLayout());

        log = new JTextArea(5,20);
        log.setMargin(new Insets(5,5,5,5));
        log.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(log);

        fc = new JFileChooser();

        botonPedidosExtra = new JButton("Abrir pedidos");
        botonPedidosExtra.addActionListener(this);
        
        botonRifasDisponibles = new JButton("Abrir rifas disponibles");
        botonRifasDisponibles.addActionListener(this);
        
        botonSortear = new JButton("SORTEAR !");
        botonSortear.addActionListener(this);

        JPanel buttonPanel = new JPanel(); //use FlowLayout
        buttonPanel.add(botonPedidosExtra);
        buttonPanel.add(botonRifasDisponibles);
        buttonPanel.add(botonSortear);

        add(buttonPanel, BorderLayout.PAGE_START);
        add(logScrollPane, BorderLayout.CENTER);
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == botonPedidosExtra) {
            int returnVal = fc.showOpenDialog(Main.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                FILENAME_PEDIDOS_EXTRA_AUX = file.getName();
                pedidosExtraDefinida = true;
                log.append("Se cargo el archivo de Pedidos Extra: " + file.getName() + "." + newline);
            } else {
            	log.append("Acción cancelada por el usuario." + newline);
            }
            log.setCaretPosition(log.getDocument().getLength());

        } else if (e.getSource() == botonRifasDisponibles) {
            int returnVal = fc.showOpenDialog(Main.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                FILENAME_RIFAS_DISPONIBLES_AUX = file.getName();
                rifasDisponiblesDefinida = true;
                log.append("Se cargo el archivo de Rifas Disponibles: " + file.getName() + "." + newline);
            } else {
                log.append("Acción cancelada por el usuario." + newline);
            }
            log.setCaretPosition(log.getDocument().getLength());
	    } else if (e.getSource() == botonSortear) {
            if (pedidosExtraDefinida && rifasDisponiblesDefinida){
            	log.append("Se va a realizar el sorteo." + newline);
                System.err.println("--- Se va a realizar el sorteo.");
                String[] parameters = {FILENAME_PEDIDOS_EXTRA_AUX, FILENAME_RIFAS_DISPONIBLES_AUX};
                try {
                	log.append("Se realizo el sorteo." + newline);
    				App.sortear(parameters);
    			} catch (Exception e1) {
    				e1.printStackTrace();
    			}
	        } else {
	        	if (!pedidosExtraDefinida && !rifasDisponiblesDefinida){
	        		log.append("Todavía no se seleccionó el archivo de Disponibles ni de Pedidos." + newline);
	        	} else if (pedidosExtraDefinida) {
	        		log.append("Todavía no se seleccionó el archivo de Disponibles." + newline);
	        	} else if (rifasDisponiblesDefinida) {
	        		log.append("Todavía no se seleccionó el archivo de Pedidos." + newline);
	        	}
	        }
	        log.setCaretPosition(log.getDocument().getLength());
	    }
    }

    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = Main.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Sorteo Rifas Extra - CCEEA");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.add(new Main());

        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                createAndShowGUI();
            }
        });
    }
}
