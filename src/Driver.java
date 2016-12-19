import java.util.ArrayList;
import java.util.List;

public class Driver {

	static List<SupportTeam> adminUsers = new ArrayList<SupportTeam>();
	public static void main(String[] args) {
		
		SetupCatalog();
		
		//SetupAdminUsers();
		
		//Shop as Guest
		
						
		//Register user
		//RegisterUser();
		
		//Login as privileged Customer and shop
		//PrivilegedCustomer();
		
		//Guest Customer
		//GuestCustomer();
		
		
		//Shipping Team
		//ShippingTeam();
		
		//Sales Team
		//SalesTeam();
		
		//External Vendors
		//ExternalVendors();
		
		//Store Units
		StoreUnits();
		
	}
	
	public static void StoreUnits(){
		
		//add to catalog and remove same as other admin users - UC4
		StoreUnit storeunit = new StoreUnit("storeUnit", "admin","U101");
		RegisterUser();
		PrivilegedCustomer();
		System.out.println("Store Unit Orders");
		List<Order> orders = storeunit.view_orders(OrderStatus.ReturnRequested);
		
		
	}
	
	public static void ITTeam(){
		//UC5
		ITTeam itteam = new ITTeam("itTeam", "admin");
		ExternalVendors vendor = new ExternalVendors("vendor2", "V101");
		
		//add vendor
		itteam.add_vendor(vendor);
		
		//remove vendor
		itteam.remove_vendor("V101");
		
		
		
	}
	public static void ExternalVendors(){
		//UC3
		ExternalVendors vendor = new ExternalVendors("vendor1", "V100");
		Item item4 = new Item("Harry Potter and the Deathly Hallows", 19.94, 0, "", 0);
		vendor.add_to_catalog(item4);
		eCommerceSystem.view_catalog();
		vendor.remove_from_catalog(5);
		eCommerceSystem.view_catalog();
	}
	
	public static void RegisterUser()
	{
		ShippingInfo shippingInfo = new ShippingInfo("street 1", "682314", "1234567892", "deliverto@gmail.com");
		PrivCustomer customer = new PrivCustomer("shopaholic@gmail.com", "1234567891", "Shopaholic", "password", shippingInfo);
		//UC2
		eCommerceSystem.register_customer(customer);
		System.out.println("Registered successfully...");
	}
	
	public static void ShippingTeam(){
		//Login
		ShippingTeam shippingTeam = null;
		for(SupportTeam supportTeam:adminUsers){
			if(supportTeam.checkCredentials("shippingTeam", "admin")){
				System.out.println("Logged in Successfully");
				shippingTeam = (ShippingTeam) supportTeam;
				break;
			}
		}
		if(shippingTeam == null){
			System.out.println("Invalid User...");
			return;
		}
		
		//Manage Orders UC9, UC10 - get the return approved orders
		//view all shipping orders
		System.out.println("Shipping Orders...");
		List<Order> shippingOrders = shippingTeam.view_orders(OrderStatus.ShippingOrderPlaced);
		
		//update order status
		shippingTeam.update_status(shippingOrders.get(0), OrderStatus.Shipped);
		//shippingTeam.view_orders(OrderStatus.ShippingOrderPlaced);
		
	}
	
	public static void SalesTeam(){
		//UC8
		SalesTeam salesTeam = null;
		for(SupportTeam supportTeam:adminUsers){
			if(supportTeam.checkCredentials("salesTeam", "admin")){
				System.out.println("Logged in Successfully");
				salesTeam = (SalesTeam) supportTeam;
				break;
			}
		}
		if(salesTeam == null){
			System.out.println("Invalid User...");
			return;
		}
		
		//view all orders
		System.out.println("All Orders...");
		List<Order> salesOrders = salesTeam.view_orders(OrderStatus.ReturnRequested);
				
				//update order status
		salesTeam.update_status(salesOrders.get(0), OrderStatus.Shipped);
		
		System.out.println("After updating order status...");	
		
		salesTeam.view_orders(OrderStatus.Shipped);
		
		//add and remove catalog item
		
		
		Item item4 = new Item("Harry Potter and the Half-Blood Prince", 16.94, 0, "U102", 0);
		SalesTeam.add_to_catalog(item4);
		eCommerceSystem.view_catalog();
		SalesTeam.remove_from_catalog(5, "U102");
		
		//UC6 - Approve return
		//List<Order> returnRequests = salesTeam.view_orders(OrderStatus.ReturnRequested);
		//salesTeam.update_status(salesOrders.get(0), OrderStatus.ReturnApproved);
	}
	
	public static void GuestCustomer(){
		//UC12
		GuestCustomer guest = new GuestCustomer("justAGuest@gmail.com","1234567890");
		List<Item> catalogItems = eCommerceSystem.view_catalog();
		guest.add_to_cart(catalogItems.get(2), 1);
		
		guest.add_to_cart(catalogItems.get(3), 1);
		
		guest.view_cart();
		
		//place order
		System.out.println("\n##place order..");
		guest.place_order(OrderType.ShipToCustomer, "U100");
		List<Order> orders = guest.view_orders();
	}
	
	public static void PrivilegedCustomer(){
		
		//Login UC11
		PrivCustomer customer = eCommerceSystem.login("shopaholic@gmail.com", "password");
		if(customer.custId != ""){
			//redirect to home page
			System.out.println("##Log in...");
			System.out.println("\nWelcome to YoLetsShop.com...!Happy Shopping...");
		}
		else{
			System.out.println("Invalid User");
		}
		
		//view catalog - UC1
		System.out.println("\n##view catalog..");
		List<Item> catalogItems = eCommerceSystem.view_catalog();
						
				
		//add to cart
		System.out.println("\n##add item 2 and 3 to cart..");
		customer.add_to_cart(catalogItems.get(1), 1);
		
		customer.add_to_cart(catalogItems.get(2), 1);
				
		//customer.view_cart();		
		//customer.remove_from_cart(1);	
		
		//place order
		System.out.println("\n##place order..");
		customer.place_order(OrderType.ShipToCustomer, "U100");
		customer.add_to_cart(catalogItems.get(2), 1);
		customer.place_order(OrderType.StorePickup, "U101");
		List<Order> orders = customer.view_orders();
		
		//Return order
		System.out.println("\n##return order..");
		customer.return_order(orders.get(0));
		//customer.view_orders();
		
		
	}
	public static void SetupCatalog(){
		Item item1 = new Item("Hersheys", 30, 10, "U100", 0);
		SalesTeam.add_to_catalog(item1);
		Item item2 = new Item("Harry Potter and the Sorcerer's Stone", 12.7, 10, "U101", 0);
		SalesTeam.add_to_catalog(item2);
		Item item3 = new Item("Harry Potter and the Prisoner of Azkaban", 15.24, 10, "U101", 0);
		SalesTeam.add_to_catalog(item3);
		Item item4 = new Item("Harry Potter and the Half-Blood Prince", 16.94, 0, "U102", 0);
		SalesTeam.add_to_catalog(item4);
		System.out.println("Catalog set up done....");
	}
	public static void SetupAdminUsers(){
		
		SalesTeam sales = new SalesTeam("salesTeam", "admin");
		ShippingTeam shippingTeam = new ShippingTeam("shippingTeam", "admin");
		ITTeam itteam = new ITTeam("itTeam", "admin");
		StoreUnit storeunit = new StoreUnit("storeUnit", "admin","U100");
		
		adminUsers.add(sales);
		adminUsers.add(shippingTeam);
		adminUsers.add(itteam);
		adminUsers.add(storeunit);
		System.out.println("Admin Users set up done....");
	}
}
