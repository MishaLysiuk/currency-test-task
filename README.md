# Project Currency Test Task

## Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/MishaLysiuk/currency-test-task.git
   ```
2. Add ENV variables:
 - CURRENCY_DATABASE_HOST = localhost
 - CURRENCY_DATABASE_NAME = currencydb
 - CURRENCY_DATABASE_USERNAME = postgres
 - CURRENCY_DATABASE_PASSWORD = 0212
 - CURRENCY_DATABASE_PORT = 5432

3. Start docker container:
    ```bash
   cd {project-root}/docker
   
   docker-compose -f docker-compose.yml up -d
    ```
# Available endpoints

1. GET /api/currency

   Get all available currencies

   Response example: 
   ```bash
   {
    [
    {
        "currencyCode": "PHP"
    },
    {
        "currencyCode": "USD"
    },
    {
        "currencyCode": "EUR"
    },
    {
        "currencyCode": "PLN"
    },
    {
        "currencyCode": "GBP"
    }
   ]
   ```

2. POST /api/currency

   Request body example: 

   ```bash
   {"currencyCode":"gbp"}
   ```
   

3. GET /api/currency/{currency-code}/exchange-rates

   {currency-code} example: usd

   Response example:
   ```bash
   {
    "sourceCode": "USD",
    "rates": {
        "PLN": 3.931813,
        "EUR": 0.913975,
        "GBP": 0.777965,
        "PHP": 55.5695
    }
   }
   ```
