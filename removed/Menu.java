
import java.util.ArrayList;
import java.util.List;

public class Menu
{
    List<String> menuItems = new ArrayList<>();
    String menuTitle = new String();
    String menuPrompt = new String();

    final public void setMenuTitle(String s)
    {
        this.menuTitle = s;
    }
    final public void setMenuPrompt(String s)
    {
        this.menuPrompt = s;
    }
    public final void addMenuItem(String item){
        menuItems.add(item);
    }
    public void displayMenus()
    {
        System.out.println(menuTitle);
        int i = 0;
        for(String item:menuItems){
            System.out.println("(" + (++i) + ") " + item);
        }
        System.out.print("\n" + menuPrompt);
    }

    public Menu(String[] args)
    {
        int i = 0;
        for(String s: args)
        {
            if(i == 0)
            {
                this.setMenuTitle(s);
            }
            else if(i == 1)
            {
                this.setMenuPrompt(s);
            }
            else if(i > 1)
            {
                this.addMenuItem(s);
            }
            i++;
        }
    }
}