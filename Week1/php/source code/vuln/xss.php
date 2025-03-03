<?php
    setrawcookie("xss", "flag{xss_is_fun}", time()+3600,"/");
?>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>Vulnerable XSS Page</title>
</head>
<body>
    <h1>XSS Test Page</h1>
    <form method="GET" action="">
        <label for="input"> Input text:</label>
        <input type="text" id="input" name="input" required><br><br>
        <input type="submit" value="Submit">
    </form>

    <?php
        if (isset($_GET['input'])) {
            // 적절한 필터링 없이 입력값을 그대로 받아서 XSS 취약점 발생
            $user_input = $_GET['input'];
            echo "<p>You entered: " . $user_input . "</p>";
        }
    ?>
</body>
</html>