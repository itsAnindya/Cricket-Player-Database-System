public class MenuGraph {
    public Menu current = new Menu(new String[]{"Untitled Menu:", "Prompt: ", "Menu Item"});

    public Menu getCurrent() {
        return current;
    }

    public void setCurrent(Menu current) {
        this.current = current;
    }

    final Menu mainMenu = new Menu(new String[]{"Main Menu:", "Select an option (1-4): ", "Search Players", "Search Clubs", "Add Players", "Exit System"});
    final Menu searchOptionsMenu = new Menu(new String[]{"Player Searching Options:", "Select an option (1-6): ", "By Player Name", "By Club and Country", "By Position", "By Salary Range", "Country-wise player count", "Back to Main Menu"});
    final Menu searchClubs = new Menu(new String[]{"Club Searching Options", "Select an option (1-5): ", "Player(s) with the maximum salary of a club", "Player(s) with the maximum age of a club", "Player(s) with the maximum height of a club", "Total yearly salary of a club", "Back to Main Menu"});
    final Menu addPlayer = new Menu(new String[]{"Add Player:", ""});
}
