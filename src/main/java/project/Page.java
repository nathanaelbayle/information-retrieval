package project;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to represent a Wikipedia page
 *
 * @author Nathanaël Bayle
 */
public class Page implements java.io.Serializable {
    // Title of the page
    private String title;

    // List of categories of the page
    private List<String> category = new ArrayList<>();

    // List of ingredients in the page
    private final List<String> ingredients = new ArrayList<>();

    public Page() {
        this.title = "";
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title.toLowerCase();
    }

    public void addCategory(String category) {
        this.category.add(category);
    }

    public void addCategory(List<String> category) {
        this.category.addAll(category);
    }

    public String getCategory() {
        return String.join("\n", this.category);
    }

    public void addIngredient(List<String> ingredients) {
        this.ingredients.addAll(ingredients);
    }

    public void addIngredient(String ingredient) {
        this.ingredients.add(ingredient);
    }

    public String getIngredients() {
        return String.join(", ", this.ingredients);
    }

    public String toString() {
        return this.title + "\t\t" + this.getCategory() + "\t\t" + this.getIngredients() + "\n";
    }
}