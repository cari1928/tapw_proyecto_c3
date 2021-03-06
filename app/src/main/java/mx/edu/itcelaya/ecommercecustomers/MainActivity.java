package mx.edu.itcelaya.ecommercecustomers;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import mx.edu.itcelaya.ecommercecustomers.model.Address;
import mx.edu.itcelaya.ecommercecustomers.model.Customer;
import mx.edu.itcelaya.ecommercecustomers.model.Order;
import mx.edu.itcelaya.ecommercecustomers.model.Product;
import mx.edu.itcelaya.ecommercecustomers.task.AsyncResponse;
import mx.edu.itcelaya.ecommercecustomers.task.LoginTask;
import mx.edu.itcelaya.ecommercecustomers.task.WooCommerceTask;
import mx.edu.itcelaya.ecommercecustomers.utils.NukeSSLCerts;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ListView list;
    List<Customer> customerItem = new ArrayList<Customer>();
    List<Product> productItem = new ArrayList<Product>();
    ArrayList<Product> cart = new ArrayList<Product>();

    public static String consumer_key = "ck_8610d1b7c089c88b439f3d8102d56ad1ef23b12f";
    public static String consumer_secret = "cs_659b8deee047824dff82defb6354d47823b01fdb";
    public static String url = "https://tapw-proyecto-c3-cari1928.c9users.io/wc-api/v3/customers";
    String auth_url = "https://tapw-proyecto-c3-cari1928.c9users.io/auth_users.php";

    private int customer_id = 0;

    //-------------------Servicios-------------------------
    String jsonResult, loginResult;
    CustomerAdapter cAdapter;
    ProductAdapter pAdapter;

    //------------Menús Customer y Administrator------------
    Menu menu;
    public static String role = "";
    //private boolean isChangedStat = false;
    private int isChangedStat = 0; //0 = invitado, 1 = admin, 2 = customer
    Dialog dLogin;
    EditText txtUsername, txtPassword;
    Button btnAceptar, btnCancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NukeSSLCerts.nuke();

        cart = new ArrayList<>();
        Toast.makeText(MainActivity.this, "Bienvenido", Toast.LENGTH_LONG).show();

        //funcionamiento normal con login!!!!
        loadProducts("https://tapw-proyecto-c3-cari1928.c9users.io/wc-api/v3/products");

        //------------para iniciar como admin
        //loadCustomers();
        //tvNombre.setText(nombre_completo);
        //role = "administrator";
        //isChangedStat = 1;

        list = (ListView) findViewById(R.id.listEcommerce);
        list.setOnItemClickListener(listenerProducts);
        registerForContextMenu(list);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Boolean bandera = true;

        if (role.equals("administrator")) {
            switch (id) {
                case 1: //categorías
                    //constructor(ventana de donde viene, ventana a donde va)
                    Intent in = new Intent(MainActivity.this, CategoryActivity.class);
                    startActivity(in);
                    break;
                case 2: //nuevo cupón
                    Intent iCupon = new Intent(this, NewCuponActivity.class);
                    iCupon.putExtra("option", 0); //0 es para admin; 1 para guest
                    startActivity(iCupon);
                    break;
                case 3: //productos
                    //loadproducts(); //no se usó porque estaba más lenta la intefaz
                    Intent iProduct = new Intent(MainActivity.this, ProductActivity.class);
                    startActivity(iProduct);
                    break;
                case 4: //reporte
                    //loadSales();
                    Intent iReport = new Intent(MainActivity.this, ReportActivity.class);
                    startActivity(iReport);
                    break;
                case 5: //desloguear
                    logout();
                    break;
                default:
                    bandera = super.onOptionsItemSelected(item);
            }
        } else if (role.equals("customer")) {
            switch (id) {
                case 1: //datos personales
                    editCustomer(customer_id);
                    break;
                case 2: //nuevo pedido
                    newOrder(customer_id); //verificar que funcione!!!
                    break;
                case 3: //pedidos
                    Intent iOrders = new Intent(MainActivity.this, OrderStatusActivity.class);
                    iOrders.putExtra("customer_id", customer_id);
                    startActivity(iOrders);
                    break;
                case 4: //desloguear
                    logout();
                    break;
                default:
                    bandera = super.onOptionsItemSelected(item);
            }
        } else {
            switch (id) {
                case 1: //login
                    mostrarLogin();
                    break;
                case 2: //ver productos del carrito
                    Intent iCart = new Intent(MainActivity.this, CartActivity.class);
                    iCart.putExtra("cart", cart);
                    startActivity(iCart);
                    break;
                case 3: //hacer la orden
                    newCustomer();
                    //Toast.makeText(MainActivity.this, "Checkout", Toast.LENGTH_LONG).show();
                    break;

                case 4: //desloguear
                    logout();
                    break;
            }
            bandera = super.onOptionsItemSelected(item);
        }
        return bandera;
    }

    private  void logout(){
        role = "";
        isChangedStat = 0;
        TextView tvNombre = (TextView) findViewById(R.id.user);
        tvNombre.setText("");
        onRestart();
    }

    private void newOrder(int customerID) {
        Intent iNewOrder = new Intent(this, NewOrderActivity.class);
        iNewOrder.putExtra("idCustomer", customerID);
        startActivity(iNewOrder);
    }

    public void loadProducts(String p_url) {
        WooCommerceTask tarea = new WooCommerceTask(this, WooCommerceTask.GET_TASK, "Cargando Productos...", new AsyncResponse() {
            @Override
            public void setResponse(String output) {
                jsonResult = output;
                ListProducts();
            }
        });
        tarea.execute(new String[]{p_url});
    }

    public void ListProducts() {
        try {
            JSONObject jsonResponse = new JSONObject(jsonResult);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("products");

            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                Integer id_product = jsonChildNode.optInt("id");
                String name = jsonChildNode.optString("title");
                Double price = jsonChildNode.optDouble("price");
                Integer stock_quantity = jsonChildNode.optInt("stock_quantity");
                String ImageURL = jsonChildNode.optString("featured_src");

                if (stock_quantity < 0) {
                    stock_quantity = 0;
                }

                productItem.add(new Product(id_product, name, price, stock_quantity, ImageURL));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_LONG).show();
        }
        pAdapter = new ProductAdapter(this, productItem);
        list.setAdapter(pAdapter);
    }

    public void loadSales() {
        String url_sales = "https://tapw-proyecto-c3-cari1928.c9users.io/wc-api/v3/reports/sales?filter[period]=week";
        //String url_sales = "https://tapw-woocomerce-customers-cari1928.c9users.io/wc-api/v3/reports/sales?filter[period]=week";

        WooCommerceTask tarea = new WooCommerceTask(this, WooCommerceTask.GET_TASK, "Cargando Reporte...", new AsyncResponse() {
            @Override
            public void setResponse(String output) {
                jsonResult = output;
                Intent intent_grafica = new Intent(MainActivity.this, Grafica1Activity.class);
                intent_grafica.putExtra("json", jsonResult);
                startActivity(intent_grafica);
            }
        });

        tarea.execute(new String[]{url_sales});
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v.getId() == R.id.listEcommerce) {
            if (role.equals("administrator")) {
                menu.setHeaderTitle("Options");
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.admin_menu, menu);
            } else if (!role.equals("customer")) {
                menu.setHeaderTitle("Options");
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.guest_menu, menu);
            } else {
                //checar esto porque no funciona!!!
                menu.setHeaderTitle("No Options");
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.customer_menu, menu);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Customer customer = null;
        Product product = null;
        //return super.onContextItemSelected(item);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Adapter adapter = list.getAdapter();
        //Object obj  = adapter.getItem(info.position);

        switch (isChangedStat) {
            case 0:
            case 2:
                product = (Product) adapter.getItem(info.position);
                break;
            case 1:
                customer = (Customer) adapter.getItem(info.position);
                break;
        }

        switch (item.getItemId()) {
            case R.id.mnuEdit:
                editCustomer(customer.getId());
                break;

            case R.id.mnuDelete:
                deleteCustomer(customer.getId());
                break;

            case R.id.mnuDetail: //pedidos
                Intent iOrders = new Intent(this, CustomerOrdersActivity.class);
                iOrders.putExtra("idCustomer", customer.getId());
                startActivity(iOrders);
                break;

            case R.id.mnuNewOrder:
                newOrder(customer.getId()); //verificar que funcione!!!
                break;

            case R.id.mnuCart:
                addToCart(searchID(product.getId()), product);
                break;
        }
        return true;
    }

    private void addToCart(int position, Product p_product) {
        if (position != -1) {
            cart.get(position).setQuantity(cart.get(position).getQuantity() + 1); //aumenta en 1 la cantidad de productos
        } else {
            //(int id, String name, Double price, int quantity, String imageUrl)
            cart.add(new Product(
                    p_product.getId(), p_product.getName(), p_product.getPrice(), 1, p_product.getImageUrl()
            )); //se agrega al carrito
        }
        Toast.makeText(this, "Product added", Toast.LENGTH_SHORT).show();
    }

    private int searchID(int p_customerID) {
        for (int i = 0; i < cart.size(); i++) {
            if (cart.get(i).getId() == p_customerID) {
                return i; //regresa la posición del producto
            }
        }

        return -1; //no encontró un producto con ese ID
    }

    private void mostrarLogin() {
        dLogin = new Dialog(this);
        dLogin.setTitle("Login");
        dLogin.setContentView(R.layout.login);

        txtUsername = (EditText) dLogin.findViewById(R.id.txtUsername);
        txtPassword = (EditText) dLogin.findViewById(R.id.txtPassword);
        btnAceptar = (Button) dLogin.findViewById(R.id.btnAceptar);
        btnCancelar = (Button) dLogin.findViewById(R.id.btnCancelar);
        btnAceptar.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);
        dLogin.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAceptar:
                validaAcceso();
                break;
            case R.id.btnCancelar:
                break;
        }
    }

    public void loadCustomers() {
        //https://tapw-proyecto-c3-cari1928.c9users.io/wc-api/v3/customers
        WooCommerceTask tarea = new WooCommerceTask(this, WooCommerceTask.GET_TASK, "Cargando Clientes...", new AsyncResponse() {
            @Override
            public void setResponse(String output) {
                jsonResult = output;
                ListCustomers();
            }
        });
        tarea.execute(new String[]{url});
    }

    public void ListCustomers() {

        try {
            JSONObject jsonResponse = new JSONObject(jsonResult);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("customers");

            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                JSONObject jsonChildNodeBillingAddress = jsonChildNode.getJSONObject("billing_address");
                Address billingAddress = new Address(jsonChildNodeBillingAddress.getString("first_name"), jsonChildNodeBillingAddress.getString("last_name"));
                JSONObject jsonChildNodeShippingAddress = jsonChildNode.getJSONObject("shipping_address");
                Address shippingAddress = new Address(jsonChildNodeShippingAddress.getString("first_name"), jsonChildNodeShippingAddress.getString("last_name"));

                customerItem.add(
                        new Customer(
                                jsonChildNode.optInt("id"),
                                jsonChildNode.optString("email"),
                                jsonChildNode.optString("first_name"),
                                jsonChildNode.optString("last_name"),
                                jsonChildNode.optString("username"),
                                billingAddress,
                                shippingAddress
                        ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_LONG).show();

        }

        cAdapter = new CustomerAdapter(this, customerItem);
        list.setAdapter(cAdapter);
    }

    AdapterView.OnItemClickListener listenerProducts = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            //Toast.makeText(MainActivity.this, view.getTag() + "", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivity.this, CustomerOrdersActivity.class);
            intent.putExtra("id_customer", view.getTag().toString());
            startActivity(intent);
        }
    };

    private void validaAcceso() {
        String username = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();
        TextView tvNombre = (TextView) findViewById(R.id.user);

        LoginTask tarea = new LoginTask(this);
        tarea.setUsername(username);
        tarea.setPassword(password);
        try {
            loginResult = tarea.execute(new String[]{auth_url}).get();
        } catch (InterruptedException e) {
            //e.printStackTrace();
            System.out.println("Error..." + e.getMessage());
        } catch (ExecutionException e) {
            //e.printStackTrace();
            System.out.println("Error..." + e.getMessage());
        }

        //Toast.makeText(MainActivity.this, loginResult, Toast.LENGTH_SHORT).show();

        //creación de web service propio
        try {
            JSONObject jso = new JSONObject(loginResult);
            JSONArray jsonMainNode = jso.optJSONArray("auth");

            for (int i = 0; i < jsonMainNode.length(); i++) {

                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                Boolean valido = jsonChildNode.optBoolean("valido");
                String id = jsonChildNode.optString("id");
                String rol = jsonChildNode.optString("rol");
                String nombre_completo = jsonChildNode.optString("nombre_completo");

                dLogin.dismiss();
                if (valido == true && rol.equals("administrator")) {
                    loadCustomers();
                    tvNombre.setText(nombre_completo);
                    role = rol;
                    isChangedStat = 1;

                } else if (valido == true && rol.equals("customer")) {
                    customer_id = Integer.parseInt(id);
                    //loadOrders("https://tapw-proyecto-c3-cari1928.c9users.io/wc-api/v3/customers/" + customer_id + "/orders");
                    tvNombre.setText(nombre_completo);
                    role = rol;
                    isChangedStat = 2;

                } else {
                    Toast.makeText(this, "" + "Usuario y/o contrase;a no validos", Toast.LENGTH_LONG).show();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("Errors:" + e.getMessage());
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();

        switch (isChangedStat) {
            case 0: //invitado
                menu.add(0, 1, 0, "Login");
                menu.add(0, 2, 0, "Cart");
                menu.add(0, 3, 0, "Checkout");
                break;

            case 1: //admin
                menu.add(0, 1, 0, "Categorías");
                //menu.add(0, 2, 0, "Nuevo Cliente");
                menu.add(0, 2, 0, "Nuevo Cupón");
                //menu.add(0, 3, 0, "Nuevo Pedido");
                menu.add(0, 3, 0, "Productos");
                menu.add(0, 4, 0, "Reporte de Ventas");
                menu.add(0, 5, 0, "Logout");
                break;

            case 2: //customer
                menu.add(0, 1, 0, "Datos Personales");
                menu.add(0, 2, 0, "Nuevo Pedido");
                menu.add(0, 3, 0, "Pedidos");
                menu.add(0, 4, 0, "Logout");
                break;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private void loadOrders(String p_url) {
        //"https://tapw-proyecto-c3-cari1928.c9users.io/wc-api/v3/customers/" + customer_id + "/orders"
        WooCommerceTask tarea = new WooCommerceTask(this, WooCommerceTask.GET_TASK, "Cargando Pedidos...", new AsyncResponse() {
            @Override
            public void setResponse(String output) {
                jsonResult = output;
                //Toast.makeText(MainActivity.this, jsonResult, Toast.LENGTH_SHORT).show();
                ListOrders();
            }
        });
        tarea.execute(new String[]{p_url});
    }

    private void ListOrders() {
        List<Order> oItems = new ArrayList<Order>();
        OrderAdapter oAdapter;

        try {
            JSONObject jsonResponse = new JSONObject(jsonResult);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("orders");

            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                JSONArray jsonProductsNode = jsonChildNode.optJSONArray("line_items");

                String sProducts = "";
                List<Product> products = new ArrayList<Product>();
                for (int x = 0; x < jsonProductsNode.length(); x++) {
                    JSONObject jsonChildNodeProduct = jsonProductsNode.getJSONObject(x);
                    //sProducts += jsonChildNodeProduct.optString("product_id") + ",";
                    products.add(new Product(
                            jsonChildNodeProduct.optInt("product_id"),
                            jsonChildNodeProduct.optString("name"),
                            jsonChildNodeProduct.optDouble("price"),
                            jsonChildNodeProduct.optInt("quantity")
                    ));
                }

                oItems.add(new Order(
                        jsonChildNode.optInt("id"),
                        jsonChildNode.optInt("order_number"),
                        jsonChildNode.optString("status"),
                        jsonChildNode.optDouble("total"),
                        products
                ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_LONG).show();

        }
        oAdapter = new OrderAdapter(this, oItems);
        list.setAdapter(oAdapter);
    }

    private void deleteCustomer(final int idCustomer) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar");
        builder.setMessage("¿Deseas eliminar el registro seleccionado?");
        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                WooCommerceTask tarea = new WooCommerceTask(MainActivity.this, WooCommerceTask.DELETE_TASK, "Eliminando Cliente", new AsyncResponse() {
                    @Override
                    public void setResponse(String output) {
                        jsonResult = output;
                        Toast.makeText(MainActivity.this, "Cliente eliminado correctamente.", Toast.LENGTH_SHORT).show();
                        onRestart();
                    }
                });
                tarea.execute(new String[]{MainActivity.url + "/" + idCustomer});

            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        Dialog d = builder.create();
        d.show();
    }

    private void editCustomer(int idCustomer) {
        Intent i = new Intent(this, EditCustomerActivity.class);
        i.putExtra("idCustomer", idCustomer);
        startActivity(i);
    }

    private void newCustomer() {
        Intent i = new Intent(this, NewCustomerActivity.class);
        i.putExtra("cart", cart);
        startActivity(i);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        list.setAdapter(null);

        //verificar que funcione esto!!
        if (role.equals("administrator")) {
            cAdapter.customers.clear();
            loadCustomers();
        } else {
            pAdapter.productos.clear();
            loadProducts("https://tapw-proyecto-c3-cari1928.c9users.io/wc-api/v3/products");
        }
    }
}
