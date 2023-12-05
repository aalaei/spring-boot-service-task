# Java Client Application

This is a straightforward Java client written to test encryption, decryption, and authentication security features. This client works like a wrapper. It exposes the `/services` endpoint and, in the background, uses encryption and authentication to communicate with the main server at `/api/v1/services` endpoint.

## Client Documentation

Explore the capabilities of the Java client application, conveniently located in the `client` directory of this repository. This illustrative client offers valuable insights into programmatically interacting with the API using Java.

### Key Features:

- **Authentication:** The client application demonstrates secure authentication using a username and password mechanism, obtaining a JWT token for subsequent requests. The JWT token is sent in the Bearer token format to ensure secure and authorized communication.

- **Encryption and Decryption:** Experience the robust security measures in place as the client encrypts service information before sending it to the server. The server, in turn, responds with encrypted data, which the client intelligently decrypts. This ensures end-to-end protection, safeguarding critical information from any malicious eavesdropping attempts.

### How It Works:

1. **Authentication Flow:**

   - The client authenticates with the server using a username and password.
   - Upon successful authentication, a JWT token is acquired and utilized for subsequent API requests.

2. **Secure Data Transmission:**

   - Before sending service information to the server, the client encrypts the data, ensuring confidentiality during transmission.

3. **Protected Server Response:**

   - The server responds with encrypted information, maintaining the security of critical texts.

4. **Integrity and Confidentiality:**
   - The encryption process guarantees that even in the face of potential eavesdropping, critical texts remain inaccessible to malicious entities.

For detailed information on setting up, configuring, and leveraging the capabilities of the Java client application, please refer to the [Server Documentation](../client/README.md).

### Get Started:

Explore secure API interaction and encryption using the Java client application, showcasing a strong commitment to robust security practices.

To run the Java client, Navigate to the client directory, then Build and run the Java client:

```bash
cd client
mvn clean package
java -jar target/clientApp.jar
```

### Configurations

The default properties file is [`application.yml`](./src/main/resources/application.yml). You can change by placing similar file next to `clientApp.jar` or specifying input argument or envirinemnt varialbe.
This is default `secrets.yml` proerty file that must be kept private:

```yml
application:
  connection:
    security:
      auth:
        user: 'admin'
        pass: 'admin'
      encryption:
        enabled: true
        key: NGHJJjWm+gp/lmJ4lX3JOA==
        initVector: K869pc8rp6oSPQwJVGvM/Q==
        algo: 'AES/CBC/PKCS5PADDING'
```

## Using the API

To interact with the application, you can make requests to the provided API endpoints. Below are some basic examples using cURL:

### **Create a new Service:**

```bash
curl -X POST -H "Content-Type: application/json" 'localhost:8090/services' \
--data-raw '{
    "criticalText": "EncryptedText1",
    "resources": [
        {
            "criticalText": "EncryptedText2",
            "owners":[
                {
                    "criticalText": "EncryptedText3",
                    "name": "Owner 1",
                    "accountNumber": "AC001",
                    "level": 1
                },
                {
                    "criticalText": "EncryptedText4",
                    "name": "Owner 2",
                    "accountNumber": "AC002",
                    "level": 2
                }
            ]
        },
        {
            "criticalText": "EncryptedText5",
            "owners":[
                {
                    "criticalText": "EncryptedText6",
                    "name": "Owner 3",
                    "accountNumber": "AC003",
                    "level": 1
                },
                {
                    "criticalText": "EncryptedText7",
                    "name": "Owner 4",
                    "accountNumber": "AC004",
                    "level": 2
                }
            ]
        }
    ]
}'
```

Retrieved Service:

```json
{
  "id": "656f58a52bdce51f938d78ce",
  "criticalText": "EncryptedText1",
  "resources": [
    {
      "id": "656f58a52bdce51f938d78cc",
      "criticalText": "EncryptedText2",
      "owners": [
        {
          "id": "656f58a52bdce51f938d78c8",
          "criticalText": "EncryptedText3",
          "name": "Owner 1",
          "accountNumber": "AC001",
          "level": 1
        },
        {
          "id": "656f58a52bdce51f938d78c9",
          "criticalText": "EncryptedText4",
          "name": "Owner 2",
          "accountNumber": "AC002",
          "level": 2
        }
      ]
    },
    {
      "id": "656f58a52bdce51f938d78cd",
      "criticalText": "EncryptedText5",
      "owners": [
        {
          "id": "656f58a52bdce51f938d78ca",
          "criticalText": "EncryptedText6",
          "name": "Owner 3",
          "accountNumber": "AC003",
          "level": 1
        },
        {
          "id": "656f58a52bdce51f938d78cb",
          "criticalText": "EncryptedText7",
          "name": "Owner 4",
          "accountNumber": "AC004",
          "level": 2
        }
      ]
    }
  ]
}
```

### **Get a list of service IDs**

```bash
curl http://localhost:8090/services
```

Retrieved Service ID List:

```json
[
  "656f3d1c548aa9030eadd196",
  "656f3d1f548aa9030eadd197",
  "656f589f2bdce51f938d78c7",
  "656f58a52bdce51f938d78ce"
]
```

### **Get details of a specific service by ID**

```bash
curl http://localhost:8090/services?id=656f58a52bdce51f938d78ce
```

Retrieved Service:

```json
{
  "criticalText": "EncryptedText1",
  "resources": [
    {
      "id": "656f58a52bdce51f938d78cc",
      "criticalText": "EncryptedText2",
      "owners": [
        {
          "id": "656f58a52bdce51f938d78c8",
          "criticalText": "EncryptedText3",
          "name": "Owner 1",
          "accountNumber": "AC001",
          "level": 1
        },
        {
          "id": "656f58a52bdce51f938d78c9",
          "criticalText": "EncryptedText4",
          "name": "Owner 2",
          "accountNumber": "AC002",
          "level": 2
        }
      ]
    },
    {
      "id": "656f58a52bdce51f938d78cd",
      "criticalText": "EncryptedText5",
      "owners": [
        {
          "id": "656f58a52bdce51f938d78ca",
          "criticalText": "EncryptedText6",
          "name": "Owner 3",
          "accountNumber": "AC003",
          "level": 1
        },
        {
          "id": "656f58a52bdce51f938d78cb",
          "criticalText": "EncryptedText7",
          "name": "Owner 4",
          "accountNumber": "AC004",
          "level": 2
        }
      ]
    }
  ]
}
```

### **Update a specific service by ID**

```bash
curl -X PUT -H "Content-Type: application/json" 'localhost:8090/services?id=656f58a52bdce51f938d78ce' \
--data-raw '{
    "criticalText": "UpdatedServiceCriticalText",
    "resources": [
        {
            "criticalText": "UpdatedEncryptedText2",
            "owners":[
                {
                    "criticalText": "UpdatedEncryptedText3",
                    "name": "UpdatedOwner 1",
                    "accountNumber": "AC005",
                    "level": 4
                },
                {
                    "criticalText": "UpdatedEncryptedText4",
                    "name": "UpdatedOwner 2",
                    "accountNumber": "AC003",
                    "level": 8
                }
            ]
        },
        {
            "criticalText": "UpdatedEncryptedText5",
            "owners":[
                {
                    "criticalText": "UpdatedEncryptedText6",
                    "name": "Owner 30",
                    "accountNumber": "AC0030",
                    "level": 10
                },
                {
                    "criticalText": "UpdatedEncryptedText7",
                    "name": "Owner 40",
                    "accountNumber": "AC0040",
                    "level": 9
                }
            ]
        }
    ]
}'
```

Retrieved Updated Service:
```json
{
  "criticalText": "UpdatedServiceCriticalText",
  "resources": [
    {
      "id": "656f59ac2bdce51f938d78d3",
      "criticalText": "UpdatedEncryptedText2",
      "owners": [
        {
          "id": "656f59ac2bdce51f938d78cf",
          "criticalText": "UpdatedEncryptedText3",
          "name": "UpdatedOwner 1",
          "accountNumber": "AC005",
          "level": 4
        },
        {
          "id": "656f59ac2bdce51f938d78d0",
          "criticalText": "UpdatedEncryptedText4",
          "name": "UpdatedOwner 2",
          "accountNumber": "AC003",
          "level": 8
        }
      ]
    },
    {
      "id": "656f59ac2bdce51f938d78d4",
      "criticalText": "UpdatedEncryptedText5",
      "owners": [
        {
          "id": "656f59ac2bdce51f938d78d1",
          "criticalText": "UpdatedEncryptedText6",
          "name": "Owner 30",
          "accountNumber": "AC0030",
          "level": 10
        },
        {
          "id": "656f59ac2bdce51f938d78d2",
          "criticalText": "UpdatedEncryptedText7",
          "name": "Owner 40",
          "accountNumber": "AC0040",
          "level": 9
        }
      ]
    }
  ]
}
```

### **Delete a specific service by ID**

```bash
curl -X DELETE http://localhost:8090/services?id=656f58a52bdce51f938d78ce
```

For more details on available endpoints, authentication, and request/response structures, refer to server [Server Documentation](../README.md).
