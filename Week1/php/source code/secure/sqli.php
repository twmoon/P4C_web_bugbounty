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
    <title>Secure SQL Injection Page</title>
</head>
<body>
    <h1>SQL Injection Test Page</h1>
    <p>Update: No funky letters or symbols is allowed into query</p>
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

        // SQL Injection 방지: Prepared Statement 사용(prepare -> bind에서 문자열 결합)
        $query = "SELECT * FROM users WHERE username = ? AND password = ?";
        $stmt = mysqli_prepare($conn, $query);
        mysqli_stmt_bind_param($stmt, "ss", $username, $password);
        mysqli_stmt_execute($stmt);
        $result = mysqli_stmt_get_result($stmt);

        if ($result) {
            if (mysqli_num_rows($result) > 0) {
                echo "<h2>Login Successful</h2>";
                while ($row = mysqli_fetch_array($result)) {
                    echo "ID: " . $row['id'] . " - Username: " . htmlspecialchars($row['username']) . " - Password: " . htmlspecialchars($row['password']) . "<br>"; //리턴값에 의한 xss 방지
                }
            } else {
                echo "<p>Login failed: Invalid username or password.</p>";
            }
        } else {
            echo "<p>Query failed.</p>"; // 에러 메시지 노출 최소화
        }

        mysqli_stmt_close($stmt);
    }

    mysqli_close($conn);
    ?>
</body>
</html>