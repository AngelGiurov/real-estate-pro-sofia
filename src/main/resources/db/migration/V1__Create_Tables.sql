-- Create Agency Table
CREATE TABLE agency (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255),
    phone VARCHAR(50),
    budget DECIMAL(19,2) DEFAULT 0
);

-- Create Agent Table
CREATE TABLE agent (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    phone VARCHAR(50),
    earnings DECIMAL(19,2) DEFAULT 0,
    agency_id BIGINT,
    FOREIGN KEY (agency_id) REFERENCES agency(id)
);

-- Create Client Table
CREATE TABLE client (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    phone VARCHAR(50),
    type VARCHAR(31),
    agent_id BIGINT,
    budget_min DECIMAL(19,2),
    budget_max DECIMAL(19,2),
    FOREIGN KEY (agent_id) REFERENCES agent(id)
);

-- Create Property Table
CREATE TABLE property (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    description VARCHAR(500),
    address VARCHAR(255),
    price DECIMAL(19,2),
    area INT,
    type VARCHAR(31),
    apartment_type VARCHAR(50),
    house_type VARCHAR(50),
    construction_type VARCHAR(50),
    parking_spaces INT,
    yard_area DECIMAL(19,2),
    parcel_type VARCHAR(50),
    regulated BOOLEAN,
    seller_id BIGINT,
    agent_id BIGINT,
    FOREIGN KEY (seller_id) REFERENCES client(id),
    FOREIGN KEY (agent_id) REFERENCES agent(id)
);


-- Create Viewing Table
CREATE TABLE viewing (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    property_id BIGINT,
    buyer_id BIGINT,
    agent_id BIGINT,
    viewing_date TIMESTAMP,  -- Renamed column
    FOREIGN KEY (property_id) REFERENCES property(id),
    FOREIGN KEY (buyer_id) REFERENCES client(id),
    FOREIGN KEY (agent_id) REFERENCES agent(id)
    );

-- Create Purchase Table
CREATE TABLE purchase (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    purchase_date DATETIME NOT NULL,
    property_id BIGINT,
    agent_id BIGINT,
    buyer_id BIGINT,
    seller_id BIGINT,
    price DECIMAL(19, 2) NOT NULL,
    agency_commission DECIMAL(19, 2),
    agent_commission DECIMAL(19, 2),

    FOREIGN KEY (property_id) REFERENCES property(id),
    FOREIGN KEY (agent_id) REFERENCES agent(id),
    FOREIGN KEY (buyer_id) REFERENCES client(id),
    FOREIGN KEY (seller_id) REFERENCES client(id)
);
