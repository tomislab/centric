# centric
Centric's Take Home Test

Using Spring Boot, embedded Tomcat and H2 database, as documented here: https://spring.io/guides/gs/rest-service/

run **gradlew build** to build

run **gradlew bootRun** to launch application

Access application via URL: http://localhost:9090/v1/products

Examples
    List all products
        curl http://localhost:9090/v1/products
        
    Find a product by id
        curl http://localhost:9090/v1/products/88351788-69a5-40ef-ac22-8464c2547b7e
    
    Create a product
        curl -i -X POST -H "Content-Type:application/json" -d '{"name": "Green Shirt", "category": "apparel"}' http://localhost:9090/v1/products
        
    Find products in category=apparel, sorted by creation time (descending), paginated
        curl http://localhost:9090/v1/products/findByCategory?category=apparel&page=0&size=5
