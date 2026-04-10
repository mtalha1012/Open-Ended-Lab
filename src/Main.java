import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter date for contest: ");
        Date date = getDate(sc);

        double prize = getDouble("Enter prize for the contest: ", sc);
        CookingContest contest = new CookingContest(date, prize);

        do{
            printMainMenu();
            int choice = getInt("Enter choice: ", sc);

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
                case 6:
                    viewAllChefs(contest, sc);
                case 7:
                    viewAllRatings(contest, sc);
                case 0:
                    break;
                default:
                    System.out.println("Invalid choice");
            } while(choice != 0);
        }
    }

    // Helper Functions
    public static double getDouble(String prompt, Scanner sc) {
        while(true) {
            try {
                System.out.print(prompt);
                return sc.nextDouble();
            } catch(InputMismatchException e) {
                System.out.println("Invalid input");
                sc.next();
            }
        }
    }

    public static int getInt(String prompt, Scanner sc) {
        while(true) {
            try {
                System.out.print(prompt);
                return sc.nextInt();
            } catch(InputMismatchException e) {
                System.out.println("Invalid input");
                sc.next();
            }
        }
    }
}

class CookingContest {
    // Attributes
    private Date date;
    private ArrayList<Chef> chefs;
    private double prize;

    // Constructors
    public CookingContest() {
    }

    public CookingContest(Date date, double prize) {
        this.date = date;
        this.chefs = new ArrayList<Chef>();
        this.prize = prize;
    }

    // Getters and setters
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ArrayList<Chef> getChefs() {
        return chefs;
    }

    public void setChefs(ArrayList<Chef> chefs) {
        this.chefs = chefs;
    }

    public double getPrize() {
        return prize;
    }

    public void setPrize(double prize) {
        this.prize = prize;
    }

    // Methods
    public void addChef(Chef chef) {
        chefs.add(chef);
    }

    public Chef findChefById(int id) throws NotFoundException{
        for (Chef c: chefs) {
            if (c.getId() == id)
                return c;
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
        for (Chef c: chefs) {
            String type = c instanceof SeniorChef ? "Senior Chef" : "Junior Chef";
            System.out.println(type + " ID: " + c.getId() + " Rating: " + c.getRating());
            for (Recipe recipe: c.getRecipes()) {
                System.out.println("\t" + recipe.getName() + " Rating: " + recipe.getRating());
            }
        }
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

abstract class Chef implements Ratable {

    // Attributes
    private int id;
    protected ArrayList<Recipe> recipes;
    protected final int maxRecipes;
    protected double rating;
    private static int NoOfChefs = 0;

    // Constructors
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

    // Getters and setters
    public int getId() {
        return id;
    }

    public ArrayList<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(ArrayList<Recipe> recipes) {
        this.recipes = recipes;
    }

    @Override
    public double getRating() {
        return this.rating;
    }

    // Force child classes
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
        if (obj instanceof Chef)
            chef = (Chef) obj;
        else
            return false;
        return chef.getId() == this.id;
    }
}

class JuniorChef extends Chef {
    // Attributes
    private SeniorChef supervisor;

    // Constructor
    public JuniorChef(ArrayList<Recipe> recipes, SeniorChef supervisor)
            throws MaxRecipesExceedException{
        super(recipes, 1);
        this.supervisor = supervisor;
    }

    public JuniorChef() {
        super(1);
    }

    // Getters and setters
    public SeniorChef getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(SeniorChef supervisor) {
        this.supervisor = supervisor;
    }

    public int getMaxRecipes() {
        return maxRecipes;
    }

    @Override
    public void rate(double score) {
        this.rating = score;
    }
}

class SeniorChef extends Chef{
    private int experience;

    // Constructors
    public SeniorChef(ArrayList<Recipe> recipes, int experience) throws MaxRecipesExceedException {
        super(recipes, 3);
        this.experience = experience;
    }

    public SeniorChef() {
        super(3);
    }

    // Getters and setters
    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    @Override
    public void rate(double score) {
        this.rating = score + (this.experience * 0.5);
    }

}

class Recipe implements Ratable {
    // Attributes
    private String name;
    private String description;
    private String instructions;
    private double rating;

    // Constructors
    public Recipe() {
        this.rating = 0.0;
    }

    public Recipe(String name, String description, String instructions) {
        this.name = name;
        this.description = description;
        this.instructions = instructions;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    @Override
    public double getRating() {
        return this.rating;
    }

    @Override
    public void rate(double score) {
        this.rating = score;
    }

    // Overriding equals method
    @Override
    public boolean equals(Object obj) {
        Recipe recipe;
        if (obj instanceof Recipe)
            recipe = (Recipe) obj;
        else
            return false;
        return (this.name.equalsIgnoreCase(recipe.getName()) &&
                this.description.equalsIgnoreCase(recipe.getDescription()) &&
                this.instructions.equalsIgnoreCase(recipe.getInstructions()));
    }
}

