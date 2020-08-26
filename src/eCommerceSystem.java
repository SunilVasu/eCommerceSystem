import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public abstract class eCommerceSystem implements SupportTeam{
	protected static List<Customer> customers = new ArrayList<Customer>();
	protected static List<ExternalVendors> vendors = new ArrayList<ExternalVendors>();
	//protected static List<Order> orders = new ArrayList<Order>();
	protected static Catalog catalog = new Catalog();
	
	public void update_status(Order order, OrderStatus status)
	{
		for(Customer customer: customers){
			if(customer.get_custId() == order.get_custId()){
				for(Order ordr:customer.get_orders()){
					if(ordr.get_orderId() == order.get_orderId())
					{
						ordr.update_order_status(status);
						break;
					}
				}
			}
		}
	}
	
	//@Requires("guest.orders.size() > 0")
	//@Ensures("customers.size() == old(customers.size)+1")
	public static void add_Guest(GuestCustomer guest){
		customers.add(guest);
	}
	
	public static void register_customer(PrivCustomer customer)
	{
		customers.add(customer);
	}
	public static PrivCustomer login(String custId, String password)
	{		
		for(Customer customer: customers)
		{
			if(customer instanceof PrivCustomer) {
				  PrivCustomer privCustomer = (PrivCustomer) customer;
				  if(privCustomer.custId == custId && privCustomer.password == password){
					  return privCustomer;
				  }
			}
		}
		return new PrivCustomer();
	}
	public static void add_to_catalog(Item item)
	{			
		item.set_item_id(catalog.last_item() +1);
		catalog.add_item(item);
	}
	public static void remove_from_catalog(int itemid, String supplierId)
	{		
		catalog.remove_item(itemid, supplierId);
	}
	public static List<Item> view_catalog(){
		return catalog.view_items();
	}
	public static String get_supplier_name(String supplierId){
		for(ExternalVendors vendor: vendors){
			if(vendor.get_vendor_id() == supplierId){
				return vendor.get_vendor_name();
			}
		}
		return "";
	}
}

interface SupportTeam{
	List<Order> view_orders(OrderStatus status);
	void update_status(Order order, OrderStatus status);
	boolean checkCredentials(String id, String pwd);
}
//@Invariant({"!String.isNullOrEmpty(userId)", "!String.isNullOrEmpty(password)"})
class SalesTeam extends eCommerceSystem {
	String userId;
	String password;
	public SalesTeam(String _userId, String _password){
		userId = _userId;
		password = _password;
	}
	public List<Order> view_orders(OrderStatus status)
	{
		List<Order> orders = new ArrayList<Order>();
		for(Customer customer: customers){
			for(Order order:customer.get_orders()){
				if(order.get_order_status() == status){
				orders.add(order);
				System.out.println("__________________________________________");
				System.out.println("Order ID:"+order.get_orderId()+"  Total: "+order.calculate_total_price()+"  Status: "+order.get_order_status());
					
				List<Item> items = order.viewItems();
				for(Item item:items){
					System.out.println(""+item.productName+":   "+item.unitPrice+"$");
					System.out.println("   		(Supplier: "+(item.supplierId.charAt(0)=='U'?"YoLetsShop.com":eCommerceSystem.get_supplier_name(item.supplierId))+"  Quantity: "+item.quantity);
				}
				System.out.println("__________________________________________");
				}
			}
		}
		return orders;
	}
	
	
	public boolean checkCredentials(String id, String pwd){
		if(userId == id && password == pwd){
			return true;
		}
		return false;
	}
	
}
//@Invariant({"!String.isNullOrEmpty(userId)", "!String.isNullOrEmpty(password)"})
class ShippingTeam extends eCommerceSystem {
	String userId;
	String password;
	public ShippingTeam(String _userId, String _password){
		userId = _userId;
		password = _password;
	}
	public List<Order> view_orders(OrderStatus status){
		List<Order> shippingOrders = new ArrayList<Order>();
		for(Customer customer: customers){
			for(Order order:customer.get_orders()){
				if(order instanceof ShipToCustomer){
					shippingOrders.add(order);
					System.out.println("__________________________________________");
					System.out.println("Order ID:"+order.get_orderId()+"  Total: "+order.calculate_total_price()+"  Status: "+order.get_order_status());
					
					List<Item> items = order.viewItems();
					for(Item item:items){
						System.out.println(""+item.productName+":   "+item.unitPrice+"$");
						System.out.println("   		(Supplier: "+(item.supplierId.charAt(0)=='U'?"YoLetsShop.com":eCommerceSystem.get_supplier_name(item.supplierId))+"  Quantity: "+item.quantity);
					}
					System.out.println("__________________________________________");
				}
			}
		}
		return shippingOrders;
	}
	
	public boolean checkCredentials(String id, String pwd){
		if(userId == id && password == pwd){
			return true;
		}
		return false;
	}
}
//@Invariant({"!String.isNullOrEmpty(userId)", "!String.isNullOrEmpty(password)"})
class ITTeam extends eCommerceSystem {
	String userId;
	String password;
	public ITTeam(String _userId, String _password){
		userId = _userId;
		password = _password;
	}
	
	public void add_vendor(ExternalVendors vendor){
		//add vendor
		vendors.add(vendor);
	}
	public void remove_vendor(String vendorId){
		for(ExternalVendors vendor: vendors){
			if(vendor.get_vendor_id() == vendorId){
				vendors.remove(vendor);
			}
		}
	}
	public List<Order> view_orders(OrderStatus status)
	{
		List<Order> orders = new ArrayList<Order>();
		for(Customer customer: customers){
			for(Order order:customer.get_orders()){
				if(order.get_order_status() == status){
				orders.add(order);
				System.out.println("__________________________________________");
				System.out.println("Order ID:"+order.get_orderId()+"  Total: "+order.calculate_total_price()+"  Status: "+order.get_order_status());
					
				List<Item> items = order.viewItems();
				for(Item item:items){
					System.out.println(""+item.productName+":   "+item.unitPrice+"$");
					System.out.println("   		(Supplier: "+(item.supplierId.charAt(0)=='U'?"YoLetsShop.com":eCommerceSystem.get_supplier_name(item.supplierId))+"  Quantity: "+item.quantity);
				}
				System.out.println("__________________________________________");
				}
			}
		}
		return orders;
	}
	
	
	public boolean checkCredentials(String id, String pwd){
		if(userId == id && password == pwd){
			return true;
		}
		return false;
	}
}
//@Invariant({"!String.isNullOrEmpty(unitId)", "!String.isNullOrEmpty(password)"})
class StoreUnit extends eCommerceSystem{
	String unitName;
	String unitId;
	String password;
	
	public StoreUnit(String _userId, String _password, String _unitId){
		unitName = _userId;
		password = _password;
		unitId = _unitId;
	}
	public List<Order> view_orders(OrderStatus status){
		//store unit orders
		List<Order> storeOrders = new ArrayList<Order>();
		for(Customer customer: customers){
			for(Order order:customer.get_orders()){
				if(order instanceof StorePickup){
					StorePickup storeOrder = (StorePickup) order;
					if(storeOrder.get_storeUnit() == unitId){
						storeOrders.add(order);
						System.out.println("__________________________________________");
						System.out.println("Order ID:"+order.get_orderId()+"  Total: "+order.calculate_total_price()+"  Status: "+order.get_order_status());
					
						List<Item> items = order.viewItems();
						for(Item item:items){						
							System.out.println(""+item.productName+":   "+item.unitPrice+"$");
							System.out.println("   		(Supplier: "+(item.supplierId.charAt(0)=='U'?"YoLetsShop.com":eCommerceSystem.get_supplier_name(item.supplierId))+"  Quantity: "+item.quantity);
						
						}
						System.out.println("__________________________________________");
					}
				}
			}
		}
		return storeOrders;
	}
	//@Requires("status != null","order.id in orders for all customers", "order instanceOf StorePickup","storeUnit of order = unitId")
	//@Ensures("Count(orders with orderstatus = status") = old(Count(orders with orderstatus = status")) +1)
	@Override
	public void update_status(Order order, OrderStatus status){
		for(Customer customer: customers){
			if(customer.get_custId() == order.get_custId()){
				for(Order ordr:customer.get_orders()){
					if(order instanceof StorePickup){
						StorePickup storeOrder = (StorePickup) ordr;
					if(ordr.get_orderId() == order.get_orderId() && storeOrder.get_storeUnit()== unitId)
					{
						ordr.update_order_status(status);
						break;
					}
					}
				}
			}
		}
	}
	public boolean checkCredentials(String id, String pwd){
		if(unitName == id && password == pwd){
			return true;
		}
		return false;
	}
}
//@Invariant({"!String.isNullOrEmpty(vendorId)"})
class ExternalVendors{
	String vendorName;
	String vendorId;
	public ExternalVendors(String _vendorName, String _vendorId){
		vendorName = _vendorName;
		vendorId = _vendorId;
	}
	
	public void add_to_catalog(Item item)
	{
		item.supplierId = vendorId;
		eCommerceSystem.add_to_catalog(item);
	}
	public void remove_from_catalog(int itemId)
	{
		eCommerceSystem.remove_from_catalog(itemId, vendorId);
	}
	public String get_vendor_id(){
		return vendorId;
	}
	public String get_vendor_name(){
		return vendorName;
	}
}
class Catalog{
	protected List<Item> items;
	
	public Catalog(){
		items = new ArrayList<Item>();
	}
	
	//@Requires("item.supplierId != String.Empty")
	//@Ensures("items.size() = old(items.size()) + 1
	public void add_item(Item item){
		items.add(item);
	}
	public int last_item(){
		if(items.size() == 0)
			return 0;
		return items.get(items.size()-1).get_item_id();
	}
	//@Requires("supplierId != String.Empty", "itemId is there in items list")
		//@Ensures("items.size() = old(items.size()) - 1
	public void remove_item(int itemId, String supplierId){
		for(Item item: items){
			if(item.get_item_id() == itemId && item.get_supplier()==supplierId){
				items.remove(item);
				return;
			}
		}
		System.out.println("Invalid item id/ supplier id");
	}
	public List<Item> view_items(){
		System.out.println("_____________Catalog__________________________");
		for(Item item:items){
			System.out.println(""+item.productName+":   "+item.unitPrice+"$");
			System.out.println("   		(Supplier: "+(item.supplierId.charAt(0)=='U'?"YoLetsShop.com":eCommerceSystem.get_supplier_name(item.supplierId))+(item.get_quantity() > 0?")  Available":")  Out of Stock"));
		}
		System.out.println("_______________________________________________");
		return items;
	}
}



