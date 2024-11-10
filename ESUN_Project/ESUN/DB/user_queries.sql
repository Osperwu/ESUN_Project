saveUser=INSERT INTO User (phone_number, user_name, email, password, cover_image, biography) VALUES (?, ?, ?, ?, ?, ?)
findByPhoneNumber=SELECT * FROM User WHERE phone_number = ?
findAllUser=SELECT * FROM User