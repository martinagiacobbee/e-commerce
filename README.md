# 🍽️ Online Restaurant E-commerce

This project is a dynamic and user-friendly online restaurant e-commerce built with HTML, CSS, JavaScript, and a backend API. It includes an authorization and authentication mechanism through Keycloak, allowing secure access to the admin panel for managing the menu. Customers can browse the menu without authentication or make an order by creating an account.

## 🚀 Features

- **Interactive Menu**: Users can browse through different categories like Appetizers, Main Courses, Desserts, and Drinks.
- **Detailed View**: Each menu item has a detailed description, price, and image.
- **Search Functionality**: Allows users to search for specific dishes by name or ingredients.
- **Responsive Design**: Optimized for both mobile and desktop users.
- **Admin Panel**: Secured with Keycloak authentication, allowing authorized staff to update the menu in real-time by adding, editing, or removing items.
- **User Authentication**: Keycloak-based login for administrators, staff members and customers.

## 🔐 Authentication & Authorization (Keycloak)

The project integrates with [Keycloak](https://www.keycloak.org/) to handle user authentication and role-based access control. Only users with the `admin` role are able to access the admin panel to manage the menu.

### Keycloak Configuration:

1. **Install Keycloak**: Follow the [Keycloak documentation](https://www.keycloak.org/getting-started) to install and set up the Keycloak server.
   
2. **Create a Realm**: In Keycloak, create a new realm for your application (e.g., `restaurant-realm`).

3. **Create a Client**: Add a new client with the following settings:
   - **Client ID**: `springboot-keycloak`
   - **Access Type**: `confidential` or `public` (depending on how you're handling authentication)
   - **Redirect URI**: `http://localhost:4200/*`

4. **Create Roles**:
   - `admin`: Grants access to the admin panel for menu management.
   - `user`: Allows access to the public-facing menu without modification rights.

5. **Create Users**:
   - Add users in the Keycloak admin console and assign them roles (e.g., `admin` for staff members, `user` for regular users).

6. **Configure Keycloak Adapter**: 
   - Use the [Keycloak JavaScript adapter](https://www.keycloak.org/docs/latest/securing_apps/#_javascript_adapter) to integrate Keycloak with your frontend.
   - For the backend, configure Keycloak for [Node.js](https://www.keycloak.org/docs/latest/securing_apps/#_nodejs_adapter) or other technologies as needed.

## 🛠️ Technologies Used

- **Frontend**: HTML5, CSS3, JavaScript, TypeScript, Angular
- **Backend**: Node.js and SpringBoot
- **Database**: Postgres or MySQL for storing menu items and user data
- **Authentication**: Keycloak for secure login and role management
- **API**: RESTful API for data handling between the frontend and backend

## 📦 Installation

### Prerequisites

- Node.js installed (for JavaScript backend)
- Postgres or MySQL installed and configured (depending on the database used)
- Tomcat installed for server deployment
- Keycloak server installed and configured

### Steps

1. Clone the repository:
    ```bash
    git clone https://github.com/martinagiacobbee/e-commerce.frontend.git
    ```

2. Navigate to the project directory:
    ```bash
    cd e-commerce.frontend
    ```

3. Install dependencies:
    ```bash
    npm install
    ```

4. Set up environment variables:
    - Create a `.env` file in the root directory and add the following:
      ```env
      DB_CONNECTION=postgres://localhost:8180/
      PORT=8180
      KEYCLOAK_REALM=MyRealm
      KEYCLOAK_CLIENT_ID=springboot-keycloak
      KEYCLOAK_AUTH_SERVER_URL=http://localhost:8080/auth
      ```

5. Configure Keycloak adapter (for both frontend and backend).

6. Start the server:
    ```bash
    npm start
    ```

7. Open a browser and visit `http://localhost:4200` to see the application in action.

## 🤝 Contributing

Contributions are welcome! Feel free to submit a pull request or open an issue. Please adhere to the following guidelines:

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/my-feature`
3. Commit your changes: `git commit -m 'Add some feature'`
4. Push to the branch: `git push origin feature/my-feature`
5. Open a pull request

