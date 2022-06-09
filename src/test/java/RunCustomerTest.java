import org.apache.commons.configuration.ConfigurationException;
import org.junit.Test;

import java.io.IOException;

public class RunCustomerTest {

    @Test
    public void login() throws ConfigurationException, IOException {
        Customer customer = new Customer();
        customer.callingLoginAPI();
    }

    @Test
    public void getList() throws IOException {
        Customer customer = new Customer();
        customer.customerList();
    }

    @Test
    public void search() throws IOException {
        Customer customer = new Customer();
        customer.customerSearch();
    }

    @Test
    public void customerGenerate() throws IOException, ConfigurationException {
        Customer customer = new Customer();
        customer.generateCustomer();
    }

    @Test
    public void addCustomer() throws ConfigurationException, IOException {
        Customer customer = new Customer();
        customer.createCustomer();
    }
}
