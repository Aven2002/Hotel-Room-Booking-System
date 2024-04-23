package my.edu.utar;

public class Printer {

	public void printInfo(String name, String member_type, String room_type) {
		System.out.println("\n==============================================");
        System.out.println("                   Booking Details              ");
        System.out.println("==============================================");
        System.out.println("        Name          : " + name);
        System.out.println("        Member Type   : " + member_type);
        System.out.println("        Room Type     : " + room_type);
        System.out.println("==============================================");

	}
	
	
//	public static void main(String[]args) {
//		Printer print=new Printer();
//		print.printInfo("Aven","VIP","VIP");
//	}
}
