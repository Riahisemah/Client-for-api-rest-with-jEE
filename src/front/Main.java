package front;

import com.sun.jersey.api.client.Client;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.GenericType;
import java.util.List;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.codehaus.jackson.map.ObjectMapper;

import javax.ws.rs.core.MediaType;
import java.util.List;



import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
    	
    	
    	
        Client client = Client.create(new DefaultClientConfig());
        URI uri = UriBuilder.fromUri("http://localhost:8080/RestApi/").build();
        URI uriPost = UriBuilder.fromUri("http://localhost:8080/RestApi/service/createProduit").build();
        URI update =UriBuilder.fromUri("http://localhost:8080/RestApi/service/").build();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            printMenu();
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    viewAllProducts(client, uri);
                    break;
                case 2:
                    createNewProduct(client, uriPost);
                    break;
                case 3:
                    updateProduct(client, update);

                    break;
                case 4:
                    deleteProduct(client, update);
                    break;

                case 5:
                    // Exit the program
                    System.out.println("Exiting the program. Goodbye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\nMenu:");
        System.out.println("1. View all products");
        System.out.println("2. Create a new product");
        System.out.println("3. Update a product");
        System.out.println("4. Delete a product");
        System.out.println("5. Exit");
        System.out.print("Enter your choice: ");
    }
    private static void deleteProduct(Client client, URI update) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the product ID to delete: ");
        int productId = scanner.nextInt();

        // Send a request to delete the product
        ClientResponse deleteProductResponse = client.resource(update)
                .path("/deleteProduit/" + productId)
                .delete(ClientResponse.class);

        if (deleteProductResponse.getStatus() == 204) {
            System.out.println("Product deleted successfully.");
        } else {
            System.out.println(deleteProductResponse);
        }
    }


    private static void viewAllProducts(Client client, URI uri) {
        ClientResponse webResource = client.resource(uri)
                .path("service")
                .path("/getAllProduits")
                .accept(MediaType.APPLICATION_JSON)
                .get(ClientResponse.class);

        if (webResource.getStatus() == 200) {
            String jsonOutput = webResource.getEntity(String.class);

            org.codehaus.jackson.map.ObjectMapper objectMapper = new org.codehaus.jackson.map.ObjectMapper();
            try {
                List<produit> productList = objectMapper.readValue(jsonOutput, new org.codehaus.jackson.type.TypeReference<List<produit>>() {});
                System.out.println("All Products:");
                for (produit product : productList) {
                    System.out.println("Product Code: " + product.getCode());
                    System.out.println("Product Name: " + product.getLib());
                    System.out.println("Product Price: " + product.getPrix());
                    System.out.println("Product Quantity: " + product.getQuantite());
                    System.out.println("Category Code: " + product.getFkcategorie());
                    System.out.println("--------------------");
                }
            } catch (Exception e) {
                System.out.println("Error processing JSON: " + e.getMessage());
            }
        } else {
            System.out.println("Error getting all products: ");
        }
    }
    private static void updateProduct(Client client, URI update) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the product ID to update: ");
        int productId = scanner.nextInt();

        produit updatedProduct = new produit();
        updatedProduct.setCode(productId);

        System.out.print("Enter new product name: ");
        updatedProduct.setLib(scanner.next());

        System.out.print("Enter new product price: ");
        updatedProduct.setPrix(scanner.nextDouble());

        System.out.print("Enter new product quantity: ");
        updatedProduct.setQuantite(scanner.nextInt());

        System.out.print("Enter new category code (fkcategorie): ");
        updatedProduct.setFkcategorie(scanner.nextInt());

        // Send a request to update the product
        ClientResponse updateProductResponse = client.resource(update)
                .path("/updateProduit")
                .type(MediaType.APPLICATION_JSON)
                .put(ClientResponse.class, updatedProduct);

        if (updateProductResponse.getStatus() == 204) {
            System.out.println("Product updated successfully.");
        } else {
            System.out.println(updateProductResponse);
        }
    }

    private static void createNewProduct(Client client, URI uriPost) {
        produit newProduct = new produit();
        Scanner scanner = new Scanner(System.in);

        

        System.out.print("Enter product name: ");
        newProduct.setLib(scanner.next());

        System.out.print("Enter product price: ");
        newProduct.setPrix(scanner.nextDouble());

        System.out.print("Enter product quantity: ");
        newProduct.setQuantite(scanner.nextInt());

        System.out.print("Enter category code (fkcategorie): ");
        newProduct.setFkcategorie(scanner.nextInt());

        ClientResponse createProductResponse = client.resource(uriPost)
                .type(MediaType.APPLICATION_JSON)
                .post(ClientResponse.class, newProduct);

        if (createProductResponse.getStatus() == 200) {
            System.out.println("Product created successfully.");
        } else {
            System.out.println("Error creating product");
        }
    }

}
