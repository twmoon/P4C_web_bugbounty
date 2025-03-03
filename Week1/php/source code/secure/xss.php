<?php
setrawcookie("xss", "flag{xss_is_fun}", time() + 3600, "/");
?>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>Secure XSS Page</title>
</head>
<body>
    <h1>XSS Test Page</h1>
    <p>Update: No funky letters or symbols is allowed into query</p>
    <form method="GET" action="">
        <label for="input">Input text:</label>
        <input type="text" id="input" name="input" required><br><br>
        <input type="submit" value="Submit">
    </form>

    <?php
    if (isset($_GET['input'])) {
        // XSS 방지: 사용자 입력을 HTML 이스케이프 처리
        $user_input = htmlspecialchars($_GET['input'], ENT_QUOTES, 'UTF-8');
        echo "<p>You entered: " . $user_input . "</p>";
    }
    ?>
</body>
</html>