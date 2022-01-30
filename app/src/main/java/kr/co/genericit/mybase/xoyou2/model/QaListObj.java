package kr.co.genericit.mybase.xoyou2.model;

public class QaListObj {
    String No;
    String Menu;
    String MenuGuBun;
    String MenuName;
    String MenuSeo;

    public QaListObj(String no, String menu, String menuGuBun, String menuName, String menuSeo) {
        No = no;
        Menu = menu;
        MenuGuBun = menuGuBun;
        MenuName = menuName;
        MenuSeo = menuSeo;
    }

    public String getNo() {
        return No;
    }

    public void setNo(String no) {
        No = no;
    }

    public String getMenu() {
        return Menu;
    }

    public void setMenu(String menu) {
        Menu = menu;
    }

    public String getMenuGuBun() {
        return MenuGuBun;
    }

    public void setMenuGuBun(String menuGuBun) {
        MenuGuBun = menuGuBun;
    }

    public String getMenuName() {
        return MenuName;
    }

    public void setMenuName(String menuName) {
        MenuName = menuName;
    }

    public String getMenuSeo() {
        return MenuSeo;
    }

    public void setMenuSeo(String menuSeo) {
        MenuSeo = menuSeo;
    }
}
