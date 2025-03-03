<?php
$downloads_dir = './downloads/';
if (!file_exists($downloads_dir)) {
    mkdir($downloads_dir, 0777, true); // 폴더 생성
}
$fake_flag_file = $downloads_dir . 'download.flag';
if (!file_exists($fake_flag_file)) {
    file_put_contents($fake_flag_file, "Try harder.. kkk"); // 가짜 플래그
}

$real_flag_dir = '../';
$real_flag_file = $real_flag_dir . 'download.flag';
if (!file_exists($real_flag_file)) {
    file_put_contents($real_flag_file, "flag{download_is_fun}"); // 진짜 플래그
}

if (isset($_GET['file'])) {
    $file = $_GET['file'];
    // Path Traversal 방지: 허용된 디렉토리 내 파일만 접근
    $base_dir = './downloads/';
    $file_path = realpath($base_dir . $file);

    // 경로가 base_dir로 시작하는지 확인
    if ($file_path && strpos($file_path, $base_dir) === 0 && file_exists($file_path)) {
        $file_name = basename($file_path);
        header('Content-Type: text/plain');
        header('Content-Disposition: attachment; filename="' . $file_name . '"');
        readfile($file_path);
        exit;
    } else {
        $error = "File not found or access denied.";
    }
}
?>

<!DOCTYPE html>
<html>
<head>
    <title>Secure File Download Page</title>
</head>
<body>
    <h1>File Download Test Page</h1>
    <p>Download the file "download.flag"</p>
    <p>Hint: It is located in the upper directory</p>
    <p>Update: Traveling though path is not allowed</p>
    <form method="GET" action="">
        <label for="file">File:</label>
        <input type="text" id="file" name="file" required><br><br>
        <input type="submit" value="Download">
    </form>

    <?php
    if (isset($error)) {
        echo "<p>$error</p>";
    }
    ?>
</body>
</html>