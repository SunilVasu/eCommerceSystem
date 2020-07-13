import java.util.Date;
import java.util.List;
import Customer.Item;

enum OrderType{
	ShipToCustomer,
	StorePickup
}

enum OrderStatus{
	ShippingOrderPlaced,
	PickupOrderPlaced,
	Shipped,
	ReadyToPickup,
	Delivered,
	ReturnRequested,
	ReturnApproved,
	ReturnDeclined,
	ProductReturned
}

//@Invariant(""orderStatus != null")
public class Order{
	protected int orderId;
	protected String custId;	//email
	protected Date orderDate;
	protected OrderStatus orderStatus;	
	protected List<Item> items;
	protected List<Order> orden;
	public void update_order_status(OrderStatus status){
		orderStatus = status;
	}
	
	public OrderStatus get_order_status(){
		return orderStatus;
	}
	public String get_custId(){
		return custId;
	}
	public int get_orderId(){
		return orderId;
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
	
	public List<Order> view_orders(){
		System.out.println("\n\n..........Your Orders.............");
		
		for(Order order:orden){
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
		return orden;	
	}
	
	public List<Order> get_orders()
	{
		return orden;
	}
	protected int last_order(){
		if(orden.size() == 0)
			return 0;
		return orden.get(orden.size()-1).get_orderId();
	}
	
}

//@Invariant("!String.isNullOrEmpty(storeUnit)")
class StorePickup extends Order{
	String storeUnit;
	public StorePickup(String _custId,int _orderId, OrderStatus _orderStatus, List<Item> _items, String storeId){
		custId = _custId;
		orderId = _orderId;
		orderStatus = _orderStatus;
		items = _items;
		select_store(storeId);
	}
	public void select_store(String storeId){
		storeUnit = storeId;
	}
	public String get_storeUnit(){
		return storeUnit;
	}
}

//@Invariant("shippingDetails != null")
class ShipToCustomer extends Order{
	ShippingInfo shippingDetails;
	public ShipToCustomer(String _custId,int _orderId, OrderStatus _orderStatus, List<Item> _items, ShippingInfo _shippingDetails){
		custId = _custId;
		orderId = _orderId;
		orderStatus = _orderStatus;
		items = _items;
		set_shipping_info(_shippingDetails);
	}
	public void set_shipping_info(ShippingInfo _shippingDetails){
		shippingDetails = _shippingDetails;
	}
}





//@Invariant("!String.isNullOrEmpty(addr1)","!String.isNullOrEmpty(pin)", "!String.isNullOrEmpty(phone)")
class ShippingInfo{
	String addr1;
	String pin;
	String phone;
	String email;
	public ShippingInfo(String _addr1, String _pin, String _phone, String _email){
		addr1 = _addr1;
		pin = _pin;
		phone = _phone;
		email = _email;
	}
	
}

