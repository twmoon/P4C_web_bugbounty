<?php
    $flag_dir = "./upload/uploads/private/";
    if (!file_exists($flag_dir)) {
        mkdir($flag_dir,0700, true);
    }

    file_put_contents($flag_dir ."upload.flag","flag{upload_is_fun}");
    chmod($flag_dir ."upload.flag",0600);
    
    if (isset($_FILES['file'])) {
        $file = $_FILES['file'];
        $upload_dir = './uploads/'; 
        $file_name = $file['name'];
        $file_path = $upload_dir . $file_name;

        if (!file_exists($upload_dir)) {
            mkdir($upload_dir, 0777, true);
        }
        // 적절한 필터링 없이 입력값을 그대로 받아서 XSS 취약점 발생
        if (move_uploaded_file($file['tmp_name'], $file_path)) {
            $message = "File uploaded successfully: <a href='/vuln/uploads/$file_name'>$file_name</a>";
        } else {
            $error = "Failed to upload file.";
        }
    }
?>

<!DOCTYPE html>
<html>
<head>
    <title>Vulnerable File Upload Page</title>
</head>
<body>
    <h1>File Upload Test Page</h1>
    <p>Read the file "upload.flag"</p>
    <p>Hint: It is located in the private directory that cannot be accessed directly via web</p>
    <form method="POST" enctype="multipart/form-data" action="">
        <label for="file">Upload a file:</label>
        <input type="file" id="file" name="file" required><br><br>
        <input type="submit" value="Upload">
    </form>

    <?php
    // 결과 메시지 출력
    if (isset($message)) {
        echo "<p>$message</p>";
    }
    if (isset($error)) {
        echo "<p>$error</p>";
    }
    ?>
</body>
</html>