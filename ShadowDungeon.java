import java.util.Scanner;
import java.util.Random;

public class ShadowDungeon {

    static Scanner sc = new Scanner(System.in);
    static Random rand = new Random();

    static String playerName;
    static String playerClass;
    static int playerHP;
    static int playerMaxHP;
    static int playerAttack;
    static int playerPotions = 2;
    static int playerXP = 0;

    public static void main(String[] args) {
        showWelcome();
        chooseCharacter();

        boolean win1 = battle("Goblin", 50, 10);
        if (!win1) { gameOver(); return; }

        boolean win2 = battle("Skeleton", 80, 15);
        if (!win2) { gameOver(); return; }

        boolean win3 = battle("Troll", 120, 20);
        if (!win3) { gameOver(); return; }

        boolean win4 = battle("DRAGON (Final Boss)", 200, 30);
        if (!win4) { gameOver(); return; }

        victory();
    }

    static void showWelcome() {
        System.out.println("*****************************************");
        System.out.println("*                                       *");
        System.out.println("*        S H A D O W  D U N G E O N    *");
        System.out.println("*          A Java RPG Adventure         *");
        System.out.println("*                                       *");
        System.out.println("*****************************************");
        System.out.println("\nThe kingdom is cursed by a mighty Dragon.");
        System.out.println("You are the last hope to save the world...\n");
        System.out.print("Enter your hero name: ");
        playerName = sc.nextLine();
        System.out.println("\nWelcome, " + playerName + "! Enter the Shadow Dungeon if you dare...\n");
    }

    static void chooseCharacter() {
        System.out.println("=========================================");
        System.out.println("         CHOOSE YOUR CHARACTER           ");
        System.out.println("=========================================");
        System.out.println("1. Warrior  (HP: 100, Attack: 20)");
        System.out.println("2. Mage     (HP: 70,  Attack: 35)");
        System.out.println("3. Archer   (HP: 85,  Attack: 25)");
        System.out.print("\nEnter choice (1, 2 or 3): ");

        int choice = getValidInt();

        switch (choice) {
            case 1:
                playerClass = "Warrior";
                playerHP = 100; playerMaxHP = 100; playerAttack = 20;
                break;
            case 2:
                playerClass = "Mage";
                playerHP = 70; playerMaxHP = 70; playerAttack = 35;
                break;
            case 3:
                playerClass = "Archer";
                playerHP = 85; playerMaxHP = 85; playerAttack = 25;
                break;
            default:
                System.out.println("Invalid! Warrior selected by default.");
                playerClass = "Warrior";
                playerHP = 100; playerMaxHP = 100; playerAttack = 20;
        }

        System.out.println("\nYou chose --> " + playerClass);
        System.out.println("Your journey into the Shadow Dungeon begins!");
        System.out.println("=========================================\n");
    }

    static boolean battle(String enemyName, int enemyHP, int enemyAttack) {
        System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("  A wild " + enemyName + " blocks your path!");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        while (playerHP > 0 && enemyHP > 0) {
            System.out.println("\n[ " + playerName + " | HP: " + playerHP + " | Potions: " + playerPotions + " | XP: " + playerXP + " ]");
            System.out.println("[ " + enemyName + " | HP: " + enemyHP + " ]");
            System.out.println("-----------------------------------------");
            System.out.println("1. Attack   2. Use Potion   3. Run");
            System.out.print("Enter choice: ");

            int action = getValidInt();

            switch (action) {
                case 1:
                    int dmg = playerAttack + rand.nextInt(10);
                    enemyHP -= dmg;
                    System.out.println("\n⚔  You strike " + enemyName + " for " + dmg + " damage!");
                    if (enemyHP <= 0) {
                        System.out.println("✔  " + enemyName + " has been defeated!");
                        playerXP += 50;
                        playerPotions += 1;
                        System.out.println("★  You earned 50 XP and 1 Potion!");
                        System.out.println("   Total XP: " + playerXP);
                        System.out.println("=========================================\n");
                        return true;
                    }
                    break;

                case 2:
                    if (playerPotions > 0) {
                        playerHP = playerMaxHP;
                        playerPotions--;
                        System.out.println("\n🧪 You drink a Shadow Potion!");
                        System.out.println("   HP fully restored to " + playerHP + "!");
                    } else {
                        System.out.println("\n✖  No potions left! Fight or flee!");
                    }
                    break;

                case 3:
                    System.out.println("\n» You flee deeper into the dungeon...");
                    System.out.println("=========================================\n");
                    return false;

                default:
                    System.out.println("Invalid choice! You hesitate and lose your turn!");
            }

            if (enemyHP > 0) {
                int enemyDmg = enemyAttack + rand.nextInt(5);
                playerHP -= enemyDmg;
                System.out.println("💀 " + enemyName + " hits you for " + enemyDmg + " damage!");
            }
        }

        return playerHP > 0;
    }

    static int getValidInt() {
        while (true) {
            try {
                int value = Integer.parseInt(sc.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.print("Please enter a NUMBER (1, 2 or 3): ");
            }
        }
    }

    static void gameOver() {
        System.out.println("\n*****************************************");
        System.out.println("*                                       *");
        System.out.println("*           *** GAME OVER ***           *");
        System.out.println("*   " + playerName + " has fallen in the Shadow    *");
        System.out.println("*   Dungeon. The Dragon still reigns!   *");
        System.out.println("*                                       *");
        System.out.println("*****************************************");
    }

    static void victory() {
        System.out.println("\n*****************************************");
        System.out.println("*                                       *");
        System.out.println("*   *** DRAGON DEFEATED! ***            *");
        System.out.println("*   " + playerName + " has saved the kingdom!      *");
        System.out.println("*   The Shadow Dungeon is no more!      *");
        System.out.println("*   Total XP Earned : " + playerXP + "               *");
        System.out.println("*   You are a TRUE LEGEND!              *");
        System.out.println("*                                       *");
        System.out.println("*****************************************");
    }
}
```

---

## ▶️ How to Run

**Delete the old file** and create a new file named exactly:
```
ShadowDungeon.java
```

Then compile and run:
```
javac ShadowDungeon.java
java ShadowDungeon
