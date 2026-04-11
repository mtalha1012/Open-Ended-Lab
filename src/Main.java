import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Date date = getDate("Enter date for contest (dd/MM/yyyy): ", sc);

        double prize = getValidDouble("Enter prize for the contest: ", sc, 0, Double.MAX_VALUE);
        CookingContest contest = new CookingContest(date, prize);

        int choice = -1;
        do{
            printMainMenu();
            choice = getInt("Enter choice: ", sc);

            switch(choice) {
                case 1:
                    addSeniorChef(contest, sc);
                    break;
                case 2:
                    addJuniorChef(contest, sc);
                    break;
                case 3:
                    addRecipeToChef(contest, sc);
                    break;
                case 4:
                    rateChef(contest, sc);
                    break;
                case 5:
                    rateRecipe(contest, sc);
                    break;
                case 6:
                    viewAllChefs(contest, sc);
                    break;
                case 7:
                    viewAllRatings(contest);
                    break;
                case 8:
                    declareWinner(contest);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        } while(choice != 0);
    }

    //Display Menu
    public static void printMainMenu() {
        System.out.println("\n=== Main Menu ===");
        System.out.println("1. Add Senior Chef");
        System.out.println("2. Add Junior Chef");
        System.out.println("3. Add Recipe to a Chef");
        System.out.println("4. Rate a Chef");
        System.out.println("5. Rate a Recipe");
        System.out.println("6. View All Chefs");
        System.out.println("7. View All Ratings");
        System.out.println("8. Declare Winner");
        System.out.println("0. Exit");
    }

    //Menu Functions
    public static void addSeniorChef(CookingContest contest, Scanner sc) {
        int experience = getInt("Enter years of cooking experience: ", sc);
        try {
            SeniorChef obj = new SeniorChef(new ArrayList<>(), experience);
            contest.addChef(obj);
            System.out.println("Senior Chef added successfully! Assigned ID: " + obj.getId());
        } catch (MaxRecipesExceedException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void addJuniorChef(CookingContest contest, Scanner sc) {
        int superId = getInt("Enter the ID of the Senior Chef who will supervise: ", sc);
        try {
            SeniorChef supervisor = contest.findSeniorChefById(superId);
            JuniorChef obj = new JuniorChef(new ArrayList<>(), supervisor);
            contest.addChef(obj);
            System.out.println("Junior Chef added successfully! Assigned ID: " + obj.getId());
        } catch (NotFoundException | MaxRecipesExceedException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void addRecipeToChef(CookingContest contest, Scanner sc) {
        int id = getInt("Enter Chef ID to add recipe to: ", sc);
        sc.nextLine(); // Buffer clear
        
        try {
            Chef chef = contest.findChefById(id);
            
            System.out.print("Enter Recipe Name: ");
            String name = sc.nextLine();
            System.out.print("Enter Ingredients: ");
            String ingr = sc.nextLine();
            System.out.print("Enter Instructions: ");
            String inst = sc.nextLine();

            Recipe recipe = new Recipe(name, ingr, inst);
            chef.addRecipe(recipe);
            System.out.println("Recipe '" + name + "' added successfully to Chef ID: " + id);

        } catch (NotFoundException | MaxRecipesExceedException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void rateChef(CookingContest contest, Scanner sc) {
        int id = getInt("Enter Chef ID to rate: ", sc);
        double score = getDouble("Enter rating score (0.0 to 10.0): ", sc);
        try {
            Chef chef = contest.findChefById(id);
            chef.rate(score);
            System.out.println("Chef rated! New Rating: " + chef.getRating());
        } catch (NotFoundException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void rateRecipe(CookingContest contest, Scanner sc) {
        int id = getInt("Enter Chef ID who owns the recipe: ", sc);
        try {
            Chef chef = contest.findChefById(id);
            if (chef.getRecipes().isEmpty()) {
                System.out.println("This chef has no recipes!");
                return;
            }
            
            System.out.println("Recipes by Chef " + id + ":");
            for (int i = 0; i < chef.getRecipes().size(); i++)
                System.out.println((i + 1) + ". " + chef.getRecipes().get(i).getName());
            
            int choice = getInt("Select recipe number to rate: ", sc);
            if (choice > 0 && choice <= chef.getRecipes().size()) {
                double score = getDouble("Enter rating score (0.0 to 10.0): ", sc);
                chef.getRecipes().get(choice - 1).rate(score);
                System.out.println("Recipe rated successfully!");
            } else {
                System.out.println("Invalid Choice!");
            }
        } catch (NotFoundException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void viewAllChefs(CookingContest contest, Scanner sc) {
        System.out.println("\n-------------------------------------------------");
        System.out.printf("%-5s | %-15s | %-15s%n", "ID", "Chef Type", "Recipes Enrolled");
        System.out.println("-------------------------------------------------");

        if (contest.getChefs().isEmpty())
            System.out.println("No chefs enrolled yet.");
        else
            for(Chef c : contest.getChefs()) {
                String type = c instanceof SeniorChef ? "Senior" : "Junior";
                System.out.printf("%-5d | %-15s | %-15d%n", c.getId(), type, c.getRecipes().size());
            }
        System.out.println("-------------------------------------------------");
    }

    public static void viewAllRatings(CookingContest contest) {
        System.out.println("\n--- Leaderboard & Ratings ---");
        contest.printAllRating();
    }
    
    public static void declareWinner(CookingContest contest) {
        contest.declareWinner();
    }
   
    // Helper Functions
    public static double getDouble(String prompt, Scanner sc) {
        while(true) {
            try {
                System.out.print(prompt);
                return sc.nextDouble();
            } catch(InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                sc.next();
            }
        }
    }

    public static double getValidDouble(String prompt, Scanner sc, double min, double max) {
        while(true) {
            double val = getDouble(prompt, sc);
            if (val >= min && val <= max) return val;
            System.out.println("Value must be between " + min + " and " + max);
        }
    }

    public static int getInt(String prompt, Scanner sc) {
        while(true) {
            try {
                System.out.print(prompt);
                return sc.nextInt();
            } catch(InputMismatchException e) {
                System.out.println("Invalid input. Please enter a whole number.");
                sc.next();
            }
        }
    }

    public static Date getDate(String prompt, Scanner sc) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false); // Prevents dates like 32/13/2026
        while (true) {
            System.out.print(prompt);
            String dateStr = sc.next();
            try {
                return sdf.parse(dateStr);
            } catch (ParseException e) {
                System.out.println("Incorrect Format. Use dd/MM/yyyy (e.g., 09/04/2026).");
            }
        }
    }
}


class CookingContest {
    private Date date;
    private ArrayList<Chef> chefs;
    private double prize;

    public CookingContest() {
    }

    public CookingContest(Date date, double prize) {
        this.date = date;
        this.chefs = new ArrayList<Chef>();
        setPrize(prize); // Route through setter for validation
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<Chef> getChefs() {
        return Collections.unmodifiableList(chefs);
    }

    public void setChefs(ArrayList<Chef> chefs) {
        this.chefs = chefs;
    }

    public double getPrize() {
        return prize;
    }

    public void setPrize(double prize) {
        if (prize < 0) throw new IllegalArgumentException("Prize money cannot be negative.");
        this.prize = prize;
    }

    public void addChef(Chef chef) {
        chefs.add(chef);
    }

    public Chef findChefById(int id) throws NotFoundException{
        for (Chef c: chefs) {
            if (c.getId() == id) return c;
        }
        throw new NotFoundException("Chef with ID " + id + " not found");
    }

    public SeniorChef findSeniorChefById(int id) throws NotFoundException{
        for (Chef c: chefs) {
            if (c instanceof SeniorChef && c.getId() == id) {
                return (SeniorChef) c;
            }
        }
        throw new NotFoundException("Senior chef with ID " + id + " not found");
    }

    public void printAllRating() {
        if(chefs.isEmpty()) {
            System.out.println("No ratings to show yet.");
            return;
        }
        
        List<Chef> rankedChefs = new ArrayList<>(chefs);
        Collections.sort(rankedChefs);
        
        System.out.println("\n-------------------------------------------------------------");
        System.out.printf("%-5s | %-12s | %-20s | %-10s%n", "ID", "Type", "Item", "Rating");
        System.out.println("-------------------------------------------------------------");

        for (Chef c: rankedChefs) {
            String type = c instanceof SeniorChef ? "Senior" : "Junior";
            System.out.printf("%-5d | %-12s | %-20s | %-10.1f%n", c.getId(), type, "[Chef's Overall]", c.getRating());
            
            List<Recipe> rankedRecipes = new ArrayList<>(c.getRecipes());
            Collections.sort(rankedRecipes);
            
            for (Recipe recipe: rankedRecipes) {
                System.out.printf("%-5s | %-12s | %-20s | %-10.1f%n", "", "", "- " + recipe.getName(), recipe.getRating());
            }
            System.out.println("-------------------------------------------------------------");
        }
    }
    
    public void declareWinner() {
        if (chefs.isEmpty()) {
            System.out.println("No chefs enrolled in the contest yet.");
            return;
        }
        
        List<Chef> rankedChefs = new ArrayList<>(chefs);
        Collections.sort(rankedChefs);
        
        Chef winner = rankedChefs.get(0);
        if (winner.getRating() == 0.0) {
            System.out.println("No one has been rated yet! Score some chefs first.");
            return;
        }
        
        System.out.println("\n**************************************************");
        System.out.println("                CONTEST WINNER!                   ");
        System.out.println("**************************************************");
        String type = winner instanceof SeniorChef ? "Senior Chef" : "Junior Chef";
        System.out.println("Winner: " + type + " (ID: " + winner.getId() + ")");
        System.out.println("Winning Score: " + winner.getRating());
        System.out.printf("Prize Awarded: $%.2f%n", this.prize);
        System.out.println("**************************************************");
    }
}

interface Ratable {
    double getRating();
    void rate(double score);
}

class NotFoundException extends Exception {
    public NotFoundException(String message) {
        super(message);
    }
}

class MaxRecipesExceedException extends Exception {
    public MaxRecipesExceedException(String message) {
        super(message);
    }
}

abstract class Chef implements Ratable, Comparable<Chef> {

    private int id;
    protected ArrayList<Recipe> recipes;
    protected final int maxRecipes;
    protected double rating;
    private static int NoOfChefs = 0;

    public Chef(int maxRecipes) {
        this.id = ++NoOfChefs;
        this.recipes = new ArrayList<Recipe>();
        this.maxRecipes = maxRecipes;
        this.rating = 0.0;
    }

    public Chef(ArrayList<Recipe> recipes, int maxRecipes) throws MaxRecipesExceedException {
        this.id = ++NoOfChefs;
        this.maxRecipes = maxRecipes;
        this.rating = 0.0;

        if (recipes == null)
            this.recipes = new ArrayList<Recipe>();
        else if (recipes.size() > maxRecipes)
            throw new MaxRecipesExceedException("Recipes cannot exceed " + maxRecipes);
        else
            this.recipes = recipes;
    }

    public int getId() {
        return id;
    }

    public List<Recipe> getRecipes() {
        return Collections.unmodifiableList(recipes);
    }

    public void setRecipes(ArrayList<Recipe> recipes) {
        this.recipes = recipes;
    }

    @Override
    public double getRating() {
        return this.rating;
    }

    @Override
    public abstract void rate(double score);

    public void addRecipe(Recipe recipe) throws MaxRecipesExceedException{
        if (recipes.size() >= maxRecipes)
            throw new MaxRecipesExceedException("Maximum recipes of " + maxRecipes + " cannot be exceeded");
        else
            recipes.add(recipe);
    }

    @Override
    public boolean equals(Object obj) {
        Chef chef;
        if (obj != null && obj instanceof Chef)
            chef = (Chef) obj;
        else
            return false;
        return chef.getId() == this.id;
    }

    @Override
    public int compareTo(Chef otherChef) {
        return Double.compare(otherChef.getRating(), this.getRating());
    }
}

class JuniorChef extends Chef {
    private SeniorChef supervisor;

    public JuniorChef(ArrayList<Recipe> recipes, SeniorChef supervisor)
            throws MaxRecipesExceedException {
        super(recipes, 1);
        if (supervisor == null) throw new IllegalArgumentException("Junior chefs must have a valid supervisor.");
        this.supervisor = supervisor;
    }

    public JuniorChef() {
        super(1);
    }

    public SeniorChef getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(SeniorChef supervisor) {
        if (supervisor == null) throw new IllegalArgumentException("Supervisor cannot be null.");
        this.supervisor = supervisor;
    }

    public int getMaxRecipes() {
        return maxRecipes;
    }

    @Override
    public void rate(double score) {
        if (score < 0.0 || score > 10.0) throw new IllegalArgumentException("Score must be between 0.0 and 10.0");
        this.rating = score;
    }
}

class SeniorChef extends Chef{
    private int experience;

    public SeniorChef(ArrayList<Recipe> recipes, int experience) throws MaxRecipesExceedException {
        super(recipes, 3);
        setExperience(experience); // Route through setter for validation
    }

    public SeniorChef() {
        super(3);
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        if (experience < 0) throw new IllegalArgumentException("Experience cannot be negative.");
        this.experience = experience;
    }

    @Override
    public void rate(double score) {
        if (score < 0.0 || score > 10.0) throw new IllegalArgumentException("Score must be between 0.0 and 10.0");
        this.rating = Math.min(10.0, score + (this.experience * 0.2));
    }
}

class Recipe implements Ratable, Comparable<Recipe> {
    private String name;
    private String ingredients;
    private String instructions;
    private double rating;

    public Recipe() {
        this.rating = 0.0;
    }

    public Recipe(String name, String ingredients, String instructions) {
        setName(name);
        setIngredients(ingredients);
        setInstructions(instructions);
        this.rating = 0.0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Recipe name cannot be empty.");
        this.name = name;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        if (ingredients == null || ingredients.trim().isEmpty()) throw new IllegalArgumentException("Ingredients cannot be empty.");
        this.ingredients = ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        if (instructions == null || instructions.trim().isEmpty()) throw new IllegalArgumentException("Instructions cannot be empty.");
        this.instructions = instructions;
    }

    @Override
    public double getRating() {
        return this.rating;
    }

    @Override
    public void rate(double score) {
        if (score < 0.0 || score > 10.0) throw new IllegalArgumentException("Score must be between 0.0 and 10.0");
        this.rating = score;
    }

    @Override
    public boolean equals(Object obj) {
        Recipe recipe;
        if (obj != null && obj instanceof Recipe)
            recipe = (Recipe) obj;
        else
            return false;
        return (this.name.equalsIgnoreCase(recipe.getName()) &&
                this.ingredients.equalsIgnoreCase(recipe.getIngredients()) &&
                this.instructions.equalsIgnoreCase(recipe.getInstructions()));
    }

    @Override
    public int compareTo(Recipe otherRecipe) {
        return Double.compare(otherRecipe.getRating(), this.getRating());
    }
}
