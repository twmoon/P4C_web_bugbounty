<?php
    $conn = mysqli_connect("db", "root", "1234");
    if (!$conn) {
        die("Connection failed: " . mysqli_connect_error());
    }

    mysqli_query($conn, "CREATE DATABASE IF NOT EXISTS vuln_db");
    mysqli_select_db($conn, "vuln_db");

    mysqli_query($conn, "CREATE TABLE IF NOT EXISTS users (
        id INT AUTO_INCREMENT PRIMARY KEY,
        username VARCHAR(50) UNIQUE,
        password VARCHAR(50)
    )");

    mysqli_query($conn, "INSERT IGNORE INTO users (username, password) VALUES ('admin', 'flag{sqli_is_fun}')");
    mysqli_query($conn, "INSERT IGNORE INTO users (username, password) VALUES ('user', 'user')");
    mysqli_query($conn, "INSERT IGNORE INTO users (username, password) VALUES ('guest', 'guest')");
?>

<!DOCTYPE html>
<html>
<head>
    <title>Vulnerable SQL Injection Page</title>
</head>
<body>
    <h1>SQL Injection Test Page</h1>
    <form method="GET" action="">
        <label for="username">Username:</label>
        <input type="text" id="username" name="username" required><br><br>
        <label for="password">Password:</label>
        <input type="password" id="password" name="password" required><br><br>
        <input type="submit" value="Login">
    </form>

    <?php
        if (isset($_GET['username']) && isset($_GET['password'])) {
            $username = $_GET['username'];
            $password = $_GET['password'];

            // 적절한 필터링 없이 입력값을 그대로 받아서 sqli 취약점 발생 가능
            $query = "SELECT * FROM users WHERE username = '$username' AND password = '$password'";
            $result = mysqli_query($conn, $query);

            if ($result) {
                if (mysqli_num_rows($result) > 0) {
                    echo "<h2>Login Successful</h2>";
                    while ($row = mysqli_fetch_array($result)) {
                        echo "ID: " . $row['id'] . " - Username: " . $row['username'] . " - Password: " . $row['password'] . "<br>";
                    }
                } else {
                    echo "<p>Login failed: Invalid username or password.</p>";
                }
            } else {
                //쿼리 에러 그대로 출력하므로서 사용자가 쿼리 구조 파악 가능
                echo "<p>Query failed: " . mysqli_error($conn) . "</p>";
            }
        }

        mysqli_close($conn);
    ?>
</body>
</html>