-- 1. Створення бази даних
CREATE DATABASE InternetShop;
GO

USE InternetShop;
GO

-- 2. Таблиця "Категорії"
CREATE TABLE Categories (
    CategoryID INT IDENTITY(1,1) PRIMARY KEY,
    CategoryName NVARCHAR(100) NOT NULL
);

-- 3. Таблиця "Користувачі"
CREATE TABLE Users (
    UserID INT IDENTITY(1,1) PRIMARY KEY,
    FirstName NVARCHAR(50) NOT NULL,
    LastName NVARCHAR(50) NOT NULL,
    Email NVARCHAR(100) UNIQUE NOT NULL,
    RegistrationDate DATETIME DEFAULT GETDATE()
);

-- 4. Таблиця "Товари" 
-- Зв'язок "Один-до-багатьох" (Одна категорія містить багато товарів)
CREATE TABLE Products (
    ProductID INT IDENTITY(1,1) PRIMARY KEY,
    ProductName NVARCHAR(150) NOT NULL,
    Price DECIMAL(10, 2) NOT NULL,
    StockQuantity INT NOT NULL,
    CategoryID INT,
    CONSTRAINT FK_Products_Categories FOREIGN KEY (CategoryID) 
        REFERENCES Categories(CategoryID) ON DELETE SET NULL
);

-- 5. Таблиця "Замовлення"
-- Зв'язок "Один-до-багатьох" (Один користувач може зробити багато замовлень)
CREATE TABLE Orders (
    OrderID INT IDENTITY(1,1) PRIMARY KEY,
    UserID INT,
    OrderDate DATETIME DEFAULT GETDATE(),
    TotalAmount DECIMAL(10, 2) NOT NULL,
    CONSTRAINT FK_Orders_Users FOREIGN KEY (UserID) 
        REFERENCES Users(UserID) ON DELETE CASCADE
);

-- 6. Проміжна таблиця "Деталі замовлення" 
-- Реалізує зв'язок "Багато-до-багатьох" між Замовленнями та Товарами
CREATE TABLE OrderDetails (
    OrderDetailID INT IDENTITY(1,1) PRIMARY KEY,
    OrderID INT,
    ProductID INT,
    Quantity INT NOT NULL,
    UnitPrice DECIMAL(10, 2) NOT NULL,
    CONSTRAINT FK_OrderDetails_Orders FOREIGN KEY (OrderID) 
        REFERENCES Orders(OrderID) ON DELETE CASCADE,
    CONSTRAINT FK_OrderDetails_Products FOREIGN KEY (ProductID) 
        REFERENCES Products(ProductID) ON DELETE CASCADE
);

-- 7. Таблиця "Відгуки"
-- Зв'язок "Один-до-багатьох" (Один користувач залишає багато відгуків, один товар має багато відгуків)
CREATE TABLE Reviews (
    ReviewID INT IDENTITY(1,1) PRIMARY KEY,
    UserID INT,
    ProductID INT,
    Rating INT CHECK (Rating BETWEEN 1 AND 5),
    Comment NVARCHAR(MAX),
    ReviewDate DATETIME DEFAULT GETDATE(),
    CONSTRAINT FK_Reviews_Users FOREIGN KEY (UserID) 
        REFERENCES Users(UserID),
    CONSTRAINT FK_Reviews_Products FOREIGN KEY (ProductID) 
        REFERENCES Products(ProductID) ON DELETE CASCADE
);
GO