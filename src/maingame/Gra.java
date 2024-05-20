package maingame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javafx.geometry.Point2D;
import maingame.organizmy.Organizm;
import maingame.organizmy.zwierzeta.Czlowiek;

class Gra extends JFrame implements ActionListener, KeyListener {
    private static final long serialVersionUID = 1L;
    private static final int ROZMIAR_N = 20;
    private static final int ROZMIAR_M = 20;

    private final JButton[][] worldButtons = new JButton[ROZMIAR_N][ROZMIAR_M];
    private final JTextArea console;
    private final Swiat swiat;

    private Gra() {
        super("Wirtualny Swiat, Olivia Jackiewicz 197920");

        setSize(1600, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        JPanel worldPanel = new JPanel(new GridLayout(ROZMIAR_N, ROZMIAR_M));
        for (int i = 0; i < ROZMIAR_N; i++) {
            for (int j = 0; j < ROZMIAR_M; j++) {
                worldButtons[i][j] = new JButton();
                worldButtons[i][j].setPreferredSize(new Dimension(ROZMIAR_N, ROZMIAR_M));
                worldButtons[i][j].addActionListener(this);
                worldPanel.add(worldButtons[i][j]);
            }
        }
        add(worldPanel, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(getWidth() / 4, getHeight()));

        // Menu
        JPanel menuPanel = new JPanel(new GridLayout(5, 1));
        menuPanel.setMaximumSize(new Dimension(200, 150));
        JButton saveButton = new JButton("Zapisz");
        saveButton.addActionListener(e -> {
            saveWorld();
            requestFocusInWindow();
        });
        menuPanel.add(saveButton);

        JButton loadButton = new JButton("Wczytaj");
        loadButton.addActionListener(e -> {
            loadWorld();
            requestFocusInWindow();
        });
        menuPanel.add(loadButton);

        JButton turnButton = new JButton("Wykonaj turę");
        turnButton.addActionListener(e -> {
            executeTurn();
            requestFocusInWindow(); // Przywracanie fokusu
        });
        menuPanel.add(turnButton);

        JButton abilityButton = new JButton("Aktywuj umiejętność");
        abilityButton.addActionListener(e -> {
            activateAbility();
            requestFocusInWindow(); // Przywracanie fokusu
        });
        menuPanel.add(abilityButton);

        rightPanel.add(menuPanel, BorderLayout.NORTH);

        // Konsola logów
        console = new JTextArea();
        console.setEditable(false);
        JScrollPane consoleScrollPane = new JScrollPane(console);
        consoleScrollPane.setPreferredSize(new Dimension(getWidth() / 4, getHeight())); // Increase the size of the console area
        rightPanel.add(consoleScrollPane, BorderLayout.CENTER);

        add(rightPanel, BorderLayout.EAST);

        // Inicjalizacja świata
        swiat = Swiat.getInstance(ROZMIAR_N, ROZMIAR_M);
        swiat.poczatkowaPopulacja();
        updateWorldView();

        // Nasłuchiwanie zdarzeń klawiatury
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        // Wyświetlenie okna
        setVisible(true);
    }
    private void saveWorld() {
        String filename = JOptionPane.showInputDialog(this, "Podaj nazwę pliku:");
        if (filename != null && !filename.isEmpty()) {
            swiat.zapiszStan(filename);
        }
    }

    private void loadWorld() {
        String filename = JOptionPane.showInputDialog(this, "Podaj nazwę pliku:");
        if (filename != null && !filename.isEmpty()) {
            swiat.resetujSwiat();
            swiat.wczytajStan(filename);
            updateWorldView();
            requestFocusInWindow();
        }
    }

    private void executeTurn() {
        swiat.wykonajTure();
        updateWorldView();
    }

    private void activateAbility() {
        swiat.getCzlowiek().aktywujUmiejetnosc();
        updateWorldView();
    }

    private void updateWorldView() {
        for (int i = 0; i < ROZMIAR_N; i++) {
            for (int j = 0; j < ROZMIAR_M; j++) {
                worldButtons[i][j].setText("");
                worldButtons[i][j].setBackground(null);
            }
        }
        Font buttonFont = new Font("Arial", Font.PLAIN, 24);
        List<Organizm> organizmy = swiat.getOrganizmy();
        for (Organizm organizm : organizmy) {
            int x = (int) organizm.getpointX();
            int y = (int) organizm.getpointY();
            if (x >= 0 && x < ROZMIAR_N && y >= 0 && y < ROZMIAR_M) {
                worldButtons[x][y].setText(Character.toString(organizm.getIkona()));
                worldButtons[x][y].setFont(buttonFont);
            }
        }

        List<String> logi = swiat.getLogi();
        StringBuilder logText = new StringBuilder();
        for (String log : logi) {
            logText.append(log).append("\n");
        }
        console.setText(logText.toString());
        requestFocusInWindow();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < ROZMIAR_N; i++) {
            for (int j = 0; j < ROZMIAR_M; j++) {
                if (e.getSource() == worldButtons[i][j]) {
                    String[] options = {"Wilk", "Owca", "Zolw","Antylopa","Lis", "Trawa", "Mlecz", "Jagody", "Barszcz", "Guarana"};
                    String choice = (String) JOptionPane.showInputDialog(
                            this, "Wybierz organizm do dodania:", "Dodaj Organizm",
                            JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
                    Organizm inny = swiat.getOrganizm(new Point2D(i,j));
                    if (inny != null) {
                        inny.zabij();
                    }
                    if (choice != null) {
                        if (choice.equals("Trawa") || choice.equals("Mlecz") || choice.equals("Jagody") || choice.equals("Barszcz") || choice.equals("Guarana")) {
                            swiat.dodajOrganizm("rosliny." + choice, new Point2D(i, j));
                        } else {
                            swiat.dodajOrganizm("zwierzeta." + choice, new Point2D(i, j));
                        }
                        updateWorldView();
                    }
                    requestFocusInWindow();
                    break;
                }
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        Point2D newKierunek = null;

        switch (key) {
            case KeyEvent.VK_LEFT:
                newKierunek = new Point2D(0, -1);
                break;
            case KeyEvent.VK_RIGHT:
                newKierunek = new Point2D(0, 1);
                break;
            case KeyEvent.VK_UP:
                newKierunek = new Point2D(-1, 0);
                break;
            case KeyEvent.VK_DOWN:
                newKierunek = new Point2D(1, 0);
                break;
            default:
                break;
        }

        Czlowiek czlowiek = swiat.getCzlowiek();
        if (newKierunek != null && czlowiek != null) {
            czlowiek.setKierunek(newKierunek);
            executeTurn();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Gra());
    }
}
