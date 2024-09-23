public class Dessert {
    public int flavor;
    public int price;
    public static int numDessert = 0;

    /** the name of the constructor must be the same as the class that defines it */
    public Dessert (int f, int p) {
        flavor = f;
        price = p;
        numDessert++;
    }

    public void printDessert () {
        System.out.print(this.flavor + " " + this.price + " " + numDessert);
    }

    public static void main(String[] args) {
        System.out.print("I love dessert!");
    }
}
