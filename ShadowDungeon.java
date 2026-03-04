import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class ShadowDungeonGUI extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    private JTextField nameField;
    private JRadioButton warriorBtn, mageBtn, archerBtn;

    private JLabel playerStatsLabel;
    private JLabel enemyStatsLabel;
    private JTextArea gameLog;

    private String playerName;
    private String playerClass;
    private int playerHP, playerMaxHP, playerAttack;
    private int playerPotions = 2;
    private int playerXP = 0;

    private String[] enemies = {"Goblin", "Skeleton", "Troll", "DRAGON"};
    private int[] enemyHPs = {50, 80, 120, 200};
    private int[] enemyAttacks = {10, 15, 20, 30};
    private int currentEnemy = 0;
    private int enemyHP;

    private Random rand = new Random();

    public ShadowDungeonGUI() {
        setTitle("ShadowDungeon - GUI RPG");
        setSize(600, 450);
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
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1, 10, 10));

        panel.add(new JLabel("SHADOW DUNGEON", SwingConstants.CENTER));

        panel.add(new JLabel("Enter Hero Name:"));
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

        JButton startBtn = new JButton("Start Game");
        panel.add(startBtn);

        startBtn.addActionListener(e -> startGame());

        return panel;
    }

    private JPanel createGamePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        playerStatsLabel = new JLabel();
        enemyStatsLabel = new JLabel();
        topPanel.add(playerStatsLabel);
        topPanel.add(enemyStatsLabel);

        panel.add(topPanel, BorderLayout.NORTH);

        gameLog = new JTextArea();
        gameLog.setEditable(false);
        panel.add(new JScrollPane(gameLog), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton attackBtn = new JButton("Attack");
        JButton potionBtn = new JButton("Use Potion");
        JButton runBtn = new JButton("Run");

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
        loadEnemy();
        updateStats();
        gameLog.setText("Welcome " + playerName + " the " + playerClass + "!\n");
        cardLayout.show(mainPanel, "Game");
    }

    private void loadEnemy() {
        enemyHP = enemyHPs[currentEnemy];
        gameLog.append("A wild " + enemies[currentEnemy] + " appears!\n");
    }

    private void attackEnemy() {
        int damage = playerAttack + rand.nextInt(10);
        enemyHP -= damage;
        gameLog.append("You hit " + enemies[currentEnemy] + " for " + damage + " damage!\n");

        if (enemyHP <= 0) {
            playerXP += 50;
            playerPotions++;
            gameLog.append(enemies[currentEnemy] + " defeated! +50 XP, +1 Potion\n");

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
            gameLog.append("You used a potion. HP restored!\n");
            updateStats();
        } else {
            gameLog.append("No potions left!\n");
        }
    }

    private void updateStats() {
        playerStatsLabel.setText("Player: " + playerName + " | HP: " + playerHP +
                "/" + playerMaxHP + " | XP: " + playerXP + " | Potions: " + playerPotions);

        enemyStatsLabel.setText("Enemy: " + enemies[currentEnemy] +
                " | HP: " + enemyHP);
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
