import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

    }

    // Helper Functions
    public static double getDouble(String prompt, Scanner sc) {
        while(true) {
            try {
                System.out.print(prompt);
                double input = sc.nextDouble();
                return input;
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
                int input = sc.nextInt();
                return input;
            } catch(InputMismatchException e) {
                System.out.println("Invalid input");
                sc.next();
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