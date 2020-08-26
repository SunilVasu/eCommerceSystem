import java.util.ArrayList;
import java.util.List;

//@Invariant("!String.isNullOrEmpty(shippingDetails.custId)")
public abstract class Customer{
	protected String custId; //email id
	protected String phone;
	protected List<Order> orders;
	protected ShoppingCart shoppingCart;
	public String get_custId(){
		return custId;
	}
	public void add_to_cart(Item item, int quant){
		if(item.get_quantity() <= 0){
			System.out.println("Sorry the item selected is currently out of stock");
		}
		else{			
			Item cartItem = new Item(item.get_prod_name(), item.get_unit_price(), quant, item.get_supplier(), last_cart_item()+1);
			shoppingCart.addItem(cartItem);
		}
		
	}
	public void remove_from_cart(int itemId){
		shoppingCart.removeItem(itemId);
	}
	private int last_cart_item(){
		return shoppingCart.last_item();
	}
	public List<Item> view_cart(){
		System.out.println("\n\n..........Your Cart.............");
		List<Item> items = shoppingCart.viewItems();
		for(Item item:items){
			System.out.println(""+item.productName+":   "+item.unitPrice+"$");
			System.out.println("   		(Supplier: "+(item.supplierId.charAt(0)=='U'?"YoLetsShop.com":eCommerceSystem.get_supplier_name(item.supplierId))+"  Quantity: "+item.quantity);
		}
		System.out.println("                  Total Amount:"+shoppingCart.calculate_total_price());
		System.out.println("......................................");
		return items;		
	}
	public List<Order> view_orders(){
		System.out.println("\n\n..........Your Orders.............");
		
		for(Order order:orders){
			System.out.println("__________________________________________");
			System.out.println("Order ID:"+order.get_orderId()+"  Total: "+order.calculate_total_price()+"  Status: "+order.get_order_status());
			
			List<Item> items = order.viewItems();
			for(Item item:items){
				System.out.println(""+item.productName+":   "+item.unitPrice+"$");
				System.out.println("   		(Supplier: "+(item.supplierId.charAt(0)=='U'?"YoLetsShop.com":eCommerceSystem.get_supplier_name(item.supplierId))+"  Quantity: "+item.quantity);
			}
			System.out.println("________________________________________________");
		}
		System.out.println("......................................");
		return orders;	
	}
	public List<Order> get_orders()
	{
		return orders;
	}
	protected int last_order(){
		if(orders.size() == 0)
			return 0;
		return orders.get(orders.size()-1).get_orderId();
	}
	
	//@Requires("shoppingCart.items.size() != 0")
	//@Ensures("orders.size() == old(orders.size)+1")
	protected abstract void place_order(OrderType orderType, String storeId);
	
}

class GuestCustomer extends Customer{
	
	public GuestCustomer(String _custId, String _phone){
		orders = new ArrayList<Order>();
		shoppingCart = new ShoppingCart();
		custId = _custId;
		phone = _phone;
	}
	public void place_order(OrderType orderType, String storeId)
	{
		//get the shipping details from user
		ShippingInfo shippingInfo = new ShippingInfo("street 1", "682314", "1234567892", "deliverto@gmail.com");
				
		if(orderType == OrderType.ShipToCustomer){
			ShipToCustomer order = new ShipToCustomer(custId,last_order()+1, OrderStatus.ShippingOrderPlaced, shoppingCart.viewItems(), shippingInfo);
			orders.add(order);
		}
		else if(orderType == OrderType.StorePickup){
			StorePickup order = new StorePickup(custId,last_order()+1, OrderStatus.PickupOrderPlaced, shoppingCart.viewItems(), storeId);
			orders.add(order);
		}
		eCommerceSystem.add_Guest(this);
		shoppingCart = new ShoppingCart();
	}
}

//@Invariant({"!String.isNullOrEmpty(custName)", "!String.isNullOrEmpty(password)"})
class PrivCustomer extends Customer{
	String custName;
	String password;
	ShippingInfo shippingDetails;
	
	public PrivCustomer(String _custId, String _phone, String _custName, String _password, ShippingInfo _shippingDetails){
		orders = new ArrayList<Order>();
		shoppingCart = new ShoppingCart();
		custName = _custName;
		custId = _custId;
		phone = _phone;
		password = _password;
		shippingDetails = _shippingDetails;
	}
	
	public PrivCustomer(){
		this.custId = "";
	}
	public void update_Profile() {
	
	}
	//@Requires("orders.size() != 0")
	//
	public void return_order(Order order) {
		for(Order ordr: orders){
			if(ordr.get_orderId() == order.get_orderId()){
				ordr.update_order_status(OrderStatus.ReturnRequested);
				return;
			}
		}
	}
	public void place_order(OrderType orderType, String storeId)
	{
		if(shoppingCart.viewItems().isEmpty()){
			System.out.println("There are no items selected in your cart");
			return;
		}
		Order order;
		if(orderType == OrderType.ShipToCustomer){
			order = new ShipToCustomer(custId,last_order()+1, OrderStatus.ShippingOrderPlaced, shoppingCart.viewItems(), shippingDetails);
			
		}
		else if(orderType == OrderType.StorePickup){
			order = new StorePickup(custId,last_order()+1, OrderStatus.PickupOrderPlaced, shoppingCart.viewItems(), storeId);
			
		}
		orders.add(order);
		shoppingCart = new ShoppingCart();
	}
	
}
class ShoppingCart{
	List<Item> items;
	
	public ShoppingCart(){
		items = new ArrayList<Item>();
	}
	
	//@Requires("item.quantity > 0")
	//@Ensures("items.size() == old(items.size)+1")
	public void addItem(Item item){
		items.add(item);
	}
	//@Requires("itemId != 0")
	//s
	public void removeItem(int itemId){
		for(Item item:items){
			if(item.get_item_id() == itemId){
				items.remove(item);
				return;
			}
		}
	}
	public List<Item> viewItems(){
		return items;
	}
	
	public double calculate_total_price(){
		double total = 0;
		for(Item item: items){
			total += item.calculateSubTotal();
		}
		total = (double) Math.round(total * 100) / 100;
		return total;
	}
	public int last_item(){
		if(items.size() == 0)
			return 0;
		return items.get(items.size()-1).get_item_id();
	}
}


//@Invariant({"!String.isNullOrEmpty(supplierId)"})
class Item{
	int itemId;
	String productName;
	double unitPrice;
	int quantity;
	String supplierId;	
	public double calculateSubTotal()
	{
		return unitPrice * quantity;
	}
	public Item(String _productName, double _unitPrice, int _quantity, String _supplierId, int _productId){
		itemId = _productId;
		productName = _productName;
		unitPrice = _unitPrice;
		quantity = _quantity;
		supplierId = _supplierId;
	}
	public int get_quantity(){
		return quantity;
	}
	public void set_quantity(int q){
		quantity = q;
	}
	public void set_item_id(int id){
		itemId = id;
	}
	
	//getters
	public String get_prod_name(){
		return productName;
	}
	public double get_unit_price(){
		return unitPrice;
	}
	public String get_supplier(){
		return supplierId;
	}
	public int get_item_id(){
		return itemId;
	}
}

