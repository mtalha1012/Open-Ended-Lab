//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

    }
}

interface Ratable {

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