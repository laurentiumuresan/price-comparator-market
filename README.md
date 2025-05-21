# price-comparator-market

**Price Comparator - Market** is a backend service for comparing grocery prices across major supermarket chains (e.g., Lidl, Kaufland, Profi).  
It allows users to:

- Compare product prices across stores
- Track price changes and discounts over time
- Find the best deals for their shopping list

The system is designed around core entities like **Product**, **Store**, **Price**, and **Discount**, making it easy to extend with new features.

---

## Class Diagram

The following diagram illustrates the main entities and relationships between them used in the project:

![Class Diagram](docs/images/class-diagram.drawio.png)

## Core Components:

- **Controllers**: REST endpoints for different functionalities
- **Services**: Business logic implementation
- **DTOs**: Data transfer objects for API requests/responses
- **Entities**: JPA entities for database persistence
- **Repositories**: Data access layer

## Entity Model Structure

1. **Product Entity**
   - Contains basic product information (id, name, category, brand)
   - Package information (quantity and unit)
   - Links to multiple Price and Discount entities through @OneToMany relationship
   - No direct user relationships as focus is on price processing

2. **Price Entity**
   - Stores price information (amount and currency)
   - Timestamp for price tracking
   - Links to Product and Supermarket entities through @ManyToOne relationships
   - Uses lazy loading for optimal performance
   - Includes JoinColumn specifications for database mapping

3. **Discount Entity**
   - Contains discount percentage
   - Start and end dates for validity period
   - Timestamp from import file for tracking
   - References Product and Supermarket through @ManyToOne relationships
   - Uses @JsonIgnore for proper serialization

4. **Supermarket Entity**
   - Basic supermarket information (id, name)
   - Links to Price entities through @OneToMany relationship
   - Manages bidirectional relationship with prices

5. **PriceAlert Entity**
   - Tracks price alert configurations for users
   - Contains target price and user email
   - Links to specific Product through @ManyToOne relationship
   - Includes activity status and creation timestamp
   - Uses @PrePersist for automatic timestamp setting

6. **ProcessedFile Entity**
   - Tracks processed import files
   - Uses filename as primary key
   - Records processing timestamp and file type
   - Helps prevent duplicate file processing

## Key Features Implementation

### 1. Daily Shopping Basket Optimization

**Input:**
- List of products and their quantities

**Processing:**
1. Finds lowest price for each product
2. Applies highest available discount
3. Groups items by supermarket of best price
4. Calculates final prices (unit and total)

**Output:**
- Per-supermarket shopping lists containing:
  - Products, quantities and prices
  - Total cost per supermarket
### 2. New Discounts (Last 24h)

**Input:** None

**Processing:**
1. Gets discounts from last 24h
2. Maps each discount to include:
   - Discount details (ID, %, dates)
   - Product info (ID, name)
   - Store info (ID, name)

**Output:**
- List of new discounts with full details

### 3. Best Discounts

**Input:** None

**Processing:**
1. Gets all discounts by timestamp and %
2. Keeps highest % per product
3. Sorts by % descending
4. Takes top 10 discounts

**Output:**
- Top 10 products with best discount %

### 4. Price History Analysis

**Input:**
- Product name
- Days to analyze
- Optional: brand, supermarket, category

**Processing:**
1. Calculates date range (now - requested days)
2. Gets filtered price history
3. For each price point:
   - Finds active discounts
   - Calculates discounted price
   - Includes store and product details

**Output:**
- Product name
- List of price points containing:
  - Date, original price, discounted price
  - Discount percentage
  - Store, brand, category details

### 5. Price Per Unit Calculation

**Input:**
- Product name

**Processing:**
1. Gets latest prices for product
2. Standardizes units:
   - ml → l
   - g → kg
   - pieces unchanged
3. Calculates price per standard unit
4. Sorts by unit price

**Output:**
- List of prices per store containing:
  - Store name
  - Package price
  - Price per standard unit
  - Unit type (kg/l/piece)
   
### 6. Price Alert System

**Input:**
- Product name & brand
- Target price
- User email

**Processing:**
1. Creates alert:
   - Sets product and price target
   - Activates monitoring
2. Monitors prices:
   - Checks every 2 minutes
   - Compares lowest price vs target
3. When triggered:
   - Sends email notification
   - Deactivates alert

**Output:**
- Email alert 

### 7. Data Import System

**Input:**
- Directory path containing CSV files
- Files named as: `store_YYYY-MM-DD.csv` or `store_discounts_YYYY-MM-DD.csv`

**Processing:**
1. File Detection:
   - Scans directory for CSVs
   - Identifies file type (price/discount)
   - Extracts store name and date
2. Data Validation:
   - Verifies required fields
   - Checks data format
3. Import Process:
   - Tracks processed files
   - Updates product details
   - Records prices/discounts
   - Caches supermarket data

**Output:**
- Updated database records
- Processing history
- Log entries for tracking


### This implementation focuses on price processing and optimization without direct user management, emphasizing:
- Efficient price tracking
- Shopping optimization
- Automated alerts
- Standardized measurements
- Historical data analysis

## API Documentation

### Shopping Basket Optimization
```
http POST /api/shopping-basket/optimize
Content-Type: application/json

[
    {
        "productName": "lapte zuzu",
        "quantity": 2
    },
    {
        "productName": "branza telemea",
        "quantity": 1
    }
]
```

### Discounts added in the last 24 hours 
```
http GET /api/discounts/new
```
### Highest percentage discounts
```
http GET /api/discounts/best
```

### Price History (supermarketName, brand and category are optional and for filtering)
```
http POST /api/prices/history Content-Type: application/json
{ 
"productName": "lapte zuzu", 
"lastNDays": 30, 
"supermarketName": "lidl", 
"brand": "Pilos", 
"category": "lactate" }
``` 

### Price Per Unit Comparison
```
http GET /api/prices/{productName}/per-unit
```

### Price Alerts
```
http POST /api/price-alerts/create
Content-Type: application/json

{
    "productName": "lapte zuzu",
    "productBrand": "Pilos",
    "targetPrice": 5.99,
    "userEmail": "user@example.com"
}
```

### Data Import
```http
http POST /api/import
Content-Type: application/json

{
    "directoryPath": "/path/to/csv/files"
}
```

## Building and Running the Application

### Prerequisites

- Java 17 or higher
- Maven
- MySQL Database
- SMTP Server (for email alerts)