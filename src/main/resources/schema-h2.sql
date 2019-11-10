CREATE table DropicUsers ( userId INT(10) UNSIGNED PRIMARY KEY AUTO_INCREMENT, username VARCHAR(30) );

CREATE table DropicMedia ( mediaId INT(10) UNSIGNED PRIMARY KEY AUTO_INCREMENT, name VARCHAR(30), message VARCHAR(255), mediaPath VARCHAR(255), droppedDate DATETIME, pickUpDeadline DATETIME );

CREATE table DropicMediaLocation ( mediaLocationId INT(10) UNSIGNED PRIMARY KEY AUTO_INCREMENT, dropperId INT(10), mediaId INT(10), longitude DOUBLE(13,10), latitude DOUBLE(12,10));

CREATE table DropicMediaPickedUp ( pickUpId INT(10) UNSIGNED PRIMARY KEY AUTO_INCREMENT, userId INT(10), mediaId INT(10), pickedUpDate DATETIME);

CREATE table DropicMediaRecipients ( id INT(10) UNSIGNED PRIMARY KEY AUTO_INCREMENT, locationDropId INT(10), recipientUserId INT(10), quantity INT(3));

INSERT INTO DropicUsers values ("jordan");