import java.awt.*;
import java.util.Random;
import javax.swing.*;

public class ShadowDungeonGUI extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    private JTextField nameField;
    private JRadioButton warriorBtn, mageBtn, archerBtn;

    private JLabel playerInfoLabel;
    private JLabel enemyInfoLabel;

    private JProgressBar playerHPBar;
    private JProgressBar enemyHPBar;

    private JTextArea gameLog;

    private String playerName, playerClass;
    private int playerHP, playerMaxHP, playerAttack;
    private int playerXP = 0, playerPotions = 2;

    private String[] enemies = {"Goblin", "Skeleton", "Troll", "DRAGON"};
    private int[] enemyHPs = {50, 80, 120, 200};
    private int[] enemyAttacks = {10, 15, 20, 30};

    private int currentEnemy = 0;
    private int enemyHP;

    private Random rand = new Random();

    public ShadowDungeonGUI() {
        setTitle("ShadowDungeon - RPG");
        setSize(750, 520);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createStartPanel(), "Start");
        mainPanel.add(createGamePanel(), "Game");

        add(mainPanel);
        setVisible(true);
    }

    private JPanel createStartPanel() {
        JPanel panel = new JPanel(new GridLayout(8, 1, 10, 10));
        panel.setBackground(Color.BLACK);

        JLabel title = new JLabel("SHADOW DUNGEON", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 30));
        title.setForeground(Color.RED);

        panel.add(title);
        panel.add(new JLabel("Enter Hero Name:", SwingConstants.CENTER));

        nameField = new JTextField();
        panel.add(nameField);

        warriorBtn = new JRadioButton("Warrior (HP:100 ATK:20)");
        mageBtn = new JRadioButton("Mage (HP:70 ATK:35)");
        archerBtn = new JRadioButton("Archer (HP:85 ATK:25)");

        ButtonGroup group = new ButtonGroup();
        group.add(warriorBtn);
        group.add(mageBtn);
        group.add(archerBtn);

        panel.add(warriorBtn);
        panel.add(mageBtn);
        panel.add(archerBtn);

        JButton startBtn = new JButton("Start Adventure");
        startBtn.setBackground(Color.RED);
        startBtn.setForeground(Color.WHITE);
        startBtn.setFont(new Font("Arial", Font.BOLD, 16));

        panel.add(startBtn);

        startBtn.addActionListener(e -> startGame());

        return panel;
    }

    private JPanel createGamePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.DARK_GRAY);

        // TOP PANEL
        JPanel topPanel = new JPanel(new GridLayout(4, 1));
        topPanel.setBackground(Color.GRAY);

        playerInfoLabel = new JLabel();
        enemyInfoLabel = new JLabel();

        playerHPBar = new JProgressBar();
        enemyHPBar = new JProgressBar();

        playerHPBar.setStringPainted(true);
        enemyHPBar.setStringPainted(true);

        topPanel.add(playerInfoLabel);
        topPanel.add(playerHPBar);
        topPanel.add(enemyInfoLabel);
        topPanel.add(enemyHPBar);

        panel.add(topPanel, BorderLayout.NORTH);

        // GAME LOG
        gameLog = new JTextArea();
        gameLog.setEditable(false);
        gameLog.setBackground(Color.BLACK);
        gameLog.setForeground(Color.GREEN);
        gameLog.setFont(new Font("Monospaced", Font.PLAIN, 14));

        panel.add(new JScrollPane(gameLog), BorderLayout.CENTER);

        // BUTTON PANEL
        JPanel buttonPanel = new JPanel();

        JButton attackBtn = new JButton("⚔ Attack");
        JButton potionBtn = new JButton("🧪 Potion");
        JButton runBtn = new JButton("🏃 Run");

        attackBtn.setBackground(Color.ORANGE);
        potionBtn.setBackground(Color.CYAN);
        runBtn.setBackground(Color.LIGHT_GRAY);

        attackBtn.setFont(new Font("Arial", Font.BOLD, 14));
        potionBtn.setFont(new Font("Arial", Font.BOLD, 14));
        runBtn.setFont(new Font("Arial", Font.BOLD, 14));

        buttonPanel.add(attackBtn);
        buttonPanel.add(potionBtn);
        buttonPanel.add(runBtn);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        attackBtn.addActionListener(e -> attackEnemy());
        potionBtn.addActionListener(e -> usePotion());
        runBtn.addActionListener(e -> gameOver("You ran away!"));

        return panel;
    }

    private void startGame() {
        playerName = nameField.getText().trim();
        if (playerName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter your name!");
            return;
        }

        if (warriorBtn.isSelected()) {
            playerClass = "Warrior";
            playerHP = playerMaxHP = 100;
            playerAttack = 20;
        } else if (mageBtn.isSelected()) {
            playerClass = "Mage";
            playerHP = playerMaxHP = 70;
            playerAttack = 35;
        } else if (archerBtn.isSelected()) {
            playerClass = "Archer";
            playerHP = playerMaxHP = 85;
            playerAttack = 25;
        } else {
            JOptionPane.showMessageDialog(this, "Select a class!");
            return;
        }

        currentEnemy = 0;
        playerXP = 0;
        playerPotions = 2;

        loadEnemy();
        updateStats();

        gameLog.setText("Welcome " + playerName + " the " + playerClass + "!\n");
        cardLayout.show(mainPanel, "Game");
    }

    private void loadEnemy() {
        enemyHP = enemyHPs[currentEnemy];
        gameLog.append("\nA wild " + enemies[currentEnemy] + " appears!\n");
    }

    private void attackEnemy() {
        int damage = playerAttack + rand.nextInt(10);
        enemyHP -= damage;
        gameLog.append("You dealt " + damage + " damage!\n");

        if (enemyHP <= 0) {
            playerXP += 50;
            playerPotions++;
            gameLog.append("Enemy defeated! +50 XP, +1 Potion\n");

            currentEnemy++;
            if (currentEnemy >= enemies.length) {
                victory();
                return;
            }
            loadEnemy();
        } else {
            enemyAttack();
        }

        updateStats();
    }

    private void enemyAttack() {
        int damage = enemyAttacks[currentEnemy] + rand.nextInt(5);
        playerHP -= damage;
        gameLog.append(enemies[currentEnemy] + " hits you for " + damage + " damage!\n");

        if (playerHP <= 0) {
            gameOver("You were defeated!");
        }
    }

    private void usePotion() {
        if (playerPotions > 0) {
            playerHP = playerMaxHP;
            playerPotions--;
            gameLog.append("HP fully restored!\n");
            updateStats();
        } else {
            gameLog.append("No potions left!\n");
        }
    }

    private void updateStats() {
        playerInfoLabel.setText("Player: " + playerName +
                " | Class: " + playerClass +
                " | XP: " + playerXP +
                " | Potions: " + playerPotions);

        enemyInfoLabel.setText("Enemy: " + enemies[currentEnemy]);

        playerHPBar.setMaximum(playerMaxHP);
        playerHPBar.setValue(Math.max(playerHP, 0));
        playerHPBar.setString("HP: " + playerHP + "/" + playerMaxHP);

        enemyHPBar.setMaximum(enemyHPs[currentEnemy]);
        enemyHPBar.setValue(Math.max(enemyHP, 0));
        enemyHPBar.setString("Enemy HP: " + enemyHP);
    }

    private void gameOver(String message) {
        JOptionPane.showMessageDialog(this, message + "\nGAME OVER");
        System.exit(0);
    }

    private void victory() {
        JOptionPane.showMessageDialog(this,
                "DRAGON DEFEATED!\nYou saved the kingdom!\nTotal XP: " + playerXP);
        System.exit(0);
    }

    public static void main(String[] args) {
        new ShadowDungeonGUI();
    }
}
