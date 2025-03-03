<?php
$flag_dir = "../upload/uploads/private/";
if (!file_exists($flag_dir)) {
    mkdir($flag_dir, 0700, true);
}

file_put_contents($flag_dir . "upload.flag", "flag{upload_is_fun}");
chmod($flag_dir . "upload.flag", 0600);

if (isset($_FILES['file'])) {
    $file = $_FILES['file'];
    $upload_dir = '/var/www/html/vuln/uploads/';
    $file_name = basename($file['name']); // 경로 조작 방지
    $file_path = $upload_dir . $file_name;

    // 확장자 화이트리스트로 제한
    $allowed_extensions = ['txt', 'pdf', 'jpg', 'png'];
    $file_ext = strtolower(pathinfo($file_name, PATHINFO_EXTENSION));
    $is_allowed = in_array($file_ext, $allowed_extensions);

    if (!file_exists($upload_dir)) {
        mkdir($upload_dir, 0777, true);
    }

    // 업로드 보안: 확장자 검증 추가
    if ($is_allowed && move_uploaded_file($file['tmp_name'], $file_path)) {
        $message = "File uploaded successfully: <a href='/vuln/uploads/" . htmlspecialchars($file_name) . "'>" . htmlspecialchars($file_name) . "</a>";
    } else {
        $error = "Failed to upload file or invalid file type.";
    }
}
?>

<!DOCTYPE html>
<html>
<head>
    <title>Secure File Upload Page</title>
</head>
<body>
    <h1>File Upload Test Page</h1>
    <p>Read the file "upload.flag"</p>
    <p>Hint: It is located in the private directory that cannot be accessed directly via web</p>
    <p>Update: Only txt, pdf, jpg, png file format is allowed</p>
    <form method="POST" enctype="multipart/form-data" action="">
        <label for="file">Upload a file:</label>
        <input type="file" id="file" name="file" required><br><br>
        <input type="submit" value="Upload">
    </form>

    <?php
    if (isset($message)) {
        echo "<p>$message</p>";
    }
    if (isset($error)) {
        echo "<p>$error</p>";
    }
    ?>
</body>
</html>