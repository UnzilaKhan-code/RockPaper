package rockpaperscissors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class RockPaperScissors extends JFrame {

    private JLabel playerDisplay;
    private JLabel cpuDisplay;
    private JLabel resultLabel;
    private JLabel scoreLabel;

    private int playerScore = 0;
    private int cpuScore = 0;

    private final String[] choices = {"🪨", "📄", "✂️"};
    private final String[] names = {"Rock", "Paper", "Scissors"};
    private final Random random = new Random();

    public RockPaperScissors() {
        setTitle("Rock Paper Scissors - Animated");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Enable Full Screen Mode
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true); // Removes window title bar for true full screen

        // Press 'ESC' key to exit full screen / close game
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    System.exit(0);
                }
            }
        });

        // Main Panel with Gradient Background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(106, 27, 154), getWidth(), getHeight(), new Color(216, 27, 96));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new GridBagLayout()); // Centered Layout for full screen

        // Container Box to hold all elements nicely in the middle
        JPanel centerContainer = new JPanel();
        centerContainer.setOpaque(false);
        centerContainer.setLayout(new BoxLayout(centerContainer, BoxLayout.Y_AXIS));

        // Title
        JLabel titleLabel = new JLabel("🎮 Rock Paper Scissors! 🎮", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 42));
        titleLabel.setForeground(new Color(255, 215, 0));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Subtitle instructions
        JLabel exitHint = new JLabel("(Press ESC to Exit)", SwingConstants.CENTER);
        exitHint.setFont(new Font("SansSerif", Font.PLAIN, 16));
        exitHint.setForeground(new Color(220, 220, 220));
        exitHint.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Scoreboard
        scoreLabel = new JLabel("Player: 0  |  CPU: 0");
        scoreLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Game Arena Display
        JPanel displayPanel = new JPanel();
        displayPanel.setOpaque(false);
        displayPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 20));

        playerDisplay = createDisplayCard("❓");
        cpuDisplay = createDisplayCard("❓");

        JLabel vsLabel = new JLabel("VS");
        vsLabel.setFont(new Font("SansSerif", Font.BOLD, 48));
        vsLabel.setForeground(new Color(255, 215, 0));

        displayPanel.add(playerDisplay);
        displayPanel.add(vsLabel);
        displayPanel.add(cpuDisplay);

        // Result Label
        resultLabel = new JLabel("Choose your move!");
        resultLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
        resultLabel.setForeground(Color.WHITE);
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 25, 10));

        Color[] btnColors = {new Color(255, 87, 34), new Color(76, 175, 80), new Color(33, 150, 243)};

        for (int i = 0; i < choices.length; i++) {
            final int choiceIndex = i;
            JButton btn = new JButton(choices[i] + " " + names[i]);
            btn.setFont(new Font("Segoe UI Emoji", Font.BOLD, 20));
            btn.setBackground(btnColors[i]);
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setPreferredSize(new Dimension(180, 65));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

            btn.addActionListener(e -> playRound(choiceIndex));
            buttonPanel.add(btn);
        }

        // Add components to the centered container
        centerContainer.add(titleLabel);
        centerContainer.add(exitHint);
        centerContainer.add(Box.createVerticalStrut(25));
        centerContainer.add(scoreLabel);
        centerContainer.add(Box.createVerticalStrut(30));
        centerContainer.add(displayPanel);
        centerContainer.add(Box.createVerticalStrut(30));
        centerContainer.add(resultLabel);
        centerContainer.add(Box.createVerticalStrut(30));
        centerContainer.add(buttonPanel);

        // Add centered container to full-screen panel
        mainPanel.add(centerContainer);
        add(mainPanel);
    }

    private JLabel createDisplayCard(String initialText) {
        JLabel label = new JLabel(initialText, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 80));
        label.setPreferredSize(new Dimension(160, 160));
        label.setOpaque(true);
        label.setBackground(Color.WHITE);
        label.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 5));
        return label;
    }

    private void playRound(int playerChoice) {
        resultLabel.setText("Thinking...");

        Timer animationTimer = new Timer(80, null);
        final int[] animationFrame = {0};

        animationTimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerDisplay.setText(choices[animationFrame[0] % 3]);
                cpuDisplay.setText(choices[(animationFrame[0] + 1) % 3]);
                animationFrame[0]++;

                if (animationFrame[0] >= 12) {
                    animationTimer.stop();

                    int cpuChoice = random.nextInt(3);
                    playerDisplay.setText(choices[playerChoice]);
                    cpuDisplay.setText(choices[cpuChoice]);

                    evaluateWinner(playerChoice, cpuChoice);
                }
            }
        });

        animationTimer.start();
    }

    private void evaluateWinner(int player, int cpu) {
        if (player == cpu) {
            resultLabel.setText("It's a Tie! 🤝");
            resultLabel.setForeground(new Color(255, 235, 59));
        } else if ((player == 0 && cpu == 2) || (player == 1 && cpu == 0) || (player == 2 && cpu == 1)) {
            resultLabel.setText("You Win! 🎉");
            resultLabel.setForeground(new Color(0, 255, 127));
            playerScore++;
        } else {
            resultLabel.setText("CPU Wins! 🤖");
            resultLabel.setForeground(new Color(255, 82, 82));
            cpuScore++;
        }

        scoreLabel.setText("Player: " + playerScore + "  |  CPU: " + cpuScore);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new RockPaperScissors().setVisible(true);
        });
    }
}