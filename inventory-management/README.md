# Vue 3 + TypeScript + Vite

# Inventory Management Frontend

## Overview

This project is a frontend application for managing inventory, built using **Vue 3**, **TypeScript**, and **Vite**. It provides an intuitive interface for managing products, tracking movements, and generating profit reports. The application integrates with a backend API developed in **Java** using **Spring Boot**.

## Features

- **Product Management**: Add, edit, delete, and list products.
- **Movements Tracking**: Monitor inventory movements.
- **Profit Reports**: Generate and view profit reports with date filters.
- **Responsive Design**: Optimized for desktop and mobile devices.
- **Modern UI**: Built with **Element Plus** for a clean and professional look.

## Technologies Used

### Frontend
- **Vue 3**: Progressive JavaScript framework for building user interfaces.
- **TypeScript**: Strongly typed programming language for better code quality.
- **Vite**: Fast build tool for modern web applications.
- **Element Plus**: UI library for Vue 3.
- **Axios**: HTTP client for API communication.

### Backend
- **Java**: Backend language.
- **Spring Boot**: Framework for building RESTful APIs.
- **Maven**: Dependency management.

## Project Structure

inventory-management/ ├── src/ │ ├── components/ # Reusable Vue components │ ├── views/ # Application pages │ ├── services/ # API service functions │ ├── types/ # TypeScript type definitions │ ├── assets/ # Static assets (images, styles, etc.) │ ├── App.vue # Main application component │ ├── main.ts # Application entry point │ └── router.ts # Vue Router configuration ├── public/ # Public static files ├── package.json # Project dependencies ├── tsconfig.json # TypeScript configuration ├── vite.config.ts # Vite configuration └── README.md # Project documentation

# Project documentation

## Installation

### Prerequisites
- **Node.js** (v16 or higher)
- **npm** (v8 or higher)

### Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/inventory-management.git
   cd inventory-management

Install dependencies:  
npm install
Start the development server:  
npm run dev
Open the application in your browser at http://localhost:3000.  
Scripts
npm run dev: Start the development server.
npm run build: Build the application for production.
npm run preview: Preview the production build locally.
npm run lint: Run linting checks.
API Integration
The frontend communicates with the backend API hosted at http://localhost:8080/api. Below are the key endpoints used:  
GET /product: List products with pagination.
GET /product/all: List all products without pagination.
POST /product: Save a new product.
PUT /product/{id}: Edit an existing product.
DELETE /product/{id}: Delete a product.
GET /product/lucro: Fetch paginated profit reports.
Customization
Environment Variables
You can configure the API base URL by creating a .env file in the project root: VITE_API_BASE_URL=http://localhost:8080/api

Styling
The application uses scoped styles and global CSS variables for customization. You can modify the styles in the App.vue file or individual components.  
Contributing
Fork the repository.
Create a new branch for your feature or bug fix.
Commit your changes and push them to your fork.
Submit a pull request.
License
This project is licensed under the MIT License. See the LICENSE file for details.
Acknowledgments
Vue.js Documentation
TypeScript Documentation
Element Plus Documentation
Vite Documentation