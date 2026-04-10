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

abstract class Chef implements Ratable{
    // Attributes
    private static int GLOBAL_ID = 1;
    private int id;
    protected ArrayList<Recipe> recipes;
    protected final int maxRecipes;

    // Constructors
    public Chef(int maxRecipes) {
        this.recipes = new ArrayList<Recipe>();
        this.maxRecipes = maxRecipes;
    }

    public Chef(ArrayList<Recipe> recipes, int maxRecipes) throws MaxRecipesExceedException{
        this.id = GLOBAL_ID;
        GLOBAL_ID++;
        this.maxRecipes = maxRecipes;
        if (recipes.size() > maxRecipes)
            throw new MaxRecipesExceedException("Recipes cannot exceed " + maxRecipes);
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

    public void addRecipe(Recipe recipe) throws MaxRecipesExceedException{
        if (recipes.size() > maxRecipes)
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
    private Chef supervisor;

    // Constructor
    public JuniorChef(ArrayList<Recipe> recipes, Chef supervisor)
    throws MaxRecipesExceedException{
        super(recipes, 1);
        this.supervisor = supervisor;
    }

    public JuniorChef() {
        super(1);
    }

    // Getters and setters
    public Chef getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Chef supervisor) {
        this.supervisor = supervisor;
    }

    public int getMaxRecipes() {
        return maxRecipes;
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
}

class Recipe implements Ratable {
    // Attributes
    private String name;
    private String description;
    private String instructions;

    // Constructors
    public Recipe() {
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