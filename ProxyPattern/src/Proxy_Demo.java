
interface OfficeInternet {

    public void getInternet();
}

class InternetAcces implements OfficeInternet {

    private String emp_name;

    InternetAcces(String emp_name) {
        this.emp_name = emp_name;
    }

    @Override
    public void getInternet() {
        System.out.println("Permission Garanted : !" + emp_name);
      
    }
}

class AccessProxy implements OfficeInternet {

    private String emp_name;
    private InternetAcces access;

    public AccessProxy(String emp_name) {
        this.emp_name = emp_name;
    }

    @Override
    public void getInternet() {
        if (getRoll(emp_name)>5) {
            access = new InternetAcces(emp_name);
            access.getInternet();
        } else {
            System.out.println("OOps.. You cannot Access!");
        }
        
    }
    
    public  int getRoll(String emp_name){
        return 10;
    }

}

public class Proxy_Demo {

    public static void main(String[] args) {
OfficeInternet oi=new AccessProxy("Pasindu Lakshan");
oi.getInternet();
    }
}
